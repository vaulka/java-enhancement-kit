package com.pongsky.springcloud.web.request;

import java.util.List;

/**
 * 白名单列表
 *
 * @author pengsenhao
 */
public interface Whitelist {

    /**
     * 放行 URL 列表
     */
    List<String> URLS = List.of("/actuator/health");

}
