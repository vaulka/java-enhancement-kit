package com.pongsky.kit.global.response.handler.processor.fail;

/**
 * 【失败】全局响应环绕处理器
 *
 * @author pengsenhao
 */
public interface BaseFailAroundProcessor {

    /**
     * 执行 {@link {@link BaseFailProcessor#exec(Throwable)} 前的前置操作
     * 如果返回为 null，则执行 {@link BaseFailProcessor#exec(Throwable)} 并返回该方法的结果
     * 如果不为 null，则不执行 {@link BaseFailProcessor#exec(Throwable)}
     * <p>
     * 应用场景：
     * 譬如对接某第三方系统需要返回特定的响应数据格式，如果失败，也要返回特定的响应数据格式
     *
     * @param exception 异常
     * @return 返回的响应结果
     */
    default Object execBefore(Throwable exception) {
        return null;
    }

    /**
     * 执行 {@link BaseFailProcessor#exec(Throwable)} 后的后置操作
     * <p>
     * 应用场景：
     * 失败后，进行邮箱告警等...
     *
     * @param exception 异常
     */
    default void execAfter(Throwable exception) {
    }

}
