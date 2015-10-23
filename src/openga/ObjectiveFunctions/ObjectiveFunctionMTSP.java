/*
 * This program evaluates the objective of multiple traveling salesmen problems.
 * The encoding technique uses the Arthur et al. (2006), which applies the two-part
 * chromosome. This program actually uses two populations; the first population
 * records the whole sequence, i.e., the first part of the chromosome.
 * Then the second population stores the result of cities per salesman.
 * The two populations will work together to present the results.
 * Please refer to the following paper of Arthur et al. (2006).
 * Arthur E. Carter, Cliff T. Ragsdale, A new approach to solving the multiple
 traveling salesperson problem using genetic algorithms, European Journal of
 Operational Research, Volume 175, Issue 1, 16 November 2006, Pages 246-257
 */

package openga.ObjectiveFunctions;
import openga.chromosomes.*;
/**
 *
 * @author Shih-Hsin Chen
 * 2009.6.19
 */
public class ObjectiveFunctionMTSP extends ObjectiveFunctionTSP implements ObjectiveFunctionTSPI{
  populationI population;// Part-one chromosomes
  double coordinates[][];
  double originalPoint[];
  int length, indexOfObjective;
  double objectiveValue = 0;
  double distanceMatrix[][];
  double distanceToOriginal[];

  //For MTSP
  populationI PartIIChromosomes;  

  public void setTSPData(double originalPoint[], double coordinates[][], populationI PartIIChromosomes){
    this.originalPoint = originalPoint;
    this.coordinates = coordinates;
    this.PartIIChromosomes = PartIIChromosomes;
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
    this.population = population;
    this.PartIIChromosomes = PartIIChromosomes;
    //this.PartIIChromosomes = PartIIChromosomes;
    this.length = population.getLengthOfChromosome();
    this.indexOfObjective = indexOfObjective;
  }

  public void calcObjective() {
    double obj;
    double objectives[];

    for(int i = 0 ; i < population.getPopulationSize() ; i ++ ){
      objectives = population.getObjectiveValues(i);
      obj = evaluateAll(population.getSingleChromosome(i), PartIIChromosomes.getSingleChromosome(i));
      objectives[indexOfObjective] = obj;
      population.setObjectiveValue(i, objectives);
      PartIIChromosomes.setObjectiveValue(i, objectives);
    }
  }

  public double evaluateAll(chromosome _chromosome1, chromosome PIISolutions){
    forMTSPDistanceCalculation forDistanceCalculation1 = new forMTSPDistanceCalculation();
    forDistanceCalculation1.setData(distanceToOriginal, distanceMatrix, _chromosome1, PIISolutions);
    forDistanceCalculation1.calcObjective();    
    
    if(typeName.equals("TotalDistance")){
      return forDistanceCalculation1.getObjectiveValue();
    }
    else{//Currently assign to MaxDistance
      return forDistanceCalculation1.getObjectiveValue2();
    }    
  }

  public populationI getPopulation() {
    return population;
  }
}
