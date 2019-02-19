package openga.ObjectiveFunctions;

import java.util.ArrayList;
import java.util.List;
import openga.chromosomes.*;

public class ObjectiveFunctionforOAS implements ObjectiveFunctionOASI {

  chromosome chromosome1;
  populationI population;// Part-one chromosomes
  int numberOfSalesmen;
  int length, indexOfObjective;

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
  double[] C;       //completion Time

  @Override
  public void calcObjective() {
//    System.out.print("calcObjective");
    double obj;
    double objectives[];
//    for (int i = 0; i < 1; i++) {
    for (int i = 0; i < population.getPopulationSize(); i++) {
//      for (int j = 0; j < population.getSingleChromosome(i).genes.length; j++) {
//        System.out.print(population.getSingleChromosome(i).genes[j] + " ");
//      }
//      System.out.println();
      objectives = population.getObjectiveValues(i);
      obj = evaluateAll(population.getSingleChromosome(i), numberOfSalesmen);
      objectives[indexOfObjective] = obj;
      population.setObjectiveValue(i, objectives);
      chromosome1.setObjValue(objectives);
      population.setSingleChromosome(i, chromosome1);
//      for (int j = 0; j < population.getSingleChromosome(i).genes.length; j++) {
//        System.out.print(population.getSingleChromosome(i).genes[j] + " ");
//      }
//      System.out.println();
    }
  }

  public void calcMaximumRevenue() {
    Revenue = 0;
    maximumRevenue = 0;
    int index, lastindex = 0;
    double time = 0, dayGap;
    List<Integer> _chromosome1 = new ArrayList<>();
    List<Integer> accept = new ArrayList<>();
    List<Integer> reject = new ArrayList<>();
    List<Integer> salesmen = new ArrayList<>();
    _chromosome1.addAll(chromosometoList(chromosome1));

    for (int i = 0; i < length; i++) {
      index = chromosome1.genes[i];
      if (time < r[index]) {
        time = r[index];
      }
      if (i == 0) {
        time += s[0][index];
      } else {
        time += s[lastindex + 1][index];
      }
      time += p[index];
      C[index] = time;

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
        if (Revenue == 0) {
          reject.add(_chromosome1.get(i));
        } else {
          accept.add(_chromosome1.get(i));
        }
        lastindex = index;
      }
    salesmen.add(accept.size());
    salesmen.add(reject.size());
    _chromosome1.clear();
    _chromosome1.addAll(accept);
    _chromosome1.addAll(reject);
//    _chromosome1.addAll(salesmen);
    chromosome1.setSolution(_chromosome1);
   
  }

  public double getMaximumRevenue() {
    return maximumRevenue;
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

  public List<Integer> chromosometoList(chromosome _chromosome1) {
    List<Integer> soln = new ArrayList<Integer>();
    for (int i = 0; i < _chromosome1.genes.length; i++) {
      soln.add(_chromosome1.genes[i]);
    }
    return soln;
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

  public double evaluateAll(chromosome _chromosome1, int numberOfSalesmen) {
    this.setData(_chromosome1, numberOfSalesmen);
    this.calcMaximumRevenue();
    return this.getMaximumRevenue();
  }

  @Override
  public void setData(chromosome chromosome1, int numberOfSalesmen) {
    this.chromosome1 = chromosome1;
    this.numberOfSalesmen = numberOfSalesmen;
    length = chromosome1.getLength();
  }

  public void setData(populationI population, int indexOfObjective) {
    this.population = population;
    this.length = population.getLengthOfChromosome();
    this.indexOfObjective = indexOfObjective;
  }

  public populationI getPopulation() {
    return population;
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

  public double getMinimumCost() {
    return minimumCost;
  }

  @Override
  public void setOASData(double[] r, double[] p, double[] d, double[] d_bar, double[] e, double[] w, double b, int numberOfSalesmen) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
}
