package openga.ObjectiveFunctions;

import java.util.ArrayList;
import java.util.List;
import openga.chromosomes.*;


public class TPObjectiveFunctionOASParallelPSD  extends TPObjectiveFunctionMTSP implements ObjectiveFunctionOASI {

  populationI population;// Part-one chromosomes
  chromosome chromosome1;
  double Revenue;
  double maximumRevenue;
  int numberOfSalesmen;
  int length, indexOfObjective;
  
  //Objective Value
  double minimumCost;
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
  double b;         //  Setup index for PSD.
  
  
  public List<Integer> chromosometoList(chromosome _chromosome1) {
    List<Integer> soln = new ArrayList<Integer>();
    for (int i = 0; i < _chromosome1.genes.length; i++) {
      soln.add(_chromosome1.genes[i]);
    }
    return soln;
  }
  
  
  public double evaluateAll(chromosome _chromosome1, int numberOfSalesmen) {
    this.setData(_chromosome1, numberOfSalesmen);
    this.calcMaximumRevenue();
    return this.getMaximumRevenue();
  }
  
  
  public void calcMaximumRevenue() {
    Revenue = 0;
    maximumRevenue = 0;
    int numberofMachines = numberOfSalesmen - 1;   
 
    int numberOfCities = length - numberOfSalesmen, index=0, lastindex = 0, currentPosition = 0, stopPosition = 0;
    
    double[] PSDtime = new double[numberofMachines];
    double[] time = new double[numberofMachines];
    
    for (int i=0;i<numberofMachines;i++){
      PSDtime[i]=0;
      time[i]=0;     
      C[i]=0;
    }

    List<Integer> _chromosome1 = new ArrayList<>();    
    List<List<Integer>> accept = new ArrayList<>();
    for (int i=0;i<numberofMachines;i++){
      accept.add(new ArrayList<Integer>());
    }
    
    List<Integer> reject = new ArrayList<>();
    List<Integer> salesmen = new ArrayList<>();
    _chromosome1.addAll(chromosometoList(chromosome1));
    
    int machineNumber = 0;
    for (int i = 0; i < numberOfSalesmen; i++) {
      stopPosition += _chromosome1.get(numberOfCities + i);
      for (int j = currentPosition; j < stopPosition; j++) {
        index = chromosome1.genes[j];
        if (time[machineNumber] < r[index]) {
          time[machineNumber] = r[index];
        }
        if (j != 0) {
          time[machineNumber] += p[index] + (PSDtime[machineNumber] * b);
        } 
        PSDtime[machineNumber] += p[index]; // Total_Time * b [0.1,0.2,1.0]
        C[machineNumber] = time[machineNumber];

        if (C[machineNumber] <= d[index]) {
          Revenue = e[index];
        } else if (C[machineNumber] > d[index] && C[machineNumber] <= d_bar[index]) {
          Revenue = e[index] - (C[machineNumber] - d[index]) * w[index];
        } else {
          Revenue = 0;
        }
        maximumRevenue += Revenue;

        if (Revenue == 0) {        
          reject.add(_chromosome1.get(j));
        } else {
          accept.get(machineNumber).add(_chromosome1.get(j));
        }
        lastindex = index;
      }
      
      currentPosition += _chromosome1.get(numberOfCities + i);      
      //判斷當前機器誰能最早空出時間處理下一個工件
      double count=99999999;
      for (int k=0;k<numberofMachines;k++){
        if (C[k] < count){count = C[k]; machineNumber = k;}                
      }
      
    }

    _chromosome1.clear();
    for (int m = 0; m < accept.size(); m++) {
      _chromosome1.addAll(accept.get(m));
      salesmen.add(accept.get(m).size());
    }
    salesmen.add(reject.size());
    _chromosome1.addAll(reject);
    _chromosome1.addAll(salesmen);
    chromosome1.setSolution(_chromosome1);
    maximumRevenue = maximumRevenue;
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
  
  public double getMaximumRevenue() {
    return maximumRevenue;
  }
  
//  public void calcMinimumCost() {
//  }
//  
//  public double getMinimumCost() {
//    return minimumCost;
//  }
  

  
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
    C = new double[p.length];
  }

  @Override
  public void setData(populationI population, int indexOfObjective) {
    this.population = population;
    this.length = population.getLengthOfChromosome();
    this.indexOfObjective = indexOfObjective;
  }

  @Override
  public void setData(chromosome chromosome1, int numberOfSalesmen) {
    this.chromosome1 = chromosome1;
    this.numberOfSalesmen = numberOfSalesmen;
    length = chromosome1.getLength();
  }
  
  @Override
  public populationI getPopulation() {
    return population;
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
  public void setOASData(double[] r, double[] p, double[] d, double[] d_bar, double[] e, double[] w, double[][] s, int numberOfSalesmen) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
  
}