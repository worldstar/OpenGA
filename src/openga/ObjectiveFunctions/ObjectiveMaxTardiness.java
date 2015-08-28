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

public class ObjectiveMaxTardiness implements ObjectiveFunctionScheduleI{
  public ObjectiveMaxTardiness() {
  }

  populationI originalPop;
  int indexOfObjective;
  int popSize;
  int dueDay[];              //due day of these jobs.
  int machineTime[];         //starting time to process these jobs.
  int processingTime[];      //processing time of the job
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

  public void setScheduleData(int dueDay[], int processingTime[], int numberOfMachine){
    this.dueDay = dueDay;
    this.processingTime = processingTime;
    this.numberOfMachine = numberOfMachine;
  }

  public void setScheduleData(int processingTime[], int numberOfMachine){

  }

  public void calcObjective(){
    for(int i = 0 ; i < popSize ; i ++ ){
      forScheduleMaxTardiness forScheduleTardiness1 = new forScheduleMaxTardiness();
      forScheduleTardiness1.setData(dueDay, processingTime, numberOfMachine);
      forScheduleTardiness1.setData(originalPop.getSingleChromosome(i));
      forScheduleTardiness1.calcObjectives();
      double originalObjValues[] = originalPop.getObjectiveValues(i);
      //write in the objective value to the variable.
      originalObjValues[indexOfObjective] = forScheduleTardiness1.getObjectiveValue();
      originalPop.setObjectiveValue(i, originalObjValues);
    }
  }

  public populationI getPopulation(){
    return originalPop;
  }

  public double[] getObjectiveValues(int index){
    return originalPop.getObjectiveValues(index);
  }

  public static void main(String[] args) {
    ObjectiveMaxTardiness objectiveTardiness1 = new ObjectiveMaxTardiness();
  }

}