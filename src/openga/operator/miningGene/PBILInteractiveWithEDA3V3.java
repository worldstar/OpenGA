package openga.operator.miningGene;

import openga.chromosomes.*;

/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: PBIL is the Population Based Incremental Learning algorithm.</p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */
/* enhance Inter and Container add D1 and D2*/
public class PBILInteractiveWithEDA3V3 extends PBILInteractive{

    public PBILInteractiveWithEDA3V3(populationI originalPop, double lamda, double beta, boolean OptMin , int epoch ) {
      super(originalPop, lamda, beta);
      this.OptMin = OptMin;
      this.epoch = epoch;
    }
    
    //Enhance
    double[] probabilitySum ;
    double[] probabilityError ;
    
    //Enhance2
    int[] index;
    int[] index2;
//        int[] index3 = new int[popSize];
    double[] objValue;
    double[] predictValue;
    double[] predictValueSorted;
    double[] targetValue;
    
    boolean OptMin;
    int epoch;

    public void startStatistics() {
        calcAverageFitness();
        calcContainer();
        calcInter();
        enhanceSort(OptMin);
        for(int i = 0 ; i < epoch; i++){
          enhanceContainer2();
        }

    }
    
    public void calcContainer() {
        double tempContainer[][] = new double[chromosomeLength][chromosomeLength];
        //to collect gene information.
/*
        //System.out.println("container:before");
        for (int i = 0; i < chromosomeLength; i++) {
        for (int j = 0; j < chromosomeLength; j++) {
        //System.out.print(container[i][j]+",")  ;
        tempContainer[i][j] = 1 / chromosomeLength;
        }
        //System.out.println()  ;
        }
         */

        int counter = 0;
        for (int i = 0; i < popSize; i++) {
            if (originalPop.getFitness(i) <= avgFitness) {
                for (int j = 0; j < chromosomeLength; j++) {  
                    int gene = originalPop.getSingleChromosome(i).getSolution()[j];
                    tempContainer[gene][j] += 1;
                    counter++;                        
                }                
            }
        }

        //to normalize them between [0, 1].
        for (int i = 0; i < chromosomeLength; i++) {
            for (int j = 0; j < chromosomeLength; j++) {
                tempContainer[i][j] /= counter;
            }
        }

        /*
        System.out.println("container:temp , " + "lamda" + lamda);
        for(int i = 0 ; i < chromosomeLength ; i ++ ){
        for(int j = 0 ; j < chromosomeLength ; j ++ ){
        System.out.print(tempContainer[i][j]+",")  ;
        }
        System.out.println()  ;
        }
         */
        //System.out.println(lamda)  ;
        //System.exit(0);

        //for learning rate lamda
        for (int i = 0; i < chromosomeLength; i++) {
            for (int j = 0; j < chromosomeLength; j++) {
                container[i][j] = (1 - lamda) * container[i][j] + (lamda) * tempContainer[i][j];
                //container[i][j]= tempContainer[i][j];
            }
        }


    }

    public void calcInter() {  // interactive array
        //no accumulate effect
        double tempinter[][] = new double[chromosomeLength][chromosomeLength];
        //initial
        for (int i = 0; i < chromosomeLength; i++) {
            for (int j = 0; j < chromosomeLength; j++) {
                tempinter[i][j] = 1 / popSize;
            }
        }

        int counter = 0;
        for (int i = 0; i < popSize; i++) {
            if (originalPop.getFitness(i) <= avgFitness) {
                for (int j = 1; j < (chromosomeLength); j++) {                  
                    int gene = originalPop.getSingleChromosome(i).getSolution()[j];
                    int gene_prior = originalPop.getSingleChromosome(i).getSolution()[j-1];
                    
                    tempinter[gene_prior][gene]++;
                    counter++;    
                }                
            }
        }

        //normalize
        for (int i = 0; i < chromosomeLength; i++) {
            for (int j = 0; j < chromosomeLength; j++) {
                tempinter[i][j] /= counter;
            }
        }


//for learning rate :beta
        for (int i = 0; i < chromosomeLength; i++) {
            for (int j = 0; j < chromosomeLength; j++) {
                inter[i][j] = (1 - beta) * inter[i][j] + (beta) * tempinter[i][j];
            }
        }
        
    }
    
    //Combine two models. V5
    double productGeneInfo(chromosome _chromosome, double[][] container, double inter[][]){
      double productValue = 0;
      for(int i = 0 ; i < _chromosome.getLength() ; i ++){
        int job = _chromosome.getSolution()[i];
                
        if(i == 0){//First job
          productValue += container[job][i];
        }
        else{
          double tempContainer = 0;
          double tempInter = 0;
          int priorJob = _chromosome.getSolution()[i];
          //Conditional proability
          tempContainer += container[priorJob][i];
          
          int priorJob2 = _chromosome.getSolution()[i-1];
          //Conditional proability
          tempInter += inter[priorJob2][job];
          
          productValue += (tempInter) * (tempContainer);
        }             
      }
      return productValue;
    }
    
    public void enhanceSort(boolean OptMin)
    {
//    /*******************************************************************/
      
        openga.util.sort.selectionSort seleSort = new openga.util.sort.selectionSort();
        index = new int[popSize];
        index2 = new int[popSize];
//        int[] index3 = new int[popSize];
        objValue = new double[popSize];
        predictValue = new double[popSize];
        predictValueSorted = new double[popSize];
        targetValue  = new double[popSize];
        for(int i = 0 ; i < popSize ; i++)
        {
          index[i] = i;
          index2[i] = i;
//          index3[i] = i;
          objValue[i] = originalPop.getSingleChromosome(i).getObjValue()[0];
          predictValue[i] = predictValueSorted[i] = productGeneInfo(originalPop.getSingleChromosome(i) , container , inter);
        }
        seleSort.setData(objValue);
        seleSort.setNomialData(index);
        if(OptMin){
          seleSort.Sort_withNomial();
        }else{
          seleSort.Sort_withNomialDesc();
        }
        index = seleSort.getNomialData();
        
        seleSort.setData(predictValueSorted);
        seleSort.setNomialData(index2);
        seleSort.Sort_withNomialDesc();
//        targetValue = seleSort.getData();                
        
//    /*******************************************************************/
    }
    
    public void enhanceContainer2()
    {
//    /*******************************************************************/
      
        double max = predictValueSorted[0];
        double min = predictValueSorted[predictValueSorted.length - 1];
        min = 0;
        
        for (int i = 0; i < popSize ; i++) {
          int objRanking = this.getRank(i, index);
          targetValue[i] = predictValueSorted[objRanking];
              double x = (targetValue[i] - predictValue[i]) / (max - min);  
              
                for (int j = 1; j < chromosomeLength; j++) {
                    int gene = originalPop.getSingleChromosome(index[i]).getSolution()[j];//1
                    int gene_before = originalPop.getSingleChromosome(index[i]).getSolution()[j-1];//0

                    //--------------------------------------------------------
                    double y = 0;
                    y = inter[gene_before][gene];
                    inter[gene_before][gene] += y*x/2;                                
                    //--------------------------------------------------------                        
                }
                
                /*container*/ //&& (index[i] > popSize * 0.8 && targetValue[index[i]] < predictValue[i])
                for (int j = 0; j < chromosomeLength; j++) {
                    int gene = originalPop.getSingleChromosome(index[i]).getSolution()[j];//0

                    //--------------------------------------------------------
                    double y = 0;
                    y = container[gene][j];
                    container[gene][j] += x*y/2;                                                                                        
                    //--------------------------------------------------------                 
                }
//              System.out.printf("%.4f \n",productGeneInfo(originalPop.getSingleChromosome(i) , container , inter) );
        }

    }
    
    public int getRank(int chromosomeIndex, int sortedRank[]){
      int rankIndex = 0;
      
      for(int i = 0 ; i < sortedRank.length; i++){
        if(sortedRank[i] == chromosomeIndex){
          rankIndex = i;
          break;
        }
      }
      
      return rankIndex;
    }

}
