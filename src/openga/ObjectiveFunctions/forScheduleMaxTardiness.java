package openga.ObjectiveFunctions;
import openga.chromosomes.*;
import openga.util.algorithm.*;
/**
 * <p>Title: The OpenGA project</p>
 * <p>Description: The project is to build general framework of Genetic algorithm and problem independent.</p>
 * <P>For the parallel machine scheduling problem.</P>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class forScheduleMaxTardiness extends forScheduleTardiness{
  public forScheduleMaxTardiness() {
  }

  public void calcObjectives() {
    //to use getMin to find out the machine which has least processing time
    getMin getMin1 = new getMin();
    objVal = 0;
    double maxTardiness = 0;
    machineTime = new int[numberOfMachine];

    //start to assign others job.
    for(int i = 0 ; i < length ; i ++ ){
      //Johnson's rule, find the machine whose current total processing time is minimum.
      int minProcessingTimeMachine = getMin1.getDataIndex(machineTime);
      //assign the job i to the machine minProcessingTimeMachine
      int index = chromosome1.genes[i];
      machineTime[minProcessingTimeMachine] = machineTime[minProcessingTimeMachine] + processingTime[index];
      if(machineTime[minProcessingTimeMachine]  > dueDay[index]){
        maxTardiness = (machineTime[minProcessingTimeMachine] - dueDay[index]);
      }
      if(maxTardiness > objVal){
        objVal = maxTardiness;
      }

    }
  }


  public static void main(String[] args) {
    forScheduleMaxTardiness forScheduleMaxTardiness1 = new forScheduleMaxTardiness();
  }

}