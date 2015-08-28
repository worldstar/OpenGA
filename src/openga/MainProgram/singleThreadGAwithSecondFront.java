package openga.MainProgram;
import openga.chromosomes.*;
import openga.operator.selection.*;
import openga.operator.crossover.*;
import openga.operator.mutation.*;
import openga.ObjectiveFunctions.*;
import openga.Fitness.*;
import openga.util.printClass;

/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class singleThreadGAwithSecondFront extends singleThreadGA {

  public populationI selectionStage(populationI Population){


    //selection, we may try to modify the population size
    Selection.setData(fixPopSize, Population);

    //To assign elitism data.
    if(archieve.getPopulationSize() < 1){
      Selection.setElite(archieve, 0);
    }
    else if(fixPopSize*elitism > archieve.getPopulationSize()){

      //expand the archieve by adding the second front.
      expandArchieve expandArchieve1 = new expandArchieve();
      populationI secondFront = findSecondFront(Population);
      int selectedFromSecondFront = secondFront.getPopulationSize();
      //System.out.println(" selectedFromSecondFront "+selectedFromSecondFront);
      int totalSize = archieve.getPopulationSize() + selectedFromSecondFront;

      totalSize = Math.min(totalSize, (int)(fixPopSize*elitism));
      expandArchieve1.setTwoPopulations(archieve, secondFront, totalSize);
      expandArchieve1.startToExpand();
      Selection.setElite(expandArchieve1.getPopulation(), totalSize);
    }
    else{//it's greater than or equal
      Selection.setElite(archieve, (int)(fixPopSize*elitism));
    }
    //int tournamentSize = Math.max((generations - currentGeneration) % 100, 2) ;
    //Selection.setTournamentSize(10);
    if(currentGeneration < 200){
      Selection.setTournamentSize(15);
    }
    else{
      Selection.setTournamentSize(2);
    }
    Selection.startToSelect();
    Population = Selection.getSelectionResult();
    return Population;

  }


  public populationI findSecondFront(populationI originalSet){
    //initial the objects
    findParetoByObjectives findParetoByObjectives1 = new findParetoByObjectives();
    findParetoByObjectives1.setOriginalPop(originalSet);
    findParetoByObjectives1.startToFind();
    indexOfParetoInds = findParetoByObjectives1.getparetoSolnIndex();

    return findParetoByObjectives1.getPopulation();
  }

  public void additionalStage(){

  }

  public static void main(String[] args) {
  }
}