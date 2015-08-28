package openga.operator.mutation;
import openga.chromosomes.*;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: A mining gene method determines the direction to mutate.
 * We forbid the direction while both genes move to inferior positions.</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class swapMutationWithMining extends swapMutation{
  public swapMutationWithMining() {
  }
  double container[][];//it's an m-by-m array to store the gene results. The i is job, the j is the position(sequence).
  double evaporateRate = 0.5;

  public void setData(double mutationRate, populationI population1){
    pop = new population();
    this.pop = population1;
    this.mutationRate = mutationRate;
    popSize = population1.getPopulationSize();
    chromosomeLength = population1.getSingleChromosome(0).genes.length;
  }

  /**
   * To collect gene informaiton.
   * @param population1 The population is not crossed and muated yet.
   */
  public void setData(populationI population1){
    pop = new population();
    this.pop = population1;
    popSize = population1.getPopulationSize();
    chromosomeLength = population1.getSingleChromosome(0).genes.length;

    if(container == null){
      container = new double[chromosomeLength][chromosomeLength];
    }
    decave();
    calcContainer();
  }

  /**
   * we determine the mutation cut-point by mining the gene information.
   */
  public void startMutation(){
    for(int i = 0 ; i < popSize ; i ++ ){
       //test the probability is larger than mutationRate.
       if(Math.random() <= mutationRate){
         checkCutPoints(pop.getSingleChromosome(i));
         pop.setChromosome(i, swaptGenes(pop.getSingleChromosome(i)));
       }
    }
  }

  /**
   * To reduce the effect of previous gene information.
   */
  public void decave(){
    for(int i = 0 ; i < container.length ; i ++ ){
      for(int j = 0 ; j < container.length ; j ++ ){
        container[i][j] *= evaporateRate;
      }
    }
  }

  /**
   * To collect the gene information.
   */
  public void calcContainer(){
    double avgFitness = calcAverageFitness();
    for(int i = 0 ; i < popSize ; i ++ ){
      if(pop.getFitness(i) < avgFitness){
         for(int j = 0 ; j < chromosomeLength ; j ++ ){
           int gene = pop.getSingleChromosome(i).getSolution()[j];
           container[gene][j] += 1;
         }
      }
    }
  }

  /**
   * The average fitness determines which chromosomes should be collected.
   */
  public double calcAverageFitness(){
    openga.Fitness.calcAverageFitness calcAverageFitness1 = new openga.Fitness.calcAverageFitness();
    calcAverageFitness1.setData(pop);
    calcAverageFitness1.startCalcFitness();
    return calcAverageFitness1.getcalcFitness();
  }

  /**
   * If the both gene moves to inferior positions, we generate the other one to replace it.
   * @param _chromosome
   */
  public void checkCutPoints(chromosome _chromosome){
    //to judge whether the jobs move to inferior positions
    boolean inferior1 = false, inferior2 = false;
    int k = 0;

    do{
      setCutpoint();
      int job1 = _chromosome.genes[cutPoint1];
      int job2 = _chromosome.genes[cutPoint2];

      if(container[job1][cutPoint1] > container[job1][cutPoint2]){//to compare the rate before move and after move position.
        inferior1 = true;
      }
      else{
        inferior1 = false;
      }

      if(container[job2][cutPoint2] > container[job2][cutPoint1]){
        inferior2 = true;
      }
      else{
        inferior2 = false;
      }
      k++;
    }
    while(inferior1 && inferior2 && k < 10);//If both genes move to inferior positions, we re-generate new cut-point.
  }
}