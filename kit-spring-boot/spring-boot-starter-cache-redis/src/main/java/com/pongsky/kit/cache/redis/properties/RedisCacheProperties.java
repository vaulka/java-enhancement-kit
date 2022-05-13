package com.pongsky.kit.cache.redis.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.concurrent.TimeUnit;

/**
 * Redis 缓存配置
 *
 * @author pengsenhao
 **/
@Getter
@Setter
@ConfigurationProperties(prefix = "spring.cache")
public class RedisCacheProperties {

    /**
     * Redis 缓存前缀
     * <p>
     * 增加统一的前缀，适用于多项目用同一个 Redis 库时，通过项目前缀进行区分
     */
    private String prefix = "";

    /**
     * 是否开启事务支持
     * <p>
     * 需引入 spring-boot-starter-jdbc，配合 JDBC 事务时，才能生效
     */
    private Boolean enableTransactionSupport = false;

    /**
     * {@link org.springframework.cache.annotation.Cacheable} 缓存时长
     */
    private Long time = 30L;

    /**
     * {@link org.springframework.cache.annotation.Cacheable} 缓存时长单位
     * <p>
     * 仅支持 DAYS、HOURS
     */
    private TimeUnit timeUnit = TimeUnit.DAYS;

}
