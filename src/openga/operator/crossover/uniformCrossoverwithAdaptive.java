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

public class uniformCrossoverwithAdaptive extends uniformCrossover {
  public uniformCrossoverwithAdaptive() {
  }
  /**
   * Used by adaptive strategy.
   */
  double avgFitness = 0, maxFitness = 0, minFitness;
  double k1 = 0.8, k2 = 0.5, k3 = 0.8, k4 = 0.5;

  /**
   * The average fitness.
   */
  public void calcAdaptiveParameter(){
    openga.Fitness.calcAverageFitness calcAverageFitness1 = new openga.Fitness.calcAverageFitness();
    calcAverageFitness1.setData(originalPop);
    calcAverageFitness1.startCalcFitness();
    avgFitness = calcAverageFitness1.getcalcFitness();

    openga.Fitness.MaxFitness MaxFitness1 = new openga.Fitness.MaxFitness();
    MaxFitness1.setData(originalPop);
    MaxFitness1.startCalcFitness();
    maxFitness = MaxFitness1.getcalcFitness();

    openga.Fitness.MinFitness minFitness1 = new openga.Fitness.MinFitness();
    minFitness1.setData(originalPop);
    minFitness1.startCalcFitness();
    minFitness = minFitness1.getcalcFitness();
  }

  public void startCrossover() {
    calcAdaptiveParameter();
    for(int i = 0 ; i < popSize ; i ++ ){
      if(originalPop.getFitness(i) < avgFitness){
        crossoverRate = k1*(newPop.getFitness(i) - minFitness)/(avgFitness - minFitness);
      }
      else{
        crossoverRate = k3;
      }

      //test the probability is larger than crossoverRate.
      if(Math.random() <= crossoverRate){
         //to get the other chromosome to crossover
         int index2 = getCrossoverChromosome(i);
         int vector1[] = generateRandomVector();
         copyElements(i, index2, vector1);
         copyElements(index2, i, vector1);
      }
    }
  }

  private int[] generateRandomVector(){
    int vector1[] = new int[chromosomeLength];
    for(int i = 0 ; i < chromosomeLength ; i ++ ){
      if(Math.random() > 0.5){
        vector1[i] = 1;
      }
      else{
        vector1[i] = 0;
      }
    }
    return vector1;
  }

  private void copyElements(int index1, int index2, int vector1[]){
    //to modify the first chromosome between the index1 to index2, which genes
    //is from chromosome 2.
    for(int i = 0 ; i < chromosomeLength; i ++ ){
      if(vector1[i] == 0){
        newPop.setGene(index1, i, originalPop.getSingleChromosome(index2).genes[i]);
      }
    }
  }

}