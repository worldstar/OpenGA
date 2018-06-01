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
public class PBILInteractiveWithEDA3 extends PBILInteractive {

    public PBILInteractiveWithEDA3(populationI originalPop, double lamda, double beta) {
      super(originalPop, lamda, beta);
    }

    public void startStatistics() {
        calcAverageFitness();
        calcContainer();
        calcInter();
        enhanceContainer();
//        CheckModelAccurracy();
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
                    int gene_prior = originalPop.getSingleChromosome(i).getSolution()[j - 1];
                    tempinter[gene_prior][gene]++;
                }
                counter++;
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

    public void enhanceContainer()
  {
      /*******************************************************************/
      /*save probabilitySumError*/
        double[] probabilitySum = new double[originalPop.getPopulationSize()];
        double[] probabilityError = new double[originalPop.getPopulationSize()];
//        double[] probabilitySumError ;
//        probabilitySumError = new double[originalPop.getPopulationSize()];

        for(int j = 0 ; j < originalPop.getPopulationSize() ; j++)
        {
          probabilitySum[j] = productGeneInfo(originalPop.getSingleChromosome(j) , container , inter);
          probabilityError[j] = 0.0;
        }

          for(int j = 0 ; j < originalPop.getPopulationSize() ; j++)
          {
            for(int k = j+1 ; k < originalPop.getPopulationSize() ; k++)
            {
              double[] PopGetObjValue = originalPop.getSingleChromosome(j).getObjValue() ;
              double[] PopGetObjValue2 = originalPop.getSingleChromosome(k).getObjValue() ;
              
              if(probabilitySum[j] >= probabilitySum[k] && PopGetObjValue[0] <= PopGetObjValue2[0])//success
              {
              }else if (probabilitySum[j] < probabilitySum[k] && PopGetObjValue[0] > PopGetObjValue2[0])//success
              {
              }else if(probabilitySum[j] >= probabilitySum[k] && PopGetObjValue[0] > PopGetObjValue2[0])//error
              {
                probabilityError[k] += (probabilitySum[j] - probabilitySum[k]);
//                probabilitySumError[j]++;
                
              }else if(probabilitySum[j] < probabilitySum[k] && PopGetObjValue[0] <= PopGetObjValue2[0])//error
              {
                probabilityError[j] += (probabilitySum[k] - probabilitySum[j]);
//                probabilitySumError[j]++;
              }
            }
          }
            
      /*******************************************************************/
      
//    /*******************************************************************/
//    /*inter before *= 1.1*//*Container up*/

        openga.util.algorithm.getMin getMin = new openga.util.algorithm.getMin();
        openga.util.algorithm.getMax getMax = new openga.util.algorithm.getMax();
        double min = getMin.setData(probabilityError);
        double max = getMax.setData(probabilityError);
        
        for (int i = 0; i < popSize; i++) {
          
//            if (originalPop.getFitness(i) <= avgFitness) {
//                /*inter*/
//                for (int j = 1; j < chromosomeLength; j++) 
//                {
//                    int gene = originalPop.getSingleChromosome(i).getSolution()[j];//1
//                    int gene_before = originalPop.getSingleChromosome(i).getSolution()[j - 1];//0
//                    
//                    if(probabilityError[i] != 0 | probabilityError[i] != 0.0) { 
//                      
//                        double x = ((probabilityError[i] - min) / (max - min));
//                        double y = 1 - inter[gene_before][gene];
//                        double z = y*x;
////                        y *= (1 +(x / (float)(1 + x)));       
//                        inter[gene_before][gene] += z;
//                    }
//                }
                /*container*/
                for (int j = 0; j < chromosomeLength; j++) 
                {
                    int gene = originalPop.getSingleChromosome(i).getSolution()[j];//0
                    if(probabilityError[i] != 0 | probabilityError[i] != 0.0) { 
                      
                        double x = ((probabilityError[i] - min) / (max - min));
                        double y = 1 - container[gene][j];
                        double z = y*x;
//                        y *= (1 +(x / (float)(1 + x)));
                        container[gene][j] += z;
//                        System.out.println(container[gene][j]);
                    }
                }
//            }//avgFitness
        }
      
  }
}
