package openga.chromosomes;
import openga.chromosomes.*;
/**
 * <p>Title: The OpenGA project</p>
 * <p>Description: The project is to build general framework of Genetic algorithm and problem independent.</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class storeSelectedChromosomes extends population{
  public storeSelectedChromosomes() {
  }

  populationI newPop, originalPop;
  int selectedIndex[];
  int size;

  public void setOriginalPop(populationI originalPop, int selectedIndex[]){
    this.originalPop = originalPop;
    this.selectedIndex = selectedIndex;
    size = selectedIndex.length;
  }

  public void formNewPopulation(){
    //initial the newPop.
    newPop = new population();
    newPop.setGenotypeSizeAndLength(true, size,
                                    originalPop.getLengthOfChromosome(), originalPop.getNumberOfObjectives());
    newPop.initNewPop();
    for(int i = 0 ; i < size ; i ++ ){
      newPop.setSingleChromosome(i, originalPop.getSingleChromosome(selectedIndex[i]));
      newPop.setFitness(i, originalPop.getFitness(selectedIndex[i]));
      //newPop.setNormalizedObjValue(i, originalPop.gettNormalizedObjValue(selectedIndex[i]));
      newPop.setObjectiveValue(i, originalPop.getObjectiveValues(selectedIndex[i]));
      newPop.setScalarizedObjectiveValue(i, originalPop.getScalarizedObjectiveValue(selectedIndex[i]));
    }
  }

  public populationI getPopulation(){
    return newPop;
  }


  public static void main(String[] args) {
    storeSelectedChromosomes storeSelectedChromosomes1 = new storeSelectedChromosomes();
  }

}