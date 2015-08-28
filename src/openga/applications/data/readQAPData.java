package openga.applications.data;
import java.io.*;
import java.util.*;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: To read QAP data instances.</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class readQAPData {
  public readQAPData() {
  }
  String fileName, message = "";
  int flow[][];
  int distance[][];
  int numberOfItems = 20;

  public void setData(String fileName){
    this.fileName = fileName;
    if( fileName == null ){
        System.out.println( "Specify the file name please.");
        System.exit(1);
    }
  }

  public void setData(int numberOfItems){
    this.numberOfItems = numberOfItems;
  }

  public void getDataFromFile(){
    // Read customers from file
    //System.out.print( "Loading customers from file..." );
    //to generate file data into message variable.

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
        numberOfItems = Integer.parseInt(tokens.nextToken());
        flow = new int[numberOfItems][numberOfItems];
        distance = new int[numberOfItems][numberOfItems];

        for(int i = 0 ; i < numberOfItems ; i ++ ){
          for(int j = 0 ; j < numberOfItems ; j ++ ){
            flow[i][j] = Integer.parseInt(tokens.nextToken());
          }
        }

        for(int i = 0 ; i < numberOfItems ; i ++ ){
          for(int j = 0 ; j < numberOfItems ; j ++ ){
            distance[i][j] = Integer.parseInt(tokens.nextToken());
          }
        }
    }   //end try
    catch( Exception e )
    {
        e.printStackTrace();
        System.out.println(e.toString());
    }   // end catch
    //System.out.println( "done" );
  }

  public int[][] getFlow(){
    return flow;
  }

  public int[][] getDistance(){
    return distance;
  }

  public int getSize(){
    return numberOfItems;
  }
}
