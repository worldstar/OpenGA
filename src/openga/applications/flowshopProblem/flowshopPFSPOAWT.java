package openga.applications.flowshopProblem;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Date;
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
  
  
  int piTotal;
  int machineTotal;
  int[] fristProfit;
  int[] di;
  double[] wi;
  int[][] processingTime;
    
  int numberOfObjs = 1;//it's a bi-objective program.
  populationI Population;
  SelectI Selection;
  CrossoverI Crossover, Crossover2;
  MutationI Mutation, Mutation2;
  ObjFunctionPFSSOAWTI[] ObjectiveFunction;
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
                          double elitism, int generation,int length , String instanceName ,  int piTotal , int machineTotal , int[] fristProfit , int[] di , double[] wi , int[][] processingTime){
    this.instance = instance;
   this.DEFAULT_crossoverRate = crossoverRate;
   this.DEFAULT_mutationRate = mutationRate;
   this.counter = counter;
   this.elitism = elitism;
   this.DEFAULT_generations = generation;
   this.instanceName = instanceName;
   this.length = length;
   this.instanceName = instanceName;
    
    
    this.piTotal = piTotal;
    this.machineTotal = machineTotal;
    this.fristProfit = fristProfit;
    this.di = di;
    this.wi = wi;
    this.processingTime = processingTime;
 }

  public void initiateVars() throws IOException{
    GaMain     = new singleThreadGA();//singleThreadGAwithMultipleCrossover singleThreadGA adaptiveGA
    Population = new population();
    Selection  = new binaryTournament();
    Crossover  = new CyclingCrossoverP(); //twoPointCrossover2()  CyclingCrossoverP multiParentsCrossover()
    Crossover2 = new PMX();
    Mutation   = new swapMutation();//shiftMutation
    Mutation2  = new shiftMutation();//inverseMutation
    ObjectiveFunction = new ObjFunctionPFSSOAWTI[numberOfObjs];
    ObjectiveFunction[0] = new ObjFunctionPFSSOAWT();//the first objective
    Fitness    = new singleObjectiveFitness();//singleObjectiveFitness singleObjectiveFitnessByNormalize
    objectiveMinimization = new boolean[numberOfObjs];
    objectiveMinimization[0] = true;
    encodeType = true;

    ObjectiveFunction[0].setOASData(this.piTotal , this.machineTotal , this.fristProfit , this.di , this.wi , this.processingTime);

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

//    String implementResult = instanceName+"\t"+DEFAULT_crossoverRate+"\t"+DEFAULT_mutationRate+"\t"+
//            elitism+"\t"+GaMain.getArchieve().getSingleChromosome(0).getObjValue()[0]
//        +"\t"+timeClock1.getExecutionTime()/1000.0+"\n";
//    writeFile("TSP_SGA1010DOE", implementResult);
//    System.out.print(implementResult);
    
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

  public static void main(String[] args) throws IOException {
    Date date = new Date();
    System.out.println("flowshopPFSPOAWT  "  + date.toString());
    flowshopPFSPOAWT flowshop1 = new flowshopPFSPOAWT();
    double crossoverRate[], mutationRate[];
    crossoverRate = new double[]{1.0};//1, 0.5
    mutationRate  = new double[]{0.5};
    int counter = 0;
    double elitism[] = new double[]{0.2};
    int generations[] = new int[]{1000};
    int numInstances = 0;
    int repeat = 30;

    //to test different kinds of combinations.
      for(int i = 0 ; i <= numInstances ; i ++ ){//numInstances
        
        //set the data to the ObjFunctionPFSSOAWT  program
//        ObjFunctionPFSSOAWT PF = new ObjFunctionPFSSOAWT();
        openga.applications.data.readPFSSOAWT rP = new openga.applications.data.readPFSSOAWT();
        rP.setData("@../../instances/PFSS-OAWT-Data/p/p10x3_0.txt");
        rP.readTxt();
//        PF.setOASData(rP.getPiTotal(), rP.getMachineTotal(), rP.getPi(), rP.getDi(), rP.getWi(), rP.getSetup());
//        PF.calcObjective();
//        PF.outPut();

        String instanceName = "";
        int length = 1;
        
        for(int j = 0 ; j < crossoverRate.length ; j ++ ){
          for(int k = 0 ; k < mutationRate.length ; k ++ ){
            for(int n = 0 ; n < elitism.length ; n ++ ){
                for(int m = 0 ; m < repeat ; m ++ ){
                  System.out.println("�ССССССССССССССССССС�");
                  System.out.println(counter);

                  flowshop1.setParameter(i, crossoverRate[j], mutationRate[k], counter, elitism[n], generations[0], length , instanceName ,
                           rP.getPiTotal(), rP.getMachineTotal(), rP.getPi(), rP.getDi(), rP.getWi(), rP.getSetup());
                  flowshop1.initiateVars();
                  flowshop1.start();
                          
                  counter ++;
                }             
            }
          }
        }
        
      }//end for   
    System.exit(0);
  }
}