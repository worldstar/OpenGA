package homework.schedule;
import homework.schedule.data.parallelMachineNew;
import homework.util.algorithm.*;
/**
 * <p>Title: The parallel machine scheduling problem is to generate an initial sequence by using
 * the Dominance Property of single machine problem. </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author Shih-Hsin Chen
 * @version 1.0
 */

public class parallelMachineWithDP extends singleMachine{
  public parallelMachineWithDP() {
  }

  int numberOfMachine = 2;
  int machineTime[];

  public void setParallelMachineData(int numberOfMachine){
    this.numberOfMachine = numberOfMachine;
  }

  /**
   * To invoke the DP procedures for single machine problem to obtain an initial sequence.
   */
  public void startAlgorithm(){
    numberOfIterations = 2;
    if(numberOfIterations == 1){
      numberOfIterations = numberOfJobs*numberOfJobs;
    }
    else{
      numberOfIterations = (int)(numberOfJobs*Math.log(numberOfJobs));
    }

    setPreviousSolution(sequence);   //to set the initial solution as the previous soln.
    int counter = 0;

    do{
      counter ++;
      calcFinishTime(sequence);      //the finish time of the schedule
      for(int pos1 = 0 ; pos1 < 2 ; pos1 ++ ){
        for(int i = pos1 ; i < numberOfJobs - 1 ; i ++ ){
          getTwoJobs(sequence[i], sequence[i+1], i);
          i += 1;
        }
      }

      //if the solution is still the same, it means the algorithm won't change the sequence anymore.
      if(isTheSame(sequence, preSequence)){
        break;
      }
      else{
        setPreviousSolution(sequence);
      }
    }
    while(counter < numberOfIterations);//the other stopping criterion is the number of iterations.Math.log(numberOfJobs)
    calcParallelObjs(sequence);
  }

  public double calcParallelObjs(int _sequence[]){
    obj = 0;

    //to use getMin to find out the machine which has least processing time
    getMin getMin1 = new getMin();
    machineTime = new int[numberOfMachine];

    //start to assign others job.
    for(int i = 0 ; i < numberOfJobs ; i ++ ){
      //Johnson's rule, find the machine whose current total processing time is minimum.
      int minProcessingTimeMachine = getMin1.getDataIndex(machineTime);
      //assign the job i to the machine minProcessingTimeMachine
      int index = _sequence[i];
      machineTime[minProcessingTimeMachine] += processingTime[index];

      if(machineTime[minProcessingTimeMachine] > dueDate[index]){//tardy job
        obj += machineTime[minProcessingTimeMachine] - dueDate[index];
      }
      else{//early job
        obj += dueDate[index] - machineTime[minProcessingTimeMachine];
      }
    }
    return obj;
  }

  public static void main(String[] args) {
    int jobSets[] = new int[]{35, 50, 65, 80};
    int numberOfMachine[] = new int[]{7, 10, 13, 16};

    for(int replications = 0 ; replications < 1 ; replications ++ ){
      for(int m = 0 ; m < 4 ; m ++ ){//jobSets.length
          parallelMachineWithDP parallelMachineWithDP1 = new parallelMachineWithDP();
          int k = 0;
          parallelMachineNew parallelMachineNew1 = new parallelMachineNew();
          int numberOfJobs = jobSets[m];
          int dueDate[] = parallelMachineNew1.getDueDay(numberOfJobs);
          int processingTime[] = parallelMachineNew1.getProcessingTime(numberOfJobs);
          double bestObj = Double.MAX_VALUE;
          int currentSoluion[] = new int[numberOfJobs];
          homework.util.timeClock timeClock1 = new homework.util.timeClock();
          timeClock1.start();
          for(int i = 0 ; i < 100 ; i ++ ){//i initial solutions
            int sequence[] = new int[numberOfJobs];//5, 3, 2, 1, 4//2 1 4 6 3 5 8 7
            for(int j = 0 ; j < numberOfJobs ; j ++ ){
              sequence[j] = j;
            }
            parallelMachineWithDP1.setData(numberOfJobs, dueDate, processingTime, sequence);
            parallelMachineWithDP1.setParallelMachineData(numberOfMachine[m]);
            parallelMachineWithDP1.generateInitialSolution(i);
            parallelMachineWithDP1.startAlgorithm();
            //to compare the objective value.
            if(bestObj > parallelMachineWithDP1.getObjValue()){
              bestObj = parallelMachineWithDP1.getObjValue();
              //currentSoluion = parallelMachineWithDP1.getSolution();
            }
          }
          timeClock1.end();
          String result = numberOfJobs+"\t"+numberOfMachine[m]+"\t"+bestObj+"\t"+timeClock1.getExecutionTime()/1000.0+"\n";
          System.out.print(result);
          parallelMachineWithDP1.writeFile("ParallelMachineProperties20060520", result);
      }
    }//end replications.

  }
}