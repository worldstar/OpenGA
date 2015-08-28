package openga.util.algorithm;

/**
 * <p>Title: The OpenGA project</p>
 * <p>Description: The project is to build general framework of Genetic algorithm and problem independent.</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class getSum {

  public getSum() {
  }

  public getSum(double vals[]) {
    values = vals;
    calcSum();
  }

  public getSum(int vals[]) {
    values_int = vals;
    calcSum();
  }

  double values[];
  int values_int[];
  double sum = 0;
  int sum_int = 0;

  public void setData(double vals[]){
    values = vals;
    calcSum();
  }

  public void calcSum(){
    for(int i = 0 ; i < values.length ; i ++ ){
      sum += values[i];
    }
  }

  public void calcSum_int(){
    for(int i = 0 ; i < values.length ; i ++ ){
      sum_int += values_int[i];
    }
  }

  public double getSumResult(){
    return sum;
  }

  public int getSumResult_int(){
    return sum_int;
  }
}
