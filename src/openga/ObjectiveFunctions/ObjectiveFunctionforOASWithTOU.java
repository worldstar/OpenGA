package openga.ObjectiveFunctions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import openga.chromosomes.*;

public class ObjectiveFunctionforOASWithTOU extends TPObjectiveFunctionMTSP implements ObjectiveFunctionOASWithTOUI {

  chromosome chromosome1;

//  private int power = 20;
  private double startTime;
  private double endTime;

  private double TOUcostStartTime = 0.0;
  private double totalCost;
  
  int count = 0;

  //Objective Value
  double minimumCost;
  double Revenue;
  double maximumRevenue;
  boolean havePunish;

  //Instance Data
  double[] r;       //  release date.(arrival time)
  double[] p;       //  processing time
  double[] d;       //  due-date
  double[] d_bar;   //  deadline
  double[] e;       //  revenue
  double[] w;       //  weight
  double[] power;       //  power
  double[][] s;     //  setup times
  double[] C;       //completion Time
  int[] data;
  String CI, RI, PowerI, PI, SI, rI, PS;
  boolean obj_Chromosome = false;

  @Override
  public void calcObjective() {

    double obj;
    double objectives[];
//    if (count == 0) {
    for (int i = 0; i < population.getPopulationSize(); i++) {
      objectives = population.getObjectiveValues(i);
      obj = evaluateAll(population.getSingleChromosome(i), numberOfSalesmen);
      objectives[indexOfObjective] = obj;
      population.setObjectiveValue(i, objectives);
      chromosome1.setObjValue(objectives);
      population.setSingleChromosome(i, chromosome1);
    }
    count++;
//    } else {
//      for (int i = 0; i < population.getPopulationSize(); i++) {
//        CI = "";
//        RI = "";
//        PowerI = "";
//        PI = "";
//        SI = "";
//        rI = "";
//        PS = "";
//        data = new int[]{5, 7, 6, 3, 2, 9, 8, 4, 1, 0};
//        chromosome1.setSolution(data);
//        population.setSingleChromosome(i, chromosome1);
//        obj_Chromosome = true;
//        objectives = population.getObjectiveValues(i);
//        obj = evaluateAll(population.getSingleChromosome(i), numberOfSalesmen);
//        objectives[indexOfObjective] = obj;
//        population.setObjectiveValue(i, objectives);
//        chromosome1.setObjValue(objectives);
//        population.setSingleChromosome(i, chromosome1);
////        System.out.println(population.getSingleChromosome(i).toString1() + "\t" + population.getSingleChromosome(i).getObjValue()[0]);
//      }
//    }

  }

  public List<Integer> chromosometoList(chromosome _chromosome1) {
    List<Integer> soln = new ArrayList<Integer>();
    for (int i = 0; i < _chromosome1.genes.length; i++) {
      soln.add(_chromosome1.genes[i]);
    }
    return soln;
  }

  @Override
  public double evaluateAll(chromosome _chromosome1, int numberOfSalesmen) {
    this.setData(_chromosome1, numberOfSalesmen);
    this.calcMaximumRevenue();
//    double obj_Chromosome_EC = this.evaluateAllwithTOUcost(_chromosome1.genes);
//    if (obj_Chromosome == true) {
//      System.out.println("EC(i):\t"+PowerI + " " + obj_Chromosome_EC + "\n" + (this.getMaximumRevenue() - obj_Chromosome_EC));
//      System.exit(1);
//    }
    return this.getMaximumRevenue() - calculateTOUMachineCost();
  }

  public void calcMaximumRevenue() {
    Revenue = 0;
    maximumRevenue = 0;
    int index, lastindex = 0;
    double time = 0;
    List<Integer> _chromosome1 = new ArrayList<>();
    List<Integer> accept = new ArrayList<>();
    List<Integer> reject = new ArrayList<>();
    List<Integer> salesmen = new ArrayList<>();
    _chromosome1.addAll(chromosometoList(chromosome1));

    for (int j = 0; j < length; j++) {
      index = chromosome1.genes[j];
      if (time <= r[index]) {
        time = r[index];
//          rI += (int) r[index] + "\t";
      }
      if (j == 0) {
        time += s[0][index];
//          SI += (int) s[0][index] + "\t";
//          PS += (int) s[0][index]+(int)p[index]+"\t";
      } else {
        time += s[lastindex + 1][index];
//          SI += (int) s[lastindex + 1][index] + "\t";
//          PS += (int) s[lastindex + 1][index]+(int)p[index]+"\t";
      }
      time += p[index];
//        PI += (int) p[index] + "\t";

      C[index] = time;
//        if (count != 0) {
//          CI += (int) C[index] + "\t";
//          count++;
//        }

      if (C[index] <= d[index]) {
        Revenue = e[index];
      } else if (C[index] > d[index] && C[index] <= d_bar[index]) {
        Revenue = e[index] - (C[index] - d[index]) * w[index];
      } else {
        Revenue = 0;
      }

      maximumRevenue += Revenue;

//        if (count != 0) {
//          RI += (int) Revenue + "\t";
//          if (count == 10) {
//            System.out.println("r(i):\t" + rI + "\n" + "S(ji):\t" + SI + "\n" + "P(i):\t" + PI + "\nPi+sji\t" + PS);
//            System.out.println("C(i):\t" + CI + "\n" + "e(i):\t" + RI + "\t" + maximumRevenue);
////            System.exit(1);
//          }
//        }
      if (Revenue == 0) {
        reject.add(_chromosome1.get(j));
      } else {
        accept.add(_chromosome1.get(j));
      }
      lastindex = index;
    }
    length = accept.size();
    _chromosome1.clear();
    _chromosome1.addAll(accept);
    _chromosome1.addAll(reject);
    chromosome1.setSolution(_chromosome1);
  }

  public double calculateTOUMachineCost() {
    totalCost = 0.0;
    int acceptedOrders = length;
    int index, lastindex = 0;
    for (int i = 0; i < acceptedOrders; i++) {
      index = chromosome1.genes[i];
      if (i == 0) {
        startTime = TOUcostStartTime + r[index];
        endTime = TOUcostStartTime + r[index] + p[index] + s[0][index];
//          System.out.println(index+"\t"+startTime + "\t" + endTime + "\t" + p[index] + "\t" + s[0][index] + "\t" + (endTime - startTime) + "\t" + power[index]);
      } else {
        startTime = Math.max(endTime, r[i]);
        endTime = startTime + p[index] + s[lastindex + 1][index];
//          System.out.println(index+"\t"+startTime + "\t" + endTime + "\t" + p[index] + "\t" + s[lastindex + 1][index] + "\t" + (endTime - startTime) + "\t" + power[index]);
      }
      totalCost += calculateTOUCost(index);
      lastindex = index;
    }
    return totalCost;
  }

  public double calculateTOUCost(int inedex) {
    double[] intervalEndTime = new double[]{0, 420, 480, 780, 900, 1440};
    double[] EC = new double[]{0, 0.0422, 0.0750, 0.1327, 0.0750, 0.0422};
    double totalPrice = 0.0;
    for (int k = 1; k < intervalEndTime.length; k++) {
      if (startTime >= intervalEndTime[k - 1] && endTime <= intervalEndTime[k]) {
        totalPrice += (((endTime - startTime) / 60) * EC[k] * power[inedex]);
        break;
      } else if (startTime >= intervalEndTime[k - 1] && endTime >= intervalEndTime[k]) {
        totalPrice += (((intervalEndTime[k] - startTime) / 60) * EC[k] * power[inedex]);
        startTime = intervalEndTime[k];
      }
    }
    return totalPrice;
  }

  public void setOASData(double[] r, double[] p, double[] d, double[] d_bar, double[] e, double[] w, double[] power, double[][] s, int numberOfSalesmen) {
    this.r = r;
    this.p = p;
    this.d = d;
    this.d_bar = d_bar;
    this.e = e;
    this.w = w;
    this.power = power;
    this.s = s;
    this.numberOfSalesmen = numberOfSalesmen;
    C = new double[p.length];
  }

  @Override
  public void setData(chromosome chromosome1, int numberOfSalesmen) {
    this.chromosome1 = chromosome1;
    this.numberOfSalesmen = numberOfSalesmen;
    length = chromosome1.getLength();
  }

  public double getMinimumCost() {
    return minimumCost;
  }

  public double getMaximumRevenue() {
    return maximumRevenue;
  }

  @Override
  public double[] getObjectiveValues(int index) {
    double objectives[];
    objectives = chromosome1.getObjValue();
    double obj = evaluateAll(chromosome1, numberOfSalesmen);
    objectives[0] = obj;
    chromosome1.setObjValue(objectives);
    return chromosome1.getObjValue();
  }

  public void setPowerData(double[] p, double[] power) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  public void printResult(double time, int index, int lastindex) {
    for (int i = 0; i < chromosome1.genes.length; i++) {
      System.out.println("obj: " + (index));
      if (time < r[index]) {
        System.out.println("release-date: " + r[index]);
      }
      if (i == 0) {
        System.out.println("setup-times: " + s[0][index]);
      }
      if (i != 0) {
        System.out.println("setup-times: " + s[index + 1][lastindex]);
      }
      System.out.println("processing-time: " + p[index]);
      System.out.println("due-date: " + d[index]);
      System.out.println("deadline: " + d_bar[index]);
      System.out.println("completion-time: " + time);
      System.out.println("weight: " + w[index]);
      System.out.println("time: " + time);
      System.out.println("Revenue: " + Revenue);
      System.out.println("maximumRevenue: " + maximumRevenue);
    }

  }

  public static void main(String[] args) throws IOException {

    ObjectiveFunctionforOASWithTOU TPOAS = new ObjectiveFunctionforOASWithTOU();

    openga.applications.data.OASInstancesWithTOU OAS = new openga.applications.data.OASInstancesWithTOU();
    OAS.setData("@../../instances/SingleMachineOASWithTOU/10orders/Tao1/R1/Dataslack_10orders_Tao1R1_1.txt", 10);
    OAS.getDataFromFile();

    TPOAS.r = OAS.getR();
    TPOAS.p = OAS.getP();
    TPOAS.d = OAS.getD();
    TPOAS.d_bar = OAS.getD_bar();
    TPOAS.e = OAS.getE();
    TPOAS.w = OAS.getW();
    TPOAS.power = OAS.getPower();
    TPOAS.s = OAS.getS();
    TPOAS.C = new double[TPOAS.r.length];

    chromosome _chromosome1 = new chromosome();
    _chromosome1.setGenotypeAndLength(true, 12, 2);
    _chromosome1.generateTwoPartPop(12, 2);
    int[] soln = new int[]{5, 7, 6, 3, 2, 9, 8, 4, 1, 0};
    _chromosome1.setSolution(soln);
    TPOAS.evaluateAll(_chromosome1, 2);
    for (int i = 0; i < _chromosome1.getLength(); i++) {
      System.out.print(_chromosome1.genes[i] + " ");
    }
    System.out.println("\n" + TPOAS.evaluateAll(_chromosome1, 2));
  }
}
