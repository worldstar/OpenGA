package openga.applications.data;
import java.io.*;
import java.util.*;
import openga.chromosomes.*;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class readFlowShopTaillardInstancePop extends readFlowShopTaillardInstance {
  public readFlowShopTaillardInstancePop() {
  }

  String fileName = "", message = "";
  int processingTime[][];
  int numberOfJobs = 20, numberOfMachines = 3;
  int NEHpopSize = 500;

  int NEHpopulation[][];
  double CPUTime[];//The time of NEH


  public void setPopData(int popSize){
    this.NEHpopSize = popSize;
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
        NEHpopulation = new int[NEHpopSize][numberOfJobs];
        CPUTime = new double[NEHpopSize];

        //System.exit(0);

        for(int i = 0 ; i < NEHpopSize ; i ++ ){
          int tempInstanceID = Integer.parseInt(tokens.nextToken());
          for(int j = 0 ; j < numberOfJobs ; j ++ ){
            NEHpopulation[i][j] = Integer.parseInt(tokens.nextToken()) - 1;
            //System.out.print(NEHpopulation[i][j]+ " ");
          }
          //System.out.println();
          int obj = Integer.parseInt(tokens.nextToken());
          //String tempString = tokens.nextToken();
        }
    }   //end try
    catch( Exception e )
    {
        e.printStackTrace();
        System.out.println(e.toString());
    }   // end catch
    //System.out.println( "done" );
  }

  public void constructInitialSolutions(populationI _Population, int numberOfJob, int exactPopSize){
    int NEHSolns = 70;//100
    if(numberOfJob > 100){
      NEHSolns = 30;
    }

    for(int i = 0 ; i < NEHSolns ; i ++ ){
      if(numberOfJob < 100){//to get a random solution
        _Population.getSingleChromosome(i).setSolution(getOneRandomNEHpopulation());
      }
      else{
        _Population.getSingleChromosome(i).setSolution(NEHpopulation[i]);
      }
    }

    for(int i = NEHSolns ; i < exactPopSize ; i ++ ){
      int sequence[] = new int[numberOfJob];
      for(int j = 0 ; j < numberOfJob ; j ++ ){
        sequence[j] = j;
      }

      for(int j = 0 ; j < numberOfJob ; j ++ ){
        int cutPoint = (int)(Math.random()*numberOfJob);
        int temp = sequence[j];
        sequence[j] = sequence[cutPoint];
        sequence[cutPoint] = temp;
      }
    }
  }


  public int[][] getNEHpopulation(){
    return NEHpopulation;
  }

  public int[] getOneRandomNEHpopulation(){
    int index = (int)(Math.random()*NEHpopSize);
    return NEHpopulation[index];
  }

  public double[] getCPUTime(){
    return CPUTime;
  }
}