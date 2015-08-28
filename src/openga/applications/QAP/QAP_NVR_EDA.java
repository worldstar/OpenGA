package openga.applications.QAP;
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

/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class QAP_NVR_EDA extends QAP_NVR {
  public QAP_NVR_EDA() {
  }
  EDAMainI GaMain;
  EDAICrossover Crossover;
  EDAIMutation Mutation;
  public String fileName = "";

  /**
   * Parameters of the EDA
   */
  double lamda = 0.9; //learning rate
  int numberOfCrossoverTournament = 2;
  int numberOfMutationTournament = 2;
  int startingGenDividen = 3;

  public int
    DEFAULT_generations = 1000,
    DEFAULT_PopSize     = 200,
    DEFAULT_initPopSize = 200,
    totalSolnsToExamine = 30000;//to fix the total number of solutions to examine. 100000

/**
 * The method is to modify the default value.
 */
public void setParameter(int seed, int DEFAULT_PopSize, double crossoverRate,
                         double mutationRate, int counter,
                         double elitism, int totalSolnsToExamine) {
  this.seed = seed;
  this.DEFAULT_PopSize = DEFAULT_PopSize;
  this.DEFAULT_crossoverRate = crossoverRate;
  this.DEFAULT_mutationRate = mutationRate;
  this.counter = counter;
  this.elitism = elitism;
  this.totalSolnsToExamine = totalSolnsToExamine;
}

  public void setEDAinfo(double lamda, int numberOfCrossoverTournament, int numberOfMutationTournament, int startingGenDividen){
    this.lamda = lamda;
    this.numberOfCrossoverTournament = numberOfCrossoverTournament;
    this.numberOfMutationTournament = numberOfMutationTournament;
    this.startingGenDividen = startingGenDividen;
  }

  public void setData(String fileName){
    this.fileName = fileName;
  }

  public void initiateVars(){
    GaMain     = new singleThreadGAwithEDA();//singleThreadGA singleThreadGAwithSecondFront singleThreadGAwithMultipleCrossover adaptiveGA
    Population = new population();
    Selection  = new binaryTournament();//binaryTournament
    Crossover  = new twoPointCrossover2EDA();//twoPointCrossover2 oneByOneChromosomeCrossover twoPointCrossover2withAdpative twoPointCrossover2withAdpativeThreshold
    Mutation   = new swapMutationEDA();//shiftMutation shiftMutationWithAdaptive shiftMutationWithAdaptiveThreshold
    ObjectiveFunction = new ObjectiveFunctionQAPI[numberOfObjs];
    ObjectiveFunction[0] = new ObjectiveFunctionQAP();//the first objective, Dij*fij
    Fitness    = new singleObjectiveFitness();
    objectiveMinimization = new boolean[numberOfObjs];
    objectiveMinimization[0] = true;
    encodeType = true;

    //initiate scheduling data, we get the data from a program.
    ObjectiveFunction[0].setQAPData(flow, distance);
    DEFAULT_generations = totalSolnsToExamine/DEFAULT_PopSize;

    //set the data to the GA main program.
    GaMain.setData(Population, Selection, Crossover, Mutation, ObjectiveFunction, Fitness, DEFAULT_generations, DEFAULT_PopSize, DEFAULT_PopSize,
                   length, DEFAULT_crossoverRate, DEFAULT_mutationRate, objectiveMinimization, numberOfObjs, encodeType, elitism);
    GaMain.setSecondaryCrossoverOperator(Crossover2, false);
    GaMain.setSecondaryMutationOperator(Mutation2, false);

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
    writeFile("EDA_DOE_QAP20080111", implementResult);
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
    int repeatExperiments = 30;
    int seed[] = new int[repeatExperiments];
    int popSize[] = new int[]{100};//100, 200
    double crossoverRate[] = new double[]{0.9};//0.6, 0.9
    double mutationRate[]  = new double[]{0.5};//0.1, 0.5
    double elitism[] = new double[]{0.2};
    int totalSolnsToExamine = 100000;//100000 is the default one for Zvi Drezner.
    int counter = 0;
    /*
     "nug30.dat", "ste36a.dat", "sko49.dat", "tai60a.dat",
       "sko72.dat", "sko81.dat", "sko90.dat", "wil100.dat"
    */
   String filenames[] = new String[]{"nug30.dat", "ste36a.dat", "sko49.dat", "tai60a.dat",
       "sko72.dat", "sko81.dat", "sko90.dat", "wil100.dat"};

     //EDA parameters.
     double[] lamda = new double[]{0.1, 0.5, 0.9}; //learning rate{0.1, 0.5, 0.9}
     int numberOfCrossoverTournament[] = new int[]{1, 2, 4};//{1, 2, 4}
     int numberOfMutationTournament[] = new int[]{1, 2, 4};//{1, 2, 4}
     int startingGenDividen[] = new int[]{2, 5};//{2, 4}

    for(int i = 0 ; i < seed.length ; i ++ ){
      seed[i] = i+500;
    }

    //to test different kinds of combinations.
    for(int i = 0 ; i < filenames.length ; i ++ ){
      String fileName = "instances\\QAP\\";
      fileName += filenames[i];
      readQAPData readQAPData1 = new readQAPData();
      readQAPData1.setData(fileName);
      readQAPData1.getDataFromFile();
      int size = readQAPData1.getSize();

      for(int j = 0 ; j < lamda.length ; j ++ ){
        for(int k = 0 ; k < numberOfCrossoverTournament.length ; k ++ ){
          for(int m = 0 ; m < numberOfMutationTournament.length ; m ++ ){
            for(int p = 0 ; p < startingGenDividen.length ; p ++ ){
              for(int n = 0 ; n < seed.length ; n ++ ){//replications
                QAP_NVR_EDA QAP_NVR1 = new QAP_NVR_EDA();
                System.out.println(counter);
                //totalSolnsToExamine = Math.max(100000, 100*20*size);
                totalSolnsToExamine = 1000*size;
                QAP_NVR1.setParameter(i, popSize[0], crossoverRate[0], mutationRate[0], counter, elitism[0], totalSolnsToExamine);
                QAP_NVR1.setQAPData(size, readQAPData1.getFlow(), readQAPData1.getDistance());
                QAP_NVR1.setData(fileName);
                QAP_NVR1.setEDAinfo(lamda[j], numberOfCrossoverTournament[k], numberOfMutationTournament[m], startingGenDividen[p]);
                QAP_NVR1.initiateVars();
                QAP_NVR1.start();
                counter ++;
              }//end for
            }
          }
        }
      }
    }

    System.exit(0);
  }

}