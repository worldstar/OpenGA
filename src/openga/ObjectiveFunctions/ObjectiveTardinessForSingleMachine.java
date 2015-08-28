package openga.ObjectiveFunctions;
import openga.chromosomes.*;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: For the single machine problem.</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class ObjectiveTardinessForSingleMachine implements ObjectiveFunctionScheduleI{
  public ObjectiveTardinessForSingleMachine() {
  }
  populationI originalPop;
  int indexOfObjective;
  int popSize, length;
  int dueDay[];              //due day of these jobs.
  int machineTime[];         //starting time to process these jobs.
  int processingTime[];      //processing time of the job
  int numberOfMachine;       // the number of parallel machine

  public void setData(populationI population, int indexOfObjective){
    this.originalPop = population;
    this.indexOfObjective = indexOfObjective;
    popSize = population.getPopulationSize();
    length = population.getLengthOfChromosome();
  }

  public void setData(chromosome chromosome1, int indexOfObjective){
    System.out.println("ObjectiveTardinessForSingleMachine doesn't implement the setData(chromosome chromosome1, int indexOfObjective).\nThe program will exit.");
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
      double originalObjValues[] = originalPop.getObjectiveValues(i);
      //write in the objective value to the variable.
      originalObjValues[indexOfObjective] = calTardiness(originalPop.getSingleChromosome(i));
      originalPop.setObjectiveValue(i, originalObjValues);
    }
  }

  private double calTardiness(chromosome chromosome1) {
    double objVal = 0;
    double machineTime = 0;
    int finishTime[] = calcFinishTime(chromosome1.genes);

    for(int i = 0 ; i < length ; i ++ ){
      int jobIndex = chromosome1.getSolution()[i];
      if(finishTime[jobIndex] > dueDay[jobIndex]){
        objVal += (finishTime[jobIndex] - dueDay[jobIndex]);
      }
    }
    return objVal;
  }

  public int[] calcFinishTime(int _seq[]){
    int finishTime[] = new int[_seq.length];
    int currentTime = 0;

    for(int i = 0 ; i < _seq.length ; i ++ ){
      finishTime[_seq[i]] = currentTime + processingTime[_seq[i]];
      currentTime = finishTime[_seq[i]];
    }
    return finishTime;
  }

  public populationI getPopulation(){
    return originalPop;
  }

  public double[] getObjectiveValues(int index){
    return originalPop.getObjectiveValues(index);
  }

}