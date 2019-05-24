package openga.applications.Continuous;

import java.io.IOException;
import java.text.ParseException;
import openga.chromosomes.*;
import openga.operator.selection.*;
import openga.operator.crossover.*;
import openga.operator.mutation.*;
import openga.ObjectiveFunctions.*;
import openga.MainProgram.*;
import openga.Fitness.*;
import openga.util.fileWrite1;

/**
 * <p>
 * Title: The OpenGA project which is to build general framework of Genetic
 * algorithm.</p>
 * <p>
 * Description: Himmelblau is a function which is a continuous problem.</p>
 * <p>
 * Copyright: Copyright (c) 2005</p>
 * <p>
 * Company: Yuan-Ze University</p>
 *
 * @author
 * @version 1.0
 */
public class flowshopOAS_RF {

  public flowshopOAS_RF() {
  }
  
  /* Basic variables of GAs.*/
  int numberOfObjs = 1;
  populationI Population;
  SelectI Selection;
  CrossoverI_Weka Crossover, Crossover2;
  MutationI_Weka Mutation, Mutation2;
  ObjFunctionPFSSOAWT_PSDI[] ObjectiveFunction;
  FitnessI Fitness;
  singleThreadGA_WekaI GaMain;
  
  /*Parameters of the GA*/
  int generations, length, initPopSize, fixPopSize;
  double crossoverRate, mutationRate;
  boolean[] objectiveMinimization; //true is minimum problem.
  boolean encodeType;  //binary of realize code
  int seed = 12345;
  int counter = 0;

  //Instance Data
  int piTotal;
  int machineTotal;
  int[] fristProfit;    //  revenue of order
  int[] di;        //  due-date
  double[] wi;     //  tardiness penalty weight
  int[][] processingTime;

  //Results
  double bestObjectiveValues[];
  populationI solutions;

  public int DEFAULT_generations = 1000,
          DEFAULT_PopSize = 100,
          DEFAULT_initPopSize = 100;

  public double DEFAULT_crossoverRate = 0.9,
          DEFAULT_mutationRate = 0.2,
          elitism = 0.2;     //the percentage of elite chromosomes

  int instance;
  double originalPoint[];
  double coordinates[][];
  double distanceMatrix[][];
  String instanceName = "";
  boolean applyLocalSearch = true;

  //Random Forest Parameters
  int BagSizePercent;
  String BatchSize;
  boolean BreakTiesRandomly;
  boolean CalcOutOfBag;
  boolean ComputeAttributeImportance;
  boolean Debug;
  boolean DoNotCheckCapabilities;
  int MaxDepth;
  int NumDecimalPlaces;
  int NumExecutionSlots;
  int NumFeatures;
  int NumIterations;
  boolean OutputOutOfBagComplexityStatistics;
  boolean PrintClassifiers;
  int Seed;
  boolean StoreOutOfBagPredictions;

  /**
   * The method is to modify the default value.
   */
  public void setParameter(double crossoverRate, double mutationRate, int counter, double elitism, int generation,
          int length, String instanceName, int piTotal, int machineTotal, int[] fristProfit, int[] di, double[] wi, int[][] processingTime) {
    this.DEFAULT_crossoverRate = crossoverRate;
    this.DEFAULT_mutationRate = mutationRate;
    this.counter = counter;
    this.elitism = elitism;
    this.DEFAULT_generations = generation;
    this.instanceName = instanceName;
    this.length = length;

    this.piTotal = piTotal;
    this.machineTotal = machineTotal;
    this.fristProfit = fristProfit;
    this.di = di;
    this.wi = wi;
    this.processingTime = processingTime;
  }

  public void setWekaRF(int newBagSizePercent, String size, boolean newBreakTiesRandomly, boolean calcOutOfBag, boolean computeAttributeImportance, 
          boolean debug,boolean doNotCheckCapabilities, int value, int num, int numSlots, int newNumFeatures, int numIterations, boolean b, boolean print, 
          int s, boolean storeOutOfBag) {
    this.BagSizePercent = newBagSizePercent;
    this.BatchSize = size;
    this.BreakTiesRandomly = newBreakTiesRandomly;
    this.CalcOutOfBag = calcOutOfBag;
    this.ComputeAttributeImportance = computeAttributeImportance;
    this.Debug = debug;
    this.DoNotCheckCapabilities = doNotCheckCapabilities;
    this.MaxDepth = value;
    this.NumDecimalPlaces = num;
    this.NumExecutionSlots = numSlots;
    this.NumFeatures = newNumFeatures;
    this.NumIterations = numIterations;
    this.OutputOutOfBagComplexityStatistics = b;
    this.PrintClassifiers = print;
    this.Seed = s;
    this.StoreOutOfBagPredictions = storeOutOfBag;
  }

  public void initiateVars() throws IOException {
    GaMain = new singleThreadGA_Weka();//singleThreadGAwithMultipleCrossover singleThreadGA adaptiveGA
    Population = new population();
    Selection = new binaryTournamentMaximization();//binaryTournamentMaximization
    Crossover = new twoPointCrossover2_Weka(); //twoPointCrossover2()  CyclingCrossoverP multiParentsCrossover()
//    Crossover2 = new PMX();
    Mutation = new swapMutation_Weka();//shiftMutation
//    Mutation2 = new shiftMutation();//inverseMutation
    ObjectiveFunction = new ObjFunctionPFSSOAWT_PSDI[numberOfObjs];
    ObjectiveFunction[0] = new ObjectiveFunctionMakespanFlowShop_OAS();//the first objective
    Fitness = new singleObjectiveFitness();//singleObjectiveFitness singleObjectiveFitnessByNormalize
    objectiveMinimization = new boolean[numberOfObjs];
    objectiveMinimization[0] = false;
    encodeType = true;
    ObjectiveFunction[0].setOASData(this.piTotal, this.machineTotal, this.fristProfit, this.di, this.wi, this.processingTime);

    //set the data to the GA main program.
    GaMain.setData(Population, Selection, Crossover, Mutation, ObjectiveFunction,
            Fitness, DEFAULT_generations, DEFAULT_initPopSize, DEFAULT_PopSize,
            length, DEFAULT_crossoverRate, DEFAULT_mutationRate, objectiveMinimization,
            numberOfObjs, encodeType, elitism);
    GaMain.setSecondaryCrossoverOperator(Crossover2, false);
    GaMain.setSecondaryMutationOperator(Mutation2, true);
    GaMain.setWekaRF(BagSizePercent,BatchSize,BreakTiesRandomly,CalcOutOfBag,ComputeAttributeImportance,
                    Debug,DoNotCheckCapabilities,MaxDepth,NumDecimalPlaces,NumExecutionSlots,
                    NumFeatures,NumIterations,OutputOutOfBagComplexityStatistics,PrintClassifiers,Seed,StoreOutOfBagPredictions);
  }

  public void start() throws ParseException {
    openga.util.timeClock timeClock1 = new openga.util.timeClock();
    timeClock1.start();
    GaMain.startGA();
    timeClock1.end();
    /*
    for(int k = 0 ; k < DEFAULT_PopSize ; k ++ ){
      System.out.print(k+": "+GaMain.getPopulation().getSingleChromosome(k).toString2()+"\t"+GaMain.getPopulation().getSingleChromosome(k).getObjValue()[0]+"\t"+GaMain.getPopulation().getSingleChromosome(k).getFitnessValue()+"\n");
    }

    for(int k = 0 ; k < GaMain.getArchieve().getPopulationSize() ; k ++ ){
      System.out.print(k+": "+GaMain.getArchieve().getSingleChromosome(k).toString2()+"\t"+GaMain.getArchieve().getSingleChromosome(k).getObjValue()[0]+"\t"+GaMain.getArchieve().getSingleChromosome(k).getFitnessValue()+"\n");
    }
     */
    String implementResult = instanceName + "\t" + DEFAULT_crossoverRate + "\t" + DEFAULT_mutationRate + "\t"
            + elitism + "\t" + BagSizePercent + "\t" + BatchSize + "\t" + MaxDepth + "\t" + NumDecimalPlaces + "\t"
            + NumExecutionSlots + "\t" + NumFeatures + "\t" + NumIterations + "\t" + Seed + "\t"
            + GaMain.getArchieve().getSingleChromosome(0).getObjValue()[0]
            + "\t " + timeClock1.getExecutionTime() / 1000.0 + "\n";
//    writeFile("flowshopOASRF", implementResult);
    System.out.print(implementResult);
  }

  /**
   * Write the data into text file.
   */
  void writeFile(String fileName, String _result) {
    fileWrite1 writeLotteryResult = new fileWrite1();
    writeLotteryResult.writeToFile(_result, fileName + ".txt");
    writeLotteryResult.run();
//    Thread thread1 = new Thread(writeLotteryResult);
//    thread1.run();
  }

  public static void main(String[] args) throws IOException, ParseException {

    flowshopOAS_RF flowshop_1 = new flowshopOAS_RF();
    double crossoverRate[], mutationRate[];
    crossoverRate = new double[]{0.9};  //0.1,0.5,0.9 //0.5,0.9
    mutationRate = new double[]{0.9};   //0.1,0.5,0.9 //0.1, 0.5
    int counter = 0;
    double elitism[] = new double[]{0.2};
    int generations[] = new int[]{1000};
    int[] orders = new int[]{10, 30};//10, 30, 50, 100, 200
    int[] numberOfMachines = new int[]{3 , 5, 10};//3 , 5, 10
    int Repeat = 6; //30

    String[] BatchSize = new String[]{"50"};  //?‰¹??? "20","50","100" //100 
    int[] BagSizePercent = new int[]{100};     //è¨“ç·´??†å¤§å°ç?„ç™¾??†æ?? 50,75,100   //BagSizePercent   //100
    int[] MaxDepth = new int[]{2};            //æ·±åº¦ 0,2,6    //2
    int[] NumDecimalPlaces = new int[]{3};    //å°æ•¸ä½æ•¸3
    int[] NumExecutionSlots = new int[]{1}; //ä¸è?èª¿ 1 //0
    int[] NumFeatures = new int[]{0};   //ä¸è?èª¿ 0
    int[] NumIterations = new int[]{100};   //100,150,200   //100
    int[] Seed = new int[]{1};  //ä¸è?èª¿1  
    boolean newBreakTiesRandomly = false;
    boolean calcOutOfBag = false;
    boolean computeAttributeImportance = false;
    boolean debug = false;
    boolean doNotCheckCapabilities = false;
    boolean b = false;
    boolean print = false;
    boolean storeOutOfBag = false;
    
    for (int i = 0; i < orders.length; i++) {
      for (int j = 0; j < numberOfMachines.length; j++) {
        for (int k = 0; k < 1; k++) { //10

          openga.applications.data.readPFSSOAWT_flowshop fs = new openga.applications.data.readPFSSOAWT_flowshop();
          String instancePath = "@..\\..\\instances\\PFSS-OAWT-Data\\p\\";
          String instanceName = "p" + orders[i] + "x" + numberOfMachines[j] + "_" + k + ".txt";
          //System.out.println(instanceName);
          fs.setData(instancePath, instanceName);
          fs.readfile();

          int length = fs.getPiTotal();

          for (int m = 0; m < crossoverRate.length; m++) {
            for (int n = 0; n < mutationRate.length; n++) {
              for (int o = 0; o < elitism.length; o++) {
                for (int BSPCount = 0; BSPCount < BagSizePercent.length; BSPCount++) {
                  for (int BSCount = 0; BSCount < BatchSize.length; BSCount++) {
                    for (int MDCount = 0; MDCount < MaxDepth.length; MDCount++) {
                      for (int NDPCount = 0; NDPCount < NumDecimalPlaces.length; NDPCount++) {
                        for (int NESCount = 0; NESCount < NumExecutionSlots.length; NESCount++) {
                          for (int NFCount = 0; NFCount < NumFeatures.length; NFCount++) {
                            for (int NICount = 0; NICount < NumIterations.length; NICount++) {
                              for (int SCount = 0; SCount < Seed.length; SCount++) {
                                for (int p = 0; p < Repeat; p++) {
                                  //System.out.println(counter);
                                  flowshop_1.setParameter(crossoverRate[m], mutationRate[n], counter, elitism[o], generations[0], length, fs.getfileName(),
                                          fs.getPiTotal(), fs.getMachineTotal(), fs.getprofit(), fs.getdi(), fs.getwi(), fs.getprocessingTime());
                                  flowshop_1.setWekaRF(BagSizePercent[BSPCount], BatchSize[BSCount], newBreakTiesRandomly, calcOutOfBag, computeAttributeImportance,
                                          debug, doNotCheckCapabilities, MaxDepth[MDCount], NumDecimalPlaces[NDPCount], NumExecutionSlots[NESCount],
                                          NumFeatures[NFCount],NumIterations[NICount], b, print, Seed[SCount], storeOutOfBag);
                                  flowshop_1.initiateVars();
                                  flowshop_1.start();
                                  counter++;
                                }//Repeat
                              }
                            }
                          }
                        }
                      }
                    }
                  }
                }//BSP
              }
            }
          }//end for
        }
      }
    }
  }
}
