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
 * <p>Title: eSGGA2 for the single machine scheduling problem with setup and dynamic arrival
 * time in a common due day environment.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */
public class singleMachineSetupDynamicArrivalEDA2 extends singleMachineEDA2 {

    public singleMachineSetupDynamicArrivalEDA2() {
        
    }
    
    int processingTime[][];
    int dynamicArrivalTime[];
    
    ObjectiveFunctionMatrixPTimeScheduleI ObjectiveFunction[];
    
    public void setData(int numberOfJobs, int processingTime[][], int dynamicArrivalTime[], String fileName){
      this.numberOfJob = numberOfJobs;      
      this.processingTime = processingTime;
      this.dynamicArrivalTime = dynamicArrivalTime;
      this.fileName = fileName;
    }    

    public void initiateVars() {
        GaMain = new singleThreadGAwithEDA2();//singleThreadGA singleThreadGAwithSecondFront singleThreadGAwithMultipleCrossover adaptiveGA
        Population = new population();
        Selection = new binaryTournament();//binaryTournament
        Crossover = new twoPointCrossover2EDA2();//twoPointCrossover2 oneByOneChromosomeCrossover twoPointCrossover2withAdpative twoPointCrossover2withAdpativeThreshold
        Mutation = new swapMutationEDA2();//shiftMutation shiftMutationWithAdaptive shiftMutationWithAdaptiveThreshold
        ObjectiveFunction = new ObjectiveFunctionMatrixPTimeScheduleI[numberOfObjs];
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
        System.out.println("singleMachineEDA2_Rabadi_20150825");
        //openga.applications.data.singleMachine singleMachineData = new openga.applications.data.singleMachine();
        int jobSets[] = new int[]{10, 15, 20, 25, 50, 100, 150, 200};//10, 15, 20, 25, 50, 100, 150, 200//20, 30, 40, 50, 60, 90, 100, 200//20, 40, 60, 80
        int instanceReplication = 15;
        String types[] = new String[]{"low", "med", "high"};
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
            for (int k = 0; k < instanceReplication; k++) { 
              for(int a = 0 ; a < types.length ; a ++){
                openga.applications.data.singleMachineSetupDynamicData readSingleMachineData1 = new openga.applications.data.singleMachineSetupDynamicData();
                int numberOfJobs = jobSets[j];
                String fileName = "instances/SingleMachineSetup/high/"+jobSets[j]+"_"+k+".etp";
                System.out.print(fileName + "\t");
                readSingleMachineData1.setData(fileName, jobSets[j]);
                readSingleMachineData1.getDataFromFile();                                        
                int processingTime[][] = readSingleMachineData1.getProcessingTime();
                int dynamicArrivalTime[] = readSingleMachineData1.getDynamicArrivalTime();   
                
                for (int lx = 0; lx < lamdalearningrate.length; lx++) {
                    for (int bx = 0; bx < betalearningrate.length; bx++) {
                        for (int m = 0; m < numberOfCrossoverTournament.length; m++) {
                            for (int n = 0; n < numberOfMutationTournament.length; n++) {
                                for (int p = 0; p < startingGenDividen.length; p++) {
                                    for (int i = 0; i < repeatExperiments; i++) {
                                        System.out.println("Combinations: " + counter);
                                        singleMachineSetupDynamicArrivalEDA2 singleMachine1 = new singleMachineSetupDynamicArrivalEDA2();
                                        singleMachine1.setData(numberOfJobs, processingTime, dynamicArrivalTime, fileName);
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
    }
}
