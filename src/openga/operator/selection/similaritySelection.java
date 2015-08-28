package openga.operator.selection;
import openga.chromosomes.*;
import openga.util.algorithm.chromosomeSimilarity;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: The selection is basically done by binary tournament and similarity mating scheme. 2005</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class similaritySelection extends binaryTournament {
  public similaritySelection() {
  }

  public int numAlpha = 3, numBeta = 5;//the population size of alpha and beta respectively.
  populationI alphaPop, betaPop;
  int similarityMatrix[][];
  binaryTournament binaryTournament1 = new binaryTournament();
  chromosomeSimilarity chromosomeSimilarity1 = new chromosomeSimilarity();

  public void setData(int similarityMatrix[][]){
    this.similarityMatrix = similarityMatrix;

    if(numBeta > similarityMatrix.length){
      numBeta = similarityMatrix.length;
    }
  }

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
      int representiveSoln = getClosestSoln(averageObjs, alphaPop);
      int getFarestPoint = getFarestSoln(representiveSoln, alphaPop);

      //for beta population.
      int representiveSoln2 = getClosetSoln(alphaPop.getSingleChromosome(getFarestPoint), betaPop);

      //set chromosome of alpha and beta population into the new population.
      newPop.setSingleChromosome(i, alphaPop.getSingleChromosome(getFarestPoint));
      i ++;
      if(i < sizeOfPop){
        newPop.setSingleChromosome(i, betaPop.getSingleChromosome(representiveSoln2));
      }
    }
  }

  //to generate the alpha and beta population.
  public final populationI getTournamentPop(int size, populationI _originalPop){
    binaryTournament1.setData(size, _originalPop);
    binaryTournament1.startToSelect();
    return binaryTournament1.getSelectionResult();
  }

  /**
   * To calculate the average vector of each objective.
   * @param _pop
   * @return
   */
  public final double[] getAverageObjectiveValues(populationI _pop){
    int popSize = _pop.getPopulationSize(), numberOfObjectives = _pop.getNumberOfObjectives();
    double objs[][] = new double[numberOfObjectives][popSize];
    double averageValues[] = new double[numberOfObjectives];

    //calculate the average objective one-by-one.
    for(int i = 0 ; i < numberOfObjectives ; i ++ ){
      for(int j = 0 ; j < popSize ; j ++ ){
        objs[i][j] = _pop.getObjectiveValues(j)[i];
      }
    }

    openga.util.algorithm.getSum getSum1 = new openga.util.algorithm.getSum();
    for(int i = 0 ; i < numberOfObjectives ; i ++ ){
      getSum1.setData(objs[i]);
      averageValues[i] = getSum1.getSumResult()/popSize;
    }
    return averageValues;
  }

  private int getFarestSoln(int _index, populationI _pop){
    int _similarity[] = new int[_pop.getPopulationSize()];

    //to calculate the similarity between the representive chromosome and others in alpha population.
    for(int i = 0 ; i < _pop.getPopulationSize() ; i ++ ){
      _similarity[i] = chromosomeSimilarity1.calcValue(_pop.getSingleChromosome(_index), _pop.getSingleChromosome(i));
    }

    //to get the "farest" solution
    double max = 0;
    int index = 0;
    for(int i = 0 ; i < _pop.getPopulationSize() ; i ++ ){
      if(max <= _similarity[i]){
        max = _similarity[i];
        index = i;
      }
    }
    return index;
  }

  /**
   * To get a solution closed to a vector.
   * @param vals Objective function value vector.
   * @param _pop To find a solution in the population.
   * @return
   */
  private int getClosestSoln(double vals[], populationI _pop){
    double distance = Double.MAX_VALUE;
    int index = 0;
    int popSize = _pop.getPopulationSize(), numberOfObjectives = _pop.getNumberOfObjectives();

    for(int i = 0 ; i < popSize ; i ++ ){
      double tempDist = 0;
      for(int j = 0 ; j < numberOfObjectives ; j ++ ){
        tempDist += Math.sqrt(vals[j] - _pop.getObjectiveValues(i)[j]);
      }
      if(tempDist < distance){
        distance = tempDist;
        index = i;
      }
    }
    return index;
  }

  private int getClosetSoln(chromosome _chromosome1, populationI _pop){
    double distance = Double.MAX_VALUE;
    int index = 0;
    int _similarity[] = new int[_pop.getPopulationSize()];

    //to calculate the similarity between the representive chromosome and others in alpha population.
    for(int i = 0 ; i < _pop.getPopulationSize() ; i ++ ){
      _similarity[i] = chromosomeSimilarity1.calcValue(_chromosome1, _pop.getSingleChromosome(i));
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
}