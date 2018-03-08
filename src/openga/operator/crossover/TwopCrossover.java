
package openga.operator.crossover;

import java.util.Random;

public class TwopCrossover extends TPCrossOver implements CrossoverMTSPI {
  
  int numberofSalesmen;
  int type;
  int job;
  int[] Child;
//  static int[] Mom;
//  static int[] Dad;

  
    @Override
    public void startCrossover() {
    for (int i = 0; i < popSize; i++) {
      // test the probability is larger than crossoverRate.
      if (Math.random() <= crossoverRate) {
        //to get the other chromosome to crossover
        int index2 = getCrossoverChromosome(i);
        int[] Mom = originalPop.getSingleChromosome(i).genes;
        int[] Dad = originalPop.getSingleChromosome(index2).genes;
        int[] Result = CrossOver(Mom, Dad);
        newPop.getSingleChromosome(i).genes = Result;
      }
    }
  }


  public int[] CrossOver(int[] Mom, int[] Dad){
    Child = new int[Mom.length];
    int length = Mom.length-numberofSalesmen;

    // Find the same genes 
    for (int i = 0; i < length; i++) {
      if (Mom[i] == Dad[i]) {
        Child[i] = Mom[i];
      }
    }
//    result_Child();
    // Random crossover sites 隨機交配站 
    Random ran = new Random(); //宣告亂數
    int[] crossover = new int[3];
    for (int i = 0; i < 3; i++) {
      if (i != 2) { //第一段的隨機交配點
        crossover[i] = ran.nextInt(length);
        if (crossover[1] == crossover[0]) {
          i = i - 1;
        }
      } else { // 第二段的隨機交配點
        crossover[2] = ran.nextInt(numberofSalesmen - 1) + length + 1;
      }
    }
    crossover[0] = 3; crossover[1] = 8; // Test   Over  can  Delect
    // swap 
    if (crossover[0] > crossover[1]) {
      int temp = crossover[0];
      crossover[0] = crossover[1];
      crossover[1] = temp;
    }
//    System.out.println("Crossover  One : " + crossover[0] + "~" + crossover[1]);
//    System.out.println("Crossover  Two : " + crossover[2]);
    //  copy the subsequence from Parent1
    for (int i = crossover[0]; i <= crossover[1]; i++) {
      Child[i] = Mom[i];
    }
    // 反轉第二段兩個基因區段排列的順序
    int[] two = new int[numberofSalesmen];
    int count2 = length;
    for (int i = crossover[2]; i < Child.length; i++) {
      Child[count2] = Mom[i];
      count2++;
    }
    for (int i = length; i < crossover[2]; i++) {
      Child[count2] = Mom[i];
      count2++;
    }
//    result_Child();

    // copy the order from Parent2 從Parent2按照順序複製
//    System.out.print("miss genes: ");
    for (int i = 0; i < length; i++) {
      int count = 0;
      for (int j = 0; j < length; j++) { // Find the missing genes  尋找遺失基因
        if (Dad[i] != Child[j]) {
          count++;
        }
      }
      if (count == length) { // After finding a fill 找到遺失基因後填上
        for (int j = 0; j < length; j++) {
          if (Child[j] == -1) {
            Child[j] = Dad[i];
//            System.out.print(Child[j] + " ");
            break;
          }
        }
      }
    }
//    System.out.println();
//    result_Child();
    return Child;
  }

  @Override
  public void setNumberofSalesmen(int numberofSalesmen) {
    this.numberofSalesmen = numberofSalesmen;
  }

//  public static void main(String[] args) {
//    TCSCFCrossover3 a = new TCSCFCrossover3();
//    a.setParameter(2, 9);
//    System.out.println("Order  : 0 1 2 3 4 5 6 7 8   T W O");
//    System.out.print("Mom    : ");    a.setGenes(Mom);    System.out.println();
//    System.out.print("Dad    : ");    a.setGenes(Dad);    System.out.println();
//    int aa[] = a.evaluateAll();
//    for (int i=0;i<aa.length;i++){
//      System.out.print(aa[i]+" ");
//    }
//  }
  
    
//  public void setParameter(int numberofSalesmen, int job) {
//    this.numberofSalesmen = numberofSalesmen;
//    this.job = job;
//    this.Mom = new int[job + numberofSalesmen];
//    this.Dad = new int[job + numberofSalesmen];
//    this.Child = new int[job + numberofSalesmen];
//    //初始化child 
//    for (int i = 0; i < job; i++) {
//      Child[i] = -1;
//    }
//  }

  
//  public void setGenes(int Genes[]) {
//    Random ran = new Random(); //宣告亂數
//    int[] Order = new int[job];
//    for (int i = 0; i < job; i++) {
//      Order[i] = i;
//    }
//    //比如 Order = 1 2 3  程式執行第一次 亂數出來 2 取走Order中的 2 給Parent[i] 然後亂數範圍 從 原本1~3 變成 1~2  而相對應的Order的數字 變成 1 3 ...類推
//    int Random_value;
//    for (int i = 0; i < job; i++) {
//      Random_value = ran.nextInt(job - i);
//      Genes[i] = Order[Random_value];
//      for (int j = Random_value; j < job - i - 1; j++) {
//        Order[j] = Order[j + 1]; //每取走一筆 將後面每一格都往前挪
//      }
//    }
//    setTwoPoint(Genes);
//  }
  
  //  public void setTwoPoint(int Genes[]) {
//    int sum = 0;
//    Random ran = new Random();
//    for (int i = job; i < job + numberofSalesmen; i++) {
//      if (i == job + numberofSalesmen - 1) {
//        Genes[i] = job - sum;
//      } else {
//        Genes[i] = ran.nextInt(job - sum);
//        sum += Genes[i];
//      }
//    }
//    for (int i = 0; i < job + numberofSalesmen; i++) {
//      if (i == job) {
//        System.out.print("  ");
//      }
//      System.out.print(Genes[i] + " ");
//    }
//  }
  
//  public void result_Child() {
//    System.out.print("Child  : ");
//    for (int i = 0; i < Child.length; i++) {
//      if (i == job) {
//        System.out.print("  ");
//      }
//      if (Child[i] != -1) {
//        System.out.print(Child[i] + " ");
//      } else {
//        System.out.print("  ");
//      }
//    }
//    System.out.println();
//  }
}
