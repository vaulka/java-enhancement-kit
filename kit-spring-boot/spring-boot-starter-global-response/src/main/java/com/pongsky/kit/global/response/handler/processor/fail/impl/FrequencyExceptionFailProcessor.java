package com.pongsky.kit.global.response.handler.processor.fail.impl;

import com.pongsky.kit.common.exception.FrequencyException;
import com.pongsky.kit.global.response.handler.processor.fail.BaseFailProcessor;

/**
 * 频率异常处理器
 *
 * @author pengsenhao
 */
public class FrequencyExceptionFailProcessor implements BaseFailProcessor<FrequencyException> {

    @Override
    public Integer code() {
        return 301;
    }

    @Override
    public boolean isHitProcessor(Throwable exception) {
        return exception.getClass() == FrequencyException.class;
    }

}
