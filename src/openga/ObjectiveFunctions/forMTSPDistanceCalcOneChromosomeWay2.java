/*
 * The objective function for the MTSP which employs the one-chromosome encoding.
 * The segmentation notation is not using the -1. Instead, we use the segmentation
 * which is larger than n.
 */

package openga.ObjectiveFunctions;
import openga.chromosomes.*;
/**
 *
 * @author nhu
 */
public class forMTSPDistanceCalcOneChromosomeWay2 extends forDistanceCalculation2{
  double distanceToOriginal[];

  double distanceMatrix[][]; //to store the distance between two points
  chromosome chromosome1;    //A chromosome
  int numberOfSalesmen; //chromosome length
  int length = 0; //Real length of the original problem, say gene length - numberOfSalesmen
  double objVal = 0;         //The traveling distance.
  
  int travelDistances[];      //The total travel distances of each salesman.
  
  public void setData(double distanceToOriginal[], double distanceMatrix[][], chromosome chromosome1, int numberOfSalesmen){
    this.distanceToOriginal = distanceToOriginal;
    this.distanceMatrix = distanceMatrix;
    this.chromosome1 = chromosome1;
    this.numberOfSalesmen = numberOfSalesmen;
    travelDistances = new int[numberOfSalesmen];
    length = chromosome1.genes.length - numberOfSalesmen;
  }

  public void setData(populationI population){
    System.out.println("Call to a function setData(populationI population) that is not supported at forMTSPDistanceCalculation.java.");
    System.exit(0);
  }


  public void calcObjective(){      
      boolean starting = true;
      int personNumber = 0;
      
      for(int k = 0 ; k < chromosome1.genes.length ; k ++){
          if(starting == true && chromosome1.genes[k] < length){
              //The first city in a group
              objVal += distanceToOriginal[chromosome1.genes[k]];                                  
              travelDistances[personNumber] += distanceToOriginal[chromosome1.genes[k]];
          }
          else if(chromosome1.genes[k] > length && k > 0 && chromosome1.genes[k-1] < length){
              //The last point then go back to original point.
              objVal += distanceToOriginal[chromosome1.genes[k-1]];
              travelDistances[personNumber] += distanceToOriginal[chromosome1.genes[k-1]];
              starting = true;
              personNumber ++;
              //System.out.println("\nGo back: "+chromosome1.genes[k-1]+" "+chromosome1.genes[k]);
          }
          else if(chromosome1.genes[k] > length && k > 0 && chromosome1.genes[k-1] >= length){              
              //It shows there is no city assigned to this saleman.
              //We don't apply this condition. We add a largest value to avoid this solution.
              objVal += Double.MAX_VALUE;              
              travelDistances[personNumber] += Double.MAX_VALUE;
              personNumber ++;
              //System.out.println("\niLLegal: "+chromosome1.genes[k-1]+" "+chromosome1.genes[k]);
          }          
          else if(chromosome1.genes[k] < length){
            int index1 = chromosome1.genes[k];
            int index2 = chromosome1.genes[k-1];
            objVal += distanceMatrix[index1][index2];         
            travelDistances[personNumber] += distanceMatrix[index1][index2];
            starting = false;
          }
      }
  }

  public double getObjectiveValue(){
    return objVal;
  }
  
  public double getObjectiveValue2(){//Max distance
    double maxDistance = 0;
    for(int i = 0 ; i < numberOfSalesmen ; i ++ ){
      if(maxDistance < travelDistances[i]){
        maxDistance = travelDistances[i];
      }
    }
    return maxDistance;
  }  
}