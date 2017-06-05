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
 * @author YU-TANG CHANG
 */
public class localSearchByIG extends localSearchBy2Opt implements localSearchMTSPI{
  
  int numberofSalesmen;
  
  public void startLocalSearch() {
    System.out.print("startLocalSearch\n");
    int selectedIndex = getBestIndex(archive);
    populationI _pop = new population();//to store the temp chromosome
    _pop.setGenotypeSizeAndLength(pop.getEncodedType(), 3, pop.getLengthOfChromosome(),
            pop.getNumberOfObjectives());
    _pop.initNewPop();

    _pop.setChromosome(0, archive.getSingleChromosome(selectedIndex));//Set the original solution to the 1st chromosome (best).
    currentUsedSolution = 0;
    IG_ls(_pop);
    _pop.setChromosome(0, _pop.getSingleChromosome(0));
    _pop.setObjectiveValue(0, _pop.getSingleChromosome(0).getObjValue());
    currentUsedSolution++;
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
    updateArchive(_pop.getSingleChromosome(0)); //update the solution in the elite set.
  }

  public final void IG_ls(populationI _sp) {    //
    chromosomeLength = _sp.getSingleChromosome(0).genes.length;
    int numberofCity = chromosomeLength-numberofSalesmen;
    List<Integer> destructedPart = new ArrayList<Integer>();
    List<Integer> reservePart = new ArrayList<Integer>();
    List<Integer> salesmenPart = new ArrayList<Integer>();
    
    evaluateNewSoln(_sp.getSingleChromosome(0));
    double originalObjValue = _sp.getSingleChromosome(0).getObjValue()[0];
    System.out.println("originalObjValue : "+originalObjValue);
/* Print SingleChromosome.
    System.out.print("getSingleChromosome : ");
    for(int i=0; i<_sp.getSingleChromosome(0).genes.length; i++){
      System.out.print(_sp.getSingleChromosome(0).genes[i]+" ");
    }
    System.out.println("End");
//*/

    for(int i=0; i<numberofCity; i++){
      reservePart.add(_sp.getSingleChromosome(0).genes[i]);
    }
    for(int i=numberofCity; i<chromosomeLength; i++){
      salesmenPart.add(_sp.getSingleChromosome(0).genes[i]);
    }
    setdestructedPart(reservePart,destructedPart,salesmenPart);
    
/*//Print reserve genes,destructed genes,salesmen genes.
    System.out.print("reservePart : ");
    for(int i=0; i<reservePart.size(); i++){
      System.out.print(reservePart.get(i)+" ");
    }
    System.out.println("End");
    
    System.out.print("destructedPart : ");
    for(int i=0; i<destructedPart.size(); i++){
      System.out.print(destructedPart.get(i)+" ");
    }
    System.out.println("End");
    
    System.out.print("salesmenPart : ");
    for(int i=0; i<salesmenPart.size(); i++){
      System.out.print(salesmenPart.get(i)+" ");
    }
    System.out.println("End");
//*/
    
//insertPoint : number of insert position
    int insertPoint = reservePart.size()+1;
//    System.out.println("insertPoint : "+insertPoint);

    List<Integer> tmpPart = new ArrayList<Integer>();
    tmpPart.addAll(reservePart);
    tmpPart.addAll(salesmenPart);
    double tmpObjValue;
//Best genes
    List<Integer> lsPart = new ArrayList<Integer>();
    lsPart.addAll(reservePart);
    lsPart.addAll(salesmenPart);
    double lsObjValue = 0;
    
    for(int i=0; i<maxNeighborhood; i++){
//    add destructedPart gene and initialize Chromosome then calculate objectivefunction
      lsPart.add(0,destructedPart.get(i));
      lsPart.set(lsPart.size()-salesmenPart.size(), lsPart.get(lsPart.size()-salesmenPart.size())+1);
      
//      System.out.print("lsPart(before) : ");
//      for(int j=0; j<lsPart.size(); j++){
//      System.out.print(lsPart.get(j)+" ");
//      }
//      System.out.println("End");
      
      chromosome lsChromosome = new chromosome();
      lsChromosome.setGenotypeAndLength(true, lsPart.size(), 1);
      lsChromosome.setSolution(lsPart);
      evaluateNewSoln(lsChromosome);
      lsObjValue = lsChromosome.getObjValue()[0];
      
//      System.out.println("lsObjValue(before)"+":"+lsObjValue);
//      System.out.println();
      
      for(int j=1; j<insertPoint; j++){
        tmpPart.add(j,destructedPart.get(i)); 
        tmpPart.set(tmpPart.size()-salesmenPart.size(), (lsPart.get(tmpPart.size()-salesmenPart.size())));
        
        chromosome tmpChromosome = new chromosome();
        tmpChromosome.setGenotypeAndLength(true, tmpPart.size(), 1);
        tmpChromosome.setSolution(tmpPart);
        evaluateNewSoln(tmpChromosome);
        tmpObjValue = tmpChromosome.getObjValue()[0];

        if(tmpObjValue>=lsObjValue){
          lsObjValue = tmpObjValue;
          lsPart.clear();
          lsPart.addAll(tmpPart);
          tmpPart.remove(j);
        }else{
          tmpPart.remove(j);
        }
      }
      tmpPart.clear();
      tmpPart.addAll(lsPart);
      insertPoint++;
    }
    System.out.println("lsObjValue"+":"+lsObjValue);
    
    if(lsObjValue > originalObjValue){
      _sp.getSingleChromosome(0).setSolution(lsPart);
      evaluateNewSoln(_sp.getSingleChromosome(0));
      double localsearchObj = _sp.getSingleChromosome(0).getObjValue()[0];
      System.out.println("localsearchObj : "+localsearchObj);
    }
  }
  
  public final void setdestructedPart(List<Integer> reservePart, List<Integer> destructedPart, List<Integer> salesmenPart) {
    int cities = chromosomeLength-salesmenPart.size();
    int[] Destructgenes = new int[numberofSalesmen];
    int numberofDestructgenes = maxNeighborhood;
    
//    System.out.print("number of Destructgenes : ");
    for(int i=0; i<Destructgenes.length-1; i++){
      Destructgenes[i] = (int)Math.round(((double)(maxNeighborhood*salesmenPart.get(i))/cities));
      numberofDestructgenes -= Destructgenes[i];
//      System.out.print(Destructgenes[i]+" ");
    }
    Destructgenes[numberofSalesmen-1] = numberofDestructgenes;
//    System.out.print(Destructgenes[numberofSalesmen-1]+" ");
//    System.out.println("End");
    
    int currentPosition = 0;
    for(int i=0; i<Destructgenes.length; i++){
//      System.out.println("i"+":"+i);
      int frequency = Destructgenes[i];
//      System.out.println("Destructfrequency"+":"+frequency);
      for(int j=0; j<frequency; j++){
//        System.out.println("j"+":"+j);
        int tmp;
        tmp = new Random().nextInt(salesmenPart.get(i))+currentPosition;
//        System.out.println("tmp"+":"+tmp);
        destructedPart.add(reservePart.get(tmp));
        reservePart.remove(reservePart.get(tmp));
        salesmenPart.set(i, (salesmenPart.get(i)-1));
        Destructgenes[i]--;
      }
      currentPosition += salesmenPart.get(i);
//      System.out.println("END");
    }
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
//    System.out.println("evaluateNewSoln");
    for (int k = 0; k < ObjectiveFunction.length; k++) {      
      ObjectiveFunction[k].setData(chromosome1, numberofSalesmen);
      chromosome1.getObjValue();
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
