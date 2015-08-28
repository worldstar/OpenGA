package openga.adaptive.diversityMeasure;
import openga.chromosomes.*;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: The method is from Zhu, K. (2002), A diversity-controlling adaptive genetic algorithm for the vehicle routing problem with time window.</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class HammingDistance extends GenoTypePositionDifference{
  public HammingDistance() {
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
    for(int i = 0 ; i < popSize ; i ++ ){
      for(int j = i+1 ; j < popSize ; j ++ ){
        genoDiversityValue += calcValue(pop.getSingleChromosome(i), pop.getSingleChromosome(j));
      }
    }
    genoDiversityValue = genoDiversityValue/((1 + pop.getLengthOfChromosome())*pop.getLengthOfChromosome()*0.333*(pop.getPopulationSize())*pop.getPopulationSize()/2);
    //System.out.println(genoDiversityValue);
  }

  public double calcValue(chromosome _chromosome1, chromosome _chromosome2){
    double val = 0;
    for(int i = 0 ; i < _chromosome1.getLength() ; i ++ ){
      val += Math.abs(_chromosome1.getSolution()[i] - _chromosome2.getSolution()[i]);
    }

    return val;
  }

  public double getPopulationDiversityValue(){
    return (double)genoDiversityValue;
  }
}