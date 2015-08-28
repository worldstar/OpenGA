package homework.schedule;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class singleMachineWithSameEarlyTardyPenalty extends singleMachineEaryTardyPenalty {
  public singleMachineWithSameEarlyTardyPenalty() {
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
    double a = ai, b = bi;
    //System.out.println("Situation 1.");
    if(dueDate[job1] > (A + processingTime[job1]) && dueDate[job2] > (A + processingTime[job1] + processingTime[job2]) && dueDate[job2] > A + processingTime[job2]  && dueDate[job1] > (A + processingTime[job1] + processingTime[job2])){
      if((ai != 0 && aj != 0) && !AisGreatThanToB(processingTime[job1], processingTime[job2])){
        notStatisfied = true;
      }
    }//end situation 1
    else if(dueDate[job1] > (A + processingTime[job1]) && dueDate[job2] > (A + processingTime[job1] + processingTime[job2]) && dueDate[job2] > (A + processingTime[job2])  && dueDate[job1] < (A + processingTime[job1] + processingTime[job2])){
      double multiple1 = (alpha[job1] + beta[job1] + alpha[job2]);
      if(!AisLessThanToB(dueDate[job1], A + (processingTime[job1]*(1 + a) + processingTime[job2]*b))){
        notStatisfied = true;
      }
    }//end situation 2
    else if(dueDate[job1] < (A + processingTime[job1]) && dueDate[job2] < (A + processingTime[job1] + processingTime[job2]) && dueDate[job2] <= (A + processingTime[job2])  && dueDate[job1] < (A + processingTime[job1] + processingTime[job2])){
      if((bi != 0 && bj != 0) && !AisLessThanToB(processingTime[job1], processingTime[job2])){
        notStatisfied = true;
      }
    }//end situation 3
    else if(dueDate[job1] < (A + processingTime[job1]) && dueDate[job2] < (A + processingTime[job1] + processingTime[job2]) && dueDate[job2] >= (A + processingTime[job2])  && dueDate[job1] < (A + processingTime[job1] + processingTime[job2])){
      if(!AisGreatThanToB(dueDate[job2], A + a*processingTime[job2] + b*processingTime[job1])){
        notStatisfied = true;
      }
    }//end situation 4
    else if(dueDate[job1] > (A + processingTime[job1]) && dueDate[job2] < (A + processingTime[job1] + processingTime[job2]) && dueDate[job2] > (A + processingTime[job2])  && dueDate[job1] < (A + processingTime[job1] + processingTime[job2])){
      if(!AisGreatThanToB((dueDate[job2] - processingTime[job2] - A) + processingTime[job2]*b, (dueDate[job1] - processingTime[job1] - A) + processingTime[job1]*b)){
        notStatisfied = true;
      }
    }//end situation 5
    else if(dueDate[job1] > (A + processingTime[job1]) && dueDate[job2] < (A + processingTime[job1] + processingTime[job2]) && dueDate[job2] <= (A + processingTime[job2])  && dueDate[job1] > (A + processingTime[job1] + processingTime[job2])){
      if(!AisLessThanToB(dueDate[job1], A + (processingTime[job1]*(1 - b)+ processingTime[job2]*b))){
        notStatisfied = true;
      }
    }//end situation 6
    else if(dueDate[job1] > (A + processingTime[job1]) && dueDate[job2] < (A + processingTime[job1] + processingTime[job2]) && dueDate[job2] > (A + processingTime[job2])  && dueDate[job1] > (A + processingTime[job1] + processingTime[job2])){
      if(!AisGreatThanToB(dueDate[job2], A + (processingTime[job1]*b + processingTime[job2]*(1 + a)))){
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

}