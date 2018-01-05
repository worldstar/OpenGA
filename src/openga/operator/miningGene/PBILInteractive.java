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
public class PBILInteractive {

    public PBILInteractive(populationI originalPop, double lamda, double beta) {
        this.originalPop = originalPop;
        popSize = originalPop.getPopulationSize();
        chromosomeLength = originalPop.getSingleChromosome(0).genes.length;
        this.lamda = lamda;
        this.beta = beta;
        container = new double[chromosomeLength][chromosomeLength];
        inter = new double[chromosomeLength][chromosomeLength];

        for (int i = 0; i < chromosomeLength; i++) {
            for (int j = 0; j < chromosomeLength; j++) {
                container[i][j] = 0.1;
            }
        }

        for (int i = 0; i < chromosomeLength; i++) {
            for (int j = 0; j < chromosomeLength; j++) {
                inter[i][j] = 0.1;  //1.0
            }
        }
    }
    populationI originalPop;
    int popSize, chromosomeLength;
    double container[][];//it's an m-by-m array to store the gene results. The i is job, the j is the position(sequence).
    double avgFitness = 0;
    double inter[][] = new double[chromosomeLength][chromosomeLength];
    double lamda; //learning rate
    double beta;

    public void setData(populationI originalPop) {
        this.originalPop = originalPop;
        popSize = originalPop.getPopulationSize();
    }

    public void startStatistics() {
        calcAverageFitness();
        calcContainer();
        calcInter();
    }

    /**
     * The average fitness.
     */
    public void calcAverageFitness() {
        openga.Fitness.calcAverageFitness calcAverageFitness1 = new openga.Fitness.calcAverageFitness();
        calcAverageFitness1.setData(originalPop);
        calcAverageFitness1.startCalcFitness();
        avgFitness = calcAverageFitness1.getcalcFitness();
        avgFitness *= 1.2;
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

//        CheckModelAccurracy();
    }
    
    public void CheckModelAccurracy()
  {
    
    /*******************************************************************/
            double[] probabilitySum = new double[originalPop.getPopulationSize()];
            
            for(int j = 0 ; j < originalPop.getPopulationSize() ; j++)
            {
              probabilitySum[j] = productGeneInfo(originalPop.getSingleChromosome(j) , container);
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
            
            System.out.printf("成功率 : %.2f\n",((double)((double)success / (double)total)));
            
            /*******************************************************************/
  }
  
  //Probability product, instead of summing up the probability.
    //2009.8.20
    private double productGeneInfo(chromosome _chromosome , double[][] container){
      double productValue = 1;
      for(int i = 0 ; i < _chromosome.getLength() ; i ++){
        int job = _chromosome.getSolution()[i];
        productValue *= container[job][i] * 10;
      }
//      //productVale *= Math.pow(10, cutPoint2 - cutPoint1);
      return productValue;
    }

    public double calculate_diversity() {
        int temp1[][] = new int[chromosomeLength][chromosomeLength];
        int temp2[][] = new int[chromosomeLength][chromosomeLength];
        double counter1 = 0;
        double counter2 = 0;
        double diversity = 0.0;
        //to collect gene information.
        /*
        System.out.println("container:before");
        for(int i = 0 ; i < chromosomeLength ; i ++ ){
        for(int j = 0 ; j < chromosomeLength ; j ++ ){
        System.out.print(container[i][j]+",")  ;
        }
        System.out.println()  ;
        }
         */

        for (int i = 0; i < popSize; i++) {
            for (int j = 0; j < chromosomeLength; j++) {
                int gene = originalPop.getSingleChromosome(i).getSolution()[j];
                temp1[gene][j] += 1;
            }
        }

        //to count position diversity
        for (int i = 0; i < chromosomeLength; i++) {
            for (int j = 0; j < chromosomeLength; j++) {
                if (temp1[i][j] > 0) {
                    counter1++;
                }
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

        for (int i = 0; i < popSize; i++) {
            for (int j = 0; j < (chromosomeLength - 1); j++) {
                int gene = originalPop.getSingleChromosome(i).getSolution()[j];
                int gene_after = originalPop.getSingleChromosome(i).getSolution()[j + 1];
                temp2[gene_after][gene]++;
            }
        }

        for (int i = 0; i < chromosomeLength; i++) {
            for (int j = 0; j < chromosomeLength; j++) {
                if (temp2[i][j] > 0) {
                    counter2++;
                }
            }
        }
        double div1 = (counter1 - chromosomeLength) / (chromosomeLength * chromosomeLength);
        double div2 = (counter2 - (chromosomeLength - 1)) / ((chromosomeLength - 1) * (chromosomeLength - 1));
        diversity = (div1 + div2) / 2;

        //System.out.println(counter1 + "," +counter2 + "," + diversity)  ;
        return diversity;
    }

    public double[][] getContainer() {
        return container;
    }

    public double[][] getInter() {
        return inter;
    }
}
