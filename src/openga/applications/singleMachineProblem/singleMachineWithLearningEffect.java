package openga.applications.singleMachineProblem;
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
import homework.schedule.singleMachine;

/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class singleMachineWithLearningEffect extends singleMachine {
  public singleMachineWithLearningEffect() {
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
  public int numberOfJob = 7;
  public int numberOfMachines = 1;

  public int
    DEFAULT_generations = 1000,//1000, 3000
    DEFAULT_PopSize     = 100,//100, 200
    DEFAULT_initPopSize = 100;

  public double
     DEFAULT_crossoverRate = 0.5,
     DEFAULT_mutationRate  = 0.3,
     elitism               =  0.2;     //the percentage of elite chromosomes

  double learningRate = -0.515;

 public void setData(int numberOfJobs, int dueDate[], int processingTime[], String fileName){
   this.numberOfJob = numberOfJobs;
   this.dueDay = dueDate;
   this.processingTime = processingTime;
   this.fileName = fileName;
 }

 public void setLearningRate(double learningRate){
   this.learningRate = learningRate;
 }

  public void initiateVars(){
    GaMain     = new singleThreadGA();//singleThreadGAwithSecondFront singleThreadGAwithMultipleCrossover
    Population = new population();
    Selection  = new binaryTournament();
    Crossover  = new twoPointCrossover2();
    Mutation   = new swapMutation();
    ObjectiveFunction = new ObjectiveFunctionScheduleI[numberOfObjs];
    ObjectiveFunction[0] = new ObjectiveEiTiPenaltyWithLearningEffect();//ObjectiveEarlinessTardinessPenalty ObjectiveEiTiPenaltyWithLearningEffect
    Fitness    = new GoldbergFitnessAssignment();
    objectiveMinimization = new boolean[numberOfObjs];
    objectiveMinimization[0] = true;
    encodeType = true;

    //set schedule data to the objectives
    ((alphaBetaI)ObjectiveFunction[0]).setLearningRate(learningRate);
    ObjectiveFunction[0].setScheduleData(dueDay, processingTime, numberOfMachines);

    //set the data to the GA main program.
    GaMain.setData(Population, Selection, Crossover, Mutation, ObjectiveFunction, Fitness, DEFAULT_generations, DEFAULT_initPopSize,DEFAULT_PopSize,
                   numberOfJob, DEFAULT_crossoverRate, DEFAULT_mutationRate, objectiveMinimization, numberOfObjs, encodeType, elitism);
    GaMain.setSecondaryCrossoverOperator(Crossover2, false);
    GaMain.setSecondaryMutationOperator(Mutation2, false);
  }

  public void start(){
    openga.util.timeClock timeClock1 = new openga.util.timeClock();
    timeClock1.start();
    GaMain.startGA();
    timeClock1.end();
    //System.out.println("\nThe final result");
    int bestInd = getBestSolnIndex(GaMain.getArchieve());
    String implementationResult = "";
    implementationResult += numberOfJob+"\t" + learningRate+"\t" + GaMain.getArchieve().getSingleChromosome(bestInd).getObjValue()[0]+"\t"+timeClock1.getExecutionTime()/1000.0+"\n";
    writeFile("singleMachineGAWithLearningEffect20060725", implementationResult);
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

  public static void main(String[] args) {
    singleMachineWithLearningEffect singleMachine1 = new singleMachineWithLearningEffect();
    System.out.println("singleMachineGAWithLearningEffect20060725");
    int counter = 0;
    int numberOfJobs = 7, numberOfMachine = 1;
    int dueDate[] = new int[numberOfJobs];
    int processingTime[] = new int[]{3, 4, 6, 9, 14, 18, 20} ;//{1, 3, 6, 8, 11, 15, 21}, {3, 4, 6, 9, 14, 18, 20}
    int repeatExperiments = 2;
    int jobSets[] = new int[]{20};//20, 30, 40, 50, 100, 200
    double learningRate[] = new double[]{-0.322};//-0.515, -0.322, -0.152
/*
     //for simple test.
     for(int i = 0 ; i < repeatExperiments ; i ++ ){
      System.out.println("Combinations: "+counter);
      singleMachine1.setData(numberOfJobs, dueDate, processingTime, "");
      singleMachine1.initiateVars();
      singleMachine1.start();
      counter ++;
    }
*/
    for(int i = 0 ; i < repeatExperiments ; i ++ ){
      for(int m = 0 ; m < jobSets.length ; m ++ ){//jobSets.length
        for(int k = 0 ; k < learningRate.length ; k ++ ){
          System.out.println("Combinations: "+counter);
          openga.applications.data.singleMachine readSingleMachineData1 = new openga.applications.data.singleMachine();
          numberOfJobs = jobSets[m];
          String fileName = readSingleMachineData1.getFileName(numberOfJobs, 0);
          //fileName = "bky"+numberOfJobs+"_1";
          System.out.print(fileName+"\t");
          readSingleMachineData1.setData("sks/"+fileName+".txt");
          readSingleMachineData1.getDataFromFile();
          dueDate = readSingleMachineData1.getDueDate();
          processingTime = readSingleMachineData1.getPtime();
          double alpha[] = readSingleMachineData1.getAlpha();
          double beta[] = readSingleMachineData1.getBeta();
          numberOfJobs = 7;
          singleMachine1.setData(numberOfJobs, dueDate, processingTime, fileName);
          singleMachine1.setLearningRate(learningRate[k]);
          singleMachine1.initiateVars();
          singleMachine1.start();
          counter ++;
        }
      }
    }

  }
}