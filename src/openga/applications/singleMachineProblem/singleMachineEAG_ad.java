package openga.applications.singleMachineProblem;
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
import openga.operator.clone.*;

/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class singleMachineEAG_ad extends singleMachinewithProbMatrix {
  public singleMachineEAG_ad() {
  }

  /***
   * Parameters of Guided Mutation.
   */
  double lamda; //learning rate
  double beta;
  
 boolean executeAdaptive = true;
  
  public void setEDAinfo(double lamda, double beta){
    this.lamda = lamda;
    this.beta = beta;
  }

  public void setApaptive(boolean executeAdaptive){
      this.executeAdaptive = executeAdaptive;
  }  

  public void initiateVars(){
    GaMain     = new guidedMutationMain();
    Population = new population();
    Selection  = new binaryTournament();
    Crossover  = new twoPointCrossover2();//
    Crossover2 = new PMX();

    Mutation   = new swapMutation();
    //Mutation   = new swapMutationWithMining2();//swapMutationWithMining2 shiftMutationWithMining2
    Mutation2  = new inverseMutation();
    ObjectiveFunction = new ObjectiveFunctionScheduleI[numberOfObjs];
    ObjectiveFunction[0] = new ObjectiveEarlinessTardinessPenalty();//the first objective, tardiness, ObjectiveEarlinessTardinessPenalty
    Fitness    = new singleObjectiveFitness();
    objectiveMinimization = new boolean[numberOfObjs];
    objectiveMinimization[0] = true;
    //objectiveMinimization[1] = true;
    encodeType = true;
    clone1  = new solutionVectorCloneWithMutation();//swap mutation
    //set schedule data to the objectives
    ObjectiveFunction[0].setScheduleData(dueDay, processingTime, numberOfMachines);
    //set the data to the GA main program.
    GaMain.setGuidedMutationInfo(lamda, beta);
    GaMain.setSequenceStrategy(strategy);
    //GaMain.setApaptive(executeAdaptive);
    DEFAULT_generations = totalSolnsToExamine/(DEFAULT_PopSize);
    GaMain.setData(Population, Selection, Crossover, Mutation, ObjectiveFunction, Fitness, DEFAULT_generations, DEFAULT_PopSize, DEFAULT_PopSize,
                   numberOfJob, DEFAULT_crossoverRate, DEFAULT_mutationRate, objectiveMinimization, numberOfObjs, encodeType, elitism);
    GaMain.setSecondaryCrossoverOperator(Crossover2, false);
    GaMain.setSecondaryMutationOperator(Mutation2, false);
  }

  public void start(){
    System.out.println();
    openga.util.timeClock timeClock1 = new openga.util.timeClock();
    timeClock1.start();
    GaMain.startGA();
    timeClock1.end();
    //System.out.println("\nThe final result");
    int bestInd = getBestSolnIndex(GaMain.getArchieve());
    String implementationResult = "";
    implementationResult += fileName+"\t"+DEFAULT_generations+"\t"+beta+"\t"+lamda+"\t" +
        GaMain.getArchieve().getSingleChromosome(bestInd).getObjValue()[0]+"\t"+
        timeClock1.getExecutionTime()/1000.0+"\n";
    writeFile("EAG_125000_0126", implementationResult);
    System.out.println(implementationResult);
  }


  public static void main(String[] args) {
    System.out.println("SingleMachine_EAG_20080323");
    //openga.applications.data.singleMachine singleMachineData = new openga.applications.data.singleMachine();
    int jobSets[] = new int[]{20, 30, 40, 50, 60, 90};//20, 30, 40, 50, 60, 90, 100, 200
    int counter = 0;
    int repeatExperiments = 15;

    
    //Parameters of Guided Mutation.
    double[] lamda = new double[]{0.9}; //learning rate{0.1, 0.5, 0.9} {0.9 is better}
    double[] beta = new double[]{0.5};//Percentage of local information{0.1, 0.5, 0.9} {0.5 is significant}
    boolean executeAdaptive = false;
    //Sourd Instance
    for(int m = 0 ; m < jobSets.length ; m ++ ){//jobSets.length
      for(int k = 0 ; k < 49 ; k ++ ){//49 9
        //if((jobSets[m] <= 50 && (k == 0 || k == 3 || k == 6 || k == 21 || k == 24 || k == 27 || k == 42 || k == 45 || k == 48)) ||  (jobSets[m] > 50 && k < 9)){
        if((jobSets[m] <= 50) || (jobSets[m] > 50 && k < 9)){
          for(int j = 0 ; j < lamda.length ; j ++ ){
            for(int n = 0 ; n < beta.length ; n ++ ){
              System.out.println("Combinations: "+counter);
              openga.applications.data.singleMachine readSingleMachineData1 = new openga.applications.data.singleMachine();
              int numberOfJobs = jobSets[m];
              String fileName = readSingleMachineData1.getFileName(numberOfJobs, k);
              //fileName = "bky"+numberOfJobs+"_1";
              System.out.print(fileName+"\t");
              readSingleMachineData1.setData("sks/"+fileName+".txt");
              readSingleMachineData1.getDataFromFile();
              int dueDate[] = readSingleMachineData1.getDueDate();
              int processingTime[] = readSingleMachineData1.getPtime();

              for(int i = 0 ; i < repeatExperiments ; i ++ ){
                singleMachineEAG singleMachine1 = new singleMachineEAG();
                singleMachine1.setData(numberOfJobs, dueDate, processingTime, fileName);
                singleMachine1.setEDAinfo(lamda[j], beta[n]); 
                //singleMachine1.setApaptive(executeAdaptive);
                singleMachine1.initiateVars();
                singleMachine1.start();
                counter ++;
              }//end for
            }
          }
        }//end if
      }
    }

    System.exit(0);
  }

 
  

}