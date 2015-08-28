package openga.Fitness;
import openga.chromosomes.*;
/**
 * <p>Title: The OpenGA project</p>
 * <p>Description: The project is to build general framework of Genetic algorithm and problem independent.</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class singleObjectiveFitness implements FitnessI{
  public singleObjectiveFitness() {
  }

  populationI pop;                 //mutation on whole population
  //The obj values of these chromosomes, [chromosome, objValues]
  double objVals[];
  double fitness[];
  int popSize;
  int numberOfObjs = 1;
  populationI paretoSet;

  public void setData(populationI population1, int numberOfObjs){
    pop = population1;
    popSize = pop.getPopulationSize();
    double objValues[] = new double[popSize];
    for(int i = 0 ; i < popSize ; i ++ ){
      objValues[i] = pop.getObjectiveValues(i)[0];
    }
    setObjVals(objValues);
  }

  public void setParetoData(populationI paretoSet){
    this.paretoSet = paretoSet;
  }

  //set the data and initialize flag.
  public void setObjVals(double _objVals[]){
    this.objVals = _objVals;
    popSize = _objVals.length;
    fitness = new double[popSize];
  }

  //to comapre the solns one-by-one of their objective value and to get
  //non-dominated solns. Goldberg's fitness assignment.
  public void calculateFitness(){
    openga.util.algorithm.getSum getSum1 = new openga.util.algorithm.getSum();
    getSum1.setData(objVals);
    double totalObjectiveValues = getSum1.getSumResult();

    //assign each fitness by obj/totalObjectiveValues
    for(int i = 0 ; i < popSize ; i ++ ){
      fitness[i] = objVals[i]/totalObjectiveValues;
    }

    //write the fitness data to the fitness of each chromosome.
    for(int i = 0 ; i < popSize ; i ++ ){
      pop.setFitness(i, fitness[i]);
    }
  }//end startToFind()


  public populationI getPopulation(){
    return pop;
  }
}