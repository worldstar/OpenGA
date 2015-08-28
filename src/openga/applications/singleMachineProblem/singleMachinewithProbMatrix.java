package openga.applications.singleMachineProblem;
import homework.schedule.singleMachine;
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

public class singleMachinewithProbMatrix extends singleMachineWithHeu1 {
  public singleMachinewithProbMatrix() {
  }
  int startingGeneration = 500;
  int interval = 30;
  int strategy = 1;

  probabilityMatrixI GaMain;
  boolean applyEvaporation = false;
  String evaporationMethod = "constant";//constant, method1, method2

  public void setProbabilityMatrixData(int startingGeneration, int interval){
    this.startingGeneration = startingGeneration;
    this.interval = interval;
  }

  public void setSequenceStrategy(int strategy){
    this.strategy = strategy;
  }

  public void setEvaporationMethod(boolean applyEvaporation, String evaporationMethod){
    this.applyEvaporation = applyEvaporation;
    this.evaporationMethod = evaporationMethod;
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
    GaMain.setProbabilityMatrixData(startingGeneration, interval);
    GaMain.setSequenceStrategy(strategy);
    GaMain.setEvaporationMethod(applyEvaporation, evaporationMethod);
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
    implementationResult += fileName+"\t"+evaporationMethod +"\t" + GaMain.getArchieve().getSingleChromosome(bestInd).getObjValue()[0]+"\t"+timeClock1.getExecutionTime()/1000.0+"\n";
    //implementationResult += fileName+"\t"+numberOfJob+"\t"+startingGeneration+"\t"+interval+"\t"+ GaMain.getArchieve().getSingleChromosome(bestInd).getObjValue()[0]+"\t"+timeClock1.getExecutionTime()/1000.0+"\n";
    writeFile("SingleMachine_ProbMatrix_Evaporation080302", implementationResult);
    System.out.println(implementationResult);
  }

  /**
   * For single objective problem
   * @param arch1
   * @return
   */
  public int getBestSolnIndex(populationI arch1){
    int index = 0;
    double bestobj = Double.MAX_VALUE;
    for(int k = 0 ; k < GaMain.getArchieve().getPopulationSize() ; k ++ ){
      if(bestobj > GaMain.getArchieve().getObjectiveValues(k)[0]){
        bestobj = GaMain.getArchieve().getObjectiveValues(k)[0];
        index = k;
      }
    }
    return index;
  }

  public static void main(String[] args) {

    System.out.println("SingleMachine_ProbMatrix_Evaporation080302");
    //openga.applications.data.singleMachine singleMachineData = new openga.applications.data.singleMachine();
    int jobSets[] = new int[]{20, 30, 40, 50, 60, 90};//20, 30, 40, 50, 60, 90, 100, 200
    int counter = 0;
    int popSize[] = new int[]{100};//50, 100, 155, 210
    double crossoverRate[] = new double[]{0.9},//0.8, 0.5
           mutationRate[] = new double[]{0.5};//0.6, 0.3
    int populationSize[] = new int[]{100};//200, 100
    int repeatExperiments = 30;

    int startingGeneration[] = new int[]{250};//100, 250
    int interval[] = new int[]{25};//25, 50
    int strategy[] = new int[]{1};
    boolean applyEvaporation = false;
    String evaporationMethod[] = new String[]{"add"};//"constant", "method1", "method2", "Threshold", "add"

    //Sourd Instance
    for(int m = 0 ; m < jobSets.length ; m ++ ){//jobSets.length
      for(int k = 0 ; k < 49 ; k ++ ){//49 9
        //if((jobSets[m] <= 50 && (k == 0 || k == 3 || k == 6 || k == 21 || k == 24 || k == 27 || k == 42 || k == 45 || k == 48)) ||  (jobSets[m] > 50 && k < 9)){
        if((jobSets[m] <= 50) ||  (jobSets[m] > 50 && k < 9)){
          for(int j = 0 ; j < startingGeneration.length ; j ++ ){
            for(int n = 0 ; n < interval.length ; n ++ ){
              for(int p = 0 ; p < strategy.length ; p ++ ){
                for(int q = 0 ; q < evaporationMethod.length ; q ++ ){
                  for(int i = 0 ; i < repeatExperiments ; i ++ ){
                    singleMachinewithProbMatrix singleMachine1 = new singleMachinewithProbMatrix();
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
                    singleMachine1.setProbabilityMatrixData(startingGeneration[j], interval[n]);
                    singleMachine1.setSequenceStrategy(strategy[p]);
                    singleMachine1.setEvaporationMethod(applyEvaporation, evaporationMethod[q]);
                    singleMachine1.initiateVars();
                    singleMachine1.start();
                    counter ++;
                  }//end for
                }
              }
            }
          }
        }//end if
      }
    }

    System.exit(0);

    /*
    //BKY Instance
    int incrementSteps = 5;
    for(int i = 0 ; i < repeatExperiments ; i ++ ){
      for(int j = 0 ; j < jobSets.length ; j ++ ){//jobSets.length
        for(int k = 0 ; k < startingGeneration.length ; k ++ ){
          for (int m = 0; m < interval.length; m++) {
            for (int n = 0; n < strategy.length; n++) {
              for(int p = 296 ; p < 297 ; ){
                openga.applications.data.readSingleMachine readSingleMachineData1 = new openga.applications.data.readSingleMachine();
                int numberOfJobs = jobSets[j];
                String fileName = readSingleMachineData1.getFileName(numberOfJobs, p);
                readSingleMachineData1.setData("instances/SingleMachineBKS/"+fileName+".txt");
                if(readSingleMachineData1.testReadData()){//to test whether the file exist
                  singleMachinewithProbMatrix singleMachine1 = new singleMachinewithProbMatrix();
                  System.out.println("Combinations: "+counter+"\t"+fileName);
                  //System.out.print(fileName+"\t");
                  readSingleMachineData1.getDataFromFile();
                  int dueDate[] = readSingleMachineData1.getDueDate();
                  int processingTime[] = readSingleMachineData1.getPtime();

                  singleMachine1.setData(numberOfJobs, dueDate, processingTime, fileName);
                  singleMachine1.setParameters(popSize[0], crossoverRate[0], mutationRate[0], 100000);

                  singleMachine1.setProbabilityMatrixData(startingGeneration[k], interval[m]);
                  singleMachine1.setSequenceStrategy(strategy[n]);
                  singleMachine1.setEvaporationMethod(false, evaporationMethod[0]);

                  singleMachine1.initiateVars();
                  singleMachine1.start();
                  counter ++;
                }
                p += incrementSteps;
              }
            }
          }
        }
      }
    }//end for of BKY
    */

  }

}
