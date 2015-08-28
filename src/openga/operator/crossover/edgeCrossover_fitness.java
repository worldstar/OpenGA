/*
 * Multi-Parents crossover. Most crossover operators are two-parents crossover operators.
 * This multi-parents crossover is able to take 3, 4, ..., to popSize chromosomes.
 * It might be able to increase the population diversity.
 * This operator is "Diagonal multi-parent crossover" by fitness base(FB)
 */

package openga.operator.crossover;
import openga.chromosomes.*;
/**
 *
 * @author nhu
 */
public class edgeCrossover_fitness implements CrossoverI{
    public edgeCrossover_fitness() {
    }
    
    populationI originalPop, newPop;
    double crossoverRate;
    int popSize, chromosomeLength;
    int cutPoint1, cutPoint2;
    int pos1, pos2;
    int randomSequence[];          //The sequence of selecting a number of chromosomes.
    int numberOfParents = 0;       //The number of chromosomes.
    int cut[]=new int[100];
    int numberOfJob=0;
    int sumProcessingTime[] = new int[500];
     int sumProcessingTime_ini[] = new int[500];
    
    public void setData(double crossoverRate, chromosome _chromosomes[],int numberOfObjs){
        //transfomation the two chromosomes into a population.
        population _pop = new population();
        _pop.setGenotypeSizeAndLength(_chromosomes[0].getEncodeType(), _chromosomes.length, _chromosomes[0].getLength(), numberOfObjs);
        _pop.initNewPop();
        for(int i = 0 ; i < _chromosomes.length ; i ++ ){
            _pop.setChromosome(i, _chromosomes[i]);
        }
        setData(crossoverRate, _pop);
        generateRandomSequence();
    }
    
    public void setData(double crossoverRate, populationI originalPop){
        this.originalPop = originalPop;
        popSize = originalPop.getPopulationSize();
        //System.out.println("originalPop.getLengthOfChromosome() "+originalPop.getLengthOfChromosome());
        newPop = new population();
        newPop.setGenotypeSizeAndLength(originalPop.getEncodedType(), popSize, originalPop.getLengthOfChromosome(), originalPop.getNumberOfObjectives());
        newPop.initNewPop();
        
        for(int i = 0 ; i < popSize ; i ++ ){
            newPop.setSingleChromosome(i, originalPop.getSingleChromosome(i));
        }
        this.crossoverRate = crossoverRate;
        chromosomeLength = originalPop.getSingleChromosome(0).genes.length;
        generateRandomSequence();
    }
    
    public void setData(int numberOfParents){
        this.numberOfParents = numberOfParents;
    }
    
    public void generateRandomSequence(){
        randomSequence = new int[newPop.getPopulationSize()];
        for(int i = 0 ; i < newPop.getPopulationSize() ; i ++){
            this.randomSequence[i] = i;
        }
        
        for(int i = 0 ; i < newPop.getPopulationSize() ; i ++){
            swaptGenes(randomSequence, i, (int)(Math.random()*newPop.getPopulationSize()));
        }
    }
    //Maintain the sequence after a combination is used.
    public int[] swaptGenes(int genes[], int pos1, int pos2){
        int backupGenes = genes[pos1];
        genes[pos1] = genes[pos2];
        genes[pos2] = backupGenes;
        return genes;
    }
    
    //start to crossover
    public void startCrossover(){
        //System.out.println("Start crossover ");
        for(int i = 0 ; i < popSize ; i ++ ){
            //System.out.println("Number of mated solution: "+i);
            //test the probability is larger than crossoverRate.
            if(Math.random() <= crossoverRate){
                //to get the other chromosomes to do crossover
                copyElements(getCrossoverChromosomeSet(i), numberOfParents);
            }
        }
    }
    
    
    
    
    /**
     * To get the other chromosomes to do crossover.
     * @param sourceParentID The index of the original chromosome.
     */
    public int[] getCrossoverChromosomeSet(int sourceParentID){
        int sets[] = new int[numberOfParents];
        int index = 0;
        int originalIndex;
        
        for(int i = 0 ; i < newPop.getPopulationSize() ; i ++){
            if(randomSequence[i] == sourceParentID){
                index = i;
                break;
            }
        }
        
        originalIndex = index;
        
        for(int i = 0 ; i < numberOfParents ; i ++){
            //System.out.println("Index is :"+index);
            sets[i] = randomSequence[index];//To get its next neighbor.
            index ++;
            
            if(index >= randomSequence.length - 2){
                index = 0;
            }
        }
        
        //shuffle the sequences, so the combination won't be shown again.
        for(int i = 0 ; i < numberOfParents ; i ++){
            swaptGenes(randomSequence, originalIndex, (int)(Math.random()*randomSequence.length));
            originalIndex ++;
            if(originalIndex >= randomSequence.length - 2){
                originalIndex = 0;
            }
        }
        
        return sets;
    }
    
    /**
     * The numberOfParents chromosomes produce a new offspring
     * @param indexes A set of chromosomes to be mated
     * @param numberOfParents How many chromosomes are involved in this mating process
     */
    private void copyElements(int[] indexes, int numberOfParents){
        //openga.util.printClass printClass1 = new openga.util.printClass();
        //printClass1.printMatrix("Selected indexes", indexes);
        //double fit[] = new double[numberOfParents];
        // double fit_r[] = new double[numberOfParents];
        //double total_fit=0.0;
        //double avg_fit=0.0;
        //double max_fit=newPop.getObjectiveValues(indexes[0])[0];
        //double min_fit=newPop.getObjectiveValues(indexes[0])[0];
        int counter = 0;
        int tmp1=0;
        int tmp2=0;
        int tmp=0;
        int edge[][]=new int[this.chromosomeLength][this.chromosomeLength];  //this is map table(chromosomeLength*chromosomeLength)
        int pre_pos, pos, next_pos;    //record point
        int sum[][] = new int[this.chromosomeLength][2];   //calculate map table total occurance
        int v=0,v_s=0,s_p=0,e_p=0;
        int r1=0,r2=0;
        int s_i = 0;
        int sum_pos = 0;
        
        
//init processtime table
        for (int ff = 0; ff< this.chromosomeLength ;ff++){
                  sumProcessingTime[ff] = sumProcessingTime_ini[ff];
        }
 
 /*      print all parents chromsome
        for(int jj = 0 ; jj < indexes.length ; jj ++){
            for(int ii = 0 ; ii < this.chromosomeLength; ii ++){
                System.out.print(newPop.getSingleChromosome(indexes[jj]).genes[ii]+" ,");
            }
            System.out.println();
        }
  */
        
 /*   for crossover all parents chromsome
        for (int chrom=0 ; chrom<indexes.length; chrom++){
            if (chrom != 0){
                tmp = indexes[chrom];
                indexes[chrom] = indexes[0];
                indexes[0] = tmp;
            }
  */
        /*
        for (int z= 0 ;z <numberOfJob ; z++ ){
         
            System.out.print(sumProcessingTime[z] + ",");
        }
        System.out.println("**************************************");
        System.exit(0);
         */
        
        
        
        for(int i = 0 ; i < indexes.length; i ++){                    //create mapping table
            for(int j = 0 ; j < this.chromosomeLength ; j ++){
                if (j == 0){
                    pre_pos = this.chromosomeLength-1;
                    next_pos=j+1;
                    pos = j;
                }else if (j == this.chromosomeLength-1){
                    pre_pos = j-1;
                    next_pos=0;
                    pos = j;
                }else{
                    pre_pos = j-1;
                    next_pos=j+1;
                    pos = j;
                }
                edge[newPop.getSingleChromosome(indexes[i]).genes[pos]][newPop.getSingleChromosome(indexes[i]).genes[next_pos]] = 1;
                //edge[newPop.getSingleChromosome(indexes[i]).genes[pos]][newPop.getSingleChromosome(indexes[i]).genes[pre_pos]] = 1;
            }
        }
        
        /*
        for(int jj = 0 ; jj < this.chromosomeLength ; jj ++){        // create total occurance
            sum[jj][0] = jj;
            sum[jj][1]=0;
            for(int ii = 0 ; ii < this.chromosomeLength; ii ++){
                if(edge[jj][ii]==1){
                    sum[jj][1]++;
                }
            }
        }
         */
        
/*        
        for(int jj = 0 ; jj < this.chromosomeLength ; jj ++){
            for(int ii = 0 ; ii < this.chromosomeLength; ii ++){
                System.out.print(edge[jj][ii]+" ,");
            }
            System.out.println();
        }
 */
 /*
        System.out.println("-----------------------------------------------------------");
        for(int jj = 0 ; jj < this.chromosomeLength ; jj ++){
            for(int ii = 0 ; ii < 2; ii ++){
                System.out.print(sum[jj][ii]+" ,");
            }
            System.out.println();
        }
  */
  /*
        for (int ff = 0; ff< this.chromosomeLength-1 ;ff++){            //sort sum table increase
            for (int gg = 0; gg< this.chromosomeLength-1-ff ;gg++){
                if (sum[gg][1]>sum[gg+1][1]){
                    tmp1 = sum[gg+1][1];
                    tmp2 = sum[gg+1][0];
                    sum[gg+1][1] = sum[gg][1];
                    sum[gg+1][0] = sum[gg][0];
                    sum[gg][1] = tmp1;
                    sum[gg][0] = tmp2;
                }
            }
        }
   */
/*
        System.out.println("----------------------sort result-------------------------");
        for(int jj = 0 ; jj < this.chromosomeLength ; jj ++){
            for(int ii = 0 ; ii < 2; ii ++){
                System.out.print(sum[jj][ii]+" ,");
            }
            System.out.println();
        }
 */
        //randomize sum table by occurance in every stage
  /*
        v_s = 0;
        s_p = 0;
        for (int i = 0; i< this.chromosomeLength ;i++){
            v = sum[i][1];
            if ((v>v_s && v_s!=0) ||(i==this.chromosomeLength-1)){
                if (i==this.chromosomeLength-1){
                    e_p = this.chromosomeLength-1;
                }
                for (int r=0; r< (e_p-s_p); r++){
                    r1=(int)(Math.random() * (e_p-s_p+1)) + s_p;
                    r2=(int)(Math.random() * (e_p-s_p+1)) + s_p;
                    tmp1 = sum[r1][1];
                    tmp2 = sum[r1][0];
                    sum[r1][1] = sum[r2][1];
                    sum[r1][0] = sum[r2][0];
                    sum[r2][1] = tmp1;
                    sum[r2][0] = tmp2;
                }
                s_p=i;
                e_p=i;
                v_s= sum[i][1];
            }else{
                e_p = i;
                v_s= sum[i][1];
            }
        }
   */
 /*       
        System.out.println("----------------------process time-------------------------");
        for(int jj = 0 ; jj < this.chromosomeLength ; jj ++){
            System.out.print(sumProcessingTime[jj]+" ,");
        }
        System.out.println();
*/        
        //       System.exit(0);
        int pt_temp=0;
        int min_pt=0;
        /*
        for(int pt = 0 ; pt<this.chromosomeLength ; pt++){
            if (sumProcessingTime[pt]>pt_temp){
                min_pt=pt;
                pt_temp = sumProcessingTime[pt];
            }
        }
         */
        
         min_pt = newPop.getSingleChromosome(indexes[0]).genes[0];
        sum_pos = min_pt ;
        sumProcessingTime[min_pt] = 0;
        //s_i =newPop.getSingleChromosome(indexes[0]).genes[0] ;                           // create link from first element of parents
        /*
        for (int vv=0;vv<this.chromosomeLength;vv++){
            if (sum[vv][0]==s_i){
                sum[vv][0]=-1;
                break;
            }
        }
         */
        //second element is selected from the smallest set of link first element and mark it by -1
        newPop.setGene(indexes[0], 0, sum_pos);        //if can not find the element(all is -1), then select the smallest unmark element
        
        for(int i =1; i< this.chromosomeLength; i++){
            pt_temp =0;
            min_pt=this.chromosomeLength;
 //           System.out.print(sum_pos+"----->");
            for(int j = 0 ; j < this.chromosomeLength ; j++){  //search link
                if (edge[sum_pos][j]==1){
//                    System.out.print( j +",");
                    if((sumProcessingTime[j]>pt_temp)&&(sumProcessingTime[j]!=0)){
                        pt_temp = sumProcessingTime[j];
                        min_pt=j;
                    }
                }
            }
            
            if(min_pt==50){
                pt_temp =0;
                for (int z=0; z<this.chromosomeLength ; z++){
                    if ((sumProcessingTime[z]>pt_temp)&&(sumProcessingTime[z]!=0)){
                        min_pt=z;
                        pt_temp = sumProcessingTime[z];
                    }
                }
            }
            
//            System.out.println("result: "+ min_pt+", "+sumProcessingTime[min_pt] + ",");
            newPop.setGene(indexes[0], i, min_pt);
            sumProcessingTime[min_pt] =0 ;
            sum_pos = min_pt;
        }
/*        
        System.out.println();
        System.out.println("----------------------parent0-------------------------");
        for(int jj = 0 ; jj < this.chromosomeLength ; jj ++){
            System.out.print(newPop.getSingleChromosome(indexes[0]).genes[jj]+" ,");
        }
*/        
        //System.exit(0);
        
    }
    
    /**
     * if there is the same gene, it return the index of the gene.
     * Else, default value is -1, which is also mean don't have the same gene
     * during cutpoint1 and cutpoint2.
     * @param newGene
     * @param _chromosome
     * @return
     */
    private boolean checkConflict(int newGene, int _chromosome[], int position, int tail){
        boolean hasConflict = false;
        for(int i = 0 ; i < position ; i ++ ){
            if(newGene == _chromosome[i]){
                return true;
            }
        }
        
        for(int i = tail ; i < this.chromosomeLength ; i ++ ){
            if(newGene == _chromosome[i]){
                return true;
            }
        }
        
        return hasConflict;
    }
    
    
    public populationI getCrossoverResult(){
        // System.out.println("crossover2 return");
        return newPop;
    }
/*
  //for test only
  public static void main(String[] args) {
    CrossoverI twoPointCrossover21 = new multiParentsCrossover();
    openga.util.printClass printClass1 = new openga.util.printClass();
    populationI population1 = new population();
    populationI newPop = new population();
    int size = 200, length = 50;
 
    population1.setGenotypeSizeAndLength(true, size, length, 2);
    population1.createNewPop();
    for(int i = 0 ; i < size ; i ++ ){
       printClass1.printMatrix(""+i,population1.getSingleChromosome(i).genes);
    }
 
 
    twoPointCrossover21.setData(0.9, population1);
    twoPointCrossover21.startCrossover();
    newPop = twoPointCrossover21.getCrossoverResult();
    System.out.println("3-parents crossover results.");
    for(int i = 0 ; i < size ; i ++ ){
       printClass1.printMatrix(""+i,newPop.getSingleChromosome(i).genes);
    }
  }
 */
    
    public void setData(double crossoverRate, chromosome[] _chromosomes, int numberOfObjs, int numberOfJob, int numberOfMachines) {
    }
    
    public void setData(int numberOfJob, int numberOfMachines, int[][] processingTime) {
        this.numberOfJob =  numberOfJob;
        for(int is = 0 ; is < numberOfJob ; is ++ ){
            sumProcessingTime_ini[is] = 0;
            for(int js = 0 ; js < numberOfMachines ; js ++ ){
                sumProcessingTime_ini[is] += processingTime[is][js];
            }
        }
        
        
        
    }
}
