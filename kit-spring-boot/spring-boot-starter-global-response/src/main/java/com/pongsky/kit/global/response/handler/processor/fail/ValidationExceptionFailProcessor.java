package com.pongsky.kit.global.response.handler.processor.fail;

import com.pongsky.kit.common.exception.ValidationException;
import com.pongsky.kit.common.global.response.processor.fail.BaseFailProcessor;

import javax.validation.ConstraintViolationException;

/**
 * 校验异常处理器
 *
 * @author pengsenhao
 */
public class ValidationExceptionFailProcessor implements BaseFailProcessor<ValidationException> {

    @Override
    public Integer code() {
        return 101;
    }

    @Override
    public boolean isHitProcessor(Throwable exception) {
        return exception.getClass() == ValidationException.class
                || exception.getClass() == ConstraintViolationException.class;
    }

}
