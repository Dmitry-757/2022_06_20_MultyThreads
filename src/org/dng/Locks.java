package org.dng;

//import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Locks {

    public static void main(String[] args) {
        Fork forkLeft = new Fork("Left");
        Fork forkRight = new Fork("Right");
        //final Lock lock = new ReentrantLock();


        new Thread(new Philosopher(forkLeft, forkRight), "Ph1").start();
        new Thread(new Philosopher(forkLeft, forkRight), "Ph2").start();

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
        while (true) {
            System.out.println(Thread.currentThread().getName()+" try to take fork");
            boolean forkLeftLocked = forkLeft.lock.tryLock();
            boolean forkRightLocked = forkRight.lock.tryLock();

            if (forkLeftLocked && forkRightLocked) {
                try {
                    System.out.println(Thread.currentThread().getName() + " took the forks");
                    System.out.println(Thread.currentThread().getName() + " eating....");
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
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


