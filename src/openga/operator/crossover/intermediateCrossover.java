package openga.operator.crossover;

/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: The crossover operator is called intermdediate crossover for continuous problem.</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class intermediateCrossover extends ArithmeticCrossover{
  public intermediateCrossover() {
  }

  /**
   * The two chromosomes produce a new offspring.
   * @param index1 The first chromosome to crossover
   * @param index2 The second chromosome to crossover
   * @param _alpha The exchange factor.
   */
  private void copyElements(int index1, int index2, double _alpha){
    //to modify the first chromosome between the index1 to index2, which genes
    //is from chromosome 2.

    for(int i = 0 ; i < chromosomeLength ; i ++ ){
      double _Value = newPop.getSingleChromosome(index1).realGenes[i]
          + _alpha*(newPop.getSingleChromosome(index2).realGenes[i] - newPop.getSingleChromosome(index1).realGenes[i]);
      newPop.setGene(index1, i, _Value);
    }
  }

}