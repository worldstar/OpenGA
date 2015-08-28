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
 * <p>Description: It is the same with singleMachineTestMutation. But there is no DP and it's standard GA.</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class singleMachineTestMutation2 extends singleMachineTestMutation {
  public singleMachineTestMutation2() {
  }

  int mutationMethod = 0;
  int withMiningProbability = 0;

  private void setMutationMethod(int mutationMethod, int withMiningProbability){
    this.mutationMethod = mutationMethod;
    this.withMiningProbability = withMiningProbability;
  }

  public void initiateVars(){
    GaMain     = new singleThreadGA();
    Population = new population();
    Selection  = new binaryTournament();
    Crossover  = new twoPointCrossover2();//
    Crossover2 = new PMX();

    if(mutationMethod == 0 && withMiningProbability == 0){
      Mutation   = new swapMutation();
    }
    else if(mutationMethod == 1 && withMiningProbability == 0){
      Mutation   = new shiftMutation();
    }
    else if(mutationMethod == 0 && withMiningProbability == 1){
       Mutation   = new swapMutationWithMining2();
    }
    else{
      Mutation   = new shiftMutationWithMining2();
    }

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
    GaMain.startGA();
    timeClock1.end();
    //System.out.println("\nThe final result");
    int bestInd = getBestSolnIndex(GaMain.getArchieve());
    String implementationResult = "";
    implementationResult += fileName+"\t"+numberOfJob+"\t"+mutationMethod+"\t"+withMiningProbability+"\t"+DEFAULT_crossoverRate+"\t"+DEFAULT_mutationRate+"\t"+DEFAULT_PopSize+"\t" + GaMain.getArchieve().getSingleChromosome(bestInd).getObjValue()[0]+"\t"+timeClock1.getExecutionTime()/1000.0+"\n";
    writeFile("singleMachineDPGA_mutationMethods_twoPopsToVote_0930", implementationResult);
    System.out.println(implementationResult);
  }

  public static void main(String[] args) {
    System.out.println("singleMachineSGA_replications_0930");
    //openga.applications.data.singleMachine singleMachineData = new openga.applications.data.singleMachine();
    int jobSets[] = new int[]{20, 30, 40, 50};//20, 30, 40, 50, 100, 200
    int counter = 0;
    double crossoverRate[] = new double[]{0.8},//0.8, 0.5
           mutationRate[] = new double[]{0.3};//0.6, 0.3
    int populationSize[] = new int[]{100};//200, 100
    int repeatExperiments = 30;
    //[0, 1, 2, 3] are swapMutation, shiftMutation
    int mutationMethods[] = new int[]{0, 1};
    int withMiningProbability[] = new int[]{0, 1};//0 is not use while 1 is to use mining.

    for(int i = 0 ; i < repeatExperiments ; i ++ ){
      for(int m = 0 ; m < jobSets.length ; m ++ ){//jobSets.length
        for(int k = 0 ; k < 49 ; k ++ ){//49
          for(int n = 0 ; n < mutationMethods.length ; n ++ ){
            for(int p = 0 ; p < withMiningProbability.length ; p ++ ){
              singleMachineTestMutation2 singleMachine1 = new singleMachineTestMutation2();
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
              singleMachine1.setMutationMethod(mutationMethods[n], withMiningProbability[p]);
              singleMachine1.initiateVars();
              singleMachine1.start();
              counter ++;
            }
          }
        }
      }
    }

  }
}