package openga.MainProgram;

/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: The usage is for probability matrix.</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public interface probabilityMatrixII extends MainI{
  public void setProbabilityMatrixData(int startingGeneration, int interval);
  public void setSequenceStrategy(int strategy);
  public void setEvaporationMethod(boolean applyEvaporation, String evaporationMethod);
  public void ProbabilityMatrix();

  //For GuidedMutation
  public void setGuidedMutationInfo(double lamda, double beta);

  public void setLearningRate(double lamda, double beta);
}

