package openga.applications.Continuous;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import openga.chromosomes.*;
import openga.operator.selection.*;
import openga.operator.crossover.*;
import openga.operator.mutation.*;
import openga.ObjectiveFunctions.*;
import openga.MainProgram.*;
import openga.Fitness.*;
import openga.util.fileWrite1;

/**
 * <p>
 * Title: The OpenGA project which is to build general framework of Genetic
 * algorithm.</p>
 * <p>
 * Description: OAS Flowshop Problem.</p>
 * <p>
 * Copyright: Copyright (c) 2018</p>
 * <p>
 * Company: Cheng-Shiu University</p>
 *
 * @author 
 * @version 1.1
 */
public class flowshop_OASPSD implements Runnable{

  public flowshop_OASPSD(){
    
  }
  
 
  /**
   * *
   * Basic variables of GAs.
   */
  int numberOfObjs = 1;
  double b = 0.1;
  populationI Population;
  SelectI Selection;
  CrossoverI Crossover, Crossover2;
  MutationI Mutation, Mutation2;
  ObjFunctionPFSSOAWT_PSDI[] ObjectiveFunction;
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
  CountDownLatch latch;

  /**
   * The method is to modify the default value.
   */
  public void setParameter(double crossoverRate, double mutationRate, int counter, double elitism, int generation,
          int length, String instanceName, int piTotal, int machineTotal, int[] fristProfit, int[] di, double[] wi, int[][] processingTime, double b, CountDownLatch latch) {
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
    this.b = b;
    this.latch = latch;
  }

  public void initiateVars() throws IOException{
    GaMain = new singleThreadGA();//singleThreadGAwithMultipleCrossover singleThreadGA adaptiveGA
    Population = new population();
    Selection = new binaryTournament();
    Crossover = new CyclingCrossoverP(); //twoPointCrossover2()  CyclingCrossoverP multiParentsCrossover()
    Crossover2 = new PMX();
    Mutation = new swapMutation();//shiftMutation
    Mutation2 = new shiftMutation();//inverseMutation
    ObjectiveFunction = new ObjFunctionPFSSOAWT_PSDI[numberOfObjs];
    ObjectiveFunction[0] = new ObjectiveFunctionMakespanFlowShop_OASPSD();//the first objective
    Fitness = new singleObjectiveFitness();//singleObjectiveFitness singleObjectiveFitnessByNormalize
    objectiveMinimization = new boolean[numberOfObjs];
    objectiveMinimization[0] = false;
    encodeType = true;
    
    ObjectiveFunction[0].setOASData(this.piTotal, this.machineTotal, this.fristProfit, this.di, this.wi, this.processingTime, this.b);

    //set the data to the GA main program.
    GaMain.setData(Population, Selection, Crossover, Mutation, ObjectiveFunction,
            Fitness, DEFAULT_generations, DEFAULT_initPopSize, DEFAULT_PopSize,
            length, DEFAULT_crossoverRate, DEFAULT_mutationRate, objectiveMinimization,
            numberOfObjs, encodeType, elitism);
    GaMain.setSecondaryCrossoverOperator(Crossover2, false);
    GaMain.setSecondaryMutationOperator(Mutation2, true);
  }

  public String start(){    
    openga.util.timeClock timeClock1 = new openga.util.timeClock();
    timeClock1.start();
    GaMain.startGA();
    timeClock1.end();

    String implementResult = instanceName + "\t" + DEFAULT_crossoverRate + "\t" + DEFAULT_mutationRate + "\t"
            + elitism + "\t" + GaMain.getArchieve().getSingleChromosome(0).getObjValue()[0]
            + "\t " + timeClock1.getExecutionTime() / 1000.0 + "\n";
    writeFile("flowshop_OASPSD", implementResult);
    System.out.print(implementResult);
    
    return implementResult;
  }
  
  @Override
  public void run() {
    try {
      initiateVars();
      start();
      latch.countDown();//Reduce the current thread count.
    } 
    catch(Exception e) {
      e.printStackTrace();
    }          
  }  

  /**
   * Write the data into text file.
   */
  void writeFile(String fileName, String _result) {
    fileWrite1 writeResult = new fileWrite1();
    writeResult.writeToFile(_result, fileName + ".txt");
    writeResult.run();
//    Thread thread1 = new Thread(writeResult);
//    thread1.run();
  }

  public static void main(String[] args) throws IOException, ParseException {
    int cores = Runtime.getRuntime().availableProcessors();//Math.max(1, Runtime.getRuntime().availableProcessors() - 2)
    ExecutorService executor = Executors.newFixedThreadPool(cores);
    
    String data = "instances/PFSS-OAWT-Data/p/",fileName;
    File f = new File(data);
    String[] fn = f.list();
    
    double crossoverRate[], mutationRate[];
    double b = 0.1;   //0.1
    crossoverRate = new double[]{0.5, 0.9};  
    mutationRate = new double[]{0.1, 0.5};  
    int counter = 1;
    double elitism[] = new double[]{0.1, 0.2};
    int generations[] = new int[]{1000};
    int instanceReplications = 2;    
    
    for (int filelist = 0; filelist < fn.length; filelist++) {
      fileName = fn[filelist];
//      System.out.println(fileName);
//      if(fn[filelist].equals("p50x3_0.txt") || fn[filelist].equals("p30x3_0.txt")){
//        
//      }
//      else{
//        continue;
//      }    
      
      openga.applications.data.readPFSSOAWT_flowshop fs = new openga.applications.data.readPFSSOAWT_flowshop();
      fs.setData(data, fileName);
      fs.readfile();             
      int length = fs.getPiTotal();  
      int innerLoopSize = instanceReplications*crossoverRate.length*mutationRate.length*elitism.length;
      CountDownLatch latch = new CountDownLatch(innerLoopSize);     

      for (int k = 0; k < instanceReplications; k++) {               
        for (int m = 0; m < crossoverRate.length; m++) {
          for (int n = 0; n < mutationRate.length; n++) {
            for (int o = 0; o < elitism.length; o++) {
//                System.out.println(counter);   
                flowshop_OASPSD flowshop1 = new flowshop_OASPSD();
                flowshop1.setParameter(crossoverRate[m], mutationRate[n], counter, elitism[o], generations[0], length, fs.getfileName(),
                      fs.getPiTotal(), fs.getMachineTotal(), fs.getprofit(), fs.getdi(), fs.getwi(), fs.getprocessingTime(), b, latch);
//                flowshop1.initiateVars();
//                flowshop1.start();
                executor.execute(flowshop1);  
                counter++;
            }
          }
        }//end for
      }       
      
      try {
        //Wait the all works are done. Then we process next instance.
        latch.await();
      } catch (InterruptedException E) {
         E.printStackTrace();
      }       
    }
        
    executor.shutdown();          
  }  

}
