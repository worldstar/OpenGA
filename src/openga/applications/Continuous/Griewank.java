package openga.applications.Continuous;
import openga.chromosomes.*;
import openga.operator.selection.*;
import openga.operator.crossover.*;
import openga.operator.mutation.*;
import openga.ObjectiveFunctions.*;
import openga.MainProgram.*;
import openga.Fitness.*;
import openga.util.printClass;
import openga.util.fileWrite1;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: Himmelblau is a function which is a continuous problem.</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class Griewank {
  public Griewank() {
  }
  /***
   * Basic variables of GAs.
   */
  int numberOfObjs = 1;//it's a bi-objective program.
  populationI Population;
  SelectI Selection;
  CrossoverI Crossover, Crossover2;
  RealMoveI Mutation, Mutation2;
  ObjectiveFunctionI ObjectiveFunction[];
  FitnessI Fitness;
  MainI GaMain;


  /**
   * Parameters of the GA
   */
  int generations, length, initPopSize, fixPopSize;
  double crossoverRate, mutationRate;
  boolean[] objectiveMinimization; //true is minimum problem.
  boolean encodeType;  //binary of realize code
  int numberOfDimension = 2;
  double lwBounds[];//lower bound
  double upBound[]; //upper bound
  int seed = 12345;
  int counter = 0;


  //Results
  double bestObjectiveValues[];
  populationI solutions;

  public final static int
    DEFAULT_generations = 500,
    DEFAULT_PopSize     = 300,
    DEFAULT_initPopSize = 500;

  public double
     DEFAULT_crossoverRate = 0.9,
     DEFAULT_mutationRate  = 0.5,
     elitism               =  0.05;     //the percentage of elite chromosomes

  printClass printClass1 = new printClass();

  /**
   * The method is to modify the default value.
   */
  public void setParameter(int seed, double crossoverRate, double mutationRate, int counter){
    this.seed = seed;
    this.DEFAULT_crossoverRate = crossoverRate;
    this.DEFAULT_mutationRate = mutationRate;
    this.counter = counter;
  }
  
  public void initiateVars(){
    lwBounds = new double[]{-600 ,-600 }; //-6 ,-6
    upBound = new double[]{600 ,600 };    //6 ,6 

    GaMain     = new singleThreadGAwithMultipleCrossover();//singleThreadGAwithMultipleCrossover singleThreadGA
    Population = new population();
    Selection  = new binaryTournament();
    Crossover  = new ArithmeticCrossover();
    Crossover2 = new intermediateCrossover();
    Mutation   = new realValueMutation();//shiftMutation
//    Mutation   = new RealCodeSwapMutation_1();
    ObjectiveFunction = new ObjectiveFunctionI[numberOfObjs];
    ObjectiveFunction[0] = new ObjectiveFunctionGriewank();//the first objective, Dij*fij
    Fitness    = new FitnessByNormalize();//singleObjectiveFitness singleObjectiveFitnessByNormalize
    objectiveMinimization = new boolean[numberOfObjs];
    objectiveMinimization[0] = true;  
    encodeType = false;
    length = 2;

    Population.setBounds(lwBounds, upBound);
    Mutation.setBounds(lwBounds, upBound);
    
    //set the data to the GA main program.
    GaMain.setData(Population, Selection, Crossover, Mutation, ObjectiveFunction, Fitness, DEFAULT_generations, DEFAULT_initPopSize,DEFAULT_PopSize,
                   length , DEFAULT_crossoverRate, DEFAULT_mutationRate, objectiveMinimization, numberOfObjs, encodeType, elitism);
    GaMain.setSecondaryCrossoverOperator(Crossover2, true);

  }

  public void start(){
    openga.util.timeClock timeClock1 = new openga.util.timeClock();
    timeClock1.start();
    GaMain.startGA();
    timeClock1.end();
/*
    for(int k = 0 ; k < DEFAULT_PopSize ; k ++ ){
      System.out.print(k+": "+GaMain.getPopulation().getSingleChromosome(k).toString2()+"\t"+GaMain.getPopulation().getSingleChromosome(k).getObjValue()[0]+"\t"+GaMain.getPopulation().getSingleChromosome(k).getFitnessValue()+"\n");
    }

    for(int k = 0 ; k < GaMain.getArchieve().getPopulationSize() ; k ++ ){
      System.out.print(k+": "+GaMain.getArchieve().getSingleChromosome(k).toString2()+"\t"+GaMain.getArchieve().getSingleChromosome(k).getObjValue()[0]+"\t"+GaMain.getArchieve().getSingleChromosome(k).getFitnessValue()+"\n");
    }
*/
    String implementResult = counter+"\t"+seed+"\t"+DEFAULT_crossoverRate+"\t"+DEFAULT_mutationRate+"\t"
        +GaMain.getArchieve().getSingleChromosome(0).toString2()+"\t"
        +GaMain.getArchieve().getSingleChromosome(0).getObjValue()[0]+"\t"+timeClock1.getExecutionTime()+"\n";
    writeFile("Continuous", implementResult);
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
    Griewank Griewank1 = new Griewank();
    System.out.println();
    int seed[] = new int[10];
    double crossoverRate[], mutationRate[];
    crossoverRate = new double[]{1, 0.8, 0.6};
    mutationRate  = new double[]{0.05, 0.2, 0.4};
    int counter = 0;

    for(int i = 0 ; i < seed.length ; i ++ ){
      seed[i] = i+500;
    }

    System.out.println("Start to calc the Griewank function, the following number is the number of combination");
    String implementResult = "counter\t seed\t crossoverRate\t mutationRate\t X1\t X2\t\t objValue\t time\n";
    Griewank1.writeFile("Continuous", implementResult);
    //to test different kinds of combinations.
    for(int i = 0 ; i < seed.length ; i ++ ){
      for(int j = 0 ; j < crossoverRate.length ; j ++ ){
        for(int k = 0 ; k < mutationRate.length ; k ++ ){
          System.out.println(counter);
          Griewank1.setParameter(seed[i], crossoverRate[j], mutationRate[k], counter);
          Griewank1.initiateVars();
          Griewank1.start();
          counter ++;
        }
      }
    }//end for
    System.exit(0);
  }

}