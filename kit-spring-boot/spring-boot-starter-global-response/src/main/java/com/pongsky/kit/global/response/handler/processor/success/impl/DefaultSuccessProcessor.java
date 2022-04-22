package com.pongsky.kit.global.response.handler.processor.success.impl;

import com.pongsky.kit.common.response.annotation.ResponseResult;
import com.pongsky.kit.global.response.handler.processor.success.BaseSuccessProcessor;
import org.springframework.context.ApplicationContext;

import javax.servlet.http.HttpServletRequest;

/**
 * 默认【成功】全局响应处理器
 *
 * @author pengsenhao
 */
public class DefaultSuccessProcessor implements BaseSuccessProcessor {

    @Override
    public boolean isHitProcessor(HttpServletRequest request, ApplicationContext applicationContext) {
        return request.getAttribute(ResponseResult.class.getName()) != null;
    }

    @Override
    public Object exec(Object body, HttpServletRequest request, ApplicationContext applicationContext) {
        return this.buildResult(body);
    }

}
