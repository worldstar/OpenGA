package openga.ObjectiveFunctions;
import openga.chromosomes.*;
/**
 * <p>Title: The OpenGA project</p>
 * <p>Description: The project is to build general framework of Genetic algorithm and problem independent.</p>
 * <P>The program will obtain objective values and then normalize it.</P>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class ObjectiveWeightSumForSchedule implements weightedSumObjectives, ObjectiveFunctionScheduleI{
  public ObjectiveWeightSumForSchedule() {
  }

  populationI originalPop;
  int indexOfObjective;
  int popSize;
  int machineTime[];         //starting time to process these jobs.
  int processingTime[];      //processing time of the job
  int numberOfMachine;       // the number of parallel machine
  double weights[];          //the weight of each objectives.
  //the objective classes
  ObjectiveFunctionI ObjectiveFunction[];

  public void setData(populationI population, int indexOfObjective){
    this.originalPop = population;
    this.indexOfObjective = indexOfObjective;
    popSize = population.getPopulationSize();
  }

  public void setData(chromosome chromosome1, int indexOfObjective){
    System.out.println("The setData(chromosome chromosome1) doesn't be implements.");
    System.out.println("The program is exit.");
    System.exit(0);
  }

  public void setScheduleData(int processingTime[], int numberOfMachine){
    this.processingTime = processingTime;
    this.numberOfMachine = numberOfMachine;
  }

  public void setScheduleData(int dueDay[], int processingTime[], int numberOfMachine){
  }

  public void setObjectiveClass(ObjectiveFunctionI ObjectiveFunction[]){
    this.ObjectiveFunction = ObjectiveFunction;
  }

  public void setWeightOfObjectives(double weights[]){
    this.weights = weights;
  }

  public void calcObjective(){
    //evaluate each objective function
    for(int i = 0 ; i < ObjectiveFunction.length ; i ++ ){
      //System.out.println("The obj "+i);
      ObjectiveFunction[i].setData(originalPop, i);
      ObjectiveFunction[i].calcObjective();
      originalPop = ObjectiveFunction[i].getPopulation();
    }

    //to get the objective values.
    int _length = popSize;
    double _values[][] = new double[ObjectiveFunction.length][_length];
    double _normObjectives[][] = new double[ObjectiveFunction.length][_length];

    //get the original objective values
    for(int i = 0 ; i < ObjectiveFunction.length ; i ++ ){
      for(int j = 0 ; j < popSize ; j ++ ){
        _values[i][j] = originalPop.getObjectiveValues(j)[i];
      }
    }
    //normalize data of each objective value
    openga.util.algorithm.normalize normalize1 = new openga.util.algorithm.normalize();
    _normObjectives = normalize1.getData2EachRow(_values);

    //set the result to each chromosome.
    for(int j = 0 ; j < popSize ; j ++ ){
      double tempValue[] = new double[ObjectiveFunction.length];
      double scalarizedValue = 0;
      for(int k = 0 ; k < ObjectiveFunction.length ; k ++ ){
        tempValue[k] = _normObjectives[k][j];
        scalarizedValue += tempValue[k]*weights[k];
      }
      originalPop.setNormalizedObjValue(j, tempValue);
      originalPop.setScalarizedObjectiveValue(j, scalarizedValue);
    }
  }

  public populationI getPopulation(){
    return originalPop;
  }

  public double[] getObjectiveValues(int index){
    return originalPop.getObjectiveValues(index);
  }
}