package com.pongsky.kit.global.response.handler.processor.fail.impl;

import com.pongsky.kit.common.exception.ExistException;
import com.pongsky.kit.global.response.handler.processor.fail.BaseFailProcessor;

/**
 * 存在异常处理器
 *
 * @author pengsenhao
 */
public class ExistExceptionFailProcessor implements BaseFailProcessor<ExistException> {

    @Override
    public Integer code() {
        return 201;
    }

    @Override
    public boolean isHitProcessor(Throwable exception) {
        return exception.getClass() == ExistException.class;
    }

}
