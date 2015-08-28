package openga.Fitness;

/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: We normalize the input objective value and let it becomes a fitness value.</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class FitnessByNormalize extends singleObjectiveFitness {
  public FitnessByNormalize() {
  }

  public void calculateFitness(){
    openga.util.algorithm.normalize normalize1 = new openga.util.algorithm.normalize();
    System.arraycopy(normalize1.getData1(objVals), 0, fitness, 0, fitness.length);

    //write the fitness data to the fitness of each chromosome.
    for(int i = 0 ; i < popSize ; i ++ ){
      pop.setFitness(i, fitness[i]);
    }
  }//end calculateFitness()

}