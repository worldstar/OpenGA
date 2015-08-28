package openga.operator.mutation;
import openga.chromosomes.*;
/*
 * This program evaluates the objective of multiple traveling salesmen problems.
 * The encoding technique uses the Arthur et al. (2006), which applies the two-part
 * chromosome. This program actually uses two populations; the first population
 * records the whole sequence, i.e., the first part of the chromosome.
 * Then the second population stores the result of cities per salesman.
 * The two populations will work together to present the results.
 * Please refer to the following paper of Arthur et al. (2006).
 * Arthur E. Carter, Cliff T. Ragsdale, A new approach to solving the multiple
 traveling salesperson problem using genetic algorithms, European Journal of
 Operational Research, Volume 175, Issue 1, 16 November 2006, Pages 246-257
 */
/**
 *
 * @author Shih-Hsin Chen
 */
public class MTSPMutation extends inverseMutation{

  public void startMutation(){
    for(int i = 0 ; i < popSize ; i ++ ){
       //test the probability is larger than crossoverRate.
       if(Math.random() <= mutationRate){
         setCutpoint();
         pop.setChromosome(i, moveGenes(pop.getSingleChromosome(i)));
       }
    }
  }

  //We move some quota from cutpoint2 to cutpoint1. Thus, the number of cities at cutpoint1 is increased.
  private chromosome moveGenes(chromosome _chromosome){
    //Because the original cutPoint1 is always less than cutPoint2, we shuffle the position.
    if(Math.random() < 0.5){
      int tempIndex = cutPoint1;
      cutPoint1 = cutPoint2;
      cutPoint2 = tempIndex;
    }
    
    //To move the number of cities from cutPoint2 to cutPoint1 and it ensures the
    //number of visiting city of the salesmen at cutPoint2 is at least one city.
    int maxMovedCities = (int)((_chromosome.genes[cutPoint2] - 1)*Math.random()*0.5);
    _chromosome.genes[cutPoint1] += maxMovedCities;
    _chromosome.genes[cutPoint2] -= maxMovedCities;
    return _chromosome;
  }
}
