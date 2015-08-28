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
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class singleMachine {
  public singleMachine() {
  }

  /***
   * Basic variables of GAs.
   */
  public int numberOfObjs = 1;//it's a bi-objective program.
  public populationI Population;
  public SelectI Selection;
  public CrossoverI Crossover, Crossover2;
  public MutationI Mutation, Mutation2;
  public ObjectiveFunctionScheduleI ObjectiveFunction[];
  public FitnessI Fitness;
  public MainI GaMain;
  public int totalSolnsToExamine = 100000;

  /**
   * Parameters of the GA
   */
  public int generations, length, initPopSize, fixPopSize;
  public double crossoverRate, mutationRate;
  public boolean[] objectiveMinimization; //true is minimum problem.
  public boolean encodeType;  //binary of realize code
  public String fileName = "";

  /***
   * Scheduling parameter
   */
  public int dueDay[], processingTime[];
  public int numberOfJob = 6;
  public int numberOfMachines = 1;

  /**
   * For DP
   */
  public int DP_Iter = 1000;

  //Results
  public double bestObjectiveValues[];
  public populationI solutions;

  public int
    DEFAULT_generations = 1000,//1000, 3000
    DEFAULT_PopSize     = 100,//100, 200
    DEFAULT_initPopSize = 100;

  public double
     DEFAULT_crossoverRate = 0.8,
     DEFAULT_mutationRate  = 0.3,
     elitism               = 0.2;     //the percentage of elite chromosomes

  public printClass printClass1 = new printClass();

  public void setData(int numberOfJobs, int dueDate[], int processingTime[], String fileName){
    this.numberOfJob = numberOfJobs;
    this.dueDay = dueDate;
    this.processingTime = processingTime;
    this.fileName = fileName;
  }

  public void setParameters(int DEFAULT_PopSize, double DEFAULT_crossoverRate, double DEFAULT_mutationRate, int totalSolnsToExamine){
    this.DEFAULT_PopSize = DEFAULT_PopSize;
    this.DEFAULT_crossoverRate = DEFAULT_crossoverRate;
    this.DEFAULT_mutationRate = DEFAULT_mutationRate;
    this.totalSolnsToExamine = totalSolnsToExamine;
    DEFAULT_generations = totalSolnsToExamine/DEFAULT_PopSize;
  }

  public void setParameters(int DEFAULT_PopSize, double DEFAULT_crossoverRate, double DEFAULT_mutationRate, int DP_Iter, int totalSolnsToExamine){
    this.DEFAULT_PopSize = DEFAULT_PopSize;
    this.DEFAULT_crossoverRate = DEFAULT_crossoverRate;
    this.DEFAULT_mutationRate = DEFAULT_mutationRate;
    this.DP_Iter = DP_Iter;
    this.totalSolnsToExamine = totalSolnsToExamine;
    DEFAULT_generations = totalSolnsToExamine/DEFAULT_PopSize;
  }

  public void initiateVars(){
    GaMain     = new singleThreadGA();//singleThreadGAwithSecondFront singleThreadGAwithMultipleCrossover
    Population = new population();
    Selection  = new binaryTournament();
    Crossover  = new twoPointCrossover2();
    Crossover2 = new PMX();
    Mutation   = new shiftMutation();//shiftMutation swapMutation swapMutation2Threads
    Mutation2  = new inverseMutation();
    ObjectiveFunction = new ObjectiveFunctionScheduleI[numberOfObjs];
    ObjectiveFunction[0] = new ObjectiveEarlinessTardinessPenalty();//the first objective, tardiness, ObjectiveTardiness ObjectiveEarlinessTardinessPenalty
    //ObjectiveFunction[1] = new ObjectiveTardinessForFlowShop();//the second one.
    Fitness    = new GoldbergFitnessAssignment();
    objectiveMinimization = new boolean[numberOfObjs];
    objectiveMinimization[0] = true;
    //objectiveMinimization[1] = true;
    encodeType = true;

    //set schedule data to the objectives
    ObjectiveFunction[0].setScheduleData(dueDay, processingTime, numberOfMachines);

    //set the data to the GA main program.
    GaMain.setData(Population, Selection, Crossover, Mutation, ObjectiveFunction, Fitness, DEFAULT_generations, DEFAULT_initPopSize,DEFAULT_PopSize,
                   numberOfJob, DEFAULT_crossoverRate, DEFAULT_mutationRate, objectiveMinimization, numberOfObjs, encodeType, elitism);
    GaMain.setSecondaryCrossoverOperator(Crossover2, true);
    GaMain.setSecondaryMutationOperator(Mutation2, true);
  }

  public void startMain(){
    openga.util.timeClock timeClock1 = new openga.util.timeClock();
    timeClock1.start();
    GaMain.startGA();
    timeClock1.end();
    //System.out.println("\nThe final result");
    int bestInd = getBestSolnIndex(GaMain.getArchieve());
    String implementationResult = "";
    implementationResult += fileName+"\t"+numberOfJob+"\t"+"\t" + DEFAULT_PopSize+"\t"+DEFAULT_crossoverRate+"\t"+ DEFAULT_mutationRate+"\t" + GaMain.getArchieve().getSingleChromosome(bestInd).getObjValue()[0]+"\t"+timeClock1.getExecutionTime()/1000.0+"\n";
    writeFile("singleMachineGA_SKS_20070604", implementationResult);
    System.out.println(implementationResult);
  }

  /**
   * For single objective problem
   * @param arch1
   * @return
   */
  public int getBestSolnIndex(populationI arch1){
    int index = 0;
    double bestobj = Double.MAX_VALUE;
    for(int k = 0 ; k < GaMain.getArchieve().getPopulationSize() ; k ++ ){
      if(bestobj > GaMain.getArchieve().getObjectiveValues(k)[0]){
        bestobj = GaMain.getArchieve().getObjectiveValues(k)[0];
        index = k;
      }
    }
    return index;
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
    singleMachine singleMachine1 = new singleMachine();
    System.out.println("singleMachineGA_SKS_20070506");
    //openga.applications.data.singleMachine singleMachineData = new openga.applications.data.singleMachine();
    int jobSets[] = new int[]{20, 30, 40, 50, 60, 90};//20, 30, 40, 50, 60, 90, 100, 200//20, 40, 60, 80
    int counter = 0;
    int repeatExperiments = 30;

    int popSize[] = new int[]{100};//50, 100, 155, 210 [100]
    double crossoverRate[] = new double[]{0.9},//0.6, 0.9 {0.9}
           mutationRate [] = new double[]{0.5},//0.1, 0.5 {0.5}
           elitism = 0.2;

    //Sourd Instance
    for(int i = 0 ; i < repeatExperiments ; i ++ ){
      for(int m = 0 ; m < jobSets.length ; m ++ ){//jobSets.length
        for(int k = 0 ; k < 49 ; k ++ ){
          if(jobSets[m] <= 50 ||  (jobSets[m] > 50 && k < 9)){
            System.out.println("Combinations: "+counter);
            openga.applications.data.singleMachine readSingleMachineData1 = new openga.applications.data.singleMachine();
            int numberOfJobs = jobSets[m];
            String fileName = readSingleMachineData1.getFileName(numberOfJobs, k);
            //fileName = "bky"+numberOfJobs+"_1";
            System.out.print(fileName+"\t");
            readSingleMachineData1.setData("sks/"+fileName+".txt");
            readSingleMachineData1.getDataFromFile();
            int dueDate[] = readSingleMachineData1.getDueDate();
            int processingTime[] = readSingleMachineData1.getPtime();

            singleMachine1.setData(numberOfJobs, dueDate, processingTime, fileName);
            singleMachine1.initiateVars();
            singleMachine1.startMain();
            counter ++;
          }
        }
      }
    }//end for
    System.exit(0);

    /*
    //BKY Instance
    int incrementSteps = 5;
    for(int i = 0 ; i < repeatExperiments ; i ++ ){
      for(int j = 0 ; j < jobSets.length ; j ++ ){//jobSets.length
        for(int k = 0 ; k < popSize.length ; k ++ ){
          for (int m = 0; m < crossoverRate.length; m++) {
            for (int n = 0; n < mutationRate.length; n++) {
              for(int p = 296 ; p < 297 ; ){
                openga.applications.data.readSingleMachine readSingleMachineData1 = new openga.applications.data.readSingleMachine();
                int numberOfJobs = jobSets[j];
                String fileName = readSingleMachineData1.getFileName(numberOfJobs, p);
                readSingleMachineData1.setData("instances/SingleMachineBKS/"+fileName+".txt");
                if(readSingleMachineData1.testReadData()){//to test whether the file exist
                  System.out.print("Combinations: "+counter+"\t");
                  System.out.print(fileName+"\t");
                  readSingleMachineData1.getDataFromFile();
                  int dueDate[] = readSingleMachineData1.getDueDate();
                  int processingTime[] = readSingleMachineData1.getPtime();

                  singleMachine1.setData(numberOfJobs, dueDate, processingTime, fileName);
                  singleMachine1.setParameters(popSize[k], crossoverRate[m], mutationRate[n], 100000);
                  singleMachine1.initiateVars();
                  singleMachine1.startMain();
                  counter ++;
                }
                p += incrementSteps;
              }
            }
          }
        }
      }
    }//end for
    */
  }
}
