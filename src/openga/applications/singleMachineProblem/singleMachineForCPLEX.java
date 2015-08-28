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
import openga.util.algorithm.arrayDimensionTransform;
import openga.operator.miningGene.probabilityMatrix;
/* Program start */
// The Mathematics package for the JMSL Numerical Library for Java.
// For Statistics, use com.imsl.stat.*
// For Math, use com.imsl.math.*;
//import com.imsl.stat.*;



/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class singleMachineForCPLEX extends singleMachine {
  public singleMachineForCPLEX() {
  }
  cloneI clone1;
  int replicationID;

  public void setReplicationID(int replicationID){
    this.replicationID = replicationID;
  }

  public void initiateVars(){
    GaMain     = new singleThreadGA();
    Population = new population();
    Selection  = new binaryTournament();
    Crossover  = new twoPointCrossover2();//
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
    DEFAULT_generations = (int)(numberOfJob*Math.log(numberOfJob));

    //set the data to the GA main program.
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
    implementationResult += fileName+"\t"+numberOfJob+"\t"+
        GaMain.getArchieve().getSingleChromosome(bestInd).getObjValue()[0]+"\t"+timeClock1.getExecutionTime()/1000.0+"\n";
    writeFile("cplexResults\\singleMachineForCplex_0116", implementationResult);
    System.out.println(implementationResult);

    //Output Xij matrix for CPLEX
    //analyzeDistribution(GaMain.getArchieve().getSingleChromosome(bestInd).getObjValue()[0], timeClock1.getExecutionTime(), numberOfJob);
  }

  /*
  public void analyzeDistribution(double obj, double executionTime, int length){
    probabilityMatrix probMatrix1 = new probabilityMatrix();
    Summary summary = new Summary();//basic description statistics
    arrayDimensionTransform arrayDimensionTransform1 = new arrayDimensionTransform();

    double avg, standardDeviation;
    double outputMatrix[][] = new double[length][length];//to determine the Xij variable by Statistic method
    String outputString = obj + "\t" + executionTime + "\n";

    openga.util.printClass p1 = new openga.util.printClass();

    population artificialPopulation = new population();//a container to store the artificial chromsomes
    artificialPopulation.setGenotypeSizeAndLength(encodeType, fixPopSize, length, numberOfObjs);
    artificialPopulation.initNewPop();

    GaMain.selectionStage(GaMain.getPopulation());
    probMatrix1.setData(Population, artificialPopulation);//archieve, Population, artificialPopulation
    probMatrix1.setDuplicateRate(1);
    probMatrix1.setStrategy(1);
    probMatrix1.foundemental();
    p1.printMatrix("probMatrix1.getContainer()", probMatrix1.getContainer());

    int counter = 0;
    for(int i = 0 ; i < length ; i ++ ){
      summary.update(probMatrix1.getContainer()[i]);
      avg = summary.getMean();
      standardDeviation = summary.standardDeviation(probMatrix1.getContainer()[i]);
      for(int j = 0 ; j < length ; j ++ ){
        double normalizedValue = (probMatrix1.getContainer()[i][j] - avg)/standardDeviation;
        double normalDistCdf = Cdf.normal(normalizedValue);
        //System.out.print(normalDistCdf);

        if(normalDistCdf < 0.1){
          outputMatrix[i][j] = 0;
          outputString += 0 + " ";
          counter ++;
        }        
        else if(normalDistCdf > 0.95){
          //outputMatrix[i][j] = 1;
          outputString += 1 + " ";
        }        
        else{
          outputMatrix[i][j] = -1;
          outputString += -1 + " ";
        }
      }
      outputString += "\n";
    }
    //System.out.println("counter "+counter);
    p1.printMatrix("outputMatrix", outputMatrix);
    System.exit(0);
    writeFile("cplexResults\\matrix_"+fileName+"_"+replicationID, outputString);
  }
  */

  public static void main(String[] args) {
    singleMachineForCPLEX singleMachine1 = new singleMachineForCPLEX();
    System.out.println("singleMachineGA_20070116");
    //openga.applications.data.singleMachine singleMachineData = new openga.applications.data.singleMachine();
    int jobSets[] = new int[]{20};//20, 30, 40, 50, 60, 90, 100, 200
    int counter = 0;
    int repeatExperiments = 30;

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
          singleMachine1.initiateVars();
          singleMachine1.setReplicationID(i);
          singleMachine1.start();
          counter ++;
        }
      }
    }

  }
}