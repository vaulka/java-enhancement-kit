# spring-boot-starter-cache-redis 模块说明

> Redis 缓存 Spring Boot Starter 模块
>
> 该模块依赖
> * [spring-boot-starter-web-core](../spring-boot-starter-web-core/README.md) 模块

## 功能说明

* 无侵入式 Starter。
* 定义统一的前缀。
* 统一序列化、反序列化器。
* 定义是否启用事务。
* 设置 @Cacheable 缓存过期时间。 
* 模糊删除 key。
* 防重检测。
* 令牌桶限流。
* 分布式锁。

## 约定

1. 在需要清理缓存的方法上加 `CacheRemove` 或 `RemoveCaching` 注解。

## 配置 Redis 缓存参数

在 `yml` 配置 Redis 缓存信息，参数如下：

|参数|是否可空|描述|默认值|
|---|---|---|---|
|spring.cache.prefix|true|Redis 缓存前缀 <br> 增加统一的前缀，适用于多项目用同一个 Redis 库时，通过项目前缀进行区分||
|spring.cache.enable-transaction-support|true|是否开启事务支持 <br> 需引入 spring-boot-starter-jdbc，配合 JDBC 事务时，才能生效|false|
|spring.cache.time|true|{@link org.springframework.cache.annotation.Cacheable} 缓存时长|30|
|spring.cache.time-unit|true|{@link org.springframework.cache.annotation.Cacheable} 缓存时长单位 <br> 仅支持 DAYS、HOURS|DAYS|
|spring.cache.prevent-duplication.enabled|true|防重检测是否启用|true|
|spring.cache.rate-limit.enabled|true|令牌桶限流是否启用|true|
|spring.cache.rate-limit.bucket-rate|true|令牌每秒恢复个数|500|
|spring.cache.rate-limit.bucket-max|true|令牌桶大小|30000|
|spring.cache.distributed-lock.prefix|true|Redis 分布式锁缓存前缀|distributed-lock|
|spring.cache.distributed-lock.expire|true|锁过期时间，单位毫秒|60000|


示例如下：

```yml

spring:
  cache:
    prefix: gateway
    enable-transaction-support: true
    time: 7
    time-unit: days
    prevent-duplication:
      enabled: true
    rate-limit:
      enabled: true
      bucket-rate: 500
      bucket-max: 30000
    distributed-lock:
      prefix: lock
      expire: 5000

```

## 使用

### 模糊删除 key

1. 在需要清理缓存的方法上加 `CacheRemove` 或 `RemoveCaching` 注解。

```java

@Service
@RequiredArgsConstructor
public class TestService {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String DEFAULT_KEY = "pongsky";

    @RemoveCaching(remove = {
            @CacheRemove(cacheNames = "test", key = "#type"),
            @CacheRemove(cacheNames = "test", key = "4"),
            @CacheRemove(cacheNames = "test", key = "5")
    })
    @CacheRemove(cacheNames = "test", key = "*")
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer type) {
    }

    @Transactional(rollbackFor = Exception.class)
    public void post(JSONObject object) {
        redisTemplate.opsForValue().set(DEFAULT_KEY, object);
    }

    @Cacheable(cacheNames = "test", key = "#type")
    @Transactional(rollbackFor = Exception.class, readOnly = true)
    public Object get(Integer type) {
        return redisTemplate.opsForValue().get(DEFAULT_KEY);
    }

}

```

### 防重检测

1. 配置好 Redis 缓存相关参数信息。
2. 如需放行接口，请实现 `PreventDuplicationHandler` 类并注册 bean。

> 详情请查阅 `com.pongsky.kit.cache.redis.web.aspect.before.PreventDuplicationAspect` 代码实现。

### 令牌桶限流

1. 配置好 Redis 缓存相关参数信息。
2. 如需放行接口、自定义限流 key 生成、自定义 QPS，请实现 `RateLimitHandler` 类并注册 bean。

> 详情请查阅 `com.pongsky.kit.cache.redis.web.aspect.before.RateLimitAspect` 代码实现。

### 分布式锁

1. 在需要分布式锁限制的方法上加 `DistributedLock` 注解。
2. 在需要分布式锁限制的代码块使用 `RedisLockRegistry` 进行获取锁。

```java

@Service
@RequiredArgsConstructor
public class TestService {

    private final TestRepository testRepository;
    private final RedisLockRegistry redisLockRegistry;

    private static final String DEFAULT_KEY = "pongsky";

    @DistributedLock(value = "#projectId")
    @Transactional(rollbackFor = Exception.class)
    public void delete(String projectId) {
        Lock lock = redisLockRegistry.obtain(projectId);
        lock.lock();
        try {
            // exec
        } finally {
            lock.unlock();
        }
    }

}

```
