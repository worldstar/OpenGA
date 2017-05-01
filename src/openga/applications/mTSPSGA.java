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
 * In order to fit the original framework of the OpenGA, two populations are used.
 * The first population is the sequence of the part I chromosome, and the secondary
 * population is the part II chromosome. So each pair of chromosomes in the two
 * populations are mapped into a single chromosome when we calculate the objective
 * functions. The benefit is when we do the crossover and mutation operator, we
 * can employ the original operators without no or few modifications. As a result,
 * it speeds up the development speed.
 */

/**
 *
 * @author Shih-Hsin Chen
 */
public class mTSPSGA extends TSP {
int numberOfSalesmen = 2;
singleThreadGAforMTSP GaMain;
SelectMTSPI Selection;
ArithmeticFixIntegerSumCrossoverI Crossover2;
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
    GaMain     = new singleThreadGAforMTSP();//singleThreadGAwithMultipleCrossover singleThreadGA adaptiveGA
    Population = new population();
    Selection  = new binaryTournamentMTSP();
    Crossover  = new twoPointCrossover2();
    Crossover2 = new ArithmeticCrossoverFixInteger();
    Mutation   = new swapMutation();//MTSPMutation
    Mutation2  = new MTSPMutation();
    ObjectiveFunction = new ObjectiveFunctionTSPI[numberOfObjs];
    ObjectiveFunction[0] = new ObjectiveFunctionMTSP();//the first objective
    Fitness    = new singleObjectiveFitness();//singleObjectiveFitness singleObjectiveFitnessByNormalize
    objectiveMinimization = new boolean[numberOfObjs];
    objectiveMinimization[0] = true;
    encodeType = true;

    if(numberOfSalesmen >= length){
      System.out.println("The number of salesmen is "+numberOfSalesmen+", which should be greater than the number of visiting locations.");
      System.out.println("The program will exit.");
      System.exit(0);
    }

    //Create the second population for the part II chromosome.
    Population2 = new population();
    Population2.setGenotypeSizeAndLength(encodeType, DEFAULT_PopSize, numberOfSalesmen, numberOfObjs);
    Population2.createNewPop();    
    initiatePartIIChromosomes(Population2, numberOfSalesmen, length);

    localSearch1 = new localSearchBy2OptForMTSP();    
    //localSearch1.setMTSPData(Population, distanceMatrix, numberOfSalesmen);
    
    //To set the part II chromosomes to the objective function object.
    ObjectiveFunction[0].setTSPData(originalPoint, coordinates, Population2);
    ObjectiveFunction[0].setObjectiveFunctionType("TotalDistance");
    Crossover2.setFixedSumNumberData(length);    

    //set the data to the GA main program.
    GaMain.setData(Population, (SelectMTSPI)Selection, Crossover, Mutation, ObjectiveFunction, Fitness, DEFAULT_generations,
            DEFAULT_initPopSize,DEFAULT_PopSize, length , DEFAULT_crossoverRate, DEFAULT_mutationRate,
            objectiveMinimization, numberOfObjs, encodeType, elitism);
    GaMain.SetPartIIChromosomes(Population2);    
    GaMain.setLocalSearchOperator(localSearch1, applyLocalSearch, 20);

    GaMain.setSecondaryCrossoverOperator(Crossover2, true);
    GaMain.setSecondaryMutationOperator(Mutation2, true);
  }

  //initialize the part-II chromosomes here.
  public void initiatePartIIChromosomes(populationI pop, int numberOfSalesmen, int numberOfCities){
    //openga.util.printClass p1 = new openga.util.printClass();

    //Each salesmen are assigned equal number of cities.
    int averageCities = numberOfCities/numberOfSalesmen;

    for(int i = 0 ; i < pop.getPopulationSize() ; i ++){
      for(int j = 0 ; j < numberOfSalesmen ; j ++){
        int cities = (int)(Math.random()*averageCities);
        if(j == numberOfSalesmen - 1){//The last salesman
          pop.setGene(i, j, numberOfCities - (numberOfSalesmen - 1)*averageCities);
        }
        else{
          pop.setGene(i, j, averageCities);
        }
      }
      //To shuffle the number of cities of all salesmen.
      for(int j = 0 ; j < numberOfSalesmen - 1 ; j ++){
        for(int k = 0 ; k < numberOfSalesmen ; k++){
          moveGenes(pop.getSingleChromosome(i), j, k);
        }
      }
      //p1.printMatrix(i+" "+instanceName+"  "+numberOfSalesmen+" "+numberOfCities, pop.getSingleChromosome(i).genes);
    }
    //System.exit(0);
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

    String implementResult = instanceName+"\t"+DEFAULT_crossoverRate+"\t"+DEFAULT_mutationRate+"\t"+numberOfSalesmen+"\t"+GaMain.getArchieve().getSingleChromosome(0).getObjValue()[0]
        +"\t"+timeClock1.getExecutionTime()/1000.0+"\n";
    writeFile("mTSPSGA20121118", implementResult);
    System.out.print(implementResult);
  }

  public static void main(String[] args) {
    System.out.println("mTSPSGA20121118");
    double crossoverRate[], mutationRate[];
    crossoverRate = new double[]{1.0};//1, 0.5
    mutationRate  = new double[]{1.0};
    int counter = 0;
    double elitism[] = new double[]{0.1};
    int generations[] = new int[]{500};
    int numInstances = 1;
    int numberOfSalesmen[] = new int[]{2};//2, 3, 5, 10, 20, 30
    int repeat = 30;

    //to test different kinds of combinations.
      for(int i = 0 ; i < 3 ; i ++ ){//numInstances
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
                  System.out.print("counter "+counter+" ");
                  mTSPSGA TSP1 = new mTSPSGA();

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
