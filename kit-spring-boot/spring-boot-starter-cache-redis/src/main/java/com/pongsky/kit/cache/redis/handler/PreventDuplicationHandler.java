package com.pongsky.kit.cache.redis.handler;

import javax.servlet.http.HttpServletRequest;

/**
 * 防重处理器
 *
 * @author pengsenhao
 */
public interface PreventDuplicationHandler {

    /**
     * 是否放行
     *
     * @param request request
     * @return 是否放行
     */
    boolean exec(HttpServletRequest request);

}
