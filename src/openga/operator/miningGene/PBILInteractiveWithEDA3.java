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
        CheckModelAccurracy();
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
                for (int j = 0; j < (chromosomeLength - 1); j++) {
                    int gene = originalPop.getSingleChromosome(i).getSolution()[j];
                    int gene_after = originalPop.getSingleChromosome(i).getSolution()[j + 1];
                    tempinter[gene_after][gene]++;
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
        int[] probabilitySumError ;
        probabilitySumError = new int[originalPop.getPopulationSize()];

        for(int j = 0 ; j < originalPop.getPopulationSize() ; j++)
        {
          probabilitySum[j] = productGeneInfo(originalPop.getSingleChromosome(j) , container , inter);
//              System.out.println(probabilitySum[j] + " , ");
        }

          int error = 0;
          int success = 0;
          for(int j = 0 ; j < originalPop.getPopulationSize() ; j++)
          {
            for(int k = j+1 ; k < originalPop.getPopulationSize() ; k++)
            {
              double[] PopGetObjValue = originalPop.getSingleChromosome(j).getObjValue() ;
              double[] PopGetObjValue2 = originalPop.getSingleChromosome(k).getObjValue() ;

              if(probabilitySum[j] >= probabilitySum[k] && PopGetObjValue[0] <= PopGetObjValue2[0])//success
              {
                success++;
              }else if (probabilitySum[j] < probabilitySum[k] && PopGetObjValue[0] > PopGetObjValue2[0])//success
              {
                success++;
              }else if(probabilitySum[j] >= probabilitySum[k] && PopGetObjValue[0] > PopGetObjValue2[0])//error
              {
                probabilityError[j] = probabilitySum[k] - probabilitySum[j];

                probabilitySumError[j]++;

                error++;
              }else if(probabilitySum[j] < probabilitySum[k] && PopGetObjValue[0] <= PopGetObjValue2[0])//error
              {
                probabilityError[j] = probabilitySum[j] - probabilitySum[k];

                probabilitySumError[j]++;

                error++;
              }else
              {
                error++;
              }
            }
          }
            
      /*******************************************************************/

//    /*******************************************************************/
//    /*inter before *= 1.1*//*Container up*/

///*tempContainer*/
//        double tempContainer[][] = new double[chromosomeLength][chromosomeLength];
//        
//        int counter = 0;
//        for (int i = 0; i < popSize; i++) {
//            if (originalPop.getFitness(i) <= avgFitness) {
//                for (int j = 0; j < chromosomeLength; j++) {
//                    int gene = originalPop.getSingleChromosome(i).getSolution()[j];
//                    tempContainer[gene][j] += 1;
//                }
//                counter++;
//            }
//        }
//        
//        //to normalize them between [0, 1].
//        for (int i = 0; i < chromosomeLength; i++) {
//            for (int j = 0; j < chromosomeLength; j++) {
//                tempContainer[i][j] /= counter;
//            }
//        }
///*tempContainer*/
        
        for (int i = 0; i < popSize; i++) {
          
            if (originalPop.getFitness(i) <= avgFitness) {
              
              
                for (int j = 1; j < chromosomeLength; j++){
                  
                    int gene = originalPop.getSingleChromosome(i).getSolution()[j];
                    int gene_before = originalPop.getSingleChromosome(i).getSolution()[j - 1];

                    if(probabilitySumError[i] != 0) // error Count up 10%
                    {
                      inter[gene_before][gene] *= (probabilitySumError[i] / (float)(1 + probabilitySumError[i])); //錯誤次數
  //                    inter[gene_before][gene] /= 1+ (probabilityError[i] / (float)(1 + probabilityError[i])); //錯誤差距

  //                      tempContainer[gene_before][gene] *=  (probabilitySumError[i] / (float)(1 + probabilitySumError[i])); //錯誤次數
  //                      tempContainer[gene_before][gene] = tempContainer[gene_before][gene] * (probabilitySumError[i] / (float)(1 + probabilitySumError[i]));


                    }
                }
            }
        }
        
//        //for learning rate lamda
//        for (int i = 0; i < chromosomeLength; i++) {
//            for (int j = 0; j < chromosomeLength; j++) {
//                container[i][j] = (1 - lamda) * container[i][j] + (lamda) * tempContainer[i][j];
//                //container[i][j]= tempContainer[i][j];
//            }
//        }
        
//    /*******************************************************************/
  }
    
//    public void CheckModelAccurracy()
//  {
//    /*******************************************************************/
//            double[] probabilitySum = new double[originalPop.getPopulationSize()];
//            
//            for(int j = 0 ; j < originalPop.getPopulationSize() ; j++)
//            {
//              probabilitySum[j] = productGeneInfo(originalPop.getSingleChromosome(j) , container , inter);
////              System.out.println(probabilitySum[j]);
//            }
//            int total = 0;
//            int error = 0;
//            int success = 0;
//            
//            for(int j = 0 ; j < originalPop.getPopulationSize() ; j++)
//            {
//              for(int k = j+1 ; k < originalPop.getPopulationSize() ; k++)
//              {
//                double[] PopGetObjValue = originalPop.getSingleChromosome(j).getObjValue() ;
//                double[] PopGetObjValue2 = originalPop.getSingleChromosome(k).getObjValue() ;
//                
//                total ++;
//              
//                if(probabilitySum[j] >= probabilitySum[k] && PopGetObjValue[0] <= PopGetObjValue2[0])
//                {
//                  success++;
//                }else if (probabilitySum[j] < probabilitySum[k] && PopGetObjValue[0] > PopGetObjValue2[0])
//                {
//                  success++;
//                }else
//                {
//                  error++;
//                }
//                
//              }
//            }
//            
//            System.out.printf("%.2f\n",((double)((double)success / (double)total)));
//            
//            /*******************************************************************/
//  }
//  
//  //Probability product, instead of summing up the probability.
//    //2009.8.20
//    private double productGeneInfo(chromosome _chromosome , double[][] container){
//      double productValue = 1;
//      for(int i = 0 ; i < _chromosome.getLength() ; i ++){
//        int job = _chromosome.getSolution()[i];
//        productValue *= container[job][i] * 10;
//      }
////      //productVale *= Math.pow(10, cutPoint2 - cutPoint1);
//      return productValue;
//    }
//    
//    //Combine two models.
//    double productGeneInfo(chromosome _chromosome ,double[][] container, double inter[][]){
//      double productValue = 1;
//      for(int i = 0 ; i < _chromosome.getLength() ; i ++){
//        int job = _chromosome.getSolution()[i];
//                
//        if(i == 0){//First job
//          productValue *= container[job][i] * 10;
//        }
//        else{
//          int priorJob = _chromosome.getSolution()[i-1];
//          productValue *= (container[job][i] * 10 + inter[priorJob][job] * 10);
//        }             
//      }
//      return productValue;
//    }    
    
}
