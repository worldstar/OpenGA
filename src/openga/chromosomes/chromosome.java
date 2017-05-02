package openga.chromosomes;
import java.util.List;
import openga.util.printClass;
/**
 * <p>Title: The OpenGA project</p>
 * <p>Description: To describe the property of a chromosome.
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Yuan-Ze University</p>
 * @author not attributable
 * @version 1.0
 */

public class chromosome {
  public chromosome(){
  }
  //The encoding of genetic algorithm. If true, it's Phenotype or real number.
  //If not, it's binary one.
  static boolean thetype;
  public int     genes[];
  public double  realGenes[];
  boolean        valid;
  static int     length;
  //to store the calculations result of objective functions and fitnesses.
  //We keep the two data here and data is passed from other program.
  static int     numberOfObjs;
  double         objectives[];
  double         scalarizedObjectiveValue;
  double         normObjectives[];
  double         fitness;
  static java.util.Random r = new java.util.Random(54354);//12358
  static double lwBounds[];//lower bound
  static double upBound[]; //upper bound

  /**
   * @param thetype if it's true which means binary one.
   * @param num The length of a chromosome.
   */
  public void setGenotypeAndLength(boolean thetype, int num, int numberOfObjs){
    this.thetype = thetype;
    this.length   = num;
    genes = new int[length];
    realGenes = new double[length];
    this.numberOfObjs = numberOfObjs;
    objectives = new double[numberOfObjs];
    normObjectives = new double[numberOfObjs];
  }

  public final void setBounds(double lwBounds[], double upBound[]){
    this.lwBounds = lwBounds;
    this.upBound= upBound;
  }

  public void setRandomObject(java.util.Random r){
    this.r = r;
  }

  public void initChromosome(){
    if(thetype == true){
      generateSequentialPop(length);
    }
    else{
      generateBinaryPop(length);
    }
  }

  public void generateBinaryPop(int numberofGenes){
    for(int i = 0 ; i < numberofGenes ; i ++ ){
      if(Math.random() > 0.5){//r.nextDouble()
        genes[i] = 1;
      }
      else{
        genes[i] = 0;
      }
    }
  }

  /**
   * It's depended on the lower bound and upper bound to generate these real value.
   * @param numberofGenes Population size
   * @version 1.1
   */
  public final void generateRealnumberPop(int numberofGenes){

    for(int i = 0 ; i < numberofGenes ; i ++ ){
      realGenes[i] = lwBounds[i] + Math.random()*(upBound[i] - lwBounds[i]);
    }
  }

  /**
   * The sequencial coded.
   * @param numberofGenes
   * @version 1.1
   */
  public final void generateSequentialPop(int numberofGenes){
    //initial the chromosome
    for(int i = 0 ; i < numberofGenes ; i ++ ){
      genes[i] = i;
    }

    for(int i = 0 ; i < numberofGenes ; i ++ ){
      //swap
      int temp1 = genes[i];
      //int indexTemp = (int)(numberofGenes*r.nextDouble());
      int indexTemp = (int)(numberofGenes*Math.random());
      int temp2 = genes[indexTemp];
      genes[indexTemp] = temp1;
      genes[i] = temp2;
    }
  }
  
    /**
   * The sequencial coded.
   * @param numberofGenes
   * @param numberOfSalesmen
   * @version 1.1
   */
  public final void generateTwoPartPop(int numberofGenes, int numberOfSalesmen){
    //initial the chromosome
    int numberofCities = numberofGenes - numberOfSalesmen;
    
    for(int i = 0 ; i < numberofCities ; i ++ ){
      genes[i] = i;
    }
    //first part
    for(int i = 0 ; i < numberofCities ; i ++ ){
      //swap
      int temp1 = genes[i];
      //int indexTemp = (int)(numberofGenes*r.nextDouble());
      int indexTemp = (int)(numberofCities*Math.random());
      int temp2 = genes[indexTemp];
      genes[indexTemp] = temp1;
      genes[i] = temp2;
    }        
    
    //secend part
    int leftCities = numberofCities;
    for(int i = numberofCities; i < length ; i++){
        genes[i] = 1;
        leftCities --;
    }         
    
    for(int i = numberofCities; i < length - 1 ; i++){        
        int assignCities = (int)(leftCities*Math.random());
        genes[i] += assignCities;
        leftCities -= assignCities;
    }
    
    genes[length - 1] += leftCities;          
  }  

  /**
   *
   * @param objectives obj values for each obj function.
   * @version 1.1
   */
  public void setObjValue(double objectives[]){
    System.arraycopy(objectives, 0, this.objectives, 0, objectives.length );
  }

  public void setScalarizedObjectiveValue(double value){
    this.scalarizedObjectiveValue = value;
  }

  /**
   *
   * @version 1.1
   */
  public final void setNormalizedObjValue(double normObjectives[]){
    System.arraycopy(normObjectives, 0, this.normObjectives, 0, normObjectives.length );
  }

  public void setFitnessValue(double fitness){
    this.fitness = fitness;
  }
  /**
   *
   * @version 1.1
   */
  public final void setSolution(int soln[]){
    //System.arraycopy(soln, 0, this.genes, 0, soln.length );
    for(int i = 0 ; i < soln.length ; i ++ ){
      genes[i] = soln[i];
    }
  }
  
  public final void setSolution(List<Integer> soln){
    //System.arraycopy(soln, 0, this.genes, 0, soln.length );
    for(int i = 0 ; i < soln.size() ; i ++ ){
      genes[i] = soln.get(i);
    }
  }

  /**
   *
   * @version 1.1
   */
  public final void setRealNumberSolution(double soln[]){
    System.arraycopy(soln, 0, this.realGenes, 0, soln.length );
  }

  public void setGeneValue(int index, int geneData){
    genes[index] = geneData;
  }

  public void setGeneValue(int index, double geneData){
    realGenes[index] = geneData;
  }

  /**
   * To get the int type(includes binary or sequential chromosome)
   * @return
   * @version 1.1
   */
  public final int[] getSolution(){
     return genes;
  }

  public final double[] getRealCodeSolution(){
     return realGenes;
  }

  /**
   * To get the real type
   * @return
   * @version 1.1
   */
  public double[] getRealNumberSolution(){
    return realGenes;
  }

  public double[] getObjValue(){
    return objectives;
  }

  public double getScalarizedObjectiveValue(){
    return scalarizedObjectiveValue;
  }

  public double[] getNormalizedObjValue(){
    return normObjectives;
  }

  public double getFitnessValue(){
    return fitness;
  }

  public boolean getEncodeType(){
    return thetype;
  }

  public int getLength(){
    return length;
  }

  public int getNumberOfObjective(){
    return numberOfObjs;
  }

  /**
   *
   * @return the lower bounds of each dimension.
   * @version 1.1
   */
  public double[] getLowerBound(){
    return lwBounds;
  }

  /**
   *
   * @return the lower bounds of each dimension.
   * @version 1.1
   */
  public double[] getUpprtBound(){
    return upBound;
  }

  /**
   *
   * @return to return the string type of all gene value
   */
  public final String toString1(){
    String allResults = "";
    for(int i = 0 ; i < genes.length ; i ++ ){
      allResults += genes[i]+" ";
    }
    return allResults;
  }

  /**
   *
   * @return to return the string type of all realGenes value
   * @version 1.1
   */
  public final String toString2(){
    String allResults = "";
    for(int i = 0 ; i < realGenes.length ; i ++ ){
      allResults += realGenes[i]+"\t";
    }

    return allResults;
  }

  public static void main(String[] args) {
    chromosome chromosome1 = new chromosome();
    chromosome1.setGenotypeAndLength(true, 5,2);
    chromosome1.initChromosome();
    printClass printClass1 = new printClass();
    printClass1.printMatrix("chromosome1",chromosome1.genes);
  }

}