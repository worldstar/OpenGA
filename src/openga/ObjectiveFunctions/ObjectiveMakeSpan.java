package openga.ObjectiveFunctions;
import openga.chromosomes.*;
/**
 * <p>Title: The OpenGA project</p>
 * <p>Description: The project is to build general framework of Genetic algorithm and problem independent.</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class ObjectiveMakeSpan implements ObjectiveFunctionScheduleI{
  public ObjectiveMakeSpan() {
  }

  populationI originalPop;
  int indexOfObjective;
  int popSize;
  int machineTime[];         //starting time to process these jobs.
  int processingTime[];      //processing time of the job
  int numberOfMachine;       // the number of parallel machine

  forScheduleMakespan forScheduleMakespan1 = new forScheduleMakespan();


  public void setData(populationI population, int indexOfObjective){
    this.originalPop = population;
    this.indexOfObjective = indexOfObjective;
    popSize = population.getPopulationSize();
  }

  public void setData(chromosome chromosome1, int indexOfObjective){
    forScheduleMakespan1.setData(processingTime, numberOfMachine);
    forScheduleMakespan1.setData(chromosome1);
    forScheduleMakespan1.calcObjectives();
    double originalObjValues[] = chromosome1.getObjValue();
    //openga.util.printClass pri1 = new openga.util.printClass();
    //write in the objective value to the variable.
    originalObjValues[indexOfObjective] = forScheduleMakespan1.getObjectiveValue();
    chromosome1.setObjValue(originalObjValues);
  }

  public void setScheduleData(int processingTime[], int numberOfMachine){
    this.processingTime = processingTime;
    this.numberOfMachine = numberOfMachine;
  }

  public void setScheduleData(int dueDay[], int processingTime[], int numberOfMachine){

  }

  public void calcObjective(){
    for(int i = 0 ; i < popSize ; i ++ ){
      setData(originalPop.getSingleChromosome(i), indexOfObjective);
    }
  }

  public populationI getPopulation(){
    return originalPop;
  }

  public double[] getObjectiveValues(int index){
    return originalPop.getObjectiveValues(index);
  }
}