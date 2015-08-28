/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package openga.ObjectiveFunctions;
import openga.chromosomes.*;
import openga.operator.repair.*;
/**
 *
 * @author user
 */
public class ObjectiveKnapsackProfitSingleObjective implements ObjectiveFunctionKnapsackI{

  populationI population, repiaredPopulation;
  int popSize;
  int indexOfObjective;
  int length;
  //Knapsack problem.
  double profit[];
  double weight[][];           //the weight for each item.
  //to check the infeasible solution
  weightedRepairI weightedScalarRepair1;

  public void setKnapsackData(double profit[], double weight[][], weightedRepairI weightedScalarRepair1){
    this.profit = profit;
    this.weight = weight;
    this.weightedScalarRepair1 = weightedScalarRepair1;
  }

  public void setKnapsackData(double profit[][], double weight[][], weightedRepairI weightedScalarRepair1){
    System.out.println("The setKnapsackData(double profit[][], double weight[][], weightedRepairI weightedScalarRepair1) is not supported.");
    System.exit(0);
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
    System.out.println("The setData(chromosome chromosome1) doesn't be supported.");
    System.exit(0);
  }

  public void calcObjective() {
    double obj;
    double objectives[];
    
    //Added in 2010/11/02
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
        value += profit[i];
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
}
