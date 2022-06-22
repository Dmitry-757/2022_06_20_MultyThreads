package org.dng;

import java.util.Random;
import java.util.concurrent.Semaphore;
//import java.util.logging.Level;
//import java.util.logging.Logger;

public class Main {

    public static void main(String[] args) {
	// write your code here
        Runnable task = new Runnable() {
            Semaphore sem = new Semaphore(5);
            Random r = new Random();
            int counter = r.nextInt(2000)+500;
            @Override
            public void run() {
                try {
                    sem.acquire();
                    System.out.println(Thread.currentThread().
                            getName()+ " is working... "+this.counter);
                    Thread.currentThread().sleep(counter);
                    System.out.println(Thread.currentThread().
                            getName()+ " is finished!");
                    sem.release();
                } catch (InterruptedException ex) {
                    System.out.println(ex.getMessage());
//                    Logger.getLogger(MySemaphore.class.
//                            getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        for(int i=0;i<20; i++){
            new Thread(task).start();
        }

    }
}
