package openga.applications.data;
import java.io.*;
import java.util.*;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: For the BKY single machine instances.</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class readSingleMachine {
  public readSingleMachine() {
  }

  String fileName = "", message = "";
  int numberOfJobs = 20;
  int processingTime[] = new int[]{6, 3, 5, 3, 3, 3};//4, 7, 1, 6, 3
  int releaseTime[];
  int dueDate[] = new int[]{6, 7, 10, 6, 14, 14};
  int pTimeTest1[] = new int[]{6, 4, 7, 5};
  int dueTest1[] = new int[]{9, 10, 8, 6};
  double alpha[];
  double beta[];

  public String getFileName(int numberOfJobs, int replication){
    return "bky"+numberOfJobs+"_"+replication;
  }

  public void setData(String fileName){
    this.fileName = fileName;
    if( fileName == null ){
        System.out.println( "Specify the file name please.");
        System.exit(1);
    }
  }

  public void setData(int numberOfJobs){
    this.numberOfJobs = numberOfJobs;
  }

  public boolean testReadData(){
    boolean canreadFile = false;
    try
    {
        File file = new File( fileName );
        canreadFile = file.canRead();
        //System.out.println(fileName+" "+canreadFile);
      }   //end try
      catch( Exception e )
      {
          e.printStackTrace();
          System.out.println(e.toString());
          System.exit(0);
      }   // end catch
      return canreadFile;
  }

  public void getDataFromFile(){
    try
    {
        File file = new File( fileName );
        FileInputStream fis = new FileInputStream( file );
        FileReader fr = new FileReader(fileName);
        //System.out.println("讀取檔案ok\n");

        //讀取一個字元
        int c = fr.read();
        while(c != -1){                         //是否到達最後一行
           message += (char)c;                  //ANSI轉為char
           c = fr.read();                       //是否要換行
        }//end while

        fr.close();
        //System.out.println(message);
        StringTokenizer tokens = new StringTokenizer(message);
        //set the coordinates of depot.
        int length = 0;

        //to set the coordination and demand of each customer
        int size = numberOfJobs = Integer.parseInt(tokens.nextToken());
        processingTime = new int[size];
        releaseTime = new int[size];
        dueDate = new int[size];
        alpha = new double[size];
        beta = new double[size];

        for(int i = 0 ; i < size ; i ++ ){
          int tempArray[] = new int[5];
          tempArray[0] = Integer.parseInt(tokens.nextToken());
          tempArray[1] = Integer.parseInt(tokens.nextToken());
          tempArray[2] = Integer.parseInt(tokens.nextToken());
          tempArray[3] = Integer.parseInt(tokens.nextToken());
          tempArray[4] = Integer.parseInt(tokens.nextToken());
          //System.out.println(tempArray[0]+" "+tempArray[1]+" "+tempArray[2]+" "+tempArray[3]+" "+tempArray[4]);

          processingTime[i] = tempArray[0];
          releaseTime[i] = tempArray[1];
          dueDate[i] = tempArray[2];
          alpha[i] = tempArray[3];
          beta[i] = tempArray[4];
        }
    }   //end try
    catch( Exception e )
    {
        e.printStackTrace();
        System.out.println(e.toString());
    }   // end catch
    //System.out.println( "done" );
  }


  public int[] getPtime(){
    return processingTime;
  }

  public int[] getDueDate(){
    return dueDate;
  }

  public double[] getAlpha(){
    return alpha;
  }

  public double[] getBeta(){
    return beta;
  }

  public int getLength(){
    return numberOfJobs;
  }

}