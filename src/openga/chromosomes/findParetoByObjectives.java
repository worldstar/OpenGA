package openga.chromosomes;
import java.util.Vector;
import openga.chromosomes.population;
/**
 * <p>Title: The OpenGA project</p>
 * <p>Description: Both the two objectives are the minimization problem.</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.1
 */

public class findParetoByObjectives{
  public findParetoByObjectives() {
  }

  //The obj values of these chromosomes, [chromosome, objValues]
  double objVals[][];
  //the flag means that whether the solution is dominated.
  //Hence, the default value is false.
  boolean flag[];
  //To store the result of pareto solution which contains the index of chromosome in a population.
  int paretoSet[];
  double paretoIndex[][];
  int nondominate = 0;
  populationI newPop, originalPop;
  int numberToCopy = 1;

  public void setOriginalPop(populationI originalPop){
    this.originalPop = originalPop;

    double _tempObjs[][] = new double[originalPop.getPopulationSize()][originalPop.getNumberOfObjectives()];

    for(int i = 0 ; i < originalPop.getPopulationSize() ; i ++ ){
      for(int j = 0 ; j < originalPop.getNumberOfObjectives() ; j ++ ){
        _tempObjs[i][j] = originalPop.getObjectiveValues(i)[j];
      }
    }
    flag = new boolean[originalPop.getPopulationSize()];

    for(int i = 0 ; i < originalPop.getPopulationSize() ; i ++ ){
      flag[i] = false;
    }

    setObjVals(_tempObjs);

  }

  public void setEliteNumber(int elitism){
    numberToCopy = elitism;
  }

  //set the data and initialize flag.
  public void setObjVals(double _objVals[][]){
    this.objVals = _objVals;
  }

  //to comapre the solns one-by-one of their objective value and to get
  //non-dominated solns. Goldberg's fitness assignment.
  public void startToFind(){
    //to determine whether it's single objective or multi-objective problem.
    if(originalPop.getNumberOfObjectives() > 1){
      forMultiObjectives();
    }
    else{
      forSingleObjective();
    }
    //form a pareto set and set it into the archieve.
    formParetoSet();
    storeInArchieve();
  }//end startToFind()

  /**
   * It's for single objective problem.
   */
  public void forSingleObjective(){
    //transform the data
    double singleObjValue[] = new double[originalPop.getPopulationSize()];
    int number[] = new int[originalPop.getPopulationSize()];

    for(int i = 0 ; i < originalPop.getPopulationSize() ; i ++ ){
      singleObjValue[i] = objVals[i][0];
      number[i] = i;
    }

    //sort these objective values by the ascending manner
    openga.util.sort.selectionSort selectionSort1 = new openga.util.sort.selectionSort();
    selectionSort1.setData(singleObjValue);
    selectionSort1.setNomialData(number);
    selectionSort1.selectionSort_withNomial();
    number = selectionSort1.getNomialData();

    //copy solutions of elitism proportions by setting the flag is false.
    for(int i = numberToCopy ; i < flag.length ; i ++ ){
      flag[number[i]] = true;
    }
  }

  /**
   * It's for single objective problem.
   */
  public void forMultiObjectives(){
    for(int i = 0 ; i < flag.length ; i ++ ){
      //if flag[i] is true, it means it has been dominated so that we can skip it directly
      if(flag[i] == false){
        for(int j = i+1; j < flag.length ; j ++ ){
          if(flag[j] == false){//the same with "if(flag[i] == false)"
            boolean comparisionRsult = getObjcomparison(objVals[i],objVals[j]);//test i is better than j
            boolean comparisionRsult2 = getObjcomparison(objVals[j],objVals[i]);//test j is better than i
            if(comparisionRsult){//if true, j is dominated by i
              flag[j] = true;
            }
            else if(comparisionRsult2){//j might better than i
              flag[i] = true;
              break;
            }
            else if(checkIdenticalObjective(objVals[i], objVals[j])){//it might be equal, we may drop the solution
              flag[j] = true;//drop the second soln.
              //To avoid the clone solution.
              //originalPop.setFitness(j, originalPop.getFitness(j)*(1.01+Math.random()*.5));
            }
          }
        }//end for of j
      }
    }//end for of i
  }

  /**
   * To form the pareto set according to the boolean value of flag.
   */
  public double[][] formParetoSet(){
    Vector Vector1 = new Vector();
    int counter = 0;
    for(int i = 0 ; i < flag.length ; i ++ ){
      if(flag[i] == false){
        Vector1.add(counter,""+i);
        counter ++;
      }
    }
    
    //initialize the paretoSet and get their own index.
    paretoSet = new int[counter];
    paretoIndex = new double[counter][originalPop.getNumberOfObjectives()];
    for(int i = 0 ; i < counter ; i ++ ){
      paretoSet[i] = Integer.parseInt(Vector1.elementAt(i).toString()); //store the index of original population
      for(int j = 0 ; j < originalPop.getNumberOfObjectives() ; j ++ ){
        paretoIndex[i][j] = objVals[paretoSet[i]][j];               //set the objective values of the solution.
      }
    }
    return paretoIndex;
  }

  public void storeInArchieve(){
    //store the pareto solution to a new archieve.
    storeSelectedChromosomes storeSelectedChromosomes1 = new storeSelectedChromosomes();
    storeSelectedChromosomes1.setOriginalPop(originalPop, paretoSet);
    storeSelectedChromosomes1.formNewPopulation();
    newPop = storeSelectedChromosomes1.getPopulation();
  }

  /**
   * If obj[i] < obj[j], then return true.
   * @param _obj1 the first obj
   * @param _obj2 the second obj
   * @return
   *       if((_obj1[i] < _obj2[i] && _obj1[i+1] <= _obj2[i+1]) ||
         (_obj1[i] <= _obj2[i] && _obj1[i+1] < _obj2[i+1])){
        better = true;
      }

   */
  public boolean getObjcomparison(double _obj1[], double _obj2[]){
    boolean better = false;
    for(int i = 0 ; i < _obj1.length - 1 ; i ++ ){
      if((_obj1[i] < _obj2[i] && _obj1[i+1] <= _obj2[i+1]) ||
         (_obj1[i] <= _obj2[i] && _obj1[i+1] < _obj2[i+1])){
        better = true;
      }
      else{
        return false;
      }
    }
    return better;
  }

  public boolean checkIdenticalObjective(double _obj1[], double _obj2[]){
    for(int i = 0 ; i < _obj1.length ; i ++ ){
      if(_obj1[i] != _obj2[i]){
        return false;
      }
    }
    return true;
  }


  public populationI getPopulation(){
    return newPop;
  }

  public double[][] getParetoObjectives(){
    return paretoIndex;
  }

  public double[][] getObjectives(){
    return objVals;
  }

  public int[] getparetoSolnIndex(){
    return paretoSet;
  }
}