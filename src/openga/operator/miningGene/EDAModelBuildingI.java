/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package openga.operator.miningGene;
import openga.chromosomes.*;
/**
 *
 * @author user
 */
public interface EDAModelBuildingI {
  public void setData(populationI originalPop);
  public void startStatistics();
  public double[][] getContainer();
}
