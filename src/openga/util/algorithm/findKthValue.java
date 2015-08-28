package openga.util.algorithm;
import openga.util.sort.selectionSort;
import openga.util.printClass;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class findKthValue {
  public findKthValue() {
  }


  double[] data;
  double[] s1, s3;//s1 is eqaul to p.
  double[][] elementSets;//elementSets store the only five elements
  double p;
  int numberS1 = 0, numberS2 = 0, numberS3 = 0;
  int k;
  boolean iteration = true;

  public void setData(double data[], int num){
    this.data = data;
    k = num;
  }

  //start to find middle point
  public void startSearch(){
    while(iteration){
      //Step 1: Divide data into n/5 subsets.
      elementSets = divideData(data);
      //Step 2: Sort each element
      elementSets = sortElements(elementSets);
      //Step 3: find the P which is the median of the medians of the subsets.
      p = findP(elementSets);
      System.out.println(p);
      //Step 4: Partition data into S1, S2, and S3.
      partitionData(elementSets);
    }
  }

  public double[][] divideData(double _data[]){
    int numberofSubsets = _data.length / 5;
    if(_data.length % 5 != 0){
      numberofSubsets += 1;
    }
    double _elementSets[][] = new double[numberofSubsets][5];

    //copy the data into the elements
    int counter = 0;
    for(int i = 0 ; i < numberofSubsets ; i ++ ){
      for(int j = 0 ; j < 5 ; j ++ ){
        if(counter < _data.length){
          _elementSets[i][j] = _data[counter++];
        }
        else{
          //when the n*5 may larger than data size, the others elements are
          //dummy elements. So that we set it to infinite large.
          _elementSets[i][j] = Double.MAX_VALUE;
        }
      }
    }
    return _elementSets;
  }

  public double[][] sortElements(double _elementSets[][]){
    //sort each subsets in elementSets by selection sort.
    //The time-comlexity is nlogn. But since each subset only has 5 elements,
    //it's constant time.
    selectionSort selectionSort1 = new selectionSort();
    for(int i = 0; i < elementSets.length ; i ++ ){
      selectionSort1.setData(_elementSets[i]);
      _elementSets[i] = selectionSort1.getData();
    }
    return _elementSets;
  }


  //arbitrarily select a p among these subsets.
  public double findP(double _elementSets[][]){
    double tempArr[] = new double[_elementSets.length];
    double MP;
    for(int i = 0 ; i < _elementSets.length ; i ++ ){
      tempArr[i] = _elementSets[i][2];
    }

    if(tempArr.length > 5){
      _elementSets = divideData(tempArr);
      MP = findP(_elementSets);
    }
    else
      MP = directSolve(tempArr);

    return MP;
  }

  public double directSolve (double array[]){
    double MP = 0.0;
    double arr[];
    selectionSort selectionSort1 = new selectionSort();
    selectionSort1.setData(array);
    arr = selectionSort1.getData();
    int index = arr.length/2;

    if(arr.length % 2 == 0 && arr.length >2){//is even, it's equal to 4
      MP = (arr[1]+arr[2])/2.0;
    }
    else if(arr.length == 2){
      if(arr[1]  == Double.MAX_VALUE){
        return arr[0];
      }
      else{
        MP = (arr[0]+arr[1])/2.0;
      }
    }
    else{//is odd
      MP = arr[index];
    }
    return MP;
  }

  public void partitionData(double _elementSets[][]){
    //to get how many items in s1, s2, and s3 of each subset.
    for(int i = 0; i < _elementSets.length ; i ++ ){
      for(int j = 0 ; j < 5 ; j ++ ){
        if(_elementSets[i][j] != Double.MAX_VALUE){
          if(_elementSets[i][j] < p){
             numberS1 ++;
          }
          else if(_elementSets[i][j] == p){
            numberS2 ++;
          }
          else if(_elementSets[i][j] > p ){//it's larger than p
            numberS3 ++;
          }
        }
       }
    }//end for

    if(numberS1 >= k){//form the S1.
      data = formS1(_elementSets);
    }
    else if(numberS1 + numberS2 >= k){
      //the p is the kth element of data.
      iteration = false;
    }
    else{//it should be at the S3.
      //set the new k'
      k = k - numberS1 - numberS2;
      data = formS3(_elementSets);
    }
  }

  public double[] formS1(double _data[][]){
    double _s1[] = new double[numberS1];
    numberS1 = 0;

    for(int i = 0; i < _data.length ; i ++ ){
        for(int j = 0 ; j < 5 ; j ++ ){
          if(elementSets[i][j] != Double.MAX_VALUE && elementSets[i][j] < p && numberS1 < _s1.length){
           _s1[numberS1 ++]  = _data[i][j];
          }
          else{
            break;
          }
        }
    }
    return _s1;
  }

  public double[] formS3(double _data[][]){
    double _s3[] = new double[numberS3];
    numberS3 = 0;

    for(int i = 0; i < _data.length ; i ++ ){
        for(int j = 0 ; j < 5 ; j ++ ){
          if(elementSets[i][j] != Double.MAX_VALUE && elementSets[i][j] > p && numberS3 < _s3.length){
           _s3[numberS3 ++]  = _data[i][j];
          }
        }
    }
    return _s3;
  }

  public double getMiddlePointValue(){
    return p;
  }


  public static void main(String[] args) {
    System.out.println("to find the kth value.");

    findKthValue findMiddle1 = new findKthValue();
    double tempArr[] = {9,2,4,3,5, 6,7,8,1};//1 2 3 4 5 6 7 8 9
    int k = tempArr.length/2;//find the middle
    System.out.println("K is "+k);
    /*
    double tempArr[] = new double[15];

    for(int i = 0 ; i < tempArr.length ; i ++ ){
      tempArr[i] = Math.random()*50;
    }
    */

    printClass printClass1 = new printClass();
    findMiddle1.setData(tempArr, k);
    findMiddle1.startSearch();
    System.out.println("findKthValue1.getMiddlePointValue() "+findMiddle1.p);

    selectionSort selectionSort1 = new selectionSort();
    selectionSort1.setData(tempArr);
    tempArr = selectionSort1.getData();
    //System.out.println("tempArr[50] "+tempArr[50]);
    //printClass1.printMatrix("tempArr", "direct", tempArr);

  }

}