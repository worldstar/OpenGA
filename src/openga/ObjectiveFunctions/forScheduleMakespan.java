package openga.ObjectiveFunctions;
import openga.chromosomes.chromosome;
import openga.util.algorithm.*;
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

  chromosome chromosome1;    //A chromosome.
  int length;                //chromosome length.
  double objVal = 0;         //The traveling distance.
  int machineTime[];         //starting time to process these jobs.
  int processingTime[];      //processing time of the job
  int numberOfMachine;       // the number of parallel machine

  public void setData(int processingTime[], chromosome chromosome1, int numberOfMachine){
    this.chromosome1 = chromosome1;
    length = processingTime.length;
    this.processingTime = processingTime;
    this.numberOfMachine = numberOfMachine;
  }

  public void setData(chromosome chromosome1){
    this.chromosome1 = chromosome1;
  }

  public void setData(int processingTime[],int numberOfMachine){
    length = processingTime.length;
    machineTime = new int[numberOfMachine];
    this.processingTime = processingTime;
    this.numberOfMachine = numberOfMachine;
  }


  public void calcObjectives() {
    //to use getMin to find out the machine which has least processing time
    getMin getMin1 = new getMin();
    getMax getMax1 = new getMax();
    machineTime = new int[numberOfMachine];

    //start to assign others job.
    for(int i = 0 ; i < length ; i ++ ){
      //Johnson's rule, find the machine whose current total processing time is minimum.
      int minProcessingTimeMachine = getMin1.getDataIndex(machineTime);
      //assign the job i to the machine minProcessingTimeMachine
      int index = chromosome1.genes[i];
      machineTime[minProcessingTimeMachine] += processingTime[index];
    }

    //the maximum process time is the makespan.
    objVal = getMax1.setData(machineTime);
  }

  public double getObjectiveValue(){
    return objVal;
  }

  public static void main(String[] args) {
    forScheduleMakespan forScheduleMakespan1 = new forScheduleMakespan();
    chromosome chromosome1 = new chromosome();
    int numberOfjob = 5, numberOfMachine = 3;
    chromosome1.setGenotypeAndLength(true, numberOfjob,2);
    chromosome1.initChromosome();

    for(int i = 0 ; i < numberOfjob ; i ++ ){
      System.out.print(chromosome1.genes[i] + " ");
    }
    System.out.println();
    //to create processing time
    int processingTime[] = new int[numberOfjob];

    for(int i = 0 ; i < numberOfjob ; i ++ ){
      processingTime[i] = 1+(int)(Math.random()*10);
    }

    //to print data
    openga.util.printClass printClass1 = new openga.util.printClass();
    printClass1.printMatrix("processingTime",processingTime);

    forScheduleMakespan1.setData(processingTime, chromosome1, numberOfMachine);
    forScheduleMakespan1.calcObjectives();
    printClass1.printMatrix("machine time",forScheduleMakespan1.machineTime);
    System.out.println(forScheduleMakespan1.getObjectiveValue());
  }

}