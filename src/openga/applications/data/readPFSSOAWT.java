
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package openga.applications.data;

import openga.ObjectiveFunctions.*;
import openga.applications.data.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author Kuo Yu-Cheng
 */
public class readPFSSOAWT {

  /**
   * @param args the command line arguments
   */
  private String fileName;
  private String data;
  private String[] STxt;
  private int piTotal;
  private int machineTotal;
  private int[] fristProfit;
  private int piStart = 2;
  private int[] di;
  private int diStart = 3;
  private double[] wi;
  private int wiStart = 4;
  private int[][] processingTime;
  private int processingTimeStart;

  public void setData(String data,String fileName) {
    this.data = data;
    this.fileName = fileName;
    if (fileName == null) {
      System.out.println("Specify the file name please.");
      System.exit(1);
    }
  }

  public void readTxt() throws FileNotFoundException, IOException {
    FileReader fr = new FileReader(data + fileName);
    BufferedReader br = new BufferedReader(fr);
    String TxtAll = "", eachLine = "";
    while ((eachLine = br.readLine()) != null) {
      TxtAll += eachLine + "\n";
    }
    STxt = TxtAll.split("\t|\n");

    piTotal = Integer.parseInt(STxt[0]);
    machineTotal = Integer.parseInt(STxt[1]);
    fristProfit = new int[piTotal];
    di = new int[piTotal];
    wi = new double[piTotal];
    processingTime = new int[piTotal][Integer.parseInt(STxt[1])];
    processingTimeStart = (wiStart + 1) + 3 * (piTotal - 1);
    for (int i = 0; i < piTotal; i++) {
      fristProfit[i] = Integer.parseInt(STxt[piStart]);
      piStart += 3;
      di[i] = Integer.parseInt(STxt[diStart]);
      diStart += 3;
      wi[i] = Double.parseDouble(STxt[wiStart]);
      wiStart += 3;

      for (int j = 0; j < Integer.parseInt(STxt[1]); j++) {
        processingTime[i][j] = Integer.parseInt(STxt[processingTimeStart]);
        processingTimeStart += 1;
//                System.out.print(setup[i][j] + ",");
      }
//            System.out.println();

//      System.out.println(fristProfit[i] + "," + di[i] + "," + wi[i]);
    }

//        for(int i = 0 ;  i < STxt.length ; i ++) 
//        {
//            System.out.println(STxt[i]);
//        }
  }

  public int getPiTotal() {
    return this.piTotal;
  }
  
  public int getMachineTotal() {
    return this.machineTotal;
  }
  
  public int[] getPi() {
    return this.fristProfit;
  }

  public int[] getDi() {
    return this.di;
  }

  public double[] getWi() {
    return this.wi;
  }

  public int[][] getSetup() {
    return this.processingTime;
  }
  public String getFileName()
  {
    return this.fileName;
  }

  public static void main(String[] args) throws IOException {
    // TODO code application logic here
    readPFSSOAWT PF = new readPFSSOAWT();
    PF.setData("@../../instances/PFSS-OAWT-Data/p/","p10x3_0.txt");
    PF.readTxt();

  }

}
