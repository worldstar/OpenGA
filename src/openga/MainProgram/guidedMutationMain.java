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
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class guidedMutationMain extends singleThreadGAwithProbabilityMatrix implements probabilityMatrixI{
  public guidedMutationMain() {
  }
  PBIL PBIL1;
  int strategy = 1;
  double container[][];
  double lamda = 0.9; //learning rate
  double beta = 0.9;
  GuidedMuation GuidedMuation1;
  populationI artificialPopulation = new population();

  public void setGuidedMutationInfo(double lamda, double beta){
    this.lamda = lamda;
    this.beta = beta;
  }

  public void startGA(){
    Population = initialStage();
    intialArttificalPopulation();
    //evaluate the objective values and calculate fitness values
    ProcessObjectiveAndFitness();
    //intialOffspringPopulation();
    archieve = findParetoFront(Population, 0);
    PBIL1 = new PBIL(Population, lamda);
    container = PBIL1.getContainer();

    for(int i = 0 ; i < generations ; i ++ ){
      //System.out.println("generations "+i);
      currentGeneration = i;
      Population = selectionStage(Population);

      //collect gene information, it's for mutation matrix
      PBIL1.setData(Population);
      PBIL1.startStatistics();
      container = PBIL1.getContainer();
      ProbabilityMatrix();

      populationI tempFront = (population)findParetoFront(Population, 0);
      archieve = updateParetoSet(archieve, tempFront);
    }
  }

  public void ProbabilityMatrix(){
      Population = selectionStage(Population);
      openga.operator.miningGene.GuidedMuation probMatrix1 = new openga.operator.miningGene.GuidedMuation();
      probMatrix1.setData(Population, artificialPopulation);//archieve, Population, artificialPopulation
      probMatrix1.setDuplicateRate(1);
      probMatrix1.setStrategy(strategy);
      probMatrix1.setEDAinfo(container, archieve, beta);
      probMatrix1.startStatistics();
      artificialPopulation = cloneStage(artificialPopulation);
      evalulatePop(artificialPopulation);
      Population = replacementStage(Population, artificialPopulation);
  }

  public void intialArttificalPopulation(){
    artificialPopulation.setGenotypeSizeAndLength(encodeType, fixPopSize, length, numberOfObjs);
    artificialPopulation.initNewPop();
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