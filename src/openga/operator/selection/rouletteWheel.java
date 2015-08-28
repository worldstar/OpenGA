package openga.operator.selection;

import openga.chromosomes.*;
import openga.Fitness.FitnessI;
//import openga.util.printClass;
/**
 * <p>Title: The OpenGA project</p>
 * <p>Description: The roulette wheel works on whole population.
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class rouletteWheel implements SelectI{
  public rouletteWheel() {
  }

  //how many chromosomes should be copied. Originally, it is equal to population size.
  //However, if we want to modify the size, we should set the parameter.
  //For example, the initial population is larger which is compared with working population.
  //So we can shrink the initial population into the size we use.
  int sizeOfPop;
  int originalPopSize;
  //The new population is created by the method.
  //Besides, the newPopulation size is equal to sizeOfPop.
  populationI newPop, originalPop;
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
    newPop = new population();
    newPop.setGenotypeSizeAndLength(originalPop.getEncodedType(), originalPop.getPopulationSize(),originalPop.getLengthOfChromosome(),2);
    newPop.initNewPop();
  }

  public void setParentAndOffspring(int sizeOfPop, populationI parent, populationI offspring){

  }

  public void setSecondPopulation(populationI archieve2){
    this.archieve2 = archieve2;
  }

  public void startToSelect(){
    //to satisfy the sizeOfPop.
    for(int i = 0 ; i < sizeOfPop ; i ++ ){
      int index1 = getIndex();
      //System.out.print(index1+"\n");
      newPop.setSingleChromosome(i, originalPop.getSingleChromosome(index1));
    }
  }

  private int getIndex(){
    int index = (int)(originalPopSize*Math.random());//where to start and current position
    double cumulated_fitness = 0;                    //to summarize chromosomes' fitness.
    //If the cumulated_fitness >= criticalVal, select the chromosome.
    double criticalVal = Math.random();

    while(true){
      if(index == originalPopSize){
        index = 0;
      }

      cumulated_fitness += originalPop.getFitness(index);
      if(cumulated_fitness >= criticalVal){
        //select the current chromosome.
        return index;
      }
      else{
        index ++;
      }
    }//end while

  }

  public populationI getSelectionResult(){
    return newPop;
  }

  public static void main(String[] args) {
    rouletteWheel rouletteWheel1 = new rouletteWheel();
    //to create a population
    population population1 = new population();
    int size = 15, length = 10;
    population1.setGenotypeSizeAndLength(true, size, length,2);
    population1.createNewPop();
    /*
    printClass printClass1 = new printClass();

    //to generate test objective values of each chromosome
    for(int i = 0 ; i < size ; i ++ ){
      double _obj[] = {(Math.random()*50),0};
      population1.setObjectiveValue(i, _obj);
    }

    //to calculate the fitness value
    singleObjFitness singleObjFitness1 = new singleObjFitness();
    singleObjFitness1.setData(population1);
    singleObjFitness1.calculateFitness();
    population1 = singleObjFitness1.getFitnessResult();

    for(int i = 0 ; i < size ; i ++ ){
       System.out.print(population1.getFitness(i)+"\n");
    }
    rouletteWheel1.setData(population1);
    rouletteWheel1.startToSelect();
    */
  }

  public void setTournamentSize(int tournamentSize) {
  }

  public void setElite(populationI archieve, int numberOfelitle) {
  }
}