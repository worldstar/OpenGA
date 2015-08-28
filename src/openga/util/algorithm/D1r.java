package openga.util.algorithm;

/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: To evaluate the solution quality by the R1r matric.</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class D1r {
  public D1r() {
  }

  double referenceSet[][];
  double targetSet[][];
  double D1rVal = 0;

  public void setData(double _referenceSet[][], double _targetSet[][]){
    this.referenceSet = new double[_referenceSet.length][_referenceSet[0].length];;
    this.targetSet = new double[_targetSet.length][_targetSet[0].length];

    for(int i = 0 ; i < referenceSet.length ; i ++ ){
      for(int j = 0 ; j < _referenceSet[0].length ; j ++ ){
        referenceSet[i][j] = _referenceSet[i][j];
      }
    }

    for(int i = 0 ; i < targetSet.length ; i ++ ){
      for(int j = 0 ; j < _targetSet[0].length ; j ++ ){
        targetSet[i][j] = _targetSet[i][j];
      }
    }
  }

  public void setData(double targetSet[][]){
    this.targetSet = targetSet;
  }

  public void calcD1r(){//for 2-objectives
    //to normalize the objective value and reference set.
    getMin getMin1 = new getMin();
    getMax getMax1 = new getMax();
    double normMin1 = getMin1.setData(referenceSet, 0);//for the first obj
    double normMin2 = getMin1.setData(referenceSet, 1);//for the second obj
    double normMax1 = getMax1.setData(referenceSet, 0);//for the first obj
    double normMax2 = getMax1.setData(referenceSet, 1);//for the second obj

    for(int i = 0 ; i < referenceSet.length ; i ++ ){
      referenceSet[i][0] = (referenceSet[i][0] - normMin1)/(normMax1 - normMin1);
      referenceSet[i][1] = (referenceSet[i][1] - normMin2)/(normMax2 - normMin2);
    }

    for(int i = 0 ; i < targetSet.length ; i ++ ){
     targetSet[i][0] = (targetSet[i][0] - normMin1)/(normMax1 - normMin1);
     targetSet[i][1] = (targetSet[i][1] - normMin2)/(normMax2 - normMin2);
     //System.out.println(targetSet[i][0]*100+" "+targetSet[i][1]*100);
    }

    for(int i = 0 ; i < referenceSet.length ; i ++ ){
      double minDxy = Double.MAX_VALUE;
      for(int j = 0 ; j < targetSet.length ; j ++ ){
        double tempDxy = calcDistance(referenceSet[i], targetSet[j]);

        if(minDxy > tempDxy){
          minDxy = tempDxy;
        }
      }
      //System.out.println("minDxy "+minDxy);
      D1rVal += minDxy;
      //System.out.println(D1rVal);
    }
    D1rVal = (D1rVal/referenceSet.length)*100.0;
  }

  public void calcD1rWithoutNormalization(){
    D1rVal = 0;
    for(int i = 0 ; i < referenceSet.length ; i ++ ){
      double minDxy = Double.MAX_VALUE;
      for(int j = 0 ; j < targetSet.length ; j ++ ){
        double tempDxy = calcDistance2(referenceSet[i], targetSet[j]);

        if(minDxy > tempDxy){
          minDxy = tempDxy;
        }
      }
      //System.out.println("minDxy "+minDxy);
      D1rVal += Math.sqrt(minDxy);
      //System.out.println(D1rVal);
    }
    D1rVal = (D1rVal/referenceSet.length);
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

  private double calcDistance2(double x[], double y[]){
    double dist = 0;

    for(int i = 0 ; i < x.length ; i ++ ){
      dist += Math.pow((x[i] - y[i]), 2);
    }
    return dist;
  }


  public double getD1r(){
    return D1rVal;
  }

  public static void main(String[] args) {
    D1r d1r1 = new D1r();
    double ref[][] = new double[][]
        {{	2664	,	2411	},
        {	2490	,	2420	},
        {	2424	,	2429	},
        {	2394	,	2438	},
        {	2378	,	2462	},
        {	2374	,	2494	},
        {	2358	,	2540	},
        {	2352	,	2551	},
        {	2343	,	2566	},
        {	2337	,	2597	},
        {	2336	,	2639	},
        {	2320	,	2643	},
        {	2317	,	2658	},
        {	2316	,	2692	},
        {	2310	,	2698	},
        {	2297	,	2700	},
        {	2295	,	2705	},
        {	2290	,	2714	},
        {	2275	,	2747	},
        {	2272	,	2776	},
        {	2268	,	2798	},
        {	2252	,	2816	},
        {	2250	,	2858	},
        {	2244	,	2882	},
        {	2232	,	2896	},
        {	2230	,	2915	},
        {	2226	,	2957	},
        {	2213	,	2999	},
        {	2212	,	3028	},
        {	2206	,	3030	},
        {	2204	,	3072	},
        {	2194	,	3090	},
        {	2190	,	3115	},
        {	2186	,	3136	},
        {	2184	,	3310	},
        {	2179	,	3656	},
        {	2173	,	3664	}};

    double obj[][] = new double[][]
        {{	2214	,	3148	},
        {	2216	,	3098	},
        {	2250	,	2858	},
        {	2212	,	3158	},
        {	2190	,	3258	},
        {	2290	,	2714	},
        {	2490	,	2420	},
        {	2295	,	2705	},
        {	2184	,	3314	},
        {	2424	,	2429	},
        {	2337	,	2621	},
        {	2317	,	2663	},
        {	2343	,	2593	},
        {	2356	,	2551	},
        {	2378	,	2478	},
        {	2394	,	2438	},
        {	2268	,	2798	},
        {	2252	,	2816	},
        {	2374	,	2498	},
        {	2336	,	2639	},
        {	2230	,	2938	},
        {	2206	,	3211	},
        {	2224	,	3030	},
        {	2209	,	3208	},
        {	2297	,	2700	},
        {	2219	,	3044	},
        {	2332	,	2656	},
        {	2305	,	2692	},
        {	2210	,	3165	},
        {	2275	,	2771	}};


    d1r1.setData(ref, obj);
    d1r1.calcD1r();
    System.out.println("d1r1 "+d1r1.getD1r());
  }

}