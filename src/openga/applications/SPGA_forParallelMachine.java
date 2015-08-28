package openga.applications;
import openga.chromosomes.*;
import openga.operator.selection.*;
import openga.operator.crossover.*;
import openga.operator.mutation.*;
import openga.ObjectiveFunctions.*;
import openga.MainProgram.*;
import openga.ObjectiveFunctions.*;
import openga.Fitness.*;
import openga.util.printClass;
import openga.util.fileWrite1;

/**
 * <p>Title: The OpenGA project</p>
 * <p>Description: The project is to build general framework of Genetic algorithm and problem independent.</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class SPGA_forParallelMachine {
  public SPGA_forParallelMachine() {
  }
  /***
   * Basic operators of GAs.
   */
  int numberOfObjs = 2;//it's a bi-objective program.
  populationI Population[];
  SelectI Selection[];
  CrossoverI Crossover[], Crossover2;
  MutationI Mutation[], Mutation2;
  ObjectiveFunctionScheduleI ObjectiveFunction[][];
  FitnessI Fitness[];
  MainWeightScalarizationI GaMain[];

  /**
   * Parameters of the GA
   */
  int generations, length, initPopSize, fixPopSize;
  double crossoverRate, mutationRate;
  boolean[] objectiveMinimization; //true is minimum problem.
  boolean encodeType;  //binary of realize code

  /**
   * The SPGA parameters
   */
  int numberOfSubPopulations = 40;
  double weights[];
  public boolean applySecCRX = false;
  public boolean applySecMutation = false;

  /***
   * Scheduling data
   */
  int dueDay[], processingTime[];
  int numberOfJob = 65;
  int numberOfMachines = 18;

  //Results
  double bestObjectiveValues[][];
  populationI solutions[];
  double implementationTime = 0;

  public int
    DEFAULT_generations = 200,
    DEFAULT_PopSize     = 5000,
    DEFAULT_initPopSize = 6000,
    firstIteration      = DEFAULT_generations,
    totalSolnsToExamine = 1000000;//to fix the total number of solutions to examine.

  public double
     DEFAULT_crossoverRate = 0.9,
     DEFAULT_mutationRate  = 0.1,
     elitism               =  0.2;     //the percentage of elite chromosomes

  printClass printClass1 = new printClass();

  public void setParameters(int numberOfSubPopulations, int PopSize, int totalSolnsToExamine,
                            double crossoverRate, double mutationRate, double elitism,
                            boolean applySecCRX, boolean applySecMutation){
    this.numberOfSubPopulations = numberOfSubPopulations;
    DEFAULT_PopSize = PopSize;
    this.totalSolnsToExamine = totalSolnsToExamine;
    this.DEFAULT_crossoverRate = crossoverRate;
    this.DEFAULT_mutationRate = mutationRate;
    this.elitism = elitism;
    DEFAULT_generations = totalSolnsToExamine/(PopSize*numberOfSubPopulations);
    this.applySecCRX = applySecCRX;
    this.applySecMutation = applySecMutation;
  }

  public void setParallelMachineData(int numberOfJob, int numberOfMachines){
    this.numberOfJob = numberOfJob;
    this.numberOfMachines = numberOfMachines;
  }

  public void initiateVars(){
    //initiate scheduling data
    openga.applications.data.parallelMachine data1 = new openga.applications.data.parallelMachine();
    dueDay = data1.getTestData3_DueDay(numberOfJob);
    processingTime = data1.getTestData3_processingTime(numberOfJob);

    objectiveMinimization = new boolean[numberOfObjs];
    objectiveMinimization[0] = true;
    objectiveMinimization[1] = true;
    encodeType = true;

    //GA objects
    GaMain     = new MainWeightScalarizationI[numberOfSubPopulations];
    Population = new populationI[numberOfSubPopulations];
    Selection  = new SelectI[numberOfSubPopulations];
    Crossover  = new CrossoverI[numberOfSubPopulations];
    Mutation   = new MutationI[numberOfSubPopulations];
    ObjectiveFunction  = new ObjectiveFunctionScheduleI[numberOfSubPopulations][numberOfObjs];
    Fitness   = new FitnessI[numberOfSubPopulations];

    for(int i = 0 ; i < numberOfSubPopulations ; i ++ ){
      GaMain[i]     = new fixWeightScalarization();//singleThreadGA
      Population[i] = new population();
      Selection[i]  = new binaryTournament();
      Crossover[i]  = new twoPointCrossover2();
      Mutation[i]   = new inverseMutation();
      ObjectiveFunction[i] = new ObjectiveFunctionScheduleI[numberOfObjs];
      ObjectiveFunction[i][0] = new ObjectiveMakeSpan();//the first objective, makespan
      ObjectiveFunction[i][1] = new ObjectiveTardiness();//the second one.
      Fitness[i]    = new FitnessByScalarizedM_objectives();

      //set schedule data to the objectives
      ObjectiveFunction[i][0].setScheduleData(processingTime, numberOfMachines);
      ObjectiveFunction[i][1].setScheduleData(dueDay, processingTime, numberOfMachines);
      //printClass1.printMatrix("processingTime",processingTime);
      //printClass1.printMatrix("dueDay",dueDay);
      //set the data to the GA main program.
      GaMain[i].setData(Population[i], Selection[i], Crossover[i], Mutation[i],
                     ObjectiveFunction[i], Fitness[i], firstIteration, DEFAULT_initPopSize/numberOfSubPopulations,DEFAULT_PopSize/numberOfSubPopulations,
                     numberOfJob, DEFAULT_crossoverRate, DEFAULT_mutationRate, objectiveMinimization,
                     numberOfObjs, encodeType, elitism);
      //set weight data
      GaMain[i].setWeight(calcWeightsForEachSubPop(i));
    }
  }

  public void start(){
    openga.util.timeClock timeClock1 = new openga.util.timeClock();
    timeClock1.start();
    String implementResult = "";

    for(int i = 0 ; i < numberOfSubPopulations ; i ++ ){
      //System.out.println("Sub-Pop "+i);
      GaMain[i].startGA();

      //to collect result and write it to a  file
      implementResult = "Sub-Pop "+i+"\n";
      //System.out.println(DEFAULT_PopSize/numberOfSubPopulations);
      for(int k = 0 ; k < GaMain[i].getArchieve().getPopulationSize() ; k ++ ){
        implementResult += GaMain[i].getArchieve().getSingleChromosome(k).toString1()+"\t";//sequnence
        for(int j = 0 ; j < numberOfObjs ; j ++ ){//for each objectives
          implementResult += GaMain[i].getArchieve().getObjectiveValues(k)[j]+"\t";
        }
        implementResult += "\n";
      }
      //writeFile("SPGA_forParallelMachine", implementResult);
    }
    timeClock1.end();
    implementationTime = timeClock1.getExecutionTime()/1000.0;
    implementResult += numberOfJob+"\t"+implementationTime+"\n";
    writeFile("SPGA_forParallelMachine", implementResult);
    System.out.print(implementResult);
  }

  /***********              Support Methods                  ***************/
  public void calcWeights(){
    weights = new double[numberOfObjs];
    double tempWeights[] = new double[numberOfObjs];
    openga.util.algorithm.getSum Sum1 = new openga.util.algorithm.getSum();
    double sum = 0;

    for(int i = 0 ; i < numberOfObjs ; i ++ ){
      tempWeights[i] = Math.random();
    }
    Sum1.setData(tempWeights);
    sum = Sum1.getSumResult();
    //start to assign weight
    for(int i = 0 ; i < numberOfObjs ; i ++ ){
      weights[i] = tempWeights[i]/sum;
    }
  }

  public final double[] calcWeightsForEachSubPop(int indexOfSubPop){
    double weights[] = new double[numberOfObjs];
    indexOfSubPop ++;
    weights[0] = ((indexOfSubPop+1)/(double)(numberOfSubPopulations+1));
    weights[1] = 1 - weights[0];
    //System.out.print(weights[0]+"\t"+weights[1]+"\n");
    return weights;
  }

  public final double[] calcUniformWeightsForEachSubPop(int indexOfSubPop){
    numberOfObjs = 3;
    double weights[] = new double[numberOfObjs];
    int sigma = 22;
    double sum = 0;
    numberOfSubPopulations = 31;
    for(int i = 0 ; i < numberOfObjs ; i ++ ){
      weights[i] = (indexOfSubPop+1)*Math.pow(sigma, i) % numberOfSubPopulations + 1;
      sum += weights[i];
    }

    for(int i = 0 ; i < numberOfObjs ; i ++ ){
      weights[i] = weights[i]/sum;
    }
    indexOfSubPop ++;
    System.out.print(weights[0]+"\t"+weights[1]+"\t"+weights[2]+"\n");
    return weights;
  }

  /**
   * Write the data into text file.
   */
  public void writeFile(String fileName, String _result){
    fileWrite1 writeLotteryResult = new fileWrite1();
    writeLotteryResult.writeToFile(_result,fileName+".txt");
    Thread thread1 = new Thread(writeLotteryResult);
    thread1.run();
  }

  public static void main(String[] args) {
    /*
    //Basic implementation of SPGA.
    SPGA_forParallelMachine SPGA_forParallelMachine1 = new SPGA_forParallelMachine();
    SPGA_forParallelMachine1.initiateVars();
    SPGA_forParallelMachine1.start();
    System.exit(0);
    */

    int numberOfSubPopulations[] = new int[]{40};//10, 20, 30, 40
    int popSize[] = new int[]{210};//100, 155, 210
    int numberOfJob[] = new int []{35, 50, 65};//35, 50, 65
    int numberOfMachines[] = new int[]{10, 15, 18};//10, 15, 18
    int totalSolnsToExamine[] = new int[]{1000000};//1000000, 1500000, 2000000
    boolean applySecCRX[] = new boolean[]{false};//false, true
    boolean applySecMutation[] = new boolean[]{true};

    double crossoverRate = 0.9,
           mutationRate = 0.1,
           elitism = 0.2;
    int repeatExperiments = 30;

    SPGA_forParallelMachine writeTitle = new SPGA_forParallelMachine();
    //to form a text file and write the title in it.
    String implementResult = "numberOfJob\t numberOfSubPopulations\t popSize\t applySecCRX\t applySecMutation\t D1r\n";
    writeTitle.writeFile("SPGA_forParallelMachine", implementResult);
    writeTitle = null;
    int counter = 0;

    for(int i = 0 ; i < repeatExperiments ; i ++ ){
      for(int j = 0 ; j < numberOfSubPopulations.length ; j ++ ){
        for(int k = 0 ; k < popSize.length ; k ++ ){
          for(int L = 0 ; L < applySecCRX.length ; L ++ ){
            for(int m = 0 ; m < applySecMutation.length ; m ++ ){
              for(int r = 0 ; r < numberOfJob.length ; r ++ ){
                //System.out.println(numberOfJob[r]+"\t"+numberOfSubPopulations[j]+"\t"+ popSize[k]);
                //System.out.println("combinations: "+counter++);
                SPGA_forParallelMachine SPGA_forParallelMachine1 = new SPGA_forParallelMachine();
                SPGA_forParallelMachine1.setParallelMachineData(numberOfJob[r], numberOfMachines[r]);
                SPGA_forParallelMachine1.setParameters(numberOfSubPopulations[j], popSize[k],
                    totalSolnsToExamine[0], crossoverRate, mutationRate, elitism, applySecCRX[L], applySecMutation[m]);
                SPGA_forParallelMachine1.initiateVars();
                SPGA_forParallelMachine1.start();
                SPGA_forParallelMachine1 = null;
                System.gc();
                //System.exit(0);
              }//end r
            }//end m
          }// end L
        }// end k
      }// end j
    }// end i

    System.exit(0);

  }

}