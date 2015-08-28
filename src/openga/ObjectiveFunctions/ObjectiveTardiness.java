package openga.ObjectiveFunctions;
import openga.chromosomes.*;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: For parallel machine.</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class ObjectiveTardiness implements ObjectiveFunctionScheduleI{
  public ObjectiveTardiness() {
  }
  populationI originalPop;
  int indexOfObjective;
  int popSize;
  int dueDay[];              //due day of these jobs.
  int machineTime[];         //starting time to process these jobs.
  int processingTime[];      //processing time of the job
  int numberOfMachine;       // the number of parallel machine

  public void setData(chromosome chromosome1, int indexOfObjective){
    System.out.println("ObjectiveTardiness doesn't implement setData(chromosome chromosome1, int indexOfObjective).");
    System.out.println("Program exits.");
    System.exit(0);
  }
  
  public void setData(populationI population, int indexOfObjective){
    this.originalPop = population;
    this.indexOfObjective = indexOfObjective;
    popSize = population.getPopulationSize();
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
      forScheduleTardiness forScheduleTardiness1 = new forScheduleTardiness();
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

}