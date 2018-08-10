package openga.ObjectiveFunctions;
import openga.chromosomes.*;

/**
 *
 * @author Administrator
 */
public class TPObjectiveFunctionforOASII extends TPObjectiveFunctionMTSP implements ObjectiveFunctionOASI {
  chromosome chromosome1;
  
  //Objective Value
  double minimumCost;
  double[] acceptionCost;
  double[] rejectionCost;
  double maximumRevenue;
  boolean havePunish = true;
  
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
    for(int i = 0 ; i < 1 ; i ++ ){
//    for(int i = 0 ; i < population.getPopulationSize() ; i ++ ){
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
    this.calcMaximumRevenue();
    return this.getMaximumRevenue();
  }
  
  public void calcMaximumRevenue() {

    maximumRevenue = 0;
    int numberOfCities = length - numberOfSalesmen;
    int currentPosition = 0;//To record the position of the Part I chromosome
    int stopPosition = chromosome1.genes[numberOfCities];
    int lastindex = 0;
    double time = 0;
    
    for(int i = 0; i < numberOfSalesmen; i++){
      for(int j = currentPosition; j < stopPosition; j++){
        int index = chromosome1.genes[j];
        
        
//        System.out.println("obj:"+(index+1));
        
        time += r[index];
        time += p[index];
        if(j == 0) {
          s[index][lastindex] = 0;
        }
        time += s[index][lastindex];
        
        C[index] = time;
//        System.out.println("C = r:"+r[index]+" + p:"+p[index]+" + s:"+s[index][lastindex]+" = "+C[index]);
        
        T[index] = Math.max(0,C[index]-d[index]);
        System.out.println("T = C:"+C[index]+" - d:"+d[index]+" = "+(C[index]-d[index]));
        
        //if Object have Fine
        if(havePunish) {
          maximumRevenue += (e[index]-(w[index]*T[index]));
          System.out.println("Revenue = e:"+e[index]+" - ( w:"+w[index]+" * T:"+T[index]+" ) = "+(e[index]-(w[index]*T[index])));
        }
        else {
          maximumRevenue += Math.max(0,(e[index]-(w[index]*T[index])));
//          System.out.println("Revenue = e:"+e[index]+" - ( w:"+w[index]+" * T:"+T[index]+" ) = "+Math.max(0,(e[index]-(w[index]*T[index]))));
        }
//        System.out.println();
//        System.out.println("e = "+e[index]+"- ( w = "+w[index]+" * T = "+T[index]+" ) = maximumRevenue = "+maximumRevenue);

        lastindex = index;
        currentPosition ++;
      }
      stopPosition += (numberOfCities - currentPosition); //for parallel machine
    }
  }

  public void calcMinimumCost() {
    int currentPosition = 0;//To record the position of the Part I chromosome
    int numberOfCities = length - numberOfSalesmen;
    int stopPosition = chromosome1.genes[numberOfCities];
    int t = 0;
    
//    System.out.print("chromosome1 : ");
//      for(int i = 0; i < chromosome1.getLength(); i++){
//        System.out.print(chromosome1.genes[i]+",");
//      }
//      System.out.println();
//    int i;
    int lasti = 0;
//    System.out.print("i = ");
    for(int j = 0; j < numberOfSalesmen; j++){
      int  i = chromosome1.genes[j];
//      System.out.print(i+" ");
      for(int k = currentPosition; k < stopPosition; k++){
        
          
          
        if(j == numberOfSalesmen - 1) {//for Rejection
          rejectionCost[i] = e[i];
          minimumCost += rejectionCost[i];   
        }
        else {//for Acception
          t += r[i];
//          System.out.println("r = "+r[i]);
//          t += s[i][lasti];
//          System.out.println("s = "+s[i][lasti]);
          t += p[i];
//          System.out.println("p = "+p[i]);
          C[i] = t;
          T[i] = Math.max(0,C[i]-d[i]);
          acceptionCost[i] = w[i]*T[i];
          minimumCost += acceptionCost[i];
        }
          lasti = i;  //last object sequence
          currentPosition ++; //for parallel machine
      }
      stopPosition += (numberOfCities - currentPosition); //for parallel machine
    }
//    System.out.println();
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

  public double getMaximumRevenue() {
    return maximumRevenue;
  }

  @Override
  public void setOASData(double[] r, double[] p, double[] d, double[] d_bar, double[] e, double[] w, double b, int numberOfSalesmen) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
  
}
