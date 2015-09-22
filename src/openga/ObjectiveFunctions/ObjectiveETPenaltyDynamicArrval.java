package openga.ObjectiveFunctions;
import openga.chromosomes.*;
import openga.util.algorithm.*;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: For Single Machine Problem with Earliness and Tardiness Penalty.</p>
 * <p>Dynamic Arrival time, setup, and common due day are considered.</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Cheng Shiu University University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class ObjectiveETPenaltyDynamicArrval implements ObjectiveFunctionMatrixPTimeScheduleI, alphaBetaI{
  public ObjectiveETPenaltyDynamicArrval() {
  }
  populationI originalPop;
  int indexOfObjective;
  int popSize;
  public int length;
  int dueDay;              //Coomon due day of these jobs.
  int machineTime[];         //starting time to process these jobs.
  int processingTime[][];      //processing time of the job
  int numberOfMachine;       // the number of parallel machine
  double alpha[];
  double beta[];

  public void setData(populationI population, int indexOfObjective){
    this.originalPop = population;
    this.indexOfObjective = indexOfObjective;
    popSize = population.getPopulationSize();
    length = population.getLengthOfChromosome();
  }

  public void setData(chromosome chromosome1, int indexOfObjective){
    double originalObjValues[] = chromosome1.getObjValue();
    //write in the objective value to the variable.
    originalObjValues[indexOfObjective] = calcEarlinessAndTardiness(chromosome1);
    chromosome1.setObjValue(originalObjValues);
  }

  @Override
  public void setScheduleData(int[][] processingTime, int numberOfMachine) {    
    this.processingTime = processingTime;
    this.numberOfMachine = numberOfMachine;    
  }

  @Override
  public void setScheduleData(int[] dueDay, int[][] processingTime, int numberOfMachine) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
  
  public void setScheduleData(int dueDay[], int processingTime[], int numberOfMachine){
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  public void setScheduleData(int processingTime[], int numberOfMachine){

  }

  public void setAlphaBeta(double alpha[], double beta[]){
    this.alpha = alpha;
    this.beta = beta;
  }

  public void setLearningRate(double learningRate){

  }

  public void calcObjective(){
    for(int i = 0 ; i < popSize ; i ++ ){
      double originalObjValues[] = originalPop.getObjectiveValues(i);
      //write in the objective value to the variable.
      originalObjValues[indexOfObjective] = calcEarlinessAndTardiness(originalPop.getSingleChromosome(i));
      originalPop.setObjectiveValue(i, originalObjValues);
    }
  }

  public double calcEarlinessAndTardiness(chromosome chromosome1) {
    double objVal = 0;
    double machineTime = 0;
    int finishTime[] = calcFinishTime(chromosome1.genes);
    dueDay = calcCommonDueDay(finishTime);

    if(alpha == null){
      for(int i = 0 ; i < length ; i ++ ){
        int jobIndex = chromosome1.getSolution()[i];
        objVal += Math.abs(finishTime[jobIndex] - dueDay);
      }
    }
    else{
      for(int i = 0 ; i < length ; i ++ ){
        int jobIndex = chromosome1.getSolution()[i];
        if(finishTime[jobIndex] < dueDay){
          objVal += alpha[jobIndex]*(dueDay - finishTime[jobIndex]);
        }
        else{
          objVal += beta[jobIndex]*(finishTime[jobIndex] - dueDay);
        }
      }
    }

    return objVal;
  }

  private int[] calcFinishTime(int _seq[]){
    int finishTime[] = new int[_seq.length];
    int currentTime = 0;

    for(int i = 0 ; i < _seq.length ; i ++ ){      
      finishTime[_seq[i]] = currentTime + processingTime[_seq[i]][i];;
      currentTime = finishTime[_seq[i]];
    }
    return finishTime;
  }
  
  private int calcCommonDueDay(int finishTime[]){
    int due = 0;
    int middlePoint = finishTime.length/2;

    if(finishTime.length % 2 == 0){
      due = finishTime[middlePoint];
    }
    else{
      middlePoint = (finishTime.length + 1)/2;
      due = finishTime[middlePoint];
    }    
    return due;
  }  

  public populationI getPopulation(){
    return originalPop;
  }

  public double[] getObjectiveValues(int index){
    return originalPop.getObjectiveValues(index);
  }


}