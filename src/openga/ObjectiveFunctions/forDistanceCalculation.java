package openga.ObjectiveFunctions;
import openga.chromosomes.*;
/**
 * <p>Title: The OpenGA project</p>
 * <p>Description: The project is to build general framework of Genetic algorithm and problem independent.</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class forDistanceCalculation {
  public forDistanceCalculation() {
  }
  double distanceMatrix[][]; //to store the distance between two points
  chromosome chromosome1;    //A chromosome
  int length;                //chromosome length
  double objVal = 0;         //The traveling distance.

  public void setData(double distanceMatrix[][], chromosome chromosome1){
    this.distanceMatrix = distanceMatrix;
    this.chromosome1 = chromosome1;
    length = distanceMatrix.length;
  }

  public void setData(populationI population){
    this.distanceMatrix = distanceMatrix;
    this.chromosome1 = chromosome1;
    length = distanceMatrix.length;
  }


  public void calcObjective(){
    objVal = 0;
    System.out.println("Not suggested to be used in TSP problem. Please use forDistanceCalculation.java.");
    System.exit(0);
    /*
    System.out.println("\nlength "+length);
    openga.util.printClass p1 = new openga.util.printClass();
    p1.printMatrix("", distanceMatrix);
    p1.printMatrix("", chromosome1.genes);
    chromosome1.genes = new int[]{0,48,31,44,18,40,7,8,9,42,32,50,10,51,13,12,46,25,26,27,11,24,3,5,14,4,23,47,37,36,39,38,35,34,33,43,45,15,28,49,19,22,29,1,6,41,20,16,2,17,30,21};
    */
    int index1, index2;
    for(int i = 0 ; i < length - 1 ; i ++ ){
      index1 = chromosome1.genes[i];
      index2 = chromosome1.genes[i+1];
      objVal += distanceMatrix[index1][index2];
      //System.out.print(index1+"\t"+index2+"\t"+distanceMatrix[index1][index2]+"\n");
    }
    //The last point then go back to original point.
    index1 = chromosome1.genes[length - 1];
    index2 = chromosome1.genes[0];
    objVal += distanceMatrix[index1][index2];
    /*
    System.out.print(chromosome1.genes[length - 1]+"\t"+chromosome1.genes[0]+"\t"+distanceMatrix[chromosome1.genes[length - 1]][0]+"\n");
    System.out.println("objVal "+objVal);
    System.exit(0);
    */
  }

  public double getObjectiveValue(){
    return objVal;
  }

  public static void main(String[] args) {
    forDistanceCalculation forDistanceCalculation1 = new forDistanceCalculation();
    double coordinates[][] = new double[10][2];

    for(int i = 0 ; i < coordinates.length ; i ++ ) {
      coordinates[i][0] = (int)(Math.random()*20);
      coordinates[i][1] = (int)(Math.random()*20);
    }

    chromosome chromosome1 = new chromosome();
    chromosome1.setGenotypeAndLength(true, 10, 2);
    chromosome1.initChromosome();
    openga.ObjectiveFunctions.util.generateMatrix_EuclideanDist generateMatrix_EuclideanDist1
        = new openga.ObjectiveFunctions.util.generateMatrix_EuclideanDist();
    generateMatrix_EuclideanDist1.setData(coordinates);
    generateMatrix_EuclideanDist1.setDistanceMatrixData();
    double distanceMatrix[][] = generateMatrix_EuclideanDist1.getMatrix();
    forDistanceCalculation1.setData(distanceMatrix, chromosome1);
    forDistanceCalculation1.calcObjective();

    openga.util.printClass printClass1 = new openga.util.printClass();
    printClass1.printMatrix("chromosome1",chromosome1.genes);
    System.out.println("The objective is "+forDistanceCalculation1.objVal);
  }
}