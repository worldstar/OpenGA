package openga.ObjectiveFunctions;

/**
 * <p>Title: The OpenGA project</p>
 * <p>Description: The project is to build general framework of Genetic algorithm and problem independent.</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */
public interface ObjFunctionPFSSOAWT_PSDI extends ObjFunctionPFSSOAWTI{
  void setOASData(int numberofOrder, int numberOfMachines, int[] profit, int[] di, double[] wi, int[][] processingTime, double b);
}