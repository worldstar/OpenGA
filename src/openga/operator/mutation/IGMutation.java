/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package openga.operator.mutation;

import java.util.ArrayList;
import java.util.Random;
import openga.chromosomes.chromosome;
import openga.chromosomes.population;
import openga.chromosomes.populationI;

/**
 *
 * @author Guo Yu-Cheng
 */
public class IGMutation{
  
  private int numberOfSalesmen;
  
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
  
  public void startMutation(int test){
    chromosome _chromosome = new chromosome();
    _chromosome.genes = new int [] {4,9,0,2,5,7,3,8,1,6};
    
    for(int i = 0 ; i < _chromosome.genes.length ; i ++ ){
       System.out.print(_chromosome.genes[i]+" ");
    }
    
    for(int i = 0 ; i < popSize ; i ++ ){
       //test the probability is larger than crossoverRate.
         pop.setChromosome(i,iterateGenes(_chromosome,1));
         System.out.println("i = "+i);
    }

  }
  
  public void startMutation(){
    for(int i = 0 ; i < popSize ; i ++ ){
       //test the probability is larger than crossoverRate.
       if(Math.random() <= mutationRate){
         pop.setChromosome(i,iterateGenes(pop.getSingleChromosome(i),1));
         System.out.println("i = "+i);
       }
    }
  }
  
  public chromosome iterateGenes(chromosome _chromosome,int number)
  {
    //set random
    int takeout,putin ;
    Random random = new Random();

    ArrayList savevalue = new ArrayList();
    ArrayList list = new ArrayList();
    for(int i = 0 ; i < _chromosome.genes.length ; i++)
    {
      list.add(_chromosome.genes[i]);
    }

    for(int count = 0 ; count < number ; count++ ){
    //takeout random
        takeout = random.nextInt(list.size());
    //save removed value
        savevalue.add(list.get(takeout));
    //remove random list
        list.remove(takeout);
        }
    putin = random.nextInt(list.size());
    for(int i=0 ; i < savevalue.size() ; i++)
    {
        list.add(putin,savevalue.get(i));
    }
    
    for(int i = 0 ; i < _chromosome.genes.length ; i++)
    {
      _chromosome.genes[i] = (int) list.get(i);
    }
    
    
    return _chromosome;
  }
  
  public populationI getMutationResult(){
    return pop;
  }
  
  public static void main(String[] args) {
    IGMutation IGMutation1 = new IGMutation();
    populationI population1 = new population();
    populationI newPop = new population();
    openga.util.printClass printClass1 = new openga.util.printClass();
    
    
    int size = 1, length = 10;
    double mutationRate = 0.95;
    
    population1.setGenotypeSizeAndLength(true, size, length,2);
    population1.createNewPop();
    
    System.out.println("getSingleChromosome");
//    for(int i = 0 ; i < size ; i ++ ){
//       printClass1.printMatrix(""+i,population1.getSingleChromosome(i).genes);
//    }

    IGMutation1.setData(mutationRate, population1);
//    IGMutation1.startMutation();
    IGMutation1.startMutation(0);//for test
    System.out.println("end Matation");

    newPop = IGMutation1.getMutationResult();

    for(int i = 0 ; i < size ; i ++ ){
       printClass1.printMatrix(""+i,newPop.getSingleChromosome(i).genes);
    }
//    */
    

  }
}
