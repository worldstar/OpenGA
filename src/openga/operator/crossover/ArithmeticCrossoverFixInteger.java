package openga.operator.crossover;
import openga.chromosomes.*;

/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: The crossover operator is extended on arithmetic crossover for continuous problem.
 * The data type of the genes is integer. In addition, the total sum of the gene values is fixed.</p>
 * <p>Copyright: Copyright (c) 2009.6.19</p>
 * <p>Company: Nanhua University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class ArithmeticCrossoverFixInteger extends ArithmeticCrossover implements ArithmeticFixIntegerSumCrossoverI{
populationI originalPop, newPop;
  double crossoverRate;
  int popSize, chromosomeLength;
  //java.util.Random r = new java.util.Random(555);

  int fixedSumNumber = 20;   //The total sum is fixed.

  public void setData(int size){

  }

  public void setData(int numberOfJob, int numberOfMachines, int processingTime[][]){
    
  }
  public void setData(double crossoverRate, populationI originalPop){
    this.originalPop = originalPop;
    popSize = originalPop.getPopulationSize();
    System.out.println("originalPop.getLengthOfChromosome() "+originalPop.getLengthOfChromosome());
    newPop = new population();
    newPop.setGenotypeSizeAndLength(originalPop.getEncodedType(), popSize, originalPop.getLengthOfChromosome(), originalPop.getNumberOfObjectives());
    newPop.initNewPop();

    for(int i = 0 ; i < popSize ; i ++ ){
      newPop.setSingleChromosome(i, originalPop.getSingleChromosome(i));
    }
    this.crossoverRate = crossoverRate;
    chromosomeLength = originalPop.getSingleChromosome(0).genes.length;
  }

  public void setFixedSumNumberData(int fixedSumNumber){
    this.fixedSumNumber = fixedSumNumber;
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
      double _Value = _alpha*(newPop.getSingleChromosome(index1).genes[i]) + (1-_alpha)*(newPop.getSingleChromosome(index2).genes[i]);
      int intValue = (int)(_Value);
      newPop.setGene(index1, i, intValue);
    }
    validateVisitingZeroCity(index1);
    validateChromosome(index1);
  }

  private void validateVisitingZeroCity(int index1){
    int sum = 0;
    for(int i = 0 ; i < chromosomeLength ; i ++ ){
      if(newPop.getSingleChromosome(index1).genes[i] == 0){
        newPop.getSingleChromosome(index1).genes[i] = 1;
      }
    }
  }

  private void validateChromosome(int index1){
    int sum = 0;
    for(int i = 0 ; i < chromosomeLength ; i ++ ){
      sum += newPop.getSingleChromosome(index1).genes[i];
    }

    if(sum < fixedSumNumber){     
      int difference = fixedSumNumber - sum;

      //assign the rest of cities to the salesman who takes less visiting amount
      int posIndex = getDataIndex(newPop.getSingleChromosome(index1).genes);
      newPop.getSingleChromosome(index1).genes[posIndex] += difference;
    }
  }

  //to find out where the minimum value is rather than the value itself.
  public int getDataIndex(int vals[]){
    int min = vals[0];
    int index = 0;

    for(int i = 0 ; i < vals.length ; i ++ ){
      if(vals[i] < min){
        min = vals[i];
        index = i;
      }
    }
    return index;
  }
  
  public populationI getCrossoverResult() {
    return newPop;
  }
 
}
