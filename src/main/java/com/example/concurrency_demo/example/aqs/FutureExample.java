package com.example.concurrency_demo.example.aqs;

import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.util.concurrent.*;

/**
 * @author: jt
 * @date: 2018-12-2
 */

@Slf4j
public class FutureExample {



    //callable 可以有返回，runable 没有返回值，使用callable+futuretask可以获取线程任务执行的返回值
    static class MyCallable implements Callable<String>{

        @Override
        public String call() throws Exception {
            log.info("do something in callable");
            Thread.sleep(5000);
            return "done";
        }
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        Future<String> future = executorService.submit(new MyCallable());
        log.info("do something in main");
        Thread.sleep(1000);
        String result = future.get();
        log.info("result:{}",result);

    }
}
