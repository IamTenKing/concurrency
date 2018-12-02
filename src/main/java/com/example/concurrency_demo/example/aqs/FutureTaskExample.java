package com.example.concurrency_demo.example.aqs;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @author: jt
 * @date: 2018-12-2
 */
@Slf4j
public class FutureTaskExample {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        FutureTask<String>  futureTask = new FutureTask<String>(new Callable<String>() {
            @Override
            public String call() throws Exception {

                log.info("do something in callable");
                Thread.sleep(11000);
                return "done";

            }
        });

        new Thread(futureTask).start();
        log.info("do something in main");
        //此处等待与futuretask任务一起等待
        Thread.sleep(10000);
        String result = futureTask.get();
        log.info("result {}",result);


    }
}
