/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package openga.operator.crossover;
import openga.chromosomes.*;
/**
 *
 * @author user
 */
public class CyclingCrossoverP implements CrossoverI{
public CyclingCrossoverP() {
  }
populationI originalPop, newPop;
  double crossoverRate;
  int popSize, chromosomeLength;
  int cutPoint1, cutPoint2;
  int pos1, pos2;

  int ran,ran1;
  int chromosomeP1[], chromosomeP2[], chromosomeC1[], chromosomeC2[];

  public void setdata(){

      chromosomeC1 = new int[chromosomeLength];
      chromosomeC2 = new int[chromosomeLength];
      chromosomeP1 = new int[chromosomeLength];
      chromosomeP2 = new int[chromosomeLength];

      for(int i = 0 ; i < popSize ; i ++ ){
          for(int j = 0 ; j < chromosomeLength ; j ++ ){
              chromosomeC1[j]=-1;
              chromosomeC2[j]=-1;
              
              if(i==0){
                  chromosomeP1[j]=originalPop.getSingleChromosome(i).genes[j];
              }else{
                  chromosomeP2[j]=originalPop.getSingleChromosome(i).genes[j];                  
              }              
          }
      }      
    }
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
  public void randomdata(){

      ran=(int)(Math.random() * 10);
      while((ran >= chromosomeLength) ){
          ran=(int)(Math.random() * 10);
      }      
    }

int dataret(int value){
    int position=-1;
    for(int i = 0 ; i<chromosomeLength;i++){
        if(value==chromosomeP1[i]){
            position=i;
        }
    }    
    return position;
}
  public void startCrossover(){
      //seek random position
      randomdata();

      setdata();
      
      //copy parent chromosome to child
    chromosomeC1[ran]=chromosomeP1[ran];
    chromosomeC2[ran]=chromosomeP2[ran];
    
    ran1=dataret(chromosomeP2[ran]);

    while(ran1!=ran){       
        chromosomeC1[ran1]=chromosomeP1[ran1];
        chromosomeC2[ran1]=chromosomeP2[ran1];

        ran1=dataret(chromosomeP2[ran1]);
    }
      
    //treat else child chromosome position
    for(int i = 0 ; i < chromosomeLength ; i ++){
        if(chromosomeC1[i]==-1){
            chromosomeC2[i]=chromosomeP1[i];
            chromosomeC1[i]=chromosomeP2[i];
        }
    }
    //put Chromosome to newPop
    for(int i = 0 ; i < popSize ; i ++){
        for(int j = 0 ; j < chromosomeLength ; j ++){
           
            newPop.getSingleChromosome(i).genes[j]=chromosomeC1[j];
            if(i==0){

                newPop.getSingleChromosome(i).genes[j]=chromosomeC1[j];
            }else{
                newPop.getSingleChromosome(i).genes[j]=chromosomeC2[j];
                 
            }
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

  public final populationI getCrossoverResult(){
    return newPop;
  }

  //for test only
  public static void main(String[] args) {
    CrossoverI CyclingCrossoverP1 = new CyclingCrossoverP();
    openga.util.printClass printClass1 = new openga.util.printClass();
    populationI population1 = new population();
    populationI newPop = new population();
    //set variable
    int size = 5, length = 10;

    //set variable for opena.chromosomes.population1
    population1.setGenotypeSizeAndLength(true, size, length, 2);

    //generate random population
    population1.createNewPop();

    //generate random population
    for(int i = 0 ; i < size ; i ++ ){
       printClass1.printMatrix(""+i,population1.getSingleChromosome(i).genes);
    }

    /*CyclingCrossoverI1.setData(0.9, population1);
     set variable for CyclingCrossoverI1
     includes originalPop¡BpopSize¡BnewPop¡BcrossoverRate¡BchromosomeLength
    */
    CyclingCrossoverP1.setData(0.9, population1);

    //start Crossover
    CyclingCrossoverP1.startCrossover();

    //get Crossover Result
    newPop = CyclingCrossoverP1.getCrossoverResult();

    ////print population after Crossover
    for(int i = 0 ; i < size ; i ++ ){
       printClass1.printMatrix(""+i,newPop.getSingleChromosome(i).genes);
    }
  }


}
