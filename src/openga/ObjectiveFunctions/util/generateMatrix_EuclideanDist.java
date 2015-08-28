package openga.ObjectiveFunctions.util;
import java.util.Vector;

/**
 *It's apply thte Euclidean distance.
 */

public class generateMatrix_EuclideanDist {

  public generateMatrix_EuclideanDist() {
  }

  double coordinates[][];
  double distanceMatrix[][];
  Vector location_id;
  double Y;

  //to set coordinates data.
  public void setData(double array1[][]){
    this.coordinates = array1;
    distanceMatrix = new double[array1.length][array1.length];
  }

  public void setDistanceMatrixData(){
    euclideanDistance euclieanDistance1;
    for(int i = 0 ; i < distanceMatrix.length ; i ++ ){
      for(int j = 0 ; j < distanceMatrix.length ; j ++ ){
        if(i > j){
          euclieanDistance1 = new euclideanDistance(coordinates[i], coordinates[j]);
          distanceMatrix[i][j] = distanceMatrix[j][i] = euclieanDistance1.getDistance();
          //System.out.println(i+": "+j+" distanceMatrix[i][j] :" +distanceMatrix[i][j]);
        }
        else if (i == j){
          distanceMatrix[i][j] = -1;
        }
      }
    }
  }

  //return the result of distanceMatrix
  public double[][] getMatrix(){
    return distanceMatrix;
  }
}
