package openga.ObjectiveFunctions;

/**
 * <p>Title: The OpenGA project</p>
 * <p>Description: The project is to build general framework of Genetic algorithm and problem independent.</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public interface weightedSumObjectives extends ObjectiveFunctionI {
  //the weights for every objective function.
  public void setWeightOfObjectives(double weights[]);
  public void setObjectiveClass(ObjectiveFunctionI ObjectiveFunction[]);
}