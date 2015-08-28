package openga.operator.selection;

import openga.chromosomes.*;
/**
 *
 */

public class singleObjFitness implements SelectionI{
  public singleObjFitness() {
  }

  int sizeOfPopulation;
  populationI newPop;
  double objs[];
  double sum, max;
  double fitnessValue[];

  public void setData(populationI originalPop){
    sizeOfPopulation = originalPop.getPopulationSize();
    newPop = new population();
    newPop = originalPop;
    objs = new double[sizeOfPopulation];

    //to get the objective values from all chromosomes
    for(int i = 0 ; i < sizeOfPopulation ; i ++ ){
      objs[i] = newPop.getObjectiveValues(i)[0];
    }

    //to get the summary value from all chromosomes
    openga.util.algorithm.getSum _sum = new openga.util.algorithm.getSum();
    _sum.setData(objs);
    sum = _sum.getSumResult();

    //to get the max value
    openga.util.algorithm.getMax _getMax = new openga.util.algorithm.getMax();
    _getMax.setData(objs);
    max = _getMax.getMax();
  }

  public void calculateFitness(){
    for(int i = 0 ; i < sizeOfPopulation ; i ++ ){
      double _fitness = ((max - objs[i])/sum);
      newPop.setFitness(i, _fitness);
    }
  }

  public populationI getPopulation(){
    return newPop;
  }

    //we test the program from rouletteWheel.java
  public static void main(String[] args) {
    singleObjFitness singleObjFitness1 = new singleObjFitness();
  }

}