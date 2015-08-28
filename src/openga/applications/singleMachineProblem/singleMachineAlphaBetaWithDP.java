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
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class singleMachineAlphaBetaWithDP extends singleMachineWithHeu1 {
  public singleMachineAlphaBetaWithDP() {
  }
  double alpha[];
  double beta[];

  public void setAlphaBeta(double alpha[], double beta[]){
    this.alpha = alpha;
    this.beta = beta;
  }

  cloneI clone1;

  public void initiateVars(){
    GaMain     = new singleThreadGAwithInitialPop();//singleThreadGAwithSecondFront singleThreadGAwithMultipleCrossover
    Population = new population();
    Selection  = new varySizeTournament();
    Crossover  = new twoPointCrossover2();
    Crossover2 = new PMX();
    Mutation   = new swapMutation();
    Mutation2  = new inverseMutation();
    ObjectiveFunction = new ObjectiveFunctionScheduleI[numberOfObjs];
    ObjectiveFunction[0] = new ObjectiveEarlinessTardinessPenalty();//the first objective, tardiness, ObjectiveTardiness ObjectiveEarlinessTardinessPenalty
    //ObjectiveFunction[1] = new ObjectiveTardinessForFlowShop();//the second one.
    Fitness    = new singleObjectiveFitness();
    objectiveMinimization = new boolean[numberOfObjs];
    objectiveMinimization[0] = true;
    //objectiveMinimization[1] = true;
    encodeType = true;
    ((alphaBetaI)ObjectiveFunction[0]).setAlphaBeta(alpha, beta);
    clone1 = new solutionVectorCloneWithMutation();

    //set schedule data to the objectives
    ObjectiveFunction[0].setScheduleData(dueDay, processingTime, numberOfMachines);

    //set the data to the GA main program.
    GaMain.setData(Population, Selection, Crossover, Mutation, ObjectiveFunction, Fitness, DEFAULT_generations, DEFAULT_initPopSize,DEFAULT_PopSize,
                   numberOfJob, DEFAULT_crossoverRate, DEFAULT_mutationRate, objectiveMinimization, numberOfObjs, encodeType, elitism);
    GaMain.setSecondaryCrossoverOperator(Crossover2, true);
    GaMain.setSecondaryMutationOperator(Mutation2, true);
  }

  public void start(){
    ((alphaBetaI)ObjectiveFunction[0]).setAlphaBeta(alpha, beta);
    openga.util.timeClock timeClock1 = new openga.util.timeClock();
    GaMain.initialStage();
    timeClock1.start();
    GaMain.setCloneOperatpr(clone1, true);
    constructInitialSolutions(Population);
    GaMain.startGA();
    timeClock1.end();
    //System.out.println("\nThe final result");
    int bestInd = getBestSolnIndex(GaMain.getArchieve());
    String implementationResult = "";
    implementationResult += fileName+"\t"+numberOfJob+"\t" + GaMain.getArchieve().getSingleChromosome(bestInd).getObjValue()[0]+"\t"+timeClock1.getExecutionTime()/1000.0+"\n";
    writeFile("singleMachineAlphaBetaWithDP_20060531", implementationResult);
    System.out.print(implementationResult);
  }

  public void constructInitialSolutions(populationI _Population){
    for(int i = 0 ; i < DEFAULT_PopSize ; i ++ ){
      int sequence[] = new int[numberOfJob];
      for(int j = 0 ; j < numberOfJob ; j ++ ){
        sequence[j] = j;
      }

      homework.schedule.singleMachineEaryTardyPenalty singleMachine1 = new homework.schedule.singleMachineEaryTardyPenalty();
      singleMachine1.setData(numberOfJob, dueDay, processingTime, sequence);
      singleMachine1.setAlphaBeta(alpha, beta);
      singleMachine1.generateInitialSolution(i);
      singleMachine1.startAlgorithm();
      _Population.getSingleChromosome(i).setSolution(singleMachine1.getSolution());
    }
  }

  public static void main(String[] args) {
    singleMachineAlphaBetaWithDP singleMachine1 = new singleMachineAlphaBetaWithDP();
    System.out.println("singleMachineAlphaBetaWithDP_20060531");
    //openga.applications.data.singleMachine singleMachineData = new openga.applications.data.singleMachine();
    int jobSets[] = new int[]{100, 200};//20, 30, 40, 50, 100, 200
    int counter = 0;

    for(int i = 0 ; i < 30 ; i ++ ){
      for(int m = 0 ; m < jobSets.length ; m ++ ){//jobSets.length
        for(int k = 3 ; k < 4 ; k ++ ){
          System.out.println("Combinations: "+counter);
          openga.applications.data.singleMachine readSingleMachineData1 = new openga.applications.data.singleMachine();
          int numberOfJobs = jobSets[m];
          String fileName = readSingleMachineData1.getFileName(numberOfJobs, k);
          fileName = "bky"+numberOfJobs+"_1";
          System.out.print(fileName+"\t");
          readSingleMachineData1.setData("sks/"+fileName+".txt");
          readSingleMachineData1.getDataFromFile();
          int dueDate[] = readSingleMachineData1.getDueDate();
          int processingTime[] = readSingleMachineData1.getPtime();
          double alpha[] = readSingleMachineData1.getAlpha();
          double beta[] = readSingleMachineData1.getBeta();

          singleMachine1.setData(numberOfJobs, dueDate, processingTime, fileName);
          singleMachine1.setAlphaBeta(alpha, beta);
          singleMachine1.initiateVars();
          singleMachine1.start();
          counter ++;
        }
      }
    }
  }

}