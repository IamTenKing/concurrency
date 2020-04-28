package com.example.concurrency_demo.config;

import redis.clients.jedis.BinaryClient;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author jt
 * @date 2020-4-27
 */
public interface IRedisService {

    String get(String key);


    String set(String key, String value, int expire);


    Long del(String... keys);


}
