package openga.chromosomes;
import openga.util.sort.selectionSort;
import openga.NSGA.crowdedDistance;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class expandArchieve extends population {

  /**
   * @param originalPop1 is the archieve
   * @param originalPop2 is the second front of the population.
   * @param newPop is the to combined the two populations under the condition of selecting the individuals
   * whose crowding distance are higher in population 2 will be selected.
   * @param size the expected size of the new archieve.
   * @param size1 the size of archieve
   * @param size2 the size of second front.
   */
  populationI originalPop1, originalPop2, newPop;
  //size is the total size after combine the Pop1 and Pop2.
  int size, size1, size2;


 public void setTwoPopulations(populationI originalPop1, populationI originalPop2, int size){
    this.originalPop1 = originalPop1;
    this.originalPop2 = originalPop2;
    size1 = originalPop1.getPopulationSize();
    size2 = originalPop2.getPopulationSize();
    this.size = size;

    //initiate the new population
    newPop = new population();
    newPop.setGenotypeSizeAndLength(originalPop1.getEncodedType(), size,
                                    originalPop1.getSingleChromosome(0).genes.length,
                                    originalPop1.getNumberOfObjectives());
    newPop.initNewPop();
  }

  public void startToExpand(){
    writeParetoSet();

    //deal with the second front.
    //The individual has higher crowding distance will be selected.
    int sequence[] = getSelectionSequence(originalPop2);
    int counter = 0;
    for(int i = size1 ; i < size ; i ++ ){
      int selectedIndex = sequence[counter++];
      writeDataToNewPopulation(i, originalPop2.getSingleChromosome(selectedIndex));

      if(counter == size2){//reset the counter
        counter = 0;
      }
    }
  }

  private void writeParetoSet(){
    //set the pareto set first
    for(int i = 0 ; i < size1 ; i ++ ){
      writeDataToNewPopulation(i, originalPop1.getSingleChromosome(i));
    }
  }

  public void writeDataToNewPopulation(int i, chromosome chromosome1){
    newPop.setChromosome(i, chromosome1);//set chromsome
    newPop.setFitness(i, chromosome1.getFitnessValue());
    newPop.setObjectiveValue(i, chromosome1.getObjValue());
  }

  //to form a priority to form a set depended on crowding distance.
  public int[] getSelectionSequence(populationI Pop1){
    int _size = Pop1.getPopulationSize();
    int[] sequence = new int[_size];
    selectionSort selectionSort1 = new selectionSort();
    double objs[][] = new double[_size][Pop1.getNumberOfObjectives()];
    double distance[] = new double[_size];

    for(int i = 0 ; i < _size ; i ++ ){
      objs[i] = Pop1.getObjectiveValues(i);
      sequence[i] = i;
    }

    crowdedDistance crowdedDistance1 = new crowdedDistance();
    crowdedDistance1.setObjectives(objs);
    crowdedDistance1.initDistance();
    crowdedDistance1.mainLoop();
    distance = crowdedDistance1.getDistance();

    selectionSort1.setData(distance);
    selectionSort1.setNomialData(sequence);
    selectionSort1.Sort_withNomial();
    sequence = selectionSort1.getNomialData();

    return sequence;
  }


  public populationI getPopulation(){
    return newPop;
  }



  public static void main(String[] args) {
  }
}