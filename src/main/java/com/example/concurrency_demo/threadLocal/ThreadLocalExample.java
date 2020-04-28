package com.example.concurrency_demo.threadLocal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadLocalExample {


    private static ThreadLocal<Integer> count = new ThreadLocal<>();
    static int count1=0;


    public static void main(String[] args) {

        ExecutorService executorService = Executors.newCachedThreadPool();

        for (int i = 0; i < 30; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    count.set(count1++);
                    System.out.println(count.get());
//                    System.out.println(count1++);

                }
            });

        }
        executorService.shutdown();
        System.out.println("总数："+count1);

    }




}
