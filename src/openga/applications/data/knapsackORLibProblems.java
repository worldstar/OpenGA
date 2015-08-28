/*
 * We take the instances from the OR-Library.
 * The number of variables n (items) was set to 100, 250, and 500.
 * The number of m (knapsacks) was 5, 10, and 30.
 * Each combination of n and m has 30 instance replications.
 */

package openga.applications.data;
import java.io.*;
import java.util.*;
/**
 *
 * @author user
 */
public class knapsackORLibProblems {
  int numberOfInstanceReplication = 0;
  double profit[][];
  double weights[][][];
  double rightHandSide[][];
  String fileName = "";

  public void readInstanceData(int n, int m){
    fileName = "instances\\knapsack\\";
    fileName += setItemsKnapsacks(n, m);
    getDataFromFile(fileName, n, m);
  }

  public String setItemsKnapsacks(int n, int m){
    if(n == 100 && m == 5){
      fileName = "mknapcb1.txt";
    }
    else if(n == 250 && m == 5){
      fileName = "mknapcb2.txt";
    }
    else if(n == 500 && m == 5){
      fileName = "mknapcb3.txt";
    }
    else if(n == 100 && m == 10){
      fileName = "mknapcb4.txt";
    }
    else if(n == 250 && m == 10){
      fileName = "mknapcb5.txt";
    }
    else if(n == 500 && m == 10){
      fileName = "mknapcb6.txt";
    }
    else if(n == 100 && m == 30){
      fileName = "mknapcb7.txt";
    }
    else if(n == 250 && m == 30){
      fileName = "mknapcb8.txt";
    }
    else if(n == 500 && m == 30){
      fileName = "mknapcb9.txt";
    }
    else if(n == 10 && m == 10){
      fileName = "knapsack1010.txt";
    }
    else if(n == 15 && m == 10){
      fileName = "knapsack1510.txt";
    }
    return fileName;
  }

  public void getDataFromFile(String fileName, int n, int m){
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
        numberOfInstanceReplication = Integer.parseInt(tokens.nextToken());

        profit = new double[numberOfInstanceReplication][n];
        weights = new double[numberOfInstanceReplication][m][n];
        rightHandSide = new double[numberOfInstanceReplication][m];

        for(int k = 0 ; k < numberOfInstanceReplication ; k ++){
        int numberOfItems = Integer.parseInt(tokens.nextToken());
        int numberOfKnapsack = Integer.parseInt(tokens.nextToken());
        int currentOpt = Integer.parseInt(tokens.nextToken());

          //For the profit
          for(int j = 0 ; j < numberOfItems ; j ++){
            profit[k][j] = Double.parseDouble(tokens.nextToken());
          }

          //For the m constraints
          for(int i = 0 ; i < numberOfKnapsack ; i ++){
            for(int j = 0 ; j < numberOfItems ; j ++){
              weights[k][i][j] = Double.parseDouble(tokens.nextToken());
            }
          }

          //For right-hand side
          for(int i = 0 ; i < numberOfKnapsack ; i ++){
            rightHandSide[k][i] = Double.parseDouble(tokens.nextToken());
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

  String values[];
  int valuesCounter = 0;

  int getNextIntValue(){
    int value1 = 0;


    return value1;
  }  

  public int getInstanceReplications(){
    return numberOfInstanceReplication;
  }

  public double[][] getProfit(){
    return profit;
  }

  public double[][][] getWeights(){
    return weights;
  }

  public double[][] getRightHandSide(){
    return rightHandSide;
  }
}
