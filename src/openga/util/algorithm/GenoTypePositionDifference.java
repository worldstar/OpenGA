package openga.util.algorithm;
import openga.chromosomes.*;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class GenoTypePositionDifference extends chromosomeSimilarity{
  public GenoTypePositionDifference() {
  }

  private populationI pop;                 //population
  private int popSize, chromosomeLength;  //size of population and number of digits of chromosome
  private int[][] similarity;

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
      val += Math.abs(_chromosome1.getSolution()[i] - _chromosome2.getSolution()[i]);
    }
    return val;
  }

  public int[][] getChromosomeSimilarity(){
    return similarity;
  }

  public static void main(String[] args) {
    GenoTypePositionDifference chromosomeSimilarity1 = new GenoTypePositionDifference();
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
   * The difference of position 1 = |1 - 3| = 2. The following is the difference from position 1 to 5.
   * 2 1 1 1 1
   * So the total difference is 2+1+2+1+1 = 6.
   */

}