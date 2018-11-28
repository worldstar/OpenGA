/*
 * This program detects the system utilization rate of the experiment.
 * If the rate is lower than a specify rate (default is 1%, we shutdown the computer
 * to save the electricity bill or the fee on the cloud environment.
 */
package openga.util;

import java.io.IOException;
import com.sun.management.OperatingSystemMXBean;
import java.lang.management.ManagementFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author user2
 */
public class SystemUtilization {     
  public static void checkSystemStatus(double utilizationRate){
    int busyCounter = 0;
    int freeCounter = 0;
    OperatingSystemMXBean osBean = (com.sun.management.OperatingSystemMXBean)ManagementFactory.getOperatingSystemMXBean();
        
    try {
        while (true) {
            Thread.sleep(1000);

            double loading = osBean.getSystemCpuLoad();//getProcessCpuLoad()
            System.out.printf("Loading: %.4f \n", loading);

            if(loading == 1 || loading < 0 || loading == Double.NaN){
              Thread.sleep(2000);
              continue;
            }
            else if(loading > utilizationRate) { 
                busyCounter ++;

              if(busyCounter > 3){
    //            System.out.println("Busying");
                break;
              }            
            }
            else{
              freeCounter ++;

              if(freeCounter > 3){
                shutdownDirectly();
    //            System.out.println("Shutdown");
                break;
              }            
            }                            
        }
    } catch (InterruptedException ex) {
      Logger.getLogger(SystemUtilization.class.getName()).log(Level.SEVERE, null, ex);
    }
    
  }
  
  public static void checkSystemStatus(){
    checkSystemStatus(0.01);// 0.01 = 1% CPU Utilization
  }  
  
  public static void shutdownDirectly(){
    String command = "sudo shutdown -h now";//Linux command
    
    if(System.getProperty("os.name").startsWith("Windows")){
      command = "shutdown -s -t 0";
    }
    
    try {
      Process p = Runtime.getRuntime().exec(command);
      p.waitFor();    
    } catch (IOException ex) {
      Logger.getLogger(SystemUtilization.class.getName()).log(Level.SEVERE, null, ex);
    } catch (InterruptedException ex) {
      Logger.getLogger(SystemUtilization.class.getName()).log(Level.SEVERE, null, ex);
    }
  }    
  
    public void incrementCounter(String fileName, int maxCounter){

      int Counter = readCount(fileName);
      if(Counter == maxCounter){
          shutdownDirectly();
      }else{
          writeCount(fileName,Counter);
      }
  }

  public int readCount(String fileName){
      String Count = "";
      try {
          FileReader fr = new FileReader(fileName);
          BufferedReader br = new BufferedReader(fr);
          String eachLine = "";
          while ((eachLine = br.readLine()) != null) {
            Count += eachLine;
          }
          fr.close();
      } catch (IOException ex) {
          Count = "1";
      }
      return Integer.parseInt(Count);
  }

  public void writeCount(String fileName,int Counter){
      try {
          FileWriter fw = new FileWriter(fileName);
          Counter += 1;
          fw.write(String.valueOf(Counter));
          fw.close();
      } catch (IOException ex) {
          Logger.getLogger(SystemUtilization.class.getName()).log(Level.SEVERE, null, ex);
      }
  }
}
