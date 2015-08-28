package homework.schedule;
import homework.schedule.data.singleMachineSetupData;
/**
 * <p>Title: </p>
 * <p>Description: The SAPT heuristic is to construct initial solutions for
 * single machine schedule problem with setup considerations.</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class SAPT_SingleMachineSetup extends singleMachineSetupDP{
  public SAPT_SingleMachineSetup() {
  }

  int numberOfJobs = 8;
  double processingTime[][];
  double backupProcessingTime[][];
  public int B[];
  int sequence[];
  double obj;//earliness + taridness
  //r is the middle job.
  int r = 4;
  double previousMinAP = 0;

  int formerIndex;
  double objFormer1 = 0;
  int laterIndex;
  double objLater1 = 0;

  public void setData(int numberOfJobs, double processingTime[][]){
    this.numberOfJobs = numberOfJobs;
    this.processingTime = processingTime;
    sequence = new int[numberOfJobs];
    B = new int[numberOfJobs];

    for(int i = 0 ; i < numberOfJobs ; i ++ ){
      sequence[i] = i;
    }
  }

  public int[] getSmallestIndex(double matrix[][]){
    int index[] = new int[2];
    double minVal = Double.MAX_VALUE;
    for(int i = 0 ; i < matrix.length ; i ++ ){
      for(int j = 0 ; j < matrix[0].length ; j ++ ){
        if(i != j && matrix[i][j] != -1 && matrix[i][j] < minVal && matrix[i][j] > previousMinAP){
          minVal = matrix[i][j];
          index[0] = i;
          index[1] = j;
        }
      }
    }
    return index;
  }

  public void SAPT(){
    findMiddlePosition(sequence);
    int index[] = getSmallestIndex(processingTime);
    B[r+1] = index[1];
    B[r] = index[0];

    dropRow(index[0], processingTime);
    dropColumn(index[1], processingTime);
    dropJobs(index[0]);
    dropJobs(index[1]);

    //printSequence(B);

    int sizeB = 1, sizeA = 1;
    while(sizeB < r || sizeA < (numberOfJobs - r - 1)){
      int jobIndex = getRandomJobs();
      int jobIndex2 = getRandomJobs();
      int jobI = B[r - sizeB + 1];
      int jobJ = B[r + sizeA];

      int penaltyB = r - sizeB;
      int penaltyA = numberOfJobs - (r + sizeA);
      //penaltyB = 1;
      //penaltyA = 1;

      if(r - sizeB < 0){
        penaltyB = Integer.MAX_VALUE;
      }

      if(r + sizeA + 1 == numberOfJobs){
        penaltyA = Integer.MAX_VALUE;
      }

      //start to test //int jobFix, int job1, int job2, int penalty
      getFormerPosValue(jobI, jobIndex, jobIndex2, penaltyB);
      getLaterPosValue(jobJ, jobIndex, jobIndex2, penaltyA);

      if(objFormer1 <= objLater1){//to put the job ahead at position r - sizeB
        B[r - sizeB] = formerIndex;
        dropRow(formerIndex, processingTime);
        dropColumn(jobI, processingTime);
        sizeB += 1;
        dropJobs(formerIndex);
      }
      else{//to put the job at [r + rizeA + 1]
        B[r + sizeA + 1] = laterIndex;
        dropRow(jobJ, processingTime);
        dropColumn(laterIndex, processingTime);
        sizeA += 1;
        dropJobs(laterIndex);
      }
      /*
      if(processingTime[jobIndex][jobI]*(penaltyB) <= processingTime[jobJ][jobIndex]*(penaltyA)){
        B[r - sizeB] = jobIndex;
        dropRow(jobIndex, processingTime);
        dropColumn(jobI, processingTime);
        sizeB += 1;
      }
      else{
        B[r + sizeA + 1] = jobIndex;
        dropRow(jobJ, processingTime);
        dropColumn(jobIndex, processingTime);
        sizeA += 1;
      }
      dropJobs(jobIndex);
      */

      //System.out.println(sizeB+" "+sizeA);
      //printSequence(B);
    }//end while
    //printSequence(B);
    calcObj(B);
    //System.out.println("obj "+obj);
  }

  public int getRandomJobs(){
    int jobIndex = (int)(Math.random()*numberOfJobs);
    while(sequence[jobIndex] == -1){
      jobIndex = (jobIndex + 1) % numberOfJobs;
    }
    return jobIndex;
  }

  public void getFormerPosValue(int jobFix, int job1, int job2, int penalty){
    if(processingTime[job1][jobFix] <= processingTime[job2][jobFix]){
      formerIndex = job1;
      objFormer1 = processingTime[job1][jobFix]*penalty;
    }
    else{
      formerIndex = job2;
      objFormer1 = processingTime[job2][jobFix]*penalty;
    }
  }


  public void getLaterPosValue(int jobFix, int job1, int job2, int penalty){
    if(processingTime[job1][jobFix] <= processingTime[job2][jobFix]){
      laterIndex = job1;
      objLater1 = processingTime[jobFix][job1]*penalty;
    }
    else{
      laterIndex = job2;
      objLater1 = processingTime[jobFix][job2]*penalty;
    }
  }


  public void dropJobs(int jobIndex){
    sequence[jobIndex] = -1;
  }

  public double[][] dropColumn(int index, double matrix[][]){
    for(int i = 0 ; i < matrix.length ; i ++ ){
      matrix[i][index] = -1;
    }
    return matrix;
  }

  public double[][] dropRow(int index, double matrix[][]){
    for(int i = 0 ; i < matrix.length ; i ++ ){
      matrix[index][i] = -1;
    }
    return matrix;
  }

  public void swapJobs(int pos1, int pos2){
    int temp = B[pos1];
    B[pos1] = B[pos2];
    B[pos2] = temp;
  }

  public void calcObj(int _sequence[]){
    obj = 0;
    restoreAP();

    //to calculate the Total earliness
    for(int i = 0 ; i < r ; i ++ ){
      obj += (i+1)*processingTime[_sequence[i]][_sequence[i+1]];
    }

    //to calculate the Total tardiness
    for(int i = r ; i < numberOfJobs - 1 ; i ++ ){
      obj += (numberOfJobs - i - 1)*processingTime[_sequence[i]][_sequence[i+1]];
    }
  }

  public double getObjValue(){
    return obj;
  }

  public int[] getSequence(){
    return B;
  }

  public void backupAP(){
    backupProcessingTime = new double[numberOfJobs][numberOfJobs];
    for(int n = 0 ; n < numberOfJobs ; n ++ ){
      for (int k = 0; k < numberOfJobs ; k++) { //15
        backupProcessingTime[n][k] = processingTime[n][k];
      }
    }
  }

  public void restoreAP(){
    for(int n = 0 ; n < numberOfJobs ; n ++ ){
      for (int k = 0; k < numberOfJobs ; k++) { //15
        processingTime[n][k] = backupProcessingTime[n][k];
      }
    }
    for(int i = 0 ; i < numberOfJobs ; i ++ ){
      sequence[i] = i;
    }
  }


  public void printSequence(int _B[]){
    for(int k = 0 ; k < _B.length ; k ++ ){//15
      System.out.print(_B[k]+" ");
    }
    System.out.print("\n");
  }

  public static void main(String[] args) {
    int numberOfJobs;
    double processingTime[][];
    int jobSets[] = new int[]{25};//10, 15, 20, 25
    String type[] = new String[]{"low"};//"low", "med", "high"

    for(int replications = 0 ; replications < 1 ; replications ++ ){
      for(int m = 0 ; m < jobSets.length ; m ++ ){//jobSets.length
        for(int n = 0 ; n < type.length ; n ++ ){
          for(int k = 1 ; k <= 1 ; k ++ ){//15
            singleMachineSetupData singleMachineData1 = new singleMachineSetupData();
            SAPT_SingleMachineSetup singleMachine1 = new SAPT_SingleMachineSetup();
            String fileName = "instances\\SingleMachineSetup\\"+type[n]+"\\"+jobSets[m]+"_"+k+".etp";
            //fileName = "Data\\SMSetupTime8.txt";//for test
            //System.out.println(fileName);
            singleMachineData1.setData(fileName);
            singleMachineData1.getDataFromFile();
            numberOfJobs = singleMachineData1.getSize();
            processingTime = singleMachineData1.getProcessingTime();

            double obj = Double.MAX_VALUE;
            int currentSoluion[] = new int[numberOfJobs];
            homework.util.timeClock timeClock1 = new homework.util.timeClock();
            timeClock1.start();

            for(int i = 0 ; i < 1 ; i ++ ){//i initial solutions
              if(i == 0){
                singleMachine1.setData(numberOfJobs, processingTime);
                singleMachine1.backupAP();
              }
              else{
                singleMachine1.restoreAP();
              }

              singleMachine1.SAPT();
              //System.out.println("obj in main "+singleMachine1.getObjValue());
              if(obj > singleMachine1.getObjValue()){
                obj = singleMachine1.getObjValue();
              }
            }
            timeClock1.end();
            String result = type[n]+"\t"+jobSets[m]+"\t"+k+"\t"+obj+"\t"+timeClock1.getExecutionTime()/1000.0+"\n";
            System.out.print(result);
            //singleMachine1.writeFile("oneMachineSetup1018", result);

            /*
            System.out.print(replications+" "+obj+" [");
            for(int j = 0 ; j < numberOfJobs ; j ++ ){
              System.out.print((currentSoluion[j]+1)+ " ");
            }
            System.out.print("]\n");
            */
          }
        }
      }
    }
  }//end main



}