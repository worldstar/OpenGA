package openga.util;

/**
 * @Authour: Chen, Shih-Hsin.
 * @email: turboman@linux.im.isu.edu.tw
 * The purpose of the program is to print result.
 */

public class printClass {

  public printClass() {
  }

  public void printMatrix(String message, String[] array){
    System.out.println(message);
    for(int i = 0 ; i < array.length ; i++){
      System.out.print(array[i]+" ");
    }
    System.out.println();
  }

  public void printMatrix(String message, String[][] array){
    System.out.println(message);
    for(int j = 0 ; j < array.length ; j ++ ) {
      for(int i = 0 ; i < array[1].length ; i++){
        System.out.print(array[j][i]+" ");
      }
      System.out.println();
    }
  }

  public void printMatrix(String message, double[][] array){
    System.out.println(message);
    for(int i = 0 ; i < array.length ; i++){
      for(int j = 0 ; j < array[0].length ; j ++ ){
        System.out.print(array[i][j]+"\t");
      }
      System.out.print("\n");
    }
    //System.out.println();
  }

  public void printMatrix(String message, int[][] array){
    System.out.println(message);
    for(int i = 0 ; i < array.length ; i++){
      for(int j = 0 ; j < array.length ; j ++ ){
        System.out.print(array[i][j]+" ");
      }
      System.out.println();
    }
    System.out.println();
  }

  public void printMatrix(String message, double[] array){
    System.out.println(message);
    for(int i = 0 ; i < array.length ; i++){
      System.out.print(array[i]+" ");
    }
    System.out.println();
  }

  public void printMatrix(String message, String direct, double[] array){
    System.out.println(message);
    for(int i = 0 ; i < array.length ; i++){
      System.out.println(array[i]);
    }
    System.out.println();
  }

  public void printMatrix(String message, int[] array){
    System.out.println(message);
    for(int i = 0 ; i < array.length ; i++){
      System.out.print(array[i]+" ");
    }
    System.out.println();
  }

  public void printMatrix(String message, int[] array, double value){
    System.out.print(message+": ");
    for(int i = 0 ; i < array.length ; i++){
      System.out.print(array[i]+" ");
    }
    System.out.print("\t"+value);
    System.out.println();
  }

  public void printMatrix(String message, int[] array, double values[]){
    System.out.print(message+": ");
    for(int i = 0 ; i < array.length ; i++){
      System.out.print(array[i]+" ");
    }
    System.out.print("\t");

    for(int i = 0 ; i < values.length ; i++){
      System.out.print(values[i]+"   ");
    }
    System.out.println();
  }

  public void printMatrix(String message, int[] array, double values[], double value){
    System.out.print(message+": ");
    for(int i = 0 ; i < array.length ; i++){
      System.out.print(array[i]+" ");
    }
    System.out.print("\t");

    for(int i = 0 ; i < values.length ; i++){
      System.out.print(values[i]+"\t");
    }
    System.out.print("\t"+value);
    System.out.print("\n");
  }

  public void printMatrix(String message, boolean[] array){
    System.out.println(message);
    for(int i = 0 ; i < array.length ; i++){
      System.out.print(array[i]+" ");
    }
    System.out.println();
  }

}
