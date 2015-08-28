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

public class twoPopReplacement {
  public twoPopReplacement() {
  }
  double thresholdOfFirstPopulation = 0.6;
  double thresholdOfSecondPopulation = 0.3;

  public void setThresholdValues(double thresholdOfFirstPopulation, double thresholdOfSecondPopulation){
    this.thresholdOfFirstPopulation = thresholdOfFirstPopulation;
    this.thresholdOfSecondPopulation = thresholdOfSecondPopulation;
  }

  public populationI replacementStage2(populationI parent, populationI offspring, FitnessI Fitness){
    openga.chromosomes.combinedTwoPopulations combinedTwoPopulations1 = new openga.chromosomes.combinedTwoPopulations();
    combinedTwoPopulations1.setTwoPopulations(parent, offspring);
    combinedTwoPopulations1.startToCombine();
    populationI combinedPop = combinedTwoPopulations1.getPopulation();

    //to get the sorting fitness sequence of the combined population.
    int seq[] = new int[combinedPop.getPopulationSize()];
    double fitnessValue[] = new double[combinedPop.getPopulationSize()];
    for(int i = 0 ; i < combinedPop.getPopulationSize() ; i ++ ){
      fitnessValue[i] = combinedPop.getFitness(i);
      //System.out.println(i+"\t"+fitnessValue[i]);
    }
    openga.util.sort.selectionSort selectionSort1 = new openga.util.sort.selectionSort();
    selectionSort1.setNomialData(seq);
    selectionSort1.setData(fitnessValue);
    selectionSort1.selectionSort_withNomial();

    for(int i = 0 ; i < offspring.getPopulationSize() ; i ++ ){
      offspring.setSingleChromosome(i, combinedPop.getSingleChromosome(seq[i]));
      //System.out.println(offspring.getFitness(i));
    }
    System.exit(0);
   return offspring;
  }

  public populationI replacementStage(populationI parent, populationI offspring){
    int counter = 0;
    //writePopu(offspring);
    for(int i = 0 ; i < offspring.getPopulationSize() ; i ++ ){
      if(offspring.getFitness(i) > thresholdOfFirstPopulation){
        counter = getPopIndex(counter, parent);
        //System.out.println(i+"\t"+offspring.getFitness(i)+"\t"+counter+"\t"+parent.getFitness(counter));
        offspring.setSingleChromosome(i, parent.getSingleChromosome(counter));
        //offspring.setSingleChromosome(i, generateNewSolution(parent.getSingleChromosome(counter)));
        counter ++;
      }
    }
   return offspring;
  }

  private int getPopIndex(int counter, populationI parent){
    counter = counter % parent.getPopulationSize();
    //thresholdOfSecondPopulation = 0.5;
    while(parent.getFitness(counter) < thresholdOfSecondPopulation){
      counter ++;
      counter = counter % parent.getPopulationSize();
    }
    counter = counter % parent.getPopulationSize();
    return counter;
  }

  /**
   * @param chromosome1 The identical solution
   * @return To generate new solution by swap mutation operator.
   */
  public chromosome generateNewSolution(chromosome chromosome1){
    openga.operator.mutation.swapMutation Mutation1 = new openga.operator.mutation.swapMutation();
    Mutation1.chromosomeLength = chromosome1.getLength();
    Mutation1.setCutpoint();
    return Mutation1.swaptGenes(chromosome1);
  }
}