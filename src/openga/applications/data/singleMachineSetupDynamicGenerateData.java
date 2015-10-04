package openga.applications.data;
import java.io.*;
import java.util.*;
import openga.util.fileWrite1;
/**
 * <p>Title: single machine scheduling problems with dynamic arrival time and setup 
 * consideration in a common due date environment</p>
 * <p>Description: We modify the instances from Rabadi et al., 2013.</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class singleMachineSetupDynamicGenerateData {
  public singleMachineSetupDynamicGenerateData() {
  }
  String fileName, message = "";
  String Instances[] = new String[]{""};
  int processingTime[];
  int adjustedProcessingTime[][];
  int dynamicArrivalTime[];
  int size;

  public void setData(String fileName, int size){
    this.fileName = fileName;
    this.size = size;
    dynamicArrivalTime = new int[size];
    
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
        //size = Integer.parseInt(tokens.nextToken());
        adjustedProcessingTime = new int[size][size];

        for(int i = 0 ; i < size ; i ++ ){
          for(int j = 0 ; j < size ; j ++ ){
            adjustedProcessingTime[i][j] = Integer.parseInt(tokens.nextToken());
          }
        }                
        
        //Dynamic Arrival Time
        for(int i = 0 ; i < size ; i ++ ){
          dynamicArrivalTime[i] = Integer.parseInt(tokens.nextToken());
        }    
        
        calcProcessingTime(adjustedProcessingTime);
        
    }   //end try
    catch( Exception e )
    {
        e.printStackTrace();
        System.out.println(e.toString());
    }   // end catch
    //System.out.println( "done" );
  }
  
  public void calcProcessingTime(int[][] adjustedProcessingTime){
    //The min processing time is from the Adjusted processing time * 0.5
    processingTime = new int[adjustedProcessingTime.length];
    
    for(int i = 0 ; i < adjustedProcessingTime.length ; i ++){
      int min = Integer.MAX_VALUE;
      
      for(int j = 0 ; j < this.adjustedProcessingTime[0].length ; j ++){
        if(min > adjustedProcessingTime[i][j]){
          min = adjustedProcessingTime[i][j];
        }
      }      
      processingTime[i] = (int)(min * 0.8);
    }    
  }
  
  public void output(String fileName){
    String outputString = "";
    
    //output the processing time first
    for(int i = 0 ; i < processingTime.length ; i ++){
      outputString += processingTime[i] +" ";
    }     
    outputString += "\n";
    
    //output the adjusted processing time
    for(int i = 0 ; i < adjustedProcessingTime.length ; i ++){      
      for(int j = 0 ; j < adjustedProcessingTime[0].length ; j ++){
        outputString += adjustedProcessingTime[i][j] +" ";
      }      
      outputString += "\n";
    }     
    
    //output the dynamic arrival time
    for(int i = 0 ; i < dynamicArrivalTime.length ; i ++){
      outputString += dynamicArrivalTime[i] +" ";
    }    
    
    writeFile(fileName, outputString);
  }
  
  /**
   * Write the data into text file.
   */
  void writeFile(String fileName, String _result){
    fileWrite1 writeLotteryResult = new fileWrite1();
    writeLotteryResult.writeToFile(_result,fileName);
    Thread thread1 = new Thread(writeLotteryResult);
    thread1.run();
  }  
  

  public String getFileName(int index){
    return Instances[index];
  }

  public int[] getProcessingTime(){
    return processingTime;
  }
  

  public int[][] getAdjustedProcessingTime(){
    return this.adjustedProcessingTime;
  }  
  
  public int[] getDynamicArrivalTime(){
    return dynamicArrivalTime;
  }  

  public int getSize(){
    return size;
  }
  
    public static void main(String[] args) {
        int jobSets[] = new int[]{200};//10, 15, 20, 25, 50, 100, 150, 200//20, 30, 40, 50, 60, 90, 100, 200//20, 40, 60, 80
        int instanceReplication = 15;
        String types[] = new String[]{"low", "med", "high"};//
        

        for (int j = 0; j < jobSets.length; j++) {//jobSets.length
            for (int k = 2; k <= instanceReplication; k++) { 
              for(int a = 0 ; a < types.length ; a ++){
                singleMachineSetupDynamicGenerateData readSingleMachineData1 
                        = new singleMachineSetupDynamicGenerateData();
                int numberOfJobs = jobSets[j];
                String fileName = "instances/SingleMachineSetupDynamicArrival/"+types[a]+"/"+jobSets[j]+"_"+k+".etp";
                System.out.print(fileName + "\t");
                readSingleMachineData1.setData(fileName, jobSets[j]);
                readSingleMachineData1.getDataFromFile();                                        
                int processingTime[] = readSingleMachineData1.getProcessingTime();
                int adjustedProcessingTime[][] = readSingleMachineData1.getAdjustedProcessingTime();
                int dynamicArrivalTime[] = readSingleMachineData1.getDynamicArrivalTime();                                   
                readSingleMachineData1.output(fileName);
                System.out.println(fileName);
              }
            }
        }
    }  
}