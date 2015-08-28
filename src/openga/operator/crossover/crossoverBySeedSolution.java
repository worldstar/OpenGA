package openga.operator.crossover;
import openga.chromosomes.*;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class crossoverBySeedSolution extends twoPointCrossover2{
  public crossoverBySeedSolution() {
  }

  //start to crossover
  public void startCrossover(){
    for(int i = 1 ; i < popSize ; i ++ ){
      //The first chromosome is the seed solution. Other solutions are mated with it.
      //to get the other chromosome to crossover
      setCutpoint();
      copyElements(i, 0);
      if(Math.random() > 0.8){
        newPop.setSingleChromosome(i, generateNewSolution(newPop.getSingleChromosome(i)));
      }
    }
  }


  /**
   * The two chromosomes produce a new offspring
   * @param index1 The first chromosome to crossover
   * @param index2 The second chromosome to crossover
   */
  private void copyElements(int index1, int index2){
    //to modify the first chromosome between the index1 to index2, which genes
    //is from chromosome 2.
    int counter = 0;
    for(int i = cutPoint1 ; i <= cutPoint2; i ++ ){
      while(checkConflict(newPop.getSingleChromosome(index2).genes[counter], newPop.getSingleChromosome(index1).genes) == true){
        counter ++;
      }
      newPop.setGene(index1, i, newPop.getSingleChromosome(index2).genes[counter]);
      counter ++;
    }
  }

  /**
   * if there is the same gene, it return the index of the gene.
   * Else, default value is -1, which is also mean don't have the same gene
   * during cutpoint1 and cutpoint2.
   * @param newGene
   * @param _chromosome
   * @return
   */
  private boolean checkConflict(int newGene, int _chromosome[]){
    boolean hasConflict = false;
    for(int i = 0 ; i < cutPoint1 ; i ++ ){
      if(newGene == _chromosome[i]){
        return true;
      }
    }

    for(int i = cutPoint2 + 1 ; i < chromosomeLength ; i ++ ){
      if(newGene == _chromosome[i]){
        return true;
      }
    }

    return hasConflict;
  }

  /**
   * @param chromosome1
   * @return To generate new solution by swap mutation operator.
   */
  public chromosome generateNewSolution(chromosome chromosome1){
    openga.operator.mutation.swapMutation Mutation1 = new openga.operator.mutation.swapMutation();
    Mutation1.chromosomeLength = newPop.getLengthOfChromosome();
    Mutation1.setCutpoint();
    return Mutation1.swaptGenes(chromosome1);
  }

}