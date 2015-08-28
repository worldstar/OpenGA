/*
 * The objective function for the MTSP which employs the one-chromosome encoding.
 * The segmentation notation is -1.
 */

package openga.ObjectiveFunctions;
import openga.chromosomes.*;
/**
 *
 * @author nhu
 */
public class forMTSPDistanceCalcOneChromosome extends forDistanceCalculation2{
  double distanceToOriginal[];

  double distanceMatrix[][]; //to store the distance between two points
  chromosome chromosome1;    //A chromosome
  int numberOfSalesmen;                //chromosome length
  double objVal = 0;         //The traveling distance.

  
  public void setData(double distanceToOriginal[], double distanceMatrix[][], chromosome chromosome1, int numberOfSalesmen){
    this.distanceToOriginal = distanceToOriginal;
    this.distanceMatrix = distanceMatrix;
    this.chromosome1 = chromosome1;
    this.numberOfSalesmen = numberOfSalesmen;
  }

  public void setData(populationI population){
    System.out.println("Call to a function setData(populationI population) that is not supported at forMTSPDistanceCalculation.java.");
    System.exit(0);
  }


  public void calcObjective(){      
      boolean starting = true;
      
      for(int k = 0 ; k < chromosome1.genes.length ; k ++){
          if(starting == true && chromosome1.genes[k] != -1){
              //The first city in a group
              objVal += distanceToOriginal[chromosome1.genes[k]];
          }
          else if(chromosome1.genes[k] == -1 && k > 0 && chromosome1.genes[k-1] != -1){
              //The last point then go back to original point.
              objVal += distanceToOriginal[chromosome1.genes[k-1]];
              starting = true;
          }
          else if(chromosome1.genes[k] == -1 && k > 0 && chromosome1.genes[k-1] == -1){              
              //We don't apply this condition. We add a largest value to avoid this solution.
              objVal += Double.MAX_VALUE;              
          }          
          else if(chromosome1.genes[k] != -1){
            int index1 = chromosome1.genes[k];
            int index2 = chromosome1.genes[k-1];
            objVal += distanceMatrix[index1][index2];             
            starting = false;
          }
      }
  }

  public double getObjectiveValue(){
    return objVal;
  }
}

