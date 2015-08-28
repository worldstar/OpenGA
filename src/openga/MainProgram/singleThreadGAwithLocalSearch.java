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


/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class singleThreadGAwithLocalSearch extends singleThreadGA{
  public singleThreadGAwithLocalSearch() {
  }

  boolean applyLocalSearch = true;
  localSearchI localSearch1;
  int currentUsedSolution = 0;
  int maxNeighborhood = 5;  //A default value of the maximum neighbors to search.
  int totalExaminedSolution = 100; //the main stopping criteria of genetic local search




  public void setLocalSearchOperator(localSearchI localSearch1, boolean applyLocalSearch, int maxNeighborhood){
    this.localSearch1 = localSearch1;
    this.applyLocalSearch = applyLocalSearch;
    this.maxNeighborhood = maxNeighborhood;
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

    for (int i = 0; i < generations && currentUsedSolution < totalExaminedSolution; i++) {
      currentGeneration = i;
      Population = selectionStage(Population);

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
    localSearch1.startLocalSearch();
    currentUsedSolution = localSearch1.getCurrentUsedSolution();
  }
}