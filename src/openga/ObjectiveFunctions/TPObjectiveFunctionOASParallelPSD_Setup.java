package openga.ObjectiveFunctions;

import java.util.ArrayList;
import java.util.List;


public class TPObjectiveFunctionOASParallelPSD_Setup extends TPObjectiveFunctionOASParallel {


    public void calcMaximumRevenue() {
    Revenue = 0;
    maximumRevenue = 0;
    int numberofMachines = numberOfSalesmen - 1;   
 
    int numberOfCities = length - numberOfSalesmen, index=0, lastindex = 0, currentPosition = 0, stopPosition = 0;
    
    double[] PSDtime = new double[numberofMachines];
    double[] time = new double[numberofMachines];
    
    for (int i=0;i<numberofMachines;i++){
      PSDtime[i]=0;
      time[i]=0;     
      C[i]=0;
    }

    List<Integer> _chromosome1 = new ArrayList<>();    
    List<List<Integer>> accept = new ArrayList<>();
    for (int i=0;i<numberofMachines;i++){
      accept.add(new ArrayList<Integer>());
    }
    
    List<Integer> reject = new ArrayList<>();
    List<Integer> salesmen = new ArrayList<>();
    _chromosome1.addAll(chromosometoList(chromosome1));
    
    int machineNumber = 0;
    for (int i = 0; i < numberOfSalesmen; i++) {
      stopPosition += _chromosome1.get(numberOfCities + i);
      for (int j = currentPosition; j < stopPosition; j++) {
        index = chromosome1.genes[j];
        if (time[machineNumber] < r[index]) {
          time[machineNumber] = r[index];
        }
        if (j != 0) {
          time[machineNumber] += p[index] + (PSDtime[machineNumber] * b);
        } 
        PSDtime[machineNumber] += p[index]; // Total_Time * b [0.1,0.2,1.0]
        C[machineNumber] = time[machineNumber];

        if (C[machineNumber] <= d[index]) {
          Revenue = e[index];
        } else if (C[machineNumber] > d[index] && C[machineNumber] <= d_bar[index]) {
          Revenue = e[index] - (C[machineNumber] - d[index]) * w[index];
        } else {
          Revenue = 0;
        }
        maximumRevenue += Revenue;

        if (Revenue == 0) {        
          reject.add(_chromosome1.get(j));
        } else {
          accept.get(machineNumber).add(_chromosome1.get(j));
        }
        lastindex = index;
      }
      
      currentPosition += _chromosome1.get(numberOfCities + i);      
      //判斷當前機器誰能最早空出時間處理下一個工件
      double count=99999999;
      for (int k=0;k<numberofMachines;k++){
        if (C[k] < count){count = C[k]; machineNumber = k;}                
      }
      
    }

    _chromosome1.clear();
    for (int m = 0; m < accept.size(); m++) {
      _chromosome1.addAll(accept.get(m));
      salesmen.add(accept.get(m).size());
    }
    salesmen.add(reject.size());
    _chromosome1.addAll(reject);
    _chromosome1.addAll(salesmen);
    chromosome1.setSolution(_chromosome1);
    maximumRevenue = maximumRevenue;
  }
  
}