package com.example.concurrency_demo.controller;

import com.example.concurrency_demo.config.IRedisService;
import com.example.concurrency_demo.distributedLock.Callback;
import com.example.concurrency_demo.distributedLock.redis.RedisReentrantLock;
import com.example.concurrency_demo.distributedLock.zk.ZkDistributedLockTemplate;
import com.example.concurrency_demo.entity.Order;
import com.example.concurrency_demo.mapper.LockTableMapper;
import com.example.concurrency_demo.mapper.OrderMapper;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
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

    @Autowired
    private LockTableMapper lockTableMapper;

    RedisReentrantLock lock =
            new RedisReentrantLock(new JedisPool("192.168.2.62",6379),"test");

    static ZkDistributedLockTemplate zkLock = null;

//    static {
//
//        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
//        CuratorFramework client = CuratorFrameworkFactory.newClient("192.168.145.186:2181", retryPolicy);
//        client.start();
//
//         zkLock=new ZkDistributedLockTemplate(client);
//    }

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



    /**
     *
     *利用db锁实现分布式锁实现锁同步代码块
     * 实现方式：建一个锁表，注意不能是一张空白表，但是不需要非要有lockkey数据，随便有一条数据就行
     * 场景：编号递增
     */
    @GetMapping("/testDBLock1")
    @Transactional
    public void testDBLock1(){

        System.out.println("test");
        //访问锁表，没有就建一张,从这条查询开始加锁，到本事务提交才释放锁
        lockTableMapper.selectLockForUpdate("test");

        /*
        select for update是悲观锁，同一个时间只允许一个事务对同一行数据进行加锁，
        如果已经有其他事务已经加锁了，则当前事务将被阻塞，所以不能采用对业务数据表进行加锁的方案，
        如果非要采用数据库锁的方式，应该另外创建一张锁表，专门用来加锁使用，而不能用业务表，因为会影响用户查询功能
        从select for update开始到commit的这段代码都是当前加锁事务的同步范围
        navicat可以用begin开始事务模拟
         */
        Order order = mapper.selectMax();
        Order order1 = new Order();
        order1.setNum(order==null?1:order.getNum()+1);
        order1.setName("test");
        mapper.insert(order1);

    }

    /**
     *
     * 利用db锁实现分布式锁实现数据幂等性
     * 实现方式：select for update ,没有就insert
     * 场景：接收数据可能存在冗余，保证数据入库幂等性
     *
     */
    @GetMapping("/testDBLock2")
    @Transactional
    public void testDBLock2(){

        System.out.println("test");

        Order order = mapper.selectMax();
        Long i = order == null ? 1 : order.getNum() + 1;

        List<Order> orders = mapper.selectOrderForUpdate(i);
        if(CollectionUtils.isEmpty(orders)){
            Order order1 = new Order();
            order1.setNum(i);
            order1.setName("test");
            mapper.insert(order1);
        }

    }


    /**
     * 利用版本号实现乐观锁
     */
    @GetMapping("/testDBLock3")
    @Transactional
    public void testDBLock3(){

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
