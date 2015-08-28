package openga.operator.clone;
import openga.chromosomes.*;
import openga.operator.crossover.*;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: To clone chromosome from other population to current population.</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class populationClone {
  public populationClone() {
  }
  populationI Population[];//all sub-populations
  CrossoverI Crossover;
  populationI archive;
  int sizeOfPop, length, numberOfObjs;
  boolean encodeType;
  //There are cloneRation % chromosomes are clones. Others are generated randomly.
  double cloneRation = 1;
  int numberOfClone = 0;
  int popIndex[];
  int numberOfSubPopulations;
  int popIndex1, popIndex2;
  double weightVector[][];
  double max[], min[];

  public void setData(populationI Population[], populationI archive, CrossoverI Crossover, int popIndex[], double weightVector[][], double cloneRation, int sizeOfPop){
    this.Population = Population;
    this.archive = archive;
    this.Crossover = Crossover;
    this.sizeOfPop = sizeOfPop;
    length = Population[0].getLengthOfChromosome();
    numberOfObjs = Population[0].getNumberOfObjectives();
    encodeType = Population[0].getEncodedType();

    numberOfClone = (int)(sizeOfPop*cloneRation);
    //numberOfClone = (int)(200*cloneRation);
    numberOfSubPopulations = Population.length;
    this.popIndex = popIndex;
    this.weightVector = weightVector;
    this.cloneRation = cloneRation;
    max = new double[numberOfObjs];
    min = new double[numberOfObjs];
    getMinMax();
    //System.out.println(cloneRation+"\t"+sizeOfPop+"\t"+numberOfClone);
  }

  public void startClonePopulation(){
    for(int i = 0 ; i < numberOfSubPopulations ; i ++ ){
      if((i < 4 || i > 7) && checkGroup(i, popIndex)){//To initialize the population that is not run yet.
        Population[i].setGenotypeSizeAndLength(encodeType, sizeOfPop, length, numberOfObjs);
        Population[i].initNewPop();
        Population[i] = generateNewPopulation(Population[i], weightVector[i]);
      }
    }
  }

  private boolean checkGroup(int index, int[] group){
    for(int i = 0 ; i < group.length ; i ++ ){
      if(index == group[i]){
        return false;
      }
    }
    return true;
  }

  private populationI generateNewPopulation(populationI population1, double _weight[]){
    int archiveSize = archive.getPopulationSize();
    int counter = 0;
    if(numberOfClone < archiveSize){
      archiveSize = numberOfClone;
    }
/*
    populationI arch1 = crossoverStage(archive);
    for(int i = 0 ; i < archiveSize ; i ++ ){
      population1.setSingleChromosome(i, arch1.getSingleChromosome(counter++));
    }
*/
     counter = 0;
     for(int i = 0 ; i < 2*archiveSize ; i ++ ){
       population1.setSingleChromosome(i, archive.getSingleChromosome(counter++));
       population1.setSingleChromosome(i, generateNewSolution(population1.getSingleChromosome(i)));
       counter = counter % archiveSize;
     }

   //clone solution by tenary tournament
   for(int i = archiveSize ; i < numberOfClone ; i ++ ){
     getPopulationIndex();
     int index1 = (int)(sizeOfPop*Math.random());//the chromsome from population 1
     chromosome cloneChromosome = Population[popIndex1].getSingleChromosome(index1);

     for(int j = 0 ; j < 10 ; j ++ ){
       int index2 = (int)(sizeOfPop*Math.random());//the chromsome from the other population
       cloneChromosome =
           binaryTournament(cloneChromosome, Population[popIndex2].getSingleChromosome(index2),
                            _weight);
       getPopulationIndex();
     }

     population1.setSingleChromosome(i, cloneChromosome);
     population1.setSingleChromosome(i, generateNewSolution(population1.getSingleChromosome(i)));
   }

    //random generate solution
    for(int i = numberOfClone ; i < sizeOfPop ; i ++ ){
      population1.setSingleChromosome(i, generateNewSolution());
    }
    //population1 crossoverStage(population1)
    return population1;
  }

  private void getPopulationIndex(){
    popIndex1 = (int)(popIndex.length*Math.random());
    popIndex2 = (int)(popIndex.length*Math.random());
    while(popIndex.length > 1 && popIndex1 == popIndex2){
      popIndex2 = (int)(popIndex.length*Math.random());
    }
    popIndex1 = popIndex[popIndex1];
    popIndex2 = popIndex[popIndex2];
  }

  private void getMinMax(){
    openga.util.algorithm.getMax getMax1 = new openga.util.algorithm.getMax();
    openga.util.algorithm.getMin getMin1 = new openga.util.algorithm.getMin();

    for(int i = 0 ; i < numberOfObjs ; i ++ ){
      max[i] = getMax1.setData(archive.getObjectiveValueArray(), i);
      min[i] = getMin1.setData(archive.getObjectiveValueArray(), i);
    }
  }

  //simply to determine which one is better by fitness value.
  //the chromosome has smaller fittness value is better from we use the criteria of Goldberg's fitness assignment.
  public chromosome binaryTournament(chromosome chromosome1, chromosome chromosome2, double _weight[]){
    double fitness[] = new double[2];

    for(int i = 0 ; i < numberOfObjs ; i ++ ){
      fitness[0] += ((chromosome1.getObjValue()[i] - min[i])/(max[i] - min[i]))*_weight[i];
      fitness[1] += ((chromosome2.getObjValue()[i] - min[i])/(max[i] - min[i]))*_weight[i];
    }

    if(fitness[0] <= fitness[1]){
      return chromosome1;
    }
    return chromosome2;
  }

  /**
   * @param chromosome1
   * @return To generate new solution by swap mutation operator.
   */
  public chromosome generateNewSolution(chromosome chromosome1){
    openga.operator.mutation.swapMutation Mutation1 = new openga.operator.mutation.swapMutation();
    Mutation1.chromosomeLength = Population[0].getLengthOfChromosome();
    Mutation1.setCutpoint();
    return Mutation1.swaptGenes(chromosome1);
  }

  /**
   * @return To generate a random solution.
   */
  public chromosome generateNewSolution(){
    chromosome chromosome1 = new chromosome();
    chromosome1.setGenotypeAndLength(Population[0].getEncodedType(),
       Population[0].getLengthOfChromosome(), Population[0].getNumberOfObjectives());
    chromosome1.generateSequentialPop(length);
    return chromosome1;
  }

  public populationI crossoverStage(populationI Population){
    //openga.operator.crossover.twoPointCrossover2 Crossover = new openga.operator.crossover.twoPointCrossover2();
    Crossover.setData(1, Population);
    Crossover.startCrossover();
    Population = Crossover.getCrossoverResult();
    return Population;
  }

/*
  public populationI crossoverStage(populationI Population){
    openga.operator.crossover.twoPointCrossover2 Crossover = new openga.operator.crossover.twoPointCrossover2();
    Crossover.setData(1, Population);
    Crossover.startCrossover();
    Population = Crossover.getCrossoverResult();
    return Population;
  }
*/




}