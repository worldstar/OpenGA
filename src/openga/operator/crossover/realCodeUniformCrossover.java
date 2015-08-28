package openga.operator.crossover;

/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class realCodeUniformCrossover extends realCodeTwoPointCrossover {
  public realCodeUniformCrossover() {
  }

  private void calcK1K2(int index1, int index2){
    K1 += newPop.getSingleChromosome(index1).getRealCodeSolution()[cutPoint1];
    K1 += newPop.getSingleChromosome(index1).getRealCodeSolution()[cutPoint2];
    K2 += newPop.getSingleChromosome(index2).getRealCodeSolution()[cutPoint1];
    K2 += newPop.getSingleChromosome(index2).getRealCodeSolution()[cutPoint2];
    X1 = K1/K2;
    X2 = K2/K1;
  }

  private void copyElements(int index1, int index2, double X){
    newPop.setGene(index1, cutPoint1, X*originalPop.getSingleChromosome(index2).genes[cutPoint1]);
    newPop.setGene(index1, cutPoint2, X*originalPop.getSingleChromosome(index2).genes[cutPoint2]);
  }
}