package openga.adaptive.strategy;
import openga.chromosomes.*;
import openga.adaptive.diversityMeasure.*;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: The method is from Zhu, K (2003), population diversity in permutation-based genetic algorithm.</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class KennyZhu2 extends KennyZhu1 {
  public KennyZhu2() {
  }

  private populationI pop;                 //population
  private int popSize, chromosomeLength;  //size of population and number of digits of chromosome

  private diversityMeasureI diversityMeasure1;//the program is to calculate the diversity of a population.
  private double genoDiversityValue = 0;
  private double expectedDiversityValue = 0.5;
  private double defaultExpectedDiversityValue = 0.5;
  private double currentRate;//the current probability of crossover or mutation rate.
  private double updatedRate;//the new rate for crossover or mutation.
  private double tuneParameter = 0.01;
  double upperBound, lowerBound;//the upper bound and lower bound of crossover or mutation probability.

  public void setBound(double upperBound, double lowerBound){
    this.upperBound = upperBound;
    this.lowerBound = lowerBound;
  }

  public void startCalcNewRate(){
    diversityMeasure1.setData(pop);
    diversityMeasure1.startCalcDiversity();
    genoDiversityValue = diversityMeasure1.getPopulationDiversityValue();

    updatedRate = Math.max(lowerBound, Math.min(upperBound, currentRate*(1+tuneParameter*(expectedDiversityValue - currentRate)/currentRate)));
  }

  public static void main(String[] args) {
    KennyZhu2 kennyZhu21 = new KennyZhu2();
  }

}