/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package openga.operator.crossover;
import openga.chromosomes.*;
import openga.ObjectiveFunctions.*;
/**
 *
 * @author chuan
 */
public class inverOverOperator extends twoPointCrossover2{

  ObjectiveFunctionI ObjectiveFunction[];
  public int inversionsCounts = 0;
  double bestObj = Double.MAX_VALUE;

  public void setObjectives(ObjectiveFunctionI ObjectiveFunction[]){
    this.ObjectiveFunction = ObjectiveFunction;
  }
  /*
  public chromosome evaluateNewSoln(chromosome chromosome1){
    //calculate its objective
    for(int k = 0 ; k < ObjectiveFunction.length ; k ++ ){
      ObjectiveFunction[k].setData(chromosome1, k);
      ObjectiveFunction[k].calcObjective();
    }
    return chromosome1;
  }
  */
  public chromosome evaluateNewSoln(chromosome chromosome1){
    populationI _pop = new population();//to store the temp chromosome
    _pop.setGenotypeSizeAndLength(newPop.getEncodedType(), 1, newPop.getLengthOfChromosome(),
                                  newPop.getNumberOfObjectives());
    _pop.initNewPop();
    _pop.setChromosome(0, chromosome1);//Set the original solution to the 1st chromosome.
    _pop = evaluateNewSoln(_pop);
    return _pop.getSingleChromosome(0);
  }
  
  public populationI evaluateNewSoln(populationI _pop){
    //calculate its objective
    for(int k = 0 ; k < ObjectiveFunction.length ; k ++ ){
      ObjectiveFunction[k].setData(_pop, k);
      ObjectiveFunction[k].calcObjective();
      _pop = ObjectiveFunction[k].getPopulation();
    }
    return _pop;
  }

  //start to crossover
  public void startCrossover(){
    evaluateNewSoln(newPop);
    boolean continueIteration = true;
    int iterationCount = 0;
    int umImprovements = 0;
    inversionsCounts = 0;//reset

    while(continueIteration || umImprovements < 10){
        System.out.println("\n\nIteration: "+iterationCount++);
        continueIteration = inverOverCore();
        double popBestObjective = 0;

        if(iterationCount == 1){
            bestObj = popBestObjective = getPopBestObjective();
        }

        if(bestObj > popBestObjective){
            bestObj = popBestObjective;
            umImprovements = 0;
        }
        else{
            umImprovements ++;
        }
    }
  }

  private boolean inverOverCore(){
    boolean continueIteration = true;
    
    for(int i = 0 ; i < popSize ; i ++ ){        
        setCutpoint();
        chromosome chromosome1 = newPop.getSingleChromosome(i);
        int c1 = chromosome1.genes[cutPoint1];
        int cs = chromosome1.genes[cutPoint1+1];
        int csPosition = cutPoint1+1;

        chromosome chromosomeTemp = newPop.getSingleChromosome(i);

        openga.util.printClass printClass1 = new openga.util.printClass();
        //printClass1.printMatrix("P"+(i+1), chromosome1.genes);

       //test the probability is larger than crossoverRate.
       if(Math.random() <= crossoverRate){
          int cePosition = cutPoint2;
          System.out.printf("(Type: Direct Inverse) C1: %d, Cs: %d, Ce: %d \n", c1, cs, newPop.getSingleChromosome(i).genes[cePosition]);
          chromosomeTemp = inverseGenes(newPop.getSingleChromosome(i), csPosition, cePosition);
       }
       else{
         //to get the other chromosome to crossover
         int index2 = getCrossoverChromosome(i);
         //printClass1.printMatrix("P"+index2, newPop.getSingleChromosome(index2).genes);
         //int index2 = 1;
         int ce = findCityEndOnP2(c1, index2);
         int cePosition = findCityEndPositionOnP1(ce, i);

         if(cs == ce){//csPosition == cePosition - 1, cs == ce
             System.out.printf("(Type: Stop) C1: %d, Cs: %d, Ce: %d \n", c1, cs, ce);
             continueIteration = false;
             break;
         }
         else{
            //swap position
            if(csPosition > cePosition){
              int temp = csPosition;
              csPosition = cePosition;
              cePosition = temp;
            }
            System.out.printf("(Type: Inver-Over) C1: %d, Cs: %d, Ce: %d \n", c1, cs, ce);
            chromosomeTemp = inverseGenes(newPop.getSingleChromosome(i), csPosition, cePosition);
            inversionsCounts ++;
         }
       }
        //should check the fitness value and then replace into the newPop
        //printClass1.printMatrix("Child "+(i+1), chromosomeTemp.genes);
        chromosomeTemp = evaluateNewSoln(chromosomeTemp);
        //System.out.printf("Child: %f, Parent: %f \n", chromosomeTemp.getObjValue()[0], newPop.getSingleChromosome(i).getObjValue()[0]);
        if(chromosomeTemp.getObjValue()[0] < newPop.getSingleChromosome(i).getObjValue()[0]){
            newPop.setSingleChromosome(i, chromosomeTemp);
            //printClass1.printMatrix("Child "+(i+1)+"-->It is better! ", newPop.getSingleChromosome(i).genes);
        }
         else{
            //printClass1.printMatrix("Child "+(i+1)+"-->Should not change! ", newPop.getSingleChromosome(i).genes);
         }        
        
    }
    return continueIteration;
  }

  private int findCityEndOnP2(int c1, int index2){
    int ce = 0;

    for(int i = 0 ; i < chromosomeLength; i ++ ){
      if(newPop.getSingleChromosome(index2).genes[i] == c1){
        if(i < chromosomeLength - 1){//Not the last city
            ce = newPop.getSingleChromosome(index2).genes[i+1];
        }
         else{//To the first city
            ce = newPop.getSingleChromosome(index2).genes[0];
         }
      }
    }
    return ce;
  }

  private int findCityEndPositionOnP1(int ce, int index1){
    for(int i = 0 ; i < chromosomeLength; i ++ ){
      if(newPop.getSingleChromosome(index1).genes[i] == ce){
        return i;
      }
    }
    return 0;
  }

  public final chromosome inverseGenes(chromosome _chromosome, int cutPoint1, int cutPoint2){
    int length = cutPoint2 - cutPoint1  + 1;
    int backupGenes[] = new int[length];
    int counter = 0;

    //store the genes at backupGenes.
    for(int i = cutPoint1 ; i <= cutPoint2 ; i ++ ){
      backupGenes[counter++] = _chromosome.genes[i];
    }

    counter = 0;
    //write data of backupGenes into the genes
    for(int i = cutPoint2; i >= cutPoint1 ; i --){
      _chromosome.genes[i] = backupGenes[counter++];
    }
    return _chromosome;
  }

  public double getPopBestObjective(){
    double popBestObj = Double.MAX_VALUE;

    for(int i = 0 ; i < popSize ; i ++ ){
       if(popBestObj > newPop.getSingleChromosome(i).getObjValue()[0]){
            popBestObj = newPop.getSingleChromosome(i).getObjValue()[0];
       }
    }
    return popBestObj;
  }

  public static void main(String[] args) {
    int instanceNumber = 2;
    openga.applications.data.TSPInstances TSPInstances1 = new openga.applications.data.TSPInstances();
    TSPInstances1.setData(TSPInstances1.getFileName(instanceNumber));
    TSPInstances1.getDataFromFile();
    String instanceName = TSPInstances1.getFileName(instanceNumber);
    TSPInstances1.calcEuclideanDistanceMatrix();
    int length = TSPInstances1.getSize();

    inverOverOperator twoPointCrossover1 = new inverOverOperator();
    populationI population1 = new population();
    int size = 100;
    population1.setGenotypeSizeAndLength(true, size, length, 1);
    population1.createNewPop();
    openga.util.printClass printClass1 = new openga.util.printClass();

    ObjectiveFunctionTSPI[] ObjectiveFunction = new ObjectiveFunctionTSPI[1];
    ObjectiveFunction[0] = new ObjectiveFunctionTSP();//the first objective
    ObjectiveFunction[0].setTSPData(TSPInstances1.getOriginalPoint(), TSPInstances1.getDistanceMatrix());

    for(int i = 0 ; i < size ; i ++ ){
       //printClass1.printMatrix(""+i,population1.getSingleChromosome(i).genes);
    }

    //printClass1.printMatrix("TSPInstances1.getOriginalPoint()", TSPInstances1.getOriginalPoint());
    //printClass1.printMatrix("TSPInstances1.getDistanceMatrix()", TSPInstances1.getDistanceMatrix());

    twoPointCrossover1.setData(0.02, population1);
    twoPointCrossover1.setObjectives(ObjectiveFunction);
    twoPointCrossover1.startCrossover();
    population1 = twoPointCrossover1.getCrossoverResult();

    for(int i = 0 ; i < size ; i ++ ){
       //printClass1.printMatrix(""+i,population1.getSingleChromosome(i).genes);
    }
    
    double bestObj = Double.MAX_VALUE;

    for(int i = 0 ; i < size ; i ++ ){
       if(bestObj > population1.getSingleChromosome(i).getObjValue()[0]){
            bestObj = population1.getSingleChromosome(i).getObjValue()[0];
       }
    }
     System.out.printf("bestObj: %f inversionsCounts %d\n", bestObj, twoPointCrossover1.inversionsCounts);
  }
}
