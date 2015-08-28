package openga.operator.localSearch;
import openga.chromosomes.*;
import openga.ObjectiveFunctions.*;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: The local search interface defines the behavior of the local search operator. A special
 * usage of the local search operator is to apply the probabilistic model to screen out bad moves in advance.</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public interface localSearchI {
  public void setData(populationI population1, int totalExaminedSolution, int maxNeighborhood);
  public void setData(populationI population1, populationI archive, int currentUsedSolution);
  public void setObjectives(ObjectiveFunctionI ObjectiveFunction[]);
  public void startLocalSearch();
  public populationI getLocalSearchResult();
  public int getCurrentUsedSolution();
  public boolean continueLocalSearch();//currentUsedSolution should be less than totalExaminedSolution.

  //EAPM
  public void setEDAinfo(double container[][]);
}