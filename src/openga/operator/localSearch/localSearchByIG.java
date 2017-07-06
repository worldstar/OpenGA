package openga.operator.localSearch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import openga.chromosomes.chromosome;
import openga.chromosomes.population;
import openga.chromosomes.populationI;

/**
 *
 * @author YU-TANG CHANG
 */
public class localSearchByIG extends localSearchBy2Opt implements localSearchMTSPI {

  int numberofSalesmen;

  @Override
  public void startLocalSearch() {
    selectedIndex = getBestIndex(archive);
    populationI _pop = new population();//to store the temp chromosome
    _pop.setGenotypeSizeAndLength(pop.getEncodedType(), 1, pop.getLengthOfChromosome(),
            pop.getNumberOfObjectives());
    _pop.initNewPop();
    _pop.setChromosome(0, archive.getSingleChromosome(selectedIndex));//Set the original solution to the 1st chromosome (best).
    evaluateNewSoln(_pop.getSingleChromosome(0));

    double ObjValue = _pop.getSingleChromosome(0).getObjValue()[0];
    double lsObjValue = IG_ls(_pop);

    if (lsObjValue > ObjValue) {
      _pop.setChromosome(0, _pop.getSingleChromosome(0));
      _pop.setObjectiveValue(0, _pop.getSingleChromosome(0).getObjValue());
      updateArchive(_pop.getSingleChromosome(0)); //update the solution in the elite set.
    }
  }

  public final double IG_ls(populationI _sp) {
    int UsedSolution = 0;
    chromosomeLength = _sp.getSingleChromosome(0).genes.length;
    int numberofCity = chromosomeLength - numberofSalesmen;
    List<Integer> destructedPart = new ArrayList<>();
    List<Integer> reservePart = new ArrayList<>();
    List<Integer> salesmenPart = new ArrayList<>();

    evaluateNewSoln(_sp.getSingleChromosome(0));
    double originalObjValue = _sp.getSingleChromosome(0).getObjValue()[0];

    for (int i = 0; i < numberofCity; i++) {
      reservePart.add(_sp.getSingleChromosome(0).genes[i]);
    }
    for (int i = numberofCity; i < chromosomeLength; i++) {
      salesmenPart.add(_sp.getSingleChromosome(0).genes[i]);
    }
    setdestructedPart(reservePart, destructedPart, salesmenPart);
//  insertPoint : number of insert position
    int insertPoint = reservePart.size() + 1;

    List<Integer> tmpPart = new ArrayList<>();
    tmpPart.addAll(reservePart);
    tmpPart.addAll(salesmenPart);
    double tmpObjValue;
    List<Integer> lsPart = new ArrayList<>();
    lsPart.addAll(reservePart);
    lsPart.addAll(salesmenPart);
    double lsObjValue = 0;
    for (int i = 0; i < maxNeighborhood; i++) {
//    add destructedPart gene and initialize Chromosome then calculate objectivefunction    
      lsPart.add(0, destructedPart.get(i));
      lsPart.set(lsPart.size() - salesmenPart.size(), lsPart.get(lsPart.size() - salesmenPart.size()) + 1);
      chromosome lsChromosome = new chromosome();
      lsChromosome.setGenotypeAndLength(true, lsPart.size(), 1);
      lsChromosome.setSolution(lsPart);
      evaluateNewSoln(lsChromosome);
      lsObjValue = lsChromosome.getObjValue()[0];

      for (int j = 1; j < insertPoint; j++) {
        tmpPart.add(j, destructedPart.get(i));
        tmpPart.set(tmpPart.size() - salesmenPart.size(), (lsPart.get(tmpPart.size() - salesmenPart.size())));

        chromosome tmpChromosome = new chromosome();
        tmpChromosome.setGenotypeAndLength(true, tmpPart.size(), 1);
        tmpChromosome.setSolution(tmpPart);
        evaluateNewSoln(tmpChromosome);
        tmpObjValue = tmpChromosome.getObjValue()[0];

        if (tmpObjValue >= lsObjValue) {
          lsObjValue = tmpObjValue;
          lsPart.clear();
          lsPart.addAll(tmpPart);
          tmpPart.remove(j);

        } else {
          tmpPart.remove(j);
        }
      }
      if (i == (maxNeighborhood - 1)) {
        UsedSolution += (tmpPart.size() - numberofSalesmen - tmpPart.get(tmpPart.size() - 1) + 1);
      }
      tmpPart.clear();
      tmpPart.addAll(lsPart);
      insertPoint++;
    }
    UsedSolution++;
    double localsearchObj;
    if (lsObjValue > originalObjValue) {
      _sp.getSingleChromosome(0).setSolution(lsPart);
      evaluateNewSoln(_sp.getSingleChromosome(0));
      localsearchObj = _sp.getSingleChromosome(0).getObjValue()[0];
      currentUsedSolution += UsedSolution;
//      System.out.println("resetGenes");
//      System.out.println("reservePart: " + reservePart.toString() + "\ndestructedPart: " + destructedPart.toString() + "\nsalesmenPart: " + salesmenPart.toString());
//      System.out.println("lsPart: " + lsPart.toString() + "\nlocalsearchObj: " + localsearchObj);
//      System.out.println("UsedSolution(F): " + UsedSolution + "\ncurrentUsedSolution(total): " + currentUsedSolution);
    } else {
      UsedSolution = 0;
      localsearchObj = originalObjValue;
    }
    return localsearchObj;
  }

  public final void setdestructedPart(List<Integer> reservePart, List<Integer> destructedPart, List<Integer> salesmenPart) {
    int cities = chromosomeLength - salesmenPart.size();
    int[] Destructgenes = new int[numberofSalesmen];
    int numberofDestructgenes = maxNeighborhood;

    for (int i = 0; i < numberofSalesmen; i++) {
      if (numberofDestructgenes == 0) {
        break;
      }
      int raitoNumber = (int) Math.round(((double) maxNeighborhood * salesmenPart.get(i)) / cities);
      for (int j = 0; j < raitoNumber; j++) {
        if (numberofDestructgenes == 0) {
          break;
        }
        Destructgenes[i] += 1;
        numberofDestructgenes -= 1;
      }
    }
    
    if (numberofDestructgenes > 0) {
      System.out.println("numberofDestructgenes: " + numberofDestructgenes);
      int _numberofcities = salesmenPart.get(0), index = 0;
      for (int i = 0; i < numberofDestructgenes; i++) {
        for (int j = 1; j < salesmenPart.size(); j++) {
          if (numberofDestructgenes == 0 || salesmenPart.get(j) == 0) {
            break;
          }
          if (_numberofcities < salesmenPart.get(j)) {
            index = j;
            _numberofcities = salesmenPart.get(j);
          }
        }
        Destructgenes[index] += 1;
        numberofDestructgenes -= 1;
      }
    }


    for (int i = 0; i < salesmenPart.size(); i++) {
      if (numberofDestructgenes > 0 && salesmenPart.get(i) == 0) {
        int _numberofcities = salesmenPart.get(0), index = 0;
        for (int j = 1; j < salesmenPart.size(); j++) {
          if (numberofDestructgenes == 0) {
            break;
          }
          if (_numberofcities < salesmenPart.get(j)) {
            index = j;
            _numberofcities = salesmenPart.get(j);
          }
        }
        Destructgenes[index] += 1;
        numberofDestructgenes -= 1;
      }
    }
    if (salesmenPart.get(numberofSalesmen - 1) == 0 && numberofDestructgenes > 0) {
//      System.out.println("Last numberofDestructgenes To Destructgenes[0]: "+ numberofDestructgenes);
      Destructgenes[0] += numberofDestructgenes;
      numberofDestructgenes = 0;
    } else if (numberofDestructgenes > 0) {
      Destructgenes[numberofSalesmen - 1] = numberofDestructgenes;
      numberofDestructgenes = 0;
    }

    int currentPosition = 0;
    for (int i = 0; i < Destructgenes.length; i++) {
      int frequency = Destructgenes[i];
      for (int j = 0; j < frequency; j++) {
        int tmp;
        tmp = new Random().nextInt(salesmenPart.get(i)) + currentPosition;
        destructedPart.add(reservePart.get(tmp));
        reservePart.remove(reservePart.get(tmp));
        salesmenPart.set(i, (salesmenPart.get(i) - 1));
        Destructgenes[i]--;
//        System.out.println("tmp:"+tmp);
      }
      currentPosition += salesmenPart.get(i);
    }
  }

  public int getBestIndex(populationI arch1) {
    int index = 0;
    double bestobj = 0;
    for (int k = 0; k < arch1.getPopulationSize(); k++) {
      if (arch1.getObjectiveValues(k)[0] > bestobj) {
        bestobj = arch1.getObjectiveValues(k)[0];
        index = k;
      }
    }
    return index;
  }

  @Override
  public chromosome evaluateNewSoln(chromosome chromosome1) {
    for (int k = 0; k < ObjectiveFunction.length; k++) {
      ObjectiveFunction[k].setData(chromosome1, numberofSalesmen);
      chromosome1.getObjValue();
      chromosome1.setObjValue(ObjectiveFunction[k].getObjectiveValues(k));
    }
    return chromosome1;
  }

  @Override
  public void setNumberofSalesmen(int numberofSalesmen) {
    this.numberofSalesmen = numberofSalesmen;
  }
}
