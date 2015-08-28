package openga.operator.clone;
import openga.chromosomes.*;
import openga.operator.miningGene.*;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class solutionVectorCloneWithMiningGene extends solutionVectorCloneWithMutation {
  public solutionVectorCloneWithMiningGene() {
  }
  openga.operator.miningGene.geneStatistics5 geneStatistics
        = new openga.operator.miningGene.geneStatistics5();
  populationI archive;

  public void setArchive(populationI archive){
     this.archive = archive;
  }

  public void startToClone(){
    numberOfOverlapped = 0;
    populationI duplicatedPopulation = new population();
    duplicatedPopulation.setGenotypeSizeAndLength(true, 1,
                                                  length,
                                                  numberOfObjs);
    duplicatedPopulation.initNewPop();
    geneStatistics.setData(originalPop, duplicatedPopulation);
    geneStatistics.startStatistics();
    chromosome artifificialChromosome = geneStatistics.getPopulation().getSingleChromosome(0);

    for(int i = 0 ; i < sizeOfPop - 1 ; i ++ ){
      chromosome template = new chromosome();
      int counter = 0;
      for(int j = i + 1; j < sizeOfPop ; j ++ ){
        if(checkIdenticalSoln(originalPop.getSingleChromosome(i), originalPop.getSingleChromosome(j))){
          template = geneStatistics.mateSolutions(artifificialChromosome, archive.getSingleChromosome(counter ++));
          /*
          if(counter == 0){//the template is null.
            template = geneStatistics.mateSolutions(artifificialChromosome, originalPop.getSingleChromosome(j));
          }
          else{
            template = generateNewSolution(originalPop.getSingleChromosome(j));
          }
              */
          originalPop.setSingleChromosome(j, template);
          counter = counter % archive.getPopulationSize();
          numberOfOverlapped ++;
        }
      }
    }
  }
}