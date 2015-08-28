package openga.applications.data;

/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: It's the four factors full factorial experiment. The levels of A,B, and C are 2.
 * The factor D has 4 treatments.</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class fourFactorFullFactorial {
  public fourFactorFullFactorial() {
  }
  //Factor A, B, C, and D.
  public int[][] getCombinations(){
    int[][] combs = new int[][]
        {{	0	,	0	,	0	,	0	},
        {	0	,	0	,	0	,	1	},
        {	0	,	0	,	0	,	2	},
        {	0	,	0	,	0	,	3	},
        {	0	,	0	,	1	,	0	},
        {	0	,	0	,	1	,	1	},
        {	0	,	0	,	1	,	2	},
        {	0	,	0	,	1	,	3	},
        {	0	,	1	,	0	,	0	},
        {	0	,	1	,	0	,	1	},
        {	0	,	1	,	0	,	2	},
        {	0	,	1	,	0	,	3	},
        {	0	,	1	,	1	,	0	},
        {	0	,	1	,	1	,	1	},
        {	0	,	1	,	1	,	2	},
        {	0	,	1	,	1	,	3	},
        {	1	,	0	,	0	,	0	},
        {	1	,	0	,	0	,	1	},
        {	1	,	0	,	0	,	2	},
        {	1	,	0	,	0	,	3	},
        {	1	,	0	,	1	,	0	},
        {	1	,	0	,	1	,	1	},
        {	1	,	0	,	1	,	2	},
        {	1	,	0	,	1	,	3	},
        {	1	,	1	,	0	,	0	},
        {	1	,	1	,	0	,	1	},
        {	1	,	1	,	0	,	2	},
        {	1	,	1	,	0	,	3	},
        {	1	,	1	,	1	,	0	},
        {	1	,	1	,	1	,	1	},
        {	1	,	1	,	1	,	2	},
        {	1	,	1	,	1	,	3	}};

    return combs;
  }

}