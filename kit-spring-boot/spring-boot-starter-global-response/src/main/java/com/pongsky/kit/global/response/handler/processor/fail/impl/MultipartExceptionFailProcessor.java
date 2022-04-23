package com.pongsky.kit.global.response.handler.processor.fail.impl;

import com.pongsky.kit.common.response.annotation.ResponseResult;
import com.pongsky.kit.global.response.handler.processor.fail.BaseFailProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.web.multipart.MultipartException;

import javax.servlet.http.HttpServletRequest;

/**
 * 空文件上传异常处理器
 *
 * @author pengsenhao
 */
public class MultipartExceptionFailProcessor implements BaseFailProcessor<MultipartException> {

    @Override
    public Integer code() {
        return 102;
    }

    @Override
    public boolean isHitProcessor(Throwable exception, HttpServletRequest request, ApplicationContext applicationContext) {
        return exception.getClass() == MultipartException.class;
    }

    /**
     * 默认错误信息
     */
    private static final String MESSAGE = "请选择文件进行上传";

    @Override
    public Object exec(MultipartException exception, HttpServletRequest request, ApplicationContext applicationContext) {
        boolean isGlobalResult = request.getAttribute(ResponseResult.class.getName()) != null;
        return isGlobalResult ? this.buildResult(MESSAGE, exception, request) : MESSAGE;
    }

}
