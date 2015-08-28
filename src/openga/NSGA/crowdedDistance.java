package openga.NSGA;

import openga.util.sort.selectionSort;
/**
 * <p>Title: The OpenGA project</p>
 * <p>Description: The crowdedDistance is to maintain the diversity of pareto set.
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class crowdedDistance {
  public crowdedDistance() {
  }

  //objectives[m][n], m is number of chromosomes and n is the number of objectives .
  private double objectives[][];
  private double firstObjective[];
  private double distance[];
  int sequence[];
  int length; //number of solns

  public void setObjectives(double objectives[][]){
    this.objectives = objectives;
    length = objectives.length;
    sequence = new int[length];
    firstObjective = new double[length];
  }

  public void initDistance(){
    //length of the distance.
    distance = new double[length];

    //initialize distance, sequence, and first objective.
    for(int i = 0 ; i < length ; i ++ ){
      distance[i] = 0;
      sequence[i] = i;
      firstObjective[i] = objectives[i][0];
    }
  }

  //the main loop of ditance of each objective.
  public void mainLoop(){
    //sorting the first objective
    selectionSort selectionSort1 = new selectionSort(firstObjective);
    selectionSort1.setNomialData(sequence);
    selectionSort1.selectionSort_withNomial();
    sequence = selectionSort1.getNomialData();


    //to set the boundary points are always selected so set them into max infinite
    distance[0] = distance[length-1] = Double.POSITIVE_INFINITY;

    //to calculate others points' distance
    for(int m = 0 ; m < objectives[0].length ; m ++ ){
      for(int i = 1; i < length -1 ; i ++ ){
        int index = sequence[i];//to get original point
        int neighborhood1 = sequence[i+1];
        int neighborhood2 = sequence[i-1];
        distance[index] += (objectives[neighborhood1][m] - objectives[neighborhood2][m]);
      }
    }
  }

  public double[] getDistance(){
    return distance;
  }

  public static void main(String[] args) {
    //the following is the test data

    crowdedDistance crowdedDistance1 = new crowdedDistance();
  }

}