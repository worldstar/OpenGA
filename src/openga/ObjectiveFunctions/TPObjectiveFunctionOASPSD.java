package openga.ObjectiveFunctions;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author YU-TANG CHANG
 */
public class TPObjectiveFunctionOASPSD extends TPObjectiveFunctionforOAS{
  double[] PSD;
  @Override
  public void calcMaximumRevenue() {
    maximumRevenue = 0;
    int numberOfCities = length - numberOfSalesmen,
            currentPosition = 0, stopPosition = 0,
            index, lastindex = 0;
    double time = 0, Revenue, dayGap;
    List<Integer> _chromosome1 = new ArrayList<Integer>();
    List<Integer> accept = new ArrayList<Integer>();
    List<Integer> reject = new ArrayList<Integer>();
    List<Integer> salesmen = new ArrayList<Integer>();
    _chromosome1.addAll(chromosometoList(chromosome1));

    for (int i = 0; i < numberOfSalesmen; i++) {
      stopPosition += _chromosome1.get(numberOfCities + i);
      for (int j = currentPosition; j < stopPosition; j++) {
        index = chromosome1.genes[j];
        
        
        
        
        if (time < r[index]) {
          time = r[index];
        }
        for (int k = 0; k < j; k++) {
          ;
        }
        if (j != 0) {
          time = Math.max(C[lastindex], r[index]);
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
  public void setOASData(double[] r, double[] p, double[] d, double[] d_bar, double[] e, double[] w, double[][] s, int numberOfSalesmen) {
    this.r = r;
    this.p = p;
    this.d = d;
    this.d_bar = d_bar;
    this.e = e;
    this.w = w;
    this.s = s;
    this.numberOfSalesmen = numberOfSalesmen;
    C = new double[p.length];
    PSD = new double[p.length];
  }
}
