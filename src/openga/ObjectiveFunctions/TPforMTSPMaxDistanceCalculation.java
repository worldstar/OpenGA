/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package openga.ObjectiveFunctions;

/**
 *
 * @author Shih-Hsin Chen
 */
public class TPforMTSPMaxDistanceCalculation extends TPforMTSPDistanceCalculation{  
    double maxDistance = 0;
    
    public void calcObjective(){
      //to get the distance of each salesmen
      int currentPosition = 0;//To record the position of the Part I chromosome            
      int numberOfCities = length - numberOfSalesmen;
      int stopPosition = chromosome1.genes[numberOfCities];//numberOfCities + currentPosition - 1; 
      double individualDistance = 0;
      
      for(int k = 0 ; k < numberOfSalesmen ; k ++){
        //from the original point to the first position.
        objVal += distanceToOriginal[chromosome1.genes[currentPosition]];       
        individualDistance = distanceToOriginal[chromosome1.genes[currentPosition]];       
        
        for(int i = currentPosition ; i < stopPosition ; i ++ ){
          if(i < stopPosition){
            int index1 = chromosome1.genes[i];
            int index2 = chromosome1.genes[i+1];
            objVal += distanceMatrix[index1][index2];   
            individualDistance += distanceMatrix[index1][index2];
          }
          else{
            //The last point then go back to original point.
            objVal += distanceToOriginal[chromosome1.genes[currentPosition]];
            individualDistance += distanceToOriginal[chromosome1.genes[currentPosition]];
          }
          currentPosition ++;
//          System.out.println(i);
        }
        stopPosition += (numberOfCities - currentPosition);
        
        if(individualDistance > maxDistance){
          maxDistance = individualDistance;
        }
      }      
    }  
    
  public double getObjectiveValue(){
    return maxDistance;//We reutnr the max distance instead.
  }    
}
