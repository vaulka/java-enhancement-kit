package com.pongsky.kit.web.interceptor;

import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;

/**
 * 将调用到接口的 Controller、Method 注解加入 HttpServletRequest.Attribute 属性中
 *
 * @author pengsenhao
 */
@Component
public class ControllerAttrInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            for (Annotation annotation : handlerMethod.getBeanType().getAnnotations()) {
                request.setAttribute(annotation.annotationType().getName(), annotation);
            }
            for (Annotation annotation : handlerMethod.getMethod().getAnnotations()) {
                request.setAttribute(annotation.annotationType().getName(), annotation);
            }
        }
        return true;
    }

    @Override
    public void postHandle(@NonNull HttpServletRequest request,
                           @NonNull HttpServletResponse response,
                           @NonNull Object handler,
                           ModelAndView modelAndView) {
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request,
                                @NonNull HttpServletResponse response,
                                @NonNull Object handler,
                                Exception ex) {
    }

}