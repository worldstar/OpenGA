
package openga.operator.crossover;

import java.util.Random;
public class TCX_SCF_TPcrossover extends TPCrossOver implements CrossoverMTSPI {
  
  int numberofSalesmen;
  int type;
  
  @Override
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
      }
    }
  }
  
  public static void main (String args[]){
//    int[] Mom = {2,9,8,6,5,7,3,4,1,0,3,7};
//    int[] Dad = {9,6,8,0,5,4,3,2,7,1,2,8};
    int[] Mom = {5,9,7,8,1,3,6,2,4,6,3};
    int[] Dad = {8,2,3,7,9,4,1,6,5,4,5};
    TCX_SCF_TPcrossover a = new TCX_SCF_TPcrossover();
    a.setNumberofSalesmen(2);
  
//    for (int j=0;j<30;j++){
      int aa[] = a.CrossOver(Mom, Dad);
     for (int i=0;i<aa.length;i++){
        System.out.print(aa[i]+" ");
      }System.out.println();
    }
//  }
    
   @Override
   public int[] CrossOver(int[] Mom, int[] Dad){
    int Child[] =new int[Mom.length];
    int length = Mom.length-numberofSalesmen; //推銷員的位置 / 扣除推銷員後真實訂單長度
    int segmentofMom[][] = new int[numberofSalesmen][]; //  將 Mom基因 依其推銷員 包含多少 分割  
    int segmentofDad[][] = new int[numberofSalesmen][]; //  將 Dad基因 依其推銷員 包含多少 分割  
    int segmentofChild[][] = new int[numberofSalesmen][length]; // 
    int remainingGenes[][] = new int[numberofSalesmen][]; // 剩餘的基因
    int GeneChild[][] = new int[numberofSalesmen][length]; // List 部分的每位推銷員各自擁有的基因
    int ListSalse[] = new int[numberofSalesmen];
    int MomSalse[] = new int[numberofSalesmen];
    int DadSalse[] = new int[numberofSalesmen];

    //初始化Child[ ] = -1 
    for (int i=0;i<Child.length;i++){Child[i] =-1;}
    
    //推銷員分割
    int MomSalesLength=0,DadSalesLength=0;// 母親的每一個推銷員 複製給二維陣列時
    for (int i=0;i<numberofSalesmen;i++){
      segmentofMom[i] = new int[Mom[length+i]];
      for (int j=0;j<Mom[length+i];j++){
        segmentofMom[i][j] = Mom[MomSalesLength];
        MomSalesLength++;
      }
      segmentofDad[i] = new int[Dad[length+i]];
      for (int j=0;j<Dad[length+i];j++){
        segmentofDad[i][j] = Dad[DadSalesLength];
        DadSalesLength++;
      }
    }
    //輸出上方推銷員分割結果
//    System.out.print("Mom: ");
//    for (int i=0;i<numberofSalesmen;i++){
//      for (int j=0;j<segmentofMom[i].length;j++){
//        System.out.print(segmentofMom[i][j]+" ");
//      } System.out.print(" ");
//    }System.out.print("\nDad: ");
//    
//    for (int i=0;i<numberofSalesmen;i++){
//      for (int j=0;j<segmentofDad[i].length;j++){
//        System.out.print(segmentofDad[i][j]+" ");
//      } System.out.print(" ");
//    }System.out.println();

    //尋找每個推銷員內相同的基因 
    int ChildLength=0;
    for (int i=0;i<numberofSalesmen;i++){
      for (int j=0;j<Mom[length+i];j++){
        for (int k=0;k<Dad[length+i];k++){
          if (segmentofMom[i][j] == segmentofDad[i][k]){
            segmentofChild[i][ChildLength] = segmentofMom[i][j]; 
             ChildLength++;
          }
        }
      }
      remainingGenes[i] = new int[Mom[length+i]-ChildLength];//這個只是給長度
//      System.out.println(Mom[length+i]-ChildLength+" ");               
      ChildLength=0;
    }
            
    //輸出每個推銷員內相同的基因，複製給小孩
    int GeneChildLength[]= new int[numberofSalesmen];    
    for (int i=0;i<numberofSalesmen;i++){
      for (int j=0;j<segmentofChild[i].length;j++){
        if (segmentofChild[i][j] !=0){
          GeneChild[i][GeneChildLength[i]] = segmentofChild[i][j];GeneChildLength[i]++;
          ListSalse[i]++;// List 各推銷員數量
//         System.out.print(segmentofChild[i][j]+" ");  
        }
      } //System.out.print(" ");  
    } //System.out.println("父母 相同的基因");  
    
    //每個推銷員剩餘的基因
    boolean Need = true;
    for (int i=0;i<numberofSalesmen;i++){
      for (int j=0;j<Mom[length+i];j++){
        for (int k=0;k<Dad[length+i];k++){// 除第一個推銷員外，其餘只要符合組內相同數字，所以這裡只需要找組內，皆不同的數字即可扔給"陣列"(剩餘的基因)
          if (segmentofMom[i][j] == segmentofDad[i][k]){
            Need = false;break;
          }          
        }
        if (Need == true){
            remainingGenes[i][ChildLength] = segmentofMom[i][j];       
//            System.out.print(remainingGenes[i][ChildLength]+" ");  //**********************************************************
            ChildLength++;
        }
        Need = true;
      }ChildLength=0;
//      System.out.print(" ");  //**********************************************************
    }//System.out.println("母親 剩餘的基因");  //**********************************************************
    
    //每個推銷員的隨機切點
    Random ran = new Random();
    int cutPoint[][] = new int[numberofSalesmen][numberofSalesmen];
    for (int i=0;i<numberofSalesmen;i++){   
      int SalseLength = remainingGenes[i].length+1;
       do {
        if (remainingGenes[i].length == 0) {
          break;
        }
//         if (SalseLength == 1) SalseLength = 2;
        cutPoint[i][0] = new Random().nextInt(SalseLength);
        cutPoint[i][1] = new Random().nextInt(SalseLength);
//        System.out.println(cutPoint[i][0]+" "+cutPoint[i][1] + "    "+ SalseLength);
      } while (cutPoint[i][0] == cutPoint[i][1]);
//       if (i == 0) {
//          cutPoint[i][0] = 1;
//          cutPoint[i][1] = 3;
//       }else{
//          cutPoint[i][0] = 1;
//          cutPoint[i][1] = 2;
//       } 

      if (cutPoint[i][0]>cutPoint[i][1]){int temp=cutPoint[i][1];cutPoint[i][1] = cutPoint[i][0]; cutPoint[i][0] = temp;}
      MomSalse[i] = cutPoint[i][1]-cutPoint[i][0]; //母親那邊 隨機切點 相減 就等於 母親這邊的各推銷員數量
//      System.out.println(cutPoint[i][0]+" "+cutPoint[i][1]+"  切點");
    }


    // 由於基因裡面包含0 因此將陣列空值處 更改為-1
    for (int i=0;i<numberofSalesmen;i++){
      for (int j=0;j<GeneChild[i].length;j++){
        if (GeneChild[i][j] ==0){
          GeneChild[i][j] = -1;
        }
      }
    }    
    //使用上述的隨機切點從Mom基因複製給小孩
    for (int i=0;i<numberofSalesmen;i++){
      cutPoint[i][1] -= 1;
      for (int j=cutPoint[i][0];j<=cutPoint[i][1];j++){
        GeneChild[i][GeneChildLength[i]]=remainingGenes[i][j];   
        GeneChildLength[i] ++;                 
      }
    }

    ChildLength=0;
    for (int i=0;i<numberofSalesmen;i++){
        for (int j=0;j<length;j++){
          if (GeneChild[i][j] == -1) break;
//          System.out.print(GeneChild[i][j]+" "); 
          Child[ChildLength] = GeneChild[i][j];ChildLength++;
        }//System.out.print(" ");
    }//System.out.println("母親那邊隨機切完且複製給小孩的結果");


    
    int count=0; //檢查是否為遺失的基因
    for (int i=0;i<numberofSalesmen;i++){
      for (int j=0;j<segmentofDad[i].length;j++){       
        for (int k=0;k<length;k++){
          if (segmentofDad[i][j] != Child[k]){
            count++;
//            System.out.print(count+" ");
          } else {
            break;
          }
        }
        if (count==length){
//          System.out.println("**"+segmentofDad[i][j]);
        DadSalse[i] ++; // Dad 那裡的各推銷員數量   
          for (int k=0;k<length;k++){
            if (GeneChild[i][k] == -1 ){
              GeneChild[i][k] = segmentofDad[i][j];
//              System.out.print(GeneChild[i][k]+" ");
              break;
            }
//            missGene[missLength] = segmentofDad[i][j];            
          }
        }
        count=0;
      }
    }//System.out.println();

    
    //將二維陣列內不同推銷員的小孩基因，合併指派給一維的Child內
    ChildLength=0;
    for (int i=0;i<numberofSalesmen;i++){
      for (int j=0;j<GeneChild[i].length;j++){
        if (GeneChild[i][j] !=-1){
          Child[ChildLength] = GeneChild[i][j];
//          System.out.print(Child[ChildLength]+" ");
          ChildLength++;        
        }
      }
    }
   
    //將推銷員補上
    count=0;
    for (int i=length;i<Mom.length;i++){
      Child[i] = ListSalse[count] + MomSalse[count] + DadSalse[count];count++;
//      System.out.print(Child[i]+" ");
    }

    return Child;
  }
  
  
  @Override
  public void setNumberofSalesmen(int numberofSalesmen) {
    this.numberofSalesmen = numberofSalesmen;
  }
  
  
  
//    public int[] Type3(int[] Mom, int[] Dad){
//    int Child[] =new int[Mom.length];
//    int length = Mom.length-numberofSalesmen; //推銷員的位置 / 扣除推銷員後真實訂單長度
//    int segmentofMom[][] = new int[numberofSalesmen][]; //  將 Mom基因 依其推銷員 包含多少 分割  
//    int segmentofDad[][] = new int[numberofSalesmen][]; //  將 Dad基因 依其推銷員 包含多少 分割  
//    int segmentofChild[][] = new int[numberofSalesmen][length]; // 
//    int remainingGenes[][] = new int[numberofSalesmen][]; // 剩餘的基因
//    int GeneChild[][] = new int[numberofSalesmen][length]; // List 部分的每位推銷員各自擁有的基因
//    int ListSalse[] = new int[numberofSalesmen];
//    int MomSalse[] = new int[numberofSalesmen];
//    int DadSalse[] = new int[numberofSalesmen];
//    
//    //初始化Child[ ] = -1
//    for (int i=0;i<Child.length;i++){Child[i] =-1;}
//    
//    //推銷員分割
//    int MomSalesLength=0,DadSalesLength=0;// 母親的每一個推銷員 複製給二維陣列時
//    for (int i=0;i<numberofSalesmen;i++){
//      segmentofMom[i] = new int[Mom[length+i]];
//      for (int j=0;j<Mom[length+i];j++){
//        segmentofMom[i][j] = Mom[MomSalesLength];
//        MomSalesLength++;
//      }
//      segmentofDad[i] = new int[Dad[length+i]];
//      for (int j=0;j<Dad[length+i];j++){
//        segmentofDad[i][j] = Dad[DadSalesLength];
//        DadSalesLength++;
//      }
//    }
//    //輸出上方推銷員分割結果
////    System.out.print("Mom:");
////    for (int i=0;i<numberofSalesmen;i++){
////      for (int j=0;j<segmentofMom[i].length;j++){
////        System.out.print(segmentofMom[i][j]+" ");
////      } System.out.print(" ");
////    }System.out.print("\nDad:");
////    
////    for (int i=0;i<numberofSalesmen;i++){
////      for (int j=0;j<segmentofDad[i].length;j++){
////        System.out.print(segmentofDad[i][j]+" ");
////      } System.out.print(" ");
////    }System.out.println();
//
//    //尋找每個推銷員內相同的基因 
//    int ChildLength=0;
//    for (int i=0;i<numberofSalesmen;i++){
//      for (int j=0;j<Mom[length+i];j++){
//        for (int k=0;k<Dad[length+i];k++){
//          if (segmentofMom[i][j] == segmentofDad[i][k]){
//            segmentofChild[i][ChildLength] = segmentofMom[i][j]; 
//            ChildLength++;
//          }
//        }
//      }
//      remainingGenes[i] = new int[Mom[length+i]-ChildLength];//這個只是給長度
////      System.out.print(Mom[length+i]-ChildLength+" ");
//      ChildLength=0;
//    }//System.out.println();
//            
//    //輸出每個推銷員內相同的基因，複製給小孩
//    int GeneChildLength[]= new int[numberofSalesmen];    
//    for (int i=0;i<numberofSalesmen;i++){
//      for (int j=0;j<segmentofChild[i].length;j++){
//        if (segmentofChild[i][j] !=0){
//          GeneChild[i][GeneChildLength[i]] = segmentofChild[i][j];GeneChildLength[i]++;
//          ListSalse[i]++;// List 各推銷員數量
////         System.out.print(segmentofChild[i][j]+" ");
//        }
//      } //System.out.print(" ");
//    } //System.out.println("父母 相同的基因");
//    
//    //每個推銷員剩餘的基因
//    boolean Need = true;
//    for (int i=0;i<numberofSalesmen;i++){
//      for (int j=0;j<Mom[length+i];j++){
//        for (int k=0;k<Dad[length+i];k++){
//          if (segmentofMom[i][j] == segmentofDad[i][k]){
//            Need = false;break;
//          }          
//        }
//        if (Need == true){
//          remainingGenes[i][ChildLength] = segmentofMom[i][j];       
////          System.out.print(remainingGenes[i][ChildLength]+" ");
//          ChildLength++;
//        }
//        Need = true;
//      } ChildLength=0;
//      //System.out.print(" ");
//    }//System.out.println("母親 剩餘的基因");
//
//    
//    //每個推銷員的隨機切點
//    Random ran = new Random();
//    int cutPoint[][] = new int[numberofSalesmen][numberofSalesmen];
//    for (int i=0;i<numberofSalesmen;i++){
//      int SalesLength = remainingGenes[i].length;     
//      for (int j=0;j<numberofSalesmen;j++){
//        cutPoint[i][j] = ran.nextInt(SalesLength+1);        
//        if (j==numberofSalesmen-1){if(cutPoint[i][1] == cutPoint[i][0]){j--;}}
//      }
//      if (cutPoint[i][0]>cutPoint[i][1]){int temp=cutPoint[i][1];cutPoint[i][1] = cutPoint[i][0]; cutPoint[i][0] = temp;}
//      MomSalse[i] = cutPoint[i][1]-cutPoint[i][0]; //母親那邊 隨機切點 相減 就等於 母親這邊的各推銷員數量
//      //System.out.println(cutPoint[i][0]+" "+cutPoint[i][1]+"  切點");
//    }
//
//    // 由於基因裡面包含0 因此將陣列空值處 更改為-1
//    for (int i=0;i<numberofSalesmen;i++){
//      for (int j=0;j<GeneChild[i].length;j++){
//        if (GeneChild[i][j] ==0){
//          GeneChild[i][j] = -1;
//        }
//      }
//    }
//    
//    //使用上述各個推銷員的隨機切點從Mom基因複製給小孩
//    for (int i=0;i<numberofSalesmen;i++){
//      cutPoint[i][1] -= 1;
//      for (int j=cutPoint[i][0];j<=cutPoint[i][1];j++){
//        GeneChild[i][GeneChildLength[i]]=remainingGenes[i][j];   
//        GeneChildLength[i] ++;                 
//      }
//    }
//
//    ChildLength=0;
//    for (int i=0;i<numberofSalesmen;i++){
//        for (int j=0;j<length;j++){
//          if (GeneChild[i][j] == -1) break;
////          System.out.print(GeneChild[i][j]+" "); 
//          Child[ChildLength] = GeneChild[i][j];ChildLength++;
//        }//System.out.print(" ");
//    }//System.out.println("母親那邊隨機切完且複製給小孩的結果");
//    
//
//    
//    int count=0; //檢查是否為遺失的基因
//    for (int i=0;i<numberofSalesmen;i++){
//      for (int j=0;j<segmentofDad[i].length;j++){       
//        for (int k=0;k<length;k++){
//          if (segmentofDad[i][j] != Child[k]){
//            count++;
////            System.out.print(count+" ");
//          } else {
//            break;
//          }
//        }
//        if (count==length){
////          System.out.println("**"+segmentofDad[i][j]);
//        DadSalse[i] ++; // Dad 那裡的各推銷員數量   
//          for (int k=0;k<length;k++){
//            if (GeneChild[i][k] == -1 ){
//              GeneChild[i][k] = segmentofDad[i][j];
////              System.out.print(GeneChild[i][k]+" ");
//              break;
//            }
////            missGene[missLength] = segmentofDad[i][j];            
//          }
//        }
//        count=0;
//      }
//    }//System.out.println();
//    
//    
//    //將二維陣列內不同推銷員的小孩基因，合併指派給一維的Child內
//    ChildLength=0;
//    for (int i=0;i<numberofSalesmen;i++){
//      for (int j=0;j<GeneChild[i].length;j++){
//        if (GeneChild[i][j] !=-1){
//          Child[ChildLength] = GeneChild[i][j];
////          System.out.print(Child[ChildLength]+" ");
//          ChildLength++;        
//        }
//      }
//    }
//   
//    //將推銷員補上
//    count=0;
//    for (int i=length;i<Mom.length;i++){
//      Child[i] = ListSalse[count] + MomSalse[count] + DadSalse[count];count++;
////      System.out.print(Child[i]+" ");
//    }
//
//    return Child;
//  }

}
