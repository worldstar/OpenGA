/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package openga.applications.flowshopProblem;
import java.util.*;
/**
 *
 * @author nhu
 */
public class NEH_Method {
    public int[] startNEH(int[][] processingTime, int numberOfJob, int numberOfMachines){
        int sumProcessingTime[] = new int[numberOfJob];
        int sumProcessingTime_temp = 0;
        int index_temp= 0 ;
        int makespan_min = 0;       
        int sequence[] = new int[numberOfJob];

        for(int j = 0 ; j < numberOfJob ; j ++ ){
            sequence[j] = j;
        }

        for(int is = 0 ; is < numberOfJob ; is ++ ){
            sumProcessingTime[is] = 0;
            for(int js = 0 ; js < numberOfMachines ; js ++ ){
                sumProcessingTime[is] += processingTime[is][js];
            }
        }

        /* check ok
            for (int z= 0 ;z <numberOfJob ; z++ ){
                System.out.print(sequence[z] + ",");
                System.out.print(sumProcessingTime[z] + ",");
            }
            System.out.println("**************************************");
         */

        //Sort sumProcessingTime (MAX->MIN)
        for (int ks = 0 ; ks <sumProcessingTime.length ; ks++){
            for(int ls = 0 ; ls <(sumProcessingTime.length-1) ; ls++){
                if (sumProcessingTime[ls]<sumProcessingTime[ls+1]){
                    sumProcessingTime_temp = sumProcessingTime[ls+1];
                    sumProcessingTime[ls+1] =  sumProcessingTime[ls];
                    sumProcessingTime[ls] = sumProcessingTime_temp;
                    index_temp = sequence[ls+1];
                    sequence[ls+1] = sequence[ls];
                    sequence[ls] = index_temp;
                }
            }
        }

        

       /* check ok
        for (int z= 0 ;z <numberOfJob ; z++ ){
            System.out.print(sequence[z] + ",");
            System.out.print(sumProcessingTime[z] + ",");
        }
        System.out.println("**************************************");
        System.exit(0);
        */

        int CurrentBestPosition = 0;        
        //List <Integer>list = new LinkedList<Integer>();
        List <Integer>list = new MyList();

        int size;        
        int checkvalue = 0;
        
        list.add(sequence[0]);

        for(int ii = 1 ; ii < numberOfJob ; ii++ ){
            size = list.size();
            for(int i = 0 ; i <= size ; i ++){
                checkvalue = sequence[ii];
                list.add(i, checkvalue);
                if(i == 0 ) {//The first position, set it as a current best objective value and position.
                    makespan_min = calcObjectives(list, processingTime, numberOfJob, numberOfMachines);
                    CurrentBestPosition = i;
                }
                else{//The rest of the positions.
                    int tempObj = calcObjectives(list, processingTime, numberOfJob, numberOfMachines);
                    if(makespan_min > tempObj ){
                        makespan_min = tempObj;
                        CurrentBestPosition = i;
                    }
                }

                //remove the testing ith position node.
                list.remove(i);
            }

            //Set the job into the current best position
            list.add(CurrentBestPosition, sequence[ii]);
         }

         size = list.size();
         Collections.reverse(list);
         
         for(int i = 0 ; i <= size ; i ++){
             sequence[i] = list.get(i);
         }
        return sequence;
    }
    

    public int calcObjectives(List <Integer>list, int[][] processingTime, int numberOfJob, int numberOfMachines) {
        int machineTime[] = new int[numberOfMachines];
        int objVal = 0;        
        int partc[] = new int[list.size()];
        
        for (int i = 0 ; i < partc.length ; i ++ ){
              partc[i] = list.get(i);                
        }

        //assign each job to each machine depended on the current machine time.
        for(int i = 0 ; i < partc.length ; i ++ ){
            int index = partc[i];
            for(int j = 0 ; j < numberOfMachines ; j ++ ){
                if(j == 0){
                    //the starting time is the completion time of last job on first machine
                    //System.out.println(i+" "+length+ " "+ " "+numberOfMachine+ " "+index);
                    machineTime[j] += processingTime[index][j];
                } else{
                    if(machineTime[j - 1] < machineTime[j]){//previous job on the machine j is not finished
                        machineTime[j] = machineTime[j] + processingTime[index][j];
                    } else{//the starting time is the completion time of last machine
                        machineTime[j] = machineTime[j - 1] + processingTime[index][j];
                    }
                }
            }
            //openga.util.printClass printClass1 = new openga.util.printClass();
            //printClass1.printMatrix("machineTime "+i, machineTime);
        }
        //The last machine time describes as the the maximum process time is the makespan.
        objVal = machineTime[numberOfMachines-1];
        return objVal;
    }
}

class MyList extends LinkedList<Integer>{
    int startTime = 0;
    int endTime = 0;
}
