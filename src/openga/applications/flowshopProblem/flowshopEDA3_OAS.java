package openga.applications.flowshopProblem;

//import java.io.File;
//import java.io.IOException;
//import openga.chromosomes.*;
//import openga.operator.selection.*;
//import openga.operator.crossover.*;
//import openga.operator.mutation.*;
//import openga.ObjectiveFunctions.*;
//import openga.MainProgram.*;
//import openga.Fitness.*;
//import openga.util.printClass;
//import openga.util.fileWrite1;
//import openga.applications.data.readPFSSOAWT_flowshop;
import openga.applications.Continuous.*;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import openga.applications.*;
import openga.chromosomes.*;
import openga.operator.selection.*;
import openga.operator.crossover.*;
import openga.operator.mutation.*;
import openga.ObjectiveFunctions.*;
import openga.MainProgram.*;
import openga.ObjectiveFunctions.*;
import openga.Fitness.*;
import static openga.applications.flowshopProblem.flowshopEDA3.writeFile;
import openga.operator.miningGene.PBILInteractiveWithEDA3I;
//import openga.util.printClass;
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
public class flowshopEDA3_OAS {

  public flowshopEDA3_OAS() {
  }
  /**
   * *
   * Basic variables of GAs.
   */
  int numberOfObjs = 1;
  populationI Population;
  SelectI Selection;
  CrossoverI Crossover2;
  MutationI Mutation2;
  ObjFunctionPFSSOAWT_PSDI[] ObjectiveFunction;
  FitnessI Fitness;

  double lamda = 0.9; //learning rate
  double beta = 0.9;
  int numberOfCrossoverTournament = 2;
  int numberOfMutationTournament = 2;
  int startingGenDividen = 4;
  String strFileName = "";
  
  public PBILInteractiveWithEDA3I GaMain;
  public int D1;
  public int D2;
  int epoch;
  public boolean OptMin;
  EDA3CrossoverI Crossover;
  EDA3MutationI Mutation;
  
  /**
   * Parameters of the GA
   */
  int generations, length, initPopSize, fixPopSize;
  double crossoverRate, mutationRate;
  boolean[] objectiveMinimization; //true is minimum problem.
  boolean encodeType;  //binary of realize code
  int seed = 12345;
  int counter = 0;

//  Instance Data
  int piTotal;
  int machineTotal;
  int[] fristProfit;    //  revenue of order
  int[] di;        //  due-date
  double[] wi;     //  tardiness penalty weight
  int[][] processingTime;

  //Results
  double bestObjectiveValues[];
  populationI solutions;

  public int
            DEFAULT_generations = 1000,
            DEFAULT_PopSize     = 200,
            DEFAULT_initPopSize = 200;
//            totalSolnsToExamine = 30000;//to fix the total number of solutions to examine. 100000

  public double DEFAULT_crossoverRate = 0.9,
          DEFAULT_mutationRate = 0.2,
          elitism = 0.2;     //the percentage of elite chromosomes

  int instance;
  double originalPoint[];
  double coordinates[][];
  double distanceMatrix[][];
  String instanceName = "";
  boolean applyLocalSearch = true;
  
//  int processingTime[][];
  int numberOfJob = 40;
  int numberOfMachines = 3;

  /**
   * The method is to modify the default value.
   */
  public void setParameter(double crossoverRate, double mutationRate, int counter, double elitism, int generation,
          int length, String instanceName, int piTotal, int machineTotal, int[] fristProfit, int[] di, double[] wi, int[][] processingTime
          ,int DEFAULT_PopSize) {
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
    
    this.DEFAULT_PopSize = DEFAULT_PopSize;
//    this.totalSolnsToExamine = totalSolnsToExamine;
  }
  
  //Single thread
  public void setEDAinfo(double lamda, double beta, int numberOfCrossoverTournament, int numberOfMutationTournament, int startingGenDividen , int D1 , int D2 , boolean OptMin , double DEFAULT_crossoverRate , double DEFAULT_mutationRate , int epoch , String strFileName) {
      this.lamda = lamda;
      this.beta = beta;
      this.numberOfCrossoverTournament = numberOfCrossoverTournament;
      this.numberOfMutationTournament = numberOfMutationTournament;
      this.startingGenDividen = startingGenDividen;
      this.D1 = D1;
      this.D2 = D2;
      this.OptMin = OptMin;
      this.DEFAULT_crossoverRate = DEFAULT_crossoverRate;
      this.DEFAULT_mutationRate = DEFAULT_mutationRate;
      this.epoch = epoch;
      this.strFileName = strFileName;
  }
  
  public void setFlowShopData(int numberOfJob, int numberOfMachines){
        this.numberOfJob = numberOfJob;
        this.numberOfMachines = numberOfMachines;
    }

  public void initiateVars(){
    GaMain = new singleThreadGAwithEDA3();//singleThreadGAwithMultipleCrossover singleThreadGA adaptiveGA
    Population = new population();
    Selection = new binaryTournament();
    Crossover = new twoPointCrossover2EDA3(); //twoPointCrossover2()  CyclingCrossoverP multiParentsCrossover()
//    Crossover2 = new PMX();
    Mutation = new swapMutationEDA3();//shiftMutation
//    Mutation2 = new shiftMutation();//inverseMutation
    ObjectiveFunction = new ObjFunctionPFSSOAWT_PSDI[numberOfObjs];
    ObjectiveFunction[0] = new ObjectiveFunctionMakespanFlowShop_OAS();//the first objective
    Fitness = new singleObjectiveFitness();//singleObjectiveFitness singleObjectiveFitnessByNormalize
    objectiveMinimization = new boolean[numberOfObjs];
    objectiveMinimization[0] = false;
    encodeType = true;
    
    ObjectiveFunction[0].setOASData(this.piTotal, this.machineTotal, this.fristProfit, this.di, this.wi, this.processingTime);
    Crossover.setEDAinfo(D1, D2);
    Mutation.setEDAinfo(D1, D2);

    //set the data to the GA main program.
    GaMain.setData(Population, Selection, Crossover, Mutation, ObjectiveFunction,
            Fitness, DEFAULT_generations, DEFAULT_initPopSize, DEFAULT_PopSize,
            length, DEFAULT_crossoverRate, DEFAULT_mutationRate, objectiveMinimization,
            numberOfObjs, encodeType, elitism);
    GaMain.setSecondaryCrossoverOperator(Crossover2, false);
    GaMain.setSecondaryMutationOperator(Mutation2, false);
    
    
    GaMain.setEDAinfo(lamda, beta, numberOfCrossoverTournament, 
            numberOfMutationTournament, startingGenDividen 
            , D1 , D2 , OptMin , epoch);  //startingGenDividen here is as interval of EDA
  }

  public void start(){
    openga.util.timeClock timeClock1 = new openga.util.timeClock();
    timeClock1.start();
//    System.out.println(Population.getPopulationSize());
//    constructInitialSolutions(Population); //ct
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
    
    
    //to output the implementation result.
    String implementResult = "";
    int bestInd = getBestSolnIndex(GaMain.getArchieve());
    
//    implementResult = strFileName + "\t" + lamda + "\t" + beta + "\t" + numberOfCrossoverTournament 
//                + "\t" + numberOfMutationTournament + "\t" + startingGenDividen + "\t" 
//                + D1 + "\t" + D2 + "\t" + OptMin + "\t" + epoch + "\t" 
//                + DEFAULT_crossoverRate + "\t" + DEFAULT_mutationRate + "\t" 
//                + GaMain.getArchieve().getSingleChromosome(bestInd).getObjValue()[0] + "\t" + timeClock1.getExecutionTime() / 1000.0  +"\n";
        
    implementResult = strFileName + "\t" + lamda + "\t" + beta + "\t" + numberOfCrossoverTournament 
                + "\t" + numberOfMutationTournament + "\t" + startingGenDividen + "\t" 
                + D1 + "\t" + D2 + "\t" + OptMin + "\t" + epoch + "\t" 
                + DEFAULT_crossoverRate + "\t" + DEFAULT_mutationRate + "\t"
            + elitism + "\t" + GaMain.getArchieve().getSingleChromosome(bestInd).getObjValue()[0]
            + "\t " + timeClock1.getExecutionTime() / 1000.0 + "\n";
    
    writeFile("EDA3_MakeSpanForFlowShop_OAS", implementResult);
    System.out.print(implementResult);
  }
  
  public int getBestSolnIndex(populationI arch1) {
        int index = 0;
        double bestobj = 0;
        for (int k = 0; k < GaMain.getArchieve().getPopulationSize(); k++) {
            //System.out.println(GaMain.getArchieve().getObjectiveValues(k)[0]);
            if (bestobj < GaMain.getArchieve().getObjectiveValues(k)[0]) {
                bestobj = GaMain.getArchieve().getObjectiveValues(k)[0];
                index = k;
            }
        }
        //System.out.println("bestindex: "+index);
        return index;
    }
  
  public static void main(String[] args){
        
    //EDA parameters.
    double lamdalearningrate[] = new double[]{0.9};//0.5,0.9
    double betalearningrate[] = new double[]{0.5};//0.1,0.5
    int numberOfCrossoverTournament[] = new int[]{4};//2,4
    int numberOfMutationTournament[] = new int[]{1};//1,2
    int startingGenDividen[] = new int[]{4};//4,7

    int[] D1 = new int[]{3};//1,2,3,4
    int[] D2 = new int[]{0};//0,1,2
    int[] epoch = new int[]{6};//1,3,4,6,7,8
    boolean optMin = false;
    
    double crossoverRate[] = new double[]{0.9};//0.5,0.9
    double mutationRate[] = new double[]{0.9};//0.9
    double elitism[] = new double[]{0.2};
    int generations[] = new int[]{1000};
    int counter = 0;

//    int[] orders = new int[]{10, 30, 50, 100, 200};
//    int[] numberOfMachines = new int[]{3, 5, 10};
    int[] orders = new int[]{50};
    int[] numberOfMachines = new int[]{10};
    //implication of instanceReplication
    int startInstance = 0;
    int endInstance = 0;
    int instanceReplications = 2;
    
    flowshopEDA3_OAS flowshop_OAS = new flowshopEDA3_OAS();
    for(int ordersCount = 0 ; ordersCount < orders.length ; ordersCount++){
      for(int numberOfMachinesCount = 0 ; numberOfMachinesCount < numberOfMachines.length ; numberOfMachinesCount++){
        for(int InstanceCount = startInstance ; InstanceCount <= endInstance ; InstanceCount++){
          
          openga.applications.data.readPFSSOAWT_flowshop fs = new openga.applications.data.readPFSSOAWT_flowshop();
          String data = "instances\\PFSS-OAWT-Data\\p\\";
          String strFileName = "p" + orders[ordersCount] + "x" + numberOfMachines[numberOfMachinesCount] + "_" +  InstanceCount + ".txt"; 
          fs.setData(data, strFileName);
          fs.readfile();
          
          for(int lamdaCount = 0 ; lamdaCount < lamdalearningrate.length ; lamdaCount++){
            for(int betaCount = 0 ; betaCount < betalearningrate.length ; betaCount++){
              for(int TCCount = 0 ; TCCount < numberOfCrossoverTournament.length ; TCCount++){
                for(int TMCount = 0 ; TMCount < numberOfMutationTournament.length ; TMCount++){
                  for(int startingGDCount = 0 ; startingGDCount < startingGenDividen.length ; startingGDCount++){
                    for(int D1Count = 0 ; D1Count < D1.length ; D1Count++){
                      for(int D2Count = 0 ; D2Count < D2.length ; D2Count++){
                        for(int epochCount = 0 ; epochCount < epoch.length ; epochCount++){
                          for(int CRCount = 0 ; CRCount < crossoverRate.length ; CRCount++){
                            for(int MRCount = 0 ; MRCount < mutationRate.length ; MRCount++){
                              for(int elitismCount = 0 ; elitismCount < elitism.length ; elitismCount++){
                                for(int generationsCount = 0 ; generationsCount < generations.length ; generationsCount++){
                                  for(int ReplicationsCount = 0 ; ReplicationsCount < instanceReplications ; ReplicationsCount ++){
                                    
//                                    System.out.println(strFileName);
//                                    generations = (100 * jobs[j]); //(numberOfJob * 100)
                                    int popSize = 100; //(numberOfJob * 10)
//                                    int totalSolnsToExamine = 100000; //(orders[ordersCount] * orders[ordersCount] * 1000)
                                    int length = fs.getPiTotal();
                                    
                                    flowshop_OAS.setFlowShopData(orders[ordersCount], numberOfMachines[numberOfMachinesCount]);
                                    
                                    flowshop_OAS.setParameter(crossoverRate[CRCount], mutationRate[MRCount], counter, elitism[elitismCount], 
                                            generations[generationsCount], length, fs.getfileName(),
                                            fs.getPiTotal(), fs.getMachineTotal(), fs.getprofit(), fs.getdi(), fs.getwi(), fs.getprocessingTime(),
                                            popSize);
                                    
                                    flowshop_OAS.setEDAinfo(lamdalearningrate[lamdaCount], betalearningrate[betaCount], numberOfCrossoverTournament[TCCount], 
                                                          numberOfMutationTournament[TMCount], startingGenDividen[startingGDCount] , D1[D1Count], D2[D2Count] , optMin , 
                                                          crossoverRate[CRCount] , mutationRate[MRCount], epoch[epochCount] , strFileName);

                                    flowshop_OAS.initiateVars();
                                    flowshop_OAS.start();
                                    counter++;
                                    
                                  }
                                }
                              }//End elitismCount
                            }
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}
