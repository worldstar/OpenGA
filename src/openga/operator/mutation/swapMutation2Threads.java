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

public class swapMutation2Threads extends swapMutation{
  public swapMutation2Threads() {
  }

  public void startMutation(){
    Thread thread1[] = new Thread[popSize];
    int threadCounter = 0;
    for(int i = 0 ; i < popSize ; i ++ ){
       //test the probability is larger than crossoverRate.
       if(Math.random() <= mutationRate){
         swapMutationInerClass swapMutationInerClass1 = new swapMutationInerClass();
         swapMutationInerClass1.setChromsomeIndex(i);
         thread1[i] = new Thread(swapMutationInerClass1);
         setCutpoint();
         thread1[i].run();
         threadCounter ++;
         if(threadCounter == 8){
           try{
             thread1[i].join();//to wait all the thread is done.
             threadCounter = 0;
           }
           catch(Exception e)
           {
             System.out.print(e.toString());
           }
         }

       }
    }
  }

  public class swapMutationInerClass implements Runnable{
    int chromsomeIndex = 0;
    public void setChromsomeIndex(int chromsomeIndex){
      this.chromsomeIndex = chromsomeIndex;
    }

    public void run(){
      try{
        //openga.util.printClass p1 = new openga.util.printClass();
        //System.out.println(cutPoint1+" "+cutPoint2);
        //p1.printMatrix("", pop.getSingleChromosome(chromsomeIndex).getSolution());
        swaptGenes(pop.getSingleChromosome(chromsomeIndex));
        //p1.printMatrix("", pop.getSingleChromosome(chromsomeIndex).getSolution());
        //System.exit(0);
      }
      catch(Exception e)
      {
        System.out.print(e.toString());
      }
    }
  }

}