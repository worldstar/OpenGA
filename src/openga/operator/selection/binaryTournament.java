package openga.operator.selection;
import openga.chromosomes.*;

/**
 * <p>Title: The OpenGA project</p>
 * <p>Description: To pass two random chromosome and compare their own fitness. We select the higher fitness chromosome.
 *  How many chromosomes should be copied. Originally, it is equal to population size.
 * However, if we want to modify the size, we should set the parameter.
 * For example, the initial population is larger which is compared with working population.
 * So we can shrink the initial population into the size we use.
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class binaryTournament implements SelectI{
  public binaryTournament() {
  }

  //The new population is created by the method.
  //Besides, the newPopulation size is equal to sizeOfPop.
  populationI newPop, originalPop;
  populationI archieve;
  populationI archieve2;
  int sizeOfPop;
  int originalPopSize, eliteSize = 0;
  int numberOfelitle = 0;
  int tournamentSize = 2;

  //If we don't want to modify the population size, the sizeOfPop is equal to the original size of population.
  public void setData(populationI originalPop){
    this.sizeOfPop = originalPopSize = originalPop.getPopulationSize();
    setData(this.sizeOfPop, originalPop);
  }

  //we want to modify the new population size, we set the numberOfPop.
  public void setData(int sizeOfPop, populationI originalPop){
    this.sizeOfPop = sizeOfPop;
    originalPopSize = originalPop.getPopulationSize();
    this.originalPop = originalPop;
    newPop = new population();//to assign a memmory space to the variable.
    //set attributes because we may copy the object latter.
    newPop.setGenotypeSizeAndLength(originalPop.getEncodedType(), sizeOfPop, originalPop.getSingleChromosome(0).genes.length, originalPop.getNumberOfObjectives());
    newPop.initNewPop();
  }

  public void setTournamentSize(int tournamentSize){
    this.tournamentSize = tournamentSize;
  }

  public void setElite(populationI archieve, int numberOfelitle){
    this.archieve = archieve;
    this.numberOfelitle = numberOfelitle;
    eliteSize = archieve.getPopulationSize();
  }

  public void setSecondPopulation(populationI archieve2){
    this.archieve2 = archieve2;
  }

  //simply to determine which one is better by fitness value.
  //the chromosome has smaller fittness value is better from we use the criteria of Goldberg's fitness assignment. (MO)
  public chromosome setData(chromosome chromosome1, chromosome chromosome2){    
    if(chromosome1.getFitnessValue() <= chromosome2.getFitnessValue()){
      return chromosome1;
    }
    return chromosome2;
  }

  public void startToSelect(){
    //to satisfy the sizeOfPop. It apply the elitism first.
    /*
    for(int i = 0 ; i < originalPop.getNumberOfObjectives() ; i ++ ){
       openga.util.algorithm.getMax getmax1 = new openga.util.algorithm.getMax();
       int indexMax = getmax1.getDataIndex(archieve.getObjectiveValueArray(), i);
       newPop.setSingleChromosome(i, archieve.getSingleChromosome(indexMax));
    }
    */
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

  public populationI getSelectionResult(){
    return newPop;
  }

  public populationI getPopulation(){
    return newPop;
  }

  public static void main(String[] args) {
    binaryTournament binaryTournament1 = new binaryTournament();
  }

}