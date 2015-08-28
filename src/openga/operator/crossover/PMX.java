package openga.operator.crossover;
import openga.chromosomes.*;
/**
 * <p>Title: The OpenGA project</p>
 * <p>Description: The project is to build general framework of Genetic algorithm and problem independent.</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class PMX extends twoPointCrossover2{
  public PMX() {
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
         //System.out.println("Finish the crossover");
       }
    }
  }

  private void copyElements(int index1, int index2){
    //to modify the first chromosome between the index1 to index2, which genes
    //is from chromosome 2.
    int counter = 0;
    //exchange the gene values between the two points
    for(int i = cutPoint1 ; i <= cutPoint2; i ++ ){
      int gene = originalPop.getSingleChromosome(index2).genes[i];
      newPop.setGene(index1, i, gene);
    }

    //copy other gene that before the cutpoint1 and after the cutpoint2
    for(int i = 0 ; i < cutPoint1; i ++ ){
      while(checkConflict(originalPop.getSingleChromosome(index1).genes[counter], originalPop.getSingleChromosome(index2).genes) == true){
        counter ++;
      }
      newPop.setGene(index1, i, originalPop.getSingleChromosome(index1).genes[counter]);
      counter ++;
    }

    for(int i = cutPoint2+1 ; i < chromosomeLength ; i ++ ){
      while(checkConflict(originalPop.getSingleChromosome(index1).genes[counter], originalPop.getSingleChromosome(index2).genes) == true){
        counter ++;
      }
      newPop.setGene(index1, i, originalPop.getSingleChromosome(index1).genes[counter]);
      counter ++;
    }
  }

  private boolean checkConflict(int newGene, int _chromosome[]){
    boolean hasConflict = false;
    for(int i = cutPoint1 ; i <= cutPoint2 ; i ++ ){
      if(newGene == _chromosome[i]){
        return true;
      }
    }
    return hasConflict;

  }

  public static void main(String[] args) {
    PMX twoPointCrossover1 = new PMX();
    populationI population1 = new population();
    int size = 2, length = 8;
    population1.setGenotypeSizeAndLength(true, size, length, 2);
    population1.createNewPop();
    openga.util.printClass printClass1 = new openga.util.printClass();

    for(int i = 0 ; i < size ; i ++ ){
       printClass1.printMatrix(""+i,population1.getSingleChromosome(i).genes);
    }

    twoPointCrossover1.setData(0.85, population1);
    twoPointCrossover1.startCrossover();
    population1 = twoPointCrossover1.getCrossoverResult();

    for(int i = 0 ; i < size ; i ++ ){
       printClass1.printMatrix(""+i,population1.getSingleChromosome(i).genes);
    }

  }

}