package com.pongsky.repository.web.request;

import java.util.List;

/**
 * @author pengsenhao
 * @description 大概描述所属模块和介绍
 * @date 2021-12-20 6:49 下午
 */
public interface Whitelist {

    /**
     * 放行 URL 列表
     */
    List<String> URLS = List.of("/actuator/health");

}
