package openga.applications.parallelMachineProblem;
import openga.applications.data.readParallelMachineSetupData;
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
import homework.schedule.parallelMachineSetup;

/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class parallelMachineSetupDP extends parallelMachineSetupGA {
  public parallelMachineSetupDP() {
  }
  double GADPCPUTIME = 0, DPCPUTIME = 0;

  public void initiateVars(){
    GaMain     = new singleThreadGAwithInitialPop();//singleThreadGAwithSecondFront singleThreadGAwithInitialPop
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
    openga.util.timeClock timeClock2 = new openga.util.timeClock();//for Dominance Properties
    timeClock1.start();
    GaMain.initialStage();
    timeClock2.start();
    constructInitialSolutions(Population);
    //To collect DP information
    timeClock2.end();
    DPCPUTIME = timeClock2.getExecutionTime()/1000.0;
    GaMain.ProcessObjectiveAndFitness();
    double DP_BestResult = GaMain.getPopulationBestObjValue();
    //GA starts
    GaMain.startGA();
    timeClock1.end();
    GADPCPUTIME = timeClock1.getExecutionTime()/1000.0;
    //System.out.println("\nThe final result");
    int bestInd = getBestSolnIndex(GaMain.getArchieve());
    String implementationResult = "";
    implementationResult += fileName+"\t"+numberOfJob+"\t" +DP_BestResult+"\t"+ DPCPUTIME+"\t"+ GaMain.getArchieve().getSingleChromosome(bestInd).getObjValue()[0]+"\t"+GaMain.getArchieve().getSingleChromosome(bestInd).toString1()+"\n";
    writeFile("ParallelMachineSetupDP_20070520", implementationResult);
    System.out.println(implementationResult);
  }

  public void constructInitialSolutions(populationI _Population){
    for(int i = 0 ; i < DEFAULT_PopSize ; i ++ ){
      int sequence[] = new int[numberOfJob];
      for(int j = 0 ; j < numberOfJob ; j ++ ){
        sequence[j] = j;
      }

      parallelMachineSetup parallelMachineSetup1 = new parallelMachineSetup();
      parallelMachineSetup1.setData(numberOfJob, numberOfMachines, processingSetupTime, sequence);
      parallelMachineSetup1.generateInitialSolution(1);
      parallelMachineSetup1.setIterations(2);
      parallelMachineSetup1.startAlgorithm();
      _Population.getSingleChromosome(i).setSolution(parallelMachineSetup1.getSolution());
    }
  }

  public static void main(String[] args) {
    String type[] = new String[]{"Balanced", "DominantProcessing", "DominantSetupTime"};//"Balanced", "DominantProcessing", "DominantSetupTime"
    int numberOfMachines[] = new int[]{2, 6, 12};//2, 6, 12
    int numberOfJobs[] = new int[]{20, 40, 60, 80};//20, 40, 60, 80, 100, 120
    int processingTime[][];
    int counter = 0;
    //int instanceReplications = 1;
    int startInstanceID = 1;
    int endStartInstanceID = 1;
    int totoalReplications = 30;

    int popSize[] = new int[]{100};//50, 100, 155, 210
    double crossoverRate[] = new double[]{0.6},//0.6, 0.9 {0.6}
           mutationRate [] = new double[]{0.5},//0.1, 0.5 {0.5}
           elitism = 0.2;

    for(int m = 0 ; m < numberOfJobs.length ; m ++ ){
      for(int n = 0 ; n < numberOfMachines.length ; n ++ ){
        for(int p = 0 ; p < type.length ; p ++ ){
          for(int k = startInstanceID ; k <= endStartInstanceID ; k ++ ){//instance replications
            readParallelMachineSetupData readParallelMachineSetupData1 = new readParallelMachineSetupData();

            //String fileName = "instances\\ParallelMachineSetup\\"+type[p]+"\\"+numberOfMachines[n]+"machines\\"+numberOfJobs[m]+"on"+numberOfMachines[n]+"Rp50Rs50_"+k+".dat";
            String fileName = "instances\\ParallelMachineSetup\\Balanced\\Balanced\\7on2Rp50Rs50_3Ameer.dat";//testing instance
            //numberOfJobs[m] = 7;
            //numberOfMachines[n] = 2;

            System.out.println(fileName);
            readParallelMachineSetupData1.setData(type[p], fileName);
            readParallelMachineSetupData1.getDataFromFile();
            int processingSetupTime[][][] = readParallelMachineSetupData1.getProcessingSetupTime();

            for(int replications = 0 ; replications < totoalReplications ; replications ++ ){
              parallelMachineSetupDP parallelMachineSetupDP1 = new parallelMachineSetupDP();
              parallelMachineSetupDP1.setData(type[p], numberOfJobs[m], numberOfMachines[n], processingSetupTime, fileName);
              parallelMachineSetupDP1.setParameters(popSize[0], crossoverRate[0], mutationRate[0], numberOfJobs[m]*2500);
              parallelMachineSetupDP1.initiateVars();
              parallelMachineSetupDP1.startMain();
              counter ++;
            }
        }
      }
    }

    }//end replications.

  }

}