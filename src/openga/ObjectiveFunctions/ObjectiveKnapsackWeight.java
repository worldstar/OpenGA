package openga.ObjectiveFunctions;
import openga.chromosomes.*;
import openga.operator.repair.*;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class ObjectiveKnapsackWeight implements ObjectiveFunctionKnapsackI{
  public ObjectiveKnapsackWeight(){
  }

  populationI population, repiaredPopulation;
  int popSize;
  int indexOfObjective;
  int length;
  //Knapsack problem.
  double profit[][];
  double weight[][];           //the weight for each item.

  //to check the infeasible solution
  weightedRepairI weightedScalarRepair1;

  public void setKnapsackData(double profit[], double weight[][], weightedRepairI weightedScalarRepair1){
    System.out.println("The method setKnapsackData(double profit[], double weight[][], weightedRepairI weightedScalarRepair1) is not supported in ObjectiveKnapsackWeight.");
    System.exit(0);
  }
  
  public void setKnapsackData(double profit[][], double weight[][], weightedRepairI weightedScalarRepair1){
    this.profit = profit;
    this.weight = weight;
    this.weightedScalarRepair1 = weightedScalarRepair1;
  }

  public void setRepairedPopulation(populationI repiaredPopulation){
    this.repiaredPopulation = repiaredPopulation;
  }

  public void setData(populationI population, int indexOfObjective) {
    this.population = population;
    popSize = population.getPopulationSize();
    this.length = population.getLengthOfChromosome();
    this.indexOfObjective = indexOfObjective;
  }

  public void setData(chromosome chromosome1, int indexOfObjective){
    System.out.println("The setData(chromosome chromosome1) doesn't be implements.");
    System.out.println("The program is exit.");
    System.exit(0);
  }

  public void calcObjective() {
    double obj;
    double objectives[];
    openga.util.printClass printClass1 = new openga.util.printClass();

    for(int i = 0 ; i < popSize ; i ++ ){
      //printClass1.printMatrix("population", population.getSingleChromosome(i).getSolution());
      objectives = population.getSingleChromosome(i).getObjValue();
      obj = evaluateWeight(repiaredPopulation.getSingleChromosome(i));
      objectives[indexOfObjective] = obj;
      //System.out.println("obj "+ obj);
      population.setObjectiveValue(i, objectives);
    }
  }

  private double evaluateWeight(chromosome chromosomes1){
    double value = 0;
    for(int i = 0 ; i < length ; i ++ ){
      if(chromosomes1.genes[i] == 1){//the 1 is true where the item is in the knapsack.
        for(int j = 0 ; j < weight.length ; j ++ ){
          value += weight[j][i];
        }
      }
    }
    return value;
  }

  public populationI getPopulation() {
    return population;
  }

  public weightedRepairI getWeightedScalarRepair(){
    return weightedScalarRepair1;
  }

  public double[] getObjectiveValues(int index) {
    return population.getObjectiveValues(index);
  }
/*
  public static void main(String[] args) {
    //System.out.println("start");
    openga.util.printClass printClass1 = new openga.util.printClass();
    java.util.Random r1 = new java.util.Random(555);
    int numberOfKnapsack = 2, size = 5;
    double profit[][] = new double[numberOfKnapsack][size];
    double weight[][] = new double[numberOfKnapsack][size];

    for(int i = 0 ; i < numberOfKnapsack ; i ++ ){
      for(int j = 0 ; j < size ; j ++ ){
        profit[i][j] = r1.nextInt(10);
        weight[i][j] = r1.nextInt(10);
      }
    }

    printClass1.printMatrix("weight", weight);

    populationI population1 = new population();
    populationI newPop = new population();
    int popSize = 1, length = 5;
    population1.setGenotypeSizeAndLength(false, popSize, length, 2);
    population1.createNewPop();
    for(int i = 0 ; i < popSize ; i ++ ){
      //printClass1.printMatrix(""+i, population1.getSingleChromosome(i).getSolution());
    }

    ObjectiveKnapsackWeight ObjectiveKnapsackWeight1 = new ObjectiveKnapsackWeight();
    ObjectiveKnapsackWeight1.setData(population1, 0);
    ObjectiveKnapsackWeight1.setKnapsackData(profit, weight);
    ObjectiveKnapsackWeight1.calcObjective();
  }
*/
}