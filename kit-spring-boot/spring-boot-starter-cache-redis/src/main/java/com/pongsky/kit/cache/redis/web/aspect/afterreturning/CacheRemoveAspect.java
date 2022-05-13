package com.pongsky.kit.cache.redis.web.aspect.afterreturning;

import com.pongsky.kit.cache.redis.annotation.CacheRemove;
import com.pongsky.kit.cache.redis.annotation.RemoveCaching;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

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

    @AfterReturning("@annotation(com.pongsky.kit.cache.redis.annotation.RemoveCaching) " +
            "|| @annotation(com.pongsky.kit.cache.redis.annotation.CacheRemove) ")
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
            key = key.contains(HASHTAG) ? this.parseKey(key, method, point.getArgs()) : key;
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

    /**
     * 解析 SpEL 参数
     *
     * @param key    key
     * @param method 方法
     * @param args   参数列表
     * @return 解析后的 key
     */
    private String parseKey(String key, Method method, Object[] args) {
        LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();
        String[] paraNameArr = discoverer.getParameterNames(method);
        StandardEvaluationContext context = new StandardEvaluationContext();
        if (paraNameArr != null) {
            for (int i = 0; i < paraNameArr.length; i++) {
                context.setVariable(paraNameArr[i], args[i]);
            }
        }
        ExpressionParser parser = new SpelExpressionParser();
        return parser.parseExpression(key).getValue(context, String.class);
    }

}