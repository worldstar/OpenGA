/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objfunctionpfssoawt;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;

/**
 *
 * @author Kuo Yu-Cheng
 */
public class ObjFunctionPFSSOAWT {

  /**
   * @param args the command line arguments
   */
  private String fileName;
  private String writeFileName;
  private String[] STxt;
  private int machineTotal;
  private int piTotal;
  private int[] pi;
  private int piStart = 2;
  private int[] di;
  private int diStart = 3;
  private Double[] wi;
  private int wiStart = 4;
  private int[][] processingTime;
  private int processingTimeStart;
  private int[] Sequence = new int[]{5,3,8,1,9,6,0,2};
  private int[][] completeTime;
  private int[] machineCompleteTime;
  private Double[] pal;
  private Double[] profit;
  

  public void setData(String fileName) {
    this.fileName = fileName;
    if (fileName == null) {
      System.out.println("Specify the file name please.");
      System.exit(1);
    }
  }
  public void setWriteData(String writeFileName) {
    this.writeFileName = writeFileName;
    if (writeFileName == null) {
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
    pal = new Double[piTotal];
    profit = new Double[piTotal];
    machineTotal = Integer.parseInt(STxt[1]);
    processingTime = new int[piTotal][machineTotal];
    processingTimeStart = (wiStart + 1) + 3 * (piTotal - 1);
    
    for (int i = 0; i < piTotal; i++) {
      pi[i] = Integer.parseInt(STxt[piStart]);
      piStart += 3;
      di[i] = Integer.parseInt(STxt[diStart]);
      diStart += 3;
      wi[i] = Double.parseDouble(STxt[wiStart]);
      wiStart += 3;

      for (int j = 0; j < Integer.parseInt(STxt[1]); j++) {
        processingTime[i][j] = Integer.parseInt(STxt[processingTimeStart]);
        processingTimeStart += 1;
//                System.out.print(processingTime[i][j] + ",");
      }
//            System.out.println();

//      System.out.println(pi[i] + "," + di[i] + "," + wi[i]);
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
    return this.processingTime;
  }
  
  private void Start()
  {
      completeTime = new int[Sequence.length][machineTotal];
      machineCompleteTime = new int[machineTotal];
      for(int i = 0 ; i < Sequence.length; i++)
      {
          for(int j = 0 ; j < machineTotal ; j++)
          {
              if(j>0)
              {
                  if(machineCompleteTime[j] == 0)
                  {
                    machineCompleteTime[j] += machineCompleteTime[j-1] +processingTime[Sequence[i]][j];
                  }else if(machineCompleteTime[j - 1] > machineCompleteTime[j])
                  {
                      machineCompleteTime[j] = machineCompleteTime[j - 1] + processingTime[Sequence[i]][j];
                  }
                  else
                  {
                    machineCompleteTime[j] += processingTime[Sequence[i]][j] ;
                  }
              }
              else
              {
              machineCompleteTime[j] += processingTime[Sequence[i]][j] ;
              }
              
                  completeTime[i][j] = machineCompleteTime[j];
              
//       System.out.println(completeTime[i][0]);
//              System.out.print(processingTime[Sequence[i]][j] + ",");
          }
          pal[i] = ((completeTime[i][machineTotal - 1] - di[Sequence[i]]) * wi[Sequence[i]]);
          
          if(pal[i] < 0)
          {
              pal[i] = 0.0;
          }
          
          profit[i] = pi[Sequence[i]] - pal[i];
//          System.out.println(profit[i]);
//          System.out.println();
      }
        
  }
private void WriteFile() throws IOException
{
     FileWriter sw = new FileWriter(writeFileName, false);
     DecimalFormat df = new DecimalFormat("#.00");
     sw.write("OrderID" + "\t" + "Pi" + "\t" + "di" + "\t" + "wi" + "\t" + "accepted" + "\t" + "devery" + "\t" + "delayed" + "\t" + "Profit\n");
     boolean accepted = false;
     int temp = 0;
     for(int i = 0 ; i < piTotal; i++)
     {
         for(int j = 0 ; j < Sequence.length; j++)
         {
            if( i == Sequence[j])
            {
                temp = j;
                accepted = true;
            } 
         }
         if(accepted)
            {
                accepted = false;
                if(completeTime[temp][machineTotal - 1] > di[i])
                {
                    sw.write(i + "\t" + pi[i] + "\t" + di[i] + "\t" + wi[i] + "\t" + "1" + "\t" + completeTime[temp][machineTotal - 1] + "\t" + "1" + "\t" + Double.parseDouble(df.format(profit[temp])) + "\n");
                }else
                {
                    sw.write(i + "\t" + pi[i] + "\t" + di[i] + "\t" + wi[i] + "\t" + "1" + "\t" + completeTime[temp][machineTotal - 1] + "\t" + "0" + "\t" + Double.parseDouble(df.format(profit[temp])) + "\n");
                }
                }else
         {
             sw.write(i + "\t" + pi[i] + "\t" + di[i] + "\t" + wi[i] + "\t" + "0" + "\t" + "\t" + "\t" + "\t" + "\t" + "\t" + "\n");
         }
     }
     sw.write("------Order Sequence(ID)-------------------------------\n");
     for(int i = 0 ; i < Sequence.length; i++)
     {
         for(int j = 0 ; j < machineTotal ; j++)
         {
             sw.write(Sequence[i] + "\t");
         }
         sw.write("\n");
     }
     sw.write("------Finished time(ID)-------------------------------\n");
     
     Double maxProfit = 0.0;
     for(int i = 0 ; i < Sequence.length; i++)
     {
         for(int j = 0 ; j < machineTotal ; j++)
         {
            sw.write("(" + Sequence[i] + ",\t" + processingTime[Sequence[i]][j] + ",\t" + completeTime[i][j] +  ")" + "\t");
         }
         sw.write("pi=\t" + pi[i] + "\tdi=\t" + di[i] + "\twi=\t" + wi[i] + "\tpal=\t" + Double.parseDouble(df.format(pal[i])) + "\tprofit=\t" + Double.parseDouble(df.format(profit[i])) );
         sw.write("\n");
         maxProfit += profit[i];
     }
     sw.write("maxProfit=\t" + maxProfit + "\n");
     sw.close();
}
  public static void main(String[] args) throws IOException {
    // TODO code application logic here
    ObjFunctionPFSSOAWT PF = new ObjFunctionPFSSOAWT();
    PF.setData("@../../File/p10x3_0.txt");
    PF.readTxt();
    PF.Start();
    PF.setWriteData("@../../File/o10x3_0.txt");
    PF.WriteFile();
  }

}