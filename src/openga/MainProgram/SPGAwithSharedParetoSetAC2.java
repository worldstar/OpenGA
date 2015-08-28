package openga.MainProgram;
import openga.chromosomes.*;
import openga.operator.clone.*;
import openga.ObjectiveFunctions.*;
import openga.Fitness.*;



/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class SPGAwithSharedParetoSetAC2 extends SPGAwithSharedParetoSet implements multiObjsProbabilityMatrixI{
  public SPGAwithSharedParetoSetAC2() {
  }
  int startingGeneration = 200;
  int interval = 50;
  int strategy = 1;
  populationI artificialPopulation = new population();

  public void setProbabilityMatrixData(int startingGeneration, int interval){
    this.startingGeneration = startingGeneration;
    this.interval = interval;
  }

  public void setSequenceStrategy(int strategy){
    this.strategy = strategy;
    intialArttificalPopulation();
  }

  public void setPopulation(populationI pop){
    this.Population = pop;
  }

  public void setEvaporationMethod(boolean applyEvaporation, String method){

  }

  public void setGuidedMutationInfo(double lamda, double beta){

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
    applyClone = true;
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
  }

  public void intialArttificalPopulation(){
    artificialPopulation.setGenotypeSizeAndLength(encodeType, fixPopSize, length, numberOfObjs);
    artificialPopulation.initNewPop();
  }

  public void ProbabilityMatrix(){
    openga.operator.miningGene.probabilityMatrix probMatrix1 = new openga.operator.miningGene.probabilityMatrix();
    probMatrix1.setData(Population, artificialPopulation);//archieve, Population, artificialPopulation
    probMatrix1.setDuplicateRate(1);
    probMatrix1.setStrategy(strategy);
    probMatrix1.startStatistics();
    artificialPopulation = cloneStage(artificialPopulation);
    evalulatePop(artificialPopulation);
    Population = replacementStage(Population, artificialPopulation);
  }

  public void evalulatePop(populationI originalSet){
    //evaluate the objective values and calculate fitness values
    ProcessObjectiveAndFitness(originalSet);
    populationI tempFront = (population)findParetoFront(originalSet, 0);
    archieve = updateParetoSet(archieve, tempFront);
  }

  public populationI replacementStage(populationI parent, populationI offspring){
    openga.operator.selection.MuPlusLamdaSelection3 replace1 = new openga.operator.selection.MuPlusLamdaSelection3();
    replace1.setData(parent.getPopulationSize(), parent);
    replace1.setSecondPopulation(offspring);
    replace1.startToSelect();
    return replace1.getSelectionResult();
  }

  public double getPopulationBest(){
    return 0;
  }

    @Override
    public void setWeight(double w1, double w2) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setLearningRate(double lamda, double beta) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setNEH(int[] NEH) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setparaData(int[][][] processingSetupTime, int numberOfMachines) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setFlowShopData(int numberOfJob, int numberOfMachines, int[][] processingTime) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}