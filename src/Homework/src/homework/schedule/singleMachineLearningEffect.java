package homework.schedule;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class singleMachineLearningEffect {
  public singleMachineLearningEffect() {
  }

  int dueDay[], processingTime[], numberOfMachine, length;
  double PiLearningMatrix[][];//[job, position]
  double learningRate = -0.152;//-0.515, -0.322, -0.152

  public void setScheduleData(int dueDay[], int processingTime[], int numberOfMachine){
    this.dueDay = dueDay;
    this.processingTime = processingTime;
    this.numberOfMachine = numberOfMachine;
    length = processingTime.length;
    processingTimeWithLearning();
  }

  public void setLearningRate(double learningRate){
    this.learningRate = learningRate;
    //System.out.println(learningRate);
  }

  public void processingTimeWithLearning(){
    PiLearningMatrix = new double[length][length];
    for(int i = 0 ; i < length ; i ++ ){//job
      for(int pos = 0 ; pos < length ; pos ++ ){//position
        PiLearningMatrix[i][pos] = processingTime[i]*Math.pow((pos+1), learningRate);
      }
    }
  }

  public double calcEarlinessAndTardiness(int chromosome1[]) {
    double objVal = 0;
    double machineTime = 0;
    double finishTime[] = calcFinishTime(chromosome1);
    //double k_due = calcDueDate(chromosome1.genes);
    //System.out.println("k_due "+k_due);

    //for the Mosheiov (2001) in EJOR
    //objVal = chromosome1.getLength()*5*k_due;

    for(int i = 0 ; i < length ; i ++ ){
      int jobIndex = chromosome1[i];
      //objVal += Math.abs(finishTime[jobIndex] - k_due);
      objVal += Math.abs(finishTime[jobIndex] - dueDay[jobIndex]);
    }

    /*
    for(int i = 0 ; i < length ; i ++ ){//job
      int jobIndex = chromosome1.getSolution()[i];
      //System.out.print((jobIndex+1)+"\t\t"+PiLearningMatrix[jobIndex][i]+"\t"+Math.abs(finishTime[jobIndex] - k_due)+"\n");
    }
    */

    return objVal;
  }

  public double[] calcFinishTime(int _seq[]){
    double finishTime[] = new double[_seq.length];
    double currentTime = 0;

    for(int i = 0 ; i < _seq.length ; i ++ ){
      finishTime[_seq[i]] = currentTime + processingTime[_seq[i]];
      currentTime = finishTime[_seq[i]];
    }
    return finishTime;
  }

  public int getMidPosition(int _seq[]){
    int r = 4;//the k is the summation processing time of the first r jobs.
    if(_seq.length % 2 == 0){
      r = _seq.length / 2;
    }
    else{
      r = (_seq.length+1) / 2;
    }
    return r;
  }

/*
  private double calcDueDate(int _seq[]){
    double k = 0;
    int r = getMidPosition(_seq);//the k is the summation processing time of the first r jobs.

    for(int i = 0 ; i < r ; i ++ ){
      k +=  processingTime[_seq[i]];
    }
    return k;
  }

  private double[] calcFinishTime(int _seq[]){
    double finishTime[] = new double[_seq.length];
    double currentTime = 0;

    for(int i = 0 ; i < _seq.length ; i ++ ){
      finishTime[_seq[i]] = currentTime + PiLearningMatrix[_seq[i]][i];
      currentTime = finishTime[_seq[i]];
    }
    return finishTime;
  }

  private double calcDueDate(int _seq[]){
    double k = 0;
    int r = 4;//the k is the summation processing time of the first r jobs.
    if(_seq.length % 2 == 0){
      r = _seq.length / 2;
    }
    else{
      r = (_seq.length+1) / 2;
    }

    for(int i = 0 ; i < r ; i ++ ){
      k +=  PiLearningMatrix[_seq[i]][i];
    }
    //System.out.println("r= "+r+"\t k= "+k);
    return k;
  }
*/
  public static void main(String args[]){
    int dueDate[], processingTime[], numberOfMachine = 1;
    int[] indices;
    int length = 20;
    double learningRate = -0.322;//-0.515, -0.322, -0.152

    //data section
    homework.util.readSingleMachineData readSingleMachineData1 = new homework.util.readSingleMachineData();
    int numberOfJobs = length;
    String fileName = "sks222a";
    System.out.print(fileName+"\t");
    readSingleMachineData1.setData("sks/"+fileName+".txt");
    readSingleMachineData1.getDataFromFile();
    dueDate = readSingleMachineData1.getDueDate();
    processingTime = readSingleMachineData1.getPtime();

    //set data
    singleMachineLearningEffect singleMachineLearningEffect1 = new singleMachineLearningEffect();
    singleMachineLearningEffect1.setLearningRate(learningRate);
    singleMachineLearningEffect1.setScheduleData(dueDate, processingTime, numberOfMachine);
    double obj = Integer.MAX_VALUE;
    int optSoln[] = new int[numberOfJobs];

    int comb[] =new int[length];
    for(int i = 0 ; i < length ; i ++ ){
      comb[i] = i + 1;
    }
    PermutationGenerator x = new PermutationGenerator (length);
    //StringBuffer permutation;
    int counter = 0;
    while (x.hasMore ()){
      //permutation = new StringBuffer ();
      indices = x.getNext ();
      double tempObj = singleMachineLearningEffect1.calcEarlinessAndTardiness(indices);
      if(obj >= tempObj){
        obj = tempObj;
        for(int i = 0 ; i < numberOfJobs ; i ++ ){
          optSoln[i] = indices[i];
        }
      }
      counter ++;
    }//end while
    System.out.print(obj+"\t[");
    for(int i = 0 ; i < numberOfJobs ; i ++ ){
      System.out.print((optSoln[i]+1)+" ");
    }
    System.out.print("]\n");

  }//end main()

}