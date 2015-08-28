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
import openga.applications.data.*; //ct

/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class singleMachineSetupDPEAG extends  singleMachineEAG {   //ct
  public singleMachineSetupDPEAG() {
  }

  /***
   * Parameters of Guided Mutation.
   */
  double lamda = 0.9; //learning rate
  double beta = 0.9;
  cloneI clone1;  //ct
  public ObjectiveFunctionSchedule2I ObjectiveFunction[];
  public double processingTime[][];
  public int numberOfJobs;
  public String fileName;
  //EAGMainI GaMain;

 public void setData(int numberOfJobs, double processingTime[][], String fileName){
    this.numberOfJobs = numberOfJobs;
    numberOfJob = numberOfJobs;
    this.processingTime = processingTime;
    this.fileName = fileName;
 }

  
  public void setEDAinfo(double lamda, double beta){
    this.lamda = lamda;
    this.beta = beta;
  }

 
  public void initiateVars(){
    GaMain     = new guidedMutationMainInitialPop();
    Population = new population();
    Selection  = new binaryTournament();
    Crossover  = new twoPointCrossover2();//
    Crossover2 = new PMX();

    Mutation   = new swapMutation();
    //Mutation   = new swapMutationWithMining2();//swapMutationWithMining2 shiftMutationWithMining2
    Mutation2  = new inverseMutation();
    ObjectiveFunction = new ObjectiveFunctionSchedule2I[numberOfObjs];
    ObjectiveFunction[0] = new ObjectiveEiTiSetupCommonDueForSingleMachine();//the first objective, tardiness, ObjectiveEarlinessTardinessPenalty
    Fitness    = new singleObjectiveFitness();
    objectiveMinimization = new boolean[numberOfObjs];
    objectiveMinimization[0] = true;
    //objectiveMinimization[1] = true;
    encodeType = true;
    clone1  = new solutionVectorCloneWithMutation();//swap mutation
    //set schedule data to the objectives
        ObjectiveFunction[0].setScheduleData(processingTime, numberOfMachines);
    //set the data to the GA main program.
    GaMain.setGuidedMutationInfo(lamda, beta);
    GaMain.setSequenceStrategy(strategy);

    DEFAULT_generations = totalSolnsToExamine/(DEFAULT_PopSize);
    GaMain.setData(Population, Selection, Crossover, Mutation, ObjectiveFunction, Fitness, DEFAULT_generations, DEFAULT_initPopSize, DEFAULT_PopSize,
                   numberOfJob, DEFAULT_crossoverRate, DEFAULT_mutationRate, objectiveMinimization, numberOfObjs, encodeType, elitism);
    GaMain.setSecondaryCrossoverOperator(Crossover2, false);
    GaMain.setSecondaryMutationOperator(Mutation2, false);
  }

  public void start(){
    System.out.println();
    openga.util.timeClock timeClock1 = new openga.util.timeClock();
    GaMain.initialStage();
    constructInitialSolutions(Population);
    timeClock1.start();
    GaMain.startGA();
    timeClock1.end();
    //System.out.println("\nThe final result");
    int bestInd = getBestSolnIndex(GaMain.getArchieve());
    String implementationResult = "";
    implementationResult += fileName+"\t"+beta+"\t"+lamda+"\t" +
        GaMain.getArchieve().getSingleChromosome(bestInd).getObjValue()[0]+"\t"+
        timeClock1.getExecutionTime()/1000.0+"\n";
    writeFile("SingleMachineSetup_DPEAG_20081123", implementationResult);
    System.out.println(implementationResult);
  }
  public void constructInitialSolutions(populationI _Population){
    for(int i = 0 ; i < DEFAULT_PopSize ; i ++ ){
      int sequence[] = new int[numberOfJob];
      for(int j = 0 ; j < numberOfJob ; j ++ ){
        sequence[j] = j;
      }

      homework.schedule.singleMachineSetupDP singleMachine1 = new homework.schedule.singleMachineSetupDP();
      singleMachine1.setData(numberOfJobs, processingTime, sequence);     
      //DP_Iter = 1;
      //singleMachine1.setIterations(DP_Iter);
     // singleMachine1.generateInitialSolution(i);
      singleMachine1.generateRandomSolution();
      singleMachine1.startAlgorithm();    
      
      _Population.getSingleChromosome(i).setSolution(singleMachine1.getSolution());
    }
    clone1.setData(_Population);
    clone1.startToClone();
    _Population = clone1.getPopulation();
  }


  public static void main(String[] args) {
    System.out.println("SingleMachine_EAG_20080323");
    //openga.applications.data.singleMachine singleMachineData = new openga.applications.data.singleMachine();
    int jobSets[] = new int[]{10, 15, 20, 25, 50};//10, 15, 20, 25, 50, 100, 150, 200
    int counter = 0;
    int repeatExperiments = 30;
    String type[] = new String[]{"low", "med", "high"};//"low", "med", "high"
    
    //Parameters of Guided Mutation.
    double[] lamda = new double[]{0.9}; //learning rate{0.1, 0.5, 0.9} {0.9 is better}
    double[] beta = new double[]{0.5};//Percentage of local information{0.1, 0.5, 0.9} {0.5 is significant}

    //Sourd Instance
    for(int m = 0 ; m < jobSets.length ; m ++ ){//jobSets.length
       for(int l = 0 ; l < type.length ; l ++ ){ 
         for(int k = 1 ; k <= 15 ; k ++ ){//49 9
        //if((jobSets[m] <= 50 && (k == 0 || k == 3 || k == 6 || k == 21 || k == 24 || k == 27 || k == 42 || k == 45 || k == 48)) ||  (jobSets[m] > 50 && k < 9)){
        if((jobSets[m] <= 50 && (k != 0 && k != 24 && k != 48)) ||  (jobSets[m] > 50 && k < 9)){
          for(int j = 0 ; j < lamda.length ; j ++ ){
            for(int n = 0 ; n < beta.length ; n ++ ){
              System.out.println("Combinations: "+counter);
              openga.applications.data.singleMachineSetupData singleMachineData1 = new openga.applications.data.singleMachineSetupData();
              String fileName = "instances\\SingleMachineSetup\\"+type[l]+"\\"+jobSets[m]+"_"+k+".etp";
              singleMachineData1.setData(fileName);
              singleMachineData1.getDataFromFile();
              int numberOfJobs = singleMachineData1.getSize();

              //fileName = "bky"+numberOfJobs+"_1";
              System.out.print(fileName+"\t");
              double processingTime[][] = singleMachineData1.getProcessingTime();

              for(int i = 0 ; i < repeatExperiments ; i ++ ){
                singleMachineSetupDPEAG singleMachine1 = new singleMachineSetupDPEAG();
                singleMachine1.setData(numberOfJobs, processingTime, fileName);
                singleMachine1.setEDAinfo(lamda[j], beta[n]);
                singleMachine1.initiateVars();
                singleMachine1.start();
                counter ++;
              }//end for
            }
          }
        }//end if
         }
       }
    }

    System.exit(0);
  }


}