package openga.operator.mutation;
import openga.chromosomes.*;

/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class bitFlipMutation implements MutationI {
  public bitFlipMutation() {
  }
  public populationI pop;                 //mutation on whole population
  public double mutationRate;            //mutation rate
  public int popSize, chromosomeLength;  //size of population and number of digits of chromosome

  public void setData(double mutationRate, populationI population1){
    //pop = new population();
    this.pop = population1;
    this.mutationRate = mutationRate;
    popSize = population1.getPopulationSize();
    chromosomeLength = population1.getSingleChromosome(0).genes.length;
  }

  public void setData(populationI population1){
    pop = new population();
    this.pop = population1;
    popSize = population1.getPopulationSize();
    chromosomeLength = population1.getSingleChromosome(0).genes.length;
  }

  public void startMutation(){
    for(int i = 0 ; i < popSize ; i ++ ){
      for(int j = 0 ; j < chromosomeLength ; j ++ ){
        if(Math.random() <= mutationRate){
          if(pop.getSingleChromosome(i).getSolution()[j] == 0){
            pop.getSingleChromosome(i).getSolution()[j] = 1;
          }
          else{
            pop.getSingleChromosome(i).getSolution()[j] = 0;
          }
        }
      }
    }
  }

  public populationI getMutationResult() {
    return pop;
  }
/*
  public static void main(String[] args) {
    MutationI Mutation1 = new bitFlipMutation();
    openga.util.printClass printClass1 = new openga.util.printClass();
    populationI population1 = new population();
    populationI newPop = new population();
    int size = 2, length = 6;

    population1.setGenotypeSizeAndLength(false, size, length, 2);
    population1.createNewPop();
    for(int i = 0 ; i < size ; i ++ ){
       printClass1.printMatrix(""+i,population1.getSingleChromosome(i).genes);
    }

    Mutation1.setData(0.9, population1);
    Mutation1.startMutation();
    newPop = Mutation1.getMutationResult();

    for(int i = 0 ; i < size ; i ++ ){
       printClass1.printMatrix(""+i,newPop.getSingleChromosome(i).genes);
    }
  }
*/
}