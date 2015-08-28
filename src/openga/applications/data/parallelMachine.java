package openga.applications.data;

/**
 * <p>Title: The OpenGA project</p>
 * <p>Description: The project is to build general framework of Genetic algorithm and problem independent.</p>
 * <P>We can get the testing data from the program for parallel machine scheduling problem.</P>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-Hsin
 * @version 1.0
 */

public class parallelMachine {
  public parallelMachine() {
  }

  //Test suit one randomly generates processing time and due date
  public int[] getTestData1_processingTime(int numberOfJob, int randomSeed, int range){
    int processingTime[] = new int[numberOfJob];
    java.util.Random r = new java.util.Random(randomSeed);
    for(int i = 0 ; i < numberOfJob ; i ++ ){
      processingTime[i] = 1+(int)(r.nextDouble()*range);
    }
    return processingTime;
  }

  public int[] getTestData1_DueDay(int numberOfJob, int randomSeed, int range){
    int DueDay[] = new int[numberOfJob];
    java.util.Random r = new java.util.Random(randomSeed);
    for(int i = 0 ; i < numberOfJob ; i ++ ){
      DueDay[i] = 1+(int)(r.nextDouble()*range);
    }
    return DueDay;
  }

  //The data is used by the paper of Two Phase Sub-Population Genetic Algorithm for Parallel Machine
  //Scheduling problem, submitted to expert system by Chang, Chen, and Lin (2005).
  public final int[] getTestData2_processingTime(){
    int processingTime[];
    processingTime = new int[]{1020,816,770,1500,1000,560,560,560,560,560,300,990,990,990,990,750,750,1032,
                               1032,384,220,174,174,174,174,128,285,285,285,346,384,384,384,1050,1050,1050,
                               1050,1050,384,375,390,750,1045,1045,760,760,720,384,760,760,285,285,285,346,
                               346,285,173,285,285,173,346,1050,1050,500,500,440,370,500,440,500,760,690,690,
                               1400,1400,692,760,1400,1400,390};
    return processingTime;
  }

  public final int[] getTestData2_DueDay(){
    int DueDay[];
    DueDay = new int[]{1920,1920,1920,1920,1920,1920,1920,1920,1920,1920,1440,1440,1440,1440,1440,1440,1440,
                       1440,1440,1440,1440,960,960,960,960,960,960,960,960,960,1440,1440,1440,1440,1440,1440,
                       1440,1440,1440,1440,2880,2880,2880,2880,2880,2880,2880,2880,2880,2880,1440,1440,1440,
                       1440,1440,1440,1440,1440,1440,1440,1440,2160,2160,2160,2160,2160,2160,2160,2160,2160,
                       2880,2880,2880,2880,2880,2880,2880,2880,2880,2880};
    return DueDay;
  }

  /**
   * @param num To specify how many jobs to test.
   * @return
   */
  public final int[] getTestData3_processingTime(int num){
    if(getTestData2_processingTime().length <= num){
      return getTestData2_processingTime();
    }
    else{
      int pTime[] = new int[num];
      for(int i = 0 ; i < num ; i ++ ){
        pTime[i] = getTestData2_processingTime()[i];
      }
      return pTime;
    }
  }

  /**
   * @param num To specify how many jobs to test.
   * @return
   */
  public final int[] getTestData3_DueDay(int num){
    if(getTestData2_DueDay().length <= num){
      return getTestData2_DueDay();
    }
    else{
      int DueDay[] = new int[num];
      for(int i = 0 ; i < num ; i ++ ){
        DueDay[i] = getTestData2_DueDay()[i];
      }
      return DueDay;
    }
  }

  /**
   * @return The Pareto result of the 35 job and 10 machines of the test problem.
   * The first objective is makepsan, the other is total tardiness.
   * We run it for 5 million time to obtain it.
   * It can be improved by increasing the number of iterations.
   */
  public double[][] getParetoJob35M10(){
    return new double[][]{
        {	2664	,	2411	},
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
        {	2317	,	2650	},
        {	2310	,	2685	},
        {	2305	,	2692	},
        {	2297	,	2700	},
        {	2295	,	2709	},
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
        {	2210	,	3026	},
        {	2206	,	3030	},
        {	2204	,	3072	},
        {	2194	,	3090	},
        {	2190	,	3100	},
        {	2186	,	3136	},
        {	2184	,	3300	},
        {	2181	,	3357	},
        {	2194	,	3090	}};
  }

  public double[][] getParetoJob50M15(){
    return new double[][]{
        {	2220	,	1973	},
        {	2223	,	1824	},
        {	2230	,	1784	},
        {	2234	,	1199	},
        {	2238	,	1184	},
        {	2250	,	1073	},
        {	2251	,	1031	},
        {	2253	,	1025	},
        {	2254	,	1011	},
        {	2261	,	969	},
        {	2268	,	954	},
        {	2269	,	927	},
        {	2270	,	824	},
        {	2276	,	812	},
        {	2278	,	807	},
        {	2279	,	806	},
        {	2280	,	786	},
        {	2281	,	784	},
        {	2284	,	769	},
        {	2285	,	766	},
        {	2289	,	739	},
        {	2291	,	737	},
        {	2294	,	722	},
        {	2298	,	699	},
        {	2300	,	668	},
        {	2304	,	671	},
        {	2306	,	659	},
        {	2307	,	626	},
        {	2309	,	610	},
        {	2310	,	572	},
        {	2312	,	568	},
        {	2315	,	550	},
        {	2316	,	512	},
        {	2320	,	507	},
        {	2324	,	504	},
        {	2330	,	475	},
        {	2334	,	466	},
        {	2335	,	451	},
        {	2337	,	446	},
        {	2342	,	413	},
        {	2344	,	391	},
        {	2353	,	358	},
        {	2356	,	348	},
        {	2360	,	339	},
        {	2361	,	331	},
        {	2362	,	309	},
        {	2368	,	297	},
        {	2370	,	270	},
        {	2371	,	267	},
        {	2376	,	248	},
        {	2377	,	233	},
        {	2380	,	212	},
        {	2389	,	210	},
        {	2394	,	190	},
        {	2406	,	155	},
        {	2409	,	146	},
        {	2410	,	141	},
        {	2419	,	100	},
        {	2423	,	96	},
        {	2424	,	76	},
        {	2429	,	62	},
        {	2434	,	51	},
        {	2439	,	42	},
        {	2443	,	37	},
        {	2444	,	28	},
        {	2448	,	23	},
        {	2452	,	18	},
        {	2461	,	9	},
        {	2474	,	4	},
        {	2484	,	0	},
        {	2353	,	377	},
        {	2401	,	178	},
        {	2399	,	179	}};
  }

  public double[][] getParetoJob65M18(){
    return new double[][]{
        {	2225	,	678	},
        {	2233	,	668	},
        {	2234	,	644	},
        {	2242	,	641	},
        {	2243	,	640	},
        {	2246	,	619	},
        {	2247	,	618	},
        {	2250	,	485	},
        {	2251	,	477	},
        {	2253	,	468	},
        {	2256	,	461	},
        {	2257	,	460	},
        {	2264	,	455	},
        {	2300	,	217	},
        {	2309	,	198	},
        {	2310	,	180	},
        {	2315	,	162	},
        {	2320	,	148	},
        {	2327	,	126	},
        {	2328	,	125	},
        {	2330	,	107	},
        {	2336	,	99	},
        {	2340	,	92	},
        {	2342	,	80	},
        {	2345	,	78	},
        {	2352	,	77	},
        {	2353	,	76	},
        {	2354	,	67	},
        {	2355	,	66	},
        {	2358	,	62	},
        {	2359	,	61	},
        {	2362	,	26	},
        {	2393	,	13	},
        {	2410	,	8	},
        {	2419	,	0	},
        {	2163	,	5762	},
        {	2166	,	5592	},
        {	2168	,	5591	},
        {	2169	,	5558	},
        {	2174	,	4642	},
        {	2175	,	2190	},
        {	2176	,	2089	},
        {	2177	,	2050	},
        {	2179	,	2037	},
        {	2182	,	2032	},
        {	2183	,	1990	},
        {	2184	,	1965	},
        {	2185	,	1934	},
        {	2187	,	1933	},
        {	2189	,	1853	},
        {	2194	,	1844	},
        {	2207	,	873	},
        {	2209	,	820	},
        {	2213	,	814	},
        {	2223	,	804	},
        {	2268	,	317	},
        {	2269	,	246	},
        {	2276	,	241	},
        {	2279	,	228	},
        {	2304	,	213	},
        {	2293	,	223	}};
  }

  public static void main(String[] args) {
    parallelMachine parallelMachine1 = new parallelMachine();
  }

}