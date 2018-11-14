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
import openga.operator.miningGene.PBILInteractiveWithEDA3I;

/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: It implements the Self-Guided GA for the flowshop scheduling
 * problem with minimization of total flowtime.</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */
public class flowshopEDA3 extends flowshopNEH_EDA2_VNS {

    public flowshopEDA3() {
    }
    ObjectiveFunctionFlowShopScheduleI ObjectiveFunction[];
    int NEH[];
    
    public int D1;
    public int D2;
    public boolean OptMin;
    PBILInteractiveWithEDA3I GaMain;
    
    public void setEDAinfo(double lamda, double beta, int numberOfCrossoverTournament, int numberOfMutationTournament, int startingGenDividen , int D1 , int D2 , boolean OptMin) {
        this.lamda = lamda;
        this.beta = beta;
        this.numberOfCrossoverTournament = numberOfCrossoverTournament;
        this.numberOfMutationTournament = numberOfMutationTournament;
        this.startingGenDividen = startingGenDividen;
        this.D1 = D1;
        this.D2 = D2;
        this.OptMin = OptMin;
    }

    @Override
    public void initiateVars() {
        GaMain = new singleThreadGAwithEDA3();//singleThreadGAwithEDA() singleThreadGA singleThreadGAwithSecondFront singleThreadGAwithMultipleCrossover adaptiveGA
        Population = new population();
        Selection = new binaryTournament();//binaryTournament
        Crossover = new twoPointCrossover2EDA2();// twoPointCrossover2EDA twoPointCrossover2 oneByOneChromosomeCrossover twoPointCrossover2withAdpative twoPointCrossover2withAdpativeThreshold
        Mutation = new swapMutationEDA2();//swapMutationEDA() shiftMutation shiftMutationWithAdaptive shiftMutationWithAdaptiveThreshold
        ObjectiveFunction = new ObjectiveFunctionFlowShopScheduleI[numberOfObjs];
        ObjectiveFunction[0] = new ObjectiveMakeSpanForFlowShop();//the first objective, ObjectiveMakeSpanForFlowShop ObjectiveEarlinessTardinessForFlowShop
        Fitness = new singleObjectiveFitness();
        objectiveMinimization = new boolean[numberOfObjs];
        objectiveMinimization[0] = true;
        encodeType = true;
        clone1 = new solutionVectorCloneWithMutation();
        GaMain.setCloneOperatpr(clone1, true);
        //set schedule data to the objectives
        ObjectiveFunction[0].setScheduleData(processingTime, numberOfMachines);
        //GaMain.setFlowShopData(numberOfJob, numberOfMachines, processingTime);
        //DEFAULT_generations = totalSolnsToExamine / (DEFAULT_PopSize);
        DEFAULT_generations = (numberOfJob * 100);   //*flowtime* //(numberOfMachines * numberOfJob) / 2 * 30
//        DEFAULT_PopSize     = 200;
        //set the data to the GA main program.
        GaMain.setData(Population, Selection, Crossover, Mutation, ObjectiveFunction, Fitness, DEFAULT_generations, DEFAULT_PopSize, DEFAULT_PopSize,
                numberOfJob, DEFAULT_crossoverRate, DEFAULT_mutationRate, objectiveMinimization, numberOfObjs, encodeType, elitism);
        GaMain.setSecondaryCrossoverOperator(Crossover2, false);
        GaMain.setSecondaryMutationOperator(Mutation2, false);

        GaMain.setEDAinfo(lamda, beta, numberOfCrossoverTournament, numberOfMutationTournament, startingGenDividen); //ct
        GaMain.setD1(this.D1);
        GaMain.setD2(this.D2);
        GaMain.setOptMin(this.OptMin);
    }

    @Override
    public void start() {
        openga.util.timeClock timeClock1 = new openga.util.timeClock();
        timeClock1.start();
        constructInitialSolutions(Population); //ct

        GaMain.startGA();
        timeClock1.end();
        //to output the implementation result.
        String implementResult = "";
        int bestInd = getBestSolnIndex(GaMain.getArchieve());
        implementResult = fileName + "\t" + lamda + "\t" + beta + "\t" + numberOfCrossoverTournament + "\t" + numberOfMutationTournament + "\t" + startingGenDividen + "\t" + DEFAULT_crossoverRate + "\t" + DEFAULT_mutationRate + "\t" + D1 + "\t" + D2 + "\t" + OptMin + "\t" + GaMain.getArchieve().getSingleChromosome(bestInd).getObjValue()[0] + "\t" + timeClock1.getExecutionTime() / 1000.0  +"\n";
        //implementResult = fileName + "\t" + lamda + "\t" + beta + "\t" + numberOfCrossoverTournament + "\t" + numberOfMutationTournament + "\t" + startingGenDividen + "\t" + DEFAULT_crossoverRate + "\t" + DEFAULT_mutationRate + "\t" + GaMain.getArchieve().getSingleChromosome(bestInd).getObjValue()[0] + "\t" + timeClock1.getExecutionTime() / 1000.0 + "\t" + GaMain.getArchieve().getSingleChromosome(bestInd).toString1() +"\n";
        DoWriteFile("EDA3_MakeSpanForFlowShop_0831", implementResult);
        System.out.print(implementResult);
    }

    /**
     * For single objective problem
     * @param arch1
     * @return
     */
    @Override
    public int getBestSolnIndex(populationI arch1) {
        int index = 0;
        double bestobj = Double.MAX_VALUE;
        for (int k = 0; k < GaMain.getArchieve().getPopulationSize(); k++) {
            //System.out.println(GaMain.getArchieve().getObjectiveValues(k)[0]);
            if (bestobj > GaMain.getArchieve().getObjectiveValues(k)[0]) {
                bestobj = GaMain.getArchieve().getObjectiveValues(k)[0];
                index = k;
            }
        }
        //System.out.println("bestindex: "+index);
        return index;
    }

    public class PNode {

        int sequence;
        PNode next;

        PNode()//??c?l
        {
            sequence = 0;
            next = null;
        }

        void SetPNodesequence(int indata) {
            sequence = indata;
        }

        void SetPNodeLink(PNode inlink) {
            next = inlink;
        }

        int GetPNodesequence() {
            return sequence;
        }

        PNode GetPNodeLink() {
            return next;
        }
    }
    PNode head = null;

    @Override
    public void constructInitialSolutions(populationI _Population) {

        int sumProcessingTime[] = new int[numberOfJob];
        int sumProcessingTime_temp = 0;
        int index_temp = 0;
        int makespan_min = 0;
        int makespan = 0;
        int sequence[] = new int[numberOfJob];

        for (int j = 0; j < numberOfJob; j++) {
            sequence[j] = j;
        }

        for (int is = 0; is < numberOfJob; is++) {
            sumProcessingTime[is] = 0;
            for (int js = 0; js < numberOfMachines; js++) {
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
        for (int ks = 0; ks < sumProcessingTime.length; ks++) {
            for (int ls = 0; ls < (sumProcessingTime.length - 1); ls++) {
                if (sumProcessingTime[ls] < sumProcessingTime[ls + 1]) {
                    sumProcessingTime_temp = sumProcessingTime[ls + 1];
                    sumProcessingTime[ls + 1] = sumProcessingTime[ls];
                    sumProcessingTime[ls] = sumProcessingTime_temp;
                    index_temp = sequence[ls + 1];
                    sequence[ls + 1] = sequence[ls];
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

        PNode travel = null;
        PNode pre_travel = null;
        PNode step = null;
        PNode pre_step = null;
        int checkvalue = 0;
        int v_index = 0;

        PNode newnode0 = new PNode();
        newnode0.SetPNodesequence(sequence[0]);
        newnode0.SetPNodeLink(null);
        head = newnode0;

        for (int ii = 1; ii < numberOfJob; ii++) {
            checkvalue = sequence[ii];
            PNode newnode = new PNode();
            newnode.SetPNodesequence(checkvalue);
            travel = head;
            newnode.SetPNodeLink(travel);
            head = newnode;

            //travel();   //check new node is created
            // System.exit(0);

            makespan_min = calcObjectives(ii);

            //System.out.print(makespan_min+"***");  // check every init_list makespan

            v_index = 0;
            head = head.GetPNodeLink();
            pre_travel = travel;
            travel = travel.GetPNodeLink();


            for (int kk = 1; kk <= ii; kk++) {   //      find out min_makespan_index in every combine of one progress
                newnode.SetPNodeLink(travel);
                pre_travel.SetPNodeLink(newnode);
                makespan = calcObjectives(ii);
                if (makespan_min >= makespan) {
                    makespan_min = makespan;
                    v_index = kk;
                }
                pre_travel.SetPNodeLink(travel);
                if (travel != null) {
                    travel = travel.GetPNodeLink();
                    pre_travel = pre_travel.GetPNodeLink();
                }
            }

            step = head;

            if (v_index == 0) {   // according to min_makespan_index, find out the best combine
                newnode.SetPNodeLink(head);
                head = newnode;
            } else {
                for (int aa = 1; aa <= v_index; aa++) {
                    pre_step = step;
                    if (step != null) {
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

        for (int zz = (numberOfJob - 1); zz >= 0; zz--) {
            sequence[zz] = head.GetPNodesequence();
            head = head.GetPNodeLink();
        }

        /*  // check the NEH solution(chromsome)
        for (int z= 0 ;z <numberOfJob ; z++ ){
        System.out.print(sequence[z] + ",");
        }
         */
        sequence = GPI(sequence);
        GaMain.initialStage();   //product population

        _Population.getSingleChromosome(0).setSolution(sequence);  //change the solution(0) from NEH solution

        //System.exit(0);
        //System.out.println(_Population.getSingleChromosome(0).getObjValue()[0]);

        clone1.setData(_Population);
        clone1.startToClone();
        _Population = clone1.getPopulation();

    }

    public int[] swapJobs2(int _sequence[], int pos1, int pos2) {
        int temp = _sequence[pos1];
        _sequence[pos1] = _sequence[pos2];
        _sequence[pos2] = temp;
        return _sequence;
    }

    public int[] GPI(int _sequence[]) {
        double obj_pre = 0, obj_after = 0;
        for (int i = 0; i < _sequence.length - 1; i++) {
            for (int j = i + 1; j < _sequence.length; j++) {
                obj_pre = calcObj2(_sequence);
                _sequence = swapJobs2(_sequence, i, j);
                obj_after = calcObj2(_sequence);

                if (obj_after >= obj_pre) {
                    _sequence = swapJobs2(_sequence, i, j);
                } else {
                    i = 0;
                    j = 1;
                }
            }
        }
//        printSequence(_sequence);
        return _sequence;
    }

    void addPNode(int s) {
        PNode link = new PNode();
        if (link == null) {
            System.out.print("allocate memory fail!\n");
        } else {
            link.SetPNodesequence(s);
            link.SetPNodeLink(head);
            head = link;
        }
    }

    void travel(PNode link) {

        do {
            if (link == null) {
                break;
            }
            System.out.print(link.GetPNodesequence() + " ");
            link = link.GetPNodeLink();
        } while (link != null);
        System.out.print("\n");
    }

    public int calcObj2(int _array[]) {
        int machineTime[] = new int[numberOfMachines];
        int objVal = 0;
        //assign each job to each machine depended on the current machine time.
        for (int i = 0; i < length; i++) {
            int index = _array[i];
            int startTime = machineTime[0], endTime;
            for (int j = 0; j < numberOfMachines; j++) {
                if (j == 0) {
                    //the starting time is the completion time of last job on first machine
                    machineTime[j] += processingTime[index][j];
                } else {
                    if (machineTime[j - 1] < machineTime[j]) {//previous job on the machine j is not finished
                        machineTime[j] = machineTime[j] + processingTime[index][j];
                    } else {//the starting time is the completion time of last machine
                        machineTime[j] = machineTime[j - 1] + processingTime[index][j];
                    }
                }

                if (j == numberOfMachines - 1) {//if the job is processed on the last machine.
                    endTime = machineTime[j];
                    objVal += (endTime);
                }
            }
        }//end the job assignment.
        return objVal;
    }

    @Override
    public int calcObjectives(int range) {
        int machineTime[] = new int[numberOfMachines];
        int objVal = 0;
        range = range + 1;
        int partc[] = new int[range];
        PNode tempc = head;
        for (int dd = (partc.length - 1); dd >= 0; dd--) {

            partc[dd] = tempc.GetPNodesequence();
            tempc = tempc.GetPNodeLink();


        }

        //assign each job to each machine depended on the current machine time.
        for (int i = 0; i < partc.length; i++) {
            int index = partc[i];
            int startTime = machineTime[0], endTime;
            for (int j = 0; j < numberOfMachines; j++) {
                if (j == 0) {
                    //the starting time is the completion time of last job on first machine
                    //System.out.println(i+" "+length+ " "+ " "+numberOfMachine+ " "+index);
                    machineTime[j] += processingTime[index][j];
                } else {
                    if (machineTime[j - 1] < machineTime[j]) {//previous job on the machine j is not finished
                        machineTime[j] = machineTime[j] + processingTime[index][j];
                    } else {//the starting time is the completion time of last machine
                        machineTime[j] = machineTime[j - 1] + processingTime[index][j];
                    }
                }

                if (j == numberOfMachines - 1) {//if the job is processed on the last machine.
                    endTime = machineTime[j];
                    objVal += (endTime);
                }

            }
            //openga.util.printClass printClass1 = new openga.util.printClass();
            //printClass1.printMatrix("machineTime "+i, machineTime);


        }
        //The last machine time describes as the the maximum process time is the makespan.
        //objVal = machineTime[numberOfMachines - 1];
        return objVal;
    }

    public static void main(String[] args) {
        System.out.println("EDA3_MakeSpanForFlowShop_0831");
        int repeatExperiments = 3;
        int popSize[] = new int[]{500};//50, 100, 155, 210
        int counter = 0;
        double crossoverRate[] = new double[]{0.6,0.9},//0.6, 0.9 {0.9}
                mutationRate[] = new double[]{0.1,0.5},//0.1, 0.5 {0.5}
                elitism = 0.1;
        int numberOfInstance = 21;
        int totalSolnsToExamine = 30000;//30000 is the default one for Reeves.

        //EDA parameters.
        double lamdalearningrate[] = new double[]{0.1,0.5,0.9}; //0.1
        double betalearningrate[] = new double[]{0.1,0.5,0.9};   //0.9
        int numberOfCrossoverTournament[] = new int[]{1, 2, 4};//{1, 2, 4} //2
        int numberOfMutationTournament[] = new int[]{1, 2, 4};//{1, 2, 4}  //4
        int startingGenDividen[] = new int[]{2,4,7};//{2, 4}  //2
        
        //For Taillard Instance.
        int jobs[] = new int[]{20,50,100};//20, 50, 100, 200, 500
        int machines[] = new int[]{5,10,20};//5, 10, 20
        //implication of instanceReplication
        int startInstance = 1;
        int endInstance = 1;
        
        int D1[] = new int[]{0,1,2,5};//n/10 , 9,10,20  , 0,1,2,10
        int D2[] = new int[]{0,1,2,5};//n/10 , 9,10,20  , 0,1,2,10
        boolean optMin = true;
        
        for (int j = 0; j < jobs.length; j++) {
            for (int s = 0; s < machines.length; s++) {
                for (int q = 1; q <= 1; q++) {//int q = startInstance; q <= endInstance; q++
                    if ((jobs[j] <= 100) || (jobs[j] == 200 && machines[s] >= 10) || (jobs[j] == 500 && machines[s] == 20)) {
                      if((jobs[j] == 20 && machines[s] == 10) || (jobs[j] == 50 && machines[s] == 10) || (jobs[j] == 100 && machines[s] == 20)){
                          readFlowShopTaillardInstance readFlowShopInstance1 = new readFlowShopTaillardInstance();
                          String fileName = "instances\\TaillardFlowshop\\";
                          fileName += readFlowShopInstance1.getFileName(jobs[j], machines[s], q);
                          //fileName += "car8.txt";
                          readFlowShopInstance1.setData(fileName);
                          readFlowShopInstance1.getDataFromFile();

                          for (int lx = 0; lx < lamdalearningrate.length; lx++) {
                              for (int bx = 0; bx < betalearningrate.length; bx++) {
                                  for (int m = 0; m < numberOfCrossoverTournament.length; m++) {
                                      for (int n = 0; n < numberOfMutationTournament.length; n++) {
                                          for (int p = 0; p < startingGenDividen.length; p++) {
                                              for (int i = 0; i < repeatExperiments; i++) {
                                                  for(int D1Count = 0 ; D1Count < D1.length ; D1Count++) {
                                                      for(int D2Count = 0 ; D2Count < D2.length ; D2Count++) {
                                                          for(int CRCount = 0 ;CRCount < crossoverRate.length ; CRCount++){
                                                              for(int MRCount = 0 ;MRCount < mutationRate.length ; MRCount++){
                                                                //System.out.println("Combinations:\t"+(counter++)+"\t"+fileName);
                                                                flowshopEDA3 flowshop1 = new flowshopEDA3();
                                                                flowshop1.setFlowShopData(jobs[j], machines[s], readFlowShopInstance1.getPtime());

                                                                //***** examined solutions are determined by Taillard.
                                                                //***** examined solutions are determined by Liang.
                                                                totalSolnsToExamine = (jobs[j] * 2 * 500);
                                                                flowshop1.setParameters(popSize[0], crossoverRate[CRCount], mutationRate[MRCount], totalSolnsToExamine);
                                                                flowshop1.setEDAinfo(lamdalearningrate[lx], betalearningrate[bx], numberOfCrossoverTournament[m], numberOfMutationTournament[n], startingGenDividen[p] , D1[D1Count] , D2[D2Count] , optMin);
                                                                flowshop1.setData(fileName);
                                                                flowshop1.initiateVars();
                                                                flowshop1.start();

                                                              }//end for MRCount
                                                          }//end for CRCount
                                                      }//end for D2Count
                                                  }//end for D1Count
                                              }//end for
                                          }
                                      }
                                  }
                              }
                          }
                      }//end if
                    }
                }
            }
        }

        
        
    }
}
