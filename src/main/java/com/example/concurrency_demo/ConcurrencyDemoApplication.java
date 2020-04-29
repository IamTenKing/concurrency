package com.example.concurrency_demo;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.RedissonRedLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@Slf4j
@MapperScan(basePackages = {"com.example.concurrency_demo.mapper"})
public class ConcurrencyDemoApplication {



    @Bean
    public  RedissonRedLock  lock(){

    RedissonRedLock redLock = new RedissonRedLock(redissonClient().getLock("test1"));
    return redLock;
    }

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
//        config.useClusterServers()
//                .setScanInterval(2000) // 集群状态扫描间隔时间，单位是毫秒
//                .addNodeAddress("redis://127.0.0.1:7000").setPassword("1")
//                        .addNodeAddress("redis://127.0.0.1:7001").setPassword("1")
//                        .addNodeAddress("redis://127.0.0.1:7002")
//                        .setPassword("1");

        config.useSingleServer().setAddress("redis://192.168.2.62:6379");
        return Redisson.create(config);
    }

    public static void main(String[] args) {
        SpringApplication.run(ConcurrencyDemoApplication.class, args);
    }
}
