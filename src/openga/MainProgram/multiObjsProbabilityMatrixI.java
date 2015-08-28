package openga.MainProgram;
import openga.chromosomes.populationI;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public interface multiObjsProbabilityMatrixI extends probabilityMatrixI {
  public void setWeight(double weights[]);
  public double[] getWeight();
  public void setPopulation(populationI pop);
}