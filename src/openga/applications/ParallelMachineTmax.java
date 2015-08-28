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
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class ParallelMachineTmax extends ParallelMachine {
  public ParallelMachineTmax() {
  }
  /***
   * Basic variables of GAs.
   */
  int numberOfObjs = 1;//it's a single objective program.

  /***
   * Scheduling data
   */
  int dueDay[], processingTime[];
  int numberOfJob = 65;
  int numberOfMachines = 18;

  public void initiateVars(){
    GaMain     = new singleThreadGA();//only apply the single thread version of GA.
    Population = new population();
    Selection  = new binaryTournament();
    Crossover  = new twoPointCrossover2();
    Mutation   = new inverseMutation();
    ObjectiveFunction = new ObjectiveFunctionScheduleI[numberOfObjs];
    ObjectiveFunction[0] = new ObjectiveMaxTardiness();//the first objective, makespan
    Fitness    = new GoldbergFitnessAssignment();
    objectiveMinimization = new boolean[numberOfObjs];
    objectiveMinimization[0] = true;
    encodeType = true;
    elitism = 0.1;

    //initiate scheduling data, we get the data from a program.
    openga.applications.data.parallelMachine parallelMachineData = new openga.applications.data.parallelMachine();
    processingTime = parallelMachineData.getTestData3_processingTime(numberOfJob);
    dueDay = parallelMachineData.getTestData3_DueDay(numberOfJob);
    numberOfJob = processingTime.length;
    parallelMachineData = null;

    //set schedule data to the objectives
    ObjectiveFunction[0].setScheduleData(dueDay, processingTime, numberOfMachines);

    //set the data to the GA main program.
    GaMain.setData(Population, Selection, Crossover, Mutation, ObjectiveFunction, Fitness, DEFAULT_generations, DEFAULT_initPopSize,DEFAULT_PopSize,
                   numberOfJob, DEFAULT_crossoverRate, DEFAULT_mutationRate, objectiveMinimization, numberOfObjs, encodeType, elitism);
  }

  public void start(){
    GaMain.startGA();
    System.out.println("\nThe final result");
    String implementResult = GaMain.getArchieve().getSingleChromosome(0).toString1()+"\n"
        +GaMain.getArchieve().getSingleChromosome(0).getObjValue()[0]+"\t"+"\n";
    System.out.println(implementResult);
  }

  public static void main(String[] args) {
    ParallelMachineTmax parallelMachineTmax1 = new ParallelMachineTmax();
    parallelMachineTmax1.initiateVars();
    parallelMachineTmax1.start();
  }

}