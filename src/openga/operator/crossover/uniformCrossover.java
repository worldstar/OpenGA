package openga.operator.crossover;
import openga.chromosomes.*;


/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class uniformCrossover implements CrossoverI {
  public uniformCrossover() {
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

  public void setData(int numberofParents){
      System.out.println("This progam has not supported the public void setData(int numberofParents) in openga.operator.crossover. ");
      System.out.println("The program will exit.");
      System.exit(0);
  }

  public void setData(int numberOfJob, int numberOfMachines, int processingTime[][]){
      System.out.println("This progam has not supported the public void setData(int numberOfJob, int numberOfMachines, int processingTime[][]) in openga.operator.crossover. ");
      System.out.println("The program will exit.");
      System.exit(0);
  }

  public void startCrossover() {
    for(int i = 0 ; i < popSize ; i ++ ){
       //test the probability is larger than crossoverRate.
       if(Math.random() <= crossoverRate){
         //to get the other chromosome to crossover
         int index2 = getCrossoverChromosome(i);
         int vector1[] = generateRandomVector();
         copyElements(i, index2, vector1);
         copyElements(index2, i, vector1);
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

  private int[] generateRandomVector(){
    int vector1[] = new int[chromosomeLength];
    for(int i = 0 ; i < chromosomeLength ; i ++ ){
      if(Math.random() > 0.5){
        vector1[i] = 1;
      }
      else{
        vector1[i] = 0;
      }
    }
    /*
    System.out.println("Vector");
    for(int i = 0 ; i < chromosomeLength ; i ++ ){
      System.out.print(vector1[i]+" ");
    }
    System.out.println("\n");
    */
    return vector1;
  }

  private void copyElements(int index1, int index2, int vector1[]){
    //to modify the first chromosome between the index1 to index2, which genes
    //is from chromosome 2.
    int counter = 0;

    for(int i = 0 ; i < chromosomeLength; i ++ ){
      if(vector1[i] == 0){
        //System.out.print(i+": "+originalPop.getSingleChromosome(index1).genes[i]+"->"+originalPop.getSingleChromosome(index2).genes[i]+"\t");
        newPop.setGene(index1, i, originalPop.getSingleChromosome(index2).genes[i]);
      }
    }
    //System.out.println();
  }

  public populationI getCrossoverResult(){
    return newPop;
  }

  public static void main(String[] args) {
    CrossoverI uniformCrossover1 = new uniformCrossover();
    openga.util.printClass printClass1 = new openga.util.printClass();
    populationI population1 = new population();
    populationI newPop = new population();
    int size = 2, length = 6;

    population1.setGenotypeSizeAndLength(false, size, length, 2);
    population1.createNewPop();
    for(int i = 0 ; i < size ; i ++ ){
       printClass1.printMatrix(""+i,population1.getSingleChromosome(i).genes);
    }

    uniformCrossover1.setData(1, population1);
    uniformCrossover1.startCrossover();
    newPop = uniformCrossover1.getCrossoverResult();

    for(int i = 0 ; i < size ; i ++ ){
       printClass1.printMatrix("Offspring "+i,newPop.getSingleChromosome(i).genes);
    }
  }

}