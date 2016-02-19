/*
 * This method implements the new two-part encoding crossover by 
 Yuan, S., Skinner, B., Huang, S., & Liu, D. (2013). A new crossover approach 
 for solving the multiple travelling salesmen problem using genetic algorithms. 
 European Journal of Operational Research, 228(1), 72-82.
 */
package openga.operator.crossover;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author UlyssesZhang
 */
public class TCSCFCrossover extends TPCrossOver implements CrossoverMTSPI {

  private int numberofSalesmen;
  private int type;

  public TCSCFCrossover(int type) {
    this.type = type;
  }
  
  
//  /*
  public static void main(String[] args) {
    int[] dad = {1, 2, 3, 4, 5, 6, 7, 8, 9, 4, 2, 3},
            mom = {1, 2, 3, 4, 5, 6, 7, 8, 9, 2, 2, 5};
    TCSCFCrossover TCSCFX = new TCSCFCrossover(0);
    TCSCFX.numberofSalesmen = 3;
    TCSCFX.CrossOver(mom, dad);
  }
//  */
  
  //start to crossover
  public void startCrossover(){
    for(int i = 0 ; i < popSize ; i ++ ){
       //test the probability is larger than crossoverRate.
       if(Math.random() <= crossoverRate){
         //to get the other chromosome to crossover
         int index2 = getCrossoverChromosome(i);
         int[] Mom = originalPop.getSingleChromosome(i).genes;
         int[] Dad = originalPop.getSingleChromosome(index2).genes;
         int[] Result = CrossOver(Mom, Dad);        
         newPop.getSingleChromosome(i).genes = Result;
       }
    }
  }
  
  //type = 0 : mtsp,  type = 2 : OA,  type = 3 : TCX
  public int[] CrossOver(int[] mom, int[] dad) {
    int[] Child  = new int[mom.length];
    
    int[][] segmentofChild = new int[numberofSalesmen][];
    int[][] segmentofMom = new int[numberofSalesmen][];
    int[][] segmentofDad = new int[numberofSalesmen][];

    List<List<Integer>> segmentofSamesite = new ArrayList<List<Integer>>();
    List<Integer> Samesite;
    int[] sizeofSamesite = new int[numberofSalesmen];

    List<List<Integer>> segmentofmomSelection = new ArrayList<List<Integer>>();
    List<Integer> momSelection;
    int[] sizeofmomSelection = new int[numberofSalesmen];
    
    List<Integer> tmpSelection = new ArrayList<Integer>();
    
    List<List<Integer>> segmentofdadSelection = new ArrayList<List<Integer>>();
    List<Integer> dadSelection;
    int[] sizeofdadSelection = new int[numberofSalesmen];
    
    int Length = mom.length - numberofSalesmen;
    int momLength = 0;
    int dadLength = 0;

    //Cutting gene segment
    for (int i = 0; i < numberofSalesmen; i++) {
      segmentofMom[i] = new int[mom[Length]];
      for (int j = 0; j < mom[Length]; j++) {
        segmentofMom[i][j] = mom[momLength];
        momLength++;
      }
      segmentofDad[i] = new int[dad[Length]];
      for (int j = 0; j < dad[Length]; j++) {
        segmentofDad[i][j] = dad[dadLength];
        dadLength++;
      }
      Length++;
    }
//    System.out.println("Length = "+Length);
    
    //print segmentofMom
    /*
    System.out.println("segmentofMom = ");
    for (int i = 0; i < segmentofMom.length; i++) {
      for (int j = 0; j < segmentofMom[i].length; j++) {
        System.out.print(segmentofMom[i][j]+" ");
      }
      System.out.println();
    }
//    */
    
//print segmentofDad
    /*
    System.out.println("segmentofDad = ");
    for (int i = 0; i < segmentofDad.length; i++) {
      for (int j = 0; j < segmentofDad[i].length; j++) {
        System.out.print(segmentofDad[i][j]+" ");
      }
      System.out.println();
    }
//    */

    //Same-site copy first
    for (int i = type; i < numberofSalesmen; i++) {
      Samesite = new ArrayList<Integer>();
      for (int j = 0; j < segmentofMom[i].length; j++) {
        for (int k = 0; k < segmentofDad[i].length; k++) {
          if (segmentofMom[i][j] == segmentofDad[i][k] && segmentofMom[i][j] != -1) {
            Samesite.add(segmentofMom[i][j]);
            segmentofMom[i][j] = -1;
            segmentofDad[i][k] = -1;
          }
        }
      }
      segmentofSamesite.add(Samesite);
      sizeofSamesite[i] = Samesite.size();
    }

    //print segmentofSamesite
    /*
    System.out.println("segmentofSamesite = ");
    for (int i = 0; i < segmentofSamesite.size(); i++) {
      for (int j = 0; j < segmentofSamesite.get(i).size(); j++) {
        System.out.print(segmentofSamesite.get(i).get(j)+" ");
      }
      System.out.println();
    }
//    */

    //Selection
    int cutPoint1, cutPoint2;
    for (int i = 0; i < numberofSalesmen; i++) {
      do {
        cutPoint1 = new Random().nextInt(segmentofMom[i].length+1);
        cutPoint2 = new Random().nextInt(segmentofMom[i].length+1);
      } while (cutPoint1 == cutPoint2);
      if (cutPoint1 > cutPoint2) {
        int tmp = cutPoint1;
        cutPoint1 = cutPoint2;
        cutPoint2 = tmp;
      }
//      System.out.println("cutPoint1 = " + cutPoint1);
//      System.out.println("cutPoint2 = " + cutPoint2);
      momSelection = new ArrayList<Integer>();
      for (int j = 0; j < segmentofMom[i].length; j++) {
        if (j >= cutPoint1 && j < cutPoint2 && segmentofMom[i][j] != -1) {
          momSelection.add(segmentofMom[i][j]);
//          System.out.print("mom"+segmentofMom[i][j]);
          segmentofMom[i][j] = -2;
        } else if (segmentofMom[i][j] != -1){
          tmpSelection.add(segmentofMom[i][j]);
//          System.out.print("dad"+segmentofMom[i][j]);
          segmentofMom[i][j] = -2;
        }
      }
//      System.out.println();
      segmentofmomSelection.add(momSelection);
      sizeofmomSelection[i] = momSelection.size();
    }
    
    for (int i = 0; i < numberofSalesmen; i++) {
      dadSelection = new ArrayList<Integer>();
      for (int j = 0; j < segmentofDad[i].length; j++) {
        for (int k = 0; k < tmpSelection.size(); k++) {
          if(segmentofDad[i][j] == tmpSelection.get(k)){
            dadSelection.add(segmentofDad[i][j]);
            segmentofDad[i][j] = -2;
          }
        }
      }
      segmentofdadSelection.add(dadSelection);
      sizeofdadSelection[i] = dadSelection.size();
    }

//print momSelection
    /*
    System.out.println("momSelection = ");
    for (int i = 0; i < segmentofmomSelection.size(); i++) {
      for (int j = 0; j < sizeofmomSelection[i]; j++) {
        System.out.print(segmentofmomSelection.get(i).get(j)+" ");
      }
      System.out.println();
    }
//    */
    
//print tmpSelection
    /*
    System.out.println("tmpSelection = ");
    for (int i = 0; i < tmpSelection.size(); i++) {
      System.out.print(tmpSelection.get(i)+" ");
    }
    System.out.println();
//    */
    
//print dadSelection
    /*
    System.out.println("dadSelection = ");
    for (int i = 0; i < segmentofdadSelection.size(); i++) {
      for (int j = 0; j < sizeofdadSelection[i]; j++) {
        System.out.print(segmentofdadSelection.get(i).get(j)+" ");
      }
      System.out.println();
    }
//    */

//print segmentofMom
    /*
    System.out.println("segmentofMom = ");
    for (int i = 0; i < segmentofMom.length; i++) {
      for (int j = 0; j < segmentofMom[i].length; j++) {
        System.out.print(segmentofMom[i][j]+" ");
      }
      System.out.println();
    }
//    */
    
//print segmentofDad
    /*
    System.out.println("segmentofDad = ");
    for (int i = 0; i < segmentofDad.length; i++) {
      for (int j = 0; j < segmentofDad[i].length; j++) {
        System.out.print(segmentofDad[i][j]+" ");
      }
      System.out.println();
    }
//    */
    for (int i = 0; i < segmentofChild.length; i++) {//segmentofChild.length = 3
      segmentofChild[i] = new int[sizeofSamesite[i]+sizeofdadSelection[i]+sizeofmomSelection[i]];
//      System.out.println("sizeofSamesite["+i+"] = "+sizeofSamesite[i]);
//      System.out.println("sizeofdadSelection["+i+"] = "+sizeofdadSelection[i]);
//      System.out.println("sizeofmomSelection["+i+"] = "+sizeofmomSelection[i]);
//      System.out.println("segmentofChild["+i+"] = "+segmentofChild[i].length);
//      System.out.println();
    }
    
    Length = 0;
    int[] position = new int[numberofSalesmen];
    
    for (int i = type; i < numberofSalesmen; i++) {
      for(int j = 0; j < sizeofSamesite[i]; j++) {
        segmentofChild[i][position[i]] = segmentofSamesite.get(Length).get(j);
        position[i]++;
      }
      Length++;
    }
    
    for (int i = 0; i < numberofSalesmen; i++) {
      for(int j = 0; j < sizeofmomSelection[i]; j++) {
        segmentofChild[i][position[i]] = segmentofmomSelection.get(i).get(j);
        position[i]++;
      }
    }
    
    for (int i = 0; i < numberofSalesmen; i++) {
      for(int j = 0; j < sizeofdadSelection[i]; j++) {
        segmentofChild[i][position[i]] = segmentofdadSelection.get(i).get(j);
        position[i]++;
      }
    }
    
    //print segmentofChild
    /*
    System.out.println("segmentofChild = ");
    for (int i = 0; i < segmentofChild.length; i++) {
      for(int j = 0; j < segmentofChild[i].length; j++) {
        System.out.print(segmentofChild[i][j]+" ");
      }
      System.out.println(); 
    }
    //    */
    
    Length = 0;
    for (int i = 0; i < segmentofChild.length; i++) {
      for(int j = 0; j < segmentofChild[i].length; j++) {
        Child[Length] = segmentofChild[i][j];
//        System.out.print(Length+" ");
        Length++;
      }
    }
    for (int i = 0; i < numberofSalesmen; i++) {
      Child[Length] = segmentofChild[i].length;
      Length++;
    }
    //print Child
    /*
    System.out.println("Child = ");
    for (int i = 0; i < Child.length; i++) {
      System.out.print(Child[i]+" ");
    }
    System.out.println(); 
    //    */
    
    return Child;
  }
  
  @Override
  public void setNumberofSalesmen(int numberofSalesmen) {
      this.numberofSalesmen = numberofSalesmen;
  }
}
