package openga.MainProgram;

import java.util.logging.Level;
import java.util.logging.Logger;
import openga.chromosomes.*;
import weka.core.Instances;
import openga.operator.miningGene.PopulationToInstances;

/**
 * <p>
 * Title: The OpenGA project which is to build general framework of Genetic
 * algorithm.</p>
 * <p>
 * Description: To set a population that is constructed by a heuristic.</p>
 * <p>
 * Copyright: Copyright (c) 2006</p>
 * <p>
 * Company: Yuan-Ze University</p>
 *
 * @author Chen, Shih-Hsin
 * @version 1.0
 */
public class singleThreadGAWeka extends singleThreadGA {
  
  public singleThreadGAWeka() {
  }

  /**
   * Main procedure starts here. You should ensure the encoding of chromosome is
   * done.
   */
  public void startGA() {
    Population = initialStage();
    double bestObjValue = 0;
    int nonimproveTime = 0;
    //evaluate the objective values and calculate fitness values
    //clone
    if (applyClone == true) {
      Population = cloneStage(Population);
    }

    ProcessObjectiveAndFitness();
    archieve = findParetoFront(Population, 0);

    int i = 0;
    PopulationToInstances WekaInstances = new PopulationToInstances();
    try {
      //    ************************This Create New ML code***************************
      Instances init_Dataset = WekaInstances.PopulationToInstances(Population); // Instances init_Dataset
    } catch (Exception ex) {
      Logger.getLogger(singleThreadGAWeka.class.getName()).log(Level.SEVERE, null, ex);
    }
    do {
      //System.out.println("generations "+i);
      currentGeneration = i;
//      selection
//      System.out.println("selection");
      Population = selectionStage(Population);

//      Crossover
//      System.out.println("Crossover");
      Population = crossoverStage(Population);

//      Mutation
//      System.out.println("mutationStage");
      Population = mutationStage(Population);

//      clone
      if (applyClone == true) {
        Population = cloneStage(Population);
      }

      //evaluate the objective values and calculate fitness values
      ProcessObjectiveAndFitness();

      populationI tempFront = (population) findParetoFront(Population, 0);
      archieve = updateParetoSet(archieve, tempFront);
      //additionalStage();
      currentUsedSolution += fixPopSize;//Solutions used in genetic search

      //local search
//      System.out.println("LocalSearch");
      if (applyLocalSearch == true && i % 10 == 0) {
        localSearchStage(1);
      }
      try {
        //   ************************This Create New ML code***************************
        Instances init_Dataset = WekaInstances.PopulationToInstances(Population); // Instances init_Dataset
      } catch (Exception ex) {
        Logger.getLogger(singleThreadGAWeka.class.getName()).log(Level.SEVERE, null, ex);
      }
      i++;
    } while (i < generations && currentUsedSolution < this.fixPopSize * this.generations);

  }

  @Override
  public void localSearchStage(int iteration) {
    localSearch1.setData(Population, totalExaminedSolution, maxNeighborhood);
    localSearch1.setData(Population, archieve, currentUsedSolution, iteration);
    localSearch1.setObjectives(ObjectiveFunction);
    localSearch1.startLocalSearch();
//        System.out.println("getCurrentUsedSolution"+localSearch1.getCurrentUsedSolution());
    currentUsedSolution = localSearch1.getCurrentUsedSolution();
//        System.out.println("currentUsedSolution"+currentUsedSolution);
  }
}
