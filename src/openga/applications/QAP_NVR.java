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
 public void setParameter(int seed, double crossoverRate, double mutationRate, int counter,
                          double elitism, int generation){
   this.seed = seed;
   this.DEFAULT_crossoverRate = crossoverRate;
   this.DEFAULT_mutationRate = mutationRate;
   this.counter = counter;
   this.elitism = elitism;
   this.DEFAULT_generations = generation;
 }

  public void initiateVars(){
    GaMain     = new adaptiveGA();//singleThreadGAwithMultipleCrossover singleThreadGA adaptiveGA
    Population = new population();
    Selection  = new binaryTournament();
    Crossover  = new twoPointCrossover2();
    Crossover2 = new PMX();
    Mutation   = new swapMutation();//shiftMutation
    Mutation2  = new shiftMutation();//inverseMutation
    ObjectiveFunction = new ObjectiveFunctionQAPI[numberOfObjs];
    ObjectiveFunction[0] = new ObjectiveFunctionQAP();//the first objective, Dij*fij
    Fitness    = new FitnessByNormalize();//singleObjectiveFitness singleObjectiveFitnessByNormalize
    objectiveMinimization = new boolean[numberOfObjs];
    objectiveMinimization[0] = true;
    encodeType = true;

    //initiate scheduling data, we get the data from a program.
    openga.applications.data.forQAP forQAP1 = new openga.applications.data.forQAP();
    ObjectiveFunction[0].setQAPData(forQAP1.getNugentData_Flow(), forQAP1.getNugentData_Distance());
    length = forQAP1.getNugentData_Flow().length;
    forQAP1 = null; //release memory of forQAP1

    //set the data to the GA main program.
    GaMain.setData(Population, Selection, Crossover, Mutation, ObjectiveFunction, Fitness, DEFAULT_generations, DEFAULT_initPopSize,DEFAULT_PopSize,
                   length , DEFAULT_crossoverRate, DEFAULT_mutationRate, objectiveMinimization, numberOfObjs, encodeType, elitism);
    GaMain.setSecondaryCrossoverOperator(Crossover2, false);
    GaMain.setSecondaryMutationOperator(Mutation2, true);
  }

  public void start(){
    openga.util.timeClock timeClock1 = new openga.util.timeClock();
    timeClock1.start();
    GaMain.startGA();
    timeClock1.end();

    String implementResult = counter+"\t"+seed+"\t"+DEFAULT_crossoverRate+"\t"+DEFAULT_mutationRate+"\t"
        +GaMain.getArchieve().getSingleChromosome(0).toString1()+"\t"+elitism+"\t"+DEFAULT_generations+"\t"
        +GaMain.getArchieve().getSingleChromosome(0).getObjValue()[0]+"\t"+timeClock1.getExecutionTime()+"\n";
    writeFile("QAP2_withAdaptive", implementResult);
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
    QAP_NVR QAP_NVR1 = new QAP_NVR();
    int seed[] = new int[10];
    double crossoverRate[], mutationRate[];
    crossoverRate = new double[]{0.9};//1, 0.8, 0.6
    mutationRate  = new double[]{0.2};
    int counter = 0;
    double elitism[] = new double[]{0.2};
    int generations[] = new int[]{1000};//400, 700, 1000

    for(int i = 0 ; i < seed.length ; i ++ ){
      seed[i] = i+500;
    }
    //set column title
    String implementResult = "counter\t seed\t crossoverRate\t mutationRate\t QAP_result\t elitism\t generations\t objValue\t time\n";
    QAP_NVR1.writeFile("QAP2_withAdaptive", implementResult);
    System.out.println("New experiment to compare the elitism and generations.\nStart the QAP, the following number is the number of combination");
    //to test different kinds of combinations.
    for(int i = 0 ; i < seed.length ; i ++ ){
      for(int j = 0 ; j < elitism.length ; j ++ ){
        for(int k = 0 ; k < generations.length ; k ++ ){
          System.out.println(counter);
          QAP_NVR1.setParameter(i, 0.6, 0.2, counter, elitism[j], generations[k]);
          QAP_NVR1.initiateVars();
          QAP_NVR1.start();
          counter ++;
        }
      }
    }//end for

    System.exit(0);
  }
}