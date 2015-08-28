package openga.applications;
import openga.operator.clone.*;
import openga.chromosomes.*;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class SPGA2_forFlowShopCloneReplace extends SPGA2_forFlowShopTwoStages{
  public SPGA2_forFlowShopCloneReplace() {
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
    openga.operator.selection.twoPopReplacement2 replacement1 = new openga.operator.selection.twoPopReplacement2();
    replacement1.setThresholdValues(0.6, 0.15);

    //we control the number of iteration of GA here.
    for(int k = 0 ; k < numberOfSubPopulations ; k ++ ){
      GaMain[k].setCloneOperatpr(clone1, true);
    }

    //we control the number of iteration of GA here.
    for(int m = 0 ; m < DEFAULT_generations ; m ++ ){
      //System.out.println("Generation: "+m);
      for(int i = 0 ; i < numberOfSubPopulations ; i ++ ){
        if(m == 0){//initial each population and its objective values.
          GaMain[i].InitialRun();
          if(i == 0){
            GaMain[i].initFirstArchieve();
          }
        }
        GaMain[i].setCurrentGeneration(m);
        GaMain[i].startGA();
      }//end for all sub-populations.

      //replacement
      for(int i = 0 ; i < numberOfSubPopulations ; i ++ ){
        replacement1.replacementStage(Population, Population[i], i);
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

  public void printResults(){
    //to output the implementation result.
    String implementResult = "";
    double refSet[][] = getReferenceSet();
    double objArray[][] = GaMain[0].getArchieveObjectiveValueArray();
    double D1r = calcSolutionQuality(refSet, objArray);
    implementResult = numberOfJob+"\t"+"\t"+cloneRatio+"\t"+D1r+"\t"+implementationTime+"\n";
    objArray = null;
    writeFile("SPGA2_FlowShop 506", implementResult);
    System.out.println(numberOfJob+"\t"+cloneRatio+"\t"+D1r+"\t"+implementationTime);
/*
    implementResult = "";
    for(int k = 0 ; k < GaMain[0].getArchieve().getPopulationSize() ; k ++ ){
      implementResult += GaMain[0].getArchieve().getSingleChromosome(k).toString1()+"\t";//sequnence
      for(int j = 0 ; j < numberOfObjs ; j ++ ){//for each objectives
        implementResult += GaMain[0].getArchieve().getObjectiveValues(k)[j]+"\t";
      }
      implementResult += "\n";
    }
    writeFile("SPGA2_forFlowShopParetoSet_"+numberOfJob+"_"+replicationNum, implementResult);
*/
    for(int i = 0 ; i < numberOfSubPopulations ; i ++ ){
      GaMain[i].destroidArchive();
      GaMain[i] = null;
    }
  }

  public static void main(String[] args) {
    System.out.println("SPGA2_FlowShop 506");

    int numberOfSubPopulations[] = new int[]{10};//35
    int popSize[] = new int[]{200};//100, 155, 210
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

    double crossoverRate[] = new double[]{0.6},//{1}
           mutationRate[] = new double[]{0.5},//{0.18}
           elitism = 0.2;
    int repeatExperiments = 30;
    int combinations = 0;
    int counter = 0;

    for(int i = 0 ; i < repeatExperiments ; i ++ ){
      for(int j = 0 ; j < numberOfJob.length ; j ++ ){
        SPGA2_forFlowShopCloneReplace SPGA2_forFlowShop1 = new SPGA2_forFlowShopCloneReplace();
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

  }

}