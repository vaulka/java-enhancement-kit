package com.pongsky.kit.global.response.handler.processor.fail.impl;

import com.pongsky.kit.common.exception.HttpException;
import com.pongsky.kit.global.response.handler.processor.fail.BaseFailProcessor;

/**
 * HTTP 请求异常处理器
 *
 * @author pengsenhao
 */
public class HttpExceptionFailProcessor implements BaseFailProcessor<HttpException> {

    @Override
    public Integer code() {
        return 204;
    }

    @Override
    public boolean isHitProcessor(Throwable exception) {
        return exception.getClass() == HttpException.class;
    }

}
