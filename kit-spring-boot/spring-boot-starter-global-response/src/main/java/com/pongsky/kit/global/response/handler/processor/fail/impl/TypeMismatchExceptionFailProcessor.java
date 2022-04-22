package com.pongsky.kit.global.response.handler.processor.fail.impl;

import com.pongsky.kit.global.response.handler.processor.fail.BaseFailProcessor;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ClassUtils;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;

/**
 * 数据类型转换异常处理器
 *
 * @author pengsenhao
 */
public class TypeMismatchExceptionFailProcessor implements BaseFailProcessor {

    @Override
    public Integer code() {
        return 110;
    }

    @Override
    public boolean isHitProcessor(Throwable exception, HttpServletRequest request, ApplicationContext applicationContext) {
        return exception.getClass() == TypeMismatchException.class;
    }

    @Override
    public Object exec(Throwable exception, HttpServletRequest request, ApplicationContext applicationContext) {
        TypeMismatchException ex = (TypeMismatchException) exception;
        String message = MessageFormat.format("无法将 \"{0}\" 值从 \"{1}\" 类型 转换为 \"{2}\" 类型",
                ex.getValue(), ClassUtils.getDescriptiveType(ex.getValue()), ClassUtils.getDescriptiveType(ex.getRequiredType()));
        return this.buildResult(message, exception, request);
    }

}
