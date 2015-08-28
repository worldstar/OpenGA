package openga.util.algorithm;

/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class arrayDimensionTransform {
  public arrayDimensionTransform() {
  }

  public double[] dim2To1(double[][] A1){
    double A2[] = new double[A1.length*A1[0].length];
    int counter = 0;
    for(int i = 0 ; i < A1.length ; i ++ ){
      for(int j = 0 ; j < A1[0].length ; j ++ ){
        A2[counter++] = A1[i][j];
      }
    }
    return A2;
  }
}