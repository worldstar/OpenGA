package openga.MainProgram;
import openga.chromosomes.*;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class singleThreadGAwithProbabilityMatrix2 extends singleThreadGAwithProbabilityMatrix {
  public singleThreadGAwithProbabilityMatrix2() {
  }

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
/*
      if(i % 10 == 0){
        System.out.print(i+"\t"+getBest()+"\n");
      }
      else if(i == generations - 1){
        System.out.print(i+"\t"+getBest()+"\n");
      }
*/

      if(i < startingGeneration || i % interval != 0){
        SGA();
      }
      else{
        //System.out.println("generations "+i);
        ProbabilityMatrix();
      }
    }
  }

}