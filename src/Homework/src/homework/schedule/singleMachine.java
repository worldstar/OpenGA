package homework.schedule;

/**
 * <p>Title: The Dominance Properties for Single Machine Problem.</p>
 * <p>Description: We derive some optimal schedule properties for single machine problem without inserting idle time.
 * The Objective function we apply is earliness-tardiness. The further information can be obtained at
 * http://ppc.iem.yzu.edu.tw/publication/sourceCodes/SingleMachine/index.html</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Yuan-Ze University.</p>
 * @author V. Mani, Pei-Chann Chang, Shih Hsin Chen.
 * @email s939506@mail.yzu.edu.tw
 * @version 1.0
 */
public class singleMachine {
  public singleMachine() {
  }
  int numberOfJobs = 5;
  int dueDate[];
  int processingTime[];
  int finishTime[];
  int sequence[];
  int preSequence[];
  boolean blockIsChanged = false;
  boolean situation8Occured = false;
  int obj;
  int numberOfIterations = 1;

  public void setData(int numberOfJobs, int dueDate[], int processingTime[], int sequence[]){
    this.numberOfJobs = numberOfJobs;
    this.dueDate = dueDate;
    this.processingTime = processingTime;
    this.sequence = sequence;
    finishTime = new int[numberOfJobs];
    preSequence = new int[numberOfJobs];
  }

  public void setIterations(int numberOfIterations){
    this.numberOfIterations = numberOfIterations;
  }

  public final void sortSequence(int deviation[]){
    homework.util.sort.selectionSort selectionSort1 = new homework.util.sort.selectionSort();
    selectionSort1.setData(deviation);
    selectionSort1.setNomialData(sequence);
    selectionSort1.selectionSort_int_withNomial();
  }

  public void generateInitialSolution(int choice){
    if(choice == 0){
      generateInitialSolution();
    }
    else if(choice == 1){
      generateInitialSolution2();
    }
    else if(choice == 2){
      generateInitialSolution3();
    }
    else if(choice == 3){
      generateInitialSolution4();
    }
    else if(choice == 4){
      generateInitialSolution5();
    }
    else if(choice == 5){
      generateInitialSolution6();
    }
    else{
      generateRandomSolution();
    }
  }

  public void generateInitialSolution(){
    int array[] = new int[numberOfJobs];
    for(int i = 0 ; i < numberOfJobs ; i ++ ){
      array[i] = dueDate[i];
    }
    sortSequence(array);
  }

  public void generateInitialSolution2(){//Pj in ascending order
    int array[] = new int[numberOfJobs];
    for(int i = 0 ; i < numberOfJobs ; i ++ ){
      array[i] = processingTime[i];
    }
    sortSequence(array);
  }

  public void generateInitialSolution3(){//Pj in descending order
    int array[] = new int[numberOfJobs];
    for(int i = 0 ; i < numberOfJobs ; i ++ ){
      array[i] = -processingTime[i];
    }
    sortSequence(array);
  }

  public void generateInitialSolution4(){//Dj - Pj in ascending order
    int array[] = new int[numberOfJobs];
    for(int i = 0 ; i < numberOfJobs ; i ++ ){
      array[i] = dueDate[i] - processingTime[i];
    }
    sortSequence(array);
  }

  public void generateInitialSolution5(){//Pj - Dj  in ascending order
    int array[] = new int[numberOfJobs];
    for(int i = 0 ; i < numberOfJobs ; i ++ ){
      array[i] =  processingTime[i] - dueDate[i];
    }
    sortSequence(array);
  }

  public void generateInitialSolution6(){//Dj  in descending order
    int array[] = new int[numberOfJobs];
    for(int i = 0 ; i < numberOfJobs ; i ++ ){
      array[i] =  -dueDate[i];
    }
    sortSequence(array);
  }

  public void generateRandomSolution(){
    for(int i = 0 ; i < numberOfJobs ; i ++ ){
      int cutPoint = getCutPoint();
      swapJobs(i, cutPoint);
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
   * If the soln1 and soln2 are the same, it returns true.
   * @param soln1
   * @param soln2
   * @return
   */
  public boolean isTheSame(int soln1[], int soln2[]){
    boolean isTheSameSolution = true;
    for(int i = 0 ; i < numberOfJobs ; i ++ ){
      if(soln1[i] != soln2[i]){
        return false;
      }
    }
    return isTheSameSolution;
  }

  /**
   * The finish time of each job.
   * @param _seq
   */
  public void calcFinishTime(int _seq[]){
    int currentTime = 0;
    for(int i = 0 ; i < _seq.length ; i ++ ){
      finishTime[_seq[i]] = currentTime + processingTime[_seq[i]];
      currentTime = finishTime[_seq[i]];
    }
  }

  private int calcSumProcessingTime(int _seq[]){
    int currentTime = 0;
    for(int i = 0 ; i < _seq.length ; i ++ ){
      currentTime += processingTime[_seq[i]];
    }
    return currentTime;
  }

  /**
   * The earliness-tardiness penalty.
   * The weight vector for the both penalty is 1.
   */
  public void calcObj(){
    obj = 0;
    for(int i = 0 ; i < numberOfJobs ; i ++ ){
      obj += Math.abs(finishTime[sequence[i]] - dueDate[sequence[i]]);
    }
  }

  public boolean checkValidProblem(int _seq[]){
    int sumProcessingtime = calcSumProcessingTime(_seq);
    boolean isValid = true;
    for(int i = 0 ; i < _seq.length ; i ++ ){
      if(sumProcessingtime < dueDate[i]){
        isValid = false;
        break;
      }
    }
    return isValid;
  }

  /**
   * @param start Job1
   * @param end Job2
   */
  public void getTwoJobs(int start, int end, int pos1){
    int previousTime = 0;
    if(pos1 > 0){
      previousTime = finishTime[sequence[pos1 - 1]];
    }

    boolean notStatisfied = checkStatus(start, end, previousTime);
    //System.out.println("job "+(start+1)+" & "+(end+1) +": Shall Change? ->"+notStatisfied);
    if(notStatisfied == true){
      //System.out.println("finishTime1 "+finishTime[start]+", finishTime2 " +finishTime[end]);
      swapJobs(pos1);
      updateFinishTime(end, start, previousTime);
      //System.out.println("NewfinishTime1 "+finishTime[end]+", NewfinishTime2 " +finishTime[start]);
    }
  }

  /**The 9 dominance properties.
   * @param job1 The prior job.
   * @param job2 The later job.
   * @param A    The finishing time of the job ahead of job1 and job2.
   * @return     to return notStatisfied. If true, it means there is a condition violated.
   */
  private boolean checkStatus(int job1, int job2, int A){
    //System.out.println("job1= "+(job1+1)+", job2= "+(job2+1)+", p1= "+processingTime[job1]+", p2= "+processingTime[job2]+", A= "+A+", d1= "+dueDate[job1] + ", d2= "+dueDate[job2]);
    boolean notStatisfied = false;
    if(dueDate[job1] > (A + processingTime[job1]) && dueDate[job2] > (A + processingTime[job1] + processingTime[job2]) && dueDate[job2] > A + processingTime[job2]  && dueDate[job1] > (A + processingTime[job1] + processingTime[job2])){
      //System.out.println("Situation 1.");
      if(!AisGreatThanToB(processingTime[job1], processingTime[job2])){
        notStatisfied = true;
      }
    }//end situation 1
    else if(dueDate[job1] > (A + processingTime[job1]) && dueDate[job2] > (A + processingTime[job1] + processingTime[job2]) && dueDate[job2] > (A + processingTime[job2])  && dueDate[job1] < (A + processingTime[job1] + processingTime[job2])){
      if(!AisLessThanToB(dueDate[job1], A + (3*processingTime[job1] + processingTime[job2])/2)){
        notStatisfied = true;
      }
    }//end situation 2
    else if(dueDate[job1] < (A + processingTime[job1]) && dueDate[job2] < (A + processingTime[job1] + processingTime[job2]) && dueDate[job2] <= (A + processingTime[job2])  && dueDate[job1] < (A + processingTime[job1] + processingTime[job2])){
      if(!AisLessThanToB(processingTime[job1], processingTime[job2])){
        notStatisfied = true;
      }
    }//end situation 3
    else if(dueDate[job1] < (A + processingTime[job1]) && dueDate[job2] < (A + processingTime[job1] + processingTime[job2]) && dueDate[job2] >= (A + processingTime[job2])  && dueDate[job1] < (A + processingTime[job1] + processingTime[job2])){
      if(!AisGreatThanToB(dueDate[job2], A + (processingTime[job1] + processingTime[job2])/2)){
        notStatisfied = true;
      }
    }//end situation 4
    else if(dueDate[job1] > (A + processingTime[job1]) && dueDate[job2] < (A + processingTime[job1] + processingTime[job2]) && dueDate[job2] > (A + processingTime[job2])  && dueDate[job1] < (A + processingTime[job1] + processingTime[job2])){
      if(!AisGreatThanToB(dueDate[job2] - processingTime[job2]/2, dueDate[job1] - processingTime[job1]/2)){
        notStatisfied = true;
      }
    }//end situation 5
    else if(dueDate[job1] > (A + processingTime[job1]) && dueDate[job2] < (A + processingTime[job1] + processingTime[job2]) && dueDate[job2] <= (A + processingTime[job2])  && dueDate[job1] > (A + processingTime[job1] + processingTime[job2])){
      if(!AisLessThanToB(dueDate[job1], A + (processingTime[job1] + processingTime[job2])/2)){
        notStatisfied = true;
      }
    }//end situation 6
    else if(dueDate[job1] > (A + processingTime[job1]) && dueDate[job2] < (A + processingTime[job1] + processingTime[job2]) && dueDate[job2] > (A + processingTime[job2])  && dueDate[job1] > (A + processingTime[job1] + processingTime[job2])){
      if(!AisGreatThanToB(dueDate[job2], A + (processingTime[job1] + 3*processingTime[job2])/2)){
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

  boolean AisGreatThanOrEqualToB(int A, int B){
    if(A >= B){
      return true;
    }
    else{
      return false;
    }
  }

  public boolean AisGreatThanToB(int A, int B){
    if(A > B){
      return true;
    }
    else{
      return false;
    }
  }
  /*** For the value with double type   ***/
  public boolean AisGreatThanToB(double A, double B){
    if(A >= B){
      return true;
    }
    else{
      return false;
    }
  }

  boolean AisLessThanOrEqualToB(int A, int B){
    if(A <= B){
      return true;
    }
    else{
      return false;
    }
  }

  public boolean AisLessThanToB(int A, int B){
    if(A < B){
      return true;
    }
    else{
      return false;
    }
  }

  /*** For the value with double type   ***/
  public boolean AisLessThanToB(double A, double B){
    if(A < B){
      return true;
    }
    else{
      return false;
    }
  }

  public void swapJobs(int pos1){
    int pos2 = pos1 + 1;
    swapJobs(pos1, pos2);
  }

  public void swapJobs(int pos1, int pos2){
    int temp = sequence[pos1];
    sequence[pos1] = sequence[pos2];
    sequence[pos2] = temp;
  }

   /**
   * Update the finish time after we swap the two jobs.
   * @param pos1
   */
  public void updateFinishTime(int start, int end, int previousTime){
    finishTime[start] = previousTime + processingTime[start];
    finishTime[end] = finishTime[start] + processingTime[end];
  }

  public void setPreviousSolution(int _seq[]){
    for(int i = 0 ; i < _seq.length ; i ++ ){
      preSequence[i] = _seq[i];
    }
  }

  private int getCutPoint(){
    int cutPoint = (int)(Math.random()*sequence.length);
    int counter = 0;
    if(cutPoint == 0){
      cutPoint = 1;
    }
    else if(cutPoint == sequence.length - 1){
      cutPoint =  sequence.length - 2;
    }
    return cutPoint;
  }

  public int getObjValue(){
    return obj;
  }

  public int[] getSolution(){
    return sequence;
  }

  /*************************  Utility functions.  ****************************/
  public void checkTardyOrEarly(){
    for(int i = 0 ; i < numberOfJobs ; i ++ ){
      if(finishTime[sequence[i]] < dueDate[sequence[i]]){
        System.out.print("Job "+(sequence[i]+1)+": Early.\n");
      }
      else if(finishTime[sequence[i]] > dueDate[sequence[i]]){
        System.out.print("Job "+(sequence[i]+1)+": Tardy.\n");
      }
      else{
        System.out.print("Job "+(sequence[i]+1)+": On time.\n");
      }
    }
  }

  /**
   * Write the data into text file.
   */
  public void writeFile(String fileName, String _result){
    homework.util.fileWrite1 writeLotteryResult = new homework.util.fileWrite1();
    writeLotteryResult.writeToFile(_result,fileName+".txt");
    Thread thread1 = new Thread(writeLotteryResult);
    thread1.run();
  }

  public final void printSequence(){
    for(int i = 0 ; i < numberOfJobs ; i ++ ){
      System.out.print((sequence[i]+1)+" ");
    }
    System.out.println();
  }

  public void printAll(){
    for(int i = 0 ; i < numberOfJobs ; i ++ ){
      System.out.print((sequence[i]+1)+" ");
    }
    System.out.print("--> "+obj+"\n");
  }

  public static void main(String[] args) {
    singleMachine singleMachine1 = new singleMachine();
    singleMachineData singleMachineData1 = new singleMachineData();

    for(int replications = 0 ; replications < 30 ; replications ++ ){
      int jobSets[] = new int[]{20, 30, 40, 50};//20, 30, 40, 50, 100, 200
      for(int m = 0 ; m < jobSets.length ; m ++ ){//jobSets.length
        for(int k = 0 ; k < 1 ; k ++ ){
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
          homework.util.timeClock timeClock1 = new homework.util.timeClock();
          timeClock1.start();
          for(int i = 0 ; i < 100 ; i ++ ){//i initial solutions
            int sequence[] = new int[numberOfJobs];//5, 3, 2, 1, 4//2 1 4 6 3 5 8 7
            for(int j = 0 ; j < numberOfJobs ; j ++ ){
              sequence[j] = j;
            }
            singleMachine1.setData(numberOfJobs, dueDate, processingTime, sequence);
            singleMachine1.generateInitialSolution(i);
            singleMachine1.startAlgorithm();
            //to compare the objective value.
            if(obj > singleMachine1.getObjValue()){
              obj = singleMachine1.getObjValue();
              currentSoluion = singleMachine1.getSolution();
            }
          }
          timeClock1.end();
          String result = obj+"\t"+timeClock1.getExecutionTime()/1000.0+"\n";
          System.out.print(result);
          singleMachine1.writeFile("singleMachineProperties20060520", result);
        }
      }
    }//end replications.

/*
    //for the Baker's instance.
    int numberOfJobs = 8;
    for(int k = 5 ; k < 6 ; k ++ ){
      int jobIndex = k;
      int obj = Integer.MAX_VALUE;
      int dueDate[] = singleMachineData1.getDueDate(jobIndex);
      int processingTime[] = singleMachineData1.getPtime(jobIndex);
      int currentSoluion[] = new int[numberOfJobs];
      for(int i = 0 ; i < 10 ; i ++ ){
        int sequence[] = new int[]{6, 4, 7, 3, 2, 1, 0, 5};//5, 3, 2, 1, 4//0, 1, 2, 3, 4, 5, 6, 7//2 1 4 6 3 5 8 7
        for(int j = 0 ; j < numberOfJobs ; j ++ ){
          sequence[j] = j;
        }

        singleMachine1.setData(numberOfJobs, dueDate, processingTime, sequence);
        singleMachine1.generateInitialSolution(i);

        singleMachine1.startAlgorithm();
        //to compare the objective value.
        if(obj > singleMachine1.getObjValue()){
          obj = singleMachine1.getObjValue();
          currentSoluion = singleMachine1.getSolution();
        }
      }
      System.out.print((k+1)+":\t"+obj+"\n");

      for(int i = 0 ; i < numberOfJobs ; i ++ ){
        System.out.print((currentSoluion[i]+1)+" ");
      }
      System.out.print("\n\n");
    }
*/
  }//end main

}