package com.pongsky.kit.global.response.handler.processor.fail;

import com.pongsky.kit.common.global.response.processor.fail.BaseFailProcessor;
import com.pongsky.kit.common.response.annotation.ResponseResult;
import com.pongsky.kit.common.utils.SpringUtils;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;

/**
 * 接口不存在异常处理器
 *
 * @author pengsenhao
 */
public class NoHandlerFoundExceptionFailProcessor implements BaseFailProcessor<NoHandlerFoundException> {

    @Override
    public Integer code() {
        return 105;
    }

    @Override
    public boolean isHitProcessor(Throwable exception) {
        return exception.getClass() == NoHandlerFoundException.class;
    }

    @Override
    public Object exec(NoHandlerFoundException exception) {
        HttpServletRequest request = SpringUtils.getHttpServletRequest();
        if (request == null) {
            return exception.getLocalizedMessage();
        }
        String message = MessageFormat.format("{0} 接口不存在", request.getRequestURI());
        boolean isGlobalResult = request.getAttribute(ResponseResult.class.getName()) != null;
        return isGlobalResult ? this.buildResult(message, exception, request) : message;
    }

}
