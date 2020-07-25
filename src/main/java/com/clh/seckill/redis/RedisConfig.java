package com.clh.seckill.redis;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 配置 JedisPool
 *
 * @author Longhua
 * @date: 2020/7/18
 */
@Data
@Configuration
@ConfigurationProperties(prefix="redis")
public class RedisConfig {

    private String host;

    private int port;
    /** 秒 */
    private int timeout;

    private int poolMaxIdle;
    /** 秒 */
    private int poolMaxWait;

    private int poolMaxTotal;

    // private String password;


    @Bean
    public JedisPool JedisPollFactory() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(poolMaxIdle);
        poolConfig.setMaxTotal(poolMaxTotal);
        poolConfig.setMaxWaitMillis(poolMaxWait * 1000);
        JedisPool jedisPool = new JedisPool(
                poolConfig,
                host,
                port,
                timeout * 1000
        );
        return jedisPool;
    }
}
