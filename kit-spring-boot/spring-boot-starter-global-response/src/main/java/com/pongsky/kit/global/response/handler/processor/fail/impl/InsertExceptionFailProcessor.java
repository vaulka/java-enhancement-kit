package com.pongsky.kit.global.response.handler.processor.fail.impl;

import com.pongsky.kit.common.exception.InsertException;
import com.pongsky.kit.global.response.handler.processor.fail.BaseFailProcessor;

/**
 * 保存异常处理器
 *
 * @author pengsenhao
 */
public class InsertExceptionFailProcessor implements BaseFailProcessor<InsertException> {

    @Override
    public Integer code() {
        return 503;
    }

    @Override
    public boolean isHitProcessor(Throwable exception) {
        return exception.getClass() == InsertException.class;
    }

}
