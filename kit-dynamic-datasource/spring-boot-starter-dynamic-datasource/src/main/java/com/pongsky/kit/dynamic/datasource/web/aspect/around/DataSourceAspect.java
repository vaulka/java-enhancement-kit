package com.pongsky.kit.dynamic.datasource.web.aspect.around;

import com.pongsky.kit.dynamic.datasource.annotation.DataSource;
import com.pongsky.kit.dynamic.datasource.core.DynamicDataSourceContextHolder;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;

import java.util.Optional;

/**
 * 动态数据源切换处理
 * <p>
 * "@Order" 保证执行顺序优先级在最前面
 *
 * @author pengsenhao
 **/
@Aspect
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
public class DataSourceAspect {

    @Around("@within(com.pongsky.kit.dynamic.datasource.annotation.DataSource) " +
            "|| (@annotation(com.pongsky.kit.dynamic.datasource.annotation.DataSource)) ")
    public Object exec(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        // 先在方法上寻找该注解
        DataSource dataSource = Optional.ofNullable(AnnotationUtils.findAnnotation(signature.getMethod(), DataSource.class))
                // 方法上没有的话，则再类上寻找该注解
                .orElse(AnnotationUtils.findAnnotation(signature.getDeclaringType(), DataSource.class));
        if (dataSource != null && StringUtils.isNotBlank(dataSource.value())) {
            DynamicDataSourceContextHolder.set(dataSource.value());
        }
        try {
            return point.proceed();
        } finally {
            DynamicDataSourceContextHolder.clear();
        }
    }

}
