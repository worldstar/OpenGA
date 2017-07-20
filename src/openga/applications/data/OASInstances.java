package openga.applications.data;

import java.io.*;
import java.util.Arrays;
import java.util.StringTokenizer;

/*
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Cheng Shiu University</p>
 * @authors Chen, Shih-Hsin ; Chang, Yu-Tang
 * @version 2.0
 * We obtain the instance from OASLIB.
 * Reference "A tabu search algorithm for order acceptance and scheduling", http://home.ku.edu.tr/~coguz/Research/Dataset_OAS.zip
 */

public class OASInstances {

  private String fileName;
  private double[] r;       //  release date.
  private double[] p;       //  processing time
  private double[] d;       //  due-date
  private double[] d_bar;   //  deadline
  private double[] e;       //  revenue
  private double[] w;       //  weight
  private double[][] s;     //  setup times
  private int size;          //  instance lengh

  public OASInstances() {
  }

//  Testing for read files.
//  /*
    public static void main(String[] args) {
        OASInstances OAS = new OASInstances();
        OAS.setData(".\\instances\\SingleMachineOAS\\10orders\\Tao5\\R7\\Dataslack_10orders_Tao5R7_10.txt",10);
        OAS.getDataFromFile();
        System.out.println("r: " + Arrays.toString(OAS.r));
        System.out.println("p: " + Arrays.toString(OAS.p));
        System.out.println("d: " + Arrays.toString(OAS.d));
        System.out.println("d_bar: " + Arrays.toString(OAS.d_bar));
        System.out.println("e: " + Arrays.toString(OAS.e));
        System.out.println("w: " + Arrays.toString(OAS.w));
        System.out.println("s: ");
        for(int i=0; i<OAS.s.length; i++){
          System.out.println(Arrays.toString(OAS.s[i]));
        }

    }
//    */
  public void getDataFromFile() {
    try {
      FileInputStream fis = new FileInputStream(fileName);
      DataInputStream in = new DataInputStream(fis);
      BufferedReader br = new BufferedReader(new InputStreamReader(in));
      String[] tmp;
      r = new double[size];
      p = new double[size];
      d = new double[size];
      d_bar = new double[size];
      e = new double[size];
      w = new double[size];
      s = new double[size+1][size];

      tmp = br.readLine().split(",");//split 0,10,10,2,4,6,4,5,7,3,5,0
      for (int i = 0; i < size; i++) {  //i = orders,test 10
        r[i] = Double.parseDouble(tmp[i + 1]);
      }

      tmp = br.readLine().split(",");//split 0,10,10,2,4,6,4,5,7,3,5,0
      for (int i = 0; i < size; i++) {  //i = orders,test 10
        p[i] = Double.parseDouble(tmp[i + 1]);
      }

      tmp = br.readLine().split(",");//split 0,10,10,2,4,6,4,5,7,3,5,0
      for (int i = 0; i < size; i++) {  //i = orders,test 10
        d[i] = Double.parseDouble(tmp[i + 1]);
      }

      tmp = br.readLine().split(",");//split 0,10,10,2,4,6,4,5,7,3,5,0
      for (int i = 0; i < size; i++) {  //i = orders,test 10
        d_bar[i] = Double.parseDouble(tmp[i + 1]);
      }

      tmp = br.readLine().split(",");//split 0,10,10,2,4,6,4,5,7,3,5,0
      for (int i = 0; i < size; i++) {  //i = orders,test 10
        e[i] = Double.parseDouble(tmp[i + 1]);
      }

      tmp = br.readLine().split(",");//split 0,10,10,2,4,6,4,5,7,3,5,0
      for (int i = 0; i < size; i++) {  //i = orders,test 10
        w[i] = Double.parseDouble(tmp[i + 1]);
      }

      for (int i = 0; i < size+1; i++) {
        tmp = br.readLine().split(",");
        for (int j = 0; j < size; j++) {
          s[i][j] = Double.parseDouble(tmp[j+1]);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println(e.toString());
    }
  }

  public void setData(String fileName, int size) {
    if (fileName == null) {
      System.out.println("Specify the file name please.");
      System.exit(1);
    }

    this.fileName = fileName;
    this.size = size;
  }

  public String getFileName() {
    return fileName;
  }

  public double[] getR() {
    return r;
  }

  public double[] getP() {
    return p;
  }

  public double[] getD() {
    return d;
  }

  public double[] getD_bar() {
    return d_bar;
  }

  public double[] getE() {
    return e;
  }

  public double[] getW() {
    return w;
  }

  public double[][] getS() {
    return s;
  }

  public int getSize() {
    return size;
  }

}
