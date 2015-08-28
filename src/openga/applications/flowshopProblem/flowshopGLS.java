package openga.applications.flowshopProblem;
import openga.chromosomes.*;
import openga.operator.selection.*;
import openga.operator.crossover.*;
import openga.operator.localSearch.*;
import openga.operator.mutation.*;
import openga.ObjectiveFunctions.*;
import openga.MainProgram.*;
import openga.ObjectiveFunctions.*;
import openga.Fitness.*;
import openga.util.printClass;
import openga.util.fileWrite1;
import openga.applications.data.*;

/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class flowshopGLS extends flowshopSGA{
  public flowshopGLS() {
  }
  /***
   * Basic variables of GAs.
   */
  int numberOfObjs = 1;//it's a bi-objective program.
  MainI GaMain;

  /**
   * Parameters of the GA
   */
  int generations, length, initPopSize, fixPopSize;
  double crossoverRate, mutationRate;
  boolean[] objectiveMinimization; //true is minimum problem.
  boolean encodeType;  //binary of realize code
  public String fileName = "";

  /**
   * Local Search Operator
   */
  boolean applyLocalSearch = true;
  localSearchI localSearch1;
  int maxNeighborhood = 5;  //A default value of the maximum neighbors to search.

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

  public void setLocalSearchData(boolean applyLocalSearch, int maxNeighborhood){
    this.applyLocalSearch = applyLocalSearch;
    this.maxNeighborhood = maxNeighborhood;
  }

  public void initiateVars(){
    GaMain     = new singleThreadGAwithLocalSearch();
    Population = new population();
    Selection  = new binaryTournament();
    Crossover  = new twoPointCrossover2();
    Mutation   = new swapMutation();
    ObjectiveFunction = new ObjectiveFunctionFlowShopScheduleI[numberOfObjs];
    ObjectiveFunction[0] = new ObjectiveMakeSpanForFlowShop();//the first objective, ObjectiveMakeSpanForFlowShop ObjectiveEarlinessTardinessForFlowShop
    //ObjectiveFunction[1] = new ObjectiveTardinessForFlowShop();//the second one.
    Fitness    = new singleObjectiveFitness();
    objectiveMinimization = new boolean[numberOfObjs];
    objectiveMinimization[0] = true;
    //objectiveMinimization[1] = true;
    encodeType = true;

    //set schedule data to the objectives
    ObjectiveFunction[0].setScheduleData(processingTime, numberOfMachines);
    //ObjectiveFunction[1].setScheduleData(dueDay, processingTime, numberOfMachines);

    DEFAULT_generations = totalSolnsToExamine/(DEFAULT_PopSize);
    //set the data to the GA main program.
    GaMain.setData(Population, Selection, Crossover, Mutation, ObjectiveFunction, Fitness, DEFAULT_generations, DEFAULT_initPopSize, DEFAULT_PopSize,
                   numberOfJob, DEFAULT_crossoverRate, DEFAULT_mutationRate, objectiveMinimization, numberOfObjs, encodeType, elitism);
    GaMain.setSecondaryCrossoverOperator(Crossover2, false);
    GaMain.setSecondaryMutationOperator(Mutation2, false);
    localSearch1 = new localSearchByShiftMutationV2();//localSearchBy2Opt localSearchBySwap localSearchByShiftMutationV2
    GaMain.setLocalSearchOperator(localSearch1, applyLocalSearch, maxNeighborhood);
  }

  public void start(){
    openga.util.timeClock timeClock1 = new openga.util.timeClock();
    timeClock1.start();
    GaMain.startGA();
    timeClock1.end();
    //to output the implementation result.
    String implementResult = "";
    int bestInd = getBestSolnIndex(GaMain.getArchieve());
    implementResult = fileName+"\t" + maxNeighborhood +"\t"+GaMain.getArchieve().getSingleChromosome(bestInd).getObjValue()[0]+"\t"
        +timeClock1.getExecutionTime()/1000.0+"\n";
    writeFile("GLS_Flowshop20080419", implementResult);
    System.out.print(implementResult);
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
    System.out.println("GLS_Flowshop20080419");
    int repeatExperiments = 30;
    int popSize[] = new int[]{100};//50, 100, 155, 210
    int counter = 0;
    double crossoverRate[] = new double[]{1.0},//0.6, 0.9 {0.9}
           mutationRate [] = new double[]{1.0},//0.1, 0.5 {0.5}
           elitism = 0.1;
    int numberOfInstance = 21;
    int totalSolnsToExamine = 30000;//30000 is the default one for Reeves.

    //local search
    boolean applyLocalSearch = true;
    int maxNeighborhood[] = new int[]{3, 5, 7};  //A default value of the maximum neighbors to search.

   //For Taillard Instance.
   int jobs[] = new int[]{20, 50, 100, 200};//20, 50, 100, 200, 500
   int machines[] = new int[]{5, 10, 20};//5, 10, 20
   //implication of instanceReplication
   int startInstance = 1;
   int endInstance = 10;

   for (int j = 0; j < jobs.length; j++) {
     for (int s = 0; s < machines.length; s++) {
       for (int q = startInstance; q <= endInstance; q++) {
         if ( (jobs[j] <= 100) || (jobs[j] == 200 && machines[s] >= 10) ||
             (jobs[j] == 500 && machines[s] == 20)) {
           //readFlowShopRevInstance readFlowShopInstance1 = new readFlowShopRevInstance();
           readFlowShopTaillardInstance readFlowShopInstance1 = new
               readFlowShopTaillardInstance();
           String fileName = "instances\\TaillardFlowshop\\";
           fileName +=
               readFlowShopInstance1.getFileName(jobs[j], machines[s], q);
           //fileName += "car8.txt";
           readFlowShopInstance1.setData(fileName);
           readFlowShopInstance1.getDataFromFile();

           for (int k = 0; k < maxNeighborhood.length; k++) {
             for (int i = 0; i < repeatExperiments; i++) {
               //System.out.println("Combinations:\t"+(counter++)+"\t"+fileName);
               flowshopGLS flowshop1 = new flowshopGLS();
               flowshop1.setFlowShopData(jobs[j], machines[s],
                                         readFlowShopInstance1.getPtime());
               //***** examined solutions are determined by Taillard.
               //totalSolnsToExamine = flowshop1.getTotalSolnsToExamineTaillard(jobs[j], machines[s]);
               //***** examined solutions are determined by Liang.
               totalSolnsToExamine = (jobs[j] * 2 * 500);
               flowshop1.setParameters(popSize[0], crossoverRate[0],
                                       mutationRate[0],
                                       totalSolnsToExamine);
               flowshop1.setData(fileName);
               flowshop1.setLocalSearchData(applyLocalSearch, maxNeighborhood[k]);
               flowshop1.initiateVars();
               flowshop1.start();
             } //end for
           }
         }
       }
     }
   }
  }


}