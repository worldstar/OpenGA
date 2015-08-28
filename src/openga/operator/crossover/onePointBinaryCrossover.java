package openga.operator.crossover;
import openga.chromosomes.chromosome;
import openga.chromosomes.*;

/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class onePointBinaryCrossover implements CrossoverI {
  public onePointBinaryCrossover() {
  }
  populationI originalPop, newPop;
  double crossoverRate;
  int popSize, chromosomeLength;

  public void setData(double crossoverRate, chromosome[] _chromosomes, int numberOfObjs) {
    //transfomation the two chromosomes into a population.
    population _pop = new population();
    _pop.setGenotypeSizeAndLength(_chromosomes[0].getEncodeType(), _chromosomes.length, _chromosomes[0].getLength(), numberOfObjs);
    _pop.initNewPop();
    for(int i = 0 ; i < _chromosomes.length ; i ++ ){
      _pop.setChromosome(i, _chromosomes[i]);
    }
    setData(crossoverRate, _pop);
  }

  public void setData(double crossoverRate, populationI originalPop) {
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

  public void startCrossover() {
    for(int i = 0 ; i < popSize ; i ++ ){
       //test the probability is larger than crossoverRate.
       if(Math.random() <= crossoverRate){
         int cutPoint = (int)(Math.random()*chromosomeLength);
         //to get the other chromosome to crossover
         int index2 = getCrossoverChromosome(i);
         copyElements(i, index2, cutPoint);
         copyElements(index2, i, cutPoint);
       }
    }
  }

  /**
   * To get the other chromosome to crossover.
   * @param index The index of original chromosome.
   * @return
   */
  public final int getCrossoverChromosome(int index){
    int index2 = (int)(Math.random()*popSize);
    if(index == index2){
      index2 = getCrossoverChromosome(index);
    }
    return index2;
  }

  private void copyElements(int index1, int index2, int cutPoint){
    //to modify the first chromosome between the index1 to index2, which genes
    //is from chromosome 2.
    int counter = 0;

    for(int i = 0 ; i < chromosomeLength; i ++ ){
      if(i < cutPoint){
        //System.out.print(i+": "+originalPop.getSingleChromosome(index1).genes[i]+"->"+originalPop.getSingleChromosome(index2).genes[i]+"\t");
        newPop.setGene(index1, i, originalPop.getSingleChromosome(index2).genes[i]);
      }
    }
    //System.out.println();
  }

  public populationI getCrossoverResult(){
    return newPop;
  }

    @Override
    public void setData(int numberOfJob, int numberOfMachines, int[][] processingTime) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setData(int numberofParents) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}