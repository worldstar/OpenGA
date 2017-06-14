package openga.ObjectiveFunctions;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author YU-TANG CHANG
 */
public class TPObjectiveFunctionOASParallel extends TPObjectiveFunctionforOAS {

  @Override
  public void calcMaximumRevenue() {
    maximumRevenue = 0;
    int numberOfCities = length - numberOfSalesmen,
            currentPosition = 0, stopPosition = 0,
            index, lastindex = 0;
    double dayGap;
    double[] time = new double[numberOfSalesmen-1],Revenue = new double[numberOfSalesmen-1];
    List<List<Double>> completionTime = new ArrayList<List<Double>>();
    List<Integer> _chromosome1 = new ArrayList<Integer>();
    List<List<Integer>> accept = new ArrayList<List<Integer>>();
    List<Integer> reject = new ArrayList<Integer>();
    List<Integer> salesmen = new ArrayList<Integer>();
    _chromosome1.addAll(chromosometoList(chromosome1));

    //give every accept machine a order first.
    System.out.println("_chromosome1(B):" + _chromosome1.toString());
    for (int i = 0; i < numberOfSalesmen - 1; i++) {
      List<Integer> genes = new ArrayList<Integer>();
      genes.add(_chromosome1.get(0));
      accept.add(genes);
      _chromosome1.remove(0);
      List<Double> times = new ArrayList<Double>();
      completionTime.add(times);
    }
//    System.out.println("accept:" + accept.toString());
//    System.out.println("_chromosome1(A):" + _chromosome1.toString());
//    System.out.println();

    /*try to insert last order on every machine, 
      if revenue & completion time is better, 
      put the order into the machine.*/
    
    for (int i = 0; i < _chromosome1.size(); i++) {
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
        System.out.println("machine: " + m+" Revenue"+Revenue[m]);
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

  public void printList(List<Integer> list) {
    System.out.println(list.toString());
  }
}
