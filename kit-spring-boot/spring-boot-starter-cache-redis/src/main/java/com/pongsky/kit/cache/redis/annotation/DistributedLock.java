package com.pongsky.kit.cache.redis.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁
 *
 * @author pengsenhao
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface DistributedLock {

    /**
     * 用法同 {@link org.springframework.cache.annotation.Cacheable#key()}
     *
     * @return key
     */
    String value() default "";

    /**
     * 获取锁的方式
     *
     * @return 获取锁的方式
     */
    GetLockMethod getLockMethod() default GetLockMethod.tryLock;

    /**
     * {@link java.util.concurrent.locks.Lock#tryLock(long, java.util.concurrent.TimeUnit)} 其中的 time 参数
     *
     * @return time
     */
    long time() default 10;

    /**
     * {@link java.util.concurrent.locks.Lock#tryLock(long, java.util.concurrent.TimeUnit)} 其中的 unit 参数
     *
     * @return unit
     */
    TimeUnit unit() default TimeUnit.SECONDS;

    /**
     * 获取锁的方式
     */
    enum GetLockMethod {

        /**
         * {@link java.util.concurrent.locks.Lock#lock()}
         */
        lock,

        /**
         * {@link java.util.concurrent.locks.Lock#tryLock()}
         */
        tryLock,

        /**
         * {@link java.util.concurrent.locks.Lock#tryLock(long, java.util.concurrent.TimeUnit)}
         */
        tryLockTime;

    }
}