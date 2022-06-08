package com.pongsky.kit.storage.processor.fail;

import com.aliyun.oss.OSSException;
import com.pongsky.kit.common.global.response.processor.fail.BaseFailProcessor;
import com.pongsky.kit.common.response.annotation.ResponseResult;
import com.pongsky.kit.common.utils.SpringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * OSS 异常处理器
 *
 * @author pengsenhao
 */
public class AliyunOssExceptionFailProcessor implements BaseFailProcessor<OSSException> {

    @Override
    public Integer code() {
        return 507;
    }

    @Override
    public boolean isHitProcessor(Throwable exception) {
        return exception.getClass() == OSSException.class;
    }

    @Override
    public Object exec(OSSException exception) {
        String message = exception.getErrorMessage();
        HttpServletRequest request = SpringUtils.getHttpServletRequest();
        if (request == null) {
            return message;
        }
        boolean isGlobalResult = request.getAttribute(ResponseResult.class.getName()) != null;
        return isGlobalResult ? this.buildResult(message, exception, request) : message;
    }

}
