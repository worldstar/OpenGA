/*
 * Implements the Self-Guided GA to solve the Multidimensional knapsack problems.
 * The test instances are drawn from OR-Library. 
 * http://people.brunel.ac.uk/~mastjjb/jeb/orlib/mknapinfo.html
 */

package openga.applications.knapsack;
import openga.chromosomes.*;
import openga.operator.selection.*;
import openga.operator.crossover.*;
import openga.operator.mutation.*;
import openga.operator.clone.*;
import openga.operator.repair.*;
import openga.ObjectiveFunctions.*;
import openga.MainProgram.*;
import openga.ObjectiveFunctions.*;
import openga.Fitness.*;
import openga.util.printClass;
import openga.util.fileWrite1;
import openga.applications.data.knapsackORLibProblems;
/**
 *
 * @author user
 */
public class knapsackSelfGuidedGA {

  public static void main(String[] args) {
    System.out.println("knapsackSelfGuidedGA20101101");
    int popSize[] = new int[]{100};//100, 155, 210
    int numberOfItem[] = new int []{100};//100, 250, 500
    int numberOfKnapsack[] = new int[]{5, 10, 30};
    int totalSolnsToExamine[] = new int[]{75000, 100000, 125000};//100000, 1000000, 1500000, 2000000
    boolean applySecCRX[] = new boolean[]{false};//false, true
    boolean applySecMutation[] = new boolean[]{false};
    int tournamentSize[] = new int[]{2, 10};

    double crossoverRate[] = new double[]{0.8},
           mutationRate[] = new double[]{0},
           elitism[] = new double[]{0.15};
    int repeatExperiments = 30;
    int counter = 0;



    for(int i = 0 ; i < numberOfItem.length ; i ++ ){
      for(int j = 0 ; j < numberOfKnapsack.length ; j ++ ){
        knapsackORLibProblems knapsackORLibProblems1 = new knapsackORLibProblems();
        knapsackORLibProblems1.readInstanceData(numberOfItem[i], numberOfKnapsack[j]);

        int numberOfInstanceReplications = knapsackORLibProblems1.getInstanceReplications();
//        int profit[][] = knapsackORLibProblems1.getProfit();
//        int weights[][][] = knapsackORLibProblems1.getWeights();
//        int rightHandSide[][] = knapsackORLibProblems1.getRightHandSide();

        //repeatExperiments

        for(int k = 0 ; k < numberOfInstanceReplications ; k ++){
          for(int m = 0 ; m < crossoverRate.length ; m ++){
            for(int n = 0 ; n < mutationRate.length ; n ++){
              String instanceName = "knapsack"+numberOfItem[i]+"-"+numberOfKnapsack[j]+"-"+numberOfInstanceReplications;
              
            }
          }
        }

      }
    }

  }
}
