package openga.ObjectiveFunctions;
import openga.chromosomes.*;
/**
 * <p>Title: The OpenGA project</p>
 * <p>Description: The project is to build general framework of Genetic algorithm and problem independent.</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public interface ObjectiveFunctionI {
  public void setData(populationI population, int indexOfObjective);
  public void setData(chromosome chromosome1, int indexOfObjective);
  public void calcObjective();
  public populationI getPopulation();
  public double[] getObjectiveValues(int index);
  //public double getMinObjectiveValue();
  //public double getMaxObjectiveValue();
}