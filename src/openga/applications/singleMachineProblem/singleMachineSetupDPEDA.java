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
 * <p>Description: GA with Dominance Propterties for setup problem.</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class singleMachineSetupDPEDA extends singleMachineSetupDP{
  public singleMachineSetupDPEDA() {
  }
  double lamda = 0.9; //CT
  int startingGenDividen = 3;  //CT
  int numberOfCrossoverTournament = 2; //ct
  int numberOfMutationTournament = 2;  //ct
  cloneI clone1;
  public ObjectiveFunctionSchedule2I ObjectiveFunction[];
  public double processingTime[][];
  public int numberOfJobs;
  public String fileName;
  EDAICrossover Crossover; //ct
  EDAIMutation Mutation; //ct

  EDAMainI GaMain;
  
  // CT {
public void setEDAinfo(double lamda, int numberOfCrossoverTournament, int numberOfMutationTournament, int startingGenDividen){
    this.lamda = lamda;
    this.numberOfCrossoverTournament = numberOfCrossoverTournament;
    this.numberOfMutationTournament = numberOfMutationTournament;
    this.startingGenDividen = startingGenDividen;
  }

  // CT }

  public void setData(int numberOfJobs, double processingTime[][], String fileName){
    this.numberOfJobs = numberOfJobs;
    numberOfJob = numberOfJobs;
    this.processingTime = processingTime;
    this.fileName = fileName;
      }

  public void initiateVars(){
    GaMain     = new singleThreadGAwithinipop_EDA();
    Population = new population();
    Selection  = new binaryTournament();
    Crossover  = new twoPointCrossover2EDA(); //ct
    //Crossover2 = new PMX();  //ct
    Mutation   = new swapMutationEDA();  //ct 
    //Mutation2  = new inverseMutation(); //ct
    ObjectiveFunction = new ObjectiveFunctionSchedule2I[numberOfObjs];
    ObjectiveFunction[0] = new ObjectiveEiTiSetupCommonDueForSingleMachine();
    //ObjectiveFunction[1] = new ObjectiveTardinessForFlowShop();//the second one., new ObjectiveEiTiSetupCommonDueForSingleMachine 
    Fitness    = new singleObjectiveFitness();
    objectiveMinimization = new boolean[numberOfObjs];
    objectiveMinimization[0] = true;
    //objectiveMinimization[1] = true;
    encodeType = true;

    //set schedule data to the objectives
    ObjectiveFunction[0].setScheduleData(processingTime, numberOfMachines);
    clone1  = new solutionVectorCloneWithMutation();//swap mutation
    DEFAULT_generations = totalSolnsToExamine/(DEFAULT_PopSize);
    //set the data to the GA main program.
    GaMain.setData(Population, Selection, Crossover, Mutation, ObjectiveFunction, Fitness, DEFAULT_generations, DEFAULT_initPopSize,DEFAULT_PopSize,
                   numberOfJob, DEFAULT_crossoverRate, DEFAULT_mutationRate, objectiveMinimization, numberOfObjs, encodeType, elitism);
    GaMain.setSecondaryCrossoverOperator(Crossover2, false);  //ct
    GaMain.setSecondaryMutationOperator(Mutation2, false);   //ct
    GaMain.setEDAinfo(lamda, numberOfCrossoverTournament, numberOfMutationTournament, startingGenDividen);
  

  }

  // ct { 
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
  // } 
  
  public void startMain(){
    openga.util.timeClock timeClock1 = new openga.util.timeClock();
    timeClock1.start();
    GaMain.initialStage();
    constructInitialSolutions(Population);
    GaMain.startGA();
    timeClock1.end();
    //System.out.println("\nThe final result");
    int bestInd = getBestSolnIndex(GaMain.getArchieve());
    String implementationResult = "";
    implementationResult += fileName+"\t"+numberOfJob+"\t" + GaMain.getArchieve().getSingleChromosome(bestInd).getObjValue()[0]+"\t"+timeClock1.getExecutionTime()/1000.0+"\n";
    writeFile("SingleMachineSetupDPEDA_20081120", implementationResult);
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
    clone1.setData(_Population); //ct
    clone1.startToClone();  //ct
    _Population = clone1.getPopulation();  //ct

  }

  public static void main(String[] args) {
    System.out.println("SingleMachineSetupDPEDA_20081120");

    int counter = 0;
    int repeatExperiments = 30;
    int jobSets[] = new int[]{10, 15, 20, 25, 50};//10, 15, 20, 25, 50, 100, 150, 200
    String type[] = new String[]{"low", "med", "high"};//"low", "med", "high"

    double crossoverRate[] = new double[]{0.5,0.8},//0.8, 0.5 (0.5)
           mutationRate[] = new double[]{0.3,0.6};//0.6, 0.3  (0.3)
    int populationSize[] = new int[]{100};//200, 100

    //CT {
    //EDA parameters.
    double[] lamda = new double[]{0.1, 0.5, 0.9}; //learning rate{0.1, 0.5, 0.9} {0.5}
    int numberOfCrossoverTournament[] = new int[]{1, 2, 4};//{1, 2, 4}
    int numberOfMutationTournament[] = new int[]{1, 2, 4};//{1, 2, 4}
    int startingGenDividen[] = new int[]{2, 4};//{2, 4}{4}
    //CT }
    
    
      for(int m = 0 ; m < jobSets.length ; m ++ ){//jobSets.length
        for(int j = 0 ; j < type.length ; j ++ ){
            for(int k = 1 ; k <= 15 ; k ++ ){                                                   
                 openga.applications.data.singleMachineSetupData singleMachineData1 = new openga.applications.data.singleMachineSetupData();
                 String fileName = "instances\\SingleMachineSetup\\"+type[j]+"\\"+jobSets[m]+"_"+k+".etp";
                 //fileName = "Data\\SMSetupTime8.txt";//for test
                 singleMachineData1.setData(fileName);
                 singleMachineData1.getDataFromFile();
                 int numberOfJobs = singleMachineData1.getSize();                            
                 double processingTime[][] = singleMachineData1.getProcessingTime();
                    
                 for(int q = 0 ; q < lamda.length ; q ++ ){  //CT
                   for (int t = 0; t < numberOfCrossoverTournament.length; t++) {
                      for (int n = 0; n < numberOfMutationTournament.length; n++) {
                         for(int p = 0 ; p < startingGenDividen.length ; p++){                             
                                for(int i = 0 ; i < repeatExperiments ; i ++ ){
                                 System.out.println("Combinations: "+counter);   
                                 singleMachineSetupDPEDA singleMachine1 = new singleMachineSetupDPEDA(); //CT
                                 singleMachine1.setData(numberOfJobs, processingTime, fileName);
                                 singleMachine1.setParameters(populationSize[0], crossoverRate[0], mutationRate[0], 1);
                                 singleMachine1.setEDAinfo(lamda[q], numberOfCrossoverTournament[t], numberOfMutationTournament[n], startingGenDividen[p]); //ct
                                 singleMachine1.initiateVars();
                                 singleMachine1.startMain();
                                 counter ++;
                                 
                                 
                                }                                           
                         }       
        
                     }        
                  }    
               }
  
            }
  
        }
      } 
    
  }

}
