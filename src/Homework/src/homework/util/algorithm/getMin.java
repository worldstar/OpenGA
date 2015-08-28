package homework.util.algorithm;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class getMin {
  public getMin() {
  }

  double min = 0;

  public double setData(double vals[]){
    min = vals[0];
    for(int i = 1 ; i < vals.length ; i ++ ){
      if(vals[i] < min){
        min = vals[i];
      }
    }

    return min;
  }

  public double setData(double vals[][]){
    min = vals[0][0];
    //find the global temp first
    for(int i = 0 ; i < vals.length ; i ++ ){
      double temp = setData(vals[i]);
      if(min > temp){
        min = temp;
      }
    }

    return min;
  }

  /**
   * To find the minimum value of each column.
   * @param vals Input the objective values.
   * @param index which column
   * @return
   */
  public double setData(double vals[][], int index){
    min = Double.MAX_VALUE;
    //find the global temp first
    for(int i = 0 ; i < vals.length ; i ++ ){
      if(min > vals[i][index]){
        min = vals[i][index];
      }
    }
    return min;
  }

  public int setData(int vals[]){
    min = vals[0];
    for(int i = 0 ; i < vals.length ; i ++ ){
      if(vals[i] < min){
        min = vals[i];
      }
    }

    return (int)min;
  }

  //to find out where the minimum value is rather than value itself.
  public int getDataIndex(int vals[]){
    min = Integer.MAX_VALUE;
    int index = 0;

    for(int i = 0 ; i < vals.length ; i ++ ){
      if(vals[i] <= min){
        min = vals[i];
        index = i;
      }
    }

    return index;
  }

  //to find out where the minimum value is rather than value itself.
  public int getDataIndex(int vals[], int numberOfJobs[], int jobPos[]){//for specific purpose
    min = Integer.MAX_VALUE;
    int index = 0;

    for(int i = 0 ; i < vals.length ; i ++ ){
      if(jobPos[i] == numberOfJobs[i]){
        vals[i] = Integer.MAX_VALUE;
      }
      if(vals[i] <= min){
        min = vals[i];
        index = i;
      }
    }

    return index;
  }

}
