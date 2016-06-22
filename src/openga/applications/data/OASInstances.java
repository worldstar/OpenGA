package openga.applications.data;
import java.io.*;
import java.util.*;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class OASInstances {
  
  public String folderPath = "instances/SingleMachineOAS";
  public List<File> FileList = new ArrayList<>();
  
  public String fileName;
  public Double [] r;
  public Double [] p;
  public Double [] d;
  public Double [] d_bar;
  public Double [] e;
  public Double [] w;
  public Double [][] s;
  public int size;
  
  
  public OASInstances() {
  }
  
  /**
   * We obtain the instance from OASLIB.
   * @return
   */
    public void Import(String folderPath) {
        final File folder = new File(folderPath);
        Import(folder);
    }
    
    private void Import(File folder) {
        //FileList = new ArrayList<>();
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                Import(fileEntry);
            } else {
                FileList.add(fileEntry);
            }
        }
    }
        
    public List<File> getFileList() {
        return FileList;
    }
    
    public static void main(String[] args) {
        OASInstances OAS = new OASInstances();
        OAS.Import("instances/SingleMachineOAS");
        OAS.getDataFromFileList();
    }
    
    public void getDataFromFileList(){
      for(int i = 0; i < FileList.size(); i++){
//        System.out.println(FileList.get(i));
//        getDataFromFile(i);
      }
      int test = 250;
      System.out.println(FileList.get(test));
      getDataFromFile(test);
    }
    
    public void getDataFromFile(int fileNumber){
      try
      {
        FileInputStream fis = new FileInputStream(FileList.get(fileNumber));
        DataInputStream in = new DataInputStream(fis);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        
        
        String line;	    
        String[] tmp;
        
        tmp = br.readLine().split(",");
        size = tmp.length;
        r = new Double [tmp.length];
        for(int i = 0; i < tmp.length; i++){
          r[i] = Double.parseDouble(tmp[i]);
          System.out.println(r[i]);
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


  public String getFileName(int index){
    return FileList.get(index).toString();
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
