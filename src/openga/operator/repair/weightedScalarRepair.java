package openga.operator.repair;
import openga.chromosomes.*;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: For multi-objective knapsack problem.</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class weightedScalarRepair implements weightedRepairI{
  public weightedScalarRepair() {
  }
  /**
   * Solution information.
   */
  populationI originalPop, newPop;
  //chromosome chromosomes1;
  double weights[];
  double ratio[];
  int seq[];
  openga.util.sort.selectionSort sort1 = new openga.util.sort.selectionSort();
  //openga.util.printClass printClass1 = new openga.util.printClass();
  int counterSequence = 0;
  int popSize = 0;

  /**
   * The knapsack information.
   */
  double profit[][], itemWeight[][];
  double capacityLimit[];
  int numKnapsack;
  double eachKnapsackWeight[];
  int length;

  public void setData(double weights[]){
    this.weights = weights;
  }

  public void setData(populationI originalPop){
    this.originalPop = originalPop;
    popSize = originalPop.getPopulationSize();
    //System.out.println("originalPop.getLengthOfChromosome() "+originalPop.getLengthOfChromosome());
    newPop = new population();
    newPop.setGenotypeSizeAndLength(originalPop.getEncodedType(), popSize, originalPop.getLengthOfChromosome(), originalPop.getNumberOfObjectives());
    newPop.initNewPop();

    for(int i = 0 ; i < popSize ; i ++ ){
      newPop.setSingleChromosome(i, originalPop.getSingleChromosome(i));
    }
    //length = originalPop.getSingleChromosome(0).genes.length;
    length = originalPop.getLengthOfChromosome();
  }
/*
  public void setData(chromosome _chromosomes1){
    chromosomes1 = new chromosome();
    chromosomes1.setGenotypeAndLength(_chromosomes1.getEncodeType(), _chromosomes1.getLength(), _chromosomes1.getObjValue().length);
    chromosomes1.initChromosome();
    chromosomes1.setSolution(_chromosomes1.getSolution());
    length = _chromosomes1.getLength();

  }
*/
  public void setData(int numKnapsack, double profit[], double itemWeight[][], double capacityLimit[]){
    System.out.println("The method setData(int numKnapsack, double profit[], double itemWeight[][], double capacityLimit[]) is not supported in weightedScalarRepair.");
    System.exit(0);
  }
  
  public void setData(int numKnapsack, double profit[][], double itemWeight[][], double capacityLimit[]){
    this.numKnapsack = numKnapsack;
    this.profit = profit;
    this.itemWeight = itemWeight;
    this.capacityLimit = capacityLimit;
  }

  public void startRepair(){
    for(int i = 0 ; i < popSize ; i ++ ){
      ratio = new double[length];
      calcRatio(newPop.getSingleChromosome(i));
      seq = sortRatio();
      eachKnapsackWeight = new double[numKnapsack];
      calcEachKnapsackWeight(newPop.getSingleChromosome(i));
      if(checkInfeasibleWeight()){//if true, repair the solution.
        repaireSolution(newPop.getSingleChromosome(i));
      }
      addPhase(newPop.getSingleChromosome(i));
    }
  }

  public final void calcEachKnapsackWeight(chromosome chromosomes1){
    for(int i = 0 ; i < length ; i ++ ){
      for(int j = 0; j < numKnapsack ; j ++ ){
        if(chromosomes1.genes[i] == 1){//the 1 is true where the item is in the knapsack.
          eachKnapsackWeight[j] += itemWeight[j][i];
        }
      }
    }
  }

  public final boolean checkInfeasibleWeight(){
    boolean infeasible = false;
    for(int i = 0 ; i < numKnapsack ; i ++ ){
      if(eachKnapsackWeight[i] > capacityLimit[i]){
        return true;
      }
    }
    return infeasible;
  }

  public void repaireSolution(chromosome chromosomes1){

    int counter = 0;

    do{
      if(chromosomes1.genes[seq[counter]] == 1){//the 1 is true where the item is in the knapsack.
        chromosomes1.setGeneValue(seq[counter], 0);
        updateKnapsackInformation(seq[counter]);
      }
      counter ++;
    }
    while(checkInfeasibleWeight());
  }

  private void calcRatio(chromosome chromosomes1){
    double sumWeight = 0, sumProfit = 0;
    //double temp = Math.random();
    //weights = new double[]{temp, 1 - temp};

    for(int i = 0 ; i < length ; i ++ ){
      for(int j = 0; j < numKnapsack ; j ++ ){
        if(chromosomes1.genes[i] == 1){//the 1 is true where the item is in the knapsack.          
          sumProfit += weights[j]*profit[j][i];
          sumWeight += itemWeight[j][i];
        }
      }
      ratio[i] = sumProfit/sumWeight;
    }
  }

  public void updateKnapsackInformation(int itemIndex){
    //to update the total weight
    for(int i = 0 ; i < numKnapsack ; i ++ ){
      eachKnapsackWeight[i] -= itemWeight[i][itemIndex];
    }
  }

  public void addPhase(chromosome chromosomes1){
    int pos = length - 1;
    int counter = 0;
    //printClass1.printMatrix("seq",seq);
    while(pos > 1){//
      if(chromosomes1.genes[seq[pos]] == 0 && checkWeight(seq[pos])){
        chromosomes1.setGeneValue(seq[pos], 1);
        updateKnapsackInformationAfterAddItem(seq[pos]);
        counter ++;
      }
      pos --;
    }
  }

  public boolean checkWeight(int itemIndex){
    boolean feasible = true;
    for(int i = 0 ; i < numKnapsack ; i ++ ){
      if(eachKnapsackWeight[i] + itemWeight[i][itemIndex]  > capacityLimit[i]){
        return false;
      }
    }
    return feasible;
  }

  public void updateKnapsackInformationAfterAddItem(int itemIndex){
    //to update the total weight
    for(int i = 0 ; i < numKnapsack ; i ++ ){
      eachKnapsackWeight[i] += itemWeight[i][itemIndex];
    }
  }

  private int[] sortRatio(){
    int index[] = new int[length];
    for(int i = 0 ; i < length ; i ++ ){
      index[i] = i;
    }
    sort1.setData(ratio);
    sort1.setNomialData(index);
    sort1.selectionSort_withNomial();
    return sort1.getNomialData();
  }

  /*
    public chromosome getResult(){
      return chromosomes1;
    }
  */
    public populationI getPopulation(){
      return newPop;
    }

  public static void main(String[] args) {
    //System.out.println("start");
    openga.util.printClass printClass1 = new openga.util.printClass();
    java.util.Random r1 = new java.util.Random(283451);
    int numberOfKnapsack = 2, size = 7;
    double profit[][] = new double[numberOfKnapsack][size];
    double weight[][] = new double[numberOfKnapsack][size];
    double weights[] = new double[]{0.2, 0.8};
    double capacityLimit[] = new double[]{15, 8};

    for(int i = 0 ; i < numberOfKnapsack ; i ++ ){
      for(int j = 0 ; j < size ; j ++ ){
        profit[i][j] = r1.nextInt(15);
        weight[i][j] = r1.nextInt(8);
      }
    }

    printClass1.printMatrix("weight", weight);

    populationI population1 = new population();
    populationI newPop = new population();
    int popSize = 1;
    population1.setGenotypeSizeAndLength(false, popSize, size, 2);
    population1.createNewPop();
    for(int i = 0 ; i < popSize ; i ++ ){
      printClass1.printMatrix(""+i, population1.getSingleChromosome(i).getSolution());
    }

    weightedScalarRepair weightedScalarRepair1 = new weightedScalarRepair();
    weightedScalarRepair1.setData(weights);
    weightedScalarRepair1.setData(population1);
/*
    openga.ObjectiveFunctions.ObjectiveKnapsackWeight ObjectiveKnapsackWeight1 = new openga.ObjectiveFunctions.ObjectiveKnapsackWeight();
    ObjectiveKnapsackWeight1.setData(population1, 0);
    ObjectiveKnapsackWeight1.setKnapsackData(profit, weight, weightedScalarRepair1);
    ObjectiveKnapsackWeight1.calcObjective();
    System.out.println();

    //weightedScalarRepair1
    weightedScalarRepair1.setData(numberOfKnapsack, profit, weight, capacityLimit);
    weightedScalarRepair1.startRepair();

    for(int i = 0 ; i < popSize ; i ++ ){
      printClass1.printMatrix(""+i, population1.getSingleChromosome(i).getSolution());
    }
    ObjectiveKnapsackWeight1.setData(population1, 0);
    ObjectiveKnapsackWeight1.setKnapsackData(profit, weight, weightedScalarRepair1);
    ObjectiveKnapsackWeight1.calcObjective();
    System.out.println();
*/

  }

}