package openga.applications.singleMachineProblem;

import openga.chromosomes.*;
import openga.operator.selection.*;
import openga.operator.crossover.*;
import openga.operator.mutation.*;
import openga.ObjectiveFunctions.*;
import openga.ObjectiveFunctions.ObjectiveETPenaltyDynamicArrval;
import openga.MainProgram.*;
import openga.Fitness.*;
import openga.applications.singleMachine;
import openga.operator.clone.*;

/**
 * <p>Title: ACGA2 for the single machine scheduling problem with setup and dynamic arrival
 * time in a common due day environment.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Cheng Shiu University University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */
public class singleMachineSetupDynamicArrivalACGA2 extends singleMachine {

    public singleMachineSetupDynamicArrivalACGA2() {
        
    }
    
    int processingTime[];
    int setupTime[][];
    int dynamicArrivalTime[];
    
    ObjectiveFunctionMatrixPTimeScheduleI ObjectiveFunction[];
    
    /***
     * AC2 parameters
     */
    int startingGeneration = 500;
    int interval = 30;
    int strategy = 1;
    boolean applyEvaporation = false;
    String evaporationMethod = "constant";//constant, method1, method2
    double startingGenerationPercent;
    double intervalPercent;
    int replications = 1;
    double lamda = 0;
    
    /***
     * Basic variables of GAs.
     */
    public int numberOfObjs = 1;//it's a bi-objective program.
    probabilityMatrixI GaMain;
    cloneI clone1;
    double beta = 0;
    
    public void setData(int numberOfJobs, int processingTime[], int setupTime[][], 
            int dynamicArrivalTime[], String fileName){
      this.numberOfJob = numberOfJobs;      
      this.processingTime = processingTime;
      this.setupTime = setupTime;
      this.dynamicArrivalTime = dynamicArrivalTime;
      this.fileName = fileName;
    }    
    
    public void setProbabilityMatrixData(int startingGeneration, int interval) {
        this.startingGeneration = startingGeneration;
        this.interval = interval;
    }

    public void setProbabilityMatrixDataPercent(double startingGenerationPercent, double intervalPercent) {
        this.startingGenerationPercent = startingGenerationPercent;
        this.intervalPercent = intervalPercent;
    }

    public void setSequenceStrategy(int strategy) {
        this.strategy = strategy;
    }

    public void setLearning(double lamda, double beta) {
        this.lamda = lamda;
        this.beta = beta;
    }
    
    public void setEvaporationMethod(boolean applyEvaporation, String evaporationMethod) {
        this.applyEvaporation = applyEvaporation;
        this.evaporationMethod = evaporationMethod;
    }

    public void setReplications(int replications) {
        this.replications = replications;
    }    

    public void initiateVars() {
        GaMain = new singleThreadGAwithProbabilityMatrixIL(); //singleThreadGAwithProbabilityMatrix singleThreadGAwithProbabilityMatrixInitialPop  singleThreadGAwithProbabilityMatrixIL singleThreadGAwithProbabilityMatrixInteraction
        Population = new population();
        Selection = new binaryTournament();//binaryTournament
        Crossover = new twoPointCrossover2();//twoPointCrossover2 oneByOneChromosomeCrossover twoPointCrossover2withAdpative twoPointCrossover2withAdpativeThreshold
        Crossover2 = new PMX();
        
        Mutation = new swapMutation();//shiftMutation shiftMutationWithAdaptive shiftMutationWithAdaptiveThresholdMutation2 = new inverseMutation();
        Mutation2 = new inverseMutation();
        
        ObjectiveFunction = new ObjectiveFunctionMatrixPTimeScheduleI[numberOfObjs];
        ObjectiveFunction[0] = new ObjectiveETPenaltyDynamicArrval();
        Fitness = new singleObjectiveFitness();
        objectiveMinimization = new boolean[numberOfObjs];
        objectiveMinimization[0] = true;
        encodeType = true;
        clone1 = new solutionVectorCloneWithMutation();//swap mutation
        //GaMain.setCloneOperatpr(clone1, true);
        //set schedule data to the objectives
        ObjectiveFunction[0].setScheduleData(processingTime, numberOfMachines);
        ObjectiveFunction[0].setScheduleData(setupTime, numberOfMachines);//We pass the setup time here.
        ((dynamicArrivalTimeI)ObjectiveFunction[0]).setDynamicArrivalTime(dynamicArrivalTime);
        totalSolnsToExamine = 125000;
        DEFAULT_PopSize = 100;
        
        //set the data to the GA main program.
        GaMain.setLearningRate(lamda, beta);  //Interaction Learning rate
        GaMain.setProbabilityMatrixData(startingGeneration, interval);
        GaMain.setSequenceStrategy(strategy);
        GaMain.setEvaporationMethod(applyEvaporation, evaporationMethod);
        DEFAULT_generations = totalSolnsToExamine / (DEFAULT_PopSize);
        
        //System.out.println("DEFAULT_PopSize: "+DEFAULT_PopSize + " "+DEFAULT_generations);
        //System.exit(0);
        
        //set the data to the GA main program.
        GaMain.setData(Population, Selection, Crossover, Mutation, ObjectiveFunction, Fitness, DEFAULT_generations, DEFAULT_PopSize, DEFAULT_PopSize,
                numberOfJob, DEFAULT_crossoverRate, DEFAULT_mutationRate, objectiveMinimization, numberOfObjs, encodeType, elitism);
        GaMain.setCloneOperatpr(clone1, true);
        GaMain.setSecondaryCrossoverOperator(Crossover2, false);
        GaMain.setSecondaryMutationOperator(Mutation2, false);
    }

    public void startMain() {
        openga.util.timeClock timeClock1 = new openga.util.timeClock();
        timeClock1.start();
        GaMain.startGA();
        timeClock1.end();
        //to output the implementation result.
        String implementResult = "";
        int bestInd = getBestSolnIndex(GaMain.getArchieve());
        
        String fileNameArray[] = fileName.split("/");
        fileName = fileNameArray[2] + "\t"+ fileNameArray[3].substring(0, fileNameArray[3].indexOf("."));
        implementResult += fileName + "\t" + startingGenerationPercent + "\t" + intervalPercent + "\t" 
                + DEFAULT_PopSize + "\t" + DEFAULT_crossoverRate + "\t" + DEFAULT_mutationRate + "\t" 
                + lamda + "\t" + beta + "\t" 
                + GaMain.getArchieve().getSingleChromosome(bestInd).getObjValue()[0] + "\t" 
                + timeClock1.getExecutionTime() / 1000.0 + "\n";
        writeFile("singleMachineDynamicArrivalACGA2_20151012", implementResult);
        System.out.println(implementResult);
    }



    public static void main(String[] args) {
        System.out.println("singleMachineDynamicArrivalACGA2_20151012");
        //openga.applications.data.singleMachine singleMachineData = new openga.applications.data.singleMachine();
        int jobSets[] = new int[]{10, 15, 20, 25, 50, 100, 150, 200};//10, 15, 20, 25, 50, 100, 150, 200//20, 30, 40, 50, 60, 90, 100, 200//20, 40, 60, 80
        int instanceReplication = 15;
        String types[] = new String[]{"low", "med", "high"};//"low", "med", "high"
        int counter = 0;
        int repeatExperiments = 1;

        int popSize[] = new int[]{100};//50, 100, 155, 210 [100]
        double crossoverRate[] = new double[]{0.9},//0.6, 0.9 {0.9}
                mutationRate[] = new double[]{0.5},//0.1, 0.5 {0.5}
                elitism = 0.1;

        //EDA parameters.
        int startingGeneration[] = new int[]{200, 500};//200, 500
        double startingGenerationPercent[] = new double[]{0.1, 0.3, 0.5};//0.1, 0.3, 0.5
        int interval[] = new int[]{25, 50};
        double intervalPercent[] = new double[]{0.01, 0.05, 0.1};//0.01, 0.05, 0.1
        int strategy[] = new int[]{0}; //0:sequence, 1:random
        int totalSolnsToExamine = 125000;
        boolean applyEvaporation = false;
        String evaporationMethod[] = new String[]{"method2"};//constant, method1, method2
        double lamdalearningrate[] = new double[]{0.7};//0.9, 0.5, 0.1
        double betalearningrate[] = new double[]{0.9, 0.5, 0.1}; //0.9, 0.5, 0.1           

        for (int j = 0; j < jobSets.length; j++) {//jobSets.length
            for (int k = 1; k <= instanceReplication; k++) { 
              for(int a = 0 ; a < types.length ; a ++){
                openga.applications.data.singleMachineSetupDynamicData readSingleMachineData1 = new openga.applications.data.singleMachineSetupDynamicData();
                int numberOfJobs = jobSets[j];
                String fileName = "instances/SingleMachineSetupDynamicArrival/"+types[a]+"/"+jobSets[j]+"_"+k+".etp";
                System.out.println(fileName + "\t");
                readSingleMachineData1.setData(fileName, jobSets[j]);
                readSingleMachineData1.getDataFromFile();                                        
                int processingTime[] = readSingleMachineData1.getProcessingTime();
                int setupTime[][] = readSingleMachineData1.getSetupTime();
                int dynamicArrivalTime[] = readSingleMachineData1.getDynamicArrivalTime();   
                
                for (int lx = 0; lx < lamdalearningrate.length; lx++) {
                    for (int bx = 0; bx < betalearningrate.length; bx++) {
                      for (int pc = 0; pc < crossoverRate.length; pc++) {
                        for (int pm = 0; pm < mutationRate.length; pm++) {
                          for (int km = 0; km < startingGenerationPercent.length; km++) {
                            for (int m = 0; m < intervalPercent.length; m++) {
                              for (int n = 0; n < strategy.length; n++) {                                
                                for (int i = 0; i < repeatExperiments; i++) {
                                    System.out.println("Combinations: " + counter);                                    
                                    int tempGeneration = totalSolnsToExamine / popSize[0];
                                    if ((int) (tempGeneration * intervalPercent[m]) == 0) {
                                        interval[0] = 2;
                                    } else {
                                        interval[0] = (int) (tempGeneration * intervalPercent[m]) + 2;  // +2
                                    }
                                    
                                    singleMachineSetupDynamicArrivalACGA2 singleMachine1 = new singleMachineSetupDynamicArrivalACGA2();
                                    singleMachine1.setData(numberOfJobs, processingTime, setupTime, dynamicArrivalTime, fileName);
                                    singleMachine1.setProbabilityMatrixData((int) (tempGeneration * startingGenerationPercent[km]), interval[0]);
                                    singleMachine1.setProbabilityMatrixDataPercent(startingGenerationPercent[km], intervalPercent[m]);
                                    singleMachine1.setSequenceStrategy(strategy[n]);
                                    singleMachine1.setLearning(lamdalearningrate[lx], betalearningrate[bx]);                                        
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
