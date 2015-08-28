package openga.applications.data;
import openga.util.fileWrite1;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: It generates the larger size instances for single machine scheduling problem 
 * with setup considerations.</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class createSingleMachineSetupData {
  public createSingleMachineSetupData() {
  }
  int jobSize[] = new int[]{50, 100, 150, 200};
  int replications = 15;

  int getLowRange(){//Unif[10,60]
    int val = 10 + (int)(60*Math.random());
    return val;
  }

  int getMediumRange(){//Unif[10,110]
    int val = 10 + (int)(110*Math.random());
    return val;
  }

  int getHighRange(){//Unif[10,160]
    int val = 10 + (int)(160*Math.random());
    return val;
  }

  /**
   * Write the data into text file.
   */
  void writeFile(String fileName, String _result){
    fileWrite1 writeLotteryResult = new fileWrite1();
    writeLotteryResult.writeToFile(_result,fileName);
    Thread thread1 = new Thread(writeLotteryResult);
    thread1.run();
  }

  void oneDataSet(String type, int _jobSize, int index){
    String fileName = "instances\\SingleMachineSetup\\"+type+"\\"+_jobSize+"_"+index+".etp";
    String result = _jobSize+"\n";
    for(int i = 0 ; i < _jobSize ; i ++ ){
      for(int j = 0 ; j < _jobSize ; j ++ ){
         if(i == j){
            result += 1000;
         }
         else{
           if(type == "low"){
             result += getLowRange();
           }
           else if(type == "med"){
             result += getMediumRange();
           }
           else if(type == "high"){
             result += getHighRange();
           }
         }//end if
         result += " ";
      }//end for
      result += "\n";
    }//end for
    writeFile(fileName, result);
  }

  void startGenerate(){
    String types[] = new String[]{"low", "med", "high"};//"low", "med", "high"
    for(int i = 0 ; i < jobSize.length ; i ++ ){
      for(int j = 0 ; j < types.length ; j ++ ){
        for(int k = 1 ; k <= replications ; k ++ ){
          System.out.println(types[j]+" "+jobSize[i]+" "+k);
          oneDataSet(types[j], jobSize[i], k);
        }
      }
    }//end for
  }

  public static void main(String[] args) {
    createSingleMachineSetupData createSingleMachineSetupData1 = new createSingleMachineSetupData();
    createSingleMachineSetupData1.startGenerate();
  }

}