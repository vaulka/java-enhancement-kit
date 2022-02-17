package com.pongsky.springcloud.web.aspect.afterreturning;

import com.pongsky.springcloud.utils.model.annotation.CacheRemove;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * Redis 模糊删除 Key
 *
 * @author pengsenhao
 */
@Aspect
@Component
@Order(value = 1)
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

    @AfterReturning("@annotation(com.pongsky.springcloud.utils.model.annotation.CacheRemove)")
    public void remove(JoinPoint point) {
        Method method = ((MethodSignature) point.getSignature()).getMethod();
        CacheRemove cacheRemove = method.getAnnotation(CacheRemove.class);
        String cacheNames = cacheRemove.cacheNames();
        if (StringUtils.isNotBlank(cacheNames)) {
            String key = cacheRemove.key();
            if (key.contains(HASHTAG)) {
                key = parseKey(key, method, point.getArgs());
            }
            removeKey(cacheNames + COLON + key);
        }
        String[] keys = cacheRemove.keys();
        if (keys.length > 0) {
            for (String key : keys) {
                removeKey(key.contains(HASHTAG) ? parseKey(key, method, point.getArgs()) : key);
            }
        }
    }

    /**
     * 模糊删除key
     *
     * @param key key
     * @author pengsenhao
     */
    private void removeKey(String key) {
        Set<String> deleteKeys = redisTemplate.keys(key);
        if (deleteKeys != null && deleteKeys.size() > 0) {
            redisTemplate.delete(deleteKeys);
        }
    }

    /**
     * 解析 SPEL 参数
     *
     * @param key    key
     * @param method 方法
     * @param args   参数列表
     * @return 解析后的 key
     * @author pengsenhao
     */
    private String parseKey(String key, Method method, Object[] args) {
        LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
        String[] paraNameArr = u.getParameterNames(method);
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();
        if (paraNameArr != null) {
            for (int i = 0; i < paraNameArr.length; i++) {
                context.setVariable(paraNameArr[i], args[i]);
            }
        }
        return parser.parseExpression(key).getValue(context, String.class);
    }

}