package com.pongsky.repository.web.aspect.before;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * controller 注解加入 request.attribute
 *
 * @author pengsenhao
 * @create 2021-02-11
 */
@Aspect
@Component
@Order(value = 1)
public class ControllerAnnotationAspect {

    @Before("(@within(org.springframework.stereotype.Controller) " +
            "|| @within(org.springframework.web.bind.annotation.RestController)) " +
            "&& (@annotation(org.springframework.web.bind.annotation.RequestMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.GetMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.PutMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.PostMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.DeleteMapping)) ")
    public void exec(JoinPoint point) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return;
        }
        HttpServletRequest request = attributes.getRequest();
        MethodSignature sign = (MethodSignature) point.getSignature();
        Method method = sign.getMethod();
        for (Annotation annotation : method.getAnnotations()) {
            String simpleName = annotation.annotationType().getSimpleName();
            request.setAttribute(simpleName, simpleName);
        }
        for (Annotation annotation : point.getTarget().getClass().getAnnotations()) {
            String simpleName = annotation.annotationType().getSimpleName();
            request.setAttribute(simpleName, simpleName);
        }
    }

}
