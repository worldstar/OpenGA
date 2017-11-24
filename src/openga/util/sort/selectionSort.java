package openga.util.sort;
/**
 * I will the selection sort frequently because it's easy to understand.
 * The sequential result will be in ascending order.
  //to initial the data we want to sort
    int nomial[] = {1,2,3};
    int data[] = {1,3,3};

    selectionSort1.setData(data);
    selectionSort1.setNomialData(nomial);
    selectionSort1.selectionSort_int_withNomial();
    //the execution result is
    getNomialData
    1 2 3
    getData_int
    1 3 3
 */

public class selectionSort {

  public selectionSort() {
  }

  public selectionSort(double data[]) {
    this.data = data;
  }

  public selectionSort(int data[]) {
    this.data_int = data;
  }

  double data[];
  int nomialData[];//optional variable
  //for the use of int type
  int data_int[];

  /***********    to set the require data of the program     **************/
  public void setData(double data[]){
    this.data = data;
    //executeSelectionSort();
  }

  public void setData(int data[]){
    this.data_int = data;
  }

  public void setNomialData(int data[]){
    this.nomialData = data;
  }

  /********************          sort the array           *****************/
  //the first one is double data type and without nomial variable
  public void executeSelectionSort(){
    int f;
    double temp;
    for(int j = 0 ; j < data.length ; j ++ ){
      f = j;
      temp = data [j];
      for( int k = j+1 ; k < data.length ; k++){
        if(data[k] < data[f]){
          f = k;
        }
      }
      //to swap it
      data[j] = data[f];
      data[f] = temp;
    }
  }

  //the second one is int data type and without nomial variable
  public void selectionSort_int(){
    int f;
    int temp;
    for(int j = 0 ; j < data_int.length ; j ++ ){
      f = j;
      temp = data_int [j];
      for( int k = j+1 ; k < data_int.length ; k++){
        if(data_int[k] < data_int[f]){
          f = k;
        }
      }
      data_int[j] = data_int[f];
      data_int[f] = temp;
    }
  }

  //the third one is double data type and with nomial variable
  public void Sort_withNomial(){
    int f;//index flag
    double temp;//temp value
    int tempNomial;
    for(int j = 0 ; j < data.length ; j ++ ){
      f = j;
      temp = data [j];
      tempNomial = nomialData[j];

      for( int k = j+1 ; k < data.length ; k++){
        if(data[k] < data[f]){
          f = k;
        }
      }
      //to swap the value
      data[j] = data[f];
      data[f] = temp;

      //to swap the nomial value
      nomialData[j] = nomialData[f];
      nomialData[f] = tempNomial;
    }
  }

  //the fourth one is int data type and with nomial variable
  public void Sort_int_withNomial(){
    int f;
    int temp;
    int tempNomial;

    for(int j = 0 ; j < data_int.length ; j ++ ){
      f = j;
      temp = data_int [j];
      tempNomial = nomialData[j];

      for( int k = j+1 ; k < data_int.length ; k++){
        if(data_int[k] < data_int[f]){
          f = k;
        }
      }
      data_int[j] = data_int[f];
      data_int[f] = temp;

      //to swap the nomial value
      nomialData[j] = nomialData[f];
      nomialData[f] = tempNomial;
    }
  }



  /***************         support methods            *******************/
  public double[] getData(){
    return data;
  }

  public int[] getData_int(){
    return data_int;
  }

  public double getMiddleValue(){
    int index = data.length/2;
    double value = data[index];
    return value;
  }

  public double getFirtQuarterValue(){
    int index = data.length/4;
    double value = data[index];
    return value;
  }

  public double getThirdQuarterValue(){
    int index = (int)(data.length*0.75);
    double value = data[index];
    return value;
  }

  public int[] getNomialData(){
    return nomialData;
  }

  /**
   * @return The original nomial order is ascending based on the sorting data.
   * It is transformed into descending order now.
   */
  public int[] reverseNomialOrder(){
    int newOrder[] = new int[nomialData.length];
    for(int j = 0 ; j < nomialData.length ; j ++ ){
      newOrder[j] = nomialData[nomialData.length - j - 1];
    }
    return newOrder;
  }

}
