package openga.applications.parallelMachineProblem;
import openga.applications.data.readParallelMachineSetupData;
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

/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class parallelMachineSetupGA extends singleMachine{
  public parallelMachineSetupGA() {
  }

  String type = "Balanced";//Balanced, DominantProcessing, DominantSetupTime
  String fileName = "", message = "";
  int numberOfMachines = 2;
  int numberOfJob = 20;
  int processingSetupTime[][][];

  //ObjectiveFunctionSetupScheduleI
  public ObjectiveFunctionSetupScheduleI ObjectiveFunction[];

  public void setData(String type, int numberOfJob, int numberOfMachines, int processingSetupTime[][][], String fileName){
    this.type = type;
    this.numberOfJob = numberOfJob;
    this.numberOfMachines = numberOfMachines;
    this.processingSetupTime = processingSetupTime;
    this.fileName = fileName;
  }

  public void initiateVars(){
    GaMain     = new singleThreadGA();//singleThreadGAwithSecondFront singleThreadGAwithMultipleCrossover
    Population = new population();
    Selection  = new binaryTournament();
    Crossover  = new twoPointCrossover2();
    Crossover2 = new PMX();
    Mutation   = new swapMutation();//shiftMutation swapMutation swapMutation2Threads
    Mutation2  = new inverseMutation();
    ObjectiveFunction = new ObjectiveFunctionSetupScheduleI[numberOfObjs];
    ObjectiveFunction[0] = new ObjectiveMakeSpanParallelWithSetup();//the first objective, tardiness, ObjectiveTardiness ObjectiveEarlinessTardinessPenalty
    Fitness    = new singleObjectiveFitness();
    objectiveMinimization = new boolean[numberOfObjs];
    objectiveMinimization[0] = true;
    //objectiveMinimization[1] = true;
    encodeType = true;

    //set schedule data to the objectives
    ObjectiveFunction[0].setScheduleData(processingSetupTime, numberOfMachines);

    //set the data to the GA main program.
    GaMain.setData(Population, Selection, Crossover, Mutation, ObjectiveFunction, Fitness, DEFAULT_generations, DEFAULT_initPopSize,DEFAULT_PopSize,
                   numberOfJob, DEFAULT_crossoverRate, DEFAULT_mutationRate, objectiveMinimization, numberOfObjs, encodeType, elitism);
    GaMain.setSecondaryCrossoverOperator(Crossover2, false);
    GaMain.setSecondaryMutationOperator(Mutation2, false);
  }

  public void startMain(){
    openga.util.timeClock timeClock1 = new openga.util.timeClock();
    timeClock1.start();
    GaMain.startGA();
    timeClock1.end();
    //System.out.println("\nThe final result");
    int bestInd = getBestSolnIndex(GaMain.getArchieve());
    String implementationResult = "";
    implementationResult += fileName+"\t"+numberOfJob+"\t"+DEFAULT_PopSize+"\t"+DEFAULT_crossoverRate+"\t"+ DEFAULT_mutationRate+"\t" + GaMain.getArchieve().getSingleChromosome(bestInd).getObjValue()[0]+"\t"+timeClock1.getExecutionTime()/1000.0+"\n";
    writeFile("ParallelMachineGA_20070517", implementationResult);
    System.out.println(implementationResult);
  }

  public static void main(String[] args) {
    String type[] = new String[]{"Balanced", "DominantProcessing", "DominantSetupTime"};//"Balanced", "DominantProcessing", "DominantSetupTime"
    int numberOfMachines[] = new int[]{2, 6, 12};//2, 6, 12
    int numberOfJobs[] = new int[]{20, 40, 60};//20, 40, 60, 80, 100, 120
    int processingTime[][];
    int counter = 0;
    int instanceReplications = 15;
    int totoalReplications = 2;

    int popSize[] = new int[]{100, 200};//50, 100, 155, 210
    double crossoverRate[] = new double[]{0.6, 0.9},//0.6, 0.9 {0.6}
           mutationRate [] = new double[]{0.1, 0.5},//0.1, 0.5 {0.5}
           elitism = 0.2;

    for(int m = 0 ; m < numberOfJobs.length ; m ++ ){
      for(int n = 0 ; n < numberOfMachines.length ; n ++ ){
        for(int p = 0 ; p < type.length ; p ++ ){
          for(int k = 1 ; k <= instanceReplications ; k ++ ){//instance replications
            readParallelMachineSetupData readParallelMachineSetupData1 = new readParallelMachineSetupData();
            String fileName = "instances\\ParallelMachineSetup\\"+type[p]+"\\"+numberOfMachines[n]+"machines\\"+numberOfJobs[m]+"on"+numberOfMachines[n]+"Rp50Rs50_"+k+".dat";
            //String fileName = "instances\\ParallelMachineSetup\\Balanced\\Balanced\\7on2Rp50Rs50_3Ameer.dat";//testing instance
            //System.out.println(fileName);
            readParallelMachineSetupData1.setData(type[p], fileName);
            readParallelMachineSetupData1.getDataFromFile();
            int processingSetupTime[][][] = readParallelMachineSetupData1.getProcessingSetupTime();

            for(int q = 0 ; q < popSize.length ; q ++ ){
              for(int r = 0 ; r < crossoverRate.length ; r ++ ){
                for(int t = 0 ; t < mutationRate.length ; t ++ ){
                  for(int replications = 0 ; replications < totoalReplications ; replications ++ ){
                  parallelMachineSetupGA parallelMachineSetupGA1 = new parallelMachineSetupGA();
                  parallelMachineSetupGA1.setData(type[p], numberOfJobs[m], numberOfMachines[n], processingSetupTime, fileName);
                  parallelMachineSetupGA1.setParameters(popSize[q], crossoverRate[r], mutationRate[t], numberOfJobs[m]*500);
                  parallelMachineSetupGA1.initiateVars();
                  parallelMachineSetupGA1.startMain();
                  counter ++;
                }
              }
            }
          }
        }
      }
    }

    }//end replications.

  }

}