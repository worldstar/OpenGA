package openga.operator.miningGene;
import openga.chromosomes.*;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: PBIL is the Population Based Incremental Learning algorithm.</p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class PBIL implements EDAModelBuildingI{
  public PBIL(populationI originalPop, double lamda) {
    this.originalPop = originalPop;
    popSize = originalPop.getPopulationSize();
    chromosomeLength = originalPop.getSingleChromosome(0).genes.length;
    this.lamda = lamda;
    container = new double[chromosomeLength][chromosomeLength];

    for(int i = 0 ; i < chromosomeLength ; i ++ ){
      for(int j = 0 ; j < chromosomeLength ; j ++ ){
        container[i][j] = 1.0/popSize;
      }
    }
  }

  public PBIL(int chromosomeLength, double lamda) {
    this.chromosomeLength = chromosomeLength;
    this.lamda = lamda;
    container = new double[chromosomeLength][chromosomeLength];

    for(int i = 0 ; i < chromosomeLength ; i ++ ){
      for(int j = 0 ; j < chromosomeLength ; j ++ ){
        container[i][j] = 1.0/popSize;
      }
    }
  }

  populationI originalPop;
  int popSize, chromosomeLength;
  double container[][];//it's an m-by-m array to store the gene results. The i is job, the j is the position(sequence).
  double avgFitness = 0;
  double lamda = 0.9; //learning rate

  public void setData(populationI originalPop){
     this.originalPop = originalPop;
     popSize = originalPop.getPopulationSize();
  }

  public void startStatistics(){
    calcAverageFitness();
    calcContainer();
  }

  /**
   * The average fitness.
   */
  public void calcAverageFitness(){
    openga.Fitness.calcAverageFitness calcAverageFitness1 = new openga.Fitness.calcAverageFitness();
    calcAverageFitness1.setData(originalPop);
    calcAverageFitness1.startCalcFitness();
    avgFitness = calcAverageFitness1.getcalcFitness();
    avgFitness *= 1.2;
  }

  public void calcContainer(){
    double tempContainer[][] = new double[chromosomeLength][chromosomeLength];
    //to collect gene information.
    //System.out.println(avgFitness+" "+popSize);
    int counter = 0;
    for(int i = 0 ; i < popSize ; i ++ ){
      if(originalPop.getFitness(i) <= avgFitness){
         for(int j = 0 ; j < chromosomeLength ; j ++ ){
           int gene = originalPop.getSingleChromosome(i).getSolution()[j];
           tempContainer[gene][j] += 1;
         }
         counter ++;
      }
    }

    //to normalize them between [0, 1].
    for(int i = 0 ; i < chromosomeLength ; i ++ ){
      for(int j = 0 ; j < chromosomeLength ; j ++ ){
        tempContainer[i][j] /= counter;
      }
    }

    for(int i = 0 ; i < chromosomeLength ; i ++ ){
      for(int j = 0 ; j < chromosomeLength ; j ++ ){
        container[i][j] = (1-lamda)*container[i][j] + (lamda)*tempContainer[i][j];
      }
    }
  }

  public double[][] getContainer(){
    return container;
  }

}