package openga.operator.miningGene;
import openga.chromosomes.*;
import openga.operator.crossover.*;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class geneStatistics5 extends geneStatistics {
  public geneStatistics5() {
  }
  int temperoryPosition[];
  double numberOfTempPositionRate = 0.2;
  int numberOfTempPosition;
  CrossoverI Crossover = new crossoverBySeedSolution();
  populationI archive;

  public void startStatistics(){
    numberOfTempPosition = (int)(numberOfTempPositionRate*originalPop.getLengthOfChromosome());
    temperoryPosition = new int[numberOfTempPosition];
    decave();
    calcAverageFitness();
    calcContainer();

    if(true){//createArtificialChromosome
      double temporaryContainer[][] = dumpArray();
      newSolution = new int[chromosomeLength];
      assignJobs(temporaryContainer);
      duplicatedPopulation = new population();
      duplicatedPopulation.setGenotypeSizeAndLength(originalPop.getEncodedType(),
          2, originalPop.getLengthOfChromosome(),
          originalPop.getNumberOfObjectives());
      duplicatedPopulation.initNewPop();
      duplicatedPopulation.getSingleChromosome(0).setSolution(newSolution);
    }
  }

  public void assignJobs(double matrix[][]){
    openga.util.algorithm.getMax getMax1 = new openga.util.algorithm.getMax();
    int counter = 0;
    for(int i = 0 ; i < chromosomeLength ; i ++ ){
      int pos[] = getMax1.getDataIndex(matrix);
      int Job = pos[0];
      int position = pos[1];
      newSolution[position] = Job;
      matrix = dropColumn(position, matrix);
      matrix = dropRow(Job, matrix);
      if(i > chromosomeLength - numberOfTempPosition){
        temperoryPosition[counter++] = position;
      }
    }
  }

  public chromosome mateSolutions(chromosome artificial, chromosome orginal){
    duplicatedPopulation.setSingleChromosome(0, artificial);
    duplicatedPopulation.setSingleChromosome(1, orginal);
    duplicatedPopulation = crossoverStage(duplicatedPopulation);

    return duplicatedPopulation.getSingleChromosome(1);
  }


  public populationI crossoverStage(populationI Population){
    Crossover.setData(1, Population);
    Crossover.startCrossover();
    Population = Crossover.getCrossoverResult();
    return Population;
  }
}