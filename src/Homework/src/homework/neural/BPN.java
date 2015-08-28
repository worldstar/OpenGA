package homework.neural;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
class singleLayerVector{
  public singleLayerVector(){
  }

  double value[];

  public void initArray(int length1){
    value = new double[length1];
  }

  public void setValue(int index1, double _value){
    value[index1] = _value;
  }

  public double[] getValue(){
    return value;
  }

  public double getValue(int index1){
    return value[index1];
  }
}

class weightVector{
  public weightVector(){
  }

  double weight[][];

  public void initArray(int length1, int length2){
    weight = new double[length1][length2];
  }

  public void setValueBySmallRandomValue(){
    for(int i = 0 ; i < weight.length ; i ++ ){
      for(int j = 0 ; j < weight[0].length ; j ++ ){
        weight[i][j] = Math.pow(Math.random()/5, 2);
      }
    }
  }

  public void setValue(int index1, int index2, double value){
    weight[index1][index2] = value;
  }

  public double[][] getWeight(){
    return weight;
  }

  public double getValue(int index1, int index2){
    return weight[index1][index2];
  }
}

public class BPN {
  public BPN() {
  }
  /**
   * Paramters of Neural network
   */
  int numberOfInput = 2;//the bias is not involved.
  int numberOfOuput = 2;
  int numberOfhiddenLayer = 1;
  int numberOfNeuron[];//the number of neuron of each hidden layer.
  weightVector weightVector1[];       //weight of each connection.[layer][From][To]
  //the first dimension is the bias for hidden layer and the last one is the bias for output layer
  weightVector weightOfBias[];
  //there are numberOfInput data in the beginning, and the last numberOfOuput is target.
  double trainningData[][];
  double learningRate = 0.25;
  int epoach = 2;               //the learnning iterations
  singleLayerVector singleLayerVector1[];

  public void initVars(){
    //init the number of neurons
    numberOfNeuron = new int[]{2};// there are two neurons in the first hidden layer.
    //init weight for each layer
    weightVector1  = new weightVector[numberOfhiddenLayer+1];
    //it includes the input and output layer
    weightOfBias   = new weightVector[numberOfhiddenLayer+1];

    //initial the weight of each connection
    for(int i = 0 ; i < numberOfhiddenLayer + 1 ; i ++ ){
      weightVector1[i] = new weightVector();

      if(i == 0){//the iput layer to the first hidden layer
        weightVector1[i].initArray(numberOfInput, numberOfNeuron[i]);
      }
      else if(i == numberOfhiddenLayer){//the last hidden layer to the output
        weightVector1[i].initArray(numberOfNeuron[i], numberOfOuput);
      }
      else{//the weight connection is hidden layer vs. hidden layer
        weightVector1[i].initArray(numberOfNeuron[i], numberOfNeuron[i+1]);
      }
      //initial the weights by a small weight value
      weightVector1[i].setValueBySmallRandomValue();
    }//end the initial weight vector

    //initial the weight of each bias, the first is for the first hidden layer
    weightOfBias[0] = new weightVector();
    weightOfBias[0].initArray(numberOfInput, numberOfNeuron[0]);
    weightOfBias[0].setValue(0, 0, 0.4);//for the first node
    weightOfBias[0].setValue(1, 1, 0.6);//for the second node

    //for the output layer
    weightOfBias[1] = new weightVector();
    weightOfBias[1].initArray(numberOfNeuron[0], numberOfOuput);
    weightOfBias[1].setValue(0, 0, 0.3);//for the first node
    weightOfBias[1].setValue(1, 1, 0.1);//for the second node

    trainningData = new double[][]{{0, 1, 1, 0},
                                {0, 1, 1, 0},
                                {0, 1, 1, 0}};

  }

  public void startBPN(){
    initVars();

    //start to learn
    for(int i = 0 ; i < epoach ; i ++ ){
      for(int j = 0 ; j < trainningData.length ; j ++ ){
        //broad cast the data to all hidden units.
        for(int k = 0 ; k < numberOfhiddenLayer ; k ++ ){
          //the weighted input value.
          double weightedSum[] = new double[numberOfNeuron[k]];
          for(int m = 0 ; m < numberOfNeuron[k] ; m ++ ){
            if(k == 0){//the input data is trainning data
              weightedSum[m] = getWegihtInputValue(m, trainningData[i], weightVector1[k].getWeight(), weightOfBias[k].getWeight());
            }

          }

        }
      }
    }
  }

  /**
   *
   * @param index the index of the neuron of current layer.
   * @param inputValue all input value
   * @param _weight weights between two neurons
   * @return
   */
  double getWegihtInputValue(int index, double inputValue[], double _weight[][], double bias[][]){
    double value = bias[index][index];//1*bias[index] = bias[index]
    for(int i = 0 ; i < inputValue.length ; i ++ ){
      value += inputValue[i]*_weight[i][index];
    }
    return value;
  }

  /**
   * Transfering functions.
   * @param value
   * @return
   */
  public static double sigmoid(double value){
    return 1/(1+Math.exp(-value));
  }

  public static double sigmoid(int value){
    return 1/(1+Math.exp(-value));
  }

  public static double differentiateSigmoid(double value){
    return sigmoid(value)*(1-sigmoid(value));
  }

  public static double differentiateSigmoid(int value){
    return sigmoid(value)*(1-sigmoid(value));
  }



  public static void main(String[] args) {
    BPN BPN1 = new BPN();
    System.out.println(BPN1.sigmoid(0.2));
  }

}