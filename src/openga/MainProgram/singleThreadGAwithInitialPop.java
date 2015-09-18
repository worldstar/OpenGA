package openga.MainProgram;
import openga.chromosomes.*;
import openga.operator.clone.*; //ct
import openga.operator.selection.*;
import openga.operator.crossover.*;
import openga.operator.mutation.*;
import openga.ObjectiveFunctions.*;
import openga.Fitness.*;

/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: To set a population that is constructed by a heuristic.</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class singleThreadGAwithInitialPop extends singleThreadGA{
  public singleThreadGAwithInitialPop() {
  }

  /**
   * Main procedure starts here. You should ensure the encoding of chromosome is done.
   */
  public void startGA(){
    //evaluate the objective values and calculate fitness values
    //clone
    if(applyClone == true){
      Population = cloneStage(Population);
    }

    ProcessObjectiveAndFitness();
    archieve = findParetoFront(Population, 0);
    //printResults();

    for(int i = 0 ; i < generations ; i ++ ){
      System.out.println("generations "+i);
/*
      if(i % 10 == 0){
        System.out.print(i+"\t"+getBest()+"\n");
      }
      else if(i == generations - 1){
        System.out.print(i+"\t"+getBest()+"\n");
      }
*/

      currentGeneration = i;
      Population = selectionStage(Population);

      //collect gene information
      Mutation.setData(archieve);//Population archieve
      Mutation.setData(Population);

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

      populationI tempFront = (population)findParetoFront(Population,0);
      archieve = updateParetoSet(archieve,tempFront);
      //additionalStage();
    }
  }



}