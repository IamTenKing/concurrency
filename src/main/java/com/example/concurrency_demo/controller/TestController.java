package com.example.concurrency_demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: jt
 * @date: 2018-11-24
 */

@RestController
@Slf4j
public class TestController {

    @GetMapping("/test")
    public String test() {
        System.out.println("sdfsdf");
        return "test";
    }


}
