package openga.operator.miningGene;
import java.util.ArrayList;
import openga.chromosomes.chromosome;
import openga.chromosomes.populationI;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.LinearRegression;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class PopulationToInstances {
  
  public Instances PopulationToInstances(populationI population) throws Exception {
    int Pop_Size = population.getPopulationSize();
//    System.out.println(Pop_Size);
    chromosome Chromosome = null;
    double[][] ALLdata = new double[Pop_Size][population.getSingleChromosome(0).genes.length + 1];
    for (int ii = 0; ii < Pop_Size; ii++) {
//      System.out.print(ii + 1 + "：");
      Chromosome = population.getSingleChromosome(ii);
      for (int jj = 0; jj <= Chromosome.getLength(); jj++) {
        if (jj != Chromosome.getLength()) {
          ALLdata[ii][jj] = Chromosome.genes[jj];
        } else {
//          ALLdata[ii][jj] = Chromosome.getNumberOfObjective();
//          ALLdata[ii][jj] = Chromosome.getScalarizedObjectiveValue();
          ALLdata[ii][jj] = Chromosome.getObjValue()[0];
        }//        System.out.print(ALLdata[ii][jj] + " ");
      }//      System.out.println();
    }
    
//        double[][] data = {{9,8,24,12,14,1,18,21,13,4,15,7,17,23,6,20,25,11,19,5,22,10,2,3,16,7,18,192.741},
//                           {23,12,21,14,4,8,18,1,15,20,2,5,25,11,7,22,10,24,16,3,19,9,6,13,17,18,7,192.576},
//                           {20,24,21,14,11,22,16,9,13,12,4,25,3,6,7,17,19,1,8,5,23,10,18,15,2,18,7,192.8}};        
        
        int numInstances = ALLdata.length;
        int numDimensions = ALLdata[0].length; // =29  (0~28)
        //  賦予屬性值
        ArrayList<Attribute> atts = new ArrayList<Attribute>();
        for (int i = 0; i < numDimensions; i++) {
            atts.add(new Attribute("Attribute" + i));
        }        
        Instances dataset = new Instances("myDataset", atts, 1);
        dataset.setClassIndex(dataset.numAttributes() - 1);
        for (int i = 0; i < numInstances; i++) {
            Instance ins = new DenseInstance(1.0, ALLdata[i]);
            ins.setDataset(dataset);
            dataset.add(ins);
        }
//        System.out.println(dataset);  // ★★★ this check Instances  ★★★   

//        DataSource source = new DataSource("D:\\eclipse-workspace\\OpenGA\\OASforSMSP_Co2_20181129MinCo2Cost.csv"); //D:\\Weka\\Weka-3-8\\data\\iris.arff
//        Classifier Regression = new LinearRegression();
//        Regression.buildClassifier(dataset);
//        // 評估模型
//        Evaluation eval = new Evaluation(dataset);
//        double result[] = eval.evaluateModel(Regression, dataset);
//        // 輸出摘要結果
//        System.out.println(eval.toSummaryString(false)+"\n"+result[0]+"\n"+result[1]); 
    
    return dataset;
  }

  
//  public static void main(String[] args) throws Exception {
//        double[][] data = {{9,8,24,12,14,1,18,21,13,4,15,7,17,23,6,20,25,11,19,5,22,10,2,3,16,7,18,192.741},
//                           {23,12,21,14,4,8,18,1,15,20,2,5,25,11,7,22,10,24,16,3,19,9,6,13,17,18,7,192.576}};
//        int numInstances = data.length;
//        int numDimensions = data[0].length; // =29  (0~28)
//        //  賦予屬性值
//        ArrayList<Attribute> atts = new ArrayList<Attribute>();
//        for (int i = 0; i < numDimensions; i++) {
//            atts.add(new Attribute("Attribute" + i));
//        }        
//        Instances dataset = new Instances("myDataset", atts, 1);
//        dataset.setClassIndex(dataset.numAttributes() - 1);
//        for (int i = 0; i < numInstances; i++) {
//            Instance ins = new DenseInstance(1.0, data[i]);
//            ins.setDataset(dataset);
//            dataset.add(ins);
//        }
//        System.out.println(dataset);  
//        
//        DataSource source = new DataSource("D:\\eclipse-workspace\\OpenGA\\OASforSMSP_Co2_20181129MinCo2Cost.csv"); //D:\\Weka\\Weka-3-8\\data\\iris.arff
//        Classifier Regression = new LinearRegression();
//        Regression.buildClassifier(dataset);
//        // 評估模型
//        Evaluation eval = new Evaluation(dataset);
//        double result[] = eval.evaluateModel(Regression, dataset);
//        // 輸出摘要結果
//        System.out.println(eval.toSummaryString(false)+"\n"+result[0]+"\n"+result[1]);   
//  }
  
}
