package com.pongsky.kit.global.response.handler.processor.fail;

import com.pongsky.kit.common.exception.DeleteException;
import com.pongsky.kit.common.global.response.processor.fail.BaseFailProcessor;

/**
 * 删除异常处理器
 *
 * @author pengsenhao
 */
public class DeleteExceptionFailProcessor implements BaseFailProcessor<DeleteException> {

    @Override
    public Integer code() {
        return 505;
    }

    @Override
    public boolean isHitProcessor(Throwable exception) {
        return exception.getClass() == DeleteException.class;
    }

}
