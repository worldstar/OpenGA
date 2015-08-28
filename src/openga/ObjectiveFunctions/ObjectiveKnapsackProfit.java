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

public class ObjectiveKnapsackProfit implements ObjectiveFunctionKnapsackI{
  public ObjectiveKnapsackProfit() {
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
    System.out.println("The method setKnapsackData(double profit[], double weight[][], weightedRepairI weightedScalarRepair1) is not supported in ObjectiveKnapsackProfit.");
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
    
    //Added in 2010/11/01
    weightedScalarRepair1.setData(population);
    weightedScalarRepair1.startRepair();
    population = weightedScalarRepair1.getPopulation();
    //End Added in 2010/11/01
    
    for(int i = 0 ; i < popSize ; i ++ ){
      objectives = population.getSingleChromosome(i).getObjValue();
      obj = evaluateProfit(population.getSingleChromosome(i));
      objectives[indexOfObjective] = obj;
      population.setObjectiveValue(i, objectives);
    }
  }

  private double evaluateProfit(chromosome chromosomes1){
    double value = 0;
    for(int i = 0 ; i < length ; i ++ ){
      if(chromosomes1.genes[i] == 1){//the 1 is true where the item is in the knapsack.
        value += profit[indexOfObjective][i];
      }
    }

    return value;
  }

  public populationI getPopulation() {
    return population;
  }

  public double[] getObjectiveValues(int index) {
    return population.getObjectiveValues(index);
  }

  public weightedRepairI getWeightedScalarRepair(){
    return weightedScalarRepair1;
  }

  public static void main(String[] args) {
    //System.out.println("start");
    openga.util.printClass printClass1 = new openga.util.printClass();
    java.util.Random r1 = new java.util.Random(555);
    int numberOfKnapsack = 2, size = 5;
    double profit[][] = new double[numberOfKnapsack][size];
    double weight[][] = new double[numberOfKnapsack][size];
    double capacityLimit[] = new double[]{20, 20};
    weightedScalarRepair weightedScalarRepair1 = new weightedScalarRepair();


    for(int i = 0 ; i < numberOfKnapsack ; i ++ ){
      for(int j = 0 ; j < size ; j ++ ){
        profit[i][j] = r1.nextInt(10);
        weight[i][j] = r1.nextInt(10);
      }
    }
    printClass1.printMatrix("profit", profit);
    printClass1.printMatrix("weight", weight);

    populationI population1 = new population();
    populationI newPop = new population();
    int popSize = 1;
    population1.setGenotypeSizeAndLength(false, popSize, size, 2);
    population1.createNewPop();
    for(int i = 0 ; i < popSize ; i ++ ){
      printClass1.printMatrix(""+i, population1.getSingleChromosome(i).getSolution());
    }
    weightedScalarRepair1.setData(numberOfKnapsack, profit, weight, capacityLimit);
    ObjectiveKnapsackProfit ObjectiveKnapsackProfit1 = new ObjectiveKnapsackProfit();
    ObjectiveKnapsackProfit1.setData(population1, 0);
    ObjectiveKnapsackProfit1.setKnapsackData(profit, weight, weightedScalarRepair1);
    ObjectiveKnapsackProfit1.calcObjective();

    ObjectiveKnapsackProfit1.setData(population1, 1);
    ObjectiveKnapsackProfit1.setKnapsackData(profit, weight, weightedScalarRepair1);
    ObjectiveKnapsackProfit1.calcObjective();

    for(int i = 0 ; i < popSize ; i ++ ){
      printClass1.printMatrix(""+i+" obj", population1.getSingleChromosome(i).getObjValue());
    }

  }
}