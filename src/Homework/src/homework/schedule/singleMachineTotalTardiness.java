package homework.schedule;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class singleMachineTotalTardiness {
  public singleMachineTotalTardiness() {
  }

  int numberOfJobs = 8;
  int dueDate[];
  int processingTime[];
  int finishTime[];
  int sequence[];
  int obj;//earliness + taridness

  public void setData(int numberOfJobs, int dueDate[], int processingTime[], int sequence[]){
    this.numberOfJobs = numberOfJobs;
    this.dueDate = dueDate;
    this.processingTime = processingTime;
    this.sequence = sequence;
    finishTime = new int[numberOfJobs];
  }

  public void startCalc(){
    int currentTime = 0;
    obj = 0;
    for(int i = 0 ; i < numberOfJobs ; i ++ ){
      finishTime[sequence[i]] = currentTime + processingTime[sequence[i]];
      currentTime = finishTime[sequence[i]];
      obj += Math.abs(finishTime[sequence[i]] - dueDate[sequence[i]]);
    }
  }

  public int getObjValue(){
    return obj;
  }

  public void checkTardyOrEarly(){
    for(int i = 0 ; i < numberOfJobs ; i ++ ){
      if(finishTime[sequence[i]] < dueDate[sequence[i]]){
        System.out.print("Job "+(sequence[i]+1)+": Early.\n");
      }
      else if(finishTime[sequence[i]] > dueDate[sequence[i]]){
        System.out.print("Job "+(sequence[i]+1)+": Tardy.\n");
      }
      else{
        System.out.print("Job "+(sequence[i]+1)+": On time.\n");
      }
    }
  }

  public static void main(String[] args) {
    System.out.println("To examine the factorial combinations.");
    singleMachineTotalTardiness singleMachineTotalTardiness1 = new singleMachineTotalTardiness();
    singleMachineData singleMachineData1 = new singleMachineData();
    int numberOfJobs = 8;

    int tenJobs = 13;
    numberOfJobs = tenJobs;
    int dueDate[] = singleMachineData1.getRandomDue(tenJobs);
    int processingTime[] = singleMachineData1.getRandomProcessingTime(tenJobs);
    int[] indices;
    int comb[] =new int[numberOfJobs];
    for(int i = 0 ; i < numberOfJobs ; i ++ ){
      comb[i] = i;
    }

    PermutationGenerator x = new PermutationGenerator (numberOfJobs);
    int counter = 0;
    int obj = Integer.MAX_VALUE;
    int optSoln[] = new int[numberOfJobs];
    int k = 0;

    while (x.hasMore ()){
      //permutation = new StringBuffer ();
      indices = x.getNext ();
      singleMachineTotalTardiness1.setData(numberOfJobs, dueDate, processingTime, indices);
      singleMachineTotalTardiness1.startCalc();
      //System.out.println(counter++ +" "+singleMachineTotalTardiness1.getObjValue());
      if(obj >= singleMachineTotalTardiness1.getObjValue()){
        obj = singleMachineTotalTardiness1.getObjValue();
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
    singleMachineTotalTardiness1.setData(numberOfJobs, dueDate, processingTime, optSoln);
    singleMachineTotalTardiness1.startCalc();

/*
    homework.util.readSingleMachineData readSingleMachineData1 = new homework.util.readSingleMachineData();
    readSingleMachineData1.setData("sks/sks228a.txt");
    readSingleMachineData1.getDataFromFile();
    int dueDate[] = readSingleMachineData1.getDueDate();
    int processingTime[] = readSingleMachineData1.getPtime();
    int[] indices;

    int comb[] =new int[numberOfJobs];
    for(int i = 0 ; i < numberOfJobs ; i ++ ){
      comb[i] = i;
    }

    PermutationGenerator x = new PermutationGenerator (numberOfJobs);
    int counter = 0;
    int obj = Integer.MAX_VALUE;
    int optSoln[] = new int[numberOfJobs];
    int k = 0;

    while (x.hasMore ()){
      //permutation = new StringBuffer ();
      indices = x.getNext ();
      singleMachineTotalTardiness1.setData(numberOfJobs, dueDate, processingTime, indices);
      singleMachineTotalTardiness1.startCalc();
      //System.out.println(counter++ +" "+singleMachineTotalTardiness1.getObjValue());
      if(obj >= singleMachineTotalTardiness1.getObjValue()){
        obj = singleMachineTotalTardiness1.getObjValue();
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
    singleMachineTotalTardiness1.setData(numberOfJobs, dueDate, processingTime, optSoln);
    singleMachineTotalTardiness1.startCalc();



    for(int k = 0 ; k < 16 ; k ++ ){
      int jobIndex = k;
      //int dueDate[] = singleMachineData1.dueTest1;
      //int processingTime[] = singleMachineData1.pTimeTest1;
      int dueDate[] = singleMachineData1.getDueDate(jobIndex);
      int processingTime[] = singleMachineData1.getPtime(jobIndex);
      int[] indices;

      int comb[] =new int[numberOfJobs];
      for(int i = 0 ; i < numberOfJobs ; i ++ ){
        comb[i] = i;
      }

      PermutationGenerator x = new PermutationGenerator (numberOfJobs);
      int counter = 0;
      int obj = Integer.MAX_VALUE;
      int optSoln[] = new int[numberOfJobs];

      while (x.hasMore ()){
        //permutation = new StringBuffer ();
        indices = x.getNext ();
        singleMachineTotalTardiness1.setData(numberOfJobs, dueDate, processingTime, indices);
        singleMachineTotalTardiness1.startCalc();
        //System.out.println(counter++ +" "+singleMachineTotalTardiness1.getObjValue());
        if(obj >= singleMachineTotalTardiness1.getObjValue()){
          obj = singleMachineTotalTardiness1.getObjValue();
          for(int i = 0 ; i < numberOfJobs ; i ++ ){
            optSoln[i] = indices[i];
          }
          /*
          for(int i = 0 ; i < numberOfJobs ; i ++ ){
            System.out.print((optSoln[i]+1)+" ");
          }
          System.out.print(" "+obj+"]\n");
              *
        }
      }//end while
      System.out.print((k+1)+"\t"+obj+"\t[");
      for(int i = 0 ; i < numberOfJobs ; i ++ ){
        System.out.print((optSoln[i]+1)+" ");
      }
      System.out.print("]\n");
      /
      singleMachineTotalTardiness1.setData(numberOfJobs, dueDate, processingTime, optSoln);
      singleMachineTotalTardiness1.startCalc();
      singleMachineTotalTardiness1.checkTardyOrEarly();

    }
*/
  }
}