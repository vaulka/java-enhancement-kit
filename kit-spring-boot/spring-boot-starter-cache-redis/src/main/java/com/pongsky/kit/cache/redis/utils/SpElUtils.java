package com.pongsky.kit.cache.redis.utils;

import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;

/**
 * SpEL 工具类
 *
 * @author pengsenhao
 */
public class SpElUtils {

    /**
     * 解析 SpEL 参数
     *
     * @param key    key
     * @param method 方法
     * @param args   参数列表
     * @return 解析后的 key
     */
    public static String parse(String key, Method method, Object[] args) {
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
