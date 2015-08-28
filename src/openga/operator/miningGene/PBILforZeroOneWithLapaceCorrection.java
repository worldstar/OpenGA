/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package openga.operator.miningGene;
import openga.chromosomes.*;
/**
 *
 * @author user
 */
public class PBILforZeroOneWithLapaceCorrection extends PBILwithLapaceCorrection{

  public PBILforZeroOneWithLapaceCorrection(populationI originalPop, double lamda) {
    this.originalPop = originalPop;
    popSize = originalPop.getPopulationSize();
    chromosomeLength = originalPop.getSingleChromosome(0).genes.length;
    this.lamda = lamda;
    container = new double[2][chromosomeLength];

    for(int i = 0 ; i < 2 ; i ++ ){
      for(int j = 0 ; j < chromosomeLength ; j ++ ){
        container[i][j] = 0.5;
      }
    }
  }

  public PBILforZeroOneWithLapaceCorrection(int chromosomeLength, double lamda) {
    this.chromosomeLength = chromosomeLength;
    this.lamda = lamda;
    container = new double[2][chromosomeLength];

    for(int i = 0 ; i < 2 ; i ++ ){
      for(int j = 0 ; j < chromosomeLength ; j ++ ){
        container[i][j] = 0.5;
      }
    }
  }

  public void startStatistics(){
    calcAverageFitness();
    calcContainer();
  }

  public void calcContainer(){
    double tempContainer[][] = new double[2][chromosomeLength];
    //to collect gene information.
    //System.out.println(avgFitness+" "+popSize);
    int counter = 0;
    for(int i = 0 ; i < popSize ; i ++ ){
      if(originalPop.getFitness(i) <= avgFitness){
         for(int j = 0 ; j < chromosomeLength ; j ++ ){
           int zeroOrOne = originalPop.getSingleChromosome(i).getSolution()[j];
           tempContainer[zeroOrOne][j] += 1;
         }
         counter ++;
      }
    }

    laplaceCorrection(tempContainer);
    //double CR = showPopCorrelationValue(originalPop, tempContainer);
    updateContainer(tempContainer, counter+2);
  }

  //To avoid the pij is 0 which causes the product becomes zero by adding each element by 1.
  //2009.8.20
  private void laplaceCorrection(double container[][]){
    for(int i = 0; i < container.length ; i ++){
      for(int j = 0 ; j < container[0].length ; j ++){
        container[i][j] += 1;
      }
    }
  }

  private void updateContainer(double tempContainer[][], int counter){
    //to normalize them between [0, 1].
    for(int i = 0 ; i < tempContainer.length ; i ++ ){
      for(int j = 0 ; j < tempContainer[i].length ; j ++ ){
        tempContainer[i][j] /= counter;
      }
    }

    for(int i = 0 ; i < container.length ; i ++ ){
      for(int j = 0 ; j < container[i].length ; j ++ ){
        container[i][j] = (1-lamda)*container[i][j] + (lamda)*tempContainer[i][j];
      }
    }
  }

  public double[][] getContainer(){
    return container;
  }
}
