/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package openga.operator.selection;
import openga.chromosomes.*;
/**
 *
 * @author nhu
 */
public interface SelectMTSPI extends SelectI{
  public void setData(populationI originalPop);
  public void setData(int sizeOfPop, populationI originalPop);
  public void setTournamentSize(int tournamentSize);
  public void setElite(populationI archieve, int numberOfelitle);
  public void setSecondPopulation(populationI archieve2);
  public void startToSelect();
  public populationI getSelectionResult();
  public void setElite2(populationI archieve2, int numberOfelitle);
}
