package com.pongsky.kit.global.response.handler.processor.fail;

import com.pongsky.kit.common.global.response.processor.fail.BaseFailProcessor;
import com.pongsky.kit.common.response.annotation.ResponseResult;
import com.pongsky.kit.common.utils.SpringUtils;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.servlet.http.HttpServletRequest;

/**
 * 文件上传大小异常处理器
 *
 * @author pengsenhao
 */
public class MaxUploadSizeExceededExceptionFailProcessor implements BaseFailProcessor<MaxUploadSizeExceededException> {

    @Override
    public Integer code() {
        return 203;
    }

    @Override
    public boolean isHitProcessor(Throwable exception) {
        return exception.getClass() == MaxUploadSizeExceededException.class;
    }

    /**
     * 默认错误信息
     */
    private static final String MESSAGE = "上传的文件体积超过限制，请缩小文件后重试";

    @Override
    public Object exec(MaxUploadSizeExceededException exception) {
        HttpServletRequest request = SpringUtils.getHttpServletRequest();
        if (request == null) {
            return MESSAGE;
        }
        boolean isGlobalResult = request.getAttribute(ResponseResult.class.getName()) != null;
        return isGlobalResult ? this.buildResult(MESSAGE, exception, request) : MESSAGE;
    }

}
