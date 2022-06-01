package com.pongsky.kit.cache.redis.autoconfigure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pongsky.kit.cache.redis.manager.RedisAutoCacheManager;
import com.pongsky.kit.cache.redis.properties.RedisCacheProperties;
import com.pongsky.kit.cache.redis.serializer.StringRedisSerializer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizers;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Redis 缓存自动装配
 *
 * @author pengsenhao
 **/
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter(RedisAutoConfiguration.class)
@EnableConfigurationProperties({CacheProperties.class})
public class RedisCacheManagerAutoConfiguration {

    private final ObjectMapper objectMapper;
    private final RedisCacheProperties properties;

    /**
     * 配置 RedisCacheConfiguration
     *
     * @return RedisCacheConfiguration
     */
    @Bean
    public RedisCacheConfiguration redisCacheConfiguration() {
        // 使用自定义前缀进行序列化 key
        // 使用默认的 Jackson 进行序列化 value
        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new StringRedisSerializer(properties.getPrefix())))
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new GenericJackson2JsonRedisSerializer(objectMapper)));
        // 设置缓存过期时间
        switch (properties.getTimeUnit()) {
            case SECONDS:
                return configuration.entryTtl(Duration.ofSeconds(properties.getTime()));
            case MINUTES:
                return configuration.entryTtl(Duration.ofMinutes(properties.getTime()));
            case HOURS:
                return configuration.entryTtl(Duration.ofHours(properties.getTime()));
            case DAYS:
            default:
                return configuration.entryTtl(Duration.ofDays(properties.getTime()));
        }
    }

    @Bean
    @ConditionalOnMissingBean(CacheManagerCustomizers.class)
    public CacheManagerCustomizers cacheManagerCustomizers(ObjectProvider<List<CacheManagerCustomizer<?>>> customizers) {
        return new CacheManagerCustomizers(customizers.getIfAvailable());
    }

    @Bean
    @Primary
    public RedisCacheManager redisCacheManager(CacheProperties cacheProperties,
                                               RedisConnectionFactory connectionFactory,
                                               CacheManagerCustomizers customizerInvoker,
                                               RedisCacheConfiguration redisCacheConfiguration) {
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory);
        List<String> cacheNames = cacheProperties.getCacheNames();
        Map<String, RedisCacheConfiguration> initialCaches = new LinkedHashMap<>();
        if (!cacheNames.isEmpty()) {
            Map<String, RedisCacheConfiguration> cacheConfigMap = new LinkedHashMap<>(cacheNames.size());
            cacheNames.forEach(it -> cacheConfigMap.put(it, redisCacheConfiguration));
            initialCaches.putAll(cacheConfigMap);
        }
        RedisAutoCacheManager cacheManager = new RedisAutoCacheManager(redisCacheWriter, redisCacheConfiguration,
                initialCaches, true);
        return customizerInvoker.customize(cacheManager);
    }

}
