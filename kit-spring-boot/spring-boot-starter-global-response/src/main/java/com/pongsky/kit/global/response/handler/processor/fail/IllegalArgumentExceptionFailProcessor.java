package com.pongsky.kit.global.response.handler.processor.fail;

import com.pongsky.kit.common.global.response.processor.fail.BaseFailProcessor;

/**
 * 非法参数异常处理器
 *
 * @author pengsenhao
 */
public class IllegalArgumentExceptionFailProcessor implements BaseFailProcessor<IllegalArgumentException> {

    @Override
    public Integer code() {
        return 103;
    }

    @Override
    public boolean isHitProcessor(Throwable exception) {
        return exception.getClass() == IllegalArgumentException.class;
    }

}
