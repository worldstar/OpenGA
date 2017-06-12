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
public class readParallelMachine {
    private String fileName;
    private String[] Number;
    private String[] STxt;
    private final int NumberOfColumns = 4; 
    private static int TxtLength ; //Read TotalLength of the Txt 
    private int ReadTxtSize; //Current Length of the Txt
    private int[] completionTime;
    private int[] Sequence;
    private int[] processingTime;
    private int[] releaseDate;
    private int[] dueDate;
    private int[] deadline;
    private double[] revenue;
      
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
            TxtLength = (STxt.length / NumberOfColumns)-1;
        }
    }
    
    private void SaveValueOfTxt(int TxtSize) {
      if(TxtSize > 0)
      {
          //Set Size
          int Numberindex = 4,due_dayindex = 5,processingTimeindex = 7;
          Number = new String[TxtSize];
          processingTime = new int[TxtSize];
          dueDate = new int[TxtSize];
          setSize(TxtSize);

          //save value of Txt
          for (int i = 0; i < TxtSize; i++) 
          {
              Number[i] = STxt[Numberindex];
              Numberindex += 4;
              dueDate[i] = Integer.parseInt(STxt[due_dayindex]);
              due_dayindex += 4;
              processingTime[i] = Integer.parseInt(STxt[processingTimeindex]);
              processingTimeindex += 4;
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
          System.out.println("processing time : " + processingTime[i] + "\t");
          System.out.println("due-date : " + dueDate[i] + "\t");
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

    public double[] getRevenue() {
        return revenue;
    }
    
    public String getTxtData()
    {
      return fileName;
    }
    
    public static void main(String[] args) throws IOException{
      {
        readParallelMachine RT = new readParallelMachine();
        RT.setReadTxtData(".\\instances\\parallelMachine.txt");
        RT.ReadTxt();
        RT.SaveValueOfTxt(0);
        RT.output();
      }
    }
}