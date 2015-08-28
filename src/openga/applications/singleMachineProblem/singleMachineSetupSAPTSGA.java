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
public class singleMachineSetupSAPTSGA extends singleMachineSetup {

    public singleMachineSetupSAPTSGA() {
    }
    cloneI clone1;
    public ObjectiveFunctionSchedule2I ObjectiveFunction[];
    public double processingTime[][];
    public int numberOfJobs;
    public String fileName;
    public int totalSolnsToExamine = 100000;
    double obj0 = 0;

    public void setData(int numberOfJobs, double processingTime[][], String fileName) {
        this.numberOfJobs = numberOfJobs;
        numberOfJob = numberOfJobs;
        this.processingTime = processingTime;
        this.fileName = fileName;

    }

    public void initiateVars() {
        GaMain = new singleThreadGAwithInitialPop();
        Population = new population();
        Selection = new binaryTournament();
        Crossover = new twoPointCrossover2();
        Crossover2 = new PMX();
        Mutation = new swapMutation();
        Mutation2 = new inverseMutation();
        ObjectiveFunction = new ObjectiveFunctionSchedule2I[numberOfObjs];
        ObjectiveFunction[0] = new ObjectiveEiTiSetupCommonDueForSingleMachine();
        //ObjectiveFunction[1] = new ObjectiveTardinessForFlowShop();//the second one.
        Fitness = new singleObjectiveFitness();
        objectiveMinimization = new boolean[numberOfObjs];
        objectiveMinimization[0] = true;
        //objectiveMinimization[1] = true;
        encodeType = true;

        //set schedule data to the objectives
        ObjectiveFunction[0].setScheduleData(processingTime, numberOfMachines);
        clone1 = new solutionVectorCloneWithMutation();//swap mutation
        DEFAULT_generations = totalSolnsToExamine / (DEFAULT_PopSize);
        //set the data to the GA main program.
        GaMain.setData(Population, Selection, Crossover, Mutation, ObjectiveFunction, Fitness, DEFAULT_generations, DEFAULT_initPopSize, DEFAULT_PopSize,
                numberOfJob, DEFAULT_crossoverRate, DEFAULT_mutationRate, objectiveMinimization, numberOfObjs, encodeType, elitism);
        GaMain.setSecondaryCrossoverOperator(Crossover2, false);
        GaMain.setSecondaryMutationOperator(Mutation2, false);
    }

    public void startMain() {
        openga.util.timeClock timeClock1 = new openga.util.timeClock();
        GaMain.initialStage();
        timeClock1.start();
        GaMain.setCloneOperatpr(clone1, true);
        constructInitialSolutionsSAPT(Population);
        GaMain.startGA();
        timeClock1.end();
        //System.out.println("\nThe final result");
        int bestInd = getBestSolnIndex(GaMain.getArchieve());
        String implementationResult = "";
        implementationResult += fileName + "\t" + numberOfJob + "\t" + GaMain.getArchieve().getSingleChromosome(bestInd).getObjValue()[0] + "\t" + timeClock1.getExecutionTime() / 1000.0 + "\n";
        writeFile("SingleMachineSetupSAPTSGA_all_2011", implementationResult);
        System.out.println(implementationResult);
    }

    public void constructInitialSolutionsSAPT(populationI _Population) {

        double bestobj = Double.MAX_VALUE;
        String result1 = "";

        double currentbestobj = Double.MAX_VALUE;
        int currentBestSequence[] = new int[numberOfJob];
        int currentSoluion[] = new int[numberOfJob];
        int BestSequence[] = new int[numberOfJob];
        double currentBestObj = Double.MAX_VALUE;

        openga.applications.SAPT_SingleMachineSetup singleMachine1 = new openga.applications.SAPT_SingleMachineSetup();
        singleMachine1.setData(numberOfJobs, processingTime);
        for (int aaa = 0; aaa < (numberOfJob); aaa++) {
            singleMachine1.setgpitimes();
            int times = singleMachine1.getsmallesTimes(processingTime);
//            System.out.println("times" + times);
            singleMachine1.setindex();
            for (int t = 0; t < times; t++) { //i initial solutions
                if (t == 0) {
                    singleMachine1.backupAP();
                } else {
                    singleMachine1.restoreAP();
                }

                singleMachine1.SAPT();
//                System.out.println("first stategy");
//                singleMachine1.printSequence(singleMachine1.getSequence());

                singleMachine1.calcObj(singleMachine1.getSequence());
                currentSoluion = singleMachine1.getSequence();
                currentBestSequence = singleMachine1.GPI(currentSoluion);
                singleMachine1.calcObj(currentBestSequence);
//            result1 = "obj " +"\t"+singleMachine1.getObjValue()+"\n";
//                System.out.println(result1);
//                currentSoluion = singleMachine1.GPI(singleMachine1.getSequence());
//                singleMachine1.calcObj(currentSoluion);
//                System.out.println("second stategy");
//                singleMachine1.printSequence(currentSoluion);

//            result1 = "obj " +"\t"+singleMachine1.getObjValue()+"\n";
//                System.out.println(result1);

                if (currentbestobj > singleMachine1.getObjValue()) {
                    currentbestobj = singleMachine1.getObjValue();
                    for (int b = 0; b < numberOfJobs; b++) {
                        currentBestSequence[b] = currentSoluion[b];
                    }
                }

                //               currentBestSequence = singleMachine1.GPI(currentBestSequence);
//                singleMachine1.calcObj(currentBestSequence);
//                currentbestobj = singleMachine1.getObjValue();

            }
            //singleMachine1.calcObj(currentBestSequence);
            //currentbestobj = singleMachine1.getObjValue();

            currentBestSequence = singleMachine1.GPI(currentBestSequence);
            singleMachine1.calcObj(currentBestSequence);
            currentbestobj = singleMachine1.getObjValue();
//            lstimes = lstimes + singleMachine1.getgpitimes();
            if (bestobj > currentbestobj) {
                bestobj = currentbestobj;
                for (int b = 0; b < numberOfJobs; b++) {
                    BestSequence[b] = currentBestSequence[b];
                }
            }



//            lstimes = lstimes + singleMachine1.getgpitimes();
        }
//        lstimes = lstimes + singleMachine1.getgpitimes();
//        singleMachine1.printSequence(currentBestSequence);
//        _Population.getSingleChromosome(i).setSolution(currentBestSequence);
//        System.out.print(_Population.getSingleChromosome(i).toString1());
//        System.exit(0);
/*
        if(bestobj > currentbestobj){
        bestobj = currentbestobj;
        for(int b = 0 ; b < numberOfJobs ; b ++ ){
        BestSequence[b] = currentBestSequence[b];
        }
        //          }
        
        }
         */
        //       lstimes = singleMachine1.getgpitimes();
//        System.out.println(lstimes);
        obj0 = bestobj;
        _Population.getSingleChromosome(0).setSolution(BestSequence);

//       System.exit(0);



//        System.out.println("final stategy");
//        result1 = "obj " + "\t" + bestobj + "\n";
//        System.out.println(result1);
//        System.exit(0);



//        SAMain.calcObjectiveValue(chromosome1, new int[]{0, 0}, 0);
//        System.out.println(chromosome1.getObjectiveValue()[0]);
//        System.exit(0);
    }

    public static void main(String[] args) {
        System.out.println("SingleMachineSetupDP_2011");

        int counter = 0;
        int repeatExperiments = 30;
        int jobSets[] = new int[]{10};//10, 15, 20, 25, 50, 100, 150, 200
        String type[] = new String[]{"high"};//"low", "med", "high"

        double crossoverRate[] = new double[]{0.5},//0.8, 0.5 (0.5)
                mutationRate[] = new double[]{0.3};//0.6, 0.3  (0.3)
        int populationSize[] = new int[]{100};//200, 100


        for (int m = 0; m < jobSets.length; m++) {//jobSets.length    
            for (int j = 0; j < type.length; j++) {
                for (int k = 1; k <= 15; k++) {
                    System.out.println("Combinations: " + counter);
                    singleMachineSetupSAPTSGA singleMachine1 = new singleMachineSetupSAPTSGA();
                    openga.applications.data.singleMachineSetupData singleMachineData1 = new openga.applications.data.singleMachineSetupData();
                    String fileName = "instances\\SingleMachineSetup\\" + type[j] + "\\" + jobSets[m] + "_" + k + ".etp";
                    //fileName = "Data\\SMSetupTime8.txt";//for test
                    singleMachineData1.setData(fileName);
                    singleMachineData1.getDataFromFile();
                    int numberOfJobs = singleMachineData1.getSize();
                    double processingTime[][] = singleMachineData1.getProcessingTime();

                    for (int i = 0; i < repeatExperiments; i++) {
                        singleMachine1.setData(numberOfJobs, processingTime, fileName);
                        singleMachine1.setParameters(populationSize[0], crossoverRate[0], mutationRate[0], 1);
                        singleMachine1.initiateVars();
                        singleMachine1.startMain();
                        counter++;
                    }
                }
            }
        }
    }
}
