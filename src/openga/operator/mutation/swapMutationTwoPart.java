/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package openga.operator.mutation;

/**
 *
 * @author UlyssesZhang
 */
public class swapMutationTwoPart extends swapMutation implements MutationMTSPI{
    
    public int numberOfSalesmen;
    
    public void startMutation(){
        for(int i = 0 ; i < popSize ; i ++ ){
           //test the probability is larger than crossoverRate.
           if(Math.random() <= mutationRate){
             setCutpoint(chromosomeLength, numberOfSalesmen);
             pop.setChromosome(i, swaptGenes(pop.getSingleChromosome(i)));
            }
        }
    }
    
    public final void setCutpoint(int numberofGenes, int numberOfSalesmen){
        //initial the chromosome
        int numberofCities = numberofGenes - numberOfSalesmen;
        cutPoint1 = (int)(Math.random() * numberofCities);
        cutPoint2 = (int)(Math.random() * numberofCities);        
        
        if(cutPoint1 == cutPoint2){
          cutPoint1 -=  (int)(Math.random()*cutPoint1);
          //increase the position of cutPoint2
          cutPoint2 += (int)((numberofCities - cutPoint2)*Math.random());

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

    @Override
    public void setNumberOfSalesmen(int numberOfSalesmen) {
        this.numberOfSalesmen = numberOfSalesmen;
    }
}
