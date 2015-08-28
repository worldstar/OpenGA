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
 * <p>Description: The main characteristics of the SPGA2 is to implement the share Pareto set and using multiple
 * crossover and mutation operator.</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class SPGA2_forParallelMachine extends SPGA_forParallelMachine {
  public SPGA2_forParallelMachine(){
  }
  /**
   * The SPGA' parameters
   */
  public int numberOfSubPopulations = 40;
  public boolean applySecCRX = false;
  public boolean applySecMutation = false;
  cloneI clone1;
  boolean applyClone = false;
  int tournamentSize = 2;
  int cloneStrategy = 2;
  double timeToClone = 0;
  double weights[][];

  /***
   * Scheduling data
   */
  int numberOfJob = 50;
  int numberOfMachines = 18;

  public int
    DEFAULT_generations = 200,
    DEFAULT_PopSize     = 5000,
    DEFAULT_initPopSize = 8000,
    totalSolnsToExamine = 1000000;//to fix the total number of solutions to examine.

  public double
     DEFAULT_crossoverRate = 0.9,
     DEFAULT_mutationRate  = 0.1,
     elitism               = 0.2;     //the percentage of elite chromosomes

 public void setParameters(int numberOfSubPopulations, int PopSize, int totalSolnsToExamine,
                           double crossoverRate, double mutationRate, double elitism,
                           boolean applySecCRX, boolean applySecMutation){
   this.numberOfSubPopulations = numberOfSubPopulations;
   DEFAULT_PopSize = PopSize;
   this.totalSolnsToExamine = totalSolnsToExamine;
   this.DEFAULT_crossoverRate = crossoverRate;
   this.DEFAULT_mutationRate = mutationRate;
   this.elitism = elitism;
   DEFAULT_generations = totalSolnsToExamine/(PopSize*numberOfSubPopulations);
   this.applySecCRX = applySecCRX;
   this.applySecMutation = applySecMutation;
 }

 public void setParallelMachineData(int numberOfJob, int numberOfMachines){
   this.numberOfJob = numberOfJob;
   this.numberOfMachines = numberOfMachines;
 }

 public void setCloneActive(boolean applyClone, int cloneStrategy){
   this.applyClone = applyClone;
   this.cloneStrategy = cloneStrategy;
 }

 public void setTimeToClone(double timeToClone){
   this.timeToClone = timeToClone;
 }

 public void setTournamentSize(int tournamentSize){
   this.tournamentSize = tournamentSize;
 }

  public void initiateVars(){
    //initiate scheduling data. We use the benchmark problem.
    openga.applications.data.parallelMachine data1 = new openga.applications.data.parallelMachine();
    dueDay = data1.getTestData3_DueDay(numberOfJob);
    processingTime = data1.getTestData3_processingTime(numberOfJob);
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
    ObjectiveFunction  = new ObjectiveFunctionScheduleI[numberOfSubPopulations][numberOfObjs];
    Fitness   = new FitnessI[numberOfSubPopulations];

    for(int i = 0 ; i < numberOfSubPopulations ; i ++ ){
      GaMain[i]     = new SPGAwithSharedParetoSet();//singleThreadGA SPGAwithSharedParetoSet adaptiveGA
      Population[i] = new population();

      if(tournamentSize == 2){//binaryTournament similaritySelection2 tenaryTournament varySizeTournament
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
     //random generate solutionVectorClone solutionVectorCloneWithMutation
     //solutionVectorCloneAdjacentSwapWithMutation solutionVectorCloneWithMiningGene
     clone1 = new solutionVectorCloneWithMutation();


      Crossover[i]  = new twoPointCrossover2();//oneByOneChromosomeCrossover twoPointCrossover2 twoPointCrossover2withAdpative
      Crossover2 = new PMX();
      Mutation[i]   = new swapMutation();//swapMutation shiftMutation swapMutationWithAdaptive
      Mutation2  = new shiftMutation();//shiftMutation

      ObjectiveFunction[i] = new ObjectiveFunctionScheduleI[numberOfObjs];
      ObjectiveFunction[i][0] = new ObjectiveMakeSpan();//the first objective, makespan
      ObjectiveFunction[i][1] = new ObjectiveTardiness();//the second one.
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
      weights[i] = calcWeightsForEachSubPop(i);
      GaMain[i].setWeight(weights[i]);
      //set secondary crossover and mutation operator.
      GaMain[i].setSecondaryCrossoverOperator(Crossover2, applySecCRX);
      GaMain[i].setSecondaryMutationOperator(Mutation2, applySecMutation);
      //GaMain[i].setCloneOperatpr(clone1, applyClone);
    }
  }

  public void start(){
    openga.util.timeClock timeClock1 = new openga.util.timeClock();
    timeClock1.start();

    //we control the number of iteration of GA here.
    for(int m = 0 ; m < DEFAULT_generations ; m ++ ){
      //System.out.println(m);

      if(m == timeToClone*DEFAULT_generations){
        //To clone solution
        for(int k = 0 ; k < numberOfSubPopulations ; k ++ ){
          GaMain[k].setCloneOperatpr(clone1, applyClone);
        }
      }

      for(int i = 0 ; i < numberOfSubPopulations ; i ++ ){
        if(m == 0){//initial each population and its objective values.
          GaMain[i].InitialRun();
          if(i == 0){
            GaMain[i].initFirstArchieve();
          }
        }
        //start the GA processes.
        GaMain[i].startGA();
      }
    }

    timeClock1.end();
    //System.out.println(timeClock1.getExecutionTime());
    //to output the implementation result.
    String implementResult = "";
    double refSet[][] = getReferenceSet();

    double objArray[][] = GaMain[0].getArchieveObjectiveValueArray();
    double D1r = calcSolutionQuality(refSet, objArray);
    implementResult = numberOfJob+"\t"+1+"\t"+1+"\t"+D1r+"\t"+timeClock1.getExecutionTime()/1000+"\n";
    objArray = null;
    writeFile("SPGA2_forParallel 20060302 Mining Gene Clone Mating with Archive", implementResult);
    System.out.println(implementResult);
/*
    implementResult = "";
    for(int k = 0 ; k < GaMain[0].getArchieve().getPopulationSize() ; k ++ ){
      implementResult += GaMain[0].getArchieve().getSingleChromosome(k).toString1()+"\t";//sequnence
      for(int j = 0 ; j < numberOfObjs ; j ++ ){//for each objectives
        implementResult += GaMain[0].getArchieve().getObjectiveValues(k)[j]+"\t";
      }
      implementResult += "\n";
    }
    //writeFile("SPGA2_forParallelMachineParetoSet"+System.currentTimeMillis(), implementResult);
*/

    for(int i = 0 ; i < numberOfSubPopulations ; i ++ ){
      GaMain[i].destroidArchive();
      GaMain[i] = null;
    }
  }

  public double[][] getReferenceSet(){
    openga.applications.data.parallelMachine data1 = new openga.applications.data.parallelMachine();
    if(numberOfJob == 35){
      return data1.getParetoJob35M10();
    }
    else if(numberOfJob == 50){
      return data1.getParetoJob50M15();
    }
    else{//for job = 65
      return data1.getParetoJob65M18();
    }
  }

  /**
   * To evaluate the solution quality by some metric. It uses the D1r here.
   * @param refSet The current known Pareto set
   * @param obtainedPareto After the implementation of your algorithm.
   * @return The D1r value.
   */
  public double calcSolutionQuality(double refSet[][], double obtainedPareto[][]){
    openga.util.algorithm.D1r d1r1 = new openga.util.algorithm.D1r();
    d1r1.setData(refSet, obtainedPareto);
    d1r1.calcD1r();
    return d1r1.getD1r();
  }

  public static void main(String[] args) {
    System.out.println("SPGA2_forParallel 20060302 Mining Gene Clone Mating with Archive");
    int numberOfSubPopulations[] = new int[]{40};//10, 20, 30, 40
    int popSize[] = new int[]{210};//100, 155, 210
    int numberOfJob[] = new int []{35, 50, 65};//35, 50, 65
    int numberOfMachines[] = new int[]{10, 15, 18};//10, 15, 18
    int totalSolnsToExamine[] = new int[]{1000000};//1000000, 1500000, 2000000
    boolean applySecCRX[] = new boolean[]{false};//false, true
    boolean applySecMutation[] = new boolean[]{true};
    boolean applyClone[] = new boolean[]{false, true};
    int tournamentSize[] = new int[]{10};//2, 10
    int cloneStrategies[] = new int[]{2};//0: random, 1: swap, 2:inverse, 3:shift, 4:adjacent.0, 1, 2, 3, 4
    double timeToClone[] = new double[]{0};//0, 0.25, 0.5, 0.75

    double crossoverRate = 0.9,
           mutationRate = 0.1,
           elitism = 0.2;
    int repeatExperiments = 15;
    int counter = 0;

/*
    for(int i = 0 ; i < repeatExperiments ; i ++ ){
      for(int j = 0 ; j < applyClone.length ; j ++ ){
        for(int k = 0 ; k < tournamentSize.length ; k ++ ){
          for(int r = 0 ; r < numberOfJob.length ; r ++ ){
            System.out.println("combinations: "+counter++);
            SPGA2_forParallelMachine SPGA2_forParallelMachine1 = new SPGA2_forParallelMachine();
            SPGA2_forParallelMachine1.setParallelMachineData(numberOfJob[r], numberOfMachines[r]);
            SPGA2_forParallelMachine1.setParameters(numberOfSubPopulations[0], popSize[0],
                totalSolnsToExamine[0], crossoverRate, mutationRate, elitism, applySecCRX[0], applySecMutation[0]);
            SPGA2_forParallelMachine1.setCloneActive(applyClone[j]);
            SPGA2_forParallelMachine1.setTournamentSize(tournamentSize[k]);
            SPGA2_forParallelMachine1.initiateVars();
            SPGA2_forParallelMachine1.start();
            SPGA2_forParallelMachine1 = null;
            System.gc();
          }
        }
      }
    }// end i

     for(int i = 0 ; i < repeatExperiments ; i ++ ){
      for(int j = 0 ; j < numberOfJob.length ; j ++ ){
         for(int r = 0 ; r < cloneStrategies.length ; r ++ ){
            for(int k = 0 ; k < timeToClone.length ; k ++ ){
              System.out.println("combinations: "+counter++);
              SPGA2_forParallelMachine SPGA2_forParallelMachine1 = new SPGA2_forParallelMachine();
              SPGA2_forParallelMachine1.setParallelMachineData(numberOfJob[j], numberOfMachines[j]);
              SPGA2_forParallelMachine1.setParameters(numberOfSubPopulations[0], popSize[0],
                  totalSolnsToExamine[0], crossoverRate, mutationRate, elitism, applySecCRX[0], applySecMutation[0]);
              SPGA2_forParallelMachine1.setCloneActive(applyClone[0], cloneStrategies[r]);
              SPGA2_forParallelMachine1.setTournamentSize(tournamentSize[0]);
              SPGA2_forParallelMachine1.initiateVars();
              SPGA2_forParallelMachine1.setTimeToClone(timeToClone[k]);
              SPGA2_forParallelMachine1.start();
              SPGA2_forParallelMachine1 = null;
              System.gc();
            }
         }
      }
    }// end i

*/

     for(int i = 0 ; i < repeatExperiments ; i ++ ){
       for(int r = 0 ; r < numberOfJob.length ; r ++ ){
         System.out.println("combinations: "+counter++);
         SPGA2_forParallelMachine SPGA2_forParallelMachine1 = new SPGA2_forParallelMachine();
         SPGA2_forParallelMachine1.setParallelMachineData(numberOfJob[r], numberOfMachines[r]);
         SPGA2_forParallelMachine1.setParameters(numberOfSubPopulations[0], popSize[0],
             totalSolnsToExamine[0], crossoverRate, mutationRate, elitism, applySecCRX[0], applySecMutation[0]);
         SPGA2_forParallelMachine1.setCloneActive(applyClone[0], 2);
         SPGA2_forParallelMachine1.initiateVars();
         SPGA2_forParallelMachine1.start();
         SPGA2_forParallelMachine1.setTimeToClone(timeToClone[0]);
         SPGA2_forParallelMachine1 = null;
         System.gc();
       }//end r
    }// end i



/*
    for(int i = 0 ; i < repeatExperiments ; i ++ ){
      for(int j = 0 ; j < numberOfSubPopulations.length ; j ++ ){
        for(int k = 0 ; k < popSize.length ; k ++ ){
          for(int L = 0 ; L < applySecCRX.length ; L ++ ){
            for(int m = 0 ; m < applySecMutation.length ; m ++ ){
              for(int r = 0 ; r < numberOfJob.length ; r ++ ){
                System.out.println(numberOfJob[r]+"\t"+numberOfSubPopulations[j]+"\t"+ popSize[k]);
                System.out.println("combinations: "+counter++);
                SPGA2_forParallelMachine SPGA2_forParallelMachine1 = new SPGA2_forParallelMachine();
                SPGA2_forParallelMachine1.setParallelMachineData(numberOfJob[r], numberOfMachines[r]);
                SPGA2_forParallelMachine1.setParameters(numberOfSubPopulations[j], popSize[k],
                    totalSolnsToExamine[0], crossoverRate, mutationRate, elitism, applySecCRX[L], applySecMutation[m]);
                SPGA2_forParallelMachine1.initiateVars();
                SPGA2_forParallelMachine1.start();
                SPGA2_forParallelMachine1 = null;
                System.gc();
                //System.exit(0);
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