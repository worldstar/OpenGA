package openga.ObjectiveFunctions;
import openga.chromosomes.*;
import openga.util.algorithm.*;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: For single machine problem.</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class ObjectiveEiTiPenaltyWithLearningEffect extends ObjectiveEarlinessTardinessPenalty {
  public ObjectiveEiTiPenaltyWithLearningEffect() {
  }
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

  private void processingTimeWithLearning(){
    PiLearningMatrix = new double[length][length];
    for(int i = 0 ; i < length ; i ++ ){//job
      for(int pos = 0 ; pos < length ; pos ++ ){//position
        PiLearningMatrix[i][pos] = processingTime[i]*Math.pow((pos+1), learningRate);
      }
    }
  }

  public double calcEarlinessAndTardiness(chromosome chromosome1) {
    double objVal = 0;
    double machineTime = 0;
    double finishTime[] = calcFinishTime(chromosome1.genes);
    //double k_due = calcDueDate(chromosome1.genes);
    //System.out.println("k_due "+k_due);

    //for the Mosheiov (2001) in EJOR
    //objVal = chromosome1.getLength()*5*k_due;

    if(alpha == null){
      for(int i = 0 ; i < length ; i ++ ){
        int jobIndex = chromosome1.getSolution()[i];
        //objVal += Math.abs(finishTime[jobIndex] - k_due);
        objVal += Math.abs(finishTime[jobIndex] - dueDay[jobIndex]);

        /*
        //for the Mosheiov (2001) in EJOR
        if(finishTime[jobIndex] < k_due){
          objVal += 11*Math.abs(finishTime[jobIndex] - k_due);
        }
        else{
          objVal += 18*Math.abs(finishTime[jobIndex] - k_due);
        }
        */
      }
    }
    else{
      for(int i = 0 ; i < length ; i ++ ){
        int jobIndex = chromosome1.getSolution()[i];
        if(finishTime[jobIndex] < dueDay[jobIndex]){
          objVal += alpha[jobIndex]*(dueDay[jobIndex] - finishTime[jobIndex]);
        }
        else{
          objVal += beta[jobIndex]*(finishTime[jobIndex] - dueDay[jobIndex]);
        }
      }
    }
    /*
    for(int i = 0 ; i < length ; i ++ ){//job
      int jobIndex = chromosome1.getSolution()[i];
      //System.out.print((jobIndex+1)+"\t\t"+PiLearningMatrix[jobIndex][i]+"\t"+Math.abs(finishTime[jobIndex] - k_due)+"\n");
    }
    */

    return objVal;
  }
/*
  private double[] calcFinishTime(int _seq[]){
    double finishTime[] = new double[_seq.length];
    double currentTime = 0;

    for(int i = 0 ; i < _seq.length ; i ++ ){
      finishTime[_seq[i]] = currentTime + processingTime[_seq[i]];
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
      k +=  processingTime[_seq[i]];
    }
    return k;
  }
*/
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

  public static void main(String[] args) {
    int numberOfJobs = 7, numberOfMachine = 1;
    int processingTime[] = new int[]{1, 3, 6, 8, 11, 15, 21};
    int sequence[] = new int[]{6, 4, 2, 0, 1, 3, 5};//7641235(6, 5, 3, 0, 1, 2, 4)6 4 2 0 1 3 5
    int dueDate[] = new int[numberOfJobs];
/*
    ObjectiveEiTiPenaltyWithLearningEffect learningEffect1 = new ObjectiveEiTiPenaltyWithLearningEffect();
    learningEffect1.setScheduleData(dueDate, processingTime, 1);
    learningEffect1.length = numberOfJobs;
    learningEffect1.processingTimeWithLearning();

    populationI population1 = new population();
    populationI newPop = new population();
    int popSize = 1;
    population1.setGenotypeSizeAndLength(false, popSize, numberOfJobs, 1);
    population1.createNewPop();
    population1.getSingleChromosome(0).setSolution(sequence);

    double obj = learningEffect1.calcEarlinessAndTardiness(population1.getSingleChromosome(0));
    System.out.println(obj);
*/
/*
    openga.applications.data.singleMachine readSingleMachineData1 = new openga.applications.data.singleMachine();
    readSingleMachineData1.setData("sks/"+"sks222a"+".txt");
    readSingleMachineData1.getDataFromFile();
    dueDate = readSingleMachineData1.getDueDate();
    processingTime = readSingleMachineData1.getPtime();
    double alpha[] = readSingleMachineData1.getAlpha();
    double beta[] = readSingleMachineData1.getBeta();
    sequence = new int[]{4,18,3,12,13,6,1,2,9,16,10,8,7,11,0,14,5,15,17};
    int popSize = 1;
    numberOfJobs = 19;

    ObjectiveEiTiPenaltyWithLearningEffect learningEffect1 = new ObjectiveEiTiPenaltyWithLearningEffect();
    learningEffect1.setScheduleData(dueDate, processingTime, 1);
    learningEffect1.length = numberOfJobs;
    learningEffect1.processingTimeWithLearning();
    learningEffect1.setScheduleData(dueDate, processingTime, 1);
    learningEffect1.length = numberOfJobs;
    learningEffect1.processingTimeWithLearning();

    populationI population1 = new population();
    populationI newPop = new population();
    population1.setGenotypeSizeAndLength(false, popSize, numberOfJobs, 1);
    population1.createNewPop();
    population1.getSingleChromosome(0).setSolution(sequence);
    double obj = learningEffect1.calcEarlinessAndTardiness(population1.getSingleChromosome(0));
    System.out.println(obj);
*/
    int[] indices;
    int length = numberOfJobs;
    int comb[] =new int[length];
    for(int i = 0 ; i < length ; i ++ ){
      comb[i] = i + 1;
    }
    double bestObj = Double.MAX_VALUE;
    int bestSeqeuence[] = new int[length];

    ObjectiveEiTiPenaltyWithLearningEffect learningEffect1 = new ObjectiveEiTiPenaltyWithLearningEffect();
    learningEffect1.setScheduleData(dueDate, processingTime, 1);
    learningEffect1.length = numberOfJobs;
    learningEffect1.processingTimeWithLearning();

    populationI population1 = new population();
    populationI newPop = new population();
    int popSize = 1;
    population1.setGenotypeSizeAndLength(false, popSize, numberOfJobs, 1);
    population1.createNewPop();

    homework.schedule.PermutationGenerator x = new homework.schedule.PermutationGenerator(numberOfJobs);
    //StringBuffer permutation;
    int counter = 0;
    while (x.hasMore ()){
      //permutation = new StringBuffer ();
      indices = x.getNext ();
      population1.getSingleChromosome(0).setSolution(indices);
      double obj = learningEffect1.calcEarlinessAndTardiness(population1.getSingleChromosome(0));
      if(obj < bestObj){
        bestObj = obj;
        for(int i = 0 ; i < length ; i ++ ){
          bestSeqeuence[i] = indices[i];
        }
      }
      counter ++;
    }//end while
    System.out.print("obj "+bestObj+"\nsequence:");

    for (int i = 0; i < bestSeqeuence.length; i++) {
      System.out.print((bestSeqeuence[i]+1)+" ");
    }
    System.out.print("\n");

  }

}