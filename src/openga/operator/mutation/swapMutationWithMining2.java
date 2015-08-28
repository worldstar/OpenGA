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

public class swapMutationWithMining2 extends swapMutationWithMining {
  public swapMutationWithMining2() {
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
    sumGeneInfo = sumGeneInfo(cutPoint1, cutPoint2, _chromosome.genes[cutPoint1], _chromosome.genes[cutPoint2]);
    tempSum = sumGeneInfo(cutPoint1, cutPoint2, _chromosome.genes[cutPoint2], _chromosome.genes[cutPoint1]);

    while(tempSum < sumGeneInfo && k < 10){//to set new cut-points.
      setCutpoint();
      tempSum = sumGeneInfo(cutPoint1, cutPoint2, _chromosome.genes[cutPoint2], _chromosome.genes[cutPoint1]);
      k++;
    }

    if(tempSum < sumGeneInfo){
      //to restore the cut-point information because the original cut-points are better.
      cutPoint1 = originalCutPoint1;
      cutPoint2 = originalCutPoint2;
    }
  }

  /**
   * The job1 is at position 1 and job2 is at the pos2.
   * @param pos1
   * @param pos2
   * @param job1
   * @param job2
   * @return
   */
  private double sumGeneInfo(int pos1, int pos2, int job1, int job2){
    double sum = 0;
    sum = container[job1][pos1];
    sum += container[job2][pos2];
    return sum;
  }
}