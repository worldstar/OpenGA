package openga.chromosomes;

/**
 * <p>Title: The OpenGA project</p>
 * <p>Description: The project is to build general framework of Genetic algorithm and problem independent.</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class findSecondFront extends population {

  int indexOfParetoInds[], dominatedSolnIndex[], numberOfDominatedSoln;
  boolean flag[];
  //To store the result of pareto solution which contains the index of chromosome in a population.
  int paretoSet[];
  double paretoIndex[][];
  int nondominate = 0;
  populationI newPop, originalPop, dominatedSolns;

  public void setOriginalPop(populationI originalPop, int indexOfParetoInds[]){
    this.originalPop = originalPop;
    this.indexOfParetoInds = indexOfParetoInds;
    numberOfDominatedSoln = originalPop.getPopulationSize() - indexOfParetoInds.length;
    dominatedSolnIndex = new int[numberOfDominatedSoln];
  }

  public void formSecondFront(){
    int counter = 0;

    //form a dominated set
    for(int i = 0 ; i < originalPop.getPopulationSize() ; i ++ ){
      boolean isParetoSoln = false;
      for(int j = 0 ; j < indexOfParetoInds.length ; j ++ ){
        if(i == indexOfParetoInds[j]){
          isParetoSoln = true;
          break;
        }
      }
      if(isParetoSoln == false){
        dominatedSolnIndex[counter++] = i;
      }
    }
    //openga.util.printClass printClass1 = new openga.util.printClass();
    //printClass1.printMatrix("indexOfParetoInds[j]", indexOfParetoInds);
    //printClass1.printMatrix("dominatedSolnIndex", dominatedSolnIndex);

    //to form them into a new population
    storeSelectedChromosomes storeSelectedChromosomes1 = new storeSelectedChromosomes();
    storeSelectedChromosomes1.setOriginalPop(originalPop, dominatedSolnIndex);
    storeSelectedChromosomes1.formNewPopulation();
    dominatedSolns = storeSelectedChromosomes1.getPopulation();
    //System.out.println("dominatedSolns "+dominatedSolns.getObjectiveValues(0));
    //printClass1.printMatrix("dominatedSolns",dominatedSolns.getObjectiveValues(0));

    //initial the objects
    findParetoByObjectives findParetoByObjectives1 = new findParetoByObjectives();
    findParetoByObjectives1.setOriginalPop(dominatedSolns);
    findParetoByObjectives1.startToFind();
    newPop = findParetoByObjectives1.getPopulation();
    //System.out.println("newPop sec "+newPop.getPopulationSize());
  }

  public populationI getPopulation(){
    return newPop;
  }

  public int getPopulationSize(){
    return newPop.getPopulationSize();
  }

}