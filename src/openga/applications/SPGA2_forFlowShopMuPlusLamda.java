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

public class SPGA2_forFlowShopMuPlusLamda extends SPGA2_forFlowShop {
  public SPGA2_forFlowShopMuPlusLamda() {
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
      GaMain[i]     = new SPGAwithSharedParetoSetAndMuPlusLamda();//singleThreadGA fixWeightScalarization
      Population[i] = new population();
      Selection[i]  = new binaryTournament();//binaryTournament similaritySelection2 MuPlusLamdaSelection
      Crossover[i] = new twoPointCrossover2();
      Mutation[i]  = new swapMutation();//oneByOneChromosomeCrossover twoPointCrossover2
      ObjectiveFunction[i] = new ObjectiveFunctionFlowShopScheduleI[numberOfObjs];
      ObjectiveFunction[i][0] = new ObjectiveMakeSpanForFlowShop();//the first objective, makespan
      ObjectiveFunction[i][1] = new ObjectiveTardinessForFlowShop();//the second one.
      Fitness[i]    = new FitnessByScalarizedM_objectives();
      DEFAULT_generations = totalSolnsToExamine/(DEFAULT_PopSize*numberOfSubPopulations);

      //set schedule data to the objectives
      ObjectiveFunction[i][0].setScheduleData(processingTime, numberOfMachines);
      ObjectiveFunction[i][1].setScheduleData(dueDay, processingTime, numberOfMachines);
      //set the data to the GA main program.
      GaMain[i].setData(Population[i], Selection[i], Crossover[i], Mutation[i],
                     ObjectiveFunction[i], Fitness[i], DEFAULT_generations, DEFAULT_PopSize*2, DEFAULT_PopSize,
                     numberOfJob, DEFAULT_crossoverRate, DEFAULT_mutationRate, objectiveMinimization,
                     numberOfObjs, encodeType, elitism);
      //set weight data
      GaMain[i].setWeight(calcWeightsForEachSubPop(i));
      //set secondary crossover and mutation operator.
      //GaMain[i].setSecondaryCrossoverOperator(Crossover2[i], applySecCRX);
      GaMain[i].setSecondaryCrossoverOperator(Crossover2, false);
      //GaMain[i].setSecondaryMutationOperator(Mutation2[i], applySecMutation);
      GaMain[i].setSecondaryMutationOperator(Mutation2, false);
    }
  }

  public void start(){
    //we control the number of iteration of GA here.
    for(int m = 0 ; m < DEFAULT_generations ; m ++ ){
      //System.out.println("Generation: "+m);

      for(int i = 0 ; i < numberOfSubPopulations ; i ++ ){
        //System.out.println("numberOfSubPopulations "+i);
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

    //to output the implementation result.
    String implementResult = "";
    double refSet[][] = getReferenceSet();

    double objArray[][] = GaMain[0].getArchieveObjectiveValueArray();
    implementResult = numberOfJob+"\t"+numberOfSubPopulations+"\t"+ DEFAULT_PopSize +"\t"+ applySecCRX+"\t"
        + applySecMutation+"\t"+calcSolutionQuality(refSet, objArray)+"\n";//
    objArray = null;
    writeFile("SPGA2_forFlowShopMuPlusLamda", implementResult);

    for(int i = 0 ; i < numberOfSubPopulations ; i ++ ){
      GaMain[i].destroidArchive();
      GaMain[i] = null;
    }
  }

  public static void main(String[] args) {
    System.out.println("SPGA2_forFlowShopMuPlusLamda.");

    int numberOfSubPopulations[] = new int[]{35};
    int popSize[] = new int[]{200};
    int numberOfJob[] = new int []{20, 40, 60, 80};
    int numberOfMachines = 20;
    int totalSolnsToExamine[] = new int[]{1000000};//1000000, 1500000, 2000000
    boolean applySecCRX[] = new boolean[]{false, true};//false, true
    boolean applySecMutation[] = new boolean[]{false, true};

    double crossoverRate[] = new double[]{1},
           mutationRate[] = new double[]{0.18},
           elitism = 0.2;
    int repeatExperiments = 5;

    //to form a text file and write the title in it.
    /*
    String implementResult = "numberOfJob\t numberOfSubPopulations\t popSize\t applySecCRX\t applySecMutation\t D1r\n";
    SPGA2_forFlowShop writeFile1 = new SPGA2_forFlowShop();
    writeFile1.writeFile("SPGA2forFlowShop", implementResult);
        */
    int counter = 0;
    for(int i = 0 ; i < repeatExperiments ; i ++ ){
      for(int j = 0 ; j < numberOfJob.length ; j ++ ){
        SPGA2_forFlowShopMuPlusLamda SPGA2_forFlowShop1 = new SPGA2_forFlowShopMuPlusLamda();
        System.out.println("combinations: "+counter++);
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