package openga.applications;
import openga.chromosomes.*;
import openga.operator.selection.*;
import openga.operator.crossover.*;
import openga.operator.mutation.*;
import openga.ObjectiveFunctions.*;
import openga.MainProgram.*;
import openga.ObjectiveFunctions.*;
import openga.Fitness.*;
import openga.util.printClass;
import openga.util.fileWrite1;

/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class SPGA2_forFlowshopWithSuburbanDistrict extends SPGA2_forFlowShop {
  public SPGA2_forFlowshopWithSuburbanDistrict() {
  }

  public void start(){
    //we control the number of iteration of GA here.
    for(int m = 0 ; m < DEFAULT_generations ; m ++ ){
      //System.out.println("Generation: "+m);

      for(int i = 0 ; i < numberOfSubPopulations ; i ++ ){
        //System.out.println("numberOfSubPopulations "+i);
        if(m == 0){//initial each population and its objective values.
          GaMain[i].InitialRun();
          if(i == 0){
            GaMain[i].initFirstArchieve();
          }
          else{
            //GaMain[i].updateNondominatedSon();
          }
        }
        GaMain[i].startGA();
      }
      //System.out.println("GaMain[i].getAarchieve.getPopulationSize() "+GaMain[0].getArchieve().getPopulationSize()+"\t");
    }

    //to output the implementation result.
    String implementResult = "";
    double refSet[][] = getReferenceSet();

    double objArray[][] = GaMain[0].getArchieveObjectiveValueArray();
    implementResult = numberOfJob+"\t"+alpha+"\t"+calcSolutionQuality(refSet, objArray)+"\n";//
    objArray = null;
    writeFile("SPGA2forFlowShopwithSubanDistrictAlpha", implementResult);

    for(int i = 0 ; i < numberOfSubPopulations ; i ++ ){
      GaMain[i].destroidArchive();
      GaMain[i] = null;
    }
  }

  double alpha = 0.2;
  public void setAlphas(double alpha){
    this.alpha = alpha;
  }

  public static void main(String[] args) {
    System.out.println("SPGA2forFlowShopwithSubanDistrictAlpha");

    int numberOfSubPopulations[] = new int[]{35};
    int popSize[] = new int[]{200};
    int numberOfJob[] = new int []{20, 40, 60, 80};
    int numberOfMachines = 20;
    int totalSolnsToExamine[] = new int[]{1000000};//1000000, 1500000, 2000000
    boolean applySecCRX[] = new boolean[]{false, true};//false, true
    boolean applySecMutation[] = new boolean[]{false, true};
    double intervals[] = new double []{0.25, 0.75};//factor A
    double stepLength[] = new double[]{0.01, 0.1};//factor B
    int generationChange[] = new int[]{1, 30};//factor C

    double crossoverRate[] = new double[]{1},
           mutationRate[] = new double[]{0.18},
           elitism = 0.2;
    int repeatExperiments = 10;
    double alpha[] = new double[]{0.2, 0.5};

    //to form a text file and write the title in it.
    /*
    String implementResult = "numberOfJob\t numberOfSubPopulations\t popSize\t applySecCRX\t applySecMutation\t D1r\n";
    SPGA2_forFlowShop writeFile1 = new SPGA2_forFlowShop();
    writeFile1.writeFile("SPGA2forFlowShop", implementResult);
    */
    int counter = 0;
    for(int i = 0 ; i < repeatExperiments ; i ++ ){
      for(int j = 0 ; j < numberOfJob.length ; j ++ ){
        for(int k = 0 ; k < alpha.length ; k ++ ){
          SPGA2_forFlowshopWithSuburbanDistrict SPGA2_forFlowShop1 = new SPGA2_forFlowshopWithSuburbanDistrict();
          System.out.println("combinations: "+counter++);
          //double lbWeight, double interval, double stepLength, int generationChange){
          SPGA2_forFlowShop1.setAlphas(alpha[k]);
          //SPGA2_forFlowShop1.setWeightBounds(0, intervals[1], stepLength[1], generationChange[1]);
          SPGA2_forFlowShop1.setFlowShopData(numberOfJob[j], numberOfMachines);

          SPGA2_forFlowShop1.setParameters(numberOfSubPopulations[0], popSize[0],
              totalSolnsToExamine[0], crossoverRate[0], mutationRate[0],
              elitism, applySecCRX[0], applySecMutation[0]);
          SPGA2_forFlowShop1.initiateVars();
          SPGA2_forFlowShop1.start();
          SPGA2_forFlowShop1 = null;
          System.gc();
        }
      }
    }

    /*
    for(int i = 0 ; i < repeatExperiments ; i ++ ){
      for(int j = 0 ; j < numberOfJob.length ; j ++ ){
        SPGA2_forFlowshopWithSuburbanDistrict SPGA2_forFlowShop1 = new SPGA2_forFlowshopWithSuburbanDistrict();
        System.out.println("combinations: "+counter++);
        //double lbWeight, double interval, double stepLength, int generationChange){

        //SPGA2_forFlowShop1.setWeightBounds(0, intervals[1], stepLength[1], generationChange[1]);
        SPGA2_forFlowShop1.setFlowShopData(numberOfJob[j], numberOfMachines);

        SPGA2_forFlowShop1.setParameters(numberOfSubPopulations[0], popSize[0],
            totalSolnsToExamine[0], crossoverRate[0], mutationRate[0],
            elitism, applySecCRX[0], applySecMutation[0]);
        SPGA2_forFlowShop1.initiateVars();
        SPGA2_forFlowShop1.start();
        SPGA2_forFlowShop1 = null;
        System.gc();
      }
    }

    openga.applications.data.fourFactorFullFactorial threeFactor1 = new openga.applications.data.fourFactorFullFactorial();
    int settings[][] = threeFactor1.getCombinations();
    for(int i = 0 ; i < repeatExperiments ; i ++ ){
      for(int j = 0 ; j < settings.length ; j ++ ){
        SPGA2_forFlowshopWithSuburbanDistrict SPGA2_forFlowShop1 = new SPGA2_forFlowshopWithSuburbanDistrict();
        System.out.println("combinations: "+counter++);
        //double lbWeight, double interval, double stepLength, int generationChange){
        //SPGA2_forFlowShop1.setWeightBounds(0, intervals[settings[j][0]], stepLength[settings[j][1]], generationChange[settings[j][2]]);
        SPGA2_forFlowShop1.setFlowShopData(numberOfJob[settings[j][3]], numberOfMachines);

        SPGA2_forFlowShop1.setParameters(numberOfSubPopulations[0], popSize[0],
            totalSolnsToExamine[0], crossoverRate[0], mutationRate[0],
            elitism, applySecCRX[0], applySecMutation[0]);
        SPGA2_forFlowShop1.initiateVars();
        SPGA2_forFlowShop1.start();
        SPGA2_forFlowShop1 = null;
        System.gc();
      }
    }
    */
   /*
    //For test weight.

    for(int j = 0 ; j < 35 ; j ++ ){
      SPGA2_forFlowshopWithSuburbanDistrict SPGA2_forFlowShop1 = new SPGA2_forFlowshopWithSuburbanDistrict();
      SPGA2_forFlowShop1.calcWeightsForEachSubPop(j);
    }
    */
  }

}