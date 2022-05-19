package com.pongsky.kit.cache.redis.config;

import com.pongsky.kit.cache.redis.properties.DistributedLockProperties;
import com.pongsky.kit.cache.redis.properties.RedisCacheProperties;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.integration.redis.util.RedisLockRegistry;

import java.text.MessageFormat;

/**
 * Redis 分布式锁配置
 *
 * @author pengsenhao
 */
@RequiredArgsConstructor
public class DistributedLockConfig {

    private final RedisCacheProperties cacheProperties;
    private final DistributedLockProperties lockProperties;

    /**
     * 配置分布式锁实现
     *
     * @param redisConnectionFactory redisConnectionFactory
     * @return 分布式锁实现
     */
    @Bean
    public RedisLockRegistry redisLockRegistry(RedisConnectionFactory redisConnectionFactory) {
        String registryKey;
        if (StringUtils.isNotBlank(cacheProperties.getPrefix()) && StringUtils.isNotBlank(lockProperties.getPrefix())) {
            registryKey = MessageFormat.format("{0}:{1}", cacheProperties.getPrefix(), lockProperties.getPrefix());
        } else if (StringUtils.isNotBlank(cacheProperties.getPrefix())) {
            registryKey = cacheProperties.getPrefix();
        } else if (StringUtils.isNotBlank(lockProperties.getPrefix())) {
            registryKey = lockProperties.getPrefix();
        } else {
            registryKey = "";
        }
        return new RedisLockRegistry(redisConnectionFactory, registryKey, lockProperties.getExpire());
    }

}