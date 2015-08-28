package openga.chromosomes;

/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: Both the two objectives are the maximization problem.</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class findParetoByObjectivesMaxMax extends findParetoByObjectives {
  public findParetoByObjectivesMaxMax() {
  }

  public boolean getObjcomparison(double _obj1[], double _obj2[]){
    boolean better = false;
    for(int i = 0 ; i < _obj1.length - 1 ; i ++ ){
      if((_obj1[i] > _obj2[i] && _obj1[i+1] >= _obj2[i+1]) ||
         (_obj1[i] >= _obj2[i] && _obj1[i+1] > _obj2[i+1])){
        better = true;
      }
      else{
        return false;
      }
    }
    return better;
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
    for(int i = 0 ; i < flag.length -numberToCopy ; i ++ ){
      flag[number[i]] = true;
    }
  }
/*
  public static void main(String[] args) {
    findParetoByObjectivesMaxMax findParetoByObjectives1 = new findParetoByObjectivesMaxMax();
    double _tempObjs[][] = new double[][]
        {{	26529	,	28841	},
        {	26555	,	28826	},
        {	26615	,	28813	},
        {	26651	,	28789	},
        {	26662	,	28786	},
        {	26710	,	28867	},
        {	26743	,	28835	},
        {	26821	,	28771	},
        {	29251	,	26126	},
        {	29252	,	26115	},
        {	29257	,	26111	},
        {	29258	,	26105	},
        {	29268	,	26101	},
        {	29271	,	26080	},
        {	29274	,	26070	},
        {	29278	,	26064	},
        {	29280	,	26058	},
        {	29281	,	26054	},
        {	29322	,	25925	},
        {	29337	,	25920	},
        {	29340	,	25868	},
        {	29362	,	25823	}};

    openga.util.printClass printClass1 = new openga.util.printClass();
    populationI population1 = new population();
    populationI newPop = new population();
    int size = _tempObjs.length, length = 6;

    population1.setGenotypeSizeAndLength(false, size, length, 2);
    population1.initNewPop();

    for(int i = 0 ; i < size ; i ++ ){
      population1.setObjectiveValue(i, _tempObjs[i]);
    }
    findParetoByObjectives1.setOriginalPop(population1);
    findParetoByObjectives1.startToFind();
    newPop = findParetoByObjectives1.getPopulation();

    String implementResult = "";
    for(int i = 0 ; i < newPop.getPopulationSize() ; i ++ ){
      for(int j = 0 ; j < 2 ; j ++ ){//for each objectives
        implementResult += newPop.getObjectiveValues(i)[j]+"\t";
      }
      implementResult += "\n";
    }
    findParetoByObjectives1.writeFile("SPGA2_Knapsack_pareto750", implementResult);
  }

  public void writeFile(String fileName, String _result){
    openga.util.fileWrite1 writeLotteryResult = new openga.util.fileWrite1();
    writeLotteryResult.writeToFile(_result,fileName+".txt");
    Thread thread1 = new Thread(writeLotteryResult);
    thread1.run();
  }
*/
}