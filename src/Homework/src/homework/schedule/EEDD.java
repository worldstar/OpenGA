package src.homework.schedule;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class EEDD {
  public EEDD() {
  }
  int jobNumber[];
  int processingTime[];
  int due[];
  int jobs = 0;
  double indexes[];
  double weights[];
  int sequence[];
  int MaxTardiness = 0;
  int sumTardiness = 0;
  int flowTime = 0;

  public void setData(int jobNumber[], int processingTime[], int due[], double weights[]){
    this.jobNumber = jobNumber;
    this.processingTime = processingTime;
    this.due = due;
    this.weights = weights;
    jobs = processingTime.length;
  }

  double[] EEDD_index(){
    indexes = new double[jobs];
    for(int i = 0 ; i < jobs ; i++ ){
      indexes[i] = processingTime[i] * weights[0] + due[i] * weights[1];
    }
    return indexes;
  }

  public void sortByEddSequence(){
    openga.util.sort.selectionSort selectionSort1 = new openga.util.sort.selectionSort();
    selectionSort1.setData(EEDD_index());
    selectionSort1.setNomialData(jobNumber);

    selectionSort1.Sort_withNomial();
    sequence = selectionSort1.getNomialData();
  }

  /**
   * The program will evaluate the max tardiness and flow time
   */
  public void evaluateSolution(){
    forScheduleMaxTardinessAndFlowTime forScheduleMaxTardiness1 = new forScheduleMaxTardinessAndFlowTime();
    forScheduleMaxTardiness1.setData(sequence, due, processingTime);
    forScheduleMaxTardiness1.calcObjectives();
    MaxTardiness = forScheduleMaxTardiness1.getMaxTardinessTime();
    flowTime = forScheduleMaxTardiness1.getFlowTime();
    sumTardiness = forScheduleMaxTardiness1.getSumTardiness();
  }

  public int[] getSequenceResult(){
    return sequence;
  }

  public int getFlowTime(){
    return flowTime;
  }

  public int getMaxTardinessTime(){
    return MaxTardiness;
  }

  public int getSumTardiness(){
    return sumTardiness;
  }

  public static void main(String[] args) {
    double weight[]      = new double[]{0,1};
    double deltaValue = 0.1;
    int numberOfCombinations = (int)(Math.abs(weight[0]-weight[1])/deltaValue)+1;
    System.out.println(numberOfCombinations);

    for(int i = 0 ; i < numberOfCombinations ; i ++ ){
      EEDD EEDD1 = new EEDD();
      int processingTime[] = new int[]{3, 2, 5, 4, 6};
      int due[]            = new int[]{13, 8, 9, 7, 10};
      int jobNumber[]      = new int[]{0, 1, 2, 3, 4};

      //get the EEDD index
      EEDD1.setData(jobNumber, processingTime, due, weight);
      EEDD1.sortByEddSequence();
      openga.util.printClass printClass1 = new openga.util.printClass();
      //printClass1.printMatrix("", EEDD1.getSequenceResult());

      for(int j = 0 ; j < EEDD1.getSequenceResult().length ; j ++ ){
        System.out.print((EEDD1.getSequenceResult()[j]+1)+ " ");
      }
      //System.out.print("\n");
      //calculate objective of Tmax and sun flow time
      EEDD1.evaluateSolution();
      System.out.print("\t"+EEDD1.getFlowTime()+"\t"+EEDD1.getSumTardiness()+"\n");
      weight[0] += deltaValue;
      weight[1] -= deltaValue;
    }
  }

}