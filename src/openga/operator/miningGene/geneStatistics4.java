package openga.operator.miningGene;
import openga.chromosomes.*;
import openga.operator.crossover.*;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class geneStatistics4 extends geneStatistics {
  public geneStatistics4() {
  }
  double votesNumber[];

  public void startStatistics(){
    int length = originalPop.getLengthOfChromosome();
    votesNumber = new double[length];
    decave();
    calcAverageFitness();
    calcContainer();

    if(true){//createArtificialChromosome
      double temporaryContainer[][] = dumpArray();
      newSolution = new int[chromosomeLength];
      assignJobs(temporaryContainer);
      duplicatedPopulation = new population();
      duplicatedPopulation.setGenotypeSizeAndLength(originalPop.getEncodedType(),
          1, originalPop.getLengthOfChromosome(),
          originalPop.getNumberOfObjectives());
      duplicatedPopulation.initNewPop();
      duplicatedPopulation.getSingleChromosome(0).setSolution(newSolution);
      eliminateJobs();

    }
  }

  public void assignJobs(double matrix[][]){
    openga.util.algorithm.getMax getMax1 = new openga.util.algorithm.getMax();
    openga.util.algorithm.normalize normalize1 = new openga.util.algorithm.normalize();
    for(int i = 0 ; i < chromosomeLength ; i ++ ){
      int pos[] = getMax1.getDataIndex(matrix);
      int Job = pos[0];
      int position = pos[1];
      //System.out.println(Job+"\t"+position+"\t"+matrix[Job][position]);
      votesNumber[position] = matrix[Job][position];
      newSolution[position] = Job;
      matrix = dropColumn(position, matrix);
      matrix = dropRow(Job, matrix);
    }
    votesNumber = normalize1.getData1(votesNumber);
  }

  public void eliminateJobs(){
    for(int i = 0 ; i < votesNumber.length ; i ++ ){
      if(votesNumber[i] < 0.5){
        duplicatedPopulation.setGene(0, i, -1);
      }
    }
  }

  public chromosome mateSolutions(chromosome artificial, chromosome orginal){
    chromosome tempChromosome = new chromosome();
    tempChromosome.setGenotypeAndLength(orginal.getEncodeType(), orginal.getLength(), orginal.getObjValue().length);
    tempChromosome.initChromosome();
    for(int i = 0 ; i < orginal.getLength() ; i ++ ){
      tempChromosome.setGeneValue(i, artificial.getSolution()[i]);
    }

    int counter = 0;
    for(int i = 0 ; i < tempChromosome.getLength() ; i ++ ){
      if(tempChromosome.getSolution()[i] == -1){
        counter = getGenePosition(counter, orginal, tempChromosome);
        tempChromosome.setGeneValue(i, orginal.getSolution()[counter]);
        counter ++;
      }
    }
    return tempChromosome;
  }

  public int getGenePosition(int counter, chromosome original, chromosome artificial){
    while(checkIdentical(original.getSolution()[counter], artificial)){
      counter ++;
    }
    return counter;
  }

  public boolean checkIdentical(int gene, chromosome artificial){
    for(int i = 0 ; i < artificial.getLength() ; i ++ ){
      if(gene == artificial.getSolution()[i]){
        return true;
      }
    }
    return false;
  }

  public static void main(String args[]){
    geneStatistics4 geneStatistics41 = new geneStatistics4();
    int length = 5;
    chromosome test1 = new chromosome();
    test1.setGenotypeAndLength(true, length, 2);
    test1.generateSequentialPop(length);
    test1.setSolution(new int[]{4, -1, 2, 1, -1});//the 0 and 3 are required to be repaired.
    chromosome test2 = new chromosome();
    test2.setGenotypeAndLength(true, length, 2);
    test2.generateSequentialPop(length);
    test2.setSolution(new int[]{2, 3, 0, 4, 1});

    for(int i = 0 ; i < length ; i ++ ){
      System.out.print(test1.getSolution()[i]+" ");
    }
    System.out.println();

    for(int i = 0 ; i < length ; i ++ ){
      System.out.print(test2.getSolution()[i]+" ");
    }
    System.out.println();

    chromosome chromosome1 = geneStatistics41.mateSolutions(test1, test2);
    for(int i = 0 ; i < length ; i ++ ){
      System.out.print(chromosome1.getSolution()[i]+" ");
    }

  }
}