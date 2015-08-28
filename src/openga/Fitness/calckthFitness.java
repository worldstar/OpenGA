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

public class calckthFitness extends calcAverageFitness {
  public calckthFitness() {
  }

  private populationI pop;                 //population
  private int popSize, chromosomeLength;  //size of population and number of digits of chromosome

  private double kthFitnessValue = 0;
  private double fitnessArray[];

  public void setData(populationI population1){
    this.pop = population1;
    popSize = population1.getPopulationSize();
    chromosomeLength = population1.getSingleChromosome(0).genes.length;
    fitnessArray = new double[popSize];

    for(int i = 0 ; i < popSize ; i ++ ){
      fitnessArray[i] = pop.getFitness(i);
    }
  }

  public void startCalcFitness(){
    double tempVal = 0;
    //use prune-and-search algorithm to find the kth value.

  }

  public double getcalcFitness(){
    return kthFitnessValue;
  }

  public static void main(String[] args) {
    calckthFitness calckthFitness1 = new calckthFitness();
  }

}