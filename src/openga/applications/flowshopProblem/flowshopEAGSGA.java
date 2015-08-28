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
 * <p>Description: It implements the EA/G.Please refer to
 * Zhang, Q., Sun, J., & Tsang, E. (2005). An evolutionary algorithm with guided mutation for the maximum clique problem. IEEE Transactions on Evolutionary Computation, 9(2), 192-200.</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class flowshopEAGSGA extends flowshopProbMatrix{
    public flowshopEAGSGA() {
    }
    
    /***
     * AC parameters
     */
    int strategy = 1;
    int replications = 1;
    
    /***
     * Parameters of Guided Mutation.
     */
    double lamda = 0.9; //learning rate
    double beta = 0.9;
    
    public void setEDAinfo(double lamda, double beta){
        this.lamda = lamda;
        this.beta = beta;
    }
    
    public void initiateVars(){
        GaMain     = new guidedMutationMain2();
        Population = new population();
        Selection  = new binaryTournament();
        Crossover  = new twoPointCrossover2();   //multiParentsCrossover   twoPointCrossover2
        Crossover2 = new multiParentsCrossover();
        
        Mutation   = new swapMutation();
        Mutation2  = new inverseMutation();
        ObjectiveFunction = new ObjectiveFunctionFlowShopScheduleI[numberOfObjs];
        ObjectiveFunction[0] = new ObjectiveMakeSpanForFlowShop();//the first objective, ObjectiveMakeSpanForFlowShop ObjectiveEarlinessTardinessForFlowShop
        Fitness    = new singleObjectiveFitness();
        objectiveMinimization = new boolean[numberOfObjs];
        objectiveMinimization[0] = true;
        //objectiveMinimization[1] = true;
        encodeType = true;
        clone1  = new solutionVectorCloneWithMutation();//swap mutation
        //set schedule data to the objectives
        ObjectiveFunction[0].setScheduleData(processingTime, numberOfMachines);
        //set the data to the GA main program.
        GaMain.setGuidedMutationInfo(lamda, beta);
        GaMain.setSequenceStrategy(strategy);
        DEFAULT_generations = totalSolnsToExamine/(DEFAULT_PopSize);
        GaMain.setData(Population, Selection, Crossover, Mutation, ObjectiveFunction, Fitness, DEFAULT_generations, DEFAULT_PopSize, DEFAULT_PopSize,
                numberOfJob, DEFAULT_crossoverRate, DEFAULT_mutationRate, objectiveMinimization, numberOfObjs, encodeType, elitism);
        GaMain.setCloneOperatpr(clone1, true);
        GaMain.setSecondaryCrossoverOperator(Crossover2, true);
        GaMain.setSecondaryMutationOperator(Mutation2, false);
    }
    
 static public synchronized void writeFile(String fileName, String _result){
        fileWrite1 writeLotteryResult = new fileWrite1();
        writeLotteryResult.writeToFile(_result,fileName+".txt");
        Thread thread1 = new Thread(writeLotteryResult);
        thread1.run();
    }
    
    public void start(){
        System.out.println();
        openga.util.timeClock timeClock1 = new openga.util.timeClock();
        timeClock1.start();
        GaMain.startGA();
        timeClock1.end();
        int bestInd = getBestSolnIndex(GaMain.getArchieve());
        String implementationResult = "";
        //implementationResult += fileName+"\t"+numberOfJob+"\t"+DEFAULT_crossoverRate+"\t"+DEFAULT_mutationRate+"\t"+DEFAULT_PopSize+"\t" + GaMain.getArchieve().getSingleChromosome(bestInd).getObjValue()[0]+"\t"+timeClock1.getExecutionTime()/1000.0+"\n";
        implementationResult += fileName+"\t"+DEFAULT_PopSize+"\t"+lamda +"\t"+beta+"\t"
                + GaMain.getArchieve().getSingleChromosome(bestInd).getObjValue()[0]+"\t"
                +timeClock1.getExecutionTime()/1000.0+"\n";
        writeFile("EAGSGA(mpc_ad_5p)_Flowshop_test7", implementationResult);
        System.out.print(implementationResult);
    }
    
    public static void main(String[] args) {
        System.out.println("EAGSGA_DOE_Flowshop0528");
    int repeatExperiments = 5;
        int popSize[] = new int[]{100};//50, 100, 155, 210
        int counter = 0;
        double crossoverRate[] = new double[]{0.6},//0.6, 0.9 {0.6}
                mutationRate [] = new double[]{0.5},//0.1, 0.5 {0.5}
                elitism = 0.1;
        int numberOfInstance = 21;
        int totalSolnsToExamine = 30000;//30000 is the default one for Reeves.
        
        //Parameters of Guided Mutation.
        double[] lamda = new double[]{0.1}; //learning rate{0.1, 0.5, 0.9} {0.1 is better}
        double[] beta = new double[]{0.9};//Percentage of local information{0.1, 0.5, 0.9} {0.9 is significant}
    /*
    for(int j = 1 ; j < numberOfInstance ; j ++ ){
      for(int k = 0 ; k < popSize.length ; k ++ ){
        for(int m = 0 ; m < lamda.length ; m ++ ){
          for(int n = 0 ; n < beta.length ; n ++ ){
            readFlowShopRevInstance readFlowShopInstance1 = new readFlowShopRevInstance();
            String fileName = "instances\\flowshop\\";
            fileName += readFlowShopInstance1.getFileName(j);
            //fileName += "car8.txt";
            readFlowShopInstance1.setData(fileName);
            readFlowShopInstance1.getDataFromFile();
            System.out.print("Combinations:\t"+(counter++)+"\t");
     
            for(int i = 0 ; i < repeatExperiments ; i ++ ){
              flowshopEAG flowshop1 = new flowshopEAG();
              flowshop1.setFlowShopData(readFlowShopInstance1.getNumberOfJobs(), readFlowShopInstance1.getNumberOfMachines(), readFlowShopInstance1.getPtime());
              //***** examined solutions are 50*m*n Reeves.
              totalSolnsToExamine = 50*readFlowShopInstance1.getNumberOfJobs()*readFlowShopInstance1.getNumberOfMachines();
              flowshop1.setParameters(popSize[k], crossoverRate[0], mutationRate[0], totalSolnsToExamine);
              flowshop1.setEDAinfo(lamda[m], beta[n]);
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
        //Taillard
        int jobs[] = new int[]{20, 50, 100, 200};//20, 50, 100, 200, 500
        int machines[] = new int[]{5, 10, 20};//5, 10, 20
        
        int startInstance = 1;
        int endInstance = 10;
        for(int j = 0 ; j < jobs.length ; j ++ ){
            for(int s = 0 ; s < machines.length ; s ++ ){
                for(int q = startInstance ; q <= endInstance ; q ++ ){//instanceReplication
                    //readFlowShopTaillardInstancePop readNEHInstancePop1 = new readFlowShopTaillardInstancePop();
                    //readFlowShopRevInstance readFlowShopInstance1 = new readFlowShopRevInstance();
                    readFlowShopTaillardInstance readFlowShopInstance1 = new readFlowShopTaillardInstance();
                    String fileName = "instances\\TaillardFlowshop\\";
                    fileName += readFlowShopInstance1.getFileName(jobs[j], machines[s], q);
                    //fileName += "car8.txt";
                    //fileName = "instances\\TaillardFlowshop\\50-20-6.txt";
                    readFlowShopInstance1.setData(fileName);
                    readFlowShopInstance1.getDataFromFile();
                    
                    for(int i = 0 ; i < repeatExperiments ; i ++ ){
                        for(int k = 0 ; k < popSize.length ; k ++ ){
                            for(int m = 0 ; m < lamda.length ; m ++ ){
                                for(int n = 0 ; n < beta.length ; n ++ ){
                                    if((jobs[j] <= 100) || (jobs[j] == 200 && machines[s] >= 10) || (jobs[j] == 500 && machines[s] == 20)){
                                        flowshopEAGSGA flowshop1 = new flowshopEAGSGA();
                                        //***** examined solutions are determined by Taillard.
                                        //totalSolnsToExamine = flowshop1.getTotalSolnsToExamineTaillard(jobs[j], machines[s]);
                                        //***** examined solutions are determined by Liang.
                                        totalSolnsToExamine = (jobs[j]*2*500);
                                        
                                        flowshop1.setFlowShopData(readFlowShopInstance1.getNumberOfJobs(), readFlowShopInstance1.getNumberOfMachines(), readFlowShopInstance1.getPtime());
                                        flowshop1.setParameters(popSize[k], crossoverRate[0], mutationRate[0], totalSolnsToExamine);
                                        flowshop1.setEDAinfo(lamda[m], beta[n]);
                                        flowshop1.setData(fileName);
                                        flowshop1.initiateVars();
                                        flowshop1.start();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }//end for
        
    }
}
