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

public class localSearchBySwap extends localSearchBy2Opt {
  public localSearchBySwap() {
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
          _pop.setChromosome(1, swapGenes(_pop.getSingleChromosome(1)));
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

  public final chromosome swapGenes(chromosome _chromosome){
    int backupGenes = _chromosome.genes[cutPoint1];
    _chromosome.genes[cutPoint1] = _chromosome.genes[cutPoint2];
    _chromosome.genes[cutPoint2] = backupGenes;
    return _chromosome;
  }

}