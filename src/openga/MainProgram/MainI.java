package openga.MainProgram;
import openga.chromosomes.*;
import openga.operator.selection.*;
import openga.operator.crossover.*;
import openga.operator.localSearch.*;
import openga.operator.mutation.*;
import openga.operator.clone.*;
import openga.ObjectiveFunctions.*;
import openga.Fitness.*;

public interface MainI {
  public void setData(populationI Population, SelectI Selection, CrossoverI Crossover, MutationI Mutation,
                      ObjectiveFunctionI ObjectiveFunction[], FitnessI Fitness,int generations,int initialPopSize, int fixPopSize,
                      int length,double crossoverRate,double mutationRate, boolean[] objectiveMinimization,
                      int numberOfObjs, boolean encodeType, double elitism);

  public void setMutationRate(double mutationRate);
  public void setSecondaryCrossoverOperator(CrossoverI Crossover2, boolean applySecCRX);
  public void setSecondaryMutationOperator(MutationI Mutation2, boolean applySecMutation);
  public void setCloneOperatpr(cloneI cloneI1, boolean applyClone);
  public void setLocalSearchOperator(openga.operator.localSearch.localSearchI LocalSearch1, boolean applyLocalSearch, int maxNeighborhood);
  public void setReplacementStrategy(int replacementStrategy);
  public void setTournamentSize(int size);
  public void setCurrentGeneration(int currentGeneration);
  public populationI selectionStage(populationI Population);
  public void startGA();
  public populationI initialStage();
  public populationI getPopulation();
  public populationI getArchieve();
  public double[] getObjectiveValue(int index);
  public double[][] getArchieveObjectiveValueArray();
  public void destroidArchive();
  public populationI ProcessObjectiveAndFitness();
  public populationI findParetoFront(populationI originalSet, int generation);
  public populationI updateParetoSet(populationI originalSet, populationI tempParetoFront);
  public void initFirstArchieve();
  public void setArchieve(populationI archieve);
  public void updateNondominatedSon();
  public void InitialRun();
  public void InitialRun(boolean isRun);
  public double getPopulationBestObjValue();
}