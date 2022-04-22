package com.pongsky.kit.global.response.handler.processor.fail.impl;

import com.pongsky.kit.global.response.handler.processor.fail.BaseFailProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.servlet.http.HttpServletRequest;

/**
 * 文件上传大小异常处理器
 *
 * @author pengsenhao
 */
public class MaxUploadSizeExceededExceptionFailProcessor implements BaseFailProcessor {

    @Override
    public Integer code() {
        return 203;
    }

    @Override
    public boolean isHitProcessor(Throwable exception, HttpServletRequest request, ApplicationContext applicationContext) {
        return exception.getClass() == MaxUploadSizeExceededException.class;
    }

    @Override
    public Object exec(Throwable exception, HttpServletRequest request, ApplicationContext applicationContext) {
        return this.buildResult("上传的文件体积超过限制，请缩小文件后重试", exception, request);
    }

}
