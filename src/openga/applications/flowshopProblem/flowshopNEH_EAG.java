package openga.applications.flowshopProblem;
import openga.chromosomes.*;
import openga.operator.selection.*;
import openga.operator.crossover.*;
import openga.operator.mutation.*;
import openga.ObjectiveFunctions.*;
import openga.MainProgram.*;
import openga.ObjectiveFunctions.*;
import openga.Fitness.*;
import openga.util.printClass;
import openga.util.fileWrite1;
import openga.applications.data.*;
import openga.operator.clone.*;

/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: It implements the EA/G.Please refer to
 * Zhang, Q., Sun, J., & Tsang, E. (2005). An evolutionary algorithm with guided mutation for the maximum clique problem. IEEE Transactions on Evolutionary Computation, 9(2), 192-200.</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class flowshopNEH_EAG extends flowshopProbMatrix{
  public flowshopNEH_EAG() {
  }

  /***
   * AC parameters
   */
  int strategy = 1;
  int replications = 1;
int NEH_makespan=0;
  /***
   * Parameters of Guided Mutation.
   */
  double lamda = 0.9; //learning rate
  double beta = 0.9;

  public void setEDAinfo(double lamda, double beta){
    this.lamda = lamda;
    this.beta = beta;
  }

  public void initiateVars(){
    GaMain     = new guidedMutationMainInitialPop();
    Population = new population();
    Selection  = new binaryTournament();
    Crossover  = new multiParentsCrossover();  //multiParentsCrossover twoPointCrossover2()
    Crossover2 = new PMX();

    Mutation   = new swapMutation();
    Mutation2  = new inverseMutation();
    ObjectiveFunction = new ObjectiveFunctionFlowShopScheduleI[numberOfObjs];
    ObjectiveFunction[0] = new ObjectiveMakeSpanForFlowShop();//the first objective, ObjectiveMakeSpanForFlowShop ObjectiveEarlinessTardinessForFlowShop
    Fitness    = new singleObjectiveFitness();
    objectiveMinimization = new boolean[numberOfObjs];
    objectiveMinimization[0] = true;
    //objectiveMinimization[1] = true;
    encodeType = true;
    clone1  = new solutionVectorCloneWithMutation();//swap mutation
    //set schedule data to the objectives
    ObjectiveFunction[0].setScheduleData(processingTime, numberOfMachines);
    //set the data to the GA main program.
    GaMain.setGuidedMutationInfo(lamda, beta);
    GaMain.setSequenceStrategy(strategy);
    DEFAULT_generations = totalSolnsToExamine/(DEFAULT_PopSize);
    GaMain.setData(Population, Selection, Crossover, Mutation, ObjectiveFunction, Fitness, DEFAULT_generations, DEFAULT_PopSize, DEFAULT_PopSize,
                   numberOfJob, DEFAULT_crossoverRate, DEFAULT_mutationRate, objectiveMinimization, numberOfObjs, encodeType, elitism);
    GaMain.setCloneOperatpr(clone1, true);
    GaMain.setSecondaryCrossoverOperator(Crossover2, false);
    GaMain.setSecondaryMutationOperator(Mutation2, false);
  }

  public void start(){
    System.out.println();
    openga.util.timeClock timeClock1 = new openga.util.timeClock();
    timeClock1.start();
    constructInitialSolutions(Population); //ct
    GaMain.startGA();
    timeClock1.end();
    int bestInd = getBestSolnIndex(GaMain.getArchieve());
    String implementationResult = "";
    //implementationResult += fileName+"\t"+numberOfJob+"\t"+DEFAULT_crossoverRate+"\t"+DEFAULT_mutationRate+"\t"+DEFAULT_PopSize+"\t" + GaMain.getArchieve().getSingleChromosome(bestInd).getObjValue()[0]+"\t"+timeClock1.getExecutionTime()/1000.0+"\n";
    //implementationResult += fileName+"\t"+DEFAULT_PopSize+"\t"+lamda +"\t"+beta+"\t" + GaMain.getArchieve().getSingleChromosome(bestInd).getObjValue()[0]+"\t" +timeClock1.getExecutionTime()/1000.0+"\n";
            implementationResult = fileName+"\t" + tt + "\t"+ NEH_makespan + "\t"+DEFAULT_PopSize+"\t"+DEFAULT_crossoverRate+"\t"
                + DEFAULT_mutationRate+"\t"+GaMain.getArchieve().getSingleChromosome(bestInd).getObjValue()[0]+"\t"
                +timeClock1.getExecutionTime()/1000.0+"\n";
    
    writeFile("NEH_EAG_3parents_Flowshop_7", implementationResult);
    System.out.print(implementationResult);
  }

    // NEH ----generate a NEH Chromosome and put in initial population
    // sort process time every job  (max -> min)
    // compare with the markspans of (1-2)(2-1) and find min in sequence
    // slot3 put in sequence(1-2) slot -> (1-2-3), (1-3-2), (3-1-2)
    // comapre the markspans of (1-2-3), (1-3-2), (3-1-2) ->find out min
    // slot4 put in sequence(3-1-2) slot ->(4-3-1-2),(3-4-1-2),(3-1-4-2),(3-1-2-4)
    //generate population
    //replace a Chromosome in population
    
    
    public class PNode {
        int sequence;
        PNode next;
        PNode()//«Øºc¤l
        { sequence=0;  next=null; }
        void SetPNodesequence(int indata) {
            sequence=indata;}
        void SetPNodeLink(PNode inlink) {
            next=inlink; }
        int GetPNodesequence() {
            return sequence; }
        PNode GetPNodeLink() {
            return next; }
    }
    String tt="";
    PNode head=null;
    int sumProcessingTime[];   //  neh
    
    public void constructInitialSolutions(populationI _Population){
        /*
        numberOfJob = 6;
        numberOfMachines=4;
        processingTime[0][0] = 25;
        processingTime[0][1] = 45;
        processingTime[0][2] = 52;
        processingTime[0][3] = 40;
        processingTime[1][0] = 7;
        processingTime[1][1] = 41;
        processingTime[1][2] = 22;
        processingTime[1][3] = 66;
        processingTime[2][0] = 41;
        processingTime[2][1] = 55;
        processingTime[2][2] = 33;
        processingTime[2][3] = 21;
        processingTime[3][0] = 74;
        processingTime[3][1] = 12;
        processingTime[3][2] = 24;
        processingTime[3][3] = 48;
        processingTime[4][0] = 7;
        processingTime[4][1] = 15;
        processingTime[4][2] = 72;
        processingTime[4][3] = 52;
        processingTime[5][0] = 12;
        processingTime[5][1] = 14;
        processingTime[5][2] = 22;
        processingTime[5][3] = 32;
        */
        
        
        int sumProcessingTime[] = new int[numberOfJob];
        int sumProcessingTime_temp = 0;
        int index_temp= 0 ;
        int makespan_min = 0;
        int makespan=0;
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
        
        PNode travel=null;
        PNode pre_travel=null;
        PNode step=null;
        PNode pre_step=null;
        int checkvalue=0;
        int v_index = 0;
        
        PNode newnode0=new PNode();
        newnode0.SetPNodesequence(sequence[0]);
        newnode0.SetPNodeLink(null);
        head = newnode0;
        
        for(int ii = 1 ; ii <numberOfJob; ii++ ){
            checkvalue = sequence[ii];
            PNode newnode=new PNode();
            newnode.SetPNodesequence(checkvalue);
            travel = head;
            newnode.SetPNodeLink(travel)   ;
            head = newnode;
            
            //travel();   //check new node is created
            // System.exit(0);
            
            makespan_min=calcObjectives(ii);
            
            //System.out.print(makespan_min+"***");  // check every init_list makespan
            
            v_index = 0;
            head = head.GetPNodeLink();
            pre_travel = travel;
            travel = travel.GetPNodeLink();
            
            
            for (int kk = 1 ; kk<=ii ; kk++){   //      find out min_makespan_index in every combine of one progress
                newnode.SetPNodeLink(travel);
                pre_travel.SetPNodeLink(newnode);
                makespan = calcObjectives(ii);
                if (makespan_min >= makespan){
                    makespan_min = makespan;
                    v_index = kk;
                }
                pre_travel.SetPNodeLink(travel);
                if (travel != null){
                    travel = travel.GetPNodeLink();
                    pre_travel = pre_travel.GetPNodeLink();
                }
            }
            
            step= head;
            
            if (v_index==0){   // according to min_makespan_index, find out the best combine
                newnode.SetPNodeLink(head);
                head = newnode;
            }else{
                for(int aa = 1 ; aa<=v_index; aa++){
                    pre_step =step ;
                    if (step != null){
                        step = step.GetPNodeLink();
                    }
                }
                newnode.SetPNodeLink(step);
                pre_step.SetPNodeLink(newnode);
            }
            
        }
        
       /*   //check the list of result, but it is revirse
        travel(head);
        System.exit(0);
        */
        PNode tempr=head;
        
        for (int zz = (numberOfJob-1) ; zz >=0  ; zz-- ){
            sequence[zz] = tempr.GetPNodesequence();
            tempr= tempr.GetPNodeLink();
            tt = tt + sequence[zz]+", ";
        }
        /*
         for (int zz = 0 ; zz<numberOfJob  ; zz++ ){
            sequence[zz] = tempr.GetPNodesequence();
            tempr= tempr.GetPNodeLink();
            tt = tt + sequence[zz]+", ";
        }       
        
          // check the NEH solution(chromsome)
        for (int z= 0 ;z <numberOfJob ; z++ ){
            System.out.print(sequence[z] + ",");
        }
         */
        
         NEH_makespan = calcObjectives(sequence);
         //System.out.println(NEH_makespan + "\n");
         //System.exit(0);
        
        GaMain.initialStage();   //product population
        
        _Population.getSingleChromosome(0).setSolution(sequence);  //change the solution(0) from NEH solution
        
        clone1.setData(_Population);
        clone1.startToClone();
        _Population = clone1.getPopulation();
        
    }
    
    void addPNode(int s) {
        PNode link=new PNode();
        if(link==null)
            System.out.print("allocate memory fail!\n");
        else {
            link.SetPNodesequence(s);
            link.SetPNodeLink(head);
            head=link;
        }
    }
    
    void travel(PNode link) {
        
        do
        {
            if(link==null)
                break;
            System.out.print(link.GetPNodesequence()+" ");
            link=link.GetPNodeLink();
        }while(link!=null);
        System.out.print("\n");
    }
    
    
    public int calcObjectives(int range) {
        int machineTime[] = new int[numberOfMachines];
        int objVal = 0;
        range = range +1;
        int partc[] = new int[range];
        PNode tempc=head;
        for (int dd = (partc.length-1) ; dd >=0  ; dd-- ){
            
            partc[dd] = tempc.GetPNodesequence();
            tempc = tempc.GetPNodeLink();
            //GetPNodeLinkSystem.out.print(partc[dd]+", ");
            
        }
        //System.out.println();
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
   
        public int calcObjectives(int[] partc) {
        int machineTime[] = new int[numberOfMachines];
        int objVal = 0;

        //System.out.println();
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
    

  
  public static void main(String[] args) {
    System.out.println("EAG_DOE_Flowshop20080308");
    int repeatExperiments = 5;
    int popSize[] = new int[]{100};//50, 100, 155, 210
    int counter = 0;
    double crossoverRate[] = new double[]{0.6},//0.6, 0.9 {0.6}
           mutationRate [] = new double[]{0.5},//0.1, 0.5 {0.5}
           elitism = 0.1;
    int numberOfInstance = 21;
    int totalSolnsToExamine = 30000;//30000 is the default one for Reeves.

    //Parameters of Guided Mutation.
    double[] lamda = new double[]{0.1}; //learning rate{0.1, 0.5, 0.9} {0.1 is better}
    double[] beta = new double[]{0.9};//Percentage of local information{0.1, 0.5, 0.9} {0.9 is significant}
    /*
    for(int j = 1 ; j < numberOfInstance ; j ++ ){
      for(int k = 0 ; k < popSize.length ; k ++ ){
        for(int m = 0 ; m < lamda.length ; m ++ ){
          for(int n = 0 ; n < beta.length ; n ++ ){
            readFlowShopRevInstance readFlowShopInstance1 = new readFlowShopRevInstance();
            String fileName = "instances\\flowshop\\";
            fileName += readFlowShopInstance1.getFileName(j);
            //fileName += "car8.txt";
            readFlowShopInstance1.setData(fileName);
            readFlowShopInstance1.getDataFromFile();
            System.out.print("Combinations:\t"+(counter++)+"\t");

            for(int i = 0 ; i < repeatExperiments ; i ++ ){
              flowshopEAG flowshop1 = new flowshopEAG();
              flowshop1.setFlowShopData(readFlowShopInstance1.getNumberOfJobs(), readFlowShopInstance1.getNumberOfMachines(), readFlowShopInstance1.getPtime());
              //***** examined solutions are 50*m*n Reeves.
              totalSolnsToExamine = 50*readFlowShopInstance1.getNumberOfJobs()*readFlowShopInstance1.getNumberOfMachines();
              flowshop1.setParameters(popSize[k], crossoverRate[0], mutationRate[0], totalSolnsToExamine);
              flowshop1.setEDAinfo(lamda[m], beta[n]);
              flowshop1.setData(fileName);
              flowshop1.initiateVars();
              flowshop1.start();
            }
          }
        }
      }
    }//end for
    System.exit(0);
    */
   //Taillard
   int jobs[] = new int[]{20, 50, 100, 200};//20, 50, 100, 200, 500
   int machines[] = new int[]{5, 10, 20};//5, 10, 20

   int startInstance = 1;
   int endInstance = 10;
   for(int j = 0 ; j < jobs.length ; j ++ ){
     for(int s = 0 ; s < machines.length ; s ++ ){
       for(int q = startInstance ; q <= endInstance ; q ++ ){//instanceReplication
         //readFlowShopTaillardInstancePop readNEHInstancePop1 = new readFlowShopTaillardInstancePop();
         //readFlowShopRevInstance readFlowShopInstance1 = new readFlowShopRevInstance();
         readFlowShopTaillardInstance readFlowShopInstance1 = new readFlowShopTaillardInstance();
         String fileName = "instances\\TaillardFlowshop\\";
         fileName += readFlowShopInstance1.getFileName(jobs[j], machines[s], q);
         //fileName += "car8.txt";
         readFlowShopInstance1.setData(fileName);
         readFlowShopInstance1.getDataFromFile();

         for(int i = 0 ; i < repeatExperiments ; i ++ ){
           for(int k = 0 ; k < popSize.length ; k ++ ){
             for(int m = 0 ; m < lamda.length ; m ++ ){
               for(int n = 0 ; n < beta.length ; n ++ ){
                 if((jobs[j] <= 100) || (jobs[j] == 200 && machines[s] >= 10) || (jobs[j] == 500 && machines[s] == 20)){
                   flowshopNEH_EAG flowshop1 = new flowshopNEH_EAG();
                   //***** examined solutions are determined by Taillard.
                   //totalSolnsToExamine = flowshop1.getTotalSolnsToExamineTaillard(jobs[j], machines[s]);
                   //***** examined solutions are determined by Liang.
                   totalSolnsToExamine = (jobs[j]*2*500);

                   flowshop1.setFlowShopData(readFlowShopInstance1.getNumberOfJobs(), readFlowShopInstance1.getNumberOfMachines(), readFlowShopInstance1.getPtime());
                   flowshop1.setParameters(popSize[k], crossoverRate[0], mutationRate[0], totalSolnsToExamine);
                   flowshop1.setEDAinfo(lamda[m], beta[n]);
                   flowshop1.setData(fileName);
                   flowshop1.initiateVars();
                   flowshop1.start();
                 }
               }
             }
           }
         }
       }
     }
   }//end for

  }
}
