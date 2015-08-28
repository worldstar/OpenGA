/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package openga.util;
/**
 *
 * @author nhu
 */
public class threadManagement {
    int numberOfCores = 2; //For multiple-core CPU/GPU. We generate corresponding threads.
    Thread threadQueue[];
    Object runableObjects[];
    int threadCounter = 0;

    public void initManagmentObject(int numberOfCores){        
        this.numberOfCores = numberOfCores;
        threadQueue = new Thread[numberOfCores];        
    }

    public void setData(Thread thread1){        
        threadQueue[threadCounter] = thread1;        
        threadQueue[threadCounter].start();
        threadCounter += 1;

        if(threadCounter == numberOfCores){
            waitForThreads();
            threadCounter = 0;
        }        
    }

    //If the queue is full, we run all threads.
    public void waitForThreads(){         
        //Wait for finishing.
        for(int i = 0 ; i < this.numberOfCores ; i ++){
            try{
                this.threadQueue[i].join();
                break;
            }
            catch(Exception e){
                System.out.println("Thread "+i+" causes a problem.");
            }
        }        
    }

    //When the environment is changed or the batch size is not full whileas the progress is finished soon,
    //we execute the result of threads.
    public void waitForRestOfThreads(){
        //Wait for finishing.
        for(int i = 0 ; i < threadCounter ; i ++){
            try{
                this.threadQueue[i].join();
            }
            catch(Exception e){
                System.out.println("Thread "+i+" causes a problem.");
            }
        }
        threadCounter = 0;
    }
}
