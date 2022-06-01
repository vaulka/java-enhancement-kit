package com.pongsky.kit.global.response.handler.processor.fail;

import com.pongsky.kit.common.global.response.processor.fail.BaseFailProcessor;
import com.pongsky.kit.common.response.annotation.ResponseResult;
import com.pongsky.kit.common.utils.SpringUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.util.ClassUtils;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;

/**
 * 数据类型转换异常处理器
 *
 * @author pengsenhao
 */
public class TypeMismatchExceptionFailProcessor implements BaseFailProcessor<TypeMismatchException> {

    @Override
    public Integer code() {
        return 110;
    }

    @Override
    public boolean isHitProcessor(Throwable exception) {
        return exception.getClass() == TypeMismatchException.class;
    }

    @Override
    public Object exec(TypeMismatchException exception) {
        String message = MessageFormat.format("无法将 \"{0}\" 值从 \"{1}\" 类型 转换为 \"{2}\" 类型",
                exception.getValue(), ClassUtils.getDescriptiveType(exception.getValue()),
                ClassUtils.getDescriptiveType(exception.getRequiredType()));
        HttpServletRequest request = SpringUtils.getHttpServletRequest();
        if (request == null) {
            return message;
        }
        boolean isGlobalResult = request.getAttribute(ResponseResult.class.getName()) != null;
        return isGlobalResult ? this.buildResult(message, exception, request) : message;
    }

}
