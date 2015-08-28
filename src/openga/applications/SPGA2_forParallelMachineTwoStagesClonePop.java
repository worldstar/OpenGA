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

public class SPGA2_forParallelMachineTwoStagesClonePop extends SPGA2_forParallelMachineTwoStages {
  public SPGA2_forParallelMachineTwoStagesClonePop() {
  }
  int replicationNum;
  double implementationTime = 0;

  public void setReplicationNum(int replicationNum){
    this.replicationNum = replicationNum;
  }

  public void start(){
    openga.util.timeClock timeClock1 = new openga.util.timeClock();
    timeClock1.start();
    //we control the number of iteration of GA here.
    //to run the first and the last sub-population at the first stage.
    int popIndex[] = new int[]{0, 1, 8, 9};//0, 1, 2, 3, 4, 35, 36, 37, 38, 39//0, 1, 2, 3, 4, 30, 31, 32, 33, 34
    for(int m = 0 ; m < DEFAULT_generations ; m ++ ){
      //System.out.println("Generation: "+m);
      for(int i = 0 ; i < popIndex.length ; i ++ ){
        //System.out.println("numberOfSubPopulations "+i);
        if(m == 0){//initial each population and its objective values.
          GaMain[popIndex[i]].InitialRun();
          if(i == 0){
            GaMain[i].initFirstArchieve();
          }
        }
        GaMain[popIndex[i]].startGA();
      }
      if(m == 0){
        //migration(popIndex);
        //To clone solution
        for(int k = 0 ; k < popIndex.length ; k ++ ){
          GaMain[popIndex[k]].setCloneOperatpr(clone1, true);
        }
      }
    }
/*
    //To clone solution
    populationClone populationClone1 = new populationClone();
    populationClone1.setData(Population, GaMain[0].getArchieve(), popIndex, weights, 0.5, DEFAULT_PopSize);
    populationClone1.startClonePopulation();
*/

    for (int k = 0; k < numberOfSubPopulations; k++) {
      if (checkGroup(k, popIndex)) {
        GaMain[k].setCloneOperatpr(clone1, true);
      }
    }

    for(int m = 0 ; m < DEFAULT_generations ; m ++ ){
      //System.out.println("Generation: "+m);
      for(int i = 0 ; i < numberOfSubPopulations ; i ++ ){
        //to skip the two center sub-populations
        if(i < 3 || i > 6){//4 7
          //System.out.println("numberOfSubPopulations "+i);
          if(m == 0 && (checkGroup(i, popIndex))){
            //GaMain[i].InitialRun();
            populationClone3 populationClone1 = new populationClone3();
            populationClone1.setData(Population, GaMain[0].getArchieve(), Crossover[0], popIndex, weights, 1, DEFAULT_PopSize);
            populationClone1.startClonePopulation(i);
            GaMain[i].ProcessObjectiveAndFitness();
            int tempIndex[] = new int[popIndex.length + 1];
            for(int k = 0 ; k < popIndex.length ; k ++ ){
              tempIndex[k] = popIndex[k];
            }
            tempIndex[popIndex.length] = i;
            popIndex = tempIndex;
          }
          GaMain[i].startGA();
        }
      }
    }
    timeClock1.end();
    implementationTime = timeClock1.getExecutionTime();
    //to output the implementation result.
    outputResults();

    for(int i = 0 ; i < numberOfSubPopulations ; i ++ ){
      GaMain[i].destroidArchive();
      GaMain[i] = null;
    }
  }

  public void outputResults(){
    String implementResult = "";
/*
    for(int i = 0 ; i < GaMain[0].getArchieve().getPopulationSize() ; i ++ ){
      for(int j = 0 ; j < numberOfObjs ; j ++ ){//for each objectives
        implementResult += GaMain[0].getArchieve().getObjectiveValues(i)[j]+"\t";
      }
      implementResult += "\n";
    }
    writeFile("SPGA2_2_"+numberOfJob+"_"+replicationNum, implementResult);
*/
    double refSet[][] = getReferenceSet();
    double objArray[][] = GaMain[0].getArchieveObjectiveValueArray();
    double D1r = calcSolutionQuality(refSet, objArray);
    implementResult = numberOfJob+"\t"+numberOfSubPopulations+"\t"+DEFAULT_crossoverRate+"\t"+DEFAULT_mutationRate+"\t"+D1r+"\t"+implementationTime/1000.0+"\n";
    objArray = null;
    writeFile("SPGA2_2_ParallelReplicationsWithClone0502 NoCXArchive 2X 0.5ratio", implementResult);
    System.out.println(implementResult);

    implementResult = "";
    for(int k = 0 ; k < GaMain[0].getArchieve().getPopulationSize() ; k ++ ){
      implementResult += GaMain[0].getArchieve().getSingleChromosome(k).toString1()+"\t";//sequnence
      for(int j = 0 ; j < numberOfObjs ; j ++ ){//for each objectives
        implementResult += GaMain[0].getArchieve().getObjectiveValues(k)[j]+"\t";
      }
      implementResult += "\n";
    }
    writeFile("SPGA2_forParallelParetoSet_"+numberOfJob+"_"+replicationNum, implementResult);
  }

  public double[][] getReferenceSet(){
    openga.applications.data.parallelMachineNew data1 = new openga.applications.data.parallelMachineNew();
    int array[][] = data1.getReferenceSet(numberOfJob);
    double newarray[][] = new double[array.length][array[0].length];
    for(int i = 0 ; i < array.length ; i ++ ){
      for(int j = 0 ; j < array[0].length ; j ++ ){
        newarray[i][j] = array[i][j];
      }
    }
    return newarray;
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
    d1r1.calcD1rWithoutNormalization();
    return d1r1.getD1r();
  }

  public static void main(String[] args) {
    System.out.println("SPGA 2 Parallel SPGA2_2_ParallelReplicationsWithClone0502");
    int numberOfSubPopulations[] = new int[]{10};//10, 20, 30, 40
    int popSize[] = new int[]{100};//100, 155, 210
    int numberOfJob[] = new int []{65};//35, 50, 65, 80
    int numberOfMachines[] = new int[]{7, 10, 13, 16};//7, 10, 13, 16
    int totalSolnsToExamine[] = new int[]{25000, 50000, 75000, 100000};
    boolean applySecCRX[] = new boolean[]{false};//false, true
    boolean applySecMutation[] = new boolean[]{false};
    boolean applyClone[] = new boolean[]{false, true};
    int cloneStrategies[] = new int[]{1};//0: random, 1: swap, 2:inverse, 3:shift, 4:adjacent.

    double crossoverRate[] = new double[]{0.6},//0.6, 0.9 {0.6}
           mutationRate [] = new double[]{1},//0.1, 0.5 {0.5}
           elitism = 0.2;
    int repeatExperiments = 30;
    int counter = 0;
    int combinations = 0;
/*
    for(int i = 0 ; i < repeatExperiments ; i ++ ){
      for(int j = 0 ; j < crossoverRate.length ; j ++ ){
        for(int k = 0 ; k < mutationRate.length ; k ++ ){
          for(int r = 0 ; r < numberOfJob.length ; r ++ ){
            System.out.println("combinations: "+combinations++);
            SPGA2_forParallelMachineTwoStagesClonePop SPGA2_forParallelMachine1 = new SPGA2_forParallelMachineTwoStagesClonePop();
            SPGA2_forParallelMachine1.setParallelMachineData(numberOfJob[r], numberOfMachines[r]);
            SPGA2_forParallelMachine1.setParameters(numberOfSubPopulations[0], popSize[0],
                totalSolnsToExamine[r], crossoverRate[j], mutationRate[k], elitism, applySecCRX[0], applySecMutation[0]);
            SPGA2_forParallelMachine1.setCloneActive(applyClone[1], cloneStrategies[0]);
            SPGA2_forParallelMachine1.setReplicationNum(counter);
            SPGA2_forParallelMachine1.initiateVars();
            SPGA2_forParallelMachine1.start();
            SPGA2_forParallelMachine1 = null;
            System.gc();
          }//end r
          counter ++;
        }
      }
    }// end i
*/

    for(int i = 0 ; i < repeatExperiments ; i ++ ){
      for(int r = 0 ; r < numberOfJob.length ; r ++ ){
        System.out.println("combinations: "+combinations++);
        SPGA2_forParallelMachineTwoStagesClonePop SPGA2_forParallelMachine1 = new SPGA2_forParallelMachineTwoStagesClonePop();
        SPGA2_forParallelMachine1.setParallelMachineData(numberOfJob[r], numberOfMachines[r]);
        SPGA2_forParallelMachine1.setParameters(numberOfSubPopulations[0], popSize[0],
            totalSolnsToExamine[r], crossoverRate[0], mutationRate[0], elitism, applySecCRX[0], applySecMutation[0]);
        SPGA2_forParallelMachine1.setCloneActive(applyClone[1], cloneStrategies[0]);
        SPGA2_forParallelMachine1.setReplicationNum(counter);
        SPGA2_forParallelMachine1.initiateVars();
        SPGA2_forParallelMachine1.start();
        SPGA2_forParallelMachine1 = null;
        System.gc();
      }//end r
      counter ++;
    }// end i

    System.exit(0);
  }

}