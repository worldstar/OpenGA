package openga.chromosomes;

/**
 * <p>Title: The OpenGA project</p>
 * <p>Description: The project is to build general framework of Genetic algorithm and problem independent.</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class population implements populationI{
  public population() {
  }

  int   size;         //total size of the population
  int   length;       //number of genes
  boolean   genotype; //real or binary code
  chromosome pop[];   //Pop is composed of chromosomes.
  int nondominated;
  int numberOfObjs;
  double lwBounds[];//lower bound
  double upBound[]; //upper bound

  public void setGenotypeSizeAndLength(boolean thetype, int size, int length, int numberOfObjs){
    this.genotype = thetype;
    this.size = size;
    this.length = length;
    this.numberOfObjs = numberOfObjs;
  }

  /**
   * @version 1.1
   */
  public void setBounds(double lwBounds[], double upBound[]){
    this.lwBounds = lwBounds;
    this.upBound= upBound;
  }

  public void createNewPop(){
    pop = new chromosome[size];
    for(int i = 0 ; i < size ; i ++ ){
      pop[i] = new chromosome();
      java.util.Random r = new java.util.Random(i+54532891);
      pop[i].setGenotypeAndLength(genotype, length, numberOfObjs);
      pop[i].setRandomObject(r);
      if(genotype == false){//it's the continuous problem.
        pop[i].setBounds(lwBounds, upBound);
      }
      pop[i].initChromosome();
    }
  }

  /**
   * Almost the same with createNewPop(). The difference is that it doesn't
   * create values of each gene on chromosome.
   */
  public void initNewPop(){
    pop = new chromosome[size];
    for(int i = 0 ; i < size ; i ++ ){
      pop[i] = new chromosome();
      pop[i].setGenotypeAndLength(genotype, length, numberOfObjs);
    }
  }

  /**
   * @param index the number of the chromsome in a population
   * @param _chromosome The solution we want to keep.
   * @version 1.1
   */
  public void setSingleChromosome(int index, chromosome _chromosome){
    pop[index].setSolution(_chromosome.getSolution());
    pop[index].setRealNumberSolution(_chromosome.getRealNumberSolution());
    pop[index].setFitnessValue(_chromosome.getFitnessValue());
    pop[index].setObjValue(_chromosome.getObjValue());
  }

  public void setFitness(int index, double Value){
    //double newValue = pop[index].getFitnessValue() + addedValue;
    pop[index].setFitnessValue(Value);
  }

  /**
   * @param index the number of the chromsome in a population
   * @param _chromosome The solution we want to keep.
   * @version 1.1
   */
  public void setChromosome(int index, chromosome _chromosome){
    pop[index].setSolution(_chromosome.getSolution());
    pop[index].setRealNumberSolution(_chromosome.getRealNumberSolution());
  }

  public void setGene(int indexOfChrosome, int indexOfGene, int gene){
    pop[indexOfChrosome].setGeneValue(indexOfGene, gene);
  }

  /**
   * @version 1.1
   */
  public void setGene(int indexOfChrosome, int indexOfGene, double gene){
    pop[indexOfChrosome].setGeneValue(indexOfGene, gene);
  }

  public void setObjectiveValue(int indexOfChrosome, double _obj[]){
    pop[indexOfChrosome].setObjValue(_obj);
  }

  public void setNormalizedObjValue(int indexOfChrosome, double normObjectives[]){
    pop[indexOfChrosome].setNormalizedObjValue(normObjectives);
  }

  public void setScalarizedObjectiveValue(int indexOfChrosome,double value){
    pop[indexOfChrosome].setScalarizedObjectiveValue(value);
  }

  public chromosome getSingleChromosome(int index){
    return pop[index];
  }

  public boolean getEncodedType(){
    return genotype;
  }

  public int getLengthOfChromosome(){
    return length;
  }

  public int getPopulationSize(){
    return size;
  }

  public int getNumberOfObjectives(){
    return numberOfObjs;
  }

  public double[] getObjectiveValues(int index){
    return pop[index].getObjValue();
  }

  public double[] gettNormalizedObjValue(int index){
    return pop[index].getNormalizedObjValue();
  }

  public double getScalarizedObjectiveValue(int indexOfChrosome){
    return pop[indexOfChrosome].getScalarizedObjectiveValue();
  }

  public double getFitness(int index){
    return pop[index].getFitnessValue();
  }

  /**
   * @return To return the objective value by the double array.
   */
  public double[][] getObjectiveValueArray(){
    double objArray[][] = new double[size][numberOfObjs];
    for(int i = 0 ; i < size ; i ++ ){
      objArray[i] = getObjectiveValues(i);
    }
    return objArray;
  }

  public double[] getFitnessValueArray(){
    double fitnessArray[] = new double[size];
    for(int i = 0 ; i < size ; i ++ ){
      fitnessArray[i] = getFitness(i);
    }
    return fitnessArray;
  }

  public static void main(String[] args) {
    population population1 = new population();
    int size = 15, length = 10, numberOfObjs = 2;
    population1.setGenotypeSizeAndLength(true, size, length, numberOfObjs);
    population1.createNewPop();

    openga.util.printClass printClass1 = new openga.util.printClass();
    for(int i = 0 ; i < size ; i ++ ){
       printClass1.printMatrix(""+i,population1.pop[i].genes);
    }
  }//end main

}