package com.pongsky.kit.web.request;

import java.util.Arrays;
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
    List<String> URLS = Arrays.asList("/actuator/health");

}
