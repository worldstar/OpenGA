package openga.Fitness;
import openga.chromosomes.*;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: The objective values are obtained from a scalarized objective values, which original problem
 * is multiple objective and we transform it into single objective by scarlization.</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class FitnessByScalarizedM_objectives extends FitnessByNormalize {
  public FitnessByScalarizedM_objectives() {
  }

  public void setData(populationI population1, int numberOfObjs){
    pop = population1;
    popSize = pop.getPopulationSize();
    double objValues[] = new double[popSize];
    for(int i = 0 ; i < popSize ; i ++ ){
      objValues[i] = pop.getScalarizedObjectiveValue(i);
    }
    setObjVals(objValues);
  }

}