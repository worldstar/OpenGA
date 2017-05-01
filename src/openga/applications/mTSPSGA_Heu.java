package openga.applications;
import openga.chromosomes.*;
import openga.operator.selection.*;
import openga.operator.crossover.*;
import openga.operator.mutation.*;
import openga.operator.localSearch.*;
import openga.ObjectiveFunctions.*;
import openga.MainProgram.*;
import openga.ObjectiveFunctions.*;
import openga.Fitness.*;
//import openga.util.printClass;
import openga.util.fileWrite1;
/*
 * This program evaluates the objective of multiple traveling salesmen problems.
 * In addition, we apply a heuristic to assign the cities in a list to the m salesmen.
 */

/**
 *
 * @author Shih-Hsin Chen
 */
public class mTSPSGA_Heu extends TSP {
int numberOfSalesmen = 2;
singleThreadGA GaMain;
SelectI Selection;
localSearchMTSP localSearch1;

String instanceName = "";
boolean applyLocalSearch = true;
double originalPoint[];
double coordinates[][];
double distanceMatrix[][];

    //ObjectiveFunction[0].setTSPData(TSPInstances1.getOriginalPoint(), TSPInstances1.getCoordinates());
 /**
  * The method is to modify the default value.
  */
 public void setParameter(int instance, double crossoverRate, double mutationRate, int counter,
                          double elitism, int generation, int numberOfSalesmen, 
                          double originalPoint[], double coordinates[][], double distanceMatrix[][], 
                          int cities, String instanceName){
   this.instance = instance;
   this.DEFAULT_crossoverRate = crossoverRate;
   this.DEFAULT_mutationRate = mutationRate;
   this.counter = counter;
   this.elitism = elitism;
   this.DEFAULT_generations = generation;
   this.numberOfSalesmen = numberOfSalesmen;
   this.originalPoint = originalPoint;
   this.coordinates = coordinates;
   this.distanceMatrix = distanceMatrix;
   this.length = cities;
   this.instanceName = instanceName;
 }

  public void initiateVars(){   
    GaMain     = new singleThreadGA();//singleThreadGAwithMultipleCrossover singleThreadGA adaptiveGA
    Population = new population();
    Selection  = new binaryTournament();
    Crossover  = new twoPointCrossover2();
    Mutation   = new swapMutation();//MTSPMutation
    ObjectiveFunction = new ObjectiveFunctionTSPI[numberOfObjs];
    ObjectiveFunction[0] = new ObjectiveFunctionMTSPHeu();//the first objective
    Fitness    = new singleObjectiveFitness();//singleObjectiveFitness singleObjectiveFitnessByNormalize
    objectiveMinimization = new boolean[numberOfObjs];
    objectiveMinimization[0] = true;
    encodeType = true;

    if(numberOfSalesmen >= length){
      System.out.println("The number of salesmen is "+numberOfSalesmen+", which should be greater than the number of visiting locations.");
      System.out.println("The program will exit.");
      System.exit(0);
    }

    localSearch1 = new localSearchBy2OptForMTSP();    
    //localSearch1.setMTSPData(Population, distanceMatrix, numberOfSalesmen);
    
    ObjectiveFunction[0].setTSPData(originalPoint, coordinates, numberOfSalesmen);
    ObjectiveFunction[0].setObjectiveFunctionType("TotalDistance");
    
    //set the data to the GA main program.
    GaMain.setData(Population, Selection, Crossover, Mutation, ObjectiveFunction, Fitness, DEFAULT_generations,
            DEFAULT_initPopSize,DEFAULT_PopSize, length , DEFAULT_crossoverRate, DEFAULT_mutationRate,
            objectiveMinimization, numberOfObjs, encodeType, elitism);
    GaMain.setLocalSearchOperator(localSearch1, applyLocalSearch, 20);

    GaMain.setSecondaryCrossoverOperator(Crossover2, true);
    GaMain.setSecondaryMutationOperator(Mutation2, true);    
  }

  public void start(){
    openga.util.timeClock timeClock1 = new openga.util.timeClock();
    timeClock1.start();
    GaMain.startGA();
    timeClock1.end();
    
    String implementResult = instanceName+"\t"+DEFAULT_crossoverRate+"\t"+DEFAULT_mutationRate+"\t"+numberOfSalesmen+"\t"+GaMain.getArchieve().getSingleChromosome(0).getObjValue()[0]
        +"\t"+timeClock1.getExecutionTime()/1000.0+"\t"+GaMain.getArchieve().getSingleChromosome(0).toString1()+"\n";
    writeFile("mTSPSGA20130905Full", implementResult);
    System.out.print(implementResult);
    //System.out.print("\n");
    //System.out.println(GaMain.getArchieve().getSingleChromosome(0).toString1());
    //System.exit(0);
    //System.out.print("\n");
    
  }

  public static void main(String[] args) {
    System.out.println("mTSPSGA20130905Full");
    double crossoverRate[], mutationRate[];
    crossoverRate = new double[]{0.5};//1, 0.5 [0.5]
    mutationRate  = new double[]{0.5};//1, 0.5 [0.5]
    int counter = 0;
    double elitism[] = new double[]{0.1};
    int generations[] = new int[]{1000};//50000
    int numInstances = 0;//33
    int numberOfSalesmen[] = new int[]{2};//2, 3, 5, 10, 20, 30
    int repeat = 10;

    //to test different kinds of combinations.
      for(int i = 0 ; i <= numInstances ; i ++ ){//numInstances
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
              for(int p = 0 ; p < numberOfSalesmen.length ; p ++){
                for(int m = 0 ; m < repeat ; m ++ ){
                  System.out.print("counter "+counter);
                  mTSPSGA_Heu TSP1 = new mTSPSGA_Heu();

                  TSP1.setParameter(i, crossoverRate[j], mutationRate[k], counter, elitism[n], generations[0], 
                          numberOfSalesmen[p], TSPInstances1.getOriginalPoint(), TSPInstances1.getCoordinates(),
                          TSPInstances1.getDistanceMatrix(), TSPInstances1.getSize(), instanceName);
                  TSP1.initiateVars();
                  TSP1.start();
                  counter ++;
                }
              }
            }
          }
        }
      }//end for
    System.exit(0);
  }
}
