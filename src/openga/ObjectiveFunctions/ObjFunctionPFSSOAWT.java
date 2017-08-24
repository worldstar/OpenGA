/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package openga.ObjectiveFunctions;
import openga.applications.flowshopProblem.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import openga.chromosomes.chromosome;
import openga.chromosomes.populationI;

/**
 *
 * @author Kuo Yu-Cheng
 */
public class ObjFunctionPFSSOAWT implements ObjFunctionPFSSOAWTI{

  /**
   * @param args the command line arguments
   */
  populationI population;// Part-one chromosomes
  chromosome chromosome1;
  int length, indexOfObjective;
  
  private static int piTotal;
  private static int machineTotal;
  private static int[] fristProfit;
  private static int[] di;
  private static Double[] wi;
  private static int[][] processingTime;
  
  private String fileName;
  private String writeFileName;
  private String[] STxt;
  private int piStart = 2;
  private int diStart = 3;
  private int wiStart = 4;
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

//  private void readTxt() throws FileNotFoundException, IOException {
//    FileReader fr = new FileReader(fileName);
//    BufferedReader br = new BufferedReader(fr);
//    String TxtAll = "", eachLine = "";
//    while ((eachLine = br.readLine()) != null) {
//      TxtAll += eachLine + "\n";
//    }
//    STxt = TxtAll.split("\t|\n");
//
//    piTotal = Integer.parseInt(STxt[0]);
//    fristProfit = new int[piTotal];
//    di = new int[piTotal];
//    wi = new Double[piTotal];
//    pal = new Double[piTotal];
//    profit = new Double[piTotal];
//    machineTotal = Integer.parseInt(STxt[1]);
//    processingTime = new int[piTotal][machineTotal];
//    processingTimeStart = (wiStart + 1) + 3 * (piTotal - 1);
//    
//    for (int i = 0; i < piTotal; i++) {
//      fristProfit[i] = Integer.parseInt(STxt[piStart]);
//      piStart += 3;
//      di[i] = Integer.parseInt(STxt[diStart]);
//      diStart += 3;
//      wi[i] = Double.parseDouble(STxt[wiStart]);
//      wiStart += 3;
//
//      for (int j = 0; j < Integer.parseInt(STxt[1]); j++) {
//        processingTime[i][j] = Integer.parseInt(STxt[processingTimeStart]);
//        processingTimeStart += 1;
////                System.out.print(processingTime[i][j] + ",");
//      }
////            System.out.println();
//
////      System.out.println(pi[i] + "," + di[i] + "," + wi[i]);
//    }
//
////        for(int i = 0 ;  i < STxt.length ; i ++) 
////        {
////            System.out.println(STxt[i]);
////        }
//  }

  public void calcObjective()
  {
      pal = new Double[piTotal];
      profit = new Double[piTotal];
      processingTimeStart = (wiStart + 1) + 3 * (piTotal - 1);
    
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
          
          profit[i] = fristProfit[Sequence[i]] - pal[i];
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
                    sw.write(i + "\t" + fristProfit[i] + "\t" + di[i] + "\t" + wi[i] + "\t" + "1" + "\t" + completeTime[temp][machineTotal - 1] + "\t" + "1" + "\t" + Double.parseDouble(df.format(profit[temp])) + "\n");
                }else
                {
                    sw.write(i + "\t" + fristProfit[i] + "\t" + di[i] + "\t" + wi[i] + "\t" + "1" + "\t" + completeTime[temp][machineTotal - 1] + "\t" + "0" + "\t" + Double.parseDouble(df.format(profit[temp])) + "\n");
                }
                }else
         {
             sw.write(i + "\t" + fristProfit[i] + "\t" + di[i] + "\t" + wi[i] + "\t" + "0" + "\t" + "\t" + "\t" + "\t" + "\t" + "\t" + "\n");
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
         sw.write("pi=\t" + fristProfit[i] + "\tdi=\t" + di[i] + "\twi=\t" + wi[i] + "\tpal=\t" + Double.parseDouble(df.format(pal[i])) + "\tprofit=\t" + Double.parseDouble(df.format(profit[i])) );
         sw.write("\n");
         maxProfit += profit[i];
     }
     sw.write("maxProfit=\t" + maxProfit + "\n");
     sw.close();
}
  public static void main(String[] args) throws IOException {
    // TODO code application logic here
    ObjFunctionPFSSOAWT PF = new ObjFunctionPFSSOAWT();
//    PF.setData("@../../instances/PFSS-OAWT-Data/p/p10x3_0.txt");
//    PF.readTxt();
    readPFSSOAWT rP = new readPFSSOAWT();
    rP.setData("@../../instances/PFSS-OAWT-Data/p/p10x3_0.txt");
    rP.readTxt();
    
    piTotal = rP.getPiTotal();
    machineTotal = rP.getMachineTotal();
    fristProfit = rP.getPi();
    di = rP.getDi();
    wi = rP.getWi();
    processingTime = rP.getSetup();
    
    PF.calcObjective();
    PF.setWriteData("@../../File/o100x10_0.txt");
    PF.WriteFile();
  }

  @Override
  public void setOASData(int[] fristProfit , Double[] wi , Double[] pal , Double[] profit) {
    this.fristProfit = fristProfit;
    this.wi = wi;
    this.pal = pal;
    this.profit = profit;
  }

  @Override
  public void setScheduleData(int[][] processingTime, int numberOfMachine) {
    this.processingTime = processingTime;
    this.machineTotal = numberOfMachine;
    }

  @Override
  public void setScheduleData(int[] dueDay, int[][] processingTime, int numberOfMachine) {
    this.di = dueDay;
    this.processingTime = processingTime;
    this.machineTotal = numberOfMachine;
  }

  @Override
  public void setData(populationI population, int indexOfObjective) {
    this.population = population;
    this.length = population.getLengthOfChromosome();
    this.indexOfObjective = indexOfObjective;
  }

  @Override
  public void setData(chromosome chromosome1, int indexOfObjective) {
    this.chromosome1 = chromosome1;
    this.indexOfObjective = indexOfObjective;
    length = chromosome1.getLength();
  }
  
  
  @Override
  public populationI getPopulation() {
    return population;
  }

  @Override
  public double[] getObjectiveValues(int index) {
    return population.getObjectiveValues(index);
  }  

}