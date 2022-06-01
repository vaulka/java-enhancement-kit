package com.pongsky.kit.global.response.handler.processor.fail;

import com.pongsky.kit.common.exception.RemoteCallException;
import com.pongsky.kit.common.global.response.processor.fail.BaseFailProcessor;

/**
 * 远程调用异常处理器
 *
 * @author pengsenhao
 */
public class RemoteCallExceptionFailProcessor implements BaseFailProcessor<RemoteCallException> {

    @Override
    public Integer code() {
        return 501;
    }

    @Override
    public boolean isHitProcessor(Throwable exception) {
        return exception.getClass() == RemoteCallException.class;
    }

    @Override
    public Object exec(RemoteCallException exception) {
        return exception.getResult();
    }

}
