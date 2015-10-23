package openga.MainProgram;
import openga.chromosomes.*;
import openga.operator.selection.*;
import openga.operator.crossover.*;
import openga.operator.mutation.*;
import openga.operator.clone.*;
import openga.ObjectiveFunctions.*;
import openga.Fitness.*;
import openga.operator.miningGene.*;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class singleThreadGAwithEDA extends singleThreadGA implements EDAMainI{
  public singleThreadGAwithEDA() {
  }
  EDAModelBuildingI PBIL1;
  double container[][];
  double lamda = 0.9; //learning rate
  int numberOfCrossoverTournament = 2;
  int numberOfMutationTournament = 2;
  int tempNumberOfCrossoverTournament = 2;
  int tempNumberOfMutationTournament = 2;
  EDAICrossover Crossover;
  EDAIMutation Mutation;
  populationI offsrping = new population();
  int startingGenDividen = 4;

  boolean startToUseEDA = false;

  //to set basic GA components.
  public void setData(populationI Population, SelectI Selection, EDAICrossover Crossover, EDAIMutation Mutation,
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

  public void setEDAinfo(double lamda, int numberOfCrossoverTournament, int numberOfMutationTournament, int startingGenDividen, EDAModelBuildingI model){
    this.lamda = lamda;
    this.numberOfCrossoverTournament = numberOfCrossoverTournament;
    this.numberOfMutationTournament = numberOfMutationTournament;
    this.startingGenDividen = startingGenDividen;
    this.PBIL1 = model;
  }

  jsc.datastructures.PairedData PairedData;
  jsc.correlation.LinearCorrelation LinearCorrelation1;
  private double showPopCorrelationValue(populationI Population){
    double Obs[] = new double[Population.getPopulationSize()];
    double productsVals[] = new double[Population.getPopulationSize()];
    //PBILwithLapaceCorrection PBIL2 = new PBILwithLapaceCorrection(Population, lamda);
    //PBIL2.startStatistics();
    //double containerTemp[][] = PBIL2.getContainer();

    for(int i = 0 ; i < Population.getPopulationSize() ; i ++){
      //System.out.println(Population.getObjectiveValues(i)[0]+"\t"+getProductValue(i));
      Obs[i] = Population.getObjectiveValues(i)[0];
      productsVals[i] = getProductValue(i, container);
    }

    PairedData = new jsc.datastructures.PairedData(Obs, productsVals);
    LinearCorrelation1 = new jsc.correlation.LinearCorrelation(PairedData);

    System.out.println(LinearCorrelation1.getTestStatistic()+"\t"+LinearCorrelation1.getSP());

    return LinearCorrelation1.getTestStatistic();
  }

  private double getProductValue(int index1, double containerTemp[][]){
    chromosome chromsome1 = Population.getSingleChromosome(index1);
    double productValue = 1;
    for(int i = 0 ; i < chromsome1.getLength() ; i ++){
      int job1 = chromsome1.getSolution()[i];
      productValue *= containerTemp[job1][i] * 10;
    }
    return productValue;
  }

  public void startGA(){
    Population = initialStage();
    //evaluate the objective values and calculate fitness values
    ProcessObjectiveAndFitness();
    //intialOffspringPopulation();
    archieve = findParetoFront(Population, 0);
    //We initial the model in the application level
    PBIL1 = new PBILwithLapaceCorrection(Population, lamda);
    container = PBIL1.getContainer();

    for(int i = 0 ; i < generations ; i ++ ){            
      //System.out.println("generations "+i);
      currentGeneration = i;
      Population = selectionStage(Population);

        //Collect genetic information.
        PBIL1.setData(Population);
        PBIL1.startStatistics();
        container = PBIL1.getContainer();

        /*
        //double CR = showPopCorrelationValue(Population);        
        if(startToUseEDA == false){//It is because the model accuracy is not good of previous run. Test again.

          if(CR <= -0.55 && i > 10){
            startToUseEDA = true;
            //System.out.println("Start to run at " +i);
            //System.out.println(LinearCorrelation1.getTestStatistic()+"\t"+LinearCorrelation1.getSP());
          }
        }
         * */
         
      /*
      //Correlation test for the objective values with the joint probability.
      if(i == generations - 1){//(generations/startingGenDividen) (generations - 1)
        PBIL1.setData(Population);
        PBIL1.startStatistics();
        container = PBIL1.getContainer();
        showPopCorrelationValue(Population);
        i = generations;        
      }
       */
       
            
      if(startToUseEDA == false){
        tempNumberOfCrossoverTournament = 1;
        tempNumberOfMutationTournament = 1;
      }
      else{//
        tempNumberOfCrossoverTournament = numberOfCrossoverTournament;
        tempNumberOfMutationTournament = numberOfMutationTournament;        
      }
             
      //Crossover
      Population = crossoverStage(Population, container);

      //Mutation
      //System.out.println("mutationStage");
      Population = mutationStage(Population, container);
      //System.out.println("timeClock1 "+timeClock1.getExecutionTime());
      //clone
      if(applyClone == true){        
        Population = cloneStage(Population);
      }

      //evaluate the objective values and calculate fitness values
      //ProcessObjectiveAndFitness(Population);
      ProcessObjectiveAndFitness();

      populationI tempFront = (population)findParetoFront(Population, 0);
      archieve = updateParetoSet(archieve, tempFront);
      //Population = replacementStage(Population, offsrping);
      //System.out.println(getArchieve().getPopulationSize());
    }
    //printResults();
  }

  public void intialOffspringPopulation(){
    offsrping.setGenotypeSizeAndLength(encodeType, fixPopSize, length, numberOfObjs);
    offsrping.initNewPop();
  }

  public populationI crossoverStage(populationI Population, double container[][]){
    Crossover.setData(crossoverRate, Population);
    Crossover.setEDAinfo(container, tempNumberOfCrossoverTournament);
    Crossover.startCrossover();
    Population = Crossover.getCrossoverResult();

    return Population;
  }

  public populationI mutationStage(populationI Population, double container[][]){
    Mutation.setData(mutationRate, Population);
    Mutation.setEDAinfo(container, numberOfMutationTournament);
    Mutation.startMutation();
    Population = Mutation.getMutationResult();

    return Population;
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

  public populationI replacementStage(populationI parent, populationI offspring){
    openga.operator.selection.MuPlusLamdaSelection2 replace1 = new openga.operator.selection.MuPlusLamdaSelection2();
    replace1.setData(parent.getPopulationSize(), parent);
    replace1.setSecondPopulation(offspring);
    replace1.startToSelect();
    return replace1.getSelectionResult();
  }

  public void setEDAinfo(double lamda, int numberOfCrossoverTournament, int numberOfMutationTournament, int startingGenDividen){
    this.lamda = lamda;
    this.numberOfCrossoverTournament = numberOfCrossoverTournament;
    this.numberOfMutationTournament = numberOfMutationTournament;
    this.startingGenDividen = startingGenDividen;
  }

  @Override
  public void setEDAinfo(double lamda, double beta, int numberOfCrossoverTournament, int numberOfMutationTournament, int startingGenDividen) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void setFlowShopData(int numberOfJob, int numberOfMachines, int[][] processingTime) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

}