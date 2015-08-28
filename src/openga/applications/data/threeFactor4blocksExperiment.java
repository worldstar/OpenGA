package openga.applications.data;

/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class threeFactor4blocksExperiment {
  public threeFactor4blocksExperiment() {
  }
  //Factor A, B, C, and Block 1.
  public int[][] getCombinations(){
    int[][] combs = new int[][]
        {{	1	,	0	,	0	,	0	},
        {	0	,	1	,	1	,	0	},
        {	1	,	1	,	0	,	1	},
        {	0	,	0	,	1	,	1	},
        {	0	,	1	,	0	,	2	},
        {	1	,	0	,	1	,	2	},
        {	0	,	0	,	0	,	3	},
        {	1	,	1	,	1	,	3	}};
    return combs;
  }

}