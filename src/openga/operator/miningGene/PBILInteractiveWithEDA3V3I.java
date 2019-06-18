/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package openga.operator.miningGene;

import openga.MainProgram.EDAMainI;

/**
 *
 * @author Kuo Yu-Cheng
 */
public interface PBILInteractiveWithEDA3V3I extends EDAMainI {
  
    public void setEDAinfo(double lamda,double beta, int numberOfCrossoverTournament, 
            int numberOfMutationTournament, int startingGenDividen 
            , boolean OptMin , int epoch );
  
}
