package openga.ObjectiveFunctions;

import java.util.ArrayList;
import java.util.List;
import openga.chromosomes.*;

/**
 *
 * @author YU-TANG CHANG
 */
public class ObjectiveFunctionforOAS_MLA extends TPObjectiveFunctionMTSP implements ObjectiveFunctionOASI {

  chromosome chromosome1;

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
  double[][] s;     //  setup times
  double[] C;       //  completion Time

  public static void main(String[] args) {
    ObjectiveFunctionforOAS_MLA TPOAS = new ObjectiveFunctionforOAS_MLA();
    TPOAS.r = new double[]{10, 2, 4, 6, 4, 5, 7, 3};
    TPOAS.p = new double[]{19, 13, 4, 13, 12, 12, 2, 6};
    TPOAS.d = new double[]{50, 60, 40, 70, 90, 100, 60, 30};
    TPOAS.d_bar = new double[]{70, 80, 50, 90, 110, 120, 70, 40};
    TPOAS.e = new double[]{19, 16, 10, 12, 19, 3, 16, 19};
    TPOAS.w = new double[]{0.95, 0.8, 1, 0.6, 0.95, 0.15, 1.6, 1.9};

    TPOAS.C = new double[TPOAS.r.length];

    chromosome _chromosome1 = new chromosome();
    _chromosome1.setGenotypeAndLength(true, 10, 2);

    int[] soln = new int[]{3, 0, 7, 4, 1, 5, 6, 2};
    _chromosome1.setSolution(soln);
    TPOAS.evaluateAll(_chromosome1, 2);
  }

  @Override
  public void calcObjective() {
    double obj;
    double objectives[];
    for (int i = 0; i < population.getPopulationSize(); i++) {
      objectives = population.getObjectiveValues(i);
      obj = evaluateAll(population.getSingleChromosome(i), numberOfSalesmen);
      objectives[indexOfObjective] = obj;
      population.setObjectiveValue(i, objectives);
      chromosome1.setObjValue(objectives);
      population.setSingleChromosome(i, chromosome1);
    }
  }

  public List<Integer> chromosometoList(chromosome _chromosome1) {
    int numberOfCities = length - numberOfSalesmen;
    List<Integer> soln = new ArrayList<Integer>();
    for (int i = 0; i < numberOfCities; i++) {
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

    int numberOfCities = length - numberOfSalesmen, currentPosition = 0, stopPosition = 0, index = 0;
    double time = 0;
    double time2 = 0;

    ArrayList<Integer> _chromosome1 = new ArrayList<>();
    ArrayList<Integer> accept = new ArrayList<>();
    ArrayList<Integer> reject = new ArrayList<>();
    _chromosome1.addAll(chromosometoList(chromosome1));    
    
    for (int i = 0; i < numberOfCities; i++) {
      index = chromosome1.genes[i];
//      System.out.print(index);
      if (time < r[index]) {
        time = r[chromosome1.genes[0]];
      }

      if (C[index] < d_bar[index]) {
        time += p[index];
        C[index] = time;
      } else {
        time2 = C[index];
      }

      if (C[index] <= d[index]) {
        Revenue = e[index];
      } else if (C[index] > d[index] && C[index] <= d_bar[index]) {
        Revenue = e[index] - (C[index] - d[index]) * w[index];
      } else {
        Revenue = 0;
      }

//      System.out.println(C[index]);
//      System.out.println(Revenue);
      if (Revenue == 0) {
        reject.add(_chromosome1.get(i));
      } else {
        accept.add(_chromosome1.get(i));
      }
    }

    _chromosome1.clear();
    _chromosome1.addAll(accept);
    _chromosome1.addAll(reject);
    chromosome1.setSolution(_chromosome1);
//    System.out.println(_chromosome1);
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
    return Revenue;
  }

//  @Override
//  public double[] getObjectiveValues(int index) {
//    double objectives[];
//    objectives = chromosome1.getObjValue();
//    double obj = evaluateAll(chromosome1, numberOfSalesmen);
//    objectives[0] = obj;
//    chromosome1.setObjValue(objectives);
//    return chromosome1.getObjValue();
//  }
}
