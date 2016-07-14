package openga.applications.data;
import java.io.*;
import java.util.*;
/**
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
  
  public String fileName;
  public Double [] r;       //  release date.
  public Double [] p;       //  processing time
  public Double [] d;       //  due-date
  public Double [] d_bar;   //  deadline
  public Double [] e;       //  revenue
  public Double [] w;       //  weight
  public Double [][] s;     //  setup times
  public int size;          //  instance lengh
  
  public OASInstances() {
  }
    
//  Testing for read files.
//    public static void main(String[] args) {
//        OASInstances OAS = new OASInstances();
//        OAS.setData(".\\instances\\SingleMachineOAS\\10orders\\Tao1\\R1\\Dataslack_10orders_Tao1R1_1.txt");
//        OAS.getDataFromFile();
//    }
    
    public void getDataFromFile(){
      try
      {
        FileInputStream fis = new FileInputStream(fileName);
        DataInputStream in = new DataInputStream(fis);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        
        String line;	    
        String[] tmp;
        
        tmp = br.readLine().split(",");
        size = tmp.length;
        r = new Double [tmp.length];
        for(int i = 0; i < tmp.length; i++){
          r[i] = Double.parseDouble(tmp[i]);
        }
        
        tmp = br.readLine().split(",");
        p = new Double [tmp.length];
        for(int i = 0; i < tmp.length; i++){
          p[i] = Double.parseDouble(tmp[i]);
        }
        
        tmp = br.readLine().split(",");
        d = new Double [tmp.length];
        for(int i = 0; i < tmp.length; i++){
          d[i] = Double.parseDouble(tmp[i]);
        }
        
        tmp = br.readLine().split(",");
        d_bar = new Double [tmp.length];
        for(int i = 0; i < tmp.length; i++){
          d_bar[i] = Double.parseDouble(tmp[i]);
        }
        
        tmp = br.readLine().split(",");
        e = new Double [tmp.length];
        for(int i = 0; i < tmp.length; i++){
          e[i] = Double.parseDouble(tmp[i]);
        }
        
        tmp = br.readLine().split(",");
        w = new Double [tmp.length];
        for(int i = 0; i < tmp.length; i++){
          w[i] = Double.parseDouble(tmp[i]);
        }
        
        s = new Double [tmp.length][tmp.length];
        int n = 0;
        while ((line = br.readLine()) != null) { 
          tmp = line.split(",");
          for(int i = 0; i < tmp.length; i++){
            s[n][i] = Double.parseDouble(tmp[i]);
          }
          n++;
        }
      }
      catch( Exception e )
      {
        e.printStackTrace();
        System.out.println(e.toString());
      }
    }

  public void setData(String fileName){
    this.fileName = fileName;
    if( fileName == null ){
        System.out.println( "Specify the file name please.");
        System.exit(1);
    }
  }
  
  public String getFileName() {
    return fileName;
  }

  public Double[] getR() {
    return r;
  }

  public Double[] getP() {
    return p;
  }

  public Double[] getD() {
    return d;
  }

  public Double[] getD_bar() {
    return d_bar;
  }

  public Double[] getE() {
    return e;
  }

  public Double[] getW() {
    return w;
  }

  public Double[][] getS() {
    return s;
  }

}
