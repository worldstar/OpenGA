package openga.operator.crossover;
import openga.chromosomes.*;

/**
 * @author: Chen, Shih-Hsin
 * <p>Title: The OpenGA project</p>
 * <p>Description: The project is to build general framework of Genetic algorithm and problem independent.</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class twoPointCrossover2 implements CrossoverI{
  public twoPointCrossover2() {
  }

  populationI originalPop, newPop;
  double crossoverRate;
  int popSize, chromosomeLength;
  int cutPoint1, cutPoint2;
  int pos1, pos2;
  //java.util.Random r = new java.util.Random(555);

  /************************************************************************
   * @param crossoverRate The crossover rate.
   * @param _chromosomes It contains two chromosomes to crossover and the two chromsome can be
   * treated as a population. Thus, the transformation way becomes more flexible for test and
   * foundation of multithread purpose.
   * <P></P>
   * Suppose there are two chromosomes and the bit size are 8. The sequence is as following.
   * Chromsome 1: 6 3 7 2 5 0 1 4
   * Chromsome 2: 5 0 4 2 7 3 1 6
   *
   * Then, we random generate two cut points at the position
   * Chromsome 1: 6 3 | 7 2 5 0 | 1 4
   * Chromsome 2: 5 0 | 4 2 7 3 | 1 6
   *
   * Therefore, the new chromosomes are as following:
   * Chromsome 1: 6 3 5 0 2 7 1 4
   * Chromsome 2: 5 0 3 7 2 4 1 6
   ****************************************************************************/

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

  //start to crossover
  public void startCrossover(){
    for(int i = 0 ; i < popSize ; i ++ ){
       //test the probability is larger than crossoverRate.
       if(Math.random() <= crossoverRate){
         //to get the other chromosome to crossover
         int index2 = getCrossoverChromosome(i);
         setCutpoint();
         copyElements(i, index2);
         copyElements(index2, i);
       }
    }
  }

  /**
   * To set the two cut points.
   */
  public final void setCutpoint(){
    boolean theSame = true;
    cutPoint1 = (int)(Math.random() * chromosomeLength);
    cutPoint2 = (int)(Math.random() * chromosomeLength);
    //cutPoint1 = 3; //default for test
    //cutPoint2 = 6; //default for test

    if(cutPoint1 == cutPoint2){
      //double temp = r.nextDouble();
      //decrease the position of cutPoint1
      cutPoint1 -=  (int)(Math.random()*cutPoint1);
      //increase the position of cutPoint2
      cutPoint2 += (int)((chromosomeLength - cutPoint2)*Math.random());

      //double check it.
      if(cutPoint1 == cutPoint2){
        setCutpoint();
      }
    }

    //swap
    if(cutPoint1 > cutPoint2){
      int temp = cutPoint2;
      cutPoint2 = cutPoint1;
      cutPoint1 = temp;
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

  /**
   * The two chromosomes produce a new offspring
   * @param index1 The first chromosome to crossover
   * @param index2 The second chromosome to crossover
   */
  private void copyElements(int index1, int index2){
    //to modify the first chromosome between the index1 to index2, which genes
    //is from chromosome 2.
    int counter = 0;
    for(int i = cutPoint1 ; i <= cutPoint2; i ++ ){
      while(checkConflict(newPop.getSingleChromosome(index2).genes[counter], newPop.getSingleChromosome(index1).genes) == true){
        counter ++;
      }
      newPop.setGene(index1, i, newPop.getSingleChromosome(index2).genes[counter]);
      counter ++;
    }
  }

  /**
   * if there is the same gene, it return the index of the gene.
   * Else, default value is -1, which is also mean don't have the same gene
   * during cutpoint1 and cutpoint2.
   * @param newGene
   * @param _chromosome
   * @return
   */
  private boolean checkConflict(int newGene, int _chromosome[]){
    boolean hasConflict = false;
    for(int i = 0 ; i < cutPoint1 ; i ++ ){
      if(newGene == _chromosome[i]){
        return true;
      }
    }

    for(int i = cutPoint2 + 1 ; i < chromosomeLength ; i ++ ){
      if(newGene == _chromosome[i]){
        return true;
      }
    }

    return hasConflict;
  }

  public final populationI getCrossoverResult(){
    return newPop;
  }
/*
  //for test only
  public static void main(String[] args) {
    CrossoverI twoPointCrossover21 = new twoPointCrossover2();
    openga.util.printClass printClass1 = new openga.util.printClass();
    populationI population1 = new population();
    populationI newPop = new population();
    int size = 3, length = 6;

    population1.setGenotypeSizeAndLength(true, size, length, 2);
    population1.createNewPop();
    for(int i = 0 ; i < size ; i ++ ){
       printClass1.printMatrix(""+i,population1.getSingleChromosome(i).genes);
    }


    twoPointCrossover21.setData(0.9, population1);
    twoPointCrossover21.startCrossover();
    newPop = twoPointCrossover21.getCrossoverResult();

    for(int i = 0 ; i < size ; i ++ ){
       printClass1.printMatrix(""+i,newPop.getSingleChromosome(i).genes);
    }
  }
*/
}