package com.example.concurrency_demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@Slf4j
@MapperScan(basePackages = {"com.example.concurrency_demo.mapper"})
public class ConcurrencyDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConcurrencyDemoApplication.class, args);
    }
}
