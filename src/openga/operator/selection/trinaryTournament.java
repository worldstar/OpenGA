package openga.operator.selection;
import openga.chromosomes.*;
/**
 * <p>Title: The OpenGA project</p>
 * <p>Description: The project is to build general framework of Genetic algorithm and problem independent.</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class trinaryTournament extends binaryTournament {
  public trinaryTournament() {
  }

  public void startToSelect(){
    int tournamentSize = 3;
    //to satisfy the sizeOfPop.
    for(int i = 0 ; i < sizeOfPop ; i ++ ){
      //randomly select five chromosomes to compare.
      int indexes[] = new int[tournamentSize];
      chromosome selectedChromosome1;

      //randomly assign chromosome
      for(int j = 0 ; j < tournamentSize ; j ++){
        indexes[j] = (int)(Math.random()*originalPopSize);
      }
      //statr to compare the five ones.
      selectedChromosome1 = originalPop.getSingleChromosome(0);
      for(int j = 1 ; j < tournamentSize ; j ++){
        selectedChromosome1 = setData(selectedChromosome1, originalPop.getSingleChromosome(indexes[j]));
      }
      newPop.setSingleChromosome(i, selectedChromosome1);
    }
  }


  public static void main(String[] args) {
    trinaryTournament trinaryTournament1 = new trinaryTournament();
  }

}