package openga.MainProgram;

import java.util.logging.Level;
import java.util.logging.Logger;
import openga.chromosomes.*;
import openga.operator.selection.*;
import openga.operator.crossover.*;
import openga.operator.localSearch.*;
import openga.operator.mutation.*;
import openga.operator.clone.*;
import openga.ObjectiveFunctions.*;
import openga.Fitness.*;
import weka.classifiers.Classifier;
import openga.operator.miningGene.PopulationToInstances;
import weka.core.Instances;
import weka.classifiers.trees.RandomForest;
import weka.core.Instance;

/**
 * <p>
 * Title: The OpenGA project</p>
 * <p>
 * Description: To control GA entire processes.
 * <p>
 * Copyright: Copyright (c) 2004</p>
 * <p>
 * Company: Yuan-Ze University</p>
 *singleThreadGA_WekaRegression
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class singleThreadGA_Weka extends singleThreadGA implements singleThreadGA_WekaI  {

  public singleThreadGA_Weka() {
  }

  CrossoverI_Weka Crossover , Crossover2;
  MutationI_Weka Mutation , Mutation2;
  Classifier Regression;
  int numberOfTournament = 2;
  double[] obj, predict;
  int count = 49;
  Instances init_Dataset;
  
  //to set basic GA components.
  @Override
  public void setData(populationI Population, SelectI Selection, CrossoverI_Weka Crossover, MutationI_Weka Mutation,
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

  public void setSecondaryCrossoverOperator(CrossoverI_Weka Crossover2, boolean applySecCRX) {
    this.Crossover2 = Crossover2;
    this.applySecCRX = applySecCRX;
  }

  public void setSecondaryMutationOperator(MutationI_Weka Mutation2, boolean applySecMutation) {
    this.Mutation2 = Mutation2;
    this.applySecMutation = applySecMutation;
  }


  public void startGA() {
    Population = initialStage();
    ProcessObjectiveAndFitness();
    archieve = findParetoFront(Population, 0);
    obj = new double[Population.getPopulationSize()];
    predict = new double[Population.getPopulationSize()];
    
    PopulationToInstances WekaInstances = new PopulationToInstances();
    Regression = new RandomForest();
    try {
//      //    ************************This Create New ML code***************************
      init_Dataset = WekaInstances.PopulationToInstances(Population); // Instances init_Dataset
      Regression.buildClassifier(init_Dataset);
    } catch (Exception ex) {
      Logger.getLogger(singleThreadGA_Weka.class.getName()).log(Level.SEVERE, null, ex);
    }

    for (int i = 0; i < generations; i++) {
      currentGeneration = i;
      Population = selectionStage(Population);
      //collect gene information, it's for mutation matrix
//      Mutation.setData(Population);

      //Crossover
      //System.out.println("Crossover");    
      Population = crossoverStage(Regression, Population);

      //Mutation
      //System.out.println("mutationStage");    
      Population = mutationStage(Regression, Population);

      //clone
      if (applyClone == true) {
        Population = cloneStage(Population);   
      }
      
      //evaluate the objective values and calculate fitness values
      ProcessObjectiveAndFitness();
      
      if(i % 50 == 0){
      for (int j = 0; j < Population.getPopulationSize(); j++) {
        obj[j] = Population.getSingleChromosome(j).getObjValue()[0]; 
//        System.out.print(j + " " + obj[j] + " ");
        try {
          Instances chromosome = WekaInstances.chromosomeToInstances(Population.getSingleChromosome(j));
          predict[j] = Regression.classifyInstance(chromosome.instance(0));   
//          System.out.println(predict[j]);
        } catch (Exception ex) {
          Logger.getLogger(swapMutation_Weka.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
      
      //計算正確率     
      
      int correct = 0;  //正確次數
      int total = 0;    //總執行次數
      for (int n = 0; n < Population.getPopulationSize() - 1; n++) {
        for (int j = n + 1; j < Population.getPopulationSize(); j++) {
          if (obj[n] >= obj[j] && predict[n] >= predict[j]) {
            correct++;
          } else if (obj[n] <= obj[j] && predict[n] <= predict[j]) {
            correct++;
          }
          total++;    //每執行一次遞增
        }
      }
//      System.out.print("generations "+i+"\t");
//      System.out.println("總正確率: "+(double) correct / total * 100+"\t"+count);
//        if(i % 50 == 0){  //i % 10 == 0
//        System.out.print("generations "+i+"\t"); 
//        System.out.println("總正確率: "+(double) correct / total * 100);
//        }else if(i == generations - 1){
//        System.out.print("generations "+i);  
//        System.out.println("總正確率: "+(double) correct / total * 100);
     }
      
//      secondFront = (population)findParetoFront(Population,0);
      populationI tempFront = (population) findParetoFront(Population, 0);
      archieve = updateParetoSet(archieve, tempFront);

      //additionalStage();
      if (applyLocalSearch == true && i % 10 == 0) {
        localSearchStage(1);
      }
          
      if (i % count == 0) {
//        System.out.println(i+"\t"+count+"\t"+i % count);
//        System.out.println("下一代的預測所使用的模型所使用的代數:\t"+count);
        try {
          Instances Dataset = WekaInstances.PopulationToInstances(Population); // Instances init_Dataset
          init_Dataset.addAll(Dataset);
          Regression.buildClassifier(init_Dataset);
        } catch (Exception ex) {
          Logger.getLogger(singleThreadGA_Weka.class.getName()).log(Level.SEVERE, null, ex);
        }
         count +=50;         
      }
        
//      if (i % count == 0) {
//      try {
//          Instances Dataset = WekaInstances.PopulationToInstances(Population); // Instances init_Dataset
//          init_Dataset.addAll(Dataset);
//          Regression.buildClassifier(init_Dataset);
//        } catch (Exception ex) {
//          Logger.getLogger(singleThreadGA_Weka.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        count +=50;
//      }
      
      
    }
//    System.out.println(archieve);
//    printResults();
  }

  
  
  public populationI crossoverStage(Classifier Regression, populationI Population){
    Crossover.setData(crossoverRate, Population);
    Crossover.setWekainfo(Regression);
    Crossover.startCrossover();
    Population = Crossover.getCrossoverResult();

    return Population;
  }

  public populationI mutationStage(Classifier Regression, populationI Population){
    Mutation.setData(mutationRate, Population);
    Mutation.setWekainfo(Regression);
    Mutation.startMutation();
    Population = Mutation.getMutationResult();

    return Population;
  }


}