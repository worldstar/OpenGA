package openga.applications;

import openga.chromosomes.*;
import openga.operator.selection.*;
import openga.operator.crossover.*;
import openga.operator.mutation.*;
import openga.operator.localSearch.*;
import openga.ObjectiveFunctions.*;
import openga.MainProgram.*;
import openga.Fitness.*;
import openga.applications.data.OASInstances;

/*
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Cheng Shiu University</p>
 * @authors Chen, Shih-Hsin ; Chang, Yu-Tang
 * @version 1.0
 * We obtain the instance from OASLIB.
 * Reference "A tabu search algorithm for order acceptance and scheduling", http://home.ku.edu.tr/~coguz/Research/Dataset_OAS.zip
 */
public class singleMachineOASPSD_SGA extends singleMachineOAS_SGA {
  double b; //Setup index for PSD.
  
  public void setParameter(double crossoverRate, double mutationRate, int counter, double elitism,
          int generation, int type, int numberOfSalesmen, int cities, String instanceName,
          double[] r, double[] p, double[] d, double[] d_bar, double[] e, double[] w, double b, int pop_Size) {
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
    this.b = b;
    this.DEFAULT_PopSize = pop_Size;
  }  

  public void initiateVars() {
    GaMain = new singleThreadGAwithInitialPop();//singleThreadGAwithMultipleCrossover singleThreadGA adaptiveGA
    Population = new population();
    Selection = new varySizeTournament();
    Crossover = new TCSCFCrossover(type);
    Mutation = new swapMutationTwoPart();//TwoPartMTSPMutation
    localSearch1 = new localSearchByIG();
    ObjectiveFunction = new ObjectiveFunctionOASI[numberOfObjs];
    ObjectiveFunction[0] = new TPObjectiveFunctionforOAS_Setup();
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

  public void start() {
    openga.util.timeClock timeClock1 = new openga.util.timeClock();
    timeClock1.start();
    GaMain.startGA();
    timeClock1.end();
    if (type == 3) {//3: TCX (Original) The TCX
      type = 1;//The best in terms of the Obj and CPU time
    } else if (type == 2) {//2: Last salesmen reserve the same sites
      type = 2;//Actually no difference compared with the TCX
    } else if (type == 0) {//0: All salesmen reserve the same sites
      type = 3;//The worst.
    }
    String implementResult = instanceName + "\t" + DEFAULT_crossoverRate + "\t" + DEFAULT_mutationRate + "\t" + type + "\t" + DEFAULT_PopSize + "\t" + alpha + "\t" + b
            + "\t" + GaMain.getArchieve().getSingleChromosome(0).getObjValue()[0]
            + "\t" + timeClock1.getExecutionTime() / 1000.0 + "\n";
    writeFile("OASforSMSPPSD_20180810_Setup" + "MaxRevenueFull", implementResult);
    System.out.print(implementResult);
  }

  public static void main(String[] args) {
    System.out.println("OASforSMSPPSD-SGALS_20180810" + "MaxRevenueFull");
    int counter = 0;
    boolean applyLocalSearch;
    double[] crossoverRate = new double[]{1,0.5};//0.9, 0.5 [0.5]
    double[] mutationRate = new double[]{0.1,0.5};//0.1, 0.5 [0.5]
    double elitism[] = new double[]{0.1};
    int[] crossoverType = new int[]{3,2};//0: All salesmen reserve the same sites,2: Last salesmen reserve the same sites,3: TCX (Original)
    int repeat = 3;//30
    int generations[] = new int[]{0};//1000
    int[] populationsSize = new int[]{50, 100, 200};//50, 100, 200 [50]
    int[] numberOfSalesmen = new int[]{2};
    double[] alpha = new double[]{0.2, 0.1, 0.05, 0};//0.2, 0.1, 0.05 [0.2]
    int[] orders = new int[]{10, 15, 20, 25, 50, 100};//10, 15, 20, 25, 50, 100
    int[] Tao = new int[]{1, 3, 5, 7, 9};//1, 3, 5, 7, 9
    int[] R = new int[]{1, 3, 5, 7, 9};//1, 3, 5, 7, 9
    double b[] = new double[]{0.1, 0.2, 0.3};//Setup index for PSD.
    int instanceReplications = 1;

    for (int i = 0; i < orders.length; i++) {
      for (int j = 0; j < Tao.length; j++) {
        for (int k = 0; k < R.length; k++) {
          if (Tao[j] != R[k]) {
            continue;
          }
          for (int l = 0; l < instanceReplications; l++) {
            OASInstances OASInstances1 = new OASInstances();
            String instanceName = new String("./instances/SingleMachineOAS/" + orders[i] + "orders/Tao" + Tao[j] + "/R" + R[k]
                    + "/Dataslack_" + orders[i] + "orders_Tao" + Tao[j] + "R" + R[k] + "_" + (l + 1) + ".txt");
            OASInstances1.setData(instanceName, orders[i]);
            OASInstances1.getDataFromFile();

            for (int m = 0; m < crossoverRate.length; m++) {
              for (int t = 0; t < crossoverType.length; t++) {
                for (int n = 0; n < mutationRate.length; n++) {
                  for (int o = 0; o < elitism.length; o++) {
                    for (int p = 0; p < numberOfSalesmen.length; p++) {
                        for (int q = 0; q < alpha.length; q++) {
                          for (int pop_Size = 0; pop_Size < populationsSize.length; pop_Size++) {
                            for(int s = 0 ; s < b.length ; s++){
                              for (int r = 0; r < repeat; r++) {
                                if (alpha[q] != 0) {
                                  applyLocalSearch = true;
                                } else {
                                  applyLocalSearch = false;
                                }                                  

                                int _alpha = (int) Math.round(((double) orders[i] * alpha[q]));
                                generations[0] = orders[i] * (numberOfSalesmen[p] - 1) * 2000 / populationsSize[pop_Size];
                                singleMachineOASPSD_SGA TSP1 = new singleMachineOASPSD_SGA();
                                TSP1.alpha = alpha[q];
                                TSP1.setParameter(crossoverRate[m], mutationRate[n], counter, elitism[o], generations[0],
                                        crossoverType[t], numberOfSalesmen[p], OASInstances1.getSize(), instanceName,
                                        OASInstances1.getR(), OASInstances1.getP(), OASInstances1.getD(), OASInstances1.getD_bar(),
                                        OASInstances1.getE(), OASInstances1.getW(), b[s], populationsSize[pop_Size]);
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
}