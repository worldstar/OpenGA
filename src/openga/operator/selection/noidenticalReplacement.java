package openga.operator.selection;

import openga.chromosomes.*;

/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: There are two populations, original population and archieve2. The archive maybe the offspring population.
 *    It supports only for the single objective problem.</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */
public class noidenticalReplacement extends MuPlusLamdaSelection {

    public noidenticalReplacement() {
    }
    double combinedObjs[];

    //we want to modify the new population size, we set the numberOfPop.
    public void setData(int sizeOfPop, populationI originalPop) {
        this.sizeOfPop = sizeOfPop;
        originalPopSize = originalPop.getPopulationSize();
        this.originalPop = originalPop;

        newPop = new population();//to assign a memmory space to the variable.
        //set attributes because we may copy the object latter.
        int length = originalPop.getSingleChromosome(0).genes.length;
        newPop.setGenotypeSizeAndLength(originalPop.getEncodedType(), sizeOfPop, length, originalPop.getNumberOfObjectives());
        newPop.initNewPop();
    }

    public void setSecondPopulation(populationI archieve2) {
        this.archieve2 = archieve2;
    }



    // prepare offspring no identical
    public void startToSelect1() {

        combinedPopuObjs();
        chromosome chromosomep = null;
        chromosome chromosomen = null;
        int index[] = sortPopulation(combinedObjs);
        int p = 0, n = 0;
        for (int i = 0; i < sizeOfPop; i++) {

            if (i == 0) {
                p = 0;
                n = 0;
            } else {
                do {
                    n = n + 1;
                    if (n==index.length) {
                        n=0;
                    }
//                    System.out.println(n);

                    if (index[n] < sizeOfPop) {
                        chromosomen = originalPop.getSingleChromosome(index[n]);
                    } else {
                        chromosomen = archieve2.getSingleChromosome(index[n] - sizeOfPop);
                    }



                } while (checkIdenticalSoln(chromosomep, chromosomen));
            }
            p = n;
            if (index[n] < sizeOfPop) {
                chromosomep = originalPop.getSingleChromosome(index[n]);
            } else {
                chromosomep = archieve2.getSingleChromosome(index[n] - sizeOfPop);
            }
            newPop.setSingleChromosome(i, chromosomep);
        }
    }

//prepare offspring with identical
public void startToSelect() {

    combinedPopuObjs();
    int index[] = sortPopulation(combinedObjs);
    for(int i = 0 ; i < sizeOfPop ; i ++ ){
      if(index[i] < sizeOfPop){
        newPop.setSingleChromosome(i, originalPop.getSingleChromosome(index[i]));
      }
      else{
        newPop.setSingleChromosome(i, archieve2.getSingleChromosome(index[i] - sizeOfPop));
      }
    }
  }

    private void combinedPopuObjs() {
        int sizeOfOriginalPop = originalPop.getPopulationSize();
        int sizeOfArchive = archieve2.getPopulationSize();
        combinedObjs = new double[sizeOfOriginalPop + sizeOfArchive];

        //for original pop
        for (int i = 0; i < sizeOfOriginalPop; i++) {
            combinedObjs[i] = originalPop.getObjectiveValues(i)[0];
        }

        //for archieve pop
        for (int i = 0; i < sizeOfArchive; i++) {
            combinedObjs[sizeOfOriginalPop + i] = archieve2.getObjectiveValues(i)[0];
        }
    }

    private int[] sortPopulation(double fitnessArray[]) {
        int index[] = new int[fitnessArray.length];
        for (int i = 0; i < fitnessArray.length; i++) {
            index[i] = i;
        }
        openga.util.sort.selectionSort selectionSort1 = new openga.util.sort.selectionSort();
        selectionSort1.setData(fitnessArray);
        selectionSort1.setNomialData(index);
        selectionSort1.Sort_withNomial();
        return selectionSort1.getNomialData();
    }

    public boolean checkIdenticalSoln(chromosome chromosome1, chromosome chromosome2) {
        boolean identical = true;
        for (int i = 0; i < chromosome1.getLength(); i++) {
            if (chromosome1.getSolution()[i] != chromosome2.getSolution()[i]) {
                identical = false;
                break;
            }
        }
//        System.out.println(identical);
        return identical;
    }

    public populationI getSelectionResult() {
        return newPop;
    }
}
