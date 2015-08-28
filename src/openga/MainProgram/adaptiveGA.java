package openga.MainProgram;
import openga.chromosomes.*;
import openga.operator.selection.*;
import openga.operator.crossover.*;
import openga.operator.mutation.*;
import openga.ObjectiveFunctions.*;
import openga.Fitness.*;
import openga.util.*;


/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class adaptiveGA extends SPGAwithSharedParetoSet {
  public adaptiveGA() {
  }

  /**
   * Main procedure starts here. You should ensure the encoding of chromosome is done.
   */
  /**
   * Main procedure starts here. You should ensure the encoding of chromosome is done.
   */
  public void startGA(){
    if(isInitialRun = true){//check whether it's initially run. If so, initiate the population.
      //if there are some pareto solns that has been found, we might update the pareto set.
      if(archieve != null){
        //System.out.println("archieve "+archieve.getPopulationSize());
        populationI tempFront = (population)findParetoFront(Population,0);
        archieve = updateParetoSet(archieve, tempFront);
      }
      isInitialRun = false;
    }

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
    adaptiveStage(0);//to activate the population-diversity measure.
  }


  public void adaptiveStage(int generation){
    double targetDiversity = 0.5;
    openga.adaptive.diversityMeasure.diversityMeasureI diversityMeasure1 = new openga.adaptive.diversityMeasure.HammingDistance();
    openga.adaptive.strategy.KennyZhu1 KennyZhu11 = new openga.adaptive.strategy.KennyZhu1();
    diversityMeasure1.setData(Population);
    diversityMeasure1.startCalcDiversity();
    double genoDiversityValue = diversityMeasure1.getPopulationDiversityValue();

    KennyZhu11.setData(Population, genoDiversityValue, targetDiversity, crossoverRate);

    KennyZhu11.startCalcNewRate();
    double temp1 = Math.min(KennyZhu11.getUpdatedRate(), 1);
    crossoverRate = temp1;
    //System.out.println("KennyZhu11.getDivisity() "+KennyZhu11.getDivisity());

    KennyZhu11.setData(Population, genoDiversityValue, targetDiversity, mutationRate);
    KennyZhu11.startCalcNewRate();
    mutationRate = Math.min(KennyZhu11.getUpdatedRate(), 0.7);

    //System.out.println("end adaptiveStage\n");
    /*
    if(generation % 5 == 0){
      writeFile("diversityByGenerations", generation+"\t"+KennyZhu11.getDivisity()+"\n");
    }
    */

  }


}