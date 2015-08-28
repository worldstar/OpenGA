package openga.ObjectiveFunctions;
import openga.chromosomes.*;
import openga.util.algorithm.*;
/**
 * <p>Title: The OpenGA project</p>
 * <p>Description: The project is to build general framework of Genetic algorithm and problem independent.</p>
 * <P>The program is based class to calculate tardiness. So the ObjectiveTardiness will apply it.</P>
 * <P>For the parallel machine scheduling problem.</P>
 * Suppose we have three machines and there are 5 jobs (0, 1, 2, 3, 4) and their processing time are [4 4 6 7 2] respectively.
 * Due date is [2 3 13 12 8].
 * If the sequence is [2 0 1 4 3], then, the machine time assginment is:
 * Machine   P_time(job No.)->tardiness   = tardiness time
 * Machine 1: 6(job 2)->0                 = 0
 * Machine 2: 4(job 0)->2,  2 (job 4)->0  = 2
 * Machine 3: 4(job 1)->1   1 (job 3)->0  = 1
 * Total tardiness sum(tardiness of machine1, machine2, and machine3)= 3.
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class forScheduleTardiness {
  public forScheduleTardiness() {
  }

  chromosome chromosome1;    //A chromosome.
  int length;                //chromosome length.
  double objVal = 0;         //The tardiness value.
  int dueDay[];              //due day of these jobs.
  int machineTime[];         //starting time to process these jobs.
  int processingTime[];      //processing time of the job
  int numberOfMachine;       // the number of parallel machine

  public void setData(int dueDay[], int processingTime[], chromosome chromosome1, int numberOfMachine){
    this.chromosome1 = chromosome1;
    this.dueDay = dueDay;
    machineTime = new int[numberOfMachine];
    this.processingTime = processingTime;
    length = dueDay.length;
    this.numberOfMachine = numberOfMachine;
  }

  public void setData(chromosome chromosome1){
    this.chromosome1 = chromosome1;
  }

  public void setData(int dueDay[], int processingTime[], int numberOfMachine){
    this.dueDay = dueDay;
    machineTime = new int[numberOfMachine];
    this.processingTime = processingTime;
    length = dueDay.length;
    this.numberOfMachine = numberOfMachine;
  }


  public void calcObjectives() {
    //to use getMin to find out the machine which has least processing time
    getMin getMin1 = new getMin();
    objVal = 0;
    machineTime = new int[numberOfMachine];

    //start to assign others job.
    for(int i = 0 ; i < length ; i ++ ){
      //Johnson's rule, find the machine whose current total processing time is minimum.
      int minProcessingTimeMachine = getMin1.getDataIndex(machineTime);
      //assign the job i to the machine minProcessingTimeMachine
      int index = chromosome1.genes[i];
      machineTime[minProcessingTimeMachine] = machineTime[minProcessingTimeMachine] + processingTime[index];
      if(machineTime[minProcessingTimeMachine]  > dueDay[index]){
        objVal += (machineTime[minProcessingTimeMachine] - dueDay[index]);
      }
    }
  }

  public double getObjectiveValue(){
    return objVal;
  }

  public static void main(String[] args) {
    forScheduleTardiness forScheduleTardiness1 = new forScheduleTardiness();

    chromosome chromosome1 = new chromosome();
    int numberOfjob = 5, numberOfMachine = 3;
    chromosome1.setGenotypeAndLength(true, numberOfjob,2);
    chromosome1.initChromosome();

    //to create processing time and due date
    int dueday[] = new int[numberOfjob];
    int processingTime[] = new int[numberOfjob];

    for(int i = 0 ; i < numberOfjob ; i ++ ){
      dueday[i] = (int)(Math.random()*15);
      processingTime[i] = 2+(int)(Math.random()*10);
    }

    //to print data
    openga.util.printClass printClass1 = new openga.util.printClass();
    printClass1.printMatrix("chromosome1.genes",chromosome1.genes);
    printClass1.printMatrix("dueday",dueday);
    printClass1.printMatrix("processingTime",processingTime);

    forScheduleTardiness1.setData(dueday, processingTime, chromosome1, numberOfMachine);
    forScheduleTardiness1.calcObjectives();
    printClass1.printMatrix("machine time",forScheduleTardiness1.machineTime);
    System.out.println(forScheduleTardiness1.getObjectiveValue());

  }

}