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

public class geneStatistics2 extends geneStatistics {
  public geneStatistics2() {
  }
  int temperoryPosition[];
  double numberOfTempPositionRate = 0.2;
  int numberOfTempPosition;
  CrossoverI Crossover = new crossoverBySeedSolution();
  populationI archive;

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
      //generateSolutions(duplicatedPopulation);
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

      if(i < matrix.length*0.1){
        //container[Job][position] = 0;
      }
    }
  }

  public void generateSolutions(populationI _duplicatedPopulation){
    for(int i = 1 ; i < _duplicatedPopulation.getPopulationSize() ; i ++ ){
      //duplicate first
      _duplicatedPopulation.setSingleChromosome(i, _duplicatedPopulation.getSingleChromosome(0));

      //variate solutions
      for(int j = 0 ; j < numberOfTempPosition ; j ++ ){
        int index1 = (int)(Math.random()*numberOfTempPosition);
        int index2 = (int)(Math.random()*numberOfTempPosition);
        int temp = _duplicatedPopulation.getSingleChromosome(i).getSolution()[temperoryPosition[index1]];
        _duplicatedPopulation.getSingleChromosome(i).getSolution()[temperoryPosition[index1]] =
            _duplicatedPopulation.getSingleChromosome(i).getSolution()[temperoryPosition[index2]];
        _duplicatedPopulation.getSingleChromosome(i).getSolution()[temperoryPosition[index2]] = temp;
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
    populationI newPop = new population();
    newPop.setGenotypeSizeAndLength(originalSet.getEncodedType(), tempParetoFront.getPopulationSize()*5, originalSet.getLengthOfChromosome(), originalSet.getNumberOfObjectives());
    newPop.initNewPop();
    newPop.setSingleChromosome(0, originalSet.getSingleChromosome(0));
    for(int i = 1 ; i < newPop.getPopulationSize() ; i ++ ){
      newPop.setSingleChromosome(i, tempParetoFront.getSingleChromosome(i%tempParetoFront.getPopulationSize()));
    }
    return newPop;
    //newPop.toString();

    /*
    combinedTwoPopulations combinedTwoPopulations1 = new combinedTwoPopulations();
    combinedTwoPopulations1.setTwoPopulations(originalSet, tempParetoFront);
    combinedTwoPopulations1.startToCombine();
    return combinedTwoPopulations1.getPopulation();
        */
  }


}