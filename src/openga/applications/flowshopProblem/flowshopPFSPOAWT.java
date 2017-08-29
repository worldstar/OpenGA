package openga.applications.flowshopProblem;
import openga.applications.*;
import openga.chromosomes.*;
import openga.operator.selection.*;
import openga.operator.crossover.*;
import openga.operator.mutation.*;
import openga.ObjectiveFunctions.*;
import openga.MainProgram.*;
import openga.ObjectiveFunctions.*;
import openga.Fitness.*;
//import openga.util.printClass;
import openga.util.fileWrite1;

/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: A TSP problem is solved by Simple Genetic Algorithm.</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class flowshopPFSPOAWT {
  public flowshopPFSPOAWT() {
  }
  /***
   * Basic variables of GAs.
   */
  int numberOfObjs = 1;//it's a bi-objective program.
  populationI Population;
  SelectI Selection;
  CrossoverI Crossover, Crossover2;
  MutationI Mutation, Mutation2;
  DistanceCalculationI[] ObjectiveFunction;
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

  //Results
  double bestObjectiveValues[];
  populationI solutions;

  public int
    DEFAULT_generations = 1000,
    DEFAULT_PopSize     = 100,
    DEFAULT_initPopSize = 100;

  public double
     DEFAULT_crossoverRate = 0.9,
     DEFAULT_mutationRate  = 0.2,
     elitism               =  0.2;     //the percentage of elite chromosomes

  int instance;
  double originalPoint[];
  double coordinates[][];
  double distanceMatrix[][];
  String instanceName = "";
  boolean applyLocalSearch = true;
 /**
  * The method is to modify the default value.
  */
 public void setParameter(int instance, double crossoverRate, double mutationRate, int counter,
                          double elitism, int generation,double originalPoint[],
                          double coordinates[][], double distanceMatrix[][],
                          int length, String instanceName){
   this.instance = instance;
   this.DEFAULT_crossoverRate = crossoverRate;
   this.DEFAULT_mutationRate = mutationRate;
   this.counter = counter;
   this.elitism = elitism;
   this.DEFAULT_generations = generation;
   this.originalPoint = originalPoint;
   this.coordinates = coordinates;
   this.distanceMatrix = distanceMatrix;
   this.length = length;
   this.instanceName = instanceName;
 }

  public void initiateVars(){
    GaMain     = new singleThreadGA();//singleThreadGAwithMultipleCrossover singleThreadGA adaptiveGA
    Population = new population();
    Selection  = new binaryTournament();
    Crossover  = new CyclingCrossoverP(); //twoPointCrossover2()  CyclingCrossoverP multiParentsCrossover()
    Crossover2 = new PMX();
    Mutation   = new swapMutation();//shiftMutation
    Mutation2  = new shiftMutation();//inverseMutation
    ObjectiveFunction = new DistanceCalculationI[numberOfObjs];
    ObjectiveFunction[0] = new DistanceCalculation();//the first objective
    Fitness    = new singleObjectiveFitness();//singleObjectiveFitness singleObjectiveFitnessByNormalize
    objectiveMinimization = new boolean[numberOfObjs];
    objectiveMinimization[0] = true;
    encodeType = true;

    ObjectiveFunction[0].setTSPData(originalPoint, distanceMatrix);

    //set the data to the GA main program.
    GaMain.setData(Population, Selection, Crossover, Mutation, ObjectiveFunction,
                  Fitness, DEFAULT_generations, DEFAULT_initPopSize,DEFAULT_PopSize,
                   length , DEFAULT_crossoverRate, DEFAULT_mutationRate, objectiveMinimization,
                   numberOfObjs, encodeType, elitism);
    GaMain.setSecondaryCrossoverOperator(Crossover2, false);
    GaMain.setSecondaryMutationOperator(Mutation2, true);
  }

  public void start(){
    openga.util.timeClock timeClock1 = new openga.util.timeClock();
    timeClock1.start();
    GaMain.startGA();
    timeClock1.end();

    String implementResult = instanceName+"\t"+DEFAULT_crossoverRate+"\t"+DEFAULT_mutationRate+"\t"+
            elitism+"\t"+GaMain.getArchieve().getSingleChromosome(0).getObjValue()[0]
        +"\t"+timeClock1.getExecutionTime()/1000.0+"\n";
    writeFile("TSP_SGA1010DOE", implementResult);
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
    System.out.print("TSP SGA 1010 DOE");
    flowshopPFSPOAWT TSP1 = new flowshopPFSPOAWT();
    double crossoverRate[], mutationRate[];
    crossoverRate = new double[]{1.0};//1, 0.5
    mutationRate  = new double[]{0.5};
    int counter = 0;
    double elitism[] = new double[]{0.2};
    int generations[] = new int[]{1000};
    int numInstances = 1;
    int repeat = 30;

    //to test different kinds of combinations.
      for(int i = 1 ; i < 2 ; i ++ ){//numInstances
        //initiate scheduling data, we get the data from a program.
        openga.applications.data.TSPInstances TSPInstances1 = new openga.applications.data.TSPInstances();
        TSPInstances1.setData(TSPInstances1.getFileName(i));
        TSPInstances1.getDataFromFile();
        String instanceName = TSPInstances1.getFileName(i);
        TSPInstances1.calcEuclideanDistanceMatrix();
        int length = TSPInstances1.getSize();
        
        for(int j = 0 ; j < crossoverRate.length ; j ++ ){
          for(int k = 0 ; k < mutationRate.length ; k ++ ){
            for(int n = 0 ; n < elitism.length ; n ++ ){
                for(int m = 0 ; m < repeat ; m ++ ){
                  System.out.println(counter);
                  TSP1.setParameter(i, crossoverRate[j], mutationRate[k], counter, elitism[n], generations[0],
                          TSPInstances1.getOriginalPoint(), TSPInstances1.getCoordinates(),
                          TSPInstances1.getDistanceMatrix(), TSPInstances1.getSize(), instanceName);
                  TSP1.initiateVars();
                  TSP1.start();
                  counter ++;
                }             
            }
          }
        }
      }//end for   
    System.exit(0);
  }
}