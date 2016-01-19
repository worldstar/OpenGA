/*
 * This method implements the new two-part encoding crossover by 
 Yuan, S., Skinner, B., Huang, S., & Liu, D. (2013). A new crossover approach 
 for solving the multiple travelling salesmen problem using genetic algorithms. 
 European Journal of Operational Research, 228(1), 72-82.
 */
package openga.operator.crossover;

import openga.chromosomes.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author UlyssesZhang
 */
public class TCSCFCrossover extends TPCrossOver implements CrossoverMTSPI {

  private int numberofSalesmen;

  public static void main(String[] args) {
    int[] dad = {1, 2, 3, 4, 5, 6, 7, 8, 9, 4, 2, 3},
            mom = {1, 2, 3, 4, 5, 6, 7, 8, 9, 2, 2, 5};

    TCSCFCrossover TCSCFX = new TCSCFCrossover();
    TCSCFX.CrossOver2(mom, dad);
  }

  public int[] CrossOver2(int[] mom, int[] dad) {

    numberofSalesmen = 3;//test
    int[] Child = new int[mom.length];

    List<List<Integer>> segmentofChild = new ArrayList<List<Integer>>();
    List<Integer> sizeofchildCities = new ArrayList<Integer>();
    List<Integer> segmentofChildcities = new ArrayList<Integer>();;
    int[][] segmentofMom = new int[numberofSalesmen][];
    int[][] segmentofDad = new int[numberofSalesmen][];
    int cities = mom.length - numberofSalesmen;
    boolean isOA = false;
    boolean hasSame = false;
    //according to the numberofSalesmen,Cutting the gene segment and assign it to segmentofMom
    int length = 0;

    for (int i = 0; i < numberofSalesmen; i++) {

      segmentofMom[i] = new int[mom[cities]];
      for (int j = 0; j < mom[cities]; j++) {
        segmentofMom[i][j] = mom[length];
        length++;
      }
      cities++;
    }
    //print segmentofMom
//    System.out.println("segmentofMom = ");
//    for(int i = 0; i < segmentofMom.length; i++){
//      for(int j = 0; j < segmentofMom[i].length; j++){
//        System.out.print(segmentofMom[i][j]);
//      }
//      System.out.println();
//    }
//    System.out.println();

    //according to the numberofSalesmen,Cutting the gene segment and assign it to segmentofDad
    length = 0;
    cities = dad.length - numberofSalesmen;
    for (int i = 0; i < numberofSalesmen; i++) {

      segmentofDad[i] = new int[dad[cities]];
      for (int j = 0; j < dad[cities]; j++) {
        segmentofDad[i][j] = dad[length];
        length++;
      }
      cities++;
    }
    //print segmentofDad
//    System.out.println("segmentofDad = ");
//    for(int i = 0; i < segmentofDad.length; i++){
//      for(int j = 0; j < segmentofDad[i].length; j++){
//        System.out.print(segmentofDad[i][j]);
//      }
//      System.out.println();
//    }
//    System.out.println();

    if (isOA) {
      segmentofChildcities = new ArrayList<Integer>();
      for (int j = 0; j < segmentofMom[mom.length - 1].length; j++) {
        for (int k = 0; k < segmentofDad[mom.length - 1].length; k++) {
          if (segmentofMom[mom.length - 1][j] == segmentofDad[mom.length - 1][k]) {
            segmentofChildcities.add(segmentofMom[mom.length - 1][j]);
            hasSame = true;
          }
        }
      }
    } else {

      for (int i = 0; i < numberofSalesmen; i++) {
        segmentofChildcities = new ArrayList<Integer>();
        for (int j = 0; j < segmentofMom[i].length; j++) {
          for (int k = 0; k < segmentofDad[i].length; k++) {
            if (segmentofMom[i][j] == segmentofDad[i][k]) {
              segmentofChildcities.add(segmentofMom[i][j]);
              segmentofMom[i][j] = -1;
              hasSame = true;
            }
          }
        }
        if (hasSame) {
          sizeofchildCities.add(segmentofChildcities.size());
          segmentofChild.add(segmentofChildcities);
        }
      }

      System.out.println("segmentofMom = ");
      for (int i = 0; i < segmentofMom.length; i++) {
        for (int j = 0; j < segmentofMom[i].length; j++) {
          System.out.print(segmentofMom[i][j]);
        }
        System.out.println();
      }
      System.out.println();

      int cutPoint1, cutPoint2;

      for (int i = 0; i < numberofSalesmen; i++) {
        do {
          cutPoint1 = new Random().nextInt(segmentofMom[i].length);
          cutPoint2 = new Random().nextInt(segmentofMom[i].length);
        } while (cutPoint1 == cutPoint2);

        if (cutPoint1 > cutPoint2) {
          int tmp = cutPoint1;
          cutPoint1 = cutPoint2;
          cutPoint2 = tmp;
        }

        System.out.println("cutPoint1 = " + cutPoint1);
        System.out.println("cutPoint2 = " + cutPoint2);
        System.out.println("segmentofMom = ");
        for (int j = cutPoint1; j < cutPoint2; j++) {
          segmentofChild.get(i).add(segmentofMom[i][j]);
          System.out.print(segmentofMom[i][j]);
        }
        System.out.println();
      }

    }
    for (int i = 0; i < segmentofChild.size(); i++) {
      for (int j = 0; j < sizeofchildCities.get(i); j++) {
        System.out.print(segmentofChild.get(i).get(j));
      }
      System.out.println();
    }

    return Child;
  }

  public int[] CrossOver(int[] mom, int[] dad) {
    List<Integer> child = new ArrayList<>();
    int numberofCities = mom.length - numberofSalesmen;

    int cutPoint1, cutPoint2;
    List<Integer> momGenepool = new ArrayList<>();
    List<Integer> tmpGenepool = new ArrayList<>();
    List<Integer> dadGenepool = new ArrayList<>();
    int[] momGenepoolofSalesmen = new int[numberofSalesmen];
    int[] dadGenepoolofSalesmen = new int[numberofSalesmen];

    int stopPosition = 0;
    int currentPosition = 0;

    for (int i = 0; i < numberofSalesmen; i++) {
      //set stopPosition
//      System.out.println(" stopPosition"+stopPosition+" currentPosition"+currentPosition);
      stopPosition += mom[numberofCities + i];
      //set cutpoints
      if ((stopPosition - currentPosition) == 1) {
        cutPoint1 = 0 + currentPosition;
        cutPoint2 = 1 + currentPosition;
      } else {
        do {
          cutPoint1 = new Random().nextInt(stopPosition - currentPosition) + currentPosition;
          cutPoint2 = new Random().nextInt(stopPosition - currentPosition) + currentPosition;
//          System.out.println(""+" cutPoint1 "+cutPoint1 +" cutPoint2 "+cutPoint2);
        } while (cutPoint1 == cutPoint2);
      }
      if (cutPoint1 > cutPoint2) {
        int tmp = cutPoint1;
        cutPoint1 = cutPoint2;
        cutPoint2 = tmp;
      }
//      System.out.println(""+" cutPoint1 "+cutPoint1 +" cutPoint2 "+cutPoint2);
      //copy gene to child.
      for (int j = currentPosition; j < stopPosition; j++) {
        if (cutPoint1 <= j && j < cutPoint2) {
          momGenepool.add(mom[j]);
          momGenepoolofSalesmen[i]++;
//          System.out.println(" momGenepool "+mom[j]);
        } else {
          tmpGenepool.add(mom[j]);
//          System.out.println(" tmpGenepool "+mom[j]);
        }
      }
      currentPosition = stopPosition;
    }

    //sort dad gene segment.
    stopPosition = dad[numberofCities];
    currentPosition = 0;
    for (int i = 0; i < numberofSalesmen; i++) {
      for (int j = currentPosition; j < stopPosition; j++) {
        for (int k = 0; k < tmpGenepool.size(); k++) {
          if (dad[j] == tmpGenepool.get(k)) {
            dadGenepool.add(tmpGenepool.get(k));
            dadGenepoolofSalesmen[i]++;
//            System.out.println(" dadGenepool "+dad[j]);
          }
        }
      }
      currentPosition = stopPosition;
      stopPosition += (numberofCities - currentPosition);
    }
    //marge gene to child.
    int p1 = 0;
    int p2 = 0;
    for (int i = 0; i < numberofSalesmen; i++) {
      for (int j = 0; j < momGenepoolofSalesmen[i]; j++) {
        child.add(momGenepool.get(p1));
        p1++;
      }
      for (int j = 0; j < dadGenepoolofSalesmen[i]; j++) {
        child.add(dadGenepool.get(p2));
        p2++;
      }
    }
    for (int i = 0; i < numberofSalesmen; i++) {
      child.add(momGenepoolofSalesmen[i] + dadGenepoolofSalesmen[i]);
    }
    //assign child to result then return.
    int[] result = new int[child.size()];
    for (int i = 0; i < child.size(); i++) {
      result[i] = child.get(i);
    }
//    System.out.println(" fin ");
    return result;
  }
}
