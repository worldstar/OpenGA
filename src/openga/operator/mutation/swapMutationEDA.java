package openga.operator.mutation;
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

public class swapMutationEDA extends swapMutation implements EDAIMutation{
  public swapMutationEDA() {
  }
  double container[][];//it's an m-by-m array to store the gene results. The i is job, the j is the position(sequence).
  int numberOfTournament = 2;

  public void setData(double mutationRate, populationI population1){
    pop = new population();
    this.pop = population1;
    this.mutationRate = mutationRate;
    popSize = population1.getPopulationSize();
    chromosomeLength = population1.getSingleChromosome(0).genes.length;
  }

  public void setEDAinfo(double container[][], int numberOfTournament){
    this.container = container;
    this.numberOfTournament = numberOfTournament;
  }

  /**
   * we determine the mutation cut-point by mining the gene information.
   */
  public void startMutation(){
    for(int i = 0 ; i < popSize ; i ++ ){
       //test the probability is larger than mutationRate.
       if(Math.random() <= mutationRate){
         checkCutPoints(pop.getSingleChromosome(i));
         pop.setChromosome(i, swaptGenes(pop.getSingleChromosome(i)));
       }
    }
  }

  /**
   * If the both gene moves to inferior positions, we generate the other one to replace it.
   * @param _chromosome
   */
  public void checkCutPoints(chromosome _chromosome){
    if(numberOfTournament == 0){
      System.out.println("numberOfTournament is at least 1.");
      System.exit(0);
    }
    int cutPoints[][] = new int[numberOfTournament][2];
    double probabilitySum;
    double maxProb = 0.0;
    int selectedIndex = 0;

    for(int i = 0 ; i < numberOfTournament ; i ++ ){
      setCutpoint();
      cutPoints[i][0] = cutPoint1;
      cutPoints[i][1] = cutPoint2;
      if(numberOfTournament == 1){//it needs not to collect the gene information
        selectedIndex = i;
      }
      else{
        //probabilitySum = sumGeneInfo(cutPoint1, cutPoint2, _chromosome.genes[cutPoint2], _chromosome.genes[cutPoint1]);
        //probabilitySum = productGeneInfo(cutPoint1, cutPoint2, _chromosome.genes[cutPoint2], _chromosome.genes[cutPoint1]);
        probabilitySum = productGeneInfo(_chromosome, container);

        if(maxProb < probabilitySum){
          maxProb = probabilitySum;
          selectedIndex = i;
        }
      }
    }

    cutPoint1 = cutPoints[selectedIndex][0];
    cutPoint2 = cutPoints[selectedIndex][1];
    cutPoints = null;
  }

  /**
   * The job1 is at position 1 and job2 is at the pos2.
   * @param pos1
   * @param pos2
   * @param job1
   * @param job2
   * @return
   */
  private double sumGeneInfo(int pos1, int pos2, int job1, int job2){
    double sum = 0;
    sum = container[job1][pos1];
    sum += container[job2][pos2];
    return sum;
  }

  //Probability product, instead of summing up the probability.
  //2009.8.20
  private double productGeneInfo(int pos1, int pos2, int job1, int job2){
    double productValue = 1;
    productValue *= container[job1][pos1];
    productValue *= container[job2][pos2];
    return productValue;
  }

  private double productGeneInfo(chromosome chromsome1, double containerTemp[][]){
    double productValue = 1;
    for(int i = 0 ; i < chromsome1.getLength() ; i ++){
      int job1 = chromsome1.getSolution()[i];
      productValue *= containerTemp[job1][i] * 10;
    }
    return productValue;
  }

  @Override
  public void setEDAinfo(double[][] container, double[][] inter, int numberOfMutationTournament) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

}