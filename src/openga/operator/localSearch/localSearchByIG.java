/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package openga.operator.localSearch;

import java.util.ArrayList;
import java.util.Random;
import openga.chromosomes.chromosome;
import openga.chromosomes.population;
import openga.chromosomes.populationI;

/**
 *
 * @author Administrator
 */
public class localSearchByIG extends localSearchBy2Opt {
  int number;
  
  public void startLocalSearch(){
    int selectedIndex = getTargetChromosome();

    populationI _pop = new population();//to store the temp chromosome
    _pop.setGenotypeSizeAndLength(pop.getEncodedType(), 2, pop.getLengthOfChromosome(),
                                  pop.getNumberOfObjectives());
    _pop.initNewPop();

    _pop.setChromosome(0, pop.getSingleChromosome(selectedIndex));//Set the original solution to the 1st chromosome.
    kickMove(_pop.getSingleChromosome(0));//To variate the current best solution.
    
    _pop.setChromosome(1, _pop.getSingleChromosome(0));
          //local search by 2-Opt (inverse mutation)
          iterateGreedyAlgorithm(_pop.getSingleChromosome(1),number);
          evaluateNewSoln(_pop.getSingleChromosome(1));
          currentUsedSolution ++;
          if(continueLocalSearch()){
            boolean compareResult = getObjcomparison(_pop.getObjectiveValues(1), pop.getObjectiveValues(selectedIndex));
            if(compareResult == true){//If the new solution is better, we replace the original solution.
            pop.setChromosome(selectedIndex, _pop.getSingleChromosome(1));
            pop.setObjectiveValue(selectedIndex, _pop.getObjectiveValues(1));
            _pop.setChromosome(0, _pop.getSingleChromosome(1));
            _pop.setObjectiveValue(0, _pop.getObjectiveValues(1));
          }
          
    }
  }
  public final chromosome iterateGreedyAlgorithm(chromosome _chromosome,int number){
    //set random
    int takeout,putin ;
    Random random = new Random();

    ArrayList savevalue = new ArrayList();
    ArrayList list = new ArrayList();
    for(int i = 0 ; i < _chromosome.genes.length ; i++)
    {
      list.add(_chromosome.genes[i]);
    }

    for(int count = 0 ; count < number ; count++ ){
    //takeout random
        takeout = random.nextInt(list.size());
    //save removed value
        savevalue.add(list.get(takeout));
    //remove random list
        list.remove(takeout);
        }
    putin = random.nextInt(list.size());
    for(int i=0 ; i < savevalue.size() ; i++)
    {
        list.add(putin,savevalue.get(i));
    }
    
    for(int i = 0 ; i < _chromosome.genes.length ; i++)
    {
      _chromosome.genes[i] = (int) list.get(i);
    }
    return _chromosome;
  }
}
