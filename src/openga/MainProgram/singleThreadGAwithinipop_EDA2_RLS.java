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
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */
public class singleThreadGAwithinipop_EDA2_RLS extends singleThreadGAwithinipop_EDA2_VNS{

    public singleThreadGAwithinipop_EDA2_RLS() {
    }
    openga.operator.localSearch.localSearchByRLS localSearch1 = new openga.operator.localSearch.localSearchByRLS();
    PBILInteractive PBIL1;   //PBIL
    PBIL PBIL2;
    double container[][];
    double inter[][];
    double temporaryContainer[][];
    openga.operator.miningGene.probabilityMatrixInteractiveL probMatrix1 = new openga.operator.miningGene.probabilityMatrixInteractiveL();
    double lamda = 0.9; //container learning rate   0.1 0.5 0.9
    double beta = 0.9; //inter learning rate   0.1 0.5 0.9
    int strategy = 1;  // 2 is default
    int numberOfCrossoverTournament = 2;
    int numberOfMutationTournament = 2;
    int tempNumberOfCrossoverTournament = 2;
    int tempNumberOfMutationTournament = 2;
    EDAICrossover Crossover;
    EDAIMutation Mutation;
    populationI offsrping = new population();
    int startingGenDividen = 4;
    int totalExaminedSolution = 0;
    int currentUsedSolution = 0;
    int maxNeighborhood = 5;  //A default value of the maximum neighbors to search.
    int processingTime[][];
    int NEH[];
    //to set basic GA components.

    public void setData(populationI Population, SelectI Selection, EDAICrossover Crossover, EDAIMutation Mutation,
            ObjectiveFunctionI ObjectiveFunction[], FitnessI Fitness, int generations, int initialPopSize, int fixPopSize,
            int length, double crossoverRate, double mutationRate, boolean[] objectiveMinimization,
            int numberOfObjs, boolean encodeType, double elitism) {
        this.Population = Population;
        this.Selection = Selection;
        this.Crossover = Crossover;
        this.Mutation = Mutation;
        this.ObjectiveFunction = ObjectiveFunction;
        this.Fitness = Fitness;
        this.generations = generations;
        this.initialPopSize = initialPopSize;
        this.fixPopSize = fixPopSize;
        this.length = length;
        this.crossoverRate = crossoverRate;
        this.mutationRate = mutationRate;
        this.objectiveMinimization = objectiveMinimization;
        this.numberOfObjs = numberOfObjs;
        this.encodeType = encodeType;
        this.elitism = elitism;
        archieve = null;
    }

    public void setEDAinfo(double lamda, double beta, int numberOfCrossoverTournament, int numberOfMutationTournament, int startingGenDividen) {
        this.lamda = lamda;
        this.beta = beta;
        this.numberOfCrossoverTournament = numberOfCrossoverTournament;
        this.numberOfMutationTournament = numberOfMutationTournament;
        this.startingGenDividen = startingGenDividen;

    }

    public void setFlowShopData(int numberOfJob, int numberOfMachines, int processingTime[][]) {
        this.numberOfJob = numberOfJob;
        this.numberOfMachines = numberOfMachines;
        this.processingTime = processingTime;
    }

    // flowshop for stopping critial total examming solution N*2* 500
    public void startGA1() {
        double diversity = 0.0;
        //Population = initialStage();  //*ct*

        if (applyClone == true) {
            Population = cloneStage(Population);
        }

        //evaluate the objective values and calculate fitness values
        ProcessObjectiveAndFitness();
        intialOffspringPopulation();
        archieve = findParetoFront(Population, 0);

        PBIL1 = new PBILInteractive(Population, lamda, beta);   // PBIL

        container = PBIL1.getContainer();
        inter = PBIL1.getInter();
        temporaryContainer = new double[length][length];
        for (int i = 0; i < generations; i++) {
            //System.out.println("generations "+i);
            currentGeneration = i;

            //collect gene information, it's for mutation matrix
            /*
            if (i % 5 == 0)  {
            diversity = diversityMeasure(Population);
            }
             */
            /*
            generationResults = "";
            generationResults = i + "\t" + getBest() + "\t" + diversityMeasure(Population) + "\n";
            System.out.print(generationResults);
            writeFile("flowshop_SGGA2_obj_plot_20_10m", generationResults);
             */

            offsrping = selectionStage(Population);
            //if (i < generations / startingGenDividen){
            //if ((i < generations / startingGenDividen) || (i % (3) == 0)) {
            if (i % startingGenDividen != 0) {
                //if (diversity < 0.3) {
                tempNumberOfCrossoverTournament = 1;
                tempNumberOfMutationTournament = 1;
            } else {
                //if ((diversity < 0.2)  && (i % (generations / 10) == 0)) {
                //     tempNumberOfCrossoverTournament = 1;
                //     tempNumberOfMutationTournament = 1;
                // } else {
                tempNumberOfCrossoverTournament = numberOfCrossoverTournament;
                tempNumberOfMutationTournament = numberOfMutationTournament;
                PBIL1.setData(offsrping);
                PBIL1.startStatistics();
                container = PBIL1.getContainer();
                inter = PBIL1.getInter();
                //temporaryContainer = AccuArray(container);
                temporaryContainer = container;
                //}
            }

            /*
            if (i < generations / startingGenDividen) {
            tempNumberOfCrossoverTournament = 1;
            tempNumberOfMutationTournament = 1;
            } else {
            if (i < generations * 5 / 8) {
            tempNumberOfCrossoverTournament = 5;
            tempNumberOfMutationTournament = 5;
            } else if (i < generations * 6 / 8) {
            tempNumberOfCrossoverTournament = 4;
            tempNumberOfMutationTournament = 4;
            } else if (i < generations * 7 / 8) {
            tempNumberOfCrossoverTournament = 3;
            tempNumberOfMutationTournament =3;
            } else {
            tempNumberOfCrossoverTournament = 2;
            tempNumberOfMutationTournament = 2;
            }
            PBIL1.setData(offsrping);
            PBIL1.startStatistics();
            container = PBIL1.getContainer();
            inter = PBIL1.getInter();
            //temporaryContainer = AccuArray(container);
            temporaryContainer = container;
            }
             */


            /*
            System.out.println("Container_EDA2");
            for (int w = 0; w < container.length; w++) {
            for (int q = 0; q < container.length; q++) {
            System.out.print(temporaryContainer[w][q] + ",");
            }
            System.out.println();
            }
            //System.out.println();

            System.exit(0);
             */
            //Crossover
            offsrping = crossoverStage(offsrping, temporaryContainer, inter);

            //Mutation
            //System.out.println("mutationStage");
            offsrping = mutationStage(offsrping, temporaryContainer, inter);
            //System.out.println("timeClock1 "+timeClock1.getExecutionTime());

            ProcessObjectiveAndFitness(offsrping);
            Population = replacementStage(Population, offsrping);  //Population
            evalulatePop(Population);


            //local search
            if (generations == 40) {   //1000
                localSearchStageRLS(25);
            } else if (generations == 100 && i % 2 == 0) {   //200
                localSearchStageRLS(4);
            } else if (generations == 200 && i % 4 == 0) {   //50
                localSearchStageRLS(1);
            } else if (generations == 400 && i % 80 == 0) {   //5
                localSearchStageRLS(1);
            }




        }
        //localSearchStage();
        //printResults();
    }

    //flowtime for stopping critial is n*m/2 * 27ms
    public void startGA() {

        openga.util.timeClock timeClock1 = new openga.util.timeClock();
        double tt = System.currentTimeMillis();
        double ct = 0.0;

        //System.out.println(Population.getSingleChromosome(0).toString1());
        //System.exit(0);

        translate(Population.getSingleChromosome(0));

        //Population = initialStage();  //*ct*

        if (applyClone == true) {
            Population = cloneStage(Population);
        }

        //evaluate the objective values and calculate fitness values
        ProcessObjectiveAndFitness();
        //intialOffspringPopulation();
        archieve = findParetoFront(Population, 0);

        PBIL1 = new PBILInteractive(Population, lamda, beta);   // PBIL
        temporaryContainer = new double[length][length];
        container = PBIL1.getContainer();
        inter = PBIL1.getInter();
        int i = 0;  //real generations number

        do {
            //System.out.println("generations "+i);
            //currentGeneration = i;
            offsrping = selectionStage(Population);

            //collect gene information, it's for mutation matrix
            if (i % startingGenDividen != 0) {
                tempNumberOfCrossoverTournament = 1;
                tempNumberOfMutationTournament = 1;
            } else {
                //if (diversity < 0.2) {
                //     tempNumberOfCrossoverTournament = 1;
                //     tempNumberOfMutationTournament = 1;
                // } else {
                tempNumberOfCrossoverTournament = numberOfCrossoverTournament;
                tempNumberOfMutationTournament = numberOfMutationTournament;
                PBIL1.setData(offsrping);
                PBIL1.startStatistics();
                container = PBIL1.getContainer();
                inter = PBIL1.getInter();
                //temporaryContainer = AccuArray(container);
                temporaryContainer = container;
                //}
            }


            //System.out.println("Container_EDA2");
            //for (int w = 0; w < container.length; w++) {
            //for (int q = 0; q < container.length; q++) {
            //System.out.print(container[w][q] + ",");
            //}
            //System.out.println();
            //}
            //System.out.println();

            //System.exit(0);

            //Crossover
            offsrping = crossoverStage(offsrping, temporaryContainer, inter);

            //Mutation
            //System.out.println("mutationStage");
            offsrping = mutationStage(offsrping, temporaryContainer, inter);
            //System.out.println("timeClock1 "+timeClock1.getExecutionTime());

            ProcessObjectiveAndFitness(offsrping);
            Population = replacementStage(Population, offsrping);  //Population
            evalulatePop(Population);

            //local search
            if (length == 20) {
                localSearchStageRLS(25);
            } else if (length == 50 && i % 2 == 0) {   //200
                localSearchStageRLS(4);
                //System.out.print(i);
            } else if (length == 100 && i % 8 == 0) {   //50
                localSearchStageRLS(2);
            } else if (length == 200 && i % 80 == 0) {   //5
                localSearchStageRLS(1);
            }

            ct = System.currentTimeMillis() - tt;
            i++;
            //System.out.println(ct + "," + i);
        } while (ct < generations);   //generations = n*m/2*27 ms

        //localSearchStage();
        //printResults();
    }

    public populationI initialStage() {
        //Population = new population();
        Population.setGenotypeSizeAndLength(encodeType, initialPopSize, length, numberOfObjs);
        Population.createNewPop();

        return Population;
    }

    public double[][] AccuArray(double _container[][]) {

        for (int i = 0; i < _container.length; i++) {
            for (int j = 0; j < _container.length; j++) {
                temporaryContainer[i][j] = _container[i][j];
            }
        }

        for (int posi = 0; posi < length - 1; posi++) {
            for (int jobj = 0; jobj < length; jobj++) {
                temporaryContainer[jobj][posi + 1] = temporaryContainer[jobj][posi] + temporaryContainer[jobj][posi + 1];
            }
        }

        return temporaryContainer;
    }

    public void intialOffspringPopulation() {
        offsrping.setGenotypeSizeAndLength(encodeType, fixPopSize, length, numberOfObjs);
        offsrping.initNewPop();
    }

    public populationI crossoverStage(populationI Population, double container[][], double inter[][]) {
        Crossover.setData(crossoverRate, Population);
        Crossover.setEDAinfo(container, inter, tempNumberOfCrossoverTournament);
        Crossover.startCrossover();
        Population = Crossover.getCrossoverResult();

        return Population;
    }

    public populationI mutationStage(populationI Population, double container[][], double inter[][]) {
        Mutation.setData(mutationRate, Population);
        Mutation.setEDAinfo(container, inter, numberOfMutationTournament);
        Mutation.startMutation();
        Population = Mutation.getMutationResult();

        return Population;
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

    public double diversityMeasure(populationI offspring) {
        double DiversityValue = 0.0;
        openga.adaptive.diversityMeasure.diversityMeasureI diversityMeasure1 = new openga.adaptive.diversityMeasure.HammingDistance();  //GenoTypePositionDifference() HammingDistance()
        diversityMeasure1.setData(offspring);
        //diversityMeasure1.CalcDiversity();
        diversityMeasure1.startCalcDiversity();
        DiversityValue = diversityMeasure1.getPopulationDiversityValue();
        return DiversityValue;

    }

    public void evalulatePop(populationI originalSet) {
        //evaluate the objective values and calculate fitness values
        ProcessObjectiveAndFitness(originalSet);
        populationI tempFront = (population) findParetoFront(Population, 0);
        archieve = updateParetoSet(archieve, tempFront);
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

    public void localSearchStageRLS(int iteration) {
        openga.operator.localSearch.localSearchByRLS localSearch1 = new openga.operator.localSearch.localSearchByRLS();
        localSearch1.setData(Population, archieve, currentUsedSolution);
        localSearch1.setData(Population, archieve, currentUsedSolution, iteration);
        localSearch1.setschedule(processingTime, numberOfMachines);
        localSearch1.setObjectives(ObjectiveFunction);
        localSearch1.setNEH(NEH);
        localSearch1.startLocalSearch();
        currentUsedSolution = localSearch1.getCurrentUsedSolution();
    }

    public void translate(chromosome _chromosome) {
        NEH = new int[_chromosome.getLength()];

        for (int i = 0; i < _chromosome.getLength(); i++) {
            NEH[i] = _chromosome.genes[i];
        }
    }

    public void localSearchStageVNS(int iteration) {
        openga.operator.localSearch.localSearchByVNS localSearch1 = new openga.operator.localSearch.localSearchByVNS();
        currentUsedSolution += fixPopSize;//Solutions used in genetic search
        localSearch1.setData(Population, totalExaminedSolution, maxNeighborhood);
        localSearch1.setData(Population, archieve, currentUsedSolution, iteration);
        localSearch1.setObjectives(ObjectiveFunction);
        localSearch1.startLocalSearch();
        currentUsedSolution = localSearch1.getCurrentUsedSolution();
    }
}
