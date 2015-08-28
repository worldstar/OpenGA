package openga.applications.flowshopProblem;
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
import openga.applications.data.*;
import openga.operator.clone.*;

/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class flowshopProbMatrixEiTi extends flowshopProbMatrix {
  public flowshopProbMatrixEiTi() {
  }
  int dueDay[];
  int totalSolnsToExamine = 100000;//to fix the total number of solutions to examine. 100000

  public void setFlowShopData(int numberOfJob, int numberOfMachines, int dueDay[], int processingTime[][]){
    this.numberOfJob = numberOfJob;
    this.numberOfMachines = numberOfMachines;
    this.dueDay = dueDay;
    this.processingTime = processingTime;
  }

  public void initiateVars(){
    GaMain     = new singleThreadGAwithProbabilityMatrix();
    Population = new population();
    Selection  = new binaryTournament();
    Crossover  = new twoPointCrossover2();//
    Crossover2 = new PMX();

    Mutation   = new swapMutation();
    //Mutation   = new swapMutationWithMining2();//swapMutationWithMining2 shiftMutationWithMining2
    Mutation2  = new inverseMutation();
    ObjectiveFunction = new ObjectiveFunctionFlowShopScheduleI[numberOfObjs];
    ObjectiveFunction[0] = new ObjectiveEarlinessTardinessForFlowShop();//the first objective, ObjectiveMakeSpanForFlowShop ObjectiveEarlinessTardinessForFlowShop
    Fitness    = new singleObjectiveFitness();
    objectiveMinimization = new boolean[numberOfObjs];
    objectiveMinimization[0] = true;
    //objectiveMinimization[1] = true;
    encodeType = true;
    clone1  = new solutionVectorCloneWithMutation();//swap mutation
    //set schedule data to the objectives
    ObjectiveFunction[0].setScheduleData(dueDay, processingTime, numberOfMachines);
    //set the data to the GA main program.
    GaMain.setProbabilityMatrixData(startingGeneration, interval);
    GaMain.setSequenceStrategy(strategy);

    GaMain.setData(Population, Selection, Crossover, Mutation, ObjectiveFunction, Fitness, DEFAULT_generations, DEFAULT_PopSize, DEFAULT_PopSize,
                   numberOfJob, DEFAULT_crossoverRate, DEFAULT_mutationRate, objectiveMinimization, numberOfObjs, encodeType, elitism);
    GaMain.setSecondaryCrossoverOperator(Crossover2, false);
    GaMain.setSecondaryMutationOperator(Mutation2, false);
  }

  public void start(){
    System.out.println();
    openga.util.timeClock timeClock1 = new openga.util.timeClock();
    timeClock1.start();
    GaMain.startGA();
    timeClock1.end();
    //System.out.println("\nThe final result");
    int bestInd = getBestSolnIndex(GaMain.getArchieve());
    String implementationResult = "";
    //implementationResult += fileName+"\t"+numberOfJob+"\t"+DEFAULT_crossoverRate+"\t"+DEFAULT_mutationRate+"\t"+DEFAULT_PopSize+"\t" + GaMain.getArchieve().getSingleChromosome(bestInd).getObjValue()[0]+"\t"+timeClock1.getExecutionTime()/1000.0+"\n";
    implementationResult += fileName+"\t"+startingGeneration+"\t"+interval+"\t"+strategy+"\t" + GaMain.getArchieve().getSingleChromosome(bestInd).getObjValue()[0]+"\t"+timeClock1.getExecutionTime()/1000.0+"\n";
    writeFile("flowshop_ProbMatrixDOE_1106", implementationResult);
    System.out.println(implementationResult);
  }


  public static void main(String[] args) {
    System.out.println("flowshop_ProbMatrixDOE_1106");
    int repeatExperiments = 30;
    int popSize[] = new int[]{100, 200};//100, 155, 210
    int counter = 0;
    double crossoverRate[] = new double[]{0.6, 0.9},//0.6, 0.9 {0.6}
           mutationRate [] = new double[]{0.2, 0.5},//0.1, 0.5 {0.5}
           elitism = 0.1;
    int startingGeneration[] = new int[]{200, 500};//200, 500
    int interval[] = new int[]{25, 50};
    int strategy[] = new int[]{0, 1};

    int numberOfJobs[] = new int[]{20, 40, 60, 80, 100};
    int numberOfMachine = 3;

    for(int i = 0 ; i < repeatExperiments ; i ++ ){
      for(int j = 0 ; j < numberOfJobs.length ; j ++ ){
        for(int k = 0 ; k < startingGeneration.length ; k ++ ){
          for(int m = 0 ; m < interval.length ; m ++ ){
            for(int n = 0 ; n < strategy.length ; n ++ ){
              System.out.print(numberOfJobs[j]+"\n");
              /*
              flowshopProbMatrixEiTi flowshop1 = new flowshopProbMatrixEiTi();
              readFlowShopData readFlowShopData1 = new readFlowShopData();
              String fileName = "instances\\flowshop\\";
              //fileName += readFlowShopRevInstance1.getFileName(j);
              fileName += "flowshop_realData.txt";
              readFlowShopData1.setData(fileName);
              readFlowShopData1.setData(numberOfJobs[j]);
              readFlowShopData1.getDataFromFile();
              System.out.print("Combinations:\t"+(counter++)+"\t");
              flowshop1.setFlowShopData(numberOfJobs[j], numberOfMachine, readFlowShopData1.getDueDate(), readFlowShopData1.getPtime());
              flowshop1.setParameters(popSize[0], crossoverRate[1], mutationRate[1]);
              flowshop1.setProbabilityMatrixData(startingGeneration[k], interval[m]);
              flowshop1.setSequenceStrategy(strategy[n]);
              flowshop1.setData(fileName);
              flowshop1.initiateVars();
              flowshop1.start();
                  */
            }
          }
        }
      }
    }
  }


}