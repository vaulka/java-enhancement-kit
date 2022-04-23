package com.pongsky.kit.global.response.handler.processor.fail.impl;

import com.pongsky.kit.common.exception.DoesNotExistException;
import com.pongsky.kit.global.response.handler.processor.fail.BaseFailProcessor;

/**
 * 不存在异常处理器
 *
 * @author pengsenhao
 */
public class DoesNotExistExceptionFailProcessor implements BaseFailProcessor<DoesNotExistException> {

    @Override
    public Integer code() {
        return 202;
    }

    @Override
    public boolean isHitProcessor(Throwable exception) {
        return exception.getClass() == DoesNotExistException.class;
    }

}
