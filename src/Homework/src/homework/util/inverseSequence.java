package homework.util;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class inverseSequence {
  public inverseSequence() {
  }

  public int[] startInverse(int seq[]){
      int length = seq.length;
      int backupGenes[] = new int[length];
      int counter = 0;

      //store the genes at backupGenes.
      for(int i = 0 ; i < length ; i ++ ){
        backupGenes[counter++] = seq[i];
      }

      counter = 0;
      //write data of backupGenes into the genes
      for(int i = length - 1 ; i > -1 ; i --){
        seq[i] = backupGenes[counter ++];
      }
      return seq;
  }
}