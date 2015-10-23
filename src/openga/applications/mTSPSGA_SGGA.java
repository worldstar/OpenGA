package openga.applications;
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
import openga.applications.data.*;
import openga.applications.singleMachine;
import openga.operator.localSearch.localSearchBy2OptForMTSP;
import openga.operator.localSearch.localSearchMTSPI;

/**
 * This SGGA solve the objective of multiple traveling salesmen problems.
 * In addition, we apply a heuristic to assign the cities in a list to the m salesmen.
 */

public class mTSPSGA_SGGA extends TSP {
  public mTSPSGA_SGGA() {
  }
  
 int numberOfSalesmen = 2;
 SelectI Selection;
 localSearchMTSPI localSearch1;  
 EDAMainI GaMain;
 EDAICrossover Crossover;
 EDAIMutation Mutation;

 double lamda = 0.9; //learning rate
 int numberOfCrossoverTournament = 2;
 int numberOfMutationTournament = 2;
 int startingGenDividen = 3;

 String instanceName = "";
 boolean applyLocalSearch = true;
 double originalPoint[];
 double coordinates[][];
 double distanceMatrix[][];  
  
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
    ObjectiveFunction = new ObjectiveFunctionTSPI[numberOfObjs];
    ObjectiveFunction[0] = new ObjectiveFunctionMTSPHeu();
    Fitness    = new singleObjectiveFitness();
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
    GaMain.setData(Population, Selection, Crossover, Mutation, ObjectiveFunction, Fitness, DEFAULT_generations, DEFAULT_PopSize, DEFAULT_PopSize,
                   length, DEFAULT_crossoverRate, DEFAULT_mutationRate, objectiveMinimization, numberOfObjs, encodeType, elitism);
    GaMain.setLocalSearchOperator(localSearch1, applyLocalSearch, 20);
    GaMain.setSecondaryCrossoverOperator(Crossover2, false);
    GaMain.setSecondaryMutationOperator(Mutation2, false);

    GaMain.setEDAinfo(lamda, numberOfCrossoverTournament, numberOfMutationTournament, startingGenDividen);
  }

  public void startMain(){
    openga.util.timeClock timeClock1 = new openga.util.timeClock();
    timeClock1.start();
    GaMain.startGA();
    timeClock1.end();
    //to output the implementation result.
    String implementResult = "";
    int bestInd = getBestSolnIndex(GaMain.getArchieve());
    implementResult = instanceName+"\t"+DEFAULT_crossoverRate+"\t"+DEFAULT_mutationRate+"\t"+lamda+"\t"+ (length+1) +"\t"+ numberOfSalesmen
        +"\t"+ startingGenDividen+"\t"+ numberOfCrossoverTournament+"\t"        
        + numberOfMutationTournament+"\t"+ startingGenDividen+"\t"
        +GaMain.getArchieve().getSingleChromosome(bestInd).getObjValue()[0]
        +"\t"+timeClock1.getExecutionTime()/1000.0+"\n";//+"\t"+GaMain.getArchieve().getSingleChromosome(0).toString1() 
    writeFile("mTSPEDA_20150731DOE_totalDistance", implementResult);
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
    System.out.println("mTSPEDA_20150731DOE_totalDistance");
    double crossoverRate[], mutationRate[];
    crossoverRate = new double[]{1, 0.5 };//1, 0.5 
    mutationRate  = new double[]{0.1, 0.5};//1, 0.5 
    int counter = 0;
    double elitism[] = new double[]{0.1};
    int generations[] = new int[]{1000};//50000  
    int numInstances = 33;//33
    int numberOfSalesmen[] = new int[]{3, 5, 10, 20, 30};//2, 3, 5, 10, 20, 30
    int repeat = 5;
    
    //EDA parameters.
    double[] lamda = new double[]{0.1}; //learning rate{0.1, 0.5, 0.9}
    int numberOfCrossoverTournament[] = new int[]{2};//{1, 2, 4}
    int numberOfMutationTournament[] = new int[]{2};//{1, 2, 4}
    int startingGenDividen[] = new int[]{4};//{2, 4}{4}    

    //to test different kinds of combinations.
      for(int i = 0 ; i <= numInstances ; i ++ ){//numInstances
        //initiate scheduling data, we get the data from a program.
        openga.applications.data.TSPInstances TSPInstances1 = new openga.applications.data.TSPInstances();
        String instanceName = TSPInstances1.getFileName(i);//getFileName getCaterFileName
        TSPInstances1.setData(instanceName);
        TSPInstances1.getDataFromFile();
        TSPInstances1.calcEuclideanDistanceMatrix();
        int length = TSPInstances1.getSize();
        
        for(int j = 0 ; j < crossoverRate.length ; j ++ ){
          for(int k = 0 ; k < mutationRate.length ; k ++ ){
            for(int n = 0 ; n < elitism.length ; n ++ ){
              for(int p = 0 ; p < numberOfSalesmen.length ; p ++){
                  
                  for(int q = 0 ; q < lamda.length ; q ++ ){
                      for (int m = 0; m < numberOfCrossoverTournament.length; m++) {
                          for (int r = 0; r < numberOfMutationTournament.length; r++) {
                              for(int s = 0 ; s < startingGenDividen.length ; s++){                               
                                for(int t = 0 ; t < repeat ; t ++ ){
                                  System.out.print("counter "+counter+"\t");
                                  mTSPSGA_SGGA TSP1 = new mTSPSGA_SGGA();

                                  TSP1.setParameter(i, crossoverRate[j], mutationRate[k], counter, elitism[n], generations[0], 
                                          numberOfSalesmen[p], TSPInstances1.getOriginalPoint(), TSPInstances1.getCoordinates(),
                                          TSPInstances1.getDistanceMatrix(), TSPInstances1.getSize(), instanceName);
                                  TSP1.setEDAinfo(lamda[q], numberOfCrossoverTournament[m], numberOfMutationTournament[r], startingGenDividen[s]);
                                  TSP1.initiateVars();
                                  TSP1.startMain();
                                  counter ++;
                                }                                                                    
                              }                              
                          }                          
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