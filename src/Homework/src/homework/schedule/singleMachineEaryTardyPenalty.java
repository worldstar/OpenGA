package homework.schedule;

/**
 * <p>Title: The Dominance Properties for Single Machine Problem with early and tardy penalty.</p>
 * <p>Description: We derive some optimal schedule properties for single machine problem without inserting idle time.
 * The Objective function we apply is earliness-tardiness. The further information can be obtained at
 * http://ppc.iem.yzu.edu.tw/publication/sourceCodes/SingleMachine/index.html</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Yuan-Ze University.</p>
 * @author V. Mani, Pei-Chann Chang, Shih Hsin Chen.
 * @email s939506@mail.yzu.edu.tw
 * @version 1.0
 */

public class singleMachineEaryTardyPenalty extends singleMachine {
  public singleMachineEaryTardyPenalty() {
  }

  double alpha[];
  double beta[];

  public void setAlphaBeta(double alpha[], double beta[]){
    this.alpha = alpha;
    this.beta = beta;
  }

  /**The 9 dominance properties.
   * @param job1 The prior job.
   * @param job2 The later job.
   * @param A    The finishing time of the job ahead of job1 and job2.
   * @return     to return notStatisfied. If true, it means there is a condition violated.
   */
  private boolean checkStatus(int job1, int job2, int A){
    //System.out.println("job1= "+(job1+1)+", job2= "+(job2+1)+", p1= "+processingTime[job1]+", p2= "+processingTime[job2]+", A= "+A+", d1= "+dueDate[job1] + ", d2= "+dueDate[job2]);
    boolean notStatisfied = false;//is the condition is false, it means the condi
    double ai = alpha[job1], aj = alpha[job2];
    double bi = beta[job1], bj = beta[job2];
    //System.out.println("Situation 1.");
    if(dueDate[job1] > (A + processingTime[job1]) && dueDate[job2] > (A + processingTime[job1] + processingTime[job2]) && dueDate[job2] > A + processingTime[job2]  && dueDate[job1] > (A + processingTime[job1] + processingTime[job2])){
      if((ai != 0 && aj != 0) && !AisGreatThanToB(processingTime[job1]/ai, processingTime[job2]/aj)){
        notStatisfied = true;
      }
    }//end situation 1
    else if(dueDate[job1] > (A + processingTime[job1]) && dueDate[job2] > (A + processingTime[job1] + processingTime[job2]) && dueDate[job2] > (A + processingTime[job2])  && dueDate[job1] < (A + processingTime[job1] + processingTime[job2])){
      double multiple1 = (alpha[job1] + beta[job1] + alpha[job2]);
      if(!AisLessThanToB(dueDate[job1], A + (processingTime[job1]*((ai + bi + aj)/ (ai + bi)) + processingTime[job2]*bj/(ai + bi)))){
        notStatisfied = true;
      }
    }//end situation 2
    else if(dueDate[job1] < (A + processingTime[job1]) && dueDate[job2] < (A + processingTime[job1] + processingTime[job2]) && dueDate[job2] <= (A + processingTime[job2])  && dueDate[job1] < (A + processingTime[job1] + processingTime[job2])){
      if((bi != 0 && bj != 0) && !AisLessThanToB(processingTime[job1]/bi, processingTime[job2]/bj)){
        notStatisfied = true;
      }
    }//end situation 3
    else if(dueDate[job1] < (A + processingTime[job1]) && dueDate[job2] < (A + processingTime[job1] + processingTime[job2]) && dueDate[job2] >= (A + processingTime[job2])  && dueDate[job1] < (A + processingTime[job1] + processingTime[job2])){
      if(!AisGreatThanToB(dueDate[job2], A + (processingTime[job2]*(aj + bj - bi)/(aj + bj) + processingTime[job1]*bj)/(aj + bj))){
        notStatisfied = true;
      }
    }//end situation 4
    else if(dueDate[job1] > (A + processingTime[job1]) && dueDate[job2] < (A + processingTime[job1] + processingTime[job2]) && dueDate[job2] > (A + processingTime[job2])  && dueDate[job1] < (A + processingTime[job1] + processingTime[job2])){
      if(!AisGreatThanToB((aj + bj)*(dueDate[job2] - processingTime[job2] - A) + processingTime[job2]*bi, (ai + bi)*(dueDate[job1] - processingTime[job1] - A) + processingTime[job1]*bj)){
        notStatisfied = true;
      }
    }//end situation 5
    else if(dueDate[job1] > (A + processingTime[job1]) && dueDate[job2] < (A + processingTime[job1] + processingTime[job2]) && dueDate[job2] <= (A + processingTime[job2])  && dueDate[job1] > (A + processingTime[job1] + processingTime[job2])){
      if(!AisLessThanToB(dueDate[job1], A + (processingTime[job1]*(ai + bi - bj)+ processingTime[job2]*bi)/(ai + bi))){
        notStatisfied = true;
      }
    }//end situation 6
    else if(dueDate[job1] > (A + processingTime[job1]) && dueDate[job2] < (A + processingTime[job1] + processingTime[job2]) && dueDate[job2] > (A + processingTime[job2])  && dueDate[job1] > (A + processingTime[job1] + processingTime[job2])){
      if(!AisGreatThanToB(dueDate[job2], A + (processingTime[job1]*bj + processingTime[job2]*(aj + bj + ai))/(aj + bj))){
        notStatisfied = true;
      }
    }//end situation 7
    else if(dueDate[job1] < (A + processingTime[job1]) && dueDate[job2] > (A + processingTime[job1] + processingTime[job2]) && dueDate[job2] > (A + processingTime[job2])  && dueDate[job1] < (A + processingTime[job1] + processingTime[job2])){
      //System.out.println("Situation 8.");//Do nothing. Job 1 is always ahead of job 2.
    }//end situation 8
    else{
      //System.out.println("Situation 9. Always swap solutions.");
      notStatisfied = true;
    }
    return notStatisfied;
  }

  public void calcObj(){
    int currentTime = 0;
    obj = 0;
    for(int i = 0 ; i < numberOfJobs ; i ++ ){
      if(finishTime[sequence[i]] < dueDate[sequence[i]]){//earliness
        obj += alpha[sequence[i]]*(dueDate[sequence[i]] - finishTime[sequence[i]]);
      }
      else{
        obj += beta[sequence[i]]*(finishTime[sequence[i]] - dueDate[sequence[i]]);
      }
    }
  }

  public static void main(String[] args) {
    singleMachineEaryTardyPenalty singleMachine1 = new singleMachineEaryTardyPenalty();
    singleMachineData singleMachineData1 = new singleMachineData();
    System.out.println("singleMachineEaryTardyPenalty");

    for(int replications = 0 ; replications < 30 ; replications ++ ){
      int jobSets[] = new int[]{20, 30, 40, 50};//20, 30, 40, 50, 100, 200
      for(int m = 0 ; m < jobSets.length ; m ++ ){//jobSets.length
        for(int k = 0 ; k < 49 ; k ++ ){
          homework.util.readSingleMachineData readSingleMachineData1 = new homework.util.readSingleMachineData();
          int numberOfJobs = jobSets[m];
          String fileName = readSingleMachineData1.getFileName(numberOfJobs, k);
          //fileName = "bky"+numberOfJobs+"_1";
          System.out.print(fileName+"\t");
          readSingleMachineData1.setData("sks/"+fileName+".txt");
          readSingleMachineData1.getDataFromFile();

          int obj = Integer.MAX_VALUE;
          int dueDate[] = readSingleMachineData1.getDueDate();
          int processingTime[] = readSingleMachineData1.getPtime();
          int currentSoluion[] = new int[numberOfJobs];
          double alpha[] = readSingleMachineData1.getAlpha();
          double beta[] = readSingleMachineData1.getBeta();
          homework.util.timeClock timeClock1 = new homework.util.timeClock();
          timeClock1.start();
          for(int i = 0 ; i < 100 ; i ++ ){//i initial solutions
            int sequence[] = new int[numberOfJobs];
            for(int j = 0 ; j < numberOfJobs ; j ++ ){
              sequence[j] = j;
            }
            singleMachine1.setData(numberOfJobs, dueDate, processingTime, sequence);
            singleMachine1.setAlphaBeta(alpha, beta);
            singleMachine1.generateInitialSolution(i);
            singleMachine1.startAlgorithm();
            //to compare the objective value.
            if(obj > singleMachine1.getObjValue()){
              obj = singleMachine1.getObjValue();
              currentSoluion = singleMachine1.getSolution();
            }
          }
          timeClock1.end();
          String result = fileName+"\t"+obj+"\t"+timeClock1.getExecutionTime()/1000.0+"\n";
          System.out.print(result);
          singleMachine1.writeFile("singleMachineDP20060526", result);
        }
      }
    }//end replications.
  }//end main

}