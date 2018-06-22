package openga.ObjectiveFunctions;

import openga.chromosomes.chromosome;
import openga.util.algorithm.*;
/**
 * <p>Title: The OpenGA project</p>
 * <p>Description: The project is to build general framework of Genetic algorithm and problem independent.</p>
 * <P>For the flow shop scheduling problem.</P>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class forScheduleMakespanFlowShopPSD extends forScheduleMakespan {
  public forScheduleMakespanFlowShopPSD() {
  }

  int processingTime[][];

  public void setScheduleData(int processingTime[][],int numberOfMachine){
    length = processingTime.length;
    //machineTime = new int[numberOfMachine];
    this.processingTime = processingTime;
    this.numberOfMachine = numberOfMachine;
  }

  public void calcObjectives() {
    machineTime = new int[numberOfMachine];

    //assign each job to each machine depended on the current machine time.
    for(int i = 0 ; i < length ; i ++ ){
      int index = chromosome1.genes[i];
      for(int j = 0 ; j < numberOfMachine ; j ++ ){
        if(j == 0){
          //the starting time is the completion time of last job on first machine
          //System.out.println(i+" "+length+ " "+ " "+numberOfMachine+ " "+index);
          machineTime[j] += processingTime[index][j];
        }
        else{
          if(machineTime[j - 1] < machineTime[j]){//previous job on the machine j is not finished
            machineTime[j] = machineTime[j] + processingTime[index][j];
          }
          else{//the starting time is the completion time of last machine
            machineTime[j] = machineTime[j - 1] + processingTime[index][j];
          }
          
          for(int k = 0 ;k < i;k++)
          {
            int temp = chromosome1.genes[k];
            machineTime[j] += processingTime[temp][j];
          }
          
        }
      }
      //openga.util.printClass printClass1 = new openga.util.printClass();
      //printClass1.printMatrix("machineTime "+i, machineTime);
    }
    //The last machine time describes as the the maximum process time is the makespan.
    objVal = machineTime[numberOfMachine-1];
  }

  public static void main(String[] args) {
    forScheduleMakespanFlowShopPSD forScheduleMakespan1 = new forScheduleMakespanFlowShopPSD();
    chromosome chromosome1 = new chromosome();
    int numberOfjob = 20, numberOfMachine = 20;
    chromosome1.setGenotypeAndLength(true, numberOfjob, 2);
    chromosome1.initChromosome();
    //191: 13 17 15 11 14 19 0 16 2 18 4 5 9 1 7 3 8 10 6 12 	1796.0   79.0   	0.0
    chromosome1.genes = new int[]{13, 17, 15, 11, 14, 19, 0, 16, 2, 18, 4, 5, 9, 1, 7, 3, 8, 10, 6, 12};


    for(int i = 0 ; i < numberOfjob ; i ++ ){
      System.out.print(chromosome1.genes[i] + " ");
    }
    System.out.println();
    //to create processing time
    int processingTime[][];
    processingTime = new int[][]{
        {28,21,5,19,66,45,28,31,88,3,79,97,41,45,4,48,6,57,77,91},
        {69,89,89,11,12,82,15,31,41,86,27,9,16,9,17,75,56,80,58,75},
        {10,84,78,45,60,37,78,65,39,61,83,44,46,42,30,41,29,6,94,14},
        {18,14,73,79,85,12,97,85,15,25,15,79,47,36,10,55,98,43,96,95},
        {7,35,55,65,7,73,51,76,91,23,98,97,61,24,18,11,9,41,41,87},
        {49,81,18,1,50,32,80,38,55,90,2,30,64,40,36,58,65,60,47,62},
        {83,10,29,7,29,72,25,34,24,8,34,49,84,2,32,75,36,48,86,30},
        {45,59,81,37,31,41,54,55,55,35,49,24,53,44,75,90,93,63,1,54},
        {32,29,52,65,58,94,60,19,86,64,14,94,99,81,59,61,92,7,34,15},
        {22,79,33,65,15,6,21,29,43,57,91,57,4,24,64,59,27,19,47,23},
        {49,62,40,26,64,44,4,29,1,6,85,25,28,32,12,67,50,80,39,27},
        {82,66,85,8,72,15,27,82,60,89,76,57,37,28,33,44,2,85,22,41},
        {73,14,15,1,53,80,42,91,4,53,91,22,31,57,9,47,38,28,88,16},
        {45,71,74,72,29,50,52,99,89,97,62,80,91,8,14,86,90,70,19,73},
        {87,17,33,75,24,47,31,95,65,90,89,65,81,86,7,94,68,21,46,68},
        {19,28,13,6,5,67,67,53,50,96,77,31,79,63,8,80,12,10,61,49},
        {86,36,58,67,88,5,80,29,66,36,54,57,2,73,3,52,26,75,50,1},
        {57,8,95,56,21,88,51,54,63,69,48,86,96,98,16,86,99,54,43,58},
        {97,91,8,97,60,85,3,36,36,19,28,58,52,9,92,23,22,94,89,46},
        {58,5,82,59,60,15,72,14,61,13,81,43,5,91,28,53,35,69,23,51}};

    //to print data
    openga.util.printClass printClass1 = new openga.util.printClass();
    //printClass1.printMatrix("processingTime", processingTime);
    forScheduleMakespan1.setScheduleData(processingTime, numberOfMachine);
    forScheduleMakespan1.setData(chromosome1);
    forScheduleMakespan1.calcObjectives();

    //printClass1.printMatrix("machine time",forScheduleMakespan1.machineTime);
    System.out.println(forScheduleMakespan1.getObjectiveValue());

  }

}