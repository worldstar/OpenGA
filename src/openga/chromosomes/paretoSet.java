package openga.chromosomes;

/**
 * <p>Title: The OpenGA project</p>
 * <p>Description: The project is to build general framework of Genetic algorithm and problem independent.</p>
 * <p>The paretoSet is actually a population which stores non-dominated solutions.</P>
 * <P>The non-dominated solutions must have "0" fitness. Consequently, it's the most important characteristics<BR>
 * when determine the solution to enter the pareto set.</P>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class paretoSet{
  public paretoSet() {
  }

  int   size;         //total size of the population
  int   length;       //number of genes
  boolean   genotype; //real or binary code
  int numberOfObjs;
  int newParetoSolutions = 0;
  populationI nonDominatedSet; //Stores the non-dominated solutions
  populationI newPop;          //the population which is generated each iteration.
  populationI reNewedPop;      //find the pareto set in newPop.

  public void setGenotypeSizeAndLength(boolean thetype, int size, int length, int numberOfObjs){
    this.genotype = thetype;
    this.size = size;
    this.length = length;
    this.numberOfObjs = numberOfObjs;
  }

  public void initParetoSet(){
    nonDominatedSet = new population();
    nonDominatedSet.setGenotypeSizeAndLength(genotype, size, length, numberOfObjs);
    nonDominatedSet.initNewPop();
  }

  /**
   * @param newPop After a generation, the program passes the whole population to here. Besides,
   * the new population will be reduced by finding out the pareto set in the newPop.
   */
  public void setNewPopulation(populationI newPop){
    //find out whether the fitness is zero
    findParetoByFitness findParetoByFitness1 = new findParetoByFitness();
    findParetoByFitness1.setPopulation(newPop);
    findParetoByFitness1.startToFindParetoSolution();
    reNewedPop = findParetoByFitness1.getPopulation();
  }

  public populationI combinedTwoPopulations(populationI _population1, populationI _population2){
    //combine the two pareto set of nonDominatedSet and reNewedPop
    combinedTwoPopulations combinedTwoPopulations1 = new combinedTwoPopulations();
    combinedTwoPopulations1.setTwoPopulations(_population1, _population2);
    combinedTwoPopulations1.startToCombine();
    return combinedTwoPopulations1.getPopulation();
  }

  /**
   * start to update the new pareto set and eliminate the identical solution.
   */
  public void updateParetoSet(){
    nonDominatedSet = combinedTwoPopulations(nonDominatedSet, reNewedPop);
    //find out whether the fitness is zero
    findParetoByFitness findParetoByFitness1 = new findParetoByFitness();
    findParetoByFitness1.setPopulation(nonDominatedSet);
    findParetoByFitness1.startToFindParetoSolution();
    nonDominatedSet = findParetoByFitness1.getPopulation();
  }



  public static void main(String[] args) {
    paretoSet paretoSet1 = new paretoSet();
  }

}