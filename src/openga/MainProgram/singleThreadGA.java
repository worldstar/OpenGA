package openga.MainProgram;
import openga.chromosomes.*;
import openga.operator.selection.*;
import openga.operator.crossover.*;
import openga.operator.localSearch.*;
import openga.operator.mutation.*;
import openga.operator.clone.*;
import openga.ObjectiveFunctions.*;
import openga.Fitness.*;
import openga.util.printClass;
/**
 * <p>Title: The OpenGA project</p>
 * <p>Description: To control GA entire processes.
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class singleThreadGA implements MainI{
  public singleThreadGA() {
  }
  /***
   * Basic variables of GAs.
   */
  populationI Population;
  SelectI Selection;
  CrossoverI Crossover, Crossover2;
  MutationI Mutation, Mutation2;
  cloneI clone1;
  ObjectiveFunctionI ObjectiveFunction[];
  localSearchI localSearch1;
  FitnessI Fitness;
  int numberOfObjs;
  boolean applySecCRX = false;
  boolean applySecMutation = false;
  boolean applyLocalSearch = false;
  boolean applyClone = false;
  boolean isInitialRun = true;
  /**
   * Parameters of the GA
   */
  int generations, length, initialPopSize, fixPopSize, currentGeneration = 0;
  double crossoverRate, mutationRate;
  boolean[] objectiveMinimization; //true is minimum problem.
  boolean encodeType;  //binary of realize code
  double elitism;     //the percentage of elite chromosomes
  int maxNeighborhood = 5;
  int totalExaminedSolution = 0;
  int currentUsedSolution = 0;

  //Results
  double bestObjectiveValues[];
  //the second front is dominated others points except individuals in archieve
  populationI archieve, secondFront;
  int indexOfParetoInds[]; //it indicates the index of inds are pareto soln at Population
  //printClass printClass1 = new printClass();

  String generationResults="";
  double genoDiversityValue=0;  

  /**
   * The 0 of replacementStrategy is total replacement by offstring. Otherwise, 1 means mu + lambda replacement.
   */
  int replacementStrategy = 0;

  //draw
  //static openga.util.draw.ScatterPlot demo = new openga.util.draw.ScatterPlot("Plot all points");
  populationI duplicatedPopulation;

  public void initFirstArchieve(){//to find the initial front.
  }

  public void setArchieve(populationI archieve){
    this.archieve = archieve;
  }

  public void setReplacementStrategy(int replacementStrategy){
    this.replacementStrategy = replacementStrategy;
  }

  public void setCurrentGeneration(int currentGeneration){
    this.currentGeneration = currentGeneration;
  }

  /**
   * The tournanment size is for the use of selection operator.
   */
  public void setTournamentSize(int size){
    Selection.setTournamentSize(size);
  }

  public void setMutationRate(double mutationRate){
    this.mutationRate = mutationRate;
  }

  public void setCloneOperatpr(cloneI clone1, boolean applyClone){
    this.clone1 = clone1;
    this.applyClone = applyClone;
  }

  public void updateNondominatedSon(){
    //System.out.println("archieve "+archieve.getPopulationSize());
    populationI tempFront = (population)findParetoFront(Population,0);
    this.archieve = updateParetoSet(this.archieve, tempFront);
  }

  public void InitialRun(){
  }

  public void InitialRun(boolean isRun){
    isInitialRun = isRun;
  }

  //to set basic GA components.
  public void setData(populationI Population, SelectI Selection, CrossoverI Crossover, MutationI Mutation,
                      ObjectiveFunctionI ObjectiveFunction[], FitnessI Fitness,int generations,int initialPopSize, int fixPopSize,
                      int length, double crossoverRate,double mutationRate, boolean[] objectiveMinimization,
                      int numberOfObjs, boolean encodeType,double elitism){
    this.Population = Population;
    this.Selection = Selection;
    this.Crossover = Crossover;
    this.Mutation = Mutation;
    this.ObjectiveFunction = ObjectiveFunction;
    this.Fitness = Fitness;
    this.generations = generations;
    this.initialPopSize = initialPopSize;
    this.fixPopSize = fixPopSize;
    this.length = length;
    this.crossoverRate = crossoverRate;
    this.mutationRate = mutationRate;
    this.objectiveMinimization = objectiveMinimization;
    this.numberOfObjs = numberOfObjs;
    this.encodeType = encodeType;
    this.elitism = elitism;
    archieve = null;
  }

  public void setSecondaryCrossoverOperator(CrossoverI Crossover2, boolean applySecCRX){
    this.Crossover2 = Crossover2;
    this.applySecCRX = applySecCRX;
  }

  public void setSecondaryMutationOperator(MutationI Mutation2, boolean applySecMutation){
    this.Mutation2 = Mutation2;
    this.applySecMutation = applySecMutation;
  }

  public void setLocalSearchOperator(localSearchI localSearch1, boolean applyLocalSearch, int maxNeighborhood){
    this.localSearch1 = localSearch1;
    this.applyLocalSearch = applyLocalSearch;
    this.maxNeighborhood = maxNeighborhood;
  }

  /**
   * Main procedure starts here. You should ensure the encoding of chromosome is done.
   */
  public void startGA(){
    Population = initialStage();
    //evaluate the objective values and calculate fitness values
    ProcessObjectiveAndFitness();
    archieve = findParetoFront(Population,0);
    //printResults();
    //System.exit(0);
/*
    for(int k = 0 ; k < Population.getPopulationSize() ; k ++ ){
      System.out.print(k+": "+Population.getSingleChromosome(k).toString1()+"\t"+Population.getSingleChromosome(k).getObjValue()[0]+"\t"+Population.getSingleChromosome(k).getFitnessValue()+"\n");
    }
*/

    for(int i = 0 ; i < generations ; i ++ ){
      /*
//      System.out.println("generations "+i);
      String generationResults = "";
      if(i % 10 == 0){
        generationResults = i+"\t"+getBest()+"\t"+getPopulationMeanObjValue()+"\n";
        System.out.print(generationResults);
      }
      else if(i == generations - 1){
        generationResults = i+"\t"+getBest()+"\t"+getPopulationMeanObjValue()+"\n"+"\n";
        System.out.print(generationResults);
      }
      writeFile("flowshopSGA_"+length, generationResults);
      */
      currentGeneration = i;
      Population = selectionStage(Population);

      //collect gene information, it's for mutation matrix
      //Mutation.setData(Population);

      //Crossover
      //System.out.println("Crossover");
      Population = crossoverStage(Population);

      //Mutation
      //System.out.println("mutationStage");
      Population = mutationStage(Population);

      //clone
      if(applyClone == true){
        Population = cloneStage(Population);
      }

      //evaluate the objective values and calculate fitness values
      ProcessObjectiveAndFitness();

      populationI tempFront = (population)findParetoFront(Population,0);
      archieve = updateParetoSet(archieve,tempFront);
      //additionalStage();
      if (applyLocalSearch == true && i % 10 == 0 ) {
                localSearchStage(1);
      } 
    }
    //printResults();
  }
  
  public void localSearchStage(int iteration) {      
        localSearch1.setData(Population, totalExaminedSolution, maxNeighborhood);
        localSearch1.setData(Population, archieve, currentUsedSolution, iteration);
        localSearch1.setObjectives(ObjectiveFunction);
        localSearch1.startLocalSearch();
        currentUsedSolution += localSearch1.getCurrentUsedSolution();
    } 

  public populationI initialStage(){
    //Population = new population();
    Population.setGenotypeSizeAndLength(encodeType, initialPopSize, length, numberOfObjs);
    Population.createNewPop();

    return Population;
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

  public populationI crossoverStage(populationI Population){
    Crossover.setData(crossoverRate, Population);
    Crossover.startCrossover();
    Population = Crossover.getCrossoverResult();

    return Population;
  }

  public populationI mutationStage(populationI Population){
    Mutation.setData(mutationRate, Population);
    Mutation.startMutation();
    Population = Mutation.getMutationResult();

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

  public populationI ProcessObjectiveAndFitness(){
    //evaluate the objective values
    for(int i = 0 ; i < ObjectiveFunction.length ; i ++ ){
      //System.out.println("The obj "+i);
      ObjectiveFunction[i].setData(Population, i);
      ObjectiveFunction[i].calcObjective();
      Population = ObjectiveFunction[i].getPopulation();
    }

    //calculate fitness values
    Fitness.setData(Population, numberOfObjs);
    Fitness.calculateFitness();
    Population = Fitness.getPopulation();
    return Population;
  }


  public final populationI findParetoFront(populationI originalSet, int generation){
    boolean allMinimization = true, allMaximization = true;
    for(int i = 0 ; i < objectiveMinimization.length ; i ++ ){//objectiveMinimization
      
      if(objectiveMinimization[i] == true){
        allMaximization = false;
        break;
      }
    }

    for(int i = 0 ; i < objectiveMinimization.length ; i ++ ){//objectiveMinimization
      if(objectiveMinimization[i] == false){
        allMinimization = false;
        break;
      }
    }

    if(allMinimization == true){
      findParetoByObjectives findParetoByObjectives1 = new findParetoByObjectives();
      //initial the objects
      findParetoByObjectives1.setOriginalPop(originalSet);
      findParetoByObjectives1.setEliteNumber((int)(elitism*fixPopSize));
      findParetoByObjectives1.startToFind();
      indexOfParetoInds = findParetoByObjectives1.getparetoSolnIndex();
      return findParetoByObjectives1.getPopulation();
    }
    else{
      findParetoByObjectivesMaxMax findParetoByObjectives1 = new findParetoByObjectivesMaxMax();
      //initial the objects
      findParetoByObjectives1.setOriginalPop(originalSet);
      findParetoByObjectives1.setEliteNumber( (int) (elitism * fixPopSize));
      findParetoByObjectives1.startToFind();
      indexOfParetoInds = findParetoByObjectives1.getparetoSolnIndex();
      return findParetoByObjectives1.getPopulation();
    }
/*
    openga.applications.data.flowShop data = new openga.applications.data.flowShop();
    //draw the pareto set
    if(generation  == 1){
      //demo.setXYData(data.getReferenceSet2(), duplicatedPopulation.getObjectiveValueArray());
      demo.setXYData(data.getReferenceSet3());
      demo.setParetoSetData(findParetoByObjectives1.getParetoObjectives());
      demo.drawMethod();
      demo.setVisible(true);
    }

    if(generation  == 1){
      //demo.setXYData(data.getReferenceSet2(), duplicatedPopulation.getObjectiveValueArray());
      demo.setXYData(findParetoByObjectives1.getParetoObjectives());
      //demo.setParetoSetData(findParetoByObjectives1.getParetoObjectives());

      demo.setParetoSetData(duplicatedPopulation.getObjectiveValueArray());
      demo.drawMethod();
      demo.setVisible(true);
    }
*/
  }

  public final populationI updateParetoSet(populationI originalSet, populationI tempParetoFront){
    //combine the tempParetoFront with original archieve.
    //System.out.println("originalSet.getPopulationSize(); "+originalSet.getPopulationSize());
    populationI newSet = combinePopuplation(originalSet, tempParetoFront);

    //find the pareto set of the newSet.
    newSet = findParetoFront(newSet, 1);
    return newSet;
  }

  public populationI combinePopuplation(populationI originalSet, populationI tempParetoFront){
    combinedTwoPopulations combinedTwoPopulations1 = new combinedTwoPopulations();
    combinedTwoPopulations1.setTwoPopulations(originalSet, tempParetoFront);
    combinedTwoPopulations1.startToCombine();
    return combinedTwoPopulations1.getPopulation();
  }

  /**
   * You can add your own methods here after the three standard operators.
   */
  private void additionalStage(){

  }

  public populationI getPopulation(){
    return Population;
  }

  public populationI getArchieve(){
    return archieve;
  }

  public double[] getObjectiveValue(int index){
    return Population.getObjectiveValues(index);
  }

  /**
   * @return To return the objective value by the double array.
   */
  public double[][] getArchieveObjectiveValueArray(){
    return getArchieve().getObjectiveValueArray();
  }

  /**
   * The best objective value among the population.
   * Note: it's only for the single objctive problem.
   * @return
   */
  public double getBest(){
    double _obj = Double.MAX_VALUE;
    for(int i = 0 ; i < archieve.getPopulationSize() ; i ++ ){
      if(_obj > archieve.getObjectiveValueArray()[i][0]){
        _obj = archieve.getObjectiveValueArray()[i][0];
      }
    }
    return _obj;
  }

  public double getPopulationBestObjValue(){
    double _obj = Double.MAX_VALUE;
    for(int i = 0 ; i < Population.getPopulationSize() ; i ++ ){
      if(_obj > Population.getObjectiveValueArray()[i][0]){
        _obj = Population.getObjectiveValueArray()[i][0];
      }
    }
    return _obj;
  }

  public double getPopulationMeanObjValue(){
    double _obj = 0;
    for(int i = 0 ; i < Population.getPopulationSize() ; i ++ ){
      _obj += Population.getObjectiveValueArray()[i][0];
    }
    _obj /= Population.getPopulationSize();
    return _obj;
  }


  /**
   * To free the data of Pareto set.
   */
  public void destroidArchive(){
    archieve = null;
    Population = null;
  }

  public void printResults(){
    //to output the implementation result.
    String implementResult = "";
    implementResult = "";

    for(int k = 0 ; k < getArchieve().getPopulationSize() ; k ++ ){
      for(int j = 0 ; j < numberOfObjs ; j ++ ){//for each objectives
        implementResult += getArchieve().getObjectiveValues(k)[j]+"\t";
      }
      for(int j = 0 ; j < length ; j ++ ){//for each objectives
        implementResult += (getArchieve().getSingleChromosome(k).genes[j]+1)+" ";
      }

      implementResult += "\n";
    }
    writeFile("singleMachineArchive_"+length, implementResult);
  }

  /**
   * Write the data into text file.
   */
  public void writeFile(String fileName, String _result){
    openga.util.fileWrite1 writeLotteryResult = new openga.util.fileWrite1();
    writeLotteryResult.writeToFile(_result,fileName+".txt");
    Thread thread1 = new Thread(writeLotteryResult);
    thread1.run();
  }

  public void diversityMeasure(){
      
      openga.adaptive.diversityMeasure.diversityMeasureI diversityMeasure1 = new openga.adaptive.diversityMeasure.HammingDistance();  //GenoTypePositionDifference() HammingDistance()
      diversityMeasure1.setData(Population);
      //diversityMeasure1.CalcDiversity();
      diversityMeasure1.startCalcDiversity();
      genoDiversityValue = diversityMeasure1.getPopulationDiversityValue();
      
      
  }  
}