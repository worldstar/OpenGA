package openga.MainProgram;

/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: The usage is for probability matrix.</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public interface probabilityMatrixI extends MainI{
  public void setProbabilityMatrixData(int startingGeneration, int interval);
  public void setSequenceStrategy(int strategy);
  public void setEvaporationMethod(boolean applyEvaporation, String evaporationMethod);
  public void ProbabilityMatrix();

    //For GuidedMutation
    public void setGuidedMutationInfo(double lamda, double beta);
    public void setWeight(double w1, double w2);
    public void setLearningRate(double lamda, double beta);
    // public void setFlowShopData(int numberOfJob, int numberOfMachines, double[][] processingTime);
    public   void setNEH(int[] NEH);
    void setparaData(int[][][] processingSetupTime, int numberOfMachines);
    //void setAdaptive(boolean );
    public void setFlowShopData(int numberOfJob, int numberOfMachines, int[][] processingTime);  
}