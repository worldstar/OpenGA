package openga.applications.singleMachineProblem;
import openga.applications.singleMachine;
import openga.chromosomes.*;
import openga.operator.selection.*;
import openga.operator.crossover.*;
import openga.operator.mutation.*;
import openga.ObjectiveFunctions.*;
import openga.MainProgram.*;
import openga.ObjectiveFunctions.*;
import openga.Fitness.*;
import openga.util.printClass;
import openga.util.fileWrite1;
import openga.operator.clone.*;

/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: Single machine scheduling problem with setup.</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class singleMachineSetup extends singleMachine {
  public singleMachineSetup() {
  }
  public ObjectiveFunctionSchedule2I ObjectiveFunction[];
  public double processingTime[][];
  public int numberOfJobs;
  public String fileName;

  public int totalSolnsToExamine = 100000;

  public void setData(int numberOfJobs, double processingTime[][], String fileName){
    this.numberOfJob = numberOfJobs;
    this.processingTime = processingTime;
    this.fileName = fileName;
  }

  public void initiateVars(){
    GaMain     = new singleThreadGA();//singleThreadGAwithSecondFront singleThreadGAwithMultipleCrossover
    Population = new population();
    Selection  = new binaryTournament();
    Crossover  = new twoPointCrossover2();
    Crossover2 = new PMX();
    Mutation   = new swapMutation();
    Mutation2  = new inverseMutation();
    ObjectiveFunction = new ObjectiveFunctionSchedule2I[numberOfObjs];
    ObjectiveFunction[0] = new ObjectiveEiTiSetupCommonDueForSingleMachine();
    //ObjectiveFunction[1] = new ObjectiveTardinessForFlowShop();//the second one.
    Fitness    = new singleObjectiveFitness();
    objectiveMinimization = new boolean[numberOfObjs];
    objectiveMinimization[0] = true;
    //objectiveMinimization[1] = true;
    encodeType = true;

    //set schedule data to the objectives
    ObjectiveFunction[0].setScheduleData(processingTime, numberOfMachines);
    DEFAULT_generations = totalSolnsToExamine/DEFAULT_PopSize;


    //set the data to the GA main program.
    GaMain.setData(Population, Selection, Crossover, Mutation, ObjectiveFunction, Fitness, DEFAULT_generations, DEFAULT_initPopSize,DEFAULT_PopSize,
                   numberOfJob, DEFAULT_crossoverRate, DEFAULT_mutationRate, objectiveMinimization, numberOfObjs, encodeType, elitism);
    GaMain.setSecondaryCrossoverOperator(Crossover2, true);
    GaMain.setSecondaryMutationOperator(Mutation2, true);
  }

  public void startMain(){
    openga.util.timeClock timeClock1 = new openga.util.timeClock();
    timeClock1.start();
    GaMain.startGA();
    timeClock1.end();
    //System.out.println("\nThe final result");

    int bestInd = getBestSolnIndex(GaMain.getArchieve());
    String implementationResult = "";
    implementationResult += fileName+"\t"+numberOfJob+"\t"+DEFAULT_crossoverRate+"\t"+DEFAULT_mutationRate+"\t"+DEFAULT_PopSize+"\t" + GaMain.getArchieve().getSingleChromosome(bestInd).getObjValue()[0]+"\t"+timeClock1.getExecutionTime()/1000.0+"\n";
    writeFile("SingleMachineSetupDOE_20070530", implementationResult);
    System.out.println(implementationResult);
  }

  public static void main(String[] args) {
    System.out.println("SingleMachineSetupDOE_20070530");

    int counter = 0;
    int repeatExperiments = 30;
    int jobSets[] = new int[]{10, 15, 20, 25, 50};//10, 15, 20, 25, 50, 100, 150, 200
    String type[] = new String[]{"low", "med", "high"};//"low", "med", "high"

    double crossoverRate[] = new double[]{0.5},//0.8, 0.5 (0.5)
           mutationRate[] = new double[]{0.6};//0.6, 0.3  (0.6)
    int populationSize[] = new int[]{100};//200, 100 (100)

    
      for(int m = 0 ; m < jobSets.length ; m ++ ){//jobSets.length
        for(int j = 0 ; j < type.length ; j ++ ){
          for(int k = 1 ; k <= 15 ; k ++ ){
            for(int n = 0 ; n < crossoverRate.length ; n ++ ){
              for(int p = 0 ; p < mutationRate.length ; p ++ ){
                for(int q = 0 ; q < populationSize.length ; q ++ ){
                  System.out.println("Combinations: "+counter);
                  singleMachineSetup singleMachine1 = new singleMachineSetup();
                  openga.applications.data.singleMachineSetupData singleMachineData1 = new openga.applications.data.singleMachineSetupData();
                  String fileName = "instances\\SingleMachineSetup\\"+type[j]+"\\"+jobSets[m]+"_"+k+".etp";
                  //fileName = "Data\\SMSetupTime8.txt";//for test
                  singleMachineData1.setData(fileName);
                  singleMachineData1.getDataFromFile();
                  int numberOfJobs = singleMachineData1.getSize();
                  double processingTime[][] = singleMachineData1.getProcessingTime();
                  for(int i = 0 ; i < repeatExperiments ; i ++ ){
                       singleMachine1.setData(numberOfJobs, processingTime, fileName);
                       singleMachine1.setParameters(populationSize[q], crossoverRate[n], mutationRate[p], 1);
                       singleMachine1.initiateVars();
                       singleMachine1.startMain();
                       counter ++;
                  }
              }
            }
          }
        }
      }
    }
  }
}
