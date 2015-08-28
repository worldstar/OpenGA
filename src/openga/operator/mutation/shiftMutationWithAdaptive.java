package openga.operator.mutation;
import openga.chromosomes.*;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class shiftMutationWithAdaptive extends shiftMutation {
  public shiftMutationWithAdaptive() {
  }

  public populationI pop;                 //mutation on whole population
  public double mutationRate;            //mutation rate
  private double tempMutationRate;
  /**
   * Used by adaptive strategy.
   */
  double avgFitness = 0, maxFitness = 0, minFitness;
  double k1 = 0.8, k2 = 0.5, k3 = 0.8, k4 = 0.5;

  public void setData(double mutationRate, populationI population1){
    pop = new population();
    this.pop = population1;
    this.mutationRate = mutationRate;
    popSize = population1.getPopulationSize();
    chromosomeLength = population1.getSingleChromosome(0).genes.length;
  }

  /**
   * The average fitness.
   */
  public void calcAdaptiveParameter(){
    openga.Fitness.calcAverageFitness calcAverageFitness1 = new openga.Fitness.calcAverageFitness();
    calcAverageFitness1.setData(pop);
    calcAverageFitness1.startCalcFitness();
    avgFitness = calcAverageFitness1.getcalcFitness();

    openga.Fitness.MaxFitness MaxFitness1 = new openga.Fitness.MaxFitness();
    MaxFitness1.setData(pop);
    MaxFitness1.startCalcFitness();
    maxFitness = MaxFitness1.getcalcFitness();

    openga.Fitness.MinFitness minFitness1 = new openga.Fitness.MinFitness();
    minFitness1.setData(pop);
    minFitness1.startCalcFitness();
    minFitness = minFitness1.getcalcFitness();
  }

  public void startMutation(){
    calcAdaptiveParameter();
    //avgFitness = Math.max(1, avgFitness);

    for(int i = 0 ; i < popSize ; i ++ ){
      //test the fitness is smaller than average fitness or larger than.
      if(pop.getFitness(i) <= avgFitness){
        tempMutationRate = k2*(pop.getFitness(i) - minFitness)/(avgFitness - minFitness);
      }
      else{
        tempMutationRate = k4;
      }
       //test the probability is larger than crossoverRate.
       if(Math.random() <= tempMutationRate){
         setCutpoint();
         pop.setChromosome(i,shiftGenes(pop.getSingleChromosome(i)));
       }
    }
  }


  public populationI getMutationResult(){
    return pop;
  }

}