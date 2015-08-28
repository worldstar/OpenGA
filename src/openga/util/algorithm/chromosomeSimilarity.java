package openga.util.algorithm;
import openga.chromosomes.*;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: The method is based on the gene position.</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class chromosomeSimilarity {
  public chromosomeSimilarity() {
  }

  private populationI pop;                 //population
  private int popSize, chromosomeLength;  //size of population and number of digits of chromosome
  private int[][] similarity;

  public void setData(populationI population1){
    pop = new population();
    this.pop = population1;
    popSize = population1.getPopulationSize();
    chromosomeLength = population1.getSingleChromosome(0).genes.length;
    similarity = new int[chromosomeLength][chromosomeLength];
  }

  public void startCalcSimilarity(){
    for(int i = 0 ; i < popSize ; i ++ ){
      for(int j = i+1 ; j < popSize ; j ++ ){
        similarity[i][j] = similarity[j][i] = calcValue(pop.getSingleChromosome(i), pop.getSingleChromosome(j));
      }
    }
  }

  public int calcValue(chromosome _chromosome1, chromosome _chromosome2){
    int val = 0;
    for(int i = 0 ; i < _chromosome1.getLength() ; i ++ ){
      val += Math.abs(i - getPosition(_chromosome1.getSolution()[i], _chromosome2));
    }
    return val;
  }

  private int getPosition(int val, chromosome _chromosome2){
    int pos = 0;
    for(int i = 0 ; i < _chromosome2.getLength() ; i ++ ){
      if(val == _chromosome2.getSolution()[i]){
        pos = i;
        break;
      }
    }
    return pos;
  }

  public int[][] getChromosomeSimilarity(){
    return similarity;
  }

  public static void main(String[] args) {
    chromosomeSimilarity chromosomeSimilarity1 = new chromosomeSimilarity();
    int solutions[][] = new int[][]{{1,2,3,4,5},{3,1,2,5,4}};//test data, two chromosomes and length = 5.

    populationI population1 = new population();
    //boolean thetype, int size, int length, int numberOfObjs
    population1.setGenotypeSizeAndLength(true, 2, 5, 2);
    population1.createNewPop();

    for(int i = 0 ; i < solutions.length ; i ++ ){
      for(int j = 0 ; j < solutions[0].length ; j ++ ){
        population1.setGene(i, j, solutions[i][j]);
      }
    }

    chromosomeSimilarity1.setData(population1);
    chromosomeSimilarity1.startCalcSimilarity();
  }
  /**
   * 1st Chromosome : 1 2 3 4 5
   * 2nd Chromosome : 3 1 2 5 4
   *
   * The chromosome difference.
   * The gene 1 at 1st chromosome is at position 0, and at position 1 in 2nd chromosome.
   * The difference of gene 1 = |0 - 1| = 1. The following is the gene difference from gene 1 to 5.
   * 1 1 2 1 1
   * So the total difference is 1+1+2+1+1 = 6.
   */


}