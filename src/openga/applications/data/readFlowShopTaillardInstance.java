package openga.applications.data;
import java.io.*;
import java.util.*;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: Taillard flowshop instances</p>
 * <p>Copyright: Copyright (c) 200</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class readFlowShopTaillardInstance extends readFlowShopRevInstance {
  public readFlowShopTaillardInstance() {
  }

  String fileName = "", message = "";
  int processingTime[][];
  int numberOfJobs = 20, numberOfMachines = 3;

  public String getFileName(int numberOfJobs, int numberOfMachines, int replication){
    return numberOfJobs+"-"+numberOfMachines+"-"+replication+".txt";
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

  public void getDataFromFile(){
    // Read customers from file
    //System.out.print( "Loading customers from file..." );
    //to generate file data into message variable.

    try
    {
        //System.out.println(fileName);
        File file = new File( fileName );
        FileInputStream fis = new FileInputStream( file );
        DataInputStream in = new DataInputStream(fis);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String message = "", eachLine = "";

        while ((eachLine = br.readLine()) != null)   {
          message += eachLine + " ";
        }

        //System.out.println(message);
        StringTokenizer tokens = new StringTokenizer(message);
        //set the coordinates of depot.
        int length = 0;
        numberOfJobs = Integer.parseInt(tokens.nextToken());
        numberOfMachines = Integer.parseInt(tokens.nextToken());

        //to set the coordination and demand of each customer
        processingTime = new int[numberOfJobs][numberOfMachines];

        for(int i = 0 ; i < numberOfMachines ; i ++ ){
          for(int j = 0 ; j < numberOfJobs ; j ++ ){
            processingTime[j][i] = Integer.parseInt(tokens.nextToken());
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

  public int getNumberOfMachines(){
    return numberOfMachines;
  }

  public int getNumberOfJobs(){
    return numberOfJobs;
  }
}