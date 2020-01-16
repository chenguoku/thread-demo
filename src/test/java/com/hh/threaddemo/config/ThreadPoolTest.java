package com.hh.threaddemo.config;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;

/**
 * @author chenguoku
 * @version 1.0.0
 * @ClassName ThreadPoolTest.java
 * @Description SpringBoot配置线程池测试类
 * @createTime 2020年01月15日
 */
@SpringBootTest
@Slf4j
public class ThreadPoolTest {

    @Autowired
    private AsyncService asyncService;

    @Test
    public void test() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(3);

        for (int i = 0; i < 3; i++) {
            asyncService.executeAsync(() -> {
                int random = (int) (2 + Math.random() * 8);
                log.info(Thread.currentThread().getName() + "睡{}秒", random);
                Thread.sleep(random * 1000);
                latch.countDown();
                log.info(Thread.currentThread().getName() + "睡{}秒,执行完毕", random);
            });
        }

        log.info("主线程执行完毕");

        latch.await();

    }

}