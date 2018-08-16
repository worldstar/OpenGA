package openga.applications.parallelMachineProblem;

import java.io.IOException;
import openga.chromosomes.*;
import openga.operator.selection.*;
import openga.operator.crossover.*;
import openga.operator.mutation.*;
import openga.operator.localSearch.*;
import openga.ObjectiveFunctions.*;
import openga.MainProgram.*;
import openga.Fitness.*;
import openga.applications.data.readParalleMachineOAS;
import openga.applications.singleMachineOAS_SGA;
import openga.util.fileWrite1;

/**
 *
 * @author Administrator
 */
public class parallelMachineSetupOAS extends singleMachineOAS_SGA {

  int maxNeighborhood;  //A default value of the maximum neighbors to search.
  int TournamentSize;
  double alpha;

  singleThreadGA GaMain;
  SelectI Selection;
  CrossoverMTSPI Crossover;
  MutationMTSPI Mutation;
  localSearchMTSPI localSearch1;
  populationI Population;
  ObjectiveFunctionOASI[] ObjectiveFunction;
  String instanceName = "";
  FitnessI Fitness;
  boolean[] objectiveMinimization; //true is minimum problem.
  boolean encodeType;

  boolean applyLocalSearch;
  int numberOfObjs = 1;

  int length;
  double[] r;       //  release date.
  double[] p;       //  processing time
  double[] d;       //  due-date
  double[] d_bar;   //  deadline
  double[] e;       //  revenue
  double[] w;       //  weight
  double[][] s;     //Sequence
  double b;         //Setup indec for PSD

  @Override
  public void setParameter(double crossoverRate, double mutationRate, int counter, double elitism,
          int generation, int type, int numberOfSalesmen, int cities, String instanceName,
          double[] r, double[] p, double[] d, double[] d_bar, double[] e, double[] w, double[][] s, int pop_Size) {
    this.DEFAULT_crossoverRate = crossoverRate;
    this.DEFAULT_mutationRate = mutationRate;
    this.elitism = elitism;
    this.DEFAULT_generations = generation;
    this.type = type;
    this.numberOfSalesmen = numberOfSalesmen;
    this.length = cities;
    this.instanceName = instanceName;
    this.r = r;
    this.p = p;
    this.d = d;
    this.d_bar = d_bar;
    this.e = e;
    this.w = w;
    this.s = s;
    this.DEFAULT_PopSize = pop_Size;
  }
  
  public void setParameter(double crossoverRate, double mutationRate, int counter, double elitism,
          int generation, int type, int numberOfSalesmen, int cities, String instanceName,
          double[] r, double[] p, double[] d, double[] d_bar, double[] e, double[] w, double b, int pop_Size) {
    this.DEFAULT_crossoverRate = crossoverRate;
    this.DEFAULT_mutationRate = mutationRate;
    this.elitism = elitism;
    this.DEFAULT_generations = generation;
    this.type = type;
    this.numberOfSalesmen = numberOfSalesmen;
    this.length = cities;
    this.instanceName = instanceName;
    this.r = r;
    this.p = p;
    this.d = d;
    this.d_bar = d_bar;
    this.e = e;
    this.w = w;
    this.b = b;
    this.DEFAULT_PopSize = pop_Size;
  }  

  @Override
  public void initiateVars() {
    GaMain = new singleThreadGAwithInitialPop();//singleThreadGAwithMultipleCrossover singleThreadGA adaptiveGA
    Population = new population();
    Selection = new varySizeTournament();
    Crossover = new TCSCFCrossover(type);
    Mutation = new swapMutationTwoPart();//TwoPartMTSPMutation
    localSearch1 = new localSearchByIG();
    ObjectiveFunction = new ObjectiveFunctionOASI[numberOfObjs];
    if (numberOfSalesmen > 2) {
      ObjectiveFunction[0] = new TPObjectiveFunctionOASParallelPSD_Setup();//the first objective
    } else {
      ObjectiveFunction[0] = new TPObjectiveFunctionforOAS_Setup();//the first objective
    }
    Fitness = new singleObjectiveFitness();//singleObjectiveFitness singleObjectiveFitnessByNormalize
    objectiveMinimization = new boolean[numberOfObjs];
    objectiveMinimization[0] = false;
    encodeType = true;

    if (numberOfSalesmen >= length) {
      System.out.println("The number of salesmen is " + numberOfSalesmen + ",\nThe cities is " + length + ",\nwhich should be greater than the number of visiting locations.");
      System.out.println("The program will exit.");
      System.exit(0);
    }

    Population.setGenotypeSizeAndLength(encodeType, DEFAULT_PopSize, length + numberOfSalesmen, numberOfObjs);
    Population.createNewPop();

    for (int i = 0; i < DEFAULT_PopSize; i++) {
      Population.getSingleChromosome(i).generateTwoPartPop(length + numberOfSalesmen, numberOfSalesmen);
    }
//    Population.
    Selection.setTournamentSize(2);
    Crossover.setNumberofSalesmen(numberOfSalesmen);
    Mutation.setNumberofSalesmen(numberOfSalesmen);
    localSearch1.setNumberofSalesmen(numberOfSalesmen);

    ObjectiveFunction[0].setOASData(r, p, d, d_bar, e, w, b, numberOfSalesmen);

    //set the data to the GA main program.
    /*Note: the gene length is problem size + numberOfSalesmen*/
    GaMain.setData(Population, Selection, Crossover, Mutation, ObjectiveFunction, Fitness, DEFAULT_generations,
            DEFAULT_initPopSize, DEFAULT_PopSize, length + numberOfSalesmen, DEFAULT_crossoverRate, DEFAULT_mutationRate,
            objectiveMinimization, numberOfObjs, encodeType, elitism);
    GaMain.setLocalSearchOperator(localSearch1, applyLocalSearch, maxNeighborhood);
  }

  @Override
  public void start() {
    openga.util.timeClock timeClock1 = new openga.util.timeClock();
    timeClock1.start();
    GaMain.startGA();
    timeClock1.end();
    if (type == 3) {
      type = 1;
    } else if (type == 2) {
      type = 2;
    } else if (type == 0) {
      type = 3;
    }
    String implementResult = instanceName + "\t" + DEFAULT_crossoverRate + "\t" + DEFAULT_mutationRate + "\t" + type + "\t" + DEFAULT_PopSize + "\t" + numberOfSalesmen + "\t" + alpha + "\t" + b
            + "\t" + GaMain.getArchieve().getSingleChromosome(0).getObjValue()[0]
            + "\t" + timeClock1.getExecutionTime() / 1000.0 + "\n";
    writeFile("parallelMachineSetupOASPSD_20180816_Type12" + "MaxRevenueFull", implementResult);
    System.out.print(implementResult);
    //System.out.print("\n");
    //System.out.print(GaMain.getArchieve().getSingleChromosome(0).toString1());
    //System.out.print("\n");
  }

  public static void main(String[] args) {
    System.out.println("parallelMachineSetupOASPSD_20170720" + "MaxRevenueFull");
    int counter = 0;
    boolean applyLocalSearch = true;
    double[] crossoverRate = new double[]{1, 0.5};//1, 0.5
    double[] mutationRate = new double[]{0.1, 0.5};//0.1, 0.5
    double elitism[] = new double[]{0.1};//3, 5, 10, 20, 30
    int[] crossoverType = new int[]{3, 2};//0: All salesmen reserve the same sites,2: Last salesmen reserve the same sites,3: TCX (Original)
    int repeat = 10;
    int generations[] = new int[]{0};//1000
    int[] populationsSize = new int[]{100, 200};
    double[] alpha = new double[]{0.2, 0.1, 0.05, 0};//0.2, 0.1, 0.05 Parameter of IG algrithm
    int[] numberOfSalesmen = new int[]{4, 6};//3,4,6
    int[] numberOfJobs = new int[]{20, 40, 60, 80};//20, 40, 60, 80, 100, 120
    double b[] = new double[]{0.1, 0.2, 0.3};
    int startInstanceID = 1;//1
    int endStartInstanceID = 1;//15

    for (int j = 0; j < numberOfJobs.length; j++) {
      for (int i = startInstanceID; i <= endStartInstanceID; i++) {
        readParalleMachineOAS RT = new readParalleMachineOAS();
        String instanceName = "instances\\ParallelMachineSetup\\OAS\\"
                + numberOfJobs[j] + "on"
                + "Rp50Rs50_" + i + ".dat";
        RT.setReadTxtData(instanceName);
        try {
          RT.ReadTxt();
          RT.SaveValueOfTxt(RT.TxtLength);
        } catch (IOException ex) {
        }
        
          for (int s = 0; s < numberOfSalesmen.length; s++) {
            for (int cr = 0; cr < crossoverRate.length; cr++) {
              for (int t = 0; t < crossoverType.length; t++) {
                for (int mr = 0; mr < mutationRate.length; mr++) {
                  for (int e = 0; e < elitism.length; e++) {
                    for (int a = 0; a < alpha.length; a++) {
                      for (int pop_Size = 0; pop_Size < populationsSize.length; pop_Size++) {
                        for(int m = 0 ; m < b.length ; m++){
                          for (int r = 0; r < repeat; r++) {
                            int _alpha = (int) Math.round(((double) numberOfJobs[j] * alpha[a]));
                            generations[0] = numberOfJobs[j] * (numberOfSalesmen[s] - 1) * 2000 / populationsSize[pop_Size];
                            parallelMachineSetupOAS OAS1 = new parallelMachineSetupOAS();
                            OAS1.alpha = alpha[a];
                            OAS1.setParameter(crossoverRate[cr], mutationRate[mr], counter, elitism[e], generations[0],
                                    crossoverType[t], numberOfSalesmen[s], RT.getReadTxtSize(), instanceName,
                                    RT.getReleaseDate(), RT.getProcessingTime(), RT.getDueDate(), RT.getDeadline(), RT.getProfit(), RT.getWeight(), b[m], populationsSize[pop_Size]);
                            OAS1.setLocalSearchData(applyLocalSearch, _alpha);
                            OAS1.initiateVars();
                            OAS1.start();
  //                    OAS1.printResults();
                            counter++;
                          }                          
                        }                        
                      }
                    }
                  }
                }
              }
            }
          }
        

      }
    }
    System.exit(0);
  }

  public void printResults() {
    //to output the implementation result.
    String implementResult = "";
    implementResult = "";
    int Clength = length + numberOfSalesmen;
    int cities = length;

    for (int k = 0; k < GaMain.getArchieve().getPopulationSize(); k++) {
      for (int j = 0; j < numberOfObjs; j++) {//for each objectives
        implementResult += GaMain.getArchieve().getObjectiveValues(k)[j] + "\t";
      }
      for (int j = 0; j < Clength; j++) {//for each objectives
        if (j < cities) {
          implementResult += (GaMain.getArchieve().getSingleChromosome(k).genes[j] + 1) + " ";
        } else {
          implementResult += (GaMain.getArchieve().getSingleChromosome(k).genes[j]) + " ";
        }
      }
      implementResult += "\n";
    }
    writeFile("singleMachineArchive_" + Clength, implementResult);
  }

  void writeFile(String fileName, String _result) {
    fileWrite1 writeLotteryResult = new fileWrite1();
    writeLotteryResult.writeToFile(_result, fileName + ".txt");
    Thread thread1 = new Thread(writeLotteryResult);
    thread1.run();
  }
}
