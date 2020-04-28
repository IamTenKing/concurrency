package com.example.concurrency_demo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.*;

/**
 * @author jt
 * @date 2020-4-27
 */
@Service
public class RedisServiceImpl implements IRedisService {
    private static final Logger log = LoggerFactory.getLogger(RedisServiceImpl.class);
    @Autowired
    private JedisPool pool;

    public RedisServiceImpl() {
    }


    @Override
    public String get(String key) {
        Jedis jedis = null;
        String value = null;

        try {
            jedis = this.pool.getResource();
            value = jedis.get(key);
        } catch (Exception var8) {
            log.warn(var8.getMessage());
        } finally {
            returnResource(this.pool, jedis);
        }

        return value;
    }

    @Override
    public String set(String key, String value, int expire) {
        Jedis jedis = null;

        String result;
        try {
            jedis = this.pool.getResource();
            int time = jedis.ttl(key).intValue() + expire;
            result = jedis.set(key, value);
            jedis.expire(key, time);
            String var7 = result;
            return var7;
        } catch (Exception var11) {
            log.warn(var11.getMessage());
            result = "0";
        } finally {
            returnResource(this.pool, jedis);
        }

        return result;
    }


    private static void returnResource(JedisPool pool, Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }

    }

    @Override
    public Long del(String... keys) {
        Jedis jedis = null;

        Long var4;
        try {
            jedis = this.pool.getResource();
            Long var3 = jedis.del(keys);
            return var3;
        } catch (Exception var8) {
            log.warn(var8.getMessage());
            var4 = 0L;
        } finally {
            returnResource(this.pool, jedis);
        }

        return var4;
    }

}