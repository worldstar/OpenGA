
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

public class readPFSSOAWT_flowshop {

  /**
   * @param args the command line arguments
   */
  private String data;
  private String fileName;
  private String[] STXT;    //processingTime
  private String[] STXT2;   //numberofOrder,numberOfMachines
  int p = 2;  //0
  int d = 3;  //1
  int w = 4;  //2

  /*variable*/
  private int piTotal;
  private int machineTotal;
  private int[] fristProfit;    //  revenue of order
  private int[] di;        //  due-date
  private double[] wi;     //  tardiness penalty weight
  private int[][] processingTime;
  private int processingTimeStart;
  public double b;
  
  public void setData(String data, String fileName) {
    this.data = data;
    this.fileName = fileName;
    if (fileName == null) {
      System.out.println("Specify the file name please.");
      System.exit(1);
    }
  }

  public void readfile() throws FileNotFoundException, IOException {
    FileReader fr = new FileReader(data + fileName);
    BufferedReader br = new BufferedReader(fr);
    String message = "", eachLine = "";
//    String Txt = br.readLine();
    while ((eachLine = br.readLine()) != null) {
      message += eachLine + "\n";
    }
    STXT = message.split("\t|\n");
//    STXT2 = Txt.split("\t|\n");

//    for (int i = 0; i < STXT.length ; i++) {
//    System.out.println(STXT[i]); 
//    }
    piTotal = Integer.parseInt(STXT[0]);
    machineTotal = Integer.parseInt(STXT[1]);
    fristProfit = new int[piTotal];
    di = new int[piTotal];
    wi = new double[piTotal];
    processingTime = new int[piTotal][machineTotal];


    processingTimeStart = (w + 1) + 3 * (piTotal - 1);
//    System.out.println(processingTimeStart);

    for (int i = 0; i < piTotal; i++) {
      fristProfit[i] = Integer.parseInt(STXT[p]);
      p += 3;
      di[i] = Integer.parseInt(STXT[d]);
      d += 3;
      wi[i] = Double.parseDouble(STXT[w]);
      w += 3;
      
      for (int j = 0; j < machineTotal; j++) {
        processingTime[i][j] = Integer.parseInt(STXT[processingTimeStart]);
        processingTimeStart += 1;
//        System.out.print(processingTime[i][j] + " ");
      }
//      System.out.println();
    }
  }

  public int getPiTotal() {
    return this.piTotal;
  }

  public int getMachineTotal() {
    return this.machineTotal;
  }

  public int[] getprofit() {
    return this.fristProfit;
  }

  public int[] getdi() {
    return this.di;
  }

  public double[] getwi() {
    return this.wi;
  }

  public int[][] getprocessingTime() {
    return this.processingTime;
  }

  public String getfileName() {
    return this.fileName;
  }
  
  public double getb(){
    return this.b;
  }

  public static void main(String[] args) throws IOException {
    readPFSSOAWT_flowshop PF = new readPFSSOAWT_flowshop();
//    PF.setData("C:\\Github\\worldstar\\OpenGA\\OpenGA\\instances\\PFSS-OAWT-Data\\p\\","p10x3_0.txt");
    PF.setData("C:\\Github\\worldstar\\OpenGA\\OpenGA\\instances\\PFSS-OAWT-Data\\n\\","p5x3_0.txt");
    PF.readfile();
  }
}
