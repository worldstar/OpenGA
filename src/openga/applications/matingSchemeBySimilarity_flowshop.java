package openga.applications;
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
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class matingSchemeBySimilarity_flowshop extends flowshop{
  public matingSchemeBySimilarity_flowshop() {
  }

  public int
    DEFAULT_generations = 200,
    DEFAULT_PopSize     = 300,
    DEFAULT_initPopSize = 500,
    totalSolnsToExamine = 1000000;//to fix the total number of solutions to examine.

    boolean applySecCRX = false;
    boolean applySecMutation = false;

    CrossoverI Crossover;

  public void setFlowShopData(int numberOfJob, int numberOfMachines){
    this.numberOfJob = numberOfJob;
    this.numberOfMachines = numberOfMachines;
  }

  public void setParameters(int PopSize, int totalSolnsToExamine,
                            double crossoverRate, double mutationRate, double elitism,
                            boolean applySecCRX, boolean applySecMutation){
    DEFAULT_PopSize = PopSize;
    this.totalSolnsToExamine = totalSolnsToExamine;
    this.DEFAULT_crossoverRate = crossoverRate;
    this.DEFAULT_mutationRate = mutationRate;
    this.elitism = elitism;
    DEFAULT_generations = totalSolnsToExamine/(PopSize);
    this.applySecCRX = applySecCRX;
    this.applySecMutation = applySecMutation;
  }

  public void initiateVars(){
    GaMain     = new singleThreadGA();//singleThreadGAwithSecondFront singleThreadGAwithMultipleCrossover
    Population = new population();
    Selection  = new similaritySelection2();
    Crossover  = new oneByOneChromosomeCrossover();//
    Crossover2 = new PMX();
    Mutation   = new shiftMutation();
    Mutation2  = new inverseMutation();
    ObjectiveFunction = new ObjectiveFunctionFlowShopScheduleI[numberOfObjs];
    ObjectiveFunction[0] = new ObjectiveMakeSpanForFlowShop();//the first objective, makespan
    ObjectiveFunction[1] = new ObjectiveTardinessForFlowShop();//the second one.
    Fitness    = new GoldbergFitnessAssignment();
    objectiveMinimization = new boolean[numberOfObjs];
    objectiveMinimization[0] = true;
    objectiveMinimization[1] = true;
    encodeType = true;

    //initiate scheduling data, we get the data from a program.
    processingTime = getProcessingTime();
    //dueDay = getDueDay();

    //set schedule data to the objectives
    ObjectiveFunction[0].setScheduleData(processingTime, numberOfMachines);
    //ObjectiveFunction[1].setScheduleData(dueDay, processingTime, numberOfMachines);
    DEFAULT_generations = totalSolnsToExamine / DEFAULT_PopSize;
    //set the data to the GA main program.
    GaMain.setData(Population, Selection, Crossover, Mutation, ObjectiveFunction, Fitness, DEFAULT_generations, DEFAULT_initPopSize,DEFAULT_PopSize,
                   numberOfJob, DEFAULT_crossoverRate, DEFAULT_mutationRate, objectiveMinimization, numberOfObjs, encodeType, elitism);
    GaMain.setSecondaryCrossoverOperator(Crossover2, false);
    GaMain.setSecondaryMutationOperator(Mutation2, true);
  }

  private int[] getDueDay(){
    openga.applications.data.flowShop data1 = new openga.applications.data.flowShop();
    if(numberOfJob == 20){
      return data1.getTestData2_DueDay();
    }
    else if(numberOfJob == 40){
      return data1.getTestData3_DueDay();
    }
    else if(numberOfJob == 60){
      return data1.getTestData4_DueDay();
    }
    else{
      return data1.getTestData5_DueDay();
    }
  }

  private int[][] getProcessingTime(){
    openga.applications.data.flowShop data1 = new openga.applications.data.flowShop();
    if(numberOfJob == 20){
      return data1.getTestData2_processingTime();
    }
    else if(numberOfJob == 40){
      return data1.getTestData3_processingTime();
    }
    else if(numberOfJob == 60){
      return data1.getTestData4_processingTime();
    }
    else{
      return data1.getTestData5_processingTime();
    }
  }

  public void start(){
    GaMain.startGA();

    //to output the implementation result.
    String implementResult = "";
    double refSet[][] = getReferenceSet();

    double objArray[][] = GaMain.getArchieveObjectiveValueArray();
    implementResult = numberOfJob+"\t"+ calcSolutionQuality(refSet, objArray) +"\n";//
    objArray = null;
    writeFile("SimilaritySelectionFlowShop", implementResult);


    implementResult = "";
    for(int i = 0 ; i < GaMain.getArchieve().getPopulationSize() ; i ++ ){
      for(int k = 0 ; k < GaMain.getPopulation().getSingleChromosome(0).getLength() ; k ++ ){
        implementResult += GaMain.getArchieve().getSingleChromosome(i).getSolution()[k] +" ";
      }
      implementResult += "\t";
      for(int j = 0 ; j < 2 ; j ++ ){//for each objectives
        implementResult += GaMain.getArchieve().getObjectiveValues(i)[j]+"\t";
      }
      implementResult += "\n";
    }
    writeFile("SimilaritySelectionFlowShop_objs", implementResult);

  }

  private double[][] getReferenceSet(){
    openga.applications.data.flowShop data1 = new openga.applications.data.flowShop();
    if(numberOfJob == 20){
      return data1.getReferenceSet2();
    }
    else if(numberOfJob == 40){
      return data1.getReferenceSet3();
    }
    else if(numberOfJob == 60){
      return data1.getReferenceSet4();
    }
    else{
      return data1.getReferenceSet5();
    }
  }

  /**
   * To evaluate the solution quality by some metric. It uses the D1r here.
   * @param refSet The current known Pareto set
   * @param obtainedPareto After the implementation of your algorithm.
   * @return The D1r value.
   */
  public double calcSolutionQuality(double refSet[][], double obtainedPareto[][]){
    openga.util.algorithm.D1r d1r1 = new openga.util.algorithm.D1r();
    d1r1.setData(refSet, obtainedPareto);
    d1r1.calcD1r();
    return d1r1.getD1r();
  }

  /**
   * Write the data into text file.
   */
  public void writeFile(String fileName, String _result){
    fileWrite1 writeLotteryResult = new fileWrite1();
    writeLotteryResult.writeToFile(_result,fileName+".txt");
    Thread thread1 = new Thread(writeLotteryResult);
    thread1.run();
  }

  public static void main(String[] args) {
    int popSize = 300;
    int numberOfJob[] = new int []{20, 40, 60, 80};
    int numberOfMachines = 20;
    int totalSolnsToExamine = 1000000;
    boolean applySecCRX[] = new boolean[]{false};//false, true
    boolean applySecMutation[] = new boolean[]{true};

    double crossoverRate = 0.9,
           mutationRate = 0.1,
           elitism = 0.2;
    int repeatExperiments = 20;

    //to form a text file and write the title in it.
    String implementResult = "numberOfJob\t D1r\n";
    matingSchemeBySimilarity_flowshop writeFile1 = new matingSchemeBySimilarity_flowshop();
    writeFile1.writeFile("SimilaritySelectionFlowShop", implementResult);
    int counter = 0;

    for(int i = 0 ; i < repeatExperiments ; i ++ ){
      for(int r = 0 ; r < numberOfJob.length ; r ++ ){
        System.out.println(counter++);
        matingSchemeBySimilarity_flowshop matingSchemeBySimilarity_flowshop1 = new matingSchemeBySimilarity_flowshop();
         matingSchemeBySimilarity_flowshop1.setFlowShopData(numberOfJob[r], numberOfMachines);
         matingSchemeBySimilarity_flowshop1.setParameters(popSize, totalSolnsToExamine, crossoverRate, mutationRate, elitism, applySecCRX[0], applySecMutation[0]);
         matingSchemeBySimilarity_flowshop1.initiateVars();
         matingSchemeBySimilarity_flowshop1.start();
         matingSchemeBySimilarity_flowshop1 = null;
         System.gc();
      }
    }
  }

}