package openga.operator.repair;

/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: Lamarckian and Baldwinian repaire scheme can be applied here.</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public interface repairScheme{
  public void setData();
  public void startRepair();
}