/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package openga.operator.repair;
import openga.chromosomes.*;
/**
 *
 * @author user
 */
public interface weightedRepairI {
  public void setData(double weights[]);//For MultiObjective problems
  public void setData(populationI originalPop);
  public void setData(int numKnapsack, double profit[], double itemWeight[][], double capacityLimit[]);
  public void setData(int numKnapsack, double profit[][], double itemWeight[][], double capacityLimit[]);//For MultiObjective problems
  public void startRepair();
  public populationI getPopulation();
}
