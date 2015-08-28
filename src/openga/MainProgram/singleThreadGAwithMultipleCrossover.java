package openga.MainProgram;
import openga.chromosomes.*;
import openga.operator.selection.*;
import openga.operator.crossover.*;
import openga.operator.mutation.*;
import openga.ObjectiveFunctions.*;
import openga.Fitness.*;
import openga.util.printClass;

/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: It may use multiple crossover to diverse the solutions in a population..</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class singleThreadGAwithMultipleCrossover extends singleThreadGA {
  public singleThreadGAwithMultipleCrossover() {
  }

  public populationI crossoverStage(populationI Population){
   Crossover.setData(crossoverRate, Population);
   Crossover.startCrossover();
   Population = Crossover.getCrossoverResult();

    if(applySecCRX == true && Math.random() < 0.1){//&& (currentGeneration < 300 || currentGeneration > 600 && currentGeneration < 800)
      if(Crossover2 != null){
        Crossover2.setData(crossoverRate*0.9, Population);
        Crossover2.startCrossover();
        Population = Crossover2.getCrossoverResult();
      }
      else{//applied original crossover operator
        Crossover.setData(crossoverRate*0.9, Population);
        Crossover.startCrossover();
        Population = Crossover.getCrossoverResult();
      }
    }
    return Population;
  }

  public populationI mutationStage(populationI Population){
    Mutation.setData(mutationRate, Population);
    Mutation.startMutation();
    Population = Mutation.getMutationResult();

    if(applySecMutation == true && Math.random() <= 0.1){
      if(Mutation2 != null){//use the secondary mutation operator.
        Mutation2.setData(mutationRate, Population);
        Mutation2.startMutation();
        Population = Mutation2.getMutationResult();
      }
      else{
        Mutation.setData(mutationRate, Population);
        Mutation.startMutation();
        Population = Mutation.getMutationResult();
      }
    }
    return Population;
  }

}