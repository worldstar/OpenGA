package openga.operator.clone;
import openga.chromosomes.*;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class solutionVectorClone extends objectiveSpaceClone {
  public solutionVectorClone() {
  }

  /**
   * @return If the objective values of the solution is the same, it return true.
   */
  public boolean checkIdenticalSoln(chromosome chromosome1, chromosome chromosome2){
    boolean identical = true;
    for(int i = 0 ; i < length ; i ++ ){
      if(chromosome1.getSolution()[i] != chromosome2.getSolution()[i]){
        identical = false;
        break;
      }
    }
    return identical;
  }

}