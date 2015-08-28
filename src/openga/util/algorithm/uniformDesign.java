package openga.util.algorithm;

/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: It generates weight vectors depended on uniform design concept.
 * Leung, Y. W. (2000). Multiobjective programming using uniform design and genetic algorithm. IEEE Transactions on Systems, Man and Cybernetics Part C: Applications and Reviews, 30(3), 293-304.</p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class uniformDesign {
  public uniformDesign() {
  }

  int n, q;//n: factors (objectives), q levels (points).
  int delta = 2;//It's from Leung's paper, which is suitable for 2 to 4 factos and the number of levels is 7.
  double uniformArry[][];
  double rowSum[];

  public void setData(int n, int q){
    this.n = n;
    this.q = q;

    uniformArry = new double[q][n];
    rowSum = new double[q];
  }

  public void startCalc(){
    if(q == 7){
       delta = 3;
    }

    if(q == 11){
       delta = 7;
    }

    for(int i = 0 ; i < q ; i ++ ){
      rowSum[i] = 0;
      for(int j = 0 ; j < n ; j ++ ){
        int indexi = i + 1;
        int indexj = j + 1;
        uniformArry[i][j] = indexi*Math.pow(delta, indexj - 1) % q + 1;
        rowSum[i] += uniformArry[i][j];
      }
    }

    for(int i = 0 ; i < q ; i ++ ){
      for(int j = 0 ; j < n ; j ++ ){
        uniformArry[i][j] /= rowSum[i];
      }
    }

    openga.util.printClass p1 = new openga.util.printClass();
    p1.printMatrix("uniformArry", uniformArry);
  }

  public double[][] getUniformArry(){
    return uniformArry;
  }

  public static void main(String[] args) {
    uniformDesign uniformDesign1 = new uniformDesign();
    uniformDesign1.setData(5, 7);
    uniformDesign1.startCalc();
  }
}