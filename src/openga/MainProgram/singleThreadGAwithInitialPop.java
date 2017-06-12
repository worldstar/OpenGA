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
  double maxNeighborhood;
  public singleThreadGAwithInitialPop() {
  }

  /**
   * Main procedure starts here. You should ensure the encoding of chromosome is
   * done.
   */
  public void startGA() {
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
      Population = selectionStage(Population);

      //collect gene information
      Mutation.setData(archieve);//Population archieve
      Mutation.setData(Population);

      //Crossover
//      System.out.println("Crossover");
      Population = crossoverStage(Population);

      //Mutation
      //System.out.println("mutationStage");
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

      currentUsedSolution += fixPopSize;//Solutions used in genetic search//???
      
      //local search
//      System.out.println("LocalSearch");
      if (applyLocalSearch == true && i % 10 == 0) {
        localSearchStage(1);
      }

      i++;

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
    } while (i < generations && currentUsedSolution < this.fixPopSize * this.generations);
    //printResults();
  }

}
