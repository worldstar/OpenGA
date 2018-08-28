package openga.applications.data;
import java.io.*;
import java.util.*;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class singleMachine {
  public singleMachine() {
  }

  /**
   * The test problem is from the lecture note of POM of Prof. Chang when we want to calculate it
   * by the Wilkerson-Irwin.
   * @return
   */
  String fileName, message = "";
  int processingTime[] = new int[]{6, 3, 5, 3, 3, 3};//4, 7, 1, 6, 3
  int dueDate[] = new int[]{6, 7, 10, 6, 14, 14};
  int pTimeTest1[] = new int[]{6, 4, 7, 5};
  int dueTest1[] = new int[]{9, 10, 8, 6};
  double alpha[];
  double beta[];

  public int[] getTest1_ProcessingTime(){
    return processingTime;
  }

  public int[] getTest1_DueData(){
    return dueDate;
  }

  String M20[] = new String[]
{"sks222a",
"sks223a",
"sks224a",
"sks225a",
"sks226a",
"sks227a",
"sks228a",
"sks232a",
"sks233a",
"sks234a",
"sks235a",
"sks236a",
"sks237a",
"sks238a",
"sks242a",
"sks243a",
"sks244a",
"sks245a",
"sks246a",
"sks247a",
"sks248a",
"sks252a",
"sks253a",
"sks254a",
"sks255a",
"sks256a",
"sks257a",
"sks258a",
"sks262a",
"sks263a",
"sks264a",
"sks265a",
"sks266a",
"sks267a",
"sks268a",
"sks272a",
"sks273a",
"sks274a",
"sks275a",
"sks276a",
"sks277a",
"sks278a",
"sks282a",
"sks283a",
"sks284a",
"sks285a",
"sks286a",
"sks287a",
"sks288a"};

  String M30[] = new String[]
      {"sks322a",
      "sks323a",
      "sks324a",
      "sks325a",
      "sks326a",
      "sks327a",
      "sks328a",
      "sks332a",
      "sks333a",
      "sks334a",
      "sks335a",
      "sks336a",
      "sks337a",
      "sks338a",
      "sks342a",
      "sks343a",
      "sks344a",
      "sks345a",
      "sks346a",
      "sks347a",
      "sks348a",
      "sks352a",
      "sks353a",
      "sks354a",
      "sks355a",
      "sks356a",
      "sks357a",
      "sks358a",
      "sks362a",
      "sks363a",
      "sks364a",
      "sks365a",
      "sks366a",
      "sks367a",
      "sks368a",
      "sks372a",
      "sks373a",
      "sks374a",
      "sks375a",
      "sks376a",
      "sks377a",
      "sks378a",
      "sks382a",
      "sks383a",
      "sks384a",
      "sks385a",
      "sks386a",
      "sks387a",
      "sks388a"};

  String M40[] = new String[]
      {"sks422a",
      "sks423a",
      "sks424a",
      "sks425a",
      "sks426a",
      "sks427a",
      "sks428a",
      "sks432a",
      "sks433a",
      "sks434a",
      "sks435a",
      "sks436a",
      "sks437a",
      "sks438a",
      "sks442a",
      "sks443a",
      "sks444a",
      "sks445a",
      "sks446a",
      "sks447a",
      "sks448a",
      "sks452a",
      "sks453a",
      "sks454a",
      "sks455a",
      "sks456a",
      "sks457a",
      "sks458a",
      "sks462a",
      "sks463a",
      "sks464a",
      "sks465a",
      "sks466a",
      "sks467a",
      "sks468a",
      "sks472a",
      "sks473a",
      "sks474a",
      "sks475a",
      "sks476a",
      "sks477a",
      "sks478a",
      "sks482a",
      "sks483a",
      "sks484a",
      "sks485a",
      "sks486a",
      "sks487a",
      "sks488a"};

  String M50[] = new String[]
      {"sks522a",
      "sks523a",
      "sks524a",
      "sks525a",
      "sks526a",
      "sks527a",
      "sks528a",
      "sks532a",
      "sks533a",
      "sks534a",
      "sks535a",
      "sks536a",
      "sks537a",
      "sks538a",
      "sks542a",
      "sks543a",
      "sks544a",
      "sks545a",
      "sks546a",
      "sks547a",
      "sks548a",
      "sks552a",
      "sks553a",
      "sks554a",
      "sks555a",
      "sks556a",
      "sks557a",
      "sks558a",
      "sks562a",
      "sks563a",
      "sks564a",
      "sks565a",
      "sks566a",
      "sks567a",
      "sks568a",
      "sks572a",
      "sks573a",
      "sks574a",
      "sks575a",
      "sks576a",
      "sks577a",
      "sks578a",
      "sks582a",
      "sks583a",
      "sks584a",
      "sks585a",
      "sks586a",
      "sks587a",
      "sks588a"};

  String M60[] = new String[]
{"sks622a",
"sks625a",
"sks628a",
"sks652a",
"sks655a",
"sks658a",
"sks682a",
"sks685a",
"sks688a"};

  String M90[] = new String[]
{
    "sks922a",
    "sks955a",
    "sks988a"
    
//"sks922a",
//"sks925a",
//"sks928a",
//"sks952a",
//"sks955a",
//"sks958a",
//"sks982a",
//"sks985a",
//"sks988a"
  };


  public String getFileName(int _size, int index){
    if(_size == 20){
      return M20[index];
    }
    else if(_size == 30){
      return M30[index];
    }
    else if(_size == 40){
      return M40[index];
    }
    else if(_size == 50){
      return M50[index];
    }
    else if(_size == 60){
      return M60[index];
    }
    else{
      return M90[index];
    }
  }

  public void setData(String fileName){
    this.fileName = fileName;
    if( fileName == null ){
        System.out.println( "Specify the file name please.");
        System.exit(1);
    }
  }

  public void getDataFromFile(){
    // Read customers from file
    //System.out.print( "Loading customers from file..." );
    //to generate file data into message variable.

    try
    {
        File file = new File( fileName );
        FileInputStream fis = new FileInputStream( file );
        DataInputStream in = new DataInputStream(fis);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String message = "", eachLine = "";

        while ((eachLine = br.readLine()) != null)   {
          message += eachLine + "\n";
        }
        
        String[] STxt;
        STxt = message.split("\n| ");
        
//        System.out.println(message);
//        StringTokenizer tokens = new StringTokenizer(message);
        //set the coordinates of depot.
        int length = 0;

        //to set the coordination and demand of each customer
        int size = Integer.parseInt(STxt[0]);//tokens.nextToken()
        processingTime = new int[size];
        dueDate = new int[size];
        alpha = new double[size];
        beta = new double[size];

        int tempCount = 1;
        for(int i = 0 ; i < size ; i++)
        {
          processingTime[i] = Integer.parseInt(STxt[tempCount]);
          dueDate[i] = Integer.parseInt(STxt[tempCount+2]);
          alpha[i] = Integer.parseInt(STxt[tempCount+3]);
          beta[i] = Integer.parseInt(STxt[tempCount+4]);
          tempCount+=5;
        }
        
        
        
//        for(int i = 0 ; i < size ; i ++ ){
//          int tempArray[] = new int[5];
//          tempArray[0] = Integer.parseInt(tokens.nextToken());
//          tempArray[1] = Integer.parseInt(tokens.nextToken());
//          tempArray[2] = Integer.parseInt(tokens.nextToken());
//          tempArray[3] = Integer.parseInt(tokens.nextToken());
//          tempArray[4] = Integer.parseInt(tokens.nextToken());
//          //System.out.println(tempArray[0]+" "+tempArray[1]+" "+tempArray[2]+" "+tempArray[3]+" "+tempArray[4]);
//
//          processingTime[i] = tempArray[0];
//          dueDate[i] = tempArray[2];
//          alpha[i] = tempArray[3];
//          beta[i] = tempArray[4];
//        }
        
        
    }   //end try
    catch( Exception e )
    {
        e.printStackTrace();
        System.out.println(e.toString());
    }   // end catch
    //System.out.println( "done" );
  }

  public int[] getPtime(){
    return processingTime;
  }

  public int[] getDueDate(){
    return dueDate;
  }

  public double[] getAlpha(){
    return alpha;
  }

  public double[] getBeta(){
    return beta;
  }

}