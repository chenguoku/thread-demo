package com.hh.threaddemo.forkjoin.recursivetask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

/**
 * @author chenguoku
 * @version 1.0.0
 * @ClassName TaskMain.java
 * @Description RecursiveTask的测试类
 * @createTime 2020年01月15日
 */
public class TaskMain {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        List<Long> list = getDataList();

        // 线程池中的线程数
        ForkJoinPool forkJoinPool = new ForkJoinPool(5);
        // list:需要处理的数据
        TaskForkJoin taskForkJoin = new TaskForkJoin(list);
        // 执行,获取Future
        ForkJoinTask<Long> submitResult = forkJoinPool.submit(taskForkJoin);
        System.out.println("主线程");
        System.out.println(submitResult.get());

        // 等待获取结果
//        Long result = forkJoinPool.invoke(taskForkJoin);
//        System.out.println("主线程");
//        System.out.println(result);
    }

    public static List<Long> getDataList() {
        List<Long> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(Long.parseLong(String.valueOf(i)));
        }

        return list;
    }

}