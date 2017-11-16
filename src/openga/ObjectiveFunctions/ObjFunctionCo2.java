package openga.ObjectiveFunctions;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import openga.chromosomes.chromosome;
import openga.chromosomes.populationI;

public final class ObjFunctionCo2 extends ObjectiveFunctionTSP  implements ObjFunctionCo2I {// �����***************************************************

  private double startTime;
  private double[] Process;
  private int Power = 20;

  public static void main(String[] args) throws IOException, Exception {
    ObjFunctionCo2 a = new ObjFunctionCo2();//�o�����
    ObjFunctionCallCo2 b = new ObjFunctionCallCo2();//�j�������
    a.setData(b.getProcess());
    System.out.println(a.evaluateAll());
  }
  
  @Override
  public void setData(double[] Process) {
    this.Process = Process;
  }
  
  public double evaluateAll()throws ParseException{
		SimpleDateFormat Time = new SimpleDateFormat("HH:mm");
                startTime = (double) Time.parse("02:00").getTime() / (1000 * 60);
		
		double Co2kg = 0, Co2kw=0, totalCo2kg=0, totalCo2kw=0, totalCo2kWh=0; 
		
		double h3 = Time.parse("03:00").getTime()/(1000*60),h6 = Time.parse("06:00").getTime()/(1000*60),h12 = Time.parse("12:00").getTime()/(1000*60);
		double h14 = Time.parse("14:00").getTime()/(1000*60),h17 = Time.parse("17:00").getTime()/(1000*60),h18 = Time.parse("18:00").getTime()/(1000*60);
		double h21 = Time.parse("21:00").getTime()/(1000*60),h23 = Time.parse("23:00").getTime()/(1000*60),h24 = Time.parse("24:00").getTime()/(1000*60);
		//   0~3   3~6  6~12 12~14 14~17
		// 23~24 21~23 18~21 17~18
		// 0.725 0.700 0.693 0.682 0.669
		for (int i=0;i<Process.length;i++) {
			Co2kg=0;Co2kw=0;
//			System.out.print("Start["+i+"] :"+Time.format(startTime*60000)+"   ");
			double endTime = startTime + Process[i]; 
//                        System.out.print((endTime-startTime)+"   "+Time.format(endTime*60000)+"     co2kwh: ");
				if (startTime < h3) { // 00:00~03:00  0.725 
					if (endTime > h3) {
						Co2kg = (h3-startTime)/60*0.725; totalCo2kg += Co2kg;
						Co2kw = (h3-startTime)/Process[i]*Power; totalCo2kw += Co2kw;
						totalCo2kWh += Co2kg*Co2kw;
						startTime = h3;
					}else {
						Co2kg = (endTime-startTime)/60*0.725; totalCo2kg += Co2kg;
						Co2kw = (endTime-startTime)/Process[i]*Power; totalCo2kw += Co2kw;
						totalCo2kWh += Co2kg*Co2kw;
					}
				}
				if (startTime >=h3 && startTime < h6) { // 03:00~06:00  0.700
					if (endTime > h6) {
						Co2kg = (h6-startTime)/60*0.700;	totalCo2kg += Co2kg;
						Co2kw = ((h6-startTime)/Process[i])*Power; totalCo2kw += Co2kw;
						totalCo2kWh += Co2kg*Co2kw;
//						System.out.print("..."+Co2kw*Co2kg+"...");

						startTime = h6;
					}else {
						Co2kg += (endTime -startTime)/60*0.700; totalCo2kg += Co2kg;
						Co2kw += (endTime -startTime)/Process[i]*Power; totalCo2kw += Co2kw;
						totalCo2kWh += Co2kg*Co2kw;
					}
				}
				if (startTime >=h6 && startTime < h12) { // 06:00~12:00 0.693
					if (endTime > h12) {
						Co2kg = (h12-startTime)/60*0.693; totalCo2kg += Co2kg;
						Co2kw = (h12-startTime)/Process[i]*Power; totalCo2kw += Co2kw;
						totalCo2kWh += Co2kg*Co2kw;
						startTime = h12;
					}else {
						Co2kg += (endTime-startTime)/60*0.693; totalCo2kg += Co2kg;
						Co2kw += (endTime-startTime)/Process[i]*Power; totalCo2kw += Co2kw;
//						System.out.print("..."+(endTime - startTime)/60*0.693 * (endTime - startTime)/Process[i]*20+"...");
						totalCo2kWh += Co2kg*Co2kw;
					}
				}
				if (startTime >=h12 && startTime <h14) { // 12:00~14:00 0.682
					if (endTime > h14) {
						Co2kg = (h14-startTime)/60*0.682; totalCo2kg += Co2kg;
						Co2kw = (h14-startTime)/Process[i]*Power; totalCo2kw += Co2kw;
						totalCo2kWh += Co2kg*Co2kw;
						startTime = h14;
					}else {
						Co2kg += (endTime-startTime)/60*0.682; totalCo2kg += Co2kg;
						Co2kw += (endTime-startTime)/Process[i]*Power; totalCo2kw += Co2kw;
						totalCo2kWh += Co2kg*Co2kw;
					}
				}
				if (startTime >=h14 && startTime <h17) { // 14:00~17:00 0.669
					if (endTime > h17) {
						Co2kg = (h17-startTime)/60*0.669; totalCo2kg += Co2kg;
						Co2kw = (h17-startTime)/Process[i]*Power; totalCo2kw += Co2kw;
						totalCo2kWh += Co2kg*Co2kw;
						startTime = h17;
					}else {
						Co2kg += (endTime-startTime)/60*0.669; totalCo2kg += Co2kg;
						Co2kw += (endTime-startTime)/Process[i]*Power; totalCo2kw += Co2kw;
						totalCo2kWh += Co2kg*Co2kw;
					}
				}
				if (startTime >=h17 && startTime <h18) { // 17:00~18:00 0.682
					if (endTime > h18) {
						Co2kg = (h18-startTime)/60*0.682; totalCo2kg += Co2kg;
						Co2kw = (h18-startTime)/Process[i]*Power; totalCo2kw += Co2kw;
						totalCo2kWh += Co2kg*Co2kw;
						startTime = h18;
					}else {
						Co2kg += (endTime-startTime)/60*0.682; totalCo2kg += Co2kg;
						Co2kw += (endTime-startTime)/Process[i]*Power; totalCo2kw += Co2kw;
						totalCo2kWh += Co2kg*Co2kw;
					}
				}
				if (startTime >=h18 && startTime <h21) { // 18:00~21:00 0.693
					if (endTime > h21) {
						Co2kg = (h21-startTime)/60*0.693; totalCo2kg += Co2kg;
						Co2kw = (h21-startTime)/Process[i]*Power; totalCo2kw += Co2kw;
						totalCo2kWh += Co2kg*Co2kw;
						startTime = h21;
					}else {
						Co2kg += (endTime-startTime)/60*0.693; totalCo2kg += Co2kg;
						Co2kw += (endTime-startTime)/Process[i]*Power; totalCo2kw += Co2kw;
						totalCo2kWh += Co2kg*Co2kw;
					}
				}
				if (startTime >=h21 && startTime <h23) { // 21:00~23:00 0.700
					if (endTime > h23) {
						Co2kg = (h23-startTime)/60*0.700; totalCo2kg += Co2kg;
						Co2kw = (h23-startTime)/Process[i]*Power; totalCo2kw += Co2kw;
						totalCo2kWh += Co2kg*Co2kw;
						startTime = h23;
					}else {
						Co2kg += (endTime-startTime)/60*0.700; totalCo2kg += Co2kg;
						Co2kw += (endTime-startTime)/Process[i]*Power; totalCo2kw += Co2kw;
						totalCo2kWh += Co2kg*Co2kw;
					}
				}
				if (startTime >=h23 && startTime <h24) { // 23:00~24:00 0.725
					if (endTime >=h24) {
						Co2kg = (h24-startTime)/60*0.725; totalCo2kg += Co2kg;
						Co2kw = (h24-startTime)/Process[i]*Power; totalCo2kw += Co2kw;
						totalCo2kWh += Co2kg*Co2kw;
						startTime = h24; //�����ɶ��j��24:00.getTime��
					}else {
						Co2kg += (endTime-startTime)/60*0.725;	totalCo2kg += Co2kg;
						Co2kw += (endTime-startTime)/Process[i]*Power; totalCo2kw += Co2kw;
						totalCo2kWh += Co2kg*Co2kw;
					}
				}
				
				if (startTime == h24) {  // �}�l���� 
					startTime-=1320;     // startTime = 02:00.getTime��
					endTime-=1320;       // endTime   = 02:xx.getTime��
						if(startTime < h3)
							if (endTime>h3) {// �����ɶ�>3�I,�h���p��(3�I-�}�u�ɶ�)����
								Co2kg += (h3-startTime)/60*0.725; totalCo2kg += Co2kg;
								Co2kw += (h3-startTime)/Process[i]*Power; totalCo2kw += Co2kw;
								startTime = h3;
								totalCo2kWh += Co2kg*Co2kw;
							}else {//�Y�L>3�I,�h��ܵ����ɶ��b�j��}�u�ɶ����Ĥ@�ӰϬq��
								Co2kg += (endTime-startTime)/60*0.725; totalCo2kg += Co2kg;
								Co2kw += (endTime-startTime)/Process[i]*Power; totalCo2kw += Co2kw;
								totalCo2kWh += Co2kg*Co2kw;
							}
						if(startTime >=h3 && startTime <h6) {
							if (endTime>h6) { // �����ɶ�>6�I,�h���p��(6�I-�ĤG�Ϭq�}�l)����
								Co2kg += (h6-startTime)/60*0.700; totalCo2kg += Co2kg;
								Co2kw += (h6-startTime)/Process[i]*Power; totalCo2kw += Co2kw;
								startTime = h6;
								totalCo2kWh += Co2kg*Co2kw;
							}else {//�Y�L>6�I,�h��ܵ����ɶ��b�j��}�u�ɶ��Ĥ@�ɶ��Ϭq+�W�ĤG�ӰϬq���ɶ�
								Co2kg += (endTime-startTime)/60*0.700; totalCo2kg += Co2kg;
								Co2kw += (endTime-startTime)/Process[i]*Power; totalCo2kw += Co2kw;
								totalCo2kWh += Co2kg*Co2kw;
							}
						}
						if(startTime >=h6 && startTime <h12){
							if (endTime>h12) {// �����ɶ�>12�I,�h���p��(12�I-�ĤT�Ϭq�}�l)����
								Co2kg += (h12-startTime)/60*0.693; totalCo2kg += Co2kg;
								Co2kw += (h12-startTime)/Process[i]*Power; totalCo2kw += Co2kw;
								startTime = h12;//�o�䪺if�P�_ ���Ȯɼg�T�Ӯɶ��Ϭq
								totalCo2kWh += Co2kg*Co2kw;
							}else {//�Y�L>12�I,�h��ܵ����ɶ��b�j��}�u�ɶ��ܲĤ@�B�G�ɶ��Ϭq+�W�ĤT�ӰϬq���ɶ�
								Co2kg += (endTime-startTime)/60*0.693; totalCo2kg += Co2kg;
								Co2kw += (endTime-startTime)/Process[i]*Power; totalCo2kw += Co2kw;
								totalCo2kWh += Co2kg*Co2kw;
							}
						}	
						if (startTime >=h12 && startTime <h14) {
							if (endTime>h14) {// �����ɶ�>14�I,�h���p��(14�I-�ĥ|�Ϭq�}�l)����
								Co2kg += (h14-startTime)/60*0.682; totalCo2kg += Co2kg;
								Co2kw += (h14-startTime)/Process[i]*Power; totalCo2kw += Co2kw;
								startTime = h14;//�o�䪺if�P�_ ���Ȯɼg�T�Ӯɶ��Ϭq
								totalCo2kWh += Co2kg*Co2kw;
							}else {//�Y�L>14�I,�h��ܵ����ɶ��b�j��}�u�ɶ��ܲĤ@�B�G�B�T�ɶ��Ϭq+�W�ĥ|�ӰϬq���ɶ�
								Co2kg += (endTime-startTime)/60*0.682; totalCo2kg += Co2kg;
								Co2kw += (endTime-startTime)/Process[i]*Power; totalCo2kw += Co2kw;
								totalCo2kWh += Co2kg*Co2kw;
							}
						}	
						if(startTime >=h14 && startTime <h17) {
							if (endTime>h17) {// �����ɶ�>17�I,�h���p��(17�I-�Ĥ��Ϭq�}�l)����
								Co2kg += (h17-startTime)/60*0.669; totalCo2kg += Co2kg;
								Co2kw += (h17-startTime)/Process[i]*Power; totalCo2kw += Co2kw;
								startTime = h17;//�o�䪺if�P�_ ���Ȯɼg�T�Ӯɶ��Ϭq
								totalCo2kWh += Co2kg*Co2kw;
							}else {//�Y�L>18�I,�h��ܵ����ɶ��b�j��}�u�ɶ��ܲĤ@�B�G�B�T�B�|�B���ɶ��Ϭq+�W�Ĥ��ӰϬq���ɶ�
								Co2kg += (endTime-startTime)/60*0.669; totalCo2kg += Co2kg;
								Co2kw += (endTime-startTime)/Process[i]*Power; totalCo2kw += Co2kw;
								totalCo2kWh += Co2kg*Co2kw;
							}
						}
						if (startTime >=h17 && startTime <h18) {
							if (endTime>h18) {// �����ɶ�>18�I,�h���p��(18�I-�Ĥ��Ϭq�}�l)����
								Co2kg += (h18-startTime)/60*0.682; totalCo2kg += Co2kg;
								Co2kw += (h18-startTime)/Process[i]*Power; totalCo2kw += Co2kw;
								startTime = h18;//�o�䪺if�P�_ ���Ȯɼg�T�Ӯɶ��Ϭq
								totalCo2kWh += Co2kg*Co2kw;
							}else {//�Y�L>18�I,�h��ܵ����ɶ��b�j��}�u�ɶ��ܲĤ@�B�G�B�T�B�|�B���ɶ��Ϭq+�W�Ĥ��ӰϬq���ɶ�
								Co2kg += (endTime-startTime)/60*0.682; totalCo2kg += Co2kg;
								Co2kw += (endTime-startTime)/Process[i]*Power; totalCo2kw += Co2kw;
								totalCo2kWh += Co2kg*Co2kw;
							}
						}	
						if (startTime >=h18 && startTime <h21) {
							if (endTime>h21) {// �����ɶ�>21�I,�h���p��(21�I-�ĤC�Ϭq�}�l)����
								Co2kg += (h21-startTime)/60*0.693; totalCo2kg += Co2kg;
								Co2kw += (h21-startTime)/Process[i]*Power; totalCo2kw += Co2kw;
								startTime = h21;//�o�䪺if�P�_ ���Ȯɼg�T�Ӯɶ��Ϭq
								totalCo2kWh += Co2kg*Co2kw;
							}else {//�Y�L>12�I,�h��ܵ����ɶ��b�j��}�u�ɶ��ܲĤ@�B�G�B�T�B�|�B���B���ɶ��Ϭq+�W�ĤC�ӰϬq���ɶ�
								Co2kg += (endTime-startTime)/60*0.693; totalCo2kg += Co2kg;
								Co2kw += (endTime-startTime)/Process[i]*Power; totalCo2kw += Co2kw;
								totalCo2kWh += Co2kg*Co2kw;
							}
						}
						if (startTime >=h21 && startTime <h23) {
							if (endTime>h23) {// �����ɶ�>23�I,�h���p��(23�I-�ĤK�Ϭq�}�l)����
								Co2kg += (h23-startTime)/60*0.700; totalCo2kg += Co2kg;
								Co2kw += (h23-startTime)/Process[i]*Power; totalCo2kw += Co2kw;
								startTime = h23;//�o�䪺if�P�_ ���Ȯɼg�T�Ӯɶ��Ϭq
								totalCo2kWh += Co2kg*Co2kw;
							}else {//�Y�L>23�I,�h��ܵ����ɶ��b�j��}�u�ɶ��ܲĤ@�B�G�B�T�B�|�B���B���B�C�ɶ��Ϭq+�W�ĤK�ӰϬq���ɶ�
								Co2kg += (endTime-startTime)/60*0.700; totalCo2kg += Co2kg;
								Co2kw += (endTime-startTime)/Process[i]*Power; totalCo2kw += Co2kw;
								totalCo2kWh += Co2kg*Co2kw;
							}
						}
						if (startTime >=h23 && startTime <h24) {
							if (endTime>=h24) {
								Co2kg += (h24-startTime)/60*0.725; totalCo2kg += Co2kg;
								Co2kw += (h24-startTime)/Process[i]*Power; totalCo2kw += Co2kw;
								startTime = h24;
								totalCo2kWh += Co2kg*Co2kw;
							}else {
								Co2kg += (endTime-startTime)/60*0.725; totalCo2kg += Co2kg;
								Co2kw += (endTime-startTime)/Process[i]*Power; totalCo2kw += Co2kw;
								totalCo2kWh += Co2kg*Co2kw;
							}
						}
				}
				startTime = endTime;
//				System.out.println();
//				System.out.println(Math.rint(totalCo2kWh*1000)/1000);
		}
//		System.out.print("totalCo2kg: "+Math.rint(totalCo2kg*1000)/1000+"\t"+"totalCo2kw: "+ Math.rint(totalCo2kw*1000)/1000+"\t"+"totalCo2kWh: "+ Math.rint(totalCo2kWh*1000)/1000);		
//		System.out.print("totalCo2kWh: "+ Math.rint(totalCo2kWh*1000)/1000);
            return Math.rint(totalCo2kWh*1000)/1000;
	}
  
  @Override
  public void calcObjective() {// �����***************************************************
//    double obj = 0;
    double objectives[];

    for (int i = 0; i < population.getPopulationSize(); i++) {
//      obj = evaluateAll(X);
      
      objectives = population.getObjectiveValues(i);
      
      try {
       objectives[indexOfObjective] = evaluateAll();
      } catch (ParseException ex) {
        Logger.getLogger(ObjFunctionCo2.class.getName()).log(Level.SEVERE, null, ex);
      }
      
//      objectives[indexOfObjective] = obj;
      population.setObjectiveValue(i, objectives);
    }
  }
}
