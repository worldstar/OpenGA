package openga.ObjectiveFunctions;
import openga.chromosomes.*;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public interface ObjectiveFunctionTSPI extends ObjectiveFunctionI{
  public void setTSPData(double originalPoint[], double coordinates[][]);
  public void setTSPData(double originalPoint[], double coordinates[][], populationI PartIIChromosomes);
  public void setTSPData(double originalPoint[], double coordinates[][], int numberOfSalesmen);
  //public void setTSPData(populationI population, populationI PartIIChromosomes, int indexOfObjective);
}