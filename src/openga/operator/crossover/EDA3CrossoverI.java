/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package openga.operator.crossover;

/**
 *
 * @author user2
 */
public interface EDA3CrossoverI extends EDAICrossover{
  public void setEDAinfo(double[][] container, double[][] inter, int numberOfCrossoverTournament, int D1 , int D2);
  public void setEDAinfo(int D1 , int D2);
}
