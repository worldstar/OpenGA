/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package openga.ObjectiveFunctions;

import openga.chromosomes.chromosome;
import openga.chromosomes.populationI;

/**
 *
 * @author user
 */
public class objectiveTC implements ObjectiveFunctionScheduleI, alphaBetaI {

  int sequence[];
  int processingTime[];
  double[] alpha = {0};
  double[] b = {0};
  double weights[];
  int n = 0;//the number of jobs
  populationI originalPop;
  int indexOfObjective;
  int popSize, length;
  int dueDay[];              //due day of these jobs.
  int machineTime[];         //starting time to process these jobs.
  //int processingTime[];      //processing time of the job
  int numberOfMachine;       // the number of parallel machine

  @Override
  public void calcObjective() {
    for (int i = 0; i < popSize; i++) {
      double originalObjValues[] = originalPop.getObjectiveValues(i);
      //write in the objective value to the variable.
      originalObjValues[indexOfObjective] = startCalc(originalPop.getSingleChromosome(i).genes, processingTime, alpha, b);
      originalPop.setObjectiveValue(i, originalObjValues);
    }
  }

  public double startCalc(int sequence[], int processingTime[], double[] alpha, double[] b) {
    n = sequence.length;
    this.alpha = alpha;
    this.b = b;
    double obj = 0;

    for (int i = 0; i < n; i++) {
      int r = i + 1;
      obj += (n - r + 1) * (1 + b[0] * (n - r) / 2) * Math.pow(r, alpha[0]) * processingTime[sequence[i] - 0];
    }

    return obj;
  }

//  private double calEarliness(chromosome chromosome1) {
//    double objVal = 0;
//    //double machineTime = 0;
//    int finishTime[] = calcFinishTime(chromosome1.genes);
//    for (int i = 0; i < length; i++) {
//      int jobIndex = chromosome1.getSolution()[i];
//      if (finishTime[jobIndex] < dueDay[jobIndex]) {
//        objVal += (dueDay[jobIndex] - finishTime[jobIndex]);
//      }
//    }
//    return objVal;
//  }
//
//  public int[] calcFinishTime(int _seq[]) {
//    int finishTime[] = new int[_seq.length];
//    int currentTime = 0;
//
//    for (int i = 0; i < _seq.length; i++) {
//      finishTime[_seq[i]] = currentTime + processingTime[_seq[i]];
//      currentTime = finishTime[_seq[i]];
//    }
//    return finishTime;
//  }
  @Override
  public void setScheduleData(int[] processingTime, int numberOfMachine) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void setScheduleData(int dueDay[], int processingTime[], int numberOfMachine) {
    this.dueDay = dueDay;
    this.processingTime = processingTime;
    this.numberOfMachine = numberOfMachine;
  }

  @Override
  public void setData(populationI population, int indexOfObjective) {
    this.originalPop = population;
    this.indexOfObjective = indexOfObjective;
    popSize = population.getPopulationSize();
    length = population.getLengthOfChromosome();
  }

  @Override
  public void setData(chromosome chromosome1, int indexOfObjective) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public populationI getPopulation() {
    return originalPop;
  }

  @Override
  public double[] getObjectiveValues(int index) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void setAlphaBeta(double[] alpha, double[] beta) {
    this.alpha = alpha;
    this.b = beta;
  }

  @Override
  public void setLearningRate(double learningRate) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

}
