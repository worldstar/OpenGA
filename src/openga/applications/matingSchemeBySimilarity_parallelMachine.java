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

public class matingSchemeBySimilarity_parallelMachine {
  public matingSchemeBySimilarity_parallelMachine() {
  }

  /***
   * Basic operators of GAs.
   */
  int numberOfObjs = 2;//it's a bi-objective program.
  populationI Population;
  SelectI Selection;
  CrossoverI Crossover;
  MutationI Mutation;
  ObjectiveFunctionScheduleI ObjectiveFunction[];
  FitnessI Fitness;
  MainI GaMain;

  boolean applySecCRX = false;
  boolean applySecMutation = false;
  /**
   * Parameters of the GA
   */
  int generations, length, initPopSize, fixPopSize;
  double crossoverRate, mutationRate;
  boolean[] objectiveMinimization; //true is minimum problem.
  boolean encodeType;  //binary of realize code

  /***
   * Scheduling data
   */
  int dueDay[], processingTime[];
  int numberOfJob = 50;
  int numberOfMachines = 18;

  public int
    DEFAULT_generations = 200,
    DEFAULT_PopSize     = 5000,
    DEFAULT_initPopSize = 8000,
    totalSolnsToExamine = 1000000;//to fix the total number of solutions to examine.

  public double
     DEFAULT_crossoverRate = 0.9,
     DEFAULT_mutationRate  = 0.1,
     elitism               = 0.2;     //the percentage of elite chromosomes

 public void setParameters(int PopSize, int totalSolnsToExamine,
                           double crossoverRate, double mutationRate, double elitism,
                           boolean applySecCRX, boolean applySecMutation){
   DEFAULT_PopSize = PopSize;
   this.totalSolnsToExamine = totalSolnsToExamine;
   this.DEFAULT_crossoverRate = crossoverRate;
   this.DEFAULT_mutationRate = mutationRate;
   this.elitism = elitism;
   DEFAULT_generations = totalSolnsToExamine/(PopSize);
   this.applySecCRX = applySecCRX;
   this.applySecMutation = applySecMutation;
 }

 public void setParallelMachineData(int numberOfJob, int numberOfMachines){
   this.numberOfJob = numberOfJob;
   this.numberOfMachines = numberOfMachines;
 }
 public void initiateVars(){
   //initiate scheduling data. We use the benchmark problem.
   openga.applications.data.parallelMachine data1 = new openga.applications.data.parallelMachine();
   dueDay = data1.getTestData3_DueDay(numberOfJob);
   processingTime = data1.getTestData3_processingTime(numberOfJob);

   GaMain     = new singleThreadGA();//only apply the single thread version of GA.
   Population = new population();
   Selection  = new similaritySelection();
   Crossover  = new twoPointCrossover2();
   Mutation   = new inverseMutation();
   ObjectiveFunction = new ObjectiveFunctionScheduleI[numberOfObjs];
   ObjectiveFunction[0] = new ObjectiveMakeSpan();//the first objective, makespan
   ObjectiveFunction[1] = new ObjectiveTardiness();//the second one.
   Fitness    = new GoldbergFitnessAssignment();
   objectiveMinimization = new boolean[numberOfObjs];
   objectiveMinimization[0] = true;
   objectiveMinimization[1] = true;
   encodeType = true;

   //set schedule data to the objectives
   ObjectiveFunction[0].setScheduleData(processingTime, numberOfMachines);
   ObjectiveFunction[1].setScheduleData(dueDay, processingTime, numberOfMachines);


   //set the data to the GA main program.
   GaMain.setData(Population, Selection, Crossover, Mutation, ObjectiveFunction, Fitness, DEFAULT_generations, DEFAULT_initPopSize,DEFAULT_PopSize,
                  numberOfJob, DEFAULT_crossoverRate, DEFAULT_mutationRate, objectiveMinimization, numberOfObjs, encodeType, elitism);
 }

 public void start(){
   GaMain.startGA();
   System.out.println("\nThe final result");
   //to output the implementation result.
   String implementResult = "";
   double refSet[][] = getReferenceSet();

   double objArray[][] = GaMain.getArchieveObjectiveValueArray();
   implementResult = calcSolutionQuality(refSet, objArray)+"\n";//
   objArray = null;
   System.out.println(implementResult);
   //writeFile("matingSchemeBySimilarity_parallelMachine", implementResult);

 }

 private double[][] getReferenceSet(){
   openga.applications.data.parallelMachine data1 = new openga.applications.data.parallelMachine();
   if(numberOfJob == 35){
     return data1.getParetoJob35M10();
   }
   else if(numberOfJob == 50){
     return data1.getParetoJob50M15();
   }
   else{//for job = 65
     return data1.getParetoJob65M18();
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
 public void writeFile(String fileName, String _result){
   fileWrite1 writeLotteryResult = new fileWrite1();
   writeLotteryResult.writeToFile(_result,fileName+".txt");
   Thread thread1 = new Thread(writeLotteryResult);
   thread1.run();
 }

  public static void main(String[] args) {
    matingSchemeBySimilarity_parallelMachine matingSchemeBySimilarity_parallelMachine1 = new matingSchemeBySimilarity_parallelMachine();
    matingSchemeBySimilarity_parallelMachine1.initiateVars();
    matingSchemeBySimilarity_parallelMachine1.start();
  }

}