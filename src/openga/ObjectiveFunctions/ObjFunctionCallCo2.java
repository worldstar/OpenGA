package openga.ObjectiveFunctions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Random;

import openga.ObjectiveFunctions.ObjFunctionCo2;

public class ObjFunctionCallCo2 {

  double[] Process;
  double[] Process2;
  int Power;
  int count = 0;
  double startTime;

  public ObjFunctionCallCo2() throws IOException, Exception {
    //設定開工時間
    SimpleDateFormat Time = new SimpleDateFormat("HH:mm");
    this.startTime = (double) Time.parse("02:00").getTime() / (1000 * 60);
    this.Power = 20;

    //讀取(練習檔) 位址
    FileReader fr = new FileReader("@..//..//p10x4_0.txt");
    BufferedReader br = new BufferedReader(fr);

    //讀入 (練習檔) 第一行 字串分割 10筆處理
    String temp = br.readLine(), str[];
    int size = Integer.parseInt(temp);
    this.Process = new double[size];
    this.Process2 = new double[size];
    //原序
//    System.out.print("Process原序：");
//    for (int i = 0; i < Process.length; i++) {
//      System.out.print(" " + i + " ");
//    }
//    System.out.println();

    //讀進(練習檔) 每台機器的利問與處理時間
//    System.out.print("Process功健：");
    while (br.ready()) {
      temp = br.readLine();
      str = temp.split("	");
      Process[count] = Double.parseDouble(str[0]);
//      System.out.print(str[0] + " ");
      Process2[count] = Process[count];
      count++;
    }//System.out.println();
    fr.close();
    br.close();

    //呼叫運算方法
//		new Co2(Process,Power,startTime);
    //Co2 call_co2 = new Co2(Process,Power,startTime);
//    System.out.println();

    //新序
    Random ran = new Random();       	//宣告亂數函數
    int ran_num;						//接收亂數產生的值
    int[] num1 = new int[size];			//陣列0~9筆(練習檔第一筆)
    for (int i = 0; i < size; i++) {
      num1[i] = i;
    }	//賦予第一個陣列[0~9]陣列0~9的數值
    int[] order = new int[size];		//宣告第二個用作記錄順序的陣列

//    System.out.print("Process新序：");
    for (int i = 0; i < order.length; i++) {	//共執行10次(筆)
      ran_num = ran.nextInt(size - i);	//亂數-i 是因為 假設一開始0~9(亂數範圍) 每取走1個值範圍就減少1筆(0~8)..(0~7)..類推
      order[i] = num1[ran_num];			//將num1[ran_num]內的值取走 給order[0]..1..2類推
      Process[i] = Process2[order[i]];
//      System.out.print(" " + order[i] + " "); //觀看順序			
      for (int j = ran_num; j < order.length - i - 1; j++) {
        num1[j] = num1[j + 1];		//每取走一筆 將後面每一筆往前挪1格
      }
    }
//    System.out.println();

    //新序對應功健
//    System.out.print("Process功健：");
//    for (int i = 0; i < order.length; i++) {
//      System.out.print((int) Process[i] + " ");
//    }
//    System.out.println();

    //呼叫運算方法
//		new Co2(Process,Power,startTime);
  }

  public double[] getProcess() {
    return Process;
  }

  public int getPower() {
    return Power;
  }

  public double getStartTime() {
    return startTime;
  }

}
