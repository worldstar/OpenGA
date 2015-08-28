package openga.operator.mutation;
import openga.chromosomes.*;
import openga.ObjectiveFunctions.*;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: The ideal is depended on the pseudo-weight vector by Deb. </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class localSearchByShiftMutation extends shiftMutation implements LocalSearchI{

  ObjectiveFunctionI ObjectiveFunction[];
  double max[], min[];//maximum and minimum of each objecive.

  public void setObjectives(ObjectiveFunctionI ObjectiveFunction[]){
    this.ObjectiveFunction = ObjectiveFunction;
  }

  public void calcMaxMinObjValue(double _obj[][]){
    max = new double[_obj[0].length];
    min = new double[_obj[0].length];
    openga.util.algorithm.getMax getMax1 = new openga.util.algorithm.getMax();
    openga.util.algorithm.getMin getMin1 = new openga.util.algorithm.getMin();

    for(int i = 0 ; i < _obj[0].length ; i ++ ){
      max[i] = getMax1.setData(_obj, i);
      min[i] = getMin1.setData(_obj, i);
    }
  }

  /**
   * The weight values of each objective of a solution.
   * @param _obj
   * @param objNew
   * @param indexOfObjective
   */
  public double[] calcWeight(double _obj[][], double objNew[]){
    //openga.util.algorithm.normalize normalize1 = new openga.util.algorithm.normalize();
    double weight[] = new double[objNew.length];;//weight for each objective of a solution x.
    double weightSum = 0;

    for(int i = 0 ; i < objNew.length ; i ++ ){
      weight[i] = (max[i] - objNew[i])/(max[i] - min[i]);
      weightSum += weight[i];
    }

    for(int i = 0 ; i < objNew.length ; i ++ ){
      weight[i] /= weightSum;
    }

    return weight;
  }

  public populationI evaluateNewSoln(populationI _pop){
    //calculate its objective
    for(int k = 0 ; k < ObjectiveFunction.length ; k ++ ){
      //System.out.println("The obj "+i);
      ObjectiveFunction[k].setData(_pop, k);
      ObjectiveFunction[k].calcObjective();
      _pop = ObjectiveFunction[k].getPopulation();
    }
    return _pop;
  }

  public void startMutation(){
    double objArray[][] = pop.getObjectiveValueArray();
    calcMaxMinObjValue(objArray);
    populationI _pop = new population();//to store the temp chromsome
    int numberOfLocalSearch = (int)(mutationRate*popSize);
    int k = 2; //number of local search.
    double weight[];//weight for each objective of a solution x.

    for(int i = 0 ; i < numberOfLocalSearch ; i ++ ){
      int chromsomeIndex = (int)(mutationRate*popSize);
      weight = calcWeight(objArray, pop.getObjectiveValues(chromsomeIndex));
      for(int j = 0 ; j < k ; j ++ ){//do local search for k times
        setCutpoint();
        _pop.setChromosome(0, pop.getSingleChromosome(chromsomeIndex));
        //local search by shift mutation
        _pop.setChromosome(0, shiftGenes(_pop.getSingleChromosome(0)));
        _pop = evaluateNewSoln(_pop);

        boolean compareResult = getObjcomparison(_pop.getObjectiveValues(0), pop.getObjectiveValues(i), weight);
        if(compareResult == true){//replace the original solution

          pop.setChromosome(i, _pop.getSingleChromosome(0));
          pop.setObjectiveValue(i, _pop.getObjectiveValues(0));
        }
      }//end for
    }//end for
  }

/*
  public void startMutation(){

    double weight[] = new double[2];
    for(int i = 0 ; i < ObjectiveFunction.length ; i ++ ){
      if(i == 0 ){
        weight[i] = Math.random();
      }
      else{
        weight[i] = 1- weight[0];
      }
    }

    populationI _pop = new population();//to store the temp chromsome
    //System.out.println("chromosomeLength"+chromosomeLength+ " mutationRate" +mutationRate);
    _pop.setGenotypeSizeAndLength(true, 1,
                                       chromosomeLength,pop.getNumberOfObjectives());
    _pop.createNewPop();

    for(int i = 0 ; i < popSize ; i ++ ){
       //test the probability is larger than mutationRate.
       if(Math.random() <= mutationRate){
         for(int m = 0 ; m < 2 ; m ++ ){
           setCutpoint();
           _pop.setChromosome(0, pop.getSingleChromosome(i));

           //local search by inverse or shift mutation
           if(Math.random() > 0.5){
             _pop.setChromosome(0, shiftGenes(_pop.getSingleChromosome(0)));
           }
           else{
             _pop.setChromosome(0, inverseGenes(_pop.getSingleChromosome(0)));
           }

          //calculate its objective
          for(int k = 0 ; k < ObjectiveFunction.length ; k ++ ){
            //System.out.println("The obj "+i);
            ObjectiveFunction[k].setData(_pop, k);
            ObjectiveFunction[k].calcObjective();
            _pop = ObjectiveFunction[k].getPopulation();
          }

          boolean compareResult = getObjcomparison(_pop.getObjectiveValues(0), pop.getObjectiveValues(i), weight);
          if(compareResult == true){//replace the original solution
            //System.out.println(compareResult+" Soln improved by local search "+pop.getObjectiveValues(i)[0]+" "+pop.getObjectiveValues(i)[1]+" -> "+_pop.getObjectiveValues(0)[0]+" "+_pop.getObjectiveValues(0)[1]);
            pop.setChromosome(i, _pop.getSingleChromosome(0));
            pop.setObjectiveValue(i, _pop.getObjectiveValues(0));
          }
         }//end for

       }//end local search of the soln.
    }//end for
  }
*/

  /**
   * If obj[i] < obj[j], then return true.
   * @param _obj1 the first obj
   * @param _obj2 the second obj
   * @param index where to start
   * @return
   */
  private boolean getObjcomparison(double _obj1[], double _obj2[], double weight[]){


    double objectiveWeightSum1 = 0 ;
   double objectiveWeightSum2 = 0 ;

   for(int i = 0 ; i < _obj1.length ; i ++ ){
     if(i == 0 ){
       weight[i] = Math.random();
     }
     else{
       weight[i] = 1- weight[0];
     }
   }

   for(int i = 0 ; i < _obj1.length ; i ++ ){

     objectiveWeightSum1 += _obj1[i]*weight[i];
     objectiveWeightSum2 += _obj2[i]*weight[i];
   }
   if(objectiveWeightSum1 < objectiveWeightSum2){
     return true;
   }
   else
     return false;
  }

  public static void main(String[] args) {
  }
}