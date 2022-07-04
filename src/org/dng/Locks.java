package org.dng;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Locks {

    public static volatile Map<String, Boolean> phHungry = new HashMap<>();

    public static void main(String[] args) {

        Fork fork1 = new Fork("fork1");
        Fork fork2 = new Fork("fork2");
        Fork fork3 = new Fork("fork3");
        Fork fork4 = new Fork("fork4");
        Fork fork5 = new Fork("fork5");


        Thread th1 = new Thread(new Philosopher(fork1, fork2), "Ph1");
        th1.start();
        phHungry.put("Ph1", true);
        Thread th2 = new Thread(new Philosopher(fork2, fork3), "Ph2");
        th2.start();
        phHungry.put("Ph2", true);
        Thread th3 = new Thread(new Philosopher(fork3, fork4), "Ph3");
        th3.start();
        phHungry.put("Ph3", true);
        Thread th4 = new Thread(new Philosopher(fork4, fork5), "Ph4");
        th4.start();
        phHungry.put("Ph4", true);
        //Thread th5 = new Thread(new Philosopher(fork5, fork1), "Ph5");
        Thread th5 = new Thread(new Philosopher(fork1, fork5), "Ph5");//change taking of forks order
        th5.start();
        phHungry.put("Ph5", true);


        //lets wait until all philosophers becomes not hungry
        while (phHungry.get("Ph1")||phHungry.get("Ph2")||phHungry.get("Ph3")||phHungry.get("Ph4")||phHungry.get("Ph5")){
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        th1.interrupt();
        th2.interrupt();
        th3.interrupt();
        th4.interrupt();
        th5.interrupt();
        try {
            th1.join();
            th2.join();
            th3.join();
            th4.join();
            th5.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("all phs has ate");

    }
}


class Philosopher implements Runnable{
    private final Fork forkLeft;
    private final Fork forkRight;

    public Philosopher(Fork forkLeft, Fork forkRight) {
        this.forkLeft = forkLeft;
        this.forkRight = forkRight;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            System.out.println(Thread.currentThread().getName()+" try to take fork");
            boolean forkLeftLocked = forkLeft.lock.tryLock();
            boolean forkRightLocked = forkRight.lock.tryLock();

            if (forkLeftLocked && forkRightLocked) {
                try {
                    System.out.println(Thread.currentThread().getName() + " took the forks");
                    System.out.println(Thread.currentThread().getName() + " eating....");
                    Locks.phHungry.put(Thread.currentThread().getName(), false);

                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        //e.printStackTrace();
                        Thread.currentThread().interrupt();
                    }
                    System.out.println(Thread.currentThread().getName() + " put the forks");
                } finally {
                        forkLeft.lock.unlock();
                        forkRight.lock.unlock();
                }
            } else {
                if (forkLeftLocked){
                    forkLeft.lock.unlock();
                }
                if (forkRightLocked){
                    forkRight.lock.unlock();
                }
                System.out.println(Thread.currentThread().getName() + " Forks is busy, go thinking ;)");
            }

            System.out.println(Thread.currentThread().getName() + " thinking....");
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

        }
    }
}


class Fork {
    //private volatile boolean busy;
    public final Lock lock = new ReentrantLock();
    //private final Condition condition = lock.newCondition();
    private final String name;

    public Fork(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Fork{ " + name + '}';
    }
}


