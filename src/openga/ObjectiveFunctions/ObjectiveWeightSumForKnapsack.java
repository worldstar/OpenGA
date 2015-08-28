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

public class ObjectiveWeightSumForKnapsack implements weightedSumObjectives, ObjectiveFunctionKnapsackI{
  public ObjectiveWeightSumForKnapsack() {
  }

  populationI originalPop;
  int indexOfObjective;
  int popSize;

  double weights[];          //the weight of each objectives.
  //the objective classes
  ObjectiveFunctionI ObjectiveFunction[];

  //Knapsack problem.
  double profit[][];
  double weight[][];           //the weight for each item.
  weightedRepairI weightedScalarRepair1;

  public void setData(populationI population, int indexOfObjective){
    this.originalPop = population;
    this.indexOfObjective = indexOfObjective;
    popSize = population.getPopulationSize();
  }

  public void setData(chromosome chromosome1, int indexOfObjective){
    System.out.println("The setData(chromosome chromosome1) doesn't be implements.");
    System.out.println("The program is exit.");
    System.exit(0);
  }

  public void setKnapsackData(double profit[], double weight[][], weightedRepairI weightedScalarRepair1){
    System.out.println("The setKnapsackData(double profit[], double weight[][], weightedRepairI weightedScalarRepair1) is not supported.");
    System.exit(0);
  }

  public void setKnapsackData(double profit[][], double weight[][], weightedRepairI weightedScalarRepair1){
    System.out.println("The setKnapsackData(double profit[][], double weight[][], weightedRepairI weightedScalarRepair1) is not supported.");
    System.exit(0);
  }

  public void setRepairedPopulation(populationI repiaredPopulation){
    System.out.println("We should create a repaired solution in the ObjectiveWeightSumForKnapsack class");
    System.exit(0);
  }

  public void setObjectiveClass(ObjectiveFunctionI ObjectiveFunction[]){
    this.ObjectiveFunction = ObjectiveFunction;
  }

  public void setWeightOfObjectives(double weights[]){
    this.weights = weights;
  }

  public void setKnapsackData(double profit[][], double weight[][], weightedScalarRepair weightedScalarRepair1){
    this.profit = profit;
    this.weight = weight;
    this.weightedScalarRepair1 = weightedScalarRepair1;
  }

  public void calcObjective(){
    weightedScalarRepair1 = ((ObjectiveFunctionKnapsackI)ObjectiveFunction[0]).getWeightedScalarRepair();
    weightedScalarRepair1.setData(originalPop);
    weightedScalarRepair1.startRepair();
    populationI repiaredPopulation = weightedScalarRepair1.getPopulation();

    //evaluate each objective function
    for(int i = 0 ; i < ObjectiveFunction.length ; i ++ ){
      //System.out.println("The obj "+i);
      ObjectiveFunction[i].setData(originalPop, i);
      ((ObjectiveFunctionKnapsackI)ObjectiveFunction[i]).setRepairedPopulation(repiaredPopulation);
      ObjectiveFunction[i].calcObjective();
      originalPop = ObjectiveFunction[i].getPopulation();
    }
/*
    //to implement the partial Larmarkian
    for(int i = 0 ; i < repiaredPopulation.getPopulationSize() ; i ++ ){
      if(Math.random() < 0.05){
        originalPop.setSingleChromosome(i, repiaredPopulation.getSingleChromosome(i));
      }
    }
*/
    //to get the objective values.
    int _length = popSize;
    double _values[][] = new double[ObjectiveFunction.length][_length];
    double _normObjectives[][] = new double[ObjectiveFunction.length][_length];

    //get the original objective values
    for(int i = 0 ; i < ObjectiveFunction.length ; i ++ ){
      for(int j = 0 ; j < popSize ; j ++ ){
        _values[i][j] = originalPop.getObjectiveValues(j)[i];
      }
    }
    //printClass1.printMatrix("_values",_values);

    //normalize data of each objective value
    openga.util.algorithm.normalize normalize1 = new openga.util.algorithm.normalize();
    _normObjectives = normalize1.getData2EachRow(_values);
    //suppose the first objective is the profit
    //printClass1.printMatrix("_normObjectives",_normObjectives);
    _normObjectives = transformObjValue(_normObjectives);

    //set the result to each chromosome.
    for(int j = 0 ; j < popSize ; j ++ ){
      double tempValue[] = new double[ObjectiveFunction.length];
      double scalarizedValue = 0;
      for(int k = 0 ; k < ObjectiveFunction.length ; k ++ ){
        tempValue[k] = _normObjectives[k][j];
        scalarizedValue += tempValue[k]*weights[k];
      }
      originalPop.setNormalizedObjValue(j, tempValue);
      originalPop.setScalarizedObjectiveValue(j, scalarizedValue);

      //System.out.println();
      //printClass1.printMatrix("_normObjectives 2",_normObjectives);
    }
  }

  /**
   * The selector is the minization one, we transform the objective value.
   * @param _normObjectives
   * @return
   */
  private double[][] transformObjValue(double _normObjectives[][]){
    for(int i = 0 ; i < originalPop.getPopulationSize() ; i ++ ){
      for(int j = 0 ; j < ObjectiveFunction.length ; j ++ ){
        _normObjectives[j][i] = 1 -_normObjectives[j][i];
      }
    }
    return _normObjectives;
  }

  public populationI getPopulation(){
    return originalPop;
  }

  public weightedRepairI getWeightedScalarRepair(){
    return weightedScalarRepair1;
  }

  public double[] getObjectiveValues(int index){
    return originalPop.getObjectiveValues(index);
  }

}