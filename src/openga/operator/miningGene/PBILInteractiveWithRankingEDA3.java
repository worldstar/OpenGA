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
public class PBILInteractiveWithRankingEDA3 extends PBILInteractive {

    public PBILInteractiveWithRankingEDA3(populationI originalPop, double lamda, double beta) {
      super(originalPop, lamda, beta);
    }
    double container[][];
    double inter[][]; 
    double container_temp[][];
    double inter_temp[][];
    double[] lamdas = new double[]{0.1,0.5,0.9};
    double[] betas = new double[]{0.1,0.5,0.9};
    double lamda_temp;
    double beta_temp;
    public void startStatistics() {
      calcAverageFitness();
      double Accurracy_temp = 0.0 , Accurracy = 0.0;
      for(int i = 0 ; i < lamdas.length ; i++)
      {
        for(int j = 0 ; j < betas.length ; j++)
        {
            container = new double[chromosomeLength][chromosomeLength];
            inter = new double[chromosomeLength][chromosomeLength];
            lamda = lamdas[i];
            beta = betas[j];
            calcContainer();
            calcInter();
            enhanceContainer();
            Accurracy_temp = CheckModelAccurracy_double();
            System.out.println(Accurracy_temp + " , lamda : " + lamda + " , beta : " + beta);
            if(Accurracy < Accurracy_temp)
            {
              Accurracy = Accurracy_temp;
              container_temp = container;
              inter_temp = inter;
              lamda_temp = lamda;
              beta_temp = beta;
            }
        }
      }
//      System.out.println(Accurracy + " , lamda : " + lamda_temp + " , beta : " + beta_temp);
      container = container_temp;
      inter = inter_temp;

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
    
    @Override
    public void calcContainer() {
        double tempContainer[][] = new double[chromosomeLength][chromosomeLength];
        //to collect gene information.

        int counter = 0;
        for (int i = 0; i < popSize; i++) {
            if (originalPop.getFitness(i) <= avgFitness) {
                for (int j = 0; j < chromosomeLength; j++) {
                    int gene = originalPop.getSingleChromosome(i).getSolution()[j];
                    tempContainer[gene][j] += 1;
                }
                counter++;
            }
        }

        //to normalize them between [0, 1].
        for (int i = 0; i < chromosomeLength; i++) {
            for (int j = 0; j < chromosomeLength; j++) {
                tempContainer[i][j] /= counter;
            }
        }
        
        //for learning rate lamda
        for (int i = 0; i < chromosomeLength; i++) {
            for (int j = 0; j < chromosomeLength; j++) {
                container[i][j] = (1 - lamda) * container[i][j] + (lamda) * tempContainer[i][j];
                //container[i][j]= tempContainer[i][j];
            }
        }
    }

    public void enhanceContainer()
  {
      /*******************************************************************/
      /*save probabilitySumError*/
        double[] probabilitySum = new double[originalPop.getPopulationSize()];
        double[] probabilityError = new double[originalPop.getPopulationSize()];
        double[] probabilitySumError ;
        probabilitySumError = new double[originalPop.getPopulationSize()];

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

//              System.out.println(probabilitySum[j] + " vs " + probabilitySum[k] + " , " + PopGetObjValue[0] + " vs " + PopGetObjValue2[0]  );
              
              if(probabilitySum[j] >= probabilitySum[k] && PopGetObjValue[0] <= PopGetObjValue2[0])//success
              {
                success++;
//                System.out.println(probabilitySum[j] + " vs " + probabilitySum[k] + " , " + PopGetObjValue[0] + " vs " +  PopGetObjValue2[0] + " , success");
              }else if (probabilitySum[j] < probabilitySum[k] && PopGetObjValue[0] > PopGetObjValue2[0])//success
              {
                success++;
//                System.out.println(probabilitySum[j] + " vs " + probabilitySum[k] + " , " + PopGetObjValue[0] + " vs " +  PopGetObjValue2[0] + " , success");
              }else if(probabilitySum[j] >= probabilitySum[k] && PopGetObjValue[0] > PopGetObjValue2[0])//error
              {
//                probabilityError[j] = probabilitySum[j] - probabilitySum[k];
                probabilityError[j] = probabilitySum[k] - probabilitySum[j];
//                probabilityError[j] = 100;
                
                probabilitySumError[j]++;
                
                error++;
//                System.out.println(probabilitySum[j] + " vs " + probabilitySum[k] + " , " + PopGetObjValue[0] + " vs " +  PopGetObjValue2[0] + " , error");
              }else if(probabilitySum[j] < probabilitySum[k] && PopGetObjValue[0] <= PopGetObjValue2[0])//error
              {
                probabilityError[j] = probabilitySum[k] - probabilitySum[j];
//                probabilityError[j] = probabilitySum[j] - probabilitySum[k];
//                probabilityError[j] = 100;
                
                probabilitySumError[j]++;//j
                
                error++;
//                System.out.println(probabilitySum[j] + " vs " + probabilitySum[k] + " , " + PopGetObjValue[0] + " vs " +  PopGetObjValue2[0] + " , error");
              }else
              {
                error++;
              }
            }
//            probabilitySumError[j] = probabilitySumError[j]/(error + success);
          }
            
      /*******************************************************************/
      
//    /*******************************************************************/
//    /*inter before *= 1.1*//*Container up*/

        openga.util.algorithm.getMin getMin = new openga.util.algorithm.getMin();
        openga.util.algorithm.getMax getMax = new openga.util.algorithm.getMax();
        double min = getMin.setData(probabilityError);
        double max = getMax.setData(probabilityError);
        
        for (int i = 0; i < popSize; i++) {
          
            if (originalPop.getFitness(i) <= avgFitness) {
              
                for (int j = 1; j < chromosomeLength; j++) {
                    
                    int gene = originalPop.getSingleChromosome(i).getSolution()[j];//1
                    int gene_before = originalPop.getSingleChromosome(i).getSolution()[j - 1];//0
                    
//                    if(probabilitySumError[i] != 0 ) { // error Count up 10%
                    if(probabilityError[i] != 0 | probabilityError[i] != 0.0) { // error Count up 10%
                      
//                      inter[gene_before][gene] *= 1.1; 
//                      System.out.println((probabilitySumError[i] / (float)(2 + probabilitySumError[i])));
//                      System.out.println(1 + (probabilitySumError[i] / (float)(1 + probabilitySumError[i])));
                      
//                      inter[gene_before][gene] *= 1 + (probabilitySumError[i] / (float)(1 + probabilitySumError[i])); //V1 
//                      inter[gene_before][gene] *= 1 + (probabilityError[i] / (float)(1 + probabilityError[i])); //V2
//                      inter[gene_before][gene] *= (probabilitySumError[i] / (float)(1 + probabilitySumError[i])); //V3 
//                      inter[gene_before][gene] *= (probabilityError[i] / (float)(1 + probabilityError[i])); //V4 
//                      System.out.println(((probabilityError[i] - min) / (max - min)));

                      if(probabilityError[i] < 0)
                      {
                        
                      double x = ((probabilityError[i] - min) / (max - min));
                      inter[gene_before][gene] *=  (x / (float)(1 + x));
                      
                      }else{
                        
                      double x = ((probabilityError[i] - min) / (max - min));
                      inter[gene_before][gene] *= 1 + (x / (float)(1 + x));
                      
                      }
                      
                      
//                      double x = ((probabilityError[i] - min) / (max - min));//EDA3_V4_revise16
//                      inter[gene_before][gene] *= 10 * (x / (float)(1 + x));//EDA3_V4_revise16
                      
//                      System.out.println(probabilityError[i] + " �G " + (probabilityError[i] / (float)(1 + probabilityError[i])));
//                      System.out.println(probabilityError[i] / (float)(1 + probabilityError[i]));
//                      inter[gene_before][gene] *= Math.tanh(probabilitySumError[i]); //V5 
//                      inter[gene_before][gene] *= Math.tanh(probabilityError[i]); //V6 


                      
                    }
                }
            }
        }
      
  }
    
    public double CheckModelAccurracy_double()
  {
    /*******************************************************************/
            double[] probabilitySum = new double[originalPop.getPopulationSize()];
            
            for(int j = 0 ; j < originalPop.getPopulationSize() ; j++)
            {
              probabilitySum[j] = productGeneInfo(originalPop.getSingleChromosome(j) , container , inter);
//              System.out.println(probabilitySum[j]);
            }
            int total = 0;
            int error = 0;
            int success = 0;
            
            for(int j = 0 ; j < originalPop.getPopulationSize() ; j++)
            {
              for(int k = j+1 ; k < originalPop.getPopulationSize() ; k++)
              {
                double[] PopGetObjValue = originalPop.getSingleChromosome(j).getObjValue() ;
                double[] PopGetObjValue2 = originalPop.getSingleChromosome(k).getObjValue() ;
                
                total ++;
              
                if(probabilitySum[j] >= probabilitySum[k] && PopGetObjValue[0] <= PopGetObjValue2[0])
                {
                  success++;
                }else if (probabilitySum[j] < probabilitySum[k] && PopGetObjValue[0] > PopGetObjValue2[0])
                {
                  success++;
                }else
                {
                  error++;
                }
                
              }
            }
            
            return ((double)((double)success / (double)total));
            /*******************************************************************/
  }
}
