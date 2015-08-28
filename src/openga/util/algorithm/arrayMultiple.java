package openga.util.algorithm;

/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class arrayMultiple {
  public arrayMultiple() {
  }
  //openga.util.printClass p1 = new openga.util.printClass();

  public double[][] calcArray(double[][] A1, double[][] A2){
    double result[][] = new double[A1.length][A1[0].length];
    for(int i = 0 ; i < A1.length ; i ++ ){
      for(int j = 0 ; j < A1[0].length ; j ++ ){
        double row[] = A1[i];
        double column[] = getColumnArray(A2, j);
        result[i][j] = calcArray(row, column);
      }
    }
    return result;
  }

  public double[][] calcMarkovArray(double[][] A1, double[][] A2){
    double result[][] = new double[A1.length][A1[0].length];
    for(int i = 0 ; i < A1.length ; i ++ ){
      for(int j = 0 ; j < A1[0].length ; j ++ ){
        if(A1[i][j] != 0){
          double row[] = A1[i];
          double column[] = getColumnArray(A2, j);
          result[i][j] = calcArray(row, column);
        }
      }
    }
    return result;
  }


  public double calcArray(double[] A1, double[] A2){
    double result = 0;
    for(int i = 0 ; i < A1.length ; i ++ ){
      result += A1[i]*A2[i];
    }
    return result;
  }

  private double[] getColumnArray(double[][] A1, int index){
    double result[] = new double[A1.length];
    for(int i = 0 ; i < A1.length ; i ++ ){
      result[i] = A1[i][index];
    }
    return result;
  }



}