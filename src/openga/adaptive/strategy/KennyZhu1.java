package openga.adaptive.strategy;
import openga.chromosomes.*;
import openga.adaptive.diversityMeasure.*;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: The method is from Zhu, K. (2002), A diversity-controlling adaptive genetic algorithm for the vehicle routing problem with time window.</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class KennyZhu1 {
  public KennyZhu1() {
  }

  private populationI pop;                 //population
  private int popSize, chromosomeLength;  //size of population and number of digits of chromosome


  private double genoDiversityValue = 0;
  private double expectedDiversityValue = 0.5;
  private double defaultExpectedDiversityValue = 0.5;
  private double currentRate;//the current probability of crossover or mutation rate.
  private double updatedRate;//the new rate for crossover or mutation.
  private double tuneParameter = 0.01;

  public void setData(populationI population1, double genoDiversityValue, double expectedDiversityValue, double currentRate){
    pop = new population();
    this.pop = population1;
    popSize = population1.getPopulationSize();
    chromosomeLength = population1.getSingleChromosome(0).genes.length;
    this.genoDiversityValue = genoDiversityValue;
    this.expectedDiversityValue = expectedDiversityValue;
    this.currentRate = currentRate;
  }

  public void setData(populationI population1, double genoDiversityValue, double currentRate){
    setData(population1, genoDiversityValue, defaultExpectedDiversityValue, currentRate);
  }

  public void startCalcNewRate(){
    updatedRate = currentRate*(1 + tuneParameter*(expectedDiversityValue - genoDiversityValue)/genoDiversityValue);
  }

  public double getUpdatedRate(){
    return updatedRate;
  }

  public double getDivisity(){
    return (double)genoDiversityValue;
  }

  public static void main(String[] args) {
    KennyZhu1 kennyZhu11 = new KennyZhu1();
  }

}