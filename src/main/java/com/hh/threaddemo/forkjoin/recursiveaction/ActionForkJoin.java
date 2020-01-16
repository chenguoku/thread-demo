package com.hh.threaddemo.forkjoin.recursiveaction;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.RecursiveAction;

@Component
@Slf4j
public class ActionForkJoin extends RecursiveAction {

    /**
     * 阀值,每组的大小
     */
    private static final Integer THRES_HOLD = 30;

    //要处理的数据
    private List records;

    public ActionForkJoin() {
    }

    public ActionForkJoin(List records) {
        this.records = records;
    }

    /**
     * 计算分组
     *
     * @return
     */
    @Override
    protected void compute() {
        //每组30个
        if (records.size() <= THRES_HOLD) {
            computeDirectly();
            return;
        }
        //如果要处理的数据大于等于30，则进行分组转换
        int size = records.size();
        //第一个分组
        ActionForkJoin aTask = new ActionForkJoin(records.subList(0, size / 2));
        //第二个分组
        ActionForkJoin bTask = new ActionForkJoin(records.subList(size / 2, records.size()));
        //两个任务并发执行起来
        invokeAll(aTask, bTask);

    }

    /**
     * 真正处理数据的逻辑
     */
    private void computeDirectly() {
        //针对每组 处理的业务逻辑
        //这里的records的值，就是每组的值
        System.out.println(Thread.currentThread().getName() + " 开始 " + records.size());
        System.out.println(Thread.currentThread().getName() + " " + records.toString());
        System.out.println(Thread.currentThread().getName() + "结束");
    }
}