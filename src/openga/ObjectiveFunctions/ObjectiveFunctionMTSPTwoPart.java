package openga.ObjectiveFunctions;
import openga.chromosomes.chromosome;
import openga.chromosomes.populationI;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author UlyssesZhang
 */
public class ObjectiveFunctionMTSPTwoPart extends ObjectiveFunctionMTSP{
    
    int numberOfSalesmen;
    
  public void setData(populationI population, int numberOfSalesmen, int indexOfObjective) {
    this.population = population;
    this.numberOfSalesmen = numberOfSalesmen;
    //this.PartIIChromosomes = PartIIChromosomes;
    this.length = population.getLengthOfChromosome();
    this.indexOfObjective = indexOfObjective;
  }
  
  public void calcObjective() {
    double obj;
    double objectives[];

    for(int i = 0 ; i < numberOfSalesmen ; i ++ ){
      objectives = population.getObjectiveValues(i);
      obj = evaluateAll(population.getSingleChromosome(i), numberOfSalesmen);
      objectives[indexOfObjective] = obj;
      population.setObjectiveValue(i, objectives);
//      PartIIChromosomes.setObjectiveValue(i, objectives);
    }
  }

  public double evaluateAll(chromosome _chromosome1, int numberOfSalesmen){
    forMTSPDistanceTwoPartCalculation TPforDistanceCalculation1 = new forMTSPDistanceTwoPartCalculation();
    TPforDistanceCalculation1.setData(distanceToOriginal, distanceMatrix, _chromosome1, numberOfSalesmen);
    TPforDistanceCalculation1.calcObjective();
    return TPforDistanceCalculation1.getObjectiveValue();
  }
  
}
