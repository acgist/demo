package com.acgist.thread;

public class SignalThread {

    public static void main(String[] args) throws InterruptedException {
        // 定义线程
        final Thread thread = new Thread(() -> {
            System.out.println("name = " + Thread.currentThread().getName());
        });
        // 设置守护线程：不会阻止程序关闭
        thread.setDaemon(true);
        // 设置名称
        thread.setName("signal thread");
        // 开始执行
        thread.start();
        // 等待thread执行完成
        thread.join();
        // 输出
        System.out.println("执行完成");
    }
    
}
