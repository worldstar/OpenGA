package openga.Fitness;
import java.util.Vector;
import openga.chromosomes.*;
/**
 * <p>Title: The OpenGA project</p>
 * <p>Description: The project is to build general framework of Genetic algorithm and problem independent.</p>
 * <P></P>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class GoldbergFitnessAssignment implements FitnessI{
  public GoldbergFitnessAssignment() {
  }

  populationI pop;                 //mutation on whole population
  //The obj values of these chromosomes, [chromosome, objValues]
  double objVals[][];
  //the flag means that whether the solution is dominated.
  //Hence, the default value is false.
  boolean flag[];
  //To store the result of pareto solution which contains the index of chromosome in a population.
  int paretoSet[];
  double paretoIndex[][];
  double fitness[];
  int nondominated = 0;
  int popSize;
  int numberOfObjs;
  populationI paretoSet1;

  public void setData(populationI population1, int numberOfObjs){
    pop = population1;
    int numberOfObjectives = numberOfObjs;
    popSize = pop.getPopulationSize();
    double objValues[][] = new double[popSize][numberOfObjectives];
    for(int i = 0 ; i < popSize ; i ++ ){
      objValues[i] = pop.getObjectiveValues(i);
    }
    setObjVals(objValues);

  }

  public void setParetoData(populationI paretoSet1){
    this.paretoSet1 = paretoSet1;
  }

  //set the data and initialize flag.
  public void setObjVals(double _objVals[][]){
    this.objVals = _objVals;
    popSize = _objVals.length;
    flag = new boolean[popSize];
    fitness = new double[popSize];

    for(int i = 0 ; i < popSize ; i ++ ){
      flag[i] = false;
    }

  }

  //to comapre the solns one-by-one of their objective value and to get
  //non-dominated solns. Goldberg's fitness assignment.
  public void calculateFitness(){
    for(int i = 0 ; i < flag.length ; i ++ ){
        for(int j = i+1; j < flag.length ; j ++ ){
          //if(flag[j] != true){//the same with "if(flag[i] != true"
            boolean comparisionRsult = getObjcomparison(objVals[i],objVals[j]);//test i is better than j
            boolean comparisionRsult2 = getObjcomparison(objVals[j],objVals[i]);//test j is better than i
            if(comparisionRsult){//if true, j is dominated by i
              flag[j] = true;
              //add the fitness assignment
              fitness[j] += 1;
            }
            else if(comparisionRsult2){//j might better than i
              flag[i] = true;
              fitness[i] += 1;
            }
          //}//end the flag if
        }//end for of j
    }//end for of i

    //write the fitness data to the fitness of each chromosome.
    for(int i = 0 ; i < popSize ; i ++ ){
      pop.setFitness(i, fitness[i]);
    }

  }//end startToFind()

  /**
   * If obj[i] < obj[j], then return true.
   * @param _obj1 the first obj
   * @param _obj2 the second obj
   * @param index where to start
   * @return
   */
  private boolean getObjcomparison(double _obj1[], double _obj2[]){
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

  /****************************************************************
   * To form the pareto set according to the boolean value of flag.
   ***************************************************************/
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
    paretoIndex = new double[counter][2];
    for(int i = 0 ; i < counter ; i ++ ){
      paretoSet[i] = Integer.parseInt(Vector1.elementAt(i).toString());
      paretoIndex[i] = objVals[paretoSet[i]];
    }
    return paretoIndex;
  }

  public populationI getPopulation(){
    return pop;
  }

  public static void main(String[] args) {
    GoldbergFitnessAssignment goldbergFitnessAssignment1 = new GoldbergFitnessAssignment();
  }

}