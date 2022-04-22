package com.pongsky.kit.global.response.handler.processor.success;

import com.pongsky.kit.common.response.GlobalResult;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;

/**
 * 【成功】全局响应处理器
 *
 * @author pengsenhao
 */
public interface BaseSuccessProcessor {

    /**
     * 返回的浏览器状态码
     *
     * @return 返回的浏览器状态码
     */
    default HttpStatus httpStatus() {
        return HttpStatus.OK;
    }

    /**
     * 返回的 code
     *
     * @return 返回的 code
     */
    default Integer code() {
        return 0;
    }

    /**
     * 判断是否命中处理器
     *
     * @param request            request
     * @param applicationContext 应用上下文
     * @return 判断是否命中处理器
     */
    boolean isHitProcessor(HttpServletRequest request, ApplicationContext applicationContext);

    /**
     * 执行 {@link BaseSuccessProcessor#exec(Object, HttpServletRequest, ApplicationContext)} 前的前置操作
     * 如果返回为 null，则执行 {@link BaseSuccessProcessor#exec(Object, HttpServletRequest, ApplicationContext)} 并返回该方法的结果
     * 如果不为 null，则不执行 {@link BaseSuccessProcessor#exec(Object, HttpServletRequest, ApplicationContext)}
     * <p>
     * 应用场景：
     * 譬如对接某第三方系统需要返回特定的响应数据格式，如果成功后，也要返回特定的响应数据格式
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
     * 返回的响应结果
     *
     * @param body               body
     * @param request            request
     * @param applicationContext 应用上下文
     * @return 返回的响应结果
     */
    Object exec(Object body, HttpServletRequest request, ApplicationContext applicationContext);

    /**
     * 执行 {@link BaseSuccessProcessor#exec(Object, HttpServletRequest, ApplicationContext)} 后的后置操作
     * <p>
     * 应用场景：
     * 执行完业务后，进行邮箱告警等...
     *
     * @param body               body
     * @param request            request
     * @param applicationContext 应用上下文
     */
    default void execAfter(Object body, HttpServletRequest request, ApplicationContext applicationContext) {
    }

    /**
     * 构建【成功】的全局响应数据
     *
     * @param <T> T
     * @param obj obj
     * @return 构建【成功】的全局响应数据
     */
    default <T> GlobalResult<T> buildResult(T obj) {
        return new GlobalResult<T>()
                .setCode(code())
                .setData(obj);
    }

}
