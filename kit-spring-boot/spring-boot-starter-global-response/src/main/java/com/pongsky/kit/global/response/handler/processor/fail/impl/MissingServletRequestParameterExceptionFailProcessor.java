package com.pongsky.kit.global.response.handler.processor.fail.impl;

import com.pongsky.kit.common.response.annotation.ResponseResult;
import com.pongsky.kit.global.response.handler.processor.fail.BaseFailProcessor;
import com.pongsky.kit.web.core.utils.SpringUtils;
import org.springframework.web.bind.MissingServletRequestParameterException;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;

/**
 * param 数据校验异常处理器
 *
 * @author pengsenhao
 */
public class MissingServletRequestParameterExceptionFailProcessor implements BaseFailProcessor<MissingServletRequestParameterException> {

    @Override
    public Integer code() {
        return 108;
    }

    @Override
    public boolean isHitProcessor(Throwable exception) {
        return exception.getClass() == MissingServletRequestParameterException.class;
    }

    @Override
    public Object exec(MissingServletRequestParameterException exception) {
        String message = MessageFormat.format("参数校验失败，一共有 1 处错误，详情如下： {0} 不能为 null", exception.getParameterName());
        HttpServletRequest request = SpringUtils.getHttpServletRequest();
        if (request == null) {
            return message;
        }
        boolean isGlobalResult = request.getAttribute(ResponseResult.class.getName()) != null;
        return isGlobalResult ? this.buildResult(message, exception, request) : message;
    }

}
