package com.pongsky.kit.cache.redis.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Redis 分布式锁配置
 *
 * @author pengsenhao
 **/
@Getter
@Setter
@ConfigurationProperties(prefix = "spring.cache.distributed-lock")
public class DistributedLockProperties {

    /**
     * Redis 分布式锁缓存前缀
     */
    private String prefix = "distributed-lock";

    /**
     * 锁过期时间，单位毫秒
     */
    private long expire = 60000L;

}
