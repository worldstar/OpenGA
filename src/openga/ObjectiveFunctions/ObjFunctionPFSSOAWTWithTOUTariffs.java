/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package openga.ObjectiveFunctions;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kuo Yu-Cheng
 */
public class ObjFunctionPFSSOAWTWithTOUTariffs extends ObjFunctionPFSSOAWT {

  private double on_peakPrice = 0.1327;
  private double mid_peakPrice = 0.0750;
  private double off_peakPrice = 0.0422;
  private int[] power = new int[]{20, 30, 15, 10, 15, 30, 10, 20, 10, 10};
  private int machine;
  private double startTime;
  private double endTime;
  private double midPeakStart;
  private double midPeakEnd;
  private double onPeakEnd;
  private double midPeakEnd2;
  private double TOUcostStartTime = 360.0;
  private double TOUcostStartTimeTemp;
  private double TOUcostProcessingTime;
  private double TOUcostCompleteTime;
  private double[][] TOUcost;
  private String[][] TOUcostStartTimeStr;
  private String[][] TOUcostCompleteTimeStr;
  private double totalCost;
  private double totalProfit;

  public void setData(int machine, String startTime, String endTime) throws ParseException {
    SimpleDateFormat simple = new SimpleDateFormat();
    simple.applyPattern("HH:mm");
    this.startTime = (double) simple.parse(startTime).getTime() / (1000 * 60);
    this.endTime = (double) simple.parse(endTime).getTime() / (1000 * 60);
    this.machine = machine;
  }

  public double calculateTOUCost() throws ParseException {
    SimpleDateFormat simple = new SimpleDateFormat();
    simple.applyPattern("HH:mm");
    double totalPrice = 0.0;
    double totalPeriod = endTime - startTime;
    midPeakStart = (double) simple.parse("07:00").getTime() / (1000 * 60);
    midPeakEnd = (double) simple.parse("15:00").getTime() / (1000 * 60);
    onPeakEnd = (double) simple.parse("20:00").getTime() / (1000 * 60);
    midPeakEnd2 = (double) simple.parse("22:00").getTime() / (1000 * 60);
    
    if (totalPeriod < 0) {
      System.out.println("endTime can not be greater than startTime!!");
      return -1;
    } else {
      if (startTime < midPeakStart)//Off_peak
      {
        if (endTime > midPeakStart) {
          totalPrice += ((((midPeakStart - startTime) / 60) * off_peakPrice) * (((midPeakStart - startTime)  / totalPeriod) * power[machine]));
          startTime = midPeakStart;
        } else {
          totalPrice += ((((endTime - startTime) / 60) * off_peakPrice) * (((endTime - startTime) / totalPeriod) * power[machine]));
        }
      }
      if (startTime >= midPeakStart && startTime < midPeakEnd)//Mid-peak 07:00 ~ 15:00
      {
        if (endTime > midPeakEnd) {
          totalPrice += ((((midPeakEnd - startTime) / 60) * mid_peakPrice) * (((midPeakEnd - startTime) / totalPeriod) * power[machine]));
          startTime = midPeakEnd;
        } else {
          totalPrice += ((((endTime - startTime) / 60) * mid_peakPrice) * (((endTime - startTime) / totalPeriod) * power[machine]));
        }
      }
      if (startTime >= midPeakEnd && startTime < onPeakEnd)//On-peak 15:00 ~ 20:00
      {
        if (endTime > onPeakEnd) {
          totalPrice += ((((onPeakEnd - startTime) / 60) * on_peakPrice) * (((onPeakEnd - startTime) / totalPeriod) * power[machine]));
          startTime = onPeakEnd;
        } else {
          totalPrice += ((((endTime - startTime) / 60) * on_peakPrice) * (((endTime - startTime) / totalPeriod) * power[machine]));
        }
      }
      if (startTime >= onPeakEnd && startTime < midPeakEnd2)//Mid-peak 20:00 ~ 22:00
      {
        if (endTime > midPeakEnd2) {
          totalPrice += ((((midPeakEnd2 - startTime) / 60) * mid_peakPrice) * (((midPeakEnd2 - startTime) / totalPeriod) * power[machine]));
          startTime = midPeakEnd2;
        } else {
          totalPrice += ((((endTime - startTime) / 60) * mid_peakPrice) * (((endTime - startTime) / totalPeriod) * power[machine]));
        }
      }
      if (startTime >= midPeakEnd2)//Off_peak
      {
        totalPrice += ((((endTime - startTime) / 60) * off_peakPrice) * (((endTime - startTime) / totalPeriod) * power[machine]));
      }
      return totalPrice;
    }
  }
  
  @Override
  public double evaluateAll(int[] Sequence) {
    
    
    this.Sequence = Sequence;

    totalProfit = 0.0;
    totalCost = 0.0;
    
    pal = new Double[piTotal];
    profit = new Double[piTotal];
    completeTime = new int[Sequence.length][machineTotal];
    accept = new boolean[Sequence.length];
    machineCompleteTime = new int[machineTotal];
    processingTimeStart = (wiStart + 1) + 3 * (piTotal - 1);
    
    TOUcostStartTimeStr = new String[Sequence.length][machineTotal];
    TOUcostCompleteTimeStr = new String[Sequence.length][machineTotal];
    TOUcost = new double[Sequence.length][machineTotal];
    
    
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

    calculateTOUMachineCost();
    
    return (totalProfit - totalCost);
  }
  
  public void calculateTOUMachineCost()
  {
    try {
        int dayTime = 1440 ;
        for (int i = 0; i < Sequence.length; i++) {
            for (int j = 0; j < machineTotal; j++) {
              double tempCompleteTime,tempStartTime;
              tempCompleteTime = 0.0;
              

              if(accept[i])
              {
                TOUcostProcessingTime = completeTime[i][j] - processingTime[Sequence[i]][j];
                TOUcostCompleteTime = completeTime[i][j] + TOUcostStartTime;
                TOUcostStartTimeTemp = (TOUcostStartTime + TOUcostProcessingTime);
                
                if(TOUcostCompleteTime > dayTime && TOUcostStartTimeTemp < dayTime)//如果結束 > 24 && 開始 < 24
                {
                    tempStartTime = TOUcostStartTimeTemp;
                    tempCompleteTime = TOUcostCompleteTime - dayTime + TOUcostStartTime;

                    TOUcostCompleteTime = dayTime;//開始 _ 換日
                    TOUcostStartTimeStr[i][j] = ((int)(TOUcostStartTimeTemp / 60) + ":" + ((int)(TOUcostStartTimeTemp % 60)));
                    TOUcostCompleteTimeStr[i][j] = (((int)(TOUcostCompleteTime / 60)) + ":" + ((int)(TOUcostCompleteTime % 60)));
                    setData(j, TOUcostStartTimeStr[i][j] , TOUcostCompleteTimeStr[i][j]);
                    TOUcost[i][j] = calculateTOUCost();

                    TOUcostStartTimeTemp = TOUcostStartTime;//換日 _ 結束
                    TOUcostCompleteTime = tempCompleteTime;
                    TOUcostStartTimeStr[i][j] = ((int)(TOUcostStartTimeTemp / 60) + ":" + ((int)(TOUcostStartTimeTemp % 60)));
                    TOUcostCompleteTimeStr[i][j] = (((int)(TOUcostCompleteTime / 60)) + ":" + ((int)(TOUcostCompleteTime % 60)));
                    TOUcost[i][j] += calculateTOUCost();
                    
                    TOUcostStartTimeTemp = tempStartTime;
                    TOUcostCompleteTime = tempCompleteTime;
                    TOUcostStartTimeStr[i][j] = ((int)(TOUcostStartTimeTemp / 60) + ":" + ((int)(TOUcostStartTimeTemp % 60)));
                    TOUcostCompleteTimeStr[i][j] = (((int)(TOUcostCompleteTime / 60)) + ":" + ((int)(TOUcostCompleteTime % 60)));

                  
                }else if(TOUcostCompleteTime > dayTime && TOUcostStartTimeTemp > dayTime)//如果結束 > 24 && 開始 > 24
                {
                  while(TOUcostCompleteTime > dayTime && TOUcostStartTimeTemp > dayTime)//則縮減到24小時內
                  {
                    TOUcostCompleteTime = (TOUcostCompleteTime - (dayTime - TOUcostStartTime));
                    TOUcostStartTimeTemp = (TOUcostStartTimeTemp - (dayTime - TOUcostStartTime));
                  }
                  
                  if(TOUcostCompleteTime > dayTime && TOUcostStartTimeTemp < dayTime)//如果結束 > 24 && 開始 < 24
                  {
                    tempStartTime = TOUcostStartTimeTemp;
                    tempCompleteTime = TOUcostCompleteTime - dayTime + TOUcostStartTime;
                  
                    TOUcostCompleteTime = dayTime;//開始 _ 換日
                    TOUcostStartTimeStr[i][j] = ((int)(TOUcostStartTimeTemp / 60) + ":" + ((int)(TOUcostStartTimeTemp % 60)));
                    TOUcostCompleteTimeStr[i][j] = (((int)(TOUcostCompleteTime / 60)) + ":" + ((int)(TOUcostCompleteTime % 60)));
                    setData(j, TOUcostStartTimeStr[i][j] , TOUcostCompleteTimeStr[i][j]);
                    TOUcost[i][j] = calculateTOUCost();

                    TOUcostStartTimeTemp = TOUcostStartTime;//換日 _ 結束
                    TOUcostCompleteTime = tempCompleteTime;
                    TOUcostStartTimeStr[i][j] = ((int)(TOUcostStartTimeTemp / 60) + ":" + ((int)(TOUcostStartTimeTemp % 60)));
                    TOUcostCompleteTimeStr[i][j] = (((int)(TOUcostCompleteTime / 60)) + ":" + ((int)(TOUcostCompleteTime % 60)));
                    TOUcost[i][j] += calculateTOUCost();
                    
                    TOUcostStartTimeTemp = tempStartTime;
                    TOUcostCompleteTime = tempCompleteTime;
                    TOUcostStartTimeStr[i][j] = ((int)(TOUcostStartTimeTemp / 60) + ":" + ((int)(TOUcostStartTimeTemp % 60)));
                    TOUcostCompleteTimeStr[i][j] = (((int)(TOUcostCompleteTime / 60)) + ":" + ((int)(TOUcostCompleteTime % 60)));
                    
                  }else
                  {
                    TOUcostStartTimeStr[i][j] = ((int)(TOUcostStartTimeTemp / 60) + ":" + ((int)(TOUcostStartTimeTemp % 60)));
                    TOUcostCompleteTimeStr[i][j] = (((int)(TOUcostCompleteTime / 60)) + ":" + ((int)(TOUcostCompleteTime % 60)));

                    setData(j, TOUcostStartTimeStr[i][j] , TOUcostCompleteTimeStr[i][j]);
                    TOUcost[i][j] = calculateTOUCost();
                    
                  }
                  
                }else
                {
                  TOUcostStartTimeStr[i][j] = ((int)(TOUcostStartTimeTemp / 60) + ":" + ((int)(TOUcostStartTimeTemp % 60)));
                  TOUcostCompleteTimeStr[i][j] = (((int)(TOUcostCompleteTime / 60)) + ":" + ((int)(TOUcostCompleteTime % 60)));

                  setData(j, TOUcostStartTimeStr[i][j] , TOUcostCompleteTimeStr[i][j]);
                  TOUcost[i][j] = calculateTOUCost();
                  
                }
                
                
                
                
//                System.out.println(" 物件 : " + Sequence[i] + " 機台 : " + j + " 開始時間 : " + TOUcostStartTimeStr[i][j] + " 結束時間 : " + TOUcostCompleteTimeStr[i][j] + " 耗費成本 : " + TOUcost[i][j]);
              }else
              {
                TOUcost[i][j] = 0;
                TOUcostStartTimeStr[i][j] = "0";
                TOUcostCompleteTimeStr[i][j] = "0";
//                System.out.println(" 物件 : " + Sequence[i] + " 機台 : " + j + " 開始時間 : " + TOUcostStartTimeStr[i][j] + " 結束時間 : " + TOUcostCompleteTimeStr[i][j] + " 耗費成本 : " + TOUcost[i][j]);
              }
              
              if(TOUcost[i][j] >= 0)
              {
              }else
              {
                TOUcost[i][j] = 0;
              }
              totalCost += TOUcost[i][j];
//              System.out.println(TOUcost[i][j]);

            }
//          System.out.println();
        }
        
    } catch (ParseException ex) {
              Logger.getLogger(ObjFunctionPFSSOAWTWithTOUTariffs.class.getName()).log(Level.SEVERE, null, ex);
            }
  }
  
  @Override
  public void output()
  {
    
    for (int i = 0; i < Sequence.length; i++) {
      for (int j = 0; j < machineTotal; j++) {
        
//        System.out.println(" 物件 : " + Sequence[i] + " 機台 : " + j + " 開始時間 : " + TOUcostStartTimeStr[i][j] + " 結束時間 : " + TOUcostCompleteTimeStr[i][j] + " 耗費成本 : " + TOUcost[i][j]);
            
      }
//      System.out.println();
    }
    System.out.println("總利潤 : " + totalProfit);
    System.out.println("總成本 : " + totalCost);
  }
  
  public static void main(String[] args) throws ParseException, IOException {
    // TODO code application logic here
    ObjFunctionPFSSOAWTWithTOUTariffs TouT = new ObjFunctionPFSSOAWTWithTOUTariffs();
    TouT.setData(9, "06:00", "9:00"); // 第X台機器 開始Y 結束Z
    System.out.println(TouT.calculateTOUCost());
    
  }
}
