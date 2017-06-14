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
    List<Integer> _chromosome1 = new ArrayList<Integer>();
    _chromosome1.addAll(chromosometoList(chromosome1));
    List<List<Integer>> accept = new ArrayList<List<Integer>>();
    List<Integer> reject = new ArrayList<Integer>();
    List<Integer> salesmen = new ArrayList<Integer>();
    List<List<Double>> completionTimes = new ArrayList<>();
    List<List<Double>> Revenues = new ArrayList<>();
    double[] time = new double[numberOfSalesmen - 1];
    int numberOfCities = length - numberOfSalesmen, index;
    maximumRevenue = 0;

    System.out.println("_chromosome1(B):" + _chromosome1.toString());
    for (int m = 0; m < numberOfSalesmen - 1; m++) {
      moveGenes(_chromosome1, accept, 0);       //give every accept machine a order first.
      List<Double> times = new ArrayList<>();   //initialize times & revenue.
      completionTimes.add(times);
      List<Double> revenue = new ArrayList<>();
      Revenues.add(revenue);
      index = accept.get(m).get(0);
      time[m] = calccompletionTime(0, index, -1);
      completionTimes.get(m).add(time[m]);
      Revenues.get(m).add(calcRevenue(time[m], index));
    }
    System.out.println("accept:" + accept.toString());
    System.out.println("_chromosome1(A):" + _chromosome1.toString());
//    System.out.println("completionTimes:" + completionTimes.toString());
//    System.out.println("Revenues:" + Revenues.toString());
//    System.out.println();

    /*try to insert last order on every machine, 
      if revenue & completion time is better, 
      put the order into the machine.*/
    double bestRevenue = calctotalRevenue(Revenues), revenue = 0;
    double _time = time[0];

    for (int i = 0; i < _chromosome1.size()-numberOfSalesmen; i++) {
      index = _chromosome1.get(i);
      copyGene(_chromosome1, accept.get(0), 0);
      System.out.println("accept(copyGene):" + accept.toString());

      _time += calccompletionTime(_time, index, accept.get(0).get(accept.get(0).size() - 1));
      revenue = calcRevenue(_time, index);
      accept.get(0).remove(accept.get(0).size() - 1);
      System.out.println("accept(remove):" + accept.toString());

      for (int m = 1; m < numberOfSalesmen - 1; m++) {
        double tmpRevenue = 0;
        double _time2 = 0;
        copyGene(_chromosome1, accept.get(m), 0);
        _time2 = time[m];
        _time2 += calccompletionTime(_time2, index, accept.get(m).get(accept.get(m).size() - 1));
        tmpRevenue = calcRevenue(_time2, index);
        System.out.println("accept(AA):" + accept.toString());

        if (tmpRevenue > bestRevenue) {
          _chromosome1.remove(0);
          System.out.println("accept(tmpRevenue > bestRevenue):" + accept.toString());
          System.out.println("_chromosome1(A):" + _chromosome1.toString());
        } else if (tmpRevenue == bestRevenue) {

          if (_time2 < _time) {
            _chromosome1.remove(0);
            System.out.println("accept(tmpRevenue == bestRevenue & _time2 < _time):" + accept.toString());
            System.out.println("_chromosome1(A):" + _chromosome1.toString());
          } else {
            accept.get(m).remove(accept.get(m).size() - 1);
            copyGene(_chromosome1, accept.get(0), 0);
            _chromosome1.remove(0);
            System.out.println("accept(tmpRevenue == bestRevenue & _time2 > _time):" + accept.toString());
            System.out.println("_chromosome1(A):" + _chromosome1.toString());
          }
        } else {
          accept.get(m).remove(accept.get(m).size() - 1);
          copyGene(_chromosome1, accept.get(0), 0);
          _chromosome1.remove(0);
          System.out.println("accept(4):" + accept.toString());
          System.out.println("_chromosome1(A):" + _chromosome1.toString());
        }
      }

    }

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
