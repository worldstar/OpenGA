package openga.operator.clone;
import openga.chromosomes.*;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: To eliminate the identical solution by its objective values.</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class objectiveSpaceClone implements cloneI{
  public objectiveSpaceClone() {
  }

  populationI originalPop;
  int sizeOfPop, length, numberOfObjs;
  int numberOfOverlapped = 0;

  public void setData(populationI originalPop){
    this.originalPop = originalPop;
    sizeOfPop = originalPop.getPopulationSize();
    length = originalPop.getLengthOfChromosome();
    numberOfObjs = originalPop.getNumberOfObjectives();
  }

  public void setArchive(populationI archive){

  }

  public chromosome generateNewSolution(chromosome chromosome1){
    return null;
  }

  public void startToClone(){
    numberOfOverlapped = 0;
    for(int i = 0 ; i < sizeOfPop - 1 ; i ++ ){
      for(int j = i + 1; j < sizeOfPop ; j ++ ){
        if(checkIdenticalSoln(originalPop.getSingleChromosome(i), originalPop.getSingleChromosome(j))){
          originalPop.setSingleChromosome(j, generateNewSolution());
          numberOfOverlapped ++;
        }
      }
    }
  }

  /**
   * @return If the objective values of the solution is the same, it return true.
   */
  public boolean checkIdenticalSoln(chromosome chromosome1, chromosome chromosome2){
    boolean identical = true;
    for(int i = 0 ; i < numberOfObjs ; i ++ ){
      if(chromosome1.getObjValue()[i] != chromosome2.getObjValue()[i]){
        identical = false;
        break;
      }
    }
    return identical;
  }

  /**
   * @return To generate a random solution.
   */
  public chromosome generateNewSolution(){
    chromosome chromosome1 = new chromosome();
    chromosome1.setGenotypeAndLength(originalPop.getEncodedType(),
       originalPop.getLengthOfChromosome(), originalPop.getNumberOfObjectives());
    chromosome1.generateSequentialPop(length);
    return chromosome1;
  }

  public int getNumberOfOverlappedSoln(){
    return numberOfOverlapped;
  }

  public populationI getPopulation(){
    return originalPop;
  }

}