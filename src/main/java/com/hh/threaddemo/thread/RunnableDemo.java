package com.hh.threaddemo.thread;

/**
 * @author chenguoku
 * @version 1.0.0
 * @ClassName RunnableThread.java
 * @Description 通过实现Runnable接口，实现线程的创建
 * @createTime 2020年01月15日
 */
public class RunnableDemo implements Runnable {
    @Override
    public void run() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + "--Runnable接口。。。。");
    }

    public static void main(String[] args) {
        Thread thread = new Thread(new RunnableDemo());
        thread.setName("Runnable线程");
        thread.start();
        System.out.println("主线程。。。");
    }

//    public static void main(String[] args) {
//        new Thread(() -> {
//            try {
//                Thread.sleep(3000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            System.out.println(Thread.currentThread().getName() + "--Runnable接口。。。。");
//        }).start();
//
//        System.out.println("主线程。。。");
//    }
}