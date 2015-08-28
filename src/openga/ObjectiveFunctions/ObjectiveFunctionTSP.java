package openga.ObjectiveFunctions;
import openga.chromosomes.*;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class ObjectiveFunctionTSP implements ObjectiveFunctionTSPI{
  public ObjectiveFunctionTSP() {
  }
  populationI population;
  double coordinates[][];
  double originalPoint[];
  int length, indexOfObjective;
  double objectiveValue = 0;
  double distanceMatrix[][];
  double distanceToOriginal[];


  public void setTSPData(double originalPoint[], double coordinates[][]){
    this.originalPoint = originalPoint;
    this.coordinates = coordinates;
    openga.ObjectiveFunctions.util.generateMatrix_EuclideanDist generateMatrix_EuclideanDist1
        = new openga.ObjectiveFunctions.util.generateMatrix_EuclideanDist();
    generateMatrix_EuclideanDist1.setData(coordinates);
    generateMatrix_EuclideanDist1.setDistanceMatrixData();
    distanceMatrix = generateMatrix_EuclideanDist1.getMatrix();

    openga.ObjectiveFunctions.util.generateMatrix_EuclideanDist2 generateMatrix_EuclideanDist21
        = new openga.ObjectiveFunctions.util.generateMatrix_EuclideanDist2();
    generateMatrix_EuclideanDist21.setData(originalPoint, coordinates);
    generateMatrix_EuclideanDist21.setDistanceMatrixData();
    distanceToOriginal = generateMatrix_EuclideanDist21.getMatrix2();
    //openga.util.printClass printClass1 = new openga.util.printClass();
    //printClass1.printMatrix("distanceToOriginal", distanceToOriginal);
  }

  public void setTSPData(double originalPoint[], double coordinates[][], populationI PartIIChromosomes){
      System.out.println("openga.ObjectiveFunctions.ObjectiveFunctionTSP does support the setTSPData(double originalPoint[], double coordinates[][], populationI PartIIChromosomes).");
      System.out.println("The program will exit");
      System.exit(0);
  }

  public void setTSPData(double originalPoint[], double coordinates[][], int numberOfSalesmen){
      System.out.println("openga.ObjectiveFunctions.ObjectiveFunctionTSP does support the setTSPData(double originalPoint[], double coordinates[][], populationI PartIIChromosomes).");
      System.out.println("The program will exit");
      System.exit(0);
  }

  public void setData(populationI population, int indexOfObjective) {
    this.population = population;
    this.length = population.getLengthOfChromosome();
    this.indexOfObjective = indexOfObjective;
  }

  public void setData(chromosome chromosome1, int indexOfObjective) {
    System.out.println("The openga.ObjectiveFunctions.ObjectiveFunctionTSP doesn't support the setData(chromosome chromosome1, int indexOfObjective).");
    System.out.println("The program will exit.");
    System.exit(0);
  }

  public void calcObjective() {
    double obj;
    double objectives[];

    for(int i = 0 ; i < population.getPopulationSize() ; i ++ ){
      objectives = population.getObjectiveValues(i);
      obj = evaluateAll(population.getSingleChromosome(i));
      objectives[indexOfObjective] = obj;
      population.setObjectiveValue(i, objectives);
    }
  }

  public double evaluateAll(chromosome _chromosome1){
    forDistanceCalculation2 forDistanceCalculation1 = new forDistanceCalculation2();
    forDistanceCalculation1.setData(distanceToOriginal, distanceMatrix, _chromosome1);
    forDistanceCalculation1.calcObjective();
    return forDistanceCalculation1.getObjectiveValue();
  }

  public populationI getPopulation() {
    return population;
  }

  public double[] getObjectiveValues(int index) {
    return population.getObjectiveValues(index);
  }
}