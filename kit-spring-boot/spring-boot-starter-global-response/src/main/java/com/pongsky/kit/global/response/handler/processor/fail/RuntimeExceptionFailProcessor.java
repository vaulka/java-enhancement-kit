package com.pongsky.kit.global.response.handler.processor.fail;

import com.pongsky.kit.common.global.response.processor.fail.BaseFailProcessor;

/**
 * 运行时异常处理器
 *
 * @author pengsenhao
 */
public class RuntimeExceptionFailProcessor implements BaseFailProcessor<RuntimeException> {

    @Override
    public Integer code() {
        return 500;
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
