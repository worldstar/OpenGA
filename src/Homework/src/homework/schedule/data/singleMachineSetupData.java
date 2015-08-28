package homework.schedule.data;
import java.io.*;
import java.util.*;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class singleMachineSetupData {
  public singleMachineSetupData() {
  }
  String fileName, message = "";
  String Instances[] = new String[]{""};
  double processingTime[][];
  int size;

  public void setData(String fileName){
    this.fileName = fileName;
    if( fileName == null ){
        System.out.println( "Specify the file name please.");
        System.exit(1);
    }
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

        //to set the coordination and demand of each customer
        size = Integer.parseInt(tokens.nextToken());
        processingTime = new double[size][size];

        for(int i = 0 ; i < size ; i ++ ){
          for(int j = 0 ; j < size ; j ++ ){
            processingTime[i][j] = Double.parseDouble(tokens.nextToken());
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

  public String getFileName(int index){
    return Instances[index];
  }

  public double[][] getProcessingTime(){
    return processingTime;
  }

  public int getSize(){
    return size;
  }

}