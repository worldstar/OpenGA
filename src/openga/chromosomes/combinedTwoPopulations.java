package openga.chromosomes;

/**
 * <p>Title: The OpenGA project</p>
 * <p>Description: The project is to build general framework of Genetic algorithm and problem independent.</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class combinedTwoPopulations extends population{
  public combinedTwoPopulations() {
  }

    populationI originalPop1, originalPop2, newPop;
    int size, size1, size2, newPopulationSize;

   public void setTwoPopulations(populationI originalPop1, populationI originalPop2){
      this.originalPop1 = originalPop1;
      this.originalPop2 = originalPop2;
      //System.out.println("originalPop1.getPopulationSize(); "+originalPop1.getPopulationSize());
      size1 = originalPop1.getPopulationSize();
      size2 = originalPop2.getPopulationSize();
      size = newPopulationSize = size1 + size2;
      //initiate the new population
      newPop = new population();
      newPop.setGenotypeSizeAndLength(originalPop1.getEncodedType(), newPopulationSize,
                                      originalPop1.getLengthOfChromosome(),
                                      originalPop1.getNumberOfObjectives());
      newPop.initNewPop();
    }

    public void startToCombine(){
      //set the originalPop1 first
      for(int i = 0 ; i < size1 ; i ++ ){
        writeDataToNewPopulation(i, originalPop1.getSingleChromosome(i));
      }
      int counter = 0;
      //set the second population.
      for(int i = size1 ; i < size ; i ++ ){
        writeDataToNewPopulation(i, originalPop2.getSingleChromosome(counter++));
      }
    }

    public void writeDataToNewPopulation(int i, chromosome chromosome1){
      newPop.setChromosome(i, chromosome1);//set chromsome
      newPop.setFitness(i, chromosome1.getFitnessValue());
      newPop.setObjectiveValue(i, chromosome1.getObjValue());
    }

    public populationI getPopulation(){
      return newPop;
    }

  public static void main(String[] args) {
    combinedTwoPopulations combinedTwoPopulations1 = new combinedTwoPopulations();
  }

}