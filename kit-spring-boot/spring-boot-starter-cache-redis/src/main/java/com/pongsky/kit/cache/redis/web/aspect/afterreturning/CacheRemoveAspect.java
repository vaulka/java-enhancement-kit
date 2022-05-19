package com.pongsky.kit.cache.redis.web.aspect.afterreturning;

import com.pongsky.kit.cache.redis.annotation.CacheRemove;
import com.pongsky.kit.cache.redis.annotation.RemoveCaching;
import com.pongsky.kit.cache.redis.utils.SpElUtils;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.redis.core.RedisTemplate;

import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Redis 模糊删除 Key
 *
 * @author pengsenhao
 */
@Aspect
@RequiredArgsConstructor
public class CacheRemoveAspect {

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 冒号
     */
    private static final String COLON = "::";

    /**
     * 井号
     */
    private static final String HASHTAG = "#";

    @AfterReturning("(@within(com.pongsky.kit.cache.redis.annotation.RemoveCaching)" +
            "|| @annotation(com.pongsky.kit.cache.redis.annotation.RemoveCaching)) " +
            "|| (@within(com.pongsky.kit.cache.redis.annotation.CacheRemove)" +
            "|| @annotation(com.pongsky.kit.cache.redis.annotation.CacheRemove)) ")
    public void exec(JoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        List<CacheRemove> cacheRemoves = new ArrayList<>();
        // 先在方法上寻找该注解
        RemoveCaching removeCaching = Optional.ofNullable(AnnotationUtils.findAnnotation(method, RemoveCaching.class))
                // 方法上没有的话，则再类上寻找该注解
                .orElse(AnnotationUtils.findAnnotation(signature.getDeclaringType(), RemoveCaching.class));
        if (removeCaching != null) {
            cacheRemoves.addAll(Arrays.stream(removeCaching.remove()).collect(Collectors.toList()));
        }
        // 先在方法上寻找该注解
        CacheRemove cacheRemove = Optional.ofNullable(AnnotationUtils.findAnnotation(method, CacheRemove.class))
                // 方法上没有的话，则再类上寻找该注解
                .orElse(AnnotationUtils.findAnnotation(signature.getDeclaringType(), CacheRemove.class));
        if (cacheRemove != null) {
            cacheRemoves.add(cacheRemove);
        }
        for (CacheRemove cache : cacheRemoves) {
            String key = cache.key();
            key = key.contains(HASHTAG) ? SpElUtils.parse(key, method, point.getArgs()) : key;
            this.removeKey(MessageFormat.format("{0}{1}{2}", cache.cacheNames(), COLON, key));
        }
    }

    /**
     * 模糊删除 key
     *
     * @param key key
     */
    private void removeKey(String key) {
        Set<String> keys = redisTemplate.keys(key);
        if (keys == null || keys.size() == 0) {
            return;
        }
        redisTemplate.delete(keys);
    }

}