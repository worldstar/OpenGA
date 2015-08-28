package openga.Fitness;

import openga.chromosomes.*;

public interface FitnessI {
  public void setData(populationI originalPop, int numberOfObjs);
  public void setParetoData(populationI paretoSet);
  public void calculateFitness();
  public populationI getPopulation();
}