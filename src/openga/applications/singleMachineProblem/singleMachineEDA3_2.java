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
import openga.operator.miningGene.PBILInteractiveWithEDA3_2I;

/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */
public class singleMachineEDA3_2 extends singleMachineEDA2 {

    public singleMachineEDA3_2() {
    }
    
    public PBILInteractiveWithEDA3_2I GaMain;
//    public EDAMainI GaMain;
    public int D1 = 0;
    public int D2 = 0;
    public int OptMin = 0;
    
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
    
    public void setEDAinfo(double lamda, double beta, int numberOfCrossoverTournament, int numberOfMutationTournament, int startingGenDividen , int D1 , int D2 , int OptMin) {
        this.lamda = lamda;
        this.beta = beta;
        this.numberOfCrossoverTournament = numberOfCrossoverTournament;
        this.numberOfMutationTournament = numberOfMutationTournament;
        this.startingGenDividen = startingGenDividen;
        this.D1 = D1;
        this.D2 = D2;
        this.OptMin = OptMin;
    }
    
    public void initiateVars() {
        GaMain = new singleThreadGAwithEDA3_2();//singleThreadGA singleThreadGAwithSecondFront singleThreadGAwithMultipleCrossover adaptiveGA
        Population = new population();
        Selection = new binaryTournament();//binaryTournament
        Crossover = new twoPointCrossover2EDA2();//twoPointCrossover2 oneByOneChromosomeCrossover twoPointCrossover2withAdpative twoPointCrossover2withAdpativeThreshold
        Mutation = new swapMutationEDA2();//shiftMutation shiftMutationWithAdaptive shiftMutationWithAdaptiveThreshold
        ObjectiveFunction = new ObjectiveFunctionScheduleI[numberOfObjs];
        ObjectiveFunction[0] = new ObjectiveEarlinessTardinessPenalty();
        Fitness = new singleObjectiveFitness();
        objectiveMinimization = new boolean[numberOfObjs];
        objectiveMinimization[0] = true;
        encodeType = true;
        //clone1 = new solutionVectorCloneWithMutation();//swap mutation
        //GaMain.setCloneOperatpr(clone1, true);
        //set schedule data to the objectives
        ObjectiveFunction[0].setScheduleData(dueDay , processingTime, numberOfMachines);
        totalSolnsToExamine = 125000;
        DEFAULT_PopSize = 100;
        //System.out.println(DEFAULT_PopSize);
        //System.exit(0);
        DEFAULT_generations = totalSolnsToExamine / (DEFAULT_PopSize);
        //set the data to the GA main program.
        GaMain.setData(Population, Selection, Crossover, Mutation, ObjectiveFunction, Fitness, DEFAULT_generations, DEFAULT_PopSize, DEFAULT_PopSize,
                numberOfJob, DEFAULT_crossoverRate, DEFAULT_mutationRate, objectiveMinimization, numberOfObjs, encodeType, elitism);
        GaMain.setSecondaryCrossoverOperator(Crossover2, false);
        GaMain.setSecondaryMutationOperator(Mutation2, false);

        GaMain.setEDAinfo(lamda, beta, numberOfCrossoverTournament, numberOfMutationTournament, startingGenDividen);  //startingGenDividen here is as interval of EDA
        GaMain.setD1(this.D1);
        GaMain.setD2(this.D2);
        GaMain.setOptMin(this.OptMin);
    }

    public void startMain() {
        openga.util.timeClock timeClock1 = new openga.util.timeClock();
        timeClock1.start();
        GaMain.startGA();
        timeClock1.end();
        //to output the implementation result.
        String implementResult = "";
        int bestInd = getBestSolnIndex(GaMain.getArchieve());
        implementResult = fileName + "\t" + lamda + "\t" + beta + "\t" + numberOfCrossoverTournament + "\t" + numberOfMutationTournament + "\t" + startingGenDividen + "\t" + GaMain.getArchieve().getSingleChromosome(bestInd).getObjValue()[0] + "\t" + timeClock1.getExecutionTime() / 1000.0 + "\n";
        writeFile("singleMachineEDA3_test", implementResult);
        System.out.print(implementResult);
    }

    public static void main(String[] args) {
//        System.out.println("singleMachineEDA_SKS_20080330");
        //openga.applications.data.singleMachine singleMachineData = new openga.applications.data.singleMachine();
//        int jobSets[] = new int[]{20,50,90};//20, 30, 40, 50, 60, 90, 100, 200//20, 40, 60, 80 //20,30,40,50,60,90//20,50,90

//        int jobSets[] = new int[]{90};//20, 30, 40, 50, 60, 90, 100, 200//20, 40, 60, 80 //20,30,40,50,60,90//20,50,90
        int jobSets[] = new int[]{100,200};//bky


        int counter = 0;
        int repeatExperiments = 50;//3

        int popSize[] = new int[]{100};//50, 100, 155, 210 [100]
        double crossoverRate[] = new double[]{0.6, 0.9},//0.6, 0.9 {0.9}
                mutationRate[] = new double[]{0.1, 0.5},//0.1, 0.5 {0.5}
                elitism = 0.1;

        //EDA parameters.
        double lamdalearningrate[] = new double[]{0.1}; //0.1, 0.5, 0.9
        double betalearningrate[] = new double[]{0.9};   //0.1, 0.5, 0.9
        int numberOfCrossoverTournament[] = new int[]{1};//{1, 2, 4} //2
        int numberOfMutationTournament[] = new int[]{4};//{1, 2, 4}  //4
        int startingGenDividen[] = new int[]{7};//{2, 4}  //2
        
//        int D1[] = new int[]{0,1,2,9};//n/10 , 9,10,20  , 0,1,2,10
//        int D2[] = new int[]{0,1,2,9};//n/10 , 9,10,20  , 0,1,2,10
        int D1[] = new int[]{0,1,2,10};//n/10 , 9,10,20  , 0,1,2,10
        int D2[] = new int[]{0,1,2,10};//n/10 , 9,10,20  , 0,1,2,10
        int optMin = 0;


        for (int j = 0; j < jobSets.length; j++) {//jobSets.length
//            for (int k = 0; k < 3; k++) {  //49
              for (int k = 0; k < 1; k++) {//bky
//                if (jobSets[j] <= 50 || (jobSets[j] > 50 && k < 9)) {
                    if ((jobSets[j] <= 50 && (k == 0 || k == 3 || k == 6 || k == 21 || k == 24 || k == 27 || k == 42 || k == 45 || k == 48)) || (jobSets[j] > 50 && k < 9)) {
                    //if((jobSets[j] <= 50 && (k != 0 && k != 3 && k != 6 && k != 21 && k != 24 && k != 27 && k != 42 && k != 45 && k != 48)) ||  (jobSets[j] > 50 && k < 9)){
                    for (int lx = 0; lx < lamdalearningrate.length; lx++) {
                        for (int bx = 0; bx < betalearningrate.length; bx++) {
                            for (int m = 0; m < numberOfCrossoverTournament.length; m++) {
                                for (int n = 0; n < numberOfMutationTournament.length; n++) {
                                    for (int p = 0; p < startingGenDividen.length; p++) {
                                      
                                      for(int D1Count = 0 ; D1Count < D1.length ; D1Count++) {
                                        for(int D2Count = 0 ; D2Count < D2.length ; D2Count++) {
                                          
                                          int numberOfJobs = jobSets[j];
                                        
                                          /*===sks===*/
//                                          openga.applications.data.singleMachine readSingleMachineData1 = new openga.applications.data.singleMachine();
//                                          String fileName = readSingleMachineData1.getFileName(numberOfJobs, k);
//                                          readSingleMachineData1.setData("sks/" + fileName + ".txt");
//                                          readSingleMachineData1.getDataFromFile();

                                          /*===bky===*/
                                          openga.applications.data.readSingleMachine readSingleMachineData1 = new openga.applications.data.readSingleMachine();
                                          String fileName = readSingleMachineData1.getFileName(numberOfJobs, k+1);
                                          readSingleMachineData1.setData("sks/" + fileName + ".txt");
                                          readSingleMachineData1.getDataFromFile();

  //                                        System.out.print(fileName + "\t");
                                          int dueDate[] = readSingleMachineData1.getDueDate();
                                          int processingTime[] = readSingleMachineData1.getPtime();

                                          for (int i = 0; i < repeatExperiments; i++) {
  //                                            System.out.println("Combinations: " + counter);
                                              singleMachineEDA3_2 singleMachine1 = new singleMachineEDA3_2();
                                              singleMachine1.setData(numberOfJobs, dueDate, processingTime,fileName);
                                              singleMachine1.setEDAinfo(lamdalearningrate[lx], betalearningrate[bx], numberOfCrossoverTournament[m], numberOfMutationTournament[n], startingGenDividen[p] , D1[D1Count], D2[D2Count] , optMin);
                                              singleMachine1.initiateVars();
                                              singleMachine1.startMain();
                                              counter++;
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
        }
    }
}
