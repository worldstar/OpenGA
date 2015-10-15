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
public class inverOverOperatorEDA2 extends twoPointCrossover2EDA2{  
  //Normal functions
  ObjectiveFunctionI ObjectiveFunction[];
  public int inversionsCounts = 0;
  double bestObj = Double.MAX_VALUE;
  int totalSolnsToExamine;

  public void setEDAinfo(double container[][], double inter[][], int numberOfTournament) {
      this.container = container;
      this.inter = inter;
      this.numberOfTournament = numberOfTournament;
  }
  
  public void setObjectives(ObjectiveFunctionI ObjectiveFunction[]){
    this.ObjectiveFunction = ObjectiveFunction;
  }

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
    for (int i = 0; i < 2; i++) {
        newChromosomes[i] = new chromosome();
        newChromosomes[i].setGenotypeAndLength(originalPop.getEncodedType(), originalPop.getLengthOfChromosome(), originalPop.getNumberOfObjectives());
        newChromosomes[i].initChromosome();
    }    
    
    for (int i = 0; i < popSize ; i++) {
        //test the probability is larger than crossoverRate.
        if (Math.random() <= crossoverRate) {
            //to get the other chromosome to crossover
            setCutpoint();
            checkCutPoints(i);
        }
    }    
  }
  
  public void checkCutPoints(int selectedSoln) {
      if (numberOfTournament == 0) {
          System.out.println("numberOfTournament is at least 1.");
          System.exit(0);
      }

      double probabilitySum;
      double maxProb = 0.0;

      for (int i = 0; i < numberOfTournament; i++) {
          int index2 = getCrossoverChromosome(selectedSoln);//to get a chromosome to be mated.
          inverOverCore(originalPop.getSingleChromosome(selectedSoln), originalPop.getSingleChromosome(index2), newChromosomes[0]);

          if (numberOfTournament == 1) {//it needs not to collect the gene information
              probabilitySum = 10.0;
          } 
          else{
              probabilitySum = sumGeneInfo(newChromosomes[0], cutPoint1, cutPoint2);
          }

          if (maxProb < probabilitySum){
              maxProb = probabilitySum;
              newPop.setSingleChromosome(selectedSoln, newChromosomes[0]);
              //newPop.setSingleChromosome(index2, newChromosomes[1]);
          }
      }         
      //System.exit(0);
  }   

  private boolean inverOverCore(chromosome chromosome1, chromosome chromosome2, chromosome child1){
    boolean continueIteration = true;
    
    for(int i = 0 ; i < popSize ; i ++ ){        
        setCutpoint();
        int c1 = chromosome1.genes[cutPoint1];
        int cs = chromosome1.genes[cutPoint1+1];
        int csPosition = cutPoint1+1;

       //test the probability is larger than crossoverRate.
       if(Math.random() <= crossoverRate){
          int cePosition = cutPoint2;
          //System.out.printf("(Type: Direct Inverse) C1: %d, Cs: %d, Ce: %d \n", c1, cs, newPop.getSingleChromosome(i).genes[cePosition]);
          child1 = inverseGenes(chromosome1, csPosition, cePosition);
       }
       else{
         int ce = findCityEndOnP2(c1, chromosome2);
         int cePosition = findCityEndPositionOnP1(ce, i);

         if(cs == ce){//csPosition == cePosition - 1, cs == ce
             //System.out.printf("(Type: Stop) C1: %d, Cs: %d, Ce: %d \n", c1, cs, ce);
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
            //System.out.printf("(Type: Inver-Over) C1: %d, Cs: %d, Ce: %d \n", c1, cs, ce);
            child1 = inverseGenes(chromosome1, csPosition, cePosition);
            inversionsCounts ++;
         }
       }               
    }
    return continueIteration;
  }

  private int findCityEndOnP2(int c1, chromosome chromosome2){
    int ce = 0;

    for(int i = 0 ; i < chromosomeLength; i ++ ){
      if(chromosome2.genes[i] == c1){
        if(i < chromosomeLength - 1){//Not the last city
            ce = chromosome2.genes[i+1];
        }
         else{//To the first city
            ce = chromosome2.genes[0];
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
}
