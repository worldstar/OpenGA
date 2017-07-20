package openga.MainProgram;

import openga.chromosomes.*;

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
public class singleThreadGAwithInitialPop extends singleThreadGA {

  public singleThreadGAwithInitialPop() {
  }

  /**
   * Main procedure starts here. You should ensure the encoding of chromosome is
   * done.
   */
  public void startGA() {
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
    do {
      //System.out.println("generations "+i);
      currentGeneration = i;

      //selection
//      System.out.println("selection");
      Population = selectionStage(Population);

      //collect gene information
      Mutation.setData(archieve);//Population archieve
      Mutation.setData(Population);

      //Crossover
//      System.out.println("Crossover");
      Population = crossoverStage(Population);

      //Mutation
//      System.out.println("mutationStage");
      Population = mutationStage(Population);

      //clone
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

      i++;
//System.out.println("i : "+i);
//System.out.println("currentUsedSolution : "+currentUsedSolution);

      /*
            if (i == 500) {
                String generationResults = "";
                generationResults = i + "\t" + beta + "\t" + getBest() + "\n";
                //System.out.print(generationResults);
                writeFile("eda2_" + i, generationResults);
            }
            if (i == 750) {
                String generationResults = "";
                generationResults = i + "\t" + beta + "\t" + getBest() + "\n";
                //System.out.print(generationResults);
                writeFile("eda2_" + i, generationResults);
            }
            if (i == 1000) {
                String generationResults = "";
                generationResults = i + "\t" + beta + "\t" + getBest() + "\n";
                //System.out.print(generationResults);
                writeFile("eda2_" + i, generationResults);
            }
       */
      /*
      double _ObjValue = archieve.getSingleChromosome(0).getObjValue()[0];
      if(_ObjValue <= bestObjValue){
        nonimproveTime++;
      } else{
        bestObjValue = _ObjValue;
      }
      */
    } while (i < generations && currentUsedSolution < this.fixPopSize * this.generations);
//        } while (nonimproveTime <= 500);
//    System.out.println("i : "+i);
    
    //printResults();
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
