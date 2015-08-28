package openga.MainProgram;
import openga.chromosomes.*;
import openga.ObjectiveFunctions.*;
/**
 * <p>Title: The OpenGA project</p>
 * <p>Description: The project is to build general framework of Genetic algorithm and problem independent.</p>
 * <P>It is the main class of SPGA. The program will sum up the normalized objective value by assignning a fixed weight.
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class fixWeightScalarization extends singleThreadGAwithMultipleCrossover implements MainWeightScalarizationI, Runnable{
  public fixWeightScalarization() {
  }
  double weights[];

  public void setWeight(double weights[]){
    this.weights = weights;
  }

  public void run(){
    startGA();
  }

  public final populationI ProcessObjectiveAndFitness(){
    //evaluate the objective values
    /*
    ObjectiveWeightSumForKnapsack ObjectiveWeightSumForKnapsack1 = new ObjectiveWeightSumForKnapsack();
    ObjectiveWeightSumForKnapsack1.setObjectiveClass(ObjectiveFunction);
    ObjectiveWeightSumForKnapsack1.setData(Population, 0);
    ObjectiveWeightSumForKnapsack1.setWeightOfObjectives(weights);
    ObjectiveWeightSumForKnapsack1.calcObjective();
    Population = ObjectiveWeightSumForKnapsack1.getPopulation();
    */

    ObjectiveWeightSumForSchedule ObjectiveWeightSumForSchedule1 = new ObjectiveWeightSumForSchedule();
    ObjectiveWeightSumForSchedule1.setObjectiveClass(ObjectiveFunction);
    ObjectiveWeightSumForSchedule1.setData(Population, 0);
    ObjectiveWeightSumForSchedule1.setWeightOfObjectives(weights);
    ObjectiveWeightSumForSchedule1.calcObjective();
    Population = ObjectiveWeightSumForSchedule1.getPopulation();

    //calculate fitness values
    Fitness.setData(Population, numberOfObjs);
    Fitness.calculateFitness();
    Population = Fitness.getPopulation();
    return Population;
  }

  public double[] getWeight(){
    return weights;
  }
}