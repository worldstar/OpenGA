package openga.chromosomes;

/**
 * <p>Title: The OpenGA project</p>
 * <p>Description: The project is to build general framework of Genetic algorithm and problem independent.</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public interface populationI {

  public void setGenotypeSizeAndLength(boolean thetype, int size, int length, int numberOfObjs);
  public void setBounds(double lwBounds[], double upBound[]);
  public void createNewPop();
  public void initNewPop();
  public void setSingleChromosome(int index, chromosome _chromosome);
  public void setFitness(int index, double addedValue);
  public void setChromosome(int index, chromosome _chromosome);
  public void setGene(int indexOfChrosome, int indexOfGene, int gene);
  public void setGene(int indexOfChrosome, int indexOfGene, double gene);
  public void setObjectiveValue(int indexOfChrosome, double _obj[]);
  public void setNormalizedObjValue(int indexOfChrosome, double normObjectives[]);
  public void setScalarizedObjectiveValue(int indexOfChrosome,double value);
  public chromosome getSingleChromosome(int index);
  public boolean getEncodedType();
  public int getLengthOfChromosome();
  public int getPopulationSize();
  public int getNumberOfObjectives();
  public double[] getObjectiveValues(int index);
  public double[] gettNormalizedObjValue(int index);
  public double getScalarizedObjectiveValue(int indexOfChrosome);
  public double getFitness(int index);
  public double[][] getObjectiveValueArray();
  public double[] getFitnessValueArray();
}