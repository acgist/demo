package com.acgist.thread;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLock {

    static int b = 0;
    
    public static void main(String[] args) throws InterruptedException {
        final java.util.concurrent.locks.ReadWriteLock lock = new ReentrantReadWriteLock();
        final Lock readLock  = lock.readLock();
        final Lock writeLock = lock.writeLock();
        final Thread threadA = new Thread(() -> {
            for(int i = 0; i < 1000; i++) {
                try {
                    writeLock.lock();
                    b = b + 1;
                } finally {
                    writeLock.unlock();
                }
            }
        });
        final Thread threadB = new Thread(() -> {
            for(int i = 0; i < 1000; i++) {
                try {
                    writeLock.lock();
                    b = b + 1;
                } finally {
                    writeLock.unlock();
                }
            }
        });
        final Thread threadC = new Thread(() -> {
            for(int i = 0; i < 1000; i++) {
                try {
                    readLock.lock();
                    int a = b;
                } finally {
                    readLock.unlock();
                }
            }
        });
        threadA.start();
        threadB.start();
        threadC.start();
        threadA.join();
        threadB.join();
        threadC.join();
        System.out.println(b);
    }
    
}
