package openga.ObjectiveFunctions;

import java.util.ArrayList;
import java.util.List;
import openga.chromosomes.*;

/**
 *
 * @author YU-TANG CHANG
 */
public class TPObjectiveFunctionforOAS extends TPObjectiveFunctionMTSP implements ObjectiveFunctionOASI {

  chromosome chromosome1;

  //Objective Value
  double minimumCost;
  double maximumRevenue;
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
    TPObjectiveFunctionforOAS TPOAS = new TPObjectiveFunctionforOAS();
    TPOAS.r = new double[]{3, 29, 22, 14, 24, 8, 16, 46, 39, 43};
    TPOAS.p = new double[]{15, 14, 11, 3, 14, 2, 11, 7, 10, 6};
    TPOAS.d = new double[]{86, 63, 98, 59, 75, 60, 101, 78, 66, 109};
    TPOAS.d_bar = new double[]{97, 73, 106, 62, 85, 62, 109, 83, 73, 114};
    TPOAS.e = new double[]{15, 8, 6, 18, 17, 10, 6, 17, 20, 15};
    TPOAS.w = new double[]{1.3636, 0.8, 0.75, 6, 1.7, 5, 0.75, 3.4, 2.8571, 3};
    TPOAS.s = new double[][]{
      {10, 9, 6, 3, 5, 4, 8, 5, 4, 2},
      {0, 5, 9, 9, 4, 8, 9, 7, 6, 10},
      {4, 0, 5, 5, 3, 7, 6, 6, 10, 3},
      {5, 9, 0, 9, 6, 3, 5, 4, 5, 4},
      {3, 4, 9, 0, 10, 8, 7, 7, 4, 10},
      {9, 8, 6, 4, 0, 5, 3, 5, 5, 6},
      {5, 7, 3, 4, 10, 0, 9, 4, 7, 9},
      {5, 9, 9, 5, 9, 9, 0, 10, 5, 9},
      {10, 10, 5, 10, 3, 10, 9, 0, 9, 7},
      {9, 10, 7, 5, 6, 8, 4, 4, 0, 10}};
    TPOAS.C = new double[TPOAS.r.length];

    chromosome _chromosome1 = new chromosome();
    chromosome _chromosome2 = new chromosome();
    _chromosome1.setGenotypeAndLength(true, 12, 2);
    _chromosome2.setGenotypeAndLength(true, 12, 2);
    _chromosome1.generateTwoPartPop(12, 2);
    _chromosome2.generateTwoPartPop(12, 2);
    
    int[] soln = new int[]{0, 4, 3, 8, 7, 2, 9, 1, 6, 5, 8, 2};
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
    maximumRevenue = 0;
    int numberOfCities = length - numberOfSalesmen,
            currentPosition = 0, stopPosition = 0,
            index, lastindex = 0;
    double time = 0, Revenue, dayGap;
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
          time += s[index][lastindex];
        }
        time += p[index];
        C[index] = time;
        
//        if(C[index] < d[index]){
//          Revenue = e[index];
//        }else{
//          Revenue = 0;
//        }
        
        dayGap = Math.max(0, Math.min((d_bar[index] - d[index]), (d_bar[index] - C[index])));
        Revenue = dayGap * w[index];
        maximumRevenue += Revenue;
//        System.out.println("currentPosition: " + currentPosition);
//        System.out.println("stopPocurrentPositionsition: " + stopPosition);
//        System.out.println("obj: " + (index));
//        System.out.println("release date: " + r[index]);
//        System.out.println("processing time: " + p[index]);
//        System.out.println("setup times: " + s[index][lastindex]);
//        System.out.println("time: " + time);
//        System.out.println("dayGap = " + dayGap);
//        System.out.println("weight = " + w[index]);
//        System.out.println("Revenue = " + Revenue);
//        System.out.println("maximumRevenue = " + maximumRevenue);

        if (Revenue == 0) {
          reject.add(_chromosome1.get(j));
        } else {
          accept.add(_chromosome1.get(j));
        }
        lastindex = index;
      }
      currentPosition += _chromosome1.get(numberOfCities + i);
    }

    for (int i = 0; i < numberOfSalesmen - 1; i++) {
      int cities = accept.size();
      salesmen.add(cities);
    }

    salesmen.add(reject.size());
    _chromosome1.clear();
    _chromosome1.addAll(accept);
    _chromosome1.addAll(reject);
    _chromosome1.addAll(salesmen);
    chromosome1.setSolution(_chromosome1);
    /*
    System.out.print("accept: ");
    for (int i = 0; i < accept.size(); i++) {
      System.out.print(accept.get(i) + " ");
    }
    System.out.println("End");
    System.out.print("reject: ");
    for (int i = 0; i < reject.size(); i++) {
      System.out.print(reject.get(i) + " ");
    }
    System.out.println("End");
    System.out.print("salesmen: ");
    for (int i = 0; i < salesmen.size(); i++) {
      System.out.print(salesmen.get(i) + " ");
    }
    System.out.println("End");
    System.out.print("_chromosome1(after): ");
    for (int i = 0; i < _chromosome1.size(); i++) {
      System.out.print(_chromosome1.get(i) + " ");
    }
    System.out.println("End");//*/

//    System.out.println("_chromosome1 : " + _chromosome1.toString());
//    System.out.println("maximumRevenue = " + maximumRevenue);
  }

  public void calcMinimumCost() {
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
}
