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

public class swapMutation extends inverseMutation {
  public swapMutation() {
  }

  public void startMutation(){
    for(int i = 0 ; i < popSize ; i ++ ){
       //test the probability is larger than crossoverRate.
       if(Math.random() <= mutationRate){
         setCutpoint();
         pop.setChromosome(i, swaptGenes(pop.getSingleChromosome(i)));
       }
    }
  }

  public final chromosome swaptGenes(chromosome _chromosome){
    int backupGenes = _chromosome.genes[cutPoint1];
    _chromosome.genes[cutPoint1] = _chromosome.genes[cutPoint2];
    _chromosome.genes[cutPoint2] = backupGenes;
    return _chromosome;
  }

}