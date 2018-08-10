package openga.ObjectiveFunctions;

import java.util.ArrayList;
import java.util.List;
import openga.chromosomes.*;

/**
 *
 * @author YU-TANG CHANG
 */
public class TPObjectiveFunctionforOAS_Setup extends TPObjectiveFunctionMTSP implements ObjectiveFunctionOASI {

  chromosome chromosome1;

  //Objective Value
  double minimumCost;
  double Revenue;
  double maximumRevenue;
  double PSDtime;
  double b;
  boolean havePunish;

  //Instance Data
  double[] r;       //  release date.(arrival time)
  double[] p;       //  processing time
  double[] d;       //  due-date
  double[] d_bar;   //  deadline
  double[] e;       //  revenue
  double[] w;       //  weight
  double[][] s;     //  setup times
  double[] C;       //completion Time

  public static void main(String[] args) {
    TPObjectiveFunctionforOAS_Setup TPOAS = new TPObjectiveFunctionforOAS_Setup();
//    TPOAS.r = new double[]{3, 29, 22, 14, 24, 8, 16, 46, 39, 43};
//    TPOAS.p = new double[]{15, 14, 11, 3, 14, 2, 11, 7, 10, 6};
//    TPOAS.d = new double[]{86, 63, 98, 59, 75, 60, 101, 78, 66, 109};
//    TPOAS.d_bar = new double[]{97, 73, 106, 62, 85, 62, 109, 83, 73, 114};
//    TPOAS.e = new double[]{15, 8, 6, 18, 17, 10, 6, 17, 20, 15};
//    TPOAS.w = new double[]{1.3636, 0.8, 0.75, 6, 1.7, 5, 0.75, 3.4, 2.8571, 3};
//    TPOAS.s = new double[][]{
//      {10, 9, 6, 3, 5, 4, 8, 5, 4, 2},
//      {0, 5, 9, 9, 4, 8, 9, 7, 6, 10},
//      {4, 0, 5, 5, 3, 7, 6, 6, 10, 3},
//      {5, 9, 0, 9, 6, 3, 5, 4, 5, 4},
//      {3, 4, 9, 0, 10, 8, 7, 7, 4, 10},
//      {9, 8, 6, 4, 0, 5, 3, 5, 5, 6},
//      {5, 7, 3, 4, 10, 0, 9, 4, 7, 9},
//      {5, 9, 9, 5, 9, 9, 0, 10, 5, 9},
//      {10, 10, 5, 10, 3, 10, 9, 0, 9, 7},
//      {9, 10, 7, 5, 6, 8, 4, 4, 0, 10}};
    TPOAS.r = new double[]{0, 0, 3};
    TPOAS.p = new double[]{5, 1, 1};
    TPOAS.d = new double[]{6, 6, 6};
    TPOAS.d_bar = new double[]{10, 10, 10};
    TPOAS.e = new double[]{1, 1, 2};
    TPOAS.w = new double[]{0.25, 0.25, 0.5};
    TPOAS.s = new double[][]{
      {1, 1, 1},
      {0, 1, 1},
      {1, 0, 1},
      {1, 1, 0}};

    TPOAS.C = new double[TPOAS.r.length];

    chromosome _chromosome1 = new chromosome();
    chromosome _chromosome2 = new chromosome();
    _chromosome1.setGenotypeAndLength(true, 5, 2);
    _chromosome2.setGenotypeAndLength(true, 5, 2);
    _chromosome1.generateTwoPartPop(5, 2);
    _chromosome2.generateTwoPartPop(5, 2);

    int[] soln = new int[]{0, 2, 1, 3, 0};
    _chromosome1.setSolution(soln);
    TPOAS.evaluateAll(_chromosome1, 2);
//    System.out.println("ObjValue1" + _chromosome1.getObjValue()[0]);
//    System.out.println("ObjValue1" + TPOAS.maximumRevenue);
//    TPOAS.maximumRevenue = 0;
//    int[] soln2 = new int[]{0, 4, 3, 8, 7, 2, 9, 1, 6, 5, 7, 3};
//    _chromosome2.setSolution(soln2);
//    TPOAS.evaluateAll(_chromosome2, 2);
//    System.out.println("ObjValue2" + _chromosome2.getObjValue()[0]);
//    System.out.println("ObjValue2" + TPOAS.maximumRevenue);
  }

  @Override
  public void calcObjective() {
//    System.out.print("calcObjective");
    double obj;
    double objectives[];
//    for (int i = 0; i < 1; i++) {
    for (int i = 0; i < population.getPopulationSize(); i++) {
//      System.out.print("population(before): ");
//      for (int j = 0; j < population.getSingleChromosome(i).genes.length; j++) {
//        System.out.print(population.getSingleChromosome(i).genes[j] + " ");
//      }
//      System.out.println("End");
      objectives = population.getObjectiveValues(i);
      obj = evaluateAll(population.getSingleChromosome(i), numberOfSalesmen);
      objectives[indexOfObjective] = obj;
      population.setObjectiveValue(i, objectives);
      chromosome1.setObjValue(objectives);
      population.setSingleChromosome(i, chromosome1);
//      System.out.print("population(after):  ");
//      for (int j = 0; j < population.getSingleChromosome(i).genes.length; j++) {
//        System.out.print(population.getSingleChromosome(i).genes[j] + " ");
//      }
//      System.out.println("End");
    }
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
    return this.getMaximumRevenue();
  }

  public void calcMaximumRevenue() {
    Revenue = 0;
    maximumRevenue = 0;
    int numberOfCities = length - numberOfSalesmen,
            currentPosition = 0, stopPosition = 0,
            index, lastindex = 0;
    double time = 0, dayGap;
    List<Integer> _chromosome1 = new ArrayList<>();
    List<Integer> accept = new ArrayList<>();
    List<Integer> reject = new ArrayList<>();
    List<Integer> salesmen = new ArrayList<>();
    _chromosome1.addAll(chromosometoList(chromosome1));

    for (int i = 0; i < numberOfSalesmen; i++) {
      stopPosition += _chromosome1.get(numberOfCities + i);
      for (int j = currentPosition; j < stopPosition; j++) {
        index = chromosome1.genes[j];
        if (time < r[index]) {
          time = r[index];
        }
        if (j != 0) {
          time += p[index] + (PSDtime * b);
        } 
        PSDtime += p[index]; // Total_Time * b [0.1,0.2,1.0]
        C[index] = time;

//        dayGap = Math.max(0, Math.min((d_bar[index] - d[index]), (d_bar[index] - C[index])));
        if (C[index] <= d[index]) {
          Revenue = e[index];
        } else if (C[index] > d[index] && C[index] <= d_bar[index]) {
          Revenue = e[index] - (C[index] - d[index]) * w[index];
        } else {
          Revenue = 0;
        }

        maximumRevenue += Revenue;
//        if(maximumRevenue>90){
//          printResult(time,index,lastindex);
//          System.exit(0);
//        }
        
//              System.out.println("obj: " + (index));
//          System.out.println("release-date: " + r[index]);
//        if (j == 0) {
//          System.out.println("setup-times: " + s[0][index]);
//        }
//        if (j != 0) {
//          System.out.println("setup-times: " + s[lastindex+1][index]);
//        }
//        System.out.println("processing-time: " + p[index]);
//        System.out.println("due-date: " + d[index]);
//        System.out.println("deadline: " + d_bar[index]);
//        System.out.println("completion-time: " + time);
//        System.out.println("weight: " + w[index]);
//        System.out.println("time: " + time);
//        System.out.println("Revenue: " + Revenue);
//        System.out.println("maximumRevenue: " + maximumRevenue);
        

        if (Revenue == 0) {
          
          reject.add(_chromosome1.get(j));
        } else {
          accept.add(_chromosome1.get(j));
        }
        lastindex = index;
      }
      currentPosition += _chromosome1.get(numberOfCities + i);
    }
    salesmen.add(accept.size());
    salesmen.add(reject.size());
    _chromosome1.clear();
    _chromosome1.addAll(accept);
    _chromosome1.addAll(reject);
    _chromosome1.addAll(salesmen);
    chromosome1.setSolution(_chromosome1);
  }

  public void calcMinimumCost() {
  }
  
  public void printResult(double time, int index, int lastindex) {
    for(int i = 0; i < chromosome1.genes.length; i++){
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

  public void setOASData(double[] r, double[] p, double[] d, double[] d_bar, double[] e, double[] w, double[][] s, int numberOfSalesmen) {
    this.r = r;
    this.p = p;
    this.d = d;
    this.d_bar = d_bar;
    this.e = e;
    this.w = w;
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

  @Override
  public void setOASData(double[] r, double[] p, double[] d, double[] d_bar, double[] e, double[] w, double b, int numberOfSalesmen) {
    this.r = r;
    this.p = p;
    this.d = d;
    this.d_bar = d_bar;
    this.e = e;
    this.w = w;
    this.b = b;
    this.numberOfSalesmen = numberOfSalesmen;
  }
}
