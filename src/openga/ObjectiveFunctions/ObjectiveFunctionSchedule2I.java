package openga.ObjectiveFunctions;

/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public interface ObjectiveFunctionSchedule2I extends ObjectiveFunctionI{
  public void setScheduleData(int processingTime_int[][], int numberOfMachine);
  public void setScheduleData(double processingTime[][], int numberOfMachine);
  public void setScheduleData(int dueDay[], int processingTime[][], int numberOfMachine);
}