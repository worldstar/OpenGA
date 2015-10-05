package openga.applications.data;
import java.io.*;
import java.util.*;
/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class TSPInstances {
  public TSPInstances() {
  }
  /**
   * We obtain the instance from TSPLIB.
   * @return
   */
  String fileName, message = "";
  String Instances[] = new String[]{
    "instances/TSP/kroC100.txt",
    "instances/TSP/pr76.txt",
    "instances/TSP/tsp225.txt",  
    "instances/TSP/berlin52.txt",
    "instances/TSP/kroB100.txt", 
    "instances/TSP/kroB200.txt",
    "instances/TSP/att48.txt",  
    "instances/TSP/pr226.txt",
    "instances/TSP/lin318.txt", 
    "instances/TSP/rd400.txt", 
    "instances/TSP/pr264.txt",
    "instances/TSP/kroB150.txt",
    "instances/TSP/kroD100.txt", 
    "instances/TSP/kroE100.txt",
    "instances/TSP/lin105.txt",
    "instances/TSP/pr124.txt",
    "instances/TSP/mtsp51.txt",
    "instances/TSP/mtsp100.txt",
    "instances/TSP/mtsp150.txt",
    "instances/TSP/pr136.txt", 
    "instances/TSP/pr144.txt",
    "instances/TSP/pr152.txt", 
    "instances/TSP/pr299.txt", 
    "instances/TSP/rat99.txt", 
    "instances/TSP/rat195.txt",
    "instances/TSP/st70.txt",
    "instances/TSP/bier127.txt",
    //It's dismiss instances from ANOVA.
    "instances/TSP/ch130.txt",    
    "instances/TSP/eil101.txt",
    "instances/TSP/eil51.txt",
    "instances/TSP/eli76.txt",
    "instances/TSP/gr96.txt",
    "instances/TSP/kroa150.txt",
    "instances/TSP/kroa200.txt"
    };  

  double coordinates[][];
  double originalPoint[];
  double distanceMatrix[][];
  int size;

  public void setData(String fileName){
    this.fileName = fileName;
    if( fileName == null ){
        System.out.println( "Specify the file name please.");
        System.exit(1);
    }
  }

  public void getDataFromFile(){
    // Read customers from file
    //System.out.print( "Loading customers from file..." );
    //to generate file data into message variable.

    try
    {
        File file = new File( fileName );
        FileInputStream fis = new FileInputStream( file );
        DataInputStream in = new DataInputStream(fis);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String message = "", eachLine = "";

        while ((eachLine = br.readLine()) != null)   {
          message += eachLine + " ";
        }

        //System.out.println(message);
        StringTokenizer tokens = new StringTokenizer(message);
        //set the coordinates of depot.
        //int length = 0;

        //to set the coordination and demand of each customer
        size = Integer.parseInt(tokens.nextToken()) - 1;
        originalPoint = new double[2];
        coordinates = new double[size][2];

        //to assign the original point
//        int cityNum = Integer.parseInt(tokens.nextToken());
//        originalPoint[0] = Double.parseDouble(tokens.nextToken());
//        originalPoint[1] = Double.parseDouble(tokens.nextToken());

        for(int i = 0 ; i < size ; i ++ ){
          int cityNum = Integer.parseInt(tokens.nextToken());
          coordinates[i][0] = Double.parseDouble(tokens.nextToken());
          coordinates[i][1] = Double.parseDouble(tokens.nextToken());
        }
    }   //end try
    catch( Exception e )
    {
        e.printStackTrace();
        System.out.println(e.toString());
    }   // end catch
    //System.out.println( "done" );
  }

  public void calcEuclideanDistanceMatrix(){
    openga.ObjectiveFunctions.util.generateMatrix_EuclideanDist generateMatrix_EuclideanDist1
        = new openga.ObjectiveFunctions.util.generateMatrix_EuclideanDist();
    generateMatrix_EuclideanDist1.setData(coordinates);
    generateMatrix_EuclideanDist1.setDistanceMatrixData();
    distanceMatrix = generateMatrix_EuclideanDist1.getMatrix();
  }

  public String getFileName(int index){
    return Instances[index];
  }

  public double[][] getCoordinates(){
    return coordinates;
  }

  public double[][] getDistanceMatrix(){
    return distanceMatrix;
  }

  public double[] getOriginalPoint(){
    return originalPoint;
  }

  public int getSize(){
    return size;
  }


}
