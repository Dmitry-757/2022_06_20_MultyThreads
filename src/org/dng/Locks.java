package org.dng;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Locks {

    public static void main(String[] args) {
        Fork fork1 = new Fork();
        //final Lock lock = new ReentrantLock();


        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    System.out.println(Thread.currentThread().getName()+" try to take fork");
                    if (fork1.lock.tryLock()) {
                        try {
                            System.out.println(Thread.currentThread().getName() + " took the fork");
                            System.out.println(Thread.currentThread().getName() + " eating....");
                            try {
                                Thread.sleep(10000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            System.out.println(Thread.currentThread().getName() + " put the fork");
                        } finally {
                            fork1.lock.unlock();
                        }
                    } else {
                        System.out.println(Thread.currentThread().getName() + " Fork is busy, waiting...");
//                        try {
//                            Thread.sleep(4000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }

                    }

                    System.out.println(Thread.currentThread().getName() + " thinking....");
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }, "Ph1").start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    System.out.println(Thread.currentThread().getName()+" try to take fork");
                    if (fork1.lock.tryLock()) {
                        try {
                            System.out.println(Thread.currentThread().getName() + " took the fork");
                            System.out.println(Thread.currentThread().getName() + " eating....");
                            try {
                                Thread.sleep(10000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            System.out.println(Thread.currentThread().getName() + " put the fork");
                        } finally {
                            fork1.lock.unlock();
                        }
                    } else {
                        System.out.println(Thread.currentThread().getName() + " Fork is busy, waiting...");
//                        try {
//                            Thread.sleep(4000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }

                    }

                    System.out.println(Thread.currentThread().getName() + " thinking....");
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }, "Ph2").start();

    }
}

class Fork {
    private volatile boolean busy;
    public final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();

    public boolean take() {
        System.out.println(Thread.currentThread().getName() + " try take the fork");
        if (lock.tryLock()) {
            busy = true;
            System.out.println(Thread.currentThread().getName() + " took the fork");
            System.out.println(Thread.currentThread().getName() + " eating...");
            return true;
        } else {
            //System.out.println("Fork is busy");
            busy = false;
//            try {
//                condition.await();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            return false;
        }
//        return busy;
    }

    public void put() {
        busy = false;
        System.out.println(Thread.currentThread().getName() + " put the fork");
//        condition.signalAll();
        lock.unlock();
    }
}


