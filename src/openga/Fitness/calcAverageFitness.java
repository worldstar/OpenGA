package openga.Fitness;
import openga.chromosomes.*;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class calcAverageFitness {
  public calcAverageFitness() {
  }

  private populationI pop;                 //population
  private int popSize, chromosomeLength;  //size of population and number of digits of chromosome
  private double averageFitnessValue = 0;

  public void setData(populationI population1){
    this.pop = population1;
    popSize = population1.getPopulationSize();
    chromosomeLength = population1.getSingleChromosome(0).genes.length;
  }

  public void startCalcFitness(){
    double tempVal = 0;
    for(int i = 0 ; i < popSize ; i ++ ){
      tempVal += pop.getFitness(i);
    }
    averageFitnessValue = tempVal/(double)popSize;
  }

  public double getcalcFitness(){
    return averageFitnessValue;
  }

  public static void main(String[] args) {
    calcAverageFitness calcAverageFitness1 = new calcAverageFitness();
  }

}