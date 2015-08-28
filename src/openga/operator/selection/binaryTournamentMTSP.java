package openga.operator.selection;
import openga.chromosomes.*;
/**
 *
 * @author nhu
 */
public class binaryTournamentMTSP extends binaryTournament implements SelectMTSPI{
  //The new population is created by the method.
  //Besides, the newPopulation size is equal to sizeOfPop.
  populationI newPop, originalPop;
  populationI newPop2, originalPop2;//For part II chromosomes
  populationI archieve;
  populationI archieve2;//For part II chromosomes
  int sizeOfPop;
  int originalPopSize, eliteSize = 0;
  int numberOfelitle = 0;
  int tournamentSize = 2;

  //we want to modify the new population size, we set the numberOfPop.
  public void setData(int sizeOfPop, populationI originalPop){
    this.sizeOfPop = sizeOfPop;
    originalPopSize = originalPop.getPopulationSize();
    this.originalPop = originalPop;
    newPop = new population();//to assign a memmory space to the variable.
    //set attributes because we may copy the object latter.
    newPop.setGenotypeSizeAndLength(originalPop.getEncodedType(), sizeOfPop, originalPop.getSingleChromosome(0).genes.length, originalPop.getNumberOfObjectives());
    newPop.initNewPop();

    newPop2 = new population();//to assign a memmory space to the variable.
    //set attributes because we may copy the object latter.
    newPop2.setGenotypeSizeAndLength(originalPop.getEncodedType(), sizeOfPop, originalPop.getSingleChromosome(0).genes.length, originalPop.getNumberOfObjectives());
    newPop2.initNewPop();
  }

  public void setElite(populationI archieve, int numberOfelitle){
    this.archieve = archieve;
    this.numberOfelitle = numberOfelitle;
    eliteSize = archieve.getPopulationSize();
  }

  public void setElite2(populationI archieve2, int numberOfelitle){
    this.archieve2 = archieve2;
    this.numberOfelitle = numberOfelitle;
    eliteSize = archieve2.getPopulationSize();
  }  

  public void setSecondPopulation(populationI originalPop2){
    this.originalPop2 = originalPop2;
  }


  public void startToSelect(){
    //int counter = 0;
    for(int i = 0 ; i < numberOfelitle ; i ++ ){
      int index1 = (int)(Math.random()*eliteSize);//randomly select two chromosomes to compare.
      newPop.setSingleChromosome(i, archieve.getSingleChromosome(index1));
    }

    for(int i = numberOfelitle ; i < sizeOfPop ; i ++ ){
      int index1 = (int)(Math.random()*originalPopSize);//randomly select two chromosomes to compare.
      int index2 = (int)(Math.random()*originalPopSize);
      int winIndex = CompareFitness(originalPop.getSingleChromosome(index1), originalPop.getSingleChromosome(index2), index1, index2);
      chromosome selectedChromosome1 = setData(originalPop.getSingleChromosome(index1), originalPop.getSingleChromosome(index2));
      newPop.setSingleChromosome(i, originalPop.getSingleChromosome(winIndex));
      newPop2.setSingleChromosome(i, originalPop2.getSingleChromosome(winIndex));
    }
  }

  public int CompareFitness(chromosome chromosome1, chromosome chromosome2, int index1, int index2){
    if(chromosome1.getFitnessValue() <= chromosome2.getFitnessValue()){
      return index1;
    }
    return index2;
  }

  public populationI getSelectionResult(){
    return newPop;
  }

  public populationI getPopulation(){
    return newPop;
  }
}
