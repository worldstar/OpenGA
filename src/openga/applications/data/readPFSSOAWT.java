
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package openga.applications.data;

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
  private String[] STxt;
  private int piTotal;
  private int[] pi;
  private int piStart = 2;
  private int[] di;
  private int diStart = 3;
  private Double[] wi;
  private int wiStart = 4;
  private int[][] setup;
  private int setupStart = 302;

  public void setData(String fileName) {
    this.fileName = fileName;
    if (fileName == null) {
      System.out.println("Specify the file name please.");
      System.exit(1);
    }
  }

  private void readTxt() throws FileNotFoundException, IOException {
    FileReader fr = new FileReader(fileName);
    BufferedReader br = new BufferedReader(fr);
    String TxtAll = "", eachLine = "";
    while ((eachLine = br.readLine()) != null) {
      TxtAll += eachLine + "\n";
    }
    STxt = TxtAll.split("\t|\n");

    piTotal = Integer.parseInt(STxt[0]);
    pi = new int[piTotal];
    di = new int[piTotal];
    wi = new Double[piTotal];
    setup = new int[piTotal][Integer.parseInt(STxt[1])];
    for (int i = 0; i < piTotal; i++) {
      pi[i] = Integer.parseInt(STxt[piStart]);
      piStart += 3;
      di[i] = Integer.parseInt(STxt[diStart]);
      diStart += 3;
      wi[i] = Double.parseDouble(STxt[wiStart]);
      wiStart += 3;

      for (int j = 0; j < Integer.parseInt(STxt[1]); j++) {
        setup[i][j] = Integer.parseInt(STxt[setupStart]);
        setupStart += 1;
//                System.out.print(setup[i][j] + ",");
      }
//            System.out.println();

      System.out.println(pi[i] + "," + di[i] + "," + wi[i]);
    }

//        for(int i = 0 ;  i < STxt.length ; i ++) 
//        {
//            System.out.println(STxt[i]);
//        }
  }

  private int[] getPi() {
    return this.pi;
  }

  private int[] getDi() {
    return this.di;
  }

  private Double[] getWi() {
    return this.wi;
  }

  private int[][] getSetup() {
    return this.setup;
  }

  public static void main(String[] args) throws IOException {
    // TODO code application logic here
    readPFSSOAWT PF = new readPFSSOAWT();
    PF.setData("@../../instances/PFSS-OAWT-Data/p/p100x10_0.txt");
    PF.readTxt();

  }

}
