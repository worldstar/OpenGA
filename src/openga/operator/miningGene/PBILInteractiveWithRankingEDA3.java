package openga.operator.miningGene;

import openga.chromosomes.*;
import java.util.ArrayList;
import java.util.Random;
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
    double calcContainer_temp[][];
    double calcInter_temp[][];
    
    double[][] Accurracy_before = new double[lamdas.length][betas.length];
    boolean isFirst = true;
    int[][] useCount = new int[lamdas.length][betas.length];
    int useTotalCount = 0;
    int C = 1;
    double[][] Reward = new double[lamdas.length][betas.length];
    double[][] Decay = new double[lamdas.length][betas.length];
    double DecaySum = 0.0;
    double D = 0.3;
    int Adaptive_Count = 0;
    ArrayList<Integer> Adaptive_list = new ArrayList<Integer>();
    double[][] naturalLog = new double[lamdas.length][betas.length];
    double[][] FRR = new double[lamdas.length][betas.length];
    double[][] FIR = new double[lamdas.length][betas.length];
    
    
    public void startStatistics() {
      calcAverageFitness();
      calcContainer();
      calcInter();
      Adaptive();
//      System.out.println(Accurracy + " , lamda : " + lamda_temp + " , beta : " + beta_temp);
//      enhanceContainer();
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
                for (int j = 1; j < (chromosomeLength); j++) {
                    int gene = originalPop.getSingleChromosome(i).getSolution()[j];
                    //int gene_after = originalPop.getSingleChromosome(i).getSolution()[j + 1];
                    //tempinter[gene_after][gene]++;
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

        calcInter_temp = tempinter;

//      //for learning rate :beta
//        for (int i = 0; i < chromosomeLength; i++) {
//            for (int j = 0; j < chromosomeLength; j++) {
//                inter[i][j] = (1 - beta) * inter[i][j] + (beta) * tempinter[i][j];
//            }
//        }
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
        
        calcContainer_temp = tempContainer;
        
//        //for learning rate lamda
//        for (int i = 0; i < chromosomeLength; i++) {
//            for (int j = 0; j < chromosomeLength; j++) {
//                container[i][j] = (1 - lamda) * container[i][j] + (lamda) * tempContainer[i][j];
//                //container[i][j]= tempContainer[i][j];
//            }
//        }
    }
    
    public void Adaptive()
    {
      if(Adaptive_Count <= (lamdas.length * betas.length))
      {
        unitormRandomSelection();
      }else
      {
        BanditParameterSelection();
      }
      
    }
    
    public void unitormRandomSelection()
    {
      Random ran = new Random();
        int i = ran.nextInt(lamdas.length);
        int j = ran.nextInt(betas.length);
        
        if(isFirst == false)
        {
          for(int k = 0 ; k < Adaptive_list.size() ; k++)
          {          
            if(Adaptive_list.get(k) == (i * lamdas.length + j))
            {
              i = ran.nextInt(lamdas.length);
              j = ran.nextInt(betas.length);
              k = 0;
            }
          }
        }
        
        Adaptive_list.add((i * lamdas.length + j));
        container = new double[chromosomeLength][chromosomeLength];
        inter = new double[chromosomeLength][chromosomeLength];
        /********************calcContainer********************/
            double tempContainer[][];
            tempContainer = calcContainer_temp;

            for (int k = 0; k < chromosomeLength; k++) {
              for (int l = 0; l < chromosomeLength; l++) {
                  container[k][l] = (1 - lamdas[i]) * container[k][l] + (lamdas[i]) * tempContainer[k][l];
                  //container[i][j]= tempContainer[i][j];
              }
            }

        /********************calcContainer********************/
        /********************calcInter********************/
        double tempinter[][] = new double[chromosomeLength][chromosomeLength];
        tempinter = calcInter_temp;

        for (int k = 0; k < chromosomeLength; k++) {
          for (int l = 0; l < chromosomeLength; l++) {
              inter[k][l] = (1 - betas[j]) * inter[k][l] + (betas[j]) * tempinter[k][l];
          }
        }

        /********************calcInter********************/
        /********************Accurracy_temp********************/
        double Accurracy_now;
        Accurracy_now = CheckModelAccurracy_double();
        FIR[i][j] = 0.0;
        Accurracy_before[i][j] = 1;
        if((Accurracy_before[i][j] - Accurracy_now) != 0.0 && Accurracy_before[i][j] != 0.0)
          FIR[i][j] = (Accurracy_before[i][j] - Accurracy_now)/Accurracy_before[i][j];
        
        if(isFirst == true)
        {
          isFirst = false;
        }else
        {    
          useCount[i][j] += 1;
          useTotalCount ++;
          Decay[i][j] += D * Reward[i][j];
          DecaySum += Decay[i][j];
          
        }
        
        Accurracy_before[i][j] = Accurracy_now;
        Reward[i][j] += FIR[i][j];
        Decay[i][j] += D * Reward[i][j];
        DecaySum += Decay[i][j];
        
        container_temp = container;
        inter_temp = inter;
        /********************Accurracy_temp********************/
        Adaptive_Count ++ ;
    }

    public void BanditParameterSelection()
    {
      /********************Adaptive********************/
        double FIR_temp = -1;
        double FRR_temp = -1;
        int lamda_Count = 0;
        int beta_Count = 0;

        for(int i = 0 ; i < lamdas.length ; i++)
        {
          for(int j = 0 ; j < betas.length ; j++)
          {
              container = new double[chromosomeLength][chromosomeLength];
              inter = new double[chromosomeLength][chromosomeLength];
              double Accurracy_now;

              /********************calcContainer********************/
              double tempContainer[][];
              tempContainer = calcContainer_temp;

              for (int k = 0; k < chromosomeLength; k++) {
                for (int l = 0; l < chromosomeLength; l++) {
                    container[k][l] = (1 - lamdas[i]) * container[k][l] + (lamdas[i]) * tempContainer[k][l];
                    //container[i][j]= tempContainer[i][j];
                }
              }

              /********************calcContainer********************/
              /********************calcInter********************/
              double tempinter[][] = new double[chromosomeLength][chromosomeLength];
              tempinter = calcInter_temp;

              for (int k = 0; k < chromosomeLength; k++) {
                for (int l = 0; l < chromosomeLength; l++) {
                    inter[k][l] = (1 - betas[j]) * inter[k][l] + (betas[j]) * tempinter[k][l];
                }
              }

              /********************calcInter********************/
              /********************Accurracy_temp********************/
              
              Accurracy_now = CheckModelAccurracy_double();
              FIR[i][j] = 0.0;
              if((Accurracy_before[i][j] - Accurracy_now) != 0.0 && Accurracy_before[i][j] != 0.0)
                FIR[i][j] = (Accurracy_before[i][j] - Accurracy_now)/Accurracy_before[i][j];
//              System.out.println(FIR[i][j]);
              
              FRR[i][j] = 0.0;
              if(Decay[i][j] != 0.0 && DecaySum != 0.0)
                FRR[i][j] = Decay[i][j] / DecaySum;
              
              naturalLog[i][j] = Math.pow(((2*Math.exp(useTotalCount))/useCount[i][j]),0.5);
//              System.out.println(naturalLog[i][j]);
              Accurracy_before[i][j] = Accurracy_now;
//              System.out.println(naturalLog[i][j]);
              
              /********************Accurracy_temp********************/
          }
        }
        openga.util.algorithm.getMin getMin = new openga.util.algorithm.getMin();
        openga.util.algorithm.getMax getMax = new openga.util.algorithm.getMax();
        double FRRmin = getMin.setData(FRR);
        double FRRmax = getMax.setData(FRR);
        double naturalLogmin = getMin.setData(naturalLog);
        double naturalLogmax = getMax.setData(naturalLog);
        double op_temp = 0.0;
        double y_temp = 0.0;
        for(int i = 0 ; i < lamdas.length ; i++)
        {
          for(int j = 0 ; j < betas.length ; j++)
          {
            double x = 0.0;
            double y = 0.0;
            double op = 0.0;
            
            if((FRR[i][j] - FRRmin) != 0.0 && (FRRmax - FRRmin) != 0.0)
              x = ((FRR[i][j] - FRRmin) / (FRRmax - FRRmin));
            
//            System.out.println(FRR[i][j]);
            if((naturalLog[i][j] - naturalLogmin) != 0.0 && (naturalLogmax - naturalLogmin) != 0.0)
              y = ((naturalLog[i][j] - naturalLogmin) / (naturalLogmax - naturalLogmin));
            
//            System.out.println(y);
            op = x + C * y;
//            System.out.println(FRR[i][j]);
            if(op_temp < op)
              {
                op_temp = op;
                y_temp = y;
                FRR_temp = x;
                FIR_temp = FIR[i][j];
                container_temp = container;
                inter_temp = inter;
                lamda_temp = lamdas[i];
                beta_temp = betas[j];
                lamda_Count = i;
                beta_Count = j;
              }
          }          
        }
        
        
        isFirst = false;
        useCount[lamda_Count][beta_Count] += 1;
        useTotalCount ++;
        Reward[lamda_Count][beta_Count] += FIR_temp;
        Decay[lamda_Count][beta_Count] += D * Reward[lamda_Count][beta_Count];
        DecaySum += Decay[lamda_Count][beta_Count];
//        System.out.println(DecaySum + " , " + Decay[lamda_Count][beta_Count]);
        
        System.out.println("FIR : " + FIR_temp + " , lamda : " + lamda_temp + " , beta : " + beta_temp + " , FRR : " + FRR_temp + " , y : " + y_temp +  " , op : " + op_temp);

        /********************Adaptive********************/
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
    //              System.out.println(probabilitySum[j] + " , ");
            }
            
            int total;
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
//                  System.out.println(probabilitySum[j] + " vs " + probabilitySum[k] + " , " + PopGetObjValue[0] + " vs " +  PopGetObjValue2[0] + " , success1");
                }else if (probabilitySum[j] < probabilitySum[k] && PopGetObjValue[0] > PopGetObjValue2[0])//success
                {
                  success++;
//                  System.out.println(probabilitySum[j] + " vs " + probabilitySum[k] + " , " + PopGetObjValue[0] + " vs " +  PopGetObjValue2[0] + " , success2");
                }else if(probabilitySum[j] >= probabilitySum[k] && PopGetObjValue[0] > PopGetObjValue2[0])//error
                {
                  error++;
//                  System.out.println(probabilitySum[j] + " vs " + probabilitySum[k] + " , " + PopGetObjValue[0] + " vs " +  PopGetObjValue2[0] + " , error1");
                }else if(probabilitySum[j] < probabilitySum[k] && PopGetObjValue[0] <= PopGetObjValue2[0])//error
                {
                  error++;
//                  System.out.println(probabilitySum[j] + " vs " + probabilitySum[k] + " , " + PopGetObjValue[0] + " vs " +  PopGetObjValue2[0] + " , error2");
                }else
                {
//                  System.out.println(probabilitySum[j] + " vs " + probabilitySum[k] + " , " + PopGetObjValue[0] + " vs " +  PopGetObjValue2[0] + " , error3");
                  error++;
                }
              }
            }
            total = success + error;
            
            return ((double)((double)success / (double)total));
            /*******************************************************************/
  }
}
