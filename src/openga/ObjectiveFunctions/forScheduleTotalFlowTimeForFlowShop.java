package openga.ObjectiveFunctions;
import openga.chromosomes.*;
import openga.util.algorithm.*;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: It's the for the flowshop scheduling problem which calculates the total flow time.</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class forScheduleTotalFlowTimeForFlowShop extends forScheduleMaxTardiness{
  public forScheduleTotalFlowTimeForFlowShop() {
  }

  int processingTime[][];
  int totalFlowTime = 0;

  public void setData(int length, int processingTime[][], int numberOfMachine){
    machineTime = new int[numberOfMachine];
    this.length = length;
    this.processingTime = processingTime;
    this.numberOfMachine = numberOfMachine;
  }

  public void calcObjectives() {
    machineTime = new int[numberOfMachine];

    //assign each job to each machine depended on the current machine time.
    for(int i = 0 ; i < length ; i ++ ){
      int index = chromosome1.genes[i];
      int startTime = machineTime[0], endTime;
      for(int j = 0 ; j < numberOfMachine ; j ++ ){
        if(j == 0){
          //the starting time is the completion time of last job on first machine
          machineTime[j] += processingTime[index][j];
        }
        else{
          if(machineTime[j - 1] < machineTime[j]){//previous job on the machine j is not finished
            machineTime[j] = machineTime[j] + processingTime[index][j];
          }
          else{//the starting time is the completion time of last machine
            machineTime[j] = machineTime[j - 1] + processingTime[index][j];
          }
        }

        if(j == numberOfMachine - 1){//if the job is processed on the last machine.
          endTime = machineTime[j];
          objVal += (endTime);
        }
      }
    }//end the job assignment.
  }


}