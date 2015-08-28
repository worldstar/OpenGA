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

public class similarity {
  public similarity() {
  }

  private populationI pop;                 //population
  private int popSize, chromosomeLength;  //size of population and number of digits of chromosome
  private double[][] similarity;

  public void setData(populationI population1){
    pop = new population();
    this.pop = population1;
    popSize = population1.getPopulationSize();
    chromosomeLength = population1.getSingleChromosome(0).genes.length;
    similarity = new double[chromosomeLength][chromosomeLength];
  }

  public void startCalcSimilarity(){
    for(int i = 0 ; i < popSize ; i ++ ){
      for(int j = i+1 ; j < popSize ; j ++ ){
        similarity[i][j] = similarity[j][i] = calcValue(pop.getSingleChromosome(i), pop.getSingleChromosome(j));
      }
    }
  }

  public double calcValue(chromosome _chromosome1, chromosome _chromosome2){
    double val = 0;
    //the euclidean distance of objective value difference of the two solutions.
    for(int i = 0 ; i < _chromosome1.getObjValue().length ; i ++ ){
      val += Math.pow(_chromosome1.getObjValue()[i] - _chromosome2.getObjValue()[i],2);
    }
    return Math.sqrt(val);
  }


  public double[][] getChromosomeSimilarity(){
    return similarity;
  }

  public static void main(String[] args) {
    similarity similarity1 = new similarity();
  }

}