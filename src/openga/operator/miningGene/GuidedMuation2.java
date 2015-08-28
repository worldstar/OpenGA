package openga.operator.miningGene;
import openga.chromosomes.*;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: EA/G. The genes are not assigned by using proportional selection.</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class GuidedMuation2 extends probabilityMatrix{
  public GuidedMuation2() {
  }

  double container[][];//Probability models. The i is job, the j is the position(sequence).
  populationI archive;//the current best solutions.
  double beta = 0.9;//the percentage of genes are sampled from probability models.
  int numberOfSamples = 1;//It is actually the beta*length
  int currentBestSolutionIndex = 0;
  int numberOfTournament = 2;


  public void setEDAinfo(double container[][], populationI archive, double beta){
    this.container = container;
    this.archive = archive;
    this.beta = beta;
  }

  public void startStatistics(){//start Guided Muatation
    numberOfSamples = (int)(beta*chromosomeLength);
    assignSequence = new int[chromosomeLength];
    currentBestSolutionIndex = getBestIndex();
    double maxProb = 0;

    for(int i = 0 ; i < popSize ; i ++ ){//start numCopied duplicatedPopulation.getPopulationSize()
      double temporaryContainer[][];
      temporaryContainer = dumpArray(container);
      //strategy
      assignmentSequence(strategy, assignSequence);
      generateChromosome(temporaryContainer, duplicatedPopulation.getSingleChromosome(i), numberOfSamples, currentBestSolutionIndex);
    }
  }

  public void generateChromosome(double temporaryContainer[][], chromosome solution, int numberOfSamples, int currentBestSolutionIndex){
    //the number of genes are copied from best solution.
    int numberOfEliteGene = chromosomeLength - numberOfSamples;

    for(int j = 0 ; j <= numberOfEliteGene ; j++){
      int pos = assignSequence[j];
      int jobIndex = archive.getSingleChromosome(currentBestSolutionIndex).getSolution()[pos];

      solution.setGeneValue(pos, jobIndex); //to set the job into the ith chromosome at position j
      temporaryContainer = dropColumn(pos, temporaryContainer);
      temporaryContainer = dropRow(jobIndex, temporaryContainer);
    }

    //to get what are the jobs which are not assigned yet.
    int unassignedJobs[] = new int[numberOfSamples];
    for(int i = numberOfEliteGene+1 ; i < chromosomeLength ; i ++){
      int pos = assignSequence[i];
      int jobIndex = archive.getSingleChromosome(currentBestSolutionIndex).getSolution()[pos];
      unassignedJobs[i] = jobIndex;
    }

    unassignedJobs = shaking(unassignedJobs);
    double maxProb = sumGeneInfo(unassignedJobs, assignSequence, numberOfEliteGene+1);
    for(int k = 0 ; k < numberOfTournament ; k ++ ){
      if(maxProb >= sumGeneInfo(unassignedJobs, assignSequence, numberOfEliteGene+1)){
        for(int i = numberOfEliteGene+1 ; i < chromosomeLength ; i ++){
          int pos = assignSequence[i];
          int jobIndex = unassignedJobs[pos];
          solution.setGeneValue(pos, jobIndex); //to set the job into the ith chromosome at position j
        }
      }
      unassignedJobs = shaking(unassignedJobs);
    }
  }

  private double sumGeneInfo(int soln[], int seq[], int start){
    double sum = 0;
    int counter = 0;
    for(int i = start ; i < seq.length ; i ++){
      int pos = seq[i];
      int job = soln[counter++];
      sum += container[job][pos];
    }
    return sum;
  }


  public int[] shaking(int array1[]){
    for(int i = 0 ; i < array1.length ; i ++ ){
      if(Math.random() > 0.5){
        int index1 = (int)(Math.random()*array1.length);
        int temp = array1[i];
        array1[i] = array1[index1];
        array1[index1] = temp;
      }
    }
    return array1;
  }



  /**
   * The best objective value among the population.
   * Note: it's only for the single objctive problem.
   * @return
   */
  public int getBestIndex(){
    double _obj = Double.MAX_VALUE;
    int index = 0;
    for(int i = 0 ; i < archive.getPopulationSize() ; i ++ ){
      if(_obj > archive.getObjectiveValueArray()[i][0]){
        _obj = archive.getObjectiveValueArray()[i][0];
        index = i;
      }
    }
    return index;
  }

}