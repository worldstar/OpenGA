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
public class singleThreadGAwithEDA2 extends singleThreadGA implements EDAMainI {

    public singleThreadGAwithEDA2() {
    }
    PBILInteractive PBIL1;   //PBIL
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

    public void startGA() {
        Population = initialStage();
        //evaluate the objective values and calculate fitness values
//System.out.println(generations);
//System.exit(0);
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
            offsrping = selectionStage(Population);

            //collect gene information, it's for mutation matrix
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
            //Crossover
            offsrping = crossoverStage(offsrping, temporaryContainer, inter);

            //Mutation
            //System.out.println("mutationStage");
            offsrping = mutationStage(offsrping, temporaryContainer, inter);
            //System.out.println("timeClock1 "+timeClock1.getExecutionTime());
            ProcessObjectiveAndFitness(offsrping);
            Population = replacementStage(Population, offsrping);  //Population
            evalulatePop(Population);


                String generationResults = "";
                generationResults = i + "\t" + getBest() + "\n";
                writeFile("eda2_655" , generationResults);

            /*
            if (i == 500) {
                String generationResults = "";
                generationResults = i + "\t" + beta + "\t" + getBest() + "\n";
                //System.out.print(generationResults);
                writeFile("eda2_" + i, generationResults);
            }
            if (i == 750) {
                String generationResults = "";
                generationResults = i + "\t" + beta + "\t" + getBest() + "\n";
                //System.out.print(generationResults);
                writeFile("eda2_" + i, generationResults);
            }
            if (i == 1000) {
                String generationResults = "";
                generationResults = i + "\t" + beta + "\t" + getBest() + "\n";
                //System.out.print(generationResults);
                writeFile("eda2_" + i, generationResults);
            }
*/

        }
        //printResults();
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

    public void evalulatePop(populationI originalSet) {
        //evaluate the objective values and calculate fitness values
        ProcessObjectiveAndFitness(originalSet);
        populationI tempFront = (population) findParetoFront(Population, 0);
        archieve = updateParetoSet(archieve, tempFront);
    }

    public void setEDAinfo(double lamda, int numberOfCrossoverTournament, int numberOfMutationTournament, int startingGenDividen) {
    }

    @Override
    public void setFlowShopData(int numberOfJob, int numberOfMachines, int[][] processingTime) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
