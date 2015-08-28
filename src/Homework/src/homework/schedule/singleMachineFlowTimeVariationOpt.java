package homework.schedule;
import homework.util.sort.selectionSort;
/**
 * <p>Title: </p>
 * <p>Description: The sorting and matching method based on position weight and processing time</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class singleMachineFlowTimeVariationOpt extends singleMachineFlowTimeVariation{
  public singleMachineFlowTimeVariationOpt() {
  }
  int numberOfJobs = 7;
  int processingTime[];
  double finishTime[];
  int sequence[];
  /* Objs in this research */
  double totalCompletionTime;
  double TADC;//the total absolute differeces in completion times.
  boolean withLearning = false;
  double learningRate = -0.152;//-0.515, -0.322, -0.152
  double delta = 0;//the weight combinations of two objectives.
  double posWeight[];
  int weightRank[];
  int processingTimeRank[];

  public void setData(double learningRate, double delta){
    if(learningRate != 0){
      withLearning = true;
    }
    this.learningRate = learningRate;
    this.delta = delta;
  }

  public void setData(int numberOfJobs, int processingTime[], int sequence[]){
    this.numberOfJobs = numberOfJobs;
    this.processingTime = processingTime;
    this.sequence = sequence;
    finishTime = new double[numberOfJobs];
  }

  public void algorithmMain(){
    calcweightRank();
    sortWeightRank();
    sortProcessingTimeRank();
    match();
    startCalc();
  }

  public void startCalc(){//to carray out the objective function calculations.
    calcFinishTime(sequence);
    totalCompletionTime = calcTC(sequence);
    TADC = calcTADC(sequence);
  }


  public void calcweightRank(){
    posWeight = new double[numberOfJobs];//position Weight
    for(int i = 0 ; i < numberOfJobs ; i ++ ){
      int r = i + 1;//acutal pos.
      posWeight[i] = (2*delta - 1)*(numberOfJobs + 1) +
          (r)*(2 - 3*delta + numberOfJobs*(1 - delta))- Math.pow(r, 2)*(1 - delta);
      posWeight[i] = posWeight[i]*Math.pow(r, learningRate);//learningRate is alhpa in the paper.
      //System.out.print(posWeight[i]+"   ");
    }
  }

  public void sortWeightRank(){
    selectionSort selectionSort1 = new selectionSort();
    weightRank = new int[numberOfJobs];
    int tempIndex[] = new int[numberOfJobs];
    for(int i = 0 ; i < numberOfJobs ; i ++ ){
      tempIndex[i] = i;
    }

    //Sorting the position weight.
    selectionSort1.setData(posWeight);
    selectionSort1.setNomialData(tempIndex);
    selectionSort1.selectionSort_withNomial();

    //to assign the rank-i of each position.
    for(int i = 0 ; i < numberOfJobs ; i ++ ){
      weightRank[selectionSort1.getNomialData()[i]] = numberOfJobs - 1 - i;
    }
  }

  public void sortProcessingTimeRank(){
    selectionSort selectionSort1 = new selectionSort();
    processingTimeRank = new int[numberOfJobs];
    int tempIndex[] = new int[numberOfJobs];
    for(int i = 0 ; i < numberOfJobs ; i ++ ){
      processingTimeRank[i] = i;
    }

    //Sorting the position weight.
    selectionSort1.setData(processingTime);
    selectionSort1.setNomialData(processingTimeRank);
    selectionSort1.selectionSort_int_withNomial();
    selectionSort1.reverseNomialOrder();//the procrssing time rank in decending order of ith job.
  }

  public void match(){//the largest processing time to smallest positional weight
    for(int i = 0 ; i < numberOfJobs ; i ++ ){
      sequence[i] = processingTimeRank[weightRank[i]];
      System.out.print((sequence[i]+1)+"   ");
    }
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

  public double getTOTALCost(){
    return delta*totalCompletionTime + (1 - delta)*TADC;
  }


  public static void main(String[] args) {
    int numberOfJobs = 4;
    int length = numberOfJobs;
    int processingTime[] = new int[]{1, 2, 3, 4};//2, 3, 6, 9, 21, 65, 82 //1, 2, 3, 4 //1, 3, 5, 7
    int finishTime[];
    int sequence[];
    int[] indices;
    boolean withLearning = true;
    double learningRate = -0.152;//-0.515, -0.322, -0.152
    double delta = 0.05;//the weight combinations of two objectives.

    while(delta < 1){
      singleMachineFlowTimeVariationOpt singleMachineFlowTimeVariationOpt1 = new singleMachineFlowTimeVariationOpt();
      sequence = new int[]{3,0,1,2};
      singleMachineFlowTimeVariationOpt1.setData(numberOfJobs, processingTime, sequence);
      singleMachineFlowTimeVariationOpt1.setData(learningRate, delta);
      //singleMachineFlowTimeVariationOpt1.algorithmMain();
      //System.out.println("Obs");
      singleMachineFlowTimeVariationOpt1.startCalc();
      System.out.print(delta+"\t"+(1 - delta) +"\t"+singleMachineFlowTimeVariationOpt1.getTC()+"\t"+singleMachineFlowTimeVariationOpt1.getTADC()+"\t"+singleMachineFlowTimeVariationOpt1.getTOTALCost()+"\n");
      delta += 0.01;
    }

    System.exit(0);
    while(delta < 1){
      singleMachineFlowTimeVariationOpt singleMachineFlowTimeVariationOpt1 = new singleMachineFlowTimeVariationOpt();
      sequence = new int[numberOfJobs];
      singleMachineFlowTimeVariationOpt1.setData(numberOfJobs, processingTime, sequence);
      singleMachineFlowTimeVariationOpt1.setData(learningRate, delta);
      singleMachineFlowTimeVariationOpt1.algorithmMain();
      //System.out.println("Obs");
      System.out.print("\t"+ delta +"\t"+singleMachineFlowTimeVariationOpt1.getTC()+"\t"+singleMachineFlowTimeVariationOpt1.getTADC()+"\n");
      delta += 0.01;
    }
  }

}