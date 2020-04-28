package com.example.concurrency_demo.lock;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jt
 * @date 2020-4-26
 */

@RestController
public class SychronizeDemo1 {


    private int i;

    @GetMapping("/test2")
    public void test2(){
        i++;
        System.out.println(i);

    }


    @GetMapping("/test1")
    public synchronized void test1(){
        i++;
        System.out.println(i);

    }


    /**
     * 静态方法和非静态方法的区别，非静态方法是对象锁，静态方法是类锁
     */
    @GetMapping("/test7")
    public synchronized void test7(){
        i++;
        System.out.println(i);

    }


    /**
     * 不存在并发问题
     */
    @GetMapping("/test3")
    public  void test3(){

        synchronized(this){
            i++;
            System.out.println(i);
        }


    }


    /**
     * 存在并发问题
     */
    @GetMapping("/test4")
    public  void test4(){

        Object o = new Object();

        synchronized(o){
            i++;
            System.out.println(i);
        }
    }

    private  Object o1 = new Object();


    /**
     * 不存在并发问题，不同线程使用的是同一把锁
     */
    @GetMapping("/test5")
    public  void test5(){


        synchronized(o1){
            i++;
            System.out.println(i);
        }
    }


    /**
     * class类作为锁，也可以保证安全行
     */
    @GetMapping("/test6")
    public  void test6(){


        synchronized(SychronizeDemo1.class){
            i++;
            System.out.println(i);
        }
    }
}
