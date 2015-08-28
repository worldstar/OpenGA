/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package openga.ObjectiveFunctions;
import openga.chromosomes.*;
/**
 *
 * @author nhu
 */
public class forMTSPDistanceCalculation extends forDistanceCalculation2{
  double distanceToOriginal[];

  double distanceMatrix[][]; //to store the distance between two points
  chromosome chromosome1;    //A chromosome
  int length;                //chromosome length
  double objVal = 0;         //The traveling distance.

  //MTSP
  chromosome PIISolutions;
  
  public void setData(double distanceToOriginal[], double distanceMatrix[][], chromosome chromosome1, chromosome PIISolutions){
    this.distanceToOriginal = distanceToOriginal;
    this.distanceMatrix = distanceMatrix;
    this.chromosome1 = chromosome1;
    this.PIISolutions = PIISolutions;
    length = distanceMatrix.length;
  }

  public void setData(populationI population){
    System.out.println("Call to a function setData(populationI population) that is not supported at forMTSPDistanceCalculation.java.");
    System.exit(0);
  }


  public void calcObjective(){
      //to get the distance of each salesmen
      int currentPosition = 0;//To record the position of the Part I chromosome
      
      for(int k = 0 ; k < PIISolutions.genes.length ; k ++){
        //from the original point to the first position.
        objVal += distanceToOriginal[chromosome1.genes[currentPosition]];
        
        int numberOfCities = PIISolutions.getSolution()[k];
        int stopPosition = numberOfCities + currentPosition - 1;

        for(int i = currentPosition ; i <= stopPosition ; i ++ ){
          if(i < stopPosition){
            int index1 = chromosome1.genes[i];
            int index2 = chromosome1.genes[i+1];
            objVal += distanceMatrix[index1][index2];       
          }
          else{
            //The last point then go back to original point.
            objVal += distanceToOriginal[chromosome1.genes[currentPosition]];
          }
          currentPosition ++;
        }
      }
  }

  public double getObjectiveValue(){
    return objVal;
  }
}

