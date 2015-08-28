package openga.operator.clone;
import openga.chromosomes.*;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public interface cloneI {
  public void setData(populationI originalPop);
  public void setArchive(populationI archive);
  public void startToClone();
  public boolean checkIdenticalSoln(chromosome chromosome1, chromosome chromosome2);
  public chromosome generateNewSolution();
  public chromosome generateNewSolution(chromosome chromosome1);
  public int getNumberOfOverlappedSoln();
  public populationI getPopulation();
}