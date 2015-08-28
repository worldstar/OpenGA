package openga.util;

/**
 * <p>Title: The OpenGA project</p>
 * <p>Description: The project is to build general framework of Genetic algorithm and problem independent.</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class getMax {

  public getMax() {
  }

  double max = 0;
  //overloading
  public void setData(double vals[], int vals2[]){

  }

  public double setData(double vals[]){
    for(int i = 0 ; i < vals.length ; i ++ ){
      if(vals[i] >= max){
        max = vals[i];
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

  public double getMax(){
    return max;
  }

  public static void main(String[] args) {
  

    getMax getMax1 = new getMax();

    double A[] = new double[]{5106.1, 506, 6094.5, 54.150, 5.547, 186.151};
    double max = getMax1.setData(A);
    System.out.println("Max is "+max);

    int B[] = new int[]{5106, 506, 6094, 54, 5, 186};
    int maxInt = getMax1.setData(B);
    System.out.println("maxInt is "+maxInt);

    double C[] = new double[10];
    for(int i = 0 ; i < C.length ; i++){
      C[i] = Math.random();
      System.out.print(C[i]+" ");
    }
    
    getMax getMax2 = new getMax();
    max = getMax2.setData(C);
    System.out.println("\nMax is "+max);

    int D[] = new int[5];
    for(int i = 0 ; i < D.length ; i++){
      D[i] = (int)(Math.random()*30);
      System.out.print(D[i]+" ");
    }

    getMax getMax3 = new getMax();
    max = getMax3.setData(D);
    System.out.println("\nMax is "+max);

  }
}
