package com.pongsky.kit.global.response.handler.processor.fail.impl;

import com.pongsky.kit.common.exception.UpdateException;
import com.pongsky.kit.global.response.handler.processor.fail.BaseFailProcessor;

/**
 * 修改异常处理器
 *
 * @author pengsenhao
 */
public class UpdateExceptionFailProcessor implements BaseFailProcessor<UpdateException> {

    @Override
    public Integer code() {
        return 504;
    }

    @Override
    public boolean isHitProcessor(Throwable exception) {
        return exception.getClass() == UpdateException.class;
    }

}
