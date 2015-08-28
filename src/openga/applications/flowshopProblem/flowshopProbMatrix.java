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

public class flowshopProbMatrix extends flowshopSGA{
  public flowshopProbMatrix() {
  }

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

  /***
   * Basic variables of GAs.
   */
  public int numberOfObjs = 1;//it's a bi-objective program.

  probabilityMatrixI GaMain;
  cloneI clone1;

  public void setProbabilityMatrixData(int startingGeneration, int interval){
    this.startingGeneration = startingGeneration;
    this.interval = interval;
  }

  public void setProbabilityMatrixDataPercent(double startingGenerationPercent, double intervalPercent){
    this.startingGenerationPercent = startingGenerationPercent;
    this.intervalPercent = intervalPercent;
  }

  public void setSequenceStrategy(int strategy){
    this.strategy = strategy;
  }

  public void setEvaporationMethod(boolean applyEvaporation, String evaporationMethod){
    this.applyEvaporation = applyEvaporation;
    this.evaporationMethod = evaporationMethod;
  }

  public void setReplications(int replications){
    this.replications = replications;
  }

  public void initiateVars(){
    GaMain     = new singleThreadGAwithProbabilityMatrix();//singleThreadGAwithProbabilityMatrix singleThreadGAwithProbabilityMatrixInitialPop
    Population = new population();
    Selection  = new binaryTournament();
    Crossover  = new twoPointCrossover2();//
    Crossover2 = new PMX();

    Mutation   = new swapMutation();
    //Mutation   = new swapMutationWithMining2();//swapMutationWithMining2 shiftMutationWithMining2
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
    GaMain.setProbabilityMatrixData(startingGeneration, interval);
    GaMain.setSequenceStrategy(strategy);
    GaMain.setEvaporationMethod(applyEvaporation, evaporationMethod);
    DEFAULT_generations = totalSolnsToExamine/(DEFAULT_PopSize);
    GaMain.setData(Population, Selection, Crossover, Mutation, ObjectiveFunction, Fitness, DEFAULT_generations, DEFAULT_PopSize, DEFAULT_PopSize,
                   numberOfJob, DEFAULT_crossoverRate, DEFAULT_mutationRate, objectiveMinimization, numberOfObjs, encodeType, elitism);
    GaMain.setCloneOperatpr(clone1, true);
    GaMain.setSecondaryCrossoverOperator(Crossover2, false);
    GaMain.setSecondaryMutationOperator(Mutation2, false);
  }

  public void start(){
    System.out.println();
    openga.util.timeClock timeClock1 = new openga.util.timeClock();
    timeClock1.start();
    /*** from NEH solutions ***/
    //constructInitialSolutions(Population, readNEHInstancePop1);
    GaMain.startGA();
    timeClock1.end();
    //System.out.println("\nThe final result");
    int bestInd = getBestSolnIndex(GaMain.getArchieve());
    String implementationResult = "";
    //implementationResult += fileName+"\t"+numberOfJob+"\t"+DEFAULT_crossoverRate+"\t"+DEFAULT_mutationRate+"\t"+DEFAULT_PopSize+"\t" + GaMain.getArchieve().getSingleChromosome(bestInd).getObjValue()[0]+"\t"+timeClock1.getExecutionTime()/1000.0+"\n";
    implementationResult += fileName+"\t"+startingGenerationPercent +"\t"+intervalPercent+"\t"+ GaMain.getArchieve().getSingleChromosome(bestInd).getObjValue()[0]+"\t"+timeClock1.getExecutionTime()/1000.0+"\n";
    writeFile("flowshop_ProbMatrixDOE_Liang_20080420CPU", implementationResult);
    System.out.print(implementationResult);
  }

  readFlowShopTaillardInstancePop readNEHInstancePop1;
  public void setPopReaderObj(readFlowShopTaillardInstancePop readNEHInstancePop1){
    this.readNEHInstancePop1 = readNEHInstancePop1;
  }

  public void constructInitialSolutions(populationI _Population, readFlowShopTaillardInstancePop readNEHInstancePop1){
    GaMain.initialStage();
    readNEHInstancePop1.constructInitialSolutions(_Population, numberOfJob, DEFAULT_PopSize);

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

  public static void main(String[] args) {
    System.out.println("flowshop_ProbMatrixDOE_Liang_20080418CPU");
    int repeatExperiments = 30;
    int popSize[] = new int[]{100};//50, 100, 155, 210 {50: Taillard}
    int counter = 0;
    double crossoverRate[] = new double[]{0.9},//0.6, 0.9 {0.9}
           mutationRate [] = new double[]{0.5},//0.1, 0.5 {0.5}
           elitism = 0.2;
    int numberOfInstance = 21;
    int startingGeneration[] = new int[]{50, 100};//200, 500
    double startingGenerationPercent[] = new double[]{0.3};//0.3, 0.7
    int interval[] = new int[]{25, 50};
    double intervalPercent[] = new double[]{0.05};//0.1, 0.05
    int strategy[] = new int[]{1};
    int totalSolnsToExamine = 30000;
    boolean applyEvaporation = false;
    String evaporationMethod[] = new String[]{"method2"};//constant, method1, method2
    /*
    for(int i = 0 ; i < repeatExperiments ; i ++ ){
      for(int j = 20 ; j < numberOfInstance ; j ++ ){//numberOfInstance
        for(int k = 0 ; k < startingGenerationPercent.length ; k ++ ){//startingGeneration startingGenerationPercent
          for(int m = 0 ; m < intervalPercent.length ; m ++ ){//interval intervalPercent
            for(int n = 0 ; n < strategy.length ; n ++ ){
              for(int p = 0 ; p < evaporationMethod.length ; p++){
                flowshopProbMatrix flowshop1 = new flowshopProbMatrix();
                readFlowShopRevInstance readFlowShopRevInstance1 = new readFlowShopRevInstance();
                String fileName = "instances\\flowshop\\";
                fileName += readFlowShopRevInstance1.getFileName(j);
                //fileName += "car8.txt";
                //fileName += "rec41.txt";
                readFlowShopRevInstance1.setData(fileName);
                readFlowShopRevInstance1.getDataFromFile();
                System.out.print("Combinations:\t"+(counter++)+"\t");
                flowshop1.setFlowShopData(readFlowShopRevInstance1.getNumberOfJobs(), readFlowShopRevInstance1.getNumberOfMachines(), readFlowShopRevInstance1.getPtime());
                //***** examined solutions are 50*m*n
                totalSolnsToExamine = 50*readFlowShopRevInstance1.getNumberOfJobs()*readFlowShopRevInstance1.getNumberOfMachines();
                int tempGeneration = totalSolnsToExamine/popSize[0];
                flowshop1.setParameters(popSize[0], crossoverRate[0], mutationRate[0], totalSolnsToExamine);
                //flowshop1.setProbabilityMatrixData(startingGeneration[k], interval[m]);
                if((int)(tempGeneration*intervalPercent[m]) == 0){
                  interval[0] = 2;
                }
                else{
                  interval[0] = (int)(tempGeneration*intervalPercent[m])+2;
                }
                //System.out.println("tempGeneration "+tempGeneration+" "+interval[0]);
                flowshop1.setProbabilityMatrixData((int)(tempGeneration*startingGenerationPercent[k]), interval[0]);
                flowshop1.setProbabilityMatrixDataPercent(startingGenerationPercent[k], intervalPercent[m]);
                flowshop1.setSequenceStrategy(strategy[n]);
                flowshop1.setEvaporationMethod(applyEvaporation, evaporationMethod[p]);
                flowshop1.setData(fileName);
                flowshop1.initiateVars();
                flowshop1.start();
              }
            }
          }
        }
      }
    }//end for
    System.exit(0);
    */

  int jobs[] = new int[]{20, 50, 100, 200};//20, 50, 100, 200, 500
  int machines[] = new int[]{5, 10, 20};//5, 10, 20
  int instanceReplication = 10;
  /*
  //for Lian et al. 2006
  String filenames[] = new String[]{"100-10-10.txt"};//"20-5-5.txt", "20-10-10.txt", "50-10-10.txt", "100-5-10.txt", "20-5-10.txt", "20-20-10.txt", "50-20-10.txt", "100-10-10.txt"
  for(int i = 0 ; i < repeatExperiments ; i ++ ){
    for(int j = 0 ; j < filenames.length ; j ++ ){
          for(int k = 0 ; k < startingGenerationPercent.length ; k ++ ){
            for(int m = 0 ; m < intervalPercent.length ; m ++ ){
              for(int n = 0 ; n < strategy.length ; n ++ ){
                flowshopProbMatrix flowshop1 = new flowshopProbMatrix();
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
                flowshop1.setParameters(popSize[0], crossoverRate[0], mutationRate[0], totalSolnsToExamine);
                int tempGeneration = totalSolnsToExamine/popSize[0];
                if((int)(tempGeneration*intervalPercent[m]) == 0){
                  interval[0] = 2;
                }
                else{
                  interval[0] = (int)(tempGeneration*intervalPercent[m])+2;
                }
                flowshop1.setProbabilityMatrixData((int)(tempGeneration*startingGenerationPercent[k]), interval[0]);
                flowshop1.setProbabilityMatrixDataPercent(startingGenerationPercent[k], intervalPercent[m]);
                flowshop1.setSequenceStrategy(strategy[n]);
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
        System.out.println(fileName);
        readFlowShopInstance1.setData(fileName);
        readFlowShopInstance1.getDataFromFile();
        /*
        readNEHInstancePop1.setData("instances\\NEH20-500\\NEH"+jobs[j]+"-"+machines[s]+"-"+q+".txt");//filename
        int NEHpopSize = 500;
        if(jobs[j] > 100){
          NEHpopSize = 30;
        }
        readNEHInstancePop1.setPopData(NEHpopSize);//available NEH solns.
        readNEHInstancePop1.setData(jobs[j]);
        readNEHInstancePop1.getDataFromFile();
        */

        for(int i = 0 ; i < repeatExperiments ; i ++ ){
          for(int k = 0 ; k < startingGenerationPercent.length ; k ++ ){
            for(int m = 0 ; m < intervalPercent.length ; m ++ ){
              for(int n = 0 ; n < strategy.length ; n ++ ){
                if((jobs[j] <= 100) || (jobs[j] == 200 && machines[s] >= 10) || (jobs[j] == 500 && machines[s] == 20)){
                  flowshopProbMatrix flowshop1 = new flowshopProbMatrix();
                  //flowshop1.setPopReaderObj(readNEHInstancePop1);

                  //System.out.print("Combinations:\t"+(counter++)+"\t");
                  flowshop1.setFlowShopData(jobs[j], machines[s], readFlowShopInstance1.getPtime());
                  //***** examined solutions are determined by Taillard.
                  //totalSolnsToExamine = flowshop1.getTotalSolnsToExamineTaillard(jobs[j], machines[s]);
                  //***** examined solutions are determined by Liang.
                  totalSolnsToExamine = (jobs[j]*2*500);
                  flowshop1.setParameters(popSize[0], crossoverRate[0], mutationRate[0], totalSolnsToExamine);
                  int tempGeneration = totalSolnsToExamine/popSize[0];
                  if((int)(tempGeneration*intervalPercent[m]) == 0){
                    interval[0] = 2;
                  }
                  else{
                    interval[0] = (int)(tempGeneration*intervalPercent[m])+2;
                  }
                  //System.out.println("tempGeneration "+tempGeneration+" "+interval[0]);
                  flowshop1.setProbabilityMatrixData((int)(tempGeneration*startingGenerationPercent[k]), interval[0]);
                  flowshop1.setProbabilityMatrixDataPercent(startingGenerationPercent[k], intervalPercent[m]);
                  flowshop1.setSequenceStrategy(strategy[n]);
                  flowshop1.setData(fileName);
                  flowshop1.setReplications(q);
                  flowshop1.initiateVars();
                  flowshop1.start();
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