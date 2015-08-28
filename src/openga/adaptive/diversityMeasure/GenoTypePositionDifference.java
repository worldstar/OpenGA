package openga.adaptive.diversityMeasure;
import openga.chromosomes.*;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: The method is proposed by Prof. Chang (2005). </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class GenoTypePositionDifference implements diversityMeasureI{
  public GenoTypePositionDifference() {
  }

  public populationI pop;                 //population
  public int popSize, chromosomeLength;  //size of population and number of digits of chromosome
  public double genoDiversityValue = 0;

  public void setData(populationI population1){
    pop = new population();
    this.pop = population1;
    popSize = population1.getPopulationSize();
    chromosomeLength = population1.getSingleChromosome(0).genes.length;
  }

  public void startCalcDiversity(){
    genoDiversityValue = 0;
    //System.out.println("GenoTypePositionDifference "+pop.getLengthOfChromosome());
    for(int i = 0 ; i < popSize ; i ++ ){
      for(int j = i+1 ; j < popSize ; j ++ ){
        genoDiversityValue += calcValue(pop.getSingleChromosome(i), pop.getSingleChromosome(j));
      }
    }
    genoDiversityValue = genoDiversityValue/((pop.getLengthOfChromosome()+1)*pop.getLengthOfChromosome()/2*(pop.getPopulationSize()+1)*pop.getPopulationSize()/2);
  }

  public double calcValue(chromosome _chromosome1, chromosome _chromosome2){
    double val = 0;
    for(int i = 0 ; i < _chromosome1.getLength() ; i ++ ){
      val += Math.abs(i - getPosition(_chromosome1.getSolution()[i], _chromosome2));
    }
    return val;
  }

  private int getPosition(int val, chromosome _chromosome2){
    int pos = 0;
    for(int i = 0 ; i < _chromosome2.getLength() ; i ++ ){
      if(val == _chromosome2.getSolution()[i]){
        pos = i;
        break;
      }
    }
    return pos;
  }

  public double getPopulationDiversityValue(){
    return (double)genoDiversityValue;
  }

  public static void main(String[] args) {
    GenoTypePositionDifference genoTypePositionDifference1 = new GenoTypePositionDifference();
  }

}