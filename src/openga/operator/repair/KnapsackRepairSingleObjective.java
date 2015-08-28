/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package openga.operator.repair;
import openga.chromosomes.*;
/**
 *
 * @author user
 */
public class KnapsackRepairSingleObjective implements weightedRepairI{
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
  double profit[], itemWeight[][];
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

  public void setData(int numKnapsack, double profit[], double itemWeight[][], double capacityLimit[]){
    this.numKnapsack = numKnapsack;
    this.profit = profit;
    this.itemWeight = itemWeight;
    this.capacityLimit = capacityLimit;
  }
  
  public void setData(int numKnapsack, double profit[][], double itemWeight[][], double capacityLimit[]){
    System.out.println("The method setData(int numKnapsack, double profit[][], double itemWeight[][], double capacityLimit[]) is not supported in KnapsackRepairSingleObjective.");
    System.exit(0);
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
    for(int i = 0 ; i < numKnapsack ; i ++ ){
      for(int j = 0; j < length ; j ++ ){
        if(chromosomes1.genes[j] == 1){//the 1 is true where the item is in the knapsack.
          eachKnapsackWeight[i] += itemWeight[i][j];
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
          sumProfit += profit[i];
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
}
