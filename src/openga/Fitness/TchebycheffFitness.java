package openga.Fitness;
import openga.chromosomes.*;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: Tchebycheff approach to assign fitness which depends on the reference point.</p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class TchebycheffFitness implements FitnessI {
  public TchebycheffFitness() {
  }

  populationI pop;                 //mutation on whole population
  //The obj values of these chromosomes, [chromosome, objValues]
  double objVals[];
  double fitness[];
  int popSize;
  int numberOfObjs;

  //For TchebycheffFitness
  populationI paretoSet;

  public void setData(populationI population1, int numberOfObjs){
    pop = population1;
    this.numberOfObjs = numberOfObjs;
    popSize = pop.getPopulationSize();
    double objValues[] = new double[popSize];
    for(int i = 0 ; i < popSize ; i ++ ){
      objValues[i] = pop.getObjectiveValues(i)[0];
    }
    setObjVals(objValues);
  }

  public void setParetoData(populationI paretoSet){
    this.paretoSet = paretoSet;
  }

  //set the data and initialize flag.
  public void setObjVals(double _objVals[]){
    this.objVals = _objVals;
    popSize = _objVals.length;
    fitness = new double[popSize];
  }

  public void calculateFitness(){
    getReferencePointClass getReferencePointClass1 = new getReferencePointClass();
    getReferencePointClass1.setData(paretoSet);
    getReferencePointClass1.calcReferencePoint();
    double tempRefPoint[] = getReferencePointClass1.getReferencePoint();

    euclideanDistance euclideanDistance1 = new euclideanDistance();

    //assign each fitness by obj/totalObjectiveValues
    for(int i = 0 ; i < popSize ; i ++ ){
      double maxTemp = 0;
      for(int j = 0 ; j < pop.getNumberOfObjectives() ; j ++ ){
        euclideanDistance1.setXY(tempRefPoint, pop.getObjectiveValueArray()[i]);
        double dimDistMax = euclideanDistance1.getDistance(j);
        if(maxTemp < dimDistMax){
          maxTemp = dimDistMax;
        }
      }
      fitness[i] = maxTemp;
      pop.setFitness(i, fitness[i]);
    }
  }//end startToFind()


  public populationI getPopulation(){
    return pop;
  }

  class getReferencePointClass{
    public getReferencePointClass(){}

    double referencePoint[];
    populationI paretoSet;
    int popSize;

    public void setData(populationI paretoSet){
      this.paretoSet = paretoSet;
      popSize = paretoSet.getPopulationSize();
      referencePoint = new double[paretoSet.getNumberOfObjectives()];
    }

    public void calcReferencePoint(){
      for(int i = 0 ; i < paretoSet.getNumberOfObjectives() ; i ++ ){
        double minTemp = Double.MAX_VALUE;
        for(int j = 0 ; j < popSize ; j ++ ){
          if(minTemp > paretoSet.getSingleChromosome(i).getObjValue()[j]){
            minTemp = paretoSet.getSingleChromosome(i).getObjValue()[j];
          }
        }
        referencePoint[i] = minTemp;
      }
    }

    public double[] getReferencePoint(){
      return referencePoint;
    }
  }

  //Euclidean distance
  public class euclideanDistance{
    //the coordinate of two points, a and b
    double a[];
    double b[];

    public euclideanDistance() {
    }

    public euclideanDistance(double x[], double y[]) {
      a = x;
      b = y;
    }

    public void setXY(double x[], double y[]) {
      a = x;
      b = y;
    }

    public double getDistance() {//total distance
      double dist = 0.0;

      for(int i = 0 ; i < a.length ; i ++ ){
        dist += Math.pow( (a[i] - b[i]), 2);
      }

      dist = Math.sqrt(dist);
      return dist;
    }

    public double getDistance(int index) {//only one-dimension
      double dist = 0.0;

      dist += Math.pow( a[index] - b[index], 2);

      dist = Math.sqrt(dist);
      return dist;
    }
  }
}