package openga.applications.flowshopProblem;
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
import openga.applications.data.*;
import openga.operator.clone.*;

/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class flowshopNEH_SGA_list_class {
    public flowshopNEH_SGA_list_class() {
    }
    /***
     * Basic variables of GAs.
     */
    int numberOfObjs = 1;//it's a bi-objective program.
    populationI Population;
    SelectI Selection;
    CrossoverI Crossover;
    CrossoverI Crossover2;//
    MutationI Mutation, Mutation2;
    ObjectiveFunctionFlowShopScheduleI ObjectiveFunction[];
    FitnessI Fitness;
    MainI GaMain;
    cloneI clone1;   //neh
    /**
     * Parameters of the GA
     */
    int generations, length, initPopSize, fixPopSize;
    double crossoverRate, mutationRate;
    boolean[] objectiveMinimization; //true is minimum problem.
    boolean encodeType;  //binary of realize code
    public String fileName = "";
    int sumProcessingTime[];   //  neh
    /***
     * Scheduling parameter
     */
    int processingTime[][];
    int numberOfJob = 40;
    int numberOfMachines = 3;
    NEH_Method NEH = new NEH_Method();
    
    //Results
    double bestObjectiveValues[];
    populationI solutions;
    
    public int
            DEFAULT_generations = 1000,
            DEFAULT_PopSize     = 200,
            DEFAULT_initPopSize = 200,
            totalSolnsToExamine = 30000;//to fix the total number of solutions to examine. 100000
    
    
    
    public double
            DEFAULT_crossoverRate = 0.9,
            DEFAULT_mutationRate  = 0.3,
            elitism               =  0.2;     //the percentage of elite chromosomes
    
    printClass printClass1 = new printClass();
    
    public void setFlowShopData(int numberOfJob, int numberOfMachines, int processingTime[][]){
        this.numberOfJob = numberOfJob;
        this.numberOfMachines = numberOfMachines;
        this.processingTime = processingTime;
    }
    
    public void setParameters(int DEFAULT_PopSize, double DEFAULT_crossoverRate,
            double DEFAULT_mutationRate, int totalSolnsToExamine){
        this.DEFAULT_PopSize = DEFAULT_PopSize;
        this.DEFAULT_crossoverRate = DEFAULT_crossoverRate;
        this.DEFAULT_mutationRate = DEFAULT_mutationRate;
        this.totalSolnsToExamine = totalSolnsToExamine;
        DEFAULT_generations = totalSolnsToExamine / DEFAULT_PopSize;
    }
    
    public void setData(String fileName){
        this.fileName = fileName;
    }
    
    public void initiateVars(){
        GaMain     = new singleThreadGAwithInitialPop();//singleThreadGA singleThreadGAwithSecondFront singleThreadGAwithMultipleCrossover adaptiveGA singleThreadGAwithinipop
        
        Population = new population();
        Selection  = new binaryTournament();//binaryTournament similaritySelection(our method) similaritySelection2(by Ishibuchi)
        Crossover  = new twoPointCrossover2();//twoPointCrossover2 oneByOneChromosomeCrossover twoPointCrossover2withAdpative twoPointCrossover2withAdpativeThreshold
        Crossover2 = new PMX();
        Mutation   = new swapMutation();//shiftMutation shiftMutationWithAdaptive shiftMutationWithAdaptiveThreshold
        Mutation2  = new inverseMutation();
        ObjectiveFunction = new ObjectiveFunctionFlowShopScheduleI[numberOfObjs];
        ObjectiveFunction[0] = new ObjectiveMakeSpanForFlowShop();//the first objective, ObjectiveMakeSpanForFlowShop ObjectiveEarlinessTardinessForFlowShop
        //ObjectiveFunction[1] = new ObjectiveTardinessForFlowShop();//the second one.
        Fitness    = new singleObjectiveFitness();
        objectiveMinimization = new boolean[numberOfObjs];
        objectiveMinimization[0] = true;
        //objectiveMinimization[1] = true;
        encodeType = true;
        clone1  = new solutionVectorCloneWithMutation();
        //set schedule data to the objectives
        ObjectiveFunction[0].setScheduleData(processingTime, numberOfMachines);
        //ObjectiveFunction[1].setScheduleData(dueDay, processingTime, numberOfMachines);
        
        DEFAULT_generations = totalSolnsToExamine/(DEFAULT_PopSize);
        //set the data to the GA main program.
        GaMain.setData(Population, Selection, Crossover, Mutation, ObjectiveFunction, Fitness, DEFAULT_generations, DEFAULT_initPopSize, DEFAULT_PopSize,
                numberOfJob, DEFAULT_crossoverRate, DEFAULT_mutationRate, objectiveMinimization, numberOfObjs, encodeType, elitism);
        GaMain.setSecondaryCrossoverOperator(Crossover2, true);
        GaMain.setSecondaryMutationOperator(Mutation2, true);
        
    }
    
    public void start(){
        openga.util.timeClock timeClock1 = new openga.util.timeClock();
        timeClock1.start();
        constructInitialSolutions(Population); //neh
        GaMain.startGA();
        timeClock1.end();
        //to output the implementation result.
        String implementResult = "";
        int bestInd = getBestSolnIndex(GaMain.getArchieve());
        implementResult = fileName+"\t" + DEFAULT_PopSize+"\t"+DEFAULT_crossoverRate+"\t"
                + DEFAULT_mutationRate+"\t"+GaMain.getArchieve().getSingleChromosome(bestInd).getObjValue()[0]+"\t"
                +timeClock1.getExecutionTime()/1000.0+"\n";
        writeFile("NEH_Flowshop_result", implementResult);
        System.out.print(implementResult);
    }
    
    
    public void constructInitialSolutions(populationI _Population){
        
        int sequence[] = new int[numberOfJob];
        int sequence_r[] = new int[numberOfJob];
        sequence = NEH.startNEH(processingTime,numberOfJob,numberOfMachines);
        
        
        for (int z=0 ;z < numberOfJob ; z++ ){
            sequence_r[numberOfJob-z-1] = sequence[z];
        }
        /*
        for (int z=0 ;z < numberOfJob ; z++ ){
            System.out.print(sequence_r[z] + ", ");
        }
        */
        //System.exit(0);
        
        GaMain.initialStage();
        
        _Population.getSingleChromosome(0).setSolution(sequence_r);
        // }
        clone1.setData(_Population);
        clone1.startToClone();
        _Population = clone1.getPopulation();
        
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
    
    public int getTotalSolnsToExamineTaillard(int numJobs, int numMachines){
        int solns = 10000;
        if((numJobs == 20 && numMachines == 5) || (numJobs == 20 && numMachines == 10) || (numJobs == 50 && numMachines == 10) || (numJobs == 100 && numMachines == 20) ){
            solns = 10000;
        } else if((numJobs == 20 && numMachines == 20) || (numJobs == 100 && numMachines == 10)){
            solns = 20000;
        } else if(numJobs == 50 && numMachines == 5){
            solns = 5000;
        } else if(numJobs == 50 && numMachines == 20){
            solns = 50000;
        } else if((numJobs == 100 && numMachines == 5) || (numJobs == 200 && numMachines == 10) || (numJobs == 200 && numMachines == 20)){
            solns = 2000;
        } else if(numJobs == 500 && numMachines == 20){
            solns = 1000;
        }
        return solns;
    }
    
    public final int getTotalSolnsToExamineLian(int numJobs, int numMachines){
        int solns = 10000;
        if(numJobs == 20 && numMachines == 5){
            solns = 3600;
        } else if(numJobs == 20 && numMachines == 10){
            solns = 100*100;
        } else if(numJobs == 20 && numMachines == 20){
            solns = 150*150;
        } else if(numJobs == 50 && numMachines == 10){
            solns = 150*200;
        } else if(numJobs == 50 && numMachines == 20){
            solns = 150*300;
        } else if(numJobs == 100 && numMachines == 5){
            solns = 200*300;
        } else if(numJobs == 100 && numMachines == 10){
            solns = 200*400;
        }
        return solns;
    }
    
    public static void main(String[] args) {
        System.out.println("SGA_Flowshop20080120");
        int repeatExperiments = 1;
        int popSize[] = new int[]{100};//50, 100, 155, 210
        int counter = 0;
        double crossoverRate[] = new double[]{0.9},//0.6, 0.9 {0.6}
                mutationRate [] = new double[]{0.5},//0.1, 0.5 {0.5}
                elitism = 0.1;
        int numberOfInstance = 21;
        int totalSolnsToExamine = 30000;//30000 is the default one for Reeves.
    /*
    for(int i = 0 ; i < repeatExperiments ; i ++ ){
      for(int j = 20 ; j < numberOfInstance ; j ++ ){
        for(int k = 0 ; k < popSize.length ; k ++ ){
          for(int m = 0 ; m < crossoverRate.length ; m ++ ){
            for(int n = 0 ; n < mutationRate.length ; n ++ ){
              flowshopSGA flowshop1 = new flowshopSGA();
              readFlowShopRevInstance readFlowShopInstance1 = new readFlowShopRevInstance();
              String fileName = "instances\\flowshop\\";
              fileName += readFlowShopInstance1.getFileName(j);
              //fileName += "car8.txt";
              readFlowShopInstance1.setData(fileName);
              readFlowShopInstance1.getDataFromFile();
              System.out.print("Combinations:\t"+(counter++)+"\t");
              flowshop1.setFlowShopData(readFlowShopInstance1.getNumberOfJobs(), readFlowShopInstance1.getNumberOfMachines(), readFlowShopInstance1.getPtime());
              //***** examined solutions are 50*m*n
              totalSolnsToExamine = 50*readFlowShopInstance1.getNumberOfJobs()*readFlowShopInstance1.getNumberOfMachines();
              flowshop1.setParameters(popSize[k], crossoverRate[m], mutationRate[n], totalSolnsToExamine);
              flowshop1.setData(fileName);
              System.out.print(fileName+" ");
              flowshop1.initiateVars();
              flowshop1.start();
            }
          }
        }
      }
    }//end for
     */
        
        //For Taillard Instance.
        int jobs[] = new int[]{20, 50, 100, 200};//20, 50, 100, 200, 500
        int machines[] = new int[]{5, 10, 20};//5, 10, 20
        int instanceReplication = 10;
   /*
   //for Lian et al. 2006
   String filenames[] = new String[]{"100-10-10.txt"};//"20-5-5.txt", "20-10-10.txt", "50-10-10.txt", "100-5-10.txt", "20-5-10.txt", "20-20-10.txt", "50-20-10.txt","100-10-10.txt"
   for(int i = 0 ; i < repeatExperiments ; i ++ ){
     for(int j = 0 ; j < filenames.length ; j ++ ){
           for(int k = 0 ; k < popSize.length ; k ++ ){
             for(int m = 0 ; m < crossoverRate.length ; m ++ ){
               for(int n = 0 ; n < mutationRate.length ; n ++ ){
                 flowshopSGA flowshop1 = new flowshopSGA();
                 //readFlowShopRevInstance readFlowShopInstance1 = new readFlowShopRevInstance();
                 readFlowShopTaillardInstance readFlowShopInstance1 = new readFlowShopTaillardInstance();
                 String fileName = "instances\\TaillardFlowshop\\";
                 fileName += filenames[j];
                 //fileName += "car8.txt";
                 readFlowShopInstance1.setData(fileName);
                 readFlowShopInstance1.getDataFromFile();
                 System.out.print("Combinations:\t"+(counter++)+"\t");
                 jobs[0] = readFlowShopInstance1.getNumberOfJobs();
                 machines[0] = readFlowShopInstance1.getNumberOfMachines();
                 flowshop1.setFlowShopData(jobs[0], machines[0], readFlowShopInstance1.getPtime());
                 //***** examined solutions are determined by Lian et al.
                 totalSolnsToExamine = flowshop1.getTotalSolnsToExamineLian(jobs[0], machines[0]);
                 flowshop1.setParameters(popSize[k], crossoverRate[m], mutationRate[n], totalSolnsToExamine);
                 flowshop1.setData(fileName);
                 flowshop1.initiateVars();
                 flowshop1.start();
               }
             }
       }
     }
   }//end for
    
   System.exit(0);
    */
        
        int startInstance = 1;
        int endInstance = 10;
        for(int j = 0 ; j < jobs.length ; j ++ ){
            for(int s = 0 ; s < machines.length ; s ++ ){
                for(int q = startInstance ; q <= endInstance ; q ++ ){
                    if((jobs[j] <= 100) || (jobs[j] == 200 && machines[s] >= 10) || (jobs[j] == 500 && machines[s] == 20)){
                        //readFlowShopRevInstance readFlowShopInstance1 = new readFlowShopRevInstance();
                        readFlowShopTaillardInstance readFlowShopInstance1 = new readFlowShopTaillardInstance();
                        String fileName = "instances\\TaillardFlowshop\\";
                        fileName += readFlowShopInstance1.getFileName(jobs[j], machines[s], q);
                        //fileName += "car8.txt";
                        readFlowShopInstance1.setData(fileName);
                        readFlowShopInstance1.getDataFromFile();
                        
                        for(int k = 0 ; k < popSize.length ; k ++ ){
                            for(int m = 0 ; m < crossoverRate.length ; m ++ ){
                                for(int n = 0 ; n < mutationRate.length ; n ++ ){
                                    for(int i = 0 ; i < repeatExperiments ; i ++ ){
                                        //System.out.println("Combinations:\t"+(counter++)+"\t"+fileName);
                                        flowshopNEH_SGA flowshop1 = new flowshopNEH_SGA();
                                        flowshop1.setFlowShopData(jobs[j], machines[s], readFlowShopInstance1.getPtime());
                                        //***** examined solutions are determined by Taillard.
                                        //totalSolnsToExamine = flowshop1.getTotalSolnsToExamineTaillard(jobs[j], machines[s]);
                                        //***** examined solutions are determined by Liang.
                                        totalSolnsToExamine = (jobs[j]*2*500);
                                        flowshop1.setParameters(popSize[k], crossoverRate[m], mutationRate[n], totalSolnsToExamine);
                                        flowshop1.setData(fileName);
                                        flowshop1.initiateVars();
                                        flowshop1.start();
                                    }//end for
                                }
                            }
                        }
                    }
                }
            }
        }
        
    }
    
}