package openga.applications.flowshopProblem;
import openga.chromosomes.*;
import openga.operator.selection.*;
import openga.operator.crossover.*;
import openga.operator.mutation.*;
import openga.operator.clone.*;
import openga.ObjectiveFunctions.*;
import openga.MainProgram.*;
import openga.ObjectiveFunctions.*;
import openga.Fitness.*;
import openga.util.printClass;
import openga.util.fileWrite1;

import openga.applications.SPGA2_forFlowShop;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class SPGA2_AC2forFlowShop{
  public SPGA2_AC2forFlowShop() {
  }
  /***
   * Basic operators of GAs.
   */
  int numberOfObjs = 2;//it's a bi-objective program.
  populationI Population[];
  SelectI Selection;
  CrossoverI Crossover, Crossover2;
  MutationI Mutation, Mutation2;
  ObjectiveFunctionFlowShopScheduleI ObjectiveFunction[];
  FitnessI Fitness;
  multiObjsProbabilityMatrixI GaMain[];
  boolean[] objectiveMinimization; //true is minimum problem.
  boolean encodeType;  //binary of realize code

  /**
   * The SPGA' parameters
   */
  public int numberOfSubPopulations = 10;
  public boolean applySecCRX = false;
  public boolean applySecMutation = false;
  cloneI clone1;
  boolean applyClone = false;
  int tournamentSize = 2;
  int cloneStrategy = 2;
  double timeToClone = 0;
  double weights[][];

  /***
   * Scheduling data
   */
  int dueDay[], processingTime[][];
  int numberOfJob = 20;
  int numberOfMachines = 20;

  public int
    DEFAULT_generations = 1000,
    DEFAULT_PopSize     = 100,
    DEFAULT_initPopSize = 100,
    totalSolnsToExamine = 100000;//to fix the total number of solutions to examine.

  public double
     DEFAULT_crossoverRate = 0.6,
     DEFAULT_mutationRate  = 0.5,
     elitism               = 0.2;     //the percentage of elite chromosomes

 /***
  * AC2 parameters
  */
  int startingGeneration = 50;
  int interval = 20;
  int strategy = 1;

  public void setFlowShopData(int numberOfJob, int numberOfMachines){
    this.numberOfJob = numberOfJob;
    this.numberOfMachines = numberOfMachines;
  }

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

  public void setCloneActive(boolean applyClone){
    this.applyClone = applyClone;
  }

  public void setProbabilityMatrixData(int startingGeneration, int interval){
    this.startingGeneration = startingGeneration;
    this.interval = interval;
  }

  public void setSequenceStrategy(int strategy){
    this.strategy = strategy;
  }

  public void initiateVars(){
    //initiate scheduling data, we get the data from a program.
    processingTime = getProcessingTime();
    dueDay = getDueDay();
    weights = new double[numberOfSubPopulations][numberOfObjs];
    objectiveMinimization = new boolean[numberOfObjs];
    objectiveMinimization[0] = true;
    objectiveMinimization[1] = true;
    encodeType = true;

    //GA objects
    GaMain     = new multiObjsProbabilityMatrixI[numberOfSubPopulations];
    Population = new populationI[numberOfSubPopulations];
    Selection  = new binaryTournament();
    Crossover  = new twoPointCrossover2();//twoPointCrossover2 twoPointCrossover2withAdpative
    Mutation   = new swapMutation();//swapMutation swapMutationWithAdaptive
    ObjectiveFunction  = new ObjectiveFunctionFlowShopScheduleI[numberOfObjs];
    clone1     = new solutionVectorCloneWithMutation();//swap mutation
    Fitness    = new FitnessByScalarizedM_objectives();
    DEFAULT_generations  = totalSolnsToExamine/(DEFAULT_PopSize*numberOfSubPopulations);
    ObjectiveFunction[0] = new ObjectiveMakeSpanForFlowShop();//the first objective, makespan
    ObjectiveFunction[1] = new ObjectiveTardinessForFlowShop();//the second one.
    ObjectiveFunction[0].setScheduleData(processingTime, numberOfMachines);
    ObjectiveFunction[1].setScheduleData(dueDay, processingTime, numberOfMachines);

    for(int i = 0 ; i < numberOfSubPopulations ; i ++ ){
      GaMain[i]     = new SPGAwithSharedParetoSetAC2();
      Population[i] = new population();

      //set the data to the GA main program.
      GaMain[i].setData(Population[i], Selection, Crossover, Mutation,
                     ObjectiveFunction, Fitness, DEFAULT_generations, DEFAULT_PopSize, DEFAULT_PopSize,
                     numberOfJob, DEFAULT_crossoverRate, DEFAULT_mutationRate, objectiveMinimization,
                     numberOfObjs, encodeType, elitism);
      //set weight data
      weights[i] = calcWeightsForEachSubPop(i);//calcWeightsForEachSubPop calcUniformWeightsForEachSubPop
      GaMain[i].setWeight(weights[i]);
      //set secondary crossover and mutation operator.
      //GaMain[i].setSecondaryCrossoverOperator(Crossover2[i], applySecCRX);
      GaMain[i].setSecondaryCrossoverOperator(Crossover2, false);
      //GaMain[i].setSecondaryMutationOperator(Mutation2[i], applySecMutation);
      GaMain[i].setSecondaryMutationOperator(Mutation2, false);
      GaMain[i].setCloneOperatpr(clone1, applyClone);

      //for AC2
      GaMain[i].setSequenceStrategy(strategy);
    }
  }

  public void start(){
    openga.util.timeClock timeClock1 = new openga.util.timeClock();
    timeClock1.start();

    //we control the number of iteration of GA here.
    for(int m = 0 ; m < DEFAULT_generations ; m ++ ){
      //System.out.println("Generation: "+m);

      for(int i = 0 ; i < numberOfSubPopulations ; i ++ ){
        //System.out.println("numberOfSubPopulations "+i);
        if(m == 0){//initial each population and its objective values.
          GaMain[i].InitialRun();
          if(i == 0){
            GaMain[i].initFirstArchieve();
          }
        }

        if(m < startingGeneration || m % interval != 0){
          GaMain[i].startGA();
        }
        else{//probabilityMatrixI
          GaMain[i].ProbabilityMatrix();
        }
      }
      //System.out.println("GaMain[i].getAarchieve.getPopulationSize() "+GaMain[0].getArchieve().getPopulationSize()+"\t");
    }

    timeClock1.end();
    double implementationTime = timeClock1.getExecutionTime();
    outputResults(implementationTime);
  }

  /*
  public void start(){//original SPGA2.
    openga.util.timeClock timeClock1 = new openga.util.timeClock();
    timeClock1.start();
    for(int i = 0 ; i < numberOfSubPopulations ; i ++ ){
      for(int m = 0 ; m < DEFAULT_generations ; m ++ ){
        if(m == 0){//initial each population and its objective values.
          GaMain[i].InitialRun();
          if(i == 0){
            GaMain[i].initFirstArchieve();
          }
          else{
            GaMain[i].setPopulation(GaMain[i-1].getPopulation());
          }
        }

        if(m < startingGeneration || m % interval != 0){
          GaMain[i].startGA();
        }
        else{//probabilityMatrixI
          GaMain[i].ProbabilityMatrix();
        }
      }
    }

    //we control the number of iteration of GA here.
    for(int m = 0 ; m < DEFAULT_generations ; m ++ ){
      //System.out.println("Generation: "+m);

      for(int i = 0 ; i < numberOfSubPopulations ; i ++ ){
        //System.out.println("numberOfSubPopulations "+i);
        if(m == 0){//initial each population and its objective values.
          GaMain[i].InitialRun();
          if(i == 0){
            GaMain[i].initFirstArchieve();
          }
        }

        if(m < startingGeneration || m % interval != 0){
          GaMain[i].startGA();
        }
        else{//probabilityMatrixI
          GaMain[i].ProbabilityMatrix();
        }
      }
      //System.out.println("GaMain[i].getAarchieve.getPopulationSize() "+GaMain[0].getArchieve().getPopulationSize()+"\t");
    }
    timeClock1.end();
    double implementationTime = timeClock1.getExecutionTime();
    outputResults(implementationTime);
  }
  */

 private boolean checkGroup(int index, int[] group){
   for(int i = 0 ; i < group.length ; i ++ ){
     if(index == group[i]){
       return false;
     }
   }
   return true;
 }

  public void outputResults(double implementationTime){
    //to output the implementation result.
    String implementResult = "";
    double refSet[][] = getReferenceSet();

    double objArray[][] = GaMain[0].getArchieveObjectiveValueArray();
    double D1r = calcSolutionQuality(refSet, objArray);
    implementResult = numberOfJob+"\t"+startingGeneration+"\t"+interval+"\t"+D1r+"\t"+implementationTime/1000+"\n";//
    objArray = null;
    writeFile("SPGA2_AC2forFlowShop1121_DOE1", implementResult);
    System.out.println(implementResult);

    implementResult = "";
    for(int k = 0 ; k < GaMain[0].getArchieve().getPopulationSize() ; k ++ ){
      //implementResult += GaMain[0].getArchieve().getSingleChromosome(k).toString1()+"\t";//sequnence
      for(int j = 0 ; j < numberOfObjs ; j ++ ){//for each objectives
        implementResult += GaMain[0].getArchieve().getObjectiveValues(k)[j]+"\t";
      }
      implementResult += "\n";
    }
    //writeFile("SPGA2_AC2forFlowShop1121_archive", implementResult);

    for(int i = 0 ; i < numberOfSubPopulations ; i ++ ){
      GaMain[i].destroidArchive();
      GaMain[i] = null;
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

  /**
   * To evaluate the solution quality by some metric. It uses the D1r here.
   * @param refSet The current known Pareto set
   * @param obtainedPareto After the implementation of your algorithm.
   * @return The D1r value.
   */
  public double calcSolutionQuality(double refSet[][], double obtainedPareto[][]){
    openga.util.algorithm.D1r d1r1 = new openga.util.algorithm.D1r();
    d1r1.setData(refSet, obtainedPareto);
    //d1r1.calcD1r();
    d1r1.calcD1rWithoutNormalization();
    return d1r1.getD1r();
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

  public final double[][] getReferenceSet(){
    openga.applications.data.flowShop data1 = new openga.applications.data.flowShop();
    return data1.getReferenceSet(numberOfJob);
  }

  public final int[] getDueDay(){
    openga.applications.data.flowShop data1 = new openga.applications.data.flowShop();
    if(numberOfJob == 20){
      return data1.getTestData2_DueDay();
    }
    else if(numberOfJob == 40){
      return data1.getTestData3_DueDay();
    }
    else if(numberOfJob == 60){
      return data1.getTestData4_DueDay();
    }
    else{
      return data1.getTestData5_DueDay();
    }
  }

  public final int[][] getProcessingTime(){
    openga.applications.data.flowShop data1 = new openga.applications.data.flowShop();
    if(numberOfJob == 20){
      return data1.getTestData2_processingTime();
    }
    else if(numberOfJob == 40){
      return data1.getTestData3_processingTime();
    }
    else if(numberOfJob == 60){
      return data1.getTestData4_processingTime();
    }
    else{
      return data1.getTestData5_processingTime();
    }
  }

  public static void main(String[] args) {
    System.out.println("SPGA2_Ac2forFlowShop 20061121");

    int numberOfSubPopulations[] = new int[]{10};//5, 10
    int popSize[] = new int[]{100};
    int numberOfJob[] = new int []{20};//20, 40, 60, 80
    int numberOfMachines = 20;
    int totalSolnsToExamine[] = new int[]{100000};
    boolean applySecCRX[] = new boolean[]{false, true};//false, true
    boolean applySecMutation[] = new boolean[]{false, true};
    boolean applyClone[] = new boolean[]{false, true};

    double crossoverRate[] = new double[]{0.6, 0.9},
           mutationRate[] = new double[]{0.2, 0.5},
           elitism[] = new double[]{0.1, 0.2};
    int repeatExperiments = 10;
    int counter = 0;
    int startingGeneration[] = new int[]{50};//25, 50
    int interval[] = new int[]{20};//5, 15
    int strategy[] = new int[]{1};

    for(int i = 0 ; i < repeatExperiments ; i ++ ){
      for(int j = 0 ; j < numberOfJob.length ; j ++ ){
        for(int k = 0 ; k < startingGeneration.length ; k ++ ){
          for(int m = 0 ; m < interval.length ; m ++ ){
            System.out.println("combinations: "+counter++);
            SPGA2_AC2forFlowShop SPGA2_forFlowShop1 = new SPGA2_AC2forFlowShop();
            SPGA2_forFlowShop1.setFlowShopData(numberOfJob[j], numberOfMachines);

            SPGA2_forFlowShop1.setParameters(numberOfSubPopulations[0], popSize[0],
                totalSolnsToExamine[0], crossoverRate[0], mutationRate[1],
                elitism[1], applySecCRX[0], applySecMutation[0]);
            SPGA2_forFlowShop1.initiateVars();
            SPGA2_forFlowShop1.setProbabilityMatrixData(startingGeneration[k], interval[m]);
            SPGA2_forFlowShop1.start();
            SPGA2_forFlowShop1 = null;
            System.gc();
          }
        }
      }
    }

    System.exit(0);
  }

}