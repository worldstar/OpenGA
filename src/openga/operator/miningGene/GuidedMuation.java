package openga.operator.miningGene;
import openga.chromosomes.*;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: The algorithm simulate from EA/G. Please refer to
 * Zhang, Q., Sun, J., & Tsang, E. (2005). An evolutionary algorithm with guided mutation for the maximum clique problem. IEEE Transactions on Evolutionary Computation, 9(2), 192-200.</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class GuidedMuation extends probabilityMatrix{
  public GuidedMuation() {
  }
  double container[][];//Probability models. The i is job, the j is the position(sequence).
  populationI archive;//the current best solutions.
  double beta = 0.9;//the percentage of genes are sampled from probability models.
  int numberOfSamples = 1;//It is actually the beta*length
  int currentBestSolutionIndex = 0;

  public void setEDAinfo(double container[][], populationI archive, double beta){
    this.container = container;
    this.archive = archive;
    this.beta = beta;
  }

  public void startStatistics(){//start Guided Muatation
    numberOfSamples = (int)(beta*chromosomeLength);
    assignSequence = new int[chromosomeLength];
    currentBestSolutionIndex = getBestIndex();

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
    for(int j = 0 ; j < chromosomeLength ; j++){
      int pos = assignSequence[j];
      int jobIndex = archive.getSingleChromosome(currentBestSolutionIndex).getSolution()[pos];

      if(j >= numberOfEliteGene){//The rest of genes are selected by probability selection.
        normArray(temporaryContainer, pos);
        jobIndex = selectJob(temporaryContainer, pos);
      }

      //System.out.println(pos+" "+jobIndex+" ");
      solution.setGeneValue(pos, jobIndex); //to set the job into the ith chromosome at position j
      temporaryContainer = dropColumn(pos, temporaryContainer);
      temporaryContainer = dropRow(jobIndex, temporaryContainer);

      if(applyEvaporation == true){
        evaporation(j, jobIndex);
      }
    }
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