package com.acgist.thread;

public class ThreadLocal {

    @SuppressWarnings({ "unchecked", "rawtypes" })
    final static java.lang.ThreadLocal<String> LOCAL = new java.lang.ThreadLocal() {
        protected Object initialValue() {
            return Thread.currentThread().getName();
        };
    };
    
    public static void main(String[] args) throws InterruptedException {
        final Thread threadA = new Thread(() -> {
            System.out.println(LOCAL.get());
            LOCAL.remove();
        });
        final Thread threadB = new Thread(() -> {
            System.out.println(LOCAL.get());
            LOCAL.remove();
        });
        System.out.println(LOCAL.get());
        LOCAL.remove();
        threadA.start();
        threadB.start();
        threadA.join();
        threadB.join();
    }
    
}
