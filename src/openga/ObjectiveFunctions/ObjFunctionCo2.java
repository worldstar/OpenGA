package openga.ObjectiveFunctions;

import openga.applications.data.OASInstancesWithTOU;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import openga.chromosomes.*;


public final class ObjFunctionCo2 extends TPObjectiveFunctionMTSP implements ObjectiveFunctionOASWithTOUI{// 有更改***************************************************

  chromosome chromosome1 = new chromosome();
  
  private double startTime;
  private double[] Process;
  private double[] Power;
  
  public static void main(String[] args) throws IOException, Exception {
    ObjFunctionCo2 a = new ObjFunctionCo2();//這份文件
    OASInstancesWithTOU OAS = new OASInstancesWithTOU();//讀檔那份文件
//    OAS.setData(".\\instances\\SingleMachineOASWithTOU\\10orders\\Tao5\\R7\\Dataslack_10orders_Tao5R7_10.txt",10);
//    OAS.getDataFromFile();
//    a.Power = OAS.getPower();
//    a.Process = OAS.getP();
    a.setPowerData(OAS.getP(),OAS.getPower());
//    System.out.println(a.evaluateAll());
  }
    
  public List<Integer> chromosometoList(chromosome _chromosome1) {
    List<Integer> soln = new ArrayList<Integer>();
    for (int i = 0; i < _chromosome1.genes.length; i++) {
      soln.add(_chromosome1.genes[i]);
    }
    return soln;
  }
  
  public double evaluateAllCo2Cost(int[] sequence) throws ParseException
  {
        SimpleDateFormat Time = new SimpleDateFormat("HH:mm");
        startTime = (double) Time.parse("02:00").getTime() / (1000 * 60);

        double Co2kg = 0, Co2kw=0,totalCo2kWh=0; 

        double h3 = Time.parse("03:00").getTime()/(1000*60),h6 = Time.parse("06:00").getTime()/(1000*60),h12 = Time.parse("12:00").getTime()/(1000*60);
        double h14 = Time.parse("14:00").getTime()/(1000*60),h17 = Time.parse("17:00").getTime()/(1000*60),h18 = Time.parse("18:00").getTime()/(1000*60);
        double h21 = Time.parse("21:00").getTime()/(1000*60),h23 = Time.parse("23:00").getTime()/(1000*60),h24 = Time.parse("24:00").getTime()/(1000*60);
        //   0~3   3~6  6~12 12~14 14~17
        // 23~24 21~23 18~21 17~18
        // 0.725 0.700 0.693 0.682 0.669
        for (int i=0;i<sequence.length;i++) {
                Co2kg=0;Co2kw=0;
//			System.out.print("Start["+i+"] :"+Time.format(startTime*60000)+"   ");
                double endTime = startTime + Process[sequence[i]]; 
//                        System.out.print((endTime-startTime)+"   "+Time.format(endTime*60000)+"     co2kwh: ");
                        if (startTime < h3) { // 00:00~03:00  0.725 
                                if (endTime > h3) {
                                        Co2kg = (h3-startTime)/60*0.725; 
                                        Co2kw = (h3-startTime)/Process[sequence[i]]*Power[sequence[i]]; 
                                        totalCo2kWh += Co2kg*Co2kw;
                                        startTime = h3;
                                }else {
                                        Co2kg = (endTime-startTime)/60*0.725; 
                                        Co2kw = (endTime-startTime)/Process[sequence[i]]*Power[sequence[i]]; 
                                        totalCo2kWh += Co2kg*Co2kw;
                                }
                        }
                        if (startTime >=h3 && startTime < h6) { // 03:00~06:00  0.700
                                if (endTime > h6) {
                                        Co2kg = (h6-startTime)/60*0.700;	
                                        Co2kw = ((h6-startTime)/Process[sequence[i]])*Power[sequence[i]]; 
                                        totalCo2kWh += Co2kg*Co2kw;
//						System.out.print("..."+Co2kw*Co2kg+"...");

                                        startTime = h6;
                                }else {
                                        Co2kg += (endTime -startTime)/60*0.700; 
                                        Co2kw += (endTime -startTime)/Process[sequence[i]]*Power[sequence[i]]; 
                                        totalCo2kWh += Co2kg*Co2kw;
                                }
                        }
                        if (startTime >=h6 && startTime < h12) { // 06:00~12:00 0.693
                                if (endTime > h12) {
                                        Co2kg = (h12-startTime)/60*0.693; 
                                        Co2kw = (h12-startTime)/Process[sequence[i]]*Power[sequence[i]]; 
                                        totalCo2kWh += Co2kg*Co2kw;
                                        startTime = h12;
                                }else {
                                        Co2kg += (endTime-startTime)/60*0.693; 
                                        Co2kw += (endTime-startTime)/Process[sequence[i]]*Power[sequence[i]]; 
//						System.out.print("..."+(endTime - startTime)/60*0.693 * (endTime - startTime)/Process[i]*20+"...");
                                        totalCo2kWh += Co2kg*Co2kw;
                                }
                        }
                        if (startTime >=h12 && startTime <h14) { // 12:00~14:00 0.682
                                if (endTime > h14) {
                                        Co2kg = (h14-startTime)/60*0.682; 
                                        Co2kw = (h14-startTime)/Process[sequence[i]]*Power[sequence[i]]; 
                                        totalCo2kWh += Co2kg*Co2kw;
                                        startTime = h14;
                                }else {
                                        Co2kg += (endTime-startTime)/60*0.682; 
                                        Co2kw += (endTime-startTime)/Process[sequence[i]]*Power[sequence[i]]; 
                                        totalCo2kWh += Co2kg*Co2kw;
                                }
                        }
                        if (startTime >=h14 && startTime <h17) { // 14:00~17:00 0.669
                                if (endTime > h17) {
                                        Co2kg = (h17-startTime)/60*0.669; 
                                        Co2kw = (h17-startTime)/Process[sequence[i]]*Power[sequence[i]]; 
                                        totalCo2kWh += Co2kg*Co2kw;
                                        startTime = h17;
                                }else {
                                        Co2kg += (endTime-startTime)/60*0.669; 
                                        Co2kw += (endTime-startTime)/Process[sequence[i]]*Power[sequence[i]]; 
                                        totalCo2kWh += Co2kg*Co2kw;
                                }
                        }
                        if (startTime >=h17 && startTime <h18) { // 17:00~18:00 0.682
                                if (endTime > h18) {
                                        Co2kg = (h18-startTime)/60*0.682; 
                                        Co2kw = (h18-startTime)/Process[sequence[i]]*Power[sequence[i]]; 
                                        totalCo2kWh += Co2kg*Co2kw;
                                        startTime = h18;
                                }else {
                                        Co2kg += (endTime-startTime)/60*0.682; 
                                        Co2kw += (endTime-startTime)/Process[sequence[i]]*Power[sequence[i]]; 
                                        totalCo2kWh += Co2kg*Co2kw;
                                }
                        }
                        if (startTime >=h18 && startTime <h21) { // 18:00~21:00 0.693
                                if (endTime > h21) {
                                        Co2kg = (h21-startTime)/60*0.693; 
                                        Co2kw = (h21-startTime)/Process[sequence[i]]*Power[sequence[i]];
                                        totalCo2kWh += Co2kg*Co2kw;
                                        startTime = h21;
                                }else {
                                        Co2kg += (endTime-startTime)/60*0.693; 
                                        Co2kw += (endTime-startTime)/Process[sequence[i]]*Power[sequence[i]]; 
                                        totalCo2kWh += Co2kg*Co2kw;
                                }
                        }
                        if (startTime >=h21 && startTime <h23) { // 21:00~23:00 0.700
                                if (endTime > h23) {
                                        Co2kg = (h23-startTime)/60*0.700; 
                                        Co2kw = (h23-startTime)/Process[sequence[i]]*Power[sequence[i]]; 
                                        totalCo2kWh += Co2kg*Co2kw;
                                        startTime = h23;
                                }else {
                                        Co2kg += (endTime-startTime)/60*0.700; 
                                        Co2kw += (endTime-startTime)/Process[sequence[i]]*Power[sequence[i]]; 
                                        totalCo2kWh += Co2kg*Co2kw;
                                }
                        }
                        if (startTime >=h23 && startTime <h24) { // 23:00~24:00 0.725
                                if (endTime >=h24) {
                                        Co2kg = (h24-startTime)/60*0.725; 
                                        Co2kw = (h24-startTime)/Process[sequence[i]]*Power[sequence[i]]; 
                                        totalCo2kWh += Co2kg*Co2kw;
                                        startTime = h24; //結束時間大於24:00.getTime值
                                }else {
                                        Co2kg += (endTime-startTime)/60*0.725;	
                                        Co2kw += (endTime-startTime)/Process[sequence[i]]*Power[sequence[i]];
                                        totalCo2kWh += Co2kg*Co2kw;
                                }
                        }

                        if (startTime == h24) {  // 開始換日 
                                startTime-=1320;     // startTime = 02:00.getTime值
                                endTime-=1320;       // endTime   = 02:xx.getTime值
                                        if(startTime < h3)
                                                if (endTime>h3) {// 結束時間>3點,則先計算(3點-開工時間)的值
                                                        Co2kg += (h3-startTime)/60*0.725; 
                                                        Co2kw += (h3-startTime)/Process[sequence[i]]*Power[sequence[i]]; 
                                                        startTime = h3;
                                                        totalCo2kWh += Co2kg*Co2kw;
                                                }else {//若無>3點,則表示結束時間在隔日開工時間的第一個區段內
                                                        Co2kg += (endTime-startTime)/60*0.725; 
                                                        Co2kw += (endTime-startTime)/Process[sequence[i]]*Power[sequence[i]]; 
                                                        totalCo2kWh += Co2kg*Co2kw;
                                                }
                                        if(startTime >=h3 && startTime <h6) {
                                                if (endTime>h6) { // 結束時間>6點,則先計算(6點-第二區段開始)的值
                                                        Co2kg += (h6-startTime)/60*0.700; 
                                                        Co2kw += (h6-startTime)/Process[sequence[i]]*Power[sequence[i]]; 
                                                        startTime = h6;
                                                        totalCo2kWh += Co2kg*Co2kw;
                                                }else {//若無>6點,則表示結束時間在隔日開工時間第一時間區段+上第二個區段內時間
                                                        Co2kg += (endTime-startTime)/60*0.700; 
                                                        Co2kw += (endTime-startTime)/Process[sequence[i]]*Power[sequence[i]]; 
                                                        totalCo2kWh += Co2kg*Co2kw;
                                                }
                                        }
                                        if(startTime >=h6 && startTime <h12){
                                                if (endTime>h12) {// 結束時間>12點,則先計算(12點-第三區段開始)的值
                                                        Co2kg += (h12-startTime)/60*0.693; 
                                                        Co2kw += (h12-startTime)/Process[sequence[i]]*Power[sequence[i]]; 
                                                        startTime = h12;//這邊的if判斷 先暫時寫三個時間區段
                                                        totalCo2kWh += Co2kg*Co2kw;
                                                }else {//若無>12點,則表示結束時間在隔日開工時間至第一、二時間區段+上第三個區段內時間
                                                        Co2kg += (endTime-startTime)/60*0.693; 
                                                        Co2kw += (endTime-startTime)/Process[sequence[i]]*Power[sequence[i]]; 
                                                        totalCo2kWh += Co2kg*Co2kw;
                                                }
                                        }	
                                        if (startTime >=h12 && startTime <h14) {
                                                if (endTime>h14) {// 結束時間>14點,則先計算(14點-第四區段開始)的值
                                                        Co2kg += (h14-startTime)/60*0.682; 
                                                        Co2kw += (h14-startTime)/Process[sequence[i]]*Power[sequence[i]]; 
                                                        startTime = h14;//這邊的if判斷 先暫時寫三個時間區段
                                                        totalCo2kWh += Co2kg*Co2kw;
                                                }else {//若無>14點,則表示結束時間在隔日開工時間至第一、二、三時間區段+上第四個區段內時間
                                                        Co2kg += (endTime-startTime)/60*0.682; 
                                                        Co2kw += (endTime-startTime)/Process[sequence[i]]*Power[sequence[i]]; 
                                                        totalCo2kWh += Co2kg*Co2kw;
                                                }
                                        }	
                                        if(startTime >=h14 && startTime <h17) {
                                                if (endTime>h17) {// 結束時間>17點,則先計算(17點-第五區段開始)的值
                                                        Co2kg += (h17-startTime)/60*0.669; 
                                                        Co2kw += (h17-startTime)/Process[sequence[i]]*Power[sequence[i]];
                                                        startTime = h17;//這邊的if判斷 先暫時寫三個時間區段
                                                        totalCo2kWh += Co2kg*Co2kw;
                                                }else {//若無>18點,則表示結束時間在隔日開工時間至第一、二、三、四、五時間區段+上第五個區段內時間
                                                        Co2kg += (endTime-startTime)/60*0.669; 
                                                        Co2kw += (endTime-startTime)/Process[sequence[i]]*Power[sequence[i]]; 
                                                        totalCo2kWh += Co2kg*Co2kw;
                                                }
                                        }
                                        if (startTime >=h17 && startTime <h18) {
                                                if (endTime>h18) {// 結束時間>18點,則先計算(18點-第六區段開始)的值
                                                        Co2kg += (h18-startTime)/60*0.682; 
                                                        Co2kw += (h18-startTime)/Process[sequence[i]]*Power[sequence[i]]; 
                                                        startTime = h18;//這邊的if判斷 先暫時寫三個時間區段
                                                        totalCo2kWh += Co2kg*Co2kw;
                                                }else {//若無>18點,則表示結束時間在隔日開工時間至第一、二、三、四、五時間區段+上第六個區段內時間
                                                        Co2kg += (endTime-startTime)/60*0.682; 
                                                        Co2kw += (endTime-startTime)/Process[sequence[i]]*Power[sequence[i]]; 
                                                        totalCo2kWh += Co2kg*Co2kw;
                                                }
                                        }	
                                        if (startTime >=h18 && startTime <h21) {
                                                if (endTime>h21) {// 結束時間>21點,則先計算(21點-第七區段開始)的值
                                                        Co2kg += (h21-startTime)/60*0.693; 
                                                        Co2kw += (h21-startTime)/Process[sequence[i]]*Power[sequence[i]]; 
                                                        startTime = h21;//這邊的if判斷 先暫時寫三個時間區段
                                                        totalCo2kWh += Co2kg*Co2kw;
                                                }else {//若無>12點,則表示結束時間在隔日開工時間至第一、二、三、四、五、六時間區段+上第七個區段內時間
                                                        Co2kg += (endTime-startTime)/60*0.693; 
                                                        Co2kw += (endTime-startTime)/Process[sequence[i]]*Power[sequence[i]]; 
                                                        totalCo2kWh += Co2kg*Co2kw;
                                                }
                                        }
                                        if (startTime >=h21 && startTime <h23) {
                                                if (endTime>h23) {// 結束時間>23點,則先計算(23點-第八區段開始)的值
                                                        Co2kg += (h23-startTime)/60*0.700; 
                                                        Co2kw += (h23-startTime)/Process[sequence[i]]*Power[sequence[i]]; 
                                                        startTime = h23;//這邊的if判斷 先暫時寫三個時間區段
                                                        totalCo2kWh += Co2kg*Co2kw;
                                                }else {//若無>23點,則表示結束時間在隔日開工時間至第一、二、三、四、五、六、七時間區段+上第八個區段內時間
                                                        Co2kg += (endTime-startTime)/60*0.700; 
                                                        Co2kw += (endTime-startTime)/Process[sequence[i]]*Power[sequence[i]]; 
                                                        totalCo2kWh += Co2kg*Co2kw;
                                                }
                                        }
                                        if (startTime >=h23 && startTime <h24) {
                                                if (endTime>=h24) {
                                                        Co2kg += (h24-startTime)/60*0.725; 
                                                        Co2kw += (h24-startTime)/Process[sequence[i]]*Power[sequence[i]];
                                                        startTime = h24;
                                                        totalCo2kWh += Co2kg*Co2kw;
                                                }else {
                                                        Co2kg += (endTime-startTime)/60*0.725; 
                                                        Co2kw += (endTime-startTime)/Process[sequence[i]]*Power[sequence[i]]; 
                                                        totalCo2kWh += Co2kg*Co2kw;
                                                }
                                        }
                        }
                        startTime = endTime;
        }
//		System.out.print("totalCo2kg: "+Math.rint(totalCo2kg*1000)/1000+"\t"+"totalCo2kw: "+ Math.rint(totalCo2kw*1000)/1000+"\t"+"totalCo2kWh: "+ Math.rint(totalCo2kWh*1000)/1000);		
//		System.out.print("totalCo2kWh: "+ Math.rint(totalCo2kWh*1000)/1000);
    return Math.rint(totalCo2kWh*1000)/1000;//Math.rint(totalCo2kWh*1000)/1000
  }
  
  @Override
  public double evaluateAll(chromosome _chromosome1, int numberOfSalesmen) {
    this.setData(_chromosome1, numberOfSalesmen);
    double result = 0;
    
    try {
      result = this.evaluateAllCo2Cost(_chromosome1.genes);
    } catch (ParseException ex) {
      Logger.getLogger(ObjFunctionCo2.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    return result ;
  }
  
  @Override
  public double[] getObjectiveValues(int index) {
    double objectives[];
    objectives = chromosome1.getObjValue();
    double obj = evaluateAll(chromosome1, numberOfSalesmen);
    objectives[0] = obj;
    chromosome1.setObjValue(objectives);
    
//    return population.getObjectiveValues(index);

    return chromosome1.getObjValue();
  }
  
  @Override
  public void calcObjective() {
    double obj;
    double objectives[];

    for (int i = 0; i < population.getPopulationSize(); i++) {
      objectives = population.getObjectiveValues(i);
      obj = evaluateAll(population.getSingleChromosome(i), numberOfSalesmen);
      objectives[indexOfObjective] = obj;
      population.setObjectiveValue(i, objectives);
      
      chromosome1.setObjValue(objectives);
      population.setSingleChromosome(i, chromosome1);
    }
  }
  
  @Override
  public void setData(chromosome chromosome1, int numberOfSalesmen) {
    this.chromosome1 = chromosome1;
    this.numberOfSalesmen = numberOfSalesmen;
    length = chromosome1.getLength();
  }
  
  @Override
  public void setOASData(double[] r, double[] p, double[] d, double[] d_bar, double[] e, double[] w, double[] power, double[][] s, int numberOfSalesmen) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
  
  @Override
  public void setPowerData(double[] Process,double[] Power) {
    this.Process = Process;
    this.Power = Power;
//    for (int i=0;i<Power.length;i++){
//      System.out.print(Power[i]+" ");
//    }
  }
 
}
