/*
 * To collect genetic information from a population of a zero-one integer problem (knapsack).
 */

package openga.operator.miningGene;
import openga.chromosomes.*;
/**
 *
 * @author user
 */
public class geneStatisticsZeroOne extends geneStatistics{
  public void setData(populationI originalPop,
                                            populationI duplicatedPopulation){
    this.originalPop = originalPop;
    this.duplicatedPopulation = duplicatedPopulation;
    popSize = originalPop.getPopulationSize();
    chromosomeLength = originalPop.getSingleChromosome(0).genes.length;
    //First row is for zero, and the 2nd-row is for the one
    container = new double[2][chromosomeLength];
  }

  public void startStatistics(){
    decave();
    calcAverageFitness();
    calcContainer();

    if(true){//createArtificialChromosome
      duplicatedPopulation = new population();
      duplicatedPopulation.setGenotypeSizeAndLength(originalPop.getEncodedType(), 1,
                                                    originalPop.getLengthOfChromosome(),
                                                    originalPop.getNumberOfObjectives());
      duplicatedPopulation.initNewPop();

      double temporaryContainer[][] = dumpArray();
      newSolution = new int[chromosomeLength];
      assignJobs(temporaryContainer);
      duplicatedPopulation.getSingleChromosome(0).setSolution(newSolution);
    }
  }

  public void calcContainer(){
    int counter = 0;
    for(int i = 0 ; i < popSize ; i ++ ){
      if(originalPop.getFitness(i) < avgFitness){
         for(int j = 0 ; j < chromosomeLength ; j ++ ){
           int gene = originalPop.getSingleChromosome(i).getSolution()[j];

           if(gene == 0){
            container[0][j] += 1.0;
           }
           else{
             container[1][j] += 1.0;
           }
         }//end for
         counter ++;
      }//end if
    }//end for

    //to normalize them between [0, 1].
    for(int i = 0 ; i < container.length ; i ++ ){
      for(int j = 0 ; j < container[i].length ; j ++ ){
        container[i][j] /= counter;
      }
    }
  }

  public void assignJobs(double matrix[][]){
    openga.util.algorithm.getMax getMax1 = new openga.util.algorithm.getMax();
    for(int i = 0 ; i < chromosomeLength ; i ++ ){
      int pos[] = getMax1.getDataIndex(matrix);
      int zeroOrone = pos[0];//0 or 1
      int position = pos[1];
      //System.out.println(Job+"\t"+position+"\t"+matrix[Job][position]);
      newSolution[position] = zeroOrone;
      matrix = dropColumn(position, matrix);
      //matrix = dropRow(zeroOrone, matrix);
    }
  }


  

}
