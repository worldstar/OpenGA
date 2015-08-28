package openga.operator.mutation;
import openga.chromosomes.*;
/**
 * <p>Title: The OpenGA project</p>
 * <p>Description: The project is to build general framework of Genetic algorithm and problem independent.</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 * Suppose there is 8 bits of a chromosome. The sequence is as following.
 * Chromsome 1: 6 3 7 2 5 0 1 4
 *
 * Then, we random generate two cut points at the position
 * Chromsome 1: 6 3 | 7 2 5 0 | 1 4
 *
 * Therefore, the new chromosome becomes:
 * Chromsome 1: 6 3 0 5 2 7 1 4
 */

public class inverseMutation implements MutationI{
  public inverseMutation() {
  }

  public populationI pop;                 //mutation on whole population
  public double mutationRate;            //mutation rate
  public int popSize, chromosomeLength;  //size of population and number of digits of chromosome
  public int cutPoint1, cutPoint2;       //the genes between the two points are inversed

  public void setData(double mutationRate, populationI population1){
    pop = new population();
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
       //test the probability is larger than crossoverRate.
       if(Math.random() <= mutationRate){
         setCutpoint();
         pop.setChromosome(i,inverseGenes(pop.getSingleChromosome(i)));
       }
    }
  }

  public final void setCutpoint(){
    boolean theSame = true;
    cutPoint1 = (int)(Math.random() * chromosomeLength);
    cutPoint2 = (int)(Math.random() * chromosomeLength);
    //cutPoint1 = 3; //default for test
    //cutPoint2 = 6; //default for test

    if(cutPoint1 == cutPoint2){
      cutPoint1 -=  (int)(Math.random()*cutPoint1);
      //increase the position of cutPoint2
      cutPoint2 += (int)((chromosomeLength - cutPoint2)*Math.random());

      //double check it.
      if(cutPoint1 == cutPoint2){
        //setCutpoint();
      }
    }

    //swap
    if(cutPoint1 > cutPoint2){
      int temp = cutPoint2;
      cutPoint2 = cutPoint1;
      cutPoint1 = temp;
    }
  }


  public final chromosome inverseGenes(chromosome _chromosome){
    int length = cutPoint2 - cutPoint1  + 1;
    int backupGenes[] = new int[length];
    int counter = 0;

    //store the genes at backupGenes.
    for(int i = cutPoint1 ; i <= cutPoint2 ; i ++ ){
      backupGenes[counter++] = _chromosome.genes[i];
    }

    counter = 0;
    //write data of backupGenes into the genes
    for(int i = cutPoint2; i >= cutPoint1 ; i --){
      _chromosome.genes[i] = backupGenes[counter++];
    }
    return _chromosome;
  }

  public populationI getMutationResult(){
    return pop;
  }

  public static void main(String[] args) {
    inverseMutation inverseMutation1 = new inverseMutation();
    populationI population1 = new population();
    populationI newPop = new population();
    int size = 2, length = 10;
    openga.util.printClass printClass1 = new openga.util.printClass();

    population1.setGenotypeSizeAndLength(true, size, length,2);
    population1.createNewPop();

    for(int i = 0 ; i < size ; i ++ ){
       printClass1.printMatrix(""+i,population1.getSingleChromosome(i).genes);
    }

    double mutationRate = 0.95;
    inverseMutation1.setData(mutationRate, population1);
    inverseMutation1.startMutation();

    newPop = inverseMutation1.getMutationResult();

    for(int i = 0 ; i < size ; i ++ ){
       printClass1.printMatrix(""+i,newPop.getSingleChromosome(i).genes);
    }

  }

}