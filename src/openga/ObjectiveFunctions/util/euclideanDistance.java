package openga.ObjectiveFunctions.util;

/**
 * <p>Title: The OpenGA project</p>
 * <p>Description: The project is to build general framework of Genetic algorithm and problem independent.</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

//to calculate the distance between two points
//Euclidean distance
public class euclideanDistance{
  //the coordinate of two points, a and b
  double a[];
  double b[];

  public euclideanDistance(){
  }

  public euclideanDistance(double x[], double y[]){
    a = x;
    b = y;
  }

  public void setXY(double x[], double y[]){
    a = x;
    b = y;
  }

  public double getDistance(){
    double dist = 0.0;
    double X_Y = Math.pow((a[0] - b[0]),2) + Math.pow((a[1] - b[1]),2);
    dist = Math.sqrt(X_Y);
    //dist = Math.pow(X_Y, 1/2);
    //System.out.print(" dist="+dist);
    return dist;
  }
}
