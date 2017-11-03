package openga.ObjectiveFunctions;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public interface ObjectiveFunctionOASWithTOUI extends ObjectiveFunctionI{
  public void setOASData(double[] r, double[] p, double[] d, double[] d_bar, double[] e, double[] w , double[] power, double[][] s, int numberOfSalesmen);
}