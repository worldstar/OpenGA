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

public class readFlowShopData {
  public readFlowShopData() {
  }
  String fileName, message = "";
  int processingTime[][];
  int dueDate[];
  int numberOfJobs = 20, numberOfMachines = 3;

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

  public void getDataFromFile(){
    // Read customers from file
    //System.out.print( "Loading customers from file..." );
    //to generate file data into message variable.

    try
    {
        File file = new File( fileName );
        FileInputStream fis = new FileInputStream( file );
        DataInputStream in = new DataInputStream(fis);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String message = "", eachLine = "";

        while ((eachLine = br.readLine()) != null)   {
          message += eachLine;
        }

        //System.out.println(message);
        StringTokenizer tokens = new StringTokenizer(message);
        //set the coordinates of depot.
        int length = 0;

        //to set the coordination and demand of each customer
        processingTime = new int[numberOfJobs][numberOfMachines];
        dueDate = new int[numberOfJobs];

        for(int i = 0 ; i < numberOfJobs ; i ++ ){
          dueDate[i] = Integer.parseInt(tokens.nextToken());
          for(int j = 0 ; j < numberOfMachines ; j ++ ){
            processingTime[i][j] = Integer.parseInt(tokens.nextToken());
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

  public int[][] getPtime(){
    return processingTime;
  }

  public int[] getDueDate(){
    return dueDate;
  }

}