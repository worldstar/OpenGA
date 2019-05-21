package openga.ObjectiveFunctions;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public interface ObjectiveFunctionOASPSDI extends ObjectiveFunctionI{
  public void setOASPSDData(double[] r, double[] p, double[] d, double[] d_bar, double[] e, double[] w , double[] power, double[][] s, int numberOfSalesmen , double PSD_b);
  public void setPowerData( double[] p , double[] power);

}