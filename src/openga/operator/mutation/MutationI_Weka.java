package openga.operator.mutation;
import openga.chromosomes.*;
import weka.classifiers.Classifier;
/**
 * <p>Title: The OpenGA project</p>
 * <p>Description: The project is to build general framework of Genetic algorithm and problem independent.</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public interface MutationI_Weka extends MutationI{
  public void setWekainfo(Classifier Regression);
}
