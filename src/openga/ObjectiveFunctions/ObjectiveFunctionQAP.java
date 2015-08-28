package openga.ObjectiveFunctions;
import openga.chromosomes.*;

/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: To calculate the QAP objective. </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class ObjectiveFunctionQAP implements ObjectiveFunctionQAPI {
  public ObjectiveFunctionQAP() {
  }

  populationI population;
  int[][] flow, distance;
  int length, indexOfObjective;
  double objectiveValue = 0;


  public void setQAPData(int[][] flow, int[][] distance) {
    this.flow = flow;
    this.distance = distance;
  }

  public void setData(populationI population, int indexOfObjective) {
    this.population = population;
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

    for(int i = 0 ; i < population.getPopulationSize() ; i ++ ){
      objectives = population.getObjectiveValues(i);
      obj = evaluateAll(population.getSingleChromosome(i));
      objectives[indexOfObjective] = obj;
      population.setObjectiveValue(i, objectives);
    }
  }

  public double evaluateAll(chromosome _chromosome1){
    double obj = 0;
    for(int i = 0 ; i < length - 1 ; i ++ ){//from point i
      for(int j = i+1 ; j < length  ; j ++ ){//to point j
        obj += objectiveValueBetweenTwoDepartments(i, j, _chromosome1); //resultantMatrix
      }
    }

    return obj*2;
  }

  //to calculate the flow from a department to others departments.
  /**
   * @param X The array position
   * @return
   */
  public double objectiveValueBetweenTwoDepartments(int X, int Y, chromosome _chromosome1){
    int index1 = _chromosome1.getSolution()[X];
    int index2 = _chromosome1.getSolution()[Y];

    return flow[index1][index2]*distance[X][Y];//resultantMatrix[i][j]
  }


  public populationI getPopulation() {
    return population;
  }

  public double[] getObjectiveValues(int index) {
    return population.getObjectiveValues(index);
  }

}