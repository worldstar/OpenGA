package openga.ObjectiveFunctions;
import openga.chromosomes.*;
import openga.util.algorithm.*;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: For single machine schedule with common due and setup consideration.</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class ObjectiveEiTiSetupCommonDueForSingleMachine implements ObjectiveFunctionSchedule2I{
  public ObjectiveEiTiSetupCommonDueForSingleMachine() {
  }

  populationI originalPop;
  int indexOfObjective;
  int popSize;
  public int length;
  int dueDay[];              //due day of these jobs.
  double processingTime[][];      //processing time of the job
  int numberOfMachine;       // the number of parallel machine
  //r is the middle job.
   int r = 4;


   public void setData(populationI population, int indexOfObjective){
     this.originalPop = population;
     this.indexOfObjective = indexOfObjective;
     popSize = population.getPopulationSize();
     length = population.getLengthOfChromosome();
   }
   
  /* CT 
   for resolve "ObjectiveTardinessForSingleMachine.java": C:\Users\nhu\jbproject\OpenGA\src\openga\ObjectiveFunctions\ObjectiveTardinessForSingleMachine.java:12: 
   openga.ObjectiveFunctions.ObjectiveTardinessForSingleMachine should be declared abstract; it does not define
  */
        
  public void setData(chromosome chromosome1, int indexOfObjective){
    System.out.println("ObjectiveTardinessForSingleMachine doesn't implement the setData(chromosome chromosome1, int indexOfObjective).\nThe program will exit.");
    System.exit(0);
  }
  
  
   public void setScheduleData(double processingTime[][], int numberOfMachine){
     this.processingTime = processingTime;
     this.numberOfMachine = numberOfMachine;
   }

  public void setScheduleData(int processingTime_int[][], int numberOfMachine){

  }

  public void setScheduleData(int dueDay[], int processingTime[][], int numberOfMachine){

  }

  public void calcObjective(){
    for(int i = 0 ; i < popSize ; i ++ ){
      double originalObjValues[] = originalPop.getObjectiveValues(i);
      //write in the objective value to the variable.
      originalObjValues[indexOfObjective] = calcEarlinessAndTardiness(originalPop.getSingleChromosome(i).getSolution());
      originalPop.setObjectiveValue(i, originalObjValues);
    }
  }

  public double calcEarlinessAndTardiness(int sequence[]) {
    double currentTime = 0;
    double obj = 0;
    double commonDueDate = calcCommonDueDate(sequence);

    //to calculate the Total earliness
    for(int i = 0 ; i < r ; i ++ ){
      obj += (i+1)*processingTime[sequence[i]][sequence[i+1]];
    }

    //to calculate the Total tardiness
    for(int i = r ; i < length - 1 ; i ++ ){
      obj += (length - i - 1)*processingTime[sequence[i]][sequence[i+1]];
    }
    return obj;
  }

  public double findMiddlePosition(int _seq[]){
    if(_seq.length % 2 == 0){
      r = _seq.length / 2;
    }
    else{
      r = (_seq.length+1) / 2;
    }

    if(r > 0){
      r -= 1;
    }
    return r;
  }

  public double calcCommonDueDate(int _seq[]){
    //the k is the summation processing time of the first r jobs.
    double k = 0;
    findMiddlePosition(_seq);

    for(int i = 0 ; i < r ; i ++ ){
      k +=  processingTime[_seq[i]][_seq[i+1]];
    }
    return k;
  }

  public populationI getPopulation(){
    return originalPop;
  }

  public double[] getObjectiveValues(int index){
    return originalPop.getObjectiveValues(index);
  }

}