package openga.applications.singleMachineProblem;
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
import openga.applications.singleMachine;
import openga.operator.clone.*;

/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class singleMachineBicriteriaWithDP extends singleMachineBicriteria {
  public singleMachineBicriteriaWithDP() {
  }
  cloneI clone1;
  int DEFAULT_initPopSize = 100;

  public void initiateVars(){
    GaMain     = new singleThreadGAwithInitialPop();
    Population = new population();
    Selection  = new binaryTournament();
    Crossover  = new twoPointCrossover2();
    //Crossover2 = new PMX();
    Mutation   = new swapMutation();
    //Mutation2  = new shiftMutation();
    ObjectiveFunction = new ObjectiveFunctionScheduleI[numberOfObjs];
    ObjectiveFunction[0] = new ObjectiveEarlinessForSingleMachine();//the first objective
    ObjectiveFunction[1] = new ObjectiveTardinessForSingleMachine();//the second one.
    Fitness    = new GoldbergFitnessAssignment();
    objectiveMinimization = new boolean[numberOfObjs];
    objectiveMinimization[0] = true;
    objectiveMinimization[1] = true;
    encodeType = true;

    //set schedule data to the objectives
    ObjectiveFunction[0].setScheduleData(dueDay, processingTime, numberOfMachines);
    ObjectiveFunction[1].setScheduleData(dueDay, processingTime, numberOfMachines);

    clone1 = new solutionVectorCloneWithMutation();
    //set the data to the GA main program.
    GaMain.setData(Population, Selection, Crossover, Mutation, ObjectiveFunction, Fitness, DEFAULT_generations -(DEFAULT_initPopSize/DEFAULT_PopSize -1), DEFAULT_initPopSize, DEFAULT_PopSize,
                   numberOfJob, DEFAULT_crossoverRate, DEFAULT_mutationRate, objectiveMinimization, numberOfObjs, encodeType, elitism);
    GaMain.setSecondaryCrossoverOperator(Crossover2, false);
    GaMain.setSecondaryMutationOperator(Mutation2, false);
  }

  public void start(){
    openga.util.timeClock timeClock1 = new openga.util.timeClock();
    GaMain.initialStage();
    timeClock1.start();
    GaMain.startGA();
    constructInitialSolutions(Population);
    timeClock1.end();
    //System.out.println("\nThe final result");
    printResults();
  }

  public void constructInitialSolutions(populationI _Population){
    /*
    for(int i = 0 ; i < DEFAULT_PopSize ; i ++ ){
      if(i % 4 == 0){
        constructInitialSolutionsByRegion(_Population, i, i+1);
      }
    }
    */
    constructInitialSolutionsByRegion(_Population, 0, DEFAULT_initPopSize);
    //constructInitialSolutionsByRegion(_Population, DEFAULT_PopSize*3/4, DEFAULT_PopSize);
    clone1.setData(_Population);
    clone1.startToClone();
    _Population = clone1.getPopulation();
  }

  public void constructInitialSolutionsByRegion(populationI _Population, int start, int end){
    for(int i = start ; i < end ; i ++ ){
      int sequence[] = new int[numberOfJob];
      for(int j = 0 ; j < numberOfJob ; j ++ ){
        sequence[j] = j;
      }
      double weights[] = calcWeightsForEachSolution(DEFAULT_PopSize, i);
      double alpha[] = duplicateWeight(numberOfJob, weights[0]);
      double beta[] = duplicateWeight(numberOfJob, weights[1]);

      homework.schedule.singleMachineWithSameEarlyTardyPenalty singleMachine1 = new homework.schedule.singleMachineWithSameEarlyTardyPenalty();
      singleMachine1.setData(numberOfJob, dueDay, processingTime, sequence);
      singleMachine1.setAlphaBeta(alpha, beta);
      singleMachine1.setIterations(DP_Iter);
      singleMachine1.generateInitialSolution(i);
      singleMachine1.startAlgorithm();
      _Population.getSingleChromosome(i).setSolution(singleMachine1.getSolution());
    }
  }


  public final double[] calcWeightsForEachSolution(int numberOfSolutions, int indexOfSoln){
    double weights[] = new double[numberOfObjs];
    indexOfSoln ++;
    weights[0] = ((indexOfSoln+1)/(double)(numberOfSolutions+1));
    weights[1] = 1 - weights[0];
    //System.out.print(weights[0]+"\t"+weights[1]+"\n");
    return weights;
  }

  private double[] duplicateWeight(int length, double value){
    double array[] = new double[length];
    for(int i = 0 ; i < length ; i ++ ){
      array[i] = value;
    }
    return array;
  }

  public void printResults(){
    //to output the implementation result.
    String implementResult = "";
    /*
    double refSet[][] = getReferenceSet();
    double objArray[][] = GaMain[0].getArchieveObjectiveValueArray();
    double D1r = calcSolutionQuality(refSet, objArray);
    implementResult = numberOfJob+"\t"+"\t"+cloneRatio+"\t"+D1r+"\t"+implementationTime+"\n";
    objArray = null;
    writeFile("singleMachineBiCriteriaGA_20060604", implementResult);
    System.out.println(numberOfJob+"\t"+DEFAULT_crossoverRate+"\t"+DEFAULT_mutationRate+"\t"+D1r+"\t"+implementationTime);
    */

    implementResult = "";

    for(int k = 0 ; k < GaMain.getArchieve().getPopulationSize() ; k ++ ){
      //implementResult += GaMain.getArchieve().getSingleChromosome(k).toString1()+"\t";//sequnence
      for(int j = 0 ; j < numberOfObjs ; j ++ ){//for each objectives
        implementResult += GaMain.getArchieve().getObjectiveValues(k)[j]+"\t";
      }
      implementResult += "\n";
    }
    writeFile("singleMachineBiCriteriaGADP_"+numberOfJob, implementResult);

    //implementationResult += fileName+"\t"+numberOfJob+"\t" + GaMain.getArchieve().getSingleChromosome(bestInd).getObjValue()[0]+"\t"+timeClock1.getExecutionTime()/1000.0+"\n";
    //writeFile("singleMachineBiCriteriaGA_20060604", implementationResult);
    //System.out.println(implementationResult);
  }


  public static void main(String[] args){
    System.out.println("singleMachineBiCriteriaGADP_20060604");
    singleMachineBicriteriaWithDP singleMachine1 = new singleMachineBicriteriaWithDP();
    int jobSets[] = new int[]{20, 30, 40, 50};//20, 30, 40, 50, 100, 200
    int counter = 0;
    double crossoverRate[] = new double[]{0.8, 0.5},
           mutationRate[] = new double[]{0.6, 0.3};
    int populationSize[] = new int[]{100};
    int repeatExperiments = 1;

    for(int i = 0 ; i < repeatExperiments ; i ++ ){
      for(int m = 0 ; m < jobSets.length ; m ++ ){//jobSets.length
        for(int k = 0 ; k < 49 ; k ++ ){
          System.out.println("Combinations: "+counter);
          openga.applications.data.singleMachine readSingleMachineData1 = new openga.applications.data.singleMachine();
          int numberOfJobs = jobSets[m];
          String fileName = readSingleMachineData1.getFileName(numberOfJobs, k);
          //fileName = "bky"+numberOfJobs+"_1";
          System.out.print(fileName+"\t");
          readSingleMachineData1.setData("sks/"+fileName+".txt");
          readSingleMachineData1.getDataFromFile();
          int dueDate[] = readSingleMachineData1.getDueDate();
          int processingTime[] = readSingleMachineData1.getPtime();

          singleMachine1.setData(numberOfJobs, dueDate, processingTime, fileName);
          singleMachine1.setParameters(populationSize[0], crossoverRate[0], mutationRate[0], 1);
          singleMachine1.initiateVars();
          singleMachine1.start();
          counter ++;
        }
      }
    }
    System.exit(0);
  }

}