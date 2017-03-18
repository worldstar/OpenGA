package openga.operator.mutation;
import openga.ObjectiveFunctions.*;
import openga.chromosomes.populationI;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: Cheng Shiu University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public interface LocalSearchI extends MutationI {
  public void setObjectives(ObjectiveFunctionI ObjectiveFunction[]);
}