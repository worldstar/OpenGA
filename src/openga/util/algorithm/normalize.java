package openga.util.algorithm;

/**
 * <p>Title: The OpenGA project</p>
 * <p>Description: The project is to build general framework of Genetic algorithm and problem independent.</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */
public class normalize {
  public normalize() {
  }

  public double[] getData1(double data[]){
    getMin getMin1 = new getMin();
    getMax getMax1 = new getMax();
    double min = getMin1.setData(data);
    double max = getMax1.setData(data);
    double[] normData = new double[data.length];

    for(int i = 0 ; i < data.length ; i ++ ){
      normData[i] = (data[i] - min)/ (max - min);
    }
    return normData;
  }

  public double[][] getData2(double data[][]){
    getMin getMin1 = new getMin();
    getMax getMax1 = new getMax();
    double min = getMin1.setData(data);
    double max = getMax1.setData(data);
    double[][] normData = new double[data.length][data.length];

    for(int i = 0 ; i < data.length ; i ++ ){
       for(int j = 0 ; j < data.length ; j ++ ){
         normData[i][j] = (data[i][j] - min)/ (max - min);
       }
    }
    return normData;
  }

  /**
   * Normalize each the values of column index.
   * @param data
   * @param index
   * @return
   */
  public double[][] getData2(double data[][], int index){
    getMin getMin1 = new getMin();
    getMax getMax1 = new getMax();
    double min = getMin1.setData(data, index);
    double max = getMax1.setData(data, index);
    double[][] normData = new double[data.length][data[0].length];

    for(int i = 0 ; i < data.length ; i ++ ){
      data[i][index] = (data[i][index] - min)/ (max - min);
    }
    return data;
  }

  //for each row treated as a single array
  public double[][] getData2EachRow(double data[][]){
    double[][] normData = new double[data.length][data[0].length];
    for(int i = 0 ; i < data.length ; i ++ ){
      normData[i] = getData1(data[i]);
    }
    return normData;
  }

  public double[] getIntData1(int data[]){
    getMin getMin1 = new getMin();
    getMax getMax1 = new getMax();
    double min = getMin1.setData(data);
    double max = getMax1.setData(data);
    double[] normData = new double[data.length];

    for(int i = 0 ; i < data.length ; i ++ ){
      normData[i] = (data[i] - min)/ (max - min);
    }
    return normData;
  }

  public double[][] getIntData2(int data[][]){
    double[][] normData = new double[data.length][data[0].length];

    for(int i = 0 ; i < data.length ; i ++ ){
      normData[i] = getIntData1(data[i]);
    }
    return normData;
  }

  public static void main(String[] args) {
    normalize normalize = new normalize();
  }
}
