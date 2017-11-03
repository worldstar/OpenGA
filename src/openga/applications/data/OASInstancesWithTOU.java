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

public class OASInstancesWithTOU {

  private String fileName;
  private String writePath;
  private double[] r;       //  release date.
  private double[] p;       //  processing time
  private double[] d;       //  due-date
  private double[] d_bar;   //  deadline
  private double[] e;       //  revenue
  private double[] w;       //  weight
  private double[] power;   //  power
  private double[][] s;     //  setup times
  private int size;          //  instance lengh

  public OASInstancesWithTOU() {
  }

//  Testing for read files.
//  /*
    public static void main(String[] args) throws IOException {
        OASInstancesWithTOU OAS = new OASInstancesWithTOU();
        OAS.setData(".\\instances\\SingleMachineOASWithTOU\\10orders\\Tao5\\R7\\Dataslack_10orders_Tao5R7_10.txt",10);
        OAS.getDataFromFile();
        System.out.println("r: " + Arrays.toString(OAS.r));
        System.out.println("p: " + Arrays.toString(OAS.p));
        System.out.println("d: " + Arrays.toString(OAS.d));
        System.out.println("d_bar: " + Arrays.toString(OAS.d_bar));
        System.out.println("e: " + Arrays.toString(OAS.e));
        System.out.println("w: " + Arrays.toString(OAS.w));
        System.out.println("power: " + Arrays.toString(OAS.power));
        System.out.println("s: ");
        for(int i=0; i<OAS.s.length; i++){
          System.out.println(Arrays.toString(OAS.s[i]));
        }
        


//        int[] orders = new int[]{10, 15, 20, 25, 50, 100};//10, 15, 20, 25, 50, 100
//        int[] Tao = new int[]{1, 3, 5, 7, 9};//1, 3, 5, 7, 9
//        int[] R = new int[]{1, 3, 5, 7, 9};//1, 3, 5, 7, 9
//        int instanceReplications = 10;
//        
//        
//        for (int i = 0; i < orders.length; i++) {
//          for (int j = 0; j < Tao.length; j++) {
//            for (int k = 0; k < R.length; k++) {
//              for (int l = 0; l < instanceReplications; l++) 
//              {
//                String instanceName = "@../../instances/SingleMachineOAS/" + orders[i] + "orders/Tao" + Tao[j] + "/R" + R[k] 
//                      + "/Dataslack_" + orders[i] + "orders_Tao" + Tao[j] + "R" + R[k] + "_" + (l + 1) + ".txt";
//                String instanceName2 = "@../../instances/SingleMachineOASWithTOU/" + orders[i] + "orders/Tao" + Tao[j] + "/R" + R[k] 
//                      + "/Dataslack_" + orders[i] + "orders_Tao" + Tao[j] + "R" + R[k] + "_" + (l + 1) + ".txt";
//              
//                System.out.println(instanceName);
//                System.out.println("Copy");
//                System.out.println(instanceName2);
//                System.out.println();
//                
//                //readFile
//                OAS.setData(instanceName,orders[i]);
//                OAS.getDataFromFile();
//              
//                //output
//                OAS.setWriteData(instanceName2);
//                OAS.output();
//              }
//            } 
//          }
//        }
        
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
      power = new double[size];
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
      
      tmp = br.readLine().split(",");//split 0,10,10,2,4,6,4,5,7,3,5,0
      for (int i = 0; i < size; i++) {  //i = orders,test 10
        power[i] = Double.parseDouble(tmp[i + 1]);
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
  
  public void output () throws IOException
  {
    FileWriter sw = new FileWriter(writePath, false);
    //r
    for(int i = 0 ; i < r.length ; i++)
    {
      if(i == 0)
      {
        sw.write("0,");
      }
      if(i == r.length-1)
      {
        sw.write((int)r[i] + ",0\n");
      }else
      {
        sw.write((int)r[i] + ",");
      }
    }
    //p
    for(int i = 0 ; i < p.length ; i++)
    {
      if(i == 0)
      {
        sw.write("0,");
      }
      if(i == p.length-1)
      {
        sw.write((int)p[i] + ",0\n");
      }else
      {
        sw.write((int)p[i] + ",");
      }
    }
    //d
    for(int i = 0 ; i < d.length ; i++)
    {
      if(i == 0)
      {
        sw.write("0,");
      }
      if(i == d.length-1)
      {
        sw.write((int)d[i] + ",0\n");
      }else
      {
        sw.write((int)d[i] + ",");
      }
    }
    //d_bar
    for(int i = 0 ; i < d_bar.length ; i++)
    {
      if(i == 0)
      {
        sw.write("0,");
      }
      if(i == d_bar.length-1)
      {
        sw.write((int)d_bar[i] + ",0\n");
      }else
      {
        sw.write((int)d_bar[i] + ",");
      }
    }
    //e
    for(int i = 0 ; i < e.length ; i++)
    {
      if(i == 0)
      {
        sw.write("0,");
      }
      if(i == e.length-1)
      {
        sw.write((int)e[i] + ",0\n");
      }else
      {
        sw.write((int)e[i] + ",");
      }
    }
    //w
    for(int i = 0 ; i < w.length ; i++)
    {
      if(i == 0)
      {
        sw.write("0,");
      }
      if(i == w.length-1)
      {
        sw.write(w[i] + ",0\n");
      }else
      {
        sw.write(w[i] + ",");
      }
    }
    
    for(int i = 0 ; i < power.length ; i++)
    {
      power[i] = (int)(Math.random()*100+1);
    }
    //power
    for(int i = 0 ; i < power.length ; i++)
    {
      if(i == 0)
      {
        sw.write("0,");
      }
      if(i == power.length-1)
      {
        sw.write((int)power[i] + ",0\n");
      }else
      {
        sw.write((int)power[i] + ",");
      }
    }
    
    //s
    for(int i = 0 ; i < s.length ; i++)
    {
      for(int j = 0 ; j < s[0].length ; j++)
      {
        if(j == 0)
        {
          sw.write("0,");
        }
        if(j == s[0].length-1)
        {
          sw.write((int)s[i][j] + ",0\n");
        }else
        {
          sw.write((int)s[i][j] + ",");
        }
      }
    }
    
    sw.write("0,0,0,0,0,0,0,0,0,0,0,0\n");
    sw.close();
  }
  
  public void setWriteData(String writePath) {
    if (writePath == null) {
      System.out.println("Specify the file name please.");
      System.exit(1);
    }

    this.writePath = writePath;
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
  
  public double[] getPower() {
    return power;
  }

  public double[][] getS() {
    return s;
  }

  public int getSize() {
    return size;
  }

}
