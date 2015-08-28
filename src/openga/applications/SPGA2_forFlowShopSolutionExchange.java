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
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class SPGA2_forFlowShopSolutionExchange extends SPGA2_forFlowShop {
  public SPGA2_forFlowShopSolutionExchange() {
  }
  boolean direction = true;//true and false are clockwise and counter clockwise.
  int counter = -1;

  public void setCloneActive(boolean applyClone){
    this.applyClone = applyClone;
  }

  public void setTournamentSize(int tournamentSize){
    this.tournamentSize = tournamentSize;
  }

  /***
  * @popIndex the range of population would like to be run.
  * @counter the position of last population
  * @direction the clockwise or counter clockwise direction.
  **/

  public int getActivePop(int popIndex[], int counter, boolean direction){
    int index = 0;//the population index
    if(direction == true){//clockwise
      counter ++;
      index = popIndex[counter];
      if(counter == popIndex.length - 1){//be the end of the clockwise direction
        direction = false;
      }
    }
    else{
      index = popIndex[counter];
      counter --;
      if(counter == -1){//be the end of the clockwise direction
        direction = true;
      }
    }
    return index;
  }

  public void start(){
    //we control the number of iteration of GA here.
    //to run the first and the last sub-population at the first stage.
    int popuIndex[] = new int[numberOfSubPopulations];
    int lastPopulation = 0;
    openga.operator.selection.twoPopReplacement twoPopReplacement1 = new openga.operator.selection.twoPopReplacement();
    twoPopReplacement1.setThresholdValues(0.8, 0.2);

    for(int i = 0 ; i < numberOfSubPopulations ; i ++ ){
      popuIndex[i] = i;
    }

    for(int m = 0 ; m < DEFAULT_generations ; m ++ ){
      if(m == 9){
        //System.out.println("9");
      }

      for(int i = 0 ; i < popuIndex.length ; i ++ ){
        int index = getActivePop(popuIndex, counter, direction);
        if(m == 0){//initial each population and its objective values.
          GaMain[index].InitialRun();
          if(i == 0){
            GaMain[index].initFirstArchieve();
          }
        }
        //replace the worsen solution of the current population by the
        if((m < 3 || m > 15 && m < 18) && (counter != 0 && direction == true || counter != numberOfSubPopulations - 1 && direction == false)){
          Population[index] = twoPopReplacement1.replacementStage(Population[lastPopulation], Population[index]);
        }
        GaMain[index].startGA();
        lastPopulation = index;
      }
    }

    //to output the implementation result.
    String implementResult = "";
    double refSet[][] = getReferenceSet();
    double objArray[][] = GaMain[0].getArchieveObjectiveValueArray();
    double D1r = calcSolutionQuality(refSet, objArray);
    implementResult = numberOfJob+"\t"+applyClone+"\t"+tournamentSize+"\t"+D1r+"\n";
    objArray = null;
    writeFile("SPGA2_forFlowShop_20060303", implementResult);
    System.out.println(numberOfJob+"\t"+applyClone+"\t"+tournamentSize+"\t"+D1r);

    for(int i = 0 ; i < numberOfSubPopulations ; i ++ ){
      GaMain[i].destroidArchive();
      GaMain[i] = null;
    }
  }

  public static void main(String[] args) {
    System.out.println("SPGA2_forFlowShop_20060303");
    int numberOfSubPopulations[] = new int[]{35};
    int popSize[] = new int[]{210};
    int numberOfJob[] = new int []{60};//20, 40, 60, 80
    int numberOfMachines = 20;
    int totalSolnsToExamine[] = new int[]{100000};//1000000, 1500000, 2000000
    boolean applySecCRX[] = new boolean[]{false, true};//false, true
    boolean applySecMutation[] = new boolean[]{false, true};
    boolean applyClone[] = new boolean[]{false, true};

    double crossoverRate[] = new double[]{0.9},
           mutationRate[] = new double[]{0.38},
           elitism = 0.2;
    int repeatExperiments = 1;

    int counter = 0;

    for(int i = 0 ; i < repeatExperiments ; i ++ ){
      for(int j = 0 ; j < numberOfJob.length ; j ++ ){
        System.out.println("combinations: "+counter++);
        SPGA2_forFlowShopSolutionExchange SPGA2_forFlowShop1 = new SPGA2_forFlowShopSolutionExchange();
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
  }

}