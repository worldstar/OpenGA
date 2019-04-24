package openga.applications;

import openga.chromosomes.*;
import openga.operator.selection.*;
import openga.operator.crossover.*;
import openga.operator.mutation.*;
import openga.operator.localSearch.*;
import openga.ObjectiveFunctions.*;
import openga.MainProgram.*;
import openga.Fitness.*;
import openga.applications.data.OASInstancesWithTOU;

public class singleMachineOAS_SGAwithTOU extends mTSPSGATwoPart {

  public int numberOfSalesmen;
  public int type; //type = 0 : mtsp,  type = 2 : OA,  type = 3 : TCX
  int maxNeighborhood;  //A default value of the maximum neighbors to search.
  int TournamentSize;
  double alpha;

  singleThreadGA GaMain;
  SelectI Selection;
//  CrossoverMTSPI Crossover;
  CrossoverI Crossover, Crossover2;
//  MutationMTSPI Mutation;
  MutationI Mutation, Mutation2;
  localSearchMTSPI localSearch1;
  populationI Population;
  ObjectiveFunctionOASWithTOUI[] ObjectiveFunction;
  String instanceName = "";

  boolean applyLocalSearch;

  double[] r;       //  release date.
  double[] p;       //  processing time
  double[] d;       //  due-date
  double[] d_bar;   //  deadline
  double[] e;       //  revenue
  double[] w;       //  weight
  double[] power;   //  power
  double[][] s;     //  setup times

  public singleMachineOAS_SGAwithTOU() {
  }

  public void setParameter(double crossoverRate, double mutationRate, int counter, double elitism,
          int generation, int type, int numberOfSalesmen, int cities, String instanceName,
          double[] r, double[] p, double[] d, double[] d_bar, double[] e, double[] w, double[] power, double[][] s, int pop_Size) {
    this.DEFAULT_crossoverRate = crossoverRate;
    this.DEFAULT_mutationRate = mutationRate;
    this.counter = counter;
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
    this.power = power;
    this.s = s;
    this.DEFAULT_PopSize = pop_Size;
  }

  public void setLocalSearchData(boolean applyLocalSearch, int maxNeighborhood) {
    this.applyLocalSearch = applyLocalSearch;
    this.maxNeighborhood = maxNeighborhood;
  }

  @Override
  public void initiateVars() {
    GaMain = new singleThreadGAUnimproveStop();
    Population = new population();
    Selection = new binaryTournamentMaximization();
    Crossover = new PMX();
    Mutation = new swapMutation();
    localSearch1 = new localSearchByIG();
    ObjectiveFunction = new ObjectiveFunctionOASWithTOUI[numberOfObjs];
    ObjectiveFunction[0] = new ObjectiveFunctionforOASWithTOU();
    Fitness = new singleObjectiveFitness();//singleObjectiveFitness singleObjectiveFitnessByNormalize
    objectiveMinimization = new boolean[numberOfObjs];
    objectiveMinimization[0] = false;
    encodeType = true;

//    Population.
    Selection.setTournamentSize(2);//Binary tournament
    localSearch1.setNumberofSalesmen(numberOfSalesmen);
    ObjectiveFunction[0].setOASData(r, p, d, d_bar, e, w, power, s, numberOfSalesmen);
    //set the data to the GA main program.
    /*Note: the gene length is problem size + numberOfSalesmen*/
    GaMain.setData(Population, Selection, Crossover, Mutation, ObjectiveFunction, Fitness, DEFAULT_generations,
            DEFAULT_initPopSize, DEFAULT_PopSize, length, DEFAULT_crossoverRate, DEFAULT_mutationRate,
            objectiveMinimization, numberOfObjs, encodeType, elitism);
    GaMain.setLocalSearchOperator(localSearch1, applyLocalSearch, maxNeighborhood);
  }

  @Override
  public void start() {
    openga.util.timeClock timeClock1 = new openga.util.timeClock();
    timeClock1.start();
    GaMain.startGA();
    timeClock1.end();

    String implementResult = instanceName + "\t" + DEFAULT_crossoverRate + "\t" + DEFAULT_mutationRate + "\t" + DEFAULT_PopSize + "\t" + applyLocalSearch + "\t" + alpha
            + "\t" + GaMain.getArchieve().getSingleChromosome(0).toString1()
            + "\t" + GaMain.getArchieve().getSingleChromosome(0).getObjValue()[0]
            + "\t" + timeClock1.getExecutionTime() / 1000.0 + "\n";
//    writeFile("OASforSMSP_TOU_20171130" + "MaxRevenueFull", implementResult);
    System.out.print(implementResult);
  }

  public static void main(String[] args) {
    System.out.println("OASforSMSP_TOU_20171130" + "MaxRevenueFull");
    int counter = 0;
    boolean applyLocalSearch;
    double[] crossoverRate = new double[]{0.1};//1, 0.5 [0.5]
    double[] mutationRate = new double[]{0.9};//0.1, 0.5 [0.5]
    double elitism[] = new double[]{0.1};
    //0: All salesmen reserve the same sites,2: Last salesmen reserve the same sites,3: TCX (Original)
    int[] crossoverType = new int[]{2};//0, 2, 3 [0] No Obj differences; 3 is a little bit higher. 0 is fatest.
    int repeat = 5;//30
    int generations[] = new int[]{5000};//1000
    int[] populationsSize = new int[]{60};
    int[] numberOfSalesmen = new int[]{2};
    double[] alpha = new double[]{0.1};//0.2, 0.1, 0.05 [0.1] //LocalSearch maxNeighborhood
    int[] orders = new int[]{10};//10, 15, 20, 25, 50, 100
    int[] Tao = new int[]{1};//1, 3, 5, 7, 9
    int[] R = new int[]{1};//1, 3, 5, 7, 9
    int instanceReplications = 1;

    for (int i = 0; i < orders.length; i++) {
      for (int j = 0; j < Tao.length; j++) {
        for (int k = 0; k < R.length; k++) {
          if (Tao[j] != R[k]) {
            continue;
          }
          for (int l = 0; l < instanceReplications; l++) {
            OASInstancesWithTOU OASInstances1 = new OASInstancesWithTOU();
            String instanceName = new String(".\\instances\\SingleMachineOASWithTOU\\" + orders[i] + "orders\\Tao" + Tao[j] + "\\R" + R[k]
                    + "\\Dataslack_" + orders[i] + "orders_Tao" + Tao[j] + "R" + R[k] + "_" + (l + 1) + ".txt");
            OASInstances1.setData(instanceName, orders[i]);
            OASInstances1.getDataFromFile();

            for (int m = 0; m < crossoverRate.length; m++) {
              for (int t = 0; t < crossoverType.length; t++) {
                for (int n = 0; n < mutationRate.length; n++) {
                  for (int o = 0; o < elitism.length; o++) {
                    for (int p = 0; p < numberOfSalesmen.length; p++) {
                      for (int ls = 0; ls < 1; ls++) {
//                        if (ls == 0) {
//                          applyLocalSearch = false;
//                        } else {
                        applyLocalSearch = true;
//                        }
                        for (int q = 0; q < alpha.length; q++) {
                          for (int pop_Size = 0; pop_Size < populationsSize.length; pop_Size++) {
                            for (int r = 0; r < repeat; r++) {
                              int _alpha = (int) Math.round(((double) orders[i] * alpha[q]));
                              singleMachineOAS_SGAwithTOU TSP1 = new singleMachineOAS_SGAwithTOU();
                              TSP1.alpha = alpha[q];
                              TSP1.setParameter(crossoverRate[m], mutationRate[n], counter, elitism[o], generations[0],
                                      crossoverType[t], numberOfSalesmen[p], OASInstances1.getSize(), instanceName,
                                      OASInstances1.getR(), OASInstances1.getP(), OASInstances1.getD(), OASInstances1.getD_bar(), OASInstances1.getE(), OASInstances1.getW(),
                                      OASInstances1.getPower(), OASInstances1.getS(), populationsSize[pop_Size]);
                              TSP1.setLocalSearchData(applyLocalSearch, _alpha);
                              TSP1.initiateVars();
                              TSP1.start();
//                        TSP1.printResults();
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
}
