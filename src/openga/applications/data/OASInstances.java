package openga.applications.data;

import java.io.*;
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
  /*
    public static void main(String[] args) {
        OASInstances OAS = new OASInstances();
        OAS.setData(".\\instances\\SingleMachineOAS\\10orders\\Tao1\\R1\\Dataslack_10orders_Tao1R1_1.txt",10);
        OAS.getDataFromFile();
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
      s = new double[size][size];

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

      br.readLine();
      for (int i = 0; i < size; i++) {
        tmp = br.readLine().split(",");
        for (int j = 0; j < size; j++) {
          s[j][i] = Double.parseDouble(tmp[j + 1]);
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
