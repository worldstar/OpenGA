package openga.MainProgram;
import openga.chromosomes.*;
import openga.operator.selection.*;
import openga.operator.crossover.*;
import openga.operator.mutation.*;
import openga.operator.clone.*;
import openga.ObjectiveFunctions.*;
import openga.Fitness.*;
import openga.operator.miningGene.PBIL;

/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class singleThreadGAwithinipop_EDA extends singleThreadGAwithEDA implements EDAMainI{
  public singleThreadGAwithinipop_EDA() {
  }
  PBIL PBIL1;
  double container[][];
  double lamda = 0.9; //learning rate
  int numberOfCrossoverTournament = 2;
  int numberOfMutationTournament = 2;
  int tempNumberOfCrossoverTournament = 2;
  int tempNumberOfMutationTournament = 2;
  EDAICrossover Crossover;
  EDAIMutation Mutation;
  populationI offsrping = new population();
  int startingGenDividen = 4;

  //to set basic GA components.
  public void setData(populationI Population, SelectI Selection, EDAICrossover Crossover, EDAIMutation Mutation,
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

  public void setEDAinfo(double lamda, int numberOfCrossoverTournament, int numberOfMutationTournament, int startingGenDividen){
    this.lamda = lamda;
    this.numberOfCrossoverTournament = numberOfCrossoverTournament;
    this.numberOfMutationTournament = numberOfMutationTournament;
    this.startingGenDividen = startingGenDividen;
  }

  public void startGA(){
    //Population = initialStage();  *ct*
    if(applyClone == true){
      Population = cloneStage(Population);
    }

    //evaluate the objective values and calculate fitness values
    ProcessObjectiveAndFitness();
    //intialOffspringPopulation();
    archieve = findParetoFront(Population, 0);
    PBIL1 = new PBIL(Population, lamda);
    container = PBIL1.getContainer();

    for(int i = 0 ; i < generations ; i ++ ){
      //System.out.println("generations "+i);
      currentGeneration = i;
      Population = selectionStage(Population);

      //collect gene information, it's for mutation matrix
      if(i < generations/startingGenDividen){
        tempNumberOfCrossoverTournament = 2;
        tempNumberOfMutationTournament = 2;
      }
      else{
        tempNumberOfCrossoverTournament = numberOfCrossoverTournament;
        tempNumberOfMutationTournament = numberOfMutationTournament;
        PBIL1.setData(Population);
        PBIL1.startStatistics();
        container = PBIL1.getContainer();
      }
      //Crossover
      Population = crossoverStage(Population, container);

      //Mutation
      //System.out.println("mutationStage");
      Population = mutationStage(Population, container);
      //System.out.println("timeClock1 "+timeClock1.getExecutionTime());
      //clone
      if(applyClone == true){
        Population = cloneStage(Population);
      }

      //evaluate the objective values and calculate fitness values
      //ProcessObjectiveAndFitness(Population);
      ProcessObjectiveAndFitness();

      populationI tempFront = (population)findParetoFront(Population, 0);
      archieve = updateParetoSet(archieve, tempFront);
      //Population = replacementStage(Population, offsrping);
      //System.out.println(getArchieve().getPopulationSize());
    }
    //printResults();
  }

  public void intialOffspringPopulation(){
    offsrping.setGenotypeSizeAndLength(encodeType, fixPopSize, length, numberOfObjs);
    offsrping.initNewPop();
  }

  public populationI crossoverStage(populationI Population, double container[][]){
    Crossover.setData(crossoverRate, Population);
    Crossover.setEDAinfo(container, tempNumberOfCrossoverTournament);
    Crossover.startCrossover();
    Population = Crossover.getCrossoverResult();

    return Population;
  }

  public populationI mutationStage(populationI Population, double container[][]){
    Mutation.setData(mutationRate, Population);
    Mutation.setEDAinfo(container, numberOfMutationTournament);
    Mutation.startMutation();
    Population = Mutation.getMutationResult();

    return Population;
  }

  public populationI ProcessObjectiveAndFitness(populationI originalSet){
    //evaluate the objective values
    for(int i = 0 ; i < ObjectiveFunction.length ; i ++ ){
      //System.out.println("The obj "+i);
      ObjectiveFunction[i].setData(originalSet, i);
      ObjectiveFunction[i].calcObjective();
      originalSet = ObjectiveFunction[i].getPopulation();
    }

    //calculate fitness values
    Fitness.setData(originalSet, numberOfObjs);
    Fitness.calculateFitness();
    originalSet = Fitness.getPopulation();
    return originalSet;
  }

  public populationI replacementStage(populationI parent, populationI offspring){
    openga.operator.selection.MuPlusLamdaSelection2 replace1 = new openga.operator.selection.MuPlusLamdaSelection2();
    replace1.setData(parent.getPopulationSize(), parent);
    replace1.setSecondPopulation(offspring);
    replace1.startToSelect();
    return replace1.getSelectionResult();
  }

}