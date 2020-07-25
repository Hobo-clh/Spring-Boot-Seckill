package com.clh.seckill;

import com.clh.seckill.service.RedisService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;

@SpringBootTest
class SpringBootSeckillApplicationTests {

    @Resource
    RedisService redisService;

    @Test
    void contextLoads() {
    }

}
