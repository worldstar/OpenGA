package openga.MainProgram;
import openga.chromosomes.*;
import openga.ObjectiveFunctions.*;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class SPGAwithSharedParetoSetAndMuPlusLamda extends SPGAwithSharedParetoSet {
  public SPGAwithSharedParetoSetAndMuPlusLamda() {
  }
  boolean isInitialRun = true;
  public static populationI archieve;
  public populationI offspring;

  public void initFirstArchieve(){//to find the initial front.
    archieve = findParetoFront(Population, 0);
  }

  public void InitialRun(){
    Population = initialStage();
    //evaluate the objective values and calculate fitness values
    ProcessObjectiveAndFitness();
  }

  public void startGA(){
    if(isInitialRun = true){//check whether it's initially run. If so, initiate the population.
      if(archieve != null){
        //System.out.println("archieve "+archieve.getPopulationSize());
        populationI tempFront = (population)findParetoFront(Population,0);
        archieve = updateParetoSet(archieve, tempFront);
      }
      isInitialRun = false;
      Population = selectionStage(Population, Population);
    }
    else{
      Population = selectionStage(Population, offspring);
    }

    //Crossover
    offspring = crossoverStage(Population);

    //Mutation
    offspring = mutationStage(offspring);

    //evaluate the objective values and calculate fitness values
    if(replacementStrategy == 0){
      ProcessObjectiveAndFitness();
      populationI tempFront = (population)findParetoFront(Population,0);
      archieve = updateParetoSet(archieve, tempFront);
      Population = offspring;
    }
    else{
      ProcessObjectiveAndFitness(offspring);
      populationI tempFront = (population)findParetoFront(offspring,0);
      archieve = updateParetoSet(archieve, tempFront);
      Population = replacementStage(Population, offspring);
    }
  }

  public populationI selectionStage(populationI parent, populationI child){
    //selection, we may try to modify the population size
    Population = combinePopuplation(parent, child);
    //re-calculate it's fitness
    ProcessObjectiveAndFitness();
    Selection.setData(fixPopSize, Population);

    //To assign elitism data.
    if(archieve.getPopulationSize() < 1){
      Selection.setElite(archieve, 0);
    }
    else if(fixPopSize*elitism > archieve.getPopulationSize()){
      Selection.setElite(archieve, archieve.getPopulationSize());
    }
    else{
      Selection.setElite(archieve, (int)(fixPopSize*elitism));
    }

    Selection.startToSelect();
    return Selection.getSelectionResult();
  }

/*
  private populationI fitnessOfPopulation(populationI combinedPop){
    //calculate fitness values
    Fitness.setData(combinedPop, numberOfObjs);
    Fitness.calculateFitness();
    openga.util.printClass printClass1 = new openga.util.printClass();
    printClass1.printMatrix("selectionSort1", combinedPop.getFitnessValueArray());
    return Fitness.getPopulation();
  }
*/

  public populationI ProcessObjectiveAndFitness(populationI originalSet){
    //evaluate the objective values
    ObjectiveWeightSumForSchedule ObjectiveWeightSumForSchedule1 = new
        ObjectiveWeightSumForSchedule();
    ObjectiveWeightSumForSchedule1.setObjectiveClass(ObjectiveFunction);
    ObjectiveWeightSumForSchedule1.setData(originalSet, 0);
    ObjectiveWeightSumForSchedule1.setWeightOfObjectives(weights);
    ObjectiveWeightSumForSchedule1.calcObjective();
    originalSet = ObjectiveWeightSumForSchedule1.getPopulation();

    //calculate fitness values
    Fitness.setData(originalSet, numberOfObjs);
    Fitness.calculateFitness();
    originalSet = Fitness.getPopulation();
    return originalSet;
  }

  public populationI replacementStage(populationI parent, populationI offspring){
    openga.operator.selection.twoPopReplacement twoPopReplacement1 = new openga.operator.selection.twoPopReplacement();
    twoPopReplacement1.setThresholdValues(0.8, 0.3);
    offspring = twoPopReplacement1.replacementStage(parent, offspring);
    return offspring;
  }

  public populationI getArchieve(){
    return archieve;
  }

}