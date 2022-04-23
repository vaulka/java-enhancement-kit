package com.pongsky.kit.global.response.handler.processor.fail;

import com.pongsky.kit.common.response.GlobalResult;
import com.pongsky.kit.ip.utils.IpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * 【失败】全局响应处理器
 *
 * @author pengsenhao
 */
public interface BaseFailProcessor<T extends Throwable> {

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
     * 返回的响应结果
     *
     * @param exception          异常
     * @param request            request
     * @param applicationContext 应用上下文
     * @return 返回的响应结果
     */
    Object exec(T exception, HttpServletRequest request, ApplicationContext applicationContext);

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
    default void log(T exception, HttpServletRequest request, ApplicationContext applicationContext) {
        LOG.error("");
        LOG.error("========================= Started EXCEPTION =========================");
        LOG.error("EXCEPTION message: [{}]", Optional.ofNullable(exception.getLocalizedMessage()).orElse(exception.getMessage()));
        if (isSprintStackTrace()) {
            exception.printStackTrace();
        }
        LOG.error("========================== Ended EXCEPTION ==========================");
    }

    /**
     * 执行 {@link BaseFailProcessor#exec(Throwable, HttpServletRequest, ApplicationContext)} 后的后置操作
     * <p>
     * 应用场景：
     * 失败后，进行邮箱告警等...
     *
     * @param exception          异常
     * @param request            request
     * @param applicationContext 应用上下文
     */
    default void execAfter(T exception, HttpServletRequest request, ApplicationContext applicationContext) {
    }

    /**
     * 构建全局响应数据
     *
     * @param message   message
     * @param exception 异常
     * @param request   request
     * @return 构建全局响应数据
     */
    default GlobalResult<Void> buildResult(String message, T exception, HttpServletRequest request) {
        return new GlobalResult<Void>()
                .setCode(code())
                .setMessage(message)
                .setException(exception.getClass().getName())
                .setIp(IpUtils.getIp(request))
                .setPath(request.getRequestURI())
                .setTimestamp(System.currentTimeMillis());
    }

}
