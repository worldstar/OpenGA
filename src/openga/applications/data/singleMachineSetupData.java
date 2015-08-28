package openga.applications.data;
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
        DataInputStream in = new DataInputStream(fis);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String message = "", eachLine = "";

        while ((eachLine = br.readLine()) != null)   {
          message += eachLine;
        }

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