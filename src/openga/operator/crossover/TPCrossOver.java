/*
 * This method implements the new two-part encoding crossover by 
 Yuan, S., Skinner, B., Huang, S., & Liu, D. (2013). A new crossover approach 
for solving the multiple travelling salesmen problem using genetic algorithms. 
European Journal of Operational Research, 228(1), 72-82.
 */
package openga.operator.crossover;
import openga.chromosomes.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author UlyssesZhang
 */
public class TPCrossOver extends twoPointCrossover2 implements CrossoverMTSPI{
    private int numberofSalesmen;

  //start to crossover
  public void startCrossover(){
    for(int i = 0 ; i < popSize ; i ++ ){
       //test the probability is larger than crossoverRate.
       if(Math.random() <= crossoverRate){
         //to get the other chromosome to crossover
         int index2 = getCrossoverChromosome(i);
         int[] Mom = originalPop.getSingleChromosome(i).genes;
         int[] Dad = originalPop.getSingleChromosome(index2).genes;
         int[] Result = CrossOver(Mom, Dad);        
         newPop.getSingleChromosome(i).genes = Result;
       }
    }
  }
  
  public int[] CrossOver(int[]mom, int[]dad) {
    List<Integer> child = new ArrayList<>();
    int numberofCities = mom.length - numberofSalesmen;
    
    int cutPoint1,cutPoint2;
    List<Integer> momGenepool = new ArrayList<>();
    List<Integer> tmpGenepool = new ArrayList<>();
    List<Integer> dadGenepool = new ArrayList<>();
    int[] momGenepoolofSalesmen = new int[numberofSalesmen];
    int[] dadGenepoolofSalesmen = new int[numberofSalesmen];
    
    int stopPosition = 0;
    int currentPosition = 0;
    
    
    for(int i = 0; i < numberofSalesmen; i++){
      //set stopPosition
//      System.out.println(" stopPosition"+stopPosition+" currentPosition"+currentPosition);
      stopPosition += mom[numberofCities+i];
      //set cutpoints
      if((stopPosition - currentPosition) == 1){
        cutPoint1 = 0 + currentPosition;
        cutPoint2 = 1 + currentPosition;
      } else{
        do{
          cutPoint1 = new Random().nextInt(stopPosition-currentPosition)+currentPosition;
          cutPoint2 = new Random().nextInt(stopPosition-currentPosition)+currentPosition;
//          System.out.println(""+" cutPoint1 "+cutPoint1 +" cutPoint2 "+cutPoint2);
        } while(cutPoint1 == cutPoint2); 
      }
      if(cutPoint1 > cutPoint2){
          int tmp = cutPoint1;
          cutPoint1 = cutPoint2;
          cutPoint2 = tmp;
      }     
//      System.out.println(""+" cutPoint1 "+cutPoint1 +" cutPoint2 "+cutPoint2);
      //copy gene to child.
      for(int j = currentPosition; j < stopPosition; j++){
        if(cutPoint1 <= j && j < cutPoint2) {
          momGenepool.add(mom[j]);
          momGenepoolofSalesmen[i]++;
//          System.out.println(" momGenepool "+mom[j]);
        } else{
          tmpGenepool.add(mom[j]);
//          System.out.println(" tmpGenepool "+mom[j]);
        }
      }
      currentPosition = stopPosition;
    }

    //sort dad gene segment.
    stopPosition = dad[numberofCities];
    currentPosition = 0;
    for(int i = 0; i < numberofSalesmen; i++){
      for(int j = currentPosition; j < stopPosition; j++){
        for(int k = 0; k < tmpGenepool.size(); k++){
          if(dad[j] == tmpGenepool.get(k)) {
            dadGenepool.add(tmpGenepool.get(k));
            dadGenepoolofSalesmen[i]++;
//            System.out.println(" dadGenepool "+dad[j]);
          }
        }
      }
      currentPosition = stopPosition;
      stopPosition += (numberofCities - currentPosition);
    }
    //marge gene to child.
    int p1 = 0;
    int p2 = 0;
    for(int i = 0; i < numberofSalesmen; i++){
      for(int j = 0; j < momGenepoolofSalesmen[i]; j++) {
        child.add(momGenepool.get(p1));
        p1++;
      }
      for(int j = 0; j < dadGenepoolofSalesmen[i]; j++) {
        child.add(dadGenepool.get(p2));
        p2++;
      }
    }
    for(int i = 0; i < numberofSalesmen; i++){
      child.add(momGenepoolofSalesmen[i]+dadGenepoolofSalesmen[i]);
    }
    //assign child to result then return.
    int[] result = new int[child.size()];
    for(int i = 0; i < child.size(); i++){
      result[i] = child.get(i);
    }
//    System.out.println(" fin ");
    return result;
  }
  
    @Override
    public void setNumberofSalesmen(int numberofSalesmen) {
        this.numberofSalesmen = numberofSalesmen;
    }
}
