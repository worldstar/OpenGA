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
    private int[] Mom = {9,7,5,6,2,8,4,3,1,5,2,2};    
    private int[] Dad = {8,2,3,7,5,1,9,4,6,3,4,2};
    private int numberofSalesmen = 3;

  //start to crossover
  public void startCrossover(){
      
    for(int i = 0 ; i < popSize ; i ++ ){
       //test the probability is larger than crossoverRate.
       if(Math.random() <= crossoverRate){
         //to get the other chromosome to crossover
         int index2 = getCrossoverChromosome(i);
         int[] Mom = originalPop.getSingleChromosome(i).genes;
         int[] Dad = originalPop.getSingleChromosome(index2).genes;
         int[] Result = CrossOver(Mom, Dad, numberofSalesmen);        
         newPop.getSingleChromosome(i).genes = Result;
       }
    }
  }
    
    public int[] CrossOver(int[]Mom, int[]Dad, int NumofSalesman) {
        //Initialization
        int[] Child = new int[Mom.length];
        int[] Cities = new int[Mom.length-NumofSalesman];
        List<Integer> MomGenePool = new ArrayList<>();
        List<Integer> DadGenePool = new ArrayList<>();
        List<Integer> TmpGenePool = new ArrayList<>();
        List<Integer> GeneSegment = new ArrayList<>();
        int[] MomAssignedCities = new int[NumofSalesman];
        int[] DadAssignedCities = new int[NumofSalesman];
        int[] MomSalesGene = new int[NumofSalesman];
        int[] DadSalesGene = new int[NumofSalesman];
        int[] CP = new int[NumofSalesman*2];
        
        for(int i = 0; i < Cities.length; i++) {
            Cities[i] = Mom[i];
        }
        for(int i = 0; i < MomAssignedCities.length; i++) {
            MomAssignedCities[i] = Mom[i+Cities.length];
        }
        for(int i = 0; i < DadAssignedCities.length; i++) {
            DadAssignedCities[i] = Dad[i+Cities.length];
        }
        
        //set Cut Point
            do{
                CP[0] = new Random().nextInt(MomAssignedCities[0]+1);
                CP[1] = new Random().nextInt(MomAssignedCities[0]+1);
            } while(CP[0] == CP[1]);
            if(CP[0] > CP[1]){
                int tmp = CP[0];
                CP[0] = CP[1];
                CP[1] = tmp;
            }

            do{
                CP[2] = new Random().nextInt(MomAssignedCities[1]+1)+MomAssignedCities[0];
                CP[3] = new Random().nextInt(MomAssignedCities[1]+1)+MomAssignedCities[0];
            } while(CP[2] == CP[3]);
            if(CP[2] > CP[3]){
                int tmp = CP[2];
                CP[2] = CP[3];
                CP[3] = tmp;
            }

            do{
                CP[4] = new Random().nextInt(MomAssignedCities[2]+1)+MomAssignedCities[0]+MomAssignedCities[1];
                CP[5] = new Random().nextInt(MomAssignedCities[2]+1)+MomAssignedCities[0]+MomAssignedCities[1];
            } while(CP[4] == CP[5]);
            if(CP[4] > CP[5]){
                int tmp = CP[4];
                CP[4] = CP[5];
                CP[5] = tmp;
            } 

//        int[] Cut1 = {1,5,8};
//        int[] Cut2 = {3,7,9};
        int[] Cut1 = {CP[0], CP[2], CP[4]};
        int[] Cut2 = {CP[1], CP[3], CP[5]};
        
        //MomGenePool
        int p = 0;
        for(int i = 0; i < MomAssignedCities.length; i++) {
            for(int j = 0; j < MomAssignedCities[i]; j++) {
                if(Cut1[i] <= p && p < Cut2[i]) {
                    MomGenePool.add(Mom[p]);
                    MomSalesGene[i]++;
                    //System.out.print(Mom[k]+" ");
                    p++;
                } else {
                    TmpGenePool.add(Mom[p]);
                    //System.out.println(Mom[k]+" ");
                    p++;
                }
            }
        }
        
        //DadGenePool
        p = 0;
        for(int i = 0; i < DadAssignedCities.length; i++) {
            for(int j = 0; j < DadAssignedCities[i]; j++) {
                for(int k = 0; k < TmpGenePool.size(); k++) {
                    if(TmpGenePool.get(k) == Dad[p]) {
                        DadGenePool.add(TmpGenePool.get(k));
                        DadSalesGene[i]++;
                    }
                }
                p++;
            }
        }
        
        //GeneSegment
        for(int i = 0; i < MomAssignedCities.length; i++) {
            GeneSegment.add(MomSalesGene[i]);
            GeneSegment.add(DadSalesGene[i]);
        }

        //Child Cities
        p = 0;
        int q = 0;
        int r = 0;
        for(int i = 0; i < GeneSegment.size(); i++) {
            for(int j = 0; j < GeneSegment.get(i); j++) {
                if(i % 2 == 0) {
                    Child[p] = MomGenePool.get(q);
                    p++;
                    q++;
                } else {
                    Child[p] = DadGenePool.get(r);
                    p++;
                    r++;
                }
            }
        }
        
        //Child Sales
        for(int i = 0; i < MomSalesGene.length; i++) {
            Child[i+Cities.length] = (MomSalesGene[i] + DadSalesGene[i]);
        }
        return Child;
    }

    @Override
    public void setNumberofSalesmen(int numberofSalesmen) {
        this.numberofSalesmen = numberofSalesmen;
    }
}
