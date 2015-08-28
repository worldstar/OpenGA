package openga.ObjectiveFunctions;
import openga.chromosomes.*;
import openga.operator.repair.*;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: The objective function for Knapsack problem.</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public interface ObjectiveFunctionKnapsackI extends ObjectiveFunctionI {
  public void setKnapsackData(double profit[], double weight[][], weightedRepairI weightedScalarRepair1);
  public void setKnapsackData(double profit[][], double weight[][], weightedRepairI weightedScalarRepair1);//Multi-Objective
  public void setRepairedPopulation(populationI repiaredPopulation);
   public weightedRepairI getWeightedScalarRepair();
}