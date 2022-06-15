package com.pongsky.kit.storage.processor.fail;

import com.aliyun.oss.ClientException;
import com.pongsky.kit.common.global.response.processor.fail.BaseFailProcessor;
import com.pongsky.kit.common.response.annotation.ResponseResult;
import com.pongsky.kit.common.utils.SpringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Client 异常处理器
 *
 * @author pengsenhao
 */
public class AliYunOssClientExceptionFailProcessor implements BaseFailProcessor<ClientException> {

    @Override
    public Integer code() {
        return 508;
    }

    @Override
    public boolean isHitProcessor(Throwable exception) {
        return exception.getClass() == ClientException.class;
    }

    @Override
    public Object exec(ClientException exception) {
        String message = exception.getErrorMessage();
        HttpServletRequest request = SpringUtils.getHttpServletRequest();
        if (request == null) {
            return message;
        }
        boolean isGlobalResult = request.getAttribute(ResponseResult.class.getName()) != null;
        return isGlobalResult ? this.buildResult(message, exception, request) : message;
    }

}
