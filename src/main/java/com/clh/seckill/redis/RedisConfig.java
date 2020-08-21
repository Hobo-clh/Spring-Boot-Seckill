package com.clh.seckill.redis;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

import java.util.HashSet;
import java.util.Set;

/**
 * 配置 JedisPool
 *
 * @author Longhua
 * @date: 2020/7/18
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "redis")
public class RedisConfig {

    private String host;

    private int port;
    /**
     * 秒
     */
    private int timeout;

    private int poolMaxIdle;
    /**
     * 秒
     */
    private int poolMaxWait;

    private int poolMaxTotal;

    /**
     * 哨兵的host:port
     */
    private String sentinel;

    // private String password;


    @Bean
    public JedisSentinelPool JedisPollFactory() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(poolMaxIdle);
        poolConfig.setMaxTotal(poolMaxTotal);
        poolConfig.setMaxWaitMillis(poolMaxWait * 1000);
        // JedisPool jedisPool = new JedisPool(
        //         poolConfig,
        //         host,
        //         port,
        //         timeout * 1000
        // );
        Set<String> sentinels = new HashSet<>();
        sentinels.add(sentinel);
        System.out.println(sentinel);
        JedisSentinelPool pool = new JedisSentinelPool("myredis", sentinels, poolConfig, timeout * 1000);
        return pool;
    }
}
