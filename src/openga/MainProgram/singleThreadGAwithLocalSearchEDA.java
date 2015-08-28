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
import openga.operator.miningGene.PBIL;

/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class singleThreadGAwithLocalSearchEDA extends singleThreadGAwithLocalSearch implements EDAMainI{
  public singleThreadGAwithLocalSearchEDA() {
  }
  //EAPM parameters and objects
  PBIL PBIL1;
  double container[][];
  double lamda = 0.9; //learning rate
  int startingGenDividen = 4;

  public void setData(populationI Population, SelectI Selection, EDAICrossover Crossover, EDAIMutation Mutation,
                      ObjectiveFunctionI ObjectiveFunction[], FitnessI Fitness,int generations,int initialPopSize, int fixPopSize,
                      int length, double crossoverRate,double mutationRate, boolean[] objectiveMinimization,
                      int numberOfObjs, boolean encodeType,double elitism){
    System.out.println("This class singleThreadGAwithLocalSearchEDA doesn't implement this method setData(). \nIt is going to exit.");
    System.exit(0);
  }

  public void setEDAinfo(double lamda, int numberOfCrossoverTournament, int numberOfMutationTournament, int startingGenDividen){
    this.lamda = lamda;
    this.startingGenDividen = startingGenDividen;
  }


  /**
   * Main procedure starts here. You should ensure the encoding of chromosome is done.
   */
  public void startGA() {
    totalExaminedSolution = generations*fixPopSize;
    Population = initialStage();
    //evaluate the objective values and calculate fitness values
    ProcessObjectiveAndFitness();
    archieve = findParetoFront(Population, 0);
    currentUsedSolution = Population.getPopulationSize();

    PBIL1 = new PBIL(Population, lamda);
    container = PBIL1.getContainer();

    for (int i = 0; i < generations && currentUsedSolution < totalExaminedSolution; i++) {
      currentGeneration = i;
      Population = selectionStage(Population);

      PBIL1.setData(Population);
      PBIL1.startStatistics();
      container = PBIL1.getContainer();

      //Crossover
      Population = crossoverStage(Population);

      //Mutation
      Population = mutationStage(Population);

      //clone
      if (applyClone == true) {
        Population = cloneStage(Population);
      }

      //evaluate the objective values and calculate fitness values
      ProcessObjectiveAndFitness();
      populationI tempFront = (population) findParetoFront(Population, 0);
      archieve = updateParetoSet(archieve, tempFront);

      localSearchStage();
    }
  }

  public void localSearchStage(){
    currentUsedSolution += fixPopSize;//Solutions used in genetic search
    localSearch1.setData(Population, totalExaminedSolution, maxNeighborhood);
    localSearch1.setData(Population, archieve, currentUsedSolution);
    localSearch1.setObjectives(ObjectiveFunction);
    localSearch1.setEDAinfo(container);//For EDA
    localSearch1.startLocalSearch();
    currentUsedSolution = localSearch1.getCurrentUsedSolution();
  }
}