package com.pongsky.kit.cache.redis.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pongsky.kit.cache.redis.properties.RedisCacheProperties;
import com.pongsky.kit.cache.redis.serializer.StringRedisSerializer;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * Redis 配置
 *
 * @author pengsenhao
 */
@EnableCaching
@RequiredArgsConstructor
public class RedisCacheConfig {

    private final ObjectMapper objectMapper;
    private final RedisCacheProperties properties;

    /**
     * 配置 RedisTemplate
     * <p>
     * 设置序列化、反序列化、事务
     *
     * @param factory factory
     * @return RedisTemplate
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        // 设置是否开启事务（仅支持单机，不支持集群）
        template.setEnableTransactionSupport(properties.getEnableTransactionSupport());
        // 设置 RedisConnection 工厂
        template.setConnectionFactory(factory);
        // 使用自定义前缀进行序列化 key
        // 使用默认的 Jackson 进行序列化 value
        template.setDefaultSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));
        template.setKeySerializer(new StringRedisSerializer(properties.getPrefix()));
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));
        template.setHashKeySerializer(new StringRedisSerializer(properties.getPrefix()));
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));
        return template;
    }

    /**
     * 将 Redis 事务与 JDBC 事务共同管理
     *
     * @param dataSource dataSource
     * @return 事务管理器
     */
    @ConditionalOnProperty(value = "spring.cache.enable-transaction-support", havingValue = "true")
    @ConditionalOnClass(DataSourceTransactionManager.class)
    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

}