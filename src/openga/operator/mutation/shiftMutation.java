package openga.operator.mutation;
import openga.chromosomes.*;
/**
 * <p>Title: The OpenGA project</p>
 * <p>Description: The project is to build general framework of Genetic algorithm and problem independent.</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class shiftMutation extends inverseMutation{
  public shiftMutation() {
  }

  public void setData(double mutationRate, populationI population1){
    pop = new population();
    this.pop = population1;
    this.mutationRate = mutationRate;
    popSize = population1.getPopulationSize();
    chromosomeLength = population1.getSingleChromosome(0).genes.length;
  }

  public void startMutation(){
    for(int i = 0 ; i < popSize ; i ++ ){
       //test the probability is larger than crossoverRate.
       if(Math.random() <= mutationRate){
         setCutpoint();
         pop.setChromosome(i,shiftGenes(pop.getSingleChromosome(i)));
       }
    }
  }

  public final chromosome shiftGenes(chromosome _chromosome){
    int length = cutPoint2 - cutPoint1  + 1;
    int backupGenes[] = new int[length];
    int counter = 0;

    //store the genes at backupGenes.
    for(int i = cutPoint1 ; i <= cutPoint2 ; i ++ ){
      backupGenes[counter++] = _chromosome.genes[i];
    }

    //assgin the gene at the end of the range to the place of the range.
    _chromosome.genes[cutPoint1] = backupGenes[backupGenes.length - 1];
    counter = 0;

    //write data of backupGenes into the genes
    for(int i = cutPoint1 + 1 ; i <= cutPoint2 ; i ++){
      _chromosome.genes[i] = backupGenes[counter++];
    }
    return _chromosome;
  }


  public static void main(String[] args) {

    shiftMutation shiftMutation1 = new shiftMutation();
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
    shiftMutation1.setData(mutationRate, population1);
    shiftMutation1.startMutation();

    newPop = shiftMutation1.getMutationResult();

    for(int i = 0 ; i < size ; i ++ ){
       printClass1.printMatrix(""+i,newPop.getSingleChromosome(i).genes);
    }

  }

}