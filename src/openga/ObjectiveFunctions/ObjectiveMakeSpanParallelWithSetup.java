package openga.ObjectiveFunctions;
import openga.chromosomes.*;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class ObjectiveMakeSpanParallelWithSetup implements ObjectiveFunctionSetupScheduleI{
  public ObjectiveMakeSpanParallelWithSetup() {
  }
  populationI originalPop;
  int indexOfObjective;
  int popSize;
  int machineTime[];         //starting time to process these jobs.
  int processingTime[][][];      //processing time of the job
  int numberOfMachine;       // the number of parallel machine


  public void setData(populationI population, int indexOfObjective){
    this.originalPop = population;
    this.indexOfObjective = indexOfObjective;
    popSize = population.getPopulationSize();
  }

  public void setData(chromosome chromosome1, int indexOfObjective){
    System.out.println("The setData(chromosome chromosome1) doesn't be implements.");
    System.out.println("The program is exit.");
    System.exit(0);
  }

  public void setScheduleData(int processingTime[], int numberOfMachine){
  }

  public void setScheduleData(int dueDay[], int processingTime[], int numberOfMachine){
  }

  public void setScheduleData(int[][][] processingTime, int numberOfMachine){
    this.processingTime = processingTime;
    this.numberOfMachine = numberOfMachine;
  }

  public void calcObjective(){
    /*
    originalPop.getSingleChromosome(0).setSolution(new int[]{3, 5, 2, 4, 0, 1});
    System.out.println(originalPop.getSingleChromosome(0).toString1());
    forScheduleMakespanParallelWithSetup forScheduleMakespan2 = new forScheduleMakespanParallelWithSetup();
    forScheduleMakespan2.setData(processingTime, numberOfMachine);
    forScheduleMakespan2.setData(originalPop.getSingleChromosome(0));
    forScheduleMakespan2.calcObjectives();
    System.out.println();
    System.exit(0);
    */
    for(int i = 0 ; i < popSize ; i ++ ){
      forScheduleMakespanParallelWithSetup forScheduleMakespan1 = new forScheduleMakespanParallelWithSetup();
      forScheduleMakespan1.setData(processingTime, numberOfMachine);
      forScheduleMakespan1.setData(originalPop.getSingleChromosome(i));
      forScheduleMakespan1.calcObjectives();
      double originalObjValues[] = originalPop.getObjectiveValues(i);
      //openga.util.printClass pri1 = new openga.util.printClass();
      //write in the objective value to the variable.
      originalObjValues[indexOfObjective] = forScheduleMakespan1.getObjectiveValue();
      originalPop.setObjectiveValue(i, originalObjValues);
    }
  }

  public populationI getPopulation(){
    return originalPop;
  }

  public double[] getObjectiveValues(int index){
    return originalPop.getObjectiveValues(index);
  }


}