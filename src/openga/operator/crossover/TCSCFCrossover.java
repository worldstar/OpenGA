/*
 * This method implements the new two-part encoding crossover by 
 Yuan, S., Skinner, B., Huang, S., & Liu, D. (2013). A new crossover approach 
 for solving the multiple travelling salesmen problem using genetic algorithms. 
 European Journal of Operational Research, 228(1), 72-82.
 */
package openga.operator.crossover;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 *
 * @author YU-TANG CHANG
 */
public class TCSCFCrossover extends TPCrossOver implements CrossoverMTSPI {

  private int numberofSalesmen;
  private int type;

  public TCSCFCrossover(int type) {
    this.type = type;
  }

//  /*
  public static void main(String[] args) {
    int[] mom = {8, 6, 3, 9, 7, 2, 5, 4, 0, 1, 3, 3, 3, 1},
            dad = {2, 3, 1, 5, 8, 4, 9, 6, 7, 0, 3, 2, 2, 3};
    TCSCFCrossover TCSCFX = new TCSCFCrossover(0);
    TCSCFX.numberofSalesmen = 4;
    TCSCFX.CrossOver(mom, dad, 1);
  }
//  */

  //start to crossover
  public void startCrossover() {
    for (int i = 0; i < popSize; i++) {
      //test the probability is larger than crossoverRate.
      if (Math.random() <= crossoverRate) {
        //to get the other chromosome to crossover
        int index2 = getCrossoverChromosome(i);
        int[] mom = originalPop.getSingleChromosome(i).genes;
        int[] dad = originalPop.getSingleChromosome(index2).genes;
        int[] Result = CrossOver(mom, dad);
        newPop.getSingleChromosome(i).genes = Result;

//        System.out.print("mom: ");
//        for (int j = 0; j < mom.length; j++) {
//          System.out.print(mom[j] + ", ");
//        }
//        System.out.println("End");
      }
    }
  }

//for test :
/*
  numberofSalesmen = 4
  order/numberofCities = 10
  chromosomeLength = 14
  
   */
  public List<Integer> CrossOver(int[] mom, int[] dad, int x) {
    chromosomeLength = mom.length;
    int numberofCities = chromosomeLength - numberofSalesmen;
    List<Integer> child = new ArrayList<>();
    List<List<Integer>> childGenes = new ArrayList<>(numberofSalesmen);
    List<List<Integer>> momGenes = new ArrayList<>();
    List<List<Integer>> dadGenes = new ArrayList<>();
    List<List<Integer>> sameGenes = new ArrayList<>();
    int length = 0;
    momGenes.addAll(genestoList(mom));
    dadGenes.addAll(genestoList(dad));

    if (type == 2) {
      type = numberofSalesmen;
    }
    for (int s = 0; s < numberofSalesmen - type; s++) {
      List<Integer> genes = new ArrayList<>();
      for (int m = 0; m < momGenes.get(s).size(); m++) {
        for (int d = 0; d < dadGenes.get(s).size(); d++) {
          if (momGenes.get(s).get(m) == dadGenes.get(s).get(d)) {
            genes.add(momGenes.get(s).get(m));
            momGenes.get(s).set(m, -1);
            dadGenes.get(s).set(d, -1);
          }
        }
      }
      sameGenes.add(genes);
    }
    System.out.println("momGenes: " + momGenes.toString());
    System.out.println("dadGenes: " + dadGenes.toString());
    System.out.println("sameGenes: " + sameGenes.toString());

    List<Integer> selectGenes = new ArrayList<>();
    for (int s = 0; s < numberofSalesmen; s++) {
      setCutpoint(momGenes, s);
      System.out.println("cutPoint1: " + cutPoint1);
      System.out.println("cutPoint2: " + cutPoint2);

      for (int m = cutPoint1; m < cutPoint2; m++) {
        if (momGenes.get(s).get(m) != -1) {
          selectGenes.add(momGenes.get(s).get(m));
          childGenes.get(s).add(momGenes.get(s).get(m));
          System.out.print("selectGene" + momGenes.get(s).get(m));
        }
      }

      for (int m = 0; m < momGenes.get(s).size(); m++) {
        if (m >= cutPoint1 && m < cutPoint2 && momGenes.get(s).get(m) != -1) {
          selectGenes.add(momGenes.get(s).get(m));
          childGenes.get(s).add(momGenes.get(s).get(m));
          momGenes.get(s).set(m, -1);
          System.out.print("selectGene" + momGenes.get(s).get(m));
        } else if (segmentofMom[i][j] != -1) {
          tmpSelection.add(segmentofMom[i][j]);
//          System.out.print("dad"+segmentofMom[i][j]);
          segmentofMom[i][j] = -2;
        }
      }

//      System.out.println();
      segmentofmomSelection.add(momSelection);
      sizeofmomSelection[i] = momSelection.size();
    }

    return child;
  }

  public void setCutpoint(List<List<Integer>> genes, int i) {
    do {
      cutPoint1 = new Random().nextInt(genes.get(i).size() + 1);
      cutPoint2 = new Random().nextInt(genes.get(i).size() + 1);
      if (genes.get(i).size() == 0) {
        break;
      }
    } while (cutPoint1 == cutPoint2);
    if (cutPoint1 > cutPoint2) {
      int tmp = cutPoint1;
      cutPoint1 = cutPoint2;
      cutPoint2 = tmp;
    }
  }

  public List<List<Integer>> genestoList(int[] genes) {
    int numberofCities = chromosomeLength - numberofSalesmen;
    int length = 0;
    List<List<Integer>> geneList = new ArrayList<>();
    for (int i = 0; i < numberofSalesmen; i++) {
      List<Integer> gene = new ArrayList<>();
      for (int j = 0; j < genes[numberofCities + i]; j++) {
        gene.add(genes[length]);
        length++;
      }
      geneList.add(gene);
    }
    return geneList;
  }

  //type = 0 : mtsp,  type = 2 : OA,  type = 3 : TCX
  @Override
  public int[] CrossOver(int[] mom, int[] dad) {
    int[] Child = new int[mom.length];

    int[][] segmentofChild = new int[numberofSalesmen][];
    int[][] segmentofMom = new int[numberofSalesmen][];
    int[][] segmentofDad = new int[numberofSalesmen][];

    List<List<Integer>> segmentofSamesite = new ArrayList<>();
    List<Integer> Samesite;
    int[] sizeofSamesite = new int[numberofSalesmen];

    List<List<Integer>> segmentofmomSelection = new ArrayList<>();
    List<Integer> momSelection;
    int[] sizeofmomSelection = new int[numberofSalesmen];

    List<Integer> tmpSelection = new ArrayList<>();

    List<List<Integer>> segmentofdadSelection = new ArrayList<>();
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
        System.out.print(segmentofMom[i][j] + " ");
      }
      System.out.println();
    }
//    */
//print segmentofDad
    /*
    System.out.println("segmentofDad = ");
    for (int i = 0; i < segmentofDad.length; i++) {
      for (int j = 0; j < segmentofDad[i].length; j++) {
        System.out.print(segmentofDad[i][j] + " ");
      }
      System.out.println();
    }
//    */
//    System.out.println("start Same-site copy first");
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
        cutPoint1 = new Random().nextInt(segmentofMom[i].length + 1);
        cutPoint2 = new Random().nextInt(segmentofMom[i].length + 1);
        if (segmentofMom[i].length == 0) {
          break;
        }
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
        } else if (segmentofMom[i][j] != -1) {
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
          if (segmentofDad[i][j] == tmpSelection.get(k)) {
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
        System.out.print(segmentofmomSelection.get(i).get(j) + " ");
      }
      System.out.println();
    }
//    */
//print tmpSelection
    /*
    System.out.println("tmpSelection = ");
    for (int i = 0; i < tmpSelection.size(); i++) {
      System.out.print(tmpSelection.get(i) + " ");
    }
    System.out.println();
//    */
//print dadSelection
    /*
    System.out.println("dadSelection = ");
    for (int i = 0; i < segmentofdadSelection.size(); i++) {
      for (int j = 0; j < sizeofdadSelection[i]; j++) {
        System.out.print(segmentofdadSelection.get(i).get(j) + " ");
      }
      System.out.println();
    }
//    */
//print segmentofMom
    /*
    System.out.println("segmentofMom = ");
    for (int i = 0; i < segmentofMom.length; i++) {
      for (int j = 0; j < segmentofMom[i].length; j++) {
        System.out.print(segmentofMom[i][j] + " ");
      }
      System.out.println();
    }
//    */
//print segmentofDad
    /*
    System.out.println("segmentofDad = ");
    for (int i = 0; i < segmentofDad.length; i++) {
      for (int j = 0; j < segmentofDad[i].length; j++) {
        System.out.print(segmentofDad[i][j] + " ");
      }
      System.out.println();
    }
//    */
    for (int i = 0; i < segmentofChild.length; i++) {//segmentofChild.length = 3
      segmentofChild[i] = new int[sizeofSamesite[i] + sizeofdadSelection[i] + sizeofmomSelection[i]];
//      System.out.println("sizeofSamesite["+i+"] = "+sizeofSamesite[i]);
//      System.out.println("sizeofdadSelection["+i+"] = "+sizeofdadSelection[i]);
//      System.out.println("sizeofmomSelection["+i+"] = "+sizeofmomSelection[i]);
//      System.out.println("segmentofChild["+i+"] = "+segmentofChild[i].length);
//      System.out.println();
    }

    Length = 0;
    int[] position = new int[numberofSalesmen];

    for (int i = type; i < numberofSalesmen; i++) {
      for (int j = 0; j < sizeofSamesite[i]; j++) {
        segmentofChild[i][position[i]] = segmentofSamesite.get(Length).get(j);
        position[i]++;
      }
      Length++;
    }

    for (int i = 0; i < numberofSalesmen; i++) {
      for (int j = 0; j < sizeofmomSelection[i]; j++) {
        segmentofChild[i][position[i]] = segmentofmomSelection.get(i).get(j);
        position[i]++;
      }
    }

    for (int i = 0; i < numberofSalesmen; i++) {
      for (int j = 0; j < sizeofdadSelection[i]; j++) {
        segmentofChild[i][position[i]] = segmentofdadSelection.get(i).get(j);
        position[i]++;
      }
    }

    //print segmentofChild
    /*
    System.out.println("segmentofChild = ");
    for (int i = 0; i < segmentofChild.length; i++) {
      for (int j = 0; j < segmentofChild[i].length; j++) {
        System.out.print(segmentofChild[i][j] + " ");
      }
      System.out.println();
    }
    //    */
//    int L = 0;
//    for (int i = 0; i < segmentofChild.length; i++) {
//      L += segmentofChild[i].length;
//    }
//    L+=segmentofChild.length;
//    System.out.println("L: "+L);
//    System.out.println("Child length: "+Child.length);
    Length = 0;
    for (int i = 0; i < segmentofChild.length; i++) {
      for (int j = 0; j < segmentofChild[i].length; j++) {
        Child[Length] = segmentofChild[i][j];
        Length++;
      }
    }
    for (int i = 0; i < numberofSalesmen; i++) {
      Child[Length] = segmentofChild[i].length;
      Length++;
    }
    return Child;
  }

  @Override
  public void setNumberofSalesmen(int numberofSalesmen) {
    this.numberofSalesmen = numberofSalesmen;
  }
}
