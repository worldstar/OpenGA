package openga.applications.singleMachineProblem;
import openga.chromosomes.*;
import openga.operator.selection.*;
import openga.operator.crossover.*;
import openga.operator.mutation.*;
import openga.ObjectiveFunctions.*;
import openga.MainProgram.*;
import openga.ObjectiveFunctions.*;
import openga.Fitness.*;
import openga.util.printClass;
import openga.applications.singleMachine;
import openga.util.fileWrite1;

/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class singleMachineSetupDynamicArrivalSGA extends singleMachine{
  public singleMachineSetupDynamicArrivalSGA() {
  }
    int processingTime[];
    int setupTime[][];
    int dynamicArrivalTime[];
    
    ObjectiveFunctionMatrixPTimeScheduleI ObjectiveFunction[];
    
    public void setData(int numberOfJobs, int processingTime[], int setupTime[][], 
            int dynamicArrivalTime[], String fileName){
      this.numberOfJob = numberOfJobs;      
      this.processingTime = processingTime;
      this.setupTime = setupTime;
      this.dynamicArrivalTime = dynamicArrivalTime;
      this.fileName = fileName;      
      length = numberOfJob;
    } 
    
  public void initiateVars(){
    GaMain     = new singleThreadGA();//singleThreadGAwithSecondFront singleThreadGAwithMultipleCrossover
    Population = new population();
    Selection  = new binaryTournament();
    Crossover  = new twoPointCrossover2();
    Crossover2 = new PMX();
    Mutation   = new shiftMutation();//shiftMutation swapMutation swapMutation2Threads
    Mutation2  = new inverseMutation();
    ObjectiveFunction = new ObjectiveFunctionMatrixPTimeScheduleI[numberOfObjs];
    ObjectiveFunction[0] = new ObjectiveETPenaltyDynamicArrval();//the first objective, tardiness, ObjectiveTardiness ObjectiveEarlinessTardinessPenalty
    //ObjectiveFunction[1] = new ObjectiveTardinessForFlowShop();//the second one.
    Fitness    = new GoldbergFitnessAssignment();
    objectiveMinimization = new boolean[numberOfObjs];
    objectiveMinimization[0] = true;
    //objectiveMinimization[1] = true;
    encodeType = true;

    //set schedule data to the objectives
    ObjectiveFunction[0].setScheduleData(processingTime, numberOfMachines);
    ObjectiveFunction[0].setScheduleData(setupTime, numberOfMachines);//We pass the setup time here.
    ((dynamicArrivalTimeI)ObjectiveFunction[0]).setDynamicArrivalTime(dynamicArrivalTime);

    //set the data to the GA main program.
    GaMain.setData(Population, Selection, Crossover, Mutation, ObjectiveFunction, Fitness, DEFAULT_generations, DEFAULT_initPopSize,DEFAULT_PopSize,
                   numberOfJob, DEFAULT_crossoverRate, DEFAULT_mutationRate, objectiveMinimization, numberOfObjs, encodeType, elitism);
    GaMain.setSecondaryCrossoverOperator(Crossover2, true);
    GaMain.setSecondaryMutationOperator(Mutation2, true);
  }

  public void startMain(){
    openga.util.timeClock timeClock1 = new openga.util.timeClock();
    timeClock1.start();
    GaMain.startGA();
    timeClock1.end();
    //System.out.println("\nThe final result");
    int bestInd = getBestSolnIndex(GaMain.getArchieve());
    String implementationResult = "";
    String fileNameArray[] = fileName.split("/");
    fileName = fileNameArray[2] + "\t"+ fileNameArray[3].substring(0, fileNameArray[3].indexOf("."));    
    implementationResult += fileName+"\t"+numberOfJob+"\t"+"\t" + DEFAULT_PopSize+"\t"+DEFAULT_crossoverRate+"\t"+ DEFAULT_mutationRate+"\t" + GaMain.getArchieve().getSingleChromosome(bestInd).getObjValue()[0]+"\t"+timeClock1.getExecutionTime()/1000.0+"\n";
    writeFile("singleMachineSetupDynamicArrivalGA_20151006", implementationResult);
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
  /**
   * Write the data into text file.
   */
  public void writeFile(String fileName, String _result){
    fileWrite1 writeLotteryResult = new fileWrite1();
    writeLotteryResult.writeToFile(_result,fileName+".txt");
    Thread thread1 = new Thread(writeLotteryResult);
    thread1.run();
  }

  public static void main(String[] args) {
    System.out.println("singleMachineSetupDynamicArrivalGA_20151006");
    //openga.applications.data.singleMachine singleMachineData = new openga.applications.data.singleMachine();
        int jobSets[] = new int[]{50};//10, 15, 20, 25, 50, 100, 150, 200
        int instanceReplication = 15;
        String types[] = new String[]{"low", "med", "high"};//
    int counter = 0;
    int repeatExperiments = 1;

    int popSize[] = new int[]{100};//50, 100, 155, 210 [100]
    double crossoverRate[] = new double[]{0.6, 0.9},//0.6, 0.9 {0.9}
           mutationRate [] = new double[]{0.1, 0.5},//0.1, 0.5 {0.5}
           elitism = 0.2;
    
    for (int j = 0; j < jobSets.length; j++) {//jobSets.length
        for (int k = 1; k <= instanceReplication; k++) { 
          for(int a = 0 ; a < types.length ; a ++){
            openga.applications.data.singleMachineSetupDynamicData readSingleMachineData1 = 
                    new openga.applications.data.singleMachineSetupDynamicData();
            int numberOfJobs = jobSets[j];
            String fileName = "instances/SingleMachineSetupDynamicArrival/"+types[a]+"/"+jobSets[j]+"_"+k+".etp";
            System.out.print(fileName + "\t");
            readSingleMachineData1.setData(fileName, jobSets[j]);
            readSingleMachineData1.getDataFromFile();                                        
            int processingTime[] = readSingleMachineData1.getProcessingTime();
            int setupTime[][] = readSingleMachineData1.getSetupTime();
            int dynamicArrivalTime[] = readSingleMachineData1.getDynamicArrivalTime();   
            
            for (int m = 0; m < crossoverRate.length; m++) {
              for (int n = 0; n < mutationRate.length; n++) {
                for (int i = 0; i < repeatExperiments; i++) {
                    System.out.println("Combinations: " + counter);
                    singleMachineSetupDynamicArrivalSGA singleMachine1 = new singleMachineSetupDynamicArrivalSGA();
                    singleMachine1.setData(numberOfJobs, processingTime, setupTime, dynamicArrivalTime, fileName);		    
                    singleMachine1.setParameters(popSize[0], crossoverRate[m], mutationRate[n], 125000);
                    singleMachine1.initiateVars();
                    singleMachine1.startMain();
                    counter++;
                }                                                
              }
            }                              
          }
        }
    }    
    System.exit(0);
  }
}
