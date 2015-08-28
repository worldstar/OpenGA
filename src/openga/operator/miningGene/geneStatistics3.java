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

public class geneStatistics3 extends geneStatistics {
  public geneStatistics3() {
  }
  int temperoryPosition[];
  double numberOfTempPositionRate = 0.2;
  int numberOfTempPosition;
  CrossoverI Crossover = new crossoverBySeedSolution();
  populationI archive;

  int bound[] = new int[2];//the lower bound and upper bound solution of archive.

  private void getBound(populationI _archive){
    double max = 0;
    double min = Double.MAX_VALUE;

    for(int i = 1 ; i < _archive.getPopulationSize() ; i ++ ){
      if(_archive.getObjectiveValues(i)[0] < min){
        min = _archive.getObjectiveValues(i)[0];
        bound[0] = i;
      }

      if(_archive.getObjectiveValues(i)[0] > max){
        max = _archive.getObjectiveValues(i)[0];
        bound[1] = i;
      }
    }
  }

  public void setArchive(populationI archive){
    this.archive = archive;
  }

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
          1, originalPop.getLengthOfChromosome(),
          originalPop.getNumberOfObjectives());
      duplicatedPopulation.initNewPop();
      duplicatedPopulation.getSingleChromosome(0).setSolution(newSolution);
      duplicatedPopulation = combinePopuplation(duplicatedPopulation, archive);
      duplicatedPopulation = crossoverStage(duplicatedPopulation);
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

  public populationI crossoverStage(populationI Population){
    Crossover.setData(1, Population);
    Crossover.startCrossover();
    Population = Crossover.getCrossoverResult();
    return Population;
  }

  public populationI combinePopuplation(populationI originalSet, populationI tempParetoFront){
   combinedTwoPopulations combinedTwoPopulations1 = new combinedTwoPopulations();
   combinedTwoPopulations1.setTwoPopulations(originalSet, tempParetoFront);
   combinedTwoPopulations1.startToCombine();
   return combinedTwoPopulations1.getPopulation();
  }

}