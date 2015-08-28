package openga.ObjectiveFunctions;
import openga.chromosomes.chromosome;
import openga.util.algorithm.*;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class forScheduleMakespanParallelWithSetup extends forScheduleMakespan {
  public forScheduleMakespanParallelWithSetup() {
  }

  chromosome chromosome1;    //A chromosome.
  int length;                //chromosome length.
  double objVal = 0;         //The traveling distance.
  int machineTime[];         //starting time to process these jobs.
  int processingTime[][][];      //processing time of the job
  int numberOfMachine;       // the number of parallel machine

  public void setData(int processingTime[][][], chromosome chromosome1, int numberOfMachine){
    this.chromosome1 = chromosome1;
    length = processingTime.length;
    this.processingTime = processingTime;
    this.numberOfMachine = numberOfMachine;
  }

  public void setData(chromosome chromosome1){
    this.chromosome1 = chromosome1;
  }

  public void setData(int processingTime[][][], int numberOfMachine){
    machineTime = new int[numberOfMachine];
    this.processingTime = processingTime;
    this.numberOfMachine = numberOfMachine;
  }

  public void calcObjectives() {
    length = chromosome1.getLength();
    //to use getMin to find out the machine which has least processing time
    getMin getMin1 = new getMin();
    getMax getMax1 = new getMax();
    machineTime = new int[numberOfMachine];
    int prevJobIndexArray[] = new int[numberOfMachine];

    //start to assign others job.
    for(int i = 0 ; i < length ; i ++ ){
      //Johnson's rule, find the machine whose current total processing time is minimum.
      int minProcessingTimeMachine = getMin1.getDataIndex(machineTime);
      //assign the job i to the machine minProcessingTimeMachine
      int index = chromosome1.genes[i];
      if(i < numberOfMachine){//the first job of each machine
        machineTime[minProcessingTimeMachine] += processingTime[minProcessingTimeMachine][index][index];
        prevJobIndexArray[minProcessingTimeMachine] = index;
        //System.out.println(index+" "+minProcessingTimeMachine+" "+processingTime[minProcessingTimeMachine][index][index]);
      }
      else{
        int prevJobIndex = prevJobIndexArray[minProcessingTimeMachine];
        machineTime[minProcessingTimeMachine] += processingTime[minProcessingTimeMachine][prevJobIndex][index];
        prevJobIndexArray[minProcessingTimeMachine] = index;
        //System.out.println(index+" "+minProcessingTimeMachine+" "+processingTime[minProcessingTimeMachine][prevJobIndex][index]);
      }
    }

    //the maximum process time is the makespan.
    objVal = getMax1.setData(machineTime);
  }

  public double getObjectiveValue(){
    return objVal;
  }


}