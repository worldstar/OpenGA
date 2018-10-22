package openga.operator.crossover;

import openga.chromosomes.*;

/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: Estimation of Distribultion algorithm determines the direction to mutate.
 * We forbid the direction while both genes move to inferior positions.</p>
 * <p>Copyright: Copyright (c) 2018</p>
 * <p>Company: Cheng Shiu University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */
public class twoPointCrossover2EDA3 extends twoPointCrossover2 implements EDA3CrossoverI {

    public twoPointCrossover2EDA3() {
    }
    double container[][];//it's an m-by-m array to store the gene results. The i is job, the j is the position(sequence).
    double inter[][];//training results
    double evaporateRate = 0.5;
    int numberOfTournament = 2;
    chromosome newChromosomes[] = new chromosome[2];
    
    int D1, D2;
    //double temporaryContainer[][];

    public void setEDAinfo(double container[][], double inter[][], int numberOfTournament) {
        this.container = container;
        this.inter = inter;
        this.numberOfTournament = numberOfTournament;
    }
    
  @Override
  public void setEDAinfo(double[][] container, double[][] inter, int numberOfCrossoverTournament, int D1, int D2) {
        this.container = container;
        this.inter = inter;
        this.numberOfTournament = numberOfTournament;
        this.D1 = D1;
        this.D2 = D2;
  }    
  
  @Override
  public void setEDAinfo(int D1, int D2) {
        this.D1 = D1;
        this.D2 = D2;
  }     

    //start to crossover
    public void startCrossover() {
        //System.out.println("Here");
        //System.exit(0);

        for (int i = 0; i < 2; i++) {
            newChromosomes[i] = new chromosome();
            newChromosomes[i].setGenotypeAndLength(originalPop.getEncodedType(), originalPop.getLengthOfChromosome(), originalPop.getNumberOfObjectives());
            newChromosomes[i].initChromosome();
        }

        for (int i = 0; i < popSize ; i++) {
            //test the probability is larger than crossoverRate.
            if (Math.random() <= crossoverRate) {
                //to get the other chromosome to crossover
                setCutpoint();
                checkCutPoints(i);
            }
        }

    }

    public void checkCutPoints(int selectedSoln) {
        if (numberOfTournament == 0) {
            System.out.println("numberOfTournament is at least 1.");
            System.exit(0);
        }

        double probabilitySum;
        double maxProb = 0.0;
        //maxProb = sumGeneInfo(originalPop.getSingleChromosome(selectedSoln), cutPoint1, cutPoint2);

        /*        //middle accumulate
        for (int posi = cutPoint1; posi < cutPoint2 - 1; posi++) {
        for (int jobj = cutPoint1; jobj < cutPoint2; jobj++) {
        temporaryContainer[jobj][posi + 1] = temporaryContainer[jobj][posi] + temporaryContainer[jobj][posi + 1];
        }
        }
         */

        for (int i = 0; i < numberOfTournament; i++) {
            int index2 = getCrossoverChromosome(selectedSoln);//to get a chromosome to be mated.
            copyElements(originalPop.getSingleChromosome(selectedSoln), originalPop.getSingleChromosome(index2), newChromosomes[0]);
            copyElements(originalPop.getSingleChromosome(index2), originalPop.getSingleChromosome(selectedSoln), newChromosomes[1]);
            /*
            System.out.println("Tournament : " + i);
            System.out.println("original : ");
            for (int k = 0; k < originalPop.getSingleChromosome(selectedSoln).getLength(); k++) {
            System.out.print(originalPop.getSingleChromosome(selectedSoln).genes[k] + ",");
            }
            System.out.println();

            System.out.println("selected : ");
            for (int k = 0; k < originalPop.getSingleChromosome(index2).getLength(); k++) {
            System.out.print(originalPop.getSingleChromosome(index2).genes[k] + ",");
            }
            System.out.println();
             */
 
            if (numberOfTournament == 1) {//it needs not to collect the gene information
                probabilitySum = 10.0;
            } else {
                probabilitySum = sumGeneInfo(newChromosomes[0], cutPoint1, cutPoint2);
                //probabilitySum += sumGeneInfo(newChromosomes[1], cutPoint1, cutPoint2);
                //System.out.println(probabilitySum);
            }

            if (maxProb < probabilitySum) {
                maxProb = probabilitySum;
                newPop.setSingleChromosome(selectedSoln, newChromosomes[0]);
                newPop.setSingleChromosome(index2, newChromosomes[1]);                
            }
        }
        
        //System.exit(0);
    }

    protected double sumGeneInfo(chromosome _chromosome, int cutPoint1, int cutPoint2) {
        double total = 0;
        double sum = 0;
        int job1 = 0;
        
        for (int i = cutPoint1; i < (cutPoint2 + D2) && i < _chromosome.getLength() - 1; i++) {
            int job2 = _chromosome.getSolution()[i];
            if (i == 0) {
//                    job1 = (int) (_chromosome.getLength() * Math.random());                
                sum = container[job2][i] ;
            } else {                    
                double tempContainer = 0;
                double tempInter = 0;
                
                for(int k = 0 ; k <= D1; k++){
                  if(i - k < 0) break;

                  job1 = _chromosome.getSolution()[i - k];
                  tempContainer += container[job2][i - k];
                }                

                for(int k = 0 ; k <= D2; k++){
                  if(i - k - 1 < 0) break;

                  job1 = _chromosome.getSolution()[i - k - 1];
                  tempInter += inter[job1][job2];
                }
                sum = tempInter * tempContainer;
            }             

            total += sum;
        }    

        return total;
    }

    private void copyElements(chromosome parent1, chromosome parent2, chromosome child1) {
        child1.setSolution(parent1.getSolution());
        //to modify the first chromosome between the index1 to index2, which genes
        //is from chromosome 2.
        int counter = 0;
        for (int i = cutPoint1; i <= cutPoint2; i++) {
            while (checkConflict(parent2.genes[counter], parent1.genes) == true) {
                counter++;
            }
            child1.setGeneValue(i, parent2.genes[counter]);
            counter++;
        }
    }

    private boolean checkConflict(int newGene, int _chromosome[]) {
        boolean hasConflict = false;
        for (int i = 0; i < cutPoint1; i++) {
            if (newGene == _chromosome[i]) {
                return true;
            }
        }

        for (int i = cutPoint2 + 1; i < chromosomeLength; i++) {
            if (newGene == _chromosome[i]) {
                return true;
            }
        }

        return hasConflict;
    }


    public void setData(int numberOfJob, int numberOfMachines, int[][] processingTime) {
    }

    public void setData(int numberofParents) {
    }

    public void setEDAinfo(double[][] container, int numberOfTournament) {
    }
}
