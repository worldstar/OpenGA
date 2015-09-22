package openga.ObjectiveFunctions;
import openga.chromosomes.*;
import openga.util.algorithm.*;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: For Single Machine Problem.</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class ObjectiveEarlinessTardinessPenalty implements ObjectiveFunctionScheduleI, alphaBetaI{
  public ObjectiveEarlinessTardinessPenalty() {
  }
  populationI originalPop;
  int indexOfObjective;
  int popSize;
  public int length;
  int dueDay[];              //due day of these jobs.
  int machineTime[];         //starting time to process these jobs.
  int processingTime[];      //processing time of the job
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


  public void setScheduleData(int dueDay[], int processingTime[], int numberOfMachine){
    this.dueDay = dueDay;
    this.processingTime = processingTime;
    this.numberOfMachine = numberOfMachine;
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

    if(alpha == null){
      for(int i = 0 ; i < length ; i ++ ){
        int jobIndex = chromosome1.getSolution()[i];
        objVal += Math.abs(finishTime[jobIndex] - dueDay[jobIndex]);
      }
    }
    else{
      for(int i = 0 ; i < length ; i ++ ){
        int jobIndex = chromosome1.getSolution()[i];
        if(finishTime[jobIndex] < dueDay[jobIndex]){
          objVal += alpha[jobIndex]*(dueDay[jobIndex] - finishTime[jobIndex]);
        }
        else{
          objVal += beta[jobIndex]*(finishTime[jobIndex] - dueDay[jobIndex]);
        }
      }
    }

    return objVal;
  }

  private int[] calcFinishTime(int _seq[]){
    int finishTime[] = new int[_seq.length];
    int currentTime = 0;

    for(int i = 0 ; i < _seq.length ; i ++ ){
      finishTime[_seq[i]] = currentTime + processingTime[_seq[i]];
      currentTime = finishTime[_seq[i]];
    }
    return finishTime;
  }

  public populationI getPopulation(){
    return originalPop;
  }

  public double[] getObjectiveValues(int index){
    return originalPop.getObjectiveValues(index);
  }
}