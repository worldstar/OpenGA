package openga.operator.crossover;
import openga.chromosomes.*;
import openga.util.sort.selectionSort;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class twoPointCrossover2withAdpativeThreshold implements AdaptiveCrossoverI{
  public twoPointCrossover2withAdpativeThreshold() {
  }
  populationI originalPop, newPop;
  double crossoverRate;
  int popSize, chromosomeLength;
  int cutPoint1, cutPoint2;
  int pos1, pos2;

  /**
   * Used by adaptive strategy.
   */
  double avgFitness = 0, maxFitness = 0, minFitness = 0;
  double k1 = 0.7, k2 = 0.5, k3 = 1, k4 = 0.5;
  double targetDiversity = 0.5;
  double thresholdFitness;

  public void setData(double crossoverRate, chromosome _chromosomes[],int numberOfObjs){
    //transfomation the two chromosomes into a population.
    population _pop = new population();
    _pop.setGenotypeSizeAndLength(_chromosomes[0].getEncodeType(), _chromosomes.length, _chromosomes[0].getLength(), numberOfObjs);
    _pop.initNewPop();
    for(int i = 0 ; i < _chromosomes.length ; i ++ ){
      _pop.setChromosome(i, _chromosomes[i]);
    }
    setData(crossoverRate, _pop);
  }

  public void setData(double crossoverRate, populationI originalPop){
    this.originalPop = originalPop;
    popSize = originalPop.getPopulationSize();
    //System.out.println("originalPop.getLengthOfChromosome() "+originalPop.getLengthOfChromosome());
    newPop = new population();
    newPop.setGenotypeSizeAndLength(originalPop.getEncodedType(), popSize, originalPop.getLengthOfChromosome(), originalPop.getNumberOfObjectives());
    newPop.initNewPop();

    for(int i = 0 ; i < popSize ; i ++ ){
      newPop.setSingleChromosome(i, originalPop.getSingleChromosome(i));
    }
    this.crossoverRate = crossoverRate;
    chromosomeLength = originalPop.getSingleChromosome(0).genes.length;
  }

  public void setAdaptiveParameters(double P1, double P2, double targetDiversity){
    k1 = P1;
    k3 = P2;
    this.targetDiversity = targetDiversity;
  }
  /**
   * The average fitness.
   */
  public void calcAdaptiveParameter(){
    openga.Fitness.calcAverageFitness calcAverageFitness1 = new openga.Fitness.calcAverageFitness();
    calcAverageFitness1.setData(originalPop);
    calcAverageFitness1.startCalcFitness();
    avgFitness = calcAverageFitness1.getcalcFitness();

    openga.Fitness.MaxFitness MaxFitness1 = new openga.Fitness.MaxFitness();
    MaxFitness1.setData(originalPop);
    MaxFitness1.startCalcFitness();
    maxFitness = MaxFitness1.getcalcFitness();

    openga.Fitness.MinFitness minFitness1 = new openga.Fitness.MinFitness();
    minFitness1.setData(originalPop);
    minFitness1.startCalcFitness();
    minFitness = minFitness1.getcalcFitness();
  }

  public void diversityMeasure(){
    openga.Fitness.calcAverageFitness calcAverageFitness1 = new openga.Fitness.calcAverageFitness();
    calcAverageFitness1.setData(originalPop);
    calcAverageFitness1.startCalcFitness();
    avgFitness = calcAverageFitness1.getcalcFitness();

    openga.adaptive.diversityMeasure.diversityMeasureI diversityMeasure1 = new openga.adaptive.diversityMeasure.HammingDistance();
    diversityMeasure1.setData(originalPop);
    diversityMeasure1.startCalcDiversity();
    double genoDiversityValue = diversityMeasure1.getPopulationDiversityValue();

    if(genoDiversityValue < targetDiversity){//diversity is not good, to decrease the threshold value
      thresholdFitness = avgFitness + (genoDiversityValue - targetDiversity)*(maxFitness - avgFitness)*0.5;
    }
    else{//the diversity is good, to increase the threshold value
      thresholdFitness = avgFitness + (genoDiversityValue - targetDiversity)*(avgFitness - minFitness)*0.5;
    }
  }

  //start to crossover
  public void startCrossover(){
    calcAdaptiveParameter();
    diversityMeasure();
    double tempCrossOverRate;

    for(int i = 0 ; i < popSize ; i ++ ){
      if(originalPop.getFitness(i) < thresholdFitness){
        if(originalPop.getFitness(i) == 0){
          tempCrossOverRate = 0.05;
        }
        else{
          tempCrossOverRate = k1*(newPop.getFitness(i) - minFitness)/(thresholdFitness - minFitness);
        }
      }
      else{
        tempCrossOverRate = k3;
      }
      //System.out.println("tempCrossOverRate  "+tempCrossOverRate+"\tthresholdFitness\t"+thresholdFitness+"\t"+newPop.getFitness(i));
      //test the probability is larger than crossoverRate.
      if(Math.random() <= tempCrossOverRate){
        //to get the other chromosome to crossover
        int index2 = getCrossoverChromosome(i);
        //System.out.println("index2 "+index2);
        setCutpoint();
        copyElements(i, index2);
        copyElements(index2, i);
      }
    }
  }

  /**
   * To get the other chromosome to crossover.
   * @param index The index of original chromosome.
   * @return
   */
  public int getCrossoverChromosome(int index){
    int index2 = (int)(Math.random()*popSize);
    if(index == index2){
      index2 = getCrossoverChromosome(index);
    }
    return index2;
  }

  /**
   * To set the two cut points.
   */
  private void setCutpoint(){
    cutPoint1 = (int)(Math.random() * chromosomeLength);
    cutPoint2 = (int)(Math.random() * chromosomeLength);
    //cutPoint1 = 3; //default for test
    //cutPoint2 = 6; //default for test

    if(cutPoint1 == cutPoint2){
      //double temp = r.nextDouble();
      //decrease the position of cutPoint1
      cutPoint1 -=  (int)(Math.random()*cutPoint1);
      //increase the position of cutPoint2
      cutPoint2 += (int)((chromosomeLength - cutPoint2)*Math.random());

      //double check it.
      if(cutPoint1 == cutPoint2){
        setCutpoint();
      }
    }

    //swap
    if(cutPoint1 > cutPoint2){
      int temp = cutPoint2;
      cutPoint2 = cutPoint1;
      cutPoint1 = temp;
    }
  }


  /**
   * The two chromosomes produce a new offspring
   * @param index1 The first chromosome to crossover
   * @param index2 The second chromosome to crossover
   */
  private void copyElements(int index1, int index2){
    //to modify the first chromosome between the index1 to index2, which genes
    //is from chromosome 2.
    int counter = 0;
    for(int i = cutPoint1 ; i <= cutPoint2; i ++ ){
      while(checkConflict(newPop.getSingleChromosome(index2).genes[counter], newPop.getSingleChromosome(index1).genes) == true){
        counter ++;
      }
      newPop.setGene(index1, i, newPop.getSingleChromosome(index2).genes[counter]);
      counter ++;
    }
  }

  /**
   * if there is the same gene, it return the index of the gene.
   * Else, default value is -1, which is also mean don't have the same gene
   * during cutpoint1 and cutpoint2.
   * @param newGene
   * @param _chromosome
   * @return
   */
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

  public populationI getCrossoverResult(){
    return newPop;
  }

    @Override
    public void setData(int numberOfJob, int numberOfMachines, int[][] processingTime) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setData(int numberofParents) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}