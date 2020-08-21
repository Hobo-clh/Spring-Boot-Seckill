package com.clh.seckill;

import com.clh.seckill.service.RedisService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

import javax.annotation.Resource;

/**
 * @author: LongHua
 * @date: 2020/8/21
 **/
@SpringBootTest(classes = SpringBootSeckillApplication.class)
public class RedisTest {

    @Resource
    private RedisService redisService;
    @Resource
    private JedisSentinelPool jedisPool;


    @Test
    public void test(){
        Jedis resource = jedisPool.getResource();
        System.out.println(resource.ping());
        System.out.println(resource.get("k1212"));

        resource.set("nb", "1234567");
    }
}
