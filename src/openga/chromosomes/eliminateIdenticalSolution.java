package openga.chromosomes;

/**
 * <p>Title: The OpenGA project</p>
 * <p>Description: The project is to build general framework of Genetic algorithm and problem independent.</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class eliminateIdenticalSolution {
  public eliminateIdenticalSolution() {
  }

  populationI nonDominatedSet; //Stores the non-dominated solutions
  populationI newPop;          //Eliminated set
  int originalSize;
  int index[];
  boolean flag[];

  public void setPopulation(populationI nonDominatedSet){
    this.nonDominatedSet = nonDominatedSet;
    originalSize = nonDominatedSet.getPopulationSize();
  }

  public void startToCheck(){
    for(int i = 0 ; i < originalSize ; i ++ ){
      for(int j = i ; j < originalSize ; j ++ ){

      }
    }


  }

  public static void main(String[] args) {
    eliminateIdenticalSolution eliminateIdenticalSolution1 = new eliminateIdenticalSolution();
  }

}