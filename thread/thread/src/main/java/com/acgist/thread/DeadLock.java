package com.acgist.thread;

public class DeadLock {
    
    static Object lockA = new Object();
    static Object lockB = new Object();

    public static void main(String[] args) throws InterruptedException {
        final Thread threadA = new Thread(new Runnable() {
            public void run() {
                synchronized (lockA) {
                    System.out.println("threadA lock A");
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        // 
                    }
                    System.out.println("threadA wait lock B");
                    synchronized (lockB) {
                        System.out.println("threadA lock B");
                    }
                }
            }
        });
        final Thread threadB = new Thread(new Runnable() {
            public void run() {
                synchronized (lockB) {
                    System.out.println("threadB lock B");
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        // 
                    }
                    System.out.println("threadB wait lock A");
                    synchronized (lockA) {
                        System.out.println("threadB lock A");
                    }
                }
            }
        });
        threadA.start();
        threadB.start();
        threadA.join();
        threadB.join();
    }

}
