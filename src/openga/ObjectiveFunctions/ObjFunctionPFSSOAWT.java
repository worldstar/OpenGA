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
import openga.applications.data.readPFSSOAWT;
import openga.chromosomes.populationI;
import openga.chromosomes.*;

/**
 *
 * @author Kuo Yu-Cheng
 */
public class ObjFunctionPFSSOAWT extends ObjectiveFunctionTSP implements ObjFunctionPFSSOAWTI {

  /**
   * @param args the command line arguments
   */
  populationI population;// Part-one chromosomes
  chromosome chromosome1 = new chromosome();
  int length, indexOfObjective;

  public static int piTotal;
  public static int machineTotal;
  public static int[] fristProfit;
  public static int[] di;
  public static double[] wi;
  public static int[][] processingTime;

  public String writeFileName;
  public String[] STxt;
  public int piStart = 2;
  public int diStart = 3;
  public int wiStart = 4;
  public int processingTimeStart;
  public int[] Sequence;
  public int[][] completeTime;
  public int[] machineCompleteTime;
  public Double[] pal;
  public Double[] profit;
  public boolean[] accept;

  @Override
  public void setData(populationI population, int indexOfObjective) {
    this.population = population;
    this.length = population.getLengthOfChromosome();
    this.indexOfObjective = indexOfObjective;
  }

  public void setWriteData(String writeFileName) {
    this.writeFileName = writeFileName;
    if (writeFileName == null) {
      System.out.println("Specify the file name please.");
      System.exit(1);
    }
  }

  public double evaluateAll(int[] Sequence) {
    
    this.Sequence = Sequence;

    Double totalProfit = 0.0;
    pal = new Double[piTotal];
    profit = new Double[piTotal];
    completeTime = new int[Sequence.length][machineTotal];
    accept = new boolean[Sequence.length];
    machineCompleteTime = new int[machineTotal];
    processingTimeStart = (wiStart + 1) + 3 * (piTotal - 1);
    
    
    for (int i = 0; i < Sequence.length; i++) {
      for (int j = 0; j < machineTotal; j++) {
        if (j > 0) {
          if (machineCompleteTime[j] == 0) {
            machineCompleteTime[j] += machineCompleteTime[j - 1] + processingTime[Sequence[i]][j];
          } else
          {
            machineCompleteTime[j] = Math.max(machineCompleteTime[j - 1], machineCompleteTime[j]) + processingTime[Sequence[i]][j];
          }
        } else {
          machineCompleteTime[j] += processingTime[Sequence[i]][j];
        }

        completeTime[i][j] = machineCompleteTime[j];

      }
      
      //pal
      pal[i] = ((completeTime[i][machineTotal - 1] - di[Sequence[i]]) * wi[Sequence[i]]);
      
      if (pal[i] < 0) {
        pal[i] = 0.0;
      }
      
      if(fristProfit[Sequence[i]] - pal[i] > 0)
      {
        profit[i] = fristProfit[Sequence[i]] - pal[i];  
        accept[i] = true;
      }else {
        
          profit[i] = 0.0;
          accept[i] = false;
          for (int j = 0; j < machineTotal; j++) {
            if (i > 0) {

              completeTime[i][j] = completeTime[i - 1][j];

            } else {
              completeTime[i][j] = 0;
            }
            machineCompleteTime[j] = completeTime[i][j];
          }
        
      }
      
    }
    
    
    for (int i = 0; i < Sequence.length; i++) {
      totalProfit += profit[i];
    }

    return totalProfit;
  }
  
@Override
  public void calcObjective() {
    double obj;
    double objectives[];

    for (int i = 0; i < population.getPopulationSize(); i++) {
//      int[] X = new int[]{5,3,8,1,9,4,6,0,2,7};
//      obj = evaluateAll(X);
      
      objectives = population.getObjectiveValues(i);
      obj = evaluateAll(population.getSingleChromosome(i).genes);
      objectives[indexOfObjective] = obj;
      population.setObjectiveValue(i, objectives);
    }
  }

  @Override
  public void output() {
    DecimalFormat df = new DecimalFormat("#.00");
    System.out.print("OrderID" + "\t" + "Pi" + "\t" + "di" + "\t" + "wi" + "\t" + "accepted" + "\t" + "devery" + "\t" + "delayed" + "\t" + "Profit\n");
    boolean accepted = false;
    int temp = 0;
    for (int i = 0; i < piTotal; i++) {
      for (int j = 0; j < Sequence.length; j++) {
        if (i == Sequence[j]) {
          temp = j;
          accepted = true;
        }
      }
      if (accepted) {
        accepted = false;
        if (completeTime[temp][machineTotal - 1] > di[i]) {
            if(profit[temp] == 0)
            {
              System.out.print(i + "\t" + fristProfit[i] + "\t" + di[i] + "\t" + wi[i] + "\t" + "0" + "\t" + completeTime[temp][machineTotal - 1] + "\t" + "1" + "\t" + Double.parseDouble(df.format(profit[temp])) + "\n");
            }else
            {
              System.out.print(i + "\t" + fristProfit[i] + "\t" + di[i] + "\t" + wi[i] + "\t" + "1" + "\t" + completeTime[temp][machineTotal - 1] + "\t" + "1" + "\t" + Double.parseDouble(df.format(profit[temp])) + "\n");
            }
          } else {
            if(profit[temp] == 0)
            {
             System.out.print(i + "\t" + fristProfit[i] + "\t" + di[i] + "\t" + wi[i] + "\t" + "0" + "\t" + completeTime[temp][machineTotal - 1] + "\t" + "0" + "\t" + Double.parseDouble(df.format(profit[temp])) + "\n");
            }else
            {
             System.out.print(i + "\t" + fristProfit[i] + "\t" + di[i] + "\t" + wi[i] + "\t" + "1" + "\t" + completeTime[temp][machineTotal - 1] + "\t" + "0" + "\t" + Double.parseDouble(df.format(profit[temp])) + "\n");
            }
         }
      } else {
        System.out.print(i + "\t" + fristProfit[i] + "\t" + di[i] + "\t" + wi[i] + "\t" + "0" + "\t" + "\t" + "\t" + "\t" + "\t" + "\t" + "\n");
      }
    }
    System.out.print("------Order Sequence(ID)-------------------------------\n");
    for (int i = 0; i < Sequence.length; i++) {
      for (int j = 0; j < machineTotal; j++) {
        System.out.print(Sequence[i] + "\t");
      }
      System.out.print("\n");
    }
    System.out.print("------Finished time(ID)-------------------------------\n");

    Double maxProfit = 0.0;
    for (int i = 0; i < Sequence.length; i++) {
      for (int j = 0; j < machineTotal; j++) {
        System.out.print("(" + Sequence[i] + ",\t" + processingTime[Sequence[i]][j] + ",\t" + completeTime[i][j] + ")" + "\t");
      }
      System.out.print("pi=\t" + fristProfit[i] + "\tdi=\t" + di[i] + "\twi=\t" + wi[i] + "\tpal=\t" + Double.parseDouble(df.format(pal[i])) + "\tprofit=\t" + Double.parseDouble(df.format(profit[i])));
      System.out.print("\n");
      maxProfit += profit[i];
    }
    System.out.print("maxProfit=\t" + maxProfit + "\n");
  }

  public void WriteFile() throws IOException {
    FileWriter sw = new FileWriter(writeFileName, false);
    DecimalFormat df = new DecimalFormat("#.00");
    sw.write("OrderID" + "\t" + "Pi" + "\t" + "di" + "\t" + "wi" + "\t" + "accepted" + "\t" + "devery" + "\t" + "delayed" + "\t" + "Profit\n");
    boolean accepted = false;
    int temp = 0;
    for (int i = 0; i < piTotal; i++) {
      for (int j = 0; j < Sequence.length; j++) {
        if (i == Sequence[j]) {
          temp = j;
          accepted = true;
        }
      }
      if (accepted) {
        accepted = false;
        if (completeTime[temp][machineTotal - 1] > di[i]) {
          sw.write(i + "\t" + fristProfit[i] + "\t" + di[i] + "\t" + wi[i] + "\t" + "1" + "\t" + completeTime[temp][machineTotal - 1] + "\t" + "1" + "\t" + Double.parseDouble(df.format(profit[temp])) + "\n");
        } else {
          sw.write(i + "\t" + fristProfit[i] + "\t" + di[i] + "\t" + wi[i] + "\t" + "1" + "\t" + completeTime[temp][machineTotal - 1] + "\t" + "0" + "\t" + Double.parseDouble(df.format(profit[temp])) + "\n");
        }
      } else {
        sw.write(i + "\t" + fristProfit[i] + "\t" + di[i] + "\t" + wi[i] + "\t" + "0" + "\t" + "\t" + "\t" + "\t" + "\t" + "\t" + "\n");
      }
    }
    sw.write("------Order Sequence(ID)-------------------------------\n");
    for (int i = 0; i < Sequence.length; i++) {
      for (int j = 0; j < machineTotal; j++) {
        sw.write(Sequence[i] + "\t");
      }
      sw.write("\n");
    }
    sw.write("------Finished time(ID)-------------------------------\n");

    Double maxProfit = 0.0;
    for (int i = 0; i < Sequence.length; i++) {
      for (int j = 0; j < machineTotal; j++) {
        sw.write("(" + Sequence[i] + ",\t" + processingTime[Sequence[i]][j] + ",\t" + completeTime[i][j] + ")" + "\t");
      }
      sw.write("pi=\t" + fristProfit[i] + "\tdi=\t" + di[i] + "\twi=\t" + wi[i] + "\tpal=\t" + Double.parseDouble(df.format(pal[i])) + "\tprofit=\t" + Double.parseDouble(df.format(profit[i])));
      sw.write("\n");
      maxProfit += profit[i];
    }
    sw.write("maxProfit=\t" + maxProfit + "\n");
    sw.close();
  }

//  public static void main(String[] args) throws IOException {
//    // TODO code application logic here
//    ObjFunctionPFSSOAWT PF = new ObjFunctionPFSSOAWT();
////    PF.setData("@../../instances/PFSS-OAWT-Data/p/p10x3_0.txt");
////    PF.readTxt();
//    readPFSSOAWT rP = new readPFSSOAWT();
//    rP.setData("@../../instances/PFSS-OAWT-Data/p/","p10x3_0.txt");
//    rP.readTxt();
//    PF.setOASData(rP.getPiTotal(), rP.getMachineTotal(), rP.getPi(), rP.getDi(), rP.getWi(), rP.getSetup());
//    PF.calcObjective();
////    PF.setWriteData("@../../File/o100x10_0.txt");
////    PF.WriteFile();
//    PF.output();
//  }

  public int getSequence(int index) {
    return Sequence[index];
  }

  public int getSequenceLength() {
    return Sequence.length;
  }

  public int getMachineTotal() {
    return machineTotal;
  }

  public int getPiTotal() {
    return piTotal;
  }
  
  public boolean[] getAccept() {
    return accept;
  }

  public int getFristProfit(int index) {
    return fristProfit[index];
  }

  public int getDi(int index) {
    return di[index];
  }

  public double getWi(int index) {
    return wi[index];
  }

  public int[][] getCompleteTime() {
    return completeTime;
  }

  public int getProcessingTime(int i, int j) {
    return processingTime[i][j];
  }

  public double getProfit(int index) {
    return profit[index];
  }
  

  @Override
  public void setOASData(int piTotal, int machineTotal, int[] fristProfit, int[] di, double[] wi, int[][] processingTime) {
    this.piTotal = piTotal;
    this.machineTotal = machineTotal;
    this.fristProfit = fristProfit;
    this.di = di;
    this.wi = wi;
    this.processingTime = processingTime;
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
