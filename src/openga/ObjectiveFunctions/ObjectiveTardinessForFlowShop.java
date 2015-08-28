package openga.ObjectiveFunctions;

/**
 * <p>Title: The OpenGA project</p>
 * <p>Description: The project is to build general framework of Genetic algorithm and problem independent.</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class ObjectiveTardinessForFlowShop extends ObjectiveTardiness implements ObjectiveFunctionFlowShopScheduleI{
  public ObjectiveTardinessForFlowShop() {
  }

  /**
   * processing time of the job, it override its superclass of parallel machine.
   * It's because the processing time on different machines are different in the flow shop problem.
   * However, in the parallel machine, the processing time is identical.
   * So there is a little bit modification on the processing time and the latter calculateObjective().
   */
  int processingTime[][];

  public void setScheduleData(int processingTime[][], int numberOfMachine){
  }

  public void setScheduleData(int dueDay[], int processingTime[][], int numberOfMachine){
    this.dueDay = dueDay;
    this.processingTime = processingTime;
    this.numberOfMachine = numberOfMachine;
  }


  public void calcObjective(){
    for(int i = 0 ; i < popSize ; i ++ ){
      forScheduleMaxTardinessForFlowShop forScheduleTardiness1 = new forScheduleMaxTardinessForFlowShop();
      forScheduleTardiness1.setScheduleData(dueDay, processingTime, numberOfMachine);
      forScheduleTardiness1.setData(originalPop.getSingleChromosome(i));
      forScheduleTardiness1.calcObjectives();
      double originalObjValues[] = originalPop.getObjectiveValues(i);
      //write in the objective value to the variable.
      originalObjValues[indexOfObjective] = forScheduleTardiness1.getObjectiveValue();
      originalPop.setObjectiveValue(i, originalObjValues);
    }
  }

}