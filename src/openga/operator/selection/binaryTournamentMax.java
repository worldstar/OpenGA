package openga.operator.selection;
import openga.chromosomes.*;

/**
 * <p>Title: The OpenGA project</p>
 * <p>Description: Binary tournament for the maximization problem.
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Cheng Shiu University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class binaryTournamentMax extends binaryTournament{
  public binaryTournamentMax() {
  }

  //simply to determine which one is better by fitness value.
  //For a maximization problem, the chromosome has larger fittness value is better.
  public chromosome setData(chromosome chromosome1, chromosome chromosome2){    
    if(chromosome1.getFitnessValue() >= chromosome2.getFitnessValue()){
      return chromosome1;
    }
    return chromosome2;
  }

  public void startToSelect(){
    //to satisfy the sizeOfPop. It applies the elitism first.
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