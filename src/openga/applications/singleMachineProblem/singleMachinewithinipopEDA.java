package openga.applications.singleMachineProblem;
import openga.chromosomes.*;
import openga.operator.clone.*;
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

/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class singleMachinewithinipopEDA extends singleMachineEDA {
  public singleMachinewithinipopEDA() {
  }
  cloneI clone1;
  EDAMainI GaMain;
  EDAICrossover Crossover;
  EDAIMutation Mutation;

  double lamda = 0.9; //learning rate
  int numberOfCrossoverTournament = 2;
  int numberOfMutationTournament = 2;
  int startingGenDividen = 3;


  public void setEDAinfo(double lamda, int numberOfCrossoverTournament, int numberOfMutationTournament, int startingGenDividen){
    this.lamda = lamda;
    this.numberOfCrossoverTournament = numberOfCrossoverTournament;
    this.numberOfMutationTournament = numberOfMutationTournament;
    this.startingGenDividen = startingGenDividen;
  }


  public void initiateVars(){
    GaMain     = new singleThreadGAwithinipop_EDA(); //singleThreadGA singleThreadGAwithSecondFront singleThreadGAwithMultipleCrossover adaptiveGA
    Population = new population();
    Selection  = new binaryTournament();//binaryTournament
    Crossover  = new twoPointCrossover2EDA();//twoPointCrossover2 oneByOneChromosomeCrossover twoPointCrossover2withAdpative twoPointCrossover2withAdpativeThreshold
    Mutation   = new swapMutationEDA();//shiftMutation shiftMutationWithAdaptive shiftMutationWithAdaptiveThreshold
    ObjectiveFunction = new ObjectiveFunctionScheduleI[numberOfObjs];
    ObjectiveFunction[0] = new ObjectiveEarlinessTardinessPenalty();
    Fitness    = new singleObjectiveFitness();
    objectiveMinimization = new boolean[numberOfObjs];
    objectiveMinimization[0] = true;
    encodeType = true;
    clone1  = new solutionVectorCloneWithMutation();//swap mutation  *ct*
    //set schedule data to the objectives
    ObjectiveFunction[0].setScheduleData(dueDay, processingTime, numberOfMachines);

    DEFAULT_generations = totalSolnsToExamine/(DEFAULT_PopSize);
    //set the data to the GA main program.
    GaMain.setData(Population, Selection, Crossover, Mutation, ObjectiveFunction, Fitness, DEFAULT_generations, DEFAULT_PopSize, DEFAULT_PopSize,
                   numberOfJob, DEFAULT_crossoverRate, DEFAULT_mutationRate, objectiveMinimization, numberOfObjs, encodeType, elitism);
    GaMain.setSecondaryCrossoverOperator(Crossover2, false);
    GaMain.setSecondaryMutationOperator(Mutation2, false);

    GaMain.setEDAinfo(lamda, numberOfCrossoverTournament, numberOfMutationTournament, startingGenDividen);
  }

  public void startMain(){
    openga.util.timeClock timeClock1 = new openga.util.timeClock();
    GaMain.initialStage();
    timeClock1.start();
    GaMain.setCloneOperatpr(clone1, true);
    constructInitialSolutions(Population);
    GaMain.startGA();
    timeClock1.end();
    //to output the implementation result.
    String implementResult = "";
    int bestInd = getBestSolnIndex(GaMain.getArchieve());
    implementResult = fileName+"\t"+ lamda+"\t"+numberOfCrossoverTournament+"\t"
        + numberOfMutationTournament+"\t"+ startingGenDividen+"\t"
        +GaMain.getArchieve().getSingleChromosome(bestInd).getObjValue()[0]
        +"\t"+timeClock1.getExecutionTime()/1000.0+"\n";
    writeFile("singleMachineEDA_SKS_20081019", implementResult);
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

  public void constructInitialSolutions(populationI _Population){
    for(int i = 0 ; i < DEFAULT_PopSize ; i ++ ){
      int sequence[] = new int[numberOfJob];
      for(int j = 0 ; j < numberOfJob ; j ++ ){
        sequence[j] = j;
      }

      homework.schedule.singleMachine singleMachine1 = new homework.schedule.singleMachine();
      singleMachine1.setData(numberOfJob, dueDay, processingTime, sequence);
      DP_Iter = 1;
      singleMachine1.setIterations(DP_Iter);

      //singleMachine1.generateInitialSolution(i);
      singleMachine1.generateRandomSolution();
      singleMachine1.startAlgorithm();
      _Population.getSingleChromosome(i).setSolution(singleMachine1.getSolution());
    }
    clone1.setData(_Population);
    clone1.startToClone();
    _Population = clone1.getPopulation();
  }

  public static void main(String[] args) {
    System.out.println("singleMachineEDA_SKS_20081019");
    //openga.applications.data.singleMachine singleMachineData = new openga.applications.data.singleMachine();
    int jobSets[] = new int[]{20, 30, 40, 50, 60, 90};//20, 30, 40, 50, 60, 90, 100, 200//20, 40, 60, 80
    int counter = 0;
    int repeatExperiments = 30;

    int popSize[] = new int[]{100};//50, 100, 155, 210 [100]
    double crossoverRate[] = new double[]{0.9},//0.6, 0.9 {0.9}
           mutationRate [] = new double[]{1.0},//0.1, 0.5 {0.5}
           elitism = 0.2;

    //EDA parameters.
    double[] lamda = new double[]{0.1}; //learning rate{0.1, 0.5, 0.9}
    int numberOfCrossoverTournament[] = new int[]{2};//{1, 2, 4}
    int numberOfMutationTournament[] = new int[]{2};//{1, 2, 4}
    int startingGenDividen[] = new int[]{10};//{2, 4}{4}

    for(int j = 0 ; j < jobSets.length ; j ++ ){//jobSets.length
      for(int k = 0 ; k < 49 ; k ++ ){
        if((jobSets[j] <= 50 ) ||  (jobSets[j] > 50 && k < 9)){//&& (k == 0 || k == 3 || k == 6 || k == 21 || k == 24 || k == 27 || k == 42 || k == 45 || k == 48)
        //if((jobSets[j] <= 50 && (k != 0 && k != 3 && k != 6 && k != 21 && k != 24 && k != 27 && k != 42 && k != 45 && k != 48)) ||  (jobSets[j] > 50 && k < 9)){
          for(int q = 0 ; q < lamda.length ; q ++ ){
            for (int m = 0; m < numberOfCrossoverTournament.length; m++) {
              for (int n = 0; n < numberOfMutationTournament.length; n++) {
                for(int p = 0 ; p < startingGenDividen.length ; p++){
                    openga.applications.data.singleMachine readSingleMachineData1 = new
                        openga.applications.data.singleMachine();
                    int numberOfJobs = jobSets[j];
                    String fileName = readSingleMachineData1.getFileName(
                        numberOfJobs, k);
                    System.out.print(fileName + "\t");
                    readSingleMachineData1.setData("sks/" + fileName + ".txt");
                    readSingleMachineData1.getDataFromFile();
                    int dueDate[] = readSingleMachineData1.getDueDate();
                    int processingTime[] = readSingleMachineData1.getPtime();

                    for(int i = 0 ; i < repeatExperiments ; i ++ ){
                      System.out.println("Combinations: " + counter);
                      singleMachinewithinipopEDA singleMachine1 = new singleMachinewithinipopEDA(); //ct
                      singleMachine1.setData(numberOfJobs, dueDate, processingTime,
                                             fileName);
                      singleMachine1.setEDAinfo(lamda[q], numberOfCrossoverTournament[m], numberOfMutationTournament[n], startingGenDividen[p]);
                      singleMachine1.initiateVars();
                      singleMachine1.startMain();
                      counter++;
                  }
                }
              }
            }
          }
        }//if for k
      }
    }


    /*
    //Sourd Instance
    for(int i = 0 ; i < repeatExperiments ; i ++ ){
      for(int m = 0 ; m < jobSets.length ; m ++ ){//jobSets.length
        for(int k = 0 ; k < 9 ; k ++ ){
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

          singleMachine1.setData(numberOfJobs, dueDate, processingTime, fileName);
          singleMachine1.initiateVars();
          singleMachine1.startMain();
          counter ++;
        }
      }
    }//end for
    System.exit(0);
    */
    /*
    //BKY Instance
    int incrementSteps = 5;
    for(int i = 0 ; i < repeatExperiments ; i ++ ){
      for(int j = 0 ; j < jobSets.length ; j ++ ){//jobSets.length
        for(int k = 0 ; k < popSize.length ; k ++ ){
          for (int m = 0; m < crossoverRate.length; m++) {
            for (int n = 0; n < mutationRate.length; n++) {
              for(int p = 296 ; p < 297 ; ){
                openga.applications.data.readSingleMachine readSingleMachineData1 = new openga.applications.data.readSingleMachine();
                int numberOfJobs = jobSets[j];
                String fileName = readSingleMachineData1.getFileName(numberOfJobs, p);
                readSingleMachineData1.setData("instances/SingleMachineBKS/"+fileName+".txt");
                if(readSingleMachineData1.testReadData()){//to test whether the file exist
                  System.out.print("Combinations: "+counter+"\t");
                  System.out.print(fileName+"\t");
                  readSingleMachineData1.getDataFromFile();
                  int dueDate[] = readSingleMachineData1.getDueDate();
                  int processingTime[] = readSingleMachineData1.getPtime();

                  singleMachine1.setData(numberOfJobs, dueDate, processingTime, fileName);
                  singleMachine1.setParameters(popSize[k], crossoverRate[m], mutationRate[n], 100000);
                  singleMachine1.initiateVars();
                  singleMachine1.startMain();
                  counter ++;
                }
                p += incrementSteps;
              }
            }
          }
        }
      }
    }//end for
    */
  }


}