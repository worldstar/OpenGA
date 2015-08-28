package openga.operator.mutation;
import openga.chromosomes.*;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: The real code type for continuous problem</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */
public class realValueMutation extends inverseMutation implements RealMoveI{
  double mutationRate = 0.1;            //mutation rate
  int popSize, chromosomeLength;  //size of population and number of digits of chromosome
  //int cutPoint1, cutPoint2;       //the genes between the two points are inversed
  chromosome chromosome1, newChromosome;
  double lwBounds[];//lower bound
  double upBound[]; //upper bound

  public void setBounds(double lwBounds[], double upBound[]){
    this.lwBounds = lwBounds;
    this.upBound= upBound;
  }

  public void startMutation(){
    //System.out.println("localSearchGenes "+mutationRate);
    for(int i = 0 ; i < pop.getPopulationSize() ; i ++ ){
       //test the probability is larger than crossoverRate.
       if(Math.random() <= mutationRate){

         //setCutpoint();
         pop.setChromosome(i,localSearchGenes(pop.getSingleChromosome(i)));
       }
    }
  }

  /**
   * It moves the value of each dimension will go up or down to its boundary.
   * @param _chromosome Current chromosome to be modified.
   * @return The modified chromsome.
   */
  private chromosome localSearchGenes(chromosome _chromosome){
    //do local search on each gene
    for(int k = 0 ; k < _chromosome.getLength() ; k ++ ){
      //decide to move up or down
      if (Math.random() > 0.5) { //move up
        _chromosome.realGenes[k] += (upBound[k] - _chromosome.realGenes[k]) * Math.random();
      }
      else {
        _chromosome.realGenes[k] -= (_chromosome.realGenes[k] - lwBounds[k]) * Math.random();
      }
    }

    return _chromosome;
  }

  public int[] getCutPoints(){
    return new int[]{cutPoint1, cutPoint2};
  }
}
