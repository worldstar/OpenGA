package openga.applications.singleMachineProblem;

import openga.chromosomes.*;
import openga.operator.selection.*;
import openga.operator.crossover.*;
import openga.operator.mutation.*;
import openga.operator.clone.*;
import openga.ObjectiveFunctions.*;
import openga.MainProgram.*;
import openga.ObjectiveFunctions.*;
import openga.Fitness.*;
import openga.util.printClass;
import openga.util.fileWrite1;

/**
 * <p>
 * Title: The OpenGA project which is to build general framework of Genetic
 * algorithm.</p>
 * <p>
 * Description: The SPGA 2 solves the two-objective single machine scheduling
 * problem. The two objectives are the tardiness and earliness. </p>
 * <p>
 * Copyright: Copyright (c) 2006</p>
 * <p>
 * Company: Yuan-Ze University</p>
 *
 * @author Chen, Shih-Hsin
 * @version 1.0
 */
public class SPGA2_forSingleMachine {

  public SPGA2_forSingleMachine() {
  }
  /**
   * *
   * Basic operators of GAs.
   */
  int numberOfObjs = 2;//it's a bi-objective program.
  populationI Population[];
  SelectI Selection;
  CrossoverI Crossover, Crossover2;
  MutationI Mutation, Mutation2;
  ObjectiveFunctionScheduleI ObjectiveFunction[];
  FitnessI Fitness;
  MainWeightScalarizationI GaMain[];

  /**
   * Parameters of the GA
   */
  int generations, length, initPopSize, fixPopSize;
  double crossoverRate, mutationRate;
  boolean[] objectiveMinimization; //true is minimum problem.
  boolean encodeType;  //binary of realize code
  public String fileName = "";

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
  double alpha;
  double b;

  /**
   * *
   * Scheduling data
   */
  int dueDay[], processingTime[];
  int numberOfJob = 65;

  public int DEFAULT_generations = 200,
          DEFAULT_PopSize = 200,
          DEFAULT_initPopSize = 200,
          totalSolnsToExamine = 100000;//to fix the total number of solutions to examine.

  public double DEFAULT_crossoverRate = 0.9,
          DEFAULT_mutationRate = 0.3,
          elitism = 0.2;     //the percentage of elite chromosomes

  /**
   * For DP
   */
  public int DP_Iter = 1000;

  public void setParameters(int numberOfSubPopulations, int PopSize, int totalSolnsToExamine,
          double crossoverRate, double mutationRate, double elitism,
          boolean applySecCRX, boolean applySecMutation, int DP_Iter,
          double alpha, double b) {
    this.numberOfSubPopulations = numberOfSubPopulations;
    DEFAULT_PopSize = PopSize;
    this.totalSolnsToExamine = totalSolnsToExamine;
    this.DEFAULT_crossoverRate = crossoverRate;
    this.DEFAULT_mutationRate = mutationRate;
    this.elitism = elitism;
    this.DP_Iter = DP_Iter;
    DEFAULT_generations = totalSolnsToExamine / (PopSize * numberOfSubPopulations);
//    System.out.println(DEFAULT_generations);
    this.applySecCRX = applySecCRX;
    this.applySecMutation = applySecMutation;
    this.alpha = alpha;
    this.b = b;
  }

  public void setData(int numberOfJobs, int dueDate[], int processingTime[], String fileName) {
    this.numberOfJob = numberOfJobs;
    this.dueDay = dueDate;
    this.processingTime = processingTime;
    this.fileName = fileName;
  }

  public void initiateVars() {
    //System.out.println("DEFAULT_generations "+DEFAULT_generations);
    //initiate scheduling data. We use the benchmark problem.
    openga.applications.data.parallelMachine data1 = new openga.applications.data.parallelMachine();
    dueDay = data1.getTestData3_DueDay(numberOfJob);
    processingTime = data1.getTestData3_processingTime(numberOfJob);
    weights = new double[numberOfSubPopulations][numberOfObjs];

    objectiveMinimization = new boolean[numberOfObjs];
    objectiveMinimization[0] = true;
    objectiveMinimization[1] = true;
    encodeType = true;

    //GA objects
    GaMain = new MainWeightScalarizationI[numberOfSubPopulations];
    Population = new populationI[numberOfSubPopulations];
    Selection = new binaryTournament();//binaryTournament similaritySelection2 tenaryTournament varySizeTournament
    Crossover = new twoPointCrossover2();//oneByOneChromosomeCrossover twoPointCrossover2 twoPointCrossover2withAdpative
    //Crossover2  = new PMX();
    Mutation = new swapMutation();//swapMutation shiftMutation swapMutationWithAdaptive
    //Mutation2  = new shiftMutation();//shiftMutation
    clone1 = new solutionVectorCloneWithMutation();
    ObjectiveFunction = new ObjectiveFunctionScheduleI[numberOfObjs];
    Fitness = new FitnessByScalarizedM_objectives();

    //ObjectiveFunction[0] = new ObjectiveEarlinessForSingleMachine();//the first objective
    //ObjectiveFunction[1] = new ObjectiveTardinessForSingleMachine();//the second one.
    ObjectiveFunction[0] = new objectiveTC();//the first objective
    ObjectiveFunction[1] = new objectiveTADC();//the second one.
    int numberOfMachines = 1;
    //set schedule data to the objectives
    ObjectiveFunction[0].setScheduleData(dueDay, processingTime, numberOfMachines);
    ((alphaBetaI) ObjectiveFunction[0]).setAlphaBeta(new double[]{alpha}, new double[]{b});
    ObjectiveFunction[1].setScheduleData(dueDay, processingTime, numberOfMachines);
    ((alphaBetaI) ObjectiveFunction[1]).setAlphaBeta(new double[]{alpha}, new double[]{b});
    System.out.println(String.valueOf(alpha) + "/" + String.valueOf(b));
    DEFAULT_generations = 5;
    for (int i = 0; i < numberOfSubPopulations; i++) {
      GaMain[i] = new SPGAwithSharedParetoSet();//singleThreadGA SPGAwithSharedParetoSet adaptiveGA
      Population[i] = new population();

      //set the data to the GA main program.
      GaMain[i].setData(Population[i], Selection, Crossover, Mutation,
              ObjectiveFunction, Fitness, DEFAULT_generations, DEFAULT_PopSize, DEFAULT_PopSize,
              numberOfJob, DEFAULT_crossoverRate, DEFAULT_mutationRate, objectiveMinimization,
              numberOfObjs, encodeType, elitism);
      //set weight data
      weights[i] = calcWeightsForEachSubPop(i);
      GaMain[i].setWeight(weights[i]);
      //set secondary crossover and mutation operator.
      GaMain[i].setSecondaryCrossoverOperator(Crossover2, applySecCRX);
      GaMain[i].setSecondaryMutationOperator(Mutation2, applySecMutation);
      GaMain[i].setCloneOperatpr(clone1, applyClone);
    }
  }

  public void start() {
    openga.util.timeClock timeClock1 = new openga.util.timeClock();
    timeClock1.start();

    //we control the number of iteration of GA here.
    for (int m = 0; m < DEFAULT_generations; m++) {
      //System.out.println("Generation: "+m);

      for (int i = 0; i < numberOfSubPopulations; i++) {
        //System.out.println("numberOfSubPopulations "+i);
        if (m == 0) {//initial each population and its objective values.
          GaMain[i].InitialRun();
          if (i == 0) {
            GaMain[i].initFirstArchieve();
          }
        }
        GaMain[i].startGA();
      }
    }
    timeClock1.end();
    printResults();
  }

  /**
   * ********* Support Methods **************
   */
  public final double[] calcWeightsForEachSubPop(int indexOfSubPop) {
    double weights[] = new double[numberOfObjs];
    indexOfSubPop++;
    weights[0] = ((indexOfSubPop + 1) / (double) (numberOfSubPopulations + 1));
    weights[1] = 1 - weights[0];
    //System.out.print(weights[0]+"\t"+weights[1]+"\n");
    return weights;
  }

  public void printResults() {
    //to output the implementation result.
    String implementResult = "";
    /*
   double refSet[][] = getReferenceSet();
   double objArray[][] = GaMain[0].getArchieveObjectiveValueArray();
   double D1r = calcSolutionQuality(refSet, objArray);
   implementResult = numberOfJob+"\t"+"\t"+cloneRatio+"\t"+D1r+"\t"+implementationTime+"\n";
   objArray = null;
   writeFile("SPGA2_FlowShop 505", implementResult);
   System.out.println(numberOfJob+"\t"+DEFAULT_crossoverRate+"\t"+DEFAULT_mutationRate+"\t"+D1r+"\t"+implementationTime);
     */

    implementResult = "";
    int tes = GaMain[0].getArchieve().getPopulationSize();
    System.out.println("GaMain[0].getArchieve().getPopulationSize() " + GaMain[0].getArchieve().getPopulationSize());
    for (int k = 0; k < GaMain[0].getArchieve().getPopulationSize(); k++) {
      //implementResult += GaMain.getArchieve().getSingleChromosome(k).toString1()+"\t";//sequnence
      for (int j = 0; j < numberOfObjs; j++) {//for each objectives
        implementResult += GaMain[0].getArchieve().getObjectiveValues(k)[j] + "\t";
      }
      implementResult += "\n";
      System.out.println(implementResult);
      implementResult += "\n";
    }
    writeFile("SPGA2_SingleMachine_20060604", implementResult);

    //implementationResult += fileName+"\t"+numberOfJob+"\t" + GaMain.getArchieve().getSingleChromosome(bestInd).getObjValue()[0]+"\t"+timeClock1.getExecutionTime()/1000.0+"\n";
    //writeFile("singleMachineBiCriteriaGA_20060604", implementationResult);
    //System.out.println(implementationResult);
  }

  /**
   * Write the data into text file.
   */
  public void writeFile(String fileName, String _result) {
    fileWrite1 writeLotteryResult = new fileWrite1();
    writeLotteryResult.writeToFile(_result, fileName + ".txt");
    Thread thread1 = new Thread(writeLotteryResult);
    thread1.run();
  }

  public static void main(String[] args) {
    System.out.println("SPGA2_SingleMachine");
    int numberOfSubPopulations[] = new int[]{5};//35
    int popSize[] = new int[]{100};//100, 155, 210
    double[] alpha = new double[]{0.05, 0.1, 0.2};//0.2, 0.1, 0.05 [0.1] //LocalSearch maxNeighborhood
    double[] b = new double[]{0.1};//0.1, 0.2, 0.3
    int numberOfJob[] = new int[]{20};//20, 30, 40, 50, 100, 200
    int totalSolnsToExamine[] = new int[]{100000};
    boolean applySecCRX[] = new boolean[]{false, true};//false, true
    boolean applySecMutation[] = new boolean[]{false, true};
    int tournamentSize[] = new int[]{2, 10};
    boolean applyClone[] = new boolean[]{false, true};
    int cloneStrategies[] = new int[]{1};//0: random, 1: swap, 2:inverse, 3:shift, 4:adjacent.
    double cloneRatio[] = new double[]{1};//0, 0.25, 0.5, 0.75, 1
    double migrationRatio[] = new double[]{0.25};//0, 0.25, 0.5, 0.75

    double crossoverRate[] = new double[]{1},//{1}
            mutationRate[] = new double[]{0.5},//{0.18}
            elitism = 0.2;
    int repeatExperiments = 1;
    int combinations = 0;
    int counter = 0;
    int DP_iter = 0;

    for (int i = 0; i < repeatExperiments; i++) {
      for (int m = 0; m < numberOfJob.length; m++) {
        for (int k = 0; k < 1; k++) {
          for (int j = 0; j < crossoverRate.length; j++) {
            for (int n = 0; n < mutationRate.length; n++) {
              for (int q = 0; q < alpha.length; q++) {
                for (int w = 0; w < b.length; w++) {

                  SPGA2_forSingleMachine singleMachine1 = new SPGA2_forSingleMachine();
                  System.out.println("Combinations: " + counter);
                  openga.applications.data.singleMachine readSingleMachineData1 = new openga.applications.data.singleMachine();
                  int numberOfJobs = numberOfJob[m];
                  String fileName = readSingleMachineData1.getFileName(numberOfJobs, k);
                  //fileName = "bky"+numberOfJobs+"_1";
                  System.out.print(fileName + "\t");
                  readSingleMachineData1.setData("sks/" + fileName + ".txt");
                  readSingleMachineData1.getDataFromFile();
                  int dueDate[] = readSingleMachineData1.getDueDate();
                  int processingTime[] = readSingleMachineData1.getPtime();

                  singleMachine1.setData(numberOfJobs, dueDate, processingTime, fileName);
                  singleMachine1.setParameters(numberOfSubPopulations[0], popSize[0],
                          totalSolnsToExamine[0], crossoverRate[j], mutationRate[n],
                          elitism, applySecCRX[0], applySecMutation[0], DP_iter,
                          alpha[q], b[w]);

                  //singleMachine1.setCloneActive(applyClone[1], cloneStrategies[0]);
                  //singleMachine1.setPopClone(cloneRatio[0]);
                  singleMachine1.initiateVars();
                  singleMachine1.start();
                  singleMachine1 = null;
                  System.gc();
                  counter++;

                }
              }
            }
          }
        }
      }
    }
  }
}
