package openga.chromosomes;

/**
 * <p>Title: The OpenGA project</p>
 * <p>Description: The project is to build general framework of Genetic algorithm and problem independent.</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class findParetoByFitness {
  public findParetoByFitness() {
  }

  populationI originalPop, newPop;
  int temp_Nondominated = 0;

 public void setPopulation(populationI originalPop){
    this.originalPop = originalPop;
  }

  public void startToFindParetoSolution(){
    int indexes[];
    for(int i = 0 ; i < originalPop.getPopulationSize() ; i ++ ){
      if(originalPop.getSingleChromosome(i).getFitnessValue() == 0){
        temp_Nondominated ++;
      }
    }

    indexes = new int[temp_Nondominated];
    newPop = new population();
    temp_Nondominated = 0;

    for(int i = 0 ; i < originalPop.getPopulationSize() ; i ++ ){
      if(originalPop.getSingleChromosome(i).getFitnessValue() == 0){
        indexes[temp_Nondominated++] = i;
      }
    }
    //form the Pareto front of the population.
    storeSelectedChromosomes storeSelectedChromosomes1 = new storeSelectedChromosomes();
    storeSelectedChromosomes1.setGenotypeSizeAndLength(originalPop.getEncodedType(), originalPop.getPopulationSize(),
                                      originalPop.getSingleChromosome(0).genes.length,
                                      originalPop.getNumberOfObjectives());
    storeSelectedChromosomes1.initNewPop();
    storeSelectedChromosomes1.setOriginalPop(originalPop, indexes);
    newPop = storeSelectedChromosomes1.getPopulation();
  }

  public populationI getPopulation(){
    return newPop;
  }


  public static void main(String[] args) {
    findParetoByFitness findParetoByFitness1 = new findParetoByFitness();
  }

}