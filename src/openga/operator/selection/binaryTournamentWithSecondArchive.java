package openga.operator.selection;
import openga.chromosomes.*;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class binaryTournamentWithSecondArchive extends binaryTournament {
  public binaryTournamentWithSecondArchive() {
  }

  public void startToSelect(){
    //to satisfy the sizeOfPop. It apply the elitism first.
    for(int i = 0 ; i < numberOfelitle ; i ++ ){
      int index1 = (int)(Math.random()*eliteSize);//randomly select two chromosomes to compare.
      newPop.setSingleChromosome(i, archieve.getSingleChromosome(index1));
    }
    int counter = 0;
    //System.out.println(archieve2.getPopulationSize());
    for(int i = numberOfelitle ; i < numberOfelitle + archieve2.getPopulationSize() ; i ++ ){
      newPop.setSingleChromosome(i, archieve2.getSingleChromosome(counter++));
    }

    for(int i = numberOfelitle + archieve2.getPopulationSize() ; i < sizeOfPop ; i ++ ){
      int index1 = (int)(Math.random()*originalPopSize);//randomly select two chromosomes to compare.
      int index2 = (int)(Math.random()*originalPopSize);
      chromosome selectedChromosome1 = setData(originalPop.getSingleChromosome(index1), originalPop.getSingleChromosome(index2));
      newPop.setSingleChromosome(i, selectedChromosome1);
    }
  }

}