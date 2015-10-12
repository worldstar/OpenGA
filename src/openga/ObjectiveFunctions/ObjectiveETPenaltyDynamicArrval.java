package openga.ObjectiveFunctions;
import java.util.Arrays;
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

public class ObjectiveETPenaltyDynamicArrval implements ObjectiveFunctionMatrixPTimeScheduleI, alphaBetaI, dynamicArrivalTimeI{
  public ObjectiveETPenaltyDynamicArrval() {
    
  }
  populationI originalPop;
  int indexOfObjective;
  int popSize;
  public int length;
  int dueDay;              //Coomon due day of these jobs.
  int machineTime[];         //starting time to process these jobs.
  int processingTime[];      //processing time of the job
  int setupTime[][];         //setup time of the job
  int numberOfMachine;       // the number of parallel machine
  double alpha[];
  double beta[];
  int dynamicArrivalTime[];  //Dynamic Arrival Time of each job.

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
  public void setScheduleData(int[][] setupTime, int numberOfMachine) {    
    this.setupTime = setupTime;
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
    this.processingTime = processingTime;
    this.numberOfMachine = numberOfMachine;  
  }

  public void setAlphaBeta(double alpha[], double beta[]){
    this.alpha = alpha;
    this.beta = beta;
  }

  public void setLearningRate(double learningRate){

  }
  
  @Override
  public void setDynamicArrivalTime(int[] dynamicArrivalTime) {
    this.dynamicArrivalTime = dynamicArrivalTime;
  }

  @Override
  public void setDynamicArrivalTime(int[][] dynamicArrivalTime) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }  

  public void calcObjective(){
    for(int i = 0 ; i < popSize ; i ++ ){
      double originalObjValues[] = originalPop.getObjectiveValues(i);
      //write in the objective value to the variable.                  
      //int _seq2[] = new int[]{9, 0, 7, 1, 2, 4, 3, 6, 8, 5 };
      //originalPop.getSingleChromosome(i).genes = _seq2;
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
    
    //openga.util.printClass printClass1 = new openga.util.printClass();
    //printClass1.printMatrix("dynamicArrivalTime", dynamicArrivalTime);
    //printClass1.printMatrix("finishTime", finishTime);
    
    //System.out.println(objVal);
    //System.exit(0);
    return objVal;
  }

  private int[] calcFinishTime(int _seq[]){
    int finishTime[] = new int[_seq.length];
    //The finish time of prior job and it is the starting time of the next job.
    int currentTime = 0;

    for(int i = 0 ; i < _seq.length ; i ++ ){      
      if(currentTime < dynamicArrivalTime[_seq[i]]){//To check the arrival time.
        currentTime = dynamicArrivalTime[_seq[i]];
      }
      
      if(i == 0){//The first job doesn't has the setup time.
        finishTime[_seq[i]] = currentTime + processingTime[_seq[i]];
      }
      else{//The starting time of this job + processing time + setup time
        finishTime[_seq[i]] = currentTime + processingTime[_seq[i]] + setupTime[_seq[i-1]][_seq[i]];
      }
      
      //System.out.println(_seq[i]+": "+finishTime[_seq[i]]);
      
      currentTime = finishTime[_seq[i]];
    }
      
    return finishTime;
  }
  
  private int calcCommonDueDay(int finishTime[]){
    int due = 0;
    int middlePoint = finishTime.length/2;    
    int tempFinishTime[] = finishTime.clone();
    Arrays.sort(tempFinishTime);            

    if(tempFinishTime.length % 2 == 0){
      due = tempFinishTime[middlePoint];
    }
    else{
      middlePoint = (tempFinishTime.length + 1)/2;
      due = tempFinishTime[middlePoint];
    }    
    //System.out.println("\ndue: "+due);
    return due;
  }  

  public populationI getPopulation(){
    return originalPop;
  }

  public double[] getObjectiveValues(int index){
    return originalPop.getObjectiveValues(index);
  }
  
  public static void main(String[] args) {
    
  }
}