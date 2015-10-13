package openga.applications.singleMachineProblem;

import openga.chromosomes.*;
import openga.operator.crossover.*;
import openga.ObjectiveFunctions.*;
import openga.Fitness.*;

/**
 * <p>Title: Inver-Over for the single machine scheduling problem with setup and dynamic arrival
 * time in a common due day environment.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Cheng Shiu University University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */
public class singleMachineSetupDynamicArrivalInverOver extends singleMachineEDA2 {

    public singleMachineSetupDynamicArrivalInverOver() {
        
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

    public void startMain() {
        openga.util.timeClock timeClock1 = new openga.util.timeClock();
        timeClock1.start();

        inverOverOperator inverover1 = new inverOverOperator();
        populationI population1 = new population();
        population1.setGenotypeSizeAndLength(true, DEFAULT_PopSize, length, 1);
        population1.createNewPop();     
        
        ObjectiveFunction = new ObjectiveFunctionMatrixPTimeScheduleI[numberOfObjs];
        ObjectiveFunction[0] = new ObjectiveETPenaltyDynamicArrval();
        Fitness = new singleObjectiveFitness();
        objectiveMinimization = new boolean[numberOfObjs];
        objectiveMinimization[0] = true;
        encodeType = true;        
        
        ObjectiveFunction[0].setScheduleData(processingTime, numberOfMachines);
        ObjectiveFunction[0].setScheduleData(setupTime, numberOfMachines);//We pass the setup time here.
        ((dynamicArrivalTimeI)ObjectiveFunction[0]).setDynamicArrivalTime(dynamicArrivalTime);        
        totalSolnsToExamine = 125000;
        
        inverover1.setData(0.02, population1);
        inverover1.setObjectives(ObjectiveFunction);
        ((InverOverOperatorI)inverover1).setTotalSolutions(totalSolnsToExamine);
        inverover1.startCrossover();
        population1 = inverover1.getCrossoverResult();        

        timeClock1.end();
        //to output the implementation result.
        String implementResult = "";
        int bestInd = getBestSolnIndex(population1);
        
        String fileNameArray[] = fileName.split("/");
        fileName = fileNameArray[2] + "\t"+ fileNameArray[3].substring(0, fileNameArray[3].indexOf("."));
        implementResult = fileName + "\t"               
                + population1.getSingleChromosome(bestInd).getObjValue()[0] 
                + "\t" + timeClock1.getExecutionTime() / 1000.0  
                + "\t" + population1.getSingleChromosome(bestInd).toString1() +"\n";
        writeFile("singleMachineDynamicArrivalInverOver_20151005", implementResult);
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
        for (int k = 0; k < arch1.getPopulationSize(); k++) {
            if (bestobj > arch1.getObjectiveValues(k)[0]) {
                bestobj = arch1.getObjectiveValues(k)[0];
                index = k;
            }
        }
        return index;
    }    

    public static void main(String[] args) {
        System.out.println("singleMachineDynamicArrivalInverOver_20151005");
        //openga.applications.data.singleMachine singleMachineData = new openga.applications.data.singleMachine();
        int jobSets[] = new int[]{10, 15, 20, 25, 50, 100, 150, 200};//10, 15, 20, 25, 50, 100, 150, 200
        int instanceReplication = 15;
        String types[] = new String[]{"low", "med", "high"};//
        int counter = 0;
        int repeatExperiments = 2;

        int popSize[] = new int[]{100};//50, 100, 155, 210 [100]
        double crossoverRate[] = new double[]{0.9},//0.6, 0.9 {0.9}
                mutationRate[] = new double[]{0.5},//0.1, 0.5 {0.5}
                elitism = 0.1;

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
                                
                for (int i = 0; i < repeatExperiments; i++) {
		    System.out.println("Combinations: " + counter);
		    singleMachineSetupDynamicArrivalInverOver singleMachine1 = new singleMachineSetupDynamicArrivalInverOver();
		    singleMachine1.setData(numberOfJobs, processingTime, setupTime, dynamicArrivalTime, fileName);		    
		    singleMachine1.startMain();
		    counter++;
		}                              
              }
            }
        }
    }
}
