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
import openga.applications.data.readQAPData;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: An application of Qudrastic Assignment Problem. We use the Nug30 as testing example.</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class QAP_NVR{
  public QAP_NVR() {
  }
  /***
   * Basic variables of GAs.
   */
  int numberOfObjs = 1;//it's a bi-objective program.
  populationI Population;
  SelectI Selection;
  CrossoverI Crossover, Crossover2;
  MutationI Mutation, Mutation2;
  ObjectiveFunctionQAPI[] ObjectiveFunction;
  FitnessI Fitness;
  MainI GaMain;

  /**
   * Parameters of the GA
   */
  int generations, length, initPopSize, fixPopSize;
  double crossoverRate, mutationRate;
  boolean[] objectiveMinimization; //true is minimum problem.
  boolean encodeType;  //binary of realize code
  int seed = 12345;
  int counter = 0;
  int totalSolnsToExamine = 100000;
  String fileName;

  /***
   * QAP parameters
   */
  int flow[][];
  int distance[][];

  //Results
  double bestObjectiveValues[];
  populationI solutions;

  public int
    DEFAULT_generations = 1000,
    DEFAULT_PopSize     = 300,
    DEFAULT_initPopSize = 500;

  public double
     DEFAULT_crossoverRate = 0.9,
     DEFAULT_mutationRate  = 0.2,
     elitism               =  0.2;     //the percentage of elite chromosomes

 /**
  * The method is to modify the default value.
  */
 public void setParameter(int seed, int DEFAULT_PopSize, double crossoverRate, double mutationRate, int counter,
                          double elitism, int totalSolnsToExamine){
   this.seed = seed;
   this.DEFAULT_PopSize = DEFAULT_PopSize;
   this.DEFAULT_crossoverRate = crossoverRate;
   this.DEFAULT_mutationRate = mutationRate;
   this.counter = counter;
   this.elitism = elitism;
   this.totalSolnsToExamine= totalSolnsToExamine;
 }

 public void setQAPData(int length, int flow[][], int distance[][]){
   this.length = length;
   this.flow = flow;
   this.distance = distance;
 }

 public void setData(String fileName){
   this.fileName = fileName;
 }

  public void initiateVars(){
    GaMain     = new singleThreadGA();//singleThreadGAwithMultipleCrossover singleThreadGA adaptiveGA
    Population = new population();
    Selection  = new binaryTournament();
    Crossover  = new twoPointCrossover2();
    Crossover2 = new PMX();
    Mutation   = new swapMutation();//shiftMutation
    Mutation2  = new shiftMutation();//inverseMutation
    ObjectiveFunction = new ObjectiveFunctionQAPI[numberOfObjs];
    ObjectiveFunction[0] = new ObjectiveFunctionQAP();//the first objective, Dij*fij
    Fitness    = new singleObjectiveFitness();//singleObjectiveFitness singleObjectiveFitnessByNormalize
    objectiveMinimization = new boolean[numberOfObjs];
    objectiveMinimization[0] = true;
    encodeType = true;

    //initiate scheduling data, we get the data from a program.
    ObjectiveFunction[0].setQAPData(flow, distance);
    DEFAULT_generations = totalSolnsToExamine/DEFAULT_PopSize;

    //set the data to the GA main program.
    GaMain.setData(Population, Selection, Crossover, Mutation, ObjectiveFunction, Fitness, DEFAULT_generations, DEFAULT_PopSize,DEFAULT_PopSize,
                   length , DEFAULT_crossoverRate, DEFAULT_mutationRate, objectiveMinimization, numberOfObjs, encodeType, elitism);
    GaMain.setSecondaryCrossoverOperator(Crossover2, false);
    GaMain.setSecondaryMutationOperator(Mutation2, false);
  }

  public void start(){
    openga.util.timeClock timeClock1 = new openga.util.timeClock();
    timeClock1.start();
    GaMain.startGA();
    timeClock1.end();

    String implementResult = fileName+"\t"+ DEFAULT_PopSize+"\t"+DEFAULT_crossoverRate+"\t"+DEFAULT_mutationRate+"\t"
        +GaMain.getArchieve().getSingleChromosome(0).getObjValue()[0]+"\t"+timeClock1.getExecutionTime()/1000.0+"\n";
    writeFile("QAP2_SGA_DOE20080110", implementResult);
    System.out.print(implementResult);
  }

  /**
   * Write the data into text file.
   */
  void writeFile(String fileName, String _result){
    fileWrite1 writeLotteryResult = new fileWrite1();
    writeLotteryResult.writeToFile(_result,fileName+".txt");
    Thread thread1 = new Thread(writeLotteryResult);
    thread1.run();
  }

  public static void main(String[] args) {
    int repeatExperiments = 30;
    int seed[] = new int[repeatExperiments];
    int popSize[] = new int[]{100, 200};//100, 200{100}
    double crossoverRate[] = new double[]{0.6, 0.9};//0.6, 0.9 (0.6)
    double mutationRate[]  = new double[]{0.1, 0.5};//0.1, 0.5 (0.1)
    double elitism[] = new double[]{0.2};
    int totalSolnsToExamine = 100000;//100000 is the default one for Zvi Drezner.
    int counter = 0;
    /*
     "nug30.dat", "ste36a.dat", "sko49.dat", "tai60a.dat",
       "sko72.dat", "sko81.dat", "sko90.dat", "wil100.dat"
    */
   String filenames[] = new String[]{"nug30.dat", "ste36a.dat", "sko49.dat", "tai60a.dat",
       "sko72.dat", "sko81.dat", "sko90.dat", "wil100.dat"};

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

      for(int j = 0 ; j < popSize.length ; j ++ ){
        for(int k = 0 ; k < crossoverRate.length ; k ++ ){
          for(int m = 0 ; m < mutationRate.length ; m ++ ){
            for(int n = 0 ; n < seed.length ; n ++ ){//replications
              QAP_NVR QAP_NVR1 = new QAP_NVR();
              System.out.println(counter);
              //totalSolnsToExamine = Math.max(100000, 100*20*size);
              totalSolnsToExamine = 1000*size;
              QAP_NVR1.setParameter(i, popSize[j], crossoverRate[k], mutationRate[m], counter, elitism[0], totalSolnsToExamine);
              QAP_NVR1.setQAPData(size, readQAPData1.getFlow(), readQAPData1.getDistance());
              QAP_NVR1.setData(fileName);
              QAP_NVR1.initiateVars();
              QAP_NVR1.start();
              counter ++;
            }//end for
          }
        }
      }
    }

    System.exit(0);
  }
}