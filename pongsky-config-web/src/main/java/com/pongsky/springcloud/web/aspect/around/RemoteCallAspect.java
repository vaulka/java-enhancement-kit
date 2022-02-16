package com.pongsky.springcloud.web.aspect.around;

import com.pongsky.springcloud.web.request.RequestUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.Method;

/**
 * feign 远程调用日志打印
 *
 * @author pengsenhao
 */
@Slf4j
@Aspect
@Component
@Order(value = 2)
@RequiredArgsConstructor
public class RemoteCallAspect {

    @SuppressWarnings({"unchecked"})
    @Around("@within(org.springframework.cloud.openfeign.FeignClient) " +
            "&& (@annotation(org.springframework.web.bind.annotation.RequestMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.GetMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.PutMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.PostMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.DeleteMapping)) ")
    public Object exec(ProceedingJoinPoint point) throws Throwable {
        Method method = ((MethodSignature) point.getSignature()).getMethod();
        long start = System.currentTimeMillis();
        Object result = point.proceed();
        log.info("Started remote call request");
        FeignClient feignClient = (FeignClient) point.getSignature().getDeclaringType().getAnnotation(FeignClient.class);
        RequestMethod requestMethod = RequestUtils.getMethod(method.getAnnotations());
        String requestUri = RequestUtils.getUri(method.getAnnotations());
        log.info("service [{}] uri [{}] method [{}]", feignClient.value(), requestUri, requestMethod);
        log.info("cost [{}] ms", System.currentTimeMillis() - start);
        log.info("Ended remote call request");
        return result;
    }

}
