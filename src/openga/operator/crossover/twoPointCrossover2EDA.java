package openga.operator.crossover;
import openga.chromosomes.*;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: Estimation of Distribultion algorithm determines the direction to mutate.
 * We forbid the direction while both genes move to inferior positions.</p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class twoPointCrossover2EDA extends twoPointCrossover2 implements EDAICrossover{
  public twoPointCrossover2EDA() {
  }

  double container[][];//it's an m-by-m array to store the gene results. The i is job, the j is the position(sequence).
  double evaporateRate = 0.5;
  int numberOfTournament = 2;
  chromosome newChromosomes[] = new chromosome[2];

  public void setEDAinfo(double container[][], int numberOfTournament){
    this.container = container;
    this.numberOfTournament = numberOfTournament;
  }

  //start to crossover
  public void startCrossover(){
    for(int i = 0 ; i < 2 ; i ++ ){
      newChromosomes[i] = new chromosome();
      newChromosomes[i].setGenotypeAndLength(originalPop.getEncodedType(), originalPop.getLengthOfChromosome(), originalPop.getNumberOfObjectives());
      newChromosomes[i].initChromosome();
    }

    for(int i = 0 ; i < popSize ; i ++ ){
       //test the probability is larger than crossoverRate.
       if(Math.random() <= crossoverRate){
         //to get the other chromosome to crossover
         setCutpoint();
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
      copyElements(originalPop.getSingleChromosome(selectedSoln), originalPop.getSingleChromosome(index2), newChromosomes[0]);
      //copyElements(originalPop.getSingleChromosome(index2), originalPop.getSingleChromosome(selectedSoln), newChromosomes[1]);
      if(numberOfTournament == 1){//it needs not to collect the gene information
        probabilitySum = 10.0;
      }
      else{
        //probabilitySum =  sumGeneInfo(newChromosomes[0], cutPoint1, cutPoint2);
        probabilitySum = productGeneInfo(newChromosomes[0], cutPoint1, cutPoint2);
      }

      if(maxProb < probabilitySum){
        maxProb = probabilitySum;
        newPop.setSingleChromosome(selectedSoln, newChromosomes[0]);
        //newPop.setSingleChromosome(index2, newChromosomes[1]);
      }
    }
  }

  private double sumGeneInfo(chromosome _chromosome, int cutPoint1, int cutPoint2){
    double sum = 0;
    for(int i = cutPoint1 ; i <= cutPoint2 ; i ++){
      int job = _chromosome.getSolution()[i];
      sum += container[job][i];
    }
    return sum;
  }

  //Probability product, instead of summing up the probability.
  //2009.8.20
  private double productGeneInfo(chromosome _chromosome, int cutPoint1, int cutPoint2){
    double productValue = 1;
    for(int i = cutPoint1 ; i <= cutPoint2 ; i ++){
      int job = _chromosome.getSolution()[i];
      productValue *= container[job][i] * 10;
    }
    //productVale *= Math.pow(10, cutPoint2 - cutPoint1);
    return productValue;
  }


  private void copyElements(chromosome parent1, chromosome parent2, chromosome child1){
    child1.setSolution(parent1.getSolution());
    //to modify the first chromosome between the index1 to index2, which genes
    //is from chromosome 2.
    int counter = 0;
    for(int i = cutPoint1 ; i <= cutPoint2; i ++ ){
      while(checkConflict(parent2.genes[counter], parent1.genes) == true){
        counter ++;
      }
      child1.setGeneValue(i, parent2.genes[counter]);
      counter ++;
    }
  }

  private boolean checkConflict(int newGene, int _chromosome[]){
    boolean hasConflict = false;
    for(int i = 0 ; i < cutPoint1 ; i ++ ){
      if(newGene == _chromosome[i]){
        return true;
      }
    }

    for(int i = cutPoint2 + 1 ; i < chromosomeLength ; i ++ ){
      if(newGene == _chromosome[i]){
        return true;
      }
    }

    return hasConflict;
  }

  @Override
  public void setEDAinfo(double[][] container, double[][] inter, int tempNumberOfCrossoverTournament) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

}