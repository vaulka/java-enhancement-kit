package com.pongsky.kit.sms.fail;

import com.pongsky.kit.common.global.response.processor.fail.BaseFailProcessor;
import com.pongsky.kit.sms.exception.AliYunSmsBizException;

/**
 * 短信业务异常处理器
 *
 * @author pengsenhao
 */
public class AliYunSmsBizExceptionFailProcessor implements BaseFailProcessor<AliYunSmsBizException> {

    @Override
    public Integer code() {
        return 510;
    }

    @Override
    public boolean isHitProcessor(Throwable exception) {
        return exception.getClass() == AliYunSmsBizException.class;
    }

}
