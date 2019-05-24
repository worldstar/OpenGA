/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package openga.MainProgram;

import openga.Fitness.FitnessI;
import openga.ObjectiveFunctions.ObjectiveFunctionI;
import openga.chromosomes.populationI;
import openga.operator.mutation.MutationI;
import openga.operator.selection.SelectI;
import openga.operator.mutation.MutationI_Weka;
import openga.operator.crossover.CrossoverI_Weka;

/**
 *
 * @author U
 */
public interface singleThreadGA_WekaI extends MainI {
  public void setData(populationI Population, SelectI Selection, CrossoverI_Weka Crossover, MutationI_Weka Mutation,
                      ObjectiveFunctionI ObjectiveFunction[], FitnessI Fitness,int generations,int initialPopSize, int fixPopSize,
                      int length, double crossoverRate,double mutationRate, boolean[] objectiveMinimization,
                      int numberOfObjs, boolean encodeType,double elitism);
  public void setSecondaryCrossoverOperator(CrossoverI_Weka Crossover2, boolean applySecCRX);
  public void setSecondaryMutationOperator(MutationI_Weka Mutation2, boolean applySecMutation);
  public void setWekaRF(int newBagSizePercent, String size, boolean newBreakTiesRandomly, boolean calcOutOfBag, boolean computeAttributeImportance, 
          boolean debug,boolean doNotCheckCapabilities, int value, int num, int numSlots,
          int newNumFeatures, int numIterations, boolean b, boolean print, int s, boolean storeOutOfBag);
  
}
