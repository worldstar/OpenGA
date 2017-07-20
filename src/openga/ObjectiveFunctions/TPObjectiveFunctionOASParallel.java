package openga.ObjectiveFunctions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author YU-TANG CHANG
 */
public class TPObjectiveFunctionOASParallel extends TPObjectiveFunctionforOAS {

  public double[] revenues;
  public double[] time;
  List<List<Double>> completionTimes = new ArrayList<>();

  public void calcMaximumRevenue(int x) {
    int numberofMachines = numberOfSalesmen - 1,
            numberofjobs = chromosome1.genes.length - numberOfSalesmen,
            index = 0;

    List<List<Integer>> revenue = new ArrayList<>(numberofMachines);
    List<List<Integer>> time = new ArrayList<>(numberofMachines);
    List<List<Double>> completionTimes = new ArrayList<>(numberofMachines);
    List<List<Integer>> accept = new ArrayList<>(numberofMachines);
    List<Integer> reject = new ArrayList<>(1);
    List<Integer> salesmen = new ArrayList<>(numberofMachines);
    List<Integer> _chromosome1 = new ArrayList<>();
    _chromosome1.addAll(chromosometoList(chromosome1));

    for (int m = 0; m < numberofMachines; m++) {//m : machine number

    }
  }

  @Override
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
      _time = calccompletionTime(_time, index, accept.get(0).get(accept.get(0).size() - 2));//after Comparison time[0] will update
      revenue = calcRevenue(_time, index);

      for (int m = 1; m < numberOfSalesmen - 1; m++) {
        _time2 = time[m];
        copyGene(_chromosome1, _accept.get(m), i);
        _time2 = calccompletionTime(_time2, index, _accept.get(m).get(_accept.get(m).size() - 2));
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

  public double calctotaltotalTime() {
    double totalTime = 0;
    for (int i = 0; i < revenues.length; i++) {
      totalTime += time[i];
    }
    return totalTime;
  }

  public double calctotalRevenue() {
    double totalRevenue = 0;
    for (int i = 0; i < revenues.length; i++) {
      totalRevenue += revenues[i];
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

  public void moveGene(List<Integer> _chromosome, List<List<Integer>> _chromosomes, int index) {
    List<Integer> genes = new ArrayList<>();
    genes.add(_chromosome.get(index));
    _chromosomes.add(genes);
    _chromosome.remove(index);
  }

  public void copyGene(List<Integer> _chromosome1, List<Integer> _chromosome2, int index) {
    _chromosome2.add(_chromosome1.get(index));
  }

  public void copyGenes(List<List<Integer>> _chromosomes1, List<List<Integer>> _chromosomes2) {
    _chromosomes2.clear();
    for (int i = 0; i < _chromosomes1.size(); i++) {
      List<Integer> genes = new ArrayList<>();
      genes.addAll(_chromosomes1.get(i));
      _chromosomes2.add(genes);
    }
  }
}
