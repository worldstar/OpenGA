/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package openga.applications.data;

import java.io.*;

/**
 *
 * @author Guo Yu-Cheng
 */
public class readParalleMachineOAS {

  private String fileName;
  private int[] Number;
  private String[] STxt;
  private final int NumberOfColumns = 5;
  public static int TxtLength; //Read TotalLength of the Txt 
  private int ReadTxtSize; //Current Length of the Txt
  private double[] completionTime;
  private double[] processingTime;
  private double[] releaseDate;
  private double[] dueDate;
  private double[] deadline;
  private double[] profit;
  private double[] weight;

  public void setReadTxtData(String fileName) {
    this.fileName = fileName;
  }

  public void ReadTxt() throws IOException {
    {
      //Read Txt
      FileReader fr = new FileReader(fileName);
      BufferedReader br = new BufferedReader(fr);
      String TxtAll = "", eachLine = "";
      while ((eachLine = br.readLine()) != null) {
        TxtAll += eachLine + "\n";
      }
      STxt = TxtAll.split("\t|\n");

      //Total TxtLength 
      TxtLength = (STxt.length / NumberOfColumns);
    }
  }

  public void SaveValueOfTxt(int TxtSize) {
    if (TxtSize > 0) {
      //Set Size
      int processingTimeindex = 0, releaseDateindex = 1,
              dueDateindex = 2, deadlineindex = 3, profitindex = 4;
      Number = new int[TxtSize];
      processingTime = new double[TxtSize];
      releaseDate = new double[TxtSize];
      dueDate = new double[TxtSize];
      deadline = new double[TxtSize];
      profit = new double[TxtSize];
      weight = new double[TxtSize];
      setSize(TxtSize);
      //save value of Txt
      for (int i = 0; i < TxtSize; i++) {
        Number[i] = (i + 1);
        processingTime[i] = Integer.parseInt(STxt[processingTimeindex]);
        processingTimeindex += NumberOfColumns;
        releaseDate[i] = Integer.parseInt(STxt[releaseDateindex]);
        releaseDateindex += NumberOfColumns;
        dueDate[i] = Integer.parseInt(STxt[dueDateindex]);
        dueDateindex += NumberOfColumns;
        deadline[i] = Integer.parseInt(STxt[deadlineindex]);
        deadlineindex += NumberOfColumns;
        profit[i] = Double.parseDouble(STxt[profitindex]);
        profitindex += NumberOfColumns;
        weight[i] = profit[i]/(deadline[i]-dueDate[i]);
      }
    } else {
      System.out.println("Current Length of SaveValueOfTxt must be greater than zero.");
    }
  }

  public void output() {
    if (ReadTxtSize > 0) {
      for (int i = 0; i < Number.length; i++) {
        System.out.println("Number : " + Number[i] + "\t");
        System.out.println("processingTime : " + processingTime[i] + "\t");
        System.out.println("releaseDate : " + releaseDate[i] + "\t");
        System.out.println("dueDate : " + dueDate[i] + "\t");
        System.out.println("deadline : " + deadline[i] + "\t");
        System.out.println("profit : " + profit[i] + "\t");
        System.out.println("weight : " + weight[i] + "\t");
      }
    }
  }

  public void setSize(int ReadTxtSize) {
    if (ReadTxtSize > 0 && ReadTxtSize <= TxtLength) {
      this.ReadTxtSize = ReadTxtSize;
    } else {
      System.out.println("Current Length of setSize is too long or too short.");
      System.exit(0);
    }
  }

  public int getReadTxtSize() {
    return ReadTxtSize;
  }

  public int getTxtLength() {
    return TxtLength;
  }

  public double[] getcompletionTime() {
    return completionTime;
  }

  public double[] getProcessingTime() {
    return processingTime;
  }

  public double[] getReleaseDate() {
    return releaseDate;
  }

  public double[] getDueDate() {
    return dueDate;
  }

  public double[] getDeadline() {
    return deadline;
  }

  public double[] getProfit() {
    return profit;
  }
  
  public double[] getWeight() {
    return weight;
  }

  public String getTxtData() {
    return fileName;
  }

  public static void main(String[] args) throws IOException {

    int numberOfMachines[] = new int[]{2};//2, 6, 12
    int numberOfJobs[] = new int[]{20, 40, 60, 80, 100, 120};//20, 40, 60, 80, 100, 120
    int startInstanceID = 1;
    int endStartInstanceID = 15;
    for (int m = 0; m < numberOfMachines.length; m++) {
      for (int j = 0; j < numberOfJobs.length; j++) {
        for (int i = startInstanceID; i <= endStartInstanceID; i++) {
          readParalleMachineOAS RT = new readParalleMachineOAS();
          String fileName = "instances\\ParallelMachineSetup\\OAS\\Balanced\\"
                  + numberOfMachines[m] + "machines\\" + numberOfJobs[j] + "on"
                  + numberOfMachines[m] + "Rp50Rs50_" + i + ".dat";
          System.out.println(fileName);
          RT.setReadTxtData(fileName);
          RT.ReadTxt();
          RT.SaveValueOfTxt(TxtLength);
          RT.output();

        }
      }
    }
  }
}
