/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package openga.applications.data;

import java.io.*;
import java.util.*;

/**
 *
 * @author Guo Yu-Cheng
 */
public class generateParallelMachineOAS {

  private String type;
  private String fileName;
  private String message;
  private String writeFileName = "";
  private int numberOfMachines;
  private int numberOfJobs;
  private int processingTimLengthOfMachine;
  private int seed = 8;
  private static int[] processingTime;
  private static int[] releaseDate;
  private static int[] dueDate;
  private static int[] deadline;
  private static int[] profit;
  private static String[] result;

  public void setData(String type, String fileName) {
    this.type = type;
    this.fileName = fileName;

    if (fileName == null) {
      System.out.println("Specify the file name please.");
      System.exit(1);
    }
  }

  public void getDataFromFile() {
    try {
      //read file
      FileReader fr = new FileReader(fileName);
      BufferedReader br = new BufferedReader(fr);
      String TxtAll = "", eachLine = "";
      while ((eachLine = br.readLine()) != null) {
        TxtAll += eachLine + "\n";
      }

      //split allString
      String[] STxt = TxtAll.split("\n| ");

      int processingTimeCount = 3;
      numberOfMachines = Integer.parseInt(STxt[0]);
      processingTimLengthOfMachine = numberOfMachines + 1;
      numberOfJobs = Integer.parseInt(STxt[1]);
      processingTime = new int[numberOfJobs];

      for (int i = 0; i < numberOfJobs; i++) {//job
        processingTime[i] = Integer.parseInt(STxt[processingTimeCount]);
        processingTimeCount += processingTimLengthOfMachine;
      }

    } catch (Exception e) {
      e.printStackTrace();
      System.out.println(e.toString());
    }//end catch
  }

  public int getSize() {
    return numberOfJobs;
  }

  public void setDataOfWrite(String WriteFileName) {
    this.writeFileName = WriteFileName;

    this.writeFileName = WriteFileName;
    if (WriteFileName == null) {
      System.out.println("Specify the file name please.");
      System.exit(1);
    }
  }

  public void writeDataFromProgram() throws IOException {
    FileWriter sw;
    sw = new FileWriter(writeFileName, false);

    for (int i = 0; i < result.length; i++) {
      sw.write(result[i] + "\n");
    }

    sw.close();
  }

  public void setReleaseDate(int[] processingTime) {
    int total = 0;
    Random r = new Random(seed);
    this.releaseDate = new int[processingTime.length];

    for (int i = 0; i < releaseDate.length; i++) {
      total += processingTime[i];
    }

    for (int i = 0; i < releaseDate.length; i++) {
      this.releaseDate[i] = (int) ((total / processingTime.length) * r.nextDouble());
    }
  }

  public void setDueDate(int[] processingTime) {
    int total = 0;
    Random r = new Random(seed);
    dueDate = new int[processingTime.length];

    for (int i = 0; i < dueDate.length; i++) {
      total += processingTime[i];
    }

    for (int i = 0; i < dueDate.length; i++) {
      dueDate[i] = releaseDate[i] + processingTime[i] + (int) (total * r.nextDouble());
      if (dueDate[i] <= releaseDate[i] + processingTime[i]) {
        dueDate[i] = releaseDate[i] + processingTime[i] + 1;
      }
    }
  }

  public void setDeadline(int[] dueDate) {
    Random r = new Random(seed);
    deadline = new int[processingTime.length];

    for (int i = 0; i < deadline.length; i++) {
      deadline[i] = dueDate[i] + r.nextInt((numberOfJobs / 10) + 3);
      if (dueDate[i] >= deadline[i]) {
        deadline[i] = dueDate[i] + 1;
      }
    }

  }

  public void setProfit() {
    Random r = new Random(seed);
    this.profit = new int[processingTime.length];

    for (int i = 0; i < deadline.length; i++) {
      this.profit[i] = (int) (100 * r.nextDouble());
    }
  }

  public void setResult() {
    Random r = new Random(seed);
    this.result = new String[processingTime.length];

    for (int i = 0; i < result.length; i++) {
      this.result[i]
              = processingTime[i] + "\t"
              + releaseDate[i] + "\t"
              + dueDate[i] + "\t"
              + deadline[i] + "\t"
              + profit[i];
    }
  }

  public void setDataOfResult() {
    generateParallelMachineOAS rPMSD2 = new generateParallelMachineOAS();
    rPMSD2.setReleaseDate(processingTime);
    rPMSD2.setDueDate(processingTime);
    rPMSD2.setDeadline(dueDate);
    rPMSD2.setProfit();
    rPMSD2.setResult();
  }

  private void repeatedExecution(String readFilePath) throws IOException {
    File folder = new File(readFilePath);
    String[] fileList = folder.list();

    for (int i = 0; i < fileList.length; i++) {
      setData("dat", readFilePath + fileList[i]);
      getDataFromFile();
      setDataOfResult();
      setDataOfWrite(".\\instances\\ParallelMachineSetup\\Balanced\\File\\12Machines\\" + fileList[i].substring(0, fileList[i].length() - 4) + ".txt");
      writeDataFromProgram();
    }
  }

  public static void main(String[] args) throws IOException {
    int numberOfMachines[] = new int[]{2};//2, 6, 12
    int numberOfJobs[] = new int[]{20, 40, 60, 80, 100, 120};//20, 40, 60, 80, 100, 120
    int startInstanceID = 1;
    int endStartInstanceID = 15;
    for (int m = 0; m < numberOfMachines.length; m++) {
      for (int j = 0; j < numberOfJobs.length; j++) {
        for (int i = startInstanceID; i <= endStartInstanceID; i++) {
          generateParallelMachineOAS rPMSD2 = new generateParallelMachineOAS();
          String fileName = "instances\\ParallelMachineSetup\\Balanced\\"
                  + numberOfMachines[m] + "machines\\" + numberOfJobs[j] + "on"
                  + numberOfMachines[m] + "Rp50Rs50_" + i + ".dat";
          System.out.println(fileName);
          rPMSD2.setData("dat", fileName);
          rPMSD2.getDataFromFile();
          rPMSD2.setDataOfResult();
          fileName = "instances\\ParallelMachineSetup\\OAS\\"
                  + numberOfJobs[j] + "on"
                  + "Rp50Rs50_" + i + ".dat";
          rPMSD2.setDataOfWrite(fileName);
          rPMSD2.writeDataFromProgram();
        }
      }
    }
  }
}
