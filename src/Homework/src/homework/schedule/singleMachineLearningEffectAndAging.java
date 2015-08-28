package homework.schedule;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class singleMachineLearningEffectAndAging extends singleMachineLearningEffect {
  public singleMachineLearningEffectAndAging() {
  }

  double P1 = 35, P2 = 11, P3 = 18;

  public void setPenaltyValues(double P1, double P2, double P3){
    this.P1 = P1;
    this.P2 = P2;
    this.P3 = P3;
  }

  public double calcEarlinessAndTardiness(int chromosome1[]) {
    double objVal = 0;
    double machineTime = 0;
    double finishTime[] = calcFinishTime(chromosome1);
    double KPrumvalue = calcKPrum();
    double Kvalue = Math.ceil(KPrumvalue);
    double k_due = calcDueDate(chromosome1);
    double Y1 = 0, Y2 = 0;
    //System.out.println("k_due "+k_due);

    //objVal = P1*k_due;
    objVal = P1*length*k_due;
    //System.out.println("X1 "+objVal);
    for(int i = 0 ; i < Kvalue ; i ++ ){
      int jobIndex = chromosome1[i];
      Y1 += i*PiLearningMatrix[jobIndex][i];
    }
    //System.out.println("X2 "+Y1*P2);

    for(int i = (int)Kvalue ; i < length ; i ++ ){
      int jobIndex = chromosome1[i];
      Y2 += (length - i)*PiLearningMatrix[jobIndex][i];
    }
    //System.out.println("X3 "+Y2*P3);
    objVal += P2*Y1 + P3*Y2;
    return objVal;
  }

  private double calcKPrum(){
    return length*(P3-P1)/(P2+P3);
  }

  private double calcDueDate(int _seq[]){
    double k = 0;
    int r = getMidPosition(_seq);//the k is the summation processing time of the first r jobs.

    for(int i = 0 ; i < r ; i ++ ){
      k +=  PiLearningMatrix[_seq[i]][i];
    }
    //System.out.println("r= "+r+"\t k= "+k);
    return k;
  }

  public static void main(String args[]){
    int dueDate[], processingTime[], numberOfMachine = 1;
    int[] indices;
    int length = 7;
    double learningRate = -0.35;//-0.515, -0.322, -0.152, -0.0010
    double P1 = 5, P2 = 11, P3 = 18;

    while(learningRate <= 0){
      //data section
      homework.util.readSingleMachineData readSingleMachineData1 = new homework.util.readSingleMachineData();
      int numberOfJobs = length;
      String fileName = "7jobsLearning";
      //System.out.print(fileName+"\t");
      readSingleMachineData1.setData("sks/"+fileName+".txt");
      readSingleMachineData1.getDataFromFile();
      dueDate = readSingleMachineData1.getDueDate();
      processingTime = readSingleMachineData1.getPtime();

      //set data
      singleMachineLearningEffectAndAging singleMachineLearningEffect1 = new singleMachineLearningEffectAndAging();
      singleMachineLearningEffect1.setLearningRate(learningRate);
      singleMachineLearningEffect1.setScheduleData(dueDate, processingTime, numberOfMachine);
      singleMachineLearningEffect1.setPenaltyValues(P1, P2, P3);
      //four sequences
      int allsequences[][] = new int[][]{{6, 4, 2, 1, 3, 5, 7}, {5,4,2,1,3,6,7},
                                   {5,3,2,1,4,6,7},{4,3,2,1,5,6,7}};

      /*
      //eight sequences
      int allsequences[][] = new int[][]{{6, 4, 2, 1, 3, 5, 7}, {6, 4, 3, 1, 2, 5, 7},{6, 5, 3, 1, 2, 4, 7},
                                         {6,5,3,2,1,4,7},{7,5,4,2,1,3,6},{7,6,4,2,1,3,5},
                                         {7,6,5,3,1,2,4},{7,6,5,4,1,2,3}};
      */
      int tempSeq[] = allsequences[3];
      //to minus one
      for(int i = 0 ; i < tempSeq.length ; i ++ ){
        tempSeq[i] -= 1;
      }
      double tempObj2 = singleMachineLearningEffect1.calcEarlinessAndTardiness(tempSeq);
      System.out.print(learningRate+"\t"+tempObj2+"\n");
      learningRate += 0.05;
    }
  }//end main()


}