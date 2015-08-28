package openga.operator.mutation;
import openga.chromosomes.*;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class shiftMutationWithMining2 extends shiftMutation {
  public shiftMutationWithMining2() {
  }
  double container[][];//it's an m-by-m array to store the gene results. The i is job, the j is the position(sequence).
  double evaporateRate = 0.5;
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

  public void startMutation(){
    for(int i = 0 ; i < popSize ; i ++ ){
       //test the probability is larger than crossoverRate.
       if(Math.random() <= mutationRate){
         checkCutPoints(pop.getSingleChromosome(i));
         pop.setChromosome(i, shiftGenes(pop.getSingleChromosome(i)));
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
    setCutpoint();
    double sumGeneInfo = 0;
    int originalCutPoint1 = cutPoint1, originalCutPoint2 = cutPoint2;
    double tempSum = 0;
    int k = 0;

    //to set the original gene information.
    sumGeneInfo = sumGeneInfoBeforeShift(cutPoint1, cutPoint2, _chromosome.genes);
    tempSum = sumGeneInfoAfterShift(cutPoint1, cutPoint2, _chromosome.genes);

    while(tempSum < sumGeneInfo && k < 10){//to set new cut-points.
      setCutpoint();
      sumGeneInfo = sumGeneInfoBeforeShift(cutPoint1, cutPoint2, _chromosome.genes);
      tempSum = sumGeneInfoAfterShift(cutPoint1, cutPoint2, _chromosome.genes);
      k++;
    }

    if(tempSum < sumGeneInfo){
      //to restore the cut-point information because the original cut-points are better.
      cutPoint1 = originalCutPoint1;
      cutPoint2 = originalCutPoint2;
    }
  }

  private double sumGeneInfoBeforeShift(int pos1, int pos2, int _genes[]){
    double sum = 0;
    int job;

    for(int i = pos1 ; i <= pos2 ; i ++ ){
      job = _genes[i];
      sum = container[job][i];
    }
    return sum;
  }

  private double sumGeneInfoAfterShift(int pos1, int pos2, int _genes[]){
    double sum = 0;
    int job;
    //for the first job at pos1 move to the last pos2.
    job = _genes[pos1];
    sum = container[job][pos2];
    for(int i = pos1 + 1 ; i <= pos2 ; i ++ ){
      job = _genes[i];
      sum = container[job][i-1];
    }
    return sum;
  }


}