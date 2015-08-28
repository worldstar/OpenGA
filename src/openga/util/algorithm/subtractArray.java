package openga.util.algorithm;

/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */
public class subtractArray {

  public subtractArray() {
  }

  /************   to delete the element at postion "index"*******************/
  public int[] subtractArray_int(int index, int[] array){
    int newArray[] = new int[array.length-1];
    int counter = 0;

    for(int i = 0 ; i < array.length ; i ++ ){
       if(i != index)
         newArray[counter++] = array[i];
    }
    return newArray;
  }

  public int[] subtractArray_ByValue(int value, int[] array){
    int newArray[] = new int[array.length-1];
    int counter = 0;

    for(int i = 0 ; i < array.length ; i ++ ){
       if(array[i] != value)
         newArray[counter++] = array[i];
    }
    return newArray;
  }

  public double[] subtractArray_double(int index, double[] array){
    double newArray[] = new double[array.length-1];
    int counter = 0;

    for(int i = 0 ; i < array.length ; i ++ ){
       if(i != index)
         newArray[counter++] = array[i];
    }
    return newArray;
  }

  public double[][] subtractArray_double(int index, double[][] array){
    double newArray[][] = new double[array.length-1][array.length-1];
    int counterRow = 0;

    for(int i = 0 ; i < array.length ; i ++ ){
      int counterColumn = 0;
      boolean getCounter = false;

      for(int j = 0 ; j < array.length ; j++){
        if(i != index){
          if(j != index){
            //System.out.println("counterRow "+counterRow+" counterColumn "+counterColumn);
            newArray[counterRow][counterColumn++] = array[i][j];
          }
        }
        else{
          //skip the row driectly
          j = array.length;
          //minus the count of the row becoz it increase 1 later.
          counterRow--;
        }
      }
      counterRow ++;
    }
    return newArray;
  }

  /************ with array interval  ******************************/
  public int[] arrayInterval_int(int start, int end, int[] array){
    int newArray[] = new int[array.length- (end -start +1)];
    int counter = 0;

    for(int i = 0 ; i < array.length ; i ++ ){
       if(i < start || i > end)
         newArray[counter++] = array[i];
    }
    return newArray;
  }

  public double[] arrayInterval_double(int start, int end, double[] array){
    double newArray[] = new double[array.length- (end -start +1)];
    int counter = 0;

    for(int i = 0 ; i < array.length ; i ++ ){
       if(i < start || i > end)
         newArray[counter++] = array[i];
    }
    return newArray;
  }
}
