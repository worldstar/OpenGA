/*
 * It calculates the mTSP objective values which employs the minimum loading heuristic to assign the n cities to m salesmen.
 * Step 1: Assign the first m cities in the sequence directly.
 * Step 2: Assign the first un-assigned city in the sequence to a salesman which is depeneded on the minimum loading.
 * Step 3: If there is one or more un-assigned cities, go to Step 2.
 */

package openga.ObjectiveFunctions;

import openga.chromosomes.chromosome;
import openga.chromosomes.populationI;

/**
 *
 * @author chuan
 */
public class forMTSP_AssignmentHeuristic_DistanceCalculation extends forDistanceCalculation2{
  double distanceToOriginal[];

  double distanceMatrix[][]; //to store the distance between two points
  chromosome chromosome1;    //A chromosome
  int length;                //chromosome length
  double objVal = 0;         //The traveling distance.
  
  int numberOfSalesmen = 2;
  int tempAssigments[][];
  int capacity[];            //The number of cities visited by a salesman.
  int travelDistances[];      //The total travel distances of each salesman.

  public void setData(double distanceToOriginal[], double distanceMatrix[][], chromosome chromosome1, int numberOfSalesmen){
    this.distanceToOriginal = distanceToOriginal;
    this.distanceMatrix = distanceMatrix;
    this.chromosome1 = chromosome1;
    length = distanceMatrix.length;
    this.numberOfSalesmen = numberOfSalesmen;

    tempAssigments = new int[numberOfSalesmen][length];
    travelDistances = new int[numberOfSalesmen];
    capacity = new int[numberOfSalesmen];
    initTempAssigments();
  }

  public void initTempAssigments(){
    for(int i = 0 ; i < numberOfSalesmen ; i ++ ){
        for(int j = 0 ; j < length ; j ++ ){
            tempAssigments[i][j] = -1;
        }
    }
  }

  public void calcObjective(){
    //Assign the first location to each salesman. It is from the original point to the first position.
    for(int i = 0 ; i < numberOfSalesmen ; i ++ ){
      tempAssigments[i][0] = chromosome1.genes[i];
      travelDistances[i] += distanceToOriginal[chromosome1.genes[i]];
      //objVal += distanceToOriginal[chromosome1.genes[i]];
      capacity[i] = 1;
    }

    for(int i = numberOfSalesmen ; i < length ; i ++ ){
      int cityIndex = chromosome1.genes[i];
      int personNumber = getDataIndex(cityIndex);
      int position = capacity[personNumber] - 1;
      int preCity = tempAssigments[personNumber][position];

      tempAssigments[personNumber][position + 1] = chromosome1.genes[i];
      
      travelDistances[i] += distanceMatrix[preCity][cityIndex];
      capacity[personNumber] += 1;
    }
  }

  //To find out the minimum traveling distance when the new city is assigned to a salesman.
  public int getDataIndex(int cityIndex){
    int selectedIndex = 0;
    double distanceValue = Double.MAX_VALUE;

    for(int i = 0 ; i < numberOfSalesmen ; i ++ ){
      int lastIndex = capacity[i] - 1;
      int index1 = tempAssigments[i][lastIndex];
      if(distanceValue > travelDistances[i] + distanceMatrix[index1][cityIndex]){
        distanceValue = travelDistances[i] + distanceMatrix[index1][cityIndex];
        selectedIndex = i;
      }
    }    
    return selectedIndex;
  }

  public double getObjectiveValue(){
    double totalDistances = 0;
    for(int i = 0 ; i < numberOfSalesmen ; i ++ ){
      int pos = capacity[i] - 1;
      int cityIndex = this.tempAssigments[i][pos];
      totalDistances += travelDistances[i] + distanceToOriginal[cityIndex];
    }
    return totalDistances;
  }

  public double getObjectiveValue2(){//Max distance
    double maxDistance = Double.MAX_VALUE;
    for(int i = 0 ; i < numberOfSalesmen ; i ++ ){
      int pos = capacity[i] - 1;
      int cityIndex = this.tempAssigments[i][pos];

      if(maxDistance > travelDistances[i] + distanceToOriginal[cityIndex]){
        maxDistance = travelDistances[i] + distanceToOriginal[cityIndex];
      }
    }
    return maxDistance;
  }
}
