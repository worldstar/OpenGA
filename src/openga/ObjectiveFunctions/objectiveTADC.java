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
public class objectiveTADC implements ObjectiveFunctionScheduleI, alphaBetaI {

  int sequence[];
  int processingTime[];
  double[] alpha = {0};
  double[] b = {0};
  double weights[];
  populationI originalPop;
  int indexOfObjective;
  int popSize, length;
  int dueDay[];              //due day of these jobs.
  int machineTime[];         //starting time to process these jobs.
  int numberOfMachine;       // the number of parallel machine
  int n = 0;//the number of jobs

  public objectiveTADC() {

  }

  public double startCalc(int sequence[], int processingTime[], double[] alpha, double[] b) {
    n = sequence.length;
    this.alpha = alpha;
    this.b = b;
    double obj = 0;
    weights = new double[n];

    for (int i = 1; i <= n; i++) {
      weights[i - 1] = (i - 1) * (n - i + 1);

      for (int j = i + 1; j <= n; j++) {
        weights[i - 1] += b[0] * (j - 1) * (n - j + 1);
      }

      weights[i - 1] = weights[i - 1] * Math.pow(i, alpha[0]);
      obj += weights[i - 1] * processingTime[sequence[i - 1]];
    }
    return obj;
  }

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
  public void calcObjective() {
    for (int i = 0; i < popSize; i++) {
      double originalObjValues[] = originalPop.getObjectiveValues(i);
      //write in the objective value to the variable.
      //originalObjValues[indexOfObjective] = calTardiness(originalPop.getSingleChromosome(i));
      originalObjValues[indexOfObjective] = startCalc(originalPop.getSingleChromosome(i).genes, processingTime, alpha, b);
      originalPop.setObjectiveValue(i, originalObjValues);
    }
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
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void setLearningRate(double learningRate) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

}
