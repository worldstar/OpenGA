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

public class SPGA2_forFlowShopTwoStages extends SPGA2_forFlowShop{
  public SPGA2_forFlowShopTwoStages() {
  }

  public void setCloneActive(boolean applyClone){
    this.applyClone = applyClone;
  }

  public void setTournamentSize(int tournamentSize){
    this.tournamentSize = tournamentSize;
  }

  public void start(){
    //we control the number of iteration of GA here.
    //to run the first and the last sub-population at the first stage.
    int popIndex[] = new int[]{0, 1, 2, 3, 4, 30, 31, 32, 33, 34};
    for(int m = 0 ; m < DEFAULT_generations ; m ++ ){
      //System.out.println("Generation: "+m);
      for(int i = 0 ; i < popIndex.length ; i ++ ){
        //System.out.println("numberOfSubPopulations "+i);
        if(m == 0){//initial each population and its objective values.
          GaMain[popIndex[i]].InitialRun();
          if(i == 0){
            GaMain[i].initFirstArchieve();
          }
          else{
            //GaMain[i].updateNondominatedSon();
          }
        }
        GaMain[popIndex[i]].startGA();
      }
    }

    for(int m = 0 ; m < DEFAULT_generations ; m ++ ){
      //System.out.println("Generation: "+m);
      for(int i = 0 ; i < numberOfSubPopulations ; i ++ ){
        //to skip the two center sub-populations
        if(i < 13 || i > 22){
          //System.out.println("numberOfSubPopulations "+i);
          if(m == 0 && (checkGroup(i, popIndex))){//initial each population and its objective values.
            GaMain[i].InitialRun();
          }
          GaMain[i].startGA();
        }
      }
    }


    //to output the implementation result.
    String implementResult = "";
    double refSet[][] = getReferenceSet();
    double objArray[][] = GaMain[0].getArchieveObjectiveValueArray();
    double D1r = calcSolutionQuality(refSet, objArray);
    implementResult = numberOfJob+"\t"+applyClone+"\t"+tournamentSize+"\t"+D1r+"\n";
    objArray = null;
    writeFile("SPGA2NewTwoStages_Exp060205", implementResult);
    System.out.println(numberOfJob+"\t"+applyClone+"\t"+tournamentSize+"\t"+D1r);

    for(int i = 0 ; i < numberOfSubPopulations ; i ++ ){
      GaMain[i].destroidArchive();
      GaMain[i] = null;
    }
  }

  private boolean checkGroup(int index, int[] group){
    for(int i = 0 ; i < group.length ; i ++ ){
      if(index == group[i]){
        return false;
      }
    }
    return true;
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
    System.out.println("SPGA2NewTwoStages_Exp070815");

    int numberOfSubPopulations[] = new int[]{35};
    int popSize[] = new int[]{200};
    int numberOfJob[] = new int []{20, 40, 60, 80};//20, 40, 60, 80
    int numberOfMachines = 20;
    int totalSolnsToExamine[] = new int[]{100000};//1000000, 1500000, 2000000
    boolean applySecCRX[] = new boolean[]{false, true};//false, true
    boolean applySecMutation[] = new boolean[]{false, true};
    boolean applyClone[] = new boolean[]{false, true};
    int tournamentSize[] = new int[]{2, 10};

    double crossoverRate[] = new double[]{1},
           mutationRate[] = new double[]{0.18},
           elitism = 0.2;
    int repeatExperiments = 5;
    int counter = 0;

    for(int i = 0 ; i < repeatExperiments ; i ++ ){
      for(int j = 0 ; j < numberOfJob.length ; j ++ ){
        for(int k = 0 ; k < applyClone.length ; k ++ ){
          for(int m = 0 ; m < tournamentSize.length ; m ++ ){
            SPGA2_forFlowShopTwoStages SPGA2_forFlowShop1 = new SPGA2_forFlowShopTwoStages();
            System.out.println("combinations: "+counter++);
            //double lbWeight, double interval, double stepLength, int generationChange){
            SPGA2_forFlowShop1.setFlowShopData(numberOfJob[j], numberOfMachines);
            SPGA2_forFlowShop1.setParameters(numberOfSubPopulations[0], popSize[0],
                totalSolnsToExamine[0], crossoverRate[0], mutationRate[0],
                elitism, applySecCRX[0], applySecMutation[0]);
            SPGA2_forFlowShop1.setCloneActive(applyClone[k]);
            SPGA2_forFlowShop1.setTournamentSize(tournamentSize[m]);
            SPGA2_forFlowShop1.initiateVars();
            SPGA2_forFlowShop1.start();
            SPGA2_forFlowShop1 = null;
            System.gc();
          }
        }
      }
    }

/*
    for(int i = 0 ; i < repeatExperiments ; i ++ ){
      for(int j = 0 ; j < numberOfJob.length ; j ++ ){
        SPGA2_forFlowShopTwoStages SPGA2_forFlowShop1 = new SPGA2_forFlowShopTwoStages();
        System.out.println("combinations: "+counter++);
        //double lbWeight, double interval, double stepLength, int generationChange){
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
*/
  }
}