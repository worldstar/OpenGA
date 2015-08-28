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

/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */
public class singleMachineEDA2 extends singleMachine {

    public singleMachineEDA2() {
    }
    
    public EDAMainI GaMain;
    public EDAICrossover Crossover;
    public EDAIMutation Mutation;
    public double lamda = 0.9; //learning rate
    public double beta = 0.9;
    public int numberOfCrossoverTournament = 2;
    public int numberOfMutationTournament = 2;
    public int startingGenDividen = 3;

    public void setEDAinfo(double lamda, double beta, int numberOfCrossoverTournament, int numberOfMutationTournament, int startingGenDividen) {
        this.lamda = lamda;
        this.beta = beta;
        this.numberOfCrossoverTournament = numberOfCrossoverTournament;
        this.numberOfMutationTournament = numberOfMutationTournament;
        this.startingGenDividen = startingGenDividen;
    }

    public void initiateVars() {
        GaMain = new singleThreadGAwithEDA2();//singleThreadGA singleThreadGAwithSecondFront singleThreadGAwithMultipleCrossover adaptiveGA
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
        ObjectiveFunction[0].setScheduleData(processingTime, numberOfMachines);
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
        writeFile("singleMachineEDA2_test", implementResult);
        System.out.print(implementResult);
    }

    /**
     * For single objective problem
     * @param arch1
     * @return
     */
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

    public static void main(String[] args) {
        System.out.println("singleMachineEDA_SKS_20080330");
        //openga.applications.data.singleMachine singleMachineData = new openga.applications.data.singleMachine();
        int jobSets[] = new int[]{60};//20, 30, 40, 50, 60, 90, 100, 200//20, 40, 60, 80
        int counter = 0;
        int repeatExperiments = 30;

        int popSize[] = new int[]{100};//50, 100, 155, 210 [100]
        double crossoverRate[] = new double[]{0.9},//0.6, 0.9 {0.9}
                mutationRate[] = new double[]{0.5},//0.1, 0.5 {0.5}
                elitism = 0.1;

        //EDA parameters.
        double lamdalearningrate[] = new double[]{0.1}; //0.1
        double betalearningrate[] = new double[]{0.9};   //0.9
        int numberOfCrossoverTournament[] = new int[]{4};//{1, 2, 4} //2
        int numberOfMutationTournament[] = new int[]{2};//{1, 2, 4}  //4
        int startingGenDividen[] = new int[]{10};//{2, 4}  //2


        for (int j = 0; j < jobSets.length; j++) {//jobSets.length
            for (int k = 4; k < 5; k++) {  //49
                if (jobSets[j] <= 50 || (jobSets[j] > 50 && k < 9)) {
                    //if ((jobSets[j] <= 50 && (k == 0 || k == 3 || k == 6 || k == 21 || k == 24 || k == 27 || k == 42 || k == 45 || k == 48)) || (jobSets[j] > 50 && k < 9)) {
                    //if((jobSets[j] <= 50 && (k != 0 && k != 3 && k != 6 && k != 21 && k != 24 && k != 27 && k != 42 && k != 45 && k != 48)) ||  (jobSets[j] > 50 && k < 9)){
                    for (int lx = 0; lx < lamdalearningrate.length; lx++) {
                        for (int bx = 0; bx < betalearningrate.length; bx++) {
                            for (int m = 0; m < numberOfCrossoverTournament.length; m++) {
                                for (int n = 0; n < numberOfMutationTournament.length; n++) {
                                    for (int p = 0; p < startingGenDividen.length; p++) {
                                        openga.applications.data.singleMachine readSingleMachineData1 = new openga.applications.data.singleMachine();
                                        int numberOfJobs = jobSets[j];
                                        String fileName = readSingleMachineData1.getFileName(numberOfJobs, k);
                                        System.out.print(fileName + "\t");
                                        readSingleMachineData1.setData("sks/" + fileName + ".txt");
                                        readSingleMachineData1.getDataFromFile();
                                        int dueDate[] = readSingleMachineData1.getDueDate();
                                        int processingTime[] = readSingleMachineData1.getPtime();

                                        for (int i = 0; i < repeatExperiments; i++) {
                                            System.out.println("Combinations: " + counter);
                                            singleMachineEDA2 singleMachine1 = new singleMachineEDA2();
                                            singleMachine1.setData(numberOfJobs, dueDate, processingTime,
                                                    fileName);
                                            singleMachine1.setEDAinfo(lamdalearningrate[lx], betalearningrate[bx], numberOfCrossoverTournament[m], numberOfMutationTournament[n], startingGenDividen[p]);
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


        /*
        //Sourd Instance
        for(int i = 0 ; i < repeatExperiments ; i ++ ){
        for(int m = 0 ; m < jobSets.length ; m ++ ){//jobSets.length
        for(int k = 0 ; k < 9 ; k ++ ){
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
        singleMachine1.startMain();
        counter ++;
        }
        }
        }//end for
        System.exit(0);
         */
        /*
        //BKY Instance
        int incrementSteps = 5;
        for(int i = 0 ; i < repeatExperiments ; i ++ ){
        for(int j = 0 ; j < jobSets.length ; j ++ ){//jobSets.length
        for(int k = 0 ; k < popSize.length ; k ++ ){
        for (int m = 0; m < crossoverRate.length; m++) {
        for (int n = 0; n < mutationRate.length; n++) {
        for(int p = 296 ; p < 297 ; ){
        openga.applications.data.readSingleMachine readSingleMachineData1 = new openga.applications.data.readSingleMachine();
        int numberOfJobs = jobSets[j];
        String fileName = readSingleMachineData1.getFileName(numberOfJobs, p);
        readSingleMachineData1.setData("instances/SingleMachineBKS/"+fileName+".txt");
        if(readSingleMachineData1.testReadData()){//to test whether the file exist
        System.out.print("Combinations: "+counter+"\t");
        System.out.print(fileName+"\t");
        readSingleMachineData1.getDataFromFile();
        int dueDate[] = readSingleMachineData1.getDueDate();
        int processingTime[] = readSingleMachineData1.getPtime();

        singleMachine1.setData(numberOfJobs, dueDate, processingTime, fileName);
        singleMachine1.setParameters(popSize[k], crossoverRate[m], mutationRate[n], 100000);
        singleMachine1.initiateVars();
        singleMachine1.startMain();
        counter ++;
        }
        p += incrementSteps;
        }
        }
        }
        }
        }
        }//end for
         */
    }
}
