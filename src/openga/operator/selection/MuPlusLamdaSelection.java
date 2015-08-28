package openga.operator.selection;
import openga.chromosomes.*;

/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class MuPlusLamdaSelection implements SelectI {
  public MuPlusLamdaSelection() {
  }

  //The new population is created by the method.
  //Besides, the newPopulation size is equal to sizeOfPop.
  populationI newPop, originalPop;//originalPop is parent and offspring.
  populationI archieve;
  int sizeOfPop;
  int originalPopSize, eliteSize = 0;
  int numberOfelitle = 0;
  int counter = 0;
  populationI archieve2;

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
    int length = originalPop.getSingleChromosome(0).genes.length;
    newPop.setGenotypeSizeAndLength(originalPop.getEncodedType(), sizeOfPop, length, originalPop.getNumberOfObjectives());
    newPop.initNewPop();
  }

  public void setTournamentSize(int tournamentSize) {
    /**@todo Implement this openga.operator.selection.SelectI method*/
    throw new java.lang.UnsupportedOperationException("Method setTournamentSize() not yet implemented.");
  }

  public void setSecondPopulation(populationI archieve2){
    this.archieve2 = archieve2;
  }

  public void setElite(populationI archieve, int numberOfelitle){
    this.archieve = archieve;
    this.numberOfelitle = numberOfelitle;
    eliteSize = archieve.getPopulationSize();
  }

  private int getBetterChromosome(double avgfitness, populationI _pop){
    int index = 0;
    while(true){
      counter = counter % _pop.getPopulationSize();
      if(_pop.getFitness(counter) <= avgfitness){
        return index;
      }
      counter ++;
    }
  }

  public void startToSelect() {
    //double avgfitness = getAverageFitness();
    //avgfitness += (1 + avgfitness*0.5);
    //to satisfy the sizeOfPop. It apply the elitism first.
    for(int i = 0 ; i < numberOfelitle ; i ++ ){
      int index1 = (int)(Math.random()*eliteSize);//randomly select two chromosomes to compare.
      newPop.setSingleChromosome(i, archieve.getSingleChromosome(index1));
    }

    int index[] = sortPopulation();
    int counter = 0;
    for(int i = numberOfelitle ; i < sizeOfPop ; i ++ ){
      //int index1 = getBetterChromosome(avgfitness, originalPop);
      chromosome selectedChromosome1 = originalPop.getSingleChromosome(index[counter++]);
      //chromosome selectedChromosome1 = originalPop.getSingleChromosome(index1);
      newPop.setSingleChromosome(i, selectedChromosome1);
    }
  }

  private double getAverageFitness(){
    double avg = 0, sum = 0;
    int total = originalPop.getPopulationSize();
    for(int i = 0 ; i < originalPop.getPopulationSize() ; i ++ ){
      sum += originalPop.getSingleChromosome(i).getFitnessValue();
      //System.out.println(originalPop.getSingleChromosome(i).getFitnessValue());
    }
    //System.out.println("sum "+sum+" total "+total+" avgfitness "+sum/(double)total);
    return sum/(double)total;
  }

  private int[] sortPopulation(){
    double fitnessArray[] = originalPop.getFitnessValueArray();
    int index[] = new int[originalPop.getPopulationSize()];
    for(int i = 0 ; i < originalPop.getPopulationSize() ; i ++ ){
      index[i] = i;
    }
    openga.util.sort.selectionSort selectionSort1 = new openga.util.sort.selectionSort();
    selectionSort1.setData(fitnessArray);
    selectionSort1.setNomialData(index);
    selectionSort1.selectionSort_withNomial();
    return selectionSort1.getNomialData();
  }

  public populationI getSelectionResult(){
    return newPop;
  }

}