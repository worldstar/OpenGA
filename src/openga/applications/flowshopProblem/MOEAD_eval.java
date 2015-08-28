package openga.applications.flowshopProblem;
import java.io.*;
import java.util.*;
import openga.util.fileWrite1;
import java.text.NumberFormat;

/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class MOEAD_eval {
  public MOEAD_eval() {
  }

  String fileName = "", message = "";
  int numberOfJobs = 20;
  int numberOfObjective = 3;
  double objs[][];

  public void SetData(String fileName){
    this.fileName = fileName;
  }

  String getFileName(int p_iType, int length, int  run, int  maxLocalSearch, double  pLS, int LS_Method){
     String str = "ParetoFront2ObjslocalSearch2\\flowshop_DMOEA_";

     if(p_iType == 1){
        str += "F1_";
      }
      else if (p_iType == 2) {
        str += "F2_";
      }
      else {
        str += "F3_";
      }

      str += length + "_" + run + "_" + maxLocalSearch + "_";

      if (pLS == 0.050000) {
        str += "0.050000_";
      }
      else if(pLS == 0.500000){
        str += "0.500000_";
      }
      else{
          str += "1.000000_";
      }

     //p_iType, length, run, p_iPop, crossoverRate, mutationRate, p_iSize, alpha
      str += LS_Method;

      str += ".dat";

      return str;
  }

  String outputStrings(int p_iType, int length, int  run, int  maxLocalSearch, double  pLS, int LS_Method){
    String str = "";
    if(p_iType == 1){
       str += "F1\t";
     }
     else if (p_iType == 2) {
       str += "F2\t";
     }
     else {
       str += "F3\t";
     }

     str += length + "\t" + run + "\t" + maxLocalSearch + "\t";

     if (pLS == 0.050000) {
       str += "0.050000\t";
     }
     else  if(pLS == 0.500000){
       str += "0.500000\t";
     }
     else{
         str += "1.000000\t";
     }

    //p_iType, length, run, p_iPop, crossoverRate, mutationRate, p_iSize, alpha
     str += LS_Method + "\t";


      return str;
  }


  public void getDataFromFile(){
    try
    {
        File file = new File( fileName );
        FileInputStream fis = new FileInputStream( file );
        FileReader fr = new FileReader(fileName);
        //System.out.println("讀取檔案ok\n");

        //讀取一個字元
        int c = fr.read();
        while(c != -1){                         //是否到達最後一行
           message += (char)c;                  //ANSI轉為char
           c = fr.read();                       //是否要換行
        }//end while

        fr.close();
        //System.out.println(message);
        StringTokenizer tokens = new StringTokenizer(message);
        //set the coordinates of depot.
        int length = 0;
        double temoObjs[][] = new double[500][numberOfObjective];

        //to set the coordination and demand of each customer
        int size = 0;
        while(tokens.hasMoreTokens()){
          for(int i = 0 ; i < numberOfObjective ; i ++ ){
            temoObjs[size][i] = Double.parseDouble(tokens.nextToken());
          }
          //temoObjs[size][0] = Double.parseDouble(tokens.nextToken());
          //temoObjs[size][1] = Double.parseDouble(tokens.nextToken());
          size ++;
        }

        objs = new double[size][numberOfObjective];

        for(int i = 0 ; i < size ; i ++ ){
          for(int k = 0 ; k < numberOfObjective ; k ++ ){
            objs[i][k] = temoObjs[i][k];
          }
        }
    }   //end try
    catch( Exception e )
    {
        e.printStackTrace();
        System.out.println(e.toString());
    }   // end catch
    //System.out.println( "done" );
  }


  public final double[][] getReferenceSet(int numberOfJob){
    openga.applications.data.flowShop data1 = new openga.applications.data.flowShop();
    if(numberOfObjective == 2){
      return data1.getReferenceSet(numberOfJob);
    }
    else{
      return data1.getReferenceSet3objs(numberOfJob);
    }
  }

  /**
   * To evaluate the solution quality by some metric. It uses the D1r here.
   * @param refSet The current known Pareto set
   * @param obtainedPareto After the implementation of your algorithm.
   * @return The D1r value.
   */
  public double calcSolutionQuality(double refSet[][], double obtainedPareto[][]){
    openga.util.algorithm.D1r d1r1 = new openga.util.algorithm.D1r();
    d1r1.setData(refSet, obtainedPareto);
    //d1r1.calcD1r();
    d1r1.calcD1rWithoutNormalization();
    return d1r1.getD1r();
  }

  /**
   * Write the data into text file.
   */
  public void writeFile(String fileName, String _result){
    fileWrite1 writeLotteryResult = new fileWrite1();
    writeLotteryResult.writeToFile(_result,fileName+".txt");
    Thread thread1 = new Thread(writeLotteryResult);
    thread1.run();
  }

  public static void main(String[] args) {
    int p_iRun   = 20;    // the total number of replications.
    int Popsize[] = new int[]{200, 300};//Size of Population[100, 200] [200, 300]
    int p_iSizeDOE[] = new int[]{10, 20, 30};// the size of neighbourhood [10, 20, 30]
    double Pc[] = new double[]{0.500000, 1.000000};
    double Pm[] = new double[]{0.500000, 1.000000};
    int number_of_job[] = new int[]{20, 40, 60, 80};
    int length;
    double alphaDOE[] = new double[]{0.600000, 0.800000, 1.000000};//[0.600000, 0.800000, 1.000000]
    double crossoverRate = 0.8;
    double mutationRate = 1.0;

    int maxLocalSearchDOE[] = new int[]{5, 10};//5, 10
    double pLSDOE[] = new double[]{0.050000, 0.500000, 1.000000};//0.05, 0.5, 1.0
    int LS_MethodDOE[] = new int[]{0, 1};//[swap = 0, shift = 1] 0, 1

    //For MOEA/D
    for(int iItem = 0 ; iItem < number_of_job.length ; iItem++){//the number of jobs
       for(int iType = 2 ; iType <= 3 ; iType++){//[Modified NBI = 1, Weighted sum method = 2, Techebycheff Function = 3]
          for(int iPopSize = 0 ; iPopSize < 2 ; iPopSize ++ ){//the population size
             for(int iPc = 0 ; iPc < 2 ; iPc ++ ){//crossover rate
                for(int iPm = 0 ; iPm < 2 ; iPm ++ ){//mutation rate
                   for(int iNeighborSize = 0 ; iNeighborSize < p_iSizeDOE.length ; iNeighborSize ++ ){//NeighborSize
                     for(int ialpha = 0 ; ialpha < 3 ; ialpha ++ ){//ialpha
                       //counter = 0;
                       //to assign the parameters
                       length = number_of_job[iItem];
                       int p_iType = iType;			//function type
                       int p_iItem = number_of_job[iItem];         //length
                       int p_iPop  = Popsize[iPopSize];            //population size
                       crossoverRate = Pc[iPc];
                       mutationRate = Pm[iPm];
                       int p_iSize = p_iSizeDOE[iNeighborSize];
                       double alpha = alphaDOE[ialpha];
                       ///////////////////////////////////
                       for(int run = 0 ; run < p_iRun ; run++){
                         String str;
                         String outputstr;
                         String NdSetFileName;
                         int SamplingParameter;
                         MOEAD_eval MOEAD_eval1 = new MOEAD_eval();

                         String typeString = "";
                         if(p_iType == 1){
                            typeString += "F1";
                          }
                          else if (p_iType == 2) {
                            typeString += "F2";
                          }
                          else {
                            typeString += "F3";
                          }
                          NumberFormat nf = NumberFormat.getInstance();
                          nf.setMinimumFractionDigits(6);

                         NdSetFileName = "Essex200711\\flowshop-MOEAD-ParetoFront3Objs1108\\flowshop_DMOEA_"+typeString+"_"+length+"_"+run+"_"+p_iPop+"_"
                             +(nf.format(crossoverRate))+"_"+nf.format(mutationRate)+"_"+p_iSize+"_"+nf.format(alpha)+".dat";
                         //System.out.println(NdSetFileName);
                         MOEAD_eval1.SetData(NdSetFileName);
                         MOEAD_eval1.getDataFromFile();
                         double refSet[][] = MOEAD_eval1.getReferenceSet(length);
                         double objArray[][] = MOEAD_eval1.objs;
                         double dEvaluation = MOEAD_eval1.calcSolutionQuality(refSet, objArray);
                         String implementResult = typeString+"\t"+length+"\t"+run+"\t"+p_iPop+"\t"
                             +(nf.format(crossoverRate))+"\t"+nf.format(mutationRate)+"\t"+p_iSize+"\t"+nf.format(alpha)+"\t";
                         implementResult += dEvaluation + "\n";
                         System.out.print(implementResult);
                         MOEAD_eval1.writeFile("MOEADforFlowShop1108_3obj", implementResult);
                     }
                  }//end run for-loop
                 }//end iNeighborSize
              }//end iPm
            }//end iPc
          }//end iPopSize
       }//end iType
    }//end iItem
    System.exit(0);
    /*
        for(int iItem = 0 ; iItem < 4 ; iItem++){//the number of jobs
           for(int iType = 2 ; iType <= 2 ; iType++){//[Modified NBI = 1, Weighted sum method = 2, Techebycheff Function = 3]
              for(int imaxLocalSearch = 0 ; imaxLocalSearch < maxLocalSearchDOE.length ; imaxLocalSearch ++ ){//the imaxLocalSearch
                 for(int ipLS = 0 ; ipLS < pLSDOE.length ; ipLS ++ ){//ipLS
                       for(int iLS_MethodDOE = 0 ; iLS_MethodDOE < LS_MethodDOE.length ; iLS_MethodDOE ++ ){//iLS_MethodDOE
                           //counter = 0;
                           //to assign the parameters
                           length = number_of_job[iItem];
                           int p_iType = iType;			//function type
                           int p_iItem = number_of_job[iItem];         //length
                           int maxLocalSearch  = maxLocalSearchDOE[imaxLocalSearch];
                           int LS_Method = LS_MethodDOE[iLS_MethodDOE];
                           double pLS = pLSDOE[ipLS];

                           ///////////////////////////////////
                           for(int run = 0 ; run < p_iRun ; run++){
                             String str;
                             String outputstr;
                             String NdSetFileName;
                             int SamplingParameter;
                             MOEAD_eval MOEAD_eval1 = new MOEAD_eval();
                             NdSetFileName = MOEAD_eval1.getFileName(p_iType, length, run, maxLocalSearch,
                                                         pLS, LS_Method);
                             //System.out.println(NdSetFileName);
                             MOEAD_eval1.SetData(NdSetFileName);
                             MOEAD_eval1.getDataFromFile();
                             double refSet[][] = MOEAD_eval1.getReferenceSet(length);
                             double objArray[][] = MOEAD_eval1.objs;
                             double dEvaluation = MOEAD_eval1.calcSolutionQuality(refSet, objArray);
                             String implementResult = MOEAD_eval1.outputStrings(p_iType, length, run, maxLocalSearch,
                                                         pLS, LS_Method);
                             implementResult += dEvaluation + "\n";
                             System.out.print(implementResult);
                             MOEAD_eval1.writeFile("MOEADforFlowShop1101_2objLocalSearch2", implementResult);
                      }//end run for-loop
                     }//end iNeighborSize
                }//end iPc
              }//end iPopSize
           }//end iType
        }//end iItem
    */

    for(int iItem = 0 ; iItem < 4 ; iItem++){//the number of jobs
      for(int iPopSize = 0 ; iPopSize < Popsize.length ; iPopSize ++ ){//the population size
         for(int iPc = 0 ; iPc < 2 ; iPc ++ ){//crossover rate
            for(int iPm = 0 ; iPm < 2 ; iPm ++ ){//mutation rate
              //counter = 0;
              //to assign the parameters
              length = number_of_job[iItem];
              int p_iItem = number_of_job[iItem];         //length
              int p_iPop  = Popsize[iPopSize];            //population size
              crossoverRate = Pc[iPc];
              mutationRate = Pm[iPm];
              ///////////////////////////////////
              for(int run = 0 ; run < p_iRun ; run++){
                String str;
                String outputstr;
                String NdSetFileName;
                int SamplingParameter;
                MOEAD_eval MOEAD_eval1 = new MOEAD_eval();

                String typeString = "";
                 NumberFormat nf = NumberFormat.getInstance();
                 nf.setMinimumFractionDigits(6);
                //flowshop-3objs-SPEA2

                NdSetFileName = "Essex200711\\flowshop-3objs-SPEA2-all\\SPEA2_"+length+"_"+run+"_"+p_iPop+"_"
                    +(nf.format(crossoverRate))+"_"+nf.format(mutationRate)+"_.txt";
               /*
                //flowshop-3objs-NSGA2
                NdSetFileName = "Essex200711\\flowshop-3objs-NSGA2-all\\NSGA2_"+length+"_"+run+"_"+p_iPop+"_"
                    +(nf.format(crossoverRate))+"_"+nf.format(mutationRate)+"_.txt";
                  */
                //System.out.println(NdSetFileName);
                MOEAD_eval1.SetData(NdSetFileName);
                MOEAD_eval1.getDataFromFile();
                double refSet[][] = MOEAD_eval1.getReferenceSet(length);
                double objArray[][] = MOEAD_eval1.objs;
                double dEvaluation = MOEAD_eval1.calcSolutionQuality(refSet, objArray);
                String implementResult = length+"\t"+run+"\t"+p_iPop+"\t"
                    +(nf.format(crossoverRate))+"\t"+nf.format(mutationRate)+"\t"+"\t";
                implementResult += dEvaluation + "\n";
                System.out.print(implementResult);
                MOEAD_eval1.writeFile("SPEA2forFlowShop1105_3objDOE", implementResult);
              }//end run for-loop
          }//end iPm
        }//end iPc
      }//end iPopSize

    }//end iItem

  }
}