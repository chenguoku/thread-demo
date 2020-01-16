package com.hh.threaddemo.config;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AsyncServiceImpl implements AsyncService {

    @Override
    @Async("asyncServiceExecutor")
    public void executeAsync(ExecutorFunctional target) {
        try {
            target.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}