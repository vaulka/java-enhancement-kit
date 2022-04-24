package com.pongsky.kit.global.response.handler.processor.fail.impl;

import com.pongsky.kit.global.response.handler.processor.fail.BaseFailProcessor;

/**
 * 空指针异常处理器
 *
 * @author pengsenhao
 */
public class NullPointerExceptionFailProcessor implements BaseFailProcessor<NullPointerException> {

    @Override
    public Integer code() {
        return 506;
    }

    @Override
    public boolean isSprintStackTrace() {
        return true;
    }

    @Override
    public boolean isHitProcessor(Throwable exception) {
        return exception.getClass() == NullPointerException.class;
    }

}
