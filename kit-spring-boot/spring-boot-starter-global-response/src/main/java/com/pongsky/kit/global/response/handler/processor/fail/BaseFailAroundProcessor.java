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
     * <p>
     * 如果命中，则不会执行 {@link BaseFailProcessor} 处理器相关业务处理
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
     * 返回的响应结果
     * <p>
     * 应用场景：
     * 譬如对接某第三方系统，失败后，也要返回特定的响应结果格式等...
     *
     * @param exception 异常
     * @return 返回的响应结果
     */
    Object exec(Throwable exception);

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
