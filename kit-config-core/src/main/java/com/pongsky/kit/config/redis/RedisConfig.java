package com.pongsky.kit.config.redis;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import com.pongsky.kit.config.SystemConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * redis 配置
 *
 * @author pengsenhao
 */
@EnableCaching
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore(RedisAutoConfiguration.class)
public class RedisConfig {

    private final SystemConfig systemConfig;

    /**
     * 缓存时长
     */
    @Value("${spring.redis.cache.time:}")
    private Long time = 30L;

    public Long getTime() {
        return Optional.ofNullable(time).orElse(30L);
    }

    /**
     * 缓存时长单位
     */
    @Value("${spring.redis.cache.time-unit:}")
    private TimeUnit timeUnit = TimeUnit.DAYS;

    public TimeUnit getTimeUnit() {
        return Optional.ofNullable(timeUnit).orElse(TimeUnit.DAYS);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        // 创建 RedisTemplate 对象
        RedisTemplate<String, Object> template = new RedisTemplate<>();

        // 【重要】设置开启事务支持
        template.setEnableTransactionSupport(true);

        // 设置 RedisConnection 工厂。😈 它就是实现多种 Java Redis 客户端接入的秘密工厂。感兴趣的胖友，可以自己去撸下。
        template.setConnectionFactory(factory);

        // 使用 String 序列化方式，序列化 KEY 。
        template.setKeySerializer(new KeyStringRedisSerializer(systemConfig));

        // 使用 String 序列化方式，序列化 VALUE 。
        // 使用 JSON 序列化方式（库是 Jackson ），会导致 emoji 乱码，并且字符串外面会扩一层字符串
        template.setValueSerializer(new GenericFastJsonRedisSerializer());

        // 使用 String 序列化方式，序列化 KEY 。
        template.setHashKeySerializer(new KeyStringRedisSerializer(systemConfig));

        // 使用 JSON 序列化方式（库是 fastJson ）
        // 使用 JSON 序列化方式（库是 Jackson ），会导致 emoji 乱码
        template.setHashValueSerializer(new GenericFastJsonRedisSerializer());
        return template;
    }

    @Bean
    public RedisCacheConfiguration redisCacheConfiguration() {
        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new KeyStringRedisSerializer(systemConfig)))
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new GenericFastJsonRedisSerializer()));
        // 设置缓存过期时间
        switch (getTimeUnit()) {
            case HOURS:
                return configuration.entryTtl(Duration.ofHours(getTime()));
            case DAYS:
            default:
                return configuration.entryTtl(Duration.ofDays(getTime()));
        }
    }

}