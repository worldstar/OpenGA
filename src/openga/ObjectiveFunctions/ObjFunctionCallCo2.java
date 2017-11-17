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
    //�]�w�}�u�ɶ�
    SimpleDateFormat Time = new SimpleDateFormat("HH:mm");
    this.startTime = (double) Time.parse("02:00").getTime() / (1000 * 60);
    this.Power = 20;

    //Ū��(�m����) ��}
    FileReader fr = new FileReader("@..//..//p10x4_0.txt");
    BufferedReader br = new BufferedReader(fr);

    //Ū�J (�m����) �Ĥ@�� �r����� 10���B�z
    String temp = br.readLine(), str[];
    int size = Integer.parseInt(temp);
    this.Process = new double[size];
    this.Process2 = new double[size];
    //���
//    System.out.print("Process��ǡG");
//    for (int i = 0; i < Process.length; i++) {
//      System.out.print(" " + i + " ");
//    }
//    System.out.println();

    //Ū�i(�m����) �C�x�������Q�ݻP�B�z�ɶ�
//    System.out.print("Process�\���G");
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

    //�I�s�B���k
//		new Co2(Process,Power,startTime);
    //Co2 call_co2 = new Co2(Process,Power,startTime);
//    System.out.println();

    //�s��
    Random ran = new Random();       	//�ŧi�üƨ��
    int ran_num;						//�����üƲ��ͪ���
    int[] num1 = new int[size];			//�}�C0~9��(�m���ɲĤ@��)
    for (int i = 0; i < size; i++) {
      num1[i] = i;
    }	//�ᤩ�Ĥ@�Ӱ}�C[0~9]�}�C0~9���ƭ�
    int[] order = new int[size];		//�ŧi�ĤG�ӥΧ@�O�����Ǫ��}�C

//    System.out.print("Process�s�ǡG");
    for (int i = 0; i < order.length; i++) {	//�@����10��(��)
      ran_num = ran.nextInt(size - i);	//�ü�-i �O�]�� ���]�@�}�l0~9(�üƽd��) �C����1�ӭȽd��N���1��(0~8)..(0~7)..����
      order[i] = num1[ran_num];			//�Nnum1[ran_num]�����Ȩ��� ��order[0]..1..2����
      Process[i] = Process2[order[i]];
//      System.out.print(" " + order[i] + " "); //�[�ݶ���			
      for (int j = ran_num; j < order.length - i - 1; j++) {
        num1[j] = num1[j + 1];		//�C�����@�� �N�᭱�C�@�����e��1��
      }
    }
//    System.out.println();

    //�s�ǹ����\��
//    System.out.print("Process�\���G");
//    for (int i = 0; i < order.length; i++) {
//      System.out.print((int) Process[i] + " ");
//    }
//    System.out.println();

    //�I�s�B���k
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
