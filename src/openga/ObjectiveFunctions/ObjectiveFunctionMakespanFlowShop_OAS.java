package openga.ObjectiveFunctions;

import openga.chromosomes.chromosome;
import openga.chromosomes.populationI;
import openga.util.algorithm.*;
import openga.applications.data.readPFSSOAWT_flowshop;

/**
 * <p>
 * Title: The OpenGA project</p>
 * <p>
 * Description: The project is to build general framework of Genetic algorithm
 * and problem independent.</p>
 * <P>
 * For the flow shop scheduling problem.</P>
 * <p>
 * Copyright: Copyright (c) 2004</p>
 * <p>
 * Company: Yuan-Ze University</p>
 *
 */
public class ObjectiveFunctionMakespanFlowShop_OAS extends ObjectiveFunctionTSP implements ObjFunctionPFSSOAWTII {

  public ObjectiveFunctionMakespanFlowShop_OAS() {
  }
  populationI population;// Part-one chromosomes
  chromosome chromosome1 = new chromosome();
  int length, indexOfObjective;

  public int numberofOrder;
  public int numberOfMachines;

  /*variable*/
  public static int piTotal;
  public static int machineTotal;
  public static int[] fristProfit;
  public static int[] di;
  public static double[] wi;
  public static int[][] processingTime;
  public int[] Sequence;

  //
  public String writeFileName;
  public double[] pal;
  public double[] profit;
  public double maximumRevenue;
  public int[][] completeTime;
  public boolean[] accept;
  public int[] machineTime;
  public int processingTimeStart;
  public int wiStart = 4;

  @Override
  public void setScheduleData(int processingTime[][], int indexOfObjective) {
//    length = processingTime.length;
    //machineTime = new int[numberOfMachine];
    this.processingTime = processingTime;
    this.indexOfObjective = indexOfObjective;
  }

  public double evaluateAll(int[] Sequence) {

    this.Sequence = Sequence;

    maximumRevenue = 0.0;

    pal = new double[piTotal];
    profit = new double[piTotal];
    completeTime = new int[Sequence.length][machineTotal];
    accept = new boolean[Sequence.length];
    machineTime = new int[machineTotal];
    processingTimeStart = (wiStart + 1) + 3 * (piTotal - 1);

    for (int i = 0; i < Sequence.length; i++) {
      for (int j = 0; j < machineTotal; j++) {
        if (j > 0) {
          if (machineTime[j] == 0) {
            machineTime[j] += machineTime[j - 1] + processingTime[Sequence[i]][j];
          } else {
            machineTime[j] = Math.max(machineTime[j - 1], machineTime[j]) + processingTime[Sequence[i]][j];
          }
        } else {
          machineTime[j] += processingTime[Sequence[i]][j];
        }
        completeTime[i][j] = machineTime[j];
      }
    
      pal[i] = ((completeTime[i][machineTotal - 1] - di[Sequence[i]]) * wi[Sequence[i]]); 
      if (pal[i] < 0) {
        pal[i] = 0.0;
      }

      if (fristProfit[Sequence[i]] - pal[i] > 0) {
        profit[i] = fristProfit[Sequence[i]] - pal[i];
//        accept[i] = true;
      } else {
        profit[i] = 0.0;
//        accept[i] = false;
        for (int j = 0; j < machineTotal; j++) {
          if (i > 0) {
            completeTime[i][j] = completeTime[i - 1][j];
          } else {
            completeTime[i][j] = 0;
          }
          machineTime[j] = completeTime[i][j];
        }
      }
    }

    for (int i = 0; i < Sequence.length; i++) {
      maximumRevenue += profit[i];
    }
    
    return maximumRevenue;
    
  }

  @Override
  public void setOASData(int piTotal, int machineTotal, int[] fristProfit, int[] di, double[] wi, int[][] processingTime) {
    this.piTotal = piTotal;
    this.machineTotal = machineTotal;
    this.fristProfit = fristProfit;
    this.di = di;
    this.wi = wi;
    this.processingTime = processingTime;
  }

  @Override
  public void setWriteData(String fileo100x10_0txt) {
  }

  @Override
  public void output() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void setScheduleData(int[] dueDay, int[][] processingTime, int numberOfMachine) {
    this.di = dueDay;
    this.processingTime = processingTime;
    this.machineTotal = numberOfMachine;
  }

  @Override
  public void setData(populationI population, int indexOfObjective) {
    this.population = population;
    this.length = population.getLengthOfChromosome();
    this.indexOfObjective = indexOfObjective;
  }

  @Override
  public void setData(chromosome chromosome1, int indexOfObjective) {
    this.chromosome1 = chromosome1;
    this.indexOfObjective = indexOfObjective;
    length = chromosome1.getLength();
  }

  @Override
  public void calcObjective() {
    double obj;
    double objectives[];

    for (int i = 0; i < population.getPopulationSize(); i++) {
      objectives = population.getObjectiveValues(i);
      obj = evaluateAll(population.getSingleChromosome(i).genes);
      objectives[indexOfObjective] = obj;
      population.setObjectiveValue(i, objectives);
    }
  }

  @Override
  public populationI getPopulation() {
    return population;
  }

  @Override
  public double[] getObjectiveValues(int index) {
    return population.getObjectiveValues(index);
  }

  //
  public int getSequence(int index) {
    return Sequence[index];
  }

  public int getSequenceLength() {
    return Sequence.length;
  }

  public int getMachineTotal() {
    return machineTotal;
  }

  public int getPiTotal() {
    return piTotal;
  }

  public boolean[] getAccept() {
    return accept;
  }

  public int getFristProfit(int index) {
    return fristProfit[index];
  }

  public int getDi(int index) {
    return di[index];
  }

  public double getWi(int index) {
    return wi[index];
  }

  public int[][] getCompleteTime() {
    return completeTime;
  }

  public int getProcessingTime(int i, int j) {
    return processingTime[i][j];
  }

  public double getProfit(int index) {
    return profit[index];
  }

  @Override
  public void setOASData(int numberofOrder, int numberOfMachines, int[] profit, int[] di, double[] wi, int[][] processingTime, double b) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

}
