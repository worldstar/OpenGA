/*
 * The main features contain the secondary population and control the evolutionary
 * process of two kinds crossover operator and the mutation operators for two-part
 * chromosome encoding.
 */
/**
 *
 * @author Shih-Hsin Chen
 */
package openga.MainProgram;
import openga.chromosomes.*;
import openga.operator.selection.*;
import openga.operator.crossover.*;
import openga.operator.localSearch.*;
import openga.operator.mutation.*;
import openga.operator.clone.*;
import openga.ObjectiveFunctions.*;
import openga.Fitness.*;
import openga.util.printClass;

public class singleThreadGAforMTSP extends singleThreadGA{

  boolean applyLocalSearch = true;
  localSearchI localSearch1;
  int currentUsedSolution = 0;
  int maxNeighborhood = 5;  //A default value of the maximum neighbors to search.
  int totalExaminedSolution = 100; //the main stopping criteria of genetic local search
  
  populationI Population2;//Part II chromosomes
  populationI archive2;//Part II chromosomes
  SelectMTSPI Selection;
  //ObjectiveFunctionTSPI[] ObjectiveFunction;


  //to set basic GA components.
  public void setData(populationI Population, SelectMTSPI Selection, CrossoverI Crossover, MutationI Mutation,
                      ObjectiveFunctionI ObjectiveFunction[], FitnessI Fitness,int generations,int initialPopSize, int fixPopSize,
                      int length, double crossoverRate,double mutationRate, boolean[] objectiveMinimization,
                      int numberOfObjs, boolean encodeType,double elitism){
    this.Population = Population;
    this.Selection = Selection;
    this.Crossover = Crossover;
    this.Mutation = Mutation;
    this.ObjectiveFunction = ObjectiveFunction;
    this.Fitness = Fitness;
    this.generations = generations;
    this.initialPopSize = initialPopSize;
    this.fixPopSize = fixPopSize;
    this.length = length;
    this.crossoverRate = crossoverRate;
    this.mutationRate = mutationRate;
    this.objectiveMinimization = objectiveMinimization;
    this.numberOfObjs = numberOfObjs;
    this.encodeType = encodeType;
    this.elitism = elitism;
    archieve = null;
  }

  public void SetPartIIChromosomes(populationI Population2){
    this.Population2 = Population2;
  }  

  public void setLocalSearchOperator(localSearchI localSearch1, boolean applyLocalSearch, int maxNeighborhood){
    this.localSearch1 = localSearch1;
    this.applyLocalSearch = applyLocalSearch;
    this.maxNeighborhood = maxNeighborhood;
  }
  
  /**
   * Main procedure starts here. You should ensure the encoding of chromosome is done.
   */
  public void startGA(){
    Population = initialStage();
    //evaluate the objective values and calculate fitness values
    ProcessObjectiveAndFitness();
    archieve = findParetoFront(Population, 0);
    archive2 = findParetoFront(Population2, 0);

    for(int i = 0 ; i < generations ; i ++ ){
      currentGeneration = i;
      Population = selectionStage(Population);
      //System.out.println("currentGeneration "+i+" "+ generations);

      //Crossover
      //System.out.println("Crossover");
      Population = crossoverStage(Population);

      //Mutation
      //System.out.println("mutationStage");
      Population = mutationStage(Population);

      //clone
      if(applyClone == true){
        Population = cloneStage(Population);
      }

      //evaluate the objective values and calculate fitness values
      ProcessObjectiveAndFitness();

      populationI tempFront = (population)findParetoFront(Population, 0);
      archieve = updateParetoSet(archieve,tempFront);
      populationI tempFront2 = (population)findParetoFront(Population2, 0);
      archive2 = updateParetoSet(archive2, tempFront2);
      //additionalStage();
      localSearchStage();
    }
    //localSearchStage();
  }

  public populationI initialStage(){
    //For the part I chromosomes from the first population.
    Population.setGenotypeSizeAndLength(encodeType, initialPopSize, length, numberOfObjs);
    Population.createNewPop();

    return Population;
  }

  public populationI selectionStage(populationI Population){
    //selection, we may try to modify the population size
    Selection.setData(fixPopSize, Population);
    Selection.setSecondPopulation(Population2);


    //To assign elitism data.
    if(archieve.getPopulationSize() < 1){
      Selection.setElite(archieve, 0);
      Selection.setElite2(archive2, 0);
    }
    else if(fixPopSize*elitism > archieve.getPopulationSize()){
      Selection.setElite(archieve, archieve.getPopulationSize());
      Selection.setElite2(archive2, archive2.getPopulationSize());
    }
    else{
      Selection.setElite(archieve, (int)(fixPopSize*elitism));
      Selection.setElite2(archive2, (int)(fixPopSize*elitism));
    }

    Selection.startToSelect();
    Population = Selection.getSelectionResult();
    return Population;
  }

  public populationI crossoverStage(populationI Population){
    //For the part I chromosomes from the first population.    
    Crossover.setData(crossoverRate, Population);
    Crossover.startCrossover();
    Population = Crossover.getCrossoverResult();

    //For the part II chromosomes from the second population.
    Crossover2.setData(crossoverRate, Population2);    
    Crossover2.startCrossover();
    Population2 = Crossover2.getCrossoverResult();    

    return Population;
  }

  public populationI mutationStage(populationI Population){
    //For the part I chromosomes from the first population.
    Mutation.setData(mutationRate, Population);
    Mutation.startMutation();
    Population = Mutation.getMutationResult();

    //For the part II chromosomes from the second population.
    Mutation2.setData(mutationRate, Population2);
    Mutation2.startMutation();
    Population2 = Mutation2.getMutationResult();
    
    return Population;
  }

  public populationI ProcessObjectiveAndFitness(){
    //evaluate the objective values
    for(int i = 0 ; i < ObjectiveFunction.length ; i ++ ){
      //System.out.println("The obj "+i);
      ObjectiveFunction[i].setData(Population, i);
      ObjectiveFunction[i].calcObjective();
      Population = ObjectiveFunction[i].getPopulation();
    }

    //calculate fitness values
    Fitness.setData(Population, numberOfObjs);
    Fitness.calculateFitness();
    Population = Fitness.getPopulation();

    Fitness.setData(Population2, numberOfObjs);
    Fitness.calculateFitness();
    Population2 = Fitness.getPopulation();
    return Population;
  }

  public void localSearchStage(){
    currentUsedSolution += fixPopSize;//Solutions used in genetic search
    localSearch1.setData(Population, totalExaminedSolution, maxNeighborhood);
    localSearch1.setData(Population, archieve, currentUsedSolution);
    //localSearch1.setMTSPData(Population, distanceMatrix, numberOfObjs);
    localSearch1.setObjectives(ObjectiveFunction);
    localSearch1.startLocalSearch();
    currentUsedSolution = localSearch1.getCurrentUsedSolution();
  }
}
