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

public class localSearchBy2OptForMTSP extends localSearchMTSP {
  public localSearchBy2OptForMTSP() {
  }

  double container[][];

  public void setEDAinfo(double container[][]){
    this.container = container;
  }

  public void startLocalSearch(){
    selectedIndex = getTargetChromosome();
    double originalSolnEstimatedFitness;
    double newSolnEstimatedFitness;

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
          //local search by 2-Opt (inverse mutation)
          _pop.setChromosome(1, inverseGenes(_pop.getSingleChromosome(1)));

          //Evaluate the fitness of the original solution and the new solution
          originalSolnEstimatedFitness = fitnessSurrogate(_pop.getSingleChromosome(0), i, j);
          newSolnEstimatedFitness = fitnessSurrogate(_pop.getSingleChromosome(1), i, j);
          double delta = originalSolnEstimatedFitness - newSolnEstimatedFitness;

          //if(delta <= 0 || BolzmanFunction(originalSolnEstimatedFitness - newSolnEstimatedFitness)){//The new solution is better than the original solution, we evaluate the "real fitness."
          if(delta <= 0 ){//The new solution is better than the original solution, we evaluate the "real fitness."
            evaluateNewSoln(_pop.getSingleChromosome(1));
            currentUsedSolution ++;
            //System.out.println("Within delta. ");

            boolean compareResult = getObjcomparison(_pop.getObjectiveValues(1), pop.getObjectiveValues(selectedIndex));
            if(compareResult == true){//If the new solution is better, we replace the original solution.
              pop.setChromosome(selectedIndex, _pop.getSingleChromosome(1));
              pop.setObjectiveValue(selectedIndex, _pop.getObjectiveValues(1));
              _pop.setChromosome(0, _pop.getSingleChromosome(1));
              _pop.setObjectiveValue(0, _pop.getObjectiveValues(1));
              //System.out.println("New soln found.");
            }
          }
        }
        else{
          break;
        }
      }
    }

    updateArchive(pop.getSingleChromosome(selectedIndex)); //update the solution in the elite set.
  }//end startLocalSearch

  //To estimated the fitness between cutPoint1 and cutPoint2.
  public double fitnessSurrogate(chromosome chromosomes1, int cutPoint1, int cutPoint2){
    double estimatedFitness = 0;
    for(int i = cutPoint1 ; i <= cutPoint2 ; i ++ ){
      int jobIndex = chromosomes1.getSolution()[i];
      estimatedFitness += container[jobIndex][i];
    }
    return estimatedFitness;
  }

  //to generate a probability to accept worse soln.
  final public boolean BolzmanFunction(double diff){
    double randomProbability = Math.random();
    //double BolzmanValue = Math.exp(-(Ztemp - Zcurrent)/currentTemperature);
    double BolzmanValue = Math.exp(-diff);
    //System.out.println(randomProbability+" "+BolzmanValue);

    if(randomProbability < BolzmanValue){
      return true;
    }
    else{
      return false;
    }
  }

}