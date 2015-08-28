package openga.MainProgram;

import openga.chromosomes.*;


/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class singleThreadGAwithProbabilityMatrixInitialPop extends singleThreadGAwithProbabilityMatrix {
  public singleThreadGAwithProbabilityMatrixInitialPop() {
  }
  /**
   * Main procedure starts here. You should ensure the encoding of chromosome is done.
   */
  public void startGA(){
    intialArttificalPopulation();
    //evaluate the objective values and calculate fitness values
    //clone
    if(applyClone == true){
      Population = cloneStage(Population);
    }

    ProcessObjectiveAndFitness();
    archieve = findParetoFront(Population, 0);
    //printResults();

    for(int i = 0 ; i < generations ; i ++ ){
      currentGeneration = i;
      String generationResults = "";
      if(i % 1 == 0){
        generationResults = i+"\t"+getBest()+"\t"+getPopulationMeanObjValue()+"\n";
        System.out.print(generationResults);
      }
      else if(i == generations - 1){
        generationResults = i+"\t"+getBest()+"\t"+getPopulationMeanObjValue()+"\n"+"\n";
        System.out.print(generationResults);
      }
      writeFile("flowshop_ProbMatrixDOE_NEH5TimesSolns"+length, generationResults);


      if(i < startingGeneration || i % interval != 0){
        SGA();
      }
      else{
        //System.out.println("generations "+i);
        ProbabilityMatrix();
        //hybridProcess();
        //System.exit(0);
      }
    }
  }



}