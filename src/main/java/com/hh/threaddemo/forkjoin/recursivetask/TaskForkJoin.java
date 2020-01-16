package com.hh.threaddemo.forkjoin.recursivetask;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.RecursiveTask;

@Component
@Slf4j
public class TaskForkJoin extends RecursiveTask<Long> {

    /**
     * 阀值
     */
    private static final Integer THRES_HOLD = 30;

    //要处理的数据
    private List records;

    public TaskForkJoin() {
    }

    public TaskForkJoin(List records) {
        this.records = records;
    }

    /**
     * 计算分组
     *
     * @return
     */
    @Override
    protected Long compute() {
        //每组30个
        if (records.size() <= THRES_HOLD) {
            return computeDirectly();
        }
        //如果要处理的数据大于等于30，则进行分组转换
        int size = records.size();
        //第一个分组
        TaskForkJoin aTask = new TaskForkJoin(records.subList(0, size / 2));
        //第二个分组
        TaskForkJoin bTask = new TaskForkJoin(records.subList(size / 2, records.size()));
        //两个任务并发执行起来
        invokeAll(aTask, bTask);

        return aTask.join() + bTask.join();
    }


    /**
     * 真正处理数据的逻辑
     */
    private Long computeDirectly() {
        //针对每组 处理的业务逻辑
        //这里的records的值，就是每组的值
        System.out.println(Thread.currentThread().getName() + " records 的数量" + records.size());
        Long num = 0L;
        for (Object obj : records) {
            long l = Long.parseLong(String.valueOf(obj));
            num += l;
        }
        return num;
    }
}