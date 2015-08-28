package openga.operator.selection;
import openga.chromosomes.*;
import openga.util.algorithm.similarity;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: It's the original version of mating scheme by Ishibushi.</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class similaritySelection2 extends similaritySelection {
  public similaritySelection2() {
  }

  public int numAlpha = 9, numBeta = 9;//the population size of alpha and beta respectively.
  populationI alphaPop, betaPop;
  int similarityMatrix[][];
  binaryTournament binaryTournament1 = new binaryTournament();
  similarity similarity1 = new similarity();

  /**
   * Start to select chromosomes into a new population.
   * It's depended on the alpha and beta rule.
   */
  public void startToSelect(){
    //to satisfy the sizeOfPop. It apply the elitism first.
    for(int i = 0 ; i < numberOfelitle ; i ++ ){
      int index1 = (int)(Math.random()*eliteSize);//randomly select two chromosomes to compare.
      newPop.setSingleChromosome(i, archieve.getSingleChromosome(index1));
    }

    for(int i = numberOfelitle ; i < sizeOfPop ; i ++ ){
      alphaPop = getTournamentPop(numAlpha, originalPop);//get alpha and beta population.
      betaPop = getTournamentPop(numBeta, originalPop);

      //for alpha population.
      double averageObjs[] = getAverageObjectiveValues(alphaPop);
      int getFarestPoint = getFarestSoln(averageObjs, alphaPop);

      //for beta population.
      int representiveSoln2 = getClosetSoln(alphaPop.getSingleChromosome(getFarestPoint), betaPop);

      //set chromosome of alpha and beta population into the new population.
      newPop.setSingleChromosome(i, alphaPop.getSingleChromosome(getFarestPoint));
      i ++;
      if(i < sizeOfPop){
        newPop.setSingleChromosome(i, betaPop.getSingleChromosome(representiveSoln2));
      }
    }//end the second for
  }

  private int getFarestSoln(double _averageObjs[], populationI _pop){
    similarity1.setData(_pop);
    similarity1.startCalcSimilarity();
    double _similarity[][] = similarity1.getChromosomeSimilarity();

    //to get the "farest" solution
    double max = 0;
    int index = 0;
    for(int i = 0 ; i < _pop.getPopulationSize() ; i ++ ){
      for(int j = i+1 ; j < _pop.getPopulationSize() ; j ++ ){
        if(max <= _similarity[i][j]){
          max = _similarity[i][j];
          index = i;
        }
      }
    }//end for
    return index;
  }

  private int getClosetSoln(chromosome _chromosome1, populationI _pop){
    double distance = Double.MAX_VALUE;
    int index = 0;
    double _similarity[] = new double[_pop.getPopulationSize()];

    //to calculate the similarity between the representive chromosome and others in alpha population.
    for(int i = 0 ; i < _pop.getPopulationSize() ; i ++ ){
      _similarity[i] = similarity1.calcValue(_chromosome1, _pop.getSingleChromosome(i));
    }

    //to get the "closest" solution
    double max = 0;
    for(int i = 0 ; i < _pop.getPopulationSize() ; i ++ ){
      if(distance >= _similarity[i]){
        distance = _similarity[i];
        index = i;
      }
    }
    return index;
  }

  public static void main(String[] args) {
    similaritySelection2 similaritySelection21 = new similaritySelection2();
  }

}