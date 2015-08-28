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

public class ObjectiveTotalFlowTimeForFlowShop implements ObjectiveFunctionScheduleI{
  public ObjectiveTotalFlowTimeForFlowShop() {
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
    System.out.println("It doesn't implement this method in program ObjectiveTotalFlowTimeForFlowShop");
    System.exit(0);
  }

  public void setScheduleData(int dueDay[], int processingTime[], int numberOfMachine){
    this.dueDay = dueDay;
    this.processingTime = processingTime;
    this.numberOfMachine = numberOfMachine;
  }

  public void setScheduleData(int processingTime[], int numberOfMachine){
    this.processingTime = processingTime;
    this.numberOfMachine = numberOfMachine;
  }

  public void calcObjective(){
    for(int i = 0 ; i < popSize ; i ++ ){
      forScheduleTotalFlowTimeForFlowShop forScheduleTotalFlowTimeForFlowShop1 = new forScheduleTotalFlowTimeForFlowShop();
      forScheduleTotalFlowTimeForFlowShop1.setData(dueDay, processingTime, numberOfMachine);
      forScheduleTotalFlowTimeForFlowShop1.setData(originalPop.getSingleChromosome(i));
      forScheduleTotalFlowTimeForFlowShop1.calcObjectives();
      double originalObjValues[] = originalPop.getObjectiveValues(i);
      //write in the objective value to the variable.
      originalObjValues[indexOfObjective] = forScheduleTotalFlowTimeForFlowShop1.getObjectiveValue();
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