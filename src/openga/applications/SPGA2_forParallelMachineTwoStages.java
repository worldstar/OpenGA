package openga.applications;

/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class SPGA2_forParallelMachineTwoStages extends SPGA2_forParallelMachine {
  public SPGA2_forParallelMachineTwoStages() {
  }

  public void start(){
    //we control the number of iteration of GA here.
    //to run the first and the last sub-population at the first stage.
    int popIndex[] = new int[]{0, 1, 2, 3, 4, 35, 36, 37, 38, 39};
    for(int m = 0 ; m < DEFAULT_generations ; m ++ ){
      //System.out.println("Generation: "+m);
      for(int i = 0 ; i < popIndex.length ; i ++ ){
        //System.out.println("numberOfSubPopulations "+i);
        if(m == 0){//initial each population and its objective values.
          GaMain[popIndex[i]].InitialRun();
          if(i == 0){
            GaMain[i].initFirstArchieve();
          }
          else{
            //GaMain[i].updateNondominatedSon();
          }
        }
        GaMain[popIndex[i]].startGA();
      }
    }

    for(int m = 0 ; m < DEFAULT_generations ; m ++ ){
      //System.out.println("Generation: "+m);
      for(int i = 0 ; i < numberOfSubPopulations ; i ++ ){
        //to skip the two center sub-populations
        if(i < 15 || i > 24){
          //System.out.println("numberOfSubPopulations "+i);
          if(m == 0 && (checkGroup(i, popIndex))){//initial each population and its objective values.
            GaMain[i].InitialRun();
          }
          GaMain[i].startGA();
        }
      }
    }

    //to output the implementation result.
    String implementResult = "";
    double refSet[][] = getReferenceSet();

    double objArray[][] = GaMain[0].getArchieveObjectiveValueArray();
    implementResult = numberOfJob+"\t"+calcSolutionQuality(refSet, objArray)+"\n";//
    objArray = null;
    writeFile("SPGA2_forParallelMachineTwoStages", implementResult);

/*
    implementResult = "";
    for(int k = 0 ; k < GaMain[0].getArchieve().getPopulationSize() ; k ++ ){
      implementResult += GaMain[0].getArchieve().getSingleChromosome(k).toString1()+",\t";//sequnence
      for(int j = 0 ; j < numberOfObjs ; j ++ ){//for each objectives
        implementResult += GaMain[0].getArchieve().getObjectiveValues(k)[j]+"\t";
      }
      implementResult += "\n";
    }
    writeFile("SPGA2_forParallelMachineParetoSet"+numberOfJob, implementResult);
*/
    for(int i = 0 ; i < numberOfSubPopulations ; i ++ ){
      GaMain[i].destroidArchive();
      GaMain[i] = null;
    }
  }

  public boolean checkGroup(int index, int[] group){
    for(int i = 0 ; i < group.length ; i ++ ){
      if(index == group[i]){
        return false;
      }
    }
    return true;
  }


  public static void main(String[] args) {
    System.out.println("SPGA 2 Parallel Two Stage New Paretp Solns");
    int numberOfSubPopulations[] = new int[]{40};//10, 20, 30, 40
    int popSize[] = new int[]{200};//100, 155, 210
    int numberOfJob[] = new int []{35, 50, 65};//35, 50, 65
    int numberOfMachines[] = new int[]{10, 15, 18};//10, 15, 18
    int totalSolnsToExamine[] = new int[]{1000000};
    boolean applySecCRX[] = new boolean[]{false};//false, true
    boolean applySecMutation[] = new boolean[]{false};

    double crossoverRate = 0.9,
           mutationRate = 0.1,
           elitism = 0.2;
    int repeatExperiments = 30;
    int counter = 0;

    for(int i = 0 ; i < repeatExperiments ; i ++ ){
      for(int r = 0 ; r < numberOfJob.length ; r ++ ){
        System.out.println(numberOfJob[r]+"\t"+numberOfSubPopulations[0]+"\t"+ popSize[0]);
        System.out.println("combinations: "+counter++);
        SPGA2_forParallelMachineTwoStages SPGA2_forParallelMachine1 = new SPGA2_forParallelMachineTwoStages();
        SPGA2_forParallelMachine1.setParallelMachineData(numberOfJob[r], numberOfMachines[r]);
        SPGA2_forParallelMachine1.setParameters(numberOfSubPopulations[0], popSize[0],
            totalSolnsToExamine[0], crossoverRate, mutationRate, elitism, applySecCRX[0], applySecMutation[0]);
        SPGA2_forParallelMachine1.initiateVars();
        SPGA2_forParallelMachine1.start();
        SPGA2_forParallelMachine1 = null;
        System.gc();
      }//end r
    }// end i

    System.exit(0);
  }

}