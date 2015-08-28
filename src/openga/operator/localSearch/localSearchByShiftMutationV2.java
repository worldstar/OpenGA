package openga.operator.localSearch;
import openga.chromosomes.*;
import openga.ObjectiveFunctions.*;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class localSearchByShiftMutationV2 extends localSearchBy2Opt {
  public localSearchByShiftMutationV2() {
  }


  public void startLocalSearch(){
    selectedIndex = getTargetChromosome();
    populationI _pop = new population();//to store the temp chromosome
    _pop.setGenotypeSizeAndLength(pop.getEncodedType(), 2, pop.getLengthOfChromosome(),
                                  pop.getNumberOfObjectives());
    _pop.initNewPop();

    _pop.setChromosome(0, pop.getSingleChromosome(selectedIndex));//Set the original solution to the 1st chromosome.
    kickMove(_pop.getSingleChromosome(0));//To variate the current best solution.

    for(int i = 0 ; i < chromosomeLength - maxNeighborhood ; i ++ ){
      for(int j = i + 1 ; j < i + maxNeighborhood ; j ++ ){
        if(continueLocalSearch()){
          cutPoint1 = i;
          cutPoint2 = j;
          _pop.setChromosome(1, _pop.getSingleChromosome(0));
          //local search by swap mutation
          _pop.setChromosome(1, shiftGenes(_pop.getSingleChromosome(1)));
          evaluateNewSoln(_pop.getSingleChromosome(1));
          currentUsedSolution ++;

          boolean compareResult = getObjcomparison(_pop.getObjectiveValues(1), pop.getObjectiveValues(selectedIndex));
          if(compareResult == true){//If the new solution is better, we replace the original solution.
            pop.setChromosome(selectedIndex, _pop.getSingleChromosome(1));
            pop.setObjectiveValue(selectedIndex, _pop.getObjectiveValues(1));
            _pop.setChromosome(0, _pop.getSingleChromosome(1));
            _pop.setObjectiveValue(0, _pop.getObjectiveValues(1));
          }
        }
        else{
          break;
        }
      }
    }

    updateArchive(pop.getSingleChromosome(selectedIndex)); //update the solution in the elite set.
  }//end startLocalSearch


  public final chromosome shiftGenes(chromosome _chromosome){
    int length = cutPoint2 - cutPoint1  + 1;
    int backupGenes[] = new int[length];
    int counter = 0;

    //store the genes at backupGenes.
    for(int i = cutPoint1 ; i <= cutPoint2 ; i ++ ){
      backupGenes[counter++] = _chromosome.genes[i];
    }

    //assgin the gene at the end of the range to the place of the range.
    _chromosome.genes[cutPoint1] = backupGenes[backupGenes.length - 1];
    counter = 0;

    //write data of backupGenes into the genes
    for(int i = cutPoint1 + 1 ; i <= cutPoint2 ; i ++){
      _chromosome.genes[i] = backupGenes[counter++];
    }
    return _chromosome;
  }


}