/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package openga.ObjectiveFunctions;

import openga.chromosomes.chromosome;
import openga.chromosomes.populationI;

/**
 *
 * @author 
 */
public class ObjectiveFunctionMTSPOneChromosome extends ObjectiveFunctionTSP implements ObjectiveFunctionTSPI{
  populationI population;
  double coordinates[][];
  double originalPoint[];
  int length, indexOfObjective;
  double objectiveValue = 0;
  double distanceMatrix[][];
  double distanceToOriginal[];

  //For MTSP
  int numberOfSalesmen = 2;

  public ObjectiveFunctionMTSPOneChromosome(){
      
  }

  public void setTSPData(double originalPoint[], double coordinates[][], int numberOfSalesmen){
    this.originalPoint = originalPoint;
    this.coordinates = coordinates;
    this.numberOfSalesmen = numberOfSalesmen;
    
    openga.ObjectiveFunctions.util.generateMatrix_EuclideanDist generateMatrix_EuclideanDist1
        = new openga.ObjectiveFunctions.util.generateMatrix_EuclideanDist();
    generateMatrix_EuclideanDist1.setData(coordinates);
    generateMatrix_EuclideanDist1.setDistanceMatrixData();
    distanceMatrix = generateMatrix_EuclideanDist1.getMatrix();

    openga.ObjectiveFunctions.util.generateMatrix_EuclideanDist2 generateMatrix_EuclideanDist21
        = new openga.ObjectiveFunctions.util.generateMatrix_EuclideanDist2();
    generateMatrix_EuclideanDist21.setData(originalPoint, coordinates);
    generateMatrix_EuclideanDist21.setDistanceMatrixData();
    distanceToOriginal = generateMatrix_EuclideanDist21.getMatrix2();
  }

  public void setData(populationI population, int indexOfObjective) {
    this.population = population;
    this.length = population.getLengthOfChromosome();
    this.indexOfObjective = indexOfObjective;
  }

  public void setData(populationI population, populationI PartIIChromosomes, int indexOfObjective) {
    System.out.print("setData(populationI population, populationI PartIIChromosomes, int indexOfObjective) is not supported here.\nProgram exit.");
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
    forMTSPDistanceCalcOneChromosomeWay2 forDistanceCalculation1 = new forMTSPDistanceCalcOneChromosomeWay2();
    forDistanceCalculation1.setData(distanceToOriginal, distanceMatrix, _chromosome1, numberOfSalesmen);    
    forDistanceCalculation1.calcObjective();    
    return forDistanceCalculation1.getObjectiveValue2();
  }

  public populationI getPopulation() {
    return population;
  }
}
