package com.example.concurrency_demo.executors;

import lombok.extern.slf4j.Slf4j;
import sun.rmi.runtime.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class CachedThreadPoolExample {
    static AtomicInteger count =new AtomicInteger();
    static int count1 =0;
    public static void main(String[] args) {

        ExecutorService executorService = Executors.newCachedThreadPool();

        for (int i = 0; i < 20; i++) {
                executorService.execute(new Runnable() {
                    @Override
                    public  void run() {
    //                    count.getAndIncrement();
    //                    System.out.println(count);

                    //使用基本数据类型 int
//                    count1++;
//                    System.out.println("int:"+count1);

                    //使用原子数据类型
//                    count.getAndIncrement();
//                    System.out.println("atomicInterger:"+count);

                    //使用对象加锁，不安全
                    synchronized (this){
                        count1++;
                        System.out.println("int:"+count1);

                    }

                    //加类锁，安全
//                    synchronized (this.getClass()){
//                        count1++;
//                        System.out.println("int:"+count1);
//
//                    }
                    //原子数据类型+对象锁
//                    synchronized (this){
//                        count1++;
//                        System.out.println("int:"+count1);
//
//                    }
                    //加类锁
//                    synchronized (this.getClass()){
//                        count.getAndIncrement();
//                        System.out.println(count);
//                    }

                }
            });
        }

        //关闭线程池
        executorService.shutdown();
    }
}
