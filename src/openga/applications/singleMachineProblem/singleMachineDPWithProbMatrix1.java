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

public class singleMachineDPWithProbMatrix1 extends singleMachineWithHeu1 {
  public singleMachineDPWithProbMatrix1() {
  }

  cloneI clone1;
  int startingGeneration = 500;
  int interval = 30;
  int strategy = 1;

  probabilityMatrixI GaMain;

  public void setProbabilityMatrixData(int startingGeneration, int interval){
    this.startingGeneration = startingGeneration;
    this.interval = interval;
  }

  public void setSequenceStrategy(int strategy){
    this.strategy = strategy;
  }

  public void initiateVars(){
    GaMain     = new singleThreadGAwithProbabilityMatrix2();
    Population = new population();
    Selection  = new binaryTournament();
    Crossover  = new twoPointCrossover2();
    Crossover2 = new PMX();
    Mutation   = new swapMutation();
    Mutation2  = new inverseMutation();
    ObjectiveFunction = new ObjectiveFunctionScheduleI[numberOfObjs];
    ObjectiveFunction[0] = new ObjectiveEarlinessTardinessPenalty();//the first objective, tardiness, ObjectiveTardiness ObjectiveEarlinessTardinessPenalty
    Fitness    = new singleObjectiveFitness();
    objectiveMinimization = new boolean[numberOfObjs];
    objectiveMinimization[0] = true;
    //objectiveMinimization[1] = true;
    encodeType = true;
    clone1  = new solutionVectorCloneWithMutation();//swap mutation
    //set schedule data to the objectives
    ObjectiveFunction[0].setScheduleData(dueDay, processingTime, numberOfMachines);

    //set the data to the GA main program.
    GaMain.setData(Population, Selection, Crossover, Mutation, ObjectiveFunction, Fitness, DEFAULT_generations, DEFAULT_PopSize, DEFAULT_PopSize,
                   numberOfJob, DEFAULT_crossoverRate, DEFAULT_mutationRate, objectiveMinimization, numberOfObjs, encodeType, elitism);
    GaMain.setSecondaryCrossoverOperator(Crossover2, false);
    GaMain.setSecondaryMutationOperator(Mutation2, false);
  }

  public void start(){
    System.out.println();
    openga.util.timeClock timeClock1 = new openga.util.timeClock();
    GaMain.initialStage();
    timeClock1.start();
    constructInitialSolutions(Population);
    GaMain.startGA();
    timeClock1.end();
    //System.out.println("\nThe final result");
    int bestInd = 0;//getBestSolnIndex(((MainI)GaMain).getArchieve());
    String implementationResult = "";
    implementationResult += fileName+"\t"+ GaMain.getArchieve().getSingleChromosome(bestInd).getObjValue()[0]+"\t"+timeClock1.getExecutionTime()/1000.0+"\n";
    writeFile("singleMachineDPwithProbMatrix_Job30_1023", implementationResult);
    System.out.println(implementationResult);
  }

  public void constructInitialSolutions(populationI _Population){
    for(int i = 0 ; i < DEFAULT_PopSize ; i ++ ){
      int sequence[] = new int[numberOfJob];
      for(int j = 0 ; j < numberOfJob ; j ++ ){
        sequence[j] = j;
      }

      homework.schedule.singleMachine singleMachine1 = new homework.schedule.singleMachine();
      singleMachine1.setData(numberOfJob, dueDay, processingTime, sequence);
      singleMachine1.setIterations(DP_Iter);

      singleMachine1.generateInitialSolution(i);
      singleMachine1.startAlgorithm();
      _Population.getSingleChromosome(i).setSolution(singleMachine1.getSolution());
    }
    clone1.setData(_Population);
    clone1.startToClone();
    _Population = clone1.getPopulation();
  }

  public static void main(String[] args) {
    System.out.println("singleMachineDPwithProbMatrix_Job30_1023");
    //openga.applications.data.singleMachine singleMachineData = new openga.applications.data.singleMachine();
    int jobSets[] = new int[]{90};//20, 30, 40, 50, 60, 90, 100, 200
    int counter = 0;
    double crossoverRate[] = new double[]{0.8},//0.8, 0.5 (0.5)
           mutationRate[] = new double[]{0.3};//0.6, 0.3  (0.3)
    int populationSize[] = new int[]{100};//200, 100
    int repeatExperiments = 1;

    int startingGeneration[] = new int[]{200};//200, 500
    int interval[] = new int[]{25};
    int strategy[] = new int[]{1};

    for(int i = 0 ; i < repeatExperiments ; i ++ ){
      for(int m = 0 ; m < jobSets.length ; m ++ ){//jobSets.length
        for(int k = 8 ; k < 9 ; k ++ ){
          //if((jobSets[m] <= 50 && (k != 0 && k != 3 && k != 6 && k != 21 && k != 24 && k != 27 && k != 42 && k != 45 && k != 48)) ||  (jobSets[m] > 50 && k < 9)){
            singleMachineDPWithProbMatrix1 singleMachine1 = new singleMachineDPWithProbMatrix1();
            System.out.print("Combinations: "+counter);
            openga.applications.data.singleMachine readSingleMachineData1 = new openga.applications.data.singleMachine();
            int numberOfJobs = jobSets[m];
            String fileName = readSingleMachineData1.getFileName(numberOfJobs, k);
            //fileName = "bky"+numberOfJobs+"_1";
            System.out.print(fileName+"\t");
            readSingleMachineData1.setData("sks/"+fileName+".txt");
            readSingleMachineData1.getDataFromFile();
            int dueDate[] = readSingleMachineData1.getDueDate();
            int processingTime[] = readSingleMachineData1.getPtime();
            double alpha[] = readSingleMachineData1.getAlpha();
            double beta[] = readSingleMachineData1.getBeta();

            singleMachine1.setData(numberOfJobs, dueDate, processingTime, fileName);
            singleMachine1.setParameters(populationSize[0], crossoverRate[0], mutationRate[0], 0);
            singleMachine1.setProbabilityMatrixData(startingGeneration[0], interval[0]);
            singleMachine1.setSequenceStrategy(strategy[0]);
            singleMachine1.initiateVars();
            singleMachine1.start();
            counter ++;
          //}
        }
      }
    }
  }

}