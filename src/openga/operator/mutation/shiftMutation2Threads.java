package openga.operator.mutation;
import java.io.*;
import openga.chromosomes.*;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class shiftMutation2Threads extends shiftMutation{
  public shiftMutation2Threads() {
  }

  public void startMutation(){
    int threadCounter = 0;
    int threadNum = 3;
    Thread thread1[] = new Thread[threadNum];
    shiftMutationInerClass mutationInerClass1[] = new shiftMutationInerClass[threadNum];

    for(int i = 0 ; i < threadNum ; i ++ ){
      mutationInerClass1[i] = new shiftMutationInerClass();
      thread1[i] = new Thread(mutationInerClass1[i]);
    }

    for(int i = 0 ; i < popSize ; i ++ ){
       //test the probability is larger than crossoverRate.
       if(Math.random() <= mutationRate){
         setCutpoint();
         mutationInerClass1[threadCounter].setChromsomeIndex(i, cutPoint1, cutPoint2);
         thread1[threadCounter].run();
         threadCounter ++;

         if(threadCounter == threadNum){
           try{
             for(int k = 0 ; k < threadNum ; k ++ ){
               thread1[k].join();//to wait all the thread is done.
             }
           }
           catch(Exception e)
           {
             System.out.print(e.toString());
           }
           threadCounter = 0;
         }
       }
    }

    for(int i = 0 ; i < threadNum ; i ++ ){
      mutationInerClass1[i] = null;
      thread1[i] = null;
    }
    thread1 = null;
    mutationInerClass1 = null;
  }

  private class shiftMutationInerClass implements Runnable{
    int chromsomeIndex = 0;
    int cutPoint2, cutPoint1;
    public void setChromsomeIndex(int chromsomeIndex, int cutPoint1, int cutPoint2){
      this.chromsomeIndex = chromsomeIndex;
    }

    private chromosome shiftGenes(chromosome _chromosome){
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
      backupGenes = null;
      return _chromosome;
    }

    public void run(){
      try{
        shiftGenes(pop.getSingleChromosome(chromsomeIndex));
      }
      catch(Exception e)
      {
        System.out.print(e.toString());
      }
    }
  }

}