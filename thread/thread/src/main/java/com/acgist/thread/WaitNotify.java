package com.acgist.thread;

public class WaitNotify {

    public static void main(String[] args) throws InterruptedException {
        final Object object = new Object();
        final Thread threadA = new Thread(() -> {
            while(true) {
                synchronized (object) {
                    object.notifyAll();
                    try {
                        object.wait();
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("a".repeat(10));
                }
            }
        });
        final Thread threadB = new Thread(() -> {
            while(true) {
                synchronized (object) {
                    object.notifyAll();
                    try {
                        object.wait();
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("b".repeat(10));
                }
            }
        });
        threadA.start();
        threadB.start();
        threadA.join();
        threadB.join();
    }
    
}
