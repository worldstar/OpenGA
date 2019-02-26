package openga.applications.flowshopProblem;
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
import openga.applications.data.*;
import openga.operator.clone.*;
import openga.operator.miningGene.*;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: It implements the Self-Guided GA for the flowshop scheduling
 * problem with minimization of makespan.</p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class flowshopEDA extends flowshopSGA {
  public flowshopEDA() {
  }

  /**
   * Parameters of the GA
   */
  int generations, length, initPopSize, fixPopSize;
  double crossoverRate, mutationRate;
  boolean[] objectiveMinimization; //true is minimum problem.
  boolean encodeType;  //binary of realize code
  public String fileName = "";

  EDAMainI GaMain;
  EDAICrossover Crossover;
  EDAIMutation Mutation;
  EDAModelBuildingI model;
  cloneI clone1;

  /**
   * Parameters of the EDA
   */
  double lamda = 0.9; //learning rate
  int numberOfCrossoverTournament = 2;
  int numberOfMutationTournament = 2;
  int startingGenDividen = 4;

  public void setData(String fileName){
    this.fileName = fileName;
  }

  public void setEDAinfo(double lamda, int numberOfCrossoverTournament, int numberOfMutationTournament, int startingGenDividen){
    this.lamda = lamda;
    this.numberOfCrossoverTournament = numberOfCrossoverTournament;
    this.numberOfMutationTournament = numberOfMutationTournament;
    this.startingGenDividen = startingGenDividen;
  }

  public void initiateVars(){
    GaMain     = new singleThreadGAwithEDA();//singleThreadGA singleThreadGAwithSecondFront singleThreadGAwithMultipleCrossover adaptiveGA
    Population = new population();
    Selection  = new binaryTournament();//binaryTournament
    Crossover  = new twoPointCrossover2EDA();//twoPointCrossover2 oneByOneChromosomeCrossover twoPointCrossover2withAdpative twoPointCrossover2withAdpativeThreshold
    Mutation   = new swapMutationEDA();//shiftMutation shiftMutationWithAdaptive shiftMutationWithAdaptiveThreshold
    ObjectiveFunction = new ObjectiveFunctionFlowShopScheduleI[numberOfObjs];
    ObjectiveFunction[0] = new ObjectiveMakeSpanForFlowShop();//the first objective, ObjectiveMakeSpanForFlowShop ObjectiveEarlinessTardinessForFlowShop
    Fitness    = new singleObjectiveFitness();
    objectiveMinimization = new boolean[numberOfObjs];
    objectiveMinimization[0] = true;
    encodeType = true;

    //set clone, 20090820 add
    clone1  = new solutionVectorCloneWithMutation(); //solutionVectorCloneWithMutation solutionVectorCloneWithMultiparents
    GaMain.setCloneOperatpr(clone1, true); 

    //set schedule data to the objectives
    ObjectiveFunction[0].setScheduleData(processingTime, numberOfMachines);
    DEFAULT_generations = totalSolnsToExamine/(DEFAULT_PopSize);
    //set the data to the GA main program.
    GaMain.setData(Population, Selection, Crossover, Mutation, ObjectiveFunction, Fitness, DEFAULT_generations, DEFAULT_PopSize, DEFAULT_PopSize,
                   numberOfJob, DEFAULT_crossoverRate, DEFAULT_mutationRate, objectiveMinimization, numberOfObjs, encodeType, elitism);
    GaMain.setSecondaryCrossoverOperator(Crossover2, false);
    GaMain.setSecondaryMutationOperator(Mutation2, false);

    model = new PBILwithLapaceCorrection(numberOfJob, lamda);
    GaMain.setEDAinfo(lamda, numberOfCrossoverTournament, numberOfMutationTournament, startingGenDividen);
  }

  public void start(){
    openga.util.timeClock timeClock1 = new openga.util.timeClock();
    timeClock1.start();
    GaMain.startGA();
    timeClock1.end();
    //to output the implementation result.
    String implementResult = "";
    int bestInd = getBestSolnIndex(GaMain.getArchieve());
    implementResult = fileName+"\t" + lamda+"\t"+numberOfCrossoverTournament+"\t"
        + numberOfMutationTournament+"\t"+ startingGenDividen+"\t"
        +GaMain.getArchieve().getSingleChromosome(bestInd).getObjValue()[0]
        +"\t"+timeClock1.getExecutionTime()/1000.0+"\n";
    writeFile("EDA_Flowshop2009820", implementResult);
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
    System.out.println("EDA_Flowshop20090820");
    int repeatExperiments = 30;
    int popSize[] = new int[]{100};//50, 100, 155, 210
    int counter = 0;
    double crossoverRate[] = new double[]{1.0},//0.6, 0.9 {0.9}
           mutationRate [] = new double[]{1.0},//0.1, 0.5 {0.5}
           elitism = 0.1;
    int numberOfInstance = 21;
    int totalSolnsToExamine = 30000;//30000 is the default one for Reeves.

    //EDA parameters.
    double[] lamda = new double[]{0.5}; //learning rate{0.1, 0.5, 0.9} {0.1}
    int numberOfCrossoverTournament[] = new int[]{4};//{1, 2, 4} //2
    int numberOfMutationTournament[] = new int[]{2};//{1, 2, 4}  //2
    int startingGenDividen[] = new int[]{6};//{2, 6}  //6
    /*
    for(int i = 0 ; i < repeatExperiments ; i ++ ){
      for(int j = 20 ; j < numberOfInstance ; j ++ ){
        for(int k = 0 ; k < popSize.length ; k ++ ){
          for(int m = 0 ; m < crossoverRate.length ; m ++ ){
            for(int n = 0 ; n < mutationRate.length ; n ++ ){
              flowshopEDA flowshop1 = new flowshopEDA();
              readFlowShopRevInstance readFlowShopInstance1 = new readFlowShopRevInstance();
              String fileName = "instances\\flowshop\\";
              fileName += readFlowShopInstance1.getFileName(j);
              //fileName += "car8.txt";
              readFlowShopInstance1.setData(fileName);
              readFlowShopInstance1.getDataFromFile();
              System.out.print("Combinations:\t"+(counter++)+"\t");
              flowshop1.setFlowShopData(readFlowShopInstance1.getNumberOfJobs(), readFlowShopInstance1.getNumberOfMachines(), readFlowShopInstance1.getPtime());
              //***** examined solutions are 50*m*n Reeves.
              //totalSolnsToExamine = 50*readFlowShopInstance1.getNumberOfJobs()*readFlowShopInstance1.getNumberOfMachines();
              flowshop1.setParameters(popSize[k], crossoverRate[m], mutationRate[n], totalSolnsToExamine);
              flowshop1.setData(fileName);
              System.out.print(fileName+" ");
              flowshop1.initiateVars();
              flowshop1.start();
            }
          }
        }
      }
    }//end for
    */
   //For Taillard Instance.
   int jobs[] = new int[]{20, 50, 100, 200};//20, 50, 100, 200, 500
   int machines[] = new int[]{5, 10, 20};//5, 10, 20
   //implication of instanceReplication
    int startInstance = 1;
    int endInstance = 1;
    /*
    //for Lian et al. 2006
    String filenames[] = new String[]{"20-5-5.txt","20-5-10.txt", "20-10-10.txt", "20-20-10.txt",
        "50-10-10.txt", "50-20-10.txt", "100-5-10.txt", "100-10-10.txt"};//"20-5-5.txt","20-5-10.txt", "20-10-10.txt", "20-20-10.txt", "50-10-10.txt", "50-20-10.txt", "100-10-10.txt"
    for(int j = 0 ; j < filenames.length ; j ++ ){
      readFlowShopTaillardInstance readFlowShopInstance1 = new readFlowShopTaillardInstance();
      String fileName = "instances\\TaillardFlowshop\\";
      fileName += filenames[j];
      //fileName += "car8.txt";
      readFlowShopInstance1.setData(fileName);
      readFlowShopInstance1.getDataFromFile();
      jobs[0] = readFlowShopInstance1.getNumberOfJobs();
      machines[0] = readFlowShopInstance1.getNumberOfMachines();

      for(int k = 0 ; k < lamda.length ; k ++ ){
        for(int m = 0 ; m < numberOfCrossoverTournament.length ; m ++ ){
          for(int n = 0 ; n < numberOfMutationTournament.length ; n ++ ){
            for(int p = 0 ; p < startingGenDividen.length ; p ++ ){
              for(int i = 0 ; i < repeatExperiments ; i ++ ){
                flowshopEDA flowshop1 = new flowshopEDA();
                System.out.print("Combinations:\t"+(counter++)+"\t");
                flowshop1.setFlowShopData(jobs[0], machines[0], readFlowShopInstance1.getPtime());
                //***** examined solutions are determined by Lian et al.
                totalSolnsToExamine = flowshop1.getTotalSolnsToExamineLian(jobs[0], machines[0]);
                flowshop1.setParameters(popSize[0], crossoverRate[0], mutationRate[0], totalSolnsToExamine);
                flowshop1.setEDAinfo(lamda[k], numberOfCrossoverTournament[m], numberOfMutationTournament[n], startingGenDividen[p]);
                flowshop1.setData(fileName);
                flowshop1.initiateVars();
                flowshop1.start();
              }
            }
          }
        }
      }
    }//end for

    System.exit(0);
    */

    for(int j = 0 ; j < jobs.length ; j ++ ){
      for(int s = 0 ; s < machines.length ; s ++ ){
        for(int q = startInstance ; q <= endInstance ; q ++ ){
          if((jobs[j] <= 100) || (jobs[j] == 200 && machines[s] >= 10) || (jobs[j] == 500 && machines[s] == 20)){
            readFlowShopTaillardInstance readFlowShopInstance1 = new readFlowShopTaillardInstance();
            String fileName = "instances\\TaillardFlowshop\\";
            fileName += readFlowShopInstance1.getFileName(jobs[j], machines[s], q);
            //fileName += "car8.txt";
            readFlowShopInstance1.setData(fileName);
            readFlowShopInstance1.getDataFromFile();
            System.out.println(fileName);

            for(int k = 0 ; k < lamda.length ; k ++ ){
              for(int m = 0 ; m < numberOfCrossoverTournament.length ; m ++ ){
                for(int n = 0 ; n < numberOfMutationTournament.length ; n ++ ){
                  for(int p = 0 ; p < startingGenDividen.length ; p ++ ){
                    for(int i = 0 ; i < repeatExperiments ; i ++ ){
                      //System.out.println("Combinations:\t"+(counter++)+"\t"+fileName);
                      flowshopEDA flowshop1 = new flowshopEDA();
                      flowshop1.setFlowShopData(jobs[j], machines[s], readFlowShopInstance1.getPtime());
                      //***** examined solutions are determined by Taillard.
                      //***** examined solutions are determined by Liang.
                      totalSolnsToExamine = (jobs[j]*2*500);
                      flowshop1.setParameters(popSize[0], crossoverRate[0], mutationRate[0], totalSolnsToExamine);
                      flowshop1.setEDAinfo(lamda[k], numberOfCrossoverTournament[m], numberOfMutationTournament[n], startingGenDividen[p]);
                      flowshop1.setData(fileName);
                      flowshop1.initiateVars();
                      flowshop1.start();
                    }//end for
                  }
                }
              }
            }
          }
        }
      }
    }


  }

}