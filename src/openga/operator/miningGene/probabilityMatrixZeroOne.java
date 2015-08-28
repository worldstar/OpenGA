/*
 * For the 0-1 problems, such as the knapsack problems.
 */

package openga.operator.miningGene;
import openga.chromosomes.*;
/**
 *
 * @author user
 */
public class probabilityMatrixZeroOne extends probabilityMatrix{
  int popSize, chromosomeLength;
  double container[][];//it's an m-by-m array to store the gene results. The i is job, the j is the position(sequence).
  double MarkovContainer[][];
  int newSolution[];
  double avgFitness = 0, maxFitness = 0, minFitness;
  double duplicateRate = 1.0;
  int numCopied;
  int strategy = 1;
  int assignSequence[];
  boolean applyEvaporation = false;
  String evaporationMethod = "constant";//constant, method1, method2
  double currentBest;
  boolean currentBestIsSet = false;
  double currentMin, currentMax;
  boolean currentMinMaxIsSet = false;

  //to restore information
  double backupcontainer[][];
  
  public void setData(populationI originalPop,
                                            populationI duplicatedPopulation){
    this.originalPop = originalPop;
    this.duplicatedPopulation = duplicatedPopulation;
    popSize = originalPop.getPopulationSize();
    chromosomeLength = originalPop.getSingleChromosome(0).genes.length;
    //First row is for zero, and the 2nd-row is for the one
    container = new double[2][chromosomeLength];
  }

  public void startStatistics(){
    foundemental();
    assignJobs(container);
  }

  public void foundemental(){
    calcAverageFitness();
    calcContainer();
    //squareProbabilityMatrix();
    assignSequence = new int[chromosomeLength];
  }

  public void calcContainer(){

    int counter = 0;
    for(int i = 0 ; i < popSize ; i ++ ){
      if(originalPop.getFitness(i) <= avgFitness){
         for(int j = 0 ; j < chromosomeLength ; j ++ ){
           int gene = originalPop.getSingleChromosome(i).getSolution()[j];

           if(gene == 0){
            container[0][j] += 1.0;
           }
           else{
             container[1][j] += 1.0;
           }           
         }
         counter ++;
      }
    }

    //to normalize them between [0, 1].
    for(int i = 0 ; i < container.length ; i ++ ){
      for(int j = 0 ; j < container[i].length ; j ++ ){
        container[i][j] /= counter;
      }
    }
  }

  public double[][] dumpArray(double _container[][]){
    double temporaryContainer[][];
    temporaryContainer = new double[_container.length][_container[0].length];
    for(int i = 0 ; i < _container.length ; i ++ ){
      for(int j = 0 ; j < _container[i].length ; j ++ ){
        temporaryContainer[i][j] = _container[i][j];
      }
    }
    return temporaryContainer;
  }

  public int selectJob(double matrix[][], int columnIndex){
    System.out.println("selectJob(double matrix[][], int columnIndex) is under construction. Exit.");
    System.exit(0);
    return 1;
  }

  public int selectJobTemp(double matrix[][], int columnIndex){
    int job = 0;
    double AccProb[] = new double[chromosomeLength];
    double prob = Math.random();
    double buff = 0.0;
    double SumFitness = 0.0;

    for(int i = 0 ; i < chromosomeLength ; i++){    //To calculate accumulative probability.
      if(matrix[i][columnIndex] != -1){
        buff += matrix[i][columnIndex];
        AccProb[i] = buff;
      }
      else{
        AccProb[i] = 0;
      }
    }

    if(buff == 0){
      reAssign(matrix, columnIndex, AccProb);
    }

    for(int j = 0 ; j < chromosomeLength ; j++){    //To check what is the region it belongs to.
      if(prob <= AccProb[j]){
        job = j;
        break;
      }
    }
    return job;
  }
  
}
