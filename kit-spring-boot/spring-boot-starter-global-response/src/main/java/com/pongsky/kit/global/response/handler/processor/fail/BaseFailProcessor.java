package com.pongsky.kit.global.response.handler.processor.fail;

import com.pongsky.kit.common.response.GlobalResult;
import com.pongsky.kit.ip.utils.IpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;

/**
 * 【失败】全局响应处理器
 *
 * @author pengsenhao
 */
public interface BaseFailProcessor {

    Logger LOG = LoggerFactory.getLogger(BaseFailProcessor.class);

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
    Integer code();

    /**
     * 判断是否命中处理器
     *
     * @param exception          异常
     * @param request            request
     * @param applicationContext 应用上下文
     * @return 判断是否命中处理器
     */
    boolean isHitProcessor(Throwable exception, HttpServletRequest request, ApplicationContext applicationContext);

    /**
     * 执行 {@link BaseFailProcessor#exec(Throwable, HttpServletRequest, ApplicationContext)} 前的前置操作
     * 如果返回为 null，则执行 {@link BaseFailProcessor#exec(Throwable, HttpServletRequest, ApplicationContext)} 并返回该方法的结果
     * 如果不为 null，则不执行 {@link BaseFailProcessor#exec(Throwable, HttpServletRequest, ApplicationContext)}
     * <p>
     * 应用场景：
     * 譬如对接某第三方系统需要返回特定的响应数据格式，如果异常后，也要返回特定的响应数据格式
     *
     * @param exception          异常
     * @param request            request
     * @param applicationContext 应用上下文
     * @return 返回的响应结果
     */
    default Object execBefore(Throwable exception, HttpServletRequest request, ApplicationContext applicationContext) {
        return null;
    }

    /**
     * 返回的响应结果
     *
     * @param exception          异常
     * @param request            request
     * @param applicationContext 应用上下文
     * @return 返回的响应结果
     */
    Object exec(Throwable exception, HttpServletRequest request, ApplicationContext applicationContext);

    /**
     * 是否打印异常堆栈信息
     *
     * @return 是否打印异常堆栈信息
     */
    default boolean isSprintStackTrace() {
        return false;
    }

    /**
     * 日志打印
     *
     * @param exception          异常
     * @param request            request
     * @param applicationContext 应用上下文
     */
    default void log(Throwable exception, HttpServletRequest request, ApplicationContext applicationContext) {
        LOG.error("");
        LOG.error("========================= Started EXCEPTION =========================");
        LOG.error("EXCEPTION message: [{}]", exception.getLocalizedMessage() != null ? exception.getLocalizedMessage() : exception.getMessage());
        if (isSprintStackTrace()) {
            exception.printStackTrace();
        }
        LOG.error("========================== Ended EXCEPTION ==========================");
    }

    /**
     * 执行 {@link BaseFailProcessor#exec(Throwable, HttpServletRequest, ApplicationContext)} 后的后置操作
     * <p>
     * 应用场景：
     * 出现异常后，进行邮箱告警等...
     *
     * @param exception          异常
     * @param request            request
     * @param applicationContext 应用上下文
     */
    default void execAfter(Throwable exception, HttpServletRequest request, ApplicationContext applicationContext) {
    }

    /**
     * 构建全局响应数据
     *
     * @param message   message
     * @param exception 异常
     * @param request   request
     * @return 构建全局响应数据
     */
    default GlobalResult<Void> buildResult(String message, Throwable exception, HttpServletRequest request) {
        return new GlobalResult<Void>()
                .setCode(code())
                .setMessage(message)
                .setException(exception.getClass().getName())
                .setIp(IpUtils.getIp(request))
                .setPath(request.getRequestURI())
                .setTimestamp(System.currentTimeMillis());
    }

}
