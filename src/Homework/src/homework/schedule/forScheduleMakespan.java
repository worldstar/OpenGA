package src.homework.schedule;
//import ga4.util.algorithm.*;
/**
 * <p>Title: The OpenGA project</p>
 * <p>Description: The project is to build general framework of Genetic algorithm and problem independent.</p>
 * <P>For the parallel machine scheduling problem.</P>
 * Suppose we have three machines and there are 5 jobs (0, 1, 2, 3, 4) and their processing time are 6, 3, 8, 1, and 4 respectively.
 * If the sequence is [0 4 2 1 3], then, the machine time assginment is:
 *  Machine   P_time(job No.)      = total processing time
 * Machine 1: 6(job 0) + 1 (job 3) = 7
 * Machine 2: 4(job 4) + 3 (job 1) = 7
 * Machine 3: 8(job 2)             = 8
 * So the makespan max{7, 7, 8} is 8.
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class forScheduleMakespan {
  public forScheduleMakespan() {
  }


  int length;                //chromosome length.
  double objVal = 0;         //The traveling distance.
  int machineTime[];         //starting time to process these jobs.
  int processingTime[];      //processing time of the job
  int numberOfMachine;       // the number of parallel machine

  public void setData(int processingTime[]){
    length = processingTime.length;
    this.processingTime = processingTime;
  }



  public void calcObjectives() {

  }

  public double getObjectiveValue(){
    return objVal;
  }

  public static void main(String[] args) {
    forScheduleMakespan forScheduleMakespan1 = new forScheduleMakespan();
  }

}