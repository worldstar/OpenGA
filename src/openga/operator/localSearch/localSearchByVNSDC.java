package openga.operator.localSearch;

import openga.chromosomes.*;
import openga.ObjectiveFunctions.*;

/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */
public class localSearchByVNSDC implements localSearchI {

    public localSearchByVNSDC() {
    }
    public populationI pop;
    public populationI archive;
    ObjectiveFunctionI ObjectiveFunction[];
    public int popSize, chromosomeLength;  //size of population and number of digits of chromosome
    public int cutPoint1, cutPoint2;       //the genes between the two points are inversed
    int totalExaminedSolution;             //the stopping criteria
    //to record how many solutions are used. currentUsedSolution <= totalExaminedSolution
    int currentUsedSolution;
    int maxNeighborhood = 5;  //A default value of the maximum neighbors to search.
    public int numberOfMachines;
    public int processingTime[][];
    public PNode head = null;
    public PNode tail = null;
    int totalTimes = 2;
    int selectedIndex;

    public void setData(populationI population1, int totalExaminedSolution, int maxNeighborhood) {
        pop = new population();
        this.pop = population1;
        this.totalExaminedSolution = totalExaminedSolution;
        this.maxNeighborhood = maxNeighborhood;
        popSize = population1.getPopulationSize();
        chromosomeLength = population1.getSingleChromosome(0).genes.length;

    }

    public void setData(populationI population1, populationI archive, int currentUsedSolution) {
        pop = new population();
        this.pop = population1;
        this.archive = archive;
        this.currentUsedSolution = currentUsedSolution;
        popSize = population1.getPopulationSize();
        chromosomeLength = population1.getSingleChromosome(0).genes.length;

    }

    public void setData(populationI population1, populationI archive, int currentUsedSolution, int iter) {
        pop = new population();
        this.pop = population1;
        this.archive = archive;
        this.currentUsedSolution = currentUsedSolution;
        popSize = population1.getPopulationSize();
        chromosomeLength = population1.getSingleChromosome(0).genes.length;
        this.totalTimes = iter;
    }

    public void setObjectives(ObjectiveFunctionI ObjectiveFunction[]) {
        this.ObjectiveFunction = ObjectiveFunction;
    }

    public void setEDAinfo(double container[][]) {
        System.out.println("This class openga.operator.localSearch.localSearchBy2Opt doesn't implement the setEDAinfo(double container[][]).");
        System.out.println("The program stops here.");
        System.exit(0);
    }

    /**
     * @return The index of the best solution in the population.
     */
    public int getTargetChromosome() {
        selectedIndex = 0;
        double minFitness = Double.MAX_VALUE;

        for (int i = 0; i < popSize; i++) {
            double indFitness = pop.getSingleChromosome(i).getFitnessValue();
            if (indFitness < minFitness) {
                minFitness = indFitness;
                selectedIndex = i;
            }
        }
        return selectedIndex;
    }

    public chromosome evaluateNewSoln(chromosome chromosome1) {
        //calculate its objective
        for (int k = 0; k < ObjectiveFunction.length; k++) {
            ObjectiveFunction[k].setData(chromosome1, k);
        }
        return chromosome1;
    }

    public populationI evaluateNewSoln(populationI _pop) {
        //calculate its objective
        for (int k = 0; k < ObjectiveFunction.length; k++) {
            ObjectiveFunction[k].setData(_pop, k);
            ObjectiveFunction[k].calcObjective();
            _pop = ObjectiveFunction[k].getPopulation();
        }
        return _pop;
    }

    public void startLocalSearch() {
        boolean compareResult = false;
        int selectedIndex = getBestIndex(archive);
        int iteration = 0;
        populationI _pop = new population();//to store the temp chromosome
        _pop.setGenotypeSizeAndLength(pop.getEncodedType(), 3, pop.getLengthOfChromosome(),
                pop.getNumberOfObjectives());
        _pop.initNewPop();

        _pop.setChromosome(0, archive.getSingleChromosome(selectedIndex));//Set the original solution to the 1st chromosome (best).
        _pop.setChromosome(1, _pop.getSingleChromosome(0));  //chromosome current
        _pop.setChromosome(2, _pop.getSingleChromosome(0));  //temp best
        currentUsedSolution = 0;
        do {
            _pop.setChromosome(1, setD(_pop.getSingleChromosome(1)));//To variate the current best solution.
            _pop.setChromosome(2, _pop.getSingleChromosome(1));

            do {
                compareResult = false;
                _pop = swap_ls(_pop);
                _pop.setChromosome(1, _pop.getSingleChromosome(2));
                _pop.setObjectiveValue(1, _pop.getObjectiveValues(2));
                   _pop = insert_ls(_pop);
                _pop.setChromosome(1, _pop.getSingleChromosome(2));
                _pop.setObjectiveValue(1, _pop.getObjectiveValues(2));

                if (_pop.getSingleChromosome(1).getObjValue()[0] < _pop.getSingleChromosome(0).getObjValue()[0]) {
                    _pop.setChromosome(0, _pop.getSingleChromosome(1));
                    _pop.setObjectiveValue(0, _pop.getObjectiveValues(1));
                    compareResult = true;
                }

            } while (compareResult);

            if (_pop.getSingleChromosome(1).getObjValue()[0] < _pop.getSingleChromosome(0).getObjValue()[0]) {
                _pop.setChromosome(0, _pop.getSingleChromosome(1));
                _pop.setObjectiveValue(0, _pop.getObjectiveValues(1));
                //iteration = 0;
            } else {
                _pop.setChromosome(1, _pop.getSingleChromosome(0));
                _pop.setObjectiveValue(1, _pop.getObjectiveValues(0));
                iteration++;
            }

//            System.out.println("-----current best result-----------");
//            System.out.println(iteration + ":" + _pop.getSingleChromosome(0).toString1() + "->" +  _pop.getSingleChromosome(0).getObjValue()[0] + " used:" +currentUsedSolution);

        } while (iteration < totalTimes);   //continueLocalSearch()

//        System.out.println("-----VNS result-----------");
//        System.out.println(_pop.getSingleChromosome(0).toString1() + "->" +  _pop.getSingleChromosome(0).getObjValue()[0] + " used:" +currentUsedSolution);
        //System.exit(0);
        pop.setChromosome(selectedIndex, _pop.getSingleChromosome(0));
        pop.setObjectiveValue(selectedIndex, _pop.getObjectiveValues(0));

        updateArchive(_pop.getSingleChromosome(0)); //update the solution in the elite set.

//        System.out.println("best of archive: " + pop.getSingleChromosome(getTargetChromosome()).toString1());

        //System.exit(0);
    }//end startLocalSearch

    public final chromosome kickMove(chromosome chromosome1) {//The standard procedure of Iterative Local Search.
        int d = 1;  //shaking times
//        for ( int i = 0 ; i< d ; i++) {
        setCutpoint();
        chromosome1 = swapGenes(chromosome1);      //inverseGenes(chromosome1);
        setCutpoint();
        chromosome1 = insertGenes(chromosome1);
        setCutpoint();
        chromosome1 = swapGenes(chromosome1);

//        }
//        System.out.println("-----shake-----------");
//        System.out.println(chromosome1.toString1());
        //System.exit(0);
        return chromosome1;
    }

    public class PNode {

        int sequence;
        PNode next;

        PNode()//«Øºc¤l
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

    public final chromosome setD(chromosome _chromosome) {
        double DCper = 0.1;  //0.1,0.2,0.3,0.4,0.5
        int DClength =1 ;  //2,3,(int) (chromosomeLength * DCper);
        int DGenes[] = new int[DClength];
        int RGenes[] = new int[chromosomeLength - DClength];
        PNode trace = head;

        boolean theSame = false;
        int i = 0;
        do {
            cutPoint1 = (int) (Math.random() * chromosomeLength);
//            System.out.print(cutPoint1 + ","+_chromosome.genes[cutPoint1]+ ",");

            if (_chromosome.genes[cutPoint1] != -1) {
                DGenes[i] = _chromosome.genes[cutPoint1];
                _chromosome.genes[cutPoint1] = -1;
//                System.out.println(DGenes[i] + " , ");
                i++;
            }
//            System.exit(0);
        } while (i < DClength);

        int pos = 0;
        for (int j = 0; j < chromosomeLength; j++) {
            if (_chromosome.genes[j] != -1) {
                RGenes[pos] = _chromosome.genes[j];
                pos++;
            }
        }

        PNode newnode0 = new PNode();
        newnode0.SetPNodesequence(RGenes[0]);
        newnode0.SetPNodeLink(null);
        head = newnode0;
        tail = head;
        for (int ii = 1; ii < (chromosomeLength - DClength); ii++) {
            PNode newnode = new PNode();
            newnode.SetPNodesequence(RGenes[ii]);
            newnode.SetPNodeLink(null);
            tail.SetPNodeLink(newnode);
            tail = newnode;
        }

        trace = head;
        /* check list
        for (int zz = 0 ; zz<(chromosomeLength-DClength) ; zz++ ){
        System.out.print(trace.GetPNodesequence()+ " , ");
        trace= trace.GetPNodeLink();
        }
        System.out.println();
         */
        for (int rrr = 0; rrr < DClength; rrr++) {
            //           System.out.println("************************ " + rrr + " ********************");
            int makespan = 0;
            int minmakespan = 0;
            int posi = 0;
            int bestposi = 0;
            PNode temp = head;
            PNode nexttemp = head;
            PNode index = head;
            PNode newnodet = new PNode();
            newnodet.SetPNodesequence(DGenes[rrr]);
            newnodet.SetPNodeLink(null);
            //node put in first pos
            nexttemp = temp.GetPNodeLink();
            newnodet.SetPNodeLink(temp);
            head = newnodet;
            minmakespan = calcObjectives(chromosomeLength - DClength + rrr);
            bestposi = 0;
            index = index.GetPNodeLink();

//            System.out.println("obj1 = "+ minmakespan);
/*
            trace = head;
            for (int zz = 0 ; zz<(chromosomeLength-DClength+1+rrr) ; zz++ ){
            System.out.print(trace.GetPNodesequence()+ " , ");
            trace= trace.GetPNodeLink();
            }
            System.out.println();
            //System.exit(0);
             */
            head = temp;

            //node put in second, third, .....pos
            for (int ddd = 0; ddd < (chromosomeLength - DClength + rrr); ddd++) {
                posi++;
                newnodet.SetPNodeLink(index);
                temp.SetPNodeLink(newnodet);
                makespan = calcObjectives(chromosomeLength - DClength + rrr);
                //System.out.println("obj2 = "+ makespan);
                if (makespan < minmakespan) {
                    minmakespan = makespan;
                    bestposi = posi;
                }
                /*
                trace = head;
                for (int zz = 0 ; zz<(chromosomeLength-DClength+1+rrr) ; zz++ ){
                System.out.print(trace.GetPNodesequence()+ " , ");
                trace= trace.GetPNodeLink();
                }
                System.out.println();
                System.out.println("obj1 = "+ minmakespan + "pos = "+bestposi);
                 */
                temp.SetPNodeLink(index);
                if (index != null) {
                    temp = index;
                    index = index.GetPNodeLink();
                }
            }

            //construct node
            PNode insn = head;
            if (bestposi == 0) {
                newnodet.SetPNodeLink(head);
                head = newnodet;
            } else {
                for (int ggg = 0; ggg < (bestposi - 1); ggg++) {
                    insn = insn.GetPNodeLink();
                }
                newnodet.SetPNodeLink(insn.GetPNodeLink());
                insn.SetPNodeLink(newnodet);
            }
            /*
            trace = head;
            for (int zz = 0 ; zz<(chromosomeLength-DClength+1+rrr) ; zz++ ){
            System.out.print(trace.GetPNodesequence()+ " , ");
            trace= trace.GetPNodeLink();
            }
            System.out.println();
            System.out.println("obj1 = "+ minmakespan + "pos = "+bestposi);
             */
        }

        trace = head;
        for (int iii = 0; iii < chromosomeLength; iii++) {
            _chromosome.setGeneValue(iii, trace.GetPNodesequence());
            trace = trace.GetPNodeLink();
        }


//        System.out.println(_chromosome.toString1());

        //System.exit(0);

        return _chromosome;
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
            //GetPNodeLinkSystem.out.print(partc[dd]+", ");
        }
        //System.out.println();
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
                        machineTime[j] = machineTime[j] + (int)processingTime[index][j];
                    } else {//the starting time is the completion time of last machine
                        machineTime[j] = machineTime[j - 1] + (int)processingTime[index][j];
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

    public final void setCutpoint() {
        boolean theSame = true;
        cutPoint1 = (int) (Math.random() * chromosomeLength);
        cutPoint2 = (int) (Math.random() * chromosomeLength);

        if (cutPoint1 == cutPoint2) {
            cutPoint1 -= (int) (Math.random() * cutPoint1);
            //increase the position of cutPoint2
            cutPoint2 += (int) ((chromosomeLength - cutPoint2) * Math.random());

            //double check it.
            if (cutPoint1 == cutPoint2) {
                //setCutpoint();
            }
        }

        //swap
        if (cutPoint1 > cutPoint2) {
            int temp = cutPoint2;
            cutPoint2 = cutPoint1;
            cutPoint1 = temp;
        }
    }

    public final chromosome swapGenes(chromosome _chromosome) {

        int backupGenes;
        int counter = 0;
        backupGenes = _chromosome.genes[cutPoint2];
        _chromosome.genes[cutPoint2] = _chromosome.genes[cutPoint1];
        _chromosome.genes[cutPoint1] = backupGenes;
        return _chromosome;
    }

    public final chromosome insertGenes(chromosome _chromosome) {
        if (cutPoint2 >= cutPoint1) {
            int length = cutPoint2 - cutPoint1 + 1;
            int backupGenes[] = new int[length];
            int counter = 0;

            //System.out.println(_chromosome.toString1());
            //System.exit(0);

            //store the genes at backupGenes.
            for (int i = cutPoint1; i <= cutPoint2; i++) {
                //System.out.print( cutPoint1 + ","  + cutPoint2 +"->");
                //System.out.println(_chromosome.genes[i]);
                backupGenes[counter++] = _chromosome.genes[i];
            }
            //System.exit(0);
            //assgin the gene at the end of the range to the place of the range.
            _chromosome.genes[cutPoint2] = backupGenes[0];
            //_chromosome.genes[cutPoint1] = backupGenes[backupGenes.length - 1];
            counter = 1;

            //write data of backupGenes into the genes
            for (int i = cutPoint1; i < cutPoint2; i++) {
                _chromosome.genes[i] = backupGenes[counter];
                counter++;
            }
        } else {
            int length = cutPoint1 - cutPoint2 + 1;
            int backupGenes[] = new int[length];
            int counter = 0;
            for (int i = cutPoint2; i <= cutPoint1; i++) {
                backupGenes[counter++] = _chromosome.genes[i];
            }
            _chromosome.genes[cutPoint2] = backupGenes[backupGenes.length - 1];
            counter = 0;
            for (int i = cutPoint2 + 1; i <= cutPoint1; i++) {
                _chromosome.genes[i] = backupGenes[counter];
                counter++;
            }

        }
        //System.out.println(_chromosome.toString1());
        return _chromosome;
    }

    public final chromosome swapJobs(chromosome _chromosome, int pos1, int pos2) {
        int backupGenes;
        backupGenes = _chromosome.genes[pos2];
        _chromosome.genes[pos2] = _chromosome.genes[pos1];
        _chromosome.genes[pos1] = backupGenes;
        return _chromosome;
    }

    public final populationI swap_ls(populationI _sp) {
        //evaluateNewSoln(_sp);
        int i = 0, count = 0, j = 0;
        boolean compareResult;
//        System.out.println(_sp.getSingleChromosome(1).toString1());
        do {
            j = i + 1;
            do {
//                System.out.println(_sp.getSingleChromosome(2).toString1());
                _sp.setChromosome(1, swapJobs(_sp.getSingleChromosome(1), i, j));  //1 is temp chromosome, 2 is best
                evaluateNewSoln(_sp);
//                System.out.println(_sp.getSingleChromosome(1).toString1() + "\t"+_sp.getSingleChromosome(1).getObjValue()[0] + " , " + _sp.getSingleChromosome(2).getObjValue()[0]);
                currentUsedSolution++;
                if (_sp.getSingleChromosome(1).getObjValue()[0] < _sp.getSingleChromosome(2).getObjValue()[0]) {
                    _sp.setChromosome(2, _sp.getSingleChromosome(1));
                    _sp.setObjectiveValue(2, _sp.getObjectiveValues(1));
                    i = i - 1;
                    j = i + 1;
                    if (i < 0) {
                        i = 0;
                        j = 1;
                    }
                    count = 0;
                    //                   System.out.println("changed");
                } else {
                    _sp.setChromosome(1, _sp.getSingleChromosome(2));
                    _sp.setObjectiveValue(1, _sp.getObjectiveValues(2));
                    j++;
                }
//                System.out.println();
            } while (j < chromosomeLength);
            i++;
            count++;
            if (i >= (chromosomeLength - 1)) {
                i = 0;
            }
//            System.out.println(_sp.getSingleChromosome(2).toString1() + "->" + _sp.getSingleChromosome(2).getObjValue()[0] + " us:" +currentUsedSolution);
        } while (count < chromosomeLength);

//        System.out.println("-----swap----end-----------");
//        System.out.println(_sp.getSingleChromosome(2).toString1() + "->" +  _sp.getSingleChromosome(2).getObjValue()[0] + " used:" +currentUsedSolution);
        //System.exit(0);
        return _sp;
    }

    public final chromosome shiftGenes(chromosome _chromosome, int cutPoint1, int cutPoint2) {
        if (cutPoint2 >= cutPoint1) {
            int length = cutPoint2 - cutPoint1 + 1;
            int backupGenes[] = new int[length];
            int counter = 0;

            //System.out.println(_chromosome.toString1());
            //System.exit(0);

            //store the genes at backupGenes.
            for (int i = cutPoint1; i <= cutPoint2; i++) {
                //System.out.print( cutPoint1 + ","  + cutPoint2 +"->");
                //System.out.println(_chromosome.genes[i]);
                backupGenes[counter++] = _chromosome.genes[i];
            }
            //System.exit(0);
            //assgin the gene at the end of the range to the place of the range.
            _chromosome.genes[cutPoint2] = backupGenes[0];
            //_chromosome.genes[cutPoint1] = backupGenes[backupGenes.length - 1];
            counter = 1;

            //write data of backupGenes into the genes
            for (int i = cutPoint1; i < cutPoint2; i++) {
                _chromosome.genes[i] = backupGenes[counter];
                counter++;
            }
        } else {
            int length = cutPoint1 - cutPoint2 + 1;
            int backupGenes[] = new int[length];
            int counter = 0;
            for (int i = cutPoint2; i <= cutPoint1; i++) {
                backupGenes[counter++] = _chromosome.genes[i];
            }
            _chromosome.genes[cutPoint2] = backupGenes[backupGenes.length - 1];
            counter = 0;
            for (int i = cutPoint2 + 1; i <= cutPoint1; i++) {
                _chromosome.genes[i] = backupGenes[counter];
                counter++;
            }

        }
        //System.out.println(_chromosome.toString1());
        return _chromosome;
    }

    public final populationI insert_ls(populationI _sp) {
//        evaluateNewSoln(_sp);
        int i = 0, count = 0, j = 0;
        boolean compareResult;
        do {
            j = 0;  //different
            do {
                _sp.setChromosome(1, shiftGenes(_sp.getSingleChromosome(1), i, j));  //temp chromosome
//                System.out.println(_sp.getSingleChromosome(1).toString1());
//                System.out.println(_sp.getSingleChromosome(2).toString1());
                evaluateNewSoln(_sp);
//                System.out.println(_sp.getSingleChromosome(1).toString1() + "\t"+_sp.getSingleChromosome(1).getObjValue()[0] + " , " + _sp.getSingleChromosome(2).getObjValue()[0]);
                currentUsedSolution++;
                if (_sp.getSingleChromosome(1).getObjValue()[0] < _sp.getSingleChromosome(2).getObjValue()[0]) {
                    _sp.setChromosome(2, _sp.getSingleChromosome(1));
                    _sp.setObjectiveValue(2, _sp.getObjectiveValues(1));
                    i = i - 1;
                    j = 0;  //different
                    count = 0;
                    if (i < 0) {
                        i = 0;
                        j = 0;
                    }
//                    System.out.println("changed");
                } else {
                    _sp.setChromosome(1, _sp.getSingleChromosome(2));
                    _sp.setObjectiveValue(1, _sp.getObjectiveValues(2));
                    j++;
                }

//                System.out.println();
            } while (j < chromosomeLength);
            i++;
            count++;
            if (i >= (chromosomeLength - 1)) {
                i = 0;
            }
//            System.out.println(_sp.getSingleChromosome(2).toString1() + "->" + _sp.getSingleChromosome(2).getObjValue()[0] + " us:" +currentUsedSolution);
        } while (count < chromosomeLength);
//        System.out.println("-----insert----end-----------");
//        System.out.println(_sp.getSingleChromosome(2).toString1() + "->" +  _sp.getSingleChromosome(2).getObjValue()[0] + " used:" +currentUsedSolution);
//        System.exit(0);
        return _sp;
    }

    public final chromosome inverseGenes(chromosome _chromosome) {
        int length = cutPoint2 - cutPoint1 + 1;
        int backupGenes[] = new int[length];
        int counter = 0;

        //store the genes at backupGenes.
        for (int i = cutPoint1; i <= cutPoint2; i++) {
            backupGenes[counter++] = _chromosome.genes[i];
        }

        counter = 0;
        //write data of backupGenes into the genes
        for (int i = cutPoint2; i >= cutPoint1; i--) {
            _chromosome.genes[i] = backupGenes[counter++];
        }
        return _chromosome;
    }

    public populationI getLocalSearchResult() {
        return pop;
    }

    /**
     * If obj[i] < obj[j], then return true.
     * @param _obj1 the first obj
     * @param _obj2 the second obj
     * @param index where to start
     * @return
     */
    public boolean getObjcomparison(double _obj1[], double _obj2[]) {
        double objectiveWeightSum1 = 0;
        double objectiveWeightSum2 = 0;
        double weight[] = new double[_obj1.length];
        double sum = 0;

        for (int i = 0; i < _obj1.length; i++) {
            weight[i] = Math.random();
            sum += weight[i];
        }

        for (int i = 0; i < _obj1.length; i++) {
            weight[i] /= sum;
        }


        for (int i = 0; i < _obj1.length; i++) {
            objectiveWeightSum1 += _obj1[i] * weight[i];
            objectiveWeightSum2 += _obj2[i] * weight[i];
        }
        if (objectiveWeightSum1 < objectiveWeightSum2) {
            return true;
        } else {
            return false;
        }
    }

    public boolean getObjcomparison1(chromosome _chromosome1, chromosome _chromosome2) {
        double objective1 = 0;
        double objective2 = 0;



        objective1 = _chromosome1.getObjValue()[0];
        objective2 = _chromosome2.getObjValue()[0];

        if (objective1 < objective2) {
            return true;
        } else {
            return false;
        }
    }

    public boolean continueLocalSearch() {//currentUsedSolution should be less than totalExaminedSolution
        if (currentUsedSolution < totalExaminedSolution) {
            return true;
        } else {
            return false;
        }
    }

    public void updateArchive(chromosome chromosome1) {
        for (int i = 0; i < archive.getPopulationSize(); i++) {
            if (archive.getFitness(i) > chromosome1.getFitnessValue()) {
                archive.setChromosome(i, chromosome1);
                archive.setObjectiveValue(i, chromosome1.getObjValue());
                archive.setFitness(i, chromosome1.getFitnessValue());
                break;
            }
        }
    }

    public int getBestIndex(populationI arch1) {
        int index = 0;
        double bestobj = Double.MAX_VALUE;

        for (int k = 0; k < arch1.getPopulationSize(); k++) {
            if (bestobj > arch1.getObjectiveValues(k)[0]) {
                bestobj = arch1.getObjectiveValues(k)[0];
                index = k;
            }
        }
        return index;
    }

    public int getCurrentUsedSolution() {
        return currentUsedSolution;
    }

    public void setschedule(int[][] processingTime, int numberOfMachines) {
        this.processingTime = processingTime;
        this.numberOfMachines = numberOfMachines;
    }
}
