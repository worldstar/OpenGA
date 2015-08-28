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

public class RealCodeSwapMutation extends swapMutation {
  public RealCodeSwapMutation() {
  }
  public void startMutation(){
    for(int i = 0 ; i < popSize ; i ++ ){
       //test the probability is larger than crossoverRate.
       if(Math.random() <= mutationRate){
         setCutpoint();
         pop.setChromosome(i,swaptGenes(pop.getSingleChromosome(i)));
       }
    }
  }

  public final chromosome swaptRealGenes(chromosome _chromosome){
    double backupGenes = _chromosome.realGenes[cutPoint1];
    _chromosome.realGenes[cutPoint1] = _chromosome.realGenes[cutPoint2];
    _chromosome.realGenes[cutPoint2] = backupGenes;
    return _chromosome;
  }
}