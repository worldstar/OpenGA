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
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class TSPProbMatrix extends TSP {
  public TSPProbMatrix() {
  }


  public void initiateVars(){
    GaMain     = new singleThreadGAwithProbabilityMatrix();//
    Population = new population();
    Selection  = new binaryTournament();
    Crossover  = new twoPointCrossover2();
    Crossover2 = new PMX();
    Mutation   = new swapMutation();//shiftMutation
    Mutation2  = new shiftMutation();//inverseMutation
    ObjectiveFunction = new ObjectiveFunctionTSPI[numberOfObjs];
    ObjectiveFunction[0] = new ObjectiveFunctionTSP();//the first objective
    Fitness    = new FitnessByNormalize();//singleObjectiveFitness singleObjectiveFitnessByNormalize
    objectiveMinimization = new boolean[numberOfObjs];
    objectiveMinimization[0] = true;
    encodeType = true;

    //initiate scheduling data, we get the data from a program.
    openga.applications.data.TSPInstances TSPInstances1 = new openga.applications.data.TSPInstances();
    TSPInstances1.setData(TSPInstances1.getFileName(instance));
    TSPInstances1.getDataFromFile();
    ObjectiveFunction[0].setTSPData(TSPInstances1.getOriginalPoint(), TSPInstances1.getCoordinates());
    length = TSPInstances1.getSize();
    TSPInstances1 = null;

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

    String implementResult = instance+"\t"+GaMain.getArchieve().getSingleChromosome(0).getObjValue()[0]
        +"\t"+timeClock1.getExecutionTime()+"\n";
    writeFile("TSPAC1010", implementResult);
    System.out.print(implementResult);
  }

  public static void main(String[] args) {
    System.out.println("TSPAC1010");
    double crossoverRate[], mutationRate[];
    crossoverRate = new double[]{0.5};//1, 0.8, 0.6
    mutationRate  = new double[]{0.5};
    int counter = 0;
    double elitism[] = new double[]{0.2};
    int generations[] = new int[]{10000};
    int numInstances = 1;
    int repeat = 10;

    //to test different kinds of combinations.
    for(int m = 0 ; m < repeat ; m ++ ){
      for(int i = 0 ; i < 3 ; i ++ ){//numInstances
        openga.applications.data.TSPInstances TSPInstances1 = new openga.applications.data.TSPInstances();
        TSPInstances1.setData(TSPInstances1.getFileName(i));
        TSPInstances1.getDataFromFile();
        String instanceName = TSPInstances1.getFileName(i);
        TSPInstances1.calcEuclideanDistanceMatrix();
        int length = TSPInstances1.getSize();
        
        for(int j = 0 ; j < elitism.length ; j ++ ){
          for(int k = 0 ; k < generations.length ; k ++ ){
            //System.out.println(counter);
            TSPProbMatrix TSP1 = new TSPProbMatrix();            
            TSP1.setParameter(i, crossoverRate[j], mutationRate[k], counter, elitism[0], 
                    generations[0], TSPInstances1.getOriginalPoint(), TSPInstances1.getCoordinates(), 
                    TSPInstances1.getDistanceMatrix(), TSPInstances1.getSize(), instanceName);                        
            TSP1.initiateVars();
            TSP1.start();
            counter ++;
          }
        }
      }
    }//end for

    System.exit(0);
  }

}