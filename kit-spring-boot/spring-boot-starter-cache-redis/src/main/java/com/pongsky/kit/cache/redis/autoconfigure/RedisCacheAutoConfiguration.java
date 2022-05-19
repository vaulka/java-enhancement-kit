package com.pongsky.kit.cache.redis.autoconfigure;

import com.pongsky.kit.cache.redis.config.DistributedLockConfig;
import com.pongsky.kit.cache.redis.config.RedisCacheConfig;
import com.pongsky.kit.cache.redis.properties.DistributedLockProperties;
import com.pongsky.kit.cache.redis.properties.PreventDuplicationProperties;
import com.pongsky.kit.cache.redis.properties.RateLimitProperties;
import com.pongsky.kit.cache.redis.properties.RedisCacheProperties;
import com.pongsky.kit.cache.redis.service.RateLimitService;
import com.pongsky.kit.cache.redis.web.aspect.afterreturning.CacheRemoveAspect;
import com.pongsky.kit.cache.redis.web.aspect.around.DistributedLockAspect;
import com.pongsky.kit.cache.redis.web.aspect.before.PreventDuplicationAspect;
import com.pongsky.kit.cache.redis.web.aspect.before.RateLimitAspect;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Redis 缓存自动装配
 *
 * @author pengsenhao
 **/
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({RedisCacheProperties.class, DistributedLockProperties.class,
        PreventDuplicationProperties.class, RateLimitProperties.class})
@Import({RedisCacheConfig.class, DistributedLockConfig.class,
        CacheRemoveAspect.class, PreventDuplicationAspect.class,
        RateLimitAspect.class, RateLimitService.class, DistributedLockAspect.class})
public class RedisCacheAutoConfiguration {


}
