package com.pongsky.kit.global.response.handler.processor.fail.impl;

import com.pongsky.kit.global.response.handler.processor.fail.BaseFailProcessor;

/**
 * 运行时异常处理器
 *
 * @author pengsenhao
 */
public class RuntimeExceptionFailProcessor implements BaseFailProcessor<RuntimeException> {

    @Override
    public Integer code() {
        return 501;
    }

    @Override
    public boolean isSprintStackTrace() {
        return true;
    }

    @Override
    public boolean isHitProcessor(Throwable exception) {
        return exception.getClass() == RuntimeException.class;
    }

}
