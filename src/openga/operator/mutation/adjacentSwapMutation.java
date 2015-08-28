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

public class adjacentSwapMutation extends swapMutation {
  public adjacentSwapMutation() {
  }

  public void startMutation(){
    for(int i = 0 ; i < popSize ; i ++ ){
       //test the probability is larger than crossoverRate.
       if(Math.random() <= mutationRate){
         setCutpoint2();
         pop.setChromosome(i,swaptGenes(pop.getSingleChromosome(i)));
       }
    }
  }

  /**
   * It's the version that to construct the adjacent cut points.
   */
  public void setCutpoint2(){
    cutPoint1 = Math.max(0, (int)(Math.random() * chromosomeLength - 2));
    cutPoint2 = cutPoint1 + 1;
  }
}