package openga.operator.mutation;

import openga.chromosomes.*;

/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: Estimation of Distribultion algorithm determines the direction to mutate.
 * We forbid the direction while both genes move to inferior positions.</p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */
public class swapMutationEDA3 extends swapMutationEDA implements EDA3MutationI {

    public swapMutationEDA3() {
    }
    double container[][];//it's an m-by-m array to store the gene results. The i is job, the j is the position(sequence).
    double inter[][];
    int numberOfTournament = 2;
    int D1;
    int D2;

    public void setData(double mutationRate, populationI population1) {
        pop = new population();
        this.pop = population1;
        this.mutationRate = mutationRate;
        popSize = population1.getPopulationSize();
        chromosomeLength = population1.getSingleChromosome(0).genes.length;
    }

    public void setEDAinfo(double container[][], double inter[][], int numberOfTournament) {
        this.container = container;
        this.inter = inter;
        this.numberOfTournament = numberOfTournament;
    }
    
    public void setEDAinfo(int D1, int D2) {
      this.D1 = D2;
      this.D2 = D2;
    }    

    /**
     * we determine the mutation cut-point by mining the gene information.
     */
    public void startMutation() {
        for (int i = 0; i < popSize; i++) {
            //test the probability is larger than mutationRate.
            if (Math.random() <= mutationRate) {
                checkCutPoints(pop.getSingleChromosome(i));
                pop.setChromosome(i, swaptGenes(pop.getSingleChromosome(i)));
                /*
                System.out.println("change : " + i);
                for (int k = 0; k < pop.getSingleChromosome(i).getLength(); k++) {
                System.out.print(pop.getSingleChromosome(i).genes[k] + ",");
                }
                System.out.println();
                 */
            }
        }
    }

    /**
     * If the both gene moves to inferior positions, we generate the other one to replace it.
     * @param _chromosome
     */
    public void checkCutPoints(chromosome _chromosome) {
        if (numberOfTournament == 0) {
            System.out.println("numberOfTournament is at least 1.");
            System.exit(0);
        }
        int cutPoints[][] = new int[numberOfTournament][2];
        double probabilitySum;
        double maxProb = 0.0;
        int selectedIndex = 0;

        for (int i = 0; i < numberOfTournament; i++) {
            setCutpoint();

            cutPoints[i][0] = cutPoint1;
            cutPoints[i][1] = cutPoint2;
            if (numberOfTournament == 1) {//it needs not to collect the gene information
                selectedIndex = i;
            } else {
                probabilitySum = sumGeneInfo(_chromosome, cutPoint1, cutPoint2);

                if (maxProb < probabilitySum) {
                    maxProb = probabilitySum;
                    selectedIndex = i;
                }
            }
        }

        cutPoint1 = cutPoints[selectedIndex][0];
        cutPoint2 = cutPoints[selectedIndex][1];
        cutPoints = null;
    }

   
    private double sumGeneInfo(chromosome _chromosome, int cutPoint1, int cutPoint2) {
        double sum = 0;
        
        for (int i = cutPoint1; i < (cutPoint1 + D2) && i < _chromosome.getLength() - 1; i++) {
          int job2 = _chromosome.getSolution()[i];
          double tempContainer = 0;
          double tempInter = 0;
          
          for(int k = 0 ; k <= D1; k++){
            if(i - k < 0) break;
            tempContainer += container[job2][i - k];
          }            
          
          for(int k = 0 ; k <= D2; k++){
            if(i == 0){              
              sum += container[job2][i];
            }
            else{
              if(i - k < 0) break;
              
              int job1 = _chromosome.getSolution()[i - k];
              tempInter += inter[job1][job2];
            }            
          }
          sum += tempInter * tempContainer;
        }
        
        for (int i = cutPoint2; i < (cutPoint2 + D2) && i < _chromosome.getLength() - 1; i++) {
          int job2 = _chromosome.getSolution()[i];
          double tempContainer = 0;
          double tempInter = 0;
          
          for(int k = 0 ; k <= D1; k++){
            if(i - k < 0) break;
            tempContainer += container[job2][i - k];
          }           
          
          for(int k = 0 ; k <= D2; k++){
            if(i - k - 1 < 0) break;

            int job1 = _chromosome.getSolution()[i - k - 1];
            tempInter += inter[job1][job2];            
          }
          
          sum += tempInter * tempContainer;
        }        
        
        return sum;
    }    

    public void setEDAinfo(double[][] container, int numberOfTournament) {
    }
}
