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
    private static int TxtLength ; //Read TotalLength of the Txt 
    private int ReadTxtSize; //Current Length of the Txt
    private int[] completionTime;
    private int[] Sequence;
    private int[] processingTime;
    private int[] releaseDate;
    private int[] dueDate;
    private int[] deadline;
    private double[] profit;
      
    public void setReadTxtData(String fileName) {
        this.fileName = fileName;
    }
        
    private void ReadTxt() throws IOException{
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
    
    private void SaveValueOfTxt(int TxtSize) {
      if(TxtSize > 0)
      {
          //Set Size
          int processingTimeindex = 0,releaseDateindex = 1,
                  dueDateindex = 2 , deadlineindex = 3 ,profitindex = 4;
          Number = new int[TxtSize];
          processingTime = new int[TxtSize];
          releaseDate = new int[TxtSize];
          dueDate = new int[TxtSize];
          deadline = new int[TxtSize];
          profit = new double[TxtSize];
          setSize(TxtSize);
          //save value of Txt
          for (int i = 0; i < TxtSize; i++) 
          {
              Number[i] = (i+1);
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
          }
      }else
      {
        System.out.println("Current Length of SaveValueOfTxt must be greater than zero.");
      }
    }
    
    public void output(){
      if(ReadTxtSize > 0)
      {
        for(int i = 0 ; i < Number.length;i++)
        {
          System.out.println("Number : " + Number[i] + "\t");
          System.out.println("processingTime : " + processingTime[i] + "\t");
          System.out.println("releaseDate : " + releaseDate[i] + "\t");
          System.out.println("dueDate : " + dueDate[i] + "\t");
          System.out.println("deadline : " + deadline[i] + "\t");
          System.out.println("profit : " + profit[i] + "\t");
        }
      }
    }
    
    public void setSize(int ReadTxtSize) {
      if(ReadTxtSize > 0 && ReadTxtSize <= TxtLength)
        {
          this.ReadTxtSize = ReadTxtSize;
        }else
        {
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
    
    public int[] getcompletionTime() {
        return completionTime;
    }

    public int[] getSequence() {
        return Sequence;
    }

    public int[] getProcessingTime() {
        return processingTime;
    }

    public int[] getReleaseDate() {
        return releaseDate;
    }

    public int[] getDueDate() {
        return dueDate;
    }

    public int[] getDeadline() {
        return deadline;
    }

    public double[] getProfit() {
        return profit;
    }
    
    public String getTxtData()
    {
      return fileName;
    }
    
    public static void main(String[] args) throws IOException{
      {
        readParalleMachineOAS RT = new readParalleMachineOAS();
        RT.setReadTxtData(".\\instances\\ParallelMachineSetup\\Balanced\\2Machines\\20on2Rp50Rs50_1.txt");
        RT.ReadTxt();
        RT.SaveValueOfTxt(TxtLength);
        RT.output();
      }
    }
}