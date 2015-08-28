/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package openga.applications.knapsack;
import openga.chromosomes.*;
import openga.operator.selection.*;
import openga.operator.crossover.*;
import openga.operator.mutation.*;
import openga.operator.repair.*;
import openga.MainProgram.*;
import openga.ObjectiveFunctions.*;
import openga.Fitness.*;
import openga.util.fileWrite1;
import openga.applications.data.knapsackORLibProblems;
import openga.operator.miningGene.*;
/**
 *
 * @author user
 */
public class SelfGuidedGAKnapsack extends knapsackSGA{
  /***
   * Basic variables of GAs.
   */
  int numberOfObjs = 1;//it's a single-objective program.
  populationI Population;
  SelectI Selection;
  ObjectiveFunctionKnapsackI ObjectiveFunction[];
  FitnessI Fitness;

  EDAMainI GaMain;
  EDAICrossover Crossover;
  EDAIMutation Mutation;  
  EDAModelBuildingI model;
  weightedRepairI weightedScalarRepair1;

  /**
   * Parameters of the GA
   */
  int generations, length, fixPopSize;
  double crossoverRate, mutationRate, elitism;
  boolean[] objectiveMinimization; //true is minimum problem.
  boolean encodeType;  //binary of realize code
  int seed = 12345;
  int counter = 0;
  int totalSolnsToExamine = 10000;  //10000 1000000

  /**
   * Parameters of the EDA
   */
  double lamda = 0.9; //learning rate
  int numberOfCrossoverTournament = 2;
  int numberOfMutationTournament = 2;
  int startingGenDividen = 4;

  /**
   * Parameters of the Knapsack
   */
  int numberOfItems;
  int numberOfKnapsack;
  double itemProfit[];
  double itemWeight[][];
  double rightHandSide[];
  String instanceName = "";


  public void setData(int numberOfItems, int numberOfKnapsack, int fixPopSize,
          double crossoverRate, double mutationRate, double elitism){
    this.numberOfItems = numberOfItems;
    this.numberOfKnapsack = numberOfKnapsack;
    this.fixPopSize = fixPopSize;
    this.crossoverRate = crossoverRate;
    this.mutationRate = mutationRate;
    this.elitism = elitism;
  }

  public void setKnapsackData(String instanceName, double profit[], double weights[][],
          double rightHandSide[]){
    this.instanceName = instanceName;
    this.itemProfit = profit;
    this.itemWeight = weights;
    this.rightHandSide = rightHandSide;
  }

  public void setEDAinfo(double lamda, int numberOfCrossoverTournament, int numberOfMutationTournament, int startingGenDividen){
    this.lamda = lamda;
    this.numberOfCrossoverTournament = numberOfCrossoverTournament;
    this.numberOfMutationTournament = numberOfMutationTournament;
    this.startingGenDividen = startingGenDividen;
  }

  public void initiateVars(){
    objectiveMinimization = new boolean[numberOfObjs];
    objectiveMinimization[0] = false;
    encodeType = false;

    //GA objects
    GaMain = new singleThreadGAwithEDA();
    Population = new population();
    Selection  = new binaryTournamentMaximization();
    Crossover  = new uniformCrossoverEDA();
    Mutation   = new bitFlipMutationEDA();
    ObjectiveFunction  = new ObjectiveFunctionKnapsackI[numberOfObjs];
    ObjectiveFunction[0] = new ObjectiveKnapsackProfitSingleObjective();
    Fitness   = new singleObjectiveFitness();

    //set knapsack data to the objectives
    weightedScalarRepair1 = new KnapsackRepairSingleObjective();
    weightedScalarRepair1.setData(numberOfKnapsack, itemProfit, itemWeight, rightHandSide);
    ObjectiveFunction[0].setKnapsackData(itemProfit, itemWeight, weightedScalarRepair1);

    generations = totalSolnsToExamine/fixPopSize;

      //set the data to the GA main program.
      GaMain.setData(Population, Selection, Crossover, Mutation,
                     ObjectiveFunction, Fitness, generations, fixPopSize, fixPopSize,
                     numberOfItems, crossoverRate, mutationRate, objectiveMinimization,
                     numberOfObjs, encodeType, elitism);
      model = new PBILforZeroOneWithLapaceCorrection(numberOfItems, lamda);
      GaMain.setEDAinfo(lamda, numberOfCrossoverTournament, numberOfMutationTournament, startingGenDividen, model);
  }

  public void start(){
    openga.util.timeClock timeClock1 = new openga.util.timeClock();
    timeClock1.start();
    GaMain.startGA();
    timeClock1.end();


    int bestInd = getBestSolnIndex(GaMain.getArchieve());
    String implementResult = instanceName+"\t"+crossoverRate+"\t"+mutationRate+"\t"+ lamda+"\t"+numberOfCrossoverTournament+"\t"
            + numberOfMutationTournament+"\t"+ startingGenDividen+"\t"
            + GaMain.getArchieve().getSingleChromosome(bestInd).getObjValue()[0]
        +"\t"+timeClock1.getExecutionTime()/1000.0+"\n";//
    writeFile("EDA_Knapsack20101109DOE", implementResult);
    System.out.print(implementResult);
  }

  /**
   * For single objective problem
   * @param arch1
   * @return
   */
  public int getBestSolnIndex(populationI arch1){
    int index = 0;
    double bestobj = Double.MAX_VALUE;
    for(int k = 0 ; k < GaMain.getArchieve().getPopulationSize() ; k ++ ){
      if(bestobj > GaMain.getArchieve().getObjectiveValues(k)[0]){
        bestobj = GaMain.getArchieve().getObjectiveValues(k)[0];
        index = k;
      }
    }
    return index;
  }

  public static void main(String[] args) {
    System.out.println("EDA_Knapsack20101109DOE");
    int popSize[] = new int[]{100};//100, 200
    int numberOfItem[] = new int []{100, 250, 500};//100, 250, 500
    int numberOfKnapsack[] = new int[]{5, 10, 30};//5, 10, 30

    double crossoverRate[] = new double[]{0.9, 0.5},//0.9, 0.5
           mutationRate[] = new double[]{0.5, 0.1},//0.5, 0.1
           elitism[] = new double[]{0.2, 0.1};//0.2, 0.1
    int repeatExperiments = 30;
    int counter = 0;

    //EDA parameters.
    double[] lamda = new double[]{0.1, 0.5, 0.9}; //learning rate{0.1, 0.5, 0.9} {0.1}
    int numberOfCrossoverTournament[] = new int[]{2, 4};//{1, 2, 4} //2
    int numberOfMutationTournament[] = new int[]{1, 2, 4};//{1, 2, 4}  //2
    int startingGenDividen[] = new int[]{2, 6};//{2, 6}  //6

    for(int i = 0 ; i < numberOfItem.length ; i ++ ){
      for(int j = 0 ; j < numberOfKnapsack.length ; j ++ ){
        knapsackORLibProblems knapsackORLibProblems1 = new knapsackORLibProblems();
        knapsackORLibProblems1.readInstanceData(numberOfItem[i], numberOfKnapsack[j]);

        int numberOfInstanceReplications = knapsackORLibProblems1.getInstanceReplications();
        double profit[][] = knapsackORLibProblems1.getProfit();
        double weights[][][] = knapsackORLibProblems1.getWeights();
        double rightHandSide[][] = knapsackORLibProblems1.getRightHandSide();

        for(int k = 0 ; k < numberOfInstanceReplications ; ){
          for(int m = 0 ; m < crossoverRate.length ; m ++){
            for(int n = 0 ; n < mutationRate.length ; n ++){
              for(int p = 0 ; p < lamda.length ; p ++){
                for(int q = 0 ; q < numberOfCrossoverTournament.length ; q ++){
                  for(int r = 0 ; r < numberOfMutationTournament.length ; r ++){
                      for(int s = 0 ; s < startingGenDividen.length ; s ++){
                        for(int x = 0 ; x < repeatExperiments ; x ++ ){
                          String instanceName = "knapsack"+numberOfItem[i]+"-"+numberOfKnapsack[j]+"-"+k;
                          SelfGuidedGAKnapsack SelfGuidedGAKnapsack1 = new SelfGuidedGAKnapsack();
                          SelfGuidedGAKnapsack1.setData(numberOfItem[i], numberOfKnapsack[j], popSize[0], crossoverRate[m], mutationRate[n], elitism[0]);
                          SelfGuidedGAKnapsack1.setKnapsackData(instanceName, profit[k], weights[k], rightHandSide[k]);
                          SelfGuidedGAKnapsack1.setEDAinfo(lamda[p], numberOfCrossoverTournament[q], numberOfMutationTournament[r], startingGenDividen[s]);
                          SelfGuidedGAKnapsack1.initiateVars();
                          SelfGuidedGAKnapsack1.start();
                        }
                    }
                  }
                }
              }
            }
          }
          k += 10;
        }

      }
    }

  }
}
