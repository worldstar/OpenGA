/*
 * 2009.8.20. To avoid the pij which is 0.
 */

package openga.operator.miningGene;
import openga.chromosomes.*;
/**
 *
 * @author nhu
 */
public class PBILwithLapaceCorrection implements EDAModelBuildingI{
  populationI originalPop;
  int popSize, chromosomeLength;
  double container[][];//it's an m-by-m array to store the gene results. The i is job, the j is the position(sequence).
  double avgFitness = 0;
  double lamda = 0.9; //learning rate

  public PBILwithLapaceCorrection(){

  }

  public PBILwithLapaceCorrection(populationI originalPop, double lamda) {
    this.originalPop = originalPop;
    popSize = originalPop.getPopulationSize();
    chromosomeLength = originalPop.getSingleChromosome(0).genes.length;
    this.lamda = lamda;
    container = new double[chromosomeLength][chromosomeLength];

    for(int i = 0 ; i < chromosomeLength ; i ++ ){
      for(int j = 0 ; j < chromosomeLength ; j ++ ){
        container[i][j] = 1.0/popSize;
      }
    }
  }

  public PBILwithLapaceCorrection(int chromosomeLength, double lamda) {
    this.chromosomeLength = chromosomeLength;
    this.lamda = lamda;
    container = new double[chromosomeLength][chromosomeLength];

    for(int i = 0 ; i < chromosomeLength ; i ++ ){
      for(int j = 0 ; j < chromosomeLength ; j ++ ){
        container[i][j] = 1.0/popSize;
      }
    }
  }

  public void setData(populationI originalPop){
     this.originalPop = originalPop;
     popSize = originalPop.getPopulationSize();
  }

  public void startStatistics(){
    calcAverageFitness();
    calcContainer();
  }

  /**
   * The average fitness.
   */
  public void calcAverageFitness(){
    openga.Fitness.calcAverageFitness calcAverageFitness1 = new openga.Fitness.calcAverageFitness();
    calcAverageFitness1.setData(originalPop);
    calcAverageFitness1.startCalcFitness();
    avgFitness = calcAverageFitness1.getcalcFitness();
    avgFitness *= 1.2;
  }

  public void calcContainer(){
    double tempContainer[][] = new double[chromosomeLength][chromosomeLength];
    //to collect gene information.
    //System.out.println(avgFitness+" "+popSize);
    int counter = 0;
    for(int i = 0 ; i < popSize ; i ++ ){
      if(originalPop.getFitness(i) <= avgFitness){
         for(int j = 0 ; j < chromosomeLength ; j ++ ){
           int gene = originalPop.getSingleChromosome(i).getSolution()[j];
           tempContainer[gene][j] += 1;
         }
         counter ++;
      }
    }

    laplaceCorrection(tempContainer);    
    //double CR = showPopCorrelationValue(originalPop, tempContainer);
    updateContainer(tempContainer, counter);
  }


  //To avoid the pij is 0 which causes the product becomes zero by adding each element by 1.
  //2009.8.20
  private void laplaceCorrection(double container[][]){
    for(int i = 0; i < container.length ; i ++){
      for(int j = 0 ; j < container[0].length ; j ++){
        container[i][j] += 1;
      }
    }
  }

  private void updateContainer(double tempContainer[][], int counter){
    //to normalize them between [0, 1].
    for(int i = 0 ; i < chromosomeLength ; i ++ ){
      for(int j = 0 ; j < chromosomeLength ; j ++ ){
        tempContainer[i][j] /= counter;
      }
    }

    for(int i = 0 ; i < chromosomeLength ; i ++ ){
      for(int j = 0 ; j < chromosomeLength ; j ++ ){
        container[i][j] = (1-lamda)*container[i][j] + (lamda)*tempContainer[i][j];
      }
    }
  }

/*
 *

  private double showPopCorrelationValue(populationI Population, double containerTemp[][]){
    double Obs[] = new double[Population.getPopulationSize()];
    double productsVals[] = new double[Population.getPopulationSize()];
    //PBILwithLapaceCorrection PBIL2 = new PBILwithLapaceCorrection(Population, lamda);
    //PBIL2.startStatistics();
    //double containerTemp[][] = PBIL2.getContainer();

    for(int i = 0 ; i < Population.getPopulationSize() ; i ++){
      Obs[i] = Population.getObjectiveValues(i)[0];
      productsVals[i] = getProductValue(i, containerTemp);
    }

    jsc.datastructures.PairedData PairedData = new jsc.datastructures.PairedData(Obs, productsVals);
    jsc.correlation.LinearCorrelation LinearCorrelation1 = new jsc.correlation.LinearCorrelation(PairedData);

    //System.out.println(LinearCorrelation1.getTestStatistic()+"\t"+LinearCorrelation1.getSP());

    return LinearCorrelation1.getTestStatistic();
  }

  private double getProductValue(int index1, double containerTemp[][]){
    chromosome chromsome1 = originalPop.getSingleChromosome(index1);
    double productValue = 1;
    for(int i = 0 ; i < chromsome1.getLength() ; i ++){
      int job1 = chromsome1.getSolution()[i];
      productValue *= containerTemp[job1][i] * 10;
    }
    return productValue;
  }
 */
  public double[][] getContainer(){
    return container;
  }
}
