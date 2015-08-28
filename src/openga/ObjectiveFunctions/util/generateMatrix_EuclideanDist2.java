package openga.ObjectiveFunctions.util;

/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: It's from original point to other points.</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class generateMatrix_EuclideanDist2 extends generateMatrix_EuclideanDist {
  public generateMatrix_EuclideanDist2() {
  }
  double distanceMatrix[];
  double originalPoint[];
  int length;

  //to set coordinates data.
  public void setData(double originalPoint[], double array1[][]){
    this.originalPoint = originalPoint;
    this.coordinates = array1;
    length = array1.length;
    distanceMatrix = new double[length];
  }

  public void setDistanceMatrixData(){
    euclideanDistance euclieanDistance1;

    for(int i = 0 ; i < length ; i ++ ){
      euclieanDistance1 = new euclideanDistance(originalPoint, coordinates[i]);
      distanceMatrix[i] = euclieanDistance1.getDistance();
    }
  }

  public double[] getMatrix2(){
    return distanceMatrix;
  }


}