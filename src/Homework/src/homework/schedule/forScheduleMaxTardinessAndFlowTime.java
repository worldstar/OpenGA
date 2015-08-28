package src.homework.schedule;

//import ga4.util.algorithm.*;
/**
 * <p>Title: The OpenGA project</p>
 * <p>Description: The project is to build general framework of Genetic algorithm and problem independent.</p>
 * <P>For the parallel machine scheduling problem.</P>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class forScheduleMaxTardinessAndFlowTime{
  public forScheduleMaxTardinessAndFlowTime() {
  }
  int length;                //chromosome length.
  int objVal_tardiness = 0;         //The tardiness value.
  int sumTardiness = 0;
  int flowTime = 0;
  int dueDay[];              //due day of these jobs.
  int machineTime = 0;         //starting time to process these jobs.
  int processingTime[];      //processing time of the job
  int sequence[];

  public void setData(int sequence[], int dueDay[], int processingTime[]){
    this.sequence = sequence;
    this.dueDay = dueDay;
    this.processingTime = processingTime;
    length = dueDay.length;
  }

  public void calcObjectives() {
    objVal_tardiness = 0;
    int maxTardiness = 0;
    machineTime = 0;
    flowTime = 0;

    for(int i = 0 ; i < length ; i ++ ){
      //to calculate the tardiness
      int index = sequence[i];
      machineTime += processingTime[index];
      if(machineTime  > dueDay[index]){
        maxTardiness = (machineTime - dueDay[index]);
        sumTardiness += maxTardiness;
      }
      if(maxTardiness > objVal_tardiness){
        objVal_tardiness = maxTardiness;
      }

      //to calculate flow time
      flowTime += processingTime[index]*(length - i );
      //flowTime += machineTime;
      //System.out.print(flowTime+" ");
    }
  }

  public int getFlowTime(){
    return flowTime;
  }

  public int getMaxTardinessTime(){
    return objVal_tardiness;
  }

  public int getSumTardiness(){
    return sumTardiness;
  }


  public static void main(String[] args) {
    forScheduleMaxTardinessAndFlowTime forScheduleMaxTardiness1 = new forScheduleMaxTardinessAndFlowTime();
  }

}