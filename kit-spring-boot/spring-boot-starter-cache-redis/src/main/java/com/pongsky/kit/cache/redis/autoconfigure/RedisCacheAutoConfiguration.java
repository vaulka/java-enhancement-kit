package com.pongsky.kit.cache.redis.autoconfigure;

import com.pongsky.kit.cache.redis.config.RedisCacheConfig;
import com.pongsky.kit.cache.redis.properties.RedisCacheProperties;
import com.pongsky.kit.cache.redis.web.aspect.afterreturning.CacheRemoveAspect;
import com.pongsky.kit.cache.redis.web.aspect.before.PreventDuplicationAspect;
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
@EnableConfigurationProperties({RedisCacheProperties.class})
@Import({RedisCacheConfig.class, CacheRemoveAspect.class, PreventDuplicationAspect.class})
public class RedisCacheAutoConfiguration {


}
