/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package openga.applications.flowshopProblem;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import openga.chromosomes.chromosome;
import openga.chromosomes.populationI;

/********************************
 * <p>Title: School_Program </p>
 * <p>Description: Get the values of the file and use the Euclidean formula to calculate the distance sum.</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: Cheng Shiu University </p>
 * @author Kuo, Yu-Cheng
 * @version 1.0
 */
public class DistanceCalculation implements DistanceCalculationI {

    /**
     * @param args the command line arguments
     */
    populationI population;
    int length, indexOfObjective;
    double distanceMatrix[][];
    double distanceToOriginal[];
    
    private String readTxtPath = "@../../instances/TSP/test10.txt";
    private String[] STxt;
    private int pointSum;
    private double[] startingPoint;
    private double[] DistanceDataX;
    private int xCount = 5;
    private double[] DistanceDataY;
    private int yCount = 6;
    private double[] startingDistanceData;
    private double[][] DistanceData;
    private double totalDistance = 0;
    private int[] Sequence;

    private void readTxt() throws FileNotFoundException, IOException {
        FileReader fr = new FileReader(readTxtPath);
        BufferedReader br = new BufferedReader(fr);
        String TxtAll = "", eachLine = "";
        while ((eachLine = br.readLine()) != null) {
            TxtAll += eachLine + "\n";
        }
        STxt = TxtAll.split(" |\n");
        pointSum = (Integer.parseInt(STxt[0]) - 1);
        DistanceDataX = new double[pointSum];
        DistanceDataY = new double[pointSum];
        DistanceData = new double[pointSum][pointSum];

        startingDistanceData = new double[pointSum];
        Sequence = new int[Integer.parseInt(STxt[0])];
//        for(int i = 0 ;  i < STxt.length ; i ++) 
//        {
//            System.out.println(STxt[i]);
//        }
        startingPoint = new double[]{Double.parseDouble(STxt[2]), Double.parseDouble(STxt[3])};
        for (int i = 0; i < pointSum; i++) {
            DistanceDataX[i] = Double.parseDouble(STxt[xCount]);
            xCount += 3;
            DistanceDataY[i] = Double.parseDouble(STxt[yCount]);
            yCount += 3;
//            System.out.print(i + " : " + DistanceDataX[i] + "," + DistanceDataY[i] + "\n");
        }
//        System.out.println("startingPoint : " + startingPoint[0] + "," + startingPoint[1]);
    }

    private Double euclideanDistance(Double x1, Double y1, Double x2, Double y2) {
        Double result;
        result = Math.pow((Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2)), 0.5);
        return result;
    }

    private void distanceCalculation() {
        for (int i = 0; i < startingDistanceData.length; i++) {
            startingDistanceData[i] = euclideanDistance(startingPoint[0], startingPoint[1], DistanceDataX[i], DistanceDataY[i]);
//            System.out.println((0) + "," + (i + 1) + " = " + startingDistanceData[i]);
        }

//        System.out.println("=================================");
        for (int i = 0; i < DistanceDataX.length; i++) {
            for (int j = 0; j < DistanceDataY.length; j++) {
                DistanceData[i][j] = euclideanDistance(DistanceDataX[i], DistanceDataY[i], DistanceDataX[j], DistanceDataY[j]);
//                    System.out.println((i + 1 ) + "," + (j + 1) + " = " + DistanceData[i][j]);
            }
        }
    }

    private void totalDistance() {
        totalDistance += startingDistanceData[Sequence[1] - 1];

        for (int i = 1; i < Sequence.length; i++) {
//            System.out.println(Sequence[i]);
            if (i < pointSum) {
                totalDistance += DistanceData[Sequence[i] - 1][Sequence[i + 1] - 1];
//            System.out.println(DistanceData[Sequence[i] - 1][Sequence[i + 1] - 1]);
            } else {
                totalDistance += startingDistanceData[Sequence[i] - 1];
//             System.out.println(startingDistanceData[Sequence[i] - 1]);
            }
        }
        System.out.println("totalDistance : " + totalDistance);
    }

    private void setSquence() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Random order please enter : 0");
        System.out.println("Manual order Please enter : 1");
        System.out.print("please enter : ");
        String scan = scanner.nextLine();

        if (scan.equals("0")) {
            int ran;
            Random r = new Random();
            for (int i = 1; i < Sequence.length; i++) {
                Sequence[0] = (Integer.parseInt(STxt[1]) - 1);
                ran = r.nextInt(Integer.parseInt(STxt[0]));
                Sequence[i] = ran;
                for (int j = 0; j < i; j++) {
                    if (Sequence[j] == ran) {
                        i -= 1;
                    }
                }
            }

            System.out.print("Sequence = ");
            for (int i = 0; i < Sequence.length; i++) {
                System.out.print(Sequence[i] + "\t");
            }
            System.out.println();
        } else if (scan.equals("1")) {
            Sequence[0] = (Integer.parseInt(STxt[1]) - 1);
            System.out.println("origin : " + Sequence[0]);
            System.out.println("Please enter the path order : 1 ~ " + pointSum);
            for (int i = 1; i < Sequence.length; i++) {
                Sequence[i] = (scanner.nextInt());
            }
        } else {
            System.out.println("Please enter the correct option or the correct file format!!");
        }
    }

    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        DistanceCalculation DC = new DistanceCalculation();
        DC.readTxt();
        DC.distanceCalculation();
        DC.setSquence();
        DC.totalDistance();
    }

  @Override
  public void setTSPData(double originalPoint[], double coordinates[][]){
    this.startingDistanceData = originalPoint;
    this.DistanceData = coordinates;
    
        DistanceCalculation DC = new DistanceCalculation();
      try {
        DC.readTxt();
      } catch (IOException ex) {
        Logger.getLogger(DistanceCalculation.class.getName()).log(Level.SEVERE, null, ex);
      }
        DC.distanceCalculation();
        DC.setSquence();
        DC.totalDistance();
    
//    openga.ObjectiveFunctions.util.generateMatrix_EuclideanDist generateMatrix_EuclideanDist1
//        = new openga.ObjectiveFunctions.util.generateMatrix_EuclideanDist();
//    generateMatrix_EuclideanDist1.setData(coordinates);
//    generateMatrix_EuclideanDist1.setDistanceMatrixData();
//    distanceMatrix = generateMatrix_EuclideanDist1.getMatrix();
//
//    openga.ObjectiveFunctions.util.generateMatrix_EuclideanDist2 generateMatrix_EuclideanDist21
//        = new openga.ObjectiveFunctions.util.generateMatrix_EuclideanDist2();
//    generateMatrix_EuclideanDist21.setData(originalPoint, coordinates);
//    generateMatrix_EuclideanDist21.setDistanceMatrixData();
//    distanceToOriginal = generateMatrix_EuclideanDist21.getMatrix2();
//    openga.util.printClass printClass1 = new openga.util.printClass();
//    printClass1.printMatrix("distanceToOriginal", distanceToOriginal);
  }

  @Override
  public void setTSPData(double originalPoint[], double coordinates[][], populationI PartIIChromosomes){
      System.out.println("openga.ObjectiveFunctions.ObjectiveFunctionTSP does support the setTSPData(double originalPoint[], double coordinates[][], populationI PartIIChromosomes).");
      System.out.println("The program will exit");
      System.exit(0);
  }

  @Override
  public void setTSPData(double originalPoint[], double coordinates[][], int numberOfSalesmen){
      System.out.println("openga.ObjectiveFunctions.ObjectiveFunctionTSP does support the setTSPData(double originalPoint[], double coordinates[][], populationI PartIIChromosomes).");
      System.out.println("The program will exit");
      System.exit(0);
  }

  @Override
  public void setObjectiveFunctionType(String typeName) {
    this.readTxtPath = typeName;
  }

  @Override
  public void setData(populationI population, int indexOfObjective) {
    this.population = population;
    this.length = population.getLengthOfChromosome();
    this.indexOfObjective = indexOfObjective;
  }

  @Override
  public void setData(chromosome chromosome1, int indexOfObjective) {
    System.out.println("The openga.ObjectiveFunctions.ObjectiveFunctionTSP doesn't support the setData(chromosome chromosome1, int indexOfObjective).");
    System.out.println("The program will exit.");
    System.exit(0);
  }

  @Override
  public void calcObjective() {
//    double obj;
//    double objectives[];
//
//    for(int i = 0 ; i < population.getPopulationSize() ; i ++ ){
//      objectives = population.getObjectiveValues(i);
//      obj = evaluateAll(population.getSingleChromosome(i));
//      objectives[indexOfObjective] = obj;
//      population.setObjectiveValue(i, objectives);
//    }
  }

  @Override
  public populationI getPopulation() {
    return population;
  }

  @Override
  public double[] getObjectiveValues(int index) {
    return population.getObjectiveValues(index);
  }  
}
