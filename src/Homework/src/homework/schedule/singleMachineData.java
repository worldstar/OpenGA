package homework.schedule;

/**
 * <p>Title: The single machine test instance. Baker(1974)</p>
 * <p>Description: The data is applied at V. Mani, Pei-Chann Chang, Shih Hsin Chen (2006), "Single Machine Scheduling: Genetic Algorithm with Dominance Properties."</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Yuan-Ze University.</p>
 * @author V. Mani, Pei-Chann Chang, Shih Hsin Chen.
 * @email s939506@mail.yzu.edu.tw
 * @version 1.0
 */


public class singleMachineData {
  public singleMachineData() {
  }
  //Numerical test instance of Chang et al. (2003).
  int processingTime[] = new int[]{4, 7, 1, 6, 3};
  int dueDate[] = new int[]{16, 16, 8, 21, 9};
  int pTimeTest1[] = new int[]{6, 4, 7, 5};
  int dueTest1[] = new int[]{9, 10, 8, 6};

  int pTime1[] = new int[]{121, 147, 102, 79, 130, 83, 96, 88};
  int due1[] = new int[]{260, 269, 400, 266, 337, 336, 683, 719};

  int pTime2[] = new int[]{101, 87, 79, 92, 103, 133, 110, 67};
  int due2[] = new int[]{471, 476, 340, 387, 357, 400, 329, 324};

  int pTime3[] = new int[]{84, 123, 102, 91, 122, 114, 98, 50};
  int due3[] = new int[]{122, 587, 645, 474, 341, 321, 129, 598};

  int pTime4[] = new int[]{99, 68, 70, 108, 102, 26, 120, 108};
  int due4[] = new int[]{336, 369, 324, 382, 379, 363, 321, 324};

  int pTime5[] = new int[]{124, 110, 105, 125, 135, 96, 136, 86};
  int due5[] = new int[]{415, 361, 547, 443, 739, 673, 598, 476};

  int pTime6[] = new int[]{89, 64, 105, 124, 64, 100, 107, 78};
  int due6[] = new int[]{408, 359, 362, 467, 394, 479, 328, 442};

  int pTime7[] = new int[]{97, 93, 117, 89, 129, 113, 67, 79};
  int due7[] = new int[]{311, 372, 127, 393, 318, 458, 481, 282};

  int pTime8[] = new int[]{129, 84, 116, 125, 97, 111, 128, 108};
  int due8[] = new int[]{382, 343, 366, 437, 338, 362, 333, 420};

  int pTime9[] = new int[]{42, 115, 111, 96, 117, 93, 95, 98};
  int due9[] = new int[]{260, 283, 616, 622, 452, 624, 447, 749};

  int pTime10[] = new int[]{138, 132, 153, 89, 141, 131, 107, 103};
  int due10[] = new int[]{466, 459, 402, 422, 478, 392, 368, 385};

  int pTime11[] = new int[]{111, 84, 78, 121, 78, 59, 82, 97};
  int due11[] = new int[]{160, 540, 518, 447, 251, 124, 319, 46};

  int pTime12[] = new int[]{69, 71, 82, 106, 84, 104, 88, 69};
  int due12[] = new int[]{352, 396, 353, 393, 328, 383, 387, 337};

  int pTime13[] = new int[]{118, 92, 133, 81, 65, 116, 96, 132};
  int due13[] = new int[]{380, 303, 691, 334, 180, 433, 724, 514};

  int pTime14[] = new int[]{115, 125, 101, 109, 104, 120, 98, 72};
  int due14[] = new int[]{437, 327, 467, 386, 401, 450, 370, 395};

  int pTime15[] = new int[]{116, 94, 80, 103, 57, 91, 115, 104};
  int due15[] = new int[]{548, 31, 67, 510, 427, 42, 322, 622};

  int pTime16[] = new int[]{129, 99, 114, 151, 106, 105, 114, 115};
  int due16[] = new int[]{477, 344, 321, 438, 363, 369, 394, 367};

  int[] getPtime(int i){
    if(i == 0){
      return pTime1;
    }
    else if(i == 1){
      return pTime2;
    }
    else if(i == 2){
      return pTime3;
    }
    else if(i == 3){
      return pTime4;
    }
    else if(i == 4){
      return pTime5;
    }
    else if(i == 5){
      return pTime6;
    }
    else if(i == 6){
      return pTime7;
    }
    else if(i == 7){
      return pTime8;
    }
    else if(i == 8){
      return pTime9;
    }
    else if(i == 9){
      return pTime10;
    }
    else if(i == 10){
      return pTime11;
    }
    else if(i == 11){
      return pTime12;
    }
    else if(i == 12){
      return pTime13;
    }
    else if(i == 13){
      return pTime14;
    }
    else if(i == 14){
      return pTime15;
    }
    else if(i == 15){
      return pTime16;
    }
    else{
      return pTime1;
    }
  }

  int[] getDueDate(int i){
    if(i == 0){
      return due1;
    }
    else if(i == 1){
      return due2;
    }
    else if(i == 2){
      return due3;
    }
    else if(i == 3){
      return due4;
    }
    else if(i == 4){
      return due5;
    }
    else if(i == 5){
      return due6;
    }
    else if(i == 6){
      return due7;
    }
    else if(i == 7){
      return due8;
    }
    else if(i == 8){
      return due9;
    }
    else if(i == 9){
      return due10;
    }
    else if(i == 10){
      return due11;
    }
    else if(i == 11){
      return due12;
    }
    else if(i == 12){
      return due13;
    }
    else if(i == 13){
      return due14;
    }
    else if(i == 14){
      return due15;
    }
    else if(i == 15){
      return due16;
    }
    else{
      return due1;
    }
  }

  public int[] getRandomDue(int size){
    int dueDate[] = new int[size];
    java.util.Random r1 = new java.util.Random(222);
    for(int i = 0 ; i < size ; i ++ ){
      dueDate[i] = r1.nextInt(500);
    }
    return dueDate;
  }

  public int[] getRandomProcessingTime(int size){
    int Ptime[] = new int[size];
    java.util.Random r1 = new java.util.Random(222);
    for(int i = 0 ; i < size ; i ++ ){
      Ptime[i] = r1.nextInt(50);
    }
    return Ptime;
  }

}