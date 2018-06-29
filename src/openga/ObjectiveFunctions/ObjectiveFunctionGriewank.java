package openga.ObjectiveFunctions;

import openga.chromosomes.*;

/**
 * <p>
 * Title: The OpenGA project which is to build general framework of Genetic
 * algorithm.</p>
 * <p>
 * Description: </p>
 * <p>
 * Copyright: Copyright (c) 2005</p>
 * <p>
 * Company: Yuan-Ze University</p>
 *
 * @author Chen, Shih-Hsin
 * @version 1.0
 */
public class ObjectiveFunctionGriewank implements ObjectiveFunctionI {

  public ObjectiveFunctionGriewank() {
  }
  populationI population;
  int[][] flow, distance;
  int length, indexOfObjective;
  double objectiveValue = 0;
  int popSize = 0;

  public void setData(populationI population, int indexOfObjective) {
    this.population = population;
    this.indexOfObjective = indexOfObjective;
    popSize = population.getPopulationSize(); //300
  }

  public void setData(chromosome chromosome1, int indexOfObjective) {
    System.out.println("The setData(chromosome chromosome1) doesn't be implements.");
    System.out.println("The program is exit.");
    System.exit(0);
  }

  public void calcObjective() {
    //first time to evaluate objective value, it needs to evaluate all combinations.
    double obj;
    double objectives[];

    for (int i = 0; i < population.getPopulationSize(); i++) { //300
      objectives = population.getObjectiveValues(i);
      obj = evaluateAll(population.getSingleChromosome(i)); //evaluateAll(chromosome chromosome1)
      objectives[indexOfObjective] = obj;
      population.setObjectiveValue(i, objectives);
    }
  }

  /*Himmelblau function改換成GRIEWANK Function求極小化問題*/
 /*x區間[-600,600]*/
  public double evaluateAll(chromosome chromosome1) {
    double obj = 0;
    double term1 = 0;
    double term2 = 1;

    for (int i = 0; i < 2; i++) { //Dimension
      term1 += Math.pow(chromosome1.realGenes[i], 2) / 4000;
      term2 *= Math.cos(chromosome1.realGenes[i] / Math.sqrt(i + 1));
      obj = term1 - term2 + 1;
    }
    return obj;
  }

  public populationI getPopulation() {
    return population;
  }

  public double[] getObjectiveValues(int index) {
    /**
     * @todo Implement this openga.ObjectiveFunctions.ObjectiveFunctionI method
     */
    throw new java.lang.UnsupportedOperationException("Method getObjectiveValues() not yet implemented.");
  }

}
