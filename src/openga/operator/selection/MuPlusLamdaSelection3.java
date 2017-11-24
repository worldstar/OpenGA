package openga.operator.selection;

/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: Chromosomes are sorted by fitness rather than objective value.</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class MuPlusLamdaSelection3 extends MuPlusLamdaSelection2 {
  public MuPlusLamdaSelection3() {
  }
  double combinedFintess[];

  private void combinedPopuObjs(){
    int sizeOfOriginalPop = originalPop.getPopulationSize();
    int sizeOfArchive = archieve2.getPopulationSize();
    combinedFintess = new double[sizeOfOriginalPop + sizeOfArchive];

    //for original pop
    for(int i = 0 ; i < sizeOfOriginalPop ; i ++ ){
      combinedFintess[i] = originalPop.getFitness(i);
    }

    //for archieve pop
    for(int i = 0 ; i < sizeOfArchive ; i ++ ){
      combinedFintess[sizeOfOriginalPop + i] = archieve2.getFitness(i);
    }
  }

  private int[] sortPopulation(double fitnessArray[]){
    int index[] = new int[fitnessArray.length];
    for(int i = 0 ; i < fitnessArray.length ; i ++ ){
      index[i] = i;
    }
    openga.util.sort.selectionSort selectionSort1 = new openga.util.sort.selectionSort();
    selectionSort1.setData(fitnessArray);
    selectionSort1.setNomialData(index);
    selectionSort1.Sort_withNomial();
    return selectionSort1.getNomialData();
  }

}