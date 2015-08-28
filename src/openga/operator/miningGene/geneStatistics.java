package openga.operator.miningGene;
import openga.chromosomes.*;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: A mining gene operator.</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class geneStatistics {
  public geneStatistics() {
  }

  populationI originalPop;
  populationI duplicatedPopulation;
  int popSize, chromosomeLength;
  double container[][];//it's an m-by-m array to store the gene results. The i is job, the j is the position(sequence).
  int newSolution[];
  double evaporateRate = 0.5;
  boolean createArtificialChromosome = false;
  double avgFitness = 0, maxFitness = 0, minFitness;

  public void setData(populationI originalPop,
                                            populationI duplicatedPopulation){
    this.originalPop = originalPop;
    this.duplicatedPopulation = duplicatedPopulation;
    popSize = originalPop.getPopulationSize();
    chromosomeLength = originalPop.getSingleChromosome(0).genes.length;
    container = new double[chromosomeLength][chromosomeLength];
  }

  public void setCreateArtificialChromosome(boolean createArtificialChromosome){
    this.createArtificialChromosome = createArtificialChromosome;
  }

  /**
   * The average fitness.
   */
  public void calcAverageFitness(){
    openga.Fitness.calcAverageFitness calcAverageFitness1 = new openga.Fitness.calcAverageFitness();
    calcAverageFitness1.setData(originalPop);
    calcAverageFitness1.startCalcFitness();
    avgFitness = calcAverageFitness1.getcalcFitness();
  }

  public void startStatistics(){
    decave();
    calcAverageFitness();
    calcContainer();

    if(true){//createArtificialChromosome
      duplicatedPopulation = new population();
      duplicatedPopulation.setGenotypeSizeAndLength(originalPop.getEncodedType(), 1,
                                                    originalPop.getLengthOfChromosome(),
                                                    originalPop.getNumberOfObjectives());
      duplicatedPopulation.initNewPop();

      double temporaryContainer[][] = dumpArray();
      newSolution = new int[chromosomeLength];
      assignJobs(temporaryContainer);
      duplicatedPopulation.getSingleChromosome(0).setSolution(newSolution);
    }
  }

  public void decave(){
    for(int i = 0 ; i < container.length ; i ++ ){
      for(int j = 0 ; j < container[i].length ; j ++ ){
        container[i][j] *= evaporateRate;
      }
    }
  }

  public void calcContainer(){
    for(int i = 0 ; i < popSize ; i ++ ){
      if(originalPop.getFitness(i) < avgFitness){
         for(int j = 0 ; j < chromosomeLength ; j ++ ){
           int gene = originalPop.getSingleChromosome(i).getSolution()[j];
           container[gene][j] += 1;
         }
      }
    }
  }

  public double[][] dumpArray(){
    double temporaryContainer[][];
    temporaryContainer = new double[chromosomeLength][chromosomeLength];
    for(int i = 0 ; i < container.length ; i ++ ){
      for(int j = 0 ; j < container[i].length ; j ++ ){
        temporaryContainer[i][j] = container[i][j];
      }
    }
    return temporaryContainer;
  }

  public void assignJobs(double matrix[][]){
    openga.util.algorithm.getMax getMax1 = new openga.util.algorithm.getMax();
    for(int i = 0 ; i < chromosomeLength ; i ++ ){
      int pos[] = getMax1.getDataIndex(matrix);
      int Job = pos[0];
      int position = pos[1];
      //System.out.println(Job+"\t"+position+"\t"+matrix[Job][position]);
      newSolution[position] = Job;
      matrix = dropColumn(position, matrix);
      matrix = dropRow(Job, matrix);
    }
  }

  public double[][] dropColumn(int index, double matrix[][]){
    for(int i = 0 ; i < matrix.length ; i ++ ){
      matrix[i][index] = -1;
    }
    return matrix;
  }

  public double[][] dropRow(int index, double matrix[][]){
    for(int i = 0 ; i < matrix.length ; i ++ ){
      matrix[index][i] = -1;
    }
    return matrix;
  }

  public int[] getSolution(){
    return newSolution;
  }

  public populationI getPopulation(){
    return duplicatedPopulation;
  }
}