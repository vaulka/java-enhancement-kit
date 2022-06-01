package com.pongsky.kit.storage.processor.fail;

import com.pongsky.kit.common.global.response.processor.fail.BaseFailProcessor;
import com.pongsky.kit.storage.exception.MinIoException;

/**
 * MinIO 异常处理器
 *
 * @author pengsenhao
 */
public class MinIoExceptionFailProcessor implements BaseFailProcessor<MinIoException> {

    @Override
    public Integer code() {
        return 509;
    }

    @Override
    public boolean isHitProcessor(Throwable exception) {
        return exception.getClass() == MinIoException.class;
    }

}
