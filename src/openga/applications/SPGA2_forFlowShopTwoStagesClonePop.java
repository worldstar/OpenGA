package openga.applications;
import openga.operator.clone.*;
import openga.chromosomes.*;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class SPGA2_forFlowShopTwoStagesClonePop extends SPGA2_forFlowShopTwoStages{
  public SPGA2_forFlowShopTwoStagesClonePop() {
  }

  double cloneRatio = 1;
  //double migrationRatio = 0.5;
  double implementationTime = 0;
  int replicationNum;

  public void setPopClone(double cloneRatio){
    this.cloneRatio = cloneRatio;
  }

  public void setMigrationRatio(double migrationRatio){
    //this.migrationRatio = migrationRatio;
  }

  public void setReplicationNum(int replicationNum){
    this.replicationNum = replicationNum;
  }

  public void start(){
    openga.util.timeClock timeClock1 = new openga.util.timeClock();
    timeClock1.start();

    //we control the number of iteration of GA here.
    //to run the first and the last sub-population at the first stage.
    int popIndex[] = new int[]{0, 4};//0, 1, 2, 3, 4, 30, 31, 32, 33, 34//0, 1, 8, 9
    for(int k = 0 ; k < popIndex.length ; k ++ ){
      GaMain[popIndex[k]].setCloneOperatpr(clone1, true);
    }

    for(int m = 0 ; m < DEFAULT_generations ; m ++ ){
      //System.out.println("Generation: "+m);
      for(int i = 0 ; i < popIndex.length ; i ++ ){
        //System.out.println("numberOfSubPopulations "+i);
        if(m == 0){//initial each population and its objective values.
          GaMain[popIndex[i]].InitialRun();
          //GaMain[popIndex[i]].setReplacementStrategy(1);
          //GaMain[popIndex[i]].setMutationRate(0.4);
          if(i == 0){
            GaMain[0].initFirstArchieve();
          }
        }
        GaMain[popIndex[i]].setCurrentGeneration(m);
        GaMain[popIndex[i]].startGA();
      }
    }
/*
    //To clone solution
    populationClone populationClone1 = new populationClone();
    populationClone1.setData(Population, GaMain[0].getArchieve(), popIndex, weights, cloneRatio, DEFAULT_PopSize);
    populationClone1.startClonePopulation();
*/
    for (int k = 0; k < numberOfSubPopulations; k++) {
      if (checkGroup(k, popIndex)) {
        GaMain[k].setCloneOperatpr(clone1, true);
      }
    }

    //System.out.println("stage 2");
    //The second stage.
    for(int m = 0 ; m < DEFAULT_generations ; m ++ ){
      //System.out.println("Generation: "+m);
      for(int i = 0 ; i < numberOfSubPopulations ; i ++ ){
        //to skip the two center sub-populations
        if(i < 2 || i > 3){//13 22, 3 6,6 9, 2 3
          //System.out.println("numberOfSubPopulations "+i);
          if(m == 0 && (checkGroup(i, popIndex))){
            populationClone3 populationClone1 = new populationClone3();
            populationClone1.setData(Population, GaMain[0].getArchieve(), Crossover[0], popIndex, weights, 1, DEFAULT_PopSize);
            populationClone1.startClonePopulation(i);
            GaMain[i].ProcessObjectiveAndFitness();
            int tempIndex[] = new int[popIndex.length + 1];
            for(int k = 0 ; k < popIndex.length ; k ++ ){
              tempIndex[k] = popIndex[k];
            }
            tempIndex[popIndex.length] = i;
            //popIndex = tempIndex;

          }
          GaMain[i].setCurrentGeneration(DEFAULT_generations+m);
          GaMain[i].startGA();
        }
      }

      if(m == 0){
        //popIndex = new int[numberOfSubPopulations];
        int counter1 = numberOfSubPopulations - 1;
        for(int k = 0; k < numberOfSubPopulations ; k ++ ){
          //popIndex[k] = counter1 --;
        }
      }
    }
    timeClock1.end();
    implementationTime = timeClock1.getExecutionTime();
    printResults();
  }

  private boolean checkGroup(int index, int[] group){
    for(int i = 0 ; i < group.length ; i ++ ){
      if(index == group[i]){
        return false;
      }
    }
    return true;
  }

  private void migration(int indexes[]){
    populationClone2 populationClone21 = new populationClone2();
    populationClone21.setData(Population, indexes, 0, DEFAULT_PopSize);
    populationClone21.startClonePopulation();
  }

  public void printResults(){
    //to output the implementation result.
    String implementResult = "";
    double refSet[][] = getReferenceSet();
    double objArray[][] = GaMain[0].getArchieveObjectiveValueArray();
    double D1r = calcSolutionQuality(refSet, objArray);
    implementResult = numberOfJob+"\t"+"\t"+cloneRatio+"\t"+D1r+"\t"+implementationTime+"\n";
    objArray = null;
    writeFile("SPGA2_FlowShop 505", implementResult);
    System.out.println(numberOfJob+"\t"+DEFAULT_crossoverRate+"\t"+DEFAULT_mutationRate+"\t"+D1r+"\t"+implementationTime);

    implementResult = "";
    for(int k = 0 ; k < GaMain[0].getArchieve().getPopulationSize() ; k ++ ){
      implementResult += GaMain[0].getArchieve().getSingleChromosome(k).toString1()+"\t";//sequnence
      for(int j = 0 ; j < numberOfObjs ; j ++ ){//for each objectives
        implementResult += GaMain[0].getArchieve().getObjectiveValues(k)[j]+"\t";
      }
      implementResult += "\n";
    }
    writeFile("SPGA2_forFlowShopParetoSet_"+numberOfJob+"_"+replicationNum, implementResult);

    for(int i = 0 ; i < numberOfSubPopulations ; i ++ ){
      GaMain[i].destroidArchive();
      GaMain[i] = null;
    }
  }

  public static void main(String[] args) {
    System.out.println("SPGA2_FlowShop 505DOE");

    int numberOfSubPopulations[] = new int[]{5};//35
    int popSize[] = new int[]{100};//100, 155, 210
    int numberOfJob[] = new int []{20, 40, 60, 80};//20, 40, 60, 80
    int numberOfMachines = 20;
    int totalSolnsToExamine[] = new int[]{100000};//1000000, 1500000, 2000000
    boolean applySecCRX[] = new boolean[]{false, true};//false, true
    boolean applySecMutation[] = new boolean[]{false, true};
    int tournamentSize[] = new int[]{2, 10};
    boolean applyClone[] = new boolean[]{false, true};
    int cloneStrategies[] = new int[]{1};//0: random, 1: swap, 2:inverse, 3:shift, 4:adjacent.
    double cloneRatio[] = new double[]{1};//0, 0.25, 0.5, 0.75, 1
    double migrationRatio[] = new double[]{0.25};//0, 0.25, 0.5, 0.75

    double crossoverRate[] = new double[]{0.6, 1},//{1}
           mutationRate[] = new double[]{0.2, 1},//{0.18}//0.2, 0.5
           elitism = 0.2;
    int repeatExperiments = 30;
    int combinations = 0;
    int counter = 0;

    for(int m = 0 ; m < numberOfJob.length ; m ++ ){
      SPGA2_forFlowShopTwoStagesClonePop SPGA2_forFlowShop1 = new SPGA2_forFlowShopTwoStagesClonePop();
      System.out.println("combinations: "+combinations++);
      SPGA2_forFlowShop1.setReplicationNum(counter);
      SPGA2_forFlowShop1.setFlowShopData(numberOfJob[m], numberOfMachines);
      SPGA2_forFlowShop1.setParameters(numberOfSubPopulations[0], popSize[0],
          totalSolnsToExamine[0], crossoverRate[0], mutationRate[1],
          elitism, applySecCRX[0], applySecMutation[0]);
      SPGA2_forFlowShop1.setCloneActive(applyClone[1], cloneStrategies[0]);
      SPGA2_forFlowShop1.setPopClone(cloneRatio[0]);
      SPGA2_forFlowShop1.initiateVars();
      SPGA2_forFlowShop1.start();
      SPGA2_forFlowShop1 = null;
      System.gc();
    }


/*
    for(int i = 0 ; i < repeatExperiments ; i ++ ){
      for(int j = 0 ; j < mutationRate.length ; j ++ ){
        for(int k = 0 ; k < crossoverRate.length ; k ++){
          for(int m = 0 ; m < numberOfJob.length ; m ++ ){
            SPGA2_forFlowShopTwoStagesClonePop SPGA2_forFlowShop1 = new SPGA2_forFlowShopTwoStagesClonePop();
            System.out.println("combinations: "+combinations++);
            SPGA2_forFlowShop1.setReplicationNum(counter);
            SPGA2_forFlowShop1.setFlowShopData(numberOfJob[m], numberOfMachines);
            SPGA2_forFlowShop1.setParameters(numberOfSubPopulations[0], popSize[0],
                totalSolnsToExamine[0], crossoverRate[k], mutationRate[j],
                elitism, applySecCRX[0], applySecMutation[0]);
            SPGA2_forFlowShop1.setCloneActive(applyClone[1], cloneStrategies[0]);
            SPGA2_forFlowShop1.setPopClone(cloneRatio[0]);
            SPGA2_forFlowShop1.initiateVars();
            SPGA2_forFlowShop1.start();
            SPGA2_forFlowShop1 = null;
            System.gc();
          }
        }
      }
      counter ++;
    }
*/


/*
        for(int i = 0 ; i < repeatExperiments ; i ++ ){
          for(int j = 0 ; j < numberOfJob.length ; j ++ ){
            SPGA2_forFlowShopTwoStagesClonePop SPGA2_forFlowShop1 = new SPGA2_forFlowShopTwoStagesClonePop();
            System.out.println("combinations: "+combinations++);
            SPGA2_forFlowShop1.setReplicationNum(counter);
            SPGA2_forFlowShop1.setFlowShopData(numberOfJob[j], numberOfMachines);
            SPGA2_forFlowShop1.setParameters(numberOfSubPopulations[0], popSize[0],
                totalSolnsToExamine[0], crossoverRate[0], mutationRate[0],
                elitism, applySecCRX[0], applySecMutation[0]);
            SPGA2_forFlowShop1.setCloneActive(applyClone[1], cloneStrategies[0]);
            SPGA2_forFlowShop1.setPopClone(cloneRatio[0]);
            SPGA2_forFlowShop1.initiateVars();
            SPGA2_forFlowShop1.start();
            SPGA2_forFlowShop1 = null;
            System.gc();
          }
          counter ++;
        }



    for(int i = 0 ; i < repeatExperiments ; i ++ ){
      for(int j = 0 ; j < numberOfJob.length ; j ++ ){
        for(int k = 0 ; k < migrationRatio.length ; k ++ ){
          for(int m = 0 ; m < cloneRatio.length ; m ++ ){
            SPGA2_forFlowShopTwoStagesClonePop SPGA2_forFlowShop1 = new SPGA2_forFlowShopTwoStagesClonePop();
            System.out.println("combinations: "+counter++);
            SPGA2_forFlowShop1.setFlowShopData(numberOfJob[j], numberOfMachines);
            SPGA2_forFlowShop1.setParameters(numberOfSubPopulations[0], popSize[0],
                totalSolnsToExamine[0], crossoverRate[0], mutationRate[0],
                elitism, applySecCRX[0], applySecMutation[0]);
            SPGA2_forFlowShop1.setCloneActive(applyClone[1], cloneStrategies[0]);
            SPGA2_forFlowShop1.setMigrationRatio(migrationRatio[k]);//migrationRatio[k]
            SPGA2_forFlowShop1.setPopClone(cloneRatio[m]);//cloneRatio[m]
            SPGA2_forFlowShop1.initiateVars();
            SPGA2_forFlowShop1.start();
            SPGA2_forFlowShop1 = null;
            System.gc();
          }
        }
      }
    }
*/
  }
}