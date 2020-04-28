package com.example.concurrency_demo.controller;

import com.example.concurrency_demo.config.IRedisService;
import com.example.concurrency_demo.distributedLock.Callback;
import com.example.concurrency_demo.distributedLock.redis.RedisReentrantLock;
import com.example.concurrency_demo.distributedLock.zk.ZkDistributedLockTemplate;
import com.example.concurrency_demo.entity.Order;
import com.example.concurrency_demo.mapper.OrderMapper;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author jt
 * @date 2020-4-27
 */

@RestController
public class LockController {


    @Autowired
    IRedisService redisService;

    RedisReentrantLock lock =
            new RedisReentrantLock(new JedisPool("192.168.2.62",6379),"test");

    static ZkDistributedLockTemplate zkLock = null;

    static {

        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient("192.168.145.186:2181", retryPolicy);
        client.start();

         zkLock=new ZkDistributedLockTemplate(client);
    }

    ReentrantLock reentrantLock =  new ReentrantLock();

    //
//    new RedisReentrantLock(new JedisPool("192.168.2.45",6379),"test");


    @Autowired
    private OrderMapper mapper;

    @GetMapping("/testRedisLock")
    public void testRedisLock(){
        System.out.println("=====================");
        try {
            //超时时间设置非常重要，如果设置的小了，并发时很多任务会因超时失败
            //实现锁的可重入，任务自旋
            if(lock.tryLock(100, TimeUnit.SECONDS)){

                List<Order> orders = mapper.selectAll();
                Optional<Order> max = orders.stream().max(new Comparator<Order>() {
                    @Override
                    public int compare(Order o1, Order o2) {
                        if (o1.getNum() < o2.getNum()) {
                            return -1;
                        }
                        return 1;
                    }
                });
                Long num = max.get().getNum();
                Order order = new Order();
                order.setName("test");
                order.setNum(++num);
                mapper.insert(order);

            }else {
                System.out.println("锁超时了。。。。。。。。。。");
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }


    }


    @GetMapping("/testRedisLock1")
    public void testRedisLock1(){
        List<Order> orders = mapper.selectAll();
        for (Order order : orders) {
            System.out.println(order.getNum());
        }
    }

    @GetMapping("/testRL")
    public void reentrantLock(){
        reentrantLock.lock();
    }



    @GetMapping("/testZk")
    public void testZk(){
        System.out.println("测试zk锁。。。。。。。。。。。。。");
        zkLock.execute("test", 100000, new Callback() {
            @Override
            public Object onGetLock() throws InterruptedException {
                List<Order> orders = mapper.selectAll();
                Optional<Order> max = orders.stream().max(new Comparator<Order>() {
                    @Override
                    public int compare(Order o1, Order o2) {
                        if (o1.getNum() < o2.getNum()) {
                            return -1;
                        }
                        return 1;
                    }
                });
                Long num = max.get().getNum();
                Order order = new Order();
                order.setName("test");
                order.setNum(++num);
                mapper.insert(order);
                return null;

            }

            @Override
            public Object onTimeout() throws InterruptedException {
                System.out.println("超时了。。。。。。。。。。。");
                return null;
            }
        });

    }
}
