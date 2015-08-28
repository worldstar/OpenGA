package openga.ObjectiveFunctions;
import openga.chromosomes.*;

/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class ObjectiveFunctionContinuous implements ObjectiveFunctionI {
  public ObjectiveFunctionContinuous() {
  }
  populationI population;
  int[][] flow, distance;
  int length, indexOfObjective;
  double objectiveValue = 0;
  int popSize = 0;

  public void setData(populationI population, int indexOfObjective){
    this.population = population;
    this.indexOfObjective = indexOfObjective;
    popSize = population.getPopulationSize();
  }

  public void setData(chromosome chromosome1, int indexOfObjective){
    System.out.println("The setData(chromosome chromosome1) doesn't be implements.");
    System.out.println("The program is exit.");
    System.exit(0);
  }

  public void calcObjective(){
    //first time to evaluate objective value, it needs to evaluate all combinations.
    double obj;
    double objectives[];

    for(int i = 0 ; i < population.getPopulationSize() ; i ++ ){
      objectives = population.getObjectiveValues(i);
      obj = evaluateAll(population.getSingleChromosome(i));
      objectives[indexOfObjective] = obj;
      population.setObjectiveValue(i, objectives);
    }
  }

  public double evaluateAll(chromosome chromosome1){
    double obj = 0;
    double term1 = Math.pow(Math.pow(chromosome1.realGenes[0], 2)+chromosome1.realGenes[1] - 11, 2);
    double term2 = Math.pow(chromosome1.realGenes[0] + Math.pow(chromosome1.realGenes[1], 2)- 7, 2);
    double term3 = 0.1*(Math.pow(chromosome1.realGenes[0] - 3, 2) + Math.pow(chromosome1.realGenes[1] - 2, 2));
    obj = term1 + term2 + term3;
    //System.out.print("\nin the Obj class, obj "+obj+" term1 "+term1+"\t"+" term2 "+term2+"tn"+" term3 "+term3+"\n");
    return obj;
  }

  public populationI getPopulation() {
    return population;
  }
  public double[] getObjectiveValues(int index) {
    /**@todo Implement this openga.ObjectiveFunctions.ObjectiveFunctionI method*/
    throw new java.lang.UnsupportedOperationException("Method getObjectiveValues() not yet implemented.");
  }

}