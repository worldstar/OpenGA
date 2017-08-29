/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package openga.applications.flowshopProblem;

import openga.ObjectiveFunctions.ObjectiveFunctionI;
import openga.chromosomes.populationI;

/**
 *
 * @author Kuo Yu-Cheng
 */
public interface DistanceCalculationI extends ObjectiveFunctionI{
  public void setTSPData(double originalPoint[], double coordinates[][]);
  public void setTSPData(double originalPoint[], double coordinates[][], populationI PartIIChromosomes);
  public void setTSPData(double originalPoint[], double coordinates[][], int numberOfSalesmen);
  //public void setTSPData(populationI population, populationI PartIIChromosomes, int indexOfObjective);
  public void setObjectiveFunctionType(String typeName);
}
