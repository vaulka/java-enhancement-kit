package com.pongsky.kit.global.response.handler.processor.success;

/**
 * 【成功】全局响应环绕处理器
 *
 * @author pengsenhao
 */
public interface BaseSuccessAroundProcessor {

    /**
     * 执行 {@link BaseSuccessProcessor#exec(Object)} 前的前置操作
     * 如果返回为 null，则执行 {@link BaseSuccessProcessor#exec(Object)} 并返回该方法的结果
     * 如果不为 null，则不执行 {@link BaseSuccessProcessor#exec(Object)}
     * <p>
     * 应用场景：
     * 譬如对接某第三方系统需要返回特定的响应数据格式，如果成功，也要返回特定的响应数据格式
     *
     * @param body body
     * @return 返回的响应结果
     */
    default Object execBefore(Object body) {
        return null;
    }

    /**
     * 执行 {@link BaseSuccessProcessor#exec(Object)} 后的后置操作
     * <p>
     * 应用场景：
     * 成功后，进行邮箱告警等...
     *
     * @param body body
     */
    default void execAfter(Object body) {
    }

}
