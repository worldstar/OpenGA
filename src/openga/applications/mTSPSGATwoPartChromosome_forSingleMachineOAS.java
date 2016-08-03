package openga.applications;
import openga.chromosomes.*;
import openga.operator.selection.*;
import openga.operator.crossover.*;
import openga.operator.mutation.*;
import openga.operator.localSearch.*;
import openga.ObjectiveFunctions.*;
import openga.MainProgram.*;
import openga.Fitness.*;
/*
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Cheng Shiu University</p>
 * @authors Chen, Shih-Hsin ; Chang, Yu-Tang
 * @version 1.0
 * We obtain the instance from OASLIB.
 * Reference "A tabu search algorithm for order acceptance and scheduling", http://home.ku.edu.tr/~coguz/Research/Dataset_OAS.zip
 */

public class mTSPSGATwoPartChromosome_forSingleMachineOAS extends TSP {
  
  int numberOfSalesmen = 3;
  int type = 2; //type = 0 : mtsp,  type = 2 : OA,  type = 3 : TCX
  singleThreadGA GaMain;
  SelectI Selection;
  CrossoverMTSPI Crossover;
  MutationMTSPI Mutation;
  localSearchMTSPI localSearch1;
  populationI Population2;
  ObjectiveFunctionOASI[] ObjectiveFunction;
  String instanceName = "";
  boolean applyLocalSearch = true;
  
  public double [] r;       //  release date.
  public double [] p;       //  processing time
  public double [] d;       //  due-date
  public double [] d_bar;   //  deadline
  public double [] e;       //  revenue
  public double [] w;       //  weight
  public double [][] s;     //  setup times
  
  public mTSPSGATwoPartChromosome_forSingleMachineOAS() {
  }
  
  public void setParameter(double crossoverRate, double mutationRate, int counter, double elitism, int generation,
          int type, int numberOfSalesmen, int cities, String instanceName,
          double[] r, double[] p, double[] d, double[] d_bar, double[] e, double[] w, double[][] s)
  {
   this.DEFAULT_crossoverRate = crossoverRate;
   this.DEFAULT_mutationRate = mutationRate;
   this.counter = counter;
   this.elitism = elitism;
   this.DEFAULT_generations = generation;
   this.type = type;
   this.numberOfSalesmen = numberOfSalesmen;
   this.length = cities;
   this.instanceName = instanceName;
   this.r = r;
   this.p = p;
   this.d = d;
   this.d_bar = d_bar;
   this.e = e;
   this.w = w;
   this.s = s;
  }
  
  public void initiateVars(){   
    GaMain     = new singleThreadGAwithInitialPop();//singleThreadGAwithMultipleCrossover singleThreadGA adaptiveGA
    Population = new population();
    Selection  = new binaryTournament();
    Crossover  = new TCSCFCrossover(type);
    Mutation   = new swapMutationTwoPart();//TwoPartMTSPMutation
    ObjectiveFunction = new ObjectiveFunctionOASI[numberOfObjs];
    ObjectiveFunction[0] = new TPObjectiveFunctionforOAS();//the first objective
    Fitness    = new singleObjectiveFitness();//singleObjectiveFitness singleObjectiveFitnessByNormalize
    objectiveMinimization = new boolean[numberOfObjs];
    objectiveMinimization[0] = true;
    encodeType = true;

    if(numberOfSalesmen >= length){
      System.out.println("The number of salesmen is "+numberOfSalesmen+",\nThe cities is "+length+",\nwhich should be greater than the number of visiting locations.");
      System.out.println("The program will exit.");
      System.exit(0);
    }    
    
    Population.setGenotypeSizeAndLength(encodeType, DEFAULT_PopSize, length + numberOfSalesmen, numberOfObjs);
    Population.createNewPop();
    
    for(int i = 0 ; i < DEFAULT_PopSize ; i ++){
      Population.getSingleChromosome(i).generateTwoPartPop(length + numberOfSalesmen, numberOfSalesmen);
    }
    
    Crossover.setNumberofSalesmen(numberOfSalesmen);
    Mutation.setNumberofSalesmen(numberOfSalesmen);
    
//    localSearch1 = new localSearchBy2OptForMTSP(); 

//      System.out.println(r);
     ObjectiveFunction[0].setOASData(r, p, d, d_bar, e, w, s, numberOfSalesmen);
     
    //set the data to the GA main program.
    /*Note: the gene length is problem size + numberOfSalesmen*/
    GaMain.setData(Population, Selection, Crossover, Mutation, ObjectiveFunction, Fitness, DEFAULT_generations,
            DEFAULT_initPopSize,DEFAULT_PopSize, length + numberOfSalesmen , DEFAULT_crossoverRate, DEFAULT_mutationRate,
            objectiveMinimization, numberOfObjs, encodeType, elitism);    
    //GaMain.setLocalSearchOperator(localSearch1, applyLocalSearch, 20);
  }
  
  public chromosome moveGenes(chromosome _chromosome, int cutPoint1, int cutPoint2){
    //Because the original cutPoint1 is always less than cutPoint2, we shuffle the position.
    if(Math.random() < 0.5){
      int tempIndex = cutPoint1;
      cutPoint1 = cutPoint2;
      cutPoint2 = tempIndex;
    }
    //To move the number of cities from cutPoint2 to cutPoint1 and it ensures the
    //number of visiting city of the salesmen at cutPoint2 is at least one city.
    int maxMovedCities = (int)((_chromosome.genes[cutPoint2] - 1)*Math.random()*.5);
    _chromosome.genes[cutPoint1] += maxMovedCities;
    _chromosome.genes[cutPoint2] -= maxMovedCities;
    return _chromosome;
  }
  
  @Override
  public void start(){
    openga.util.timeClock timeClock1 = new openga.util.timeClock();
    timeClock1.start();
    GaMain.startGA();
    timeClock1.end();

    String implementResult = instanceName+"\t"+DEFAULT_crossoverRate+"\t"+DEFAULT_mutationRate+"\t"+type+"\t"+numberOfSalesmen
        +"\t"+GaMain.getArchieve().getSingleChromosome(0).getObjValue()[0]
        +"\t"+timeClock1.getExecutionTime()/1000.0+"\n";
    writeFile("mTSPSGATwoPartChromosome_forSingleMachineOAS20160718MaxRevenueFull", implementResult);
    System.out.print(implementResult);
    //System.out.print("\n");
    //System.out.print(GaMain.getArchieve().getSingleChromosome(0).toString1());
    //System.out.print("\n");
  }
  
  public static void main(String[] args) {
    
    System.out.println("mTSPSGATwoPartChromosome_forSingleMachineOAS20160718MaxRevenueFull");
    
    double[] crossoverRate = new double[]{0.5};//1, 0.5 [0.5]
    double[] mutationRate  = new double[]{0.1};//0.1, 0.5 [0.1]
    int counter = 0;
    double elitism[] = new double[]{0.1};
    int generations[] = new int[]{1000};//1000
    int numberOfSalesmen[] = new int[]{2};//3, 5, 10, 20, 30
    int type = 2;//0: All salesmen reserve the same sites,2: Last salesmen reserve the same sites,3: TCX (Original)
    int repeat = 1;
    
    //Test Parameter
    int[] orders = new int[]{10};
    int[] Tao = new int[]{1};
    int[] R = new int[]{1};
    int instanceReplications = 2;
    
    //Real Parameter
//    int[] orders = new int[]{10,15,20,25,50,100};
//    int[] Tao = new int[]{1,3,5,7,9};
//    int[] R = new int[]{1,3,5,7,9};
//    int instanceReplications = 10;
    
    for(int i = 0; i < orders.length; i++){
      for(int j = 0; j < Tao.length; j++){
        for(int k = 0; k < R.length; k++){
          for(int l = 0; l < instanceReplications; l++){
            
            openga.applications.data.OASInstances OASInstances1 = new openga.applications.data.OASInstances();
            String instanceName = new String(".\\instances\\SingleMachineOAS\\"+orders[i]+"orders\\Tao"+Tao[j]+"\\R"+R[k]+"\\Dataslack_"+orders[i]+"orders_Tao"+Tao[j]+"R"+R[k]+"_"+(l+1)+".txt");

//            System.out.println(instanceName);
            OASInstances1.setData(instanceName);
            OASInstances1.getDataFromFile();

            for(int m = 0 ; m < crossoverRate.length ; m ++ ){
              for(int n = 0 ; n < mutationRate.length ; n ++ ){
                for(int o = 0 ; o < elitism.length ; o ++ ){
                  for(int p = 0 ; p < numberOfSalesmen.length ; p ++){
                    for(int r = 0 ; r < repeat ; r ++ ){
                      
//                      System.out.print("counter "+counter+" ");
                      mTSPSGATwoPartChromosome_forSingleMachineOAS TSP1 = new mTSPSGATwoPartChromosome_forSingleMachineOAS();
                      
                      TSP1.setParameter(crossoverRate[m], mutationRate[n], counter, elitism[o], generations[0],
                              type, numberOfSalesmen[p], OASInstances1.getSize(), instanceName, 
                              OASInstances1.getR(), OASInstances1.getP(), OASInstances1.getD(), OASInstances1.getD_bar(), OASInstances1.getE(), OASInstances1.getW(), OASInstances1.getS());
                      
                      TSP1.initiateVars();
                      TSP1.start();
                      counter ++;
                    }
                  }
                }
              }
            }
            
            
          }
        }
      }
    }
    
    System.exit(0);
  }
}
