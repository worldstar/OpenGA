package homework.schedule;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class flowshop extends singleMachine{
  public flowshop() {
  }
  int numberOfJobs = 5;
  int dueDate[];
  int processingTime[][];
  int finishTime[][];
  int sequence[];
  int preSequence[];
  int obj;
  int numberOfIterations = 1;
  //for flowshop
  int sumProcessingTime[];
  int LbIdleTimeForJob[][];
  int numberOfMachine = 2;

  public void setData(int numberOfJobs, int numberOfMachine, int dueDate[], int processingTime[][], int sequence[]){
    this.numberOfJobs = numberOfJobs;
    this.numberOfMachine = numberOfMachine;
    this.dueDate = dueDate;
    this.processingTime = processingTime;
    this.sequence = sequence;
    finishTime = new int[numberOfJobs][numberOfMachine];
    preSequence = new int[numberOfJobs];
    sumProcessingTime = new int[numberOfJobs];
    LbIdleTimeForJob = new int[numberOfJobs][numberOfJobs];
  }

  /**
   * To calculate the lower bound idle time for job I which is ahead of job J.
   */
  public void calcLowerBoundIdleTime(){
    for(int i = 0 ; i < numberOfJobs ; i ++ ){
      for(int j = 0 ; j < numberOfJobs ; j ++ ){
        if(i != j){
          LbIdleTimeForJob[i][j] = iBeforeJIdleTime(i, j);
        }
      }
    }
  }

  /**
   * When we only have two jobs which are job I and Job J,
   * to compute the lowerbound idle time that cause by job I to job J.
   * @param jobI The previous job
   * @param jobJ The later job
   * @return The lowerbound idle time of job J when Job I is before job J.
   */
  private int iBeforeJIdleTime(int jobI, int jobJ){
    int idleTime = 0, _currentTime = 0;
    int _finishTime[] = new int[numberOfJobs];
    //assign the job i to each machine.
    for(int i = 0 ; i < numberOfJobs ; i ++ ){
      _finishTime[i] = _currentTime + processingTime[jobI][i];
      _currentTime = _finishTime[i];
    }
    //assign the job j to each machine
    _currentTime = _finishTime[0];
    for(int i = 0 ; i < numberOfJobs ; i ++ ){
      if(_currentTime >= _finishTime[i]){
        _currentTime += processingTime[jobJ][i];
      }
      else{
        idleTime = _finishTime[i] - _currentTime;
        _currentTime = _finishTime[i] + processingTime[jobJ][i];
      }
    }
    return idleTime;
  }

  /**
   * The summation processing time of each job on all machines.
   */
  public void calcSumProcessingTime(){
    for(int i = 0 ; i < numberOfJobs ; i ++ ){
      sumProcessingTime[i] = 0;
      for(int j = 0 ; j < numberOfMachine ; j ++ ){
        sumProcessingTime[i] += processingTime[i][j];
      }
    }
  }

  private void calcCompletionTime(int genes[]){
    int[] machineTime = new int[numberOfMachine];
    double objVal = 0;
    double tempTardiness = 0;
    int length = 5;
    int completionTime[] = new int[numberOfJobs];


   //assign each job to each machine depended on the current machine time.
    for(int i = 0 ; i < length ; i ++ ){
      int index = genes[i];
      for(int j = 0 ; j < numberOfMachine ; j ++ ){
        //the starting time is the completion time of last job on first machine
        if(j == 0){
          machineTime[j] += processingTime[index][j];
        }
        else{
          if(machineTime[j - 1] < machineTime[j]){//previous job on the machine j is not finished
            machineTime[j] = machineTime[j] + processingTime[index][j];
          }
          else{//the starting time is the completion time of last machine
            machineTime[j] = machineTime[j - 1] + processingTime[index][j];
          }
        }
      }

      //check whether the completion time of the job i is late or not.
      if(machineTime[numberOfMachine - 1]  > dueDate[index]){
        tempTardiness = (machineTime[numberOfMachine - 1] - dueDate[index]);
      }
      else{
        tempTardiness = 0;
      }
      //System.out.println("job "+index+" machineTime "+machineTime[numberOfMachine-1]+" due "+dueDay[index]+" tempTardiness "+tempTardiness);
      //find out the max tardiness which is compared to previous jobs.
      if(tempTardiness > objVal){
        objVal = tempTardiness;
      }
      completionTime[index] = machineTime[numberOfMachine - 1];
    }
  }

  public void startAlgorithm(){
    if(numberOfIterations == 1){
      numberOfIterations = numberOfJobs*numberOfJobs;
    }
    else{
      numberOfIterations = (int)(numberOfJobs*Math.log(numberOfJobs));
    }

    setPreviousSolution(sequence);   //to set the initial solution as the previous soln.
    int counter = 0;

    do{
      counter ++;
      calcFinishTime(sequence);      //the finish time of the schedule
      for(int pos1 = 0 ; pos1 < 2 ; pos1 ++ ){
        for(int i = pos1 ; i < numberOfJobs - 1 ; i ++ ){
          getTwoJobs(sequence[i], sequence[i+1], i);
          i += 1;
        }
      }

      //if the solution is still the same, it means the algorithm won't change the sequence anymore.
      if(isTheSame(sequence, preSequence)){
        break;
      }
      else{
        setPreviousSolution(sequence);
      }
    }
    while(counter < numberOfIterations);//the other stopping criterion is the number of iterations.Math.log(numberOfJobs)
    calcObj();
  }

  /**
   * @param start Job1
   * @param end Job2
   */
  public void getTwoJobs(int start, int end, int pos1){
    int previousTime = 0;
    if(pos1 > 0){
      previousTime = processingTime[sequence[pos1 - 1]][0];
    }

    boolean notStatisfied = false;//checkStatus(start, end, previousTime);
    //System.out.println("job "+(start+1)+" & "+(end+1) +": Shall Change? ->"+notStatisfied);
    if(notStatisfied == true){
      //System.out.println("finishTime1 "+finishTime[start]+", finishTime2 " +finishTime[end]);
      swapJobs(pos1);
      //updateFinishTime(end, start, previousTime);
      //System.out.println("NewfinishTime1 "+finishTime[end]+", NewfinishTime2 " +finishTime[start]);
    }
  }

  /**
   * The lowerbound finish time of each job.
   * @param _seq
   */
  public void calcLowerBoundFinishTime(int _seq[]){
    int currentTime = 0; //the finishTime on machine 1.
    for(int i = 0 ; i < _seq.length ; i ++ ){
      if(i != 0){
        currentTime += processingTime[_seq[i-1]][0];
      }
      finishTime[_seq[i]][numberOfMachine - 1] = calcLowerBoundFinishTime(i, _seq, currentTime);
    }
  }

  /**
   * The lowerbound finish time of each job.
   * @param _seq
   */
  public int calcLowerBoundFinishTime(int position, int _seq[], int A){
    if(position == 0){
      return sumProcessingTime[_seq[position]];
    }
    else{
      return (A + sumProcessingTime[_seq[position]] + LbIdleTimeForJob[_seq[position-1]][_seq[position]]);
    }
  }

  public int calcLowerBoundFinishTime(int position, int prev_job, int job, int A){
    if(position == 0){
      return sumProcessingTime[job];
    }
    else{
      return (A + sumProcessingTime[job] + LbIdleTimeForJob[prev_job][job]);
    }
  }

  /**The 9 dominance properties.
   * @param job1 The prior job.
   * @param job2 The later job.
   * @param A    The finishing time of the job ahead of job1 and job2.
   * @return     to return notStatisfied. If true, it means there is a condition violated.
   */
/*
  private boolean checkStatus(int job1, int job2, int A, int position, int _seq[]){
    //System.out.println("job1= "+(job1+1)+", job2= "+(job2+1)+", p1= "+processingTime[job1]+", p2= "+processingTime[job2]+", A= "+A+", d1= "+dueDate[job1] + ", d2= "+dueDate[job2]);
    int Ci = calcLowerBoundFinishTime(position, _seq, A);
    int Cj = calcLowerBoundFinishTime(position+1, _seq, A + processingTime[job1][0]);
    int Cj1 = calcLowerBoundFinishTime(position, _seq, A);
    int Ci2 = calcLowerBoundFinishTime(position+1, _seq, A + processingTime[job2][0]);

    boolean notStatisfied = false;
    if(dueDate[job1] > Ci && dueDate[job2] > Cj && dueDate[job2] > Cj1  && dueDate[job1] > (Ci2)){
      //System.out.println("Situation 1.");
      if(!AisGreatThanToB(processingTime[job1], processingTime[job2])){
        notStatisfied = true;
      }
    }//end situation 1
    else if(dueDate[job1] > Ci && dueDate[job2] > Cj && dueDate[job2] > Cj1  && dueDate[job1] < (Ci2)){
      if(!AisLessThanToB(dueDate[job1], A + (3*processingTime[job1] + processingTime[job2])/2)){
        notStatisfied = true;
      }
    }//end situation 2
    else if(dueDate[job1] < Ci && dueDate[job2] < Cj && dueDate[job2] <= Cj1  && dueDate[job1] < (Ci2)){
      if(!AisLessThanToB(processingTime[job1], processingTime[job2])){
        notStatisfied = true;
      }
    }//end situation 3
    else if(dueDate[job1] < Ci && dueDate[job2] < Cj && dueDate[job2] >= Cj1  && dueDate[job1] < (Ci2)){
      if(!AisGreatThanToB(dueDate[job2], A + (processingTime[job1] + processingTime[job2])/2)){
        notStatisfied = true;
      }
    }//end situation 4
    else if(dueDate[job1] > Ci && dueDate[job2] < Cj && dueDate[job2] > Cj1  && dueDate[job1] < (Ci2)){
      if(!AisGreatThanToB(dueDate[job2] - processingTime[job2]/2, dueDate[job1] - processingTime[job1]/2)){
        notStatisfied = true;
      }
    }//end situation 5
    else if(dueDate[job1] > Ci && dueDate[job2] < Cj && dueDate[job2] <= Cj1  && dueDate[job1] > (Ci2)){
      if(!AisLessThanToB(dueDate[job1], A + (processingTime[job1] + processingTime[job2])/2)){
        notStatisfied = true;
      }
    }//end situation 6
    else if(dueDate[job1] > Ci && dueDate[job2] < Cj && dueDate[job2] > Cj1  && dueDate[job1] > (Ci2)){
      if(!AisGreatThanToB(dueDate[job2], A + (processingTime[job1] + 3*processingTime[job2])/2)){
        notStatisfied = true;
      }
    }//end situation 7
    else if(dueDate[job1] < Ci && dueDate[job2] > Cj && dueDate[job2] > Cj1  && dueDate[job1] < (Ci2)){
      //System.out.println("Situation 8.");//Do nothing. Job 1 is always ahead of job 2.
    }//end situation 8
    else{
      //System.out.println("Situation 9. Always swap solutions.");
      notStatisfied = true;
    }

    return notStatisfied;
  }
*/
   public static void main(String[] args) {
     int numberOfJobs = 5;
     int dueDay[] = new int[]{36, 105, 72, 39, 28};
     int processingTime[][];
     int completionTime[] = new int[numberOfJobs];
     processingTime = new int[][]{
         {12, 5},
         {15, 12},
         {15, 18},
         {1, 8},
         {19, 14}};

   }
}