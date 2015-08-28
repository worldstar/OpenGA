package openga.util.algorithm;

/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: The performance measure is to evalaute the closeness from solution to reference set.</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class generationalDistance {
  public generationalDistance() {
  }

  double referenceSet[][];
  double targetSet[][];
  double GDVal = 0;

  public void setData(double _referenceSet[][], double _targetSet[][]){
    this.referenceSet = new double[_referenceSet.length][_referenceSet[0].length];;
    this.targetSet = new double[_targetSet.length][_targetSet[0].length];

    for(int i = 0 ; i < referenceSet.length ; i ++ ){
      referenceSet[i][0] = _referenceSet[i][0];
      referenceSet[i][1] = _referenceSet[i][1];
   }

   for(int i = 0 ; i < targetSet.length ; i ++ ){
     targetSet[i][0] = _targetSet[i][0];
     targetSet[i][1] = _targetSet[i][1];
   }
  }

  public void setData(double targetSet[][]){
    this.targetSet = targetSet;
  }

  public void calcGD(){
    //to normalize the objective value and reference set.
    getMin getMin1 = new getMin();
    getMax getMax1 = new getMax();
    double normMin1 = getMin1.setData(referenceSet, 0);//for the first obj
    double normMin2 = getMin1.setData(referenceSet, 1);//for the second obj
    double normMax1 = getMax1.setData(referenceSet, 0);//for the first obj
    double normMax2 = getMax1.setData(referenceSet, 1);//for the second obj
    //to normalize the reference set.
    for(int i = 0 ; i < referenceSet.length ; i ++ ){
      referenceSet[i][0] = (referenceSet[i][0] - normMin1)/(normMax1 - normMin1);
      referenceSet[i][1] = (referenceSet[i][1] - normMin2)/(normMax2 - normMin2);
   }
   //to normalize the targetSet set.
   for(int i = 0 ; i < targetSet.length ; i ++ ){
     targetSet[i][0] = (targetSet[i][0] - normMin1)/(normMax1 - normMin1);
     targetSet[i][1] = (targetSet[i][1] - normMin2)/(normMax2 - normMin2);
     //System.out.println(targetSet[i][0]*100+" "+targetSet[i][1]*100);
   }

   for(int i = 0 ; i < targetSet.length ; i ++ ){
     double minDxy = Double.MAX_VALUE;
     for(int j = 0 ; j < referenceSet.length ; j ++ ){
       double tempDxy = calcDistance(targetSet[i], referenceSet[j]);
       if(minDxy > tempDxy){
         minDxy = tempDxy;
       }
     }
     //System.out.println("minDxy "+minDxy);
     GDVal += minDxy;
     //System.out.println(GDVal);
    }
    GDVal = (GDVal/targetSet.length);
  }

  /**
   * To calculate the dxy by euclidean distance.
   * @param x Your soln.
   * @param y Current best.
   * @return
   */
  private double calcDistance(double x[], double y[]){
    double dist = 0;
    for(int i = 0 ; i < x.length ; i ++ ){
      dist += Math.pow((x[i] - y[i]), 2);
    }
    return Math.sqrt(dist);
  }

  public double getGD(){
    return GDVal;
  }

}