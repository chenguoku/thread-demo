package com.hh.threaddemo.forkjoin.recursiveaction;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

/**
 * @author chenguoku
 * @version 1.0.0
 * @ClassName ActionMain.java
 * @Description RecursiveAction的测试类
 * @createTime 2020年01月15日
 */
public class ActionMain {

    public static void main(String[] args) throws InterruptedException {

        List<String> dataList = getDataList();

        // 线程池中的线程数
        ForkJoinPool forkJoinPool = new ForkJoinPool(3);
        // dataList:需要处理的数据
        ActionForkJoin actionForkJoin = new ActionForkJoin(dataList);
        forkJoinPool.execute(actionForkJoin);

        //阻塞当前线程直到 ForkJoinPool 中所有的任务都执行结束
//        forkJoinPool.awaitTermination(100, TimeUnit.SECONDS);
        forkJoinPool.awaitQuiescence(100, TimeUnit.SECONDS);
    }

    public static List<String> getDataList() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(String.valueOf(i));
        }

        return list;
    }

}