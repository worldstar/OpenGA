package openga.MultipleObjective;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class SamplePareto_ReadFile {
    ArrayList a = new ArrayList();
    ArrayList b = new ArrayList();
    
    double data[][];
    String FileName="",extension="";
    
    public void setRead_FileName(String Filename, String extension){
      this.FileName = Filename;
      this.extension = extension;
    }
    
    public double[][] getdata() {
      return data;
    }
    
    public void read_csv() throws FileNotFoundException, IOException {
        FileReader fr = new FileReader(FileName + extension);
        BufferedReader br = new BufferedReader(fr);

        String str = br.readLine(), temp[];
        while ((str = br.readLine()) != null) {
            temp = str.split(",");            
            a.add(temp[0]);
            b.add(temp[1]);

        }
        fr.close();
        br.close();

        data = new double[a.size()][2];
        for (int i = 0; i < a.size(); i++) {
            data[i][0] = Double.parseDouble((String) a.get(i));
            data[i][1] = Double.parseDouble((String) b.get(i));
        }
    }
    
    
    public void result_output() {
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                if (j!=data[0].length-1) {
                    System.out.print(data[i][j] + "\t");}
                else {
                    System.out.print(data[i][j]);                
                }
            }System.out.println();
        }
    }
    
    
    public static void main(String[] args) throws IOException  {
        SamplePareto_ReadFile a = new SamplePareto_ReadFile();
        a.read_csv();
        a.result_output();
    }

}
