package openga.ObjectiveFunctions;
import openga.chromosomes.*;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: For Single Machine Problem.</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class ObjectiveEarlinessForSingleMachine extends ObjectiveTardinessForSingleMachine {
  public ObjectiveEarlinessForSingleMachine() {
  }

  public void calcObjective(){
    for(int i = 0 ; i < popSize ; i ++ ){
      double originalObjValues[] = originalPop.getObjectiveValues(i);
      //write in the objective value to the variable.
      originalObjValues[indexOfObjective] = calEarliness(originalPop.getSingleChromosome(i));
      originalPop.setObjectiveValue(i, originalObjValues);
    }
  }

  private double calEarliness(chromosome chromosome1) {
    double objVal = 0;
    double machineTime = 0;
    int finishTime[] = calcFinishTime(chromosome1.genes);

    for(int i = 0 ; i < length ; i ++ ){
      int jobIndex = chromosome1.getSolution()[i];
      if(finishTime[jobIndex] < dueDay[jobIndex]){
        objVal += (dueDay[jobIndex] - finishTime[jobIndex]);
      }
    }
    return objVal;
  }

}