package com.acgist.thread;

public class Synchronized {

    static int a = 0;
    static int b = 0;
    static Object lock = new Object();
    
    public static void main(String[] args) throws InterruptedException {
        final Thread threadA = new Thread(() -> {
            for(int i = 0; i < 1000; i++) {
                ++a;
                synchronized (lock) {
                    ++b;
                }
            }
        });
        final Thread threadB = new Thread(() -> {
            for(int i = 0; i < 1000; i++) {
                ++a;
                synchronized (lock) {
                    ++b;
                }
            }
        });
        threadA.start();
        threadB.start();
        threadA.join();
        threadB.join();
        // 可能错误输出
        System.out.println("a = " + a);
        // 正确输出
        System.out.println("b = " + b);
    }
    
    public void lockClazz() {
        // 所有线程同步执行
        synchronized (Synchronized.class) {
        }
    }
    
    public void lockObject() {
        // 所有线程相同对象同步执行
        synchronized (this) {
        }
    }
    
    public synchronized void lockObjectMethod() {
        // 所有线程相同对象同步执行
    }
    
    public synchronized static void lockStaticMethod() {
        // 所有线程同步执行
    }
    
}
