package openga.MultipleObjective;
import java.io.IOException;
import java.io.FileWriter;
import java.util.Vector;
import openga.chromosomes.population;
import openga.MultipleObjective.*;

/**
 * <p>Pareto Optimal Set</p>
 * <p>Find all pareto optimal from a population</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class findParetoSet {
  public findParetoSet() {
  }

  //The obj values of these chromosomes, [chromosome, objValues]
  double objVals[][];
  //the flag means that whether the solution is dominated.
  //Hence, the default value is false.
  boolean flag[];
  //To store the result of pareto solution which contains the index of chromosome in a population.
  int paretoIndex[];
  double paretoSet[][];
  double fitness[];
  int nondominate = 0;

  //set the data and initialize flag.
  public void setObjVals(double _objVals[][]){
    this.objVals = _objVals;
    flag = new boolean[_objVals.length];
    fitness = new double[_objVals.length];

    for(int i = 0 ; i < flag.length ; i ++ ){
      flag[i] = false;
    }
  }

  //to comapre the solns one-by-one of their objective value and to get
  //non-dominated solns. Goldberg's fitness assignment.
  public void startToFind(){
    for(int i = 0 ; i < flag.length ; i ++ ){
      //if flag[i] is true, it means it has been dominated which we can skip it directly
      if(flag[i] != true){
        for(int j = i+1; j < flag.length ; j ++ ){
          if(flag[j] != true){//the same with "if(flag[i] != true"            
            boolean comparisionRsult = getObjcomparison(objVals[i],objVals[j]);//test i is better than j
            boolean comparisionRsult2 = getObjcomparison(objVals[j],objVals[i]);//test j is better than i
            if(comparisionRsult){//if true, j is dominated by i
              flag[j] = true;
              //add the fitness assignment
              //fitness[i] += 1;
            }
            else if(comparisionRsult2){//j might better than i
              flag[i] = true;
              //fitness[j] += 1;
            }
          }
        }//end for of j
      }


    }//end for of i
  }//end startToFind()

  /**
   * If obj[i] > obj[j], then return true.
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

  /**
   * To form the pareto set according to the boolean value of flag.
   */
  public double[][] formParetoSet() throws IOException{
    Vector Vector1 = new Vector();
    int counter = 0;
    for(int i = 0 ; i < flag.length ; i ++ ){
      if(flag[i] == false){
        Vector1.add(counter,""+i);
        counter ++;
      }
    }
    //initialize the paretoSet and get their own index.
    paretoIndex = new int[counter];
    paretoSet = new double[counter][2];
    for(int i = 0 ; i < counter ; i ++ ){
      paretoIndex[i] = Integer.parseInt(Vector1.elementAt(i).toString()); // this index   
      paretoSet[i] = objVals[paretoIndex[i]]; // this pareto value           
    }
    return paretoSet;
  }

  public void printParetoValue(String Filename, double[][] value) throws IOException{
    FileWriter fw =new FileWriter(Filename);
    String implementResult = "X_value" + "," + "Y_Value" + "\n";
    for (int i=0;i<value.length;i++){
      implementResult += value[i][0] + "," + value[i][1] + "\n";
    }
    fw.write(implementResult);
    fw.close();
//    System.out.print(implementResult);
  }
  
  public static void main(String[] args) throws IOException {
    findParetoSet findParetoSet1 = new findParetoSet();
    //double testObjs[][] = {{20,32},{25,30},{27,23},{40,32},{38,25},{15,30},{26,25}};

//        int size = 50, noObjs = 2;
//        double testObjs[][] = new double[size][noObjs];
//        for(int i = 0; i < size ; i ++ ){
//          for(int j = 0 ; j < noObjs ; j ++ ){
//            testObjs[i][j] = 1+Math.random()*10;
//          }
//        }

    SamplePareto_ReadFile a = new SamplePareto_ReadFile();
    a.setRead_FileName("SPGA2_TADC_TC", ".csv");
    a.read_csv();
    double[][] testObjs = a.getdata();

    findParetoSet1.setObjVals(testObjs);
    findParetoSet1.startToFind();

    openga.util.printClass printClass1 = new openga.util.printClass();
    printClass1.printMatrix("",findParetoSet1.flag);

    openga.util.draw.ScatterPlot demo = new openga.util.draw.ScatterPlot("Plot all points");
    demo.setXYData(testObjs);
    demo.setParetoSetData(findParetoSet1.formParetoSet());
    demo.drawMethod();
    demo.setVisible(true);

  }

}
