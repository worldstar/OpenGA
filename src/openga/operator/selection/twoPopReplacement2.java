package openga.operator.selection;
import openga.chromosomes.*;
import openga.Fitness.*;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: Suppose there are two populations and we replace the worse solutions of first population (offspring)
 * by the elite solutions of the second population (parent).</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class twoPopReplacement2 {
  public twoPopReplacement2() {
  }
  double thresholdOfFirstPopulation = 0.6;
  double thresholdOfSecondPopulation = 0.3;

  public void setThresholdValues(double thresholdOfFirstPopulation, double thresholdOfSecondPopulation){
    this.thresholdOfFirstPopulation = thresholdOfFirstPopulation;
    this.thresholdOfSecondPopulation = thresholdOfSecondPopulation;
  }

  public populationI replacementStage(populationI[] parent, populationI offspring, int currentPopId){
    int counter = 0;
    //writePopu(offspring);
    for(int i = 0 ; i < offspring.getPopulationSize() ; i ++ ){
      if(offspring.getFitness(i) > thresholdOfFirstPopulation){
        int popId = (int)(Math.random()*parent.length);
        counter = getChromosomeIndex(counter, parent[popId]);
        offspring.setSingleChromosome(i, parent[popId].getSingleChromosome(counter));
        counter ++;
      }
    }
   return offspring;
  }

  private int getPopIndex(int numbPops, int popId){
    int index = (int)(Math.random()*numbPops);
    while(index == popId){
      index = (int)(Math.random()*numbPops);
    }
    return index;
  }

  private int getChromosomeIndex(int counter, populationI parent){
    while(parent.getFitness(counter % parent.getPopulationSize()) < thresholdOfSecondPopulation){
      counter ++;
      counter = counter % parent.getPopulationSize();
    }
    counter = counter % parent.getPopulationSize();
    return counter;
  }

}
