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
  
  double travelDistances[];      //The total travel distances of each salesman.
  
  public void setData(double distanceToOriginal[], double distanceMatrix[][], chromosome chromosome1, int numberOfSalesmen){
    this.distanceToOriginal = distanceToOriginal;
    this.distanceMatrix = distanceMatrix;
    this.chromosome1 = chromosome1;
    this.numberOfSalesmen = numberOfSalesmen;
    travelDistances = new double[numberOfSalesmen];
    length = chromosome1.getLength() - (numberOfSalesmen - 1);
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
              //System.out.println("Total gene length: "+chromosome1.getLength()+", City length: "+length+", salesmen: "+numberOfSalesmen+", gene value: "+chromosome1.genes[k]+". starting");
              //The first city in a group
              objVal += distanceToOriginal[chromosome1.genes[k]];
              travelDistances[personNumber] += distanceToOriginal[chromosome1.genes[k]];
              starting = false;
          }
          else if((k == 0 && chromosome1.genes[k] >= length) || (k == chromosome1.getLength() - 1 && chromosome1.genes[k] >= length && personNumber < numberOfSalesmen) ||
                  (chromosome1.genes[k] >= length && k > 0 && chromosome1.genes[k-1] >= length)){    
              //System.out.println("Total gene length: "+chromosome1.getLength()+", City length: "+length+", salesmen: "+numberOfSalesmen+", gene value: "+chromosome1.genes[k]+". forbid");
              //It shows there is no city assigned to this saleman.
              //We don't apply this condition. We add a largest value to avoid this solution.
              objVal += Double.MAX_VALUE;              
              travelDistances[personNumber] += Double.MAX_VALUE;
              personNumber ++;
          }          
          else if(chromosome1.genes[k] >= length && k > 0 && chromosome1.genes[k-1] < length){
              //System.out.println("Total gene length: "+chromosome1.getLength()+", City length: "+length+", salesmen: "+numberOfSalesmen+", gene value: "+chromosome1.genes[k]+". ending");
              //The last point then go back to original point.
              objVal += distanceToOriginal[chromosome1.genes[k-1]];
              travelDistances[personNumber] += distanceToOriginal[chromosome1.genes[k-1]];
              starting = true;
              personNumber ++;
          }          
          else if(chromosome1.genes[k] < length && chromosome1.genes[k-1] < length){
              //System.out.println("Total gene length: "+chromosome1.getLength()+", City length: "+length+", salesmen: "+numberOfSalesmen+", gene value: "+chromosome1.genes[k]+". s");
            int index1 = chromosome1.genes[k-1];
            int index2 = chromosome1.genes[k];
            objVal += distanceMatrix[index1][index2];         
            travelDistances[personNumber] += distanceMatrix[index1][index2];            
          }
      }
      
      //The return distance for the last city which doesn't mark the ending for the last salesman.
      int lastGenePosition = chromosome1.getLength() - 1;       
      if(chromosome1.genes[lastGenePosition] < length){
           objVal += distanceToOriginal[chromosome1.genes[lastGenePosition]];
           travelDistances[personNumber] += distanceToOriginal[chromosome1.genes[lastGenePosition]];   
           //System.out.println("The distance between the city "+chromosome1.genes[lastGenePosition] +" to the depot is added.");
       }
      
       //System.out.println("");
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