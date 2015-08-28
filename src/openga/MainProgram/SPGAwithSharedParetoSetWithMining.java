package openga.MainProgram;
import openga.chromosomes.*;
import openga.operator.clone.*;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class SPGAwithSharedParetoSetWithMining extends SPGAwithSharedParetoSet {
  public SPGAwithSharedParetoSetWithMining() {
  }
  openga.operator.miningGene.geneStatistics2 miningGene1 = new openga.operator.miningGene.geneStatistics2();

  public void InitialRun(){
    Population = initialStage();
    //evaluate the objective values and calculate fitness values
    ProcessObjectiveAndFitness();
    duplicatedPopulation = new population();
    duplicatedPopulation.setGenotypeSizeAndLength(encodeType, 1,
                                                  length,
                                                  numberOfObjs);
    duplicatedPopulation.initNewPop();
  }

  public void startGA(){
    if(isInitialRun = true){//check whether it's initially run. If so, initiate the population.
      if(archieve != null){
        //System.out.println("archieve "+archieve.getPopulationSize());
        populationI tempFront = (population)findParetoFront(Population,0);
        archieve = updateParetoSet(archieve, tempFront);
      }
      isInitialRun = false;
    }
    miningGeneStage(Population);
    Population = selectionStage(Population);

    //Crossover
    Population = crossoverStage(Population);

    //Mutation
    Population = mutationStage(Population);

    //clone
    if(applyClone == true){
      Population = cloneStage(Population);
    }

    //evaluate the objective values and calculate fitness values
    ProcessObjectiveAndFitness();

    populationI tempFront = (population)findParetoFront(Population,0);
    archieve = updateParetoSet(archieve, tempFront);

    tempFront = (population)findParetoFront(duplicatedPopulation,0);
    archieve = updateParetoSet(archieve, tempFront);
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
    Selection.setSecondPopulation(duplicatedPopulation);
    Selection.startToSelect();
    Population = Selection.getSelectionResult();
    return Population;
  }

  public void miningGeneStage(populationI Population){
    miningGene1.setData(Population, duplicatedPopulation);
    miningGene1.setArchive(archieve);
    miningGene1.startStatistics();
    duplicatedPopulation = miningGene1.getPopulation();
    //ProcessObjectiveAndFitness(duplicatedPopulation);
  }

}