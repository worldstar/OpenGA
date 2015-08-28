package openga.operator.mutation;
import openga.chromosomes.*;
import openga.util.sort.selectionSort;

public class shiftMutationWithAdaptiveThreshold extends shiftMutationWithAdaptive {
  public shiftMutationWithAdaptiveThreshold() {
  }

  /**
   * Used by adaptive strategy.
   */
  double avgFitness = 0, maxFitness = 0, minFitness = 0;
  double k1 = 0.8, k2 = 0.5, k3 = 0.8, k4 = 0.5;
  double targetDiversity = 0.5;
  double thresholdFitness;

  public populationI pop;                 //mutation on whole population

  public void setData(double mutationRate, populationI population1){
    pop = new population();
    this.pop = population1;
    this.mutationRate = mutationRate;
    popSize = population1.getPopulationSize();
    chromosomeLength = population1.getSingleChromosome(0).genes.length;
  }

  /**
   * The average fitness.
   */
  public void calcAdaptiveParameter(){
    openga.Fitness.calcAverageFitness calcAverageFitness1 = new openga.Fitness.calcAverageFitness();
    calcAverageFitness1.setData(pop);
    calcAverageFitness1.startCalcFitness();
    avgFitness = calcAverageFitness1.getcalcFitness();

    openga.Fitness.MaxFitness MaxFitness1 = new openga.Fitness.MaxFitness();
    MaxFitness1.setData(pop);
    MaxFitness1.startCalcFitness();
    maxFitness = MaxFitness1.getcalcFitness();

    openga.Fitness.MinFitness minFitness1 = new openga.Fitness.MinFitness();
    minFitness1.setData(pop);
    minFitness1.startCalcFitness();
    minFitness = minFitness1.getcalcFitness();
  }

  public void diversityMeasure(){
    openga.Fitness.calcAverageFitness calcAverageFitness1 = new openga.Fitness.calcAverageFitness();
    calcAverageFitness1.setData(pop);
    calcAverageFitness1.startCalcFitness();
    avgFitness = calcAverageFitness1.getcalcFitness();

    openga.adaptive.diversityMeasure.diversityMeasureI diversityMeasure1 = new openga.adaptive.diversityMeasure.HammingDistance();
    //openga.adaptive.strategy.KennyZhu1 KennyZhu11 = new openga.adaptive.strategy.KennyZhu1();
    diversityMeasure1.setData(pop);
    diversityMeasure1.startCalcDiversity();
    double genoDiversityValue = diversityMeasure1.getPopulationDiversityValue();

    if(genoDiversityValue < targetDiversity){//diversity is not good, to increase the threshold value
      thresholdFitness = avgFitness + (genoDiversityValue - targetDiversity)*(maxFitness - avgFitness);
    }
    else{//the diversity is good, to decrease the threshold value
      thresholdFitness = avgFitness + (genoDiversityValue - targetDiversity)*(avgFitness - minFitness);
    }
  }

  public void startMutation(){
    calcAdaptiveParameter();
    diversityMeasure();
    double tempMutationRate;

    for(int i = 0 ; i < popSize ; i ++ ){
      if(pop.getFitness(i) < thresholdFitness){
        if(pop.getFitness(i) == 0){
          tempMutationRate = 0.05;
        }
        else{
          tempMutationRate = k2*(pop.getFitness(i) - minFitness)/(thresholdFitness - minFitness);
        }
      }
      else{
        tempMutationRate = k4;
      }
      //System.out.println("tempMutationRate "+tempMutationRate);

       //test the probability is larger than crossoverRate.
       if(Math.random() <= tempMutationRate){
         setCutpoint();
         pop.setChromosome(i,shiftGenes(pop.getSingleChromosome(i)));
       }
    }
  }

  public populationI getMutationResult(){
    return pop;
  }

}