package com.pongsky.kit.cache.redis.web.aspect.around;

import com.pongsky.kit.cache.redis.annotation.DistributedLock;
import com.pongsky.kit.cache.redis.utils.SpElUtils;
import com.pongsky.kit.common.exception.FrequencyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.integration.redis.util.RedisLockRegistry;

import java.lang.reflect.Method;
import java.util.Optional;
import java.util.concurrent.locks.Lock;

/**
 * 分布式锁 注解实现
 *
 * @author pengsenhao
 */
@Slf4j
@Aspect
@RequiredArgsConstructor
public class DistributedLockAspect {

    private final RedisLockRegistry redisLockRegistry;

    /**
     * 井号
     */
    private static final String HASHTAG = "#";

    @Around("@within(com.pongsky.kit.cache.redis.annotation.DistributedLock)" +
            "|| @annotation(com.pongsky.kit.cache.redis.annotation.DistributedLock) ")
    public Object exec(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        // 先在方法上寻找该注解
        DistributedLock distributedLock = Optional.ofNullable(AnnotationUtils.findAnnotation(method, DistributedLock.class))
                // 方法上没有的话，则再类上寻找该注解
                .orElse(AnnotationUtils.findAnnotation(signature.getDeclaringType(), DistributedLock.class));
        if (distributedLock == null) {
            return point.proceed();
        }
        String key = distributedLock.value();
        key = key.contains(HASHTAG) ? SpElUtils.parse(key, method, point.getArgs()) : key;
        Lock lock = redisLockRegistry.obtain(key);
        boolean isHaveLock;
        switch (distributedLock.getLockMethod()) {
            case lock:
                lock.lock();
                isHaveLock = true;
                break;
            case tryLock:
            default: {
                isHaveLock = lock.tryLock();
                break;
            }
            case tryLockTime: {
                isHaveLock = lock.tryLock(distributedLock.time(), distributedLock.unit());
                break;
            }
        }
        if (!isHaveLock) {
            throw new FrequencyException("当前请求资源已被其他请求占用，请稍后重试");
        }
        try {
            return point.proceed();
        } finally {
            lock.unlock();
        }
    }

}
