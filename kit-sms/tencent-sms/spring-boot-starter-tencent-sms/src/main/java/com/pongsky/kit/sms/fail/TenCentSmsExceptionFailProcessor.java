package com.pongsky.kit.sms.fail;

import com.pongsky.kit.common.global.response.processor.fail.BaseFailProcessor;
import com.pongsky.kit.sms.exception.TenCentSmsException;

/**
 * 腾讯云短信异常处理器
 *
 * @author pengsenhao
 */
public class TenCentSmsExceptionFailProcessor implements BaseFailProcessor<TenCentSmsException> {

    @Override
    public Integer code() {
        return 512;
    }

    @Override
    public boolean isHitProcessor(Throwable exception) {
        return exception.getClass() == TenCentSmsException.class;
    }

}
