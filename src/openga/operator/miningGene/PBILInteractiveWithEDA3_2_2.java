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
public class PBILInteractiveWithEDA3_2_2 extends PBILInteractive{

    public PBILInteractiveWithEDA3_2_2(populationI originalPop, double lamda, double beta , int D1 , int D2 , boolean OptMin) {
      super(originalPop, lamda, beta);
      this.D1 = D1;
      this.D2 = D2;
      this.OptMin = OptMin;
    }
    
    int D1;
    int D2;
    boolean OptMin;
    double[] probabilitySum ;
    double[] probabilityError ;

    public void startStatistics() {
        calcAverageFitness();
        calcContainer();
        calcInter();
        
//        if(OptMin)
//        {
//          checkModelAccuracyMin();
//        }else
//        {
//          checkModelAccuracyMax();
//        }
//          enhanceContainer();

          enhanceContainer2();
        
//        System.out.println(D1 + " , " + D2);
//        CheckModelAccurracy();
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
                  if(D1 >= 0 && j >= D1)
                  {
                    for(int k = 0 ; k <= D1 ; k++)
                    {
                      int gene = originalPop.getSingleChromosome(i).getSolution()[j - k];
                      tempContainer[gene][j] += 1;
                      counter++;
                    }      
                  }                                    
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

//System.out.println(lamda);
        /*
        System.out.println("container:after");
        for (int i = 0; i < chromosomeLength; i++) {
        for (int j = 0; j < chromosomeLength; j++) {
        System.out.print(container[i][j] + ",");
        }
        System.out.println();
        }
        //System.exit(0);
         */

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
                    
                    if(D2 >= 0 && (j-1) >= D2)
                    {
                      for(int k = 0 ; k <= D2 ; k++)
                      {
                        int gene_prior = originalPop.getSingleChromosome(i).getSolution()[j-k-1];
                        tempinter[gene_prior][gene]++;
                        counter++;
                      }      
                    }                  
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
        //       System.out.println(beta);
        /*
        System.out.println("inter:after");
        for (int i = 0; i < chromosomeLength; i++) {
        for (int j = 0; j < chromosomeLength; j++) {
        System.out.print(inter[i][j] + ",");
        }
        System.out.println();
        }
         */
        //System.exit(0);

        
    }
    
    
    public void checkModelAccuracyMin()
    {
      /*******************************************************************/
        probabilitySum = new double[originalPop.getPopulationSize()];
        probabilityError = new double[originalPop.getPopulationSize()];

        for(int i = 0 ; i < originalPop.getPopulationSize() ; i++)
        {
          probabilitySum[i] = productGeneInfo(originalPop.getSingleChromosome(i) , container , inter);
          probabilityError[i] = 0;
        }

        for(int i = 0 ; i < originalPop.getPopulationSize() ; i++)
        {
          for(int j = i+1 ; j < originalPop.getPopulationSize() ; j++)
          {
            double[] PopGetObjValue = originalPop.getSingleChromosome(i).getObjValue() ;
            double[] PopGetObjValue2 = originalPop.getSingleChromosome(j).getObjValue() ;

            if(probabilitySum[i] >= probabilitySum[j] && PopGetObjValue[0] <= PopGetObjValue2[0])//success
            {
            }else if (probabilitySum[i] < probabilitySum[j] && PopGetObjValue[0] > PopGetObjValue2[0])//success
            {
            }else if(probabilitySum[i] >= probabilitySum[j] && PopGetObjValue[0] > PopGetObjValue2[0])//error
            {
              probabilityError[j] += (probabilitySum[i] - probabilitySum[j]);

            }else if(probabilitySum[i] < probabilitySum[j] && PopGetObjValue[0] <= PopGetObjValue2[0])//error
            {
              probabilityError[i] += (probabilitySum[j] - probabilitySum[i]);
            }
          }
        }
            
      /*******************************************************************/
    }
    
    public void checkModelAccuracyMax()
    {
      /*******************************************************************/
      /*save probabilitySumError*/
        probabilitySum = new double[originalPop.getPopulationSize()];
        probabilityError = new double[originalPop.getPopulationSize()];

        for(int j = 0 ; j < originalPop.getPopulationSize() ; j++)
        {
          probabilitySum[j] = productGeneInfo(originalPop.getSingleChromosome(j) , container , inter);
          probabilityError[j] = 0;
        }

          for(int j = 0 ; j < originalPop.getPopulationSize() ; j++)
          {
            for(int k = j+1 ; k < originalPop.getPopulationSize() ; k++)
            {
              double[] PopGetObjValue = originalPop.getSingleChromosome(j).getObjValue() ;
              double[] PopGetObjValue2 = originalPop.getSingleChromosome(k).getObjValue() ;
              
              if(probabilitySum[j] >= probabilitySum[k] && PopGetObjValue[0] >= PopGetObjValue2[0])//success
              {
                
              }else if (probabilitySum[j] < probabilitySum[k] && PopGetObjValue[0] < PopGetObjValue2[0])//success
              {
              }else if(probabilitySum[j] >= probabilitySum[k] && PopGetObjValue[0] < PopGetObjValue2[0])//error
              {
                probabilityError[k] += (probabilitySum[j] - probabilitySum[k]);
                
              }else if(probabilitySum[j] < probabilitySum[k] && PopGetObjValue[0] >= PopGetObjValue2[0])//error
              {
                probabilityError[j] += (probabilitySum[k] - probabilitySum[j]);
              }
            }
          }
            
      /*******************************************************************/
    }
    
    public void enhanceContainer()
    {
//    /*******************************************************************/
//    /*inter before *= 1.1*//*Container up*/

        openga.util.algorithm.getMin getMin = new openga.util.algorithm.getMin();
        openga.util.algorithm.getMax getMax = new openga.util.algorithm.getMax();
        double min = getMin.setData(probabilityError);
        double max = getMax.setData(probabilityError);
        
        for (int i = 0; i < popSize; i++) {
          if(probabilityError[i] != 0) {
              double x = 1 - ((probabilityError[i] - min) / (max - min));
              
//            if (originalPop.getFitness(i) <= avgFitness) {
                /*inter*/
                for (int j = 1; j < chromosomeLength; j++) {
                    int gene = originalPop.getSingleChromosome(i).getSolution()[j];//1
                    
                    if(D2 >= 0 && (j-1) >= D2){
                        for(int k = 0 ; k <= D2 ; k++){
                            int gene_before = originalPop.getSingleChromosome(i).getSolution()[j-k-1];//0
                            
                            double y = 1 - inter[gene_before][gene];
                            double z = y*x;
                            inter[gene_before][gene] += z;
                        }      
                    }
                }
                
                /*container*/
                for (int j = 0; j < chromosomeLength; j++) {
                    if(D1 >= 0 && j >= D1){
                        for(int k = 0 ; k <= D1 ; k++){
                          
                            int gene = originalPop.getSingleChromosome(i).getSolution()[j - k];//0

                            double y = 1 - container[gene][j];
                            double z = y*x;
                            container[gene][j] += z;
                            
                        }
                    }
                }
                
            }
        }
    }
    
    public void enhanceContainer2()
    {
//    /*******************************************************************/
      
        openga.util.sort.selectionSort seleSort = new openga.util.sort.selectionSort();
        int[] index = new int[popSize];
        double[] objValue = new double[popSize];
        double[] predictValue = new double[popSize];
        double[] predictValue2;
        for(int i = 0 ; i < popSize ; i++)
        {
          index[i] = i;
          objValue[i] = originalPop.getSingleChromosome(i).getObjValue()[0];
          predictValue[i] = productGeneInfo(originalPop.getSingleChromosome(i) , container , inter);
        }
        
        seleSort.setData(objValue);
        seleSort.setNomialData(index);
        seleSort.Sort_withNomial();
        index = seleSort.getNomialData();
        
        seleSort.setData(predictValue);
        seleSort.executeSelectionSort();
        seleSort.reverseNomialOrder();
        predictValue2 = seleSort.getData();
        
        double max = predictValue2[0];
        double min = predictValue2[predictValue2.length - 1];
        
        for (int i = 0; i < popSize; i++) {
              double x = 1 - ((predictValue[index[i]] - min) / (max - min)); //原本簡易計算
//              double x = java.lang.Math.tanh(predictValue[index[i]]);//tanh (幅度大)
//              double x = predictValue[index[i]]/(1 + predictValue[index[i]]);//x/1+|x| (幅度小)
              
              double x2 = (1/(1+((predictValue[index[i]] - min) / (max - min)))); //原本簡易計算
//              double x2 = java.lang.Math.tanh(predictValue[index[i]]);//tanh (幅度大)
//              double x2 = predictValue[index[i]]/(1 + predictValue[index[i]]);//x/1+|x| (幅度小)
              
//            System.out.printf("%.4f,",originalPop.getSingleChromosome(i).getObjValue()[0]);
//            System.out.printf("%.4f,",productGeneInfo(originalPop.getSingleChromosome(i) , container , inter));

                /*inter*/
                for (int j = 1; j < chromosomeLength; j++) {
                  
                    int gene = originalPop.getSingleChromosome(index[i]).getSolution()[j];//1
                    if(D2 >= 0 && (j-1) >= D2){
                        for(int k = 0 ; k <= D2 ; k++){
                            int gene_before = originalPop.getSingleChromosome(index[i]).getSolution()[j-k-1];//0
                            
                            //--------------------------------------------------------
                            double y = 0 , z = 0;
                            if((predictValue2[i] - predictValue[index[i]]) >= 0)
                            {
                                y = 1 - inter[gene_before][gene];
                                z = y*x;
                            }else
                            {
                                y = inter[gene_before][gene];
                                z = y*(x2)*-1;
                            } 
                                inter[gene_before][gene] += z;
                                
                            //--------------------------------------------------------
                            
                        }      
                    }
                }
                
                /*container*/
                for (int j = 0; j < chromosomeLength; j++) {
                    if(D1 >= 0 && j >= D1){
                        for(int k = 0 ; k <= D1 ; k++){
                            int gene = originalPop.getSingleChromosome(index[i]).getSolution()[j - k];//0
                            
                            //--------------------------------------------------------
                            double y = 0 , z = 0 ;
                            if((predictValue2[i] - predictValue[index[i]]) >= 0)
                            {
                                y = 1 - container[gene][j];
                                z = y*x;  
                            }else
                            {
                                y = container[gene][j];
                                z = y*(x2)*-1;
                            } 
                                container[gene][j] += z;
                            //--------------------------------------------------------
                            
                        }      
                    }
                }
//              System.out.printf("%.4f \n",productGeneInfo(originalPop.getSingleChromosome(i) , container , inter) );
        }
        
    }

}
