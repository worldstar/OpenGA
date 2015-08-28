package homework.schedule;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class singleMachineFlowTimeVariation {
  public singleMachineFlowTimeVariation() {
  }

  public int numberOfJobs = 7;
  public int processingTime[];
  public double finishTime[];
  public int sequence[];
  /* Objs in this research */
  public double totalCompletionTime;
  public double TADC;//the total absolute differeces in completion times.
  public boolean withLearning = false;
  public double learningRate = -0.152;//-0.515, -0.322, -0.152
  public double delta = 0;//the weight combinations of two objectives.
  public double posWeight[];
  public int weightRank[];
  public int processingTimeRank[];

  public void setData(int numberOfJobs, int processingTime[], int sequence[]){
    this.numberOfJobs = numberOfJobs;
    this.processingTime = processingTime;
    this.sequence = sequence;
    finishTime = new double[numberOfJobs];
  }

  public void setLearningRate(boolean withLearning, double learningRate){
    this.withLearning = withLearning;
    this.learningRate = learningRate;
    //System.out.println(learningRate);
  }

  public void startCalc(){
    calcFinishTime(sequence);
    totalCompletionTime = calcTC(sequence);
    TADC = calcTADC(sequence);
  }

  /**
   * The finish time of each job.
   * @param _seq
   */
  public void calcFinishTime(int _seq[]){
    double currentTime = 0;
    for(int i = 0 ; i < _seq.length ; i ++ ){
      if(withLearning){
        finishTime[_seq[i]] = currentTime + processingTime[_seq[i]]*Math.pow((i+1), learningRate);
      }
      else{
        finishTime[_seq[i]] = currentTime + processingTime[_seq[i]];
      }
      currentTime = finishTime[_seq[i]];
    }
  }

  public double calcTC(int _seq[]){
    double currentTime = 0;
    for(int i = 0 ; i < _seq.length ; i ++ ){
      currentTime += finishTime[_seq[i]];
    }
    return currentTime;
  }


  public double calcTADC(int _seq[]){
    double currentTime = 0;
    for(int i = 0 ; i < _seq.length ; i ++ ){
      for(int j = i ; j < _seq.length ; j ++ ){
        currentTime += Math.abs(finishTime[_seq[j]] - finishTime[_seq[i]]);
      }
    }
    return currentTime;
  }

  public double getTC(){
    return totalCompletionTime;
  }

  public double getTADC(){
    return TADC;
  }

  public final void printSequence(){
    for(int i = 0 ; i < numberOfJobs ; i ++ ){
      System.out.print((sequence[i]+1)+" ");
    }
  }

  public static void main(String[] args) {
    int numberOfJobs = 4;
    int length = numberOfJobs;
    int processingTime[] = new int[]{1, 2, 3, 4};//2, 3, 6, 9, 21, 65, 82 //1, 2, 3, 4 //1, 3, 5, 7
    int finishTime[];
    int sequence[];
    int[] indices;

    singleMachineFlowTimeVariation singleMachineFlowTimeVariation1 = new singleMachineFlowTimeVariation();

    PermutationGenerator x = new PermutationGenerator (length);
    //StringBuffer permutation;
    int counter = 0;
    while (x.hasMore ()){
      //permutation = new StringBuffer ();
      indices = x.getNext ();
      singleMachineFlowTimeVariation1.setData(numberOfJobs, processingTime, indices);
      singleMachineFlowTimeVariation1.startCalc();
      singleMachineFlowTimeVariation1.printSequence();
      System.out.print("\t"+singleMachineFlowTimeVariation1.getTC()+"\t"+singleMachineFlowTimeVariation1.getTADC()+"\n");
      counter ++;
    }//end while
  }

}