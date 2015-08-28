package openga.chromosomes;
import java.util.Vector;
import openga.chromosomes.population;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: The class extends the findParetoByObjectives and use Divide-and-Conquer to speed up the process of finding Pareto Solns. </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class fastFindParetoByObjectives extends findParetoByObjectives {
  public fastFindParetoByObjectives() {
  }

  int indexes[];
  int group1Index[];
  int group2Index[];

  //to divide the size into n segments.
  private void dividePopulation(int _size){
    indexes = new int[_size];
    for(int i = 0 ; i < _size ; i ++ ){
      indexes[i] = i;
    }
  }

  public int[] mergeIndex(int from, int to){
    Vector Vector1 = new Vector();
    int storedIndexes[];
    int counter = 0;
    for(int i = from ; i < to ; i ++ ){
      if(flag[i] == false){
        Vector1.add(counter,""+i);
        counter ++;
      }
    }

    //initialize the paretoSet and get their own index.
    storedIndexes = new int[counter];
    for(int i = 0 ; i < counter ; i ++ ){
      storedIndexes[i] = Integer.parseInt(Vector1.elementAt(i).toString()); //store the index of original population
    }
    return storedIndexes;
  }

  public void startToFind(){
    dividePopulation(originalPop.getPopulationSize());
    //to determine whether it's single objective or multi-objective problem.
    if(originalPop.getNumberOfObjectives() > 1){
      forMultiObjectives(new int[]{0, originalPop.getPopulationSize()/2}, indexes);
      forMultiObjectives(new int[]{originalPop.getPopulationSize()/2, originalPop.getPopulationSize()}, indexes);
      int currentFrontIndexes1[] = mergeIndex(0, originalPop.getPopulationSize()/2);
      int currentFrontIndexes2[] = mergeIndex(originalPop.getPopulationSize()/2, originalPop.getPopulationSize());
      forMultiObjectives2(currentFrontIndexes1, currentFrontIndexes2);
      //merge
    }
    else{
      forSingleObjective();
    }
    //form a pareto set and set it into the archieve.
    formParetoSet();
    storeInArchieve();

  }//end startToFind()

  public void forMultiObjectives(int interval[], int popIndex[]){
    for(int i = interval[0] ; i < interval[1] ; i ++ ){
      //if flag[i] is true, it means it has been dominated so that we can skip it directly.
      int from = popIndex[i];
      if(flag[from] == false){
        for(int j = i+1; j < interval[1] ; j ++ ){
          int to = popIndex[j];
          if(flag[to] == false){//the same with "if(flag[i] == false)"
            boolean comparisionRsult = getObjcomparison(objVals[from],objVals[to]);//test i is better than j
            boolean comparisionRsult2 = getObjcomparison(objVals[to],objVals[from]);//test j is better than i
            if(comparisionRsult){//if true, j is dominated by i
              flag[to] = true;
            }
            else if(comparisionRsult2){//j might better than i
              flag[from] = true;
              break;
            }
            else if(checkIdenticalObjective(objVals[from], objVals[to])){//it might be equal, we may drop the solution
              flag[to] = true;//drop the second soln.
            }
          }
        }//end for of j
      }
    }//end for of i
  }

  public void forMultiObjectives2(int popIndex[], int popIndex2[]){
    for(int i = 0; i < popIndex.length ; i ++ ){
      //if flag[i] is true, it means it has been dominated so that we can skip it directly.
      int from = popIndex[i];
      if(flag[from] == false){
        for(int j = 0; j < popIndex2.length ; j ++ ){
          int to = popIndex2[j];
          if(flag[to] == false){//the same with "if(flag[i] == false)"
            boolean comparisionRsult = getObjcomparison(objVals[from],objVals[to]);//test i is better than j
            boolean comparisionRsult2 = getObjcomparison(objVals[to],objVals[from]);//test j is better than i
            if(comparisionRsult){//if true, j is dominated by i
              flag[to] = true;
            }
            else if(comparisionRsult2){//j might better than i
              flag[from] = true;
              break;
            }
            else if(checkIdenticalObjective(objVals[from], objVals[to])){//it might be equal, we may drop the solution
              flag[to] = true;//drop the second soln.
            }
          }
        }//end for of j
      }
    }//end for of i
  }

}