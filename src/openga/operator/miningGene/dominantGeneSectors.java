package openga.operator.miningGene;
import openga.chromosomes.*;
import java.util.Vector;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class dominantGeneSectors {
  public dominantGeneSectors() {
  }

  chromosome elite;
  int eliteGenes[];
  int existingTimes[];
  int popSize, chromosomeLength;

  public void initialize(int length, int size){
    eliteGenes = new int[length];
    existingTimes = new int[length];
    chromosomeLength = length;
    popSize = size;
    for(int i = 0 ; i < chromosomeLength ; i ++ ){
      eliteGenes[i] = -1;
    }
  }

  public void setData(chromosome elite){
    this.elite = elite;
  }

  public void startStatistics(){
    for(int i = 0 ; i < chromosomeLength ; i ++ ){
      if(eliteGenes[i] == elite.genes[i]){
        existingTimes[i] += 1;
      }
      else{
        eliteGenes[i] = elite.genes[i];
        existingTimes[i] = 0;
      }
    }
  }

  public void purification(){
    int numberOfSegments = countSegments();
    Vector vec[] = new Vector[numberOfSegments];
    boolean activate[] = new boolean[chromosomeLength/2];
    int counter = 0;
    int pos = 0, start = 0, geneLength = 0;

    while(pos < chromosomeLength){
      if(existingTimes[pos] >= 1){
        geneLength ++;
      }
      else if(geneLength >= 2){//the activated gene segment end here.
        //to copy gene from start to previous position
        copyGenes(start, pos-1, vec[counter]);
        counter ++;
        geneLength = 0;
        start = pos+1;
      }
      else{
        start = pos+1;
      }
      pos ++;
    }
  }

  private int countSegments(){
    int counts = 0, pos = 0, geneLength = 0;
    while(pos < chromosomeLength){
      if(existingTimes[pos] >= 1){
        geneLength ++;
      }
      else if(geneLength >= 2){//the activated gene segment end here.
        counts ++;
        geneLength = 0;
      }
      pos ++;
    }

    return counts;
  }

  private void copyGenes(int start, int end, Vector vec){
    for(int i = start ; i < end ; i ++ ){
      vec.addElement(Integer.toString(eliteGenes[i]));
    }
  }



}