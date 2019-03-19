package openga.operator.crossover;

import java.util.logging.Level;
import java.util.logging.Logger;
import openga.chromosomes.*;
import openga.operator.miningGene.PopulationToInstances;
import weka.classifiers.Classifier;
import weka.core.Instances;

/**
 * <p>
 * Title: The OpenGA project</p>
 * <p>
 * Description: The project is to build general framework of Genetic algorithm
 * and problem independent.</p>
 * <p>
 * Copyright: Copyright (c) 2004</p>
 * <p>
 * Company: Yuan-Ze University</p>
 *
 * @author Chen, Shih-Hsin
 * @version 1.0
 */
public class PMX_Weka extends twoPointCrossover2 implements CrossoverI_Weka {

  int numberOfTournament = 7;
  chromosome newChromosomes[] = new chromosome[1];
  Classifier Regression;

  @Override
  public void setWekainfo(Classifier Regression) {
    this.Regression = Regression;
  }

  //start to crossover
  public void startCrossover() {
    newChromosomes[0] = new chromosome();
    newChromosomes[0].setGenotypeAndLength(originalPop.getEncodedType(), originalPop.getLengthOfChromosome(), originalPop.getNumberOfObjectives());
    newChromosomes[0].initChromosome();
    for (int i = 0; i < popSize; i++) {
      //test the probability is larger than crossoverRate.
      if (Math.random() <= crossoverRate) {
        //to get the other chromosome to crossover
//        int index2 = getCrossoverChromosome(i);
        setCutpoint();
        checkCutPoints(i);
//        copyElements(i, index2);
//        copyElements(index2, i);
//         System.out.println("Finish the crossover");
      }
    }
  }

  private void checkCutPoints(int selectedSoln) {
    double maxProb = 0.0;
    for (int i = 0; i < numberOfTournament; i++) {
      int index2 = getCrossoverChromosome(selectedSoln);//to get a chromosome to be mated.
      copyElements(selectedSoln, index2, newChromosomes[0]);

      PopulationToInstances WekaInstances = new PopulationToInstances();
      double prediction = 0;
      try {
        Instances chromosome_prediction = WekaInstances.chromosomeToInstances(newChromosomes[0]); // Instances init_Dataset
        prediction = Regression.classifyInstance(chromosome_prediction.instance(0));
      } catch (Exception ex) {
        Logger.getLogger(twoPointCrossover2_Weka.class.getName()).log(Level.SEVERE, null, ex);
      }

      if (maxProb < prediction) {
        maxProb = prediction;
        newPop.setSingleChromosome(selectedSoln, newChromosomes[0]);
      }
    }
  }

  private void copyElements(int index1, int index2, chromosome child) {
    //to modify the first chromosome between the index1 to index2, which genes
    //is from chromosome 2.
    int counter = 0;
    //exchange the gene values between the two points

    for (int i = cutPoint1; i <= cutPoint2; i++) {
      int gene = originalPop.getSingleChromosome(index2).genes[i];
//      newPop.setGene(index1, i, gene);
      child.setGeneValue(i, gene);
    }

    //copy other gene that before the cutpoint1 and after the cutpoint2
    for (int i = 0; i < cutPoint1; i++) {
      while (checkConflict(originalPop.getSingleChromosome(index1).genes[counter], originalPop.getSingleChromosome(index2).genes) == true) {
        counter++;
      }
//      newPop.setGene(index1, i, originalPop.getSingleChromosome(index1).genes[counter]);
      child.setGeneValue(i, originalPop.getSingleChromosome(index1).genes[counter]);
//      System.out.println("counter:"+counter);
      counter++;
    }

    for (int i = cutPoint2 + 1; i < chromosomeLength; i++) {
      while (checkConflict(originalPop.getSingleChromosome(index1).genes[counter], originalPop.getSingleChromosome(index2).genes) == true) {
        counter++;
      }
//      newPop.setGene(index1, i, originalPop.getSingleChromosome(index1).genes[counter]);
      child.setGeneValue(i, originalPop.getSingleChromosome(index1).genes[counter]);
      counter++;
    }
  }

  
  
  private boolean checkConflict(int newGene, int _chromosome[]) {
    boolean hasConflict = false;
    for (int i = cutPoint1; i <= cutPoint2; i++) {
      if (newGene == _chromosome[i]) {
        return true;
      }
    }
    return hasConflict;

  }
}
