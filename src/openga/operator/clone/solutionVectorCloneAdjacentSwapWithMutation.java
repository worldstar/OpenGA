package openga.operator.clone;
import openga.chromosomes.*;
import openga.operator.mutation.*;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class solutionVectorCloneAdjacentSwapWithMutation extends solutionVectorCloneWithMutation{
  public solutionVectorCloneAdjacentSwapWithMutation() {
  }
  /**
   * @param chromosome1 The identical solution
   * @return To generate new solution by adjacent swap mutation operator.
   */
  public chromosome generateNewSolution(chromosome chromosome1){
    openga.operator.mutation.adjacentSwapMutation Mutation1 = new openga.operator.mutation.adjacentSwapMutation();
    Mutation1.chromosomeLength = originalPop.getLengthOfChromosome();
    Mutation1.setCutpoint2();
    return Mutation1.swaptGenes(chromosome1);
  }
}