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

public class singleMachineOASPSD_SGA extends mTSPSGATwoPart {

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
  ObjectiveFunctionOASPSDI[] ObjectiveFunction;
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
  double PSD_b;

  public singleMachineOASPSD_SGA() {
  }

  public void setParameter(double crossoverRate, double mutationRate, int counter, double elitism,
          int generation, int cities, String instanceName,
          double[] r, double[] p, double[] d, double[] d_bar, double[] e, double[] w, double[] power, double[][] s, int pop_Size,
          double PSD_b) {
    this.DEFAULT_crossoverRate = crossoverRate;
    this.DEFAULT_mutationRate = mutationRate;
    this.counter = counter;
    this.elitism = elitism;
    this.DEFAULT_generations = generation;
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
    this.PSD_b = PSD_b;
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
    ObjectiveFunction = new ObjectiveFunctionOASPSDI[numberOfObjs];
    ObjectiveFunction[0] = new ObjectiveFunctionOASPSD();
    Fitness = new singleObjectiveFitness();//singleObjectiveFitness singleObjectiveFitnessByNormalize
    objectiveMinimization = new boolean[numberOfObjs];
    objectiveMinimization[0] = false;
    encodeType = true;

//    Population.
    Selection.setTournamentSize(2);//Binary tournament
    localSearch1.setNumberofSalesmen(numberOfSalesmen);
    ObjectiveFunction[0].setOASPSDData(r, p, d, d_bar, e, w, power, s, numberOfSalesmen , PSD_b);
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

//    for(int i = 0 ; i < GaMain.getArchieve().getSingleChromosome(0).getLength() ; i++){
//      System.out.print(GaMain.getArchieve().getSingleChromosome(0).genes[i] + ",");
//    }
//    System.out.println();

    String implementResult = instanceName + "\t" + DEFAULT_crossoverRate + "\t" + DEFAULT_mutationRate + "\t" + elitism + "\t" 
            + DEFAULT_PopSize + "\t" + DEFAULT_generations + "\t" + applyLocalSearch + "\t" + alpha + "\t" + PSD_b
            + "\t" + GaMain.getArchieve().getSingleChromosome(0).getObjValue()[0]
            + "\t" + timeClock1.getExecutionTime() / 1000.0 + "\n";
//    writeFile("OASforSMSP_TOU_20171130" + "MaxRevenueFull", implementResult);
    System.out.print(implementResult);
  }

  public static void main(String[] args) {
    System.out.println("OASforSMSP_TOU_20171130" + "MaxRevenueFull");
    int counter = 0;
    boolean applyLocalSearch;
    double[] crossoverRate = new double[]{0.1,0.5,0.9};//1, 0.5 [0.5]
    double[] mutationRate = new double[]{0.1,0.5,0.9};//0.1, 0.5 [0.5]
    double elitism[] = new double[]{0.1};
    int repeat = 30;//30
    double[] alpha = new double[]{0.1};//0.2, 0.1, 0.05 [0.1] //LocalSearch maxNeighborhood
    int[] orders = new int[]{10,15,20,25,50,100};//10, 15, 20, 25, 50, 100
    int[] Tao = new int[]{1,5,9};//1, 3, 5, 7, 9
    int[] R = new int[]{1,5,9};//1, 3, 5, 7, 9
    int instanceReplications = 1;
    double[] PSD_b = new double[]{0.1,0.2,0.3};
    int generations = 1000;//1000
    int populationsSize = 100;
    for (int i = 0; i < orders.length; i++) {
        for (int j = 0; j < Tao.length; j++) {
            for (int k = 0; k < R.length; k++) {
                for (int l = 0; l < instanceReplications; l++) {
                    OASInstancesWithTOU OASInstances1 = new OASInstancesWithTOU();
                    String FileName = "Dataslack_" + orders[i] + "orders_Tao" + Tao[j] + "R" + R[k] + "_" + (l + 1) + ".txt";
                    String instanceName = new String(".\\instances\\SingleMachineOASWithTOU\\" + orders[i] + "orders\\Tao" + Tao[j] + "\\R" + R[k] +
                        "\\" + FileName);
                    OASInstances1.setData(instanceName, orders[i]);
                    OASInstances1.getDataFromFile();

                    for (int m = 0; m < crossoverRate.length; m++) {
                        for (int n = 0; n < mutationRate.length; n++) {
                            for (int o = 0; o < elitism.length; o++) {
                                for (int ls = 0; ls < 1; ls++) {
                                    applyLocalSearch = false;
                                    for (int q = 0; q < alpha.length; q++) {
                                        for (int PSD_bCount = 0; PSD_bCount < PSD_b.length; PSD_bCount++) {
                                            for (int r = 0; r < repeat; r++) {
                                                int _alpha = (int) Math.round(((double) orders[i] * alpha[q]));
                                                singleMachineOASPSD_SGA TSP1 = new singleMachineOASPSD_SGA();
                                                TSP1.alpha = alpha[q];
                                                generations = (orders[i] * 2000) / populationsSize;
                                                TSP1.setParameter(
                                                    crossoverRate[m], mutationRate[n], counter, elitism[o], generations,
                                                    OASInstances1.getSize(), FileName, OASInstances1.getR(), OASInstances1.getP(),
                                                    OASInstances1.getD(), OASInstances1.getD_bar(), OASInstances1.getE(), OASInstances1.getW(),
                                                    OASInstances1.getPower(), OASInstances1.getS(), populationsSize, PSD_b[PSD_bCount]);
                                                TSP1.setLocalSearchData(applyLocalSearch, _alpha);
                                                TSP1.initiateVars();
                                                TSP1.start();
                                                counter++;
                                            }
                                        } //PSD_bCount
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
