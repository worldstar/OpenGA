package openga.operator.localSearch;
import openga.chromosomes.*;
import openga.ObjectiveFunctions.*;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class localSearchBy2Opt implements localSearchI{
  public localSearchBy2Opt() {
  }

  public populationI pop;
  public populationI archive;
  ObjectiveFunctionI ObjectiveFunction[];
  public int popSize, chromosomeLength;  //size of population and number of digits of chromosome
  public int cutPoint1, cutPoint2;       //the genes between the two points are inversed
  int totalExaminedSolution;             //the stopping criteria
  //to record how many solutions are used. currentUsedSolution <= totalExaminedSolution
  int currentUsedSolution;
  int maxNeighborhood = 5;  //A default value of the maximum neighbors to search.
  int selectedIndex;
  int totalTimes = 2;

  public void setData(populationI population1, int totalExaminedSolution, int maxNeighborhood){
    pop = new population();
    this.pop = population1;
    this.totalExaminedSolution = totalExaminedSolution;
    this.maxNeighborhood = maxNeighborhood;
    popSize = population1.getPopulationSize();
    chromosomeLength = population1.getSingleChromosome(0).genes.length;
  }

  public void setData(populationI population1, populationI archive, int currentUsedSolution){
    pop = new population();
    this.pop = population1;
    this.archive = archive;
    this.currentUsedSolution = currentUsedSolution;
    popSize = population1.getPopulationSize();
    chromosomeLength = population1.getSingleChromosome(0).genes.length;
  }
  
    public void setData(populationI population1, populationI archive, int currentUsedSolution, int iter) {
        pop = new population();
        this.pop = population1;
        this.archive = archive;
        this.currentUsedSolution = currentUsedSolution;
        popSize = population1.getPopulationSize();
        chromosomeLength = population1.getSingleChromosome(0).genes.length;
        this.totalTimes = iter;
    }  

  public void setObjectives(ObjectiveFunctionI ObjectiveFunction[]){
    this.ObjectiveFunction = ObjectiveFunction;
  }

  public void setEDAinfo(double container[][]){
    System.out.println("This class openga.operator.localSearch.localSearchBy2Opt doesn't implement the setEDAinfo(double container[][]).");
    System.out.println("The program stops here.");
    System.exit(0);
  }

  /**
   * @return The index of the best solution in the population.
   */

  public int getTargetChromosome(){
    selectedIndex = 0;
    double minFitness = Double.MAX_VALUE;

    for(int i = 0 ; i < popSize ; i ++ ){
      double indFitness = pop.getSingleChromosome(i).getFitnessValue();
      if(indFitness < minFitness){
        minFitness = indFitness;
        selectedIndex = i;
      }
    }
    return selectedIndex;
  }

  public chromosome evaluateNewSoln(chromosome chromosome1){
    //calculate its objective
    for(int k = 0 ; k < ObjectiveFunction.length ; k ++ ){
      ObjectiveFunction[k].setData(chromosome1, k);
      ObjectiveFunction[k].calcObjective();
    }
    return chromosome1;
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

  public void startLocalSearch(){
    int selectedIndex = getTargetChromosome();

    populationI _pop = new population();//to store the temp chromosome
    _pop.setGenotypeSizeAndLength(pop.getEncodedType(), 2, pop.getLengthOfChromosome(),
                                  pop.getNumberOfObjectives());
    _pop.initNewPop();

    _pop.setChromosome(0, pop.getSingleChromosome(selectedIndex));//Set the original solution to the 1st chromosome.
    kickMove(_pop.getSingleChromosome(0));//To variate the current best solution.

    for(int i = 0 ; i < chromosomeLength - maxNeighborhood ; i ++ ){
      for(int j = i + 1 ; j < i + maxNeighborhood ; j ++ ){
        if(continueLocalSearch()){
          cutPoint1 = i;
          cutPoint2 = j;
          _pop.setChromosome(1, _pop.getSingleChromosome(0));
          //local search by 2-Opt (inverse mutation)
          inverseGenes(_pop.getSingleChromosome(1));
          evaluateNewSoln(_pop.getSingleChromosome(1));
          currentUsedSolution ++;

          boolean compareResult = getObjcomparison(_pop.getObjectiveValues(1), pop.getObjectiveValues(selectedIndex));
          if(compareResult == true){//If the new solution is better, we replace the original solution.
            pop.setChromosome(selectedIndex, _pop.getSingleChromosome(1));
            pop.setObjectiveValue(selectedIndex, _pop.getObjectiveValues(1));
            _pop.setChromosome(0, _pop.getSingleChromosome(1));
            _pop.setObjectiveValue(0, _pop.getObjectiveValues(1));
          }
        }
        else{
          break;
        }
      }
    }

    updateArchive(pop.getSingleChromosome(selectedIndex)); //update the solution in the elite set.
  }//end startLocalSearch

  public void kickMove(chromosome chromosome1){//The standard procedure of Iterative Local Search.
    setCutpoint();
    inverseGenes(chromosome1);
  }

  public final void setCutpoint(){
    boolean theSame = true;
    cutPoint1 = (int)(Math.random() * chromosomeLength);
    cutPoint2 = (int)(Math.random() * chromosomeLength);

    if(cutPoint1 == cutPoint2){
      cutPoint1 -=  (int)(Math.random()*cutPoint1);
      //increase the position of cutPoint2
      cutPoint2 += (int)((chromosomeLength - cutPoint2)*Math.random());

      //double check it.
      if(cutPoint1 == cutPoint2){
        //setCutpoint();
      }
    }

    //swap
    if(cutPoint1 > cutPoint2){
      int temp = cutPoint2;
      cutPoint2 = cutPoint1;
      cutPoint1 = temp;
    }
  }

  public final chromosome inverseGenes(chromosome _chromosome){
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


  public populationI getLocalSearchResult(){
    return pop;
  }

  /**
   * If obj[i] < obj[j], then return true.
   * @param _obj1 the first obj
   * @param _obj2 the second obj
   * @param index where to start
   * @return
   */
  public boolean getObjcomparison(double _obj1[], double _obj2[]){
   double objectiveWeightSum1 = 0 ;
   double objectiveWeightSum2 = 0 ;
   double weight[] = new double[_obj1.length];
   double sum = 0;

   for(int i = 0 ; i < _obj1.length ; i ++ ){
     weight[i] = Math.random();
     sum += weight[i];
   }

   for(int i = 0 ; i < _obj1.length ; i ++ ){
     weight[i] /= sum;
   }


   for(int i = 0 ; i < _obj1.length ; i ++ ){
     objectiveWeightSum1 += _obj1[i]*weight[i];
     objectiveWeightSum2 += _obj2[i]*weight[i];
   }
   if(objectiveWeightSum1 < objectiveWeightSum2){
     return true;
   }
   else
     return false;
  }

  public boolean continueLocalSearch(){//currentUsedSolution should be less than totalExaminedSolution
    if(currentUsedSolution < totalExaminedSolution){
      return true;
    }
    else{
      return false;
    }
  }

  public void updateArchive(chromosome chromosome1){
    for(int i = 0 ; i < archive.getPopulationSize() ; i ++ ){
      if(archive.getFitness(i) > chromosome1.getFitnessValue()){
        archive.setChromosome(i, chromosome1);
        archive.setObjectiveValue(i, chromosome1.getObjValue());
        archive.setFitness(i, chromosome1.getFitnessValue());
        break;
      }
    }
  }

  public int getCurrentUsedSolution(){
    return currentUsedSolution;
  }
}