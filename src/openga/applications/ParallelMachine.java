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

/**
 * <p>Title: The OpenGA project</p>
 * <p>Description: The project is to build general framework of Genetic algorithm and problem independent.</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class ParallelMachine {
  public ParallelMachine() {
  }

  /***
   * Basic variables of GAs.
   */
  int numberOfObjs = 2;//it's a bi-objective program.
  populationI Population;
  SelectI Selection;
  CrossoverI Crossover;
  MutationI Mutation;
  ObjectiveFunctionScheduleI ObjectiveFunction[];
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
   * Scheduling data
   */
  int dueDay[], processingTime[];
  int numberOfJob = 65;
  int numberOfMachines = 18;

  //Results
  double bestObjectiveValues[];
  populationI solutions;

  public final static int
    DEFAULT_generations = 1000,
    DEFAULT_PopSize     = 100,
    DEFAULT_initPopSize = 400;

  public static double
     DEFAULT_crossoverRate = 0.9,
     DEFAULT_mutationRate  = 0.5,
     elitism               =  0.2;     //the percentage of elite chromosomes

  printClass printClass1 = new printClass();

  public void initiateVars(){
    GaMain     = new singleThreadGA();//only apply the single thread version of GA.
    Population = new population();
    Selection  = new binaryTournament();
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

    //initiate scheduling data
    java.util.Random r = new java.util.Random(12345);
    dueDay = new int[numberOfJob];
    processingTime = new int[numberOfJob];
    for(int i = 0 ; i < numberOfJob ; i ++ ){
      dueDay[i] = (int)(r.nextDouble()*50);
      processingTime[i] = 1+(int)(r.nextDouble()*15);
    }
    //set schedule data to the objectives
    ObjectiveFunction[0].setScheduleData(processingTime, numberOfMachines);
    ObjectiveFunction[1].setScheduleData(dueDay, processingTime, numberOfMachines);
    printClass1.printMatrix("processingTime",processingTime);
    printClass1.printMatrix("dueDay",dueDay);

    //set the data to the GA main program.
    GaMain.setData(Population, Selection, Crossover, Mutation, ObjectiveFunction, Fitness, DEFAULT_generations, DEFAULT_initPopSize,DEFAULT_PopSize,
                   numberOfJob, DEFAULT_crossoverRate, DEFAULT_mutationRate, objectiveMinimization, numberOfObjs, encodeType, elitism);
  }

  public void start(){
    GaMain.startGA();
    System.out.println("\nThe final result");
    for(int k = 0 ; k < DEFAULT_PopSize ; k ++ ){
      openga.util.printClass pri1 = new openga.util.printClass();
      pri1.printMatrix(""+k, GaMain.getPopulation().getSingleChromosome(k).genes, GaMain.getPopulation().getObjectiveValues(k), GaMain.getPopulation().getFitness(k));
    }
  }

  public static void main(String[] args) {
    ParallelMachine flowshopParallelMachine1 = new ParallelMachine();
    flowshopParallelMachine1.initiateVars();
    flowshopParallelMachine1.start();
  }

}