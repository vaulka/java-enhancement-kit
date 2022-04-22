package com.pongsky.kit.global.response.handler.processor.success;

import org.springframework.context.ApplicationContext;

import javax.servlet.http.HttpServletRequest;

/**
 * 【成功】全局响应环绕处理器
 *
 * @author pengsenhao
 */
public interface BaseSuccessAroundProcessor {

    /**
     * 执行 {@link BaseSuccessProcessor#exec(Object, HttpServletRequest, ApplicationContext)} 前的前置操作
     * 如果返回为 null，则执行 {@link BaseSuccessProcessor#exec(Object, HttpServletRequest, ApplicationContext)} 并返回该方法的结果
     * 如果不为 null，则不执行 {@link BaseSuccessProcessor#exec(Object, HttpServletRequest, ApplicationContext)}
     * <p>
     * 应用场景：
     * 譬如对接某第三方系统需要返回特定的响应数据格式，如果成功，也要返回特定的响应数据格式
     *
     * @param body               body
     * @param request            request
     * @param applicationContext 应用上下文
     * @return 返回的响应结果
     */
    default Object execBefore(Object body, HttpServletRequest request, ApplicationContext applicationContext) {
        return null;
    }

    /**
     * 执行 {@link BaseSuccessProcessor#exec(Object, HttpServletRequest, ApplicationContext)} 后的后置操作
     * <p>
     * 应用场景：
     * 成功后，进行邮箱告警等...
     *
     * @param body               body
     * @param request            request
     * @param applicationContext 应用上下文
     */
    default void execAfter(Object body, HttpServletRequest request, ApplicationContext applicationContext) {
    }

}
