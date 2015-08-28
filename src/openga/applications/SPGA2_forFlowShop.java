package openga.applications;
import openga.chromosomes.*;
import openga.operator.selection.*;
import openga.operator.crossover.*;
import openga.operator.mutation.*;
import openga.operator.clone.*;
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

public class SPGA2_forFlowShop extends SPGA2_forParallelMachine {
  public SPGA2_forFlowShop() {
  }
  /***
   * Scheduling data
   */
  int dueDay[], processingTime[][];
  int numberOfJob = 20;
  int numberOfMachines = 20;

  ObjectiveFunctionFlowShopScheduleI ObjectiveFunction[][];
  cloneI clone1;
  boolean applyClone = false;
  int tournamentSize = 2;

  public void setFlowShopData(int numberOfJob, int numberOfMachines){
    this.numberOfJob = numberOfJob;
    this.numberOfMachines = numberOfMachines;
  }

  public void setCloneActive(boolean applyClone){
    this.applyClone = applyClone;
  }

  public void initiateVars(){
    //initiate scheduling data, we get the data from a program.
    processingTime = getProcessingTime();
    dueDay = getDueDay();
    weights = new double[numberOfSubPopulations][numberOfObjs];

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
    //clone1     = new cloneI[numberOfSubPopulations];
    ObjectiveFunction  = new ObjectiveFunctionFlowShopScheduleI[numberOfSubPopulations][numberOfObjs];
    Fitness   = new FitnessI[numberOfSubPopulations];

    for(int i = 0 ; i < numberOfSubPopulations ; i ++ ){
      GaMain[i]     = new SPGAwithSharedParetoSet();//SPGAwithSharedParetoSet SPGAwithSharedParetoSetAndMuPlusLamda SPGAwithSharedParetoSetWithMining
      Population[i] = new population();
      if(tournamentSize == 2){//binaryTournamentWithSecondArchive tenaryTournament varySizeTournament
        Selection[i]  = new binaryTournament();
      }
      else if(tournamentSize == 5){
        Selection[i]  = new quintetTournament();
      }
      else if(tournamentSize == 10){
        Selection[i]  = new tenaryTournament();
      }
      else{
        Selection[i]  = new twentyTournament();
      }

      clone1  = new solutionVectorCloneWithMutation();//swap mutation
      /*
      if(cloneStrategy == 0){//0: random, 1: swap, 2:inverse, 3:shift, 4:adjacent.
        clone1 = new solutionVectorClone();//random generate solutionVectorClone solutionVectorCloneWithMutation
      }
      else if(cloneStrategy == 1){
        clone1  = new solutionVectorCloneWithMutation();//swap mutation
      }
      else if(cloneStrategy == 2){
        clone1  = new solutionVectorCloneWithInverseMutation();
      }
      else if(cloneStrategy == 3){
        clone1  = new solutionVectorCloneWithShiftMutation();
      }
      else{
        clone1  = new solutionVectorCloneAdjacentSwapWithMutation();
      }
      */

      Crossover[i] = new twoPointCrossover2();//twoPointCrossover2 twoPointCrossover2withAdpative
      Mutation[i]  = new swapMutation();//swapMutation swapMutationWithAdaptive
      ObjectiveFunction[i] = new ObjectiveFunctionFlowShopScheduleI[numberOfObjs];
      ObjectiveFunction[i][0] = new ObjectiveMakeSpanForFlowShop();//the first objective, makespan
      ObjectiveFunction[i][1] = new ObjectiveTardinessForFlowShop();//the second one.
      Fitness[i]    = new FitnessByScalarizedM_objectives();//FitnessByScalarizedM_objectives, TchebycheffFitness
      DEFAULT_generations = totalSolnsToExamine/(DEFAULT_PopSize*numberOfSubPopulations);
      //System.out.println(totalSolnsToExamine+" "+DEFAULT_PopSize+" "+numberOfSubPopulations+" "+DEFAULT_generations);

      //set schedule data to the objectives
      ObjectiveFunction[i][0].setScheduleData(processingTime, numberOfMachines);
      ObjectiveFunction[i][1].setScheduleData(dueDay, processingTime, numberOfMachines);
      //set the data to the GA main program.
      GaMain[i].setData(Population[i], Selection[i], Crossover[i], Mutation[i],
                     ObjectiveFunction[i], Fitness[i], DEFAULT_generations, DEFAULT_PopSize*2, DEFAULT_PopSize,
                     numberOfJob, DEFAULT_crossoverRate, DEFAULT_mutationRate, objectiveMinimization,
                     numberOfObjs, encodeType, elitism);
      //set weight data
      weights[i] = calcWeightsForEachSubPop(i);//calcWeightsForEachSubPop calcUniformWeightsForEachSubPop
      GaMain[i].setWeight(weights[i]);
      //set secondary crossover and mutation operator.
      //GaMain[i].setSecondaryCrossoverOperator(Crossover2[i], applySecCRX);
      GaMain[i].setSecondaryCrossoverOperator(Crossover2, false);
      //GaMain[i].setSecondaryMutationOperator(Mutation2[i], applySecMutation);
      GaMain[i].setSecondaryMutationOperator(Mutation2, false);
      //GaMain[i].setCloneOperatpr(clone1, applyClone);
    }
  }

  public void start(){
    openga.util.timeClock timeClock1 = new openga.util.timeClock();
    timeClock1.start();

    //we control the number of iteration of GA here.
    for(int m = 0 ; m < DEFAULT_generations ; m ++ ){
      //System.out.println("Generation: "+m);

      for(int i = 0 ; i < numberOfSubPopulations ; i ++ ){
        System.out.println("numberOfSubPopulations "+i);
        if(m == 0){//initial each population and its objective values.
          GaMain[i].InitialRun();
          if(i == 0){
            GaMain[i].initFirstArchieve();
          }
          else{
            //GaMain[i].updateNondominatedSon();
          }
        }
        GaMain[i].startGA();
      }
      //System.out.println("GaMain[i].getAarchieve.getPopulationSize() "+GaMain[0].getArchieve().getPopulationSize()+"\t");
    }
    timeClock1.end();
    double implementationTime = timeClock1.getExecutionTime();
    //to output the implementation result.
    String implementResult = "";
    double refSet[][] = getReferenceSet();

    double objArray[][] = GaMain[0].getArchieveObjectiveValueArray();
    double D1r = calcSolutionQuality(refSet, objArray);
    implementResult = numberOfJob+"\t"+"0"+"\t"+D1r+"\t"+implementationTime/1000+"\n";//
    objArray = null;
    writeFile("2011SPGA2forFlowShop Testing Parameter", implementResult);
    System.out.println(implementResult);

    for(int i = 0 ; i < numberOfSubPopulations ; i ++ ){
      GaMain[i].destroidArchive();
      GaMain[i] = null;
    }
  }

  public final double[][] getReferenceSet(){
    openga.applications.data.flowShop data1 = new openga.applications.data.flowShop();
    return data1.getReferenceSet(numberOfJob);
  }

  public final int[] getDueDay(){
    System.out.println("numberOfJob "+numberOfJob);
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

  public final int[][] getProcessingTime(){
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
    System.out.println("SPGA2_forFlowShop 20070815");

    int numberOfSubPopulations[] = new int[]{10};//35
    int popSize[] = new int[]{100};
    int numberOfJob[] = new int []{20};//20, 40, 60, 80
    int numberOfMachines = 20;
    int totalSolnsToExamine[] = new int[]{100000};//1000000, 1500000, 2000000
    boolean applySecCRX[] = new boolean[]{false, true};//false, true
    boolean applySecMutation[] = new boolean[]{false, true};
    boolean applyClone[] = new boolean[]{false, true};

    double crossoverRate[] = new double[]{0.9},
           mutationRate[] = new double[]{0.18},
           elitism = 0.2;
    int repeatExperiments = 30;

    int counter = 0;

    for(int i = 0 ; i < repeatExperiments ; i ++ ){
      for(int j = 0 ; j < numberOfJob.length ; j ++ ){
        System.out.println("combinations: "+counter++);
        SPGA2_forFlowShop SPGA2_forFlowShop1 = new SPGA2_forFlowShop();
        SPGA2_forFlowShop1.setFlowShopData(numberOfJob[j], numberOfMachines);

        SPGA2_forFlowShop1.setParameters(numberOfSubPopulations[0], popSize[0],
            totalSolnsToExamine[0], crossoverRate[0], mutationRate[0],
            elitism, applySecCRX[0], applySecMutation[0]);
        SPGA2_forFlowShop1.initiateVars();
        SPGA2_forFlowShop1.start();
        SPGA2_forFlowShop1 = null;
        System.gc();
      }
    }

    /*
    for(int i = 0 ; i < repeatExperiments ; i ++ ){
      for(int j = 0 ; j < numberOfJob.length ; j ++ ){
        System.out.println("combinations: "+counter++);
        SPGA2_forFlowShop SPGA2_forFlowShop1 = new SPGA2_forFlowShop();
        SPGA2_forFlowShop1.setFlowShopData(numberOfJob[j], numberOfMachines);

        SPGA2_forFlowShop1.setParameters(numberOfSubPopulations[0], popSize[0],
            totalSolnsToExamine[0], crossoverRate[0], mutationRate[0],
            elitism, applySecCRX[0], applySecMutation[0]);
        SPGA2_forFlowShop1.initiateVars();
        SPGA2_forFlowShop1.start();
        SPGA2_forFlowShop1 = null;
        System.gc();
      }
    }

    openga.applications.data.threeFactorFullFactorial screeningExperiment1 = new openga.applications.data.threeFactorFullFactorial();
    int settings[][] = screeningExperiment1.getCombinations();
    for(int i = 0 ; i < repeatExperiments ; i ++ ){
      for(int j = 0 ; j < settings.length ; j ++ ){
        System.out.println("combinations: "+counter++);
        SPGA2_forFlowShop SPGA2_forFlowShop1 = new SPGA2_forFlowShop();
        SPGA2_forFlowShop1.setFlowShopData(numberOfJob[settings[j][2]], numberOfMachines);

        SPGA2_forFlowShop1.setParameters(numberOfSubPopulations[settings[j][1]], popSize[0],
            totalSolnsToExamine[0], crossoverRate[settings[j][0]], mutationRate[0],
            elitism, applySecCRX[0], applySecMutation[0]);
        SPGA2_forFlowShop1.initiateVars();
        SPGA2_forFlowShop1.start();
        SPGA2_forFlowShop1 = null;
        System.gc();
      }
    }
*/
    /*
    for(int i = 0 ; i < repeatExperiments ; i ++ ){
      for(int j = 0 ; j < numberOfSubPopulations.length ; j ++ ){
        for(int k = 0 ; k < popSize.length ; k ++ ){
          for(int L = 0 ; L < applySecCRX.length ; L ++ ){
            for(int m = 0 ; m < applySecMutation.length ; m ++ ){
              for(int r = 0 ; r < numberOfJob.length ; r ++ ){
                for(int s = 0 ; s < crossoverRate.length ; s ++ ){
                  for(int u = 0 ; u < mutationRate.length ; u ++ ){
                    System.out.println(numberOfJob[r]+"\t"+numberOfSubPopulations[j]+"\t"+ popSize[k]+"\t"+numberOfMachines);
                    System.out.println("combinations: "+counter++);
                    SPGA2_forFlowShop SPGA2_forFlowShop1 = new SPGA2_forFlowShop();
                    SPGA2_forFlowShop1.setFlowShopData(numberOfJob[r], numberOfMachines);
                    SPGA2_forFlowShop1.setParameters(numberOfSubPopulations[j], popSize[k],
                        totalSolnsToExamine[0], crossoverRate[s], mutationRate[u], elitism, applySecCRX[L], applySecMutation[m]);
                    SPGA2_forFlowShop1.initiateVars();
                    SPGA2_forFlowShop1.start();
                    SPGA2_forFlowShop1 = null;
                    System.gc();
                  }//end u
                }//end s
              }//end r
            }//end m
          }// end L
        }// end k
      }// end j
    }// end i
        */

    System.exit(0);

  }

}