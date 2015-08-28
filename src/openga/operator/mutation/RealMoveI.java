package openga.operator.mutation;

/**
 * <p>Title: The OpenGA project is a open structure of Simulated Annealing</p>
 * <p>Description: It's extended from MoveI so that it has the behavior of MoveI.
 * Moreover, because the continous problem needs boundary information, we create a method
 * so that it's able to set the best </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-HSin
 * @version 1.0
 */

public interface RealMoveI extends MutationI {
  public void setBounds(double lwBounds[], double upBound[]);
}
