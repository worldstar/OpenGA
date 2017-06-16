package openga.ObjectiveFunctions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author YU-TANG CHANG
 */
public class TPObjectiveFunctionOASParallel extends TPObjectiveFunctionforOAS {

  @Override
  public void calcMaximumRevenue() {
    List<Integer> _chromosome1 = new ArrayList<>();
    _chromosome1.addAll(chromosometoList(chromosome1));
    List<List<Integer>> accept = new ArrayList<>();
    List<Integer> reject = new ArrayList<>();
    List<Integer> salesmen = new ArrayList<>();
    List<List<Double>> completionTimes = new ArrayList<>();
    double[] time = new double[numberOfSalesmen - 1],
            revenues = new double[numberOfSalesmen - 1];
    int index;
    maximumRevenue = 0;

    System.out.println("_chromosome1(B):" + _chromosome1.toString());
    for (int m = 0; m < numberOfSalesmen - 1; m++) {
//      System.out.println("m: " + m);
      List<Double> times = new ArrayList<>();   //initialize times & revenue.
      completionTimes.add(times);
      moveGenes(_chromosome1, accept, 0);       //give every accept machine a order first.
      index = accept.get(m).get(0);
      time[m] = calccompletionTime(0, index, -1);
      completionTimes.get(m).add(time[m]);
      revenues[m] = calcRevenue(time[m], index);
    }
//    System.out.println("accept:" + accept.toString());
//    System.out.println("_chromosome1(A):" + _chromosome1.toString());
//    System.out.println("completionTimes:" + completionTimes.toString());
    System.out.println();

    /*try to insert last order on every machine, 
      if revenue & completion time is better, 
      put the order into the machine.*/
    double _time = 0, _time2 = 0,revenue = 0,_revenue = 0;
    List<List<Integer>> _accept = new ArrayList<>();
    List<List<Integer>> _accept2 = new ArrayList<>();
    _accept.addAll(accept);
    

    for (int i = 0; i < _chromosome1.size() - numberOfSalesmen; i++) {
      _time += time[0];
      index = _chromosome1.get(i);
      copyGene(_chromosome1, _accept.get(0), 0);
      System.out.println("_accept(copyGene1):" + _accept.toString());
      System.out.println("_accept2(copyGene1):" + _accept.toString());

      _time += calccompletionTime(_time, index, _accept.get(0).get(_accept.get(0).size() - 1));
      revenue = calcRevenue(_time, index);

      for (int m = 1; m < numberOfSalesmen - 1; m++) {
        _accept2.addAll(accept);
        System.out.println("_accept2:" + _accept2.toString());
//        System.out.println("m: " + m);
        _time2 += time[m];
        copyGene(_chromosome1, _accept2.get(m), 0);
        System.out.println("_accept2(copyGene2):" + _accept2.toString());

        _time2 += calccompletionTime(_time2, index, _accept2.get(m).get(_accept2.get(m).size() - 1));
        _revenue = calcRevenue(_time2, index);

        if (_revenue == 0 && revenue == 0 && m == numberOfSalesmen - 2) {
          moveGene(_chromosome1, reject, 0);
        } else if (_revenue > revenue) {
          _accept.clear();
          _accept.addAll(_accept2);
          _time = _time2;
          revenue = _revenue;
          System.out.println("tmpRevenue > bestRevenue");
        } else if (_revenue == revenue) {
          if (_time2 < _time) {
            _accept.clear();
            _accept.addAll(_accept2);
            _time = _time2;
            revenue = _revenue;
            System.out.println("tmpRevenue == bestRevenue & _time2 < _time");
          } else {//_time2 >= _time
            System.out.println("tmpRevenue == bestRevenue & _time2 >= _time");
          }
        } else {//tmpRevenue <= revenue
          System.out.println("tmpRevenue <= revenue");
        }

        System.out.println("accept:" + accept.toString());
      }
      System.out.println();
    }

    for (int m = 0; m < numberOfSalesmen - 1; m++) {
      salesmen.add(accept.get(m).size());
    }
    salesmen.add(reject.size());

//    System.out.println("_chromosome1(F):" + _chromosome1.toString());
//    System.out.println("accept:" + accept.toString());
//    System.out.println("reject:" + reject.toString());
//    System.out.println("salesmen:" + salesmen.toString());
//    System.out.println("completionTimes:" + completionTimes.toString());
//    System.out.println("revenues:" + Arrays.toString(revenues));
//    System.out.println();

    /*for (int i = 0; i < _chromosome1.size(); i++) {
      index = _chromosome1.get(i);

      for (int m = 0; m < accept.size(); m++) {//accept.size() = number of accepted machines, m = machine.
        if (time[m] < r[index]) {
          time[m] = r[index];
        }
        if (i != 0) {
          time[m] += s[index][lastindex];
        }
        time[m] += p[index];
        completionTime.get(m).add(time[m]);
        dayGap = Math.max(0, Math.min((d_bar[index] - d[index]), (d_bar[index] - completionTime.get(m).get(i))));
        Revenue[m] = dayGap * w[index];
        System.out.println("machine: " + m + " Revenue" + Revenue[m]);
      }
      lastindex = index;
    }
    System.out.println("completionTime:" + completionTime.toString());
    System.out.println();
    /*for (int i = 0; i < numberOfSalesmen; i++) {
      stopPosition += _chromosome1.get(numberOfCities + i);
      for (int j = currentPosition; j < stopPosition; j++) {
        index = chromosome1.genes[j];
        if (time < r[index]) {
          time = r[index];
        }
        if (j != 0) {
          time += s[index][lastindex];
        }
        time += p[index];
        C[index] = time;

        dayGap = Math.max(0, Math.min((d_bar[index] - d[index]), (d_bar[index] - C[index])));
        Revenue = dayGap * w[index];
        maximumRevenue += Revenue;
//        System.out.println("currentPosition: " + currentPosition);
//        System.out.println("stopPocurrentPositionsition: " + stopPosition);
//        System.out.println("obj: " + (index));
//        System.out.println("release date: " + r[index]);
//        System.out.println("processing time: " + p[index]);
//        System.out.println("setup times: " + s[index][lastindex]);
//        System.out.println("time: " + time);
//        System.out.println("dayGap = " + dayGap);
//        System.out.println("weight = " + w[index]);
//        System.out.println("Revenue = " + Revenue);
//        System.out.println("maximumRevenue = " + maximumRevenue);

        if (Revenue == 0) {
          reject.add(_chromosome1.get(j));
        } else {
          accept.add(_chromosome1.get(j));
        }
        lastindex = index;
      }
      currentPosition += _chromosome1.get(numberOfCities + i);
    }

    for (int i = 0; i < numberOfSalesmen - 1; i++) {
      int cities = (int) Math.round((double) accept.size() / (numberOfSalesmen - 1));
      salesmen.add(cities);
    }
    salesmen.add(reject.size());
    _chromosome1.clear();
    _chromosome1.addAll(accept);
    _chromosome1.addAll(reject);
    _chromosome1.addAll(salesmen);
    chromosome1.setSolution(_chromosome1);
    /*
    System.out.print("accept: ");
    for (int i = 0; i < accept.size(); i++) {
      System.out.print(accept.get(i) + " ");
    }
    System.out.println("End");
    
    System.out.print("reject: ");
    for (int i = 0; i < reject.size(); i++) {
      System.out.print(reject.get(i) + " ");
    }
    System.out.println("End");
    
    System.out.print("salesmen: ");
    for (int i = 0; i < salesmen.size(); i++) {
      System.out.print(salesmen.get(i) + " ");
    }
    System.out.println("End");
    System.out.print("_chromosome1(after): ");
    for (int i = 0; i < _chromosome1.size(); i++) {
      System.out.print(_chromosome1.get(i) + " ");
    }
    System.out.println("End");//*/
  }

  public double calctotalRevenue(List<List<Double>> Revenues) {
    double totalRevenue = 0;
    for (int i = 0; i < Revenues.size(); i++) {
      for (int j = 0; j < Revenues.get(i).size(); j++) {
        totalRevenue += Revenues.get(i).get(j);
      }
    }
    return totalRevenue;
  }

  public double calcRevenue(double completionTime, int index) {
    double revenue = Math.max(0, Math.min((d_bar[index] - d[index]), (d_bar[index] - completionTime))) * w[index];
    return revenue;
  }

  public double calccompletionTime(double time, int index, int lastindex) {
    if (lastindex != -1) {
      time += s[index][lastindex];
    }
    if (time < r[index]) {
      time = r[index];
    }
    time += p[index];
    return time;
  }

  public void moveGene(List<Integer> _chromosome1, List<Integer> _chromosome2, int index) {
    _chromosome2.add(_chromosome1.get(index));
    _chromosome1.remove(index);
  }

  public void copyGene(List<Integer> _chromosome1, List<Integer> _chromosome2, int index) {
    _chromosome2.add(_chromosome1.get(index));
  }

  public void moveGenes(List<Integer> _chromosome, List<List<Integer>> _chromosomes, int index) {
    List<Integer> genes = new ArrayList<>();
    genes.add(_chromosome.get(index));
    _chromosomes.add(genes);
    _chromosome.remove(index);
  }
}
