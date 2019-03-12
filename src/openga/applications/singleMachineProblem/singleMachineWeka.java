package openga.applications.singleMachineProblem;

//import java.io.File;
//import java.io.IOException;
//import openga.chromosomes.*;
//import openga.operator.selection.*;
//import openga.operator.crossover.*;
//import openga.operator.mutation.*;
//import openga.ObjectiveFunctions.*;
//import openga.MainProgram.*;
//import openga.Fitness.*;
//import openga.util.printClass;
//import openga.util.fileWrite1;
//import openga.applications.data.readPFSSOAWT_flowshop;
import openga.applications.Continuous.*;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Date;
import openga.applications.*;
import openga.chromosomes.*;
import openga.operator.selection.*;
import openga.operator.crossover.*;
import openga.operator.mutation.*;
import openga.ObjectiveFunctions.*;
import openga.MainProgram.*;
import openga.ObjectiveFunctions.*;
import openga.Fitness.*;
//import openga.util.printClass;
import openga.util.fileWrite1;

/**
 * <p>
 * Title: The OpenGA project which is to build general framework of Genetic
 * algorithm.</p>
 * <p>
 * Description: Himmelblau is a function which is a continuous problem.</p>
 * <p>
 * Copyright: Copyright (c) 2005</p>
 * <p>
 * Company: Yuan-Ze University</p>
 *
 * @author 
 * @version 1.0
 */
public class singleMachineWeka extends singleMachine {

  public singleMachineWeka() {
  }
  /**
   * *
   * Basic variables of GAs.
   */
  int numberOfObjs = 1;
  populationI Population;
  SelectI Selection;
  CrossoverI_Weka Crossover , Crossover2;
  MutationI_Weka Mutation , Mutation2;
  ObjectiveFunctionScheduleI[] ObjectiveFunction;
  FitnessI Fitness;
  singleThreadGA_WekaI GaMain;

  /**
   * Parameters of the GA
   */
  int generations, length, initPopSize, fixPopSize;
  double crossoverRate, mutationRate;
  boolean[] objectiveMinimization; //true is minimum problem.
  boolean encodeType;  //binary of realize code
  int seed = 12345;
  int counter = 0;

//  Instance Data
  public double lamda = 0.9; //learning rate
  public double beta = 0.9;
  public int numberOfCrossoverTournament = 2;
  public int numberOfMutationTournament = 2;
  public int startingGenDividen = 3;

  //Results
  double bestObjectiveValues[];
  populationI solutions;

  public int DEFAULT_generations = 1000,
          DEFAULT_PopSize = 100,
          DEFAULT_initPopSize = 100;

  public double DEFAULT_crossoverRate = 0.9,
          DEFAULT_mutationRate = 0.2,
          elitism = 0.2;     //the percentage of elite chromosomes

  int instance;
  double originalPoint[];
  double coordinates[][];
  double distanceMatrix[][];
  String fileName = "";
  boolean applyLocalSearch = true;

  /**
   * The method is to modify the default value.
   */  
  public void setEDAinfo(double lamda, double beta, int numberOfCrossoverTournament, int numberOfMutationTournament, 
          int startingGenDividen ,double DEFAULT_crossoverRate , double DEFAULT_mutationRate,
          int length) {
      this.lamda = lamda;
      this.beta = beta;
      this.numberOfCrossoverTournament = numberOfCrossoverTournament;
      this.numberOfMutationTournament = numberOfMutationTournament;
      this.startingGenDividen = startingGenDividen;
      this.DEFAULT_crossoverRate = DEFAULT_crossoverRate;
      this.DEFAULT_mutationRate = DEFAULT_mutationRate;
      this.length = length;
  }
  
  public void setData(int numberOfJobs, int dueDate[], int processingTime[], String fileName,
          double DEFAULT_crossoverRate , double DEFAULT_mutationRate,int length){
    this.numberOfJob = numberOfJobs;
    this.dueDay = dueDate;
    this.processingTime = processingTime;
    this.fileName = fileName;
    this.DEFAULT_crossoverRate = DEFAULT_crossoverRate;
    this.DEFAULT_mutationRate = DEFAULT_mutationRate;
    this.length = length;
  }
  
  @Override
  public void initiateVars(){
    GaMain = new singleThreadGA_Weka();//singleThreadGAwithMultipleCrossover singleThreadGA adaptiveGA
    Population = new population();
    Selection = new binaryTournament();//binaryTournamentMaximization
    Crossover = new twoPointCrossover2_Weka(); //twoPointCrossover2()  CyclingCrossoverP multiParentsCrossover()
//    Crossover2 = new PMX();
    Mutation = new swapMutation_Weka();//shiftMutation
//    Mutation2 = new shiftMutation();//inverseMutation
    ObjectiveFunction = new ObjectiveFunctionScheduleI[numberOfObjs];
    ObjectiveFunction[0] = new ObjectiveEarlinessTardinessPenalty();//the first objective
    Fitness = new singleObjectiveFitness();//singleObjectiveFitness singleObjectiveFitnessByNormalize
    objectiveMinimization = new boolean[numberOfObjs];
    objectiveMinimization[0] = true;
    encodeType = true;
    ObjectiveFunction[0].setScheduleData(dueDay , processingTime, numberOfMachines);
    
    //set the data to the GA main program.
    GaMain.setData(Population, Selection, Crossover, Mutation, ObjectiveFunction,
            Fitness, DEFAULT_generations, DEFAULT_initPopSize, DEFAULT_PopSize,
            length, DEFAULT_crossoverRate, DEFAULT_mutationRate, objectiveMinimization,
            numberOfObjs, encodeType, elitism);
    GaMain.setSecondaryCrossoverOperator(Crossover2, false);
    GaMain.setSecondaryMutationOperator(Mutation2, false);
  }

  @Override
  public void startMain(){
    openga.util.timeClock timeClock1 = new openga.util.timeClock();
    timeClock1.start();
    GaMain.startGA();
    timeClock1.end();
//    String implementResult = instanceName + "\t" + DEFAULT_crossoverRate + "\t" + DEFAULT_mutationRate + "\t"
//            + elitism + "\t" + GaMain.getArchieve().getSingleChromosome(0).getObjValue()[0]
//            + "\t " + timeClock1.getExecutionTime() / 1000.0 + "\n";
//    
//    String implementResult = "";
//    int bestInd = getBestSolnIndex(GaMain.getArchieve());
//
    String implementResult = fileName + "\t" + DEFAULT_crossoverRate + "\t" + DEFAULT_mutationRate + "\t" + elitism + "\t"
            + GaMain.getArchieve().getSingleChromosome(0).getObjValue()[0] + "\t" + timeClock1.getExecutionTime() / 1000.0 + "\n";

    System.out.print(implementResult);
  }

  /**
   * Write the data into text file.
   * @param args
   */

  public static void main(String[] args) {
     
        int jobSets[] = new int[]{20,30,40,50,60,90};
        
        int counter = 0;
        int repeatExperiments = 1;//30

        int popSize[] = new int[]{100};//50, 100, 155, 210 [100]
        double crossoverRate[] = new double[]{0.1},//0.6, 0.9 {0.9}
                mutationRate[] = new double[]{0.9},//0.1, 0.5 {0.5}
                elitism = 0.1;

        //EDA parameters.
        double lamdalearningrate[] = new double[]{0.9};//0.1, 0.5, 0.9
        double betalearningrate[] = new double[]{0.5};  //0.1, 0.5, 0.9
        int numberOfCrossoverTournament[] = new int[]{5};//{1,2,4,5} 
        int numberOfMutationTournament[] = new int[]{1};//{1,2,4}
        int startingGenDividen[] = new int[]{4};//{2,4,7}
        
        int[] D1 = new int[]{1};//0,1,2,3
        int[] D2 = new int[]{0};//0,1,2,3,4
        boolean optMin = true;
        int[] epoch = new int[]{6};
        
        for (int j = 0; j < jobSets.length; j++) {//jobSets.length
          
            /*===sks===*/
            openga.applications.data.singleMachine readSingleMachineData1 = new openga.applications.data.singleMachine();
            int numberOfJobs = jobSets[j];
            int instanceReplications = readSingleMachineData1.getInstancesLength(String.valueOf(numberOfJobs));
//            System.out.println(instanceReplications);
            
            for (int k = 0; k < instanceReplications; k++) {  //49
              
                /*===sks===*/
                String fileName = readSingleMachineData1.getFileName(numberOfJobs, k);
                
                if(fileName == "sks222a" ||fileName ==  "sks255a" ||fileName ==  "sks288a" || fileName == "sks322a" ||fileName ==  "sks355a" ||fileName ==  "sks388a" ||
                    fileName == "sks422a" ||fileName ==  "sks455a" ||fileName ==  "sks488a" || fileName == "sks522a" ||fileName ==  "sks555a" ||fileName ==  "sks588a" ||
                    fileName == "sks622a" ||fileName ==  "sks655a" ||fileName ==  "sks688a" || fileName == "sks922a" ||fileName ==  "sks955a" ||fileName ==  "sks988a" )
//                if(fileName == "sks455a" || fileName == "sks555a" || fileName == "sks688a" || fileName == "sks955a" || fileName == "sks988a")
                {
                  readSingleMachineData1.setData("sks/" + fileName + ".txt");
                  readSingleMachineData1.getDataFromFile();

                  /*===bky===*/
  //                openga.applications.data.readSingleMachine readSingleMachineData1 = new openga.applications.data.readSingleMachine();
  //                String fileName = readSingleMachineData1.getFileName(numberOfJobs, k+1);
  //                readSingleMachineData1.setData("sks/" + fileName + ".txt");
  //                readSingleMachineData1.getDataFromFile();

  //                System.out.print(fileName + "\t");

                  int dueDate[] = readSingleMachineData1.getDueDate();
                  int processingTime[] = readSingleMachineData1.getPtime();
  //              for (int k = 0; k < 1; k++) {//bky
  //                if (jobSets[j] <= 50 || (jobSets[j] > 50 && k < 9)) {
  //                    if ((jobSets[j] <= 50 && (k == 0 || k == 3 || k == 6 || k == 21 || k == 24 || k == 27 || k == 42 || k == 45 || k == 48)) || (jobSets[j] > 50 && k < 9)) {
                      //if((jobSets[j] <= 50 && (k != 0 && k != 3 && k != 6 && k != 21 && k != 24 && k != 27 && k != 42 && k != 45 && k != 48)) ||  (jobSets[j] > 50 && k < 9)){
//                      for (int lx = 0; lx < lamdalearningrate.length; lx++) {
//                          for (int bx = 0; bx < betalearningrate.length; bx++) {
//                              for (int m = 0; m < numberOfCrossoverTournament.length; m++) {
//                                  for (int n = 0; n < numberOfMutationTournament.length; n++) {
//                                      for (int p = 0; p < startingGenDividen.length; p++) {

                                          for(int CRCount = 0 ; CRCount < crossoverRate.length ; CRCount++) {
                                              for(int MRCount = 0 ; MRCount < mutationRate.length ; MRCount++) {
//                                                  for(int D1Count = 0 ; D1Count < D1.length ; D1Count++) {
//                                                      for(int D2Count = 0 ; D2Count < D2.length ; D2Count++) {
//                                                        for(int epochCount = 0; epochCount < epoch.length ; epochCount++){
                                                          for (int i = 0; i < repeatExperiments; i++) {
                //                                            System.out.println("Combinations: " + counter);

                                                            singleMachineWeka singleMachineWeka1 = new singleMachineWeka();
                                                            singleMachineWeka1.setData(numberOfJobs, dueDate, processingTime,fileName,crossoverRate[CRCount],mutationRate[MRCount],numberOfJobs);
//                                                            singleMachineWeka1.setEDAinfo(lamdalearningrate[lx],betalearningrate[bx],numberOfCrossoverTournament[m],
//                                                                    numberOfMutationTournament[n],startingGenDividen[p],crossoverRate[CRCount],mutationRate[MRCount],numberOfJobs);
                                                            singleMachineWeka1.initiateVars();
                                                            singleMachineWeka1.startMain();
                                                            counter++;
                                                          }
//                                                        }
//                                                      }
//                                                  }
                                              }
                                          }//end CRCount
//                                      }
//                                  }
//                              }
//                          }
//                      }
                }// Problem if end                
            }
        }
    
  }
}
