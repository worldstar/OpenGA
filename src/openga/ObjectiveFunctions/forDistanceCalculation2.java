package openga.ObjectiveFunctions;
import openga.chromosomes.*;
import openga.chromosomes.chromosome;

/**
 * <p>Title: The OpenGA project</p>
 *
 * <p>Description: The project is to build general framework of Genetic
 * algorithm and problem independent. It adds original point.</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Yuan-Ze University</p>
 *
 * @author Chen, Shih-Hsin
 * @version 1.0
 */
public class forDistanceCalculation2 {
  public forDistanceCalculation2() {
  }
  double distanceToOriginal[];

  double distanceMatrix[][]; //to store the distance between two points
  chromosome chromosome1;    //A chromosome
  int length;                //chromosome length
  double objVal = 0;         //The traveling distance.

  public void setData(double distanceToOriginal[], double distanceMatrix[][], chromosome chromosome1){
    this.distanceToOriginal = distanceToOriginal;
    this.distanceMatrix = distanceMatrix;
    this.chromosome1 = chromosome1;
    length = distanceMatrix.length;
  }

  public void setData(populationI population){
    System.out.println("Call to a function that is not supported at forDistanceCalculation2.java.");
    System.exit(0);
    this.distanceMatrix = distanceMatrix;
    this.chromosome1 = chromosome1;
    length = distanceMatrix.length;
  }


  public void calcObjective(){
    //from the original point to the first position.
    objVal += distanceToOriginal[chromosome1.genes[0]];
    for(int i = 0 ; i < length - 1 ; i ++ ){
      int index1 = chromosome1.genes[i];
      int index2 = chromosome1.genes[i+1];
      objVal += distanceMatrix[index1][index2];
    }
    //The last point then go back to original point.
    objVal += distanceToOriginal[chromosome1.genes[length - 1]];
  }

  public double getObjectiveValue(){
    return objVal;
  }

}
