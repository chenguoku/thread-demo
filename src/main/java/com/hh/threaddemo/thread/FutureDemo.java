package com.hh.threaddemo.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * @author chenguoku
 * @version 1.0.0
 * @ClassName FutureThread.java
 * @Description 通过实现Callable接口，实现线程创建
 * @createTime 2020年01月15日
 */
public class FutureDemo implements Callable<String> {
    @Override
    public String call() throws Exception {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + "-- Callable接口。。。");
        return "hello world";
    }

    public static void main(String[] args) throws Exception {
        FutureTask futureTask = new FutureTask(new FutureDemo());
        new Thread(futureTask).start();

        System.out.println("主线程");
        // 阻塞获取结果
        System.out.println(futureTask.get());
        System.out.println("主线程继续执行");
    }
}