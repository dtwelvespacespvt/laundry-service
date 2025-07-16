package com.stanzaliving.laundry.config;

import com.stanzaliving.laundry.constant.Constants;
import lombok.extern.log4j.Log4j2;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

    @Value("${redis.url}")
    private String redisUrl;

    @Value("${redis.port:6379}")
    private String redisPort;

    @Value("${redis.database:0}")
    private int redisDatabase;

    @Value("${redis.connection.pool.size:20}")
    private int redisConnectionPool;

    @Value("${redis.connection.pool.size.min:2}")
    private int redisMinConnectionPool;

    @Value("${redis.connection.timeout:5000}")
    private int redisConnectionTimeout;

    @Value("${redis.idle.connection.timeout:120000}")
    private int redisIdleConnectionTimeout;

    @Bean
    public RedissonClient redissonClient() {
        String address = "redis://" + redisUrl + ":" + redisPort;
        Config config = new Config();
        config.useSingleServer()
                .setAddress(address)
                .setDatabase(redisDatabase)
                .setConnectionPoolSize(redisConnectionPool)
                .setConnectionMinimumIdleSize(redisMinConnectionPool)
                .setConnectTimeout(redisConnectionTimeout)
                .setIdleConnectionTimeout(redisIdleConnectionTimeout);
        return Redisson.create(config);
    }
    @Primary
    @Bean("postman")
    CacheManager cacheManager(RedissonClient redissonClient) {
        Map<String, CacheConfig> cacheConfigMap = new HashMap<>();

        cacheConfigMap.put(Constants.COLLECTION, new CacheConfig(5 * 60 * 60 * 1000, 5 * 60 * 60 * 1000));

        RedissonSpringCacheManager redissonSpringCacheManager = new RedissonSpringCacheManager(redissonClient, cacheConfigMap);
        redissonSpringCacheManager.setCacheNames(cacheConfigMap.keySet());
        return redissonSpringCacheManager;
    }
}
