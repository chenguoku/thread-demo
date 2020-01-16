package com.hh.threaddemo.thread;

/**
 * @author chenguoku
 * @version 1.0.0
 * @ClassName ThreadClassThread.java
 * @Description 通过继承Thread类，实现线程的创建
 * @createTime 2020年01月15日
 */
public class ThreadClassDemo extends Thread {
    @Override
    public void run() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + "-- Thread类。。。。");
    }

    public static void main(String[] args) {
        Thread thread = new Thread(new ThreadClassDemo());
        thread.setName("Thread Class线程");
        thread.start();
        System.out.println("主线程。。。");
    }
}