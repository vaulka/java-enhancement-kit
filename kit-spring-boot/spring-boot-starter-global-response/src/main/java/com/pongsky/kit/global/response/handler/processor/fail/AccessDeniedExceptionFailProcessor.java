package com.pongsky.kit.global.response.handler.processor.fail;

import com.pongsky.kit.common.global.response.processor.fail.BaseFailProcessor;
import com.pongsky.kit.common.response.annotation.ResponseResult;
import com.pongsky.kit.common.utils.SpringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.security.access.AccessDeniedException;

import javax.servlet.http.HttpServletRequest;

/**
 * 访问权限异常处理器
 *
 * @author pengsenhao
 */
@ConditionalOnClass(AccessDeniedException.class)
public class AccessDeniedExceptionFailProcessor implements BaseFailProcessor<AccessDeniedException> {

    @Override
    public Integer code() {
        return 403;
    }

    @Override
    public boolean isHitProcessor(Throwable exception) {
        return exception.getClass() == AccessDeniedException.class;
    }

    /**
     * 默认错误信息
     */
    public static final String MESSAGE = "访问凭证已过期，请重新登录";

    @Override
    public Object exec(AccessDeniedException exception) {
        HttpServletRequest request = SpringUtils.getHttpServletRequest();
        if (request == null) {
            return MESSAGE;
        }
        boolean isGlobalResult = request.getAttribute(ResponseResult.class.getName()) != null;
        return isGlobalResult ? this.buildResult(MESSAGE, exception, request) : MESSAGE;
    }

}
