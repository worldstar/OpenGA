package homework.util.algorithm;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class getMax {

  public getMax() {
  }

  double max = 0;

  public double setData(double vals[]){
    for(int i = 0 ; i < vals.length ; i ++ ){
      if(vals[i] >= max){
        max = vals[i];
      }
    }

    return max;
  }

  public double setData(double vals[][]){
    //find the global temp first
    for(int i = 0 ; i < vals.length ; i ++ ){
      double temp = setData(vals[i]);
      if(temp >= max){
        max = temp;
      }
    }

    return max;
  }

  /**
   * To find the minimum value of each column.
   * @param vals Input the objective values.
   * @param index which column
   * @return
   */
  public double setData(double vals[][], int index){
    max = 0;
    //find the global temp first
    for(int i = 0 ; i < vals.length ; i ++ ){
      if(max < vals[i][index]){
        max = vals[i][index];
      }
    }
    return max;
  }

  public int setData(int vals[]){
    for(int i = 0 ; i < vals.length ; i ++ ){
      if(vals[i] >= max){
        max = vals[i];
      }
    }

    return (int)max;
  }

  public int getDataIndex(double vals[]){
    int index = 0;
    for(int i = 0 ; i < vals.length ; i ++ ){
      if(vals[i] >= max){
        max = vals[i];
        index = i;
      }
    }
    return index;
  }

  public int getDataIndex(int vals[]){
    int index = 0;
    for(int i = 0 ; i < vals.length ; i ++ ){
      if(vals[i] >= max){
        max = vals[i];
        index = i;
      }
    }
    return index;
  }

  public int[] getDataIndex(double matrix[][]){
    int pos[] = new int[2];
    max = 0;
    for(int i = 0 ; i < matrix.length ; i ++ ){
      for(int j = 0 ; j < matrix.length ; j ++ ){
        if(matrix[i][j] != -1 && matrix[i][j] >= max){
          max = matrix[i][j];
          pos[0] = i;
          pos[1] = j;
        }
      }
    }
    return pos;
  }

  public int getDataIndex(double matrix[][], int index){
    int pos = 0;
    max = 0;
    for(int i = 0 ; i < matrix.length ; i ++ ){
      if(matrix[i][index] != -1 && matrix[i][index] >= max){
        max = matrix[i][index];
      }
    }
    return pos;
  }

  public double getMax(){
    return max;
  }
}