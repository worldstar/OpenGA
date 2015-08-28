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

public class realCodeTwoPointCrossover extends twoPointCrossover2{
  public realCodeTwoPointCrossover() {
  }
  populationI originalPop, newPop;
  double crossoverRate;
  int popSize, chromosomeLength;
  int cutPoint1, cutPoint2;
  int pos1, pos2;
  double K1 = 0, K2 = 0, X1, X2;

  //start to crossover
  public void startCrossover(){
    for(int i = 0 ; i < popSize ; i ++ ){
       //test the probability is larger than crossoverRate.
       if(Math.random() <= crossoverRate){
         //to get the other chromosome to crossover
         int index2 = getCrossoverChromosome(i);
         setCutpoint();
         calcK1K2(i, index2);
         copyElements(i, index2, X1);
         copyElements(index2, i, X2);
       }
    }
  }

  private void calcK1K2(int index1, int index2){
    for(int i = cutPoint1 ; i <= cutPoint2; i ++ ){
      K1 += newPop.getSingleChromosome(index1).getRealCodeSolution()[i];
      K2 += newPop.getSingleChromosome(index2).getRealCodeSolution()[i];
    }
    X1 = K1/K2;
    X2 = K2/K1;
  }

  private void copyElements(int index1, int index2, double X){
    for(int i = cutPoint1 ; i <= cutPoint2; i ++ ){
      newPop.setGene(index1, i, X*originalPop.getSingleChromosome(index2).genes[i]);
    }
  }

}