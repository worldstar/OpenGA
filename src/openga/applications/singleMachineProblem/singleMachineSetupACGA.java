package openga.applications.singleMachineProblem;
//import homework.schedule.singleMachine;
import openga.applications.singleMachine;  //ct
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
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class singleMachineSetupACGA extends singleMachineSetup {   //ct
    public singleMachineSetupACGA() {
    }
    int startingGeneration;
    int interval;
    int strategy;
    //cloneI clone1; //ct
    public ObjectiveFunctionSchedule2I ObjectiveFunction[]; //ct
    public double processingTime[][]; //ct
    public int numberOfJobs; //ct
    public String fileName; //ct
    
    probabilityMatrixI GaMain;
    boolean applyEvaporation = false;
    String evaporationMethod = "constant";//constant, method1, method2
    
    public void setProbabilityMatrixData(int startingGeneration, int interval){
        this.startingGeneration = startingGeneration;
        this.interval = interval;
    }
    
    public void setSequenceStrategy(int strategy){
        this.strategy = strategy;
    }
    
    public void setEvaporationMethod(boolean applyEvaporation, String evaporationMethod){
        this.applyEvaporation = applyEvaporation;
        this.evaporationMethod = evaporationMethod;
    }
    
    //ct
    public void setData(int numberOfJobs, double processingTime[][], String fileName){
        this.numberOfJobs = numberOfJobs;
        numberOfJob = numberOfJobs;
        this.processingTime = processingTime;
        this.fileName = fileName;
    }
    //ct
    
    
    public void initiateVars(){
        GaMain     = new singleThreadGAwithProbabilityMatrix();
        Population = new population();
        Selection  = new binaryTournament();
        Crossover  = new twoPointCrossover2();//
        Crossover2 = new PMX();
        
        Mutation   = new swapMutation();
        //Mutation   = new swapMutationWithMining2();//swapMutationWithMining2 shiftMutationWithMining2
        Mutation2  = new inverseMutation();
        
        ObjectiveFunction = new ObjectiveFunctionSchedule2I[numberOfObjs];  //ct
        ObjectiveFunction[0] = new ObjectiveEiTiSetupCommonDueForSingleMachine(); //*ct*  the first objective, tardiness, ObjectiveTardiness ObjectiveEarlinessTardinessPenalty
        Fitness    = new singleObjectiveFitness();
        objectiveMinimization = new boolean[numberOfObjs];
        objectiveMinimization[0] = true;
        //objectiveMinimization[1] = true;
        encodeType = true;
        // clone1  = new solutionVectorCloneWithMutation();//swap mutation  *ct
        //set schedule data to the objectives
        ObjectiveFunction[0].setScheduleData(processingTime, numberOfMachines); //ct
        //set the data to the GA main program.
        DEFAULT_generations = totalSolnsToExamine/(DEFAULT_PopSize);//ct
        
        GaMain.setProbabilityMatrixData(startingGeneration, interval);
        GaMain.setSequenceStrategy(strategy);
        GaMain.setEvaporationMethod(applyEvaporation, evaporationMethod);
        GaMain.setData(Population, Selection, Crossover, Mutation, ObjectiveFunction, Fitness, DEFAULT_generations, DEFAULT_PopSize, DEFAULT_PopSize,
                numberOfJob, DEFAULT_crossoverRate, DEFAULT_mutationRate, objectiveMinimization, numberOfObjs, encodeType, elitism);
        GaMain.setSecondaryCrossoverOperator(Crossover2, false);
        GaMain.setSecondaryMutationOperator(Mutation2, false);
    }
    
    public void start(){
        System.out.println();
        openga.util.timeClock timeClock1 = new openga.util.timeClock();
        timeClock1.start();
        //GaMain.initialStage();  //ct
        //constructInitialSolutions(Population);  //ct
        GaMain.startGA();
        timeClock1.end();
        //System.out.println("\nThe final result");
        int bestInd = getBestSolnIndex(GaMain.getArchieve());
        String implementationResult = "";
        implementationResult += fileName+"\t"+evaporationMethod+"\t"+startingGeneration+"\t"+interval+"\t"+DEFAULT_crossoverRate+"\t"+DEFAULT_mutationRate+"\t"+DEFAULT_PopSize+"\t" + GaMain.getArchieve().getSingleChromosome(bestInd).getObjValue()[0]+"\t"+timeClock1.getExecutionTime()/1000.0+"\n";
        //implementationResult += fileName+"\t"+evaporationMethod +"\t" + GaMain.getArchieve().getSingleChromosome(bestInd).getObjValue()[0]+"\t"+timeClock1.getExecutionTime()/1000.0+"\n";
        //implementationResult += fileName+"\t"+numberOfJob+"\t"+startingGeneration+"\t"+interval+"\t"+ GaMain.getArchieve().getSingleChromosome(bestInd).getObjValue()[0]+"\t"+timeClock1.getExecutionTime()/1000.0+"\n";
        writeFile("SingleMachineSetupACGA_XXXX", implementationResult);
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
    
  /*ct
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
    clone1.setData(_Population);
    clone1.startToClone();
    _Population = clone1.getPopulation();
  }
ct  */
    
    public static void main(String[] args) {
        
        System.out.println("SingleMachineSetupDP_ACGA_Evaporation080302");
        //openga.applications.data.singleMachine singleMachineData = new openga.applications.data.singleMachine();
        int jobSets[] = new int[]{10, 15, 20, 25, 50};//10, 15, 20, 25, 50, 100, 150, 200
        String type[] = new String[]{"low", "med", "high"};//"low", "med", "high"
        int counter = 0;
        int popSize[] = new int[]{100};//50, 100, 155, 210
        double crossoverRate[] = new double[]{0.5, 0.8},//0.8, 0.5
                mutationRate[] = new double[]{0.3, 0.6};//0.6, 0.3
        int populationSize[] = new int[]{100};//200, 100
        int repeatExperiments = 30;
        
        int startingGeneration[] = new int[]{100, 250};//100, 250
        int interval[] = new int[]{25, 50};//25, 50
        int strategy[] = new int[]{1};
        boolean applyEvaporation = false;
        String evaporationMethod[] = new String[]{"constant", "method1", "method2", "Threshold", "add"};//"constant", "method1", "method2", "Threshold", "add"
        
        //Sourd Instance
        for(int m = 0 ; m < jobSets.length ; m ++ ){//jobSets.length
            for(int l = 0 ; l < type.length ; l ++ ){
                for(int k = 1 ; k <= 1 ; k ++ ){//49 9
                    //if((jobSets[m] <= 50 && (k == 0 || k == 3 || k == 6 || k == 21 || k == 24 || k == 27 || k == 42 || k == 45 || k == 48)) ||  (jobSets[m] > 50 && k < 9)){
                    //if((jobSets[m] <= 50) ||  (jobSets[m] > 50 && k < 9)){
                    openga.applications.data.singleMachineSetupData singleMachineData1 = new openga.applications.data.singleMachineSetupData();
                    String fileName = "instances\\SingleMachineSetup\\"+type[l]+"\\"+jobSets[m]+"_"+k+".etp";
                    singleMachineData1.setData(fileName);
                    singleMachineData1.getDataFromFile();
                    int numberOfJobs = singleMachineData1.getSize();
                    double processingTime[][] = singleMachineData1.getProcessingTime();
                    for(int j = 0 ; j < startingGeneration.length ; j ++ ){
                        for(int n = 0 ; n < interval.length ; n ++ ){
                            for(int p = 0 ; p < strategy.length ; p ++ ){
                                for(int q = 0 ; q < evaporationMethod.length ; q ++ ){
                                    for(int aa=0; aa<crossoverRate.length;aa++ ){
                                        for(int bb=0; bb<mutationRate.length;bb++ ){
                                            for(int i = 0 ; i < repeatExperiments ; i ++ ){
                                                singleMachineSetupACGA singleMachine1 = new singleMachineSetupACGA();
                                                System.out.println("Combinations: "+counter);
                                                //openga.applications.data.singleMachine readSingleMachineData1 = new openga.applications.data.singleMachine();
                                                //int numberOfJobs = jobSets[m];
                                                //String fileName = readSingleMachineData1.getFileName(numberOfJobs, k);
                                                //fileName = "bky"+numberOfJobs+"_1";
                                                //System.out.print(fileName+"\t");
                                                //readSingleMachineData1.setData("sks/"+fileName+".txt");
                                                //readSingleMachineData1.getDataFromFile();
                                                //int dueDate[] = readSingleMachineData1.getDueDate();
                                                //int processingTime[] = readSingleMachineData1.getPtime();
                                                singleMachine1.setData(numberOfJobs, processingTime, fileName);
                                                singleMachine1.setParameters(populationSize[0], crossoverRate[aa], mutationRate[bb], 1);
                                                
                                                singleMachine1.setProbabilityMatrixData(startingGeneration[j], interval[n]);
                                                singleMachine1.setSequenceStrategy(strategy[p]);
                                                singleMachine1.setEvaporationMethod(applyEvaporation, evaporationMethod[q]);
                                                singleMachine1.initiateVars();
                                                
                                                singleMachine1.start();
                                                counter ++;
                                            }//end for
                                        }
                                    }
                                }
                            }
                        }
                    }
                }//}//end if
            }
        }
        
        System.exit(0);
        
    /*
    //BKY Instance
    int incrementSteps = 5;
    for(int i = 0 ; i < repeatExperiments ; i ++ ){
      for(int j = 0 ; j < jobSets.length ; j ++ ){//jobSets.length
        for(int k = 0 ; k < startingGeneration.length ; k ++ ){
          for (int m = 0; m < interval.length; m++) {
            for (int n = 0; n < strategy.length; n++) {
              for(int p = 296 ; p < 297 ; ){
                openga.applications.data.readSingleMachine readSingleMachineData1 = new openga.applications.data.readSingleMachine();
                int numberOfJobs = jobSets[j];
                String fileName = readSingleMachineData1.getFileName(numberOfJobs, p);
                readSingleMachineData1.setData("instances/SingleMachineBKS/"+fileName+".txt");
                if(readSingleMachineData1.testReadData()){//to test whether the file exist
                  singleMachinewithProbMatrix singleMachine1 = new singleMachinewithProbMatrix();
                  System.out.println("Combinations: "+counter+"\t"+fileName);
                  //System.out.print(fileName+"\t");
                  readSingleMachineData1.getDataFromFile();
                  int dueDate[] = readSingleMachineData1.getDueDate();
                  int processingTime[] = readSingleMachineData1.getPtime();
     
                  singleMachine1.setData(numberOfJobs, dueDate, processingTime, fileName);
                  singleMachine1.setParameters(popSize[0], crossoverRate[0], mutationRate[0], 100000);
     
                  singleMachine1.setProbabilityMatrixData(startingGeneration[k], interval[m]);
                  singleMachine1.setSequenceStrategy(strategy[n]);
                  singleMachine1.setEvaporationMethod(false, evaporationMethod[0]);
     
                  singleMachine1.initiateVars();
                  singleMachine1.start();
                  counter ++;
                }
                p += incrementSteps;
              }
            }
          }
        }
      }
    }//end for of BKY
     */
        
    }
    
}
