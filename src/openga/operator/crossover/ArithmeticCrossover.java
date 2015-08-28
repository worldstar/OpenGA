package openga.operator.crossover;
import openga.chromosomes.*;

/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: The crossover operator is called arithmetic crossover for continuous problem.</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class ArithmeticCrossover implements CrossoverI{
  public ArithmeticCrossover() {
  }
  populationI originalPop, newPop;
  double crossoverRate;
  int popSize, chromosomeLength;
  //java.util.Random r = new java.util.Random(555);


  public void setData(double crossoverRate, chromosome _chromosomes[],int numberOfObjs){
    //transfomation the two chromosomes into a population.
    population _pop = new population();
    _pop.setGenotypeSizeAndLength(_chromosomes[0].getEncodeType(), _chromosomes.length, _chromosomes[0].getLength(), numberOfObjs);
    _pop.initNewPop();
    for(int i = 0 ; i < _chromosomes.length ; i ++ ){
      _pop.setChromosome(i, _chromosomes[i]);
    }
    setData(crossoverRate, _pop);
  }

  public void setData(double crossoverRate, populationI originalPop){
    this.originalPop = originalPop;
    popSize = originalPop.getPopulationSize();
    //System.out.println("originalPop.getLengthOfChromosome() "+originalPop.getLengthOfChromosome());
    newPop = new population();
    newPop.setGenotypeSizeAndLength(originalPop.getEncodedType(), popSize, originalPop.getLengthOfChromosome(), originalPop.getNumberOfObjectives());
    newPop.initNewPop();

    for(int i = 0 ; i < popSize ; i ++ ){
      newPop.setSingleChromosome(i, originalPop.getSingleChromosome(i));
    }
    this.crossoverRate = crossoverRate;
    chromosomeLength = originalPop.getSingleChromosome(0).genes.length;
  }
public void setData(int numberOfJob, int numberOfMachines, int processingTime[][]){

}

    public void setData(int numberofParents){

    }
  //start to crossover
  public void startCrossover(){
    for(int i = 0 ; i < popSize ; i ++ ){
       //test the probability is larger than crossoverRate.
       if(Math.random() <= crossoverRate){
         //to get the other chromosome to crossover
         int index2 = getCrossoverChromosome(i);
         double alpha = Math.random();
         copyElements(i, index2, alpha);
         copyElements(index2, i, alpha);
       }
    }
  }

  /**
   * The two chromosomes produce a new offspring.
   * @param index1 The first chromosome to crossover
   * @param index2 The second chromosome to crossover
   * @param _alpha The exchange factor for first gene and (1-alpha) for second chromosome.
   */
  private void copyElements(int index1, int index2, double _alpha){
    //to modify the first chromosome between the index1 to index2, which genes
    //is from chromosome 2.

    for(int i = 0 ; i < chromosomeLength ; i ++ ){
      double _Value = _alpha*(newPop.getSingleChromosome(index1).realGenes[i]) + (1-_alpha)*(newPop.getSingleChromosome(index2).realGenes[i]);
      newPop.setGene(index1, i, _Value);
    }
  }

  /**
   * To set the two cut points.
   * It won't work here.
   */
  private void setCutpoint(){
  }


  /**
   * To get the other chromosome to crossover.
   * @param index The index of original chromosome.
   * @return
   */
  public final int getCrossoverChromosome(int index){
    int index2 = (int)(Math.random()*popSize);
    /*
    if(index == index2){
      index2 = getCrossoverChromosome(index);
    }
     */
    return index2;
  }

  public populationI getCrossoverResult(){
    return newPop;
  }

}