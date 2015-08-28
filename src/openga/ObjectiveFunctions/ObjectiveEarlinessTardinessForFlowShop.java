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

public class ObjectiveEarlinessTardinessForFlowShop extends ObjectiveTardinessForFlowShop {
  public ObjectiveEarlinessTardinessForFlowShop() {
  }
  public void setScheduleData(int dueDay[], int processingTime[][], int numberOfMachine){
    this.dueDay = dueDay;
    this.processingTime = processingTime;
    this.numberOfMachine = numberOfMachine;
  }

  public void calcObjective(){
    for(int i = 0 ; i < popSize ; i ++ ){
      double originalObjValues[] = originalPop.getObjectiveValues(i);
      //write in the objective value to the variable.
      originalObjValues[indexOfObjective] = calcObjectives(originalPop.getSingleChromosome(i));
      originalPop.setObjectiveValue(i, originalObjValues);
    }
  }

  public double calcObjectives(chromosome chromosome1) {
    machineTime = new int[numberOfMachine];
    double objVal = 0;
    //assign each job to each machine depended on the current machine time.
    for (int i = 0; i < chromosome1.getLength() ; i++) {
      int index = chromosome1.genes[i];
      for (int j = 0; j < numberOfMachine; j++) {
        //the starting time is the completion time of last job on first machine
        if (j == 0) {
          machineTime[j] += processingTime[index][j];
        }
        else {
          if (machineTime[j - 1] < machineTime[j]) { //previous job on the machine j is not finished
            machineTime[j] = machineTime[j] + processingTime[index][j];
          }
          else { //the starting time is the completion time of last machine
            machineTime[j] = machineTime[j - 1] + processingTime[index][j];
          }
        }
        //check whether the completion time of the job i is late or not.
        if(machineTime[j]  > dueDay[index]){
          objVal += (machineTime[j] - dueDay[index]);
        }
        else{
          objVal += (dueDay[index] - machineTime[j]);
        }

      }
    }
    return objVal;
  }
}