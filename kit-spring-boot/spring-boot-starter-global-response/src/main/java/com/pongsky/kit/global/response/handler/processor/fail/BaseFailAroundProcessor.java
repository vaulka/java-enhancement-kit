package com.pongsky.kit.global.response.handler.processor.fail;

import com.pongsky.kit.global.response.handler.processor.success.BaseSuccessAroundProcessor;

/**
 * 【失败】全局响应环绕处理器
 *
 * @author pengsenhao
 */
public interface BaseFailAroundProcessor {

    /**
     * 判断是否命中处理器
     *
     * @param exception 异常
     * @return 判断是否命中处理器
     */
    boolean isHitProcessor(Throwable exception);

    /**
     * 处理器选择顺序
     * <p>
     * 从小到大排序，只取符合 {@link BaseSuccessAroundProcessor#isHitProcessor(java.lang.Object)} 条件的第一个处理器
     *
     * @return 处理器选择顺序
     */
    default Integer order() {
        return Integer.MAX_VALUE;
    }

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
