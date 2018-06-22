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

public class ObjectiveMakeSpanForFlowShopPSD extends ObjectiveMakeSpan implements ObjectiveFunctionFlowShopScheduleI{
  public ObjectiveMakeSpanForFlowShopPSD() {
  }
  /**
   * processing time of the job, it override its superclass of parallel machine.
   * It's because the processing time on different machines are different in the flow shop problem.
   * However, in the parallel machine, the processing time is identical.
   * So there is a little bit modification on the processing time and the latter calculateObjective().
   */
  int processingTime[][];
  forScheduleMakespanFlowShop forScheduleMakespan1 = new forScheduleMakespanFlowShop();

  public void setScheduleData(int processingTime[][], int numberOfMachine){
    this.processingTime = processingTime;
    this.numberOfMachine = numberOfMachine;
  }

  public void setScheduleData(int dueDay[], int processingTime[][], int numberOfMachine){

  }

  public void setData(chromosome chromosome1, int indexOfObjective){
    forScheduleMakespan1.setScheduleData(processingTime, numberOfMachine);
    forScheduleMakespan1.setData(chromosome1);
    forScheduleMakespan1.calcObjectives();
    double originalObjValues[] = chromosome1.getObjValue();
    originalObjValues[indexOfObjective] = forScheduleMakespan1.getObjectiveValue();
    chromosome1.setObjValue(originalObjValues);
  }


  public void calcObjective(){
    for(int i = 0 ; i < popSize ; i ++ ){
      setData(originalPop.getSingleChromosome(i), indexOfObjective);
    }
  }

  public static void main(String[] args) {
    ObjectiveMakeSpanForFlowShopPSD objectiveMakeSpanForFlowShop1 = new ObjectiveMakeSpanForFlowShopPSD();
  }

}