package com.hh.threaddemo.ThreadPool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author chenguoku
 * @version 1.0.0
 * @ClassName ThreadPoolDemo.java
 * @Description 创建线程池的Demo
 * @createTime 2020年01月15日
 */
public class ThreadPoolDemo {

//    public static void main(String[] args) {
//        ExecutorService executorService = Executors.newFixedThreadPool(3);
//
//        for (int i = 0; i < 10; i++) {
//            executorService.execute(() -> {
//                System.out.println(Thread.currentThread().getName());
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            });
//        }
//
//        System.out.println("主线程");
//    }

//    public static void main(String[] args) {
//        ExecutorService executorService = Executors.newSingleThreadExecutor();
//        for (int i = 0; i < 10; i++) {
//            executorService.execute(() -> {
//                System.out.println(Thread.currentThread().getName());
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            });
//        }
//
//        System.out.println("主线程");
//    }

//    public static void main(String[] args) {
//        ExecutorService executorService = Executors.newCachedThreadPool();
//
//        for (int i = 0; i < 10; i++) {
//            executorService.execute(() -> {
//                System.out.println(Thread.currentThread().getName());
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            });
//        }
//
//        System.out.println("主线程");
//    }

//    public static void main(String[] args) {
//        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);
//
//        for (int i = 0; i < 10; i++){
//            scheduledExecutorService.schedule(() -> {
//                System.out.println(Thread.currentThread().getName());
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            },3, TimeUnit.SECONDS);
//        }
//
//        System.out.println("主线程");
//    }

//    public static void main(String[] args) {
//        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 5,
//                0, TimeUnit.MILLISECONDS,
//                new ArrayBlockingQueue<>(5, true),
//                Executors.defaultThreadFactory(),
//                new ThreadPoolExecutor.AbortPolicy());
//
//        for (int i = 0; i < 10; i++) {
//            threadPoolExecutor.execute(() -> {
//                System.out.println(Thread.currentThread().getName());
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            });
//        }
//
//        System.out.println("主线程");
//    }

}