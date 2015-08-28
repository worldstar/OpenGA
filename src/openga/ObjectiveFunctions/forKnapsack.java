package openga.ObjectiveFunctions;

/**
 * <p>Title: The OpenGA project</p>
 * <p>Description: The project is to build general framework of Genetic algorithm and problem independent.</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class forKnapsack {
  public forKnapsack() {
  }

  double profit, weight;

  private void calcObjectives() {
      profit = 0;
      weight = 0;

      /*
      for (int i = 0; i < genotype.length; i++) {
          if (genotype[i] == true) {
              profit += Params.items[i].profit;
              weight += Params.items[i].weight;
          }
      }
      valid = true;
        */
  }

  public static void main(String[] args) {
    forKnapsack forKnapsack1 = new forKnapsack();
  }

}