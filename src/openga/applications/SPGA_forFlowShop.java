package openga.applications;
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

/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class SPGA_forFlowShop extends SPGA2_forParallelMachine {
  public SPGA_forFlowShop() {
  }
  /***
   * Scheduling data
   */
  int dueDay[], processingTime[][];
  int numberOfJob = 20;
  int numberOfMachines = 20;

  ObjectiveFunctionFlowShopScheduleI ObjectiveFunction[][];

  public void setFlowShopData(int numberOfJob, int numberOfMachines){
    this.numberOfJob = numberOfJob;
    this.numberOfMachines = numberOfMachines;
  }



  public void initiateVars(){
    //initiate scheduling data, we get the data from a program.
    processingTime = getProcessingTime();
    dueDay = getDueDay();

    objectiveMinimization = new boolean[numberOfObjs];
    objectiveMinimization[0] = true;
    objectiveMinimization[1] = true;
    encodeType = true;

    //GA objects
    GaMain     = new MainWeightScalarizationI[numberOfSubPopulations];
    Population = new populationI[numberOfSubPopulations];
    Selection  = new SelectI[numberOfSubPopulations];
    Crossover  = new CrossoverI[numberOfSubPopulations];
    //Crossover2  = new CrossoverI[numberOfSubPopulations];
    Mutation   = new MutationI[numberOfSubPopulations];
    //Mutation2  = new MutationI[numberOfSubPopulations];
    ObjectiveFunction  = new ObjectiveFunctionFlowShopScheduleI[numberOfSubPopulations][numberOfObjs];
    Fitness   = new FitnessI[numberOfSubPopulations];

    for(int i = 0 ; i < numberOfSubPopulations ; i ++ ){
      GaMain[i]     = new fixWeightScalarization();//singleThreadGA fixWeightScalarization
      Population[i] = new population();
      Selection[i]  = new binaryTournament();
      Crossover[i]  = new twoPointCrossover2();
      Crossover2 = new PMX();
      Mutation[i]   = new shiftMutation();//swapMutation
      Mutation2  = new swapMutation();//shiftMutation
      ObjectiveFunction[i] = new ObjectiveFunctionFlowShopScheduleI[numberOfObjs];
      ObjectiveFunction[i][0] = new ObjectiveMakeSpanForFlowShop();//the first objective, makespan
      ObjectiveFunction[i][1] = new ObjectiveTardinessForFlowShop();//the second one.
      Fitness[i]    = new FitnessByScalarizedM_objectives();

      //set schedule data to the objectives
      ObjectiveFunction[i][0].setScheduleData(processingTime, numberOfMachines);
      ObjectiveFunction[i][1].setScheduleData(dueDay, processingTime, numberOfMachines);
      //set the data to the GA main program.
      GaMain[i].setData(Population[i], Selection[i], Crossover[i], Mutation[i],
                     ObjectiveFunction[i], Fitness[i], firstIteration, DEFAULT_PopSize*2, DEFAULT_PopSize,
                     numberOfJob, DEFAULT_crossoverRate, DEFAULT_mutationRate, objectiveMinimization,
                     numberOfObjs, encodeType, elitism);
      //set weight data
      GaMain[i].setWeight(calcWeightsForEachSubPop(i));
      //set secondary crossover and mutation operator.
      GaMain[i].setSecondaryCrossoverOperator(Crossover2, applySecCRX);
      GaMain[i].setSecondaryMutationOperator(Mutation2, applySecMutation);
    }
  }

  public void start(){
    //we control the number of iteration of GA here.
    for(int m = 0 ; m < DEFAULT_generations ; m ++ ){
      //System.out.println(m);

      for(int i = 0 ; i < numberOfSubPopulations ; i ++ ){
        if(m == 0){//initial each population and its objective values.
          GaMain[i].InitialRun();
          if(i == 0){
            GaMain[i].initFirstArchieve();
          }
          else{
            GaMain[i].updateNondominatedSon();
          }
        }

        GaMain[i].startGA();
      }
      //System.out.println("GaMain[i].getAarchieve.getPopulationSize() "+GaMain[0].getArchieve().getPopulationSize()+"\t");
    }

    //to output the implementation result.
    String implementResult = "";
    double refSet[][] = getReferenceSet();

    double objArray[][] = GaMain[0].getArchieveObjectiveValueArray();
    implementResult = numberOfJob+"\t"+numberOfSubPopulations+"\t"+ DEFAULT_PopSize +"\t"+ applySecCRX+"\t"
        + applySecMutation+"\t"+calcSolutionQuality(refSet, objArray)+"\n";//
    objArray = null;
    writeFile("2011SPGAforFlowShop", implementResult);

    for(int i = 0 ; i < numberOfSubPopulations ; i ++ ){
      GaMain[i].destroidArchive();
      GaMain[i] = null;
    }
  }

  public double[][] getReferenceSet(){
    openga.applications.data.flowShop data1 = new openga.applications.data.flowShop();
    if(numberOfJob == 20){
      return data1.getReferenceSet2();
    }
    else if(numberOfJob == 40){
      return data1.getReferenceSet3();
    }
    else if(numberOfJob == 60){
      return data1.getReferenceSet4();
    }
    else{
      return data1.getReferenceSet5();
    }
  }

  private int[] getDueDay(){
    openga.applications.data.flowShop data1 = new openga.applications.data.flowShop();
    if(numberOfJob == 20){
      return data1.getTestData2_DueDay();
    }
    else if(numberOfJob == 40){
      return data1.getTestData3_DueDay();
    }
    else if(numberOfJob == 60){
      return data1.getTestData4_DueDay();
    }
    else{
      return data1.getTestData5_DueDay();
    }
  }

  private int[][] getProcessingTime(){
    openga.applications.data.flowShop data1 = new openga.applications.data.flowShop();
    if(numberOfJob == 20){
      return data1.getTestData2_processingTime();
    }
    else if(numberOfJob == 40){
      return data1.getTestData3_processingTime();
    }
    else if(numberOfJob == 60){
      return data1.getTestData4_processingTime();
    }
    else{
      return data1.getTestData5_processingTime();
    }
  }

  public static void main(String[] args) {
    System.out.println("SPGA_forFlowShop");

    int numberOfSubPopulations[] = new int[]{40};
    int popSize[] = new int[]{210};
    int numberOfJob[] = new int []{20, 40, 60, 80};
    int numberOfMachines = 20;
    int totalSolnsToExamine[] = new int[]{1000000};//1000000, 1500000, 2000000
    boolean applySecCRX[] = new boolean[]{false};//false, true
    boolean applySecMutation[] = new boolean[]{true};

    double crossoverRate = 0.9,
           mutationRate = 0.1,
           elitism = 0.2;
    int repeatExperiments = 20;

    //to form a text file and write the title in it.
    String implementResult = "numberOfJob\t numberOfSubPopulations\t popSize\t applySecCRX\t applySecMutation\t D1r\n";
    SPGA_forFlowShop writeFile1 = new SPGA_forFlowShop();
    writeFile1.writeFile("SPGAforFlowShop", implementResult);
    int counter = 0;

    for(int i = 0 ; i < repeatExperiments ; i ++ ){
      for(int j = 0 ; j < numberOfSubPopulations.length ; j ++ ){
        for(int k = 0 ; k < popSize.length ; k ++ ){
          for(int L = 0 ; L < applySecCRX.length ; L ++ ){
            for(int m = 0 ; m < applySecMutation.length ; m ++ ){
              for(int r = 0 ; r < numberOfJob.length ; r ++ ){
                System.out.println(numberOfJob[r]+"\t"+numberOfSubPopulations[j]+"\t"+ popSize[k]+"\t"+numberOfMachines);
                System.out.println("combinations: "+counter++);

                SPGA_forFlowShop SPGA_forFlowShop1 = new SPGA_forFlowShop();
                SPGA_forFlowShop1.setFlowShopData(numberOfJob[r], numberOfMachines);
                SPGA_forFlowShop1.setParameters(numberOfSubPopulations[j], popSize[k],
                    totalSolnsToExamine[0], crossoverRate, mutationRate, elitism, applySecCRX[L], applySecMutation[m]);
                SPGA_forFlowShop1.initiateVars();
                SPGA_forFlowShop1.start();
                SPGA_forFlowShop1 = null;
                System.gc();
                //System.exit(0);
              }//end r
            }//end m
          }// end L
        }// end k
      }// end j
    }// end i

    System.exit(0);

  }

}
