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
 * <p>Description: It implements the Self-Guided GA for the flowshop scheduling
 * problem with minimization of makespan.</p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */
public class flowshopNEH_EDA2_VNS extends flowshopNEH_EDA_VNS {

    public flowshopNEH_EDA2_VNS() {
    }
    /**
     * Parameters of the GA
     */
    int generations, length, initPopSize, fixPopSize;
    double crossoverRate, mutationRate;
    boolean[] objectiveMinimization; //true is minimum problem.
    boolean encodeType;  //binary of realize code
    public String fileName = "";
    cloneI clone1;   //neh
    EDAMainI GaMain;
    EDAICrossover Crossover;
    EDAIMutation Mutation;
    /**
     * Parameters of the EDA
     */
    double lamda = 0.9; //learning rate
    double beta = 0.9;
    int numberOfCrossoverTournament = 2;
    int numberOfMutationTournament = 2;
    int startingGenDividen = 4;

    public void setData(String fileName) {
        this.fileName = fileName;
    }

    public void setEDAinfo(double lamda, double beta, int numberOfCrossoverTournament, int numberOfMutationTournament, int startingGenDividen) {
        this.lamda = lamda;
        this.beta = beta;
        this.numberOfCrossoverTournament = numberOfCrossoverTournament;
        this.numberOfMutationTournament = numberOfMutationTournament;
        this.startingGenDividen = startingGenDividen;


    }

    public void initiateVars() {
        GaMain = new singleThreadGAwithinipop_EDA2_VNS();//singleThreadGA singleThreadGAwithSecondFront singleThreadGAwithMultipleCrossover adaptiveGA
        Population = new population();
        Selection = new binaryTournament();//binaryTournament
        Crossover = new twoPointCrossover2EDA2();//twoPointCrossover2 oneByOneChromosomeCrossover twoPointCrossover2withAdpative twoPointCrossover2withAdpativeThreshold
        Mutation = new swapMutationEDA2();//shiftMutation shiftMutationWithAdaptive shiftMutationWithAdaptiveThreshold
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
        DEFAULT_generations = totalSolnsToExamine / (DEFAULT_PopSize);
        //System.out.print(totalSolnsToExamine+","+DEFAULT_PopSize);
        //System.exit(0);
        //set the data to the GA main program.
        GaMain.setData(Population, Selection, Crossover, Mutation, ObjectiveFunction, Fitness, DEFAULT_generations, DEFAULT_PopSize, DEFAULT_PopSize,
                numberOfJob, DEFAULT_crossoverRate, DEFAULT_mutationRate, objectiveMinimization, numberOfObjs, encodeType, elitism);
        GaMain.setSecondaryCrossoverOperator(Crossover2, false);
        GaMain.setSecondaryMutationOperator(Mutation2, false);
        GaMain.setEDAinfo(lamda, beta, numberOfCrossoverTournament, numberOfMutationTournament, startingGenDividen);
    }

    public void start() {
        openga.util.timeClock timeClock1 = new openga.util.timeClock();
        timeClock1.start();

        //constructInitialSolutions(Population); //ct
        GaMain.startGA();
        timeClock1.end();
        //to output the implementation result.
        String implementResult = "";
        int bestInd = getBestSolnIndex(GaMain.getArchieve());

        implementResult = fileName + "\t" + lamda + "\t" + beta + "\t" + numberOfCrossoverTournament + "\t" + numberOfMutationTournament + "\t" + startingGenDividen + "\t" + DEFAULT_crossoverRate + "\t" + DEFAULT_mutationRate + "\t" + GaMain.getArchieve().getSingleChromosome(bestInd).getObjValue()[0] + "\t" + timeClock1.getExecutionTime() / 1000.0 + "\n";
        writeFile("Flowshop_lozaro_eda2_doe_1120_100_20_2", implementResult);
        System.out.print(implementResult);
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

        PNode()//�غc�l
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

        GaMain.initialStage();   //product population

        _Population.getSingleChromosome(0).setSolution(sequence);  //change the solution(0) from NEH solution

        //System.out.println(_Population.getSingleChromosome(0));
        //System.exit(0);
        
        clone1.setData(_Population);
        clone1.startToClone();
        _Population = clone1.getPopulation();

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
            }
            //openga.util.printClass printClass1 = new openga.util.printClass();
            //printClass1.printMatrix("machineTime "+i, machineTime);
        }
        //The last machine time describes as the the maximum process time is the makespan.
        objVal = machineTime[numberOfMachines - 1];
        return objVal;
    }

    /**
     * For single objective problem
     * @param arch1
     * @return
     */
    public int getBestSolnIndex(populationI arch1) {
        int index = 0;
        double bestobj = Double.MAX_VALUE;
        for (int k = 0; k < GaMain.getArchieve().getPopulationSize(); k++) {
            if (bestobj > GaMain.getArchieve().getObjectiveValues(k)[0]) {
                bestobj = GaMain.getArchieve().getObjectiveValues(k)[0];
                index = k;
            }
        }
        return index;
    }

    public static void main(String[] args) {
        System.out.println("EDA_Flowshop20080120");
        int repeatExperiments = 5;
        int popSize[] = new int[]{1000};//50, 100, 155, 210  10n:200 500 1000

        int counter = 0;
        double crossoverRate[] = new double[]{0.9},//0.6, 0.9 {0.9}
                mutationRate[] = new double[]{0.5},//0.1, 0.5 {0.5}
                elitism = 0.1;
        int numberOfInstance = 21;
        int totalSolnsToExamine = 30000;//30000 is the default one for Reeves.

        //EDA parameters.
        double lamdalearningrate[] = new double[]{0.1, 0.5, 0.9}; //0.1
        double betalearningrate[] = new double[]{0.1, 0.5, 0.9};   //0.9
        int numberOfCrossoverTournament[] = new int[]{2, 4, 8};//{1, 2, 4} //2
        int numberOfMutationTournament[] = new int[]{2, 4, 8};//{1, 2, 4}  //4
        int startingGenDividen[] = new int[]{10, 30, 45, 60};//{2, 4}  //2


        /*
        for(int i = 0 ; i < repeatExperiments ; i ++ ){
        for(int j = 20 ; j < numberOfInstance ; j ++ ){
        for(int k = 0 ; k < popSize.length ; k ++ ){
        for(int m = 0 ; m < crossoverRate.length ; m ++ ){
        for(int n = 0 ; n < mutationRate.length ; n ++ ){
        flowshopEDA flowshop1 = new flowshopEDA();
        readFlowShopRevInstance readFlowShopInstance1 = new readFlowShopRevInstance();
        String fileName = "instances\\flowshop\\";
        fileName += readFlowShopInstance1.getFileName(j);
        //fileName += "car8.txt";
        readFlowShopInstance1.setData(fileName);
        readFlowShopInstance1.getDataFromFile();
        System.out.print("Combinations:\t"+(counter++)+"\t");
        flowshop1.setFlowShopData(readFlowShopInstance1.getNumberOfJobs(), readFlowShopInstance1.getNumberOfMachines(), readFlowShopInstance1.getPtime());
        //***** examined solutions are 50*m*n Reeves.
        //totalSolnsToExamine = 50*readFlowShopInstance1.getNumberOfJobs()*readFlowShopInstance1.getNumberOfMachines();
        flowshop1.setParameters(popSize[k], crossoverRate[m], mutationRate[n], totalSolnsToExamine);
        flowshop1.setData(fileName);
        System.out.print(fileName+" ");
        flowshop1.initiateVars();
        flowshop1.start();
        }
        }
        }
        }
        }//end for
         */
        //For Taillard Instance.
        int jobs[] = new int[]{100};//20, 50, 100, 200, 500
        int machines[] = new int[]{20};//5, 10, 20
        //implication of instanceReplication
        int startInstance = 1;
        int endInstance = 2;
        /*
        //for Lian et al. 2006
        String filenames[] = new String[]{"20-5-5.txt","20-5-10.txt", "20-10-10.txt", "20-20-10.txt",
        "50-10-10.txt", "50-20-10.txt", "100-5-10.txt", "100-10-10.txt"};//"20-5-5.txt","20-5-10.txt", "20-10-10.txt", "20-20-10.txt", "50-10-10.txt", "50-20-10.txt", "100-10-10.txt"
        for(int j = 0 ; j < filenames.length ; j ++ ){
        readFlowShopTaillardInstance readFlowShopInstance1 = new readFlowShopTaillardInstance();
        String fileName = "instances\\TaillardFlowshop\\";
        fileName += filenames[j];
        //fileName += "car8.txt";
        readFlowShopInstance1.setData(fileName);
        readFlowShopInstance1.getDataFromFile();
        jobs[0] = readFlowShopInstance1.getNumberOfJobs();
        machines[0] = readFlowShopInstance1.getNumberOfMachines();

        for(int k = 0 ; k < lamda.length ; k ++ ){
        for(int m = 0 ; m < numberOfCrossoverTournament.length ; m ++ ){
        for(int n = 0 ; n < numberOfMutationTournament.length ; n ++ ){
        for(int p = 0 ; p < startingGenDividen.length ; p ++ ){
        for(int i = 0 ; i < repeatExperiments ; i ++ ){
        flowshopEDA flowshop1 = new flowshopEDA();
        System.out.print("Combinations:\t"+(counter++)+"\t");
        flowshop1.setFlowShopData(jobs[0], machines[0], readFlowShopInstance1.getPtime());
        //***** examined solutions are determined by Lian et al.
        totalSolnsToExamine = flowshop1.getTotalSolnsToExamineLian(jobs[0], machines[0]);
        flowshop1.setParameters(popSize[0], crossoverRate[0], mutationRate[0], totalSolnsToExamine);
        flowshop1.setEDAinfo(lamda[k], numberOfCrossoverTournament[m], numberOfMutationTournament[n], startingGenDividen[p]);
        flowshop1.setData(fileName);
        flowshop1.initiateVars();
        flowshop1.start();
        }
        }
        }
        }
        }
        }//end for

        System.exit(0);
         */

        for (int j = 0; j < jobs.length; j++) {
            for (int s = 0; s < machines.length; s++) {
                for (int q = startInstance; q <= endInstance; q++) {
                    if ((jobs[j] <= 100) || (jobs[j] == 200 && machines[s] >= 10) || (jobs[j] == 500 && machines[s] == 20)) {
                        readFlowShopTaillardInstance readFlowShopInstance1 = new readFlowShopTaillardInstance();
                        String fileName = "instances\\TaillardFlowshop\\";
                        fileName += readFlowShopInstance1.getFileName(jobs[j], machines[s], q);
                        //fileName += "car8.txt";
                        readFlowShopInstance1.setData(fileName);
                        readFlowShopInstance1.getDataFromFile();


                        for (int m = 0; m < numberOfCrossoverTournament.length; m++) {
                            for (int n = 0; n < numberOfMutationTournament.length; n++) {
                                for (int p = 0; p < startingGenDividen.length; p++) {
                                    for (int u = 0; u < crossoverRate.length; u++) {
                                        for (int t = 0; t < mutationRate.length; t++) {
                                            for (int lx = 0; lx < lamdalearningrate.length; lx++) {
                                                for (int bx = 0; bx < betalearningrate.length; bx++) {
                                                    for (int i = 0; i < repeatExperiments; i++) {
                                                        //System.out.println("Combinations:\t"+(counter++)+"\t"+fileName);
                                                        flowshopNEH_EDA2_VNS flowshop1 = new flowshopNEH_EDA2_VNS();   //ct
                                                        flowshop1.setFlowShopData(jobs[j], machines[s], readFlowShopInstance1.getPtime());
                                                        //***** examined solutions are determined by Taillard.
                                                        //***** examined solutions are determined by Liang.
                                                        //totalSolnsToExamine = (jobs[j] * 2 * 500);
                                                        totalSolnsToExamine = (jobs[j] * jobs[j] * 2 * 500);
                                                        popSize[0] = jobs[j] * 10;
                                                        flowshop1.setParameters(popSize[0], crossoverRate[u], mutationRate[t], totalSolnsToExamine);
                                                        flowshop1.setEDAinfo(lamdalearningrate[lx], betalearningrate[bx], numberOfCrossoverTournament[m], numberOfMutationTournament[n], startingGenDividen[p]);
                                                        flowshop1.setData(fileName);
                                                        flowshop1.initiateVars();
                                                        flowshop1.start();
                                                    }//end for
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }


    }
}
