package openga.applications.data;
import java.io.*;
import java.util.*;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: Reeves (1995) flowshop instances which is available on OR-Library.</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class readFlowShopRevInstance {
  public readFlowShopRevInstance() {
  }

  String fileName, message = "";
  int processingTime[][];
  int numberOfJobs = 20, numberOfMachines = 3;
  String testInstance = "car8.txt";
  String fileIDs[] = new String[]{"rec01.txt","rec03.txt","rec05.txt", "rec07.txt",
      "rec09.txt","rec11.txt","rec13.txt","rec15.txt","rec17.txt", "rec19.txt",
      "rec21.txt","rec23.txt","rec25.txt","rec27.txt","rec29.txt", "rec31.txt",
      "rec33.txt","rec35.txt","rec37.txt","rec39.txt","rec41.txt"};

  public String getFileName(int index){
    return fileIDs[index];
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
        System.out.println(fileName);
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
        numberOfJobs = Integer.parseInt(tokens.nextToken());
        numberOfMachines = Integer.parseInt(tokens.nextToken());

        //to set the coordination and demand of each customer
        processingTime = new int[numberOfJobs][numberOfMachines];

        for(int i = 0 ; i < numberOfJobs ; i ++ ){
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

  public int getNumberOfJobs(){
    return numberOfJobs;
  }

  public int getNumberOfMachines(){
    return numberOfMachines;
  }
}