package openga.MainProgram;
import openga.chromosomes.*;
import openga.operator.clone.*;
import openga.ObjectiveFunctions.*;
import openga.Fitness.*;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class SPGAwithSharedParetoSet extends fixWeightScalarization{
  public SPGAwithSharedParetoSet() {
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
      //Population = initialStage();
      //evaluate the objective values and calculate fitness values
      //ProcessObjectiveAndFitness();
      //if there are some pareto solns that has been found, we might update the pareto set.
      if(archieve != null){
        populationI tempFront = (population)findParetoFront(Population, 0);
        archieve = updateParetoSet(archieve, tempFront);
      }
      else{
        initFirstArchieve();
      }
      isInitialRun = false;
    }

    Population = selectionStage(Population);

    //Crossover
    //Population = crossoverStage(Population);
    offspring = crossoverStage(Population);

    //Mutation
    //Population = mutationStage(Population);
    offspring = mutationStage(offspring);

    //clone
    if(applyClone == true){
      //Population = cloneStage(Population);
      offspring = cloneStage(offspring);
    }

    //evaluate the objective values and calculate fitness values
    replacementStrategy = 1;
    if(replacementStrategy == 0){
      ProcessObjectiveAndFitness();
      populationI tempFront = (population)findParetoFront(offspring, 0);
      archieve = updateParetoSet(archieve, tempFront);
      Population = offspring;
    }
    else{
      ProcessObjectiveAndFitness(offspring);
      populationI tempFront = (population)findParetoFront(offspring, 0);
      archieve = updateParetoSet(archieve, tempFront);
      Population = replacementStage(Population, offspring);
    }
/*
    //evaluate the objective values and calculate fitness values
    ProcessObjectiveAndFitness();
    populationI tempFront = (population)findParetoFront(Population,0);
    archieve = updateParetoSet(archieve, tempFront);
*/
    //Population = diversityAsObject(Population, archieve);
/*
  openga.operator.mutation.localSearchBySwapMutation localSearchBySwapMutation1 = new openga.operator.mutation.localSearchBySwapMutation();
  localSearchBySwapMutation1.setData(1, archieve);
  localSearchBySwapMutation1.setObjectives(ObjectiveFunction);
  localSearchBySwapMutation1.startMutation();
  archieve = localSearchBySwapMutation1.getMutationResult();
*/
  }

  public populationI selectionStage(populationI Population){
    //selection, we may try to modify the population size
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
    Population = Selection.getSelectionResult();
    return Population;
  }

  public populationI cloneStage(populationI Population){
    //cloneI clone1 = new solutionVectorCloneWithMutation();//solutionVectorClone solutionVectorCloneWithMutation
    clone1.setData(Population);
    clone1.setArchive(archieve);
    clone1.startToClone();
    //System.out.println(clone1.getNumberOfOverlappedSoln());
    return clone1.getPopulation();
  }

  public populationI diversityAsObject(populationI Population, populationI archive){
    openga.util.printClass printClass1 = new openga.util.printClass();
    openga.ObjectiveFunctions.forExtremeSolnDistance forExtremeSolnDistance1 = new openga.ObjectiveFunctions.forExtremeSolnDistance();
    forExtremeSolnDistance1.setData(Population, 0);
    forExtremeSolnDistance1.setElite(archive);
    forExtremeSolnDistance1.calcObjective();
    double newObj[] = forExtremeSolnDistance1.getObjectiveValues(0);
    openga.util.algorithm.normalize normalize1 = new openga.util.algorithm.normalize();
    newObj = normalize1.getData1(newObj);
    double weight1 = (double)currentGeneration/((double)generations*2);
    weight1 *= 0.2;
    double weight2 = 1 - weight1;
    for(int i = 0 ; i < fixPopSize ; i ++ ){
      Population.setFitness(i, weight1*newObj[i] + weight2*Population.getFitness(i));
    }
    return Population;
  }

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
    twoPopReplacement1.setThresholdValues(0.8, 0.3);//0.7 0.2
    offspring = twoPopReplacement1.replacementStage(parent, offspring);
    return offspring;
  }

  public populationI getArchieve(){
    return archieve;
  }

}