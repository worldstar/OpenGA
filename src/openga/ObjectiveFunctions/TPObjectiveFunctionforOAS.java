package openga.ObjectiveFunctions;
import openga.chromosomes.*;

/**
 *
 * @author Administrator
 */
public class TPObjectiveFunctionforOAS extends TPObjectiveFunctionMTSP implements ObjectiveFunctionOASI {
  chromosome chromosome1;
  
  //Objective Value
  double minimumCost;
  double[] acceptionCost;
  double[] rejectionCost;
  
  //Instance Data
  double [] r;       //  release date.
  double [] p;       //  processing time
  double [] d;       //  due-date
  double [] d_bar;   //  deadline
  double [] e;       //  revenue
  double [] w;       //  weight
  double [][] s;     //  setup times
  double [] T;
  double [] C;
  
  @Override
  public void calcObjective() {
    double obj;
    double objectives[];

    for(int i = 0 ; i < population.getPopulationSize() ; i ++ ){
      objectives = population.getObjectiveValues(i);
      obj = evaluateAll(population.getSingleChromosome(i), numberOfSalesmen);
//      System.out.println(obj);
      objectives[indexOfObjective] = obj;
      population.setObjectiveValue(i, objectives);
//      for(int j=0; j<population.getObjectiveValues(i).length; j++){
//        System.out.println(population.getObjectiveValues(i)[j]);
//      }
    }
  }
  
  @Override
  public double evaluateAll(chromosome _chromosome1, int numberOfSalesmen){
    this.setData(_chromosome1, numberOfSalesmen);
    this.calcMinimumCost();
    return this.getMinimumCost();
  }

  public void calcMinimumCost() {
    int currentPosition = 0;//To record the position of the Part I chromosome
    int numberOfCities = length - numberOfSalesmen;
    int stopPosition = chromosome1.genes[numberOfCities];
    int t = 0;
    
    for(int j = 0; j < numberOfSalesmen; j++){
      for(int k = currentPosition; k < stopPosition; k++){
          int i = chromosome1.genes[j];
        if(j == numberOfSalesmen - 1) {//Ii = 0
          rejectionCost[i] = e[i];
          minimumCost += rejectionCost[i];   
        }
        else {//Ii = 1
          t += p[i];
          C[i] = t;
          T[i] = Math.max(0,C[i]-d[i]);
          acceptionCost[i] = w[i]*T[i];
          minimumCost += acceptionCost[i];
        }
          currentPosition ++;
      }
      stopPosition += (numberOfCities - currentPosition);
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
    T = new double[p.length];
    acceptionCost = new double[p.length];
    rejectionCost = new double[e.length];
  }

  public void setData(chromosome chromosome1, int numberOfSalesmen) {
    this.chromosome1 = chromosome1;
    this.numberOfSalesmen = numberOfSalesmen;
    length = chromosome1.getLength();
  }
  
  public double getMinimumCost() {
    return minimumCost;
  }
  
}
