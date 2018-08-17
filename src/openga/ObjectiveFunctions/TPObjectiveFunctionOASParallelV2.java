/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package openga.ObjectiveFunctions;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class TPObjectiveFunctionOASParallelV2 extends TPObjectiveFunctionOASParallel {

  double[] PSD;

  public void calcMaximumRevenue() {
    revenues = new double[numberOfSalesmen - 1];
    time = new double[numberOfSalesmen - 1];
    List<Integer> _chromosome1 = new ArrayList<>();
    _chromosome1.addAll(chromosometoList(chromosome1));
    List<List<Integer>> accept = new ArrayList<>();
    List<Integer> reject = new ArrayList<>();
    List<Integer> salesmen = new ArrayList<>();
    int index;

//    System.out.println("_chromosome1(B):" + _chromosome1.toString());
    for (int m = 0; m < numberOfSalesmen - 1; m++) {
      List<Double> times = new ArrayList<>();   //initialize times & revenue.
      completionTimes.add(times);
      moveGene(_chromosome1, accept, 0);       //give every accept machine a order first.
      index = accept.get(m).get(0);
      time[m] = calccompletionTime(0, index, -1);
      completionTimes.get(m).add(time[m]);
      revenues[m] = calcRevenue(time[m], index);
    }
//    System.out.println("accept:" + accept.toString());
//    System.out.println("times:" + Arrays.toString(time));
//    System.out.println("revenues:" + Arrays.toString(revenues));
//    System.out.println("completionTimes:" + completionTimes.toString());

    /*try to insert last order on every machine, 
      if revenue & completion time is better, 
      put the order into the machine.*/
    double _time, _time2, revenue, _revenue;
    List<List<Integer>> _accept = new ArrayList<>();
    copyGenes(accept, _accept);
    int machineNumber = 0;
    for (int i = 0; i < _chromosome1.size() - numberOfSalesmen; i++) {
      index = _chromosome1.get(i);
      _time = time[0];
      copyGene(_chromosome1, accept.get(0), i);
      _time = calccompletionTime(_time, index, accept.get(0).get(accept.get(0).size() - 2),i);//after Comparison time[0] will update
      revenue = calcRevenue(_time, index);

      for (int m = 1; m < numberOfSalesmen - 1; m++) {
        _time2 = time[m];
        copyGene(_chromosome1, _accept.get(m), i);
        _time2 = calccompletionTime(_time2, index, _accept.get(m).get(_accept.get(m).size() - 2),i);
        _revenue = calcRevenue(_time2, index);
//        System.out.println("accept:" + accept.toString() + "\t" + "revenue:" + revenue + "\t" + "mb" + " " + _time + "\t");
//        System.out.println("_accept:" + _accept.toString() + "\t" + "_revenue:" + _revenue + "\t" + "m" + (m + 1) + " " + _time2 + "\t");

        if (m == numberOfSalesmen - 2 && revenue == 0 && _revenue == 0) {
//          System.out.println("the order is reject.");
          copyGene(_chromosome1, reject, i);
          accept.get(machineNumber).remove(accept.get(machineNumber).size() - 1);
          machineNumber = numberOfSalesmen;
        } else if (revenue > _revenue) {
//          System.out.println("revenue > _revenue");
        } else if (revenue == _revenue) {
          if (_time <= _time2) {
//            System.out.println("revenue = _revenue, _time < _time2");
          } else if (_time == _time2) {
            int numberofOrders = accept.get(0).size(), _numberofOrders = accept.get(m).size();
            if (numberofOrders <= _numberofOrders) {
//              System.out.println("revenue = _revenue, _time = _time2, numberofOrders <= _numberofOrders");
            } else {
//              System.out.println("revenue = _revenue, _time = _time2, numberofOrders > _numberofOrders");
              copyGenes(_accept, accept);
              _time = _time2;
              revenue = _revenue;
              machineNumber = m;
            }
          } else {
//            System.out.println("revenue = _revenue, _time > _time2");
            copyGenes(_accept, accept);
            _time = _time2;
            revenue = _revenue;
            machineNumber = m;
          }
        } else {
//          System.out.println("revenue < _revenue");
          copyGenes(_accept, accept);
          _time = _time2;
          revenue = _revenue;
          machineNumber = m;
        }
        _accept.get(m).remove(_accept.get(m).size() - 1);
        _revenue -= calcRevenue(_time2, index);
        _time2 = 0;
//        System.out.println("accept(C):" + accept.toString() + "\t" + "revenue:" + revenue + "\t" + _time + "\t");
//        System.out.println();
      }
      if (machineNumber != numberOfSalesmen) {
//        System.out.println("one gene moved.");
        time[machineNumber] = _time;
        revenues[machineNumber] += revenue;
        completionTimes.get(machineNumber).add(_time);
      }
      copyGenes(accept, _accept);
      machineNumber = 0;
//      System.out.println("times:" + Arrays.toString(time));
//      System.out.println("revenues:" + Arrays.toString(revenues));
//      System.out.println();
    }
//    System.out.println();
    _chromosome1.clear();
    for (int m = 0; m < accept.size(); m++) {
      _chromosome1.addAll(accept.get(m));
      salesmen.add(accept.get(m).size());
    }
    salesmen.add(reject.size());
    _chromosome1.addAll(reject);
    _chromosome1.addAll(salesmen);
    chromosome1.setSolution(_chromosome1);
    maximumRevenue = calctotalRevenue();

//    System.out.println("accept:"+accept.toString());
//    System.out.println("reject:"+reject.toString());
//    System.out.println("salesmen:"+salesmen.toString());
//    System.out.println("_chromosome1:"+_chromosome1.toString());
//    System.out.println("maximumRevenue:"+maximumRevenue);
//    System.out.println();
  }

  public double calccompletionTime(double time, int index, int lastindex, int i) {
    if (lastindex != -1) {
      for (int k = 0; k < i; k++) {
        time += p[chromosome1.genes[k]];
      }
    }
    if (time < r[index]) {
      time = r[index];
    }
    time += p[index];
    return time;
  }
}
