package org.dng;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Locks {

    public static void main(String[] args) {
        Fork fork1 = new Fork();
        //final Lock lock = new ReentrantLock();


        new Thread(new Runnable(){
            @Override
            public void run() {
                while (true){
                    //System.out.println(Thread.currentThread().getName()+" try to take");
                    if (fork1.take()) {
                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        //System.out.println(Thread.currentThread().getName()+"try put");
                        fork1.put();

                        System.out.println(Thread.currentThread().getName()+" thinking....");
                        try {
                            Thread.sleep(12000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                    else
                    {
                        System.out.println(Thread.currentThread().getName()+" Fork is busy, waiting...");
                        try {
                            Thread.sleep(7000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        },"Ph1").start();

        new Thread(new Runnable(){
            @Override
            public void run() {
                while (true){
                    //System.out.println(Thread.currentThread().getName()+" try to take");
                    if (fork1.take()) {
                        try {
                            Thread.sleep(9000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        //System.out.println(Thread.currentThread().getName()+"try put");
                        fork1.put();

                        System.out.println(Thread.currentThread().getName()+" thinking....");
                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                    else
                    {
                        System.out.println(Thread.currentThread().getName()+" Fork is busy, waiting...");
                        try {
                            Thread.sleep(6000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        },"Ph2").start();

    }
}

class Fork{
    private boolean busy;
    private final Lock lock = new ReentrantLock();

    public boolean take(){
        System.out.println(Thread.currentThread().getName()+" try take the fork");
        if (lock.tryLock()){
            busy = true;
            System.out.println(Thread.currentThread().getName() + " took the fork");
            return true;
        }
        else{
            //System.out.println("Fork is busy");
            busy = false;
            return false;
        }
    }

    public boolean put(){
        busy = false;
        System.out.println(Thread.currentThread().getName() + " put the fork");
        lock.unlock();
        return false;
    }
}


