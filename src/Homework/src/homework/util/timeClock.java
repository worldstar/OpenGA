package homework.util;

/**
 * <p>Title: The OpenGA project which is to build general framework of Genetic algorithm.</p>
 * <p>Description: To record the starting time and ending time, so that we can know how long the program runs.</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: Yuan-Ze University</p>
 * @author Chen, Shih-HSin
 * @version 1.0
 */

public class timeClock {
  long start, end;

  public void start(){
    start = System.currentTimeMillis();
  }

  public void end(){
    end = System.currentTimeMillis();
  }

  public long getExecutionTime(){
    return (end - start);
  }

  public static void main(String[] args) {
  }
}
