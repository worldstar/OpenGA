package openga.ObjectiveFunctions;
import openga.chromosomes.*;
import openga.ObjectiveFunctions.util.euclideanDistance;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class forExtremeSolnDistance implements ObjectiveFunctionI{
  public forExtremeSolnDistance() {
  }

  populationI archieve;
  double distance[];
  double extremeSolns[][];
  populationI originalPop;
  int sizeOfPop;
  int originalPopSize;
  int numberOfObjs;

  public void setElite(populationI archieve){
    this.archieve = archieve;
  }

  public void setData(populationI population, int indexOfObjective){
    originalPopSize = population.getPopulationSize();
    this.originalPop = population;
    numberOfObjs = population.getNumberOfObjectives();
  }

  public void setData(chromosome chromosome1, int indexOfObjective){
    System.out.println("The setData(chromosome chromosome1) doesn't be implements.");
    System.out.println("The program is exit.");
    System.exit(0);
  }

  public void calcObjective(){
    getExtremeSolutions();
    distance = new double[originalPopSize];
    double dist1, dist2;
    euclideanDistance euclideanDistance1 = new euclideanDistance();

    for(int i = 0 ; i < originalPopSize ; i ++ ){
      euclideanDistance1.setXY(originalPop.getObjectiveValues(i), extremeSolns[0]);
      dist1 = euclideanDistance1.getDistance();

      euclideanDistance1.setXY(originalPop.getObjectiveValues(i), extremeSolns[1]);
      dist2 = euclideanDistance1.getDistance();

      if(dist1 < dist2){
        distance[i] = dist1;
      }
      else{
        distance[i] = dist2;
      }
    }
  }

  private void getExtremeSolutions(){
    double archiveObjs[][] = archieve.getObjectiveValueArray();
    extremeSolns = new double[numberOfObjs][numberOfObjs];

    for(int i = 0 ; i < numberOfObjs ; i ++ ){
      int index1 = getColumnMin(archiveObjs, i);
      for(int j = 0 ; j < numberOfObjs ; j ++ ){
        extremeSolns[i][j] = archiveObjs[index1][j];
      }
    }

    for(int i = 0 ; i < numberOfObjs ; i ++ ){
      if(i == 0){
        extremeSolns[i][0] *= 0.9;
      }
      else{
        extremeSolns[i][1] *= 0.9;
      }
    }
  }

  private int getColumnMin(double array[][], int columnIndex){
    int index = 0;
    double min = Double.MAX_VALUE;

    for(int i = 0 ; i < array.length ; i ++ ){
      if(array[i][columnIndex] < min){
        min = array[i][columnIndex];
        index = i;
      }
    }
    return index;
  }

  public populationI getPopulation(){
    return originalPop;
  }

  public double[] getObjectiveValues(int index){
    return distance;
  }

}