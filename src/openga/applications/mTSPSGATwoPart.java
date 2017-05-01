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
 * The encoding technique uses the Arthur et al. (2006), which applies the two-part
 * chromosome. This program actually uses two populations; the first population
 * records the whole sequence, i.e., the first part of the chromosome.
 * Then the second population stores the result of cities per salesman.
 * The two populations will work together to present the results.
 * Please refer to the following paper of Arthur et al. (2006).
 * Arthur E. Carter, Cliff T. Ragsdale, A new approach to solving the multiple
 traveling salesperson problem using genetic algorithms, European Journal of
 Operational Research, Volume 175, Issue 1, 16 November 2006, Pages 246-257.
 * Then, we apply the new crossover operator. This method implements the new 
two-part encoding crossover by Yuan, S., Skinner, B., Huang, S., & Liu, D. (2013). 
A new crossover approach for solving the multiple travelling salesmen problem 
using genetic algorithms. European Journal of Operational Research, 228(1), 72-82.
 */

/**
 *
 * @author Shih-Hsin Chen
 */
public class mTSPSGATwoPart extends TSP {
int numberOfSalesmen = 3;
int type = 0; //type = 0 : mtsp,  type = 2 : OA,  type = 3 : TCX
singleThreadGA GaMain;
SelectI Selection;
CrossoverMTSPI Crossover;
MutationMTSPI Mutation;
localSearchMTSP localSearch1;
populationI Population2;
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
                          double elitism, int generation,int type, int numberOfSalesmen, 
                          double originalPoint[], double coordinates[][], double distanceMatrix[][], 
                          int cities, String instanceName){
   this.instance = instance;
   this.DEFAULT_crossoverRate = crossoverRate;
   this.DEFAULT_mutationRate = mutationRate;
   this.counter = counter;
   this.elitism = elitism;
   this.DEFAULT_generations = generation;
   this.type = type;
   this.numberOfSalesmen = numberOfSalesmen;
   this.originalPoint = originalPoint;
   this.coordinates = coordinates;
   this.distanceMatrix = distanceMatrix;
   this.length = cities;
   this.instanceName = instanceName;
 }

  public void initiateVars(){   
    GaMain     = new singleThreadGAwithInitialPop();//singleThreadGAwithMultipleCrossover singleThreadGA adaptiveGA
    Population = new population();
    Selection  = new binaryTournament();
    Crossover  = new TCSCFCrossover(type);
    Mutation   = new swapMutationTwoPart();//TwoPartMTSPMutation
    ObjectiveFunction = new ObjectiveFunctionTSPI[numberOfObjs];
    ObjectiveFunction[0] = new TPObjectiveFunctionMTSP();//the first objective
    Fitness    = new singleObjectiveFitness();//singleObjectiveFitness singleObjectiveFitnessByNormalize
    objectiveMinimization = new boolean[numberOfObjs];
    objectiveMinimization[0] = true;
    encodeType = true;
    
    
    
    if(numberOfSalesmen >= length){
      System.out.println("The number of salesmen is "+numberOfSalesmen+", which should be greater than the number of visiting locations.");
      System.out.println("The program will exit.");
      System.exit(0);
    }    
    
    Population.setGenotypeSizeAndLength(encodeType, DEFAULT_PopSize, length + numberOfSalesmen, numberOfObjs);
    Population.createNewPop();
    
    for(int i = 0 ; i < DEFAULT_PopSize ; i ++){
      Population.getSingleChromosome(i).generateTwoPartPop(length + numberOfSalesmen, numberOfSalesmen);
    }
    
    Crossover.setNumberofSalesmen(numberOfSalesmen);
    Mutation.setNumberofSalesmen(numberOfSalesmen);
    
    localSearch1 = new localSearchBy2OptForMTSP();    
    //localSearch1.setMTSPData(Population, distanceMatrix, numberOfSalesmen);
    ObjectiveFunction[0].setTSPData(originalPoint, coordinates, numberOfSalesmen);
    ObjectiveFunction[0].setObjectiveFunctionType("TotalDistance");
    
    //set the data to the GA main program.
    /*Note: the gene length is problem size + numberOfSalesmen*/
    GaMain.setData(Population, Selection, Crossover, Mutation, ObjectiveFunction, Fitness, DEFAULT_generations,
            DEFAULT_initPopSize,DEFAULT_PopSize, length + numberOfSalesmen , DEFAULT_crossoverRate, DEFAULT_mutationRate,
            objectiveMinimization, numberOfObjs, encodeType, elitism);    
    //GaMain.setLocalSearchOperator(localSearch1, applyLocalSearch, 20);
  }

  public chromosome moveGenes(chromosome _chromosome, int cutPoint1, int cutPoint2){
    //Because the original cutPoint1 is always less than cutPoint2, we shuffle the position.
    if(Math.random() < 0.5){
      int tempIndex = cutPoint1;
      cutPoint1 = cutPoint2;
      cutPoint2 = tempIndex;
    }
    //To move the number of cities from cutPoint2 to cutPoint1 and it ensures the
    //number of visiting city of the salesmen at cutPoint2 is at least one city.
    int maxMovedCities = (int)((_chromosome.genes[cutPoint2] - 1)*Math.random()*.5);
    _chromosome.genes[cutPoint1] += maxMovedCities;
    _chromosome.genes[cutPoint2] -= maxMovedCities;
    return _chromosome;
  }

  public void start(){
    openga.util.timeClock timeClock1 = new openga.util.timeClock();
    timeClock1.start();
    GaMain.startGA();
    timeClock1.end();

    String implementResult = instanceName+"\t"+DEFAULT_crossoverRate+"\t"+DEFAULT_mutationRate+"\t"+type+"\t"+numberOfSalesmen+"\t"+GaMain.getArchieve().getSingleChromosome(0).getObjValue()[0]
        +"\t"+timeClock1.getExecutionTime()/1000.0+"\n";
    writeFile("mTSPSGA20160226MaxDistanceFull", implementResult);
    System.out.print(implementResult);
    //System.out.print("\n");
    //System.out.print(GaMain.getArchieve().getSingleChromosome(0).toString1());
    //System.out.print("\n");
  }

  public static void main(String[] args) {
    System.out.println("mTSPSGA20160226MaxDistanceFull");
    double crossoverRate[], mutationRate[];
    crossoverRate = new double[]{0.5};//1, 0.5 [0.5]
    mutationRate  = new double[]{0.1};//0.1, 0.5 [0.1]
    int counter = 0;
    double elitism[] = new double[]{0.1};
    int generations[] = new int[]{1000};//1000
    int numInstances = 33;//33
    int numberOfSalesmen[] = new int[]{3, 5, 10, 20};//3, 5, 10, 20, 30
    int type [] = new int[]{0,2,3};//0: All salesmen reserve the same sites,2: Last salesmen reserve the same sites,3: TCX (Original)
    int repeat = 2;

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
                for(int q = 0 ; q < type.length ; q ++){
                  for(int m = 0 ; m < repeat ; m ++ ){
  //                  System.out.print("counter "+counter+" ");
                    mTSPSGATwoPart TSP1 = new mTSPSGATwoPart();
                    TSP1.setParameter(i, crossoverRate[j], mutationRate[k], counter, elitism[n], generations[0], type[q],
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
        }
      }//end for
    System.exit(0);
  }
}
