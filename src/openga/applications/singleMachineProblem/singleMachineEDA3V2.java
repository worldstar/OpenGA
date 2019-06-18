package openga.applications.singleMachineProblem;

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
import openga.applications.data.*;
import openga.applications.singleMachine;
import openga.operator.clone.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import openga.operator.miningGene.PBILInteractiveWithEDA3V2I;


/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */
public class singleMachineEDA3V2 extends singleMachineEDA2 implements Runnable {

    public singleMachineEDA3V2() {
    }
    
    public PBILInteractiveWithEDA3V2I GaMain;
//    public EDAMainI GaMain;
    public int D1;
    public int D2;
    public boolean OptMin;
    int epoch;
    CountDownLatch latch;
    EDA3CrossoverI Crossover;
    EDA3MutationI Mutation;
    
    @Override
    public int getBestSolnIndex(populationI arch1) {
        int index = 0;
        double bestobj = Double.MAX_VALUE;
        for (int k = 0; k < GaMain.getArchieve().getPopulationSize(); k++) {
            if (bestobj > GaMain.getArchieve().getObjectiveValues(k)[0]) {
                bestobj = GaMain.getArchieve().getObjectiveValues(k)[0];
                index = k;
            }
        }
        return index;
    }
    
    //Single thread
    public void setEDAinfo(double lamda, double beta, int numberOfCrossoverTournament, int numberOfMutationTournament, int startingGenDividen , int D1 , int D2 , boolean OptMin , double DEFAULT_crossoverRate , double DEFAULT_mutationRate , int epoch ) {
        this.lamda = lamda;
        this.beta = beta;
        this.numberOfCrossoverTournament = numberOfCrossoverTournament;
        this.numberOfMutationTournament = numberOfMutationTournament;
        this.startingGenDividen = startingGenDividen;
        this.D1 = D1;
        this.D2 = D2;
        this.OptMin = OptMin;
        this.DEFAULT_crossoverRate = DEFAULT_crossoverRate;
        this.DEFAULT_mutationRate = DEFAULT_mutationRate;
        this.epoch = epoch;
    }
    
    
    public void initiateVars() {
        GaMain = new singleThreadGAwithEDA3V2();//singleThreadGA singleThreadGAwithSecondFront singleThreadGAwithMultipleCrossover adaptiveGA
        Population = new population();
        Selection = new binaryTournament();//binaryTournament
        Crossover = new twoPointCrossover2EDA3();//twoPointCrossover2EDA3 twoPointCrossover2 oneByOneChromosomeCrossover twoPointCrossover2withAdpative twoPointCrossover2withAdpativeThreshold
        Mutation = new swapMutationEDA3();//shiftMutation shiftMutationWithAdaptive shiftMutationWithAdaptiveThreshold
        ObjectiveFunction = new ObjectiveFunctionScheduleI[numberOfObjs];
        ObjectiveFunction[0] = new ObjectiveEarlinessTardinessPenalty();
        Fitness = new singleObjectiveFitness();
        objectiveMinimization = new boolean[numberOfObjs];
        objectiveMinimization[0] = true;
        encodeType = true;
        
        Crossover.setEDAinfo(D1, D2);
        Mutation.setEDAinfo(D1, D2);
        //clone1 = new solutionVectorCloneWithMutation();//swap mutation
        //GaMain.setCloneOperatpr(clone1, true);
        //set schedule data to the objectives
        ObjectiveFunction[0].setScheduleData(dueDay , processingTime, numberOfMachines);
        totalSolnsToExamine = 125000;//125000 100000 75000
        DEFAULT_PopSize = 100;
        //System.out.println(DEFAULT_PopSize);
        //System.exit(0);
        DEFAULT_generations = totalSolnsToExamine / (DEFAULT_PopSize);
        //set the data to the GA main program.
        GaMain.setData(Population, Selection, Crossover, Mutation, ObjectiveFunction, Fitness, DEFAULT_generations, DEFAULT_PopSize, DEFAULT_PopSize,
                numberOfJob, DEFAULT_crossoverRate, DEFAULT_mutationRate, objectiveMinimization, numberOfObjs, encodeType, elitism);
        GaMain.setSecondaryCrossoverOperator(Crossover2, false);
        GaMain.setSecondaryMutationOperator(Mutation2, false);

        GaMain.setEDAinfo(lamda, beta, numberOfCrossoverTournament, 
                numberOfMutationTournament, startingGenDividen 
                , D1 , D2 , OptMin , epoch);  //startingGenDividen here is as interval of EDA
        
    }

    public void startMain() {
        openga.util.timeClock timeClock1 = new openga.util.timeClock();
        timeClock1.start();
        GaMain.startGA();
        timeClock1.end();
        //to output the implementation result.
        String implementResult = "";
        int bestInd = getBestSolnIndex(GaMain.getArchieve());
        
        implementResult = fileName + "\t" + lamda + "\t" + beta + "\t" + numberOfCrossoverTournament 
                + "\t" + numberOfMutationTournament + "\t" + startingGenDividen + "\t" 
                + D1 + "\t" + D2 + "\t" + OptMin + "\t" + epoch + "\t"
                + DEFAULT_crossoverRate + "\t" + DEFAULT_mutationRate + "\t" 
                + GaMain.getArchieve().getSingleChromosome(bestInd).getObjValue()[0] + "\t" + timeClock1.getExecutionTime() / 1000.0 + "\n";
        
        writeFile("singleMachineEDA3V2", implementResult);
        System.out.print(implementResult);
    }

    @Override
    public void run() {
        try {
        initiateVars();
        startMain();
        latch.countDown();//Reduce the current thread count.
        } 
        catch(Exception e) {
          e.printStackTrace();
        }     
    }
    
    private static void mainSingleThread() {
        int jobSets[] = new int[]{20,30,40,50,60,90};//20,30,40,50,60,90
//        int jobSets[] = new int[]{100,200};//bky

        int counter = 0;
        int repeatExperiments = 5;//30

        int popSize[] = new int[]{100};//50, 100, 155, 210 [100]
        double crossoverRate[] = new double[]{0.1},//0.6, 0.9 {0.9}//0.1,0.5,0.9 // 固定 0.1
                mutationRate[] = new double[]{0.9},//0.1, 0.5 {0.5}//0.1,0.5,0.9 // 固定 0.9
                elitism = 0.1;

        //EDA parameters.
        double lamdalearningrate[] = new double[]{0.1};//0.1, 0.5, 0.9
        double betalearningrate[] = new double[]{0.1};  //0.1, 0.5, 0.9 // 固定 0.1
        int numberOfCrossoverTournament[] = new int[]{1};//{1,2,4,5} 
        int numberOfMutationTournament[] = new int[]{1};//{1,2,4} // 固定 1
        int startingGenDividen[] = new int[]{7};//{2,4,7}
        
        int[] D1 = new int[]{0};//0,1,2 // 固定 0
        int[] D2 = new int[]{0};//0,1,2 // 固定 0
        boolean optMin = true;
        int[] epoch = new int[]{6};//2,4,6 // 固定 6
        
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
                      for (int lx = 0; lx < lamdalearningrate.length; lx++) {
                          for (int bx = 0; bx < betalearningrate.length; bx++) {
                              for (int m = 0; m < numberOfCrossoverTournament.length; m++) {
                                  for (int n = 0; n < numberOfMutationTournament.length; n++) {
                                      for (int p = 0; p < startingGenDividen.length; p++) {

                                          for(int CRCount = 0 ; CRCount < crossoverRate.length ; CRCount++) {
                                              for(int MRCount = 0 ; MRCount < mutationRate.length ; MRCount++) {
                                                  for(int D1Count = 0 ; D1Count < D1.length ; D1Count++) {
                                                      for(int D2Count = 0 ; D2Count < D2.length ; D2Count++) {
                                                        for(int epochCount = 0; epochCount < epoch.length ; epochCount++){
                                                          for (int i = 0; i < repeatExperiments; i++) {
                //                                            System.out.println("Combinations: " + counter);

                                                            singleMachineEDA3V2 singleMachine1 = new singleMachineEDA3V2();
                                                            singleMachine1.setData(numberOfJobs, dueDate, processingTime,fileName);
                                                            singleMachine1.setEDAinfo(lamdalearningrate[lx], betalearningrate[bx], numberOfCrossoverTournament[m], 
                                                                    numberOfMutationTournament[n], startingGenDividen[p] , D1[D1Count], D2[D2Count] , optMin , 
                                                                    crossoverRate[CRCount] , mutationRate[MRCount], epoch[epochCount] );
                                                            singleMachine1.initiateVars();
                                                            singleMachine1.startMain();
                                                            counter++;
                                                          }
                                                        }
                                                      }
                                                  }
                                              }
                                          }//end CRCount
                                      }
                                  }
                              }
                          }
                      }
                }// Problem if end                
            }
        }
    }
    
    
    public static void main(String[] args) {
      mainSingleThread();
    }
}
