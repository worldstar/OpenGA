package openga.MainProgram;
import openga.chromosomes.*;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class singleThreadGAwithProbabilityMatrix extends singleThreadGA implements probabilityMatrixI{
  public singleThreadGAwithProbabilityMatrix() {
  }
  int startingGeneration = 500;
  int interval = 20;
  int strategy = 2;
  boolean applyEvaporation;
  String evaporationMethod = "constant";//constant, method1, method2
  populationI artificialPopulation = new population();
  openga.util.printClass printClass1 = new openga.util.printClass();

  public void setProbabilityMatrixData(int startingGeneration, int interval){
    this.startingGeneration = startingGeneration;
    this.interval = interval;
  }

  public void setSequenceStrategy(int strategy){
    this.strategy = strategy;
  }

  public void setEvaporationMethod(boolean applyEvaporation, String evaporationMethod){
    this.applyEvaporation = applyEvaporation;
    this.evaporationMethod = evaporationMethod;
  }

  public void setGuidedMutationInfo(double lamda, double beta){
    System.out.println("This method is not implemented here.");
    System.exit(0);
  }
  /**
   * Main procedure starts here. You should ensure the encoding of chromosome is done.
   */
  public void startGA(){
    Population = initialStage();
    intialArttificalPopulation();
    //evaluate the objective values and calculate fitness values
    //clone
    if(applyClone == true){
      Population = cloneStage(Population);
    }

    ProcessObjectiveAndFitness();
    archieve = findParetoFront(Population, 0);
    //printResults();

    for(int i = 0 ; i < generations ; i ++ ){
      currentGeneration = i;
      /*
      String generationResults = "";
      if(i % 10 == 0){
        generationResults = i+"\t"+getBest()+"\t"+getPopulationMeanObjValue()+"\n";
        System.out.print(generationResults);
      }
      else if(i == generations - 1){
        generationResults = i+"\t"+getBest()+"\t"+getPopulationMeanObjValue()+"\n"+"\n";
        System.out.print(generationResults);
      }
      writeFile("SingleMachineSKSAC2GA_"+length, generationResults);
      */
      /*
      if(i == 0 || i == 500 || i == 999){
        openga.operator.miningGene.probabilityMatrix probMatrix1 = new openga.operator.miningGene.probabilityMatrix();
        probMatrix1.setData(Population, artificialPopulation);//archieve, Population, artificialPopulation
        probMatrix1.setDuplicateRate(1);
        probMatrix1.setStrategy(strategy);
        printClass1.printMatrix("generation "+i, probMatrix1.backupInformation());
      }
      */

      if(i < startingGeneration || i % interval != 0){
        SGA();
      }
      else{
        //System.out.println("generations "+i);
        ProbabilityMatrix();
        //hybridProcess();
        //System.exit(0);
      }
    }
  }

  public void SGA(){
    Population = selectionStage(Population);

    //Crossover
    //System.out.println("Crossover");
    Population = crossoverStage(Population);

    //Mutation
    //System.out.println("mutationStage");
    Population = mutationStage(Population);

    //clone
    Population = cloneStage(Population);
    evalulatePop(Population);
  }

  public void ProbabilityMatrix(){
    Population = selectionStage(Population);
    openga.operator.miningGene.probabilityMatrix probMatrix1 = new openga.operator.miningGene.probabilityMatrix();
    probMatrix1.setData(Population, artificialPopulation);//archieve, Population, artificialPopulation
    probMatrix1.setDuplicateRate(1);
    probMatrix1.setStrategy(strategy);
    probMatrix1.setEvaporationMethod(applyEvaporation, evaporationMethod);
    probMatrix1.startStatistics();
    artificialPopulation = cloneStage(artificialPopulation);
    evalulatePop(artificialPopulation);
    Population = replacementStage(Population, artificialPopulation);
  }

  public void hybridProcess(){
    Population = selectionStage(Population);
    openga.operator.miningGene.probabilityMatrix probMatrix1 = new openga.operator.miningGene.probabilityMatrix();
    probMatrix1.setData(Population, artificialPopulation);//archieve, Population, artificialPopulation
    probMatrix1.setDuplicateRate(1);
    probMatrix1.setStrategy(strategy);
    probMatrix1.setEvaporationMethod(applyEvaporation, evaporationMethod);
    probMatrix1.foundemental();

    for(int i = 0 ; i < Population.getPopulationSize()/2 ; i ++ ){
      probMatrix1.generateChromosome(probMatrix1.dumpArray(probMatrix1.getMarkovContainer()), Population.getSingleChromosome(i));
    }
    //System.out.println("Crossover");
    Population = crossoverStage(Population);

    //Mutation
    //System.out.println("mutationStage");
    Population = mutationStage(Population);

    //clone
    Population = cloneStage(Population);
    evalulatePop(Population);

  }

  public populationI cloneStage(populationI _Population){
    openga.operator.clone.cloneI clone1 = new openga.operator.clone.solutionVectorCloneWithMutation();
    clone1.setData(_Population);
    clone1.setArchive(archieve);
    clone1.startToClone();
    //System.out.println(clone1.getNumberOfOverlappedSoln());
    return clone1.getPopulation();
  }

  public void evalulatePop(populationI originalSet){
    //evaluate the objective values and calculate fitness values
    ProcessObjectiveAndFitness(originalSet);
    populationI tempFront = (population)findParetoFront(Population,0);
    archieve = updateParetoSet(archieve, tempFront);
  }

  public void intialArttificalPopulation(){
    artificialPopulation.setGenotypeSizeAndLength(encodeType, fixPopSize, length, numberOfObjs);
    artificialPopulation.initNewPop();
  }

  public populationI replacementStage(populationI parent, populationI offspring){
    openga.operator.selection.MuPlusLamdaSelection2 replace1 = new openga.operator.selection.MuPlusLamdaSelection2();
    replace1.setData(parent.getPopulationSize(), parent);
    replace1.setSecondPopulation(offspring);
    replace1.startToSelect();
    return replace1.getSelectionResult();
  }

  public populationI ProcessObjectiveAndFitness(populationI originalSet){
    //evaluate the objective values
    for(int i = 0 ; i < ObjectiveFunction.length ; i ++ ){
      //System.out.println("The obj "+i);
      ObjectiveFunction[i].setData(originalSet, i);
      ObjectiveFunction[i].calcObjective();
      originalSet = ObjectiveFunction[i].getPopulation();
    }

    //calculate fitness values
    Fitness.setData(originalSet, numberOfObjs);
    Fitness.calculateFitness();
    originalSet = Fitness.getPopulation();
    return originalSet;
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