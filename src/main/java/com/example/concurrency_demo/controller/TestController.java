package com.example.concurrency_demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author: jt
 * @date: 2018-11-24
 */

@RestController
@Slf4j
public class TestController {

    int count =0;

    public String test() {

        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 100; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    count++;
                    System.out.println("逗你玩:"+count);
                }
            });
        }
        executorService.shutdown();

        return "hellooooooooo";
    }



}
