package openga.MainProgram;

import openga.chromosomes.*;
import openga.operator.selection.*;
import openga.operator.crossover.*;
import openga.operator.mutation.*;
import openga.operator.clone.*;
import openga.ObjectiveFunctions.*;
import openga.Fitness.*;
import openga.operator.miningGene.*;

/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 *
 *   add interaction and learning effect  --->IL
 */
public class singleThreadGAwithProbabilityMatrixILRLS extends singleThreadGA implements probabilityMatrixI {

    public singleThreadGAwithProbabilityMatrixILRLS() {
    }
    PBILInteractive PBIL1;   //PBIL
    double container[][];
    double inter[][];
    int processingTime[][];
    openga.operator.miningGene.probabilityMatrixInteractiveL probMatrix1 = new openga.operator.miningGene.probabilityMatrixInteractiveL();
    //it is for learning effect
   
    int startingGeneration = 500;
    int interval = 20;
    int strategy = 1;  // 2 is default
    boolean applyEvaporation;
    String evaporationMethod = "constant";//constant, method1, method2
    populationI artificialPopulation = new population();
    openga.util.printClass printClass1 = new openga.util.printClass();
    double lamda = 0.9; //container learning rate   0.1 0.5 0.9
    double beta = 0.9; //inter learning rate   0.1 0.5 0.9
    int totalExaminedSolution = 0;
    int currentUsedSolution = 0;
    int maxNeighborhood = 5;  //A default value of the maximum neighbors to search.
    int NEH[];
    int bestindex = 0;

    public void setProbabilityMatrixData(int startingGeneration, int interval) {
        this.startingGeneration = startingGeneration;
        this.interval = interval;
    }

    public void setLearningRate(double lamda, double beta) {
        this.lamda = lamda;
        this.beta = beta;
    }

    public void setSequenceStrategy(int strategy) {
        this.strategy = strategy;
    }

    public void setEvaporationMethod(boolean applyEvaporation, String evaporationMethod) {
        this.applyEvaporation = applyEvaporation;
        this.evaporationMethod = evaporationMethod;
    }

    public void setGuidedMutationInfo(double lamda, double beta) {
        System.out.println("This method is not implemented here.");
        System.exit(0);
    }

    public void setFlowShopData(int numberOfJob, int numberOfMachines, int processingTime[][]) {
        this.numberOfJob = numberOfJob;
        this.numberOfMachines = numberOfMachines;
        this.processingTime = processingTime;
    }

    public void setSchedulingData(int numberOfJob, int numberOfMachines, int processingTime[][]) {
        this.numberOfJob = numberOfJob;
        this.numberOfMachines = numberOfMachines;
        this.processingTime = processingTime;
    }

    /**
     * Main procedure starts here. You should ensure the encoding of chromosome is done.
     */
    public void startGA() {
        translate(Population.getSingleChromosome(0));
        //Population = initialStage();   //without initial population must be cancel
        intialArttificalPopulation();

        totalExaminedSolution = generations * fixPopSize;

        PBIL1 = new PBILInteractive(Population, lamda, beta);   // PBIL

        //evaluate the objective values and calculate fitness values
        //clonea
        if (applyClone == true) {
            Population = cloneStage(Population);
        }

        ProcessObjectiveAndFitness();
        archieve = findParetoFront(Population, 0);
        //printResults();
        //     System.out.println(Population.getSingleChromosome(0).getObjValue()[0]);
//        System.exit(0);




        for (int i = 0; i < generations; i++) {
            currentGeneration = i;

            //if(i % 5 == 0){
            //System.out.print(i+"\t"+getBest()+"\n");
            //generationResults = i+"\t"+getBest()+"\n";
            //writeFile("flowshop_eACGA_obj_plot_0312", generationResults);
            // }

            // else if(i == generations - 1){
            // System.out.print(i+"\t"+getBest()+"\n");
            // }

            // set lamda, beta from 1.0 to 0
            //lamda = lamda * (double) (generations - currentGeneration) / (double) generations;
            //beta = beta * (double) (generations - currentGeneration) / (double) generations;
            // set lamda, beta from 0.001 to 0.9999
            //lamda = lamda * (double) (currentGeneration) / (double) generations;
            //beta = beta * (double) (currentGeneration) / (double) generations;
 /*
            diversityMeasure();
            generationResults = "";
            generationResults = i+"\t"+getBest()+"\t"+genoDiversityValue+"\n";
            //System.out.print(generationResults);
            writeFile("flowshop_eACGA_NEH_objDiv_ta110_div", generationResults);
             */
            if (i < startingGeneration || i % interval != 0) {
                SGA();
            } else {
                //System.out.println("generations "+i);
                ProbabilityMatrix();   // ProbabilityMatrixforRLS(i,interval, rep)    ProbabilityMatrix()
            }


            /*  for flowshop 500*2*n generations
            if (generations == 50) {   //total time 1000,500,100,50
            localSearchStageDC(20);
            } else if (generations == 125 && i % 25 == 0) {
            localSearchStageDC(40);
            } else if (generations == 250 && i % 10 == 0) {
            localSearchStageDC(2);
            } else if (generations == 500 && i % 100 == 0) {
            localSearchStageDC(1);
            }
             */

            //for setup 1000 generations  5-10  10-20  20-40  40-80 total 2000 neighbor

            if (i % 1 == 0) {   ////localSearchStagepop(50);  // localSearchStage() localSearchStagepop(100) localSearchStage(40)
                //if(i<500 )  {
                  localSearchStage(20);
                //}else{
                //  localSearchStage_VNS(20);
                //}
            }
            //evalulatePop(Population);  //if do pop local search then need evalulate

        }

//        System.out.println("before LS -> "+archieve.getObjectiveValues(0)[0]) ;
//        localSearchStagepop(100);
//        localSearchStageBest();


    }

    public void SGA() {
        populationI offspring = new population();
        offspring = selectionStage(Population);

        //Crossover
        //System.out.println("Crossover");
        offspring = crossoverStage(offspring);

        //Mutation
        //System.out.println("mutationStage");
        offspring = mutationStage(offspring);

        //clone
        //offspring = cloneStage(offspring);
        //evalulatePop(offspring);
        //Population = replacementStage(Population, offspring);
        ProcessObjectiveAndFitness(offspring);
        Population = replacementStage(Population, offspring);  //Population
        evalulatePop(Population);
//

    }

    /*
    public void SGA() {
    Population = selectionStage(Population);
    
    //Crossover
    //System.out.println("Crossover");
    Population = crossoverStage(Population);
    
    //Mutation
    //System.out.println("mutationStage");
    Population = mutationStage(Population);
    
    //clone
    Population = cloneStage(Population);
    evalulatePop(Population);
    }
     */
    public void ProbabilityMatrix() {
        Population = selectionStage(Population);
        PBIL1.setData(Population);
        PBIL1.startStatistics();
        container = PBIL1.getContainer();
        inter = PBIL1.getInter();
        probMatrix1.setData(Population, artificialPopulation, container, inter);//archieve, Population, artificialPopulation
        probMatrix1.setDuplicateRate(1);
        probMatrix1.setStrategy(strategy);     // strategy
        probMatrix1.setEvaporationMethod(applyEvaporation, evaporationMethod);
        probMatrix1.assignJobs(container, inter);

        //artificialPopulation = cloneStage(artificialPopulation);
        //evalulatePop(artificialPopulation);
        //Population = replacementStage(Population, artificialPopulation);
        //Population = cloneStage(artificialPopulation);
        ProcessObjectiveAndFitness(artificialPopulation);
        Population = replacementStage(Population, artificialPopulation);  //Population
        evalulatePop(Population);
//

    }

    public void hybridProcess() {
        Population = selectionStage(Population);
        openga.operator.miningGene.probabilityMatrix probMatrix1 = new openga.operator.miningGene.probabilityMatrix();
        probMatrix1.setData(Population, artificialPopulation);//archieve, Population, artificialPopulation
        probMatrix1.setDuplicateRate(1);
        probMatrix1.setStrategy(strategy);
        probMatrix1.setEvaporationMethod(applyEvaporation, evaporationMethod);
        probMatrix1.foundemental();

        for (int i = 0; i < Population.getPopulationSize() / 2; i++) {
            probMatrix1.generateChromosome(probMatrix1.dumpArray(probMatrix1.getMarkovContainer()), Population.getSingleChromosome(i));
        }
        //System.out.println("Crossover");
        Population = crossoverStage(Population);

        //Mutation
        //System.out.println("mutationStage");
        Population = mutationStage(Population);

        //clone
        Population = cloneStage(Population);
        evalulatePop(Population);

    }

    public populationI initialStage() {
        //Population = new population();
        Population.setGenotypeSizeAndLength(encodeType, initialPopSize, length, numberOfObjs);
        Population.createNewPop();

        return Population;
    }

    public populationI cloneStage(populationI _Population) {
        openga.operator.clone.solutionVectorCloneWithMutation clone1 = new openga.operator.clone.solutionVectorCloneWithMutation();
        clone1.setData(_Population);
        clone1.setArchive(Population);  //Populationarchieve
        clone1.startToClone();
        //System.out.println(clone1.getNumberOfOverlappedSoln());
        return clone1.getPopulation();
    }

    public void evalulatePop(populationI originalSet) {
        //evaluate the objective values and calculate fitness values
        ProcessObjectiveAndFitness(originalSet);
        populationI tempFront = (population) findParetoFront(Population, 0);
        archieve = updateParetoSet(archieve, tempFront);
    }

    public void intialArttificalPopulation() {
        artificialPopulation.setGenotypeSizeAndLength(encodeType, fixPopSize, length, numberOfObjs);
        artificialPopulation.initNewPop();
    }

    public populationI replacementStage(populationI parent, populationI offspring) {
        openga.operator.selection.noidenticalReplacement replace1 = new openga.operator.selection.noidenticalReplacement();  //noidenticalReplacement  MuPlusLamdaSelection2
        openga.operator.clone.solutionVectorCloneWithMutation clone1 = new openga.operator.clone.solutionVectorCloneWithMutation();

        replace1.setData(parent.getPopulationSize(), parent);
        replace1.setSecondPopulation(offspring);
        replace1.startToSelect();  //with identical

        clone1.setData(replace1.getSelectionResult());
        clone1.setArchive(archieve);  //Population
        clone1.startToClone();
        //System.out.println(clone1.getNumberOfOverlappedSoln());
        return clone1.getPopulation();

    }

    public populationI replacementStage1(populationI parent, populationI offspring) {
        openga.operator.selection.noidenticalReplacement replace1 = new openga.operator.selection.noidenticalReplacement();  //noidenticalReplacement  MuPlusLamdaSelection2
        openga.operator.clone.solutionVectorCloneWithMutation clone1 = new openga.operator.clone.solutionVectorCloneWithMutation();

        replace1.setData(parent.getPopulationSize(), parent);
        replace1.setSecondPopulation(offspring);
        replace1.startToSelect1();  //with no identical

        clone1.setData(replace1.getSelectionResult());
        clone1.setArchive(archieve);  //Population
        clone1.startToClone();
        //System.out.println(clone1.getNumberOfOverlappedSoln());
        return clone1.getPopulation();

    }

    public populationI replacementStage2(populationI parent, populationI offspring) {
        openga.operator.selection.noidenticalReplacement replace1 = new openga.operator.selection.noidenticalReplacement();  //noidenticalReplacement  MuPlusLamdaSelection2
        openga.operator.clone.solutionVectorCloneWithMutation clone1 = new openga.operator.clone.solutionVectorCloneWithMutation();

        replace1.setData(parent.getPopulationSize(), parent);
        replace1.setSecondPopulation(offspring);
        replace1.startToSelect1();  //with no identical

        //System.out.println(clone1.getNumberOfOverlappedSoln());
        return replace1.getSelectionResult();

    }

    public populationI ProcessObjectiveAndFitness(populationI originalSet) {
        //evaluate the objective values
        for (int i = 0; i < ObjectiveFunction.length; i++) {
            //System.out.println("The obj "+i);
            ObjectiveFunction[i].setData(originalSet, i);
            ObjectiveFunction[i].calcObjective();
            originalSet = ObjectiveFunction[i].getPopulation();
        }

        //calculate fitness values
        Fitness.setData(originalSet, numberOfObjs);
        Fitness.calculateFitness();
        originalSet = Fitness.getPopulation();
        return originalSet;
    }

    public void setNEH(int NEH[]) {      //201011 for RIS
        this.NEH = NEH;
    }

    public void localSearchStage() {
        openga.operator.localSearch.localSearchByVNS localSearch1 = new openga.operator.localSearch.localSearchByVNS();
        currentUsedSolution += fixPopSize;//Solutions used in genetic search
        localSearch1.setData(Population, totalExaminedSolution, maxNeighborhood);
        localSearch1.setData(Population, archieve, currentUsedSolution);
        localSearch1.setObjectives(ObjectiveFunction);
        localSearch1.startLocalSearch();
        currentUsedSolution = localSearch1.getCurrentUsedSolution();
    }

       public void localSearchStage_VNS(int iteration) {
        openga.operator.localSearch.localSearchByVNS localSearch1 = new openga.operator.localSearch.localSearchByVNS();
        currentUsedSolution += fixPopSize;//Solutions used in genetic search
        localSearch1.setData(Population, totalExaminedSolution, maxNeighborhood);
        localSearch1.setData(Population, archieve, currentUsedSolution, iteration);
        localSearch1.setObjectives(ObjectiveFunction);
        localSearch1.startLocalSearch();
        currentUsedSolution = localSearch1.getCurrentUsedSolution();
    }

    public void localSearchStage(int iteration) {
        openga.operator.localSearch.localSearchByRLS localSearch1 = new openga.operator.localSearch.localSearchByRLS();
        localSearch1.setData(Population, archieve, currentUsedSolution);
        localSearch1.setData(Population, archieve, currentUsedSolution, iteration);
        localSearch1.setschedule(processingTime, numberOfMachines);
        localSearch1.setObjectives(ObjectiveFunction);
        localSearch1.setNEH(NEH);
        localSearch1.startLocalSearch();
        currentUsedSolution = localSearch1.getCurrentUsedSolution();
    }

    public void localSearchStageDC(int iteration) {
        openga.operator.localSearch.localSearchByVNSDC localSearch1 = new openga.operator.localSearch.localSearchByVNSDC();
        currentUsedSolution += fixPopSize;//Solutions used in genetic search
        localSearch1.setData(Population, totalExaminedSolution, maxNeighborhood);
        localSearch1.setData(Population, archieve, currentUsedSolution, iteration);
        localSearch1.setObjectives(ObjectiveFunction);
        localSearch1.setschedule(processingTime, numberOfMachines);
        localSearch1.startLocalSearch();
        currentUsedSolution = localSearch1.getCurrentUsedSolution();
    }

    public void localSearchStagepop(int iteration) {
        openga.operator.localSearch.localSearchByVNS localSearch1 = new openga.operator.localSearch.localSearchByVNS();
        currentUsedSolution += fixPopSize;//Solutions used in genetic search
        localSearch1.setData(Population, totalExaminedSolution, maxNeighborhood);
        localSearch1.setData(Population, archieve, currentUsedSolution, iteration);
        localSearch1.setObjectives(ObjectiveFunction);
        localSearch1.startLocalSearchpop();
        currentUsedSolution = localSearch1.getCurrentUsedSolution();
    }

    public void localSearchStageRLS_setup(int iteration) {

        //ProbabilityMatrixforRLS();
        //        System.out.println(artificialPopulation.getSingleChromosome(0).toString1()+"->"+artificialPopulation.getSingleChromosome(0).getObjValue()[0]);
        //   System.exit(0);
         openga.operator.localSearch.localSearchByRLS localSearch1 = new openga.operator.localSearch.localSearchByRLS();

        localSearch1.setNEH(NEH);
        localSearch1.setData(Population, archieve, currentUsedSolution, iteration);
        //localSearch1.setData(artificialPopulation, totalExaminedSolution, maxNeighborhood);
        localSearch1.setschedule(processingTime, numberOfMachines);
        localSearch1.setObjectives(ObjectiveFunction);
        localSearch1.startLocalSearch_setup();
        currentUsedSolution = localSearch1.getCurrentUsedSolution();
    }

    public void translate(chromosome _chromosome) {
        NEH = new int[numberOfJob];
        for (int i = 0; i < _chromosome.getLength(); i++) {
            NEH[i] = _chromosome.genes[i];
        }
    }

    public void ProbabilityMatrixforRLS(int i, int interval, int iteration) {
        Population = selectionStage(Population);
        PBIL1.setData(Population);
        PBIL1.startStatistics();
        container = PBIL1.getContainer();
        inter = PBIL1.getInter();
        probMatrix1.setData(Population, artificialPopulation, container, inter);//archieve, Population, artificialPopulation
        probMatrix1.setDuplicateRate(1);
        probMatrix1.setStrategy(strategy);     // strategy
        probMatrix1.setEvaporationMethod(applyEvaporation, evaporationMethod);
        probMatrix1.assignJobs(container, inter);
        ProcessObjectiveAndFitness(artificialPopulation);
        openga.operator.localSearch.localSearchByRLS localSearch1 = new openga.operator.localSearch.localSearchByRLS();
        if (i % interval == 0) {

            localSearch1.setData(artificialPopulation, archieve, currentUsedSolution, iteration);
//            localSearch1.setData(artificialPopulation, totalExaminedSolution, maxNeighborhood);
            localSearch1.setschedule(processingTime, numberOfMachines);
            localSearch1.setObjectives(ObjectiveFunction);
//        bestindex = getBestSolnIndex();
//        translate(artificialPopulation.getSingleChromosome(bestindex));
            localSearch1.setNEH(NEH);
            localSearch1.startLocalSearchP();
            currentUsedSolution = localSearch1.getCurrentUsedSolution();
        }
        Population = replacementStage(Population, artificialPopulation);  //Population
        evalulatePop(Population);

//

    }

    public int getBestSolnIndex() {
        int index = 0;
        double bestobj = Double.MAX_VALUE;
        for (int k = 0; k < Population.getPopulationSize(); k++) {
            if (bestobj > Population.getObjectiveValues(k)[0]) {
                bestobj = Population.getObjectiveValues(k)[0];
                index = k;
            }
        }
        return index;
    }

    @Override
    public void setWeight(double w1, double w2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setparaData(int[][][] processingSetupTime, int numberOfMachines) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
