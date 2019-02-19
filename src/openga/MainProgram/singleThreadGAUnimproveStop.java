package openga.MainProgram;

import openga.chromosomes.*;

public class singleThreadGAUnimproveStop extends singleThreadGA{

  @Override
  public void startGA() {
    Population = initialStage();

    ProcessObjectiveAndFitness();
    archieve = findParetoFront(Population, 0);

    int NumberOfUnimprove = 0;
    double CurrentBest = getArchieve().getSingleChromosome(0).getObjValue()[0];

    for (int i = 0;i<=generations; i++) {
      currentGeneration = i;
      Population = selectionStage(Population);
      //collect gene information, it's for mutation matrix
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
      //additionalStage();
      if (applyLocalSearch == true && i % 10 == 0) {
        localSearchStage(1);
      }
      if (generations >= 30000) {
        if (CurrentBest >= (double) getArchieve().getSingleChromosome(0).getObjValue()[0]) {
          NumberOfUnimprove++;
        } else {
          CurrentBest = getArchieve().getSingleChromosome(0).getObjValue()[0];
          NumberOfUnimprove = 0;
        }
      System.out.println("NumberOfUnimprove:"+NumberOfUnimprove+"\t Generations:"+i+"\t"+"CurrentBest:"+CurrentBest);
        if (NumberOfUnimprove == 5000) {
          break;
        }
      }
    }
  }
}
