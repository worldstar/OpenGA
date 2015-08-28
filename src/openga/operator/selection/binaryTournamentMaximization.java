/*
 * For the purpose of the maximization problem. Larger fitness is better.
 */

package openga.operator.selection;
import openga.chromosomes.*;
/**
 *
 * @author user
 */
public class binaryTournamentMaximization extends binaryTournament{
  //The new population is created by the method.
  //Besides, the newPopulation size is equal to sizeOfPop.
  populationI newPop, originalPop;
  populationI archieve;
  populationI archieve2;
  int sizeOfPop;
  int originalPopSize, eliteSize = 0;
  int numberOfelitle = 0;
  int tournamentSize = 2;


  public chromosome setData(chromosome chromosome1, chromosome chromosome2){
    if(chromosome1.getFitnessValue() >= chromosome2.getFitnessValue()){
      return chromosome1;
    }
    return chromosome2;
  }

  public void startToSelect(){
    //int counter = 0;
    for(int i = 0 ; i < numberOfelitle ; i ++ ){
      int index1 = (int)(Math.random()*eliteSize);//randomly select two chromosomes to compare.
      newPop.setSingleChromosome(i, archieve.getSingleChromosome(index1));
    }

    for(int i = numberOfelitle ; i < sizeOfPop ; i ++ ){
      int index1 = (int)(Math.random()*originalPopSize);//randomly select two chromosomes to compare.
      int index2 = (int)(Math.random()*originalPopSize);
      chromosome selectedChromosome1 = setData(originalPop.getSingleChromosome(index1), originalPop.getSingleChromosome(index2));
      newPop.setSingleChromosome(i, selectedChromosome1);
    }
  }
}
