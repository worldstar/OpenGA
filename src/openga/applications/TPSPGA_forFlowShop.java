package openga.applications;
import openga.chromosomes.*;
import openga.operator.selection.*;
import openga.operator.crossover.*;
import openga.operator.mutation.*;
import openga.ObjectiveFunctions.*;
import openga.MainProgram.*;
import openga.ObjectiveFunctions.*;
import openga.Fitness.*;
import openga.util.printClass;

/**
 * <p>Title: The OpenGA project</p>
 * <p>Description: The project is to build general framework of Genetic algorithm and problem independent.</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class TPSPGA_forFlowShop {
  public TPSPGA_forFlowShop() {
  }

  /***
   * Basic operators of GAs.
   */
  int numberOfObjs = 2;//it's a bi-objective program.
  populationI Population[];
  SelectI Selection[];
  CrossoverI Crossover[];
  MutationI Mutation[];
  ObjectiveFunctionFlowShopScheduleI ObjectiveFunction[][];
  FitnessI Fitness[];
  MainWeightScalarizationI GaMain[];


  /**
   * Parameters of the GA
   */
  int generations, length, initPopSize, fixPopSize;
  double crossoverRate, mutationRate;
  boolean[] objectiveMinimization; //true is minimum problem.
  boolean encodeType;  //binary of realize code

  /**
   * The TPSPGA parameters
   */
  int numberOfSubPopulations = 10;
  double weights[];

  /***
   * Scheduling data
   */
  int dueDay[], processingTime[][];
  int numberOfJob = 20;
  int numberOfMachines = 20;

  //Results
  double bestObjectiveValues[][];
  populationI solutions[];

  public final static int
    DEFAULT_generations = 1000,
    DEFAULT_PopSize     = 300,
    DEFAULT_initPopSize = 400,
    firstIteration      = 1000;

  public final static double
     DEFAULT_crossoverRate = 0.9,
     DEFAULT_mutationRate  = 0.6,
     elitism               =  0.1;     //the percentage of elite chromosomes

  printClass printClass1 = new printClass();

  public void initiateVars(){
    //initiate scheduling data, we get the data from a program.
    openga.applications.data.flowShop flowShopData = new openga.applications.data.flowShop();
    processingTime = flowShopData.getTestData2_processingTime();
    dueDay = flowShopData.getTestData2_DueDay();

    objectiveMinimization = new boolean[numberOfObjs];
    objectiveMinimization[0] = true;
    objectiveMinimization[1] = true;
    encodeType = true;

    //GA objects
    GaMain     = new MainWeightScalarizationI[numberOfSubPopulations];
    Population = new populationI[numberOfSubPopulations];
    Selection  = new SelectI[numberOfSubPopulations];
    Crossover  = new CrossoverI[numberOfSubPopulations];
    Mutation   = new MutationI[numberOfSubPopulations];
    ObjectiveFunction  = new ObjectiveFunctionFlowShopScheduleI[numberOfSubPopulations][numberOfObjs];
    Fitness   = new FitnessI[numberOfSubPopulations];

    for(int i = 0 ; i < numberOfSubPopulations ; i ++ ){
      GaMain[i]     = new fixWeightScalarization();//only apply the single thread version of GA.
      Population[i] = new population();
      Selection[i]  = new trinaryTournament();
      Crossover[i]  = new twoPointCrossover2();
      Mutation[i]   = new inverseMutation();
      ObjectiveFunction[i] = new ObjectiveFunctionFlowShopScheduleI[numberOfObjs];
      ObjectiveFunction[i][0] = new ObjectiveMakeSpanForFlowShop();//the first objective, makespan
      ObjectiveFunction[i][1] = new ObjectiveTardinessForFlowShop();//the second one.
      Fitness[i]    = new FitnessByScalarizedM_objectives();

      //set schedule data to the objectives
      ObjectiveFunction[i][0].setScheduleData(processingTime, numberOfMachines);
      ObjectiveFunction[i][1].setScheduleData(dueDay, processingTime, numberOfMachines);
      //set the data to the GA main program.
      GaMain[i].setData(Population[i], Selection[i], Crossover[i], Mutation[i],
                     ObjectiveFunction[i], Fitness[i], firstIteration, DEFAULT_initPopSize/numberOfSubPopulations,DEFAULT_PopSize/numberOfSubPopulations,
                     numberOfJob, DEFAULT_crossoverRate, DEFAULT_mutationRate, objectiveMinimization,
                     numberOfObjs, encodeType, elitism);
      //set weight data
      GaMain[i].setWeight(calcWeightsForEachSubPop(i));
    }
  }

  public void start(){
    java.util.Date start = new java.util.Date();

    for(int i = 0 ; i < numberOfSubPopulations ; i ++ ){
      //System.out.println("Sub-Pop "+i);
      GaMain[i].startGA();

      for(int k = 0 ; k < DEFAULT_PopSize/numberOfSubPopulations ; k ++ ){
        openga.util.printClass pri1 = new openga.util.printClass();
        pri1.printMatrix(""+k, GaMain[i].getPopulation().getSingleChromosome(k).genes, GaMain[i].getPopulation().getObjectiveValues(k), GaMain[i].getPopulation().getFitness(k));
      }
    }

    java.util.Date end = new java.util.Date();
    System.out.println(end.getTime() - start.getTime());
  }

  /***********              Support Methods                  ***************/
  public void calcWeights(){
    weights = new double[numberOfObjs];
    double tempWeights[] = new double[numberOfObjs];
    openga.util.algorithm.getSum Sum1 = new openga.util.algorithm.getSum();
    double sum = 0;

    for(int i = 0 ; i < numberOfObjs ; i ++ ){
      tempWeights[i] = Math.random();
    }
    Sum1.setData(tempWeights);
    sum = Sum1.getSumResult();
    //start to assign weight
    for(int i = 0 ; i < numberOfObjs ; i ++ ){
      weights[i] = tempWeights[i]/sum;
    }
  }

  public double[] calcWeightsForEachSubPop(int indexOfSubPop){
    weights = new double[numberOfObjs];
    indexOfSubPop ++;
    weights[0] = (indexOfSubPop/(double)(numberOfSubPopulations+1));
    weights[1] = 1 - (indexOfSubPop/(double)(numberOfSubPopulations+1));
    //System.out.println(weights[0]+"\t"+weights[1]);
    return weights;
  }


  public static void main(String[] args) {
    TPSPGA_forFlowShop TPSPGA_forFlowShop1 = new TPSPGA_forFlowShop();
    TPSPGA_forFlowShop1.initiateVars();
    TPSPGA_forFlowShop1.start();
  }

}