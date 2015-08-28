/*
 * Multi-Parents crossover. Most crossover operators are two-parents crossover operators.
 * This multi-parents crossover is able to take 3, 4, ..., to popSize chromosomes.
 * It might be able to increase the population diversity.
 * This operator is based on order-based two-points crossover operator.
 */

package openga.operator.crossover;
import openga.chromosomes.*;
/**
 *
 * @author nhu
 */
public class multiParentsCrossover implements CrossoverI{
  public multiParentsCrossover() {
  }

  populationI originalPop, newPop;
  double crossoverRate;
  int popSize, chromosomeLength;
  int cutPoint1, cutPoint2;
  int pos1, pos2;
  int randomSequence[];          //The sequence of selecting a number of chromosomes.
  int numberOfParents = 4;       //The number of chromosomes.


  public void setData(double crossoverRate, chromosome _chromosomes[],int numberOfObjs){
    //transfomation the two chromosomes into a population.
    population _pop = new population();
    _pop.setGenotypeSizeAndLength(_chromosomes[0].getEncodeType(), _chromosomes.length, _chromosomes[0].getLength(), numberOfObjs);
    _pop.initNewPop();
    for(int i = 0 ; i < _chromosomes.length ; i ++ ){
      _pop.setChromosome(i, _chromosomes[i]);
    }
    setData(crossoverRate, _pop);
    generateRandomSequence();
  }

  public void setData(int numberOfParents){
      this.numberOfParents = numberOfParents;
  }

  public void setData(double crossoverRate, populationI originalPop){
    this.originalPop = originalPop;
    popSize = originalPop.getPopulationSize();
    //System.out.println("originalPop.getLengthOfChromosome() "+originalPop.getLengthOfChromosome());
    newPop = new population();
    newPop.setGenotypeSizeAndLength(originalPop.getEncodedType(), popSize, originalPop.getLengthOfChromosome(), originalPop.getNumberOfObjectives());
    newPop.initNewPop();

    for(int i = 0 ; i < popSize ; i ++ ){
      newPop.setSingleChromosome(i, originalPop.getSingleChromosome(i));
    }
    this.crossoverRate = crossoverRate;
    chromosomeLength = originalPop.getSingleChromosome(0).genes.length;
    generateRandomSequence();
  }

  public void generateRandomSequence(){
      randomSequence = new int[newPop.getPopulationSize()];
      for(int i = 0 ; i < newPop.getPopulationSize() ; i ++){
          this.randomSequence[i] = i;
      }

      for(int i = 0 ; i < newPop.getPopulationSize() ; i ++){
          swaptGenes(randomSequence, i, (int)(Math.random()*newPop.getPopulationSize()));
      }
  }
  //Maintain the sequence after a combination is used.
  public final int[] swaptGenes(int genes[], int pos1, int pos2){
    int backupGenes = genes[pos1];
    genes[pos1] = genes[pos2];
    genes[pos2] = backupGenes;
    return genes;
  }
  
  //start to crossover
  public void startCrossover(){
      //System.out.println("Start crossover ");
    for(int i = 0 ; i < popSize ; i ++ ){
        //System.out.println("Number of mated solution: "+i);
       //test the probability is larger than crossoverRate.
       if(Math.random() <= crossoverRate){
         //to get the other chromosomes to do crossover
         copyElements(getCrossoverChromosomeSet(i), numberOfParents);
       }
    }
  }

  

  /**
   * To get the other chromosomes to do crossover.
   * @param sourceParentID The index of the original chromosome.   
   */  
  public final int[] getCrossoverChromosomeSet(int sourceParentID){
    int sets[] = new int[numberOfParents];
    int index = 0;
    int originalIndex;

    for(int i = 0 ; i < newPop.getPopulationSize() ; i ++){
        if(randomSequence[i] == sourceParentID){
            index = i;
            break;
        }
    }

    originalIndex = index;

    for(int i = 0 ; i < numberOfParents ; i ++){
        //System.out.println("Index is :"+index);
        sets[i] = randomSequence[index];//To get its next neighbor.
        index ++;

        if(index >= randomSequence.length - 2){
            index = 0;
        }
    }

    //shuffle the sequences, so the combination won't be shown again.
    for(int i = 0 ; i < numberOfParents ; i ++){
          swaptGenes(randomSequence, originalIndex, (int)(Math.random()*randomSequence.length));
          originalIndex ++;
        if(originalIndex >= randomSequence.length - 2){
            originalIndex = 0;
        }
    }      

    return sets;
  }

  /**
   * The numberOfParents chromosomes produce a new offspring
   * @param indexes A set of chromosomes to be mated
   * @param numberOfParents How many chromosomes are involved in this mating process
   */
  private void copyElements(int[] indexes, int numberOfParents){
    //openga.util.printClass printClass1 = new openga.util.printClass();
    //printClass1.printMatrix("Selected indexes", indexes);
    
    cutPoint1 = this.chromosomeLength/numberOfParents;             //Starting point for copying a chromosome
    cutPoint2 = cutPoint1 + this.chromosomeLength/numberOfParents; //Stopping point for copying a chromosome
    
    for(int i = 1 ; i < indexes.length; i ++){
        int counter = 0;

        if(i == indexes.length - 1){//The last parent
            cutPoint2 = this.chromosomeLength ;
        }
        //System.out.println("Cutpoints: "+this.cutPoint1+" "+this.cutPoint2);
        for(int j = cutPoint1 ; j < cutPoint2 ; j ++){
            try{
              while(checkConflict(newPop.getSingleChromosome(indexes[i]).genes[counter], newPop.getSingleChromosome(indexes[0]).genes, j) == true){
                counter ++;
              }
            }
            catch(Exception e){//Debugging
                System.out.println("Current pos: "+j+" Cutpoints: "+this.cutPoint1+" "+this.cutPoint2);
                openga.util.printClass printClass1 = new openga.util.printClass();
                printClass1.printMatrix("Counter: "+counter+"", newPop.getSingleChromosome(indexes[0]).genes);
                printClass1.printMatrix("Counter: "+counter+"", newPop.getSingleChromosome(indexes[i]).genes);
            }

          newPop.setGene(indexes[0], j, newPop.getSingleChromosome(indexes[i]).genes[counter]);
          counter ++;
        }
        cutPoint1 = cutPoint2 ;
        cutPoint2 += this.chromosomeLength/numberOfParents;
        counter = 0;
    }
  }

  /**
   * if there is the same gene, it return the index of the gene.
   * Else, default value is -1, which is also mean don't have the same gene
   * during cutpoint1 and cutpoint2.
   * @param newGene
   * @param _chromosome
   * @return
   */
  private boolean checkConflict(int newGene, int _chromosome[], int position){
    boolean hasConflict = false;
    for(int i = 0 ; i < position ; i ++ ){
      if(newGene == _chromosome[i]){
        return true;
      }
    }
    return hasConflict;
  }

  public final populationI getCrossoverResult(){
    return newPop;
  }
/*
  //for test only
  public static void main(String[] args) {
    CrossoverI twoPointCrossover21 = new multiParentsCrossover();
    openga.util.printClass printClass1 = new openga.util.printClass();
    populationI population1 = new population();
    populationI newPop = new population();
    int size = 200, length = 50;

    population1.setGenotypeSizeAndLength(true, size, length, 2);
    population1.createNewPop();
    for(int i = 0 ; i < size ; i ++ ){
       printClass1.printMatrix(""+i,population1.getSingleChromosome(i).genes);
    }


    twoPointCrossover21.setData(0.9, population1);
    twoPointCrossover21.startCrossover();
    newPop = twoPointCrossover21.getCrossoverResult();
    System.out.println("3-parents crossover results.");
    for(int i = 0 ; i < size ; i ++ ){
       printClass1.printMatrix(""+i,newPop.getSingleChromosome(i).genes);
    }
  }
*/

    @Override
    public void setData(int numberOfJob, int numberOfMachines, int[][] processingTime) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
