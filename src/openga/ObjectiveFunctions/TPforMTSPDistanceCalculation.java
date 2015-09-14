/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package openga.ObjectiveFunctions;
import openga.chromosomes.*;
/**
 *
 * @author UlyssesZhang
 */
public class TPforMTSPDistanceCalculation extends forMTSPDistanceCalculation{
    
      int numberOfSalesmen;
  
    public void setData(double distanceToOriginal[], double distanceMatrix[][], 
            chromosome chromosome1, int numberOfSalesmen){
      this.distanceToOriginal = distanceToOriginal;
      this.distanceMatrix = distanceMatrix;
      this.chromosome1 = chromosome1;
      this.numberOfSalesmen = numberOfSalesmen;
      length = chromosome1.getLength();
    }
   
    
    public void calcObjective(){
      //to get the distance of each salesmen
      int currentPosition = 0;//To record the position of the Part I chromosome
      
      openga.util.printClass p1 = new openga.util.printClass();
      p1.printMatrix("C1", chromosome1.genes);
      
      int numberOfCities = length - numberOfSalesmen;
      int stopPosition = chromosome1.genes[numberOfCities];//numberOfCities + currentPosition - 1; 
      
      for(int k = 0 ; k < numberOfSalesmen ; k ++){
        //from the original point to the first position.
        objVal += distanceToOriginal[chromosome1.genes[currentPosition]];

        System.out.println("\n"
//                +"length "+length
//                +" numberOfSalesmen "+numberOfSalesmen
                +" numberOfCities "+numberOfCities
                +" stopPosition "+stopPosition
                +" currentPosition "+currentPosition);        

        for(int i = currentPosition ; i < stopPosition ; i ++ ){
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
//          System.out.println(i);
        }
        stopPosition += (numberOfCities - currentPosition);
      }
      
    }
}
