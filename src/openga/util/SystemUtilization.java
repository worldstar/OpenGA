/*
 * This program detects the system utilization rate of the experiment.
 * If the rate is lower than a specify rate (default is 1%, we shutdown the computer
 * to save the electricity bill or the fee on the cloud environment.
 */
package openga.util;

import java.io.IOException;
import com.sun.management.OperatingSystemMXBean;
import java.lang.management.ManagementFactory;
/**
 *
 * @author user2
 */
public class SystemUtilization {     
  public static void checkSystemStatus(double utilizationRate) throws InterruptedException, IOException{
    int busyCounter = 0;
    int freeCounter = 0;
    OperatingSystemMXBean osBean = (com.sun.management.OperatingSystemMXBean)ManagementFactory.getOperatingSystemMXBean();
        
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
  }
  
  public static void checkSystemStatus() throws InterruptedException, IOException{
    checkSystemStatus(0.01);// 0.01 = 1% CPU Utilization
  }  
  
  public static void shutdownDirectly() throws IOException, InterruptedException{
    String command = "sudo shutdown -h now";//Linux command
    
    if(System.getProperty("os.name").startsWith("Windows")){
      command = "shutdown -s -t 0";
    }
    
    Process p = Runtime.getRuntime().exec(command);
    p.waitFor();    
  }    
}
