package openga.util.data;

import java.io.*;
import java.util.*;
/********************************************************************
 * The program reads the data from text file to import the coordinate
 * X and Y and demand of each customer.
 ********************************************************************/

public class readSDVRPdata {
  public readSDVRPdata() {
  }
  String fileName, message = "";
  double coordinates[][];
  Vector vectorXY = new Vector();
  Vector demandVector = new Vector();
  int demand[];

  public void setData(String fileName){
    this.fileName = fileName;
    if( fileName == null )
    {
        System.out.println( "Specify the file name please." );
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
        //set the coordinates of depot.
        int length = 0;

        //to set the coordination and demand of each customer
        while(tokens.hasMoreTokens()){
          double tempArray[] = new double[2];
          tempArray[0] = Double.parseDouble(tokens.nextToken());
          tempArray[1] = Double.parseDouble(tokens.nextToken());
          vectorXY.addElement(tempArray);

          if(length != 0){
            demandVector.addElement(tokens.nextToken());
          }
          length ++;
        }

        coordinates = new double[vectorXY.size()][2];
        for( int i = 0; i < coordinates.length; i++ )
        {   // Extract tuple
            double[] tuple = (double[]) vectorXY.elementAt( i );
            coordinates[i] = tuple;
        }   // end for: through each pair

        demand = new int[demandVector.size()];
        for( int i = 0; i < demandVector.size() ; i++ ){
            demand[i] = Integer.parseInt(String.valueOf(demandVector.elementAt( i )));
        }   // end for: through each pair
    }   //end try
    catch( Exception e )
    {
        e.printStackTrace();
        System.out.println(e.toString());
    }   // end catch
    //System.out.println( "done" );
  }


  public static void main(String[] args) {
    readSDVRPdata readSDVRPdata1 = new readSDVRPdata();
    readSDVRPdata1.setData("customers.txt");
    readSDVRPdata1.getDataFromFile();
  }

}