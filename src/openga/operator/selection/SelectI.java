package openga.operator.selection;
import openga.chromosomes.*;
/**
 * <p>Title: The OpenGA project</p>
 * <p>Description: The project is to build general framework of Genetic algorithm and problem independent.</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public interface SelectI {
  public void setData(populationI originalPop);
  public void setData(int sizeOfPop, populationI originalPop);
  public void setTournamentSize(int tournamentSize);
  public void setElite(populationI archieve, int numberOfelitle);
  public void setSecondPopulation(populationI archieve2);
  public void startToSelect();
  public populationI getSelectionResult();
}