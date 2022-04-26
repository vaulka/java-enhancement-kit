package com.pongsky.kit.global.response.handler.processor.success.impl;

import com.pongsky.kit.common.response.annotation.ResponseResult;
import com.pongsky.kit.global.response.handler.processor.success.BaseSuccessProcessor;
import com.pongsky.kit.web.core.utils.SpringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 默认【成功】全局响应处理器
 *
 * @author pengsenhao
 */
public class DefaultSuccessProcessor implements BaseSuccessProcessor {

    @Override
    public boolean isHitProcessor() {
        HttpServletRequest request = SpringUtils.getHttpServletRequest();
        if (request == null) {
            return false;
        }
        return request.getAttribute(ResponseResult.class.getName()) != null;
    }

}
