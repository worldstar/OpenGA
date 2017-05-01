/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package openga.operator.localSearch;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import openga.ObjectiveFunctions.ObjectiveFunctionI;
import openga.chromosomes.chromosome;
import openga.chromosomes.population;
import openga.chromosomes.populationI;

/**
 *
 * @author Administrator
 */
public class localSearchByIG extends localSearchBy2Opt implements localSearchMTSPI{
  
  int numberofSalesmen;
  
  public void startLocalSearch() {
    System.out.print("startLocalSearch\n");
    
    boolean compareResult;
    int selectedIndex = getBestIndex(archive);
    int iteration = 0;
    populationI _pop = new population();//to store the temp chromosome
    _pop.setGenotypeSizeAndLength(pop.getEncodedType(), 3, pop.getLengthOfChromosome(),
            pop.getNumberOfObjectives());
    _pop.initNewPop();

    _pop.setChromosome(0, archive.getSingleChromosome(selectedIndex));//Set the original solution to the 1st chromosome (best).
    _pop.setChromosome(1, _pop.getSingleChromosome(0));  //chromosome current
    _pop.setChromosome(2, _pop.getSingleChromosome(0));  //temp best
    currentUsedSolution = 0;
    
    IG_ls(_pop);
    
//    System.out.println("-----current best result-----------");
//    System.out.println(iteration + ":" + _pop.getSingleChromosome(0).toString1() + "->" + _pop.getSingleChromosome(0).getObjValue()[0] + " used:" + currentUsedSolution);
//
//    System.out.println("-----IG result-----------");
//    System.out.println(_pop.getSingleChromosome(0).toString1() + "->" + _pop.getSingleChromosome(0).getObjValue()[0] + " used:" + currentUsedSolution);
//        System.exit(0);
    
//    do {
//      do {
//        IG_ls(_pop);
//        _pop.setChromosome(1, _pop.getSingleChromosome(2));
//        _pop.setObjectiveValue(1, _pop.getObjectiveValues(2));
//
//      } while (_pop.getSingleChromosome(1).getObjValue()[0] > _pop.getSingleChromosome(0).getObjValue()[0]);
//
//      if (_pop.getSingleChromosome(1).getObjValue()[0] > _pop.getSingleChromosome(0).getObjValue()[0]) {
//        _pop.setChromosome(0, _pop.getSingleChromosome(1));
//        _pop.setObjectiveValue(0, _pop.getObjectiveValues(1));
//        iteration = 0;
//      } else {
//        _pop.setChromosome(1, _pop.getSingleChromosome(0));
//        _pop.setObjectiveValue(1, _pop.getObjectiveValues(0));
//        iteration++;
//      }
//
//      System.out.println("-----current best result-----------");
//      System.out.println(iteration + ":" + _pop.getSingleChromosome(0).toString1() + "->" + _pop.getSingleChromosome(0).getObjValue()[0] + " used:" + currentUsedSolution);
//
//            _pop.setChromosome(1, kickMove(_pop.getSingleChromosome(1)));//To variate the current best solution.
//      _pop.setChromosome(2, _pop.getSingleChromosome(1));
//
//    } while (iteration < 10);   // 50  200  totalTimes continueLocalSearch()   _pop.getSingleChromosome(0).getLength()
//
//    System.out.println("-----IG result-----------");
//    System.out.println(_pop.getSingleChromosome(0).toString1() + "->" + _pop.getSingleChromosome(0).getObjValue()[0] + " used:" + currentUsedSolution);
//        System.exit(0);
//    pop.setChromosome(selectedIndex, _pop.getSingleChromosome(0));
//    pop.setObjectiveValue(selectedIndex, _pop.getObjectiveValues(0));
//    updateArchive(_pop.getSingleChromosome(0)); //update the solution in the elite set.
  }

  public final populationI IG_ls(populationI _sp) {
    int count = 0;
    boolean compareResult;
    
    chromosomeLength = _sp.getSingleChromosome(0).genes.length;
//    System.out.println("chromosomeLength"+chromosomeLength);
    int numberofCity = chromosomeLength-numberofSalesmen;
//    System.out.println("numberofCity"+numberofCity);
    
    List<Integer> destructedPart = new ArrayList<Integer>();
    List<Integer> reservePart = new ArrayList<Integer>();
    
    for(int i=0; i<numberofCity; i++){
      reservePart.add(_sp.getSingleChromosome(0).genes[i]);
//      System.out.println("getSingleChromosome1"+i+":"+_sp.getSingleChromosome(0).genes[i]);
//      System.out.println("getSingleChromosome"+i+":"+reservePart.get(i));
    }
    
    for(int i=0; i<maxNeighborhood; i++){
      int tmp = new Random().nextInt(reservePart.size());
      destructedPart.add(reservePart.get(tmp));
      reservePart.remove(reservePart.get(tmp));
    }
    
//    for(int i=0; i<reservePart.size(); i++){
//      System.out.println("reservePart"+i+":"+reservePart.get(i));
//    }
//    
//    for(int i=0; i<destructedPart.size(); i++){
//      System.out.println("destructed"+i+":"+destructedPart.get(i));
//    }
    
    int reservechromosomeLength = reservePart.size();
    chromosome reserveChromosome = new chromosome();
    reserveChromosome.setGenotypeAndLength(true, reservechromosomeLength, 1);
    reserveChromosome.initChromosome();
    
    for(int i=0; i<reservechromosomeLength; i++){
      reserveChromosome.genes[i] = reservePart.get(i);
    }
    evaluateNewSoln(reserveChromosome);
    
    List<Integer> lsPart = new ArrayList<Integer>();
    int insertPoint = reservechromosomeLength+1;
    double tmpObjValue;
    
    double lsObjValue = reserveChromosome.getObjValue()[0];
    List<Integer> tmpPart = new ArrayList<Integer>();
    for(int i=0; i<reservechromosomeLength; i++){
      tmpPart.add(reservePart.get(i));
    }
    
    for(int i=0; i<maxNeighborhood; i++){
      for(int j=0; j<insertPoint; j++){
//        System.out.println("insertPoint"+":"+insertPoint);
//        System.out.println("i"+":"+i);
        System.out.println("j"+":"+j);
//        System.out.println("destructedPart"+":"+destructedPart.get(i));

        if(j<tmpPart.size()){
          tmpPart.add(j,destructedPart.get(i));
        }else{
          tmpPart.add(destructedPart.get(i));
        }
        System.out.print("tmpPart:");
        for(int k=0; k<tmpPart.size();k++){
          System.out.print(tmpPart.get(k));
        }
        System.out.println();
        
        int tmpchromosomeLength = tmpPart.size();
        chromosome tmpChromosome = new chromosome();
        tmpChromosome.setGenotypeAndLength(true, tmpchromosomeLength, 1);
        tmpChromosome.initChromosome();
        for(int k=0; k<tmpchromosomeLength; k++){
          tmpChromosome.genes[k] = tmpPart.get(k);
        }
        evaluateNewSoln(tmpChromosome);
        tmpObjValue = tmpChromosome.getObjValue()[0];
        System.out.println("tmpObjValue"+":"+tmpObjValue);
        
        if(tmpObjValue>=lsObjValue){//> OR >=?
          lsObjValue = tmpObjValue;
          lsPart.clear();
          for(int k=0; k<tmpPart.size(); k++){
            lsPart.add(k,tmpPart.get(k));
          }
          tmpPart.remove(j);
        }else{
          tmpPart.remove(j);
        }
      }
      
      tmpPart.clear();
      for(int k=0; k<lsPart.size(); k++){
            tmpPart.add(k,lsPart.get(k));
      }
      System.out.print("lsPart:");
        for(int k=0; k<lsPart.size();k++){
          System.out.print(lsPart.get(k));
        }
        System.out.println();
        
      insertPoint++;
    }
    
    for(int i=0; i<reservePart.size()+1; i++){
      for(int j=0; j<maxNeighborhood; j++){
        
      }
    }
    

    
//    chromosome tmpChromosome = new chromosome();
//    tmpChromosome.setGenotypeAndLength(true, tmpchromosomeLength, 1);
//    tmpChromosome.initChromosome();
//    evaluateNewSoln(tmpChromosome);
//    double result = _sp.getSingleChromosome(0).getObjValue()[0];
//    double result = tmpChromosome.getObjValue()[0];
//    System.out.println("result"+":"+result);
    
//    do {
//    }while (_sp.getSingleChromosome(0).getObjValue()[0]>_sp.getSingleChromosome(1).getObjValue()[0]);
    

//    for (int i = 0; i < chromosomeLength; i++) {
//      _sp.setChromosome(1, iterateGreedyAlgorithm(_sp.getSingleChromosome(1), 3));
//      evaluateNewSoln(_sp);
//      currentUsedSolution++;
//      if (_sp.getSingleChromosome(1).getObjValue()[0] > _sp.getSingleChromosome(2).getObjValue()[0]) {
//        _sp.setChromosome(2, _sp.getSingleChromosome(1));
//        _sp.setObjectiveValue(2, _sp.getObjectiveValues(1));
//        count = 0;
//        //                   System.out.println("changed");
//      } else {
//        _sp.setChromosome(1, _sp.getSingleChromosome(2));
//        _sp.setObjectiveValue(1, _sp.getObjectiveValues(2));
//      }
//      count++;
//    }
    return _sp;
  }

  public final chromosome iterateGreedyAlgorithm(chromosome _chromosome, int number) {
    //Destruction phase
//    int[] destruction = new int[number];
//    
//    for(int i=0; i<destruction.length; i++){
//      destruction[i] = new Random().nextInt(_chromosome.genes.length);
//    }
//    
    
    //set random
    int takeout, putin;
    Random random = new Random();

    ArrayList savevalue = new ArrayList();
    ArrayList list = new ArrayList();
    for (int i = 0; i < _chromosome.genes.length; i++) {
      list.add(_chromosome.genes[i]);
    }
    
    for (int count = 0; count < number; count++) {
      //takeout random
      takeout = random.nextInt(list.size());
      //save removed value
      savevalue.add(list.get(takeout));
      //remove random list
      list.remove(takeout);
    }
    putin = random.nextInt(list.size());
    for (int i = 0; i < savevalue.size(); i++) {
      list.add(putin, savevalue.get(i));
    }

    for (int i = 0; i < _chromosome.genes.length; i++) {
      _chromosome.genes[i] = (int) list.get(i);
    }
    return _chromosome;
  }

  public boolean getObjcomparison(double _obj1[], double _obj2[]) {
    double objectiveWeightSum1 = 0;
    double objectiveWeightSum2 = 0;
    double weight[] = new double[_obj1.length];
    double sum = 0;

    for (int i = 0; i < _obj1.length; i++) {
      weight[i] = Math.random();
      sum += weight[i];
    }

    for (int i = 0; i < _obj1.length; i++) {
      weight[i] /= sum;
    }

    for (int i = 0; i < _obj1.length; i++) {
      objectiveWeightSum1 += _obj1[i] * weight[i];
      objectiveWeightSum2 += _obj2[i] * weight[i];
    }
    if (objectiveWeightSum1 > objectiveWeightSum2) {
      return true;
    } else {
      return false;
    }
  }

  public int getBestIndex(populationI arch1) {
    int index = 0;
    double bestobj = Double.MAX_VALUE;

    for (int k = 0; k < arch1.getPopulationSize(); k++) {
      if (bestobj > arch1.getObjectiveValues(k)[0]) {
        bestobj = arch1.getObjectiveValues(k)[0];
        index = k;
      }
    }
    return index;
  }

  @Override
  public chromosome evaluateNewSoln(chromosome chromosome1) {
    //calculate its objective 
//    System.out.println("evaluateNewSoln");
    for (int k = 0; k < ObjectiveFunction.length; k++) {
      ObjectiveFunction[k].setData(chromosome1, numberofSalesmen);
      ObjectiveFunction[k].calcObjective();
      chromosome1.setObjValue(ObjectiveFunction[k].getObjectiveValues(k));
    }
    return chromosome1;
  }

  @Override
  public void setObjectives(ObjectiveFunctionI ObjectiveFunction[]) {
    this.ObjectiveFunction = ObjectiveFunction;
  }

  @Override
  public void setNumberofSalesmen(int numberofSalesmen) {
    this.numberofSalesmen = numberofSalesmen;
  }
}
