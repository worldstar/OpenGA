/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package openga.applications.knapsack;
import openga.chromosomes.*;
import openga.operator.selection.*;
import openga.operator.crossover.*;
import openga.operator.mutation.*;
import openga.operator.clone.*;
import openga.operator.repair.*;
import openga.ObjectiveFunctions.*;
import openga.MainProgram.*;
import openga.ObjectiveFunctions.*;
import openga.Fitness.*;
import openga.util.printClass;
import openga.util.fileWrite1;
import openga.applications.data.knapsackORLibProblems;
/**
 *
 * @author user
 */
public class knapsackSGA {
  /***
   * Basic variables of GAs.
   */
  int numberOfObjs = 1;//it's a single-objective program.
  populationI Population;
  SelectI Selection;
  CrossoverI Crossover;
  MutationI Mutation, Mutation2;
  ObjectiveFunctionKnapsackI ObjectiveFunction[];
  FitnessI Fitness;
  MainI GaMain;
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

  public void initiateVars(){
    objectiveMinimization = new boolean[numberOfObjs];
    objectiveMinimization[0] = false;
    encodeType = false;

    //GA objects
    GaMain = new singleThreadGA();
    Population = new population();
    Selection  = new binaryTournamentMaximization();
    Crossover  = new uniformCrossover();//uniformCrossover onePointBinaryCrossover
    Mutation   = new bitFlipMutation();
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
  }

  public void start(){
    openga.util.timeClock timeClock1 = new openga.util.timeClock();
    timeClock1.start();
    GaMain.startGA();
    timeClock1.end();

    int bestInd = getBestSolnIndex(GaMain.getArchieve());
    String implementResult = instanceName+"\t"+crossoverRate+"\t"+mutationRate+"\t"+elitism+"\t"+GaMain.getArchieve().getSingleChromosome(bestInd).getObjValue()[0]
        +"\t"+timeClock1.getExecutionTime()/1000.0+"\n";//
    writeFile("knapsackSGA_DOE20101107", implementResult);
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
  
  /**
   * Write the data into text file.
   */
  void writeFile(String fileName, String _result){
    fileWrite1 writeLotteryResult = new fileWrite1();
    writeLotteryResult.writeToFile(_result,fileName+".txt");
    Thread thread1 = new Thread(writeLotteryResult);
    thread1.run();
  }


  public static void main(String[] args) {
    System.out.println("knapsackSGA_DOE20101107");
    int popSize[] = new int[]{100};//100, 200
    int numberOfItem[] = new int []{500};//100, 250, 500
    int numberOfKnapsack[] = new int[]{5, 10, 30};//5, 10, 30

    double crossoverRate[] = new double[]{0.9, 0.5},//0.9, 0.5
           mutationRate[] = new double[]{0.5, 0.1},//0.5, 0.1
           elitism[] = new double[]{0.2, 0.1};//0.2, 0.1
    int repeatExperiments = 30;
    int counter = 0;

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
              for(int p = 0 ; p < elitism.length ; p ++){
                for(int x = 0 ; x < repeatExperiments ; x ++ ){
                  String instanceName = "knapsack"+numberOfItem[i]+"-"+numberOfKnapsack[j]+"-"+k;
                  knapsackSGA knapsackSGA1 = new knapsackSGA();
                  knapsackSGA1.setData(numberOfItem[i], numberOfKnapsack[j], popSize[0], crossoverRate[m], mutationRate[n], elitism[p]);
                  knapsackSGA1.setKnapsackData(instanceName, profit[k], weights[k], rightHandSide[k]);
                  knapsackSGA1.initiateVars();
                  knapsackSGA1.start();
                }
              }
            }
          }
          //k += 10; //Choosing the first instance when we do the DOE.
        }

      }
    }

  }
}
