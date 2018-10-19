package openga.applications.Continuous;

//import java.io.File;
//import java.io.IOException;
//import openga.chromosomes.*;
//import openga.operator.selection.*;
//import openga.operator.crossover.*;
//import openga.operator.mutation.*;
//import openga.ObjectiveFunctions.*;
//import openga.MainProgram.*;
//import openga.Fitness.*;
//import openga.util.printClass;
//import openga.util.fileWrite1;
//import openga.applications.data.readPFSSOAWT_flowshop;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
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
 * <p>
 * Title: The OpenGA project which is to build general framework of Genetic
 * algorithm.</p>
 * <p>
 * Description: Himmelblau is a function which is a continuous problem.</p>
 * <p>
 * Copyright: Copyright (c) 2005</p>
 * <p>
 * Company: Yuan-Ze University</p>
 *
 * @author 
 * @version 1.0
 */
public class flowshop_OAS {

  public flowshop_OAS() {
  }
  /**
   * *
   * Basic variables of GAs.
   */
  int numberOfObjs = 1;
  populationI Population;
  SelectI Selection;
  CrossoverI Crossover, Crossover2;
  MutationI Mutation, Mutation2;
  ObjFunctionPFSSOAWTII[] ObjectiveFunction;
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

//  Instance Data
  int piTotal;
  int machineTotal;
  int[] fristProfit;    //  revenue of order
  int[] di;        //  due-date
  double[] wi;     //  tardiness penalty weight
  int[][] processingTime;

  //Results
  double bestObjectiveValues[];
  populationI solutions;

  public int DEFAULT_generations = 1000,
          DEFAULT_PopSize = 100,
          DEFAULT_initPopSize = 100;

  public double DEFAULT_crossoverRate = 0.9,
          DEFAULT_mutationRate = 0.2,
          elitism = 0.2;     //the percentage of elite chromosomes

  int instance;
  double originalPoint[];
  double coordinates[][];
  double distanceMatrix[][];
  String instanceName = "";
  boolean applyLocalSearch = true;

  /**
   * The method is to modify the default value.
   */
  public void setParameter(double crossoverRate, double mutationRate, int counter, double elitism, int generation,
          int length, String instanceName, int piTotal, int machineTotal, int[] fristProfit, int[] di, double[] wi, int[][] processingTime) {
    this.DEFAULT_crossoverRate = crossoverRate;
    this.DEFAULT_mutationRate = mutationRate;
    this.counter = counter;
    this.elitism = elitism;
    this.DEFAULT_generations = generation;
    this.instanceName = instanceName;
    this.length = length;

    this.piTotal = piTotal;
    this.machineTotal = machineTotal;
    this.fristProfit = fristProfit;
    this.di = di;
    this.wi = wi;
    this.processingTime = processingTime;
  }

  public void initiateVars() throws IOException{
    GaMain = new singleThreadGA();//singleThreadGAwithMultipleCrossover singleThreadGA adaptiveGA
    Population = new population();
    Selection = new binaryTournament();
    Crossover = new CyclingCrossoverP(); //twoPointCrossover2()  CyclingCrossoverP multiParentsCrossover()
    Crossover2 = new PMX();
    Mutation = new swapMutation();//shiftMutation
    Mutation2 = new shiftMutation();//inverseMutation
    ObjectiveFunction = new ObjFunctionPFSSOAWTII[numberOfObjs];
    ObjectiveFunction[0] = new ObjectiveFunctionMakespanFlowShop_OAS();//the first objective
    Fitness = new singleObjectiveFitness();//singleObjectiveFitness singleObjectiveFitnessByNormalize
    objectiveMinimization = new boolean[numberOfObjs];
    objectiveMinimization[0] = false;
    encodeType = true;
    
    ObjectiveFunction[0].setOASData(this.piTotal, this.machineTotal, this.fristProfit, this.di, this.wi, this.processingTime);

    //set the data to the GA main program.
    GaMain.setData(Population, Selection, Crossover, Mutation, ObjectiveFunction,
            Fitness, DEFAULT_generations, DEFAULT_initPopSize, DEFAULT_PopSize,
            length, DEFAULT_crossoverRate, DEFAULT_mutationRate, objectiveMinimization,
            numberOfObjs, encodeType, elitism);
    GaMain.setSecondaryCrossoverOperator(Crossover2, false);
    GaMain.setSecondaryMutationOperator(Mutation2, true);
  }

  public void start() throws ParseException{
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
    String implementResult = instanceName + "\t" + DEFAULT_crossoverRate + "\t" + DEFAULT_mutationRate + "\t"
            + elitism + "\t" + GaMain.getArchieve().getSingleChromosome(0).getObjValue()[0]
            + "\t " + timeClock1.getExecutionTime() / 1000.0 + "\n";
    writeFile("flowshop_", implementResult);
    System.out.print(implementResult);
  }

  /**
   * Write the data into text file.
   */
  void writeFile(String fileName, String _result) {
    fileWrite1 writeLotteryResult = new fileWrite1();
    writeLotteryResult.writeToFile(_result, fileName + ".txt");
    Thread thread1 = new Thread(writeLotteryResult);
    thread1.run();
  }

  public static void main(String[] args) throws IOException, ParseException {
    
    String data = "C:\\Github\\worldstar\\OpenGA\\OpenGA\\instances\\PFSS-OAWT-Data\\p\\",fileName;
    File f = new File(data);
    String[] fn = f.list();
    for (int filelist = 0; filelist < fn.length; filelist++) {
      fileName = fn[filelist];
//      System.out.println(filelist+"."+fileName);
      
      flowshop_OAS flowshop_1 = new flowshop_OAS();
//      int seed[] = new int[10];
      double crossoverRate[], mutationRate[];
      crossoverRate = new double[]{1.0};  //1, 0.8, 0.6
      mutationRate = new double[]{0.5};  //0.05, 0.2, 0.4
      int counter = 0;
      double elitism[] = new double[]{0.2};
      int generations[] = new int[]{1000};
      
//      int[] orders = new int[]{10, 30, 50, 100, 200};
//      int[] numberOfMachines = new int[]{3, 5, 10};
      int instanceReplications = 1;
      
//      for (int i = 0; i < orders.length; i++) {
//        for (int j = 0; j < numberOfMachines.length; j++) {
          for (int k = 0; k < instanceReplications; k++) {
              openga.applications.data.readPFSSOAWT_flowshop fs = new openga.applications.data.readPFSSOAWT_flowshop();
//            String instanceName = new String("C:\\Github\\worldstar\\OpenGA\\OpenGA\\instances\\PFSS-OAWT-Data\\p" + orders[i] + "x" + numberOfMachines[j] + "_" + (k + 1) + ".txt");
//          String instanceName = new String("@../../../instances/PFSS-OAWT-Data/p/" + "p" + orders[i] + "orders\\numberOfMachines" + "x" + numberOfMachines[j] + "x" + "_" + (k + 1) + ".txt");
//            System.out.println(instanceName);
            fs.setData(data, fileName);
            fs.readfile();
            
            int length = fs.getPiTotal();
//          System.out.println("flowshop");
//          String implementResult = "counter\t seed\t crossoverRate\t mutationRate\t X1\t X2\t\t objValue\t time\n";
//          flowshop_1.writeFile("Continuous", implementResult);
            //to test different kinds of combinations.
//            for (int m = 0; m < seed.length; m++) {
            for (int m = 0; m < crossoverRate.length; m++) {
              for (int n = 0; n < mutationRate.length; n++) {
                for (int o = 0; o < elitism.length; o++) {
//                  System.out.println(counter);
                  flowshop_1.setParameter(crossoverRate[m], mutationRate[n], counter, elitism[o], generations[0], length, fs.getfileName(),
                          fs.getPiTotal(), fs.getMachineTotal(), fs.getprofit(), fs.getdi(), fs.getwi(), fs.getprocessingTime());
                  flowshop_1.initiateVars();
                  flowshop_1.start();
                  counter++;
                }
              }
            }//end for
          }
        }
//        System.exit(0);
//      }
//    }
  }
}
