/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package openga.operator.crossover;
import openga.chromosomes.*;

/**
 *
 * @author user
 */
public class uniformCrossoverEDA extends uniformCrossover implements EDAICrossover{
  double container[][];//it's an m-by-m array to store the gene results. The i is job, the j is the position(sequence).
  int numberOfTournament = 2;
  chromosome newChromosomes[] = new chromosome[2];

  public void setEDAinfo(double container[][], int numberOfTournament){
    this.container = container;
    this.numberOfTournament = numberOfTournament;
  }
  
  public void startCrossover() {
    for(int i = 0 ; i < 2 ; i ++ ){
      newChromosomes[i] = new chromosome();
      newChromosomes[i].setGenotypeAndLength(originalPop.getEncodedType(), originalPop.getLengthOfChromosome(), originalPop.getNumberOfObjectives());
      newChromosomes[i].initChromosome();
    }
    
    for(int i = 0 ; i < popSize ; i ++ ){
       //test the probability is larger than crossoverRate.
       if(Math.random() <= crossoverRate){
         //to get the other chromosome to crossover
         checkCutPoints(i);
       }
    }
  }

  public void checkCutPoints(int selectedSoln){
    if(numberOfTournament == 0){
      System.out.println("numberOfTournament is at least 1.");
      System.exit(0);
    }

    double probabilitySum;
    double maxProb = 0.0;
    //maxProb = sumGeneInfo(originalPop.getSingleChromosome(selectedSoln), cutPoint1, cutPoint2);

    for(int i = 0 ; i < numberOfTournament ; i ++ ){
      int index2 = getCrossoverChromosome(selectedSoln);//to get a chromosome to be mated.
      int vector1[] = generateRandomVector();
      copyElements(originalPop.getSingleChromosome(selectedSoln), originalPop.getSingleChromosome(index2), newChromosomes[0], vector1);


         
      //copyElements(originalPop.getSingleChromosome(index2), originalPop.getSingleChromosome(selectedSoln), newChromosomes[1]);
      if(numberOfTournament == 1){//it needs not to collect the gene information
        probabilitySum = 10.0;
      }
      else{
        //probabilitySum =  sumGeneInfo(newChromosomes[0], cutPoint1, cutPoint2);
        probabilitySum = productGeneInfo(newChromosomes[0], vector1);
      }

      if(maxProb < probabilitySum){
        maxProb = probabilitySum;
        newPop.setSingleChromosome(selectedSoln, newChromosomes[0]);
        //newPop.setSingleChromosome(index2, newChromosomes[1]);
      }
    }
  }

  private int[] generateRandomVector(){
    int vector1[] = new int[chromosomeLength];
    for(int i = 0 ; i < chromosomeLength ; i ++ ){
      if(Math.random() > 0.5){
        vector1[i] = 1;
      }
      else{
        vector1[i] = 0;
      }
    }
    return vector1;
  }

  private void copyElements(chromosome parent1, chromosome parent2, chromosome child1, int vector1[]){
    child1.setSolution(parent1.getSolution());
    //to modify the first chromosome between the index1 to index2, which genes
    //is from chromosome 2.
    for(int i = 0 ; i < chromosomeLength; i ++ ){
      if(vector1[i] == 0){
        child1.setGeneValue(i, parent2.genes[i]);
      }
    }
  }

  /*
  private void copyElements(int index1, int index2, int vector1[]){
    //to modify the first chromosome between the index1 to index2, which genes
    //is from chromosome 2.
    int counter = 0;

    for(int i = 0 ; i < chromosomeLength; i ++ ){
      if(vector1[i] == 0){
        //System.out.print(i+": "+originalPop.getSingleChromosome(index1).genes[i]+"->"+originalPop.getSingleChromosome(index2).genes[i]+"\t");
        newPop.setGene(index1, i, originalPop.getSingleChromosome(index2).genes[i]);
      }
    }
    //System.out.println();
  }
   */

  private double productGeneInfo(chromosome _chromosome, int vector1[]){
    double productValue = 1;
    for(int i = 0 ; i < chromosomeLength; i ++ ){
      if(vector1[i] == 0){
        int zeroOrone = _chromosome.getSolution()[i];
        productValue *= container[zeroOrone][i] * 10;
      }            
    }   
    return productValue;
  }
  
  public populationI getCrossoverResult(){
    return newPop;
  }

  @Override
  public void setEDAinfo(double[][] container, double[][] inter, int tempNumberOfCrossoverTournament) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
}
