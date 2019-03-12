package openga.operator.crossover;

import openga.chromosomes.populationI;
import weka.classifiers.Classifier;

/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public interface CrossoverI_Weka extends CrossoverI{
  public void setWekainfo(Classifier Regression); 
}