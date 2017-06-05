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
  double maximumRevenue;
  boolean havePunish = false;
  
  //Instance Data
  double [] r;       //  release date.
  double [] p;       //  processing time
  double [] d;       //  due-date
  double [] d_bar;   //  deadline
  double [] e;       //  revenue
  double [] w;       //  weight
  double [][] s;     //  setup times
  double [] T;//
  double [] C;//completion Time
  
  
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
      chromosome1.setObjValue(objectives);
//      System.out.println(chromosome1.getObjValue()[0]);
    }
  }
  
  @Override
  public double evaluateAll(chromosome _chromosome1, int numberOfSalesmen){
    this.setData(_chromosome1, numberOfSalesmen);
    this.calcMaximumRevenue(numberOfSalesmen);
    return this.getMaximumRevenue();
  }
  
  public void calcMaximumRevenue(int numberOfSalesmen) {
    maximumRevenue = 0;
    int numberOfCities = length - numberOfSalesmen;
//    System.out.println("length "+length);
//    System.out.println("numberOfSalesmen "+numberOfSalesmen);
//    System.out.println("numberOfCities "+numberOfCities);
    
    int currentPosition = 0;//To record the position of the Part I chromosome
    int stopPosition = chromosome1.genes[numberOfCities];
    int lastindex = 0;
    double time = 0;
    
//    System.out.println("stopPosition "+stopPosition);
    
//    for(int i = 0; i < chromosome1.genes.length; i++){
//      System.out.println("obj:"+i+" "+(chromosome1.genes[i]+1));
//    }
    
    for(int i = 0; i < numberOfSalesmen-1; i++){
      for(int j = currentPosition; j < stopPosition; j++){
         int index = chromosome1.genes[j];
        
//        System.out.println("obj:"+(index));
        
        time += r[index];
        time += p[index];
        if(j == 0) {
          s[index][lastindex] = 0;
        }
        time += s[index][lastindex];
        
        C[index] = time;
//        System.out.println("Completion-Time = release-date:"+r[index]+" + processing-time:"+p[index]+" + sequence dependent setup times:"+s[index][lastindex]+" = "+C[index]);
        
//        T[index] = Math.max(0,C[index]-d[index]);
//        System.out.println("T = C:"+C[index]+" - d:"+d[index]+" = "+T[index]);
        
        //if Object have Fine
        if(havePunish) {
          double dayGap = C[index]-d_bar[index];
          maximumRevenue += dayGap*w[index];
//          System.out.println("Revenue = "+maximumRevenue);
        }
        else {
          
//          System.out.println("due-date = "+d[index]);
//          System.out.println("deadline = "+d_bar[index]);
//          System.out.println("Completion-Time = "+C[index]);
          
          double dayGap = Math.max(0,Math.min((d_bar[index]-d[index]),(d_bar[index]-C[index])));
          
//          System.out.println("dayGap = "+dayGap);
//          System.out.println("weight = "+w[index]);
          
          double Revenue = dayGap*w[index];
          maximumRevenue += Revenue;
          
//          maximumRevenue += (e[index]-(w[index]*T[index]));
//          System.out.println("Revenue = "+Revenue);
//          System.out.println("maximumRevenue = "+maximumRevenue);
        }
//        System.out.println();
//        System.out.println("e = "+e[index]+"- ( w = "+w[index]+" * T = "+T[index]+" ) = maximumRevenue = "+maximumRevenue);

        lastindex = index;
        currentPosition ++;
      }
//      stopPosition += (numberOfCities - currentPosition); //for parallel machine
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
  
  public double[] getObjectiveValues(int index) {
//    System.out.println("getObjectiveValues");
    double objectives[];
    objectives = chromosome1.getObjValue();
    double obj = evaluateAll(chromosome1, numberOfSalesmen);
    objectives[0] = obj;
    chromosome1.setObjValue(objectives);
    return chromosome1.getObjValue();
  } 
  
}
