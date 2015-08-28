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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
//import javax.util.concurrent.ManagedExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.ArrayList;

/**
 * <p>Title: The OpenGA project</p>
 * <p>Description: The project is to build general framework of Genetic algorithm and problem independent.</p>
 * <P>For the flowshop problem.</P>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class flowshop<T> implements Callable {
  public flowshop() {
  }
  /***
   * Basic variables of GAs.
   */
  int numberOfObjs = 2;//it's a bi-objective program.
  populationI Population;
  SelectI Selection;
  CrossoverI Crossover;
  CrossoverI Crossover2;
  MutationI Mutation, Mutation2;
  ObjectiveFunctionFlowShopScheduleI ObjectiveFunction[];
  FitnessI Fitness;
  MainI GaMain;

  /**
   * Parameters of the GA
   */
  int generations, length, initPopSize, fixPopSize;
  double crossoverRate, mutationRate;
  boolean[] objectiveMinimization; //true is minimum problem.
  boolean encodeType;  //binary of realize code

  /***
   * Scheduling parameter
   */
  int dueDay[], processingTime[][];
  int numberOfJob = 40;
  int numberOfMachines = 20;

  /**
   * Adaptive parameters
   */
  double k1, k3;
  double targetDiversity;

  //Results
  double bestObjectiveValues[];
  populationI solutions;

  public int
    DEFAULT_generations = 1000,
    DEFAULT_PopSize     = 100,
    DEFAULT_initPopSize = 100,
    totalSolnsToExamine = 100000;//to fix the total number of solutions to examine.

  public double
     DEFAULT_crossoverRate = 0.9,
     DEFAULT_mutationRate  = 0.6,
     elitism               =  0.2;     //the percentage of elite chromosomes

  printClass printClass1 = new printClass();

  public void setFlowShopData(int numberOfJob, int numberOfMachines){
    this.numberOfJob = numberOfJob;
    this.numberOfMachines = numberOfMachines;
  }

  public void setParameters(double k1, double targetDiversity){
    this.k1 = k1;
    k3 = k1;
    this.targetDiversity = targetDiversity;
  }

  public void initiateVars(){
    GaMain     = new singleThreadGA();//singleThreadGA singleThreadGAwithSecondFront singleThreadGAwithMultipleCrossover adaptiveGA
    Population = new population();
    Selection  = new binaryTournament();//binaryTournament similaritySelection(our method) similaritySelection2(by Ishibuchi)
    Crossover  = new twoPointCrossover2();//twoPointCrossover2 oneByOneChromosomeCrossover twoPointCrossover2withAdpative twoPointCrossover2withAdpativeThreshold
    Crossover2 = new PMX();
    Mutation   = new shiftMutation();//shiftMutation shiftMutationWithAdaptive shiftMutationWithAdaptiveThreshold
    Mutation2  = new inverseMutation();
    ObjectiveFunction = new ObjectiveFunctionFlowShopScheduleI[numberOfObjs];
    ObjectiveFunction[0] = new ObjectiveMakeSpanForFlowShop();//the first objective, makespan
    ObjectiveFunction[1] = new ObjectiveTardinessForFlowShop();//the second one.
    Fitness    = new GoldbergFitnessAssignment();
    objectiveMinimization = new boolean[numberOfObjs];
    objectiveMinimization[0] = true;
    objectiveMinimization[1] = true;
    encodeType = true;

    //initiate scheduling data, we get the data from a program.
    processingTime = getProcessingTime();
    dueDay = getDueDay();

    //to set the parameters of adpative strategy
    //Crossover.setAdaptiveParameters(k1, k3, targetDiversity);

    //set schedule data to the objectives
    ObjectiveFunction[0].setScheduleData(processingTime, numberOfMachines);
    ObjectiveFunction[1].setScheduleData(dueDay, processingTime, numberOfMachines);

    DEFAULT_generations = totalSolnsToExamine/(DEFAULT_PopSize);

    //set the data to the GA main program.
    GaMain.setData(Population, Selection, Crossover, Mutation, ObjectiveFunction, Fitness, DEFAULT_generations, DEFAULT_initPopSize,DEFAULT_PopSize,
                   numberOfJob, DEFAULT_crossoverRate, DEFAULT_mutationRate, objectiveMinimization, numberOfObjs, encodeType, elitism);
    GaMain.setSecondaryCrossoverOperator(Crossover2, true);
    GaMain.setSecondaryMutationOperator(Mutation2, true);
  }

  public T call(){
    start();
    return null;
  }

  public void start(){
    openga.util.timeClock timeClock1 = new openga.util.timeClock();
    timeClock1.start();
    GaMain.startGA();
    timeClock1.end();
    //to output the implementation result.
    String implementResult = "";
    double refSet[][] = getReferenceSet();

    double objArray[][] = GaMain.getArchieveObjectiveValueArray();
    implementResult = "SGA\t1"+"\t"+targetDiversity+"\t"+numberOfJob+"\t"+calcSolutionQuality(refSet, objArray)+"\t"+timeClock1.getExecutionTime()/1000.0+"\n";//
    objArray = null;
    System.out.print(implementResult);
    writeFile("SGA", implementResult);    
  }

  private int[] getDueDay(){
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

  private int[][] getProcessingTime(){
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

  private double[][] getReferenceSet(){
    openga.applications.data.flowShop data1 = new openga.applications.data.flowShop();
    if(numberOfJob == 20){
      return data1.getReferenceSet2();
    }
    else if(numberOfJob == 40){
      return data1.getReferenceSet3();
    }
    else if(numberOfJob == 60){
      return data1.getReferenceSet4();
    }
    else{
      return data1.getReferenceSet5();
    }
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
    d1r1.calcD1r();
    return d1r1.getD1r();
  }

  /**
   * Write the data into text file.
   */
  void writeFile(String fileName, String _result){
    fileWrite1 writeLotteryResult = new fileWrite1();
    writeLotteryResult.writeToFile(_result,fileName+".txt");
    Thread thread1 = new Thread(writeLotteryResult);
    thread1.run();
  }

  void sendToPool(flowshop<Callable<T>> flowshopArray[]){
    ArrayList<Callable<T>> retrieverTasks = new ArrayList<Callable<T>>();
    ExecutorService application = Executors.newCachedThreadPool();
    for(int i = 0 ; i < flowshopArray.length ; i++){
      retrieverTasks.add((Callable<T>)flowshopArray[i]);
    }
            try{
              application.invokeAll(retrieverTasks);
            }
            catch(Exception e){

            }
    application.shutdown();
  }

  public static void main(String[] args) {
    System.out.println("in flow shop SGA");
    flowshop flowshop1 = new flowshop();
    int repeatExperiments = 30;
    int numberOfJob[] = new int []{20, 40, 60, 80};//
    int numberOfMachines = 20;
    int counter = 0;
    double kValues[] = new double[]{0.8};
    double targetValue[] = new double[]{0.5};

    int cores = 2;
    
        

    for(int j = 0 ; j < numberOfJob.length ; j ++ ){
      for(int k = 0 ; k < kValues.length ; k ++ ){
        for(int m = 0 ; m < targetValue.length ; m ++ ){
          for(int i = 0 ; i < repeatExperiments/cores ; i ++ ){
            ExecutorService application = Executors.newCachedThreadPool();
            //ArrayList<Callable> retrieverTasks = new ArrayList<Callable>();
            //ArrayList<Callable<T>> retrieverTasks = new ArrayList<Callable<T>>();
            flowshop flowshopArray[] = new flowshop[cores];
            for(int x = 0 ; x < cores ; x ++){
              System.out.println(counter++);
              flowshopArray[x] = new flowshop();
              flowshopArray[x].setFlowShopData(numberOfJob[j], numberOfMachines);
              flowshopArray[x].setParameters(kValues[k], targetValue[m]);
              flowshopArray[x].initiateVars();
              //retrieverTasks.add((Callable<T>)flowshopArray[x]);
            }
            flowshopArray[0].sendToPool(flowshopArray);
            //application.shutdown();
          }//end for
        }
      }
    }
    System.exit(0);

    
    for(int i = 0 ; i < repeatExperiments ; i ++ ){
      for(int j = 0 ; j < numberOfJob.length ; j ++ ){
        for(int k = 0 ; k < kValues.length ; k ++ ){
          for(int m = 0 ; m < targetValue.length ; m ++ ){
            System.out.println(counter++);
            flowshop1.setFlowShopData(numberOfJob[j], numberOfMachines);
            flowshop1.setParameters(kValues[k], targetValue[m]);
            flowshop1.initiateVars();
            flowshop1.start();
          }
        }
      }
    }//end for
  }
}