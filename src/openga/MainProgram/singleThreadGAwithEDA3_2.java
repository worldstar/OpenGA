/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package openga.MainProgram;
import openga.Fitness.FitnessI;
import openga.ObjectiveFunctions.ObjectiveFunctionI;
import openga.chromosomes.population;
import openga.chromosomes.populationI;
import openga.operator.crossover.EDAICrossover;
import openga.operator.miningGene.*;
import openga.operator.mutation.EDAIMutation;
import openga.operator.selection.SelectI;

/**
 *
 * @author Kuo Yu-Cheng
 */
public class singleThreadGAwithEDA3_2 extends singleThreadGAwithEDA2 implements PBILInteractiveWithEDA3_2I {

    public singleThreadGAwithEDA3_2() {
    }
    
    int D1;
    int D2;
    int OptMin;
    
//    PBILInteractiveWithEDA3 PBIL1;   //PBIL
//    PBILInteractiveWithEDA3_2 PBIL1;   //PBIL
    PBILInteractiveWithEDA3_2_2 PBIL1;

    public void startGA() {
        Population = initialStage();
        //evaluate the objective values and calculate fitness values
        //System.out.println(generations);
        //System.exit(0);
        ProcessObjectiveAndFitness();
        intialOffspringPopulation();
        archieve = findParetoFront(Population, 0);

//        PBIL1 = new PBILInteractiveWithEDA3(Population, lamda, beta);   // PBIL
//        PBIL1 = new PBILInteractiveWithEDA3_2(Population, lamda, beta , D1 , D2);   // PBIL
        PBIL1 = new PBILInteractiveWithEDA3_2_2(Population, lamda, beta , D1 , D2 , OptMin);   // PBIL

        container = PBIL1.getContainer();
        inter = PBIL1.getInter();
        temporaryContainer = new double[length][length];
        
        int i = 0;

        //for (int i = 0; i < generations; i++) {
        do{
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

            //String generationResults = "";
            //generationResults = i + "\t" + getBest() + "\n";
            //writeFile("eda2_655" , generationResults);
            
            currentUsedSolution += fixPopSize;//Solutions used in genetic search

            //local search
            if (applyLocalSearch == true && i % 10 == 0 ) {
                localSearchStage(1);
            } 
            
            i ++;

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
        }while(i < generations && currentUsedSolution < this.fixPopSize*this.generations);
        //printResults();
    }

  @Override
  public void setD1(int D1) {
    this.D1 = D1;
  }

  @Override
  public void setD2(int D2) {
    this.D2 = D2;
  }

  @Override
  public void setOptMin(int OptMin) {
    this.OptMin = OptMin;
  }

}
