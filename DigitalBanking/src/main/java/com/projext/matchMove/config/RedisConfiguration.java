package com.projext.matchMove.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.integration.redis.util.RedisLockRegistry;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
@Slf4j
public class RedisConfiguration {

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
       //providing default values
        //In production we can get these values from property file

        final String redisHost = "localhost";
        final int redisPort = 6379;
        final String redisPassword = "";

        log.info("Connecting to redis @ redis://{}@{}:{}", redisPassword, redisHost, redisPort);

        JedisConnectionFactory factory = new JedisConnectionFactory();
        factory.setHostName(redisHost);
        factory.setPort(redisPort);
        factory.setTimeout(5000);
        if(redisPassword != null && !redisPassword.isEmpty()) factory.setPassword(redisPassword);
        factory.setUsePool(true);
        factory.setPoolConfig(jedisPoolConfig());
        return factory;
    }

    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(10);
        poolConfig.setMaxIdle(5);
        poolConfig.setMinIdle(1);
        return poolConfig;
    }

    @Bean(name = "redisLockRegistry")
    RedisLockRegistry redisLockRegistry(JedisConnectionFactory jedisConnectionFactory) {
        return new RedisLockRegistry(jedisConnectionFactory, "onlineBanking", 60000l);
    }
}
