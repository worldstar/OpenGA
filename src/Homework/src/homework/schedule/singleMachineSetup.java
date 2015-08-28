package homework.schedule;
import homework.schedule.data.singleMachineSetupData;
/**
 * <p>Title: </p>
 * <p>Description: It's the single machine problem with setup time in common due date.</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class singleMachineSetup {
  public singleMachineSetup() {
  }
  int numberOfJobs = 8;
  double processingTime[][];
  double finishTime[];
  int sequence[];
  double obj;//earliness + taridness
  //r is the middle job.
   int r = 4;

  public void setData(int numberOfJobs, double processingTime[][], int sequence[]){
    this.numberOfJobs = numberOfJobs;
    this.processingTime = processingTime;
    this.sequence = sequence;
    finishTime = new double[numberOfJobs];
    /*
    for(int i = 0 ; i < numberOfJobs ; i ++ ){
      for(int j = 0 ; j < numberOfJobs ; j ++ ){
        System.out.print(processingTime[i][j]+" ");
      }
      System.out.print("\n");
    }
    */
  }

  public double calcCommonDueDate(int _seq[]){
    //the k is the summation processing time of the first r jobs.
    double k = 0;
    findMiddlePosition(_seq);

    for(int i = 0 ; i < r ; i ++ ){
      k +=  processingTime[_seq[i]][_seq[i+1]];
      //System.out.println(_seq[i]+"\t"+_seq[i+1]+" = "+processingTime[_seq[i]][_seq[i+1]]);
    }
    //System.out.println("r= "+r+"\t k= "+k);
    return k;
  }

  public double findMiddlePosition(int _seq[]){
    if(_seq.length % 2 == 0){
      r = _seq.length / 2;
    }
    else{
      r = (_seq.length+1) / 2;
    }

    if(r > 0){
      r -= 1;
    }
    return r;
  }

  public void startCalc(){
    double currentTime = 0;
    obj = 0;
    double commonDueDate = calcCommonDueDate(sequence);

    //to calculate the Total earliness
    for(int i = 0 ; i < r ; i ++ ){
      obj += (i+1)*processingTime[sequence[i]][sequence[i+1]];
      //System.out.println((sequence[i]+1)+"\t"+(sequence[i+1]+1)+" = "+(i+1)*processingTime[sequence[i]][sequence[i+1]]);
    }

    //to calculate the Total tardiness
    for(int i = r ; i < numberOfJobs - 1 ; i ++ ){
      obj += (numberOfJobs - i - 1)*processingTime[sequence[i]][sequence[i+1]];
      //System.out.println((sequence[i]+1)+"\t"+(sequence[i+1]+1)+" = "+(numberOfJobs - i - 1)*processingTime[sequence[i]][sequence[i+1]]);
    }
  }

  public double getObjValue(){
    return obj;
  }

  public static void main(String[] args) {
    System.out.println("Single Machine with setup time in common due date.");
    singleMachineSetup singleMachineSetup1 = new singleMachineSetup();

    singleMachineSetupData singleMachineData1 = new singleMachineSetupData();
    singleMachineData1.setData("Data\\SMSetupTime8.txt");
    singleMachineData1.getDataFromFile();

    int numberOfJobs = singleMachineData1.getSize();
    double processingTime[][] = singleMachineData1.getProcessingTime();
    int[] indices;
    int comb[] =new int[numberOfJobs];
    for(int i = 0 ; i < numberOfJobs ; i ++ ){
      comb[i] = i;
    }

    PermutationGenerator x = new PermutationGenerator (numberOfJobs);
    int counter = 0;
    double obj = Double.MAX_VALUE;
    int optSoln[] = new int[numberOfJobs];
    int k = 0;

    while (x.hasMore ()){
      //permutation = new StringBuffer ();
      indices = x.getNext ();
      singleMachineSetup1.setData(numberOfJobs, processingTime, indices);
      singleMachineSetup1.startCalc();
      //System.out.println(counter++ +" "+singleMachineTotalTardiness1.getObjValue());
      if(obj >= singleMachineSetup1.getObjValue()){
        obj = singleMachineSetup1.getObjValue();
        for(int i = 0 ; i < numberOfJobs ; i ++ ){
          optSoln[i] = indices[i];
        }
      }
    }//end while

    System.out.print((k+1)+"\t"+obj+"\t[");
    for(int i = 0 ; i < numberOfJobs ; i ++ ){
      System.out.print((optSoln[i]+1)+" ");
    }
    System.out.print("]\n");
    /*
    optSoln = new int[]{5, 1, 2, 4, 6, 7, 0, 3};
    for(int i = 0 ; i < numberOfJobs ; i ++ ){
      System.out.print((optSoln[i]+1)+" ");
    }
    System.out.print("\n");

    singleMachineSetup1.setData(numberOfJobs, processingTime, optSoln);
    singleMachineSetup1.startCalc();
    System.out.print(singleMachineSetup1.getObjValue()+"\n");
   */
  }

}