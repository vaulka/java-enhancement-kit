package com.pongsky.kit.global.response.handler.processor.fail.impl;

import com.pongsky.kit.global.response.handler.processor.fail.BaseFailProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.web.multipart.MultipartException;

import javax.servlet.http.HttpServletRequest;

/**
 * 空文件上传异常处理器
 *
 * @author pengsenhao
 */
public class MultipartExceptionFailProcessor implements BaseFailProcessor {

    @Override
    public Integer code() {
        return 102;
    }

    @Override
    public boolean isHitProcessor(Throwable exception, HttpServletRequest request, ApplicationContext applicationContext) {
        return exception.getClass() == MultipartException.class;
    }

    @Override
    public Object exec(Throwable exception, HttpServletRequest request, ApplicationContext applicationContext) {
        return this.buildResult("请选择文件进行上传", exception, request);
    }

}
