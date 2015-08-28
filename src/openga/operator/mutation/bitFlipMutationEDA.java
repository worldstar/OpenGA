/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package openga.operator.mutation;
import openga.chromosomes.*;
/**
 *
 * @author user
 */
public class bitFlipMutationEDA extends bitFlipMutation implements EDAIMutation{
  double container[][];//it's an m-by-m array to store the gene results. The i is job, the j is the position(sequence).
  int numberOfTournament = 2;

  public void setEDAinfo(double container[][], int numberOfTournament){
    this.container = container;
    this.numberOfTournament = numberOfTournament;
  }

  /**
   * we determine the mutation cut-point by mining the gene information.
   */
  public void startMutation(){
    for(int i = 0 ; i < popSize ; i ++ ){
      checkBitFlipPoints(pop.getSingleChromosome(i));
    }
  }

  /**
   * If the both gene moves to inferior positions, we generate the other one to replace it.
   * @param _chromosome
   */
  public void checkBitFlipPoints(chromosome _chromosome){
    if(numberOfTournament == 0){
      System.out.println("numberOfTournament is at least 1.");
      System.exit(0);
    }

    double originalProbabilitySum;
    double newProbabilitySum;
    double maxProb = Double.MIN_VALUE;
    int selectedIndex = 0;
    int originalSolution[] = _chromosome.genes.clone();

    int vector1[][] = generateRandomVector();

    for(int i = 0 ; i < numberOfTournament ; i ++ ){      
      
      if(numberOfTournament == 1){//it needs not to collect the gene information
        selectedIndex = i;
      }
      else{
        originalProbabilitySum = productGeneInfo(_chromosome, container, vector1[i]);
        bitFlip(_chromosome, vector1[i]);
        newProbabilitySum = productGeneInfo(_chromosome, container, vector1[i]);
        //The improvement of the mutation
        double difference = newProbabilitySum - originalProbabilitySum;
        
        if(maxProb < difference){
          maxProb = newProbabilitySum;
          selectedIndex = i;
        }
      }

      //Restore for the next test.
      _chromosome.genes = originalSolution.clone();
    }
    bitFlip(_chromosome, vector1[selectedIndex]);
  }

  private int[][] generateRandomVector(){
    int vector1[][] = new int[numberOfTournament][chromosomeLength];

    for(int i = 0 ; i < numberOfTournament ; i ++ ){
      for(int j = 0 ; j < chromosomeLength ; j ++){
        if(Math.random() <= mutationRate){
          vector1[i][j] = 1;
        }
      }
    }
    return vector1;
  }

  private void bitFlip(chromosome chromosome1, int vector1[]){
    for(int i = 0 ; i < chromosomeLength ; i ++ ){
      if(vector1[i] == 1){//Do the bit-flip
        if(chromosome1.genes[i] == 0){
          chromosome1.genes[i] = 1;
        }
        else{
          chromosome1.genes[i] = 0;
        }
      }
    }
  }

  private double productGeneInfo(chromosome chromsome1, double containerTemp[][], int vector1[]){
    double productValue = 1;
    for(int i = 0 ; i < chromsome1.getLength() ; i ++){
      if(vector1[i] == 1){//For the genes will do the bit-flip.
        int zeroOrOne = chromsome1.getSolution()[i];
        productValue *= containerTemp[zeroOrOne][i] * 10;
      }
    }
    return productValue;
  }
}
