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

/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class SPGA2_KSTwoStagesClonePop {
  public SPGA2_KSTwoStagesClonePop(){
  }
  /***
   * Basic operators of GAs.
   */
  int numberOfObjs = 2;//it's a bi-objective program.
  populationI Population[];
  SelectI Selection;
  CrossoverI Crossover;
  MutationI Mutation;
  ObjectiveFunctionKnapsackI ObjectiveFunction[];
  FitnessI Fitness;
  MainWeightScalarizationI GaMain[];
  weightedScalarRepair weightedScalarRepair1;

  /**
   * Parameters of the GA
   */
  int generations, length, initPopSize, fixPopSize;
  double crossoverRate, mutationRate;
  boolean[] objectiveMinimization; //true is minimum problem.
  boolean encodeType;  //binary of realize code
  public int
    DEFAULT_generations = 200,
    DEFAULT_PopSize     = 5000,
    DEFAULT_initPopSize = 8000,
    totalSolnsToExamine = 1000000;//to fix the total number of solutions to examine.

  public double
     DEFAULT_crossoverRate = 0.9,
     DEFAULT_mutationRate  = 0.1,
     elitism               = 0.2;     //the percentage of elite chromosomes

  /**
   * The SPGA' parameters
   */
  public int numberOfSubPopulations = 40;
  public boolean applySecCRX = false;
  public boolean applySecMutation = false;
  cloneI clone1;
  boolean applyClone = false;
  int tournamentSize = 2;
  int cloneStrategy = 2;
  double timeToClone = 0;
  double weights[][];
  double cloneRatio;
  double implementationTime = 0;

  /***
   * Knapsack data
   */
  int numberOfKnapsack = 2, numberItems = 100;
  double itemWeight[][];
  double itemProfit[][];
  double knapsackWeightLimit[];
  int replicationNum = 0;

  public void setParameters(int numberOfSubPopulations, int PopSize,
                           int totalSolnsToExamine,
                           double crossoverRate, double mutationRate,
                           double elitism,
                           boolean applySecCRX, boolean applySecMutation){
    this.numberOfSubPopulations = numberOfSubPopulations;
    DEFAULT_PopSize = PopSize;
    this.totalSolnsToExamine = totalSolnsToExamine;
    this.DEFAULT_crossoverRate = crossoverRate;
    this.DEFAULT_mutationRate = mutationRate;
    this.elitism = elitism;
    DEFAULT_generations = totalSolnsToExamine /
        (PopSize * numberOfSubPopulations);
    this.applySecCRX = applySecCRX;
    this.applySecMutation = applySecMutation;
  }

  public void setKnapsackData(int numberOfKnapsack, int numberItems){
    this.numberOfKnapsack = numberOfKnapsack;
    this.numberItems = numberItems;
  }

  public void setPopClone(double cloneRatio){
    this.cloneRatio = cloneRatio;
  }

  public void setReplicationNum(int replicationNum){
    this.replicationNum = replicationNum;
  }

  public void initiateVars(){
    //initiate Knapsack data. We use the benchmark problem.
    openga.applications.data.knapsackProblem data1 = new openga.applications.data.knapsackProblem();
    itemWeight = data1.getWeight(numberOfKnapsack, numberItems);
    itemProfit = data1.getProfit(numberOfKnapsack, numberItems);
    knapsackWeightLimit = data1.getWeightConstraint(numberOfKnapsack, numberItems);
    weights = new double[numberOfSubPopulations][numberOfObjs];

    objectiveMinimization = new boolean[numberOfObjs];
    objectiveMinimization[0] = false;
    objectiveMinimization[1] = false;
    encodeType = false;

    //GA objects
    GaMain = new MainWeightScalarizationI[numberOfSubPopulations];
    Population = new populationI[numberOfSubPopulations];
    Selection  = new binaryTournament();
    Crossover  = new uniformCrossover();//uniformCrossover onePointBinaryCrossover
    Mutation   = new bitFlipMutation();
    ObjectiveFunction  = new ObjectiveFunctionKnapsackI[numberOfObjs];
    Fitness   = new FitnessByScalarizedM_objectives();

    //set knapsack data to the objectives
    weightedScalarRepair1 = new weightedScalarRepair();
    weightedScalarRepair1.setData(numberOfKnapsack, itemProfit, itemWeight, knapsackWeightLimit);
    ObjectiveFunction = new ObjectiveFunctionKnapsackI[numberOfObjs];
    ObjectiveFunction[0] = new ObjectiveKnapsackProfit();
    ObjectiveFunction[1] = new ObjectiveKnapsackProfit();
    ObjectiveFunction[0].setKnapsackData(itemProfit, itemWeight, weightedScalarRepair1);
    ObjectiveFunction[1].setKnapsackData(itemProfit, itemWeight, weightedScalarRepair1);

    for(int i = 0 ; i < numberOfSubPopulations ; i ++ ){
      GaMain[i] = new SPGAwithSharedParetoSet();
      Population[i] = new population();
      //set weight data
      weights[i] = calcWeightsForEachSubPop(i);
      GaMain[i].setWeight(weights[i]);

      //set the data to the GA main program.
      GaMain[i].setData(Population[i], Selection, Crossover, Mutation,
                     ObjectiveFunction, Fitness, DEFAULT_generations, DEFAULT_PopSize, DEFAULT_PopSize,
                     numberItems, DEFAULT_crossoverRate, DEFAULT_mutationRate, objectiveMinimization,
                     numberOfObjs, encodeType, elitism);
    }
  }

  public void start(){
    openga.util.timeClock timeClock1 = new openga.util.timeClock();
    timeClock1.start();
    //we control the number of iteration of GA here.
    //to run the first and the last sub-population at the first stage.
    int popIndex[] = new int[]{0, 1, 13, 14};//0, 1, 2, 3, 4, 30, 31, 32, 33, 34//0, 1, 2, 17, 18, 19//0, 1, 13, 14
    for(int m = 0 ; m < DEFAULT_generations ; m ++ ){
      //System.out.println("Generation: "+m);
      for(int i = 0 ; i < popIndex.length ; i ++ ){
        weightedScalarRepair1.setData(GaMain[popIndex[i]].getWeight());
        //System.out.println("numberOfSubPopulations "+i);
        if(m == 0){//initial each population and its objective values.
          GaMain[popIndex[i]].InitialRun();
          if(i == 0){
            GaMain[i].initFirstArchieve();
          }
        }
        GaMain[popIndex[i]].startGA();
      }
    }

    //To clone solution
    populationClone populationClone1 = new populationClone();
    populationClone1.setData(Population, GaMain[0].getArchieve(), Crossover, popIndex, weights, 1, DEFAULT_PopSize);
    populationClone1.startClonePopulation();

/*
    populationClone2 populationClone21 = new populationClone2();
    populationClone21.setData(Population, popIndex, 1, DEFAULT_PopSize);
    populationClone21.startClonePopulation();
    //System.out.println("Stage 2.");
*/

     //it is the version in 2006
     for(int m = 0 ; m < DEFAULT_generations ; m ++ ){
      //System.out.println("Stage 2. Generation: "+m);
      for(int i = 0 ; i < numberOfSubPopulations ; i ++ ){
        weightedScalarRepair1.setData(GaMain[i].getWeight());
        //to skip the two center sub-populations
        if(i < 4 || i > 7){//7 12, 13 22, 7 10, 6 11
          //System.out.println("numberOfSubPopulations "+i);
          if(m == 0 && (checkGroup(i, popIndex))){
            GaMain[i].ProcessObjectiveAndFitness();
          }
          GaMain[i].startGA();
        }
      }
    }
    /*
   System.out.println("Stage 2.");
   for(int m = 0 ; m < DEFAULT_generations ; m ++ ){
     System.out.println("Generation: "+m);
     for(int i = 0 ; i < numberOfSubPopulations ; i ++ ){
       //to skip the two center sub-populations
       if(i < 6 || i > 11){//7 12, 13 22, 7 10, 6 11
         //System.out.println("numberOfSubPopulations "+i);
         if(m == 0 && (checkGroup(i, popIndex))){
           //GaMain[i].InitialRun();
           populationClone3 populationClone1 = new populationClone3();
           populationClone1.setData(Population, GaMain[0].getArchieve(), Crossover, popIndex, weights, 1, DEFAULT_PopSize);
           populationClone1.startClonePopulation(i);
           GaMain[i].ProcessObjectiveAndFitness();
           int tempIndex[] = new int[popIndex.length + 1];
           for(int k = 0 ; k < popIndex.length ; k ++ ){
             tempIndex[k] = popIndex[k];
           }
           tempIndex[popIndex.length] = i;
           popIndex = tempIndex;
         }
         GaMain[i].startGA();
       }
     }
   }
   */
    //to output the implementation result.
    timeClock1.end();
    implementationTime = timeClock1.getExecutionTime();
    outputResults();

    for(int i = 0 ; i < numberOfSubPopulations ; i ++ ){
      GaMain[i].destroidArchive();
      GaMain[i] = null;
    }

  }

  public boolean checkGroup(int index, int[] group){
    for(int i = 0 ; i < group.length ; i ++ ){
      if(index == group[i]){
        return false;
      }
    }
    return true;
  }

  public populationI getArchiveByRepairOperator(int i){
    weightedScalarRepair1.setData(GaMain[i].getArchieve());
    weightedScalarRepair1.setData(GaMain[i].getWeight());
    weightedScalarRepair1.startRepair();
    return weightedScalarRepair1.getPopulation();
  }

  public void outputResults(){
    String implementResult = "";
    implementResult = numberItems+"\t"+implementationTime/1000.0+"\n";//implementationTime

    writeFile("SPGA2_knapsackCPU", implementResult);
    System.out.println(implementResult);

/*
    //double objArray[][] = GaMain[0].getArchieveObjectiveValueArray();
    populationI combinedArchive = GaMain[0].getArchieve();
    for(int i = 0 ; i < numberOfSubPopulations ; i ++ ){
      //to skip the two center sub-populations
      if(i < 7 || i > 10){
        combinedArchive = GaMain[0].updateParetoSet(combinedArchive, getArchiveByRepairOperator(i));
      }
    }

    for(int i = 0 ; i < combinedArchive.getPopulationSize() ; i ++ ){
      for(int j = 0 ; j < numberOfObjs ; j ++ ){//for each objectives
        implementResult += combinedArchive.getObjectiveValues(i)[j]+"\t";
      }
      implementResult += "\n";
    }
    writeFile("SPGA2_2_"+numberItems+"_"+replicationNum, implementResult);

    double refSet[][] = getReferenceSet();
    double D1r = calcSolutionQuality(refSet, combinedArchive.getObjectiveValueArray());
    implementResult = numberOfKnapsack+"\t"+numberItems+"\t"+DEFAULT_crossoverRate+"\t"+DEFAULT_mutationRate+"\t"+elitism+"\t"+D1r+"\t"+implementationTime/1000.0+"\n";//
    writeFile("SPGA2_Knapsack_AddPhase", implementResult);
    System.out.println(implementResult);

    //to get the Item 750 Pareto set.
    implementResult = "";
    for(int i = 0 ; i < GaMain[0].getArchieve().getPopulationSize() ; i ++ ){
      for(int j = 0 ; j < numberOfObjs ; j ++ ){//for each objectives
        implementResult += GaMain[0].getArchieve().getObjectiveValues(i)[j]+"\t";
      }
      implementResult += "\n";
    }//numberOfKnapsack = 2,
    writeFile("SPGA2_2_"+numberItems, implementResult);

    double refSet[][] = getReferenceSet();
    double D1r = calcSolutionQuality(refSet, objArray);
    implementResult = numberOfKnapsack+"\t"+numberItems+"\t"+DEFAULT_crossoverRate+"\t"+DEFAULT_mutationRate+"\t"+elitism+"\t"+D1r+"\n";//
    writeFile("SPGA2_Knapsack_AddPhase", implementResult);
    System.out.println(implementResult);
*/
    /*
    implementResult = "";
    for(int i = 0 ; i < GaMain[0].getArchieve().getPopulationSize() ; i ++ ){
      for(int j = 0 ; j < numberOfObjs ; j ++ ){//for each objectives
        implementResult += GaMain[0].getArchieve().getObjectiveValues(i)[j]+"\t";
      }
      implementResult += "\n";
    }
    writeFile("SPGA2_Knapsack_pareto", implementResult);
    /*
    implementResult = "";
    weightedScalarRepair1.setData(GaMain[0].getArchieve().getSingleChromosome(0));
    weightedScalarRepair1.startRepair();
    for(int j = 0 ; j < GaMain[0].getArchieve().getLengthOfChromosome() ; j ++ ){//for each objectives
      implementResult += weightedScalarRepair1.getResult().getSolution()[j]+"\t";
    }
    implementResult += "\n";
    writeFile("SPGA2_Knapsack_soln", implementResult);
    */
  }

  public double[][] getReferenceSet(){
    openga.applications.data.knapsackProblem data1 = new openga.applications.data.knapsackProblem();
    return data1.getReferenceSet(numberOfKnapsack, numberItems);
  }

  public double calcSolutionQuality(double refSet[][], double obtainedPareto[][]){
    openga.util.algorithm.D1r d1r1 = new openga.util.algorithm.D1r();
    d1r1.setData(refSet, obtainedPareto);
    d1r1.calcD1r();
    //d1r1.calcD1rWithoutNormalization();
    return d1r1.getD1r();
  }

  /**
   * Write the data into text file.
   */
  public void writeFile(String fileName, String _result){
    fileWrite1 writeLotteryResult = new fileWrite1();
    writeLotteryResult.writeToFile(_result,fileName+".txt");
    Thread thread1 = new Thread(writeLotteryResult);
    thread1.run();
  }

  public final double[] calcWeightsForEachSubPop(int indexOfSubPop){
    double weights[] = new double[numberOfObjs];
    indexOfSubPop ++;
    weights[0] = ((indexOfSubPop+1)/(double)(numberOfSubPopulations+1));
    weights[1] = 1 - weights[0];
    //System.out.print(weights[0]+"\t"+weights[1]+"\n");
    return weights;
  }

  public static void main(String[] args) {
    System.out.println("SPGA2_Knapsack 424");
    int numberOfSubPopulations[] = new int[]{15};//35 20
    int popSize[] = new int[]{200};//100, 155, 210
    int numberOfItem[] = new int []{750};//5, 100, 250, 500, 750
    int numberOfKnapsack = 2;
    int totalSolnsToExamine[] = new int[]{75000, 100000, 125000};//100000, 1000000, 1500000, 2000000
    boolean applySecCRX[] = new boolean[]{false};//false, true
    boolean applySecMutation[] = new boolean[]{false};
    int tournamentSize[] = new int[]{2, 10};

    double crossoverRate[] = new double[]{0.8},
           mutationRate[] = new double[]{0},
           elitism[] = new double[]{0.15};
    int repeatExperiments = 30;
    int counter = 0;

    for(int i = 0 ; i < repeatExperiments ; i ++ ){
      for(int j = 0 ; j < numberOfItem.length ; j ++ ){
        SPGA2_KSTwoStagesClonePop SPGA2_knapsack1 = new SPGA2_KSTwoStagesClonePop();
        System.out.println("combinations: "+counter++);
        SPGA2_knapsack1.setKnapsackData(numberOfKnapsack, numberOfItem[j]);
        SPGA2_knapsack1.setParameters(numberOfSubPopulations[0], popSize[0],
            totalSolnsToExamine[j], crossoverRate[0], mutationRate[0],
            elitism[0], applySecCRX[0], applySecMutation[0]);
        //SPGA2_forFlowShop1.setCloneActive(applyClone[1], cloneStrategies[0]);
        //SPGA2_forFlowShop1.setPopClone(cloneRatio[0]);
        SPGA2_knapsack1.initiateVars();
        SPGA2_knapsack1.setReplicationNum(i);
        SPGA2_knapsack1.start();
        SPGA2_knapsack1 = null;
        System.gc();
      }
    }
    /*
   for(int i = 0 ; i < repeatExperiments ; i ++ ){
     for(int k = 0 ; k < crossoverRate.length ; k ++ ){
       for(int m = 0 ; m < mutationRate.length ; m ++ ){
         for(int n = 0 ; n < elitism.length ; n ++ ){
           for(int j = 0 ; j < numberOfItem.length ; j ++ ){
             SPGA2_KSTwoStagesClonePop SPGA2_forFlowShop1 = new SPGA2_KSTwoStagesClonePop();
             System.out.println("combinations: "+counter++);
             SPGA2_forFlowShop1.setKnapsackData(numberOfKnapsack, numberOfItem[j]);
             SPGA2_forFlowShop1.setParameters(numberOfSubPopulations[0], popSize[0],
                 totalSolnsToExamine[0], crossoverRate[k], mutationRate[m],
                 elitism[n], applySecCRX[0], applySecMutation[0]);
             //SPGA2_forFlowShop1.setCloneActive(applyClone[1], cloneStrategies[0]);
             //SPGA2_forFlowShop1.setPopClone(cloneRatio[0]);
             SPGA2_forFlowShop1.initiateVars();
             SPGA2_forFlowShop1.start();
             SPGA2_forFlowShop1 = null;
             System.gc();
           }
         }
       }
     }
   }
   */
  }
}