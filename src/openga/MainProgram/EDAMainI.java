package openga.MainProgram;
import openga.chromosomes.*;
import openga.operator.selection.*;
import openga.operator.crossover.*;
import openga.operator.mutation.*;
import openga.operator.clone.*;
import openga.ObjectiveFunctions.*;
import openga.Fitness.*;
import openga.operator.miningGene.*;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public interface EDAMainI extends MainI {
  public void setData(populationI Population, SelectI Selection, EDAICrossover Crossover, EDAIMutation Mutation,
                      ObjectiveFunctionI ObjectiveFunction[], FitnessI Fitness,int generations,int initialPopSize, int fixPopSize,
                      int length, double crossoverRate,double mutationRate, boolean[] objectiveMinimization,
                      int numberOfObjs, boolean encodeType,double elitism);
  //public void setEDAinfo(double lamda, int numberOfCrossoverTournament, int numberOfMutationTournament, int startingGenDividen, EDAModelBuildingI model);
  public void setEDAinfo(double lamda, int numberOfCrossoverTournament, int numberOfMutationTournament, int startingGenDividen);
  public void setEDAinfo(double lamda,double beta, int numberOfCrossoverTournament, int numberOfMutationTournament, int startingGenDividen);
  public void setFlowShopData(int numberOfJob, int numberOfMachines, int processingTime[][]) ;
}
