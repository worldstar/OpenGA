package openga.operator.crossover;
import openga.chromosomes.*;
/**
 * <p>Title: The OpenGA project</p>
 * <p>Description: The project is to build general framework of Genetic algorithm and problem independent.</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public interface CrossoverI {
    public void setData(double crossoverRate, chromosome _chromosomes[],int numberOfObjs);
    public void setData(int numberOfJob, int numberOfMachines, int processingTime[][]);
    public void setData(double crossoverRate, populationI originalPop);
    public void setData(int numberofParents);    
    public void startCrossover();
    public populationI getCrossoverResult();
}