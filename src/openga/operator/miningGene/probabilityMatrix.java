package openga.operator.miningGene;
import openga.chromosomes.*;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: The basis of ACGA. Please refer to
 * Chang, P. C., Chen, S. H., & Fan, C. Y. (2008), Mining gene structures to inject artificial chromosomes for genetic algorithm in single machine scheduling problems, Applied Soft Computing Journal, 8(1), 767-777.</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class probabilityMatrix extends geneStatistics{
  public probabilityMatrix() {
  }
  int popSize, chromosomeLength;
  double container[][];//it's an m-by-m array to store the gene results. The i is job, the j is the position(sequence).
  double MarkovContainer[][];
  int newSolution[];
  double avgFitness = 0, maxFitness = 0, minFitness;
  double duplicateRate = 1.0;
  int numCopied;
  int strategy = 1;
  int assignSequence[];
  boolean applyEvaporation = false;
  String evaporationMethod = "constant";//constant, method1, method2
  double currentBest;
  boolean currentBestIsSet = false;
  double currentMin, currentMax;
  boolean currentMinMaxIsSet = false;

  //to restore information
  double backupcontainer[][];

  //openga.util.printClass p1 = new openga.util.printClass();

  public void setData(populationI originalPop,
                                            populationI duplicatedPopulation){
    this.originalPop = originalPop;
    this.duplicatedPopulation = duplicatedPopulation;
    popSize = originalPop.getPopulationSize();
    chromosomeLength = originalPop.getSingleChromosome(0).genes.length;
    container = new double[chromosomeLength][chromosomeLength];
  }

  public void setDuplicateRate(double duplicateRate){
    this.duplicateRate = duplicateRate;
    numCopied = (int)(duplicateRate * duplicatedPopulation.getPopulationSize());
  }

  public void setStrategy(int strategy){
    this.strategy = strategy;
  }

  public void setEvaporationMethod(boolean applyEvaporation, String evaporationMethod){
    this.applyEvaporation = applyEvaporation;
    this.evaporationMethod = evaporationMethod;
  }

  public void clearMatrix(){
    for(int i = 0 ; i < container.length ; i ++ ){
      for(int j = 0 ; j < container[i].length ; j ++ ){
        container[i][j] = 0.0;
      }
    }
  }

  public void startStatistics(){
    //clearMatrix();
    /*
    for(int i = 0 ; i < originalPop.getPopulationSize() ; i ++ ){
      p1.printMatrix("originalPop "+i, originalPop.getSingleChromosome(i).getSolution());
    }
    */
    foundemental();
    assignJobs(container);
  }

  public void foundemental(){
    calcAverageFitness();
    calcContainer();
    //squareProbabilityMatrix();
    assignSequence = new int[chromosomeLength];
  }

  /**
   * The average fitness.
   */
  public void calcAverageFitness(){
    openga.Fitness.calcAverageFitness calcAverageFitness1 = new openga.Fitness.calcAverageFitness();
    calcAverageFitness1.setData(originalPop);
    calcAverageFitness1.startCalcFitness();
    avgFitness = calcAverageFitness1.getcalcFitness();
    avgFitness *= 1.2;
  }

  public void calcContainer(){
    //to collect gene information.
    //System.out.println(avgFitness);
    int counter = 0;
    for(int i = 0 ; i < popSize ; i ++ ){
      if(originalPop.getFitness(i) <= avgFitness){
         for(int j = 0 ; j < chromosomeLength ; j ++ ){
           int gene = originalPop.getSingleChromosome(i).getSolution()[j];
           container[gene][j] += 1;
         }
         counter ++;
      }
    }

    //to normalize them between [0, 1].
    for(int i = 0 ; i < chromosomeLength ; i ++ ){
      for(int j = 0 ; j < chromosomeLength ; j ++ ){
        container[i][j] /= counter;
      }
    }
  }

  public void squareProbabilityMatrix(){
    openga.util.algorithm.arrayMultiple arrayMultiple1 = new openga.util.algorithm.arrayMultiple();
    MarkovContainer = arrayMultiple1.calcArray(container, container);
    //p1.printMatrix("sqaured container", MarkovContainer);
  }

  public void assignJobs(double matrix[][]){
    //To assign jobs onto the chromosome in a population
    for(int i = 0 ; i < numCopied ; i ++ ){//start numCopied duplicatedPopulation.getPopulationSize()
      double temporaryContainer[][];

      temporaryContainer = dumpArray(container);
      /*
      if(i < numCopied/2){
        temporaryContainer = dumpArray(container);
      }
      else{
        temporaryContainer = dumpArray(MarkovContainer);
      }
      */

      generateChromosome(temporaryContainer, duplicatedPopulation.getSingleChromosome(i));
      //p1.printMatrix(""+i, duplicatedPopulation.getSingleChromosome(i).getSolution());
    }
    //System.exit(0);
  }

  public double[][] dumpArray(double _container[][]){
    double temporaryContainer[][];
    temporaryContainer = new double[chromosomeLength][chromosomeLength];
    for(int i = 0 ; i < _container.length ; i ++ ){
      for(int j = 0 ; j < _container.length ; j ++ ){
        temporaryContainer[i][j] = _container[i][j];
      }
    }
    return temporaryContainer;
  }

  public void generateChromosome(double temporaryContainer[][], chromosome solution){
    //strategy
    assignmentSequence(strategy, assignSequence);
    for(int j = 0 ; j < chromosomeLength ; j++){
      int pos = assignSequence[j];
      normArray(temporaryContainer, pos);
      int jobIndex = selectJob(temporaryContainer, pos); //to get a job by RWS.

      //System.out.print(j+" "+jobIndex+" ");
      solution.setGeneValue(pos, jobIndex); //to set the job into the ith chromosome at position j
      temporaryContainer = dropColumn(pos, temporaryContainer);
      temporaryContainer = dropRow(jobIndex, temporaryContainer);
      //p1.printMatrix("\ndrop container", temporaryContainer);
      //to use evaporation after we assign a job to a position.

      if(applyEvaporation == true){
        evaporation(j, jobIndex);
      }
    }
  }

  public int selectJob(double matrix[][], int columnIndex){
    int job = 0;
    double AccProb[] = new double[chromosomeLength];
    double prob = Math.random();
    double buff = 0.0;
    double SumFitness = 0.0;

    for(int i = 0 ; i < chromosomeLength ; i++){    //To calculate accumulative probability.
      if(matrix[i][columnIndex] != -1){
        buff += matrix[i][columnIndex];
        AccProb[i] = buff;
      }
      else{
        AccProb[i] = 0;
      }
    }

    if(buff == 0){
      reAssign(matrix, columnIndex, AccProb);
    }

    for(int j = 0 ; j < chromosomeLength ; j++){    //To check what is the region it belongs to.
      if(prob <= AccProb[j]){
        job = j;
        break;
      }
    }
    return job;
  }

  public double[][] dropColumn(int index, double matrix[][]){
    for(int i = 0 ; i < matrix.length ; i ++ ){
      matrix[i][index] = -1;
    }
    return matrix;
  }

  public double[][] dropRow(int index, double matrix[][]){
    for(int i = 0 ; i < matrix.length ; i ++ ){
      matrix[index][i] = -1;
    }
    return matrix;
  }

  public void reAssign(double matrix[][], int columnIndex, double AccProb[]){
    double buff = 0;
    for(int i = 0 ; i < chromosomeLength ; i++){
      if(matrix[i][columnIndex] != -1){
        AccProb[i] = Math.random();
        buff += AccProb[i];
      }
      else{
        AccProb[i] = 0;
      }
    }

    for(int i = 0 ; i < chromosomeLength ; i++){
      if(matrix[i][columnIndex] != -1){
        AccProb[i] /= buff;
        //System.out.print(AccProb[j]+" ");
      }
    }
    //System.exit(0);
  }

  public void normArray(double matrix[][], int columnIndex){
    double SumFitness = 0;
    for(int i = 0 ; i < chromosomeLength ; i++){    //To calculate accumulative probability.
      if(matrix[i][columnIndex] != -1){
        SumFitness += matrix[i][columnIndex];
      }
    }

    if(SumFitness == 0){
      for(int i = 0 ; i < chromosomeLength ; i++){    //To calculate accumulative probability.
        if(matrix[i][columnIndex] != -1){
          matrix[i][columnIndex] = Math.random();
          SumFitness += matrix[i][columnIndex];
        }
      }
    }

    for(int i = 0 ; i < chromosomeLength ; i++){
      if(matrix[i][columnIndex] != -1){
        matrix[i][columnIndex] /= SumFitness;
      }
    }
  }

  public void assignmentSequence(int strategy, int sequence[]){
    if(strategy == 0){//sequential assign
      for(int i = 0 ; i < sequence.length ; i ++ ){
        sequence[i] = i;
      }
    }
    else if(strategy == 1){//random assign
      randomSequence(sequence);
    }
    else{//assign by maximum vote and random assign.
      if(Math.random() < 0.1){
        maximumVoteSequence(sequence);
      }
      else{
        randomSequence(sequence);
      }
    }
  }

  public void randomSequence(int genes[]){
    for(int i = 0 ; i < chromosomeLength ; i ++ ){
      genes[i] = i;
    }

    for(int i = 0 ; i < chromosomeLength ; i ++ ){
      //swap
      int temp1 = genes[i];
      //int indexTemp = (int)(numberofGenes*r.nextDouble());
      int indexTemp = (int)(chromosomeLength*Math.random());
      int temp2 = genes[indexTemp];
      genes[indexTemp] = temp1;
      genes[i] = temp2;
    }
  }

  public void maximumVoteSequence(int genes[]){
    double temporaryContainer[][] = dumpArray();
    for(int i = 0 ; i < chromosomeLength ; i ++ ){
      int indexes[] = getMaxNumber(temporaryContainer);
      genes[i] = indexes[1];
      dropRow(indexes[0], temporaryContainer);
      dropColumn(indexes[1], temporaryContainer);
    }
  }

  private int[] getMaxNumber(double matrix[][]){
    int indexes[] = new int[2];//to record i and j
    double temp = 0;
    for(int i = 0 ; i < matrix.length ; i ++ ){
      for(int j = 0 ; j < matrix.length ; j ++ ){
        if(temp < matrix[i][j]){
          temp = matrix[i][j];
          indexes[0] = i;
          indexes[1] = j;
        }
      }
    }
    return indexes;
  }

  public void evaporation(int columIndex, int rowIndex){
    if(evaporationMethod == "constant"){
      container[columIndex][rowIndex] -= container[columIndex][rowIndex]*0.05;
    }
    else if(evaporationMethod == "method1"){
      double tho = 0.05;
      if(currentBestIsSet == false){
        currentBest = getBest();
        currentBestIsSet = true;
      }
      container[columIndex][rowIndex] = container[columIndex][rowIndex]*(1.0-tho)+tho*(1/currentBest);
    }
    else if(evaporationMethod == "Threshold"){
      double tho = 0.05;
      if(container[columIndex][rowIndex] >= 1/chromosomeLength){
        container[columIndex][rowIndex] -= container[columIndex][rowIndex]*tho;
      }
    }
    else if(evaporationMethod == "add"){
      double tho = 0.05;
      if(container[columIndex][rowIndex] + container[columIndex][rowIndex]*tho <= 1.0){
        container[columIndex][rowIndex] += container[columIndex][rowIndex]*tho;
      }
    }
    else if(evaporationMethod == "method2"){
      double tho = 0.05;
      if(currentMinMaxIsSet == false){
        setMinMax();
        currentMinMaxIsSet = true;
      }
      //System.out.print("container[columIndex][rowIndex] "+container[columIndex][rowIndex]);
      container[columIndex][rowIndex] = container[columIndex][rowIndex]*(1.0-tho)+tho*(1/(currentMax-currentMin));
      //System.out.println("\t"+container[columIndex][rowIndex]);
      //System.exit(0);
    }

  }

  public double[][] backupInformation(){
    calcAverageFitness();
    calcContainer();
    return container;
  }

  public double[][] getContainer(){
    return container;
  }

  public double[][] getMarkovContainer(){
    return MarkovContainer;
  }

  public double getBest(){
    double _obj = Double.MAX_VALUE;
    for(int i = 0 ; i < originalPop.getPopulationSize() ; i ++ ){
      if(_obj > originalPop.getObjectiveValueArray()[i][0]){
        _obj = originalPop.getObjectiveValueArray()[i][0];
      }
    }
    return _obj;
  }

  public void setMinMax(){
    currentMin = Double.MAX_VALUE;
    currentMax = 0;

    for(int i = 0 ; i < originalPop.getPopulationSize() ; i ++ ){
      if(currentMin > originalPop.getObjectiveValueArray()[i][0]){
        currentMin = originalPop.getObjectiveValueArray()[i][0];
      }
      if(currentMax < originalPop.getObjectiveValueArray()[i][0]){
        currentMax = originalPop.getObjectiveValueArray()[i][0];
      }
    }
  }



}
