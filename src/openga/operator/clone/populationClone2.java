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

public class populationClone2 {
  public populationClone2() {
  }
  populationI Population[];//all sub-populations
  int sizeOfPop, length, numberOfObjs;
  boolean encodeType;
  //There are cloneRation % chromosomes are clones. Others are generated randomly.
  double cloneRation = 0.2;
  int popIndex[];
  int numberOfSubPopulations;

  public void setData(populationI Population[], int popIndex[], double cloneRation, int sizeOfPop){
    this.Population = Population;
    this.sizeOfPop = sizeOfPop;
    length = Population[0].getLengthOfChromosome();
    numberOfObjs = Population[0].getNumberOfObjectives();
    encodeType = Population[0].getEncodedType();

    //numberOfClone = (int)(200*cloneRation);
    numberOfSubPopulations = Population.length;
    this.popIndex = popIndex;
    this.cloneRation = cloneRation;
    //System.out.println(cloneRation+"\t"+sizeOfPop+"\t"+numberOfClone);
  }


  public void startClonePopulation(){
    for(int i = 0 ; i < popIndex.length - 1 ; i ++ ){
      int pop1 = popIndex[i], pop2 = popIndex[i+1];
      exchangeBetweenPopulations(Population[pop1], Population[pop2]);
    }
  }

  private void exchangeBetweenPopulations(populationI population1, populationI population2){
    for(int i = 0 ; i < sizeOfPop ; i ++ ){
      double randomValue = Math.random();
      if(Math.random() < cloneRation){
        int temp[] = new int[length];
        for(int j = 0 ; j < length ; j ++ ){
          temp[j] = population1.getSingleChromosome(i).genes[j];
        }
        population1.setSingleChromosome(i, population2.getSingleChromosome(i));
        population2.getSingleChromosome(i).setSolution(temp);
      }
    }
  }



}