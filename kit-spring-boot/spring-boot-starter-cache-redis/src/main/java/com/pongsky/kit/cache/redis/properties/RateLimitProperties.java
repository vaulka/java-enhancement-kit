package com.pongsky.kit.cache.redis.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 令牌桶限流配置
 *
 * @author pengsenhao
 **/
@Getter
@Setter
@ConfigurationProperties(prefix = "spring.cache.rate-limit")
public class RateLimitProperties {

    /**
     * 是否启用
     */
    private boolean enabled = true;

    /**
     * 令牌每秒恢复个数
     * <p>
     * 默认 QPS：500
     */
    private int bucketRate = 500;

    /**
     * 令牌桶大小
     */
    private int bucketMax = bucketRate * 60;

}
